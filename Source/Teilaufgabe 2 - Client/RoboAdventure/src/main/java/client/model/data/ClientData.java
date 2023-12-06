package client.model.data;

import client.exceptions.NullOrEmptyParameterException;

public class ClientData {
    private final String studentFirstName;
    private final String studentLastName;
    private final String studentUAccount;
    private String PlayerID;


    public ClientData(String studentFirstName, String studentLastName, String studentUAccount) {
        if (studentFirstName == null || studentLastName == null || studentUAccount == null || studentUAccount.isEmpty()) {
            throw new NullOrEmptyParameterException();
        }

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

    public void setPlayerID(String playerID) {
        PlayerID = playerID;
    }
}
