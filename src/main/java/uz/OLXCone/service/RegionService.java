package uz.OLXCone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.OLXCone.component.MyMapper;
import uz.OLXCone.model.Region;
import uz.OLXCone.payload.ApiResult;
import uz.OLXCone.payload.in.RegionInDTO;
import uz.OLXCone.payload.out.RegionOutDTO;
import uz.OLXCone.repository.RegionRepository;
import uz.OLXCone.utils.Checks;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;
    private final MyMapper mapper;

    public ApiResult<Object> add(RegionInDTO regionIndto) {
        boolean exist = regionRepository
                .existsByName(regionIndto.getName());
        if (exist) return ApiResult.noObject(
                "must be not duplicate region name",
                false);
        Region region = mapper.
                regionInDTOToRegion(regionIndto);
        regionRepository.save(region);
        return ApiResult.noObject(
                "successfully saved",
                true);
    }

    public ApiResult<RegionOutDTO> edite(String id, RegionInDTO regionIn) {
        if (Checks.isUUID(id)) {
            UUID uuid = UUID.fromString(id);
            Region region = regionRepository.
                    findByName(regionIn.getName());
            if (region != null && region.getId() != uuid)
                return ApiResult.noObject(
                        "must be not duplicate Region name",
                        false);
            Optional<Region> regionOptional =
                    regionRepository.findById(uuid);
            if (regionOptional.isPresent()) {
                region = regionOptional.get();
                region.setName(regionIn.getName());
                Region saved = regionRepository.
                        save(region);
                RegionOutDTO regionOut = mapper.regionToRegionOutDTO(saved);
                return ApiResult.success(
                        "successfully saved",
                        regionOut);
            }
        }
        return ApiResult.noObject(
                "Region not found",
                false);
    }

    public ApiResult<Object> delete(String id) {
        if (Checks.isUUID(id)) {
            Optional<Region> regionOptional =
                    regionRepository.findById(UUID.fromString(id));
            if (regionOptional.isPresent()) {
                Region region = regionOptional.get();
                if (region.getDistricts().isEmpty()) {
                    regionRepository.delete(region);
                    return ApiResult.noObject(
                            "successfully deleted"
                            , true);
                } else {
                    return ApiResult.noObject(
                            "region not deleted !!! because there are " +
                                    "districts related to this region",
                            false);
                }
            }
        }
        return ApiResult.noObject(
                "region not found",
                false);
    }

    public ApiResult<List<RegionOutDTO>> getAll() {
        List<Region> regionList =
                regionRepository.findAll();
        if (regionList.isEmpty()) {
            return ApiResult.noObject(
                    "we haven't any Region",
                    false);
        }
        List<RegionOutDTO> regionOutDTOList = mapper.
                regionListToRedionOutDTOList(regionList);
        return ApiResult.noMessage(true,
                regionOutDTOList);
    }
}
