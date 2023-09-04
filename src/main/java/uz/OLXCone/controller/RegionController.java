package uz.OLXCone.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.OLXCone.payload.ApiResult;
import uz.OLXCone.payload.in.RegionInDTO;
import uz.OLXCone.payload.out.RegionOutDTO;
import uz.OLXCone.service.RegionService;
import uz.OLXCone.utils.AppConstants;

import java.util.List;

import static uz.OLXCone.utils.AppConstants.ADMIN;
import static uz.OLXCone.utils.AppConstants.SUPER_ADMIN;

@RestController
@RequestMapping(RegionController.PATH)
@RequiredArgsConstructor
@Tag(name = "Region Controller")
public class RegionController {

    public static final String PATH = AppConstants.BASE_PATH + "/Region";
    public static final String ADD = "/add";
    public static final String GET_ALL = "/get-all";
    public static final String DELETE = "/delete/{id}";
    public static final String EDITE = "/edite/{id}";

    private final RegionService regionService;

    @PreAuthorize("hasAnyRole('" + SUPER_ADMIN + "','" + ADMIN + "') ")
    @PostMapping(ADD)
    @Operation(summary = "Add a new Region ",
            description = "<b> Admin, super admin can access  </b> <br> ",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResult<Object> add(@RequestBody @Valid
                                 RegionInDTO regionIndto) {
        return regionService.add(regionIndto);
    }

    @PreAuthorize("hasAnyRole('" + SUPER_ADMIN + "','" + ADMIN + "') ")
    @PutMapping(EDITE)
    @Operation(summary = "Edite a Region",
            description = "<b> Admin, super admin can access  </b> <br> ",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResult<RegionOutDTO> edite(@PathVariable String id,
                                         @RequestBody @Valid
                                         RegionInDTO regionIn) {
        return regionService.edite(id, regionIn);
    }

    @PreAuthorize("hasAnyRole('" + SUPER_ADMIN + "','" + ADMIN + "') ")
    @DeleteMapping(DELETE)
    @Operation(summary = "Delete a Region ",
            description = "<b> Admin, super admin can access </b> <br> ",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResult<Object> delete(@PathVariable String id) {
        return regionService.delete(id);
    }

    @GetMapping(GET_ALL)
    @Operation(summary = "Get all Regions",
            description = "<b> No login required </b> <br> " )
    public ApiResult<List<RegionOutDTO>> gatAll() {
        return regionService.getAll();
    }
}
