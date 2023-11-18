package io.github.morpheustv.scrapers.helper;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskManager {
    public static ThreadPoolExecutor EXECUTOR;
    public static ThreadPoolExecutor OPENLOAD_EXECUTOR;
    public static ThreadPoolExecutor RESOLVER_EXECUTOR;
    public static ThreadPoolExecutor STREAMANGO_EXECUTOR;
    public static ThreadPoolExecutor THEVIDEO_EXECUTOR;
    public static ThreadPoolExecutor WEBVIEW_EXECUTOR;

    public static void clearAllResolvers() {
        clearTasks(EXECUTOR);
        clearTasks(RESOLVER_EXECUTOR);
        clearTasks(OPENLOAD_EXECUTOR);
        clearTasks(STREAMANGO_EXECUTOR);
        clearTasks(THEVIDEO_EXECUTOR);
        clearTasks(WEBVIEW_EXECUTOR);
    }

    public static boolean anyTaskRunning() {
        if (taskCount(EXECUTOR) <= 0 && taskCount(RESOLVER_EXECUTOR) <= 0 && taskCount(OPENLOAD_EXECUTOR) <= 0 && taskCount(STREAMANGO_EXECUTOR) <= 0 && taskCount(THEVIDEO_EXECUTOR) <= 0 && taskCount(WEBVIEW_EXECUTOR) <= 0) {
            return false;
        }
        return true;
    }

    public static int taskCount(ThreadPoolExecutor threadPoolExecutor) {
        return threadPoolExecutor != null ? threadPoolExecutor.getQueue().size() : null;
    }

    public static void clearTasks(ThreadPoolExecutor threadPoolExecutor) {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.getQueue().drainTo(new ArrayList());
        }
    }

    public static void setExecutors(int i) {
        BlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
        BlockingQueue linkedBlockingQueue2 = new LinkedBlockingQueue();
        LinkedBlockingQueue linkedBlockingQueue3 = new LinkedBlockingQueue();
        LinkedBlockingQueue linkedBlockingQueue4 = new LinkedBlockingQueue();
        LinkedBlockingQueue linkedBlockingQueue5 = new LinkedBlockingQueue();
        LinkedBlockingQueue linkedBlockingQueue6 = new LinkedBlockingQueue();
        long j = (long) 5;
        EXECUTOR = new ThreadPoolExecutor(i, i, j, TimeUnit.MINUTES, linkedBlockingQueue);
        long j2 = j;
        LinkedBlockingQueue linkedBlockingQueue7 = linkedBlockingQueue6;
        LinkedBlockingQueue linkedBlockingQueue8 = linkedBlockingQueue5;
        RESOLVER_EXECUTOR = new ThreadPoolExecutor(i, i, j2, TimeUnit.MINUTES, linkedBlockingQueue2);
        LinkedBlockingQueue linkedBlockingQueue9 = linkedBlockingQueue4;
        LinkedBlockingQueue linkedBlockingQueue10 = linkedBlockingQueue3;
        OPENLOAD_EXECUTOR = new ThreadPoolExecutor(i, i, j2, TimeUnit.MINUTES, linkedBlockingQueue10);
        int i2 = i;
        int i3 = i;
        long j3 = j2;
        STREAMANGO_EXECUTOR = new ThreadPoolExecutor(i2, i3, j3, TimeUnit.MINUTES, linkedBlockingQueue9);
        THEVIDEO_EXECUTOR = new ThreadPoolExecutor(i2, i3, j3, TimeUnit.MINUTES, linkedBlockingQueue8);
        WEBVIEW_EXECUTOR = new ThreadPoolExecutor(i2, i3, j3, TimeUnit.MINUTES, linkedBlockingQueue7);
    }
}
