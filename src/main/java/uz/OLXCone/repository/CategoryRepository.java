package uz.OLXCone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.OLXCone.model.Category;

import java.util.List;
import java.util.UUID;
@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findAllByParentNull();

    Category findByName(String name);

    boolean existsByName(String name);
}
