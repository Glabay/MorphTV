package kotlin.coroutines.experimental.intrinsics;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.NotImplementedError;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u00008\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a5\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\"\u0004\b\u0000\u0010\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u000e0\f2\u0010\b\u0004\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0011H\b\u001a5\u0010\u0012\u001a\u0002H\u000e\"\u0004\b\u0000\u0010\u000e2\u001c\b\u0004\u0010\u0010\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0\f\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0013HHø\u0001\u0000¢\u0006\u0002\u0010\u0014\u001a5\u0010\u0015\u001a\u0002H\u000e\"\u0004\b\u0000\u0010\u000e2\u001c\b\u0004\u0010\u0010\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0\f\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0013HHø\u0001\u0000¢\u0006\u0002\u0010\u0014\u001aD\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\r0\f\"\u0004\b\u0000\u0010\u000e*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0\f\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u00132\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u000e0\fH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0017\u001a]\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\r0\f\"\u0004\b\u0000\u0010\u0018\"\u0004\b\u0001\u0010\u000e*#\b\u0001\u0012\u0004\u0012\u0002H\u0018\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0\f\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0019¢\u0006\u0002\b\u001a2\u0006\u0010\u001b\u001a\u0002H\u00182\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u000e0\fH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001a\u001f\u0010\u001d\u001a\b\u0012\u0004\u0012\u0002H\u000e0\f\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\fH\b\"\u001c\u0010\u0000\u001a\u00020\u00018\u0006X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u0002\u0010\u0003\u001a\u0004\b\u0004\u0010\u0005\"\u001b\u0010\u0006\u001a\u00020\u00078Æ\u0002X\u0004¢\u0006\f\u0012\u0004\b\b\u0010\u0003\u001a\u0004\b\t\u0010\n\u0002\u0004\n\u0002\b\t¨\u0006\u001e"}, d2 = {"COROUTINE_SUSPENDED", "", "COROUTINE_SUSPENDED$annotations", "()V", "getCOROUTINE_SUSPENDED", "()Ljava/lang/Object;", "coroutineContext", "Lkotlin/coroutines/experimental/CoroutineContext;", "coroutineContext$annotations", "getCoroutineContext", "()Lkotlin/coroutines/experimental/CoroutineContext;", "buildContinuationByInvokeCall", "Lkotlin/coroutines/experimental/Continuation;", "", "T", "completion", "block", "Lkotlin/Function0;", "suspendCoroutineOrReturn", "Lkotlin/Function1;", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "suspendCoroutineUninterceptedOrReturn", "createCoroutineUnchecked", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/experimental/Continuation;)Lkotlin/coroutines/experimental/Continuation;", "R", "Lkotlin/Function2;", "Lkotlin/ExtensionFunctionType;", "receiver", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/experimental/Continuation;)Lkotlin/coroutines/experimental/Continuation;", "intercepted", "kotlin-stdlib"}, k = 2, mv = {1, 1, 10})
@JvmName(name = "IntrinsicsKt")
/* compiled from: Intrinsics.kt */
public final class IntrinsicsKt {
    @NotNull
    private static final Object COROUTINE_SUSPENDED = new Object();

    @SinceKotlin(version = "1.1")
    public static /* synthetic */ void COROUTINE_SUSPENDED$annotations() {
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Use kotlin.coroutines.experimental.coroutineContext instead", replaceWith = @ReplaceWith(expression = "kotlin.coroutines.experimental.coroutineContext", imports = {}))
    @SinceKotlin(version = "1.2")
    public static /* synthetic */ void coroutineContext$annotations() {
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> Object suspendCoroutineOrReturn(Function1<? super Continuation<? super T>, ? extends Object> function1, Continuation<? super T> continuation) {
        InlineMarker.mark(0);
        function1 = function1.invoke(CoroutineIntrinsics.normalizeContinuation(continuation));
        InlineMarker.mark(1);
        return function1;
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final <T> Object suspendCoroutineUninterceptedOrReturn(Function1<? super Continuation<? super T>, ? extends Object> function1, Continuation<? super T> continuation) {
        throw ((Throwable) new NotImplementedError("Implementation of suspendCoroutineUninterceptedOrReturn is intrinsic"));
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final <T> Continuation<T> intercepted(@NotNull Continuation<? super T> continuation) {
        throw ((Throwable) new NotImplementedError("Implementation of intercepted is intrinsic"));
    }

    @NotNull
    public static final CoroutineContext getCoroutineContext() {
        throw new NotImplementedError("Implemented as intrinsic");
    }

    @NotNull
    public static final Object getCOROUTINE_SUSPENDED() {
        return COROUTINE_SUSPENDED;
    }

    @NotNull
    @SinceKotlin(version = "1.1")
    public static final <T> Continuation<Unit> createCoroutineUnchecked(@NotNull Function1<? super Continuation<? super T>, ? extends Object> function1, @NotNull Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function1, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        if (function1 instanceof CoroutineImpl) {
            function1 = ((CoroutineImpl) function1).create(continuation);
            if (function1 != null) {
                return ((CoroutineImpl) function1).getFacade();
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.jvm.internal.CoroutineImpl");
        }
        return CoroutineIntrinsics.interceptContinuationIfNeeded(continuation.getContext(), new C1400xe6b592e7(continuation, function1, continuation));
    }

    @NotNull
    @SinceKotlin(version = "1.1")
    public static final <R, T> Continuation<Unit> createCoroutineUnchecked(@NotNull Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2, R r, @NotNull Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function2, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        if (function2 instanceof CoroutineImpl) {
            function2 = ((CoroutineImpl) function2).create(r, continuation);
            if (function2 != null) {
                return ((CoroutineImpl) function2).getFacade();
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.jvm.internal.CoroutineImpl");
        }
        return CoroutineIntrinsics.interceptContinuationIfNeeded(continuation.getContext(), new C1401xe6b592e8(continuation, function2, r, continuation));
    }

    private static final <T> Continuation<Unit> buildContinuationByInvokeCall(Continuation<? super T> continuation, Function0<? extends Object> function0) {
        return CoroutineIntrinsics.interceptContinuationIfNeeded(continuation.getContext(), new IntrinsicsKt$buildContinuationByInvokeCall$continuation$1(continuation, function0));
    }
}
