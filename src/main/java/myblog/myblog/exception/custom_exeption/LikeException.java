package myblog.myblog.exception.custom_exeption;

public class LikeException extends RuntimeException{
    public LikeException() {
        super();
    }

    public LikeException(String message) {
        super(message);
    }

    public LikeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LikeException(Throwable cause) {
        super(cause);
    }
}
