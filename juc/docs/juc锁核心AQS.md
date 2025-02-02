>线程同步核心类，定义模板方法实现线程排队、阻塞和唤醒
# 实现原理

## CLH队列

```plain
abstract static class Node {
    volatile Node prev;       // initially attached via casTail
    volatile Node next;       // visibly nonnull when signallable
    Thread waiter;            // visibly nonnull when enqueued
    volatile int status;      // written by owner, atomic bit ops by others


    // methods for atomic operations
    final boolean casPrev(Node c, Node v) {  // for cleanQueue
        return U.weakCompareAndSetReference(this, PREV, c, v);
    }
    final boolean casNext(Node c, Node v) {  // for cleanQueue
        return U.weakCompareAndSetReference(this, NEXT, c, v);
    }
    final int getAndUnsetStatus(int v) {     // for signalling
        return U.getAndBitwiseAndInt(this, STATUS, ~v);
    }
    final void setPrevRelaxed(Node p) {      // for off-queue assignment
        U.putReference(this, PREV, p);
    }
    final void setStatusRelaxed(int s) {     // for off-queue assignment
        U.putInt(this, STATUS, s);
    }
    final void clearStatus() {               // for reducing unneeded signals
        U.putIntOpaque(this, STATUS, 0);
    }


    private static final long STATUS
        = U.objectFieldOffset(Node.class, "status");
    private static final long NEXT
        = U.objectFieldOffset(Node.class, "next");
    private static final long PREV
        = U.objectFieldOffset(Node.class, "prev");
}


双向链表封装等待线程，status表示线程状态


JDK8定义在Node内
static final int CANCELLED =  1;   // 线程已取消等待
static final int SIGNAL    = -1;   // 后继节点需被唤醒
static final int CONDITION = -2;   // 节点在条件队列中等待
static final int PROPAGATE = -3;   // 共享模式下唤醒需传播


JDK17定义在外部 JDK14精简了代码
// Node status bits, also used as argument and return values
static final int WAITING   = 1;          // must be 1
static final int CANCELLED = 0x80000000; // must be negative
static final int COND      = 2;          // in a condition wait


1表示线程等待队列处理，负数表示不处理随后会移除，2表示条件队列处理，0表示未阻塞
```


# 核心流程

### 获取资源

```plain
/**
* node condition条件时为aqs节点否则为null
* arg 透传 ReentrantLock做重入 Semaphore做许可数
* shared 区分共享和独占
* interruptible 是否忽略线程中断信号
* timed 开关
* time 线程阻塞最长计时
* return 超时0  中断返回-
*/
final int acquire(Node node, int arg, boolean shared,
                  boolean interruptible, boolean timed, long time) {
                
    Thread current = Thread.currentThread();
    byte spins = 0, postSpins = 0;   // retries upon unpark of first thread
    boolean interrupted = false, first = false;
    Node pred = null;                // predecessor of node when enqueued


    /*
     * Repeatedly:
     *  Check if node now first
     *    if so, ensure head stable, else ensure valid predecessor
     *  if node is first or not yet enqueued, try acquiring
     *  else if node not yet created, create it
     *  else if not yet enqueued, try once to enqueue
     *  else if woken from park, retry (up to postSpins times)
     *  else if WAITING status not set, set and retry
     *  else park and clear WAITING status, and check cancellation
     */
    
    for (;;) {
        // 1. clh队头节点检查，保证前置节点正常
        if (!first && (pred = (node == null) ? null : node.prev) != null &&
            !(first = (head == pred))) {
            if (pred.status < 0) {
                cleanQueue();           // predecessor cancelled
                continue;
                // 节点是第一个阻塞节点 自旋
            } else if (pred.prev == null) {
                Thread.onSpinWait();    // ensure serialization
                continue;
            }
        }
        // 2.不同操作模式 共享锁
        if (first || pred == null) {
            boolean acquired;
            try {
                if (shared)
                    acquired = (tryAcquireShared(arg) >= 0);
                else
                    acquired = tryAcquire(arg);
            } catch (Throwable ex) {
                cancelAcquire(node, interrupted, false);
                throw ex;
            }
            // 操作申请成功
            if (acquired) {
                // 第一个有效阻塞节点设为头节点
                if (first) {
                    node.prev = null;
                    head = node;
                    pred.next = null;
                    node.waiter = null;
                    // 共享唤醒后续节点
                    if (shared)
                        signalNextIfShared(node);
                    if (interrupted)
                        current.interrupt();
                }
                return 1;
            }
        }
        // 3. 正确的从clh尾部添加本次节点
        // A 本次操作权处理的Node节点还没有被创建，则创建这个Node节点
        if (node == null) {                 // allocate; retry before enqueue
            if (shared)
                node = new SharedNode();
            else
                node = new ExclusiveNode();
         //  B 节点没有尾插CLH队列  线程竞争
        } else if (pred == null) {          // try to enqueue
            node.waiter = current;
            Node t = tail;
            node.setPrevRelaxed(t);         // avoid unnecessary fence
            // 前置为null 初始化后重试
            if (t == null)
                tryInitializeHead();
            // 当前节点设置为尾节点 失败前置设为null重试
            else if (!casTail(t, node))
                node.setPrevRelaxed(null);  // back out
            // 节点入队成功
            else
                t.next = node;
          // C 这个Node节点对应的线程，在操作权获取过程中进入过阻塞状态且刚刚解除，则重试
        } else if (first && spins != 0) {
            --spins;                        // reduce unfairness on rewaits
            Thread.onSpinWait();
         // D 节点wait失败 设置为wait
        } else if (node.status == 0) {
            node.status = WAITING;          // enable signal and recheck
         // E 节点需要被继续阻塞
         } else {
            long nanos;
            spins = postSpins = (byte)((postSpins << 1) | 1);
            if (!timed)
                LockSupport.park(this);
            else if ((nanos = time - System.nanoTime()) > 0L)
                LockSupport.parkNanos(this, nanos);
            else
                break;
            node.clearStatus();
            if ((interrupted |= Thread.interrupted()) && interruptible)
                break;
        }
    }
    // 重试完毕不满足  取消申请获取资源
    return cancelAcquire(node, interrupted, interruptible);
}


```
### 取消获取资源

