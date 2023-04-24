package myblog.myblog.exception.custom_exeption.member;

import myblog.myblog.exception.custom_exeption.comment.CommentException;
import myblog.myblog.util.ExceptionMessage;

public class DuplicateMemberException extends MemberException {

    public DuplicateMemberException() {super(ExceptionMessage.DUPLICATE_ID_EXCEPTION.getMessage());}
}
