package myblog.myblog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import myblog.myblog.dto.comment.CommentRequestDto;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int likeCount;

    public Comment(CommentRequestDto commentRequestDTO) {
        this.comment = commentRequestDTO.getComment();
    }

    public void update(CommentRequestDto commentRequestDTO) {
        this.comment = commentRequestDTO.getComment();
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    // ============ 비즈니스 메서드============
    public void addLike() {
        likeCount += 1;
    }

    public void cancelLike() {
        if (likeCount - 1 < 0) return;
        likeCount -= 1;
    }
}
