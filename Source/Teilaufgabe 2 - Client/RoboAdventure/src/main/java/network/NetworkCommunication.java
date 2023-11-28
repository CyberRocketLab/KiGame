package network;

import converter.IClientConverter;
import converter.ClientConverter;
import converter.IServerConverter;
import converter.ServerConverter;
import exceptions.NetworkException;
import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.*;
import messagesbase.messagesfromserver.*;
import model.data.ClientData;
import model.data.Field;
import model.data.GameID;
import model.state.ClientState;
import model.state.FortState;
import model.state.GameState;
import move.EMoves;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NetworkCommunication {
    private static final Logger logger = LoggerFactory.getLogger(NetworkCommunication.class);
    private final GameID gameID;
    private final ClientData clientData;
    private final WebClient baseWebClient;
    private final IClientConverter clientConverter;
    private final IServerConverter serverConverter;

    public NetworkCommunication(URL serverBaseURL, GameID gameID, ClientData clientData, IClientConverter clientConverter, IServerConverter serverConverter) {
        this.gameID = gameID;
        this.clientData = clientData;

        baseWebClient = WebClient.builder()
                .baseUrl(serverBaseURL + "/games")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();

        this.clientConverter = clientConverter;
        this.serverConverter = serverConverter;
    }

    public GameState getGameState() {
        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
                .uri("/" + gameID.id() + "/states/" + clientData.getPlayerID())
                .retrieve()
                .bodyToMono(ResponseEnvelope.class);

        ResponseEnvelope<messagesbase.messagesfromserver.GameState> requestResult = webAccess.block();

        if (requestResult.getState() == ERequestState.Error) {
            System.err.println("Client error, errormessage: " + requestResult.getExceptionMessage());
        }

        messagesbase.messagesfromserver.GameState serverGameState = requestResult.getData().get();

        Optional<PlayerState> playerState = serverGameState.getPlayers().stream()
                .filter(player -> player.getUniquePlayerID().equals(clientData.getPlayerID()))
                .findFirst();

        if(playerState.isEmpty()) {
            throw new NetworkException("PlayerState is empty");
        }

        GameState clientGameState = new GameState();
        clientGameState.addClientState(
                clientConverter.getConvertedClientState(playerState.get().getState()),
                playerState.get().hasCollectedTreasure()
        );

        clientGameState.addMap(clientConverter.getConvertedMap(serverGameState.getMap()));

        return clientGameState;
    }

    public void sendClientMap(List<Field> clientMap) {

        while (getGameState().getClientState() != ClientState.MustAct) {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        IServerConverter serverConverter = new ServerConverter();

        List<PlayerHalfMapNode> clientMapToSend = serverConverter.convertToPlayerHalfMapNode(clientMap);

        PlayerHalfMap playerHalfMap = new PlayerHalfMap(clientData.getPlayerID(), clientMapToSend);

        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameID.id() + "/halfmaps")
                .body(BodyInserters.fromValue(playerHalfMap)) // specify the data which is sent to the server
                .retrieve().bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

        ResponseEnvelope result = webAccess.block();

        if (result.getState() == ERequestState.Error) {
            System.err.println("Client error, errormessage: " + result.getExceptionMessage());
        } else {
            System.out.println("ERequestState : " + result.getState());
            logger.info("Map was send by Player={}", clientData.getStudentFirstName());
        }


    }

    public void registerClient() {
        PlayerRegistration playerReg = new PlayerRegistration(clientData.getStudentFirstName(),
                                                                clientData.getStudentLastName(),
                                                                clientData.getStudentUAccount());

        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameID.id() + "/players")
                .body(BodyInserters.fromValue(playerReg)) // specify the data which is sent to the server
                .retrieve().bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

        ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();


        if (resultReg.getState() == ERequestState.Error) {
            System.err.println("Client error, errormessage: " + resultReg.getExceptionMessage());
        } else {
            UniquePlayerIdentifier uniqueID = resultReg.getData().get();
           // System.out.println("My Player ID: " + uniqueID.getUniquePlayerID());
            clientData.setPlayerID(uniqueID.getUniquePlayerID());
            logger.info("Client {} was created!", clientData.getStudentFirstName());
        }

    }

    public void sendMove(EMoves move) {
        ClientState state = getGameState().getClientState();

        if (state == ClientState.Lost || state == ClientState.Won) {
            logger.info("Game:{}",  state);
            return;
        }

        while (getGameState().getClientState() != ClientState.MustAct) {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        PlayerMove playerMove = PlayerMove.of(clientData.getPlayerID(), serverConverter.convertToEMove(move));


        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameID.id() + "/moves")
                .body(BodyInserters.fromValue(playerMove))
                .retrieve().bodyToMono(ResponseEnvelope.class);

        ResponseEnvelope result = webAccess.block();

        if (result.getState() == ERequestState.Error) {
            System.err.println("Move error, errormessage: " + result.getExceptionMessage());
        } else {
            logger.info("Move was send by Player={}", clientData.getStudentFirstName());
        }

    }

    private void waitForMyTurn() {
        while (getGameState().getClientState() != ClientState.MustAct) {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
