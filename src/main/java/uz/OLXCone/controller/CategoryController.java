package uz.OLXCone.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.OLXCone.payload.ApiResult;
import uz.OLXCone.payload.out.CategoryOutDTO;
import uz.OLXCone.payload.in.CategoryInDTO;
import uz.OLXCone.service.CategoryService;
import uz.OLXCone.utils.AppConstants;

import java.util.List;

import static uz.OLXCone.utils.AppConstants.ADMIN;
import static uz.OLXCone.utils.AppConstants.SUPER_ADMIN;

@RestController
@RequestMapping(CategoryController.PATH)
@RequiredArgsConstructor
@Tag(name = "Category Controller")
public class CategoryController {
    //region paths
    public static final String PATH = AppConstants.BASE_PATH + "/category";
    public static final String GET_ALL = "/get-all";
    public static final String DELETE = "/delete/{id}";
    public static final String ADD = "/add";
    public static final String EDITE = "/edite/{id}";
    //endregion
    private final CategoryService categoryService;

    @GetMapping(GET_ALL)
    @Operation(summary = "view all Categories",
            description = "<b> No login required </b> <br> ")
    public ApiResult<List<CategoryOutDTO>> gatAll() {
        return categoryService.getAll();
    }

    @PreAuthorize("hasAnyRole('" + SUPER_ADMIN + "','" + ADMIN + "') ")
    @PostMapping(ADD)
    @Operation(summary = "Add a new Category",
            description = "<b> Admin, super admin can access  </b> <br> " +
                    "<b> If a parent ID is given, a subcategory is created\n" +
                    "otherwise, a simple category is created </b> <br> ",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResult<Object> add(@RequestParam(required = false)
                                 String parentId,
                                 @RequestBody @Valid
                                 CategoryInDTO category) {
        return categoryService.add(category, parentId);
    }

    @PreAuthorize("hasAnyRole('" + SUPER_ADMIN + "','" + ADMIN + "') ")
    @PutMapping(EDITE)
    @Operation(summary = "Edite Category",
            description = "<b> Admin, super admin can access  </b> <br> ",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResult<CategoryOutDTO> edite(@PathVariable String id,
                                           @RequestBody @Valid
                                           CategoryInDTO category) {
        return categoryService.edite(category, id);
    }

    @PreAuthorize("hasAnyRole('" + SUPER_ADMIN + "','" + ADMIN + "') ")
    @DeleteMapping(DELETE)
    @Operation(summary = "Delete Category",
            description = "<b> Admin, super admin can access </b> <br>  ",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResult<Object> delete(@PathVariable String id) {
        return categoryService.delete(id);
    }

}
