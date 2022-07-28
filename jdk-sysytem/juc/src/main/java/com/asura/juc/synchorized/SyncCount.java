package com.asura.juc.synchorized;

import org.openjdk.jol.info.ClassLayout;

public class SyncCount {
	private volatile int count = 1;
	public synchronized int add() {
		count++;
		System.out.println(ClassLayout.parseInstance(this).toPrintable());
		return count;
	}
	public synchronized boolean checkCount() {
		if (count  % 2 == 1) {
			return true;
		} else {
			return false;
		}
	}

	public int getCount() {
		synchronized (SyncCount.class) {
			return count;
		}

	}
}
