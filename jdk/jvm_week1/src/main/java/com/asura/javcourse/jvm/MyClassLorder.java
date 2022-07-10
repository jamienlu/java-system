package com.asura.javcourse.jvm;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

public class MyClassLorder extends ClassLoader {

    public List<Class<?>> loadMyJar(String path) throws IOException, URISyntaxException {
        List<Class<?>> result = new ArrayList<>();
        File xFile = new File(JVMClassLoaderDemo.class.getClassLoader().getResource(path).toURI());
        ZipFile zipFile = new ZipFile(xFile);
        List<String> names = zipFile.stream().map(x -> x.getName()).collect(Collectors.toList());
        names.stream().forEach(name -> {
            try {
                result.add(findClass(zipFile.getInputStream(zipFile.getEntry(name)), name));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    public  Class<?> loadMyClass(String path) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        return this.findClass(inputStream,"Hello");
    }

    private final Class<?> findClass(InputStream inputStream, String name) {
        byte[] datas = input2byte(inputStream);
        byte[] target = new byte[datas.length];
        for (int i = 0; i < datas.length; i++) {
            target[i] = (byte) (255 - datas[i]);
        }
        if (datas != null) {
            return defineClass(name, target, 0, target.length);
        }
        return null;
    }

    private final byte[] input2byte(InputStream inputStream) {
        byte[] byteArray = null;
        try {
            int length = inputStream.available();
            byteArray = new byte[length];
            inputStream.read(byteArray);
            return byteArray;
        } catch (IOException e) {
            e.printStackTrace();
            return byteArray;
        } finally {
            close(inputStream);
        }
    }

    private static void close(Closeable res) {
        if (null != res) {
            try {
                res.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
