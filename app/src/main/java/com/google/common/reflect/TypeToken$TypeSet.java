package com.google.common.reflect;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken.TypeCollector;
import java.io.Serializable;
import java.util.Set;

public class TypeToken$TypeSet extends ForwardingSet<TypeToken<? super T>> implements Serializable {
    private static final long serialVersionUID = 0;
    final /* synthetic */ TypeToken this$0;
    private transient ImmutableSet<TypeToken<? super T>> types;

    TypeToken$TypeSet(TypeToken typeToken) {
        this.this$0 = typeToken;
    }

    public TypeToken$TypeSet interfaces() {
        return new TypeToken$InterfaceSet(this.this$0, this);
    }

    public TypeToken$TypeSet classes() {
        return new TypeToken$ClassSet(this.this$0, null);
    }

    protected Set<TypeToken<? super T>> delegate() {
        Set set = this.types;
        if (set != null) {
            return set;
        }
        set = FluentIterable.from(TypeCollector.FOR_GENERIC_TYPE.collectTypes(this.this$0)).filter(TypeToken$TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet();
        this.types = set;
        return set;
    }

    public Set<Class<? super T>> rawTypes() {
        return ImmutableSet.copyOf(TypeCollector.FOR_RAW_TYPE.collectTypes(TypeToken.access$200(this.this$0)));
    }
}
