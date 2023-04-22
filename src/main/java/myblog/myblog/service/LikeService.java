package myblog.myblog.service;

import lombok.RequiredArgsConstructor;
import myblog.myblog.domain.*;
import myblog.myblog.dto.BasicResponseDto;
import myblog.myblog.exception.custom_exeption.CommentException;
import myblog.myblog.exception.custom_exeption.PostException;
import myblog.myblog.repository.CommentRepository;
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
    private final CommentRepository commentRepository;

    /**
     * 게시글 좋아요/좋아요 취소
     */
    public ResponseEntity updatePostLike(Long id, Member member) {
        Post post = validatePost(id);
        isPostLike(member, post);
        BasicResponseDto basicResponseDto;

        //이미 좋아요를 했으면 취소
        if (isPostLike(member, post)) {
            post.cancelLike();
            likeRepository.deleteByMemberIdAndPostId(member.getId(), id);
            basicResponseDto = BasicResponseDto.setSuccess("cancel like success", StatusCode.OK);
        } else {
            // 추가
            post.addLike();
            Likes like = new Likes(member, post);
            likeRepository.save(like);
            basicResponseDto = BasicResponseDto.setSuccess("add like success", StatusCode.OK);
        }
        return new ResponseEntity(basicResponseDto, HttpStatus.OK);
    }

    /**
     * 댓글 좋아요/좋아요 취소
     */
    public ResponseEntity updateCommentLike(Long postId, Long commentId, Member member) {
        Post post = validatePost(postId);
        Comment comment = validateComment(commentId);
        BasicResponseDto basicResponseDto;

        //이미 좋아요를 했으면 취소
        if (isCommentLike(member, post, comment)) {
            comment.cancelLike();
            likeRepository.deleteByMemberIdAndPostIdAndCommentId(member.getId(), postId, commentId);
            basicResponseDto = BasicResponseDto.setSuccess("cancel like success", StatusCode.OK);
        }else{
            //추가
            comment.addLike();
            Likes like = new Likes(member, post, comment);
            likeRepository.save(like);
            basicResponseDto = BasicResponseDto.setSuccess("add like success", StatusCode.OK);
        }
        return new ResponseEntity(basicResponseDto, HttpStatus.OK);
    }

    //댓글 존재 여부 확인
    private Comment validateComment(Long id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new CommentException(ExceptionMessage.NO_SUCH_COMMENT_EXCEPTION.getMessage())
        );
    }

    //게시글 존재 여부 확인
    private Post validatePost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new PostException(ExceptionMessage.NO_SUCH_BOARD_EXCEPTION.getMessage())
        );
    }

    //게시글 좋아요 여부 확인
    private boolean isPostLike(Member member, Post post) {
        Optional<Likes> like = likeRepository.findByMemberIdAndPostId(member.getId(), post.getId());
        if (like.isPresent()) {
            return true;
        }
        return false;
    }

    //댓글 좋아요 여부 확인
    private boolean isCommentLike(Member member, Post post, Comment comment) {
        Optional<Likes> like = likeRepository.findByMemberIdAndPostIdAndCommentId(member.getId(), post.getId(), comment.getId());
        if (like.isPresent()) {
            return true;
        }
        return false;
    }
}
