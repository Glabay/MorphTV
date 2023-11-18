package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Ordering;
import com.google.common.collect.Queues;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

@Beta
public final class Futures {
    private static final AsyncFunction<ListenableFuture<Object>, Object> DEREFERENCER = new Futures$4();
    private static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST = Ordering.natural().onResultOf(new Futures$7()).reverse();

    private static class ChainingListenableFuture<I, O> extends AbstractFuture<O> implements Runnable {
        private AsyncFunction<? super I, ? extends O> function;
        private ListenableFuture<? extends I> inputFuture;
        private volatile ListenableFuture<? extends O> outputFuture;

        private ChainingListenableFuture(AsyncFunction<? super I, ? extends O> asyncFunction, ListenableFuture<? extends I> listenableFuture) {
            this.function = (AsyncFunction) Preconditions.checkNotNull(asyncFunction);
            this.inputFuture = (ListenableFuture) Preconditions.checkNotNull(listenableFuture);
        }

        public boolean cancel(boolean z) {
            if (!super.cancel(z)) {
                return false;
            }
            cancel(this.inputFuture, z);
            cancel(this.outputFuture, z);
            return true;
        }

        private void cancel(@Nullable Future<?> future, boolean z) {
            if (future != null) {
                future.cancel(z);
            }
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
            r0 = 0;
            r1 = r4.inputFuture;	 Catch:{ CancellationException -> 0x0047, ExecutionException -> 0x003e }
            r1 = com.google.common.util.concurrent.Uninterruptibles.getUninterruptibly(r1);	 Catch:{ CancellationException -> 0x0047, ExecutionException -> 0x003e }
            r2 = r4.function;	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            r1 = r2.apply(r1);	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            r2 = "AsyncFunction may not return null.";	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            r1 = com.google.common.base.Preconditions.checkNotNull(r1, r2);	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            r1 = (com.google.common.util.concurrent.ListenableFuture) r1;	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            r4.outputFuture = r1;	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            r2 = r4.isCancelled();	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            if (r2 == 0) goto L_0x002b;	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
        L_0x001d:
            r2 = r4.wasInterrupted();	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            r1.cancel(r2);	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            r4.outputFuture = r0;	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
        L_0x0026:
            r4.function = r0;
            r4.inputFuture = r0;
            return;
        L_0x002b:
            r2 = new com.google.common.util.concurrent.Futures$ChainingListenableFuture$1;	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            r2.<init>(r4, r1);	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            r3 = com.google.common.util.concurrent.MoreExecutors.directExecutor();	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            r1.addListener(r2, r3);	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            goto L_0x004f;	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
        L_0x0038:
            r1 = move-exception;	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            goto L_0x005d;	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
        L_0x003a:
            r1 = move-exception;	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            goto L_0x004c;	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
        L_0x003c:
            r1 = move-exception;	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            goto L_0x0054;	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
        L_0x003e:
            r1 = move-exception;	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            r1 = r1.getCause();	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            r4.setException(r1);	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            goto L_0x0026;	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
        L_0x0047:
            r1 = 0;	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            r4.cancel(r1);	 Catch:{ UndeclaredThrowableException -> 0x003c, Throwable -> 0x003a }
            goto L_0x0026;
        L_0x004c:
            r4.setException(r1);	 Catch:{ all -> 0x0038 }
        L_0x004f:
            r4.function = r0;
            r4.inputFuture = r0;
            goto L_0x005c;
        L_0x0054:
            r1 = r1.getCause();	 Catch:{ all -> 0x0038 }
            r4.setException(r1);	 Catch:{ all -> 0x0038 }
            goto L_0x004f;
        L_0x005c:
            return;
        L_0x005d:
            r4.function = r0;
            r4.inputFuture = r0;
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Futures.ChainingListenableFuture.run():void");
        }
    }

    private static abstract class ImmediateFuture<V> implements ListenableFuture<V> {
        private static final Logger log = Logger.getLogger(ImmediateFuture.class.getName());

        public boolean cancel(boolean z) {
            return false;
        }

        public abstract V get() throws ExecutionException;

