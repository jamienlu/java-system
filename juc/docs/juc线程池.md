# ExecutorService

>包装类线程池接口，根据实际线程池执行逻辑
# ThreadPoolExector

## 核心原理

```plain
public class ThreadPoolExecutor extends AbstractExecutorService {
    /**
     * The main pool control state, ctl, is an atomic integer packing
     * two conceptual fields
     *   workerCount, indicating the effective number of threads
     *   runState,    indicating whether running, shutting down etc
     *
     * In order to pack them into one int, we limit workerCount to
     * (2^29)-1 (about 500 million) threads rather than (2^31)-1 (2
     * billion) otherwise representable. If this is ever an issue in
     * the future, the variable can be changed to be an AtomicLong,
     * and the shift/mask constants below adjusted. But until the need
     * arises, this code is a bit faster and simpler using an int.
     *
     * The workerCount is the number of workers that have been
     * permitted to start and not permitted to stop.  The value may be
     * transiently different from the actual number of live threads,
     * for example when a ThreadFactory fails to create a thread when
     * asked, and when exiting threads are still performing
     * bookkeeping before terminating. The user-visible pool size is
     * reported as the current size of the workers set.
     *
     * The runState provides the main lifecycle control, taking on values:
     *
     *   RUNNING:  Accept new tasks and process queued tasks
     *   SHUTDOWN: Don't accept new tasks, but process queued tasks
     *   STOP:     Don't accept new tasks, don't process queued tasks,
     *             and interrupt in-progress tasks
     *   TIDYING:  All tasks have terminated, workerCount is zero,
     *             the thread transitioning to state TIDYING
     *             will run the terminated() hook method
     *   TERMINATED: terminated() has completed
     *
     * The numerical order among these values matters, to allow
     * ordered comparisons. The runState monotonically increases over
     * time, but need not hit each state. The transitions are:
     *
     * RUNNING -> SHUTDOWN
     *    On invocation of shutdown()
     * (RUNNING or SHUTDOWN) -> STOP
     *    On invocation of shutdownNow()
     * SHUTDOWN -> TIDYING
     *    When both queue and pool are empty
     * STOP -> TIDYING
     *    When pool is empty
     * TIDYING -> TERMINATED
     *    When the terminated() hook method has completed
     *
     * Threads waiting in awaitTermination() will return when the
     * state reaches TERMINATED.
     *
     * Detecting the transition from SHUTDOWN to TIDYING is less
     * straightforward than you'd like because the queue may become
     * empty after non-empty and vice versa during SHUTDOWN state, but
     * we can only terminate if, after seeing that it is empty, we see
     * that workerCount is 0 (which sometimes entails a recheck -- see
     * below).
     */
     
    // ctl维护2个变量有效线程数和线程池状态
    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int COUNT_MASK = (1 << COUNT_BITS) - 1;


    // runState is stored in the high-order bits
    // 任务运行  关闭中  关闭中拒绝正在执行的任务  任务执行完毕  关闭完成
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final int STOP       =  1 << COUNT_BITS;
    private static final int TIDYING    =  2 << COUNT_BITS;
    private static final int TERMINATED =  3 << COUNT_BITS;
  


public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          ThreadFactory threadFactory,
                          RejectedExecutionHandler handler) {
    if (corePoolSize < 0 ||
        maximumPoolSize <= 0 ||
        maximumPoolSize < corePoolSize ||
        keepAliveTime < 0)
        throw new IllegalArgumentException();
    if (workQueue == null || threadFactory == null || handler == null)
        throw new NullPointerException();
    this.corePoolSize = corePoolSize;
    this.maximumPoolSize = maximumPoolSize;
    this.workQueue = workQueue;
    this.keepAliveTime = unit.toNanos(keepAliveTime);
    this.threadFactory = threadFactory;
    this.handler = handler;
}


执行逻辑：
当线程数小于核心线程数时，创建线程。
当线程数大于等于核心线程数，且任务队列未满时，将任务放入任务队列。
当线程数大于等于核心线程数，且任务队列已满
  - 若线程数小于最大线程数，创建线程
  - 若线程数等于最大线程数，抛出异常，拒绝任务


```


## 核心方法

### 调度线程任务

