package uz.OLXCone.payload.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class RegionInDTO {
    @NotBlank(message = "Region name must be not empty")
    @Length(min = 1, max = 20, message =
            "Region name must be between 1 and 20 ")
    private String name;
}
