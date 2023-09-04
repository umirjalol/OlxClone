package uz.OLXCone.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import uz.OLXCone.model.template.AbsUUIDEntity;
import uz.OLXCone.utils.AppConstants;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
public class Ads extends AbsUUIDEntity {
    @ManyToOne()
    private User user;
    @ManyToOne
    private Category category;
    @Column(nullable = false, length = 50)
    private String title;
    @Column(nullable = false, length = 200)
    private String description;
    @Column(nullable = false,
            columnDefinition = "numeric(10,2) default 0.0 ")
    private Double price;
    @Column(nullable = false,
            columnDefinition = "boolean default true")
    private Boolean isActive;
    @Column(nullable = false,
            columnDefinition = "integer default 0 ")
    private Integer viewCount;
    @ManyToOne
    private Region region;
    @ManyToOne
    private District district;
    @Column(nullable = false,
            columnDefinition = "varchar(80) default '" +
            AppConstants.DEFAULT_IMAGE_PATH + "'")
    private String imagePath;
    @Column(nullable = false, length = 13)
    private String phoneNumber;
    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

}
