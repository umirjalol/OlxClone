package uz.OLXCone.payload.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DistrictOutDTO {
    private UUID id;
    private String name;
}
