package uz.OLXCone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.OLXCone.component.MyMapper;
import uz.OLXCone.model.*;
import uz.OLXCone.payload.ApiResult;
import uz.OLXCone.payload.in.AdsInDTO;
import uz.OLXCone.payload.out.AdsOutDTO;
import uz.OLXCone.payload.out.FullAdDTO;
import uz.OLXCone.repository.*;
import uz.OLXCone.utils.Checks;
import uz.OLXCone.utils.CommonUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdsService {

    //region beans
    private final MyMapper myMapper;
    private final AdsRepository adsRepository;
    private final RegionRepository regionRepository;
    private final DistrictRepository districtRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;

    //endregion
    public ApiResult<?> delete(String id) {
        if (Checks.isUUID(id)) {
            UUID adsId = UUID.fromString(id);
            Optional<Ads> adsOptional =
                    adsRepository.findById(adsId);
            if (adsOptional.isPresent()) {
                User user = adsOptional.get().getUser();
                if(CommonUtils.isCurrentUser(user)) {
                    commentRepository.deleteByAds_Id(adsId);
                    adsRepository.deleteById(adsId);
                    return ApiResult.noObject(
                            "successfully deleted ",
                            true);
                }
            }
        }
        return ApiResult.noObject(
                "ads not found",
                false);
    }

    public ApiResult<?> add(String regionId, String districtId,
                            String categoryId, AdsInDTO adsInDTO) {
        Boolean[] areValidParam = new Boolean[1];
        StringBuilder msg = areValidParameters(
                areValidParam, regionId,
                districtId, categoryId);
        if (!areValidParam[0]) return ApiResult.
                noObject(msg.toString(), false);
        msg = new StringBuilder(1);
        findBlock:
        {
            Optional<Region> regionOptional =
                    regionRepository.findById(
                            UUID.fromString(regionId));
            if (regionOptional.isEmpty()) {
                msg.append(" region not found ");
                break findBlock;
            }
            Optional<District> districtOptional =
                    districtRepository.findById(
                            UUID.fromString(districtId));
            if (districtOptional.isEmpty()) {
                msg.append(" district not found ");
                break findBlock;
            }
            Optional<Category> categoryOptional =
                    categoryRepository.findById(
                            UUID.fromString(categoryId));
            if (categoryOptional.isEmpty()) {
                msg.append(" category not found ");
                break findBlock;
            }
            Ads ads = myMapper.AdsInDTOToAds(adsInDTO);
            ads.setRegion(regionOptional.get());
            ads.setDistrict(districtOptional.get());
            ads.setCategory(categoryOptional.get());
            ads.setUser(CommonUtils.getCurrentUser());
            adsRepository.save(ads);
            return ApiResult.noObject(
                    "successfully added",
                    true);
        }
        return ApiResult.
                noObject(msg.toString(), false);
    }

    public ApiResult<AdsOutDTO> makeActive(String id) {
        if (Checks.isUUID(id)) {
            Optional<Ads> adsOptional =
                    adsRepository.findById(UUID.fromString(id));
            if (adsOptional.isPresent()) {
                Ads ads = adsOptional.get();
                if(CommonUtils.isCurrentUser(ads.getUser())) {
                    ads.setIsActive(true);
                    adsRepository.save(ads);
                    AdsOutDTO res = myMapper.AdsToAdsOutDTO(ads);
                    return ApiResult.success(
                            "successfully Active "
                            , res);
                }
            }
        }
        return ApiResult.noObject(
                "ads  not found",
                false);
    }

    public ApiResult<AdsOutDTO> makeDisable(String id) {
        if (Checks.isUUID(id)) {
            Optional<Ads> adsOptional =
                    adsRepository.findById(UUID.fromString(id));
            if (adsOptional.isPresent()) {
                Ads ads = adsOptional.get();
                if(CommonUtils.isCurrentUser(ads.getUser())) {
                    ads.setIsActive(false);
                    adsRepository.save(ads);
                    AdsOutDTO res = myMapper.AdsToAdsOutDTO(ads);
                    return ApiResult.success("successfully Disable ", res);
                }
            }
        }
        return ApiResult.noObject(
                "ads  not found",
                false);
    }

    //region get
    public ApiResult<?> getAll(Integer page, Integer size) {
        PageRequest pageable = CommonUtils.getPageable(page, size);
        Page<Ads> adsPage = adsRepository.
                findAllByIsActiveTrueOrderByCreatedAtDesc(pageable);
        if (adsPage.isEmpty()) {
            return ApiResult.noObject(
                    "ads not found",
                    false);
        }
        Page<AdsOutDTO> adsOutDTOPage =
                adsPage.map(myMapper::AdsToAdsOutDTO);
        return ApiResult.noMessage(true, adsOutDTOPage);
    }

    public ApiResult<?> getByCategory(String id, Integer page, Integer size) {
        if (Checks.isUUID(id)) {
            PageRequest pageable = CommonUtils.getPageable(page, size);
            Page<Ads> adsPage = adsRepository.
                    findByIsActiveTrueAndCategoryIdOrderByCreatedAtDesc(pageable,
                            UUID.fromString(id));
            if (!adsPage.isEmpty()) {
                Page<AdsOutDTO> adsOutDTOPage =
                        adsPage.map(myMapper::AdsToAdsOutDTO);
                return ApiResult.noMessage(
                        true, adsOutDTOPage);
            }
        }
        return ApiResult.noObject(
                "ads not found",
                false);
    }

    public ApiResult<?> getTop() {
        long count = adsRepository.
                countAllByIsActiveTrue();
        int limit = (int) Math.sqrt((double) count / 16 + 1);
        int page = (int) ((Math.random() * (limit + 10) % limit));
        PageRequest pageRequest =
                PageRequest.of(page, 16);
        Page<Ads> adsPage = adsRepository.
                findAllByIsActiveTrueOrderByCreatedAtDesc(pageRequest);
        Page<AdsOutDTO> adsOutDTOPage =
                adsPage.map(myMapper::AdsToAdsOutDTO);
        return ApiResult.noMessage(
                true, adsOutDTOPage);
    }

    public ApiResult<?> getByUserId(String userId, Integer page, Integer size) {
        if (Checks.isUUID(userId)) {
            PageRequest pageable = CommonUtils.getPageable(page, size);
            Page<Ads> adsPage = adsRepository.
                    findByIsActiveTrueAndUserIdOrderByCreatedAtDesc(pageable,
                            UUID.fromString(userId));
            if (!adsPage.isEmpty()) {
                Page<AdsOutDTO> adsOutDTOPage =
                        adsPage.map(myMapper::AdsToAdsOutDTO);
                return ApiResult.noMessage(
                        true, adsOutDTOPage);
            }
        }
        return ApiResult.noObject(
                "ads not found",
                false);
    }
    //endregion

    public ApiResult<?> filter(String category, String key,
                               String region, String district,
                               Double maxPrice, Double minPrice,
                               Integer page, Integer size) {
        boolean isValid = Checks.checkFilterParam(
                category, key, region,
                district, maxPrice, minPrice);
        if (isValid) {
            Page<Ads> result = adsRepository.filter(
                    category, key, region, district, maxPrice, minPrice,
                    CommonUtils.getPageable(page, size)
            );
            if (!result.isEmpty()) {
                Page<AdsOutDTO> dtoPage = result.map(myMapper::AdsToAdsOutDTO);
                return ApiResult.noMessage(true, dtoPage);
            }
        }
        return ApiResult.noObject("not fount", false);
    }

    public ApiResult<?> search(String query, Integer page, Integer size) {
        if (query.isBlank() || query.isEmpty())
            return getAll(page, size);
        Page<Ads> adsPage = adsRepository.
                searching(query, page, size);
        if (adsPage.isEmpty())
            return ApiResult.noObject(
                "ads not found",
                false);
        Page<AdsOutDTO> adsOutDTOPage =
                adsPage.map(myMapper::AdsToAdsOutDTO);
        return ApiResult.noMessage(
                true,
                adsOutDTOPage);
    }

    //region other
    private void increaseViews(Ads ads) {
        ads.setViewCount(ads.getViewCount() + 1);
        adsRepository.save(ads);
    }

    private StringBuilder areValidParameters(Boolean[] areValidParam,
                                             String regionId,
                                             String districtId,
                                             String categoryId) {
        areValidParam[0] = true;
        StringBuilder sb = new StringBuilder(1);
        if (!Checks.isUUID(regionId)) {
            areValidParam[0] = false;
            sb.append(" region not found");
        }
        if (!Checks.isUUID(districtId)) {
            areValidParam[0] = false;
            sb.append(" district not found");
        }
        if (!Checks.isUUID(categoryId)) {
            areValidParam[0] = false;
            sb.append(" category not found");
        }
        return sb;
    }

    public ApiResult<?> getFullAd(String id) {
        if (Checks.isUUID(id)) {
            Ads ad = adsRepository.findByIdAndIsActiveTrue(UUID.fromString(id));
            if (ad != null) {
                FullAdDTO adDTO = myMapper.AdsToFullAdDTO(ad);
                increaseViews(ad);
                return ApiResult.noMessage(true, adDTO);
            }
        }
        return ApiResult.noObject(
                "ads not found",
                false);
    }
    //endregion
}