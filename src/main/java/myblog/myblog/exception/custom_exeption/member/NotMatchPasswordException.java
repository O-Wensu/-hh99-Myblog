package myblog.myblog.exception.custom_exeption.member;

import myblog.myblog.exception.custom_exeption.comment.CommentException;
import myblog.myblog.util.ExceptionMessage;

public class NotMatchPasswordException extends MemberException {
    public NotMatchPasswordException() {super(ExceptionMessage.NOT_MATCHING_PASSWORD_EXCEPTION.getMessage());}
}
