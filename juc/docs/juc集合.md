# ConcurrentLinkedQueue

```plain
public boolean offer(E e) {
    final Node<E> newNode = new Node<E>(Objects.requireNonNull(e));


    for (Node<E> t = tail, p = t;;) {
        Node<E> q = p.next;
        if (q == null) {
            // p is last node
            if (NEXT.compareAndSet(p, null, newNode)) {
                // Successful CAS is the linearization point
                // for e to become an element of this queue,
                // and for newNode to become "live".
                if (p != t) // hop two nodes at a time; failure is OK
                    TAIL.weakCompareAndSet(this, t, newNode);
                return true;
            }
            // Lost CAS race to another thread; re-read next
        }
        else if (p == q)
            // We have fallen off list.  If tail is unchanged, it
            // will also be off-list, in which case we need to
            // jump to head, from which all live nodes are always
            // reachable.  Else the new tail is a better bet.
            p = (t != (t = tail)) ? t : head;
        else
            // Check for tail updates after two hops.
            p = (p != t && t != (t = tail)) ? t : q;
    }
}
```


无界链表，所有并发操作基于cas


# ArrayBlockingQueue

```plain
final Object[] items;


/** items index for next take, poll, peek or remove */
int takeIndex;


/** items index for next put, offer, or add */
int putIndex;


/** Number of elements in the queue */
int count;


/*
 * Concurrency control uses the classic two-condition algorithm
 * found in any textbook.
 */


/** Main lock guarding all access */
final ReentrantLock lock;


/** Condition for waiting takes */
@SuppressWarnings("serial")  // Classes implementing Condition may be serializable.
private final Condition notEmpty;


/** Condition for waiting puts */
@SuppressWarnings("serial")  // Classes implementing Condition may be serializable.
private final Condition notFull;
```


基于数组实现的FIFO队列线程安全版


# LinkedBlockingQueue

```plain
private final ReentrantLock takeLock = new ReentrantLock();


/** Wait queue for waiting takes */
@SuppressWarnings("serial") // Classes implementing Condition may be serializable.
private final Condition notEmpty = takeLock.newCondition();


/** Lock held by put, offer, etc */
private final ReentrantLock putLock = new ReentrantLock();


/** Wait queue for waiting puts */
@SuppressWarnings("serial") // Classes implementing Condition may be serializable.
private final Condition notFull = putLock.newCondition();
```


定义了2把锁关联2个条件变量一个保护插入一个保护取出减少竞争的队列，

根据构造函数决定是否有界。


# DelayQueue

```plain
private final transient ReentrantLock lock = new ReentrantLock();
private final PriorityQueue<E> q = new PriorityQueue<E>();


/**
 * Thread designated to wait for the element at the head of
 * the queue.  This variant of the Leader-Follower pattern
 * (http://www.cs.wustl.edu/~schmidt/POSA/POSA2/) serves to
 * minimize unnecessary timed waiting.  When a thread becomes
 * the leader, it waits only for the next delay to elapse, but
 * other threads await indefinitely.  The leader thread must
 * signal some other thread before returning from take() or
 * poll(...), unless some other thread becomes leader in the
 * interim.  Whenever the head of the queue is replaced with
 * an element with an earlier expiration time, the leader
 * field is invalidated by being reset to null, and some
 * waiting thread, but not necessarily the current leader, is
 * signalled.  So waiting threads must be prepared to acquire
 * and lose leadership while waiting.
 */
private Thread leader;


/**
 * Condition signalled when a newer element becomes available
 * at the head of the queue or a new thread may need to
 * become leader.
 */
private final Condition available = lock.newCondition();
```


基于延迟时间Delay接口的优先级队列，只有时间到期才可访问，用可重入锁保护


#  CopyOnWriteArrayList

```plain
public class CopyOnWriteArrayList<E>
    implements List<E>, RandomAccess, Cloneable, java.io.Serializable {
    private static final long serialVersionUID = 8673264195747942595L;


    /**
     * The lock protecting all mutators.  (We have a mild preference
     * for builtin monitors over ReentrantLock when either will do.)
     */
    final transient Object lock = new Object();


    /** The array, accessed only via getArray/setArray. */
    private transient volatile Object[] array;
    
public void add(int index, E element) {
    synchronized (lock) {
        Object[] es = getArray();
        int len = es.length;
        if (index > len || index < 0)
            throw new IndexOutOfBoundsException(outOfBounds(index, len));
        Object[] newElements;
        int numMoved = len - index;
        if (numMoved == 0)
            newElements = Arrays.copyOf(es, len + 1);
        else {
            newElements = new Object[len + 1];
            System.arraycopy(es, 0, newElements, 0, index);
            System.arraycopy(es, index, newElements, index + 1,
                             numMoved);
        }
        newElements[index] = element;
        setArray(newElements);
    }
}


```
ArrayList线程安全版，synchronized实现，只卡写操作，先写副本在替换原数组
# CopyOnWriteArraySet

ArraySet线程安全，同样只卡写操作

# ConcurrentSkipListMap

线程安全的跳表，提高get put remove效率的多层链表，cas无锁设计

# ConcurrentHashMap

hashmap线程安全版，1.7之前采用segment分段机制，切割hash桶分组

1.8后使用cas+锁hash链表的头节点实现，整体数据结构也和hashmap一致，扩容为多线程协助扩容少阻塞



