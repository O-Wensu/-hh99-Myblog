package myblog.myblog.exception.custom_exeption.member;

import myblog.myblog.exception.custom_exeption.comment.CommentException;
import myblog.myblog.util.ExceptionMessage;

public class NoSuchMemberException extends MemberException {

    public NoSuchMemberException() {
        super(ExceptionMessage.NO_SUCH_MEMBER_EXCEPTION.getMessage());
    }
}
