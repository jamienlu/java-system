package com.asura.nio.gateway.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
    
    private final ThreadGroup group;
    private final AtomicInteger threadNum = new AtomicInteger(1);
    private final String name;

    public NamedThreadFactory(String name) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        this.name = name;
    }
    
    @Override
    public Thread newThread(Runnable runnable) {
        return new Thread(group, runnable, name + "-thread-" + threadNum.getAndIncrement(), 0);
    }
}