package com.google.android.gms.cast;

import android.text.TextUtils;
import com.google.android.gms.internal.cast.zzcu;
import java.util.Collection;
import java.util.Locale;

public final class CastMediaControlIntent {
    public static final String ACTION_SYNC_STATUS = "com.google.android.gms.cast.ACTION_SYNC_STATUS";
    public static final String DEFAULT_MEDIA_RECEIVER_APPLICATION_ID = "3B221EB7";
    public static final int ERROR_CODE_REQUEST_FAILED = 1;
    public static final int ERROR_CODE_SESSION_START_FAILED = 2;
    public static final int ERROR_CODE_TEMPORARILY_DISCONNECTED = 3;
    public static final String EXTRA_CAST_APPLICATION_ID = "com.google.android.gms.cast.EXTRA_CAST_APPLICATION_ID";
    public static final String EXTRA_CAST_LANGUAGE_CODE = "com.google.android.gms.cast.EXTRA_CAST_LANGUAGE_CODE";
    public static final String EXTRA_CAST_RELAUNCH_APPLICATION = "com.google.android.gms.cast.EXTRA_CAST_RELAUNCH_APPLICATION";
    public static final String EXTRA_CAST_STOP_APPLICATION_WHEN_SESSION_ENDS = "com.google.android.gms.cast.EXTRA_CAST_STOP_APPLICATION_WHEN_SESSION_ENDS";
    public static final String EXTRA_CUSTOM_DATA = "com.google.android.gms.cast.EXTRA_CUSTOM_DATA";
    public static final String EXTRA_DEBUG_LOGGING_ENABLED = "com.google.android.gms.cast.EXTRA_DEBUG_LOGGING_ENABLED";
    public static final String EXTRA_ERROR_CODE = "com.google.android.gms.cast.EXTRA_ERROR_CODE";

    private CastMediaControlIntent() {
    }

    public static String categoryForCast(String str) throws IllegalArgumentException {
        if (str != null) {
            return zza("com.google.android.gms.cast.CATEGORY_CAST", str, null, false);
        }
        throw new IllegalArgumentException("applicationId cannot be null");
    }

    public static String categoryForCast(String str, Collection<String> collection) {
        if (str == null) {
            throw new IllegalArgumentException("applicationId cannot be null");
        } else if (collection != null) {
            return zza("com.google.android.gms.cast.CATEGORY_CAST", str, collection, false);
        } else {
            throw new IllegalArgumentException("namespaces cannot be null");
        }
    }

    public static String categoryForCast(Collection<String> collection) throws IllegalArgumentException {
        if (collection != null) {
            return zza("com.google.android.gms.cast.CATEGORY_CAST", null, collection, false);
        }
        throw new IllegalArgumentException("namespaces cannot be null");
    }

    public static String categoryForRemotePlayback() {
        return zza("com.google.android.gms.cast.CATEGORY_CAST_REMOTE_PLAYBACK", null, null, false);
    }

    public static String categoryForRemotePlayback(String str) throws IllegalArgumentException {
        if (!TextUtils.isEmpty(str)) {
            return zza("com.google.android.gms.cast.CATEGORY_CAST_REMOTE_PLAYBACK", str, null, false);
        }
        throw new IllegalArgumentException("applicationId cannot be null or empty");
    }

    public static String languageTagForLocale(Locale locale) {
        return zzcu.zza(locale);
    }

    private static String zza(String str, String str2, Collection<String> collection, boolean z) throws IllegalArgumentException {
        StringBuilder stringBuilder = new StringBuilder(str);
        if (str2 != null) {
            str = str2.toUpperCase();
            if (str.matches("[A-F0-9]+")) {
                stringBuilder.append("/");
                stringBuilder.append(str);
            } else {
                String str3 = "Invalid application ID: ";
                str2 = String.valueOf(str2);
                throw new IllegalArgumentException(str2.length() != 0 ? str3.concat(str2) : new String(str3));
            }
        }
        if (collection != null) {
            if (collection.isEmpty()) {
                throw new IllegalArgumentException("Must specify at least one namespace");
            }
            if (str2 == null) {
                stringBuilder.append("/");
            }
            stringBuilder.append("/");
            Object obj = 1;
            for (String str32 : collection) {
                zzcu.zzo(str32);
                if (obj != null) {
                    obj = null;
                } else {
                    stringBuilder.append(",");
                }
                stringBuilder.append(zzcu.zzq(str32));
            }
        }
        return stringBuilder.toString();
    }
}
