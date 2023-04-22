package myblog.myblog.domain;

import jakarta.persistence.*;
import lombok.*;
import myblog.myblog.dto.post.PostRequestDTO;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @OrderBy("createdAt desc")
    private List<Comment> commentList = new ArrayList<>();

    @Column(nullable = false)
    @ColumnDefault("0")
    private int likeCount;

    //RequestDTO 를 Post로 변환
    public Post(PostRequestDTO requestDTO) {
        this.title = requestDTO.getTitle();
        this.content = requestDTO.getContent();
    }

    public void setMember(Member member) {
        this.member = member;
    }

    // ============연관 관계 편의 메서드============
    public void addComment(Comment comment) {
        commentList.add(comment);
        comment.setPost(this);
    }

    // ============ 비즈니스 메서드============
    public void update(PostRequestDTO reqDTO) {
        this.title = reqDTO.getTitle();
        this.content = reqDTO.getContent();
    }

    public void addLike() {
        likeCount += 1;
    }

    public void cancelLike() {
        if (likeCount - 1 < 0) return;
        likeCount -= 1;
    }
}
