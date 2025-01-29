package io.github.jamielu.jvmdemo.api;

/**
 *  反汇编
 * public class io.github.jamielu.jvmdemo.api.ModuleRunner implements io.github.jamielu.jvmdemo.api.ModuleApi {
 *   public io.github.jamielu.jvmdemo.api.ModuleRunner();
 *     Code:
 *        0: aload_0
 *        1: invokespecial #1                  // Method java/lang/Object."<init>":()V
 *        4: aload_0
 *        5: iconst_0
 *        6: putfield      #7                  // Field running:Z
 *        9: return
 *
 *   public void start();
 *     Code:
 *        0: aload_0
 *        1: iconst_1
 *        2: putfield      #7                  // Field running:Z
 *        5: aload_0
 *        6: new           #13                 // class java/lang/Thread
 *        9: dup
 *       10: aload_0
 *       11: invokedynamic #15,  0             // InvokeDynamic #0:run:(Lio/github/jamielu/jvmdemo/api/ModuleRunner;)Ljava/lang/Runnable;
 *       16: invokespecial #19                 // Method java/lang/Thread."<init>":(Ljava/lang/Runnable;)V
 *       19: putfield      #22                 // Field workerThread:Ljava/lang/Thread;
 *       22: aload_0
 *       23: getfield      #22                 // Field workerThread:Ljava/lang/Thread;
 *       26: invokevirtual #26                 // Method java/lang/Thread.start:()V
 *       29: return
 *
 *   public void stop();
 *     Code:
 *        0: aload_0
 *        1: iconst_0
 *        2: putfield      #7                  // Field running:Z
 *        5: aload_0
 *        6: getfield      #22                 // Field workerThread:Ljava/lang/Thread;
 *        9: ifnull        19
 *       12: aload_0
 *       13: getfield      #22                 // Field workerThread:Ljava/lang/Thread;
 *       16: invokevirtual #29                 // Method java/lang/Thread.interrupt:()V
 *       19: getstatic     #32                 // Field java/lang/System.out:Ljava/io/PrintStream;
 *       22: ldc           #38                 // String Module stopped
 *       24: invokevirtual #40                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
 *       27: return
 *
 *   public java.lang.String execute();
 *     Code:
 *        0: ldc           #46                 // String Result from module
 *        2: areturn
 * }
 *
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
