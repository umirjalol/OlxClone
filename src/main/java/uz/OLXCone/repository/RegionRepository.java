package uz.OLXCone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.OLXCone.model.Region;

import java.util.UUID;
@Repository
public interface RegionRepository extends JpaRepository<Region, UUID> {
    boolean existsByName(String name);

    Region findByName(String name);
}
