package com.google.common.reflect;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken.TypeCollector;
import java.util.Set;

final class TypeToken$InterfaceSet extends TypeToken$TypeSet {
    private static final long serialVersionUID = 0;
    private final transient TypeToken$TypeSet allTypes;
    private transient ImmutableSet<TypeToken<? super T>> interfaces;
    final /* synthetic */ TypeToken this$0;

    /* renamed from: com.google.common.reflect.TypeToken$InterfaceSet$1 */
    class C11851 implements Predicate<Class<?>> {
        C11851() {
        }

        public boolean apply(Class<?> cls) {
            return cls.isInterface();
        }
    }

    public TypeToken$TypeSet interfaces() {
        return this;
    }

    TypeToken$InterfaceSet(TypeToken typeToken, TypeToken$TypeSet typeToken$TypeSet) {
        this.this$0 = typeToken;
        super(typeToken);
        this.allTypes = typeToken$TypeSet;
    }

    protected Set<TypeToken<? super T>> delegate() {
        Set set = this.interfaces;
        if (set != null) {
            return set;
        }
        set = FluentIterable.from(this.allTypes).filter(TypeToken$TypeFilter.INTERFACE_ONLY).toSet();
        this.interfaces = set;
        return set;
    }

    public Set<Class<? super T>> rawTypes() {
        return FluentIterable.from(TypeCollector.FOR_RAW_TYPE.collectTypes(TypeToken.access$200(this.this$0))).filter(new C11851()).toSet();
    }

    public TypeToken$TypeSet classes() {
        throw new UnsupportedOperationException("interfaces().classes() not supported.");
    }

    private Object readResolve() {
        return this.this$0.getTypes().interfaces();
    }
}
