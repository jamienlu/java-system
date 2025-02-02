package io.github.jamielu.juc.atomic;

import lombok.Data;
import lombok.ToString;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.DoubleAccumulator;

/**
 * @author jamieLu
 * @create 2025-01-30
 */
public class AtomicMain {
    private static final AtomicInteger atomicInteger = new AtomicInteger(0);
    private static final AtomicReference<KV> atomicReference = new AtomicReference();
    @Data
    private static class KV {
        private int key;
        private int value;
        public KV(int key, int value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "KV{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(4);
        final KV kv = new KV(10, 10);
        atomicReference.set(kv);
        for (int i = 0; i < 10; i++) {
            final int temp = i;
            service.submit(() -> {
                System.out.println(">>"+atomicInteger.getAndIncrement());
                // 随机第一次提交生效
                System.out.println(">>"+atomicReference.compareAndSet(kv, new KV(temp, temp)));
            });
        }
        service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        service.shutdown();
        System.out.println("###"+atomicInteger.get());
        System.out.println("###"+atomicReference.get().toString());
        DoubleAccumulator doubleAccumulator = new DoubleAccumulator((left,right) -> (left + right)/2,-100.0);
        doubleAccumulator.accumulate(10);
        System.out.println("###"+doubleAccumulator.get());
        doubleAccumulator.accumulate(20);
        System.out.println("###"+doubleAccumulator.get());
    }
}
