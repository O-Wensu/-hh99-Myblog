package myblog.myblog.dto.comment;

import lombok.*;
import myblog.myblog.domain.Comment;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private Long id;
    private String username;
    private String comment;
    private int likeCount;
    private Long parentComment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.username = comment.getMember().getUsername();
        this.likeCount = comment.getLikeCount();
        if (comment.getParent() != null)
            this.parentComment = comment.getParent().getId();
        this.comment = comment.getComment();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }

/*
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }*/
}
