package com.team.veco.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    public void printExceptionMessage(HttpServletRequest request, Exception ex, String message){
        log.error(request.getRequestURI());
        log.error(message);
        ex.printStackTrace();
    }
}
