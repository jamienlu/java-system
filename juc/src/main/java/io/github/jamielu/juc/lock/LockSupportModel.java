package io.github.jamielu.juc.lock;

import java.util.concurrent.locks.LockSupport;

/**
 * @author jamieLu
 * @create 2025-02-01
 */
public class LockSupportModel {
    public static void testLockSupport() throws InterruptedException {
        /**
         * 1.LockSupport unpark和park无先后关系
         * 2.unpark只能唤醒传入的park线程
         */
        Thread otherThread = new Thread(() -> {
            Thread thread = Thread.currentThread();
            System.out.println(thread.getName() + ":prepare lock1");
            LockSupport.park();
            System.out.println(thread.getName() + ":lock already cancle1");
        });
        otherThread.start();
        Thread childThread = new Thread(() -> {
            Thread thread = Thread.currentThread();
            System.out.println(thread.getName() + ":prepare lock2");
            LockSupport.park();
            System.out.println(thread.getName() + ":lock already cancle2");
        });
        childThread.start();
        LockSupport.unpark(childThread);
        LockSupport.unpark(Thread.currentThread());
        Thread.sleep(5000);
        LockSupport.unpark(otherThread);
    }

    public static void main(String[] args) throws InterruptedException {
        testLockSupport();
    }
}

