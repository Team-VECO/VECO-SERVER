package com.team.veco.exception.handler;

import com.team.veco.exception.ErrorResponse;
import com.team.veco.exception.exception.DuplicateMemberException;
import com.team.veco.exception.exception.MemberNotFindException;
import com.team.veco.exception.exception.PasswordNotCorrectException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MemberNotFindException.class)
    public ResponseEntity<ErrorResponse> MemberNotExistsExceptionHandler(HttpServletRequest request, HttpServletResponse response, MemberNotFindException ex){
        printExceptionMessage(request, ex, "User Exists Exception");
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(PasswordNotCorrectException.class)
    public ResponseEntity<ErrorResponse> PasswordNotCorrectExceptionHandler(HttpServletRequest request, HttpServletResponse response, PasswordNotCorrectException ex){
        printExceptionMessage(request, ex, "Password is not correct");
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ErrorResponse> DuplicateMemberExceptionHandler(HttpServletRequest request, HttpServletResponse response, DuplicateMemberException ex){
        printExceptionMessage(request, ex, "Password is not correct");
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    public void printExceptionMessage(HttpServletRequest request, Exception ex, String message){
        log.error(request.getRequestURI());
        log.error(message);
        ex.printStackTrace();
    }
}
