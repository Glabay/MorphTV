package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

@Beta
public final class JdkFutureAdapters {

    private static class ListenableFutureAdapter<V> extends ForwardingFuture<V> implements ListenableFuture<V> {
        private static final Executor defaultAdapterExecutor = Executors.newCachedThreadPool(threadFactory);
        private static final ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ListenableFutureAdapter-thread-%d").build();
        private final Executor adapterExecutor;
        private final Future<V> delegate;
        private final ExecutionList executionList;
        private final AtomicBoolean hasListeners;

        /* renamed from: com.google.common.util.concurrent.JdkFutureAdapters$ListenableFutureAdapter$1 */
        class C12301 implements Runnable {
            C12301() {
            }

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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
                /*
                r1 = this;
                r0 = com.google.common.util.concurrent.JdkFutureAdapters.ListenableFutureAdapter.this;	 Catch:{ Error -> 0x0013, Throwable -> 0x0009 }
                r0 = r0.delegate;	 Catch:{ Error -> 0x0013, Throwable -> 0x0009 }
                com.google.common.util.concurrent.Uninterruptibles.getUninterruptibly(r0);	 Catch:{ Error -> 0x0013, Throwable -> 0x0009 }
            L_0x0009:
                r0 = com.google.common.util.concurrent.JdkFutureAdapters.ListenableFutureAdapter.this;
                r0 = r0.executionList;
                r0.execute();
                return;
            L_0x0013:
                r0 = move-exception;
                throw r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.JdkFutureAdapters.ListenableFutureAdapter.1.run():void");
            }
        }

        ListenableFutureAdapter(Future<V> future) {
            this(future, defaultAdapterExecutor);
        }

        ListenableFutureAdapter(Future<V> future, Executor executor) {
            this.executionList = new ExecutionList();
            this.hasListeners = new AtomicBoolean(false);
            this.delegate = (Future) Preconditions.checkNotNull(future);
            this.adapterExecutor = (Executor) Preconditions.checkNotNull(executor);
        }

        protected Future<V> delegate() {
            return this.delegate;
        }

        public void addListener(Runnable runnable, Executor executor) {
            this.executionList.add(runnable, executor);
            if (this.hasListeners.compareAndSet(null, true) != null) {
                if (this.delegate.isDone() != null) {
                    this.executionList.execute();
                    return;
                }
                this.adapterExecutor.execute(new C12301());
            }
        }
    }

    public static <V> ListenableFuture<V> listenInPoolThread(Future<V> future) {
        if (future instanceof ListenableFuture) {
            return (ListenableFuture) future;
        }
        return new ListenableFutureAdapter(future);
    }

    public static <V> ListenableFuture<V> listenInPoolThread(Future<V> future, Executor executor) {
        Preconditions.checkNotNull(executor);
        if (future instanceof ListenableFuture) {
            return (ListenableFuture) future;
        }
        return new ListenableFutureAdapter(future, executor);
    }

    private JdkFutureAdapters() {
    }
}
