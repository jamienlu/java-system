>Java内存模型和线程规范修订 jsr-133
```plain
Happen-before：定义了操作之间的顺序关系，确保在多线程环境下对共享变量的修改和读取能够按照一定的顺序进行，保证可见性和有序性。


volatile关键字：标记共享变量，确保对该变量的读取和写入操作在多线程之间具有可见性和有序性。


synchronized关键字：提供了一种互斥的同步机制，用于保护共享数据的一致性，确保多个线程之间的操作按照一定的顺序执行。


final关键字：保证被final修饰的变量在构造完成后对所有线程可见。


线程的启动和终止规则：定义了线程启动和终止时对共享变量的同步规则
```
# violatile

## 可见性

```plain
violatile变量操作会添加cpu lock指令，确保立即写入内存，和其他cpu更新
```
## 有序性

```plain
jvm jit会对volatile相关代码禁用重排序，同样依赖lock指令


violatile写操作前StoreStore和LoadStore,
写后StoreLoad ，violatile读操作后StoreLoad 和LoadLoad 
```



# synchronized

>**JAVA对象锁，根据对象头判断锁状态，分别由偏向锁 无锁 轻量级锁 重量锁构成**
。

### 锁实现

```plain
ObjectMonitor实现
class ObjectMonitor : public CHeapObj<mtObjectMonitor> {
  friend class ObjectSynchronizer;
  friend class ObjectWaiter;
  friend class VMStructs;
  JVMCI_ONLY(friend class JVMCIVMStructs;)
volatile markWord _header;        // displaced object header word - mark
WeakHandle _object;               // backward object pointer
void* volatile _owner;            // pointer to owning thread OR BasicLock
ObjectWaiter* volatile _EntryList;  // Threads blocked on entry or reentry.
ObjectWaiter* volatile _cxq;      // LL of recently-arrived threads blocked on entry.
ObjectWaiter* volatile _WaitSet;  // LL of threads wait()ing on the monitor


_owner cas线程地址获取锁
竞争锁失败，进入_cxq单向链表等待锁
锁线程释放后从_EntryList取出线程，再次cas竞争，_EntryList空从复制_cxq
_WaitSet实现wait() notify()
```


### 锁特性

```plain
锁升级
初始竞争为偏向锁，偏向线程id用0填充，存在线程竞争升级为轻量级锁,轻量级释放后变更成无锁，存在大量竞争升级成重量级锁。


锁粗化
2个synchronized片段锁相同，中间代码 jit 判断执行少会把2个代码块合成1个块使用同一把锁
循环内锁可能会移到循环外


锁消除
JVM根据数据逃逸分析，会去掉锁
```

### 

### 锁使用

```plain
锁代码块 
入口 monitorenter
出口 monitorexit


锁方法  ACC_SYNCHRONIZED flags标记方法
```


# CAS

## CAS原理

```plain
1.硬件支持的CPU指令,X86为cmpxchg指令，多核添加LOCK前缀实现原子性
2.JAVA实现：JAVA采用Unsafe类的compareAndSwap*方法实现，JVM采用Atomic:cmpxchg汇编指令实现。
```
## CAS优缺点

```plain
优点：
1.不阻塞线程，不存在线程的阻塞和唤醒，减少了上下文切换的开销，通常性能更佳
2.由于使用底层硬件指令，开发者不需要关注并发逻辑，成功继续，失败自旋或退出，处理相对简单。


缺点：
1.存在ABA问题，以链表添加和删除为例，ABA问题会导致添加节点失败
版本号方案 - AtomicStamped
2.CPU开销问题，高并发长期失败,一直处于自旋状态消耗CPU性能
cpu pause指令，延迟流水线执行命令避免CPU过多消耗资源
3.只能保证一个共享变量的原子操作
AtomicReference可对一个引用类，包裹多个变量执行原子操作
```

### 








