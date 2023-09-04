package uz.OLXCone.payload.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CategoryOutDTO {
    private UUID id;
    private String name;
    private List<CategoryOutDTO> categoryList;
}
