package kotlin.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000D\n\u0000\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u0012\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002\u001a\u001f\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0004j\b\u0012\u0004\u0012\u0002H\u0002`\u0005\"\u0004\b\u0000\u0010\u0002H\b\u001a5\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0004j\b\u0012\u0004\u0012\u0002H\u0002`\u0005\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0007\"\u0002H\u0002¢\u0006\u0002\u0010\b\u001a\u001f\u0010\t\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\nj\b\u0012\u0004\u0012\u0002H\u0002`\u000b\"\u0004\b\u0000\u0010\u0002H\b\u001a5\u0010\t\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\nj\b\u0012\u0004\u0012\u0002H\u0002`\u000b\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0007\"\u0002H\u0002¢\u0006\u0002\u0010\f\u001a\u0015\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u00020\u000e\"\u0004\b\u0000\u0010\u0002H\b\u001a+\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0007\"\u0002H\u0002¢\u0006\u0002\u0010\u000f\u001a\u0015\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002H\b\u001a!\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0011\u001a\u0002H\u0002H\u0007¢\u0006\u0002\u0010\u0012\u001a+\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0007\"\u0002H\u0002¢\u0006\u0002\u0010\u000f\u001a-\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0014\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0007\"\u0002H\u0002H\u0007¢\u0006\u0002\u0010\u0015\u001aI\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0014\"\u0004\b\u0000\u0010\u00022\u001a\u0010\u0016\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0017j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u00182\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0007\"\u0002H\u0002H\u0007¢\u0006\u0002\u0010\u0019\u001a\u001e\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0000\u001a!\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0004\u0012\u0002H\u0002\u0018\u00010\u0001H\b¨\u0006\u001c"}, d2 = {"emptySet", "", "T", "hashSetOf", "Ljava/util/HashSet;", "Lkotlin/collections/HashSet;", "elements", "", "([Ljava/lang/Object;)Ljava/util/HashSet;", "linkedSetOf", "Ljava/util/LinkedHashSet;", "Lkotlin/collections/LinkedHashSet;", "([Ljava/lang/Object;)Ljava/util/LinkedHashSet;", "mutableSetOf", "", "([Ljava/lang/Object;)Ljava/util/Set;", "setOf", "element", "(Ljava/lang/Object;)Ljava/util/Set;", "sortedSetOf", "Ljava/util/TreeSet;", "([Ljava/lang/Object;)Ljava/util/TreeSet;", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/util/Comparator;[Ljava/lang/Object;)Ljava/util/TreeSet;", "optimizeReadOnlySet", "orEmpty", "kotlin-stdlib"}, k = 5, mv = {1, 1, 10}, xi = 1, xs = "kotlin/collections/SetsKt")
/* compiled from: Sets.kt */
class SetsKt__SetsKt {
    @NotNull
    public static final <T> Set<T> emptySet() {
        return EmptySet.INSTANCE;
    }

    @NotNull
    public static final <T> Set<T> setOf(@NotNull T... tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        return tArr.length > 0 ? ArraysKt___ArraysKt.toSet((Object[]) tArr) : emptySet();
    }

    @InlineOnly
    private static final <T> Set<T> setOf() {
        return emptySet();
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> Set<T> mutableSetOf() {
        return new LinkedHashSet();
    }

    @NotNull
    public static final <T> Set<T> mutableSetOf(@NotNull T... tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        return (Set) ArraysKt___ArraysKt.toCollection((Object[]) tArr, (Collection) new LinkedHashSet(MapsKt__MapsKt.mapCapacity(tArr.length)));
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> HashSet<T> hashSetOf() {
        return new HashSet();
    }

    @NotNull
    public static final <T> HashSet<T> hashSetOf(@NotNull T... tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        return (HashSet) ArraysKt___ArraysKt.toCollection((Object[]) tArr, (Collection) new HashSet(MapsKt__MapsKt.mapCapacity(tArr.length)));
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> LinkedHashSet<T> linkedSetOf() {
        return new LinkedHashSet();
    }

    @NotNull
    public static final <T> LinkedHashSet<T> linkedSetOf(@NotNull T... tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        return (LinkedHashSet) ArraysKt___ArraysKt.toCollection((Object[]) tArr, (Collection) new LinkedHashSet(MapsKt__MapsKt.mapCapacity(tArr.length)));
    }

    @InlineOnly
    private static final <T> Set<T> orEmpty(@Nullable Set<? extends T> set) {
        return set != null ? set : emptySet();
    }

    @NotNull
    public static final <T> Set<T> setOf(T t) {
        t = Collections.singleton(t);
        Intrinsics.checkExpressionValueIsNotNull(t, "java.util.Collections.singleton(element)");
        return t;
    }

    @NotNull
    public static final <T> TreeSet<T> sortedSetOf(@NotNull T... tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        return (TreeSet) ArraysKt___ArraysKt.toCollection((Object[]) tArr, (Collection) new TreeSet());
    }

    @NotNull
    public static final <T> TreeSet<T> sortedSetOf(@NotNull Comparator<? super T> comparator, @NotNull T... tArr) {
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        return (TreeSet) ArraysKt___ArraysKt.toCollection((Object[]) tArr, (Collection) new TreeSet(comparator));
    }

    @NotNull
    public static final <T> Set<T> optimizeReadOnlySet(@NotNull Set<? extends T> set) {
        Intrinsics.checkParameterIsNotNull(set, "$receiver");
        switch (set.size()) {
            case 0:
                return emptySet();
            case 1:
                return setOf((Object) set.iterator().next());
            default:
                return set;
        }
    }
}
