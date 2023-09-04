package uz.OLXCone.utils;

import uz.OLXCone.controller.*;

import java.util.Arrays;
import java.util.List;

public interface AppConstants {
    String BEARER = "Bearer ";
    /* Paths */
    String BASE_PATH = "api/v1";
    String DOMAIN = "http://localhost:8080/";
    String[] OPEN_PAGES = {
            AuthController.PATH + "/**",
            AdsController.PATH + AdsController.TOP,
            AdsController.PATH + AdsController.FILTER,
            AdsController.PATH + AdsController.SEARCH,
            AdsController.PATH + AdsController.GET_ALL,
            AdsController.PATH + AdsController.GET_BY_CATEGORY,
            AdsController.PATH + AdsController.GET_BY_USER,
            CategoryController.PATH + CategoryController.GET_ALL,
            CommentController.PATH + CommentController.GET_BY_ADS,
            DistrictController.PATH + DistrictController.GET_BY_REGION,
            ImageController.PATH + "/**",
            RegionController.PATH + RegionController.GET_ALL,
            "/*"
    };

    /* Roles */
    String ADMIN = "ADMIN";
    String USER = "USER";
    String SUPER_ADMIN = "SUPER_ADMIN";
    /* others */
    String DefaultImageId = "700af346-8d2e-43af-b594-dada4f799ba1";

    String DEFAULT_IMAGE_PATH = DOMAIN + ImageController.PATH +
            "/" + DefaultImageId;


    List<String> ImageFormat = Arrays.asList(
            "image/avif",
            "image/avifs",
            "image/bmp",
            "image/gif",
            "image/heic",
            "image/jpeg",
            "image/jpg",
            "image/jxl",
            "image/pbm",
            "image/pgm",
            "image/png",
            "image/ppm",
            "image/webp",
            "image/xbm",
            "image/xpm");
}