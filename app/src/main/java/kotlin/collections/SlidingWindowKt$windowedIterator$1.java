package kotlin.collections;

import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.SequenceBuilder;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00040\u0003H\n¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "Lkotlin/coroutines/experimental/SequenceBuilder;", "", "invoke", "(Lkotlin/coroutines/experimental/SequenceBuilder;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 10})
/* compiled from: SlidingWindow.kt */
final class SlidingWindowKt$windowedIterator$1 extends CoroutineImpl implements Function2<SequenceBuilder<? super List<? extends T>>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Iterator $iterator;
    final /* synthetic */ boolean $partialWindows;
    final /* synthetic */ boolean $reuseBuffer;
    final /* synthetic */ int $size;
    final /* synthetic */ int $step;
    int I$0;
    int I$1;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    private SequenceBuilder p$;

    SlidingWindowKt$windowedIterator$1(int i, int i2, Iterator it, boolean z, boolean z2, Continuation continuation) {
        this.$step = i;
        this.$size = i2;
        this.$iterator = it;
        this.$reuseBuffer = z;
        this.$partialWindows = z2;
        super(2, continuation);
    }

    @NotNull
    public final Continuation<Unit> create(@NotNull SequenceBuilder<? super List<? extends T>> sequenceBuilder, @NotNull Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(sequenceBuilder, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        Continuation slidingWindowKt$windowedIterator$1 = new SlidingWindowKt$windowedIterator$1(this.$step, this.$size, this.$iterator, this.$reuseBuffer, this.$partialWindows, continuation);
        slidingWindowKt$windowedIterator$1.p$ = sequenceBuilder;
        return slidingWindowKt$windowedIterator$1;
    }

    @Nullable
    public final Object invoke(@NotNull SequenceBuilder<? super List<? extends T>> sequenceBuilder, @NotNull Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(sequenceBuilder, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        return ((SlidingWindowKt$windowedIterator$1) create((SequenceBuilder) sequenceBuilder, (Continuation) continuation)).doResume(Unit.INSTANCE, null);
    }

    @org.jetbrains.annotations.Nullable
    public final java.lang.Object doResume(@org.jetbrains.annotations.Nullable java.lang.Object r12, @org.jetbrains.annotations.Nullable java.lang.Throwable r13) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:37)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:61)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r11 = this;
        r12 = kotlin.coroutines.experimental.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        r0 = r11.label;
        r1 = 1;
        r2 = 2;
        switch(r0) {
            case 0: goto L_0x0068;
            case 1: goto L_0x004f;
            case 2: goto L_0x0044;
            case 3: goto L_0x002d;
            case 4: goto L_0x001c;
            case 5: goto L_0x0013;
            default: goto L_0x000b;
        };
    L_0x000b:
        r12 = new java.lang.IllegalStateException;
        r13 = "call to 'resume' before 'invoke' with coroutine";
        r12.<init>(r13);
        throw r12;
    L_0x0013:
        r12 = r11.L$0;
        r12 = (kotlin.collections.RingBuffer) r12;
        r12 = r11.I$0;
        if (r13 == 0) goto L_0x0195;
    L_0x001b:
        throw r13;
    L_0x001c:
        r0 = r11.L$1;
        r0 = (kotlin.collections.RingBuffer) r0;
        r3 = r11.I$0;
        r4 = r11.L$0;
        r4 = (kotlin.coroutines.experimental.SequenceBuilder) r4;
        if (r13 == 0) goto L_0x0029;
    L_0x0028:
        throw r13;
    L_0x0029:
        r13 = r12;
        r12 = r11;
        goto L_0x0174;
    L_0x002d:
        r0 = r11.L$3;
        r0 = (java.util.Iterator) r0;
        r3 = r11.L$2;
        r3 = r11.L$1;
        r3 = (kotlin.collections.RingBuffer) r3;
        r4 = r11.I$0;
        r5 = r11.L$0;
        r5 = (kotlin.coroutines.experimental.SequenceBuilder) r5;
        if (r13 == 0) goto L_0x0040;
    L_0x003f:
        throw r13;
    L_0x0040:
        r13 = r12;
        r12 = r11;
        goto L_0x013a;
    L_0x0044:
        r12 = r11.I$1;
        r12 = r11.L$0;
        r12 = (java.util.ArrayList) r12;
        r12 = r11.I$0;
        if (r13 == 0) goto L_0x0195;
    L_0x004e:
        throw r13;
    L_0x004f:
        r0 = r11.L$3;
        r0 = (java.util.Iterator) r0;
        r3 = r11.L$2;
        r3 = r11.I$1;
        r3 = r11.L$1;
        r3 = (java.util.ArrayList) r3;
        r4 = r11.I$0;
        r5 = r11.L$0;
        r5 = (kotlin.coroutines.experimental.SequenceBuilder) r5;
        if (r13 == 0) goto L_0x0064;
    L_0x0063:
        throw r13;
    L_0x0064:
        r6 = r12;
        r13 = r4;
        r12 = r11;
        goto L_0x00b6;
    L_0x0068:
        if (r13 == 0) goto L_0x006b;
    L_0x006a:
        throw r13;
    L_0x006b:
        r13 = r11.p$;
        r0 = r11.$step;
        r3 = r11.$size;
        r0 = r0 - r3;
        if (r0 < 0) goto L_0x00ef;
    L_0x0074:
        r3 = new java.util.ArrayList;
        r4 = r11.$size;
        r3.<init>(r4);
        r4 = 0;
        r5 = r11.$iterator;
        r6 = r12;
        r12 = r11;
        r10 = r5;
        r5 = r13;
        r13 = r0;
        r0 = r10;
    L_0x0084:
        r7 = r0.hasNext();
        if (r7 == 0) goto L_0x00c7;
    L_0x008a:
        r7 = r0.next();
        if (r4 <= 0) goto L_0x0093;
    L_0x0090:
        r4 = r4 + -1;
        goto L_0x0084;
    L_0x0093:
        r3.add(r7);
        r8 = r3.size();
        r9 = r12.$size;
        if (r8 != r9) goto L_0x0084;
    L_0x009e:
        r12.L$0 = r5;
        r12.I$0 = r13;
        r12.L$1 = r3;
        r12.I$1 = r4;
        r12.L$2 = r7;
        r12.L$3 = r0;
        r12.label = r1;
        r4 = r5.yield(r3, r12);
        kotlin.jvm.internal.InlineMarker.mark(r2);
        if (r4 != r6) goto L_0x00b6;
    L_0x00b5:
        return r6;
    L_0x00b6:
        r4 = r12.$reuseBuffer;
        if (r4 == 0) goto L_0x00be;
    L_0x00ba:
        r3.clear();
        goto L_0x00c5;
    L_0x00be:
        r3 = new java.util.ArrayList;
        r4 = r12.$size;
        r3.<init>(r4);
    L_0x00c5:
        r4 = r13;
        goto L_0x0084;
    L_0x00c7:
        r0 = r3;
        r0 = (java.util.Collection) r0;
        r0 = r0.isEmpty();
        r0 = r0 ^ r1;
        if (r0 == 0) goto L_0x0195;
    L_0x00d1:
        r0 = r12.$partialWindows;
        if (r0 != 0) goto L_0x00dd;
    L_0x00d5:
        r0 = r3.size();
        r1 = r12.$size;
        if (r0 != r1) goto L_0x0195;
    L_0x00dd:
        r12.I$0 = r13;
        r12.L$0 = r3;
        r12.I$1 = r4;
        r12.label = r2;
        r12 = r5.yield(r3, r12);
        kotlin.jvm.internal.InlineMarker.mark(r2);
        if (r12 != r6) goto L_0x0195;
    L_0x00ee:
        return r6;
    L_0x00ef:
        r3 = new kotlin.collections.RingBuffer;
        r4 = r11.$size;
        r3.<init>(r4);
        r4 = r11.$iterator;
        r5 = r13;
        r13 = r12;
        r12 = r11;
        r10 = r4;
        r4 = r0;
        r0 = r10;
    L_0x00fe:
        r6 = r0.hasNext();
        if (r6 == 0) goto L_0x0140;
    L_0x0104:
        r6 = r0.next();
        r3.add(r6);
        r7 = r3.isFull();
        if (r7 == 0) goto L_0x00fe;
    L_0x0111:
        r7 = r12.$reuseBuffer;
        if (r7 == 0) goto L_0x0119;
    L_0x0115:
        r7 = r3;
        r7 = (java.util.List) r7;
        goto L_0x0123;
    L_0x0119:
        r7 = new java.util.ArrayList;
        r8 = r3;
        r8 = (java.util.Collection) r8;
        r7.<init>(r8);
        r7 = (java.util.List) r7;
    L_0x0123:
        r12.L$0 = r5;
        r12.I$0 = r4;
        r12.L$1 = r3;
        r12.L$2 = r6;
        r12.L$3 = r0;
        r6 = 3;
        r12.label = r6;
        r6 = r5.yield(r7, r12);
        kotlin.jvm.internal.InlineMarker.mark(r2);
        if (r6 != r13) goto L_0x013a;
    L_0x0139:
        return r13;
    L_0x013a:
        r6 = r12.$step;
        r3.removeFirst(r6);
        goto L_0x00fe;
    L_0x0140:
        r0 = r12.$partialWindows;
        if (r0 == 0) goto L_0x0195;
    L_0x0144:
        r0 = r3;
        r3 = r4;
        r4 = r5;
    L_0x0147:
        r5 = r0.size();
        r6 = r12.$step;
        if (r5 <= r6) goto L_0x017a;
    L_0x014f:
        r5 = r12.$reuseBuffer;
        if (r5 == 0) goto L_0x0157;
    L_0x0153:
        r5 = r0;
        r5 = (java.util.List) r5;
        goto L_0x0161;
    L_0x0157:
        r5 = new java.util.ArrayList;
        r6 = r0;
        r6 = (java.util.Collection) r6;
        r5.<init>(r6);
        r5 = (java.util.List) r5;
    L_0x0161:
        r12.L$0 = r4;
        r12.I$0 = r3;
        r12.L$1 = r0;
        r6 = 4;
        r12.label = r6;
        r5 = r4.yield(r5, r12);
        kotlin.jvm.internal.InlineMarker.mark(r2);
        if (r5 != r13) goto L_0x0174;
    L_0x0173:
        return r13;
    L_0x0174:
        r5 = r12.$step;
        r0.removeFirst(r5);
        goto L_0x0147;
    L_0x017a:
        r5 = r0;
        r5 = (java.util.Collection) r5;
        r5 = r5.isEmpty();
        r1 = r1 ^ r5;
        if (r1 == 0) goto L_0x0195;
    L_0x0184:
        r12.I$0 = r3;
        r12.L$0 = r0;
        r1 = 5;
        r12.label = r1;
        r12 = r4.yield(r0, r12);
        kotlin.jvm.internal.InlineMarker.mark(r2);
        if (r12 != r13) goto L_0x0195;
    L_0x0194:
        return r13;
    L_0x0195:
        r12 = kotlin.Unit.INSTANCE;
        return r12;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.collections.SlidingWindowKt$windowedIterator$1.doResume(java.lang.Object, java.lang.Throwable):java.lang.Object");
    }
}
