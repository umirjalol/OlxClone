package uz.OLXCone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.OLXCone.component.MyMapper;
import uz.OLXCone.model.Ads;
import uz.OLXCone.model.User;
import uz.OLXCone.payload.ApiResult;
import uz.OLXCone.payload.in.UserEditeInfoDTO;
import uz.OLXCone.payload.out.FullAdDTO;
import uz.OLXCone.payload.out.UserInfoDTO;
import uz.OLXCone.repository.AdsRepository;
import uz.OLXCone.repository.UserRepository;
import uz.OLXCone.utils.Checks;
import uz.OLXCone.utils.CommonUtils;
import uz.OLXCone.utils.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdsRepository adsRepository;
    private final MyMapper mapper;


    public ApiResult<?> profileInfo() {
        User user = CommonUtils.getCurrentUser();
        UserInfoDTO userInfoDTO = mapper.userToUserInfoDTO(user);
        userInfoDTO.setIsBlocked(null);
        return ApiResult.noMessage(true, userInfoDTO);
    }

    public ApiResult<?> userEditeProfile(UserEditeInfoDTO userEditeDTO) {
        StringBuilder sb = new StringBuilder();
        if (userEditeDTO.getFullName() != null)
            sb.append(editeFullName(userEditeDTO));
        if (userEditeDTO.getOldPassword() != null)
            sb.append(editePassword(userEditeDTO));
        boolean empty = sb.isEmpty();
        String res = empty ? "Some wrong" : sb.toString();
        return ApiResult.noObject(res, !empty);
    }

    private String editePassword(UserEditeInfoDTO userEditeDTO) {
        User currentUser = CommonUtils.getCurrentUser();
        boolean matches = passwordEncoder.matches(
                userEditeDTO.getOldPassword(),
                currentUser.getPassword());
        if (!matches) return "";
        boolean isValid = Checks.checkPassword(userEditeDTO);
        if (isValid) {
            String encoded = passwordEncoder.
                    encode(userEditeDTO.getPassword());
            currentUser.setPassword(encoded);
            userRepository.save(currentUser);
            return "password successfully edited\n";
        }
        return "";
    }


    private String editeFullName(UserEditeInfoDTO userEditeDTO) {
        String fullName = userEditeDTO.getFullName();
        boolean isValid = Checks.checkFullName(fullName);
        if (isValid) {
            User user = CommonUtils.getCurrentUser();
            user.setFullName(fullName);
            userRepository.save(user);
            return "full name successfully edited \n";
        }
        return "";
    }


    //region Admin methods
    public ApiResult<?> getAllUsers(Integer page, Integer size, Role role) {
        PageRequest pageable = CommonUtils.getPageable(page, size);
        Page<User> userPage = userRepository.
                findAllByRoleEquals(role, pageable);
        if (userPage.isEmpty())
            return ApiResult.noObject(
                    "we haven't " + role.name().toLowerCase(),
                    false);
        Page<UserInfoDTO> userInfoDTOS =
                userPage.map(mapper::userToUserInfoDTO);
        return ApiResult.noMessage(
                true,
                userInfoDTOS);
    }

    public ApiResult<?> appointAsAdmin(String id) {
        return changeRole(id, Role.ADMIN,
                "successfully appoint as admin");
    }

    public ApiResult<?> assignAsUser(String id) {
        return changeRole(id, Role.USER,
                "successfully assign as user");
    }

    public ApiResult<?> blocking(String id) {
        if (Checks.isUUID(id)) {
            UUID uuid = UUID.fromString(id);
            Role role = CommonUtils
                    .getCurrentUser()
                    .getRole();
            if (role.equals(Role.ADMIN))
                return userBlocking(uuid, true);
            else if (role.equals(Role.SUPER_ADMIN))
                return userOrAdminBlocking(uuid, true);
        }
        return ApiResult.noObject(
                "user not found ",
                false);
    }

    public ApiResult<?> unblock(String id) {
        if (Checks.isUUID(id)) {
            UUID uuid = UUID.fromString(id);
            Role role = CommonUtils
                    .getCurrentUser()
                    .getRole();
            if (role.equals(Role.ADMIN))
                return userBlocking(uuid, false);
            else if (role.equals(Role.SUPER_ADMIN))
                return userOrAdminBlocking(uuid, false);
        }
        return ApiResult.noObject(
                "user not found ",
                false);
    }
    //endregion

    //region other methods
    public ApiResult<?> changeRole(String id, Role role, String txt) {
        if (Checks.isUUID(id)) {
            Optional<User> userOptional =
                    userRepository.findById(
                            UUID.fromString(id));
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setRole(role);
                userRepository.save(user);
                return ApiResult.noObject(
                        txt, true);
            }
        }
        return ApiResult.noObject(
                "user not found"
                , false);
    }

    private ApiResult<?> userOrAdminBlocking(UUID id, boolean isBlocked) {
        Optional<User> userOptional =
                userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.getRole().equals(Role.SUPER_ADMIN)) {
                updateIsBlocked(user, isBlocked);
                return ApiResult.noObject(
                        "user " + (isBlocked ? "blocked" : "unblocked"),
                        true);
            }
        }
        return ApiResult.noObject(
                "user not found ",
                false);
    }

    private ApiResult<?> userBlocking(UUID id, boolean isBlocked) {
        User user = userRepository.
                findByRoleEqualsAndId(Role.USER, id);
        if (user != null) {
            updateIsBlocked(user, isBlocked);
            return ApiResult.noObject(
                    "user " + (isBlocked ? "blocked" : "unblocked"),
                    true);
        }
        return ApiResult.noObject(
                "user not found ",
                false);
    }

    private void updateIsBlocked(User user, boolean isBlocked) {
        user.setIsBlocked(isBlocked);
        userRepository.save(user);
    }

    public ApiResult<?> myDisableAds() {
        User currentUser = CommonUtils.getCurrentUser();
        List<Ads> adsList = adsRepository.
                findByUserIdAndIsActiveFalse(currentUser.getId());
        if (!adsList.isEmpty()) {
            List<FullAdDTO> adDTOList =
                    mapper.AdsListToFullAdDTO(adsList);
            return ApiResult.
                    noMessage(true, adDTOList);
        }
        return ApiResult.noObject(
                "you haven't disable ads"
                , false);
    }
    //endregion
}
