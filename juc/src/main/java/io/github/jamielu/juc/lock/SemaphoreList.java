package io.github.jamielu.juc.lock;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * @author jamieLu
 * @create 2025-01-30
 */
public class SemaphoreList {
    private int capacity = 10;
    private Semaphore semaphore;
    private Queue<Integer> queue = new ArrayDeque<>();

    public SemaphoreList(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        capacity = size;
        semaphore = new Semaphore(capacity);
    }
    public void add(Integer i) throws InterruptedException {
        if (queue.size() < capacity) {
            semaphore.acquire();
            queue.offer(i);
            System.out.println("add " + i + "##semaphore premier " + semaphore.availablePermits() + "##thread:" +Thread.currentThread().getId());
            semaphore.release();
        }
    }
    public Integer remove() throws InterruptedException {
        semaphore.acquire();
        Integer i = queue.poll();
        System.out.println("remove " + i + "semaphore drainPermits " + semaphore.drainPermits());
        semaphore.release();
        return i;
    }
    public void printQueueSize() {
        System.out.println("###queue size:" + queue.size());
    }
}
