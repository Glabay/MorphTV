package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Iterator;
import javax.annotation.Nullable;

@Beta
public final class Parameter implements AnnotatedElement {
    private final ImmutableList<Annotation> annotations;
    private final Invokable<?, ?> declaration;
    private final int position;
    private final TypeToken<?> type;

    Parameter(Invokable<?, ?> invokable, int i, TypeToken<?> typeToken, Annotation[] annotationArr) {
        this.declaration = invokable;
        this.position = i;
        this.type = typeToken;
        this.annotations = ImmutableList.copyOf((Object[]) annotationArr);
    }

    public TypeToken<?> getType() {
        return this.type;
    }

    public Invokable<?, ?> getDeclaringInvokable() {
        return this.declaration;
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> cls) {
        return getAnnotation(cls) != null ? true : null;
    }

    @Nullable
    public <A extends Annotation> A getAnnotation(Class<A> cls) {
        Preconditions.checkNotNull(cls);
        Iterator it = this.annotations.iterator();
        while (it.hasNext()) {
            Annotation annotation = (Annotation) it.next();
            if (cls.isInstance(annotation)) {
                return (Annotation) cls.cast(annotation);
            }
        }
        return null;
    }

    public Annotation[] getAnnotations() {
        return getDeclaredAnnotations();
    }

    public <A extends Annotation> A[] getAnnotationsByType(Class<A> cls) {
        return getDeclaredAnnotationsByType(cls);
    }

    public Annotation[] getDeclaredAnnotations() {
        return (Annotation[]) this.annotations.toArray(new Annotation[this.annotations.size()]);
    }

    @Nullable
    public <A extends Annotation> A getDeclaredAnnotation(Class<A> cls) {
        Preconditions.checkNotNull(cls);
        return (Annotation) FluentIterable.from(this.annotations).filter((Class) cls).first().orNull();
    }

    public <A extends Annotation> A[] getDeclaredAnnotationsByType(Class<A> cls) {
        return (Annotation[]) FluentIterable.from(this.annotations).filter((Class) cls).toArray(cls);
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = false;
        if (!(obj instanceof Parameter)) {
            return false;
        }
        Parameter parameter = (Parameter) obj;
        if (this.position == parameter.position && this.declaration.equals(parameter.declaration) != null) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return this.position;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.type);
        stringBuilder.append(" arg");
        stringBuilder.append(this.position);
        return stringBuilder.toString();
    }
}
