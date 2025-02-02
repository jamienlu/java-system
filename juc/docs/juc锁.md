# synchronized 

>参考JMM规范
# Semaphore

```plain
限制允许多少个线程获得许可，基于AQS的共享锁做


public Semaphore(int permits, boolean fair) {
    sync = fair ? new FairSync(permits) : new NonfairSync(permits);
}


```
用于并发线程控制的协作

# Condition

AQS内部类

```plain
public class ConditionObject implements Condition, java.io.Serializable {
    /** First node of condition queue. */
    private transient ConditionNode firstWaiter;
    /** Last node of condition queue. */
    private transient ConditionNode lastWaiter;
    
单向链表的等待队列 存储await线程


```


AQS同步队列用于管理锁竞争,节点转移到同步队列重新参与锁竞争

用于线程双向交互协作，互相阻塞唤醒（生产者消费者模式）




# CountDownLatch

同样基于AQS实现，增加了一个计数器

```plain
CountDownLatch {
    /**
     * Synchronization control For CountDownLatch.
     * Uses AQS state to represent count.
     */
    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 4982264981922014374L;


        Sync(int count) {
            setState(count);
        }
        
```


```plain
用于线程协作，当指定数量的线程解锁count=0，await才打开,适合主任务等待子任务
```


# CyclicBarrier

```plain
基于ReentrantLock 和Condition实现，一组count线程阻塞完成后继续执行，
相比CountDownLatch线程可以回收重复使用


public class CyclicBarrier {
    /** The lock for guarding barrier entry */
    private final ReentrantLock lock = new ReentrantLock();
    /** Condition to wait on until tripped */
    private final Condition trip = lock.newCondition();
    /** The number of parties */
    private final int parties;
    /** The command to run when tripped */
    private final Runnable barrierCommand;
    /** The current generation */
    private Generation generation = new Generation();
    /**
     * Number of parties still waiting. Counts down from parties to 0
     * on each generation.  It is reset to parties on each new
     * generation or when broken.
     */
    private int count;
 
```


适合任务多阶段协助


# Lock

```plain
锁接口，加锁，解锁，创建条件变量等方法
```
#  LockSupport

 线程证书模型，线程绑定证书完成加锁解锁的过程

```plain
public static void unpark(Thread thread) {
    if (thread != null)
        U.unpark(thread);
}
public static void park(Object blocker) {
    Thread t = Thread.currentThread();
    setBlocker(t, blocker);
    U.park(false, 0L);
    setBlocker(t, null);
}


Unsafe实现，依赖操作系统pthread_mutex pthread_cond 互斥量条件变量。
```

# 


# ReentrantLock

完善了AQS的模板方法，独占模式实现公平锁和非公平锁

# ReentrantReadWriteLock

可重入的读写锁实现，读共享，写独占

```plain
public class ReentrantReadWriteLock
        implements ReadWriteLock, java.io.Serializable {
    private static final long serialVersionUID = -6992448646407690164L;
    /** Inner class providing readlock */
    private final ReentrantReadWriteLock.ReadLock readerLock;
    /** Inner class providing writelock */
    private final ReentrantReadWriteLock.WriteLock writerLock;
    /** Performs all synchronization mechanics */
    final Sync sync;
    /**
     * Synchronization implementation for ReentrantReadWriteLock.
     * Subclassed into fair and nonfair versions.
     */
    abstract static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 6317671515068378041L;


        /*
         * Read vs write count extraction constants and functions.
         * Lock state is logically divided into two unsigned shorts:
         * The lower one representing the exclusive (writer) lock hold count,
         * and the upper the shared (reader) hold count.
         */
        static final int SHARED_SHIFT   = 16;
        static final int SHARED_UNIT    = (1 << SHARED_SHIFT);
        static final int MAX_COUNT      = (1 << SHARED_SHIFT) - 1;
        static final int EXCLUSIVE_MASK = (1 << SHARED_SHIFT) - 1;
      
```


AQS 的 state 被拆分为高16位（读锁计数）和低16位（写锁计数）

应对读多写少场景，支持先持有写锁在持有读锁，释放写锁后任然有读锁，避免并发写



# StampedLock

ReentrantReadWriteLock的优化版！沿用了AQS的CLH队列抛弃了模板方法

实现了一套乐观读、悲观读、写 3中方式的线程等待机制


```plain
/** The number of bits to use for reader count before overflowing */
private static final int LG_READERS = 7; // 127 readers


// Values for lock state and stamp operations
private static final long RUNIT = 1L;
// 写锁是否被持有
private static final long WBIT  = 1L << LG_READERS;
// 读锁数量掩码
private static final long RBITS = WBIT - 1L;
// 读锁容量126
private static final long RFULL = RBITS - 1L;
// 锁模式掩码
//(state & ABITS) == 0：无锁状态。
//(state & ABITS) == WBIT：写锁被持有。
//(state & ABITS) <= RFULL：读锁被持有（数量 ≤ 126）
private static final long ABITS = RBITS | WBIT;
//  版本号掩码 乐观锁
private static final long SBITS = ~RBITS; // note overlap with ABITS
// not writing and conservatively non-overflowing
private static final long RSAFE = ~(3L << (LG_READERS - 1));
/*
 * 3 stamp modes can be distinguished by examining (m = stamp & ABITS):
 * write mode: m == WBIT
 * optimistic read mode: m == 0L (even when read lock is held)
 * read mode: m > 0L && m <= RFULL (the stamp is a copy of state, but the
 * read hold count in the stamp is unused other than to determine mode)
 *
 * This differs slightly from the encoding of state:
 * (state & ABITS) == 0L indicates the lock is currently unlocked.
 * (state & ABITS) == RBITS is a special transient value
 * indicating spin-locked to manipulate reader bits overflow.
 */
/** Initial value for lock state; avoids failure value zero. */
private static final long ORIGIN = WBIT << 1;
// Special value from cancelled acquire methods so caller can throw IE
private static final long INTERRUPTED = 1L;
// Bits for Node.status
static final int WAITING   = 1;
static final int CANCELLED = 0x80000000; // must be negative


// views
transient ReadLockView readLockView;
transient WriteLockView writeLockView;
transient ReadWriteLockView readWriteLockView;


/** Lock sequence/state */
private transient volatile long state;


```


基于state变量，通过位操作实现不通锁模式，不可重入，读锁会升级


