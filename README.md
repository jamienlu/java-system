# java17 learn 
>[jdk-17.0.10-ga](https://github.com/openjdk/jdk17u/releases/tag/jdk-17.0.10-ga)
# 源码编译

## 编译

sudo apt-get install build-essential

```plain
bash configure --enable-debug --with-jvm-variants=server --with-native-debug-symbols=none
提示报错依赖不足一步一步安装依赖
sudo apt-get install zip
sudo apt-get install autoconf
sudo apt-get install libx11-dev libxext-dev libxrender-dev libxrandr-dev libxtst-dev libxt-dev
sudo apt-get install libcups2-dev
sudo apt-get install libfontconfig1-dev
sudo apt-get install libasound2-dev
```

https://uploader.shimo.im/f/sAUAg88e5RHzqfyd.png?sm_xform=image%2Fauto-orient%2C1![image](https://github.com/user-attachments/assets/8145fbfd-4b4e-4abb-9dc0-166d972de366)


```plain
make compile-commands
make all


cd  /snap/clion/265/bin/
sh clion.sh => open compile-commands.json
```


# JVM

>类加载 经编译执行进 运行数据区  执行垃圾回收策略和调用本地方法等
## 类加载系统

```plain
加载 (.class文件)-> 验证 (是否是java文件语法合规) -> 链接(静态变量默认值) -> 
解析（符号引入用转内存地址引用） -> 初始化 (静态变量赋值和静态代码执行)
```
## 运行时数据区

```plain
堆和非堆
非堆主要包含元数据 JIT缓存 常量池 直接内存 静态变量 栈内存等
堆  java对象
```
## JIT编译器

```plain
编译代码，热点代码直转机器码跳过字节码  c1\c2分层编译等
方法内联
```
## 字节码解释器

## 垃圾回收器

```plain
java对象内存回收策略
```
## 本地方法接口和本地方法库

```plain
C\C++ 标准库链接接口和操作系统交互依赖库
```


# JAVA线程相关

## 锁

```plain
synchronized
Semaphore
CountDownLatch
Condition
Lock
LockSupport
StampedLock
ReentrantLock
ReentrantReadWriteLock
```
## 原子类

```plain
java.util.concurrent.atomic
```
## 并发容器

```plain
ConcurrentHashMap
CopyOnWriteArrayList
CopyOnWriteArraySet
```


## 线程池

```plain
AbstractQueuedSynchronizer
ExecutorService
ThreadPoolExecutor
ForkJoinPool
```


# JAVA类库相关

## 反射

```plain
Class
AnnotatedType
Constructor
Field
Method
Modifier
Parameter
ParameterizedType
Proxy
InvocationHandler
```
## 数据结构

```plain
byte boolean char short int float double string 
Array
List
Set
Hash
```
## io

```plain
File


InputStream
OutputStream
类型包括 ByteArray Buffered CharArray Data Object Piped Zip Object
```


## 函数式

```plain
Stream
  BaseStream
  IntStream
  AbstractPipeline


Function
  Consumer
  Predicate
  Supplier
  
Collector
  Collectors


```
## spi

