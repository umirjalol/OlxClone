package uz.OLXCone.payload.out;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class UserDTO {
    private UUID id;
    private String imagePath;
    private String fullName;
}
