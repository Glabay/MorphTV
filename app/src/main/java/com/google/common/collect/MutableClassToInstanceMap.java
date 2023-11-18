package com.google.common.collect;

import com.google.common.primitives.Primitives;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class MutableClassToInstanceMap<B> extends ConstrainedMap<Class<? extends B>, B> implements ClassToInstanceMap<B> {
    private static final MapConstraint<Class<?>, Object> VALUE_CAN_BE_CAST_TO_KEY = new C10431();
    private static final long serialVersionUID = 0;

    /* renamed from: com.google.common.collect.MutableClassToInstanceMap$1 */
    static class C10431 implements MapConstraint<Class<?>, Object> {
        C10431() {
        }

        public void checkKeyValue(Class<?> cls, Object obj) {
            MutableClassToInstanceMap.cast(cls, obj);
        }
    }

    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    public /* bridge */ /* synthetic */ void putAll(Map map) {
        super.putAll(map);
    }

    public static <B> MutableClassToInstanceMap<B> create() {
        return new MutableClassToInstanceMap(new HashMap());
    }

    public static <B> MutableClassToInstanceMap<B> create(Map<Class<? extends B>, B> map) {
        return new MutableClassToInstanceMap(map);
    }

    private MutableClassToInstanceMap(Map<Class<? extends B>, B> map) {
        super(map, VALUE_CAN_BE_CAST_TO_KEY);
    }

    public <T extends B> T putInstance(Class<T> cls, T t) {
        return cast(cls, put(cls, t));
    }

    public <T extends B> T getInstance(Class<T> cls) {
        return cast(cls, get(cls));
    }

    private static <B, T extends B> T cast(Class<T> cls, B b) {
        return Primitives.wrap(cls).cast(b);
    }
}
