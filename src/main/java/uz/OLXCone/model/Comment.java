package uz.OLXCone.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import uz.OLXCone.model.template.AbsUUIDEntity;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Comment extends AbsUUIDEntity {
    @ManyToOne()
    private User owner;
    @ManyToOne
    private Ads ads;
    @Column(nullable = false)
    private String text;
    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp time;
}
