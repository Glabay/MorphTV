package com.google.android.gms.cast.framework.internal.featurehighlight;

import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.cast.framework.C0782R;
import com.google.android.gms.internal.cast.zzev;

@Keep
public class HelpTextView extends LinearLayout implements zzi {
    private TextView zzjx;
    private TextView zzjy;

    @Keep
    public HelpTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private static void zza(TextView textView, @Nullable CharSequence charSequence) {
        textView.setText(charSequence);
        textView.setVisibility(TextUtils.isEmpty(charSequence) ? 8 : 0);
    }

    @Keep
    public View asView() {
        return this;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.zzjx = (TextView) zzev.checkNotNull((TextView) findViewById(C0782R.id.cast_featurehighlight_help_text_header_view));
        this.zzjy = (TextView) zzev.checkNotNull((TextView) findViewById(C0782R.id.cast_featurehighlight_help_text_body_view));
    }

    @Keep
    public void setText(@Nullable CharSequence charSequence, @Nullable CharSequence charSequence2) {
        zza(this.zzjx, charSequence);
        zza(this.zzjy, charSequence2);
    }
}
