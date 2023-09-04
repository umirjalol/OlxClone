package uz.OLXCone.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import uz.OLXCone.payload.ApiResult;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<Object> handleValidationException(BindException ex) {
        return ApiResult.noObject(ex.getMessage(), false);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.
                getBindingResult().
                getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ApiResult.errorResponse(errors);
    }

    @ExceptionHandler(CheckException.class)
    public ApiResult<Object> handleCheckException(CheckException e) {
        return ApiResult.errorResponse(List.of(e.getMessage()));
    }

    @ExceptionHandler(value = {ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResult<Object> handlerCustomException(ConflictException e) {
        return ApiResult.errorResponse(List.of(e.getMessage()));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResult<?> handRoleAccess(AccessDeniedException ignoredE) {
        return ApiResult.errorResponse(List.of("you haven't access "));
    }

    @ExceptionHandler(value = MailException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ApiResult<?> handEmailSenderException(MailException e) {
        log.warn("Email sender exception :", e);
        return ApiResult.errorResponse(List.of("error sending email"));
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus
    public ApiResult<?> otherErrors(Exception e) {
        log.error("Defined unknown exception :", e);
        return ApiResult.noObject("some error", false);
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus
    public ApiResult<?> otherRunTimeErrors(Exception e) {
        log.error("Defined unknown RuntimeException :", e);
        return ApiResult.noObject("some error", false);
    }

    @ExceptionHandler(value = {MultipartException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> handlerCustomException(MultipartException multipartException) {
        return ApiResult.noObject(multipartException.getMessage(), false);
    }

}