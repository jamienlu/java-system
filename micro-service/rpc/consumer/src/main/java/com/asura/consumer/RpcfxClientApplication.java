package com.asura.consumer;

import com.asura.rpc.api.Filter;
import com.asura.rpc.api.LoadBalancer;
import com.asura.rpc.api.Router;
import com.asura.rpc.api.RpcfxRequest;
import com.asura.rpc.cilent.Rpcfx;
import com.asura.services.User;
import com.asura.services.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class RpcfxClientApplication {

	// 二方库
	// 三方库 lib
	// nexus, userserivce -> userdao -> user
	//

	public static void main(String[] args) {
		// 作业启动顺序 先启动服务器RpcfxServerApplication  在开启注册 最后调用RPC
		UserService userService = Rpcfx.create(UserService.class, "http://127.0.0.1:8888/");
		User user = userService.findById(1);
		System.out.println("find user id=1 from server: " + user.getName());
	}
}



