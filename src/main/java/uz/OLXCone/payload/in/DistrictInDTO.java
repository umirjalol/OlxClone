package uz.OLXCone.payload.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class DistrictInDTO {
    @NotBlank(message = "District name must be not blank")
    @Length(min =1,max = 20, message =
            "District name length must be between 1 and 20 ")
    private String name;

}
