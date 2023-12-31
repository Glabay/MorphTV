package kotlin.sequences;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.collections.Grouping;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0015\u0010\u0003\u001a\u00028\u00012\u0006\u0010\u0004\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0005J\u000e\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007H\u0016¨\u0006\b"}, d2 = {"kotlin/sequences/SequencesKt___SequencesKt$groupingBy$1", "Lkotlin/collections/Grouping;", "(Lkotlin/sequences/Sequence;Lkotlin/jvm/functions/Function1;)V", "keyOf", "element", "(Ljava/lang/Object;)Ljava/lang/Object;", "sourceIterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
/* compiled from: _Sequences.kt */
public final class SequencesKt___SequencesKt$groupingBy$1 implements Grouping<T, K> {
    final /* synthetic */ Function1 $keySelector;
    final /* synthetic */ Sequence receiver$0;

    public SequencesKt___SequencesKt$groupingBy$1(Sequence<? extends T> sequence, Function1 function1) {
        this.receiver$0 = sequence;
        this.$keySelector = function1;
    }

    @NotNull
    public Iterator<T> sourceIterator() {
        return this.receiver$0.iterator();
    }

    public K keyOf(T t) {
        return this.$keySelector.invoke(t);
    }
}
