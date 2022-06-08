package com.project.mt.response.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResultResponse {
    private boolean success;
    private int code;
    private String msg;
}
