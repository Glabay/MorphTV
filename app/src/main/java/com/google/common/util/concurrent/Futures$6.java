package com.google.common.util.concurrent;

import java.util.concurrent.ExecutionException;

class Futures$6 implements Runnable {
    final /* synthetic */ FutureCallback val$callback;
    final /* synthetic */ ListenableFuture val$future;

    Futures$6(ListenableFuture listenableFuture, FutureCallback futureCallback) {
        this.val$future = listenableFuture;
        this.val$callback = futureCallback;
    }

    public void run() {
        try {
            this.val$callback.onSuccess(Uninterruptibles.getUninterruptibly(this.val$future));
        } catch (ExecutionException e) {
            this.val$callback.onFailure(e.getCause());
        } catch (Throwable e2) {
            this.val$callback.onFailure(e2);
        } catch (Throwable e22) {
            this.val$callback.onFailure(e22);
        }
    }
}
