package com.tonyodev.fetch2;

import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0002\b\b\b\u0001\u0018\u0000 \n2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\nB\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\t¨\u0006\u000b"}, d2 = {"Lcom/tonyodev/fetch2/NetworkType;", "", "value", "", "(Ljava/lang/String;II)V", "getValue", "()I", "GLOBAL_OFF", "ALL", "WIFI_ONLY", "Companion", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: NetworkType.kt */
public enum NetworkType {
    ;
    
    public static final Companion Companion = null;
    private final int value;

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007¨\u0006\u0007"}, d2 = {"Lcom/tonyodev/fetch2/NetworkType$Companion;", "", "()V", "valueOf", "Lcom/tonyodev/fetch2/NetworkType;", "value", "", "fetch2_release"}, k = 1, mv = {1, 1, 10})
    /* compiled from: NetworkType.kt */
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @NotNull
        public final NetworkType valueOf(int i) {
            switch (i) {
                case -1:
                    return NetworkType.GLOBAL_OFF;
                case 0:
                    return NetworkType.ALL;
                case 1:
                    return NetworkType.WIFI_ONLY;
                default:
                    return NetworkType.ALL;
            }
        }
    }

    @JvmStatic
    @NotNull
    public static final NetworkType valueOf(int i) {
        return Companion.valueOf(i);
    }

    protected NetworkType(int i) {
        this.value = i;
    }

    public final int getValue() {
        return this.value;
    }

    static {
        Companion = new Companion();
    }
}
