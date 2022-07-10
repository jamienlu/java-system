package com.asura.rpc.exception;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

@Getter
public class RPCException extends Exception {
    private String message;

    public RPCException(String message) {
        super(message);
        this.message = message;
    }

    public static RPCException builder(String reason, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(reason, message);
        return new RPCException(jsonObject.toString());
    }
}
