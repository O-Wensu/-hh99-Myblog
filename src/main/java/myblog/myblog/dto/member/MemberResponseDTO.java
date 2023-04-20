package myblog.myblog.dto.member;

import lombok.Getter;
import lombok.ToString;
import myblog.myblog.domain.Member;
import myblog.myblog.domain.UserRoleEnum;

@Getter
@ToString
public class MemberResponseDTO {

    private Long id;
    private String username;
    private UserRoleEnum role;

    public MemberResponseDTO(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.role = member.getRole();
    }
}
