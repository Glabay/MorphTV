package kotlin.collections;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.internal.ArrayIteratorsKt;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0010\u001c\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0002¨\u0006\u0005¸\u0006\u0000"}, d2 = {"kotlin/collections/CollectionsKt__IterablesKt$Iterable$1", "", "(Lkotlin/jvm/functions/Function0;)V", "iterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
/* compiled from: Iterables.kt */
public final class ArraysKt___ArraysKt$asIterable$$inlined$Iterable$5 implements Iterable<Long>, KMappedMarker {
    final /* synthetic */ long[] receiver$0$inlined;

    public ArraysKt___ArraysKt$asIterable$$inlined$Iterable$5(long[] jArr) {
        this.receiver$0$inlined = jArr;
    }

    @NotNull
    public Iterator<Long> iterator() {
        return ArrayIteratorsKt.iterator(this.receiver$0$inlined);
    }
}
