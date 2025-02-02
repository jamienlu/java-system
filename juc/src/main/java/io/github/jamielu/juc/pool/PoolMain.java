package io.github.jamielu.juc.pool;

import java.util.concurrent.ForkJoinPool;

/**
 * @author jamieLu
 * @create 2025-02-02
 */
public class PoolMain {
    public static void main(String[] args) throws InterruptedException {
      /*  ThreadPoolExecutorModel poolExecutorModel = new ThreadPoolExecutorModel();
        poolExecutorModel.runPoolState();
        poolExecutorModel.waitAlive();*/
        ForkJoinPoolModel ForkJoinPoolModel = new ForkJoinPoolModel();
        ForkJoinPoolModel.runPoolState();

    }
}
