package uz.OLXCone.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.OLXCone.payload.ApiResult;
import uz.OLXCone.payload.in.SignInDTO;
import uz.OLXCone.payload.in.SignUpDTO;
import uz.OLXCone.service.AuthService;
import uz.OLXCone.utils.AppConstants;

import java.net.URI;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Tag(name = "Auth Controller")
public class AuthController {

    public static final String PATH = AppConstants.BASE_PATH + "/auth";
    public static final String SIGN_UP = PATH + "/sign-up";
    public static final String SIGN_IN = PATH + "/sign-in";
    public static final String VERIFY = PATH + "/verification/{code}";

    private final AuthService authService;

    @PostMapping(SIGN_UP)
    @Operation(summary = "Sign up"
            , description = "<b> Sign up with email verification </b> <br> "
    )
    public ApiResult<?> signUp(@RequestBody @Valid
                               SignUpDTO signUpDTO) {
        signUpDTO.checkRePassword();
        return authService.signUp(signUpDTO);
    }

    @PostMapping(SIGN_IN)
    @Operation(summary = "Sign in",
            description = "<b> Returns a token </b> <br> "
    )
    public ApiResult<?> signIn(@RequestBody @Valid
                               SignInDTO signInDTO) {
        return authService.signIn(signInDTO);
    }

    @PostMapping(VERIFY)
    @Operation(summary = "Verify verification code")
    public ApiResult<?> verification(@PathVariable
                                     Integer code) {
        return authService.verification(code);
    }

    @GetMapping("/")
    public ResponseEntity<Void> home() {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("swagger-ui/index.html"))
                .build();
    }

}
