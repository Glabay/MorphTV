package com.google.android.exoplayer2.drm;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.drm.DefaultDrmSession.ProvisioningManager;
import com.google.android.exoplayer2.drm.DrmInitData.SchemeData;
import com.google.android.exoplayer2.drm.DrmSession.DrmSessionException;
import com.google.android.exoplayer2.drm.ExoMediaDrm.OnEventListener;
import com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@TargetApi(18)
public class DefaultDrmSessionManager<T extends ExoMediaCrypto> implements DrmSessionManager<T>, ProvisioningManager<T> {
    private static final String CENC_SCHEME_MIME_TYPE = "cenc";
    public static final int INITIAL_DRM_REQUEST_RETRY_COUNT = 3;
    public static final int MODE_DOWNLOAD = 2;
    public static final int MODE_PLAYBACK = 0;
    public static final int MODE_QUERY = 1;
    public static final int MODE_RELEASE = 3;
    public static final String PLAYREADY_CUSTOM_DATA_KEY = "PRCustomData";
    private static final String TAG = "DefaultDrmSessionMgr";
    private final MediaDrmCallback callback;
    private final Handler eventHandler;
    private final EventListener eventListener;
    private final int initialDrmRequestRetryCount;
    private final ExoMediaDrm<T> mediaDrm;
    volatile MediaDrmHandler mediaDrmHandler;
    private int mode;
    private final boolean multiSession;
    private byte[] offlineLicenseKeySetId;
    private final HashMap<String, String> optionalKeyRequestParameters;
    private Looper playbackLooper;
    private final List<DefaultDrmSession<T>> provisioningSessions;
    private final List<DefaultDrmSession<T>> sessions;
    private final UUID uuid;

    public interface EventListener {
        void onDrmKeysLoaded();

        void onDrmKeysRemoved();

        void onDrmKeysRestored();

        void onDrmSessionManagerError(Exception exception);
    }

    private class MediaDrmEventListener implements OnEventListener<T> {
        private MediaDrmEventListener() {
        }

        public void onEvent(ExoMediaDrm<? extends T> exoMediaDrm, byte[] bArr, int i, int i2, byte[] bArr2) {
            if (DefaultDrmSessionManager.this.mode == null) {
                DefaultDrmSessionManager.this.mediaDrmHandler.obtainMessage(i, bArr).sendToTarget();
            }
        }
    }

