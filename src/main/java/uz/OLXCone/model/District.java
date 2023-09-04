package uz.OLXCone.model;

import jakarta.persistence.*;
import lombok.*;
import uz.OLXCone.model.template.AbsUUIDEntity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "region_id"})})
public class District extends AbsUUIDEntity {
    @Column(nullable = false, length = 20)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;
}
