package uz.OLXCone.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.OLXCone.model.Ads;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdsRepository extends JpaRepository<Ads, UUID> {

    boolean existsByCategoryId(UUID categoryId);

    boolean existsByDistrictId(UUID uuid);

    boolean existsByIdAndIsActiveTrue(UUID id);

    Page<Ads> findByIsActiveTrueAndCategoryIdOrderByCreatedAtDesc(Pageable pageable, UUID categoryId);

    Page<Ads> findByIsActiveTrueAndUserIdOrderByCreatedAtDesc(Pageable pageable, UUID userId);

    Page<Ads> findAllByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);

    Long countAllByIsActiveTrue();

    Page<Ads> searchByTitleLikeIgnoreCaseOrDescriptionLikeIgnoreCaseAndIsActiveIsTrueOrderByCreatedAtDesc(String title, String description, Pageable pageable);

    default Page<Ads> searching(String key, Integer page, Integer size) {
        PageRequest pageable = PageRequest.
                of(page < 0 ? 1 : page, size < 1 ? 10 : size);
        String regex = "%";
        return
                searchByTitleLikeIgnoreCaseOrDescriptionLikeIgnoreCaseAndIsActiveIsTrueOrderByCreatedAtDesc(
                        regex + key + regex, regex + key + regex, pageable
                );
    }

    @Query(value = "SELECT * FROM Ads as a WHERE a.is_active" +
            "  AND ((:category IS NOT NULL AND a.category_id =" +
            "             (SELECT c.id FROM category AS c WHERE c.name = :category))" +
            "    OR (:word IS NOT NULL AND a.description LIKE '%' || :word || '%'" +
            "        OR a.title LIKE '%'||:word||'%')" +
            "    OR (:regi IS NOT NULL AND a.region_id =" +
            "             (SELECT r.id FROM region AS r WHERE r.name = :regi))" +
            "    OR (:district IS NOT NULL AND a.district_id =" +
            "             (SELECT d.id FROM district AS d WHERE d.name = :district))" +
            "    OR ((:maxP IS NOT NULL OR :minP IS NOT NULL) AND" +
            "        ((:maxP IS NULL OR (a.price < :maxP)) = (:minP IS NULL OR (a.price > :minP)))));"
            , nativeQuery = true
    )
    Page<Ads> filter(@Param("category") String category, @Param("word") String query,
                     @Param("regi") String region, @Param("district") String district,
                     @Param("maxP") Double maxPrice, @Param("minP") Double minPrice,
                     Pageable pageable);

    Ads findByIdAndIsActiveTrue(UUID id);

    List<Ads> findByUserIdAndIsActiveFalse(UUID userId);
}