package com.asura.juc.volatileinfo;

import java.util.Objects;

public class VolatileSee {
	private static volatile int count = 0;
	private static volatile VolatileSee instance;
	private VolatileSee() {

	}
	public static VolatileSee testReOrder() {
		// 1分配内存空间地址  2.初始化  3.地址赋值
		// 不使用有可能3>2，多线程拿了一个对象还位初始化使用出问题
		if (instance == null) {
			synchronized (VolatileSee.class) {
				if (instance == null) {
					instance = new VolatileSee();
				}
			}
		}
		return instance;
	}
	public static void testNotSee() {
		for (int i = 0; i < 100; i++) {
			new Thread(() -> {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count++;
			}).start();
			new Thread(() -> {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// 不使用violatile值可能不是最新
				System.out.println(count);
			}).start();
		}
	}

	public static void main(String[] args) {
		System.out.println(Objects.isNull(testReOrder()));


	}
}
