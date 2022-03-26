package net.eugenpaul.jlexi.exception;

public class NotInitializedException extends RuntimeException {
    public NotInitializedException() {
    }

    public NotInitializedException(String message) {
        super(message);
    }
}