```plain
/**
* node == null 表示节点未创建已经被通知
* interrupted 是否是因为线程中断原因取消
* interruptibl 是否报告调用者中断
*/
private int cancelAcquire(Node node, boolean interrupted,
                          boolean interruptible) {
    if (node != null) {
        node.waiter = null;
        node.status = CANCELLED;
        // 节点还有前置节点才需要重新清理队列
        if (node.prev != null)
            cleanQueue();
    }
    if (interrupted) {
        if (interruptible)
            return CANCELLED;
        else
            Thread.currentThread().interrupt();
    }
    return 0;
}


```
# 相关方法操作

## 队列操作

```plain
// 入队操作 线程signal通知时候执行
final void enqueue(Node node) {
    if (node != null) {
        for (;;) {
            Node t = tail;
            node.setPrevRelaxed(t);        // avoid unnecessary fence
            if (t == null)                 // initialize
                tryInitializeHead();
            // cas尾插
            else if (casTail(t, node)) {
                t.next = node;
                // 插入的节点前一个节点取消阻塞 唤醒当前节点
                if (t.status < 0)          // wake up to clean link
                    LockSupport.unpark(node.waiter);
                break;
            }
        }
    }
}
// 节点清理 取消阻塞的节点会被清理出队 从尾遍历
private void cleanQueue() {
    for (;;) {                               // restart point
        for (Node q = tail, s = null, p, n;;) { // (p, q, s) triples
            // 当前节点为null或者前置节点null 清理完成
            if (q == null || (p = q.prev) == null)
                return;                      // end of list
            // 节点变化重新进入尾节点
            if (s == null ? tail != q : (s.prev != q || s.status < 0))
                break;                       // inconsistent
            
            if (q.status < 0) {              // cancelled
                // s是上一次清理的节点 p节点是清理节点的前节点
                if ((s == null ? casTail(q, p) : s.casPrev(q, p)) &&
                    q.prev == p) {
                    p.casNext(q, s);         // OK if fails
                    // p节点是头节点取消后续节点阻塞
                    if (p.prev == null)
                        signalNext(p);
                }
                break;
            }
            // 当前节点的前节点不是P继续清理
            if ((n = p.next) != q) {         // help finish
               // s.casPrev(q, p))完成后续清理未完成
                if (n != null && q.prev == p) {
                    p.casNext(n, q);
                    if (p.prev == null)
                        signalNext(p);
                }
                break;
            }
            s = q;
            q = q.prev;
        }
    }
}
```


## 模板方法

```plain
// AQS独占获取
protected boolean tryAcquire(int arg) {
    throw new UnsupportedOperationException();
}
// AQS独占释放
protected boolean tryRelease(int arg) {
    throw new UnsupportedOperationException();
}
// AQS共享获取
protected int tryAcquireShared(int arg) {
    throw new UnsupportedOperationException();
}
// AQS共享释放
protected boolean tryReleaseShared(int arg) {
    throw new UnsupportedOperationException();
}
// 借由Condition控制的是否线程具有独占权
protected boolean isHeldExclusively() {
    throw new UnsupportedOperationException();
}
```


# 整体功能

1. 共享资源访问逻辑和线程排队管理器，提供模板方法完成共享资源访问逻辑

2. 实现独占锁、共享锁功能，取决于唤醒后续节点的数

3. 实现公平锁和非公平锁，队列空入队按顺序获取，未入队列线程和队列尾同时抢锁







