package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class MoreExecutors {

    @VisibleForTesting
    static class Application {
        Application() {
        }

        final ExecutorService getExitingExecutorService(ThreadPoolExecutor threadPoolExecutor, long j, TimeUnit timeUnit) {
            MoreExecutors.useDaemonThreadFactory(threadPoolExecutor);
            threadPoolExecutor = Executors.unconfigurableExecutorService(threadPoolExecutor);
            addDelayedShutdownHook(threadPoolExecutor, j, timeUnit);
            return threadPoolExecutor;
        }

        final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, long j, TimeUnit timeUnit) {
            MoreExecutors.useDaemonThreadFactory(scheduledThreadPoolExecutor);
            scheduledThreadPoolExecutor = Executors.unconfigurableScheduledExecutorService(scheduledThreadPoolExecutor);
            addDelayedShutdownHook(scheduledThreadPoolExecutor, j, timeUnit);
            return scheduledThreadPoolExecutor;
        }

        final void addDelayedShutdownHook(ExecutorService executorService, long j, TimeUnit timeUnit) {
            Preconditions.checkNotNull(executorService);
            Preconditions.checkNotNull(timeUnit);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DelayedShutdownHook-for-");
            stringBuilder.append(executorService);
            final ExecutorService executorService2 = executorService;
            final long j2 = j;
            final TimeUnit timeUnit2 = timeUnit;
            addShutdownHook(MoreExecutors.newThread(stringBuilder.toString(), new Runnable() {
                public void run() {
                    /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
                    /*
                    r4 = this;
                    r0 = r3;	 Catch:{ InterruptedException -> 0x000e }
                    r0.shutdown();	 Catch:{ InterruptedException -> 0x000e }
                    r0 = r3;	 Catch:{ InterruptedException -> 0x000e }
                    r1 = r4;	 Catch:{ InterruptedException -> 0x000e }
                    r3 = r6;	 Catch:{ InterruptedException -> 0x000e }
                    r0.awaitTermination(r1, r3);	 Catch:{ InterruptedException -> 0x000e }
                L_0x000e:
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.MoreExecutors.Application.1.run():void");
                }
            }));
        }

        final ExecutorService getExitingExecutorService(ThreadPoolExecutor threadPoolExecutor) {
            return getExitingExecutorService(threadPoolExecutor, 120, TimeUnit.SECONDS);
        }

        final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
            return getExitingScheduledExecutorService(scheduledThreadPoolExecutor, 120, TimeUnit.SECONDS);
        }

        @VisibleForTesting
        void addShutdownHook(Thread thread) {
            Runtime.getRuntime().addShutdownHook(thread);
        }
    }

    private enum DirectExecutor implements Executor {
        INSTANCE;

        public void execute(Runnable runnable) {
            runnable.run();
        }
    }

    private static class DirectExecutorService extends AbstractListeningExecutorService {
        private final Lock lock;
        private int runningTasks;
        private boolean shutdown;
        private final Condition termination;

        private DirectExecutorService() {
            this.lock = new ReentrantLock();
            this.termination = this.lock.newCondition();
            this.runningTasks = 0;
            this.shutdown = false;
        }

        public void execute(Runnable runnable) {
            startTask();
            try {
                runnable.run();
            } finally {
                endTask();
            }
        }

        public boolean isShutdown() {
            this.lock.lock();
            try {
                boolean z = this.shutdown;
                return z;
            } finally {
                this.lock.unlock();
            }
        }

        public void shutdown() {
            this.lock.lock();
            try {
                this.shutdown = true;
            } finally {
                this.lock.unlock();
            }
        }

        public List<Runnable> shutdownNow() {
            shutdown();
            return Collections.emptyList();
        }

        public boolean isTerminated() {
            this.lock.lock();
            try {
                boolean z = this.shutdown && this.runningTasks == 0;
                this.lock.unlock();
                return z;
            } catch (Throwable th) {
                this.lock.unlock();
            }
        }

        public boolean awaitTermination(long j, TimeUnit timeUnit) throws InterruptedException {
            j = timeUnit.toNanos(j);
            this.lock.lock();
            while (isTerminated() == null) {
                try {
                    if (j <= 0) {
                        j = null;
                        break;
                    }
                    j = this.termination.awaitNanos(j);
                } catch (Throwable th) {
                    this.lock.unlock();
                }
            }
            j = 1;
            this.lock.unlock();
            return j;
        }

        private void startTask() {
            this.lock.lock();
            try {
                if (isShutdown()) {
                    throw new RejectedExecutionException("Executor already shutdown");
                }
                this.runningTasks++;
            } finally {
                this.lock.unlock();
            }
        }

        private void endTask() {
            this.lock.lock();
            try {
                this.runningTasks--;
                if (isTerminated()) {
                    this.termination.signalAll();
                }
                this.lock.unlock();
            } catch (Throwable th) {
                this.lock.unlock();
            }
        }
    }

    private static class ListeningDecorator extends AbstractListeningExecutorService {
        private final ExecutorService delegate;

        ListeningDecorator(ExecutorService executorService) {
            this.delegate = (ExecutorService) Preconditions.checkNotNull(executorService);
        }

        public boolean awaitTermination(long j, TimeUnit timeUnit) throws InterruptedException {
            return this.delegate.awaitTermination(j, timeUnit);
        }

        public boolean isShutdown() {
            return this.delegate.isShutdown();
        }

        public boolean isTerminated() {
            return this.delegate.isTerminated();
        }

        public void shutdown() {
            this.delegate.shutdown();
        }

        public List<Runnable> shutdownNow() {
            return this.delegate.shutdownNow();
        }

        public void execute(Runnable runnable) {
            this.delegate.execute(runnable);
        }
    }

    private static class ScheduledListeningDecorator extends ListeningDecorator implements ListeningScheduledExecutorService {
        final ScheduledExecutorService delegate;

        private static final class ListenableScheduledTask<V> extends ForwardingListenableFuture$SimpleForwardingListenableFuture<V> implements ListenableScheduledFuture<V> {
            private final ScheduledFuture<?> scheduledDelegate;

            public ListenableScheduledTask(ListenableFuture<V> listenableFuture, ScheduledFuture<?> scheduledFuture) {
                super(listenableFuture);
                this.scheduledDelegate = scheduledFuture;
            }

            public boolean cancel(boolean z) {
                boolean cancel = super.cancel(z);
                if (cancel) {
                    this.scheduledDelegate.cancel(z);
                }
                return cancel;
            }

            public long getDelay(TimeUnit timeUnit) {
                return this.scheduledDelegate.getDelay(timeUnit);
            }

            public int compareTo(Delayed delayed) {
                return this.scheduledDelegate.compareTo(delayed);
            }
        }

        private static final class NeverSuccessfulListenableFutureTask extends AbstractFuture<Void> implements Runnable {
            private final Runnable delegate;

            public NeverSuccessfulListenableFutureTask(Runnable runnable) {
                this.delegate = (Runnable) Preconditions.checkNotNull(runnable);
            }

            public void run() {
                try {
                    this.delegate.run();
                } catch (Throwable th) {
                    setException(th);
                    RuntimeException propagate = Throwables.propagate(th);
                }
            }
        }

        ScheduledListeningDecorator(ScheduledExecutorService scheduledExecutorService) {
            super(scheduledExecutorService);
            this.delegate = (ScheduledExecutorService) Preconditions.checkNotNull(scheduledExecutorService);
        }

        public ListenableScheduledFuture<?> schedule(Runnable runnable, long j, TimeUnit timeUnit) {
            runnable = ListenableFutureTask.create(runnable, null);
            return new ListenableScheduledTask(runnable, this.delegate.schedule(runnable, j, timeUnit));
        }

        public <V> ListenableScheduledFuture<V> schedule(Callable<V> callable, long j, TimeUnit timeUnit) {
            callable = ListenableFutureTask.create(callable);
            return new ListenableScheduledTask(callable, this.delegate.schedule(callable, j, timeUnit));
        }

        public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
            ListenableFuture neverSuccessfulListenableFutureTask = new NeverSuccessfulListenableFutureTask(runnable);
            return new ListenableScheduledTask(neverSuccessfulListenableFutureTask, this.delegate.scheduleAtFixedRate(neverSuccessfulListenableFutureTask, j, j2, timeUnit));
        }

        public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
            ListenableFuture neverSuccessfulListenableFutureTask = new NeverSuccessfulListenableFutureTask(runnable);
            return new ListenableScheduledTask(neverSuccessfulListenableFutureTask, this.delegate.scheduleWithFixedDelay(neverSuccessfulListenableFutureTask, j, j2, timeUnit));
        }
    }

    private MoreExecutors() {
    }

    @Beta
    public static ExecutorService getExitingExecutorService(ThreadPoolExecutor threadPoolExecutor, long j, TimeUnit timeUnit) {
        return new Application().getExitingExecutorService(threadPoolExecutor, j, timeUnit);
    }

    @Beta
    public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, long j, TimeUnit timeUnit) {
        return new Application().getExitingScheduledExecutorService(scheduledThreadPoolExecutor, j, timeUnit);
    }

    @Beta
    public static void addDelayedShutdownHook(ExecutorService executorService, long j, TimeUnit timeUnit) {
        new Application().addDelayedShutdownHook(executorService, j, timeUnit);
    }

    @Beta
    public static ExecutorService getExitingExecutorService(ThreadPoolExecutor threadPoolExecutor) {
        return new Application().getExitingExecutorService(threadPoolExecutor);
    }

    @Beta
    public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
        return new Application().getExitingScheduledExecutorService(scheduledThreadPoolExecutor);
    }

    private static void useDaemonThreadFactory(ThreadPoolExecutor threadPoolExecutor) {
        threadPoolExecutor.setThreadFactory(new ThreadFactoryBuilder().setDaemon(true).setThreadFactory(threadPoolExecutor.getThreadFactory()).build());
    }

    @Deprecated
    public static ListeningExecutorService sameThreadExecutor() {
        return new DirectExecutorService();
    }

    public static ListeningExecutorService newDirectExecutorService() {
        return new DirectExecutorService();
    }

    public static Executor directExecutor() {
        return DirectExecutor.INSTANCE;
    }

    public static ListeningExecutorService listeningDecorator(ExecutorService executorService) {
        if (executorService instanceof ListeningExecutorService) {
            return (ListeningExecutorService) executorService;
        }
        return executorService instanceof ScheduledExecutorService ? new ScheduledListeningDecorator((ScheduledExecutorService) executorService) : new ListeningDecorator(executorService);
    }

    public static ListeningScheduledExecutorService listeningDecorator(ScheduledExecutorService scheduledExecutorService) {
        return scheduledExecutorService instanceof ListeningScheduledExecutorService ? (ListeningScheduledExecutorService) scheduledExecutorService : new ScheduledListeningDecorator(scheduledExecutorService);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static <T> T invokeAnyImpl(com.google.common.util.concurrent.ListeningExecutorService r21, java.util.Collection<? extends java.util.concurrent.Callable<T>> r22, boolean r23, long r24) throws java.lang.InterruptedException, java.util.concurrent.ExecutionException, java.util.concurrent.TimeoutException {
        /*
        r1 = r21;
        com.google.common.base.Preconditions.checkNotNull(r21);
        r3 = r22.size();
        if (r3 <= 0) goto L_0x000d;
    L_0x000b:
        r5 = 1;
        goto L_0x000e;
    L_0x000d:
        r5 = 0;
    L_0x000e:
        com.google.common.base.Preconditions.checkArgument(r5);
        r5 = com.google.common.collect.Lists.newArrayListWithCapacity(r3);
        r6 = com.google.common.collect.Queues.newLinkedBlockingQueue();
        if (r23 == 0) goto L_0x0024;
    L_0x001b:
        r7 = java.lang.System.nanoTime();	 Catch:{ all -> 0x0020 }
        goto L_0x0026;
    L_0x0020:
        r0 = move-exception;
        r1 = r0;
        goto L_0x00b7;
    L_0x0024:
        r7 = 0;
    L_0x0026:
        r9 = r22.iterator();	 Catch:{ all -> 0x0020 }
        r10 = r9.next();	 Catch:{ all -> 0x0020 }
        r10 = (java.util.concurrent.Callable) r10;	 Catch:{ all -> 0x0020 }
        r10 = submitAndAddQueueListener(r1, r10, r6);	 Catch:{ all -> 0x0020 }
        r5.add(r10);	 Catch:{ all -> 0x0020 }
        r3 = r3 + -1;
        r10 = 0;
        r11 = r24;
        r13 = r7;
        r8 = r10;
        r7 = 1;
    L_0x003f:
        r15 = r6.poll();	 Catch:{ all -> 0x0020 }
        r15 = (java.util.concurrent.Future) r15;	 Catch:{ all -> 0x0020 }
        if (r15 != 0) goto L_0x008e;
    L_0x0047:
        if (r3 <= 0) goto L_0x005d;
    L_0x0049:
        r3 = r3 + -1;
        r16 = r9.next();	 Catch:{ all -> 0x0020 }
        r4 = r16;
        r4 = (java.util.concurrent.Callable) r4;	 Catch:{ all -> 0x0020 }
        r4 = submitAndAddQueueListener(r1, r4, r6);	 Catch:{ all -> 0x0020 }
        r5.add(r4);	 Catch:{ all -> 0x0020 }
        r7 = r7 + 1;
        goto L_0x008e;
    L_0x005d:
        if (r7 != 0) goto L_0x0067;
    L_0x005f:
        if (r8 != 0) goto L_0x0066;
    L_0x0061:
        r8 = new java.util.concurrent.ExecutionException;	 Catch:{ all -> 0x0020 }
        r8.<init>(r10);	 Catch:{ all -> 0x0020 }
    L_0x0066:
        throw r8;	 Catch:{ all -> 0x0020 }
    L_0x0067:
        if (r23 == 0) goto L_0x0087;
    L_0x0069:
        r4 = java.util.concurrent.TimeUnit.NANOSECONDS;	 Catch:{ all -> 0x0020 }
        r4 = r6.poll(r11, r4);	 Catch:{ all -> 0x0020 }
        r15 = r4;
        r15 = (java.util.concurrent.Future) r15;	 Catch:{ all -> 0x0020 }
        if (r15 != 0) goto L_0x007a;
    L_0x0074:
        r1 = new java.util.concurrent.TimeoutException;	 Catch:{ all -> 0x0020 }
        r1.<init>();	 Catch:{ all -> 0x0020 }
        throw r1;	 Catch:{ all -> 0x0020 }
    L_0x007a:
        r17 = java.lang.System.nanoTime();	 Catch:{ all -> 0x0020 }
        r4 = 0;
        r19 = r17 - r13;
        r13 = r11 - r19;
        r11 = r13;
        r13 = r17;
        goto L_0x008e;
    L_0x0087:
        r4 = r6.take();	 Catch:{ all -> 0x0020 }
        r15 = r4;
        r15 = (java.util.concurrent.Future) r15;	 Catch:{ all -> 0x0020 }
    L_0x008e:
        if (r15 == 0) goto L_0x003f;
    L_0x0090:
        r7 = r7 + -1;
        r4 = r15.get();	 Catch:{ ExecutionException -> 0x00b4, RuntimeException -> 0x00ac }
        r1 = r5.iterator();
    L_0x009a:
        r2 = r1.hasNext();
        if (r2 == 0) goto L_0x00ab;
    L_0x00a0:
        r2 = r1.next();
        r2 = (java.util.concurrent.Future) r2;
        r3 = 1;
        r2.cancel(r3);
        goto L_0x009a;
    L_0x00ab:
        return r4;
    L_0x00ac:
        r0 = move-exception;
        r4 = new java.util.concurrent.ExecutionException;	 Catch:{ all -> 0x0020 }
        r4.<init>(r0);	 Catch:{ all -> 0x0020 }
        r8 = r4;
        goto L_0x003f;
    L_0x00b4:
        r0 = move-exception;
        r8 = r0;
        goto L_0x003f;
    L_0x00b7:
        r2 = r5.iterator();
    L_0x00bb:
        r3 = r2.hasNext();
        if (r3 == 0) goto L_0x00cc;
    L_0x00c1:
        r3 = r2.next();
        r3 = (java.util.concurrent.Future) r3;
        r4 = 1;
        r3.cancel(r4);
        goto L_0x00bb;
    L_0x00cc:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.MoreExecutors.invokeAnyImpl(com.google.common.util.concurrent.ListeningExecutorService, java.util.Collection, boolean, long):T");
    }

    private static <T> ListenableFuture<T> submitAndAddQueueListener(ListeningExecutorService listeningExecutorService, Callable<T> callable, final BlockingQueue<Future<T>> blockingQueue) {
        listeningExecutorService = listeningExecutorService.submit(callable);
        listeningExecutorService.addListener(new Runnable() {
            public void run() {
                blockingQueue.add(listeningExecutorService);
            }
        }, directExecutor());
        return listeningExecutorService;
    }

    @Beta
    public static ThreadFactory platformThreadFactory() {
        if (!isAppEngine()) {
            return Executors.defaultThreadFactory();
        }
        try {
            return (ThreadFactory) Class.forName("com.google.appengine.api.ThreadManager").getMethod("currentRequestThreadFactory", new Class[0]).invoke(null, new Object[0]);
        } catch (Throwable e) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
        } catch (Throwable e2) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e2);
        } catch (Throwable e22) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e22);
        } catch (InvocationTargetException e3) {
            throw Throwables.propagate(e3.getCause());
        }
    }

    private static boolean isAppEngine() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = "com.google.appengine.runtime.environment";
        r0 = java.lang.System.getProperty(r0);
        r1 = 0;
        if (r0 != 0) goto L_0x000a;
    L_0x0009:
        return r1;
    L_0x000a:
        r0 = "com.google.apphosting.api.ApiProxy";	 Catch:{ ClassNotFoundException -> 0x0026, InvocationTargetException -> 0x0025, IllegalAccessException -> 0x0024, NoSuchMethodException -> 0x0023 }
        r0 = java.lang.Class.forName(r0);	 Catch:{ ClassNotFoundException -> 0x0026, InvocationTargetException -> 0x0025, IllegalAccessException -> 0x0024, NoSuchMethodException -> 0x0023 }
        r2 = "getCurrentEnvironment";	 Catch:{ ClassNotFoundException -> 0x0026, InvocationTargetException -> 0x0025, IllegalAccessException -> 0x0024, NoSuchMethodException -> 0x0023 }
        r3 = new java.lang.Class[r1];	 Catch:{ ClassNotFoundException -> 0x0026, InvocationTargetException -> 0x0025, IllegalAccessException -> 0x0024, NoSuchMethodException -> 0x0023 }
        r0 = r0.getMethod(r2, r3);	 Catch:{ ClassNotFoundException -> 0x0026, InvocationTargetException -> 0x0025, IllegalAccessException -> 0x0024, NoSuchMethodException -> 0x0023 }
        r2 = 0;	 Catch:{ ClassNotFoundException -> 0x0026, InvocationTargetException -> 0x0025, IllegalAccessException -> 0x0024, NoSuchMethodException -> 0x0023 }
        r3 = new java.lang.Object[r1];	 Catch:{ ClassNotFoundException -> 0x0026, InvocationTargetException -> 0x0025, IllegalAccessException -> 0x0024, NoSuchMethodException -> 0x0023 }
        r0 = r0.invoke(r2, r3);	 Catch:{ ClassNotFoundException -> 0x0026, InvocationTargetException -> 0x0025, IllegalAccessException -> 0x0024, NoSuchMethodException -> 0x0023 }
        if (r0 == 0) goto L_0x0022;
    L_0x0021:
        r1 = 1;
    L_0x0022:
        return r1;
    L_0x0023:
        return r1;
    L_0x0024:
        return r1;
    L_0x0025:
        return r1;
    L_0x0026:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.MoreExecutors.isAppEngine():boolean");
    }

    static java.lang.Thread newThread(java.lang.String r1, java.lang.Runnable r2) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        com.google.common.base.Preconditions.checkNotNull(r1);
        com.google.common.base.Preconditions.checkNotNull(r2);
        r0 = platformThreadFactory();
        r2 = r0.newThread(r2);
        r2.setName(r1);	 Catch:{ SecurityException -> 0x0011 }
    L_0x0011:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.MoreExecutors.newThread(java.lang.String, java.lang.Runnable):java.lang.Thread");
    }

    static Executor renamingDecorator(final Executor executor, final Supplier<String> supplier) {
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(supplier);
        if (isAppEngine()) {
            return executor;
        }
        return new Executor() {
            public void execute(Runnable runnable) {
                executor.execute(Callables.threadRenaming(runnable, supplier));
            }
        };
    }

    static ExecutorService renamingDecorator(ExecutorService executorService, final Supplier<String> supplier) {
        Preconditions.checkNotNull(executorService);
        Preconditions.checkNotNull(supplier);
        if (isAppEngine()) {
            return executorService;
        }
        return new WrappingExecutorService(executorService) {
            protected <T> Callable<T> wrapTask(Callable<T> callable) {
                return Callables.threadRenaming(callable, supplier);
            }

            protected Runnable wrapTask(Runnable runnable) {
                return Callables.threadRenaming(runnable, supplier);
            }
        };
    }

    static ScheduledExecutorService renamingDecorator(ScheduledExecutorService scheduledExecutorService, final Supplier<String> supplier) {
        Preconditions.checkNotNull(scheduledExecutorService);
        Preconditions.checkNotNull(supplier);
        if (isAppEngine()) {
            return scheduledExecutorService;
        }
        return new WrappingScheduledExecutorService(scheduledExecutorService) {
            protected <T> Callable<T> wrapTask(Callable<T> callable) {
                return Callables.threadRenaming(callable, supplier);
            }

            protected Runnable wrapTask(Runnable runnable) {
                return Callables.threadRenaming(runnable, supplier);
            }
        };
    }

    @com.google.common.annotations.Beta
    public static boolean shutdownAndAwaitTermination(java.util.concurrent.ExecutorService r2, long r3, java.util.concurrent.TimeUnit r5) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        com.google.common.base.Preconditions.checkNotNull(r5);
        r2.shutdown();
        r0 = java.util.concurrent.TimeUnit.NANOSECONDS;	 Catch:{ InterruptedException -> 0x0020 }
        r3 = r0.convert(r3, r5);	 Catch:{ InterruptedException -> 0x0020 }
        r0 = 2;	 Catch:{ InterruptedException -> 0x0020 }
        r3 = r3 / r0;	 Catch:{ InterruptedException -> 0x0020 }
        r5 = java.util.concurrent.TimeUnit.NANOSECONDS;	 Catch:{ InterruptedException -> 0x0020 }
        r5 = r2.awaitTermination(r3, r5);	 Catch:{ InterruptedException -> 0x0020 }
        if (r5 != 0) goto L_0x002a;	 Catch:{ InterruptedException -> 0x0020 }
    L_0x0017:
        r2.shutdownNow();	 Catch:{ InterruptedException -> 0x0020 }
        r5 = java.util.concurrent.TimeUnit.NANOSECONDS;	 Catch:{ InterruptedException -> 0x0020 }
        r2.awaitTermination(r3, r5);	 Catch:{ InterruptedException -> 0x0020 }
        goto L_0x002a;
    L_0x0020:
        r3 = java.lang.Thread.currentThread();
        r3.interrupt();
        r2.shutdownNow();
    L_0x002a:
        r2 = r2.isTerminated();
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.MoreExecutors.shutdownAndAwaitTermination(java.util.concurrent.ExecutorService, long, java.util.concurrent.TimeUnit):boolean");
    }
}
