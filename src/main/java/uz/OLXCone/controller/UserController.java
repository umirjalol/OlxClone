package uz.OLXCone.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.OLXCone.payload.ApiResult;
import uz.OLXCone.payload.in.UserEditeInfoDTO;
import uz.OLXCone.service.UserService;
import uz.OLXCone.utils.AppConstants;
import uz.OLXCone.utils.Role;

import static uz.OLXCone.utils.AppConstants.*;

@RestController
@RequestMapping(UserController.PATH)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Controller", description = "Users and Admins dashboard")
public class UserController {
    //region path
    public static final String PATH = AppConstants.BASE_PATH + "/user";
    public static final String PROFILE_INFO = "/me";
    public static final String EDIT_PROFILE = "/editProfile";
    public static final String APPOINT_AS_ADMIN = "/appoint-as-admin/{id}";
    public static final String ASSIGN_AS_USER = "/assign-as-user/{id}";
    public static final String BLOCKING = "/blocking/{id}";
    public static final String UNBLOCK = "/unblock/{id}";
    public static final String GET_ALL_USERS = "/get-all-users";
    public static final String GET_ALL_ADMINS = "/get-all-admins";
    public static final String MY_DISABLE_ADS = "/my-disable-ads";
    //endregion
    private final UserService userService;

    @PreAuthorize("hasAnyRole('" + USER + "','" + ADMIN + "','" + SUPER_ADMIN + "') ")
    @PutMapping(EDIT_PROFILE)
    @Operation(summary = "Edit Profile information",
            description = "<b> User, admin, super admin can access  </b> <br> " +
                    "<b> You can edit the name and password </b> <br>")
    public ApiResult<?> editeProfile(@RequestBody UserEditeInfoDTO userEditeInfoDTO) {
        return userService.userEditeProfile(userEditeInfoDTO);
    }

    @PreAuthorize("hasAnyRole('" + USER + "','" + ADMIN + "','" + SUPER_ADMIN + "') ")
    @GetMapping(PROFILE_INFO)
    @Operation(summary = "View Profile information",
            description = "<b> User, admin, super admin can access  </b> <br> ")
    public ApiResult<?> profileInfo() {
        return userService.profileInfo();
    }

    @PreAuthorize("hasAnyRole('" + SUPER_ADMIN + "','" + ADMIN + "') ")
    @GetMapping(GET_ALL_USERS)
    @Operation(summary = "View all Users list",
            description = "<b> Admin, super admin can access  </b> <br> ")
    public ApiResult<?> gatAllUsers(@RequestParam(defaultValue = "0")
                                    Integer page,
                                    @RequestParam(defaultValue = "10")
                                    Integer size) {
        return userService.getAllUsers(page, size, Role.USER);
    }

    @PreAuthorize("hasAnyRole('" + USER + "','" + ADMIN + "','" + SUPER_ADMIN + "') ")
    @GetMapping(MY_DISABLE_ADS)
    @Operation(summary = "view inactive ads",
            description = "<b> User, admin, super admin can access  </b> <br> ")
    public ApiResult<?> myDisableAds() {
        return userService.myDisableAds();
    }


    @PreAuthorize("hasAnyRole('" + SUPER_ADMIN + "','" + ADMIN + "') ")
    @GetMapping(GET_ALL_ADMINS)
    @Operation(summary = "View all Admins list",
            description = "<b> Admin, super admin can access  </b> <br> ")
    public ApiResult<?> gatAllAdmins(@RequestParam(defaultValue = "0")
                                     Integer page,
                                     @RequestParam(defaultValue = "10")
                                     Integer size) {
        return userService.getAllUsers(page, size, Role.ADMIN);
    }


    @PreAuthorize("hasAnyRole('" + SUPER_ADMIN + "') ")
    @PutMapping(APPOINT_AS_ADMIN)
    @Operation(summary = "Appoint as admin",
            description = "<b> Super admin can access  </b> <br> ")
    public ApiResult<?> appointAsAdmin(@PathVariable String id) {
        return userService.appointAsAdmin(id);
    }

    @PreAuthorize("hasAnyRole('" + SUPER_ADMIN + "') ")
    @PutMapping(ASSIGN_AS_USER)
    @Operation(summary = "Assign as user",
            description = "<b> Super admin can access  </b> <br> ")
    public ApiResult<?> assignAsUser(@PathVariable String id) {
        return userService.assignAsUser(id);
    }

    @PreAuthorize("hasAnyRole('" + SUPER_ADMIN + "','" + ADMIN + "') ")
    @PutMapping(BLOCKING)
    @Operation(summary = "Block users",
            description = "<b> Admin, super admin can access  </b> <br> " +
                    "<b> admin can block users on request super" +
                    " admin can block users and admins on request </b> <br>")
    public ApiResult<?> blocking(@PathVariable String id) {
        return userService.blocking(id);
    }

    @PreAuthorize("hasAnyRole('" + SUPER_ADMIN + "','" + ADMIN + "') ")
    @PutMapping(UNBLOCK)
    @Operation(summary = "Unblock users",
            description = "<b> Admin, super admin can access  </b> <br> " +
                    "<b>Admin can unblock users on request super admin " +
                    "can unblock users and admins on request")
    public ApiResult<?> unblock(@PathVariable String id) {
        return userService.unblock(id);
    }

}
