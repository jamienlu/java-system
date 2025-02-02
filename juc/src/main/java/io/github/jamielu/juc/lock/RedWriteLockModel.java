package io.github.jamielu.juc.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 写锁卡读锁
 *
 * @author jamieLu
 * @create 2025-02-01
 */
public class RedWriteLockModel {
    private final List<Integer> result = new ArrayList<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    public void write(Integer i) throws InterruptedException {
        lock.writeLock().lock();
        boolean over =false;
        try {
            if (result.size() > 10) {
                lock.readLock().lock();
                over = true;
            }
            result.add(i);
        } finally {
            lock.writeLock().unlock();
        }
        // 写读一致
        if (over) {
            System.out.println(Thread.currentThread().getName() + ":write result:" +result);
            lock.readLock().unlock();
        }
    }
    public void read() throws InterruptedException {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + ":read result:" +result);
        } finally {
            lock.readLock().unlock();
        }
    }
}
