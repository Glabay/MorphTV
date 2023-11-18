package kotlin.sequences;

import java.util.Collection;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0002¨\u0006\u0005"}, d2 = {"kotlin/sequences/SequencesKt___SequencesKt$minus$3", "Lkotlin/sequences/Sequence;", "(Lkotlin/sequences/Sequence;Ljava/lang/Iterable;)V", "iterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
/* compiled from: _Sequences.kt */
public final class SequencesKt___SequencesKt$minus$3 implements Sequence<T> {
    final /* synthetic */ Iterable $elements;
    final /* synthetic */ Sequence receiver$0;

    SequencesKt___SequencesKt$minus$3(Sequence<? extends T> sequence, Iterable iterable) {
        this.receiver$0 = sequence;
        this.$elements = iterable;
    }

    @NotNull
    public Iterator<T> iterator() {
        Collection convertToSetForSetOperation = CollectionsKt.convertToSetForSetOperation(this.$elements);
        if (convertToSetForSetOperation.isEmpty()) {
            return this.receiver$0.iterator();
        }
        return SequencesKt___SequencesKt.filterNot(this.receiver$0, new SequencesKt___SequencesKt$minus$3$iterator$1(convertToSetForSetOperation)).iterator();
    }
}
