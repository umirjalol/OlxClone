package uz.OLXCone.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.OLXCone.payload.ApiResult;
import uz.OLXCone.payload.in.AdsInDTO;
import uz.OLXCone.payload.out.AdsOutDTO;
import uz.OLXCone.service.AdsService;

import static uz.OLXCone.utils.AppConstants.*;


@RestController
@RequestMapping(value = AdsController.PATH)
@RequiredArgsConstructor
@Tag(name = "Ad Controller")
public class AdsController {

    //region paths
    public static final String PATH = BASE_PATH + "/ads";
    public static final String FILTER = "/filter";
    public static final String SEARCH = "/search/{query}";
    public static final String GET_ALL = "/get-all";
    public static final String GET_FULL_AD = "/get-full-ad/{id}";
    public static final String GET_BY_CATEGORY = "/get-by-category/{id}";
    public static final String TOP = "/top";
    public static final String GET_BY_USER = "/get-by-user/{userId}";
    public static final String ADD = "/add";
    public static final String DELETE = "/delete/{id}";
    public static final String ACTIVE = "/active/{id}";
    public static final String DISABLE = "/disable/{id}";
    //endregion
    private final AdsService adsService;

    @PreAuthorize("hasAnyRole('" + USER + "','" + ADMIN + "','" + SUPER_ADMIN + "') ")
    @PutMapping(ACTIVE)
    @Operation(summary = "Advertising activation",
            description = "<b> User, admin, super admin can access  </b> <br> ",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResult<AdsOutDTO> makeActive(@PathVariable String id) {
        return adsService.makeActive(id);
    }

    @PreAuthorize("hasAnyRole('" + USER + "','" + ADMIN + "','" + SUPER_ADMIN + "') ")
    @PutMapping(DISABLE)
    @Operation(summary = "Deactivate advertising",
            description = "<b> User, admin, super admin can access  </b> <br> ",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResult<AdsOutDTO> makeDisable(@PathVariable String id) {
        return adsService.makeDisable(id);
    }

    @PreAuthorize("hasAnyRole('" + USER + "','" + ADMIN + "','" + SUPER_ADMIN + "') ")
    @PostMapping(ADD)
    @Operation(summary = "Add a new Ad",
            description = "<b> User, admin, super admin can access  </b> <br> ",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResult<?> add(@RequestParam String regionId,
                            @RequestParam String districtId,
                            @RequestParam String categoryId,
                            @RequestBody @Valid AdsInDTO adsInDTO) {
        return adsService.add(
                regionId, districtId, categoryId, adsInDTO);
    }

    @PreAuthorize("hasAnyRole('" + USER + "','" + ADMIN + "','" + SUPER_ADMIN + "') ")
    @DeleteMapping(DELETE)
    @Operation(summary = "Delete a Ad ",
            description = "<b> User, admin, super admin can access  </b> <br> ",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResult<?> delete(@PathVariable String id) {
        return adsService.delete(id);
    }

    //region get
    @GetMapping(FILTER)
    @Operation(summary = "Filter Ads by parameters",
            description = "<b> No login required </b> <br> " +
                    "<b> An ad is returned even if at least one parameter matches </b> <br> "
    )
    public ApiResult<?> filter(
            @RequestParam(required = false) String key,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return adsService.filter(category,
                key, region,
                district,
                maxPrice,
                minPrice,
                page, size);
    }

    @GetMapping(SEARCH)
    @Operation(summary = "Search Ads by keyword",
            description = "<b> No login required </b> <br>  " +
                    "<b> An ad will be returned if the keyword is in the title or description </b> <br> "
    )
    public ApiResult<?> search(@PathVariable String query,
                               @RequestParam(defaultValue = "0")
                               Integer page,
                               @RequestParam(defaultValue = "10")
                               Integer size) {
        return adsService.search(query, page, size);
    }

    @GetMapping(GET_FULL_AD)
    @Operation(summary = "View full Ad",
            description = "<b> No login required </b> <br>  " +
                    "<b> Advertising information will be returned in full </b> <br> " +
                    "<b> The number of views will increase </b> <br> ",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResult<?> gatFullAd(@PathVariable String id) {
        return adsService.getFullAd(id);
    }

    @GetMapping(GET_ALL)
    @Operation(summary = "View all Ads ",
            description = "<b> No login required </b> <br> " +
                    "<b> All ads are returned by pagination </b> <br> "
    )
    public ApiResult<?> getAll(@RequestParam(defaultValue = "0")
                               Integer page,
                               @RequestParam(defaultValue = "10")
                               Integer size) {
        return adsService.getAll(page, size);
    }


    @GetMapping(GET_BY_CATEGORY)
    @Operation(summary = "View all Ads By category",
            description = "<b> No login required </b> <br> " +
                    "<b> returned by pagination </b> <br> "
    )
    public ApiResult<?> gatByCategory(@PathVariable String id,
                                      @RequestParam(defaultValue = "0") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size) {
        return adsService.getByCategory(id, page, size);
    }

    @GetMapping(TOP)
    @Operation(summary = "View top sixteen Ads",
            description = "<b> No login required </b> <br> " +
                    "<b> the sixteen most recent ads are returned </b> <br> "
    )
    public ApiResult<?> top() {
        return adsService.getTop();
    }

    @GetMapping(GET_BY_USER)
    @Operation(summary = "View all Ads by user ",
            description = "<b> No login required </b> <br> " +
                    "<b> returned by pagination </b> <br> "
    )
    public ApiResult<?> getByUserId(@PathVariable String userId,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        return adsService.getByUserId(userId, page, size);
    }
    //endregion
}
