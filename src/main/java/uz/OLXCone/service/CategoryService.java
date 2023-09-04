package uz.OLXCone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.OLXCone.component.MyMapper;
import uz.OLXCone.model.Category;
import uz.OLXCone.payload.ApiResult;
import uz.OLXCone.payload.out.CategoryOutDTO;
import uz.OLXCone.payload.in.CategoryInDTO;
import uz.OLXCone.repository.AdsRepository;
import uz.OLXCone.repository.CategoryRepository;
import uz.OLXCone.utils.Checks;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final AdsRepository adsRepository;
    private final MyMapper myMapper;

    public ApiResult<Object> add(CategoryInDTO categoryInDTO, String parentId) {
        boolean exists = categoryRepository.
                existsByName(categoryInDTO.getName());
        if (exists) return ApiResult.noObject(
                "must be not category name duplicate ",
                false);
        if (parentId == null) {
            Category category = myMapper.
                    categoryInDTOToCategory(categoryInDTO);
            categoryRepository.save(category);
            return ApiResult.noObject(
                    "successfully saved ",
                    true);
        } else if (Checks.isUUID(parentId)) {
            Optional<Category> optionalCategory =
                    categoryRepository.findById(
                            UUID.fromString(parentId)
                    );
            if (optionalCategory.isPresent()) {
                Category category =
                        new Category(categoryInDTO.getName(),
                                optionalCategory.get(),
                                null);
                categoryRepository.save(category);
                return ApiResult.noObject(
                        "successfully saved ",
                        true);
            }
        }
        return ApiResult.noObject(
                "parent category not found ",
                false);
    }

    public ApiResult<List<CategoryOutDTO>> getAll() {
        List<Category> categories =
                categoryRepository.findAllByParentNull();
        if (categories.isEmpty()) {
            return ApiResult.
                    noObject("we haven't any category", false);
        }
        List<CategoryOutDTO> categoryDTOS = myMapper.
                categoryListToCategoryDTOList(categories);
        return ApiResult.noMessage(true, categoryDTOS);
    }

    public ApiResult<Object> delete(String id) {
        if (Checks.isUUID(id)) {
            UUID categoryId = UUID.fromString(id);
            Optional<Category> categoryOptional = categoryRepository.
                    findById(categoryId);
            if (categoryOptional.isPresent()) {
                boolean isEmpty = categoryOptional.
                        get().
                        getCategoryList().
                        isEmpty();
                boolean isExist = adsRepository.
                        existsByCategoryId(categoryId);
                if (isExist || !isEmpty) {
                    return ApiResult.noObject(
                            "category not deleted !!! because there are ads " +
                                    "and sub categories related to this category",
                            false);
                } else {
                    categoryRepository.
                            deleteById(categoryId);
                    return ApiResult.
                            noObject("successfully deleted ", true);
                }
            }

        }
        return ApiResult.
                noObject("category not found ", false);
    }

    public ApiResult<CategoryOutDTO> edite(CategoryInDTO categoryIn, String id) {
        if (Checks.isUUID(id)) {
            UUID uuid = UUID.fromString(id);
            Optional<Category> categoryOptional =
                    categoryRepository.findById(uuid);
            if (categoryOptional.isPresent()) {
                Category category = categoryRepository.findByName(categoryIn.getName());
                if (category != null && category.getId() != uuid)
                    return ApiResult.noObject(
                            "must be not category name duplicate",
                            false);
                category = categoryOptional.get();
                category.setName(categoryIn.getName());
                Category saved = categoryRepository.
                        save(category);
                CategoryOutDTO categoryDTO = myMapper.
                        categoryToCategoryDTO(saved);
                return ApiResult.success(
                        "successfully edited",
                        categoryDTO);
            }
        }
        return ApiResult.noObject(
                "category not found",
                false);
    }
}