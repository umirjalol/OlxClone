package uz.OLXCone.payload.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CommentInDTO {
    @NotBlank(message = "comment must not be blank")
    @Length(max = 225, min = 1,
            message = "comment length between 1 and 255 ")
    private String text;

}
