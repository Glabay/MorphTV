package android.support.v4.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public class Pair<F, S> {
    @Nullable
    public final F first;
    @Nullable
    public final S second;

    public Pair(@Nullable F f, @Nullable S s) {
        this.first = f;
        this.second = s;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair pair = (Pair) obj;
        if (objectsEqual(pair.first, this.first) && objectsEqual(pair.second, this.second) != null) {
            z = true;
        }
        return z;
    }

    private static boolean objectsEqual(Object obj, Object obj2) {
        if (obj != obj2) {
            if (obj == null || obj.equals(obj2) == null) {
                return null;
            }
        }
        return true;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = this.first == null ? 0 : this.first.hashCode();
        if (this.second != null) {
            i = this.second.hashCode();
        }
        return hashCode ^ i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Pair{");
        stringBuilder.append(String.valueOf(this.first));
        stringBuilder.append(StringUtils.SPACE);
        stringBuilder.append(String.valueOf(this.second));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @NonNull
    public static <A, B> Pair<A, B> create(@Nullable A a, @Nullable B b) {
        return new Pair(a, b);
    }
}
