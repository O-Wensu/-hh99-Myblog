package myblog.myblog.exception.custom_exeption.comment;

import myblog.myblog.util.ExceptionMessage;

public class NoSuchCommentException extends CommentException{
    public NoSuchCommentException() {
        super(ExceptionMessage.NO_SUCH_COMMENT_EXCEPTION.getMessage());
    }
}
