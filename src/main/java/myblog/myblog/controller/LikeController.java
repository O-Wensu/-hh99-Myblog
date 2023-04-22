package myblog.myblog.controller;

import lombok.RequiredArgsConstructor;
import myblog.myblog.security.UserDetailsImpl;
import myblog.myblog.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/like/{id}")
    public ResponseEntity insert(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.updatePostLike(id, userDetails.getMember());
    }

    @PostMapping("/like/{postId}/{commentId}")
    public ResponseEntity updateCommentLike(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.updateCommentLike(postId, commentId, userDetails.getMember());
    }
}
