package myblog.myblog.dto.comment;

import lombok.Data;

@Data
public class CommentRequestDto {
    private Long parentId;
    private String comment;

}
