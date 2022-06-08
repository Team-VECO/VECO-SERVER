package com.project.mt.response;

import com.project.mt.response.result.CommonResultResponse;
import com.project.mt.response.result.ListResultResponse;
import com.project.mt.response.result.SingleResultResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {
    @AllArgsConstructor
    @Getter
    public enum CommonResponse{
        SUCCESS(true, "성공하였습니다", 200);
        private boolean success;
        private String msg;
        private int status;
    }

    public CommonResultResponse getSuccessResult(){
        return getCommonResultResponse();
    }

    public <T>SingleResultResponse<T> getSingleResult(T data){
        return new SingleResultResponse<>(getCommonResultResponse(), data);
    }

    public <T>ListResultResponse<T> getListResult(List<T> list){
        return new ListResultResponse<>(getCommonResultResponse(), list);
    }

    private CommonResultResponse getCommonResultResponse() {
        return new CommonResultResponse(CommonResponse.SUCCESS.isSuccess(), CommonResponse.SUCCESS.getStatus(), CommonResponse.SUCCESS.getMsg());
    }
}
