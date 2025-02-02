package io.github.jamielu.juc.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author jamieLu
 * @create 2025-01-30
 */
public class LockMain {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(8);
        testSemaphore(service);
        testCondition(service);
        testCount(service);
        mixwriteread(service);
        testStamp(service);
        service.awaitTermination(5000, TimeUnit.MILLISECONDS);
        service.shutdown();
    }
    private static void testStamp(ExecutorService service) throws InterruptedException {
        StampLockModel stampLockModel = new StampLockModel();
        for (int i = 0; i < 10; i++) {
            final int temp = i;
            stampLockModel.write(temp);
        }
        for (int i = 0; i < 10; i++) {
            final int temp = i;
            service.submit(stampLockModel::read);
            service.submit(() -> stampLockModel.write(temp));
        }
    }
    private static void mixwriteread(ExecutorService service) throws InterruptedException {
        RedWriteLockModel redWriteLockModel = new RedWriteLockModel();
        for (int i = 0; i < 10; i++) {
            final int temp = i;
            redWriteLockModel.write(temp);
        }
        for (int i = 0; i < 5; i++) {
            final int temp = i;
            service.submit(() -> {
                try {
                    redWriteLockModel.read();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            service.submit(() -> {
                try {
                    redWriteLockModel.write(temp);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }

    private static void testSemaphore(ExecutorService service) throws InterruptedException {
        SemaphoreList semaphoreList = new SemaphoreList(10);
        ;
        for (int i = 0; i < 20; i++) {
            final int temp = i;
            service.submit(() -> {
                try {
                    semaphoreList.add(temp);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        service.submit(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    semaphoreList.remove();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        service.awaitTermination(2000, TimeUnit.MILLISECONDS);
        // 防阻塞
        service.submit(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    semaphoreList.remove();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        semaphoreList.printQueueSize();
    }

    private static void testCondition(ExecutorService service) throws InterruptedException {
        ConditionModel conditionModel = new ConditionModel();
        service.submit(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    conditionModel.produce("msg" + i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        service.submit(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    conditionModel.consumer();
                    conditionModel.printQueueSize();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 消费完后再放10条防阻塞
        service.awaitTermination(2000, TimeUnit.MILLISECONDS);
        service.submit(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    conditionModel.produce("msg" + i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static void testCount(ExecutorService service) throws InterruptedException {
        CountLockModel countLockModel = new CountLockModel();
        countLockModel.runTask(service);
        countLockModel.runTaskGroup(service);
    }
}
