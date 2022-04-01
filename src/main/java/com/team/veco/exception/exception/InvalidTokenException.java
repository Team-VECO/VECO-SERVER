package com.team.veco.exception.exception;

import com.team.veco.exception.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidTokenException extends RuntimeException{
    private final ErrorCode errorCode;

    public InvalidTokenException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
