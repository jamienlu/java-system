package com.asura.concurrent.thread;

import java.util.concurrent.*;

public class ThreadWork3 {
    public static void main(String[] args) {
        new ThreadWork3().testSemaphoreApi();
        new ThreadWork3().testCountDownLatchApi();
        new ThreadWork3().testCyclicBarrierApi();
    }

    public void testExecutorsApi() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2); // 无法应对不确定场景
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool(); // 自由扩展-使用不确定场景，扩展有额外开销
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor(); // 只能单线程
        ExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(12); // 使用延时队列
        fixedThreadPool.execute(() -> {
            System.out.println(1);
        });
        fixedThreadPool.shutdown();
        Future future = cachedThreadPool.submit(() -> 1);
        future.get(5, TimeUnit.SECONDS);
        cachedThreadPool.shutdownNow();
        singleThreadExecutor.shutdown();
        scheduledExecutorService.shutdownNow();
    }
    /**
     * 多线程协作  可分权不足蜕化成串行
     */
    public void testSemaphoreApi() {
        final Semaphore teacher = new Semaphore(3);
        final Semaphore student = new Semaphore(5);
        final Semaphore demo = new Semaphore(2);
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool.execute(() -> teach(teacher, student));
        cachedThreadPool.execute(() -> showStudent(student, demo));
        cachedThreadPool.execute(() -> showTeacher(teacher, demo));
        cachedThreadPool.execute(() -> showStudent(student, demo));
        cachedThreadPool.execute(() -> showTeacher(teacher, demo));
        cachedThreadPool.execute(() -> teach(teacher, student));
        cachedThreadPool.shutdown();
    }
    /**
     * 多线程协作  各个子线程干完了活才可以进行下一步 countDown用于子线程完城递减操作主线程await
     */
    public void testCountDownLatchApi() {
        long time = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(6);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(6);
        try {
            for (int i=0;i <6; i++) {
                fixedThreadPool.execute(() -> countDownLatch.countDown());
            }
            countDownLatch.await();
            fixedThreadPool.shutdown();
            System.out.println("cost:" + (System.currentTimeMillis() - time));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同CountDownLatch 子线程阻塞
     */
    public void testCyclicBarrierApi() {
        long time = System.currentTimeMillis();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(6);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(6);
        for (int i=0;i <6; i++) {
            fixedThreadPool.execute(() -> {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        System.out.println("cost:" + (System.currentTimeMillis() - time));
        fixedThreadPool.shutdown();
    }

    public void teach(Semaphore teacher, Semaphore student) {
        try {
            teacher.acquire();
            student.acquire(3);
            Thread.sleep(1000);
            System.out.println("teach");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            teacher.release();
            student.release(2);
        }
    }

    public void showTeacher(Semaphore teacher, Semaphore demo) {
        try {
            teacher.acquire();
            demo.acquire();
            Thread.sleep(1000);
            System.out.println("showTeacher");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            teacher.release();
            demo.release();
        }
    }

    public void showStudent(Semaphore student, Semaphore demo) {
        try {
            student.acquire();
            demo.acquire();
            Thread.sleep(1000);
            System.out.println("showStudent");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            student.release();
            demo.release();
        }
    }


}