```plain
public void execute(Runnable command) {
    if (command == null)
        throw new NullPointerException();
    /*
     * Proceed in 3 steps:
     *
     * 1. If fewer than corePoolSize threads are running, try to
     * start a new thread with the given command as its first
     * task.  The call to addWorker atomically checks runState and
     * workerCount, and so prevents false alarms that would add
     * threads when it shouldn't, by returning false.
     *
     * 2. If a task can be successfully queued, then we still need
     * to double-check whether we should have added a thread
     * (because existing ones died since last checking) or that
     * the pool shut down since entry into this method. So we
     * recheck state and if necessary roll back the enqueuing if
     * stopped, or start a new thread if there are none.
     *
     * 3. If we cannot queue task, then we try to add a new
     * thread.  If it fails, we know we are shut down or saturated
     * and so reject the task.
     */
    int c = ctl.get();
    // 工作线程数小于核心线程直接进入work线程队列核心线程中执行
    if (workerCountOf(c) < corePoolSize) {
        if (addWorker(command, true))
            return;
        c = ctl.get();
    }
    // 检查线程状态并进入等待队列
    if (isRunning(c) && workQueue.offer(command)) {
        int recheck = ctl.get();
        // 线程池突然关闭拒绝执行
        if (! isRunning(recheck) && remove(command))
            reject(command);
         // work线程队列中有新的空闲线程添加一个非核心线程
        else if (workerCountOf(recheck) == 0)
            addWorker(null, false);
    }
    // 等待队列满 添加进work队列失败执行拒绝策略
    else if (!addWorker(command, false))
        reject(command);
}
```


### 进入工作任务队列


```plain
private boolean addWorker(Runnable firstTask, boolean core) {
    retry:
        // 线程池是否运行状态
    for (int c = ctl.get();;) {
        // Check if queue empty only if necessary.
        if (runStateAtLeast(c, SHUTDOWN)
            && (runStateAtLeast(c, STOP)
                || firstTask != null
                || workQueue.isEmpty()))
            return false;


        for (;;) {
            // 检查工作队列数和 核心非核心需求是否一致  不一致失败
            if (workerCountOf(c)
                >= ((core ? corePoolSize : maximumPoolSize) & COUNT_MASK))
                return false;
            // cas 增加工作队列数 成功表示创建成功
            if (compareAndIncrementWorkerCount(c))
                break retry;
            c = ctl.get();  // Re-read ctl
            // 失败重新检查
            if (runStateAtLeast(c, SHUTDOWN))
                continue retry;
            // else CAS failed due to workerCount change; retry inner loop
        }
    }


    boolean workerStarted = false;
    boolean workerAdded = false;
    Worker w = null;
    try {
        //  创建和启动线程
        w = new Worker(firstTask);
        final Thread t = w.thread;
      
        if (t != null) {
            final ReentrantLock mainLock = this.mainLock;
             // 获取主线程锁 线程池和工作队列一致
            mainLock.lock();
            try {
                // Recheck while holding lock.
                // Back out on ThreadFactory failure or if
                // shut down before lock acquired.
                int c = ctl.get();
                  // 线程池在运行或添加非核心线程
                if (isRunning(c) ||
                    (runStateLessThan(c, STOP) && firstTask == null)) {                   
                    if (t.getState() != Thread.State.NEW)
                        throw new IllegalThreadStateException();
                        // 进入work队列更新最大线程数
                    workers.add(w);
                    workerAdded = true;
                    int s = workers.size();
                    if (s > largestPoolSize)
                        largestPoolSize = s;
                }
            } finally {
                mainLock.unlock();
            }
            // 添加成功启动线程
            if (workerAdded) {
                t.start();
                workerStarted = true;
            }
        }
    } finally {
      // 清理启动失败的线程
        if (! workerStarted)
            addWorkerFailed(w);
    }
    return workerStarted;
}
```


### 执行线程任务


```plain
private final class Worker
    extends AbstractQueuedSynchronizer
    implements Runnable
{
    /**
     * This class will never be serialized, but we provide a
     * serialVersionUID to suppress a javac warning.
     */
    private static final long serialVersionUID = 6138294804551838833L;


    /** Thread this worker is running in.  Null if factory fails. */
    @SuppressWarnings("serial") // Unlikely to be serializable
    final Thread thread;
    /** Initial task to run.  Possibly null. */
    @SuppressWarnings("serial") // Not statically typed as Serializable
    Runnable firstTask;
    /** Per-thread task counter */
    volatile long completedTasks;
    
work工作队列是1个实现了AQS的线程


final void runWorker(Worker w) {
    Thread wt = Thread.currentThread();
    Runnable task = w.firstTask;
    w.firstTask = null;
    w.unlock(); // allow interrupts
    boolean completedAbruptly = true;
    try {
        // 非队列线程初始任务是我们提交的任务 为空会去获取队列任务
        while (task != null || (task = getTask()) != null) {
            w.lock();
            // If pool is stopping, ensure thread is interrupted;
            // if not, ensure thread is not interrupted.  This
            // requires a recheck in second case to deal with
            // shutdownNow race while clearing interrupt
            if ((runStateAtLeast(ctl.get(), STOP) ||
                 (Thread.interrupted() &&
                  runStateAtLeast(ctl.get(), STOP))) &&
                !wt.isInterrupted())
                wt.interrupt();
            // 加锁执行任务
            try {
                // 线程池的钩子方法
                beforeExecute(wt, task);
                try {
                    // 实际任务执行
                    task.run();
                    afterExecute(task, null);
                } catch (Throwable ex) {
                    afterExecute(task, ex);
                    throw ex;
                }
            } finally {
                task = null;
                w.completedTasks++;
                w.unlock();
            }
        }
        completedAbruptly = false;
    } finally {
        // work资源回收 移除或者重新变为空闲线程
        processWorkerExit(w, completedAbruptly);
    }
}


```



