package uz.OLXCone.payload.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.OLXCone.utils.Role;

import java.util.UUID;

@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserInfoDTO {
    private UUID id;
    private String imagePath;
    private String fullName;
    private String email;
    private Role role;
    private Boolean isBlocked;
}
