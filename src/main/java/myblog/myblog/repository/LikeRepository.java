package myblog.myblog.repository;

import myblog.myblog.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByMemberIdAndPostId(Long memberId, Long postId);

    Optional<Likes> findByMemberIdAndPostIdAndCommentId(Long memberId, Long postId, Long commentId);

    void deleteByMemberIdAndPostId(Long memberId, Long postId);

    void deleteByMemberIdAndPostIdAndCommentId(Long memberId, Long postId, Long commentId);

    void deleteByPostId(Long postId);

    void deleteByCommentId(Long commentId);
}
