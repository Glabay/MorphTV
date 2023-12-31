package kotlin.jvm.internal.unsafe;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\u001a\u0012\u0010\u0000\u001a\u00020\u00012\b\b\u0001\u0010\u0002\u001a\u00020\u0003H\u0002\u001a\u0012\u0010\u0004\u001a\u00020\u00012\b\b\u0001\u0010\u0002\u001a\u00020\u0003H\u0002¨\u0006\u0005"}, d2 = {"monitorEnter", "", "monitor", "", "monitorExit", "kotlin-stdlib"}, k = 2, mv = {1, 1, 10})
/* compiled from: monitor.kt */
public final class MonitorKt {
    private static final void monitorEnter(Object obj) {
        throw ((Throwable) new UnsupportedOperationException("This function can only be used privately"));
    }

    private static final void monitorExit(Object obj) {
        throw ((Throwable) new UnsupportedOperationException("This function can only be used privately"));
    }
}
