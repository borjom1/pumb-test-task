package com.example.task.controller;

import com.example.task.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({
            MultipartException.class,
            ConstraintViolationException.class,
            IllegalArgumentException.class
    })
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleBadRequest(@NonNull Exception e, @NonNull HttpServletRequest request) {
        return ApiError.builder()
                .status(BAD_REQUEST.value())
                .error(e.getMessage())
                .path(request.getRequestURI())
                .build();
    }

}
