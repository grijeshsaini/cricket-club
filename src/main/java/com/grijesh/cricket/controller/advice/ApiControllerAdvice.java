package com.grijesh.cricket.controller.advice;

import com.grijesh.cricket.dto.ApiError;
import com.grijesh.cricket.exception.DuplicatePlayerException;
import com.grijesh.cricket.exception.PlayerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controller Advice to handle the exception
 *
 * @author Grijesh Saini
 */
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(PlayerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(PlayerNotFoundException playerNotFoundException) {
        return new ApiError(HttpStatus.NOT_FOUND.value(), playerNotFoundException.getMessage());
    }

    @ExceptionHandler(DuplicatePlayerException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDuplicateException(DuplicatePlayerException duplicatePlayerException) {
        return new ApiError(HttpStatus.CONFLICT.value(), duplicatePlayerException.getMessage());
    }

}
