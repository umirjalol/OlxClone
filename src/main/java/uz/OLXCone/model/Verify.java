package uz.OLXCone.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import uz.OLXCone.model.template.AbsUUIDEntity;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Verify extends AbsUUIDEntity {
    @OneToOne(cascade = CascadeType.REMOVE)
    private User user;
    @Column(nullable = false, unique = true)
    private Integer code;
    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp time;

}
