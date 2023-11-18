package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.base.C0780R;
import com.google.android.gms.common.C0783R;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.util.DeviceProperties;
import de.timroes.axmlrpc.serializer.SerializerHandler;
import javax.annotation.concurrent.GuardedBy;

public final class ConnectionErrorMessages {
    @GuardedBy("sCache")
    private static final SimpleArrayMap<String, String> zzse = new SimpleArrayMap();

    private ConnectionErrorMessages() {
    }

    public static java.lang.String getAppName(android.content.Context r2) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = r2.getPackageName();
        r1 = com.google.android.gms.common.wrappers.Wrappers.packageManager(r2);	 Catch:{ NameNotFoundException -> 0x0011, NameNotFoundException -> 0x0011 }
        r1 = r1.getApplicationLabel(r0);	 Catch:{ NameNotFoundException -> 0x0011, NameNotFoundException -> 0x0011 }
        r1 = r1.toString();	 Catch:{ NameNotFoundException -> 0x0011, NameNotFoundException -> 0x0011 }
        return r1;
    L_0x0011:
        r2 = r2.getApplicationInfo();
        r2 = r2.name;
        r1 = android.text.TextUtils.isEmpty(r2);
        if (r1 == 0) goto L_0x001e;
    L_0x001d:
        return r0;
    L_0x001e:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.ConnectionErrorMessages.getAppName(android.content.Context):java.lang.String");
    }

    public static String getDefaultNotificationChannelName(Context context) {
        return context.getResources().getString(C0780R.string.common_google_play_services_notification_channel_name);
    }

    @NonNull
    public static String getErrorDialogButtonMessage(Context context, int i) {
        Resources resources = context.getResources();
        switch (i) {
            case 1:
                i = C0780R.string.common_google_play_services_install_button;
                break;
            case 2:
                i = C0780R.string.common_google_play_services_update_button;
                break;
            case 3:
                i = C0780R.string.common_google_play_services_enable_button;
                break;
            default:
                i = 17039370;
                break;
        }
        return resources.getString(i);
    }

    @NonNull
    public static String getErrorMessage(Context context, int i) {
        Resources resources = context.getResources();
        String appName = getAppName(context);
        if (i == 5) {
            return zza(context, "common_google_play_services_invalid_account_text", appName);
        }
        if (i == 7) {
            return zza(context, "common_google_play_services_network_error_text", appName);
        }
        if (i == 9) {
            return resources.getString(C0780R.string.common_google_play_services_unsupported_text, new Object[]{appName});
        } else if (i == 20) {
            return zza(context, "common_google_play_services_restricted_profile_text", appName);
        } else {
            switch (i) {
                case 1:
                    return resources.getString(C0780R.string.common_google_play_services_install_text, new Object[]{appName});
                case 2:
                    if (DeviceProperties.isWearableWithoutPlayStore(context)) {
                        return resources.getString(C0780R.string.common_google_play_services_wear_update_text);
                    }
                    return resources.getString(C0780R.string.common_google_play_services_update_text, new Object[]{appName});
                case 3:
                    return resources.getString(C0780R.string.common_google_play_services_enable_text, new Object[]{appName});
                default:
                    switch (i) {
                        case 16:
                            return zza(context, "common_google_play_services_api_unavailable_text", appName);
                        case 17:
                            return zza(context, "common_google_play_services_sign_in_failed_text", appName);
                        case 18:
                            return resources.getString(C0780R.string.common_google_play_services_updating_text, new Object[]{appName});
                        default:
                            return resources.getString(C0783R.string.common_google_play_services_unknown_issue, new Object[]{appName});
                    }
            }
        }
    }

    @NonNull
    public static String getErrorNotificationMessage(Context context, int i) {
        return i == 6 ? zza(context, "common_google_play_services_resolution_required_text", getAppName(context)) : getErrorMessage(context, i);
    }

    @NonNull
    public static String getErrorNotificationTitle(Context context, int i) {
        String zzb = i == 6 ? zzb(context, "common_google_play_services_resolution_required_title") : getErrorTitle(context, i);
        return zzb == null ? context.getResources().getString(C0780R.string.common_google_play_services_notification_ticker) : zzb;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.support.annotation.Nullable
    public static java.lang.String getErrorTitle(android.content.Context r3, int r4) {
        /*
        r0 = r3.getResources();
        r1 = 20;
        if (r4 == r1) goto L_0x0081;
    L_0x0008:
        r1 = 0;
        switch(r4) {
            case 1: goto L_0x007a;
            case 2: goto L_0x0073;
            case 3: goto L_0x006c;
            case 4: goto L_0x006b;
            case 5: goto L_0x005d;
            case 6: goto L_0x006b;
            case 7: goto L_0x004f;
            case 8: goto L_0x004a;
            case 9: goto L_0x0045;
            case 10: goto L_0x0040;
            case 11: goto L_0x003b;
            default: goto L_0x000c;
        };
    L_0x000c:
        switch(r4) {
            case 16: goto L_0x0036;
            case 17: goto L_0x0028;
            case 18: goto L_0x006b;
            default: goto L_0x000f;
        };
    L_0x000f:
        r3 = "GoogleApiAvailability";
        r0 = 33;
        r2 = new java.lang.StringBuilder;
        r2.<init>(r0);
        r0 = "Unexpected error code ";
        r2.append(r0);
        r2.append(r4);
        r4 = r2.toString();
    L_0x0024:
        android.util.Log.e(r3, r4);
        return r1;
    L_0x0028:
        r4 = "GoogleApiAvailability";
        r0 = "The specified account could not be signed in.";
        android.util.Log.e(r4, r0);
        r4 = "common_google_play_services_sign_in_failed_title";
        r3 = zzb(r3, r4);
        return r3;
    L_0x0036:
        r3 = "GoogleApiAvailability";
        r4 = "One of the API components you attempted to connect to is not available.";
        goto L_0x0024;
    L_0x003b:
        r3 = "GoogleApiAvailability";
        r4 = "The application is not licensed to the user.";
        goto L_0x0024;
    L_0x0040:
        r3 = "GoogleApiAvailability";
        r4 = "Developer error occurred. Please see logs for detailed information";
        goto L_0x0024;
    L_0x0045:
        r3 = "GoogleApiAvailability";
        r4 = "Google Play services is invalid. Cannot recover.";
        goto L_0x0024;
    L_0x004a:
        r3 = "GoogleApiAvailability";
        r4 = "Internal error occurred. Please see logs for detailed information";
        goto L_0x0024;
    L_0x004f:
        r4 = "GoogleApiAvailability";
        r0 = "Network error occurred. Please retry request later.";
        android.util.Log.e(r4, r0);
        r4 = "common_google_play_services_network_error_title";
        r3 = zzb(r3, r4);
        return r3;
    L_0x005d:
        r4 = "GoogleApiAvailability";
        r0 = "An invalid account was specified when connecting. Please provide a valid account.";
        android.util.Log.e(r4, r0);
        r4 = "common_google_play_services_invalid_account_title";
        r3 = zzb(r3, r4);
        return r3;
    L_0x006b:
        return r1;
    L_0x006c:
        r3 = com.google.android.gms.base.C0780R.string.common_google_play_services_enable_title;
        r3 = r0.getString(r3);
        return r3;
    L_0x0073:
        r3 = com.google.android.gms.base.C0780R.string.common_google_play_services_update_title;
        r3 = r0.getString(r3);
        return r3;
    L_0x007a:
        r3 = com.google.android.gms.base.C0780R.string.common_google_play_services_install_title;
        r3 = r0.getString(r3);
        return r3;
    L_0x0081:
        r4 = "GoogleApiAvailability";
        r0 = "The current user profile is restricted and could not use authenticated features.";
        android.util.Log.e(r4, r0);
        r4 = "common_google_play_services_restricted_profile_title";
        r3 = zzb(r3, r4);
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.ConnectionErrorMessages.getErrorTitle(android.content.Context, int):java.lang.String");
    }

    private static String zza(Context context, String str, String str2) {
        Resources resources = context.getResources();
        String zzb = zzb(context, str);
        if (zzb == null) {
            zzb = resources.getString(C0783R.string.common_google_play_services_unknown_issue);
        }
        return String.format(resources.getConfiguration().locale, zzb, new Object[]{str2});
    }

    @Nullable
    private static String zzb(Context context, String str) {
        synchronized (zzse) {
            String str2 = (String) zzse.get(str);
            if (str2 != null) {
                return str2;
            }
            Resources remoteResource = GooglePlayServicesUtil.getRemoteResource(context);
            if (remoteResource == null) {
                return null;
            }
            int identifier = remoteResource.getIdentifier(str, SerializerHandler.TYPE_STRING, "com.google.android.gms");
            if (identifier == 0) {
                String str3 = "GoogleApiAvailability";
                String str4 = "Missing resource: ";
                str = String.valueOf(str);
                Log.w(str3, str.length() != 0 ? str4.concat(str) : new String(str4));
                return null;
            }
            Object string = remoteResource.getString(identifier);
            if (TextUtils.isEmpty(string)) {
                str3 = "GoogleApiAvailability";
                str4 = "Got empty resource: ";
                str = String.valueOf(str);
                Log.w(str3, str.length() != 0 ? str4.concat(str) : new String(str4));
                return null;
            }
            zzse.put(str, string);
            return string;
        }
    }
}
