package com.google.common.io;

import com.google.common.annotations.Beta;
import java.io.Flushable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Beta
public final class Flushables {
    private static final Logger logger = Logger.getLogger(Flushables.class.getName());

    private Flushables() {
    }

    public static void flush(Flushable flushable, boolean z) throws IOException {
        try {
            flushable.flush();
        } catch (Flushable flushable2) {
            if (z) {
                logger.log(Level.WARNING, "IOException thrown while flushing Flushable.", flushable2);
                return;
            }
            throw flushable2;
        }
    }

    public static void flushQuietly(Flushable flushable) {
        try {
            flush(flushable, true);
        } catch (Flushable flushable2) {
            logger.log(Level.SEVERE, "IOException should not have been thrown.", flushable2);
        }
    }
}
