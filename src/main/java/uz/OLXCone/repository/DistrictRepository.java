package uz.OLXCone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.OLXCone.model.District;

import java.util.List;
import java.util.UUID;
@Repository
public interface DistrictRepository extends JpaRepository<District, UUID> {
    List<District> findByRegionId(UUID regionId);

    District findByRegionIdAndName(UUID id, String name);

    boolean existsByNameAndRegionId(String name, UUID id);
}
