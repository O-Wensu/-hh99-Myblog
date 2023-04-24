package myblog.myblog.exception.custom_exeption.post;

import myblog.myblog.util.ExceptionMessage;

public class NoAuthorizationException extends PostException{
    public NoAuthorizationException() {
        super(ExceptionMessage.NO_AUTHORIZATION_EXCEPTION.getMessage());
    }
}
