>java.util.concurrent.atomic

# 核心实现

```plain
volatile存储数据指针
Unsafe 提供提针引用的cas操作  对内存地址读取偏移量数据操作
```


# 其他类

## Striped64

```plain


/**
 * Table of cells. When non-null, size is a power of 2.
 */
transient volatile Cell[] cells;


/**
 * Base value, used mainly when there is no contention, but also as
 * a fallback during table initialization races. Updated via CAS.
 */
transient volatile long base;


/**
 * Spinlock (locked via CAS) used when resizing and/or creating Cells.
 */
transient volatile int cellsBusy;


并发竞争cas自旋失败，性能下降设计的cell分段竞争，
cas失败cells扩容减少竞争，最终会汇总值，吞吐高实时性差




```


## DoubleAdder

```plain
高效累加器，double适配Striped64
```
## DoubleAccumulator

```plain
允许自定义累加函数
DoubleAccumulator doubleAccumulator = new DoubleAccumulator((left,right) -> (left + right)/2,-100.0);
doubleAccumulator.accumulate(10);
System.out.println("###"+doubleAccumulator.get());
```


