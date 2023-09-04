package uz.OLXCone.payload.out;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;
@Getter
@Setter
public class FullAdDTO {
    private UUID id;
    private UserDTO user;
    private CategoryOutDTO  category;
    private String title;
    private String description;
    private Double price;
    private Integer viewCount;
    private RegionOutDTO region;
    private DistrictOutDTO district;
    private String imagePath;
    private String phoneNumber;
    private Timestamp createdAt;
}
