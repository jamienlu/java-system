package com.asura.nio.gateway.thread;

import java.util.concurrent.*;

public class ThreadPool {
    private ExecutorService executorService;

    private ThreadPool() {
    }

    private static class ThreadPoolHolder {
        private static final ThreadPool threadPool = new ThreadPool();
    }

    public static ThreadPool getInstance() {
        return ThreadPoolHolder.threadPool;
    }

    public void buildCacheThreadPool() {
        executorService = Executors.newCachedThreadPool();
    }

    public void buildFixThreadPvoidvoiool(int num) {
        executorService = Executors.newFixedThreadPool(num);
    }

    public void buildFixThreadPool(int cores, long keepAliveTime, int cacheQueueSize,
        RejectedExecutionHandler handler, String name) {
        executorService = new ThreadPoolExecutor(cores, cores * 2,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(cacheQueueSize),
                new NamedThreadFactory(name), handler);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
