package network;

import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.*;
import messagesbase.messagesfromserver.*;
import model.data.ClientData;
import model.data.Field;
import model.data.Terrain;
import model.state.ClientState;
import model.state.FortState;
import model.state.GamePlayState;
import model.state.PlayerPositionState;
import move.EMoves;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.List;

public class NetworkCommunication {
    private static final Logger logger = LoggerFactory.getLogger(NetworkCommunication.class);

    private final String gameID;
    private ClientData clientData;
    private final WebClient baseWebClient;

    public NetworkCommunication(String serverBaseURL, String gameID, ClientData clientData) {
        this.gameID = gameID;
        this.clientData = clientData;

        baseWebClient = WebClient.builder().baseUrl(serverBaseURL + "/games")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) // the network protocol uses
                // XML
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();
    }

    public GamePlayState getGameState() {

        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
                .uri("/" + gameID + "/states/" + clientData.getPlayerID()).retrieve().bodyToMono(ResponseEnvelope.class);

        ResponseEnvelope<GameState> requestResult = webAccess.block();

        if (requestResult.getState() == ERequestState.Error) {
            System.err.println("Client error, errormessage: " + requestResult.getExceptionMessage());
        }

        GameState gameState = requestResult.getData().get();
        PlayerState playerState = gameState.getPlayers().stream()
                        .filter(player -> player.getUniquePlayerID().equals(clientData.getPlayerID()))
                        .findAny()
                        .orElse(null);

        GamePlayState gamePlayState = new GamePlayState();
        assert playerState != null;
        gamePlayState.addClientState(playerState.getState(), playerState.hasCollectedTreasure());

        FullMap fullMapNodes = gameState.getMap();

        fullMapNodes.stream().forEach(
                node ->
                        gamePlayState.addFieldToMap(
                                node.getX(),
                                node.getY(),
                                node.getTerrain(),
                                node.getPlayerPositionState(),
                                node.getTreasureState(),
                                node.getFortState()
                        )
                );

        return gamePlayState;
    }

    public void sendClientMap(List<Field> clientMap) {

        while (getGameState().getClientState() != ClientState.MustAct) {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        List<PlayerHalfMapNode> clientMapToSend = new ArrayList<>();

        for (Field field : clientMap) {
                boolean fortPresent = false;
                ETerrain terrain = null;

                if (field.getFortState() == FortState.MyFort) {
                    fortPresent = true;
                }

                terrain = switch (field.getTerrain()) {
                    case GRASS -> ETerrain.Grass;
                    case WATER -> ETerrain.Water;
                    case MOUNTAIN -> ETerrain.Mountain;
                };

                clientMapToSend.add(new PlayerHalfMapNode(field.getPositionX(), field.getPositionY(), fortPresent, terrain));

        }

        PlayerHalfMap playerHalfMap = new PlayerHalfMap(clientData.getPlayerID(), clientMapToSend);

        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameID + "/halfmaps")
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

        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameID + "/players")
                .body(BodyInserters.fromValue(playerReg)) // specify the data which is sent to the server
                .retrieve().bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

        ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();


        if (resultReg.getState() == ERequestState.Error) {
            // typically happens if you forgot to create a new game before the client
            // execution or forgot to adapt the run configuration so that it supplies
            // the id of the new game to the client
            // open http://swe1.wst.univie.ac.at:18235/games in your browser to create a new
            // game and obtain its game id
            System.err.println("Client error, errormessage: " + resultReg.getExceptionMessage());
        } else {
            UniquePlayerIdentifier uniqueID = resultReg.getData().get();
           // System.out.println("My Player ID: " + uniqueID.getUniquePlayerID());
            clientData.setPlayerID(uniqueID.getUniquePlayerID());
            logger.info("Client {} was created!", clientData.getStudentFirstName());
        }

    }

    public void sendMove(EMoves move) {

        while (getGameState().getClientState() != ClientState.MustAct) {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        EMove moveToSend = null;

        switch (move) {
            case Up -> moveToSend = EMove.Up;
            case Down -> moveToSend = EMove.Down;
            case Left -> moveToSend = EMove.Left;
            case Right -> moveToSend = EMove.Right;
        }

        PlayerMove playerMove = PlayerMove.of(clientData.getPlayerID(), moveToSend);


        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameID + "/moves")
                .body(BodyInserters.fromValue(playerMove)) // specify the data which is sent to the server
                .retrieve().bodyToMono(ResponseEnvelope.class); // specify the object returned by the server



        ResponseEnvelope result = webAccess.block();

        if (result.getState() == ERequestState.Error) {
            System.err.println("Move error, errormessage: " + result.getExceptionMessage());
        } else {
            logger.info("Move was send by Player={}", clientData.getStudentFirstName());
        }

    }
}
