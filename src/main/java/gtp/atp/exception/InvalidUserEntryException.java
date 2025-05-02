package gtp.atp.exception;

public class InvalidUserEntryException extends RuntimeException {
    public InvalidUserEntryException(String message) {
        super(message);
    }
}