package org.apache.commons.lang3.tuple;

import java.io.Serializable;
import java.util.Map.Entry;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

public abstract class Pair<L, R> implements Entry<L, R>, Comparable<Pair<L, R>>, Serializable {
    private static final long serialVersionUID = 4954918890077093841L;

    public abstract L getLeft();

    public abstract R getRight();

    public static <L, R> Pair<L, R> of(L l, R r) {
        return new ImmutablePair(l, r);
    }

    public final L getKey() {
        return getLeft();
    }

    public R getValue() {
        return getRight();
    }

    public int compareTo(Pair<L, R> pair) {
        return new CompareToBuilder().append(getLeft(), pair.getLeft()).append(getRight(), pair.getRight()).toComparison();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Entry)) {
            return false;
        }
        Entry entry = (Entry) obj;
        if (!ObjectUtils.equals(getKey(), entry.getKey()) || ObjectUtils.equals(getValue(), entry.getValue()) == null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = getKey() == null ? 0 : getKey().hashCode();
        if (getValue() != null) {
            i = getValue().hashCode();
        }
        return hashCode ^ i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('(');
        stringBuilder.append(getLeft());
        stringBuilder.append(',');
        stringBuilder.append(getRight());
        stringBuilder.append(')');
        return stringBuilder.toString();
    }

    public String toString(String str) {
        return String.format(str, new Object[]{getLeft(), getRight()});
    }
}
