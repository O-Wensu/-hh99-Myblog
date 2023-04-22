package myblog.myblog.service;

import lombok.RequiredArgsConstructor;
import myblog.myblog.domain.Likes;
import myblog.myblog.domain.Member;
import myblog.myblog.domain.Post;
import myblog.myblog.exception.custom_exeption.PostException;
import myblog.myblog.repository.LikeRepository;
import myblog.myblog.repository.PostRepository;
import myblog.myblog.util.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;


    /**
     * 게시글 좋아요/좋아요 취소
     */
    public ResponseEntity updateLike(Long id, Member member) {
        Post post = validatePost(id);
        isAlreadyLike(member, post);

        //이미 좋아요를 했으면 취소
        if (isAlreadyLike(member, post)) {
            post.cancelLike();
            likeRepository.deleteByMemberIdAndPostId(member.getId(), id);
            return new ResponseEntity("cancel like success", HttpStatus.OK);
        }

        // 추가
        post.addLike();
        Likes like = new Likes(member, post);
        likeRepository.save(like);
        return new ResponseEntity("add like success", HttpStatus.OK);
    }

    //게시글 존재 여부 확인
    private Post validatePost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new PostException(ExceptionMessage.NO_SUCH_BOARD_EXCEPTION.getMessage())
        );
    }

    //좋아요 여부 확인
    private boolean isAlreadyLike(Member member, Post post) {
        Optional<Likes> like = likeRepository.findByMemberIdAndPostId(member.getId(), post.getId());
        if (like.isPresent()) {
            return true;
        }
        return false;
    }
}
