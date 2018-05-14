package com.hanyangcun.response;

import com.hanyangcun.constant.StatusCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = -7027730963172108610L;

    private int code;
    private String msg;
    private T data;

    public BaseResponse(){
        this.code = StatusCode.SUCCESS.getCode();
        this.msg = StatusCode.SUCCESS.getMsg();
    }

    public BaseResponse(int code){
        this(code,"",(T)"");
    }

    public BaseResponse(int code, String msg){
        this(code,msg,(T)"");
    }

    public BaseResponse(int code,String msg,T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
