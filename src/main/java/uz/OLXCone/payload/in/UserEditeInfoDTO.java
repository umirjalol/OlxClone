package uz.OLXCone.payload.in;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEditeInfoDTO {
    private String fullName;
    private String oldPassword;
    private String password;
    private String rePassword;
}
