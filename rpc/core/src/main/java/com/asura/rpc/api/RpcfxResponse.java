package com.asura.rpc.api;

import com.asura.rpc.exception.RPCException;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
@Getter
public class RpcfxResponse implements Serializable {
    private Object result;
    private boolean status;
    private RPCException exception;
    private String name;


}
