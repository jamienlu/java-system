package com.asura.provider;

import com.asura.provider.api.OrderServiceImpl;
import com.asura.provider.api.UserServiceImpl;
import com.asura.rpc.netty.NettyClient;

import java.util.Arrays;

public class ServerRegist {
    public static void main(String[] args) throws InterruptedException {
        // 注册接口实现
        new NettyClient().registServices(Arrays.asList(UserServiceImpl.class, OrderServiceImpl.class), "127.0.0.1", 8888);

    }
}