        public boolean isCancelled() {
            return false;
        }

        public boolean isDone() {
            return true;
        }

        private ImmediateFuture() {
        }

        public void addListener(Runnable runnable, Executor executor) {
            Preconditions.checkNotNull(runnable, "Runnable was null.");
            Preconditions.checkNotNull(executor, "Executor was null.");
            try {
                executor.execute(runnable);
            } catch (Throwable e) {
                Logger logger = log;
                Level level = Level.SEVERE;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("RuntimeException while executing runnable ");
                stringBuilder.append(runnable);
                stringBuilder.append(" with executor ");
                stringBuilder.append(executor);
                logger.log(level, stringBuilder.toString(), e);
            }
        }

        public V get(long j, TimeUnit timeUnit) throws ExecutionException {
            Preconditions.checkNotNull(timeUnit);
            return get();
        }
    }

    private static class ImmediateSuccessfulCheckedFuture<V, X extends Exception> extends ImmediateFuture<V> implements CheckedFuture<V, X> {
        @Nullable
        private final V value;

        ImmediateSuccessfulCheckedFuture(@Nullable V v) {
            super();
            this.value = v;
        }

        public V get() {
            return this.value;
        }

        public V checkedGet() {
            return this.value;
        }

        public V checkedGet(long j, TimeUnit timeUnit) {
            Preconditions.checkNotNull(timeUnit);
            return this.value;
        }
    }

    private static class ImmediateSuccessfulFuture<V> extends ImmediateFuture<V> {
        @Nullable
        private final V value;

        ImmediateSuccessfulFuture(@Nullable V v) {
            super();
            this.value = v;
        }

        public V get() {
            return this.value;
        }
    }

    private Futures() {
    }

    public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(ListenableFuture<V> listenableFuture, Function<? super Exception, X> function) {
        return new Futures$MappingCheckedFuture((ListenableFuture) Preconditions.checkNotNull(listenableFuture), function);
    }

    public static <V> ListenableFuture<V> immediateFuture(@Nullable V v) {
        return new ImmediateSuccessfulFuture(v);
    }

    public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(@Nullable V v) {
        return new ImmediateSuccessfulCheckedFuture(v);
    }

    public static <V> ListenableFuture<V> immediateFailedFuture(Throwable th) {
        Preconditions.checkNotNull(th);
        return new Futures$ImmediateFailedFuture(th);
    }

    public static <V> ListenableFuture<V> immediateCancelledFuture() {
        return new Futures$ImmediateCancelledFuture();
    }

    public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(X x) {
        Preconditions.checkNotNull(x);
        return new Futures$ImmediateFailedCheckedFuture(x);
    }

    public static <V> ListenableFuture<V> withFallback(ListenableFuture<? extends V> listenableFuture, FutureFallback<? extends V> futureFallback) {
        return withFallback(listenableFuture, futureFallback, MoreExecutors.directExecutor());
    }

    public static <V> ListenableFuture<V> withFallback(ListenableFuture<? extends V> listenableFuture, FutureFallback<? extends V> futureFallback, Executor executor) {
        Preconditions.checkNotNull(futureFallback);
        return new Futures$FallbackFuture(listenableFuture, futureFallback, executor);
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> listenableFuture, AsyncFunction<? super I, ? extends O> asyncFunction) {
        Object chainingListenableFuture = new ChainingListenableFuture(asyncFunction, listenableFuture);
        listenableFuture.addListener(chainingListenableFuture, MoreExecutors.directExecutor());
        return chainingListenableFuture;
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> listenableFuture, AsyncFunction<? super I, ? extends O> asyncFunction, Executor executor) {
        Preconditions.checkNotNull(executor);
        ListenableFuture<O> chainingListenableFuture = new ChainingListenableFuture(asyncFunction, listenableFuture);
        listenableFuture.addListener(rejectionPropagatingRunnable(chainingListenableFuture, chainingListenableFuture, executor), MoreExecutors.directExecutor());
        return chainingListenableFuture;
    }

