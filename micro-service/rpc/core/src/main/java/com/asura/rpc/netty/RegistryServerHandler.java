package com.asura.rpc.netty;

import com.asura.rpc.api.RpcfxRequest;
import com.asura.rpc.api.RpcfxResponse;
import com.asura.rpc.hession.SerializeUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryServerHandler extends ChannelInboundHandlerAdapter {
    // 注册中心 (容器)
    public static Map<String,Object> contexts = new ConcurrentHashMap<String,Object>();
    // 类信息集合
    private static Map<String,Class> classList = new ConcurrentHashMap<String,Class>();

    private Method resolveMethodFromClass(Class<?> klass, String methodName) {
        return Arrays.stream(klass.getMethods()).filter(m -> methodName.equals(m.getName())).findFirst().get();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcfxResponse rpcfxResponse = new RpcfxResponse();
        if (msg instanceof List) {
            List<Class> classNames = (List<Class>) msg;
            classNames.forEach(clazz -> {
                Class interface0 = clazz.getInterfaces()[0];
                try {
                    classList.put(interface0.getName(), clazz);
                    contexts.put(interface0.getName(), clazz.getConstructor().newInstance());
                    System.out.println("注册服务：" + interface0.getName());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            });
            rpcfxResponse.setStatus(true);
            rpcfxResponse.setResult("regist success");
        }
        if (msg instanceof RpcfxRequest) {

            Object result = new Object();
            // 约定传输对象
            RpcfxRequest request = (RpcfxRequest) msg;
            rpcfxResponse.setName(request.getServiceClass());
            // 判断当前调用服务在容器中是否存在本对象
            if(classList.containsKey(request.getServiceClass())){
                Object clazz = contexts.get(request.getServiceClass());
                Method method = resolveMethodFromClass(classList.get(request.getServiceClass()), request.getMethod());
                result = method.invoke(clazz, request.getParams());
            }
            if(result != null){
                rpcfxResponse.setResult(result);
            }
        }
        ctx.writeAndFlush(rpcfxResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
