package com.android.morpheustv.casting;

import android.content.Context;
import com.android.morpheustv.settings.Settings;
import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.CastOptions.Builder;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;
import com.google.android.gms.cast.framework.media.CastMediaOptions;
import com.google.android.gms.cast.framework.media.NotificationOptions;
import java.util.List;

public class CastOptionsProvider implements OptionsProvider {
    public List<SessionProvider> getAdditionalSessionProviders(Context context) {
        return null;
    }

    public CastOptions getCastOptions(Context context) {
        return new Builder().setReceiverApplicationId(Settings.CAST_RECEIVER_APP_ID).setCastMediaOptions(new CastMediaOptions.Builder().setNotificationOptions(new NotificationOptions.Builder().setTargetActivityClassName(ExpandedControlsActivity.class.getName()).build()).setExpandedControllerActivityClassName(ExpandedControlsActivity.class.getName()).build()).build();
    }
}
