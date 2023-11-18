package com.google.common.eventbus;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.annotation.Nullable;

final class SubscriberRegistry {
    private static final LoadingCache<Class<?>, ImmutableSet<Class<?>>> flattenHierarchyCache = CacheBuilder.newBuilder().weakKeys().build(new C11262());
    private EventBus bus;
    private Class<? extends Annotation> subscriberAnnotation;
    private final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache = CacheBuilder.newBuilder().weakKeys().build(new C11251());
    private final ConcurrentMap<Class<?>, CopyOnWriteArraySet<Subscriber>> subscribers = Maps.newConcurrentMap();

    /* renamed from: com.google.common.eventbus.SubscriberRegistry$1 */
    class C11251 extends CacheLoader<Class<?>, ImmutableList<Method>> {
        C11251() {
        }

        public ImmutableList<Method> load(Class<?> cls) throws Exception {
            return SubscriberRegistry.this.getAnnotatedMethodsNotCached(cls);
        }
    }

    /* renamed from: com.google.common.eventbus.SubscriberRegistry$2 */
    static class C11262 extends CacheLoader<Class<?>, ImmutableSet<Class<?>>> {
        C11262() {
        }

        public ImmutableSet<Class<?>> load(Class<?> cls) {
            return ImmutableSet.copyOf(TypeToken.of((Class) cls).getTypes().rawTypes());
        }
    }

    private static final class MethodIdentifier {
        private final String name;
        private final List<Class<?>> parameterTypes;

        MethodIdentifier(Method method) {
            this.name = method.getName();
            this.parameterTypes = Arrays.asList(method.getParameterTypes());
        }

        public int hashCode() {
            return Objects.hashCode(this.name, this.parameterTypes);
        }

        public boolean equals(@Nullable Object obj) {
            boolean z = false;
            if (!(obj instanceof MethodIdentifier)) {
                return false;
            }
            MethodIdentifier methodIdentifier = (MethodIdentifier) obj;
            if (this.name.equals(methodIdentifier.name) && this.parameterTypes.equals(methodIdentifier.parameterTypes) != null) {
                z = true;
            }
            return z;
        }
    }

    SubscriberRegistry(EventBus eventBus, Class<? extends Annotation> cls) {
        this.bus = (EventBus) Preconditions.checkNotNull(eventBus);
        this.subscriberAnnotation = cls;
    }

    SubscriberRegistry(Class<? extends Annotation> cls) {
        this.subscriberAnnotation = cls;
    }

    void setBus(EventBus eventBus) {
        this.bus = (EventBus) Preconditions.checkNotNull(eventBus);
    }

    void register(Object obj) {
        for (Entry entry : findAllSubscribers(obj).asMap().entrySet()) {
            Class cls = (Class) entry.getKey();
            Collection collection = (Collection) entry.getValue();
            CopyOnWriteArraySet copyOnWriteArraySet = (CopyOnWriteArraySet) this.subscribers.get(cls);
            if (copyOnWriteArraySet == null) {
                copyOnWriteArraySet = new CopyOnWriteArraySet();
                copyOnWriteArraySet = (CopyOnWriteArraySet) MoreObjects.firstNonNull(this.subscribers.putIfAbsent(cls, copyOnWriteArraySet), copyOnWriteArraySet);
            }
            copyOnWriteArraySet.addAll(collection);
        }
    }

    void unregister(Object obj) {
        for (Entry entry : findAllSubscribers(obj).asMap().entrySet()) {
            Class cls = (Class) entry.getKey();
            Collection collection = (Collection) entry.getValue();
            CopyOnWriteArraySet copyOnWriteArraySet = (CopyOnWriteArraySet) this.subscribers.get(cls);
            if (copyOnWriteArraySet != null) {
                if (!copyOnWriteArraySet.removeAll(collection)) {
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("missing event subscriber for an annotated method. Is ");
            stringBuilder.append(obj);
            stringBuilder.append(" registered?");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    @VisibleForTesting
    Set<Subscriber> getSubscribersForTesting(Class<?> cls) {
        return (Set) MoreObjects.firstNonNull(this.subscribers.get(cls), ImmutableSet.of());
    }

    Iterator<Subscriber> getSubscribers(Object obj) {
        obj = flattenHierarchy(obj.getClass());
        List newArrayListWithCapacity = Lists.newArrayListWithCapacity(obj.size());
        obj = obj.iterator();
        while (obj.hasNext()) {
            CopyOnWriteArraySet copyOnWriteArraySet = (CopyOnWriteArraySet) this.subscribers.get((Class) obj.next());
            if (copyOnWriteArraySet != null) {
                newArrayListWithCapacity.add(copyOnWriteArraySet.iterator());
            }
        }
        return Iterators.concat(newArrayListWithCapacity.iterator());
    }

    private Multimap<Class<?>, Subscriber> findAllSubscribers(Object obj) {
        Multimap<Class<?>, Subscriber> create = HashMultimap.create();
        Iterator it = getAnnotatedMethods(obj.getClass()).iterator();
        while (it.hasNext()) {
            Method method = (Method) it.next();
            create.put(method.getParameterTypes()[0], Subscriber.create(this.bus, obj, method));
        }
        return create;
    }

    private ImmutableList<Method> getAnnotatedMethods(Class<?> cls) {
        return (ImmutableList) this.subscriberMethodsCache.getUnchecked(cls);
    }

    private ImmutableList<Method> getAnnotatedMethodsNotCached(Class<?> cls) {
        cls = TypeToken.of((Class) cls).getTypes().rawTypes();
        Map newHashMap = Maps.newHashMap();
        cls = cls.iterator();
        while (cls.hasNext()) {
            for (Method method : ((Class) cls.next()).getDeclaredMethods()) {
                if (method.isAnnotationPresent(this.subscriberAnnotation) && !method.isSynthetic()) {
                    Preconditions.checkArgument(method.getParameterTypes().length == 1, "Method %s has @Subscribe annotation but has %s parameters.Subscriber methods must have exactly 1 parameter.", method, Integer.valueOf(method.getParameterTypes().length));
                    MethodIdentifier methodIdentifier = new MethodIdentifier(method);
                    if (!newHashMap.containsKey(methodIdentifier)) {
                        newHashMap.put(methodIdentifier, method);
                    }
                }
            }
        }
        return ImmutableList.copyOf(newHashMap.values());
    }

    @VisibleForTesting
    static ImmutableSet<Class<?>> flattenHierarchy(Class<?> cls) {
        try {
            return (ImmutableSet) flattenHierarchyCache.getUnchecked(cls);
        } catch (Class<?> cls2) {
            throw Throwables.propagate(cls2.getCause());
        }
    }
}
