package network;

import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.*;
import model.ClientData;
import model.Field;
import model.FieldClient;
import model.FortState;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class NetworkCommunication {
    private final String serverBaseURL;
    private final String gameID;
    private final ClientData clientData;
    private WebClient baseWebClient;

    public NetworkCommunication(String serverBaseURL, String gameID, ClientData clientData) {
        this.serverBaseURL = serverBaseURL;
        this.gameID = gameID;
        this.clientData = clientData;

        baseWebClient = WebClient.builder().baseUrl(serverBaseURL + "/games")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) // the network protocol uses
                // XML
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();
    }

    public void getGameState() {

    }

    public void sendClientMap(List<Field> clientMap) {
        List<PlayerHalfMapNode> clientMapToSend = new ArrayList<>();


        for (Field field : clientMap) {
            if (field instanceof FieldClient) {
                boolean fortPresent = false;
                ETerrain terrain = null;

                if (((FieldClient) field).getFortState() == FortState.MyFort) {
                    fortPresent = true;
                }

                switch (field.getTerrain()) {
                    case GRASS:
                        terrain = ETerrain.Grass;
                        break;
                    case WATER:
                        terrain = ETerrain.Water;
                        break;
                    case MOUNTAIN:
                        terrain = ETerrain.Mountain;
                        break;
                }

                clientMapToSend.add(new PlayerHalfMapNode(field.getPositionX(), field.getPositionY(), fortPresent, terrain));
            }
        }

        PlayerHalfMap playerHalfMap = new PlayerHalfMap(clientData.getPlayerID(), clientMapToSend);

        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameID + "/halfmaps")
                .body(BodyInserters.fromValue(playerHalfMap)) // specify the data which is sent to the server
                .retrieve().bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

        ResponseEnvelope<ERequestState> result = webAccess.block();

        if (result.getState() == ERequestState.Error) {
            System.err.println("Client error, errormessage: " + result.getExceptionMessage());
        } else {
            ERequestState eRequestState = result.getData().get();
            System.out.println("ERequestState : " + eRequestState);
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