    @SuppressLint({"HandlerLeak"})
    private class MediaDrmHandler extends Handler {
        public MediaDrmHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            byte[] bArr = (byte[]) message.obj;
            for (DefaultDrmSession defaultDrmSession : DefaultDrmSessionManager.this.sessions) {
                if (defaultDrmSession.hasSessionId(bArr)) {
                    defaultDrmSession.onMediaDrmEvent(message.what);
                    return;
                }
            }
        }
    }

    public static final class MissingSchemeDataException extends Exception {
        private MissingSchemeDataException(UUID uuid) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Media does not support uuid: ");
            stringBuilder.append(uuid);
            super(stringBuilder.toString());
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    public static DefaultDrmSessionManager<FrameworkMediaCrypto> newWidevineInstance(MediaDrmCallback mediaDrmCallback, HashMap<String, String> hashMap, Handler handler, EventListener eventListener) throws UnsupportedDrmException {
        return newFrameworkInstance(C0649C.WIDEVINE_UUID, mediaDrmCallback, hashMap, handler, eventListener);
    }

    public static DefaultDrmSessionManager<FrameworkMediaCrypto> newPlayReadyInstance(MediaDrmCallback mediaDrmCallback, String str, Handler handler, EventListener eventListener) throws UnsupportedDrmException {
        HashMap hashMap;
        if (TextUtils.isEmpty(str)) {
            hashMap = null;
        } else {
            hashMap = new HashMap();
            hashMap.put(PLAYREADY_CUSTOM_DATA_KEY, str);
        }
        return newFrameworkInstance(C0649C.PLAYREADY_UUID, mediaDrmCallback, hashMap, handler, eventListener);
    }

    public static DefaultDrmSessionManager<FrameworkMediaCrypto> newFrameworkInstance(UUID uuid, MediaDrmCallback mediaDrmCallback, HashMap<String, String> hashMap, Handler handler, EventListener eventListener) throws UnsupportedDrmException {
        return new DefaultDrmSessionManager(uuid, FrameworkMediaDrm.newInstance(uuid), mediaDrmCallback, hashMap, handler, eventListener, false, 3);
    }

    public DefaultDrmSessionManager(UUID uuid, ExoMediaDrm<T> exoMediaDrm, MediaDrmCallback mediaDrmCallback, HashMap<String, String> hashMap, Handler handler, EventListener eventListener) {
        this(uuid, exoMediaDrm, mediaDrmCallback, hashMap, handler, eventListener, false, 3);
    }

    public DefaultDrmSessionManager(UUID uuid, ExoMediaDrm<T> exoMediaDrm, MediaDrmCallback mediaDrmCallback, HashMap<String, String> hashMap, Handler handler, EventListener eventListener, boolean z) {
        this(uuid, exoMediaDrm, mediaDrmCallback, hashMap, handler, eventListener, z, 3);
    }

    public DefaultDrmSessionManager(UUID uuid, ExoMediaDrm<T> exoMediaDrm, MediaDrmCallback mediaDrmCallback, HashMap<String, String> hashMap, Handler handler, EventListener eventListener, boolean z, int i) {
        Assertions.checkNotNull(uuid);
        Assertions.checkNotNull(exoMediaDrm);
        Assertions.checkArgument(C0649C.COMMON_PSSH_UUID.equals(uuid) ^ 1, "Use C.CLEARKEY_UUID instead");
        this.uuid = uuid;
        this.mediaDrm = exoMediaDrm;
        this.callback = mediaDrmCallback;
        this.optionalKeyRequestParameters = hashMap;
        this.eventHandler = handler;
        this.eventListener = eventListener;
        this.multiSession = z;
        this.initialDrmRequestRetryCount = i;
        this.mode = null;
        this.sessions = new ArrayList();
        this.provisioningSessions = new ArrayList();
        if (z) {
            exoMediaDrm.setPropertyString("sessionSharing", "enable");
        }
        exoMediaDrm.setOnEventListener(new MediaDrmEventListener());
    }

    public final String getPropertyString(String str) {
        return this.mediaDrm.getPropertyString(str);
    }

    public final void setPropertyString(String str, String str2) {
        this.mediaDrm.setPropertyString(str, str2);
    }

    public final byte[] getPropertyByteArray(String str) {
        return this.mediaDrm.getPropertyByteArray(str);
    }

    public final void setPropertyByteArray(String str, byte[] bArr) {
        this.mediaDrm.setPropertyByteArray(str, bArr);
    }

    public void setMode(int i, byte[] bArr) {
        Assertions.checkState(this.sessions.isEmpty());
        if (i == 1 || i == 3) {
            Assertions.checkNotNull(bArr);
        }
        this.mode = i;
        this.offlineLicenseKeySetId = bArr;
    }

    public boolean canAcquireSession(@NonNull DrmInitData drmInitData) {
        boolean z = true;
        if (this.offlineLicenseKeySetId != null) {
            return true;
        }
        if (getSchemeData(drmInitData, this.uuid, true) == null) {
            if (drmInitData.schemeDataCount != 1 || !drmInitData.get(0).matches(C0649C.COMMON_PSSH_UUID)) {
                return false;
            }
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DrmInitData only contains common PSSH SchemeData. Assuming support for: ");
            stringBuilder.append(this.uuid);
            Log.w(str, stringBuilder.toString());
        }
        drmInitData = drmInitData.schemeType;
        if (drmInitData != null) {
            if (!"cenc".equals(drmInitData)) {
                if (!(C0649C.CENC_TYPE_cbc1.equals(drmInitData) || C0649C.CENC_TYPE_cbcs.equals(drmInitData))) {
                    if (C0649C.CENC_TYPE_cens.equals(drmInitData) == null) {
                        return true;
                    }
                }
                if (Util.SDK_INT < 24) {
                    z = false;
                }
                return z;
            }
        }
        return true;
    }

    public DrmSession<T> acquireSession(Looper looper, DrmInitData drmInitData) {
        boolean z;
        C06761 c06761;
        SchemeData schemeData;
        final Throwable missingSchemeDataException;
        String schemeMimeType;
        byte[] bArr;
        DrmSession drmSession;
        Looper looper2 = looper;
        if (this.playbackLooper != null) {
            if (r14.playbackLooper != looper2) {
                z = false;
                Assertions.checkState(z);
                if (r14.sessions.isEmpty()) {
                    r14.playbackLooper = looper2;
                    if (r14.mediaDrmHandler == null) {
                        r14.mediaDrmHandler = new MediaDrmHandler(looper2);
                    }
                }
                c06761 = null;
                if (r14.offlineLicenseKeySetId != null) {
                    schemeData = getSchemeData(drmInitData, r14.uuid, false);
                    if (schemeData != null) {
                        missingSchemeDataException = new MissingSchemeDataException(r14.uuid);
                        if (!(r14.eventHandler == null || r14.eventListener == null)) {
                            r14.eventHandler.post(new Runnable() {
                                public void run() {
                                    DefaultDrmSessionManager.this.eventListener.onDrmSessionManagerError(missingSchemeDataException);
                                }
                            });
                        }
                        return new ErrorStateDrmSession(new DrmSessionException(missingSchemeDataException));
                    }
                    byte[] schemeInitData = getSchemeInitData(schemeData, r14.uuid);
                    schemeMimeType = getSchemeMimeType(schemeData, r14.uuid);
                    bArr = schemeInitData;
                } else {
                    bArr = null;
                    schemeMimeType = bArr;
                }
                if (!r14.multiSession) {
                    for (DefaultDrmSession defaultDrmSession : r14.sessions) {
                        if (defaultDrmSession.hasInitData(bArr)) {
                            c06761 = defaultDrmSession;
                            break;
                        }
                    }
                } else if (r14.sessions.isEmpty()) {
                    c06761 = (DefaultDrmSession) r14.sessions.get(0);
                }
                if (c06761 != null) {
                    DrmSession defaultDrmSession2 = new DefaultDrmSession(r14.uuid, r14.mediaDrm, r14, bArr, schemeMimeType, r14.mode, r14.offlineLicenseKeySetId, r14.optionalKeyRequestParameters, r14.callback, looper2, r14.eventHandler, r14.eventListener, r14.initialDrmRequestRetryCount);
                    r14.sessions.add(defaultDrmSession2);
                } else {
                    drmSession = c06761;
                }
                drmSession.acquire();
                return drmSession;
            }
        }
        z = true;
        Assertions.checkState(z);
        if (r14.sessions.isEmpty()) {
            r14.playbackLooper = looper2;
            if (r14.mediaDrmHandler == null) {
                r14.mediaDrmHandler = new MediaDrmHandler(looper2);
            }
        }
        c06761 = null;
        if (r14.offlineLicenseKeySetId != null) {
            bArr = null;
            schemeMimeType = bArr;
        } else {
            schemeData = getSchemeData(drmInitData, r14.uuid, false);
            if (schemeData != null) {
                byte[] schemeInitData2 = getSchemeInitData(schemeData, r14.uuid);
                schemeMimeType = getSchemeMimeType(schemeData, r14.uuid);
                bArr = schemeInitData2;
            } else {
                missingSchemeDataException = new MissingSchemeDataException(r14.uuid);
                r14.eventHandler.post(/* anonymous class already generated */);
                return new ErrorStateDrmSession(new DrmSessionException(missingSchemeDataException));
            }
        }
        if (!r14.multiSession) {
            for (DefaultDrmSession defaultDrmSession3 : r14.sessions) {
                if (defaultDrmSession3.hasInitData(bArr)) {
                    c06761 = defaultDrmSession3;
                    break;
                }
            }
        } else if (r14.sessions.isEmpty()) {
            c06761 = (DefaultDrmSession) r14.sessions.get(0);
        }
        if (c06761 != null) {
            drmSession = c06761;
        } else {
            DrmSession defaultDrmSession22 = new DefaultDrmSession(r14.uuid, r14.mediaDrm, r14, bArr, schemeMimeType, r14.mode, r14.offlineLicenseKeySetId, r14.optionalKeyRequestParameters, r14.callback, looper2, r14.eventHandler, r14.eventListener, r14.initialDrmRequestRetryCount);
            r14.sessions.add(defaultDrmSession22);
        }
        drmSession.acquire();
        return drmSession;
    }

    public void releaseSession(DrmSession<T> drmSession) {
        if (!(drmSession instanceof ErrorStateDrmSession)) {
            DefaultDrmSession defaultDrmSession = (DefaultDrmSession) drmSession;
            if (defaultDrmSession.release()) {
                this.sessions.remove(defaultDrmSession);
                if (this.provisioningSessions.size() > 1 && this.provisioningSessions.get(0) == defaultDrmSession) {
                    ((DefaultDrmSession) this.provisioningSessions.get(1)).provision();
                }
                this.provisioningSessions.remove(defaultDrmSession);
            }
        }
    }

    public void provisionRequired(DefaultDrmSession<T> defaultDrmSession) {
        this.provisioningSessions.add(defaultDrmSession);
        if (this.provisioningSessions.size() == 1) {
            defaultDrmSession.provision();
        }
    }

    public void onProvisionCompleted() {
        for (DefaultDrmSession onProvisionCompleted : this.provisioningSessions) {
            onProvisionCompleted.onProvisionCompleted();
        }
        this.provisioningSessions.clear();
    }

    public void onProvisionError(Exception exception) {
        for (DefaultDrmSession onProvisionError : this.provisioningSessions) {
            onProvisionError.onProvisionError(exception);
        }
        this.provisioningSessions.clear();
    }

    private static SchemeData getSchemeData(DrmInitData drmInitData, UUID uuid, boolean z) {
        List arrayList = new ArrayList(drmInitData.schemeDataCount);
        int i = 0;
        while (true) {
            Object obj = 1;
            if (i >= drmInitData.schemeDataCount) {
                break;
            }
            SchemeData schemeData = drmInitData.get(i);
            if (!schemeData.matches(uuid)) {
                if (!C0649C.CLEARKEY_UUID.equals(uuid) || !schemeData.matches(C0649C.COMMON_PSSH_UUID)) {
                    obj = null;
                }
            }
            if (obj != null && (schemeData.data != null || z)) {
                arrayList.add(schemeData);
            }
            i++;
        }
        if (arrayList.isEmpty() != null) {
            return null;
        }
        if (C0649C.WIDEVINE_UUID.equals(uuid) != null) {
            for (drmInitData = null; drmInitData < arrayList.size(); drmInitData++) {
                SchemeData schemeData2 = (SchemeData) arrayList.get(drmInitData);
                z = schemeData2.hasData() ? PsshAtomUtil.parseVersion(schemeData2.data) : true;
                if (Util.SDK_INT < 23 && !z) {
                    return schemeData2;
                }
                if (Util.SDK_INT >= 23 && z) {
                    return schemeData2;
                }
            }
        }
        return (SchemeData) arrayList.get(0);
    }

    private static byte[] getSchemeInitData(SchemeData schemeData, UUID uuid) {
        schemeData = schemeData.data;
        if (Util.SDK_INT >= 21) {
            return schemeData;
        }
        uuid = PsshAtomUtil.parseSchemeSpecificData(schemeData, uuid);
        return uuid == null ? schemeData : uuid;
    }

    private static String getSchemeMimeType(SchemeData schemeData, UUID uuid) {
        schemeData = schemeData.mimeType;
        if (Util.SDK_INT >= 26 || C0649C.CLEARKEY_UUID.equals(uuid) == null) {
            return schemeData;
        }
        return (MimeTypes.VIDEO_MP4.equals(schemeData) == null && MimeTypes.AUDIO_MP4.equals(schemeData) == null) ? schemeData : "cenc";
    }
}
