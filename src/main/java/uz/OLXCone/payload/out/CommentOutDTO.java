package uz.OLXCone.payload.out;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
public class CommentOutDTO {
    private UUID id;
    private UserDTO Owner;
    private String text;
    private Timestamp time;
}
