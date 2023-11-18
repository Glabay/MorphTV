package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.lang.ref.WeakReference;

@GwtCompatible(emulated = true)
final class Platform {
    private Platform() {
    }

    static long systemNanoTime() {
        return System.nanoTime();
    }

    static CharMatcher precomputeCharMatcher(CharMatcher charMatcher) {
        return charMatcher.precomputedInternal();
    }

    static <T extends Enum<T>> Optional<T> getEnumIfPresent(Class<T> cls, String str) {
        WeakReference weakReference = (WeakReference) Enums.getEnumConstants(cls).get(str);
        return weakReference == null ? Optional.absent() : Optional.of(cls.cast(weakReference.get()));
    }
}
