package com.asura.provider;

import com.asura.rpc.netty.NettyRpcServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RpcfxServerApplication {

	public static void main(String[] args) throws Exception {
		NettyRpcServer.start(8888); // 开启服务端
	}
}
