package uz.OLXCone.payload.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import uz.OLXCone.exception.CheckException;

@Getter
@Setter
public class SignUpDTO {
    @NotEmpty(message = "FullName cannot be empty")
    @Length(min = 1,max = 50,message = "name length isn't valid")
    private String fullName;
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,}$",
            message = "Password is not valid")
    private String password;
    private String rePassword;
    public void checkRePassword() {
        if (!password.equals(rePassword)) {
            throw new CheckException("password and re password don't match");
        }
    }
}
