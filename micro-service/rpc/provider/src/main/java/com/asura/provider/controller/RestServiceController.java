package com.asura.provider.controller;

import com.asura.provider.api.*;
import com.asura.rpc.api.RpcfxRequest;
import com.asura.rpc.api.RpcfxResolver;
import com.asura.rpc.api.RpcfxResponse;
import com.asura.rpc.service.RpcfxInvoker;
import com.asura.services.OrderService;
import com.asura.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestServiceController {
    @Autowired
    RpcfxInvoker invoker;

    @PostMapping("/")
    public RpcfxResponse invoke(@RequestBody RpcfxRequest request) {
        return invoker.invoke(request);
    }

    @Bean
    public RpcfxInvoker createInvoker(@Autowired RpcfxResolver resolver){
        return new RpcfxInvoker(resolver);
    }

    @Bean
    public RpcfxResolver createResolver(){
        return new DemoResolver();
    }

    @Bean(name = "com.asura.services.UserService")
    public UserService createUserService(){
        return new UserServiceImpl();
    }

    @Bean(name = "com.asura.services.OrderService")
    public OrderService createOrderService(){
        return new OrderServiceImpl();
    }
}
