package uz.OLXCone.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.OLXCone.model.User;
import uz.OLXCone.utils.Role;

import java.util.Optional;
import java.util.UUID;

@Repository

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Page<User> findAllByRoleEquals(Role role, Pageable pageable);

    User findByRoleEqualsAndId(Role role, UUID id);
}
