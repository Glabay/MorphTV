package com.tonyodev.fetch2.helper;

import com.tonyodev.fetch2.NetworkType;
import java.util.List;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\bf\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\u00020\u0002J\u000e\u0010\r\u001a\b\u0012\u0004\u0012\u00028\u00000\u000eH&J\b\u0010\u000f\u001a\u00020\u0010H&J\b\u0010\u0011\u001a\u00020\u0010H&J\b\u0010\u0012\u001a\u00020\u0010H&J\b\u0010\u0013\u001a\u00020\u0010H&R\u0018\u0010\u0003\u001a\u00020\u0004X¦\u000e¢\u0006\f\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u0012\u0010\t\u001a\u00020\nX¦\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\u000bR\u0012\u0010\f\u001a\u00020\nX¦\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\u000b¨\u0006\u0014"}, d2 = {"Lcom/tonyodev/fetch2/helper/PriorityListProcessor;", "T", "", "globalNetworkType", "Lcom/tonyodev/fetch2/NetworkType;", "getGlobalNetworkType", "()Lcom/tonyodev/fetch2/NetworkType;", "setGlobalNetworkType", "(Lcom/tonyodev/fetch2/NetworkType;)V", "isPaused", "", "()Z", "isStopped", "getPriorityList", "", "pause", "", "resume", "start", "stop", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: PriorityListProcessor.kt */
public interface PriorityListProcessor<T> {
    @NotNull
    NetworkType getGlobalNetworkType();

    @NotNull
    List<T> getPriorityList();

    boolean isPaused();

    boolean isStopped();

    void pause();

    void resume();

    void setGlobalNetworkType(@NotNull NetworkType networkType);

    void start();

    void stop();
}