    private static Runnable rejectionPropagatingRunnable(AbstractFuture<?> abstractFuture, Runnable runnable, Executor executor) {
        return new Futures$1(executor, runnable, abstractFuture);
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> listenableFuture, Function<? super I, ? extends O> function) {
        Preconditions.checkNotNull(function);
        Object chainingListenableFuture = new ChainingListenableFuture(asAsyncFunction(function), listenableFuture);
        listenableFuture.addListener(chainingListenableFuture, MoreExecutors.directExecutor());
        return chainingListenableFuture;
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> listenableFuture, Function<? super I, ? extends O> function, Executor executor) {
        Preconditions.checkNotNull(function);
        return transform((ListenableFuture) listenableFuture, asAsyncFunction(function), executor);
    }

    private static <I, O> AsyncFunction<I, O> asAsyncFunction(Function<? super I, ? extends O> function) {
        return new Futures$2(function);
    }

    public static <I, O> Future<O> lazyTransform(Future<I> future, Function<? super I, ? extends O> function) {
        Preconditions.checkNotNull(future);
        Preconditions.checkNotNull(function);
        return new Futures$3(future, function);
    }

    public static <V> ListenableFuture<V> dereference(ListenableFuture<? extends ListenableFuture<? extends V>> listenableFuture) {
        return transform((ListenableFuture) listenableFuture, DEREFERENCER);
    }

