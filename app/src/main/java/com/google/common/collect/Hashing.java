package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
final class Hashing {
    private static final int C1 = -862048943;
    private static final int C2 = 461845907;
    private static int MAX_TABLE_SIZE = 1073741824;

    private Hashing() {
    }

    static int smear(int i) {
        return Integer.rotateLeft(i * C1, 15) * C2;
    }

    static int smearedHash(@Nullable Object obj) {
        return smear(obj == null ? null : obj.hashCode());
    }

    static int closedTableSize(int i, double d) {
        i = Math.max(i, 2);
        int highestOneBit = Integer.highestOneBit(i);
        if (i <= ((int) (d * ((double) highestOneBit)))) {
            return highestOneBit;
        }
        i = highestOneBit << 1;
        if (i <= 0) {
            i = MAX_TABLE_SIZE;
        }
        return i;
    }

    static boolean needsResizing(int i, int i2, double d) {
        return ((double) i) > d * ((double) i2) && i2 < MAX_TABLE_SIZE;
    }
}
