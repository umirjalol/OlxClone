package uz.OLXCone.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.OLXCone.exception.ConflictException;
import uz.OLXCone.model.User;

import java.time.LocalDateTime;

public class CommonUtils {

    public static String getExtension(String name) {
        String ext = null;
        if (name != null && !name.isEmpty()) {
            int dot = name.lastIndexOf('.');
            if (dot > 0 && dot <= name.length() - 2) {
                ext = name.substring(dot + 1);
            }
        }
        return ext;
    }

    public static PageRequest getPageable(Integer page, Integer size) {
        return PageRequest.
                of(page < 0 ? 1 : page, size < 1 ? 10 : size);
    }

    public static User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User)
            return (User) authentication.getPrincipal();
        throw new ConflictException("some wrong");
    }

    public static boolean isCurrentUser(User user) {
        return user != null && user.equals(getCurrentUser());
    }

}
