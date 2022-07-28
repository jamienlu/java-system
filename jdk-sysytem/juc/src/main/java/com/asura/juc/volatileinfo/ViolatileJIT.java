package com.asura.juc.volatileinfo;

public class ViolatileJIT {
	private static boolean running = true;
	private static int count = 0;

	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(() -> {
			while (running) {
				count++;
			}
			System.out.println("stop runing:" + count);
		});

		Thread t2 = new Thread(() -> {
			running = false;
		});
		t1.start();
		Thread.sleep(100);
		t2.start();
		t1.join();
		t2.join();
	}
}
