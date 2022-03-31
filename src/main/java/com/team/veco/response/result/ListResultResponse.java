package com.team.veco.response.result;

import lombok.Getter;

import java.util.List;

@Getter
public class ListResultResponse <T> extends CommonResultResponse {
    List<T> list;

    public ListResultResponse(CommonResultResponse commonResult, List<T> list) {
        super(commonResult.getSuccess(), commonResult.getCode(), commonResult.getMsg());
        this.list = list;
    }
}
