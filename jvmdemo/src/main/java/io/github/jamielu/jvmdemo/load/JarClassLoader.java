package io.github.jamielu.jvmdemo.load;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarClassLoader extends ClassLoader {
    public final Map<String, Class<?>> classCache = new HashMap<>();
    private final String jarFilePath;

    public JarClassLoader(String jarFilePath) {
        this.jarFilePath = jarFilePath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 检查缓存
        if (classCache.containsKey(name)) {
            return classCache.get(name);
        }

        try (JarFile jarFile = new JarFile(jarFilePath)) {
            // 将类名转换为 JAR 内的路径（注意路径分隔符）
            String classPath = name.replace('.', '/') + ".class";
            JarEntry entry = jarFile.getJarEntry(classPath);

            if (entry == null) {
                throw new ClassNotFoundException("Class not found in JAR: " + name);
            }

            // 读取类字节码
            byte[] classBytes = readClassBytes(jarFile, entry);
            Class<?> clazz = defineClass(name, classBytes, 0, classBytes.length);
            classCache.put(name, clazz);
            return clazz;
        } catch (IOException e) {
            throw new ClassNotFoundException("Failed to load class: " + name, e);
        }
    }

    private byte[] readClassBytes(JarFile jarFile, JarEntry entry) throws IOException {
        try (InputStream is = jarFile.getInputStream(entry);
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            return os.toByteArray();
        }
    }
}