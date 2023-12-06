package client.exceptions;

public class NoFoundException extends Exception {
    public NoFoundException() {
        super("Could not find specific Element");
    }
}
