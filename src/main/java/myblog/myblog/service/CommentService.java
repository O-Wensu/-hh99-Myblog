package myblog.myblog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myblog.myblog.domain.Comment;
import myblog.myblog.domain.Member;
import myblog.myblog.domain.Post;
import myblog.myblog.dto.BasicResponseDto;
import myblog.myblog.dto.comment.CommentRequestDto;
import myblog.myblog.dto.comment.CommentResponseDto;
import myblog.myblog.exception.custom_exeption.comment.CommentException;
import myblog.myblog.exception.custom_exeption.comment.NoSuchCommentException;
import myblog.myblog.exception.custom_exeption.post.NoAuthorizationException;
import myblog.myblog.exception.custom_exeption.post.NoSuchPostException;
import myblog.myblog.repository.CommentRepository;
import myblog.myblog.repository.LikeRepository;
import myblog.myblog.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    /**
     * 댓글 등록
     */
    @Transactional
    public ResponseEntity saveComment(Long postId, CommentRequestDto commentRequestDTO, Member member) {

        Comment comment = new Comment(commentRequestDTO);

        //게시글 존재 여부 확인
        Post post = validatePost(postId);

        Comment parentComment = null;

        //자식 댓글
        if (commentRequestDTO.getParentId() != null) {
            //대댓글을 작성하고자 하는 부모 댓글이 존재하는지 판단
            parentComment = validateComment(commentRequestDTO.getParentId());

            // 부모댓글의 게시글 번호와 자식댓글의 게시글 번호 같은지 체크하기
            if(parentComment.getPost().getId() != postId){
                throw new CommentException("부모 댓글과 자식 댓글의 게시글 번호가 일치하지 않습니다.");
            }
        }

        comment.setPost(post);
        comment.setMember(member);

        //대댓글의 부모 댓글 설정
        if (parentComment != null) {
            comment.updateParent(parentComment);
            log.info("parentCommentId: " + comment.getParent().getId());

        } else {
            comment.updateParent(null);
        }
/*
        // post의 댓글 리스트에 추가
        post.addComment(comment);*/

        commentRepository.save(comment);

        CommentResponseDto commentResponseDto = null;
        if(parentComment != null){
            commentResponseDto = CommentResponseDto.builder()
                    .id(comment.getId())
                    .username(comment.getMember().getUsername())
                    .comment(comment.getComment())
                    .createdAt(comment.getCreatedAt())
                    .modifiedAt(comment.getModifiedAt())
                    .parentComment(comment.getParent().getId())
                    .build();
        } else {
            commentResponseDto = CommentResponseDto.builder()
                    .id(comment.getId())
                    .username(comment.getMember().getUsername())
                    .comment(comment.getComment())
                    .createdAt(comment.getCreatedAt())
                    .modifiedAt(comment.getModifiedAt())
                    .build();
        }

        BasicResponseDto basicResponseDTO = BasicResponseDto.setSuccess("save success", commentResponseDto);
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);
        /*
        Comment comment = new Comment(commentRequestDTO);

        //게시글 존재 여부 확인
        Post post = validatePost(postId);

        // post의 댓글 리스트에 추가
        post.addComment(comment);
        comment.setMember(member);

        commentRepository.save(comment);
        BasicResponseDto basicResponseDTO = BasicResponseDto.setSuccess("save success", new CommentResponseDto(comment));
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);*/
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public ResponseEntity deleteComment(Long commentId, Member member) {
        //댓글 존재 여부 확인
        Comment comment = validateComment(commentId);

        //작성자의 댓글인지 확인
        isCommentAuthor(member, comment);

        //좋아요 삭제
        likeRepository.deleteByCommentId(comment.getId());

        commentRepository.deleteById(commentId);
        BasicResponseDto basicResponseDTO = BasicResponseDto.setSuccess("delete success", null);
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public ResponseEntity updateComment(Long commentId, CommentRequestDto commentRequestDTO, Member member) {
        // 댓글 존재 여부 확인
        Comment comment = validateComment(commentId);

        //작성자의 댓글인지 확인
        isCommentAuthor(member, comment);

        comment.update(commentRequestDTO);
        BasicResponseDto basicResponseDTO = BasicResponseDto.setSuccess("update success", new CommentResponseDto(comment));
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);
    }

    //댓글 존재 여부 확인
    private Comment validateComment(Long id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new NoSuchCommentException()
        );
    }

    //게시글 존재 여부 확인
    private Post validatePost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new NoSuchPostException()
        );
    }

    //작성자 일치 여부 판단
    private void isCommentAuthor(Member member, Comment comment) {
        if (!comment.getMember().getUsername().equals(member.getUsername())) {
            if (member.isAdmin()) return;
            throw new NoAuthorizationException();
        }
    }
}
