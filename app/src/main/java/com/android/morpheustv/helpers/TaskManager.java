package com.android.morpheustv.helpers;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskManager {
    public static ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, 10, KEEP_ALIVE_TIME_UNIT, mTraktWorkQueue);
    private static final int KEEP_ALIVE_TIME = 10;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.MINUTES;
    public static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    public static ThreadPoolExecutor OPENLOAD_EXECUTOR = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, 10, KEEP_ALIVE_TIME_UNIT, mOpenloadWorkQueue);
    public static ThreadPoolExecutor RESOLVER_EXECUTOR = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, 10, KEEP_ALIVE_TIME_UNIT, mResolveWorkQueue);
    public static ThreadPoolExecutor STREAMANGO_EXECUTOR = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, 10, KEEP_ALIVE_TIME_UNIT, mStreamangoWorkQueue);
    public static ThreadPoolExecutor THEVIDEO_EXECUTOR = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, 10, KEEP_ALIVE_TIME_UNIT, mThevideoWorkQueue);
    private static final BlockingQueue<Runnable> mOpenloadWorkQueue = new LinkedBlockingQueue();
    private static final BlockingQueue<Runnable> mResolveWorkQueue = new LinkedBlockingQueue();
    private static final BlockingQueue<Runnable> mStreamangoWorkQueue = new LinkedBlockingQueue();
    private static final BlockingQueue<Runnable> mThevideoWorkQueue = new LinkedBlockingQueue();
    private static final BlockingQueue<Runnable> mTraktWorkQueue = new LinkedBlockingQueue();

    public static void clearAllResolvers() {
        clearTasks(RESOLVER_EXECUTOR);
        clearTasks(OPENLOAD_EXECUTOR);
        clearTasks(STREAMANGO_EXECUTOR);
        clearTasks(THEVIDEO_EXECUTOR);
    }

    public static boolean anyTaskRunning() {
        if (taskCount(RESOLVER_EXECUTOR) <= 0 && taskCount(OPENLOAD_EXECUTOR) <= 0 && taskCount(STREAMANGO_EXECUTOR) <= 0 && taskCount(THEVIDEO_EXECUTOR) <= 0) {
            return false;
        }
        return true;
    }

    public static int taskCount(ThreadPoolExecutor threadPoolExecutor) {
        return threadPoolExecutor.getQueue().size();
    }

    public static void clearTasks(ThreadPoolExecutor threadPoolExecutor) {
        threadPoolExecutor.getQueue().drainTo(new ArrayList());
    }
}
