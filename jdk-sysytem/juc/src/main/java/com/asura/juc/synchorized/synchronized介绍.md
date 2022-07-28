## 作用：

**JAVA对象锁，根据对象头判断锁状态，分别由偏向锁 无锁 轻量级锁 重量锁构成**。

javap 反编译为出现 monitorenter monitorexit 2个字节码指令，方法出现ACC_SYNCHRONIZED 标识

### 锁实现: 

1.偏向锁：CAS线程号为当前线程，获得锁 

升级：使用FULLGC的STW暂停偏向锁线程

       2.无锁：在栈创建Lock record,拷贝markword到record,cas检查锁标志位，设置对象都id指向record

3.轻量锁：同无锁，CAS失败自旋

    升级:  创建ObjectMonitor对象，将阻塞线程放到cxt_队列

4.重量锁：Monitor对象队列竞争，调用CPU指令阻塞

#### ObjectMonitor作用：

1.object ： 锁的对象

2.owner ： 获得锁的线程

3.cxq ：竞争失败的线程队列

4.entrylist ：准备竞争的线程队列，为空会复制cxq到entrylist 

5.waitset : 调用wait()的线程 notify从队列移除

## 锁特性：

### 1.锁升级

初始为偏向锁，偏向线程id用0填充，存在线程竞争升级为轻量级锁,轻量级释放后变更成无锁，存在大量竞争升级成重量级锁。

#### 2.锁粗化

 2个synchronized片段锁相同，中间代码 jit 判断执行少会把2个代码块合成1个块使用同一把锁

#### 3.锁消除

JVM根据数据逃逸分析，会去掉锁






