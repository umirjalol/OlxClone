package uz.OLXCone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.OLXCone.model.Image;

import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO image (extension, size, id, name, content_type, data)\n" +
            "VALUES ( ?1,?2,?3,?4,?5,?6);", nativeQuery = true)
    void insert(String extension, Long size, UUID id, String name, String contentTyp, byte[] data);

    default void insert(Image image) {
        insert(image.getExtension(),
                image.getSize(),
                image.getId(),
                image.getName(),
                image.getContentType(),
                image.getData());
    }
}
