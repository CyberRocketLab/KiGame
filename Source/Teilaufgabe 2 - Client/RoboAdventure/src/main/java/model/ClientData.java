package model;

public class ClientData {
    private final String studentFirstName;
    private final String studentLastName;
    private final String studentUAccount;
    private String PlayerID;
    private String GameID;


    public ClientData(String studentFirstName, String studentLastName, String studentUAccount) {
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.studentUAccount = studentUAccount;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public String getStudentUAccount() {
        return studentUAccount;
    }

    public String getPlayerID() {
        return PlayerID;
    }

    public String getGameID() {
        return GameID;
    }
    public void setPlayerID(String playerID) {
        PlayerID = playerID;
    }

    public void setGameID(String gameID) {
        GameID = gameID;
    }
}
