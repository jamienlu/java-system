package com.asura.rpc.api;

import java.util.List;

public interface LoadBalancer {

    String select(List<String> urls);

}
