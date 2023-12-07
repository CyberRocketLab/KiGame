package client.network;

import client.exceptions.NullOrEmptyParameterException;
import client.model.state.ClientState;
import client.converter.IClientConverter;
import client.converter.IServerConverter;
import client.exceptions.NetworkException;
import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.*;
import messagesbase.messagesfromserver.*;
import client.model.data.ClientData;
import client.model.data.Field;
import client.model.data.GameID;
import client.model.state.GameState;
import client.move.EMoves;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URL;
import java.util.List;
import java.util.Optional;

public class NetworkCommunication {
    private static final Logger logger = LoggerFactory.getLogger(NetworkCommunication.class);
    private final GameID gameID;
    private final ClientData clientData;
    private final WebClient baseWebClient;
    private final IClientConverter clientConverter;
    private final IServerConverter serverConverter;

    public NetworkCommunication(URL serverBaseURL,
                                GameID gameID,
                                ClientData clientData,
                                IClientConverter clientConverter,
                                IServerConverter serverConverter) {
        if (serverBaseURL == null || gameID == null || clientData == null || clientConverter == null || serverConverter == null) {
            logger.error("Provided Parameter was null");
            throw new NullOrEmptyParameterException();
        }


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
        logger.info("Requesting GameState from Server");
        Mono<ResponseEnvelope> webAccess =
                baseWebClient.method(HttpMethod.GET)
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

        if (playerState.isEmpty()) {
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
        logger.info("Sending Client Map");
        if (clientMap == null || clientMap.isEmpty()) {
            logger.error("Provided List<Field> clientMap was null or empty");
            throw new NullOrEmptyParameterException();
        }

        List<PlayerHalfMapNode> clientMapToSend =
                serverConverter.convertToPlayerHalfMapNode(clientMap);

        PlayerHalfMap playerHalfMap =
                new PlayerHalfMap(clientData.getPlayerID(), clientMapToSend);

        Mono<ResponseEnvelope> webAccess =
                baseWebClient.method(HttpMethod.POST).uri("/" + gameID.id() + "/halfmaps")
                        .body(BodyInserters.fromValue(playerHalfMap))
                        .retrieve().bodyToMono(ResponseEnvelope.class);

        ResponseEnvelope result = webAccess.block();

        if (result.getState() == ERequestState.Error) {
            System.err.println("Client error, errormessage: " + result.getExceptionMessage());
        } else {
            System.out.println("ERequestState : " + result.getState());
            logger.info("Map was send by Player={}", clientData.getStudentFirstName());
        }
    }

    public void registerClient() {
        logger.info("Registering Client: {} ", clientData.getStudentFirstName());
        PlayerRegistration playerReg =
                new PlayerRegistration(
                        clientData.getStudentFirstName(),
                        clientData.getStudentLastName(),
                        clientData.getStudentUAccount());

        Mono<ResponseEnvelope> webAccess =
                baseWebClient.method(HttpMethod.POST).uri("/" + gameID.id() + "/players")
                        .body(BodyInserters.fromValue(playerReg))
                        .retrieve().bodyToMono(ResponseEnvelope.class);

        ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();

        if (resultReg.getState() == ERequestState.Error) {
            System.err.println("Client error, errormessage: " + resultReg.getExceptionMessage());
        } else {
            UniquePlayerIdentifier uniqueID = resultReg.getData().get();
            clientData.setPlayerID(uniqueID.getUniquePlayerID());
            logger.info("Client {} was created!", clientData.getStudentFirstName());
        }
    }

    public void sendMove(EMoves move) {
        logger.info("Starting sending Move to Server");
        if (move == null) {
            logger.error("Provided EMoves was null");
            throw new NullOrEmptyParameterException();
        }

        PlayerMove playerMove = PlayerMove.of(clientData.getPlayerID(), serverConverter.convertToEMove(move));

        Mono<ResponseEnvelope> webAccess =
                baseWebClient.method(HttpMethod.POST).uri("/" + gameID.id() + "/moves")
                        .body(BodyInserters.fromValue(playerMove))
                        .retrieve().bodyToMono(ResponseEnvelope.class);

        ResponseEnvelope result = webAccess.block();

        if (result.getState() == ERequestState.Error) {
            System.err.println("Move error, errormessage: " + result.getExceptionMessage());
        } else {
            logger.info("Move successfully sent by Player = {}", clientData.getStudentFirstName());
        }
    }

}
