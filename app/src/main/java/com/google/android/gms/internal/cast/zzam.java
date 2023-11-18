package com.google.android.gms.internal.cast;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.annotation.ColorInt;
import android.view.View;
import com.google.android.gms.cast.AdBreakInfo;
import java.util.List;

public final class zzam extends View {
    private List<AdBreakInfo> zzdf;
    private final int zzpn;
    private int zzpo = 1;
    private Paint zzpp;

    public zzam(Context context) {
        super(context);
        context = getContext();
        this.zzpn = (int) (context == null ? Math.round(3.0d) : Math.round(((double) context.getResources().getDisplayMetrics().density) * 3.0d));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected final synchronized void onDraw(@android.support.annotation.NonNull android.graphics.Canvas r9) {
        /*
        r8 = this;
        monitor-enter(r8);
        super.onDraw(r9);	 Catch:{ all -> 0x006d }
        r0 = r8.zzdf;	 Catch:{ all -> 0x006d }
        if (r0 == 0) goto L_0x006b;
    L_0x0008:
        r0 = r8.zzdf;	 Catch:{ all -> 0x006d }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x006d }
        if (r0 == 0) goto L_0x0011;
    L_0x0010:
        goto L_0x006b;
    L_0x0011:
        r0 = r8.getMeasuredHeight();	 Catch:{ all -> 0x006d }
        r0 = (float) r0;	 Catch:{ all -> 0x006d }
        r1 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r0 = r0 / r1;
        r0 = java.lang.Math.round(r0);	 Catch:{ all -> 0x006d }
        r1 = r8.getMeasuredWidth();	 Catch:{ all -> 0x006d }
        r2 = r8.getPaddingLeft();	 Catch:{ all -> 0x006d }
        r1 = r1 - r2;
        r2 = r8.getPaddingRight();	 Catch:{ all -> 0x006d }
        r1 = r1 - r2;
        r2 = r8.zzdf;	 Catch:{ all -> 0x006d }
        r2 = r2.iterator();	 Catch:{ all -> 0x006d }
    L_0x0031:
        r3 = r2.hasNext();	 Catch:{ all -> 0x006d }
        if (r3 == 0) goto L_0x0069;
    L_0x0037:
        r3 = r2.next();	 Catch:{ all -> 0x006d }
        r3 = (com.google.android.gms.cast.AdBreakInfo) r3;	 Catch:{ all -> 0x006d }
        if (r3 == 0) goto L_0x0031;
    L_0x003f:
        r3 = r3.getPlaybackPositionInMs();	 Catch:{ all -> 0x006d }
        r5 = 0;
        r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r7 < 0) goto L_0x0031;
    L_0x0049:
        r5 = r8.zzpo;	 Catch:{ all -> 0x006d }
        r5 = (long) r5;	 Catch:{ all -> 0x006d }
        r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r7 > 0) goto L_0x0031;
    L_0x0050:
        r3 = (double) r3;	 Catch:{ all -> 0x006d }
        r5 = (double) r1;	 Catch:{ all -> 0x006d }
        r3 = r3 * r5;
        r5 = r8.zzpo;	 Catch:{ all -> 0x006d }
        r5 = (double) r5;	 Catch:{ all -> 0x006d }
        r3 = r3 / r5;
        r3 = (int) r3;	 Catch:{ all -> 0x006d }
        r4 = r8.getPaddingLeft();	 Catch:{ all -> 0x006d }
        r4 = r4 + r3;
        r3 = (float) r4;	 Catch:{ all -> 0x006d }
        r4 = (float) r0;	 Catch:{ all -> 0x006d }
        r5 = r8.zzpn;	 Catch:{ all -> 0x006d }
        r5 = (float) r5;	 Catch:{ all -> 0x006d }
        r6 = r8.zzpp;	 Catch:{ all -> 0x006d }
        r9.drawCircle(r3, r4, r5, r6);	 Catch:{ all -> 0x006d }
        goto L_0x0031;
    L_0x0069:
        monitor-exit(r8);
        return;
    L_0x006b:
        monitor-exit(r8);
        return;
    L_0x006d:
        r9 = move-exception;
        monitor-exit(r8);
        throw r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzam.onDraw(android.graphics.Canvas):void");
    }

    public final synchronized void zzb(List<AdBreakInfo> list, @ColorInt int i) {
        this.zzdf = list;
        this.zzpp = new Paint(1);
        this.zzpp.setColor(-1);
        this.zzpp.setStyle(Style.FILL);
        invalidate();
    }

    public final synchronized void zzj(int i) {
        this.zzpo = i;
    }
}
