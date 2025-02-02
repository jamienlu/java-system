package io.github.jamielu.juc.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * @author jamieLu
 * @create 2025-02-01
 */
public class StampLockModel {
    private final List<Integer> result = new ArrayList<>();
    private final StampedLock stampedLock = new StampedLock();

    public void read() {
        long stamp = stampedLock.tryOptimisticRead();
        System.out.println(Thread.currentThread().getName() + ":op read result" + result);
        // 有写数据 读锁升级
        if (!stampedLock.validate(stamp)) {
            stamp = stampedLock.readLock();
            System.out.println(Thread.currentThread().getName() + ":down read result" + result);
            stampedLock.unlock(stamp);
        }
    }

    public void write(Integer i) {
        long stamp = 0;
        try {
            try {
                stamp = stampedLock.tryWriteLock(1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (stamp == 0) {
                System.out.println("get lock error");
                return;
            }
            result.add(i);
        } finally {
            stampedLock.unlock(stamp);
        }
    }
}
