package com.asura.nio.gateway;

import com.asura.nio.gateway.server.HttpNettyServer;

import java.util.Arrays;

public class NettyAppliction {
    public static void main(String[] args) {

        String proxyPort = System.getProperty("proxyPort","8888");
        // 多个后端url由netty代理路由
        // String proxyServers = System.getProperty("proxyServers","http://localhost:8801,http://localhost:8802,http://localhost:8803");
        String proxyServers = System.getProperty("proxyServers","https://www.baidu.com/," + "https://www.jianshu.com/," +
                "https://blog.csdn.net/," + "http://localhost:8801,http://localhost:8802,http://localhost:8803," + "abcasf1634");
        int port = Integer.parseInt(proxyPort); // netty server处理请求的端口
        System.out.println("gateway starting...");
        HttpNettyServer server = new HttpNettyServer(port, Arrays.asList(proxyServers.split(",")));
        System.out.println("gateway started success at http://localhost:" + port);
        try {
            server.run();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
