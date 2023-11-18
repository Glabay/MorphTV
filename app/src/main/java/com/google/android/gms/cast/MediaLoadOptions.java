package com.google.android.gms.cast;

import org.json.JSONObject;

public class MediaLoadOptions {
    public static final double PLAYBACK_RATE_MAX = 2.0d;
    public static final double PLAYBACK_RATE_MIN = 0.5d;
    private boolean zzdj;
    private long zzdk;
    private double zzdl;
    private long[] zzdm;
    private String zzdn;
    private String zzdo;
    private JSONObject zzp;

    public static class Builder {
        private boolean zzdj = true;
        private long zzdk = 0;
        private double zzdl = 1.0d;
        private long[] zzdm = null;
        private String zzdn = null;
        private String zzdo = null;
        private JSONObject zzp = null;

        public MediaLoadOptions build() {
            return new MediaLoadOptions(this.zzdj, this.zzdk, this.zzdl, this.zzdm, this.zzp, this.zzdn, this.zzdo);
        }

        public Builder setActiveTrackIds(long[] jArr) {
            this.zzdm = jArr;
            return this;
        }

        public Builder setAutoplay(boolean z) {
            this.zzdj = z;
            return this;
        }

        public Builder setCredentials(String str) {
            this.zzdn = str;
            return this;
        }

        public Builder setCredentialsType(String str) {
            this.zzdo = str;
            return this;
        }

        public Builder setCustomData(JSONObject jSONObject) {
            this.zzp = jSONObject;
            return this;
        }

        public Builder setPlayPosition(long j) {
            this.zzdk = j;
            return this;
        }

        public Builder setPlaybackRate(double d) {
            if (Double.compare(d, 2.0d) <= 0) {
                if (Double.compare(d, 0.5d) >= 0) {
                    this.zzdl = d;
                    return this;
                }
            }
            throw new IllegalArgumentException("playbackRate must be between PLAYBACK_RATE_MIN and PLAYBACK_RATE_MAX");
        }
    }

    private MediaLoadOptions(boolean z, long j, double d, long[] jArr, JSONObject jSONObject, String str, String str2) {
        this.zzdj = z;
        this.zzdk = j;
        this.zzdl = d;
        this.zzdm = jArr;
        this.zzp = jSONObject;
        this.zzdn = str;
        this.zzdo = str2;
    }

    public long[] getActiveTrackIds() {
        return this.zzdm;
    }

    public boolean getAutoplay() {
        return this.zzdj;
    }

    public String getCredentials() {
        return this.zzdn;
    }

    public String getCredentialsType() {
        return this.zzdo;
    }

    public JSONObject getCustomData() {
        return this.zzp;
    }

    public long getPlayPosition() {
        return this.zzdk;
    }

    public double getPlaybackRate() {
        return this.zzdl;
    }
}
