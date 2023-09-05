package uz.OLXCone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.OLXCone.model.Verify;

import java.sql.Timestamp;
import java.util.UUID;
@Repository
public interface VerifyRepository extends JpaRepository<Verify, UUID> {
    Verify findByCode(Integer code);
    boolean existsByCode(Integer code);

    @Transactional
    void deleteAllByTimeBefore(Timestamp valueOf);

    @Modifying
    @Transactional
    @Query(value = "delete from Verify where id = ?1",nativeQuery = true)
    void remove( UUID id);
}
