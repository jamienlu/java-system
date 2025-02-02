package io.github.jamielu.juc.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jamieLu
 * @create 2025-02-02
 */
public class ThreadPoolExecutorModel {
    private int coreSize;
    private int maxCoreSize;
    private static class JamieThreadFactory implements ThreadFactory {
        private final AtomicInteger counter = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("jamie-" + counter.getAndIncrement());
            return new Thread(r);
        }
    }
    private ThreadPoolExecutor threadPoolExecutor;

    public ThreadPoolExecutorModel() {
        this(Runtime.getRuntime().availableProcessors(),true);
    }

    public ThreadPoolExecutorModel(int cpuCore, boolean ioTask) {
        coreSize = ioTask? cpuCore : cpuCore * 2;
        maxCoreSize = coreSize * 2; // 根据队列排队缓冲时间决定
        threadPoolExecutor = new ThreadPoolExecutor(coreSize, maxCoreSize,
                10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100), new JamieThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    private void finishCoreThread() {
        for (int i = 0; i < coreSize; i++) {
            threadPoolExecutor.submit(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + ": core wait 5s finish");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        System.out.println("###finishCoreThread queue:" +threadPoolExecutor.getQueue().remainingCapacity());
    }
    private void finishQueueThread() {
        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.submit(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + ": queue wait 1s finish");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        System.out.println("###finishQueueThread pool:" +threadPoolExecutor.getPoolSize());
        System.out.println("###finishQueueThread queue:" +threadPoolExecutor.getQueue().remainingCapacity());
        System.out.println("###finishQueueThread task end:" +threadPoolExecutor.getCompletedTaskCount());
        System.out.println("###finishQueueThread task:" +threadPoolExecutor.getTaskCount());
    }

    private void incurThreadFinishEnd() {
        for (int i = 0; i < maxCoreSize - coreSize; i++) {
            threadPoolExecutor.submit(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + ": add thread wait 1s finish");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        System.out.println("###incurThreadFinishEnd pool:" +threadPoolExecutor.getPoolSize());
        System.out.println("###incurThreadFinishEnd queue:" +threadPoolExecutor.getQueue().remainingCapacity());
        System.out.println("###incurThreadFinishEnd task end:" +threadPoolExecutor.getCompletedTaskCount());
        System.out.println("###incurThreadFinishEnd task:" +threadPoolExecutor.getTaskCount());
    }

    public void runPoolState () throws InterruptedException {
        finishCoreThread();
        finishQueueThread();
        Thread.sleep(2000);
        incurThreadFinishEnd();
    }

    public void waitAlive() throws InterruptedException {
        Thread.sleep(15000);
        System.out.println("###incurThreadFinishEnd pool:" +threadPoolExecutor.getPoolSize());
        System.out.println("###incurThreadFinishEnd queue:" +threadPoolExecutor.getQueue().remainingCapacity());
        System.out.println("###incurThreadFinishEnd task end:" +threadPoolExecutor.getCompletedTaskCount());
        System.out.println("###incurThreadFinishEnd task:" +threadPoolExecutor.getTaskCount());
    }
}
