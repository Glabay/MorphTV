package kotlin.sequences;

import java.util.Iterator;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0002¨\u0006\u0005"}, d2 = {"kotlin/sequences/SequencesKt___SequencesKt$minus$2", "Lkotlin/sequences/Sequence;", "(Lkotlin/sequences/Sequence;[Ljava/lang/Object;)V", "iterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
/* compiled from: _Sequences.kt */
public final class SequencesKt___SequencesKt$minus$2 implements Sequence<T> {
    final /* synthetic */ Object[] $elements;
    final /* synthetic */ Sequence receiver$0;

    SequencesKt___SequencesKt$minus$2(Sequence<? extends T> sequence, Object[] objArr) {
        this.receiver$0 = sequence;
        this.$elements = objArr;
    }

    @NotNull
    public Iterator<T> iterator() {
        return SequencesKt___SequencesKt.filterNot(this.receiver$0, new SequencesKt___SequencesKt$minus$2$iterator$1(ArraysKt___ArraysKt.toHashSet(this.$elements))).iterator();
    }
}
