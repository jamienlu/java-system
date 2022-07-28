package com.asura.juc.synchorized;

import org.openjdk.jol.info.ClassLayout;

import java.util.Vector;

public class Synchronized {
	private static Vector<Integer> vector = new Vector<Integer>();

	public static void testError(String[] args) {
		for (int j = 0; j < 1000; j++) {
			for (int i = 0; i < 10; i++) {
				vector.add(i);
			}

			new Thread(() -> {
				for (int i = 0; i < vector.size(); i++) {
					vector.remove(i);
				}
			}).start();

			new Thread(() -> {
				for (int i = 0; i < vector.size(); i++) {
					System.out.println((vector.get(i)));
				}
			}).start();
		}
	}

	public static void main(String[] args) throws InterruptedException {

		testSyncMehodNoSafe();
	}

	public static void testSyncMehodNoSafe() {
		SyncCount syncCount = new SyncCount();
		System.out.println(ClassLayout.parseInstance(syncCount).toPrintable());
		for (int i = 0; i < 10000; i++) {
			final int t = i;
			new Thread(() -> {
				if (syncCount.checkCount()) {
					// 中间无代码 JIT会触发锁粗化 不会造成同步问题
					/*for (int j = 0; j < 10000; j++) {
						Math.random();
					}*/
					int value = syncCount.add();
					System.out.println("count:"+value);
				}


			}).start();

		}
		System.out.println(ClassLayout.parseInstance(syncCount).toPrintable());
	}




}
