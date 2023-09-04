package uz.OLXCone.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.OLXCone.model.Comment;

import java.util.UUID;
@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Page<Comment> findByAdsIdOrderByTimeAsc(UUID adsId , Pageable pageable);
    @Transactional
    void deleteByAds_Id(UUID adsId);

}
