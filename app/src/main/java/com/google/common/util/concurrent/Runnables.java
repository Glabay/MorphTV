package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
@Beta
public final class Runnables {
    private static final Runnable EMPTY_RUNNABLE = new C12371();

    /* renamed from: com.google.common.util.concurrent.Runnables$1 */
    static class C12371 implements Runnable {
        public void run() {
        }

        C12371() {
        }
    }

    public static Runnable doNothing() {
        return EMPTY_RUNNABLE;
    }

    private Runnables() {
    }
}
