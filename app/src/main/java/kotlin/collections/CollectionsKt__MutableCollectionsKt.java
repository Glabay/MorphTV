package kotlin.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.RandomAccess;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.NotImplementedError;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.TypeCastException;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000t\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u001f\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0010\u001d\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a-\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a&\u0010\t\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000b2\u0006\u0010\f\u001a\u0002H\u0002H\b¢\u0006\u0002\u0010\r\u001a9\u0010\u000e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u00112\u0006\u0010\u0012\u001a\u00020\u0001H\u0002¢\u0006\u0002\b\u0013\u001a9\u0010\u000e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000b2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u00112\u0006\u0010\u0012\u001a\u00020\u0001H\u0002¢\u0006\u0002\b\u0013\u001a(\u0010\u0014\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u0006\u0010\u0015\u001a\u0002H\u0002H\n¢\u0006\u0002\u0010\u0016\u001a.\u0010\u0014\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\n¢\u0006\u0002\u0010\u0017\u001a)\u0010\u0014\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007H\n\u001a)\u0010\u0014\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\n\u001a(\u0010\u0018\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u0006\u0010\u0015\u001a\u0002H\u0002H\n¢\u0006\u0002\u0010\u0016\u001a.\u0010\u0018\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\n¢\u0006\u0002\u0010\u0017\u001a)\u0010\u0018\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007H\n\u001a)\u0010\u0018\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\n\u001a-\u0010\u0019\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u001a*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u0006\u0010\u0015\u001a\u0002H\u0002H\b¢\u0006\u0002\u0010\u001b\u001a&\u0010\u0019\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u001dH\b¢\u0006\u0002\u0010\u001e\u001a-\u0010\u001f\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010\u001f\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u001f\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a.\u0010\u001f\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u001a*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020 H\b\u001a*\u0010\u001f\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u0011\u001a*\u0010\u001f\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000b2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u0011\u001a-\u0010!\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010!\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010!\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a.\u0010!\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u001a*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020 H\b\u001a*\u0010!\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u0011\u001a*\u0010!\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000b2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u0011\u001a\u0015\u0010\"\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\u0003H\u0002¢\u0006\u0002\b#\u001a\u0019\u0010$\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000bH\b\u001a!\u0010$\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000b2\u0006\u0010%\u001a\u00020&H\b\u001a\u001e\u0010'\u001a\b\u0012\u0004\u0012\u0002H\u00020(\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0007\u001a&\u0010'\u001a\b\u0012\u0004\u0012\u0002H\u00020(\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00072\u0006\u0010%\u001a\u00020&H\u0007\u001a\"\u0010)\u001a\u00020\n\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020**\b\u0012\u0004\u0012\u0002H\u00020\u000bH\u0007\u001a3\u0010)\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000b2\u0018\u0010+\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u001d0,H\b\u001a5\u0010)\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000b2\u001a\u0010-\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020.j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`/H\b\u001a4\u00100\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000b2\u001a\u0010-\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020.j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`/H\u0007¨\u00061"}, d2 = {"addAll", "", "T", "", "elements", "", "(Ljava/util/Collection;[Ljava/lang/Object;)Z", "", "Lkotlin/sequences/Sequence;", "fill", "", "", "value", "(Ljava/util/List;Ljava/lang/Object;)V", "filterInPlace", "", "predicate", "Lkotlin/Function1;", "predicateResultToRemove", "filterInPlace$CollectionsKt__MutableCollectionsKt", "minusAssign", "element", "(Ljava/util/Collection;Ljava/lang/Object;)V", "(Ljava/util/Collection;[Ljava/lang/Object;)V", "plusAssign", "remove", "Lkotlin/internal/OnlyInputTypes;", "(Ljava/util/Collection;Ljava/lang/Object;)Z", "index", "", "(Ljava/util/List;I)Ljava/lang/Object;", "removeAll", "", "retainAll", "retainNothing", "retainNothing$CollectionsKt__MutableCollectionsKt", "shuffle", "random", "Ljava/util/Random;", "shuffled", "", "sort", "", "comparison", "Lkotlin/Function2;", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "sortWith", "kotlin-stdlib"}, k = 5, mv = {1, 1, 10}, xi = 1, xs = "kotlin/collections/CollectionsKt")
/* compiled from: MutableCollections.kt */
class CollectionsKt__MutableCollectionsKt extends CollectionsKt__IteratorsKt {
    @InlineOnly
    private static final <T> boolean remove(@NotNull Collection<? extends T> collection, T t) {
        if (collection != null) {
            return TypeIntrinsics.asMutableCollection(collection).remove(t);
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
    }

    @InlineOnly
    private static final <T> boolean removeAll(@NotNull Collection<? extends T> collection, Collection<? extends T> collection2) {
        if (collection != null) {
            return TypeIntrinsics.asMutableCollection(collection).removeAll(collection2);
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
    }

    @InlineOnly
    private static final <T> boolean retainAll(@NotNull Collection<? extends T> collection, Collection<? extends T> collection2) {
        if (collection != null) {
            return TypeIntrinsics.asMutableCollection(collection).retainAll(collection2);
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Use removeAt(index) instead.", replaceWith = @ReplaceWith(expression = "removeAt(index)", imports = {}))
    @InlineOnly
    private static final <T> T remove(@NotNull List<T> list, int i) {
        return list.remove(i);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Use sortWith(comparator) instead.", replaceWith = @ReplaceWith(expression = "this.sortWith(comparator)", imports = {}))
    @InlineOnly
    private static final <T> void sort(@NotNull List<T> list, Comparator<? super T> comparator) {
        throw ((Throwable) new NotImplementedError(null, 1, null));
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Use sortWith(Comparator(comparison)) instead.", replaceWith = @ReplaceWith(expression = "this.sortWith(Comparator(comparison))", imports = {}))
    @InlineOnly
    private static final <T> void sort(@NotNull List<T> list, Function2<? super T, ? super T, Integer> function2) {
        throw ((Throwable) new NotImplementedError(null, 1, null));
    }

    @InlineOnly
    private static final <T> void plusAssign(@NotNull Collection<? super T> collection, T t) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        collection.add(t);
    }

    @InlineOnly
    private static final <T> void plusAssign(@NotNull Collection<? super T> collection, Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        CollectionsKt.addAll(collection, iterable);
    }

    @InlineOnly
    private static final <T> void plusAssign(@NotNull Collection<? super T> collection, T[] tArr) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        CollectionsKt.addAll(collection, tArr);
    }

    @InlineOnly
    private static final <T> void plusAssign(@NotNull Collection<? super T> collection, Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        CollectionsKt.addAll(collection, sequence);
    }

    @InlineOnly
    private static final <T> void minusAssign(@NotNull Collection<? super T> collection, T t) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        collection.remove(t);
    }

    @InlineOnly
    private static final <T> void minusAssign(@NotNull Collection<? super T> collection, Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        CollectionsKt.removeAll(collection, iterable);
    }

    @InlineOnly
    private static final <T> void minusAssign(@NotNull Collection<? super T> collection, T[] tArr) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        CollectionsKt.removeAll(collection, tArr);
    }

    @InlineOnly
    private static final <T> void minusAssign(@NotNull Collection<? super T> collection, Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        CollectionsKt.removeAll(collection, sequence);
    }

    public static final <T> boolean addAll(@NotNull Collection<? super T> collection, @NotNull Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        Intrinsics.checkParameterIsNotNull(iterable, "elements");
        if (iterable instanceof Collection) {
            return collection.addAll((Collection) iterable);
        }
        boolean z = false;
        for (Object add : iterable) {
            if (collection.add(add)) {
                z = true;
            }
        }
        return z;
    }

    public static final <T> boolean addAll(@NotNull Collection<? super T> collection, @NotNull Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        Intrinsics.checkParameterIsNotNull(sequence, "elements");
        boolean z = false;
        for (Object add : sequence) {
            if (collection.add(add)) {
                z = true;
            }
        }
        return z;
    }

    public static final <T> boolean addAll(@NotNull Collection<? super T> collection, @NotNull T[] tArr) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        return collection.addAll(ArraysKt.asList(tArr));
    }

    public static final <T> boolean removeAll(@NotNull Iterable<? extends T> iterable, @NotNull Function1<? super T, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(iterable, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((Iterable) iterable, (Function1) function1, true);
    }

    public static final <T> boolean retainAll(@NotNull Iterable<? extends T> iterable, @NotNull Function1<? super T, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(iterable, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((Iterable) iterable, (Function1) function1, false);
    }

    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(@NotNull Iterable<? extends T> iterable, Function1<? super T, Boolean> function1, boolean z) {
        iterable = iterable.iterator();
        boolean z2 = false;
        while (iterable.hasNext()) {
            if (((Boolean) function1.invoke(iterable.next())).booleanValue() == z) {
                iterable.remove();
                z2 = true;
            }
        }
        return z2;
    }

    public static final <T> boolean removeAll(@NotNull List<T> list, @NotNull Function1<? super T, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(list, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((List) list, (Function1) function1, true);
    }

    public static final <T> boolean retainAll(@NotNull List<T> list, @NotNull Function1<? super T, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(list, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((List) list, (Function1) function1, false);
    }

    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(@NotNull List<T> list, Function1<? super T, Boolean> function1, boolean z) {
        if (list instanceof RandomAccess) {
            int i;
            int lastIndex = CollectionsKt.getLastIndex(list);
            if (lastIndex >= 0) {
                int i2 = 0;
                i = 0;
                while (true) {
                    Object obj = list.get(i2);
                    if (((Boolean) function1.invoke(obj)).booleanValue() != z) {
                        if (i != i2) {
                            list.set(i, obj);
                        }
                        i++;
                    }
                    if (i2 == lastIndex) {
                        break;
                    }
                    i2++;
                }
            } else {
                i = 0;
            }
            if (i >= list.size()) {
                return false;
            }
            function1 = CollectionsKt.getLastIndex(list);
            if (function1 >= i) {
                while (true) {
                    list.remove(function1);
                    if (function1 == i) {
                        break;
                    }
                    function1--;
                }
            }
            return true;
        } else if (list != null) {
            return filterInPlace$CollectionsKt__MutableCollectionsKt(TypeIntrinsics.asMutableIterable(list), (Function1) function1, z);
        } else {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableIterable<T>");
        }
    }

    public static final <T> boolean removeAll(@NotNull Collection<? super T> collection, @NotNull Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        Intrinsics.checkParameterIsNotNull(iterable, "elements");
        return TypeIntrinsics.asMutableCollection(collection).removeAll(CollectionsKt.convertToSetForSetOperationWith(iterable, collection));
    }

    public static final <T> boolean removeAll(@NotNull Collection<? super T> collection, @NotNull Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        Intrinsics.checkParameterIsNotNull(sequence, "elements");
        Collection toHashSet = SequencesKt.toHashSet(sequence);
        return ((toHashSet.isEmpty() ^ 1) == 0 || collection.removeAll(toHashSet) == null) ? false : true;
    }

    public static final <T> boolean removeAll(@NotNull Collection<? super T> collection, @NotNull T[] tArr) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        return (((tArr.length == 0 ? 1 : 0) ^ 1) == 0 || collection.removeAll(ArraysKt.toHashSet(tArr)) == null) ? false : true;
    }

    public static final <T> boolean retainAll(@NotNull Collection<? super T> collection, @NotNull Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        Intrinsics.checkParameterIsNotNull(iterable, "elements");
        return TypeIntrinsics.asMutableCollection(collection).retainAll(CollectionsKt.convertToSetForSetOperationWith(iterable, collection));
    }

    public static final <T> boolean retainAll(@NotNull Collection<? super T> collection, @NotNull T[] tArr) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        if (((tArr.length == 0 ? 1 : 0) ^ 1) != 0) {
            return collection.retainAll(ArraysKt.toHashSet(tArr));
        }
        return retainNothing$CollectionsKt__MutableCollectionsKt(collection);
    }

    public static final <T> boolean retainAll(@NotNull Collection<? super T> collection, @NotNull Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        Intrinsics.checkParameterIsNotNull(sequence, "elements");
        Collection toHashSet = SequencesKt.toHashSet(sequence);
        if ((toHashSet.isEmpty() ^ 1) != 0) {
            return collection.retainAll(toHashSet);
        }
        return retainNothing$CollectionsKt__MutableCollectionsKt(collection);
    }

    private static final boolean retainNothing$CollectionsKt__MutableCollectionsKt(@NotNull Collection<?> collection) {
        boolean isEmpty = collection.isEmpty() ^ 1;
        collection.clear();
        return isEmpty;
    }

    public static final <T extends Comparable<? super T>> void sort(@NotNull List<T> list) {
        Intrinsics.checkParameterIsNotNull(list, "$receiver");
        if (list.size() > 1) {
            Collections.sort(list);
        }
    }

    public static final <T> void sortWith(@NotNull List<T> list, @NotNull Comparator<? super T> comparator) {
        Intrinsics.checkParameterIsNotNull(list, "$receiver");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        if (list.size() > 1) {
            Collections.sort(list, comparator);
        }
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final <T> void fill(@NotNull List<T> list, T t) {
        Collections.fill(list, t);
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final <T> void shuffle(@NotNull List<T> list) {
        Collections.shuffle(list);
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final <T> void shuffle(@NotNull List<T> list, Random random) {
        Collections.shuffle(list, random);
    }

    @NotNull
    @SinceKotlin(version = "1.2")
    public static final <T> List<T> shuffled(@NotNull Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(iterable, "$receiver");
        iterable = CollectionsKt.toMutableList(iterable);
        Collections.shuffle(iterable);
        return iterable;
    }

    @NotNull
    @SinceKotlin(version = "1.2")
    public static final <T> List<T> shuffled(@NotNull Iterable<? extends T> iterable, @NotNull Random random) {
        Intrinsics.checkParameterIsNotNull(iterable, "$receiver");
        Intrinsics.checkParameterIsNotNull(random, "random");
        iterable = CollectionsKt.toMutableList(iterable);
        Collections.shuffle(iterable, random);
        return iterable;
    }
}
