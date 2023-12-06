package client.exceptions;

public class NullOrEmptyParameterException extends RuntimeException {
    public NullOrEmptyParameterException() {
        super("Parameter cannot be Null or Empty");
    }
}
