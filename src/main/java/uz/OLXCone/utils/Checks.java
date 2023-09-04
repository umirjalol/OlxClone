package uz.OLXCone.utils;

import org.springframework.web.multipart.MultipartFile;
import uz.OLXCone.payload.in.UserEditeInfoDTO;

public class Checks {

    public static boolean isUUID(String str) {
        if (str.isBlank())
            return false;
        return str.matches(
                "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"
        );
    }

    public static boolean isValidImage(MultipartFile image) {
        if (!image.isEmpty()) {
            String name = image.getName();
            if (!name.isBlank() &&
                    name.length() > 0 &&
                    name.length() < 50) {
                String type =image.getContentType();
                return AppConstants.ImageFormat.contains(type);
            }
        }
        return false;
    }

    public static boolean isValidCode(Integer code) {
        return 99999 < code && code < 1000000;
    }

    public static boolean checkFullName(String fullName) {
        return !fullName.isBlank() &&
                fullName.length() >= 1 &&
                fullName.length() <= 50;
    }


    public static boolean checkPassword(UserEditeInfoDTO userEditeDTO) {
        String password = userEditeDTO.getPassword();
        if (!password.isBlank()) {
            boolean matches = password.
                    matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)" +
                            "(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,}$");
            if (matches) {
                return userEditeDTO
                        .getPassword()
                        .equals(userEditeDTO
                                .getRePassword()
                        );
            }
        }
        return false;
    }

    public static boolean checkFilterParam(String category, String key,
                                           String region, String district,
                                           Double maxPrice, Double minPrice) {
        category = checkStr(category);
        key = checkStr(key);
        region = checkStr(region);
        district = checkStr(district);
        maxPrice = checkDouble(maxPrice);
        minPrice = checkDouble(minPrice);
        return category != null || key != null ||
                region != null || district != null ||
                minPrice != null || maxPrice != null;
    }

    private static Double checkDouble(Double num) {
        if (num != null) {
            num = num > 100_000_000 ?
                    100_000_000 : num;
            num = num < 0 ? 0 : num;
        }
        return num;
    }

    private static String checkStr(String str) {
        return str == null ? null :
                str.isBlank() ? null : str;
    }
}
