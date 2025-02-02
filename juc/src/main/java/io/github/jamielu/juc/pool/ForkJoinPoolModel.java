package io.github.jamielu.juc.pool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author jamieLu
 * @create 2025-02-02
 */
public class ForkJoinPoolModel {
    private ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    public void runPoolState () {
        List<Integer> num2 = new ArrayList<Integer>();
        for (int i =0; i < 10000; i++) {
            num2.add(i);
        }
        Optional<Integer> result = num2.parallelStream().reduce(Integer::sum);
        System.out.println(result.get());
        printState();
        sortTest();
        printState();
    }

    public void sortTest() {
        IntStream stream = IntStream.range(0,100000000).map(x -> new Random().nextInt(1000000));
        int[] demo = stream.toArray();
        long t1 = System.currentTimeMillis();
        int[] demo1 = Arrays.stream(demo).sorted().toArray();
        long t2 = System.currentTimeMillis();
        int[] demo2 = Arrays.stream(demo).parallel().sorted().toArray();
        long t3 = System.currentTimeMillis();
        System.out.println("t1:" + (t2-t1) + "##t2:" + (t3-t2));
    }
    public void printState() {
        System.out.println("forkJoinPool.getParallelism() = " + forkJoinPool.getParallelism());
        System.out.println("forkJoinPool.getActiveThreadCount() = " + forkJoinPool.getActiveThreadCount());
        System.out.println("forkJoinPool.getQueuedTaskCount() = " + forkJoinPool.getQueuedTaskCount());
        // 不稳定根据实际情况窃取
        System.out.println("forkJoinPool.getStealCount() = " + forkJoinPool.getStealCount());
    }
}
