package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingSet<E> extends ForwardingCollection<E> implements Set<E> {
    protected abstract Set<E> delegate();

    protected ForwardingSet() {
    }

    public boolean equals(@Nullable Object obj) {
        if (obj != this) {
            if (delegate().equals(obj) == null) {
                return null;
            }
        }
        return true;
    }

    public int hashCode() {
        return delegate().hashCode();
    }

    protected boolean standardRemoveAll(Collection<?> collection) {
        return Sets.removeAllImpl((Set) this, (Collection) Preconditions.checkNotNull(collection));
    }

    protected boolean standardEquals(@Nullable Object obj) {
        return Sets.equalsImpl(this, obj);
    }

    protected int standardHashCode() {
        return Sets.hashCodeImpl(this);
    }
}
