package io.github.jamielu.jvmdemo.gc;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * @author jamieLu
 * @create 2025-01-29
 */
public class GcMain {
    public static void main(String[] args) {
        GcObject gcObject = GcObject.builder().age(1).name("jamie").desc("test").build();
        // 打印jvm的具体参数
        System.out.println(VM.current().details());
        // 打印普通对象头信息
        System.out.println(ClassLayout.parseInstance(gcObject).toPrintable());
    }
}
