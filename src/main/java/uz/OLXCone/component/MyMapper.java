package uz.OLXCone.component;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.OLXCone.model.*;
import uz.OLXCone.payload.in.SignUpDTO;
import uz.OLXCone.payload.in.*;
import uz.OLXCone.payload.out.*;

import java.util.List;


@Mapper( componentModel = "spring")
public interface MyMapper {

    List<CategoryOutDTO> categoryListToCategoryDTOList(List<Category> categories);

    Category categoryInDTOToCategory(CategoryInDTO categoryInDTO);


    CategoryOutDTO categoryToCategoryDTO(Category category);

    Region regionInDTOToRegion(RegionInDTO regionIndto);

    RegionOutDTO regionToRegionOutDTO(Region region);

    List<RegionOutDTO> regionListToRedionOutDTOList(List<Region> regionList);

    District districtInDTOToDistrict(DistrictInDTO districtDTO);

    DistrictOutDTO districtToDistrictOutDTO(District district);

    List<DistrictOutDTO> districtListToDistrictOutDTOList(List<District> districtList);

    User signUpDTOToUser(SignUpDTO signUpDTO);
    Ads AdsInDTOToAds(AdsInDTO adsInDTO);

    @Mapping(target ="region.districts" ,ignore = true )
    @Mapping(target ="category.categoryList" ,ignore = true )
    AdsOutDTO AdsToAdsOutDTO(Ads ads);

    Comment commentInDTOToComment(CommentInDTO commentInDTO);

    CommentOutDTO commentToCommentOutDTO(Comment comment);

    UserInfoDTO userToUserInfoDTO(User user);

    FullAdDTO AdsToFullAdDTO(Ads ad);

    List<FullAdDTO> AdsListToFullAdDTO(List<Ads> adsList);
}
