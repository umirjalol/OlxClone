package uz.OLXCone.model;

import jakarta.persistence.*;
import lombok.*;
import uz.OLXCone.model.template.AbsUUIDEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Category extends AbsUUIDEntity {
    @Column(nullable = false, unique = true, length = 20)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category parent;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<Category> categoryList;
}
