package com.hacker.news.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HackNewsException extends RuntimeException {

    private String errorMessage;
    private String errorCode;
    private String request;
    private String requestType;
    private String customMessage;

}
