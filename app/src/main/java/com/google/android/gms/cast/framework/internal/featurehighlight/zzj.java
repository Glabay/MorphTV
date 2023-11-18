package com.google.android.gms.cast.framework.internal.featurehighlight;

import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import com.google.android.gms.cast.framework.C0782R;
import com.google.android.gms.internal.cast.zzev;

final class zzj {
    private final Rect zzkf = new Rect();
    private final int zzkg;
    private final int zzkh;
    private final int zzki;
    private final int zzkj;
    private final zza zzkk;

    zzj(zza zza) {
        this.zzkk = (zza) zzev.checkNotNull(zza);
        Resources resources = zza.getResources();
        this.zzkg = resources.getDimensionPixelSize(C0782R.dimen.cast_libraries_material_featurehighlight_inner_radius);
        this.zzkh = resources.getDimensionPixelOffset(C0782R.dimen.cast_libraries_material_featurehighlight_inner_margin);
        this.zzki = resources.getDimensionPixelSize(C0782R.dimen.cast_libraries_material_featurehighlight_text_max_width);
        this.zzkj = resources.getDimensionPixelSize(C0782R.dimen.cast_libraries_material_featurehighlight_text_horizontal_offset);
    }

    private final int zza(View view, int i, int i2, int i3, int i4) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        int i5 = i3 / 2;
        i4 = i4 - i <= i2 - i4 ? (i4 - i5) + this.zzkj : (i4 - i5) - this.zzkj;
        return i4 - marginLayoutParams.leftMargin < i ? i + marginLayoutParams.leftMargin : (i4 + i3) + marginLayoutParams.rightMargin > i2 ? (i2 - i3) - marginLayoutParams.rightMargin : i4;
    }

    private final void zza(View view, int i, int i2) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        view.measure(MeasureSpec.makeMeasureSpec(Math.min((i - marginLayoutParams.leftMargin) - marginLayoutParams.rightMargin, this.zzki), 1073741824), MeasureSpec.makeMeasureSpec(i2, Integer.MIN_VALUE));
    }

    final void zza(Rect rect, Rect rect2) {
        View zzaq = this.zzkk.zzaq();
        int i = 0;
        if (!rect.isEmpty()) {
            if (!rect2.isEmpty()) {
                int centerY = rect.centerY();
                int centerX = rect.centerX();
                if (centerY < rect2.centerY()) {
                    i = 1;
                }
                int max = Math.max(this.zzkg * 2, rect.height()) / 2;
                int i2 = (centerY + max) + this.zzkh;
                int zza;
                if (i != 0) {
                    zza(zzaq, rect2.width(), rect2.bottom - i2);
                    zza = zza(zzaq, rect2.left, rect2.right, zzaq.getMeasuredWidth(), centerX);
                    zzaq.layout(zza, i2, zzaq.getMeasuredWidth() + zza, zzaq.getMeasuredHeight() + i2);
                } else {
                    i2 = (centerY - max) - this.zzkh;
                    zza(zzaq, rect2.width(), i2 - rect2.top);
                    zza = zza(zzaq, rect2.left, rect2.right, zzaq.getMeasuredWidth(), centerX);
                    zzaq.layout(zza, i2 - zzaq.getMeasuredHeight(), zzaq.getMeasuredWidth() + zza, i2);
                }
                this.zzkf.set(zzaq.getLeft(), zzaq.getTop(), zzaq.getRight(), zzaq.getBottom());
                this.zzkk.zzar().zzb(rect, this.zzkf);
                this.zzkk.zzas().zza(rect);
            }
        }
        zzaq.layout(0, 0, 0, 0);
        this.zzkf.set(zzaq.getLeft(), zzaq.getTop(), zzaq.getRight(), zzaq.getBottom());
        this.zzkk.zzar().zzb(rect, this.zzkf);
        this.zzkk.zzas().zza(rect);
    }
}
