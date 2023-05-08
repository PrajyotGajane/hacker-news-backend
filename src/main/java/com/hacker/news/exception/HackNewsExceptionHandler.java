package com.hacker.news.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HackNewsExceptionHandler {

    @ExceptionHandler(HackNewsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HackNewsException handleNullStoryIdException(RuntimeException ex, HttpServletRequest httpServletRequest) {
        HackNewsException hackNewsException = new HackNewsException();
        hackNewsException.setErrorMessage(ex.getLocalizedMessage());
        hackNewsException.setErrorCode(HttpStatus.BAD_REQUEST.toString());
        hackNewsException.setRequest(httpServletRequest.getRequestURI());
        hackNewsException.setRequestType(httpServletRequest.getMethod());
        hackNewsException.setCustomMessage("Cannot pass null or empty story id");

        return hackNewsException;
    }
}
