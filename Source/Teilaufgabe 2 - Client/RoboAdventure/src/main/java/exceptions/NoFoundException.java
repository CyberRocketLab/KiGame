package exceptions;

public class NoFoundException extends RuntimeException {
    public NoFoundException() {
        super("Could not find specific Element");
    }
}
