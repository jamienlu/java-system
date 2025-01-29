package io.github.jamielu.jvmdemo.api;

/**
 * @author jamieLu
 * @create 2025-01-28
 */
public class ModuleRunner implements ModuleApi {
    private volatile boolean running = false;
    private Thread workerThread;
    @Override
    public void start() {
        running = true;
        workerThread = new Thread(() -> {
            while (running) {
                System.out.println("Module working...111");
                try { Thread.sleep(1000);
                } catch (InterruptedException e) {}
            }
        });
        workerThread.start();
    }

    @Override
    public void stop() {
        running = false;
        if (workerThread != null) {
            workerThread.interrupt();
        }
        System.out.println("Module stopped");
    }

    @Override
    public String execute() {
        return "Result from module";
    }
}
