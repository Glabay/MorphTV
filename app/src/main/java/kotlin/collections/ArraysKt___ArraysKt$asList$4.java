package kotlin.collections;

import java.util.RandomAccess;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004B\u0005¢\u0006\u0002\u0010\u0005J\u0011\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0002H\u0002J\u0016\u0010\r\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u0007H\u0002¢\u0006\u0002\u0010\u000fJ\u0010\u0010\u0010\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0002H\u0016J\b\u0010\u0011\u001a\u00020\u000bH\u0016J\u0010\u0010\u0012\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0002H\u0016R\u0014\u0010\u0006\u001a\u00020\u00078VX\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u0013"}, d2 = {"kotlin/collections/ArraysKt___ArraysKt$asList$4", "Lkotlin/collections/AbstractList;", "", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "([J)V", "size", "", "getSize", "()I", "contains", "", "element", "get", "index", "(I)Ljava/lang/Long;", "indexOf", "isEmpty", "lastIndexOf", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
/* compiled from: _Arrays.kt */
public final class ArraysKt___ArraysKt$asList$4 extends AbstractList<Long> implements RandomAccess {
    final /* synthetic */ long[] receiver$0;

    ArraysKt___ArraysKt$asList$4(long[] jArr) {
        this.receiver$0 = jArr;
    }

    public final /* bridge */ boolean contains(Object obj) {
        return obj instanceof Long ? contains(((Number) obj).longValue()) : null;
    }

    public final /* bridge */ int indexOf(Object obj) {
        return obj instanceof Long ? indexOf(((Number) obj).longValue()) : -1;
    }

    public final /* bridge */ int lastIndexOf(Object obj) {
        return obj instanceof Long ? lastIndexOf(((Number) obj).longValue()) : -1;
    }

    public int getSize() {
        return this.receiver$0.length;
    }

    public boolean isEmpty() {
        return this.receiver$0.length == 0;
    }

    public boolean contains(long j) {
        return ArraysKt___ArraysKt.contains(this.receiver$0, j);
    }

    @NotNull
    public Long get(int i) {
        return Long.valueOf(this.receiver$0[i]);
    }

    public int indexOf(long j) {
        return ArraysKt___ArraysKt.indexOf(this.receiver$0, j);
    }

    public int lastIndexOf(long j) {
        return ArraysKt___ArraysKt.lastIndexOf(this.receiver$0, j);
    }
}
