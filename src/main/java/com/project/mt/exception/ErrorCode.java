package com.project.mt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN_ERROR(500, "Unknown Error", ErrorClassification.SERVER + "-ERR-500"),
    BAD_REQUEST(400, "Bad Request", ErrorClassification.COMMON+"-ERR-400"),
    UNAUTHORIZED(401, "Unauthorized", ErrorClassification.COMMON+"-ERR-401"),
    FORBIDDEN(403, "Forbidden", ErrorClassification.COMMON+"-ERR-403"),
    ACCESS_TOKEN_EXPIRED(403, "AccessToken is expired", ErrorClassification.MEMBER+"-ERR403"),
    REFRESH_TOKEN_EXPIRED(403, "RefreshToken is expired", ErrorClassification.MEMBER+"-ERR403"),
    MEMBER_NOT_FIND(404, "Member can't find", ErrorClassification.MEMBER+"-ERR-404"),
    DUPLICATE_MEMBER(400, "This member is duplicate", ErrorClassification.MEMBER+"-ERR-400"),
    PASSWORD_NOT_CORRECT(403, "Password isn't correct", ErrorClassification.MEMBER+"-ERR-403"),
    ;
    private int status;
    private String msg;
    private String details;
}
