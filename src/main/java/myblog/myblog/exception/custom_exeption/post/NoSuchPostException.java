package myblog.myblog.exception.custom_exeption.post;

import myblog.myblog.util.ExceptionMessage;

public class NoSuchPostException extends PostException {
    public NoSuchPostException() {
        super(ExceptionMessage.NO_SUCH_BOARD_EXCEPTION.getMessage());
    }
}
