package io.github.jamielu.juc.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * countDownLatch cyclicBarrier
 *
 * @author jamieLu
 * @create 2025-01-30
 */
public class CountLockModel {
    private CountDownLatch task = new CountDownLatch(4);
    private AtomicInteger count1 = new AtomicInteger(4);
    private AtomicInteger count2 = new AtomicInteger(5);
    private CyclicBarrier taskGroup = new CyclicBarrier(5);

    public void runTaskGroup(ExecutorService executor) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                try {
                    // 当真实线程不足5个会被全部悬挂
                    System.out.println("barrier before finish task:" +  count2.get());

                    taskGroup.await();
                    System.out.println("barrier finish task:" +  count2.decrementAndGet());
                } catch (Exception e) { /* ... */ }
            });
        }
    }

    public void runTask(ExecutorService executor) throws InterruptedException {
        for (int i = 0; i < 4; i++) {
            executor.submit(() -> {
                try {
                    task.countDown();
                    System.out.println("latch finish task:" +  count1.decrementAndGet());
                } catch (Exception e) { /* ... */ }
            });
        }
        task.await(); // 主线程等待所有子线程完成
        System.out.println("finish end:"+count1.get());
    }
}
