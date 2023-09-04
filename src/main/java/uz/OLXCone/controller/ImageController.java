package uz.OLXCone.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.OLXCone.model.Image;
import uz.OLXCone.payload.ApiResult;
import uz.OLXCone.service.ImageService;
import uz.OLXCone.utils.AppConstants;

import static java.net.URLEncoder.encode;
import static uz.OLXCone.utils.AppConstants.*;

@RestController
@RequestMapping(ImageController.PATH)
@RequiredArgsConstructor
@Tag(name = "Image Controller", description = "Upload image and preview image")
public class ImageController {

    public static final String PATH = AppConstants.BASE_PATH + "/image";
    public static final String PREVIEW = "/{imageId}";
    public static final String UPLOAD_ADS = "/ads/upload/{adsId}";
    public static final String UPLOAD_USER = "/user/upload";

    private final ImageService imageService;

    @PreAuthorize("hasAnyRole('" + USER + "','" + ADMIN + "','" + SUPER_ADMIN + "') ")
    @PostMapping(value = UPLOAD_ADS, consumes = {"multipart/form-data"})
    @Operation(summary = "Upload a Image for ad",
            description = "<b> User, admin, super admin can access </b> <br> " +
                    "<b>  photo format avif ,avifs ,bmp ,gif ,heic ,jpeg ,jpg ,jxl " +
                    ",pbm ,pgm ,png ,ppm, webp, xbm xpm ,should be one of them </b> <br>",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResult<Object> uploadForAds(
            @RequestParam("file")
            MultipartFile image,
            @PathVariable String adsId) {
        return imageService.saveForAds(image, adsId);
    }

    @PreAuthorize("hasAnyRole('" + USER + "','" + ADMIN + "','" + SUPER_ADMIN + "') ")
    @PostMapping(value = UPLOAD_USER, consumes = {"multipart/form-data"})
    @Operation(summary = "Upload a profile picture",
            description = "<b> User, admin, super admin can access  </b> <br> " +
                    "<b>  photo format avif ,avifs ,bmp ,gif ,heic ,jpeg ,jpg ,jxl " +
                    ",pbm ,pgm ,png ,ppm, webp, xbm xpm ,should be one of them </b> <br>",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResult<Object> uploadForUser(
            @RequestParam("file") MultipartFile image
    ) {
        return imageService.saveForUser(image);
    }

    @GetMapping(PREVIEW)
    @Operation(summary = "Get a Image",
            description = "<b> No login required </b> <br> ")
    public ResponseEntity<?> preview(@PathVariable String imageId) {
        Image image = imageService.findById(imageId);
        if (image == null)
            return new ResponseEntity<>(
                    HttpStatusCode.valueOf(404));
        byte[] imageData = image.getData();
        String headerValue = "inline :fileName \""
                + encode(image.getName());
        MediaType mediaType =
                MediaType.parseMediaType(
                        image.getContentType());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .contentType(mediaType)
                .contentLength(image.getSize())
                .body(imageData);
    }

}
