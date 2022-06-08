package com.project.mt.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String msg;
    private String details;

    public ErrorResponse(ErrorCode errorCode) {
        this.msg=errorCode.getMsg();
        this.details=errorCode.getDetails();
    }
}
