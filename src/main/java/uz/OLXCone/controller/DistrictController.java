package uz.OLXCone.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.OLXCone.payload.ApiResult;
import uz.OLXCone.payload.in.DistrictInDTO;
import uz.OLXCone.payload.out.DistrictOutDTO;
import uz.OLXCone.service.DistrictService;
import uz.OLXCone.utils.AppConstants;

import java.util.List;

import static uz.OLXCone.utils.AppConstants.ADMIN;
import static uz.OLXCone.utils.AppConstants.SUPER_ADMIN;

@RestController
@RequestMapping(DistrictController.PATH)
@RequiredArgsConstructor
@Tag(name = "District Controller")
public class DistrictController {

    public static final String PATH = AppConstants.BASE_PATH + "/district";
    public static final String ADD = "/add/{regionId}";
    public static final String EDITE = "/edite/{id}";
    public static final String DELETE = "/delete/{id}";
    public static final String GET_BY_REGION = "/get-by-region/{id}";

    private final DistrictService districtService;

    @PreAuthorize("hasAnyRole('" + SUPER_ADMIN + "','" + ADMIN + "') ")
    @PostMapping(ADD)
    @Operation(summary = "Add a new District on the region",
            description = " <b> Admin, super admin can access  </b> <br> ",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResult<Object> add(@RequestBody @Valid
                                 DistrictInDTO district,
                                 @PathVariable String regionId) {
        return districtService.add(district, regionId);
    }

    @PreAuthorize("hasAnyRole('" + SUPER_ADMIN + "','" + ADMIN + "') ")
    @PutMapping(EDITE)
    @Operation(summary = "Edite a District",
            description = "<b> Admin, super admin can access  </b> <br> ",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResult<DistrictOutDTO> edite(@RequestBody @Valid
                                           DistrictInDTO districtIn,
                                           @PathVariable String id) {
        return districtService.edite(districtIn, id);
    }

    @PreAuthorize("hasAnyRole('" + SUPER_ADMIN + "','" + ADMIN + "') ")
    @DeleteMapping(DELETE)
    @Operation(summary = "Delete a District",
            description = "<b> Admin, super admin can access </b> <br>  ",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResult<Object> delete(@PathVariable String id) {
        return districtService.delete(id);
    }

    @GetMapping(GET_BY_REGION)
    @Operation(summary = "Get all Districts by region",
            description = "<b> No login required </b> <br> " )
    public ApiResult<List<DistrictOutDTO>> gatAll(@PathVariable String id) {
        return districtService.getByRegion(id);
    }

}
