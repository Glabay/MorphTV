package com.google.android.gms.cast;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Display;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.ApiOptions.HasOptions;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.cast.zzcx;
import com.google.android.gms.internal.cast.zzdf;
import com.google.android.gms.internal.cast.zzdq;
import com.google.android.gms.internal.cast.zzea;

public final class CastRemoteDisplay {
    @Deprecated
    public static final Api<CastRemoteDisplayOptions> API = new Api("CastRemoteDisplay.API", CLIENT_BUILDER, zzdf.zzwe);
    private static final AbstractClientBuilder<zzea, CastRemoteDisplayOptions> CLIENT_BUILDER = new zzo();
    public static final int CONFIGURATION_INTERACTIVE_NONREALTIME = 2;
    public static final int CONFIGURATION_INTERACTIVE_REALTIME = 1;
    public static final int CONFIGURATION_NONINTERACTIVE = 3;
    @Deprecated
    public static final CastRemoteDisplayApi CastRemoteDisplayApi = new zzdq(API);
    public static final String EXTRA_INT_SESSION_ENDED_STATUS_CODE = "extra_int_session_ended_status_code";

    @Deprecated
    public static final class CastRemoteDisplayOptions implements HasOptions {
        final CastDevice zzai;
        final CastRemoteDisplaySessionCallbacks zzba;
        final int zzbb;

        @Deprecated
        public static final class Builder {
            CastDevice zzai;
            int zzbb = 2;
            CastRemoteDisplaySessionCallbacks zzbc;

            public Builder(CastDevice castDevice, CastRemoteDisplaySessionCallbacks castRemoteDisplaySessionCallbacks) {
                Preconditions.checkNotNull(castDevice, "CastDevice parameter cannot be null");
                this.zzai = castDevice;
                this.zzbc = castRemoteDisplaySessionCallbacks;
            }

            public final CastRemoteDisplayOptions build() {
                return new CastRemoteDisplayOptions();
            }

            public final Builder setConfigPreset(@Configuration int i) {
                this.zzbb = i;
                return this;
            }
        }

        private CastRemoteDisplayOptions(Builder builder) {
            this.zzai = builder.zzai;
            this.zzba = builder.zzbc;
            this.zzbb = builder.zzbb;
        }
    }

    @Deprecated
    public interface CastRemoteDisplaySessionCallbacks {
        void onRemoteDisplayEnded(Status status);
    }

    @Deprecated
    public interface CastRemoteDisplaySessionResult extends Result {
        Display getPresentationDisplay();
    }

    public @interface Configuration {
    }

    private CastRemoteDisplay() {
    }

    public static CastRemoteDisplayClient getClient(@NonNull Context context) {
        return new CastRemoteDisplayClient(context);
    }

    public static final boolean isRemoteDisplaySdkSupported(Context context) {
        zzcx.initialize(context);
        return ((Boolean) zzcx.zzwc.get()).booleanValue();
    }
}
