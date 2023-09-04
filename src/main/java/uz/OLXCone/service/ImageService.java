package uz.OLXCone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.OLXCone.controller.ImageController;
import uz.OLXCone.model.Ads;
import uz.OLXCone.model.Image;
import uz.OLXCone.model.User;
import uz.OLXCone.payload.ApiResult;
import uz.OLXCone.repository.AdsRepository;
import uz.OLXCone.repository.ImageRepository;
import uz.OLXCone.repository.UserRepository;
import uz.OLXCone.utils.AppConstants;
import uz.OLXCone.utils.Checks;
import uz.OLXCone.utils.CommonUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;

    public ApiResult<Object> saveForAds(MultipartFile multipartImg,
                                        String adsId) {
        if (Checks.isValidImage(multipartImg)) {
            if (Checks.isUUID(adsId)) {
                Image image;
                try {
                    image = multipartFToImage(multipartImg);
                } catch (IOException e) {
                    return ApiResult.noObject(
                            " error ", false);
                }
                Optional<Ads> adsOptional = adsRepository.
                        findById(UUID.fromString(adsId));
                if (adsOptional.isPresent()) {
                    Ads ads = adsOptional.get();
                    if (CommonUtils.isCurrentUser(ads.getUser())) {
                        Image save = imageRepository.save(image);
                        ads.setImagePath(
                                imageIdToImagePath(save.
                                        getId().
                                        toString()
                                ));
                        adsRepository.save(ads);
                        return ApiResult.noObject(
                                "successfully upload",
                                true);
                    }
                }
            } else return ApiResult.
                    noObject("Ads not found", false);
        }
        return ApiResult.noObject(
                "image extension must be jpg or jpeg or png " +
                        "image name length must be between 1 and 50  ",
                false);
    }

    public ApiResult<Object> saveForUser(MultipartFile inputImage) {
        UUID userId = CommonUtils.getCurrentUser().getId();
        if (Checks.isValidImage(inputImage)) {
                Image image;
                try {
                    image = multipartFToImage(inputImage);
                } catch (IOException e) {
                    return ApiResult.noObject(
                            " error ", false);
                }
                Optional<User> userOptional =
                        userRepository.findById(userId);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    if (CommonUtils.isCurrentUser(user)) {
                        Image save = imageRepository.save(image);
                        user.setImagePath(
                                imageIdToImagePath(save.
                                        getId().
                                        toString()
                                ));
                        userRepository.save(user);
                        return ApiResult.noObject(
                                "successfully upload",
                                true);
                    }
                }
            } else return ApiResult.
                    noObject("user not found", false);
        return ApiResult.noObject(
                "image extension must be jpg or jpeg or png " +
                        "image name length must be between 1 and 50  ",
                false);
    }

    private String imageIdToImagePath(String id) {
        return AppConstants.DOMAIN + ImageController.PATH + "/" + id;
    }

    private Image multipartFToImage(MultipartFile multipartF) throws IOException {
        Image image = new Image();
        String ext = CommonUtils.getExtension(
                multipartF.getOriginalFilename()
        );
        image.setExtension(ext);
        image.setName(multipartF.
                getOriginalFilename());
        image.setSize(multipartF.getSize());
        image.setData(multipartF.getBytes());
        image.setContentType(multipartF.getContentType());
        return image;
    }

    @Transactional(readOnly = true)
    public Image findById(String id) {
        if (Checks.isUUID(id)) {
            Optional<Image> imageOptional = imageRepository.
                    findById(UUID.fromString(id));
            if (imageOptional.isPresent()) {
                return imageOptional
                        .get();
            }
        }
        return null;
    }
}
