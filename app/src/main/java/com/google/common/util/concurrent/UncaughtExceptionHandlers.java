package com.google.common.util.concurrent;

import com.google.common.annotations.VisibleForTesting;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class UncaughtExceptionHandlers {

    @VisibleForTesting
    static final class Exiter implements UncaughtExceptionHandler {
        private static final Logger logger = Logger.getLogger(Exiter.class.getName());
        private final Runtime runtime;

        Exiter(Runtime runtime) {
            this.runtime = runtime;
        }

        public void uncaughtException(Thread thread, Throwable th) {
            try {
                logger.log(Level.SEVERE, String.format("Caught an exception in %s.  Shutting down.", new Object[]{thread}), th);
            } catch (Throwable th2) {
                this.runtime.exit(1);
            }
            this.runtime.exit(1);
        }
    }

    private UncaughtExceptionHandlers() {
    }

    public static UncaughtExceptionHandler systemExit() {
        return new Exiter(Runtime.getRuntime());
    }
}
