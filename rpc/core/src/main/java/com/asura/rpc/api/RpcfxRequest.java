package com.asura.rpc.api;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcfxRequest implements Serializable {
  private String serviceClass;
  private String method;
  private Object[] params;
}
