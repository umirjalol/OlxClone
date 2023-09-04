package uz.OLXCone.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import uz.OLXCone.model.template.AbsUUIDEntity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Image extends AbsUUIDEntity {
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 5)
    private String extension;
    private Long size;
    private String contentType;
    private byte[] data;

}
