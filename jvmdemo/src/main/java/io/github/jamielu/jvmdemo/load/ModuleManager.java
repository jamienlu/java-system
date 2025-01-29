package io.github.jamielu.jvmdemo.load;

import io.github.jamielu.jvmdemo.api.ModuleApi;

import java.util.HashMap;
import java.util.Map;

public class ModuleManager {
    private final Map<String, JarClassLoader> moduleLoaders = new HashMap<>();
    private final Map<String, ModuleApi> modules = new HashMap<>();

    public void loadModule(String moduleName, String jarPath) throws Exception {
        if (moduleLoaders.containsKey(moduleName)) {
            throw new IllegalStateException("Module already loaded: " + moduleName);
        }

        // 1. 创建模块专属的 ClassLoader
        JarClassLoader loader = new JarClassLoader(jarPath);
        moduleLoaders.put(moduleName, loader);

        // 2. 加载模块入口类（假设主类名为 <模块名>.Main）
        String mainClassName = "io.github.jamielu.jvmdemo.api.ModuleRunner";
        /*Class<?> clazz = loader.loadClass(mainClassName);*/
        // 打破双亲委派强制加载
        Class<?> clazz = loader.findClass(mainClassName);

        // 3. 实例化模块并初始化
        ModuleApi module = (ModuleApi) clazz.getDeclaredConstructor().newInstance();
        module.start();
        modules.put(moduleName, module);
    }

    public void unloadModule(String moduleName) {
        ModuleApi module = modules.get(moduleName);
        if (module != null) {
            module.stop();
            modules.remove(moduleName);
            moduleLoaders.remove(moduleName);
            // 提示 JVM 进行垃圾回收（实际卸载由 GC 决定）
            System.gc();
        }
    }
}