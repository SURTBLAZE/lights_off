package lightsoff.service;

public class GameInfoException extends RuntimeException {
    public GameInfoException(String message) {
        super(message);
    }

    public GameInfoException(String message, Throwable cause) {
        super(message, cause);
    }
}
