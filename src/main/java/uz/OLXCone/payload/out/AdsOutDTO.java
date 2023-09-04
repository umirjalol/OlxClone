package uz.OLXCone.payload.out;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
public class AdsOutDTO {
    private UUID id;
    private String title;
    private Double price;
    private RegionOutDTO region;
    private DistrictOutDTO district;
    private String imagePath;
    private Timestamp createdAt;
}
