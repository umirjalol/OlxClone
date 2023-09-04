package uz.OLXCone.payload.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CategoryInDTO {
    @NotBlank(message = " name must not be blank ")
    @Length(max = 20,min = 1,
            message = "name must be between 1 and 20 in length")
    private String name;
}
