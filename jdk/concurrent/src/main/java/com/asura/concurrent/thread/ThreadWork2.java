package com.asura.concurrent.thread;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadWork2 {
    public static void main(String[] args) {
        ThreadWork2 work = new ThreadWork2();
        work.test1();
        work.test2();
        work.test3();
        work.test4();
        work.test5();
        work.test6();
        work.test7();
        work.test8();
        work.test9();
        work.test10();
        work.test11();
    }

    /**
     * 线程池方式 Callable
     */
    private void test1() {
        long start = System.currentTimeMillis();
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Future future = executorService.submit(() -> sum(1,1));
            int result = (int)future.get(10, TimeUnit.SECONDS);
            executorService.shutdown();
            System.out.println("计算时间："+ (System.currentTimeMillis()-start) + " ms");
            System.out.println("异步计算结果为："+result);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
    }
    /**
     * futureTask Callable
     */
    private void test2() {
        long start = System.currentTimeMillis();
        try {
            FutureTask futureTask = new FutureTask(() -> sum(2, 2));
            new Thread(futureTask).start();
            int result = (int)futureTask.get(10, TimeUnit.SECONDS);
            System.out.println("计算时间："+ (System.currentTimeMillis()-start) + " ms");
            System.out.println("异步计算结果为："+result);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * thread类获取返回值 + join
     */
    private void test3() {
        long start = System.currentTimeMillis();
        try {
            class ThreadSum extends Thread {
                public int result;
                private int a;
                private int b;

                public ThreadSum(int a, int b) {
                    this.a = a;
                    this.b = b;
                }

                @Override
                public void run() {
                    result = sum(a, b);
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
            ThreadSum threadSum = new ThreadSum(3,3);
            threadSum.start();
            threadSum.join();
            System.out.println("计算时间："+ (System.currentTimeMillis()-start) + " ms");
            System.out.println("异步计算结果为："+threadSum.result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * thread类获取返回值  + wait
     */
    private void test4() {
        long start = System.currentTimeMillis();
        try {
            class ThreadSum extends Thread {
                public int result;
                private int a;
                private int b;

                public ThreadSum(int a, int b) {
                    this.a = a;
                    this.b = b;
                }

                @Override
                public void run() {
                    result = sum(a, b);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                    }

                }
            }
            ThreadSum threadSum = new ThreadSum(4,4);
            threadSum.start();
            synchronized (threadSum) {
                threadSum.wait();
            }
            System.out.println("计算时间："+ (System.currentTimeMillis()-start) + " ms");
            System.out.println("异步计算结果为："+threadSum.result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * thread类获取返回值  + countDownLatch
     */
    private void test5() {
        long start = System.currentTimeMillis();
        try {
            class ThreadSum extends Thread {
                public int result;
                private int a;
                private int b;
                private CountDownLatch countDownLatch;
                public ThreadSum(int a, int b, CountDownLatch countDownLatch) {
                    this.a = a;
                    this.b = b;
                    this.countDownLatch = countDownLatch;
                }

                @Override
                public void run() {
                    result = sum(a, b);
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }

                }
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            ThreadSum threadSum = new ThreadSum(5,5, countDownLatch);
            threadSum.start();
            countDownLatch.await();
            System.out.println("计算时间："+ (System.currentTimeMillis()-start) + " ms");
            System.out.println("异步计算结果为："+threadSum.result);
        }  catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * thread类获取返回值  + CyclicBarrier
     */
    private void test6() {
        long start = System.currentTimeMillis();
        try {
            class ThreadSum extends Thread {
                public int result;
                private int a;
                private int b;
                private CyclicBarrier barrier;
                public ThreadSum(int a, int b, CyclicBarrier barrier) {
                    this.a = a;
                    this.b = b;
                    this.barrier = barrier;
                }

                @Override
                public void run() {
                    result = sum(a, b);
                    try {
                        Thread.sleep(2);
                        barrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                }
            }
            CyclicBarrier barrier  = new CyclicBarrier(2);
            ThreadSum threadSum = new ThreadSum(6,6, barrier);
            threadSum.start();
            barrier.await();
            System.out.println("计算时间："+ (System.currentTimeMillis()-start) + " ms");
            System.out.println("异步计算结果为："+threadSum.result);
        }  catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
    /**
     * thread类获取返回值  + BlockingDeque
     */
    private void test7() {
        long start = System.currentTimeMillis();
        BlockingDeque blockingDeque  = new LinkedBlockingDeque(1);
        try {
            class ThreadSum extends Thread {
                public int result;
                private int a;
                private int b;
                private BlockingDeque blockingDeque;
                public ThreadSum(int a, int b, BlockingDeque blockingDeque) {
                    this.a = a;
                    this.b = b;
                    this.blockingDeque = blockingDeque;
                }

                @Override
                public void run() {
                    result = sum(a, b);
                    try {
                        Thread.sleep(2);
                        blockingDeque.put(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
            ThreadSum threadSum = new ThreadSum(7,7, blockingDeque);
            threadSum.start();
            blockingDeque.take();
            System.out.println("计算时间："+ (System.currentTimeMillis()-start) + " ms");
            System.out.println("异步计算结果为："+threadSum.result);
        }  catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

        }
    }
    /**
     * thread类获取返回值  + Semaphore
     */
    private void test8() {
        long start = System.currentTimeMillis();
        Semaphore semaphore  = new Semaphore(1);
        try {
            class ThreadSum extends Thread {
                public int result;
                private int a;
                private int b;
                private Semaphore semaphore;
                public ThreadSum(int a, int b, Semaphore semaphore) {
                    this.a = a;
                    this.b = b;
                    this.semaphore = semaphore;
                }

                @Override
                public void run() {
                    result = sum(a, b);
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        semaphore.release();
                    }

                }
            }
            ThreadSum threadSum = new ThreadSum(8,8, semaphore);
            semaphore.acquire();
            threadSum.start();
            semaphore.acquire();
            System.out.println("计算时间："+ (System.currentTimeMillis()-start) + " ms");
            System.out.println("异步计算结果为："+threadSum.result);
        }  catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    /**
     * thread类获取返回值  + sleep预算时间
     */
    private void test9() {
        long start = System.currentTimeMillis();
        try {
            class ThreadSum extends Thread {
                public int result;
                private int a;
                private int b;
                public ThreadSum(int a, int b) {
                    this.a = a;
                    this.b = b;
                }

                @Override
                public void run() {
                    result = sum(a, b);
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                    }

                }
            }
            ThreadSum threadSum = new ThreadSum(9,9);
            threadSum.start();
            Thread.sleep(100); // 预计子线程执行时间
            System.out.println("计算时间："+ (System.currentTimeMillis()-start) + " ms");
            System.out.println("异步计算结果为："+threadSum.result);
        }  catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * CompletableFuture 异步
     */
    private void test10() {
        long start = System.currentTimeMillis();
        CompletableFuture completableFuture = CompletableFuture.supplyAsync(() -> sum(10, 10));
        try {
            int result = (int)completableFuture.get();
            System.out.println("计算时间："+ (System.currentTimeMillis()-start) + " ms");
            System.out.println("异步计算结果为："+ result);
        }  catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * thread类获取返回值  + ReentrantLock + Condition
     */
    private void test11() {
        long start = System.currentTimeMillis();
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        try {
            lock.lock();
            class ThreadSum extends Thread {
                public int result;
                private int a;
                private int b;
                public ThreadSum(int a, int b) {
                    this.a = a;
                    this.b = b;
                }

                @Override
                public void run() {
                    lock.lock();
                    try {
                        Thread.sleep(2);
                        result = sum(a, b);
                        condition.signal();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        lock.unlock();
                    }
                }
            }

            ThreadSum threadSum = new ThreadSum(11,11);
            threadSum.start();
            condition.await();
            System.out.println("计算时间："+ (System.currentTimeMillis()-start) + " ms");
            System.out.println("异步计算结果为："+threadSum.result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    private static int sum(int a, int b) {
        return a +b;
    }


}
