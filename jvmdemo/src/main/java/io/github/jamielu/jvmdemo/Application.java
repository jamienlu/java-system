package io.github.jamielu.jvmdemo;

import io.github.jamielu.jvmdemo.load.ModuleManager;

/**
 * @author jamieLu
 * @create 2025-01-29
 */
public class Application {
    public static void main(String[] args) throws Exception{
        ModuleManager manager = new ModuleManager();
        String path = System.getProperty("user.dir") + "/modules/";
        // 加载模块
        manager.loadModule("module", path+"v1.jar");
        // 卸载模块
        Thread.sleep(2000);
        manager.unloadModule("module");
    }
}
