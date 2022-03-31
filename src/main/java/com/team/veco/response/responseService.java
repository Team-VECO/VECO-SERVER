package com.team.veco.response;

import com.team.veco.response.result.CommonResultResponse;
import com.team.veco.response.result.ListResultResponse;
import com.team.veco.response.result.SingleResultResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class responseService {
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
