package com.project.mt.response.result;

import lombok.Getter;

import java.util.List;

@Getter
public class ListResultResponse <T> extends CommonResultResponse {
    List<T> list;

    public ListResultResponse(CommonResultResponse commonResult, List<T> list) {
        super(commonResult.isSuccess(), commonResult.getCode(), commonResult.getMsg());
        this.list = list;
    }
}