# ScheduledThreadPoolExecutor

>定时线程池，继承自ThreadPoolExecutor, 封装了ScheduledExecutorService的定时操作命令
## 核心原理

```plain
public ScheduledThreadPoolExecutor(int corePoolSize,
                                   ThreadFactory threadFactory,
                                   RejectedExecutionHandler handler) {
    super(corePoolSize, Integer.MAX_VALUE,
          DEFAULT_KEEPALIVE_MILLIS, MILLISECONDS,
          new DelayedWorkQueue(), threadFactory, handler);
}


private class ScheduledFutureTask<V>
        extends FutureTask<V> implements RunnableScheduledFuture<V> {
    /** Sequence number to break ties FIFO */
    private final long sequenceNumber;
    /** The nanoTime-based time when the task is enabled to execute. */
    private volatile long time;
    /**
     * Period for repeating tasks, in nanoseconds.
     * A positive value indicates fixed-rate execution.
     * A negative value indicates fixed-delay execution.
     * A value of 0 indicates a non-repeating (one-shot) task.
     */
    private final long period;
    /** The actual task to be re-enqueued by reExecutePeriodic */
    RunnableScheduledFuture<V> outerTask = this;
    /**
     * Index into delay queue, to support faster cancellation.
     */
    int heapIndex;
    
定义了一个延迟任务类，封装任务， 
工作线程必须在正确时间才能从DelayedWorkQueue队列获取元素，无界队列最大线程无意义
```


## 执行逻辑

```plain
private void delayedExecute(RunnableScheduledFuture<?> task) {
    if (isShutdown())
        reject(task);
    else {
        // 队列添加任务
        super.getQueue().add(task);
        // 线程池不运行执行删除成功移除该任务
        if (!canRunInCurrentRunState(task) && remove(task))
            task.cancel(false);
        else
        // 工作线程处理任务
            ensurePrestart();
    }
}
// 延用work那套机制
void ensurePrestart() {
    int wc = workerCountOf(ctl.get());
    if (wc < corePoolSize)
        addWorker(null, true);
    else if (wc == 0)
        addWorker(null, false);
}


static class DelayedWorkQueue extends AbstractQueue<Runnable>
    implements BlockingQueue<Runnable> {


该线程池使用的队列是延迟队列必须等到一定时间work线程才能poll出任务
对比worker的getTask会阻塞线程到超时才能获取到真正的任务
```


# ForkJoinPool


抛弃了ThreadPoolExector设计，自行实现适用于多核并行计算的线程池

java1.8 ForkJoinPool.commonPool()内置实现，用于流并行处理


## 核心原理

### 工作队列 WorkQueue 

```plain
static final class WorkQueue {
    volatile int phase;        // versioned, negative if inactive
    int stackPred;             // pool stack (ctl) predecessor link
    int config;                // index, mode, ORed with SRC after init
    int base;                  // index of next slot for poll
    ForkJoinTask<?>[] array;   // the queued tasks; power of 2 size
    final ForkJoinWorkerThread owner; // owning thread or null if shared


    // segregate fields frequently updated but not read by scans or steals
    @jdk.internal.vm.annotation.Contended("w")
    int top;                   // index of next slot for push
    @jdk.internal.vm.annotation.Contended("w")
    volatile int source;       // source queue id, lock, or sentinel
    @jdk.internal.vm.annotation.Contended("w")
    int nsteals;               // number of steals from other queues


    // Support for atomic operations
    private static final VarHandle QA; // for array slots
    private static final VarHandle SOURCE;
    private static final VarHandle BASE;
    
线程首先从自己的队列顶部（top）弹出任务执行，优先执行任务线程任务
如果本地队列为空，则尝试从其他线程的队列底部（base）窃取任务执行。
```


### 工作线程 ForkJoinWorkerThread


