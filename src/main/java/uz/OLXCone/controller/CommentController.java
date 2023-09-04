package uz.OLXCone.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.OLXCone.payload.ApiResult;
import uz.OLXCone.payload.in.CommentInDTO;
import uz.OLXCone.service.CommentService;
import uz.OLXCone.utils.AppConstants;

import static uz.OLXCone.utils.AppConstants.*;

@RestController
@RequestMapping(CommentController.PATH)
@RequiredArgsConstructor
@Tag(name = "Comment Controller")
public class CommentController {
    public static final String PATH = AppConstants.BASE_PATH + "/comment";
    public static final String GET_BY_ADS = "/get-by-ads/{adsId}";
    public static final String WRITE_BY_ADS = "/write-by-ads/{adsId}";
    public static final String DELETE = "/delete/{id}";

    private final CommentService commentService;

    @GetMapping(GET_BY_ADS)
    @Operation(summary = "view all Comments by ad",
            description = "<b> No login required </b> <br> " +
                    "<b> pagination with a return size of 15 </b> <br> " )
    public ApiResult<?> getByAds(@PathVariable String adsId,
                                 @RequestParam(defaultValue = "0")
                                 Integer page) {
        return commentService.getByAdsId(adsId, page);
    }

    @PreAuthorize("hasAnyRole('" + USER + "','" + ADMIN + "','" + SUPER_ADMIN + "') ")
    @PostMapping(WRITE_BY_ADS)
    @Operation(summary = "Write Comment on the ad",
            description = "<b> User, admin, super admin can access  </b> <br> ",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResult<Object> add(@PathVariable String adsId,
                                 @RequestBody @Valid
                                 CommentInDTO commentInDTO) {
        return commentService.write(adsId, commentInDTO);
    }

    @PreAuthorize("hasAnyRole('" + USER + "','" + ADMIN + "','" + SUPER_ADMIN + "') ")
    @DeleteMapping(DELETE)
    @Operation(summary = "Delete a Comment",
            description = "<b> User, admin, super admin can access  </b> <br> ",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResult<Object> delete(@PathVariable String id) {
        return commentService.delete(id);
    }
}
