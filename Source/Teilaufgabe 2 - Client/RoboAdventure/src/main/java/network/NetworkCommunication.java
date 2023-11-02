package network;

import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.*;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.PlayerState;
import model.data.ClientData;
import model.data.Field;
import model.state.ClientState;
import model.state.FortState;
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

    public ClientState getGameState() {
        ClientState clientState;

        System.out.println("Currnent Player " + clientData.getStudentFirstName());

        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
                .uri("/" + gameID + "/states/" + clientData.getPlayerID()).retrieve().bodyToMono(ResponseEnvelope.class); // specify the
                                                                                                        // object
                                                                                                        // returned
                                                                                                        // by the
                                                                                                        // server
        ResponseEnvelope<GameState> requestResult = webAccess.block();

        // always check for errors, and if some are reported, at least print them to the
        // console (logging should always be preferred!)
        // so that you become aware of them during debugging! The provided server gives
        // you constructive error messages.
        if (requestResult.getState() == ERequestState.Error) {
            System.err.println("Client error, errormessage: " + requestResult.getExceptionMessage());
        }

        GameState gameState = requestResult.getData().get();

        PlayerState playerState = gameState.getPlayers().stream()
                        .filter(player -> player.getUniquePlayerID().equals(clientData.getPlayerID()))
                        .findAny()
                        .orElse(null);


        clientState = switch (playerState.getState()) {
            case Won -> ClientState.Won;
            case Lost -> ClientState.Lost;
            case MustAct -> ClientState.MustAct;
            case MustWait -> ClientState.MustWait;
        };

        return clientState;
    }

    public void sendClientMap(List<Field> clientMap) {

        while (getGameState() != ClientState.MustAct) {
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
            System.out.println("My Player ID: " + uniqueID.getUniquePlayerID());
            clientData.setPlayerID(uniqueID.getUniquePlayerID());
        }

    }
}
