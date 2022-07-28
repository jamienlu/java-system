## 作用：

###  1.禁用指令重排序

jvm jit会对volatile相关代码禁用重排序

### 禁用重排序规则：

**1.StoreStore 写写屏障**

**2.StoreLoad 写读屏障**

**3.LoadLoad 读读屏障**

**4.LoadStore 读写屏障**

java规则：violatile写操作前StoreStore和LoadStore,写后StoreLoad ，violatile读操作后StoreLoad 和LoadLoad 

### 2.可见性

**内存屏障：jvm 对volatile变量的修改添加lock cpu指令,立刻写入内存，并通知其他CPU更新缓存**

1.LOCK指令：声明之后锁定总线，独占共享内存，通过一种排它确保当前对内存操作的只有一个线程，然后确定在这段声明期间指令执行不会被打断，来保证其原子性。 

2.java 使用CMPXCHG指令 ,自动加总线锁保，导致其它 处理器不能同时访问，证其原子性

### 3.volatile有序性

happens-before规则：

 **1.单线程规则：在一个线程中，按照代码的顺序，前面的操作Before于后面的任意操作**

**2.锁规则：线程释放锁先去另一个线程获取锁**

**3.volatile变量规则：一个使用了volatile变量的写操作，先行发生于后面对这个变量的读操作**

**4.线程启动规则：线程A在启动过程中创建线程B，A对共享变量的修改对B可见**

**5.线程终止规则：线程A通过join等待B,线程B对共享变量的修改在线程B终止后对A可见**

**6.线程中断规则：线程A调用线程B的interupt,先于线程B之前的代码检测到B的中断**

**7.对象终结规则：init 先去finalize**

**8.传递规则： A >B, B>C => A>C**

### 







