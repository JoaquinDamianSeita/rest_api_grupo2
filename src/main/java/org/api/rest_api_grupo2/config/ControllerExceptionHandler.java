package org.api.rest_api_grupo2.config;

import org.api.rest_api_grupo2.dto.response.ValidationErrorDto;
import org.api.rest_api_grupo2.exceptions.ApiError;
import org.api.rest_api_grupo2.exceptions.UnprocessableEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);
    private static final String BAD_REQUEST = "bad_request";

    // Handler for NoHandlerFoundException route not found
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> noHandlerFoundException(NoHandlerFoundException ex) {
        ApiError apiError = new ApiError(
                "route_not_found",
                String.format("Route %s not found", ex.getRequestURL()),
                HttpStatus.NOT_FOUND.value());
        return ResponseEntity
                .status(apiError.getStatus())
                .body(apiError);
    }

    // Handler for HttpMessageNotReadableException json parse error
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<?> handleValidationExceptions(HttpMessageNotReadableException e) {
        ApiError validationErrorDto = new ApiError(
                BAD_REQUEST,"Formato invalido en la request.", HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(validationErrorDto, HttpStatus.BAD_REQUEST);
    }

    // Handler for UnknownException not handled errors
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiError> handleUnknownException(Exception e) {
        LOGGER.error("Internal error", e);

        ApiError apiError =
                new ApiError(
                        "internal_error", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    // Handler for MethodArgumentNotValidException validation errors from dtos validations
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDto> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ValidationErrorDto validationErrorDto = new ValidationErrorDto(
                BAD_REQUEST,
                ex.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList(),
                HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity
                .status(validationErrorDto.getStatus())
                .body(validationErrorDto);
    }

    // Handler for UnprocessableEntityException validation errors from services
    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ApiError> unprocessableEntityException(UnprocessableEntityException ex) {
        ApiError apiError = new ApiError(
                "unprocessable_entity",
                ex.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY.value()
        );
        return ResponseEntity
                .status(apiError.getStatus())
                .body(apiError);
    }

    // TODO: Handler for NotFoundException not found resources
    // @ExceptionHandler(NotFoundException.class)
    // public ResponseEntity<?> notFound(NotFoundException e) {
    //     LOGGER.warn("Resource not found", e);
    //     ApiError apiError =
    //            new ApiError(
    //                     "resource_not_found", e.getMessage(), HttpStatus.NOT_FOUND.value());
    //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    // }
}