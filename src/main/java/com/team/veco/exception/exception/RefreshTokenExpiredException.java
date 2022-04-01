package com.team.veco.exception.exception;

import com.team.veco.exception.ErrorCode;
import lombok.Getter;

@Getter
public class RefreshTokenExpiredException extends RuntimeException{
    private ErrorCode errorCode;

    public RefreshTokenExpiredException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
