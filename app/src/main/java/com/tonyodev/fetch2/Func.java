package com.tonyodev.fetch2;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\bg\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00020\u0002J\u0017\u0010\u0003\u001a\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00028\u0000H&¢\u0006\u0002\u0010\u0006¨\u0006\u0007"}, d2 = {"Lcom/tonyodev/fetch2/Func;", "T", "", "call", "", "t", "(Ljava/lang/Object;)V", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: Func.kt */
public interface Func<T> {
    void call(@NotNull T t);
}