    @Beta
    public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V>... listenableFutureArr) {
        return listFuture(ImmutableList.copyOf((Object[]) listenableFutureArr), true, MoreExecutors.directExecutor());
    }

    @Beta
    public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> iterable) {
        return listFuture(ImmutableList.copyOf((Iterable) iterable), true, MoreExecutors.directExecutor());
    }

    public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> listenableFuture) {
        return new Futures$NonCancellationPropagatingFuture(listenableFuture);
    }

    @Beta
    public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V>... listenableFutureArr) {
        return listFuture(ImmutableList.copyOf((Object[]) listenableFutureArr), false, MoreExecutors.directExecutor());
    }

    @Beta
    public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> iterable) {
        return listFuture(ImmutableList.copyOf((Iterable) iterable), false, MoreExecutors.directExecutor());
    }

    @Beta
    public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(Iterable<? extends ListenableFuture<? extends T>> iterable) {
        ConcurrentLinkedQueue newConcurrentLinkedQueue = Queues.newConcurrentLinkedQueue();
        Builder builder = ImmutableList.builder();
        Executor serializingExecutor = new SerializingExecutor(MoreExecutors.directExecutor());
        for (ListenableFuture listenableFuture : iterable) {
            Object create = AsyncSettableFuture.create();
            newConcurrentLinkedQueue.add(create);
            listenableFuture.addListener(new Futures$5(newConcurrentLinkedQueue, listenableFuture), serializingExecutor);
            builder.add(create);
        }
        return builder.build();
    }

    public static <V> void addCallback(ListenableFuture<V> listenableFuture, FutureCallback<? super V> futureCallback) {
        addCallback(listenableFuture, futureCallback, MoreExecutors.directExecutor());
    }

    public static <V> void addCallback(ListenableFuture<V> listenableFuture, FutureCallback<? super V> futureCallback, Executor executor) {
        Preconditions.checkNotNull(futureCallback);
        listenableFuture.addListener(new Futures$6(listenableFuture, futureCallback), executor);
    }

    public static <V, X extends Exception> V get(Future<V> future, Class<X> cls) throws Exception {
        Preconditions.checkNotNull(future);
        Preconditions.checkArgument(RuntimeException.class.isAssignableFrom(cls) ^ true, "Futures.get exception type (%s) must not be a RuntimeException", cls);
        try {
            return future.get();
        } catch (Future<V> future2) {
            Thread.currentThread().interrupt();
            throw newWithCause(cls, future2);
        } catch (Future<V> future22) {
            wrapAndThrowExceptionOrError(future22.getCause(), cls);
            throw new AssertionError();
        }
    }

    public static <V, X extends Exception> V get(Future<V> future, long j, TimeUnit timeUnit, Class<X> cls) throws Exception {
        Preconditions.checkNotNull(future);
        Preconditions.checkNotNull(timeUnit);
        Preconditions.checkArgument(RuntimeException.class.isAssignableFrom(cls) ^ true, "Futures.get exception type (%s) must not be a RuntimeException", cls);
        try {
            return future.get(j, timeUnit);
        } catch (Future<V> future2) {
            Thread.currentThread().interrupt();
            throw newWithCause(cls, future2);
        } catch (Future<V> future22) {
            throw newWithCause(cls, future22);
        } catch (Future<V> future222) {
            wrapAndThrowExceptionOrError(future222.getCause(), cls);
            throw new AssertionError();
        }
    }

    private static <X extends Exception> void wrapAndThrowExceptionOrError(Throwable th, Class<X> cls) throws Exception {
        if (th instanceof Error) {
            throw new ExecutionError((Error) th);
        } else if (th instanceof RuntimeException) {
            throw new UncheckedExecutionException(th);
        } else {
            throw newWithCause(cls, th);
        }
    }

    public static <V> V getUnchecked(Future<V> future) {
        Preconditions.checkNotNull(future);
        try {
            return Uninterruptibles.getUninterruptibly(future);
        } catch (Future<V> future2) {
            wrapAndThrowUnchecked(future2.getCause());
            throw new AssertionError();
        }
    }

    private static void wrapAndThrowUnchecked(Throwable th) {
        if (th instanceof Error) {
            throw new ExecutionError((Error) th);
        }
        throw new UncheckedExecutionException(th);
    }

    private static <X extends Exception> X newWithCause(Class<X> cls, Throwable th) {
        for (Constructor newFromConstructor : preferringStrings(Arrays.asList(cls.getConstructors()))) {
            Exception exception = (Exception) newFromConstructor(newFromConstructor, th);
            if (exception != null) {
                if (exception.getCause() == null) {
                    exception.initCause(th);
                }
                return exception;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No appropriate constructor for exception of type ");
        stringBuilder.append(cls);
        stringBuilder.append(" in response to chained exception");
        throw new IllegalArgumentException(stringBuilder.toString(), th);
    }

    private static <X extends Exception> List<Constructor<X>> preferringStrings(List<Constructor<X>> list) {
        return WITH_STRING_PARAM_FIRST.sortedCopy(list);
    }

    @javax.annotation.Nullable
    private static <X> X newFromConstructor(java.lang.reflect.Constructor<X> r6, java.lang.Throwable r7) {
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
        r0 = r6.getParameterTypes();
        r1 = r0.length;
        r1 = new java.lang.Object[r1];
        r2 = 0;
    L_0x0008:
        r3 = r0.length;
        r4 = 0;
        if (r2 >= r3) goto L_0x002b;
    L_0x000c:
        r3 = r0[r2];
        r5 = java.lang.String.class;
        r5 = r3.equals(r5);
        if (r5 == 0) goto L_0x001d;
    L_0x0016:
        r3 = r7.toString();
        r1[r2] = r3;
        goto L_0x0027;
    L_0x001d:
        r5 = java.lang.Throwable.class;
        r3 = r3.equals(r5);
        if (r3 == 0) goto L_0x002a;
    L_0x0025:
        r1[r2] = r7;
    L_0x0027:
        r2 = r2 + 1;
        goto L_0x0008;
    L_0x002a:
        return r4;
    L_0x002b:
        r6 = r6.newInstance(r1);	 Catch:{ IllegalArgumentException -> 0x0033, InstantiationException -> 0x0032, IllegalAccessException -> 0x0031, InvocationTargetException -> 0x0030 }
        return r6;
    L_0x0030:
        return r4;
    L_0x0031:
        return r4;
    L_0x0032:
        return r4;
    L_0x0033:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Futures.newFromConstructor(java.lang.reflect.Constructor, java.lang.Throwable):X");
    }

    private static <V> ListenableFuture<List<V>> listFuture(ImmutableList<ListenableFuture<? extends V>> immutableList, boolean z, Executor executor) {
        return new Futures$CombinedFuture(immutableList, z, executor, new Futures$8());
    }
}
