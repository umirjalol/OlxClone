package uz.OLXCone.payload.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.OLXCone.utils.Role;

@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserInfoDTO {
    private String imagePath;
    private String fullName;
    private String email;
    private Role role;
    private Boolean isBlocked;
}
