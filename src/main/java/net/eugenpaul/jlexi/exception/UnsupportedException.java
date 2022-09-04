package net.eugenpaul.jlexi.exception;

public class UnsupportedException extends RuntimeException {
    public UnsupportedException() {
    }

    public UnsupportedException(String message) {
        super(message);
    }

    public UnsupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
