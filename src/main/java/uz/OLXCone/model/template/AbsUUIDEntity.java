package uz.OLXCone.model.template;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@MappedSuperclass
@EqualsAndHashCode
@Getter
@Setter
public abstract class AbsUUIDEntity {
    @Id
    @GeneratedValue(generator = "uuid_my_id")
    @GenericGenerator(name = "uuid_my_id", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
}