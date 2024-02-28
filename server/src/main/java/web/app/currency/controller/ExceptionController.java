package web.app.currency.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import web.app.currency.model.ApiError;
import web.app.currency.exceptions.GenericClientException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionController {

    private HttpStatus status;
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleApiException(Exception e) {
        if (e instanceof MalformedURLException) {
            status = HttpStatus.BAD_REQUEST;
        }
        if (e instanceof ProtocolException) {
            status = HttpStatus.CONFLICT;
        }
        if (e instanceof FileNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        }
        if (e instanceof IOException | e instanceof RuntimeException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return buildResponseEntity(new ApiError(
                status,
                e.getMessage()
        ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST,ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> details = new ArrayList<>();
        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, details));
    }

    @ExceptionHandler(GenericClientException.class)
    public ResponseEntity<ApiError> handleGenericClientException(GenericClientException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}