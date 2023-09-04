package uz.OLXCone.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import uz.OLXCone.model.template.AbsUUIDEntity;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Region extends AbsUUIDEntity {
    @Column(nullable = false, length = 20, unique = true)
    private String name;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "region")
    private List<District> districts;
}
