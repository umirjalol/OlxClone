package uz.OLXCone.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.OLXCone.model.Image;
import uz.OLXCone.model.User;
import uz.OLXCone.repository.ImageRepository;
import uz.OLXCone.repository.UserRepository;
import uz.OLXCone.utils.AppConstants;
import uz.OLXCone.utils.Role;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlMode;

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws IOException {
        if (!Objects.equals(ddlMode, "create"))
            return;
        loadSuperAdmin();
        loadAdminAndUser();
        loadDefaultImage();
    }

    private void loadSuperAdmin() {
        User user = new User();
        user.setFullName("super admin");
        user.setEmail("superAdmin@gmail.com");
        user.setPassword(passwordEncoder.encode("Aa1!1234"));
        user.setIsBlocked(false);
        user.setRole(Role.SUPER_ADMIN);
        userRepository.save(user);
    }

    private void loadAdminAndUser() {
        String password = passwordEncoder.
                encode("Aa1!1234");
        User user = new User();
        user.setFullName("admin");
        user.setEmail("admin@gmail.com");
        user.setPassword(password);
        user.setIsBlocked(false);
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        user.setId(null);
        user.setFullName("user");
        user.setEmail("user@gmail.com");
        user.setPassword(password);
        user.setIsBlocked(false);
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    private void loadDefaultImage() throws IOException {
        FileInputStream stream = new FileInputStream(
                "src/main/resources/static/default image.jpg");
        byte[] bytes = stream.readAllBytes();
        stream.close();
        Image image = new Image();
        image.setName("default image.jpg");
        image.setSize(5292L);
        image.setExtension("jpg");
        image.setContentType("image/jpg");
        image.setData(bytes);
        image.setId(UUID.fromString(AppConstants.
                DefaultImageId));
        imageRepository.insert(image);
    }

}
