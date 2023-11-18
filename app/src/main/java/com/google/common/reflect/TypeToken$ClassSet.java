package com.google.common.reflect;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken.TypeCollector;
import java.util.Set;

final class TypeToken$ClassSet extends TypeToken$TypeSet {
    private static final long serialVersionUID = 0;
    private transient ImmutableSet<TypeToken<? super T>> classes;
    final /* synthetic */ TypeToken this$0;

    public TypeToken$TypeSet classes() {
        return this;
    }

    private TypeToken$ClassSet(TypeToken typeToken) {
        this.this$0 = typeToken;
        super(typeToken);
    }

    protected Set<TypeToken<? super T>> delegate() {
        Set set = this.classes;
        if (set != null) {
            return set;
        }
        set = FluentIterable.from(TypeCollector.FOR_GENERIC_TYPE.classesOnly().collectTypes(this.this$0)).filter(TypeToken$TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet();
        this.classes = set;
        return set;
    }

    public Set<Class<? super T>> rawTypes() {
        return ImmutableSet.copyOf(TypeCollector.FOR_RAW_TYPE.classesOnly().collectTypes(TypeToken.access$200(this.this$0)));
    }

    public TypeToken$TypeSet interfaces() {
        throw new UnsupportedOperationException("classes().interfaces() not supported.");
    }

    private Object readResolve() {
        return this.this$0.getTypes().classes();
    }
}
