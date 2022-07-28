package com.asura.juc.lock;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.LockSupport;

public class LockSupportTest {
	@Test
	public void testLockSupport() {
		/**
		 * 1.LockSupport unpark和park无先后关系
		 * 2.unpark只能唤醒传入的park线程
		 */
		new Thread(() -> {
			Thread thread = Thread.currentThread();
			System.out.println(thread.getName() + ":prepare lock1");
			LockSupport.park();
			System.out.println(thread.getName() + ":lock already cancle1");
		}).start();
		Thread childThread = new Thread(() -> {
			Thread thread = Thread.currentThread();
			System.out.println(thread.getName() + ":prepare lock2");
			LockSupport.park();
			System.out.println(thread.getName() + ":lock already cancle2");
		});
		childThread.start();
		LockSupport.unpark(childThread);
	}
}
