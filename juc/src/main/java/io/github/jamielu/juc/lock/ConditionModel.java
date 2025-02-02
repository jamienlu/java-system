package io.github.jamielu.juc.lock;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jamieLu
 * @create 2025-01-30
 */
public class ConditionModel {
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    private final Queue<String> list = new ArrayDeque<>();

    public void produce(String msg) throws InterruptedException {
        lock.lock();
        try {
            while (list.size() == 10) {
                notFull.await();
            }
            list.offer(msg);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public void consumer() throws InterruptedException {
        lock.lock();
        try {
            while (list.isEmpty()) {
                notEmpty.await();
            }
            list.poll();
            notFull.signal();
        } finally {
            lock.unlock();
        }
    }
    public void printQueueSize() {
        System.out.println("size:" + list.size());
    }
}