```plain
public class ForkJoinWorkerThread extends Thread {
    /*
     * ForkJoinWorkerThreads are managed by ForkJoinPools and perform
     * ForkJoinTasks. For explanation, see the internal documentation
     * of class ForkJoinPool.
     *
     * This class just maintains links to its pool and WorkQueue.
     */


    final ForkJoinPool pool;                // the pool this thread works in
    final ForkJoinPool.WorkQueue workQueue; // work-stealing mechanics
    


public void run() {
    Throwable exception = null;
    ForkJoinPool p = pool;
    ForkJoinPool.WorkQueue w = workQueue;
    if (p != null && w != null) {   // skip on failed initialization
        try {
            // 当前线程的工作队列注册进参与fork和join
            p.registerWorker(w);
            // 扩展方法
            onStart();
            // 不断扫描和等待任务，确保线程始终忙碌，直到没有更多任务
            p.runWorker(w);
        } catch (Throwable ex) {
            exception = ex;
        } finally {
            try {
               // 扩展方法
                onTermination(exception);
            } catch (Throwable ex) {
                if (exception == null)
                    exception = ex;
            } finally {
                // 注销当前线程及其工作队列，释放资源
                p.deregisterWorker(this, exception);
            }
        }
    }
}


```



### 工作任务 ForkJoinTask

```plain
public final ForkJoinTask<V> fork() {
    Thread t; ForkJoinWorkerThread w;
    // 任务可以由当前线程或通过工作窃取机制由其他线程执行。
外部线程处理
    if ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread)
        (w = (ForkJoinWorkerThread)t).workQueue.push(this, w.pool);
    else
    // 任务会被推入公共 ForkJoinPool这适用于从普通线程提交任务的情况
        ForkJoinPool.common.externalPush(this);
    return this;
}


public final V join() {
    int s;
    if ((s = status) >= 0)
       //  等待完成，定期检查确保合理时间
        s = awaitDone(null, false, false, false, 0L);
    if ((s & ABNORMAL) != 0)
        reportException(s);
    return getRawResult();
}


```

### 

## 核心方法

### 窃取算法

```plain
private int scan(WorkQueue w, int prevSrc, int r) {
    WorkQueue[] qs = queues;
    int n = (w == null || qs == null) ? 0 : qs.length;
    for (int step = (r >>> 16) | 1, i = n; i > 0; --i, r += step) {
        int j, cap, b; WorkQueue q; ForkJoinTask<?>[] a;
        if ((q = qs[j = r & (n - 1)]) != null && // poll at qs[j].array[k]
            (a = q.array) != null && (cap = a.length) > 0) {
            int k = (cap - 1) & (b = q.base), nextBase = b + 1;
            int nextIndex = (cap - 1) & nextBase, src = j | SRC;
            ForkJoinTask<?> t = WorkQueue.getSlot(a, k);
            // k 是当前任务的索引位置。nextBase 和 nextIndex 分别是下一个任务的索引位置
            if (q.base != b)                // inconsistent
                return prevSrc;
            else if (t != null && WorkQueue.casSlotToNull(a, k, t)) {
                q.base = nextBase;
                ForkJoinTask<?> next = a[nextIndex];
                if ((w.source = src) != prevSrc && next != null)
                    signalWork();           // propagate
                w.topLevelExec(t, q);
                // 执行窃取的任务 t，并返回新的 src 
                return src;
            }
            else if (a[nextIndex] != null)  // revisit
                return prevSrc;
        }
    }
    return (queues != qs) ? prevSrc: -1;    // possibly resized
}


随机位置遍历，优先获取其他队列尾部


```
### 执行任务

```plain
private <T> ForkJoinTask<T> externalSubmit(ForkJoinTask<T> task) {
    Thread t; ForkJoinWorkerThread wt; WorkQueue q;
    if (task == null)
        throw new NullPointerException();
    if (((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) &&
        (q = (wt = (ForkJoinWorkerThread)t).workQueue) != null &&
        wt.pool == this)
        // forkjoin线程 直接推入该线程工作队列
        q.push(task, this);
    else
      // 推入工作池，和外部提交队列交给其他forkjoin线程完成
        externalPush(task);
    return task;
}
```


### 任务fork/join

```plain
数据拆分：Spliterator机制
所有能转Stream的数据都能通过Spliterator数据拆分


递归拆分：AbstractTask实现了compute()方法
public void compute() {
    Spliterator<P_IN> rs = spliterator, ls; // right, left spliterators
    long sizeEstimate = rs.estimateSize();
    long sizeThreshold = getTargetSize(sizeEstimate);
    boolean forkRight = false;
    @SuppressWarnings("unchecked") K task = (K) this;
    // 拆分阈值
    while (sizeEstimate > sizeThreshold && (ls = rs.trySplit()) != null) {
        K leftChild, rightChild, taskToFork;
        task.leftChild  = leftChild = task.makeChild(ls);
        task.rightChild = rightChild = task.makeChild(rs);
        task.setPendingCount(1);
        if (forkRight) {
            forkRight = false;
            rs = ls;
            task = leftChild;
            taskToFork = rightChild;
        }
        else {
            forkRight = true;
            task = rightChild;
            taskToFork = leftChild;
        }
        // 递归fork
        taskToFork.fork();
        sizeEstimate = rs.estimateSize();
    }
    task.setLocalResult(task.doLeaf());
    task.tryComplete();
}
```



