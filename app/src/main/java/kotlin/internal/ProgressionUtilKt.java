package kotlin.internal;

import kotlin.Metadata;
import kotlin.PublishedApi;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0006\u001a \u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002\u001a \u0010\u0000\u001a\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0002\u001a \u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u0001H\u0001\u001a \u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u0005H\u0001\u001a\u0018\u0010\n\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0002\u001a\u0018\u0010\n\u001a\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005H\u0002¨\u0006\u000b"}, d2 = {"differenceModulo", "", "a", "b", "c", "", "getProgressionLastElement", "start", "end", "step", "mod", "kotlin-runtime"}, k = 2, mv = {1, 1, 10})
/* compiled from: progressionUtil.kt */
public final class ProgressionUtilKt {
    private static final int mod(int i, int i2) {
        i %= i2;
        return i >= 0 ? i : i + i2;
    }

    private static final long mod(long j, long j2) {
        j %= j2;
        return j >= 0 ? j : j + j2;
    }

    private static final int differenceModulo(int i, int i2, int i3) {
        return mod(mod(i, i3) - mod(i2, i3), i3);
    }

    private static final long differenceModulo(long j, long j2, long j3) {
        return mod(mod(j, j3) - mod(j2, j3), j3);
    }

    @PublishedApi
    public static final int getProgressionLastElement(int i, int i2, int i3) {
        if (i3 > 0) {
            return i2 - differenceModulo(i2, i, i3);
        }
        if (i3 < 0) {
            return i2 + differenceModulo(i, i2, -i3);
        }
        throw ((Throwable) new IllegalArgumentException("Step is zero."));
    }

    @PublishedApi
    public static final long getProgressionLastElement(long j, long j2, long j3) {
        if (j3 > 0) {
            return j2 - differenceModulo(j2, j, j3);
        }
        if (j3 < 0) {
            return j2 + differenceModulo(j, j2, -j3);
        }
        throw ((Throwable) new IllegalArgumentException("Step is zero."));
    }
}
