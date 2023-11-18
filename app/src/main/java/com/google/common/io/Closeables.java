package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

@Beta
public final class Closeables {
    @VisibleForTesting
    static final Logger logger = Logger.getLogger(Closeables.class.getName());

    private Closeables() {
    }

    public static void close(@Nullable Closeable closeable, boolean z) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Closeable closeable2) {
                if (z) {
                    logger.log(Level.WARNING, "IOException thrown while closing Closeable.", closeable2);
                } else {
                    throw closeable2;
                }
            }
        }
    }

    public static void closeQuietly(@Nullable InputStream inputStream) {
        try {
            close(inputStream, true);
        } catch (InputStream inputStream2) {
            throw new AssertionError(inputStream2);
        }
    }

    public static void closeQuietly(@Nullable Reader reader) {
        try {
            close(reader, true);
        } catch (Reader reader2) {
            throw new AssertionError(reader2);
        }
    }
}
