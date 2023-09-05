package uz.OLXCone.payload.in;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class AdsInDTO {
    @Length(min = 15, max = 50, message = "title isn't valid")
    private String title;
    @Length(min = 90, max = 200 , message = "description isn't valid")
    private String description;
    @Max(value = 90_000_000, message = "price is very big")
    @Min(value = 0, message = "price is not negative")
    private Double price;
    @Pattern(regexp = "^\\+998(9[012345789]|6[125679]|7[01234569])[0-9]{7}$"
               , message = "phone number not valid")
    private String phoneNumber;
}
