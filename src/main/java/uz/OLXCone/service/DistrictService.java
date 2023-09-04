package uz.OLXCone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.OLXCone.component.MyMapper;
import uz.OLXCone.model.District;
import uz.OLXCone.model.Region;
import uz.OLXCone.payload.ApiResult;
import uz.OLXCone.payload.in.DistrictInDTO;
import uz.OLXCone.payload.out.DistrictOutDTO;
import uz.OLXCone.repository.AdsRepository;
import uz.OLXCone.repository.DistrictRepository;
import uz.OLXCone.repository.RegionRepository;
import uz.OLXCone.utils.Checks;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DistrictService {
    private final DistrictRepository districtRepository;
    private final RegionRepository regionRepository;
    private final AdsRepository adsRepository;
    private final MyMapper mapper;


    public ApiResult<Object> add(DistrictInDTO districtDTO, String regionId) {
        if (Checks.isUUID(regionId)) {
            Optional<Region> regionOptional =
                    regionRepository.findById(UUID.
                            fromString(regionId));
            if (regionOptional.isPresent()) {
                Region region = regionOptional.get();
                boolean exists = districtRepository.
                        existsByNameAndRegionId(
                                districtDTO.getName(),
                                region.getId());
                if (exists) return ApiResult.noObject(
                        "must be not District name duplicate ",
                        false);
                District district = mapper.
                        districtInDTOToDistrict(districtDTO);
                district.setRegion(region);
                districtRepository.save(district);
                return ApiResult.noObject(
                        "successfully saved",
                        true);
            }
        }
        return ApiResult.noObject(
                "region not found ",
                false);
    }

    public ApiResult<DistrictOutDTO> edite(DistrictInDTO districtIn, String id) {
        if (Checks.isUUID(id)) {
            UUID uuid = UUID.fromString(id);
            Optional<District> districtOptional =
                    districtRepository.findById(uuid);
            if (districtOptional.isPresent()) {
                District district = districtOptional.get();
                District district1 = districtRepository.
                        findByRegionIdAndName(
                                district.getRegion().getId(),
                                districtIn.getName()
                        );
                if (district1 != null && district1.getId() != uuid)
                    return ApiResult.noObject(
                        "must be not District name duplicate ",
                        false);
                district.setName(districtIn.getName());
                District saved =
                        districtRepository.save(district);
                DistrictOutDTO districtOut =
                        mapper.districtToDistrictOutDTO(saved);
                return ApiResult.success("successfully edited",
                        districtOut);
            }
        }
        return ApiResult.noObject(
                "District not found ",
                false);
    }

    public ApiResult<Object> delete(String id) {
        if (Checks.isUUID(id)) {
            UUID uuid = UUID.fromString(id);
            Optional<District> districtOptional =
                    districtRepository.findById(uuid);
            if (districtOptional.isPresent()) {
                District district = districtOptional.get();
                boolean isExist = adsRepository.
                        existsByDistrictId(uuid);
                if (!isExist) {
                    districtRepository.delete(district);
                    return ApiResult.noObject(
                            "successfully deleted"
                            , true);
                } else {
                    return ApiResult.noObject(
                            "Districts not deleted !!! because there are " +
                                    " Ads related to this District",
                            false);
                }
            }
        }
        return ApiResult.noObject(
                "District not found ",
                false);
    }

    public ApiResult<List<DistrictOutDTO>> getByRegion(String id) {
        if (Checks.isUUID(id)) {
            List<District> districtList =
                    districtRepository.findByRegionId(UUID.fromString(id));
            if (districtList.isEmpty()) {
                return ApiResult.noObject(
                        "we haven't any District",
                        false);
            }
            List<DistrictOutDTO> outDTOList =
                    mapper.districtListToDistrictOutDTOList(districtList);
            return ApiResult.noMessage(true, outDTOList);
        }
        return ApiResult.noObject(
                " error ",
                false);
    }
}
