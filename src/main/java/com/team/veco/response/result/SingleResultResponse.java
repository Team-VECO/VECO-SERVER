package com.team.veco.response.result;

import lombok.Getter;

@Getter
public class SingleResultResponse <T> extends CommonResultResponse {
    private T data;

    public SingleResultResponse(CommonResultResponse commonResult, T data) {
        super(commonResult.isSuccess(), commonResult.getCode(), commonResult.getMsg());
        this.data = data;
    }
}
