package android.support.v7.media;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.app.ActivityManagerCompat;
import android.support.v4.hardware.display.DisplayManagerCompat;
import android.support.v4.media.VolumeProviderCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.MediaSessionCompat.OnActiveChangeListener;
import android.support.v4.media.session.MediaSessionCompat.Token;
import android.support.v4.util.Pair;
import android.support.v7.media.MediaRouteProvider.ProviderMetadata;
import android.support.v7.media.MediaRouteProvider.RouteController;
import android.support.v7.media.MediaRouteSelector.Builder;
import android.support.v7.media.RemoteControlClientCompat.PlaybackInfo;
import android.support.v7.media.RemoteControlClientCompat.VolumeCallback;
import android.support.v7.media.SystemMediaRouteProvider.SyncCallback;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import de.timroes.axmlrpc.serializer.SerializerHandler;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class MediaRouter {
    public static final int AVAILABILITY_FLAG_IGNORE_DEFAULT_ROUTE = 1;
    public static final int AVAILABILITY_FLAG_REQUIRE_MATCH = 2;
    public static final int CALLBACK_FLAG_FORCE_DISCOVERY = 8;
    public static final int CALLBACK_FLAG_PERFORM_ACTIVE_SCAN = 1;
    public static final int CALLBACK_FLAG_REQUEST_DISCOVERY = 4;
    public static final int CALLBACK_FLAG_UNFILTERED_EVENTS = 2;
    static final boolean DEBUG = Log.isLoggable(TAG, 3);
    static final String TAG = "MediaRouter";
    public static final int UNSELECT_REASON_DISCONNECTED = 1;
    public static final int UNSELECT_REASON_ROUTE_CHANGED = 3;
    public static final int UNSELECT_REASON_STOPPED = 2;
    public static final int UNSELECT_REASON_UNKNOWN = 0;
    static GlobalMediaRouter sGlobal;
    final ArrayList<CallbackRecord> mCallbackRecords;
    final Context mContext;

    public static abstract class Callback {
        public void onProviderAdded(MediaRouter mediaRouter, ProviderInfo providerInfo) {
        }

        public void onProviderChanged(MediaRouter mediaRouter, ProviderInfo providerInfo) {
        }

        public void onProviderRemoved(MediaRouter mediaRouter, ProviderInfo providerInfo) {
        }

        public void onRouteAdded(MediaRouter mediaRouter, RouteInfo routeInfo) {
        }

        public void onRouteChanged(MediaRouter mediaRouter, RouteInfo routeInfo) {
        }

        public void onRoutePresentationDisplayChanged(MediaRouter mediaRouter, RouteInfo routeInfo) {
        }

        public void onRouteRemoved(MediaRouter mediaRouter, RouteInfo routeInfo) {
        }

        public void onRouteSelected(MediaRouter mediaRouter, RouteInfo routeInfo) {
        }

        public void onRouteUnselected(MediaRouter mediaRouter, RouteInfo routeInfo) {
        }

        public void onRouteVolumeChanged(MediaRouter mediaRouter, RouteInfo routeInfo) {
        }

        public void onRouteUnselected(MediaRouter mediaRouter, RouteInfo routeInfo, int i) {
            onRouteUnselected(mediaRouter, routeInfo);
        }
    }

    public static abstract class ControlRequestCallback {
        public void onError(String str, Bundle bundle) {
        }

        public void onResult(Bundle bundle) {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface CallbackFlags {
    }

    private static final class CallbackRecord {
        public final Callback mCallback;
        public int mFlags;
        public final MediaRouter mRouter;
        public MediaRouteSelector mSelector = MediaRouteSelector.EMPTY;

        public CallbackRecord(MediaRouter mediaRouter, Callback callback) {
            this.mRouter = mediaRouter;
            this.mCallback = callback;
        }

        public boolean filterRouteEvent(RouteInfo routeInfo) {
            if ((this.mFlags & 2) == 0) {
                if (routeInfo.matchesSelector(this.mSelector) == null) {
                    return null;
                }
            }
            return true;
        }
    }

    private static final class GlobalMediaRouter implements SyncCallback, android.support.v7.media.RegisteredMediaRouteProviderWatcher.Callback {
        final Context mApplicationContext;
        private RouteInfo mBluetoothRoute;
        final CallbackHandler mCallbackHandler = new CallbackHandler();
        private MediaSessionCompat mCompatSession;
        private RouteInfo mDefaultRoute;
        private MediaRouteDiscoveryRequest mDiscoveryRequest;
        private final DisplayManagerCompat mDisplayManager;
        private final boolean mLowRam;
        private MediaSessionRecord mMediaSession;
        final PlaybackInfo mPlaybackInfo = new PlaybackInfo();
        private final ProviderCallback mProviderCallback = new ProviderCallback();
        private final ArrayList<ProviderInfo> mProviders = new ArrayList();
        MediaSessionCompat mRccMediaSession;
        private RegisteredMediaRouteProviderWatcher mRegisteredProviderWatcher;
        private final ArrayList<RemoteControlClientRecord> mRemoteControlClients = new ArrayList();
        private final Map<String, RouteController> mRouteControllerMap = new HashMap();
        final ArrayList<WeakReference<MediaRouter>> mRouters = new ArrayList();
        private final ArrayList<RouteInfo> mRoutes = new ArrayList();
        RouteInfo mSelectedRoute;
        private RouteController mSelectedRouteController;
        private OnActiveChangeListener mSessionActiveListener = new C02561();
        final SystemMediaRouteProvider mSystemProvider;
        private final Map<Pair<String, String>, String> mUniqueIdMap = new HashMap();

        /* renamed from: android.support.v7.media.MediaRouter$GlobalMediaRouter$1 */
        class C02561 implements OnActiveChangeListener {
            C02561() {
            }

            public void onActiveChanged() {
                if (GlobalMediaRouter.this.mRccMediaSession == null) {
                    return;
                }
                if (GlobalMediaRouter.this.mRccMediaSession.isActive()) {
                    GlobalMediaRouter.this.addRemoteControlClient(GlobalMediaRouter.this.mRccMediaSession.getRemoteControlClient());
                } else {
                    GlobalMediaRouter.this.removeRemoteControlClient(GlobalMediaRouter.this.mRccMediaSession.getRemoteControlClient());
                }
            }
        }

        private final class CallbackHandler extends Handler {
            public static final int MSG_PROVIDER_ADDED = 513;
            public static final int MSG_PROVIDER_CHANGED = 515;
            public static final int MSG_PROVIDER_REMOVED = 514;
            public static final int MSG_ROUTE_ADDED = 257;
            public static final int MSG_ROUTE_CHANGED = 259;
            public static final int MSG_ROUTE_PRESENTATION_DISPLAY_CHANGED = 261;
            public static final int MSG_ROUTE_REMOVED = 258;
            public static final int MSG_ROUTE_SELECTED = 262;
            public static final int MSG_ROUTE_UNSELECTED = 263;
            public static final int MSG_ROUTE_VOLUME_CHANGED = 260;
            private static final int MSG_TYPE_MASK = 65280;
            private static final int MSG_TYPE_PROVIDER = 512;
            private static final int MSG_TYPE_ROUTE = 256;
            private final ArrayList<CallbackRecord> mTempCallbackRecords = new ArrayList();

            CallbackHandler() {
            }

            public void post(int i, Object obj) {
                obtainMessage(i, obj).sendToTarget();
            }

            public void post(int i, Object obj, int i2) {
                i = obtainMessage(i, obj);
                i.arg1 = i2;
                i.sendToTarget();
            }

            public void handleMessage(Message message) {
                int i = message.what;
                Object obj = message.obj;
                message = message.arg1;
                if (i == MSG_ROUTE_CHANGED && GlobalMediaRouter.this.getSelectedRoute().getId().equals(((RouteInfo) obj).getId())) {
                    GlobalMediaRouter.this.updateSelectedRouteIfNeeded(true);
                }
                syncWithSystemProvider(i, obj);
                try {
                    int size = GlobalMediaRouter.this.mRouters.size();
                    while (true) {
                        size--;
                        if (size < 0) {
                            break;
                        }
                        MediaRouter mediaRouter = (MediaRouter) ((WeakReference) GlobalMediaRouter.this.mRouters.get(size)).get();
                        if (mediaRouter == null) {
                            GlobalMediaRouter.this.mRouters.remove(size);
                        } else {
                            this.mTempCallbackRecords.addAll(mediaRouter.mCallbackRecords);
                        }
                    }
                    size = this.mTempCallbackRecords.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        invokeCallback((CallbackRecord) this.mTempCallbackRecords.get(i2), i, obj, message);
                    }
                } finally {
                    this.mTempCallbackRecords.clear();
                }
            }

            private void syncWithSystemProvider(int i, Object obj) {
                if (i != MSG_ROUTE_SELECTED) {
                    switch (i) {
                        case 257:
                            GlobalMediaRouter.this.mSystemProvider.onSyncRouteAdded((RouteInfo) obj);
                            return;
                        case MSG_ROUTE_REMOVED /*258*/:
                            GlobalMediaRouter.this.mSystemProvider.onSyncRouteRemoved((RouteInfo) obj);
                            return;
                        case MSG_ROUTE_CHANGED /*259*/:
                            GlobalMediaRouter.this.mSystemProvider.onSyncRouteChanged((RouteInfo) obj);
                            return;
                        default:
                            return;
                    }
                }
                GlobalMediaRouter.this.mSystemProvider.onSyncRouteSelected((RouteInfo) obj);
            }

            private void invokeCallback(CallbackRecord callbackRecord, int i, Object obj, int i2) {
                MediaRouter mediaRouter = callbackRecord.mRouter;
                Callback callback = callbackRecord.mCallback;
                int i3 = 65280 & i;
                if (i3 == 256) {
                    RouteInfo routeInfo = (RouteInfo) obj;
                    if (callbackRecord.filterRouteEvent(routeInfo) != null) {
                        switch (i) {
                            case 257:
                                callback.onRouteAdded(mediaRouter, routeInfo);
                                return;
                            case MSG_ROUTE_REMOVED /*258*/:
                                callback.onRouteRemoved(mediaRouter, routeInfo);
                                return;
                            case MSG_ROUTE_CHANGED /*259*/:
                                callback.onRouteChanged(mediaRouter, routeInfo);
                                return;
                            case MSG_ROUTE_VOLUME_CHANGED /*260*/:
                                callback.onRouteVolumeChanged(mediaRouter, routeInfo);
                                return;
                            case MSG_ROUTE_PRESENTATION_DISPLAY_CHANGED /*261*/:
                                callback.onRoutePresentationDisplayChanged(mediaRouter, routeInfo);
                                return;
                            case MSG_ROUTE_SELECTED /*262*/:
                                callback.onRouteSelected(mediaRouter, routeInfo);
                                return;
                            case MSG_ROUTE_UNSELECTED /*263*/:
                                callback.onRouteUnselected(mediaRouter, routeInfo, i2);
                                return;
                            default:
                                return;
                        }
                    }
                } else if (i3 == 512) {
                    ProviderInfo providerInfo = (ProviderInfo) obj;
                    switch (i) {
                        case 513:
                            callback.onProviderAdded(mediaRouter, providerInfo);
                            return;
                        case MSG_PROVIDER_REMOVED /*514*/:
                            callback.onProviderRemoved(mediaRouter, providerInfo);
                            return;
                        case MSG_PROVIDER_CHANGED /*515*/:
                            callback.onProviderChanged(mediaRouter, providerInfo);
                            return;
                        default:
                            return;
                    }
                }
            }
        }

        private final class MediaSessionRecord {
            private int mControlType;
            private int mMaxVolume;
            private final MediaSessionCompat mMsCompat;
            private VolumeProviderCompat mVpCompat;

            public MediaSessionRecord(Object obj) {
                this.mMsCompat = MediaSessionCompat.fromMediaSession(GlobalMediaRouter.this.mApplicationContext, obj);
            }

            public MediaSessionRecord(MediaSessionCompat mediaSessionCompat) {
                this.mMsCompat = mediaSessionCompat;
            }

            public void configureVolume(int i, int i2, int i3) {
                if (this.mVpCompat != null && i == this.mControlType && i2 == this.mMaxVolume) {
                    this.mVpCompat.setCurrentVolume(i3);
                    return;
                }
                this.mVpCompat = new VolumeProviderCompat(i, i2, i3) {
                    public void onSetVolumeTo(final int i) {
                        GlobalMediaRouter.this.mCallbackHandler.post(new Runnable() {
                            public void run() {
                                if (GlobalMediaRouter.this.mSelectedRoute != null) {
                                    GlobalMediaRouter.this.mSelectedRoute.requestSetVolume(i);
                                }
                            }
                        });
                    }

                    public void onAdjustVolume(final int i) {
                        GlobalMediaRouter.this.mCallbackHandler.post(new Runnable() {
                            public void run() {
                                if (GlobalMediaRouter.this.mSelectedRoute != null) {
                                    GlobalMediaRouter.this.mSelectedRoute.requestUpdateVolume(i);
                                }
                            }
                        });
                    }
                };
                this.mMsCompat.setPlaybackToRemote(this.mVpCompat);
            }

            public void clearVolumeHandling() {
                this.mMsCompat.setPlaybackToLocal(GlobalMediaRouter.this.mPlaybackInfo.playbackStream);
                this.mVpCompat = null;
            }

            public Token getToken() {
                return this.mMsCompat.getSessionToken();
            }
        }

        private final class ProviderCallback extends android.support.v7.media.MediaRouteProvider.Callback {
            ProviderCallback() {
            }

            public void onDescriptorChanged(MediaRouteProvider mediaRouteProvider, MediaRouteProviderDescriptor mediaRouteProviderDescriptor) {
                GlobalMediaRouter.this.updateProviderDescriptor(mediaRouteProvider, mediaRouteProviderDescriptor);
            }
        }

        private final class RemoteControlClientRecord implements VolumeCallback {
            private boolean mDisconnected;
            private final RemoteControlClientCompat mRccCompat;

            public RemoteControlClientRecord(Object obj) {
                this.mRccCompat = RemoteControlClientCompat.obtain(GlobalMediaRouter.this.mApplicationContext, obj);
                this.mRccCompat.setVolumeCallback(this);
                updatePlaybackInfo();
            }

            public Object getRemoteControlClient() {
                return this.mRccCompat.getRemoteControlClient();
            }

            public void disconnect() {
                this.mDisconnected = true;
                this.mRccCompat.setVolumeCallback(null);
            }

            public void updatePlaybackInfo() {
                this.mRccCompat.setPlaybackInfo(GlobalMediaRouter.this.mPlaybackInfo);
            }

            public void onVolumeSetRequest(int i) {
                if (!this.mDisconnected && GlobalMediaRouter.this.mSelectedRoute != null) {
                    GlobalMediaRouter.this.mSelectedRoute.requestSetVolume(i);
                }
            }

            public void onVolumeUpdateRequest(int i) {
                if (!this.mDisconnected && GlobalMediaRouter.this.mSelectedRoute != null) {
                    GlobalMediaRouter.this.mSelectedRoute.requestUpdateVolume(i);
                }
            }
        }

        GlobalMediaRouter(Context context) {
            this.mApplicationContext = context;
            this.mDisplayManager = DisplayManagerCompat.getInstance(context);
            this.mLowRam = ActivityManagerCompat.isLowRamDevice((ActivityManager) context.getSystemService("activity"));
            this.mSystemProvider = SystemMediaRouteProvider.obtain(context, this);
        }

        public void start() {
            addProvider(this.mSystemProvider);
            this.mRegisteredProviderWatcher = new RegisteredMediaRouteProviderWatcher(this.mApplicationContext, this);
            this.mRegisteredProviderWatcher.start();
        }

        public MediaRouter getRouter(Context context) {
            int size = this.mRouters.size();
            while (true) {
                size--;
                if (size >= 0) {
                    MediaRouter mediaRouter = (MediaRouter) ((WeakReference) this.mRouters.get(size)).get();
                    if (mediaRouter == null) {
                        this.mRouters.remove(size);
                    } else if (mediaRouter.mContext == context) {
                        return mediaRouter;
                    }
                } else {
                    MediaRouter mediaRouter2 = new MediaRouter(context);
                    this.mRouters.add(new WeakReference(mediaRouter2));
                    return mediaRouter2;
                }
            }
        }

        public ContentResolver getContentResolver() {
            return this.mApplicationContext.getContentResolver();
        }

        public android.content.Context getProviderContext(java.lang.String r3) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r2 = this;
            r0 = "android";
            r0 = r3.equals(r0);
            if (r0 == 0) goto L_0x000b;
        L_0x0008:
            r3 = r2.mApplicationContext;
            return r3;
        L_0x000b:
            r0 = r2.mApplicationContext;	 Catch:{ NameNotFoundException -> 0x0013 }
            r1 = 4;	 Catch:{ NameNotFoundException -> 0x0013 }
            r3 = r0.createPackageContext(r3, r1);	 Catch:{ NameNotFoundException -> 0x0013 }
            return r3;
        L_0x0013:
            r3 = 0;
            return r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.media.MediaRouter.GlobalMediaRouter.getProviderContext(java.lang.String):android.content.Context");
        }

        public Display getDisplay(int i) {
            return this.mDisplayManager.getDisplay(i);
        }

        public void sendControlRequest(RouteInfo routeInfo, Intent intent, ControlRequestCallback controlRequestCallback) {
            if ((routeInfo != this.mSelectedRoute || this.mSelectedRouteController == null || this.mSelectedRouteController.onControlRequest(intent, controlRequestCallback) == null) && controlRequestCallback != null) {
                controlRequestCallback.onError(null, null);
            }
        }

        public void requestSetVolume(RouteInfo routeInfo, int i) {
            if (routeInfo == this.mSelectedRoute && this.mSelectedRouteController != null) {
                this.mSelectedRouteController.onSetVolume(i);
            } else if (!this.mRouteControllerMap.isEmpty()) {
                RouteController routeController = (RouteController) this.mRouteControllerMap.get(routeInfo.mDescriptorId);
                if (routeController != null) {
                    routeController.onSetVolume(i);
                }
            }
        }

        public void requestUpdateVolume(RouteInfo routeInfo, int i) {
            if (routeInfo == this.mSelectedRoute && this.mSelectedRouteController != null) {
                this.mSelectedRouteController.onUpdateVolume(i);
            }
        }

        public RouteInfo getRoute(String str) {
            Iterator it = this.mRoutes.iterator();
            while (it.hasNext()) {
                RouteInfo routeInfo = (RouteInfo) it.next();
                if (routeInfo.mUniqueId.equals(str)) {
                    return routeInfo;
                }
            }
            return null;
        }

        public List<RouteInfo> getRoutes() {
            return this.mRoutes;
        }

        List<ProviderInfo> getProviders() {
            return this.mProviders;
        }

        @NonNull
        RouteInfo getDefaultRoute() {
            if (this.mDefaultRoute != null) {
                return this.mDefaultRoute;
            }
            throw new IllegalStateException("There is no default route.  The media router has not yet been fully initialized.");
        }

        RouteInfo getBluetoothRoute() {
            return this.mBluetoothRoute;
        }

        @NonNull
        RouteInfo getSelectedRoute() {
            if (this.mSelectedRoute != null) {
                return this.mSelectedRoute;
            }
            throw new IllegalStateException("There is no currently selected route.  The media router has not yet been fully initialized.");
        }

        void selectRoute(@NonNull RouteInfo routeInfo) {
            selectRoute(routeInfo, 3);
        }

        void selectRoute(@NonNull RouteInfo routeInfo, int i) {
            StringBuilder stringBuilder;
            if (!this.mRoutes.contains(routeInfo)) {
                i = MediaRouter.TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Ignoring attempt to select removed route: ");
                stringBuilder.append(routeInfo);
                Log.w(i, stringBuilder.toString());
            } else if (routeInfo.mEnabled) {
                setSelectedRouteInternal(routeInfo, i);
            } else {
                i = MediaRouter.TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Ignoring attempt to select disabled route: ");
                stringBuilder.append(routeInfo);
                Log.w(i, stringBuilder.toString());
            }
        }

        public boolean isRouteAvailable(MediaRouteSelector mediaRouteSelector, int i) {
            if (mediaRouteSelector.isEmpty()) {
                return false;
            }
            if ((i & 2) == 0 && this.mLowRam) {
                return true;
            }
            int size = this.mRoutes.size();
            for (int i2 = 0; i2 < size; i2++) {
                RouteInfo routeInfo = (RouteInfo) this.mRoutes.get(i2);
                if ((i & 1) == 0 || !routeInfo.isDefaultOrBluetooth()) {
                    if (routeInfo.matchesSelector(mediaRouteSelector)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public void updateDiscoveryRequest() {
            Builder builder = new Builder();
            int size = this.mRouters.size();
            Object obj = null;
            boolean z = false;
            while (true) {
                size--;
                if (size < 0) {
                    break;
                }
                MediaRouter mediaRouter = (MediaRouter) ((WeakReference) this.mRouters.get(size)).get();
                if (mediaRouter == null) {
                    this.mRouters.remove(size);
                } else {
                    int size2 = mediaRouter.mCallbackRecords.size();
                    boolean z2 = z;
                    Object obj2 = obj;
                    for (int i = 0; i < size2; i++) {
                        CallbackRecord callbackRecord = (CallbackRecord) mediaRouter.mCallbackRecords.get(i);
                        builder.addSelector(callbackRecord.mSelector);
                        if ((callbackRecord.mFlags & 1) != 0) {
                            obj2 = 1;
                            z2 = true;
                        }
                        if (!((callbackRecord.mFlags & 4) == 0 || this.mLowRam)) {
                            obj2 = 1;
                        }
                        if ((callbackRecord.mFlags & 8) != 0) {
                            obj2 = 1;
                        }
                    }
                    obj = obj2;
                    z = z2;
                }
            }
            MediaRouteSelector build = obj != null ? builder.build() : MediaRouteSelector.EMPTY;
            if (this.mDiscoveryRequest == null || !this.mDiscoveryRequest.getSelector().equals(build) || this.mDiscoveryRequest.isActiveScan() != z) {
                if (!build.isEmpty() || z) {
                    this.mDiscoveryRequest = new MediaRouteDiscoveryRequest(build, z);
                } else if (this.mDiscoveryRequest != null) {
                    this.mDiscoveryRequest = null;
                } else {
                    return;
                }
                if (MediaRouter.DEBUG) {
                    String str = MediaRouter.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Updated discovery request: ");
                    stringBuilder.append(this.mDiscoveryRequest);
                    Log.d(str, stringBuilder.toString());
                }
                if (!(obj == null || z || !this.mLowRam)) {
                    Log.i(MediaRouter.TAG, "Forcing passive route discovery on a low-RAM device, system performance may be affected.  Please consider using CALLBACK_FLAG_REQUEST_DISCOVERY instead of CALLBACK_FLAG_FORCE_DISCOVERY.");
                }
                int size3 = this.mProviders.size();
                for (int i2 = 0; i2 < size3; i2++) {
                    ((ProviderInfo) this.mProviders.get(i2)).mProviderInstance.setDiscoveryRequest(this.mDiscoveryRequest);
                }
            }
        }

        public void addProvider(MediaRouteProvider mediaRouteProvider) {
            if (findProviderInfo(mediaRouteProvider) < 0) {
                ProviderInfo providerInfo = new ProviderInfo(mediaRouteProvider);
                this.mProviders.add(providerInfo);
                if (MediaRouter.DEBUG) {
                    String str = MediaRouter.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Provider added: ");
                    stringBuilder.append(providerInfo);
                    Log.d(str, stringBuilder.toString());
                }
                this.mCallbackHandler.post(513, providerInfo);
                updateProviderContents(providerInfo, mediaRouteProvider.getDescriptor());
                mediaRouteProvider.setCallback(this.mProviderCallback);
                mediaRouteProvider.setDiscoveryRequest(this.mDiscoveryRequest);
            }
        }

        public void removeProvider(MediaRouteProvider mediaRouteProvider) {
            int findProviderInfo = findProviderInfo(mediaRouteProvider);
            if (findProviderInfo >= 0) {
                mediaRouteProvider.setCallback(null);
                mediaRouteProvider.setDiscoveryRequest(null);
                ProviderInfo providerInfo = (ProviderInfo) this.mProviders.get(findProviderInfo);
                updateProviderContents(providerInfo, null);
                if (MediaRouter.DEBUG) {
                    String str = MediaRouter.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Provider removed: ");
                    stringBuilder.append(providerInfo);
                    Log.d(str, stringBuilder.toString());
                }
                this.mCallbackHandler.post(CallbackHandler.MSG_PROVIDER_REMOVED, providerInfo);
                this.mProviders.remove(findProviderInfo);
            }
        }

        void updateProviderDescriptor(MediaRouteProvider mediaRouteProvider, MediaRouteProviderDescriptor mediaRouteProviderDescriptor) {
            mediaRouteProvider = findProviderInfo(mediaRouteProvider);
            if (mediaRouteProvider >= null) {
                updateProviderContents((ProviderInfo) this.mProviders.get(mediaRouteProvider), mediaRouteProviderDescriptor);
            }
        }

        private int findProviderInfo(MediaRouteProvider mediaRouteProvider) {
            int size = this.mProviders.size();
            for (int i = 0; i < size; i++) {
                if (((ProviderInfo) this.mProviders.get(i)).mProviderInstance == mediaRouteProvider) {
                    return i;
                }
            }
            return -1;
        }

        private void updateProviderContents(ProviderInfo providerInfo, MediaRouteProviderDescriptor mediaRouteProviderDescriptor) {
            GlobalMediaRouter globalMediaRouter = this;
            ProviderInfo providerInfo2 = providerInfo;
            MediaRouteProviderDescriptor mediaRouteProviderDescriptor2 = mediaRouteProviderDescriptor;
            if (providerInfo.updateDescriptor(mediaRouteProviderDescriptor)) {
                int i;
                RouteInfo routeGroup;
                RouteInfo routeInfo;
                boolean z;
                int size;
                String str;
                StringBuilder stringBuilder;
                String str2;
                StringBuilder stringBuilder2;
                if (mediaRouteProviderDescriptor2 != null) {
                    String str3;
                    if (mediaRouteProviderDescriptor.isValid()) {
                        List routes = mediaRouteProviderDescriptor.getRoutes();
                        int size2 = routes.size();
                        List<Pair> arrayList = new ArrayList();
                        List<Pair> arrayList2 = new ArrayList();
                        i = 0;
                        boolean z2 = false;
                        for (int i2 = 0; i2 < size2; i2++) {
                            MediaRouteDescriptor mediaRouteDescriptor = (MediaRouteDescriptor) routes.get(i2);
                            String id = mediaRouteDescriptor.getId();
                            int findRouteByDescriptorId = providerInfo2.findRouteByDescriptorId(id);
                            if (findRouteByDescriptorId < 0) {
                                String assignRouteUniqueId = assignRouteUniqueId(providerInfo2, id);
                                Object obj = mediaRouteDescriptor.getGroupMemberIds() != null ? 1 : null;
                                routeGroup = obj != null ? new RouteGroup(providerInfo2, id, assignRouteUniqueId) : new RouteInfo(providerInfo2, id, assignRouteUniqueId);
                                findRouteByDescriptorId = i + 1;
                                providerInfo.mRoutes.add(i, routeGroup);
                                globalMediaRouter.mRoutes.add(routeGroup);
                                if (obj != null) {
                                    arrayList.add(new Pair(routeGroup, mediaRouteDescriptor));
                                } else {
                                    routeGroup.maybeUpdateDescriptor(mediaRouteDescriptor);
                                    if (MediaRouter.DEBUG) {
                                        String str4 = MediaRouter.TAG;
                                        StringBuilder stringBuilder3 = new StringBuilder();
                                        stringBuilder3.append("Route added: ");
                                        stringBuilder3.append(routeGroup);
                                        Log.d(str4, stringBuilder3.toString());
                                    }
                                    globalMediaRouter.mCallbackHandler.post(257, routeGroup);
                                }
                                i = findRouteByDescriptorId;
                            } else if (findRouteByDescriptorId < i) {
                                str3 = MediaRouter.TAG;
                                StringBuilder stringBuilder4 = new StringBuilder();
                                stringBuilder4.append("Ignoring route descriptor with duplicate id: ");
                                stringBuilder4.append(mediaRouteDescriptor);
                                Log.w(str3, stringBuilder4.toString());
                            } else {
                                routeGroup = (RouteInfo) providerInfo.mRoutes.get(findRouteByDescriptorId);
                                int i3 = i + 1;
                                Collections.swap(providerInfo.mRoutes, findRouteByDescriptorId, i);
                                if (routeGroup instanceof RouteGroup) {
                                    arrayList2.add(new Pair(routeGroup, mediaRouteDescriptor));
                                } else if (updateRouteDescriptorAndNotify(routeGroup, mediaRouteDescriptor) != 0 && routeGroup == globalMediaRouter.mSelectedRoute) {
                                    i = i3;
                                    z2 = true;
                                }
                                i = i3;
                            }
                        }
                        for (Pair pair : arrayList) {
                            routeInfo = (RouteInfo) pair.first;
                            routeInfo.maybeUpdateDescriptor((MediaRouteDescriptor) pair.second);
                            if (MediaRouter.DEBUG) {
                                str3 = MediaRouter.TAG;
                                StringBuilder stringBuilder5 = new StringBuilder();
                                stringBuilder5.append("Route added: ");
                                stringBuilder5.append(routeInfo);
                                Log.d(str3, stringBuilder5.toString());
                            }
                            globalMediaRouter.mCallbackHandler.post(257, routeInfo);
                        }
                        z = z2;
                        for (Pair pair2 : arrayList2) {
                            RouteInfo routeInfo2 = (RouteInfo) pair2.first;
                            if (updateRouteDescriptorAndNotify(routeInfo2, (MediaRouteDescriptor) pair2.second) != 0 && routeInfo2 == globalMediaRouter.mSelectedRoute) {
                                z = true;
                            }
                        }
                        for (size = providerInfo.mRoutes.size() - 1; size >= i; size--) {
                            routeInfo = (RouteInfo) providerInfo.mRoutes.get(size);
                            routeInfo.maybeUpdateDescriptor(null);
                            globalMediaRouter.mRoutes.remove(routeInfo);
                        }
                        updateSelectedRouteIfNeeded(z);
                        for (size = providerInfo.mRoutes.size() - 1; size >= i; size--) {
                            routeGroup = (RouteInfo) providerInfo.mRoutes.remove(size);
                            if (MediaRouter.DEBUG) {
                                str = MediaRouter.TAG;
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("Route removed: ");
                                stringBuilder.append(routeGroup);
                                Log.d(str, stringBuilder.toString());
                            }
                            globalMediaRouter.mCallbackHandler.post(CallbackHandler.MSG_ROUTE_REMOVED, routeGroup);
                        }
                        if (MediaRouter.DEBUG) {
                            str2 = MediaRouter.TAG;
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("Provider changed: ");
                            stringBuilder2.append(providerInfo2);
                            Log.d(str2, stringBuilder2.toString());
                        }
                        globalMediaRouter.mCallbackHandler.post(CallbackHandler.MSG_PROVIDER_CHANGED, providerInfo2);
                    }
                    str3 = MediaRouter.TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Ignoring invalid provider descriptor: ");
                    stringBuilder.append(mediaRouteProviderDescriptor2);
                    Log.w(str3, stringBuilder.toString());
                }
                z = false;
                i = 0;
                for (size = providerInfo.mRoutes.size() - 1; size >= i; size--) {
                    routeInfo = (RouteInfo) providerInfo.mRoutes.get(size);
                    routeInfo.maybeUpdateDescriptor(null);
                    globalMediaRouter.mRoutes.remove(routeInfo);
                }
                updateSelectedRouteIfNeeded(z);
                for (size = providerInfo.mRoutes.size() - 1; size >= i; size--) {
                    routeGroup = (RouteInfo) providerInfo.mRoutes.remove(size);
                    if (MediaRouter.DEBUG) {
                        str = MediaRouter.TAG;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Route removed: ");
                        stringBuilder.append(routeGroup);
                        Log.d(str, stringBuilder.toString());
                    }
                    globalMediaRouter.mCallbackHandler.post(CallbackHandler.MSG_ROUTE_REMOVED, routeGroup);
                }
                if (MediaRouter.DEBUG) {
                    str2 = MediaRouter.TAG;
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Provider changed: ");
                    stringBuilder2.append(providerInfo2);
                    Log.d(str2, stringBuilder2.toString());
                }
                globalMediaRouter.mCallbackHandler.post(CallbackHandler.MSG_PROVIDER_CHANGED, providerInfo2);
            }
        }

        private int updateRouteDescriptorAndNotify(RouteInfo routeInfo, MediaRouteDescriptor mediaRouteDescriptor) {
            mediaRouteDescriptor = routeInfo.maybeUpdateDescriptor(mediaRouteDescriptor);
            if (mediaRouteDescriptor != null) {
                String str;
                StringBuilder stringBuilder;
                if ((mediaRouteDescriptor & 1) != 0) {
                    if (MediaRouter.DEBUG) {
                        str = MediaRouter.TAG;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Route changed: ");
                        stringBuilder.append(routeInfo);
                        Log.d(str, stringBuilder.toString());
                    }
                    this.mCallbackHandler.post(CallbackHandler.MSG_ROUTE_CHANGED, routeInfo);
                }
                if ((mediaRouteDescriptor & 2) != 0) {
                    if (MediaRouter.DEBUG) {
                        str = MediaRouter.TAG;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Route volume changed: ");
                        stringBuilder.append(routeInfo);
                        Log.d(str, stringBuilder.toString());
                    }
                    this.mCallbackHandler.post(CallbackHandler.MSG_ROUTE_VOLUME_CHANGED, routeInfo);
                }
                if ((mediaRouteDescriptor & 4) != 0) {
                    if (MediaRouter.DEBUG) {
                        str = MediaRouter.TAG;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Route presentation display changed: ");
                        stringBuilder.append(routeInfo);
                        Log.d(str, stringBuilder.toString());
                    }
                    this.mCallbackHandler.post(CallbackHandler.MSG_ROUTE_PRESENTATION_DISPLAY_CHANGED, routeInfo);
                }
            }
            return mediaRouteDescriptor;
        }

        private String assignRouteUniqueId(ProviderInfo providerInfo, String str) {
            providerInfo = providerInfo.getComponentName().flattenToShortString();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(providerInfo);
            stringBuilder.append(":");
            stringBuilder.append(str);
            String stringBuilder2 = stringBuilder.toString();
            if (findRouteByUniqueId(stringBuilder2) < 0) {
                this.mUniqueIdMap.put(new Pair(providerInfo, str), stringBuilder2);
                return stringBuilder2;
            }
            String str2 = MediaRouter.TAG;
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Either ");
            stringBuilder3.append(str);
            stringBuilder3.append(" isn't unique in ");
            stringBuilder3.append(providerInfo);
            stringBuilder3.append(" or we're trying to assign a unique ID for an already added route");
            Log.w(str2, stringBuilder3.toString());
            int i = 2;
            while (true) {
                String format = String.format(Locale.US, "%s_%d", new Object[]{stringBuilder2, Integer.valueOf(i)});
                if (findRouteByUniqueId(format) < 0) {
                    this.mUniqueIdMap.put(new Pair(providerInfo, str), format);
                    return format;
                }
                i++;
            }
        }

        private int findRouteByUniqueId(String str) {
            int size = this.mRoutes.size();
            for (int i = 0; i < size; i++) {
                if (((RouteInfo) this.mRoutes.get(i)).mUniqueId.equals(str)) {
                    return i;
                }
            }
            return -1;
        }

        private String getUniqueId(ProviderInfo providerInfo, String str) {
            return (String) this.mUniqueIdMap.get(new Pair(providerInfo.getComponentName().flattenToShortString(), str));
        }

        private void updateSelectedRouteIfNeeded(boolean r5) {
            /* JADX: method processing error */
/*
Error: java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:410)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.fixIterableType(LoopRegionVisitor.java:308)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.checkIterableForEach(LoopRegionVisitor.java:249)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.processLoopRegion(LoopRegionVisitor.java:68)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.enterRegion(LoopRegionVisitor.java:52)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:56)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:18)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.visit(LoopRegionVisitor.java:46)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r4 = this;
            r0 = r4.mDefaultRoute;
            r1 = 0;
            if (r0 == 0) goto L_0x0027;
        L_0x0005:
            r0 = r4.mDefaultRoute;
            r0 = r0.isSelectable();
            if (r0 != 0) goto L_0x0027;
        L_0x000d:
            r0 = "MediaRouter";
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "Clearing the default route because it is no longer selectable: ";
            r2.append(r3);
            r3 = r4.mDefaultRoute;
            r2.append(r3);
            r2 = r2.toString();
            android.util.Log.i(r0, r2);
            r4.mDefaultRoute = r1;
        L_0x0027:
            r0 = r4.mDefaultRoute;
            if (r0 != 0) goto L_0x006b;
        L_0x002b:
            r0 = r4.mRoutes;
            r0 = r0.isEmpty();
            if (r0 != 0) goto L_0x006b;
        L_0x0033:
            r0 = r4.mRoutes;
            r0 = r0.iterator();
        L_0x0039:
            r2 = r0.hasNext();
            if (r2 == 0) goto L_0x006b;
        L_0x003f:
            r2 = r0.next();
            r2 = (android.support.v7.media.MediaRouter.RouteInfo) r2;
            r3 = r4.isSystemDefaultRoute(r2);
            if (r3 == 0) goto L_0x0039;
        L_0x004b:
            r3 = r2.isSelectable();
            if (r3 == 0) goto L_0x0039;
        L_0x0051:
            r4.mDefaultRoute = r2;
            r0 = "MediaRouter";
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "Found default route: ";
            r2.append(r3);
            r3 = r4.mDefaultRoute;
            r2.append(r3);
            r2 = r2.toString();
            android.util.Log.i(r0, r2);
        L_0x006b:
            r0 = r4.mBluetoothRoute;
            if (r0 == 0) goto L_0x0091;
        L_0x006f:
            r0 = r4.mBluetoothRoute;
            r0 = r0.isSelectable();
            if (r0 != 0) goto L_0x0091;
        L_0x0077:
            r0 = "MediaRouter";
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "Clearing the bluetooth route because it is no longer selectable: ";
            r2.append(r3);
            r3 = r4.mBluetoothRoute;
            r2.append(r3);
            r2 = r2.toString();
            android.util.Log.i(r0, r2);
            r4.mBluetoothRoute = r1;
        L_0x0091:
            r0 = r4.mBluetoothRoute;
            if (r0 != 0) goto L_0x00d5;
        L_0x0095:
            r0 = r4.mRoutes;
            r0 = r0.isEmpty();
            if (r0 != 0) goto L_0x00d5;
        L_0x009d:
            r0 = r4.mRoutes;
            r0 = r0.iterator();
        L_0x00a3:
            r1 = r0.hasNext();
            if (r1 == 0) goto L_0x00d5;
        L_0x00a9:
            r1 = r0.next();
            r1 = (android.support.v7.media.MediaRouter.RouteInfo) r1;
            r2 = r4.isSystemLiveAudioOnlyRoute(r1);
            if (r2 == 0) goto L_0x00a3;
        L_0x00b5:
            r2 = r1.isSelectable();
            if (r2 == 0) goto L_0x00a3;
        L_0x00bb:
            r4.mBluetoothRoute = r1;
            r0 = "MediaRouter";
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "Found bluetooth route: ";
            r1.append(r2);
            r2 = r4.mBluetoothRoute;
            r1.append(r2);
            r1 = r1.toString();
            android.util.Log.i(r0, r1);
        L_0x00d5:
            r0 = r4.mSelectedRoute;
            if (r0 == 0) goto L_0x017f;
        L_0x00d9:
            r0 = r4.mSelectedRoute;
            r0 = r0.isSelectable();
            if (r0 != 0) goto L_0x00e3;
        L_0x00e1:
            goto L_0x017f;
        L_0x00e3:
            if (r5 == 0) goto L_0x019f;
        L_0x00e5:
            r5 = r4.mSelectedRoute;
            r5 = r5 instanceof android.support.v7.media.MediaRouter.RouteGroup;
            if (r5 == 0) goto L_0x017b;
        L_0x00eb:
            r5 = r4.mSelectedRoute;
            r5 = (android.support.v7.media.MediaRouter.RouteGroup) r5;
            r5 = r5.getRoutes();
            r0 = new java.util.HashSet;
            r0.<init>();
            r1 = r5.iterator();
        L_0x00fc:
            r2 = r1.hasNext();
            if (r2 == 0) goto L_0x0110;
        L_0x0102:
            r2 = r1.next();
            r2 = (android.support.v7.media.MediaRouter.RouteInfo) r2;
            r2 = r2.mDescriptorId;
            r0.add(r2);
            goto L_0x00fc;
        L_0x0110:
            r1 = r4.mRouteControllerMap;
            r1 = r1.entrySet();
            r1 = r1.iterator();
        L_0x011a:
            r2 = r1.hasNext();
            if (r2 == 0) goto L_0x0140;
        L_0x0120:
            r2 = r1.next();
            r2 = (java.util.Map.Entry) r2;
            r3 = r2.getKey();
            r3 = r0.contains(r3);
            if (r3 != 0) goto L_0x011a;
        L_0x0130:
            r2 = r2.getValue();
            r2 = (android.support.v7.media.MediaRouteProvider.RouteController) r2;
            r2.onUnselect();
            r2.onRelease();
            r1.remove();
            goto L_0x011a;
        L_0x0140:
            r5 = r5.iterator();
        L_0x0144:
            r0 = r5.hasNext();
            if (r0 == 0) goto L_0x017b;
        L_0x014a:
            r0 = r5.next();
            r0 = (android.support.v7.media.MediaRouter.RouteInfo) r0;
            r1 = r4.mRouteControllerMap;
            r2 = r0.mDescriptorId;
            r1 = r1.containsKey(r2);
            if (r1 != 0) goto L_0x0144;
        L_0x015c:
            r1 = r0.getProviderInstance();
            r2 = r0.mDescriptorId;
            r3 = r4.mSelectedRoute;
            r3 = r3.mDescriptorId;
            r1 = r1.onCreateRouteController(r2, r3);
            r1.onSelect();
            r2 = r4.mRouteControllerMap;
            r0 = r0.mDescriptorId;
            r2.put(r0, r1);
            goto L_0x0144;
        L_0x017b:
            r4.updatePlaybackInfoFromSelectedRoute();
            goto L_0x019f;
        L_0x017f:
            r5 = "MediaRouter";
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r1 = "Unselecting the current route because it is no longer selectable: ";
            r0.append(r1);
            r1 = r4.mSelectedRoute;
            r0.append(r1);
            r0 = r0.toString();
            android.util.Log.i(r5, r0);
            r5 = r4.chooseFallbackRoute();
            r0 = 0;
            r4.setSelectedRouteInternal(r5, r0);
        L_0x019f:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.media.MediaRouter.GlobalMediaRouter.updateSelectedRouteIfNeeded(boolean):void");
        }

        RouteInfo chooseFallbackRoute() {
            Iterator it = this.mRoutes.iterator();
            while (it.hasNext()) {
                RouteInfo routeInfo = (RouteInfo) it.next();
                if (routeInfo != this.mDefaultRoute && isSystemLiveAudioOnlyRoute(routeInfo) && routeInfo.isSelectable()) {
                    return routeInfo;
                }
            }
            return this.mDefaultRoute;
        }

        private boolean isSystemLiveAudioOnlyRoute(RouteInfo routeInfo) {
            return (routeInfo.getProviderInstance() == this.mSystemProvider && routeInfo.supportsControlCategory(MediaControlIntent.CATEGORY_LIVE_AUDIO) && routeInfo.supportsControlCategory(MediaControlIntent.CATEGORY_LIVE_VIDEO) == null) ? true : null;
        }

        private boolean isSystemDefaultRoute(RouteInfo routeInfo) {
            return (routeInfo.getProviderInstance() != this.mSystemProvider || routeInfo.mDescriptorId.equals(SystemMediaRouteProvider.DEFAULT_ROUTE_ID) == null) ? null : true;
        }

        private void setSelectedRouteInternal(@NonNull RouteInfo routeInfo, int i) {
            StringBuilder stringBuilder;
            String str;
            if (MediaRouter.sGlobal == null || (this.mBluetoothRoute != null && routeInfo.isDefault())) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                stringBuilder = new StringBuilder();
                for (int i2 = 3; i2 < stackTrace.length; i2++) {
                    StackTraceElement stackTraceElement = stackTrace[i2];
                    stringBuilder.append(stackTraceElement.getClassName());
                    stringBuilder.append(".");
                    stringBuilder.append(stackTraceElement.getMethodName());
                    stringBuilder.append(":");
                    stringBuilder.append(stackTraceElement.getLineNumber());
                    stringBuilder.append("  ");
                }
                StringBuilder stringBuilder2;
                if (MediaRouter.sGlobal == null) {
                    str = MediaRouter.TAG;
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("setSelectedRouteInternal is called while sGlobal is null: pkgName=");
                    stringBuilder2.append(this.mApplicationContext.getPackageName());
                    stringBuilder2.append(", callers=");
                    stringBuilder2.append(stringBuilder.toString());
                    Log.w(str, stringBuilder2.toString());
                } else {
                    str = MediaRouter.TAG;
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Default route is selected while a BT route is available: pkgName=");
                    stringBuilder2.append(this.mApplicationContext.getPackageName());
                    stringBuilder2.append(", callers=");
                    stringBuilder2.append(stringBuilder.toString());
                    Log.w(str, stringBuilder2.toString());
                }
            }
            if (this.mSelectedRoute != routeInfo) {
                if (this.mSelectedRoute != null) {
                    if (MediaRouter.DEBUG) {
                        str = MediaRouter.TAG;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Route unselected: ");
                        stringBuilder.append(this.mSelectedRoute);
                        stringBuilder.append(" reason: ");
                        stringBuilder.append(i);
                        Log.d(str, stringBuilder.toString());
                    }
                    this.mCallbackHandler.post(CallbackHandler.MSG_ROUTE_UNSELECTED, this.mSelectedRoute, i);
                    if (this.mSelectedRouteController != null) {
                        this.mSelectedRouteController.onUnselect(i);
                        this.mSelectedRouteController.onRelease();
                        this.mSelectedRouteController = null;
                    }
                    if (!this.mRouteControllerMap.isEmpty()) {
                        for (RouteController routeController : this.mRouteControllerMap.values()) {
                            routeController.onUnselect(i);
                            routeController.onRelease();
                        }
                        this.mRouteControllerMap.clear();
                    }
                }
                this.mSelectedRoute = routeInfo;
                this.mSelectedRouteController = routeInfo.getProviderInstance().onCreateRouteController(routeInfo.mDescriptorId);
                if (this.mSelectedRouteController != null) {
                    this.mSelectedRouteController.onSelect();
                }
                if (MediaRouter.DEBUG != null) {
                    routeInfo = MediaRouter.TAG;
                    i = new StringBuilder();
                    i.append("Route selected: ");
                    i.append(this.mSelectedRoute);
                    Log.d(routeInfo, i.toString());
                }
                this.mCallbackHandler.post(CallbackHandler.MSG_ROUTE_SELECTED, this.mSelectedRoute);
                if ((this.mSelectedRoute instanceof RouteGroup) != null) {
                    RouteInfo<RouteInfo> routes = ((RouteGroup) this.mSelectedRoute).getRoutes();
                    this.mRouteControllerMap.clear();
                    for (RouteInfo routeInfo2 : routes) {
                        RouteController onCreateRouteController = routeInfo2.getProviderInstance().onCreateRouteController(routeInfo2.mDescriptorId, this.mSelectedRoute.mDescriptorId);
                        onCreateRouteController.onSelect();
                        this.mRouteControllerMap.put(routeInfo2.mDescriptorId, onCreateRouteController);
                    }
                }
                updatePlaybackInfoFromSelectedRoute();
            }
        }

        public void onSystemRouteSelectedByDescriptorId(String str) {
            this.mCallbackHandler.removeMessages(CallbackHandler.MSG_ROUTE_SELECTED);
            int findProviderInfo = findProviderInfo(this.mSystemProvider);
            if (findProviderInfo >= 0) {
                ProviderInfo providerInfo = (ProviderInfo) this.mProviders.get(findProviderInfo);
                str = providerInfo.findRouteByDescriptorId(str);
                if (str >= null) {
                    ((RouteInfo) providerInfo.mRoutes.get(str)).select();
                }
            }
        }

        public void addRemoteControlClient(Object obj) {
            if (findRemoteControlClientRecord(obj) < 0) {
                this.mRemoteControlClients.add(new RemoteControlClientRecord(obj));
            }
        }

        public void removeRemoteControlClient(Object obj) {
            obj = findRemoteControlClientRecord(obj);
            if (obj >= null) {
                ((RemoteControlClientRecord) this.mRemoteControlClients.remove(obj)).disconnect();
            }
        }

        public void setMediaSession(Object obj) {
            setMediaSessionRecord(obj != null ? new MediaSessionRecord(obj) : null);
        }

        public void setMediaSessionCompat(MediaSessionCompat mediaSessionCompat) {
            this.mCompatSession = mediaSessionCompat;
            if (VERSION.SDK_INT >= 21) {
                setMediaSessionRecord(mediaSessionCompat != null ? new MediaSessionRecord(mediaSessionCompat) : null);
            } else if (VERSION.SDK_INT >= 14) {
                if (this.mRccMediaSession != null) {
                    removeRemoteControlClient(this.mRccMediaSession.getRemoteControlClient());
                    this.mRccMediaSession.removeOnActiveChangeListener(this.mSessionActiveListener);
                }
                this.mRccMediaSession = mediaSessionCompat;
                if (mediaSessionCompat != null) {
                    mediaSessionCompat.addOnActiveChangeListener(this.mSessionActiveListener);
                    if (mediaSessionCompat.isActive()) {
                        addRemoteControlClient(mediaSessionCompat.getRemoteControlClient());
                    }
                }
            }
        }

        private void setMediaSessionRecord(MediaSessionRecord mediaSessionRecord) {
            if (this.mMediaSession != null) {
                this.mMediaSession.clearVolumeHandling();
            }
            this.mMediaSession = mediaSessionRecord;
            if (mediaSessionRecord != null) {
                updatePlaybackInfoFromSelectedRoute();
            }
        }

        public Token getMediaSessionToken() {
            if (this.mMediaSession != null) {
                return this.mMediaSession.getToken();
            }
            return this.mCompatSession != null ? this.mCompatSession.getSessionToken() : null;
        }

        private int findRemoteControlClientRecord(Object obj) {
            int size = this.mRemoteControlClients.size();
            for (int i = 0; i < size; i++) {
                if (((RemoteControlClientRecord) this.mRemoteControlClients.get(i)).getRemoteControlClient() == obj) {
                    return i;
                }
            }
            return -1;
        }

        private void updatePlaybackInfoFromSelectedRoute() {
            if (this.mSelectedRoute != null) {
                this.mPlaybackInfo.volume = this.mSelectedRoute.getVolume();
                this.mPlaybackInfo.volumeMax = this.mSelectedRoute.getVolumeMax();
                this.mPlaybackInfo.volumeHandling = this.mSelectedRoute.getVolumeHandling();
                this.mPlaybackInfo.playbackStream = this.mSelectedRoute.getPlaybackStream();
                this.mPlaybackInfo.playbackType = this.mSelectedRoute.getPlaybackType();
                int size = this.mRemoteControlClients.size();
                int i = 0;
                for (int i2 = 0; i2 < size; i2++) {
                    ((RemoteControlClientRecord) this.mRemoteControlClients.get(i2)).updatePlaybackInfo();
                }
                if (this.mMediaSession != null) {
                    if (this.mSelectedRoute != getDefaultRoute()) {
                        if (this.mSelectedRoute != getBluetoothRoute()) {
                            if (this.mPlaybackInfo.volumeHandling == 1) {
                                i = 2;
                            }
                            this.mMediaSession.configureVolume(i, this.mPlaybackInfo.volumeMax, this.mPlaybackInfo.volume);
                            return;
                        }
                    }
                    this.mMediaSession.clearVolumeHandling();
                }
            } else if (this.mMediaSession != null) {
                this.mMediaSession.clearVolumeHandling();
            }
        }
    }

    public static final class ProviderInfo {
        private MediaRouteProviderDescriptor mDescriptor;
        private final ProviderMetadata mMetadata;
        private final MediaRouteProvider mProviderInstance;
        private Resources mResources;
        private boolean mResourcesNotAvailable;
        private final List<RouteInfo> mRoutes = new ArrayList();

        ProviderInfo(MediaRouteProvider mediaRouteProvider) {
            this.mProviderInstance = mediaRouteProvider;
            this.mMetadata = mediaRouteProvider.getMetadata();
        }

        public MediaRouteProvider getProviderInstance() {
            MediaRouter.checkCallingThread();
            return this.mProviderInstance;
        }

        public String getPackageName() {
            return this.mMetadata.getPackageName();
        }

        public ComponentName getComponentName() {
            return this.mMetadata.getComponentName();
        }

        public List<RouteInfo> getRoutes() {
            MediaRouter.checkCallingThread();
            return this.mRoutes;
        }

        Resources getResources() {
            if (this.mResources == null && !this.mResourcesNotAvailable) {
                String packageName = getPackageName();
                Context providerContext = MediaRouter.sGlobal.getProviderContext(packageName);
                if (providerContext != null) {
                    this.mResources = providerContext.getResources();
                } else {
                    String str = MediaRouter.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to obtain resources for route provider package: ");
                    stringBuilder.append(packageName);
                    Log.w(str, stringBuilder.toString());
                    this.mResourcesNotAvailable = true;
                }
            }
            return this.mResources;
        }

        boolean updateDescriptor(MediaRouteProviderDescriptor mediaRouteProviderDescriptor) {
            if (this.mDescriptor == mediaRouteProviderDescriptor) {
                return null;
            }
            this.mDescriptor = mediaRouteProviderDescriptor;
            return true;
        }

        int findRouteByDescriptorId(String str) {
            int size = this.mRoutes.size();
            for (int i = 0; i < size; i++) {
                if (((RouteInfo) this.mRoutes.get(i)).mDescriptorId.equals(str)) {
                    return i;
                }
            }
            return -1;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MediaRouter.RouteProviderInfo{ packageName=");
            stringBuilder.append(getPackageName());
            stringBuilder.append(" }");
            return stringBuilder.toString();
        }
    }

    public static class RouteInfo {
        static final int CHANGE_GENERAL = 1;
        static final int CHANGE_PRESENTATION_DISPLAY = 4;
        static final int CHANGE_VOLUME = 2;
        public static final int CONNECTION_STATE_CONNECTED = 2;
        public static final int CONNECTION_STATE_CONNECTING = 1;
        public static final int CONNECTION_STATE_DISCONNECTED = 0;
        @RestrictTo({Scope.LIBRARY_GROUP})
        public static final int DEVICE_TYPE_BLUETOOTH = 3;
        public static final int DEVICE_TYPE_SPEAKER = 2;
        public static final int DEVICE_TYPE_TV = 1;
        @RestrictTo({Scope.LIBRARY_GROUP})
        public static final int DEVICE_TYPE_UNKNOWN = 0;
        public static final int PLAYBACK_TYPE_LOCAL = 0;
        public static final int PLAYBACK_TYPE_REMOTE = 1;
        public static final int PLAYBACK_VOLUME_FIXED = 0;
        public static final int PLAYBACK_VOLUME_VARIABLE = 1;
        @RestrictTo({Scope.LIBRARY_GROUP})
        public static final int PRESENTATION_DISPLAY_ID_NONE = -1;
        static final String SYSTEM_MEDIA_ROUTE_PROVIDER_PACKAGE_NAME = "android";
        private boolean mCanDisconnect;
        private boolean mConnecting;
        private int mConnectionState;
        private final ArrayList<IntentFilter> mControlFilters = new ArrayList();
        private String mDescription;
        MediaRouteDescriptor mDescriptor;
        private final String mDescriptorId;
        private int mDeviceType;
        private boolean mEnabled;
        private Bundle mExtras;
        private Uri mIconUri;
        private String mName;
        private int mPlaybackStream;
        private int mPlaybackType;
        private Display mPresentationDisplay;
        private int mPresentationDisplayId = -1;
        private final ProviderInfo mProvider;
        private IntentSender mSettingsIntent;
        private final String mUniqueId;
        private int mVolume;
        private int mVolumeHandling;
        private int mVolumeMax;

        @Retention(RetentionPolicy.SOURCE)
        private @interface ConnectionState {
        }

        @Retention(RetentionPolicy.SOURCE)
        private @interface DeviceType {
        }

        @Retention(RetentionPolicy.SOURCE)
        private @interface PlaybackType {
        }

        @Retention(RetentionPolicy.SOURCE)
        private @interface PlaybackVolume {
        }

        RouteInfo(ProviderInfo providerInfo, String str, String str2) {
            this.mProvider = providerInfo;
            this.mDescriptorId = str;
            this.mUniqueId = str2;
        }

        public ProviderInfo getProvider() {
            return this.mProvider;
        }

        @NonNull
        public String getId() {
            return this.mUniqueId;
        }

        public String getName() {
            return this.mName;
        }

        @Nullable
        public String getDescription() {
            return this.mDescription;
        }

        public Uri getIconUri() {
            return this.mIconUri;
        }

        public boolean isEnabled() {
            return this.mEnabled;
        }

        public boolean isConnecting() {
            return this.mConnecting;
        }

        public int getConnectionState() {
            return this.mConnectionState;
        }

        public boolean isSelected() {
            MediaRouter.checkCallingThread();
            return MediaRouter.sGlobal.getSelectedRoute() == this;
        }

        public boolean isDefault() {
            MediaRouter.checkCallingThread();
            return MediaRouter.sGlobal.getDefaultRoute() == this;
        }

        public boolean isBluetooth() {
            MediaRouter.checkCallingThread();
            return MediaRouter.sGlobal.getBluetoothRoute() == this;
        }

        public boolean isDeviceSpeaker() {
            return isDefault() && Resources.getSystem().getText(Resources.getSystem().getIdentifier("default_audio_route_name", SerializerHandler.TYPE_STRING, "android")).equals(this.mName);
        }

        public List<IntentFilter> getControlFilters() {
            return this.mControlFilters;
        }

        public boolean matchesSelector(@NonNull MediaRouteSelector mediaRouteSelector) {
            if (mediaRouteSelector == null) {
                throw new IllegalArgumentException("selector must not be null");
            }
            MediaRouter.checkCallingThread();
            return mediaRouteSelector.matchesControlFilters(this.mControlFilters);
        }

        public boolean supportsControlCategory(@NonNull String str) {
            if (str == null) {
                throw new IllegalArgumentException("category must not be null");
            }
            MediaRouter.checkCallingThread();
            int size = this.mControlFilters.size();
            for (int i = 0; i < size; i++) {
                if (((IntentFilter) this.mControlFilters.get(i)).hasCategory(str)) {
                    return true;
                }
            }
            return false;
        }

        public boolean supportsControlAction(@NonNull String str, @NonNull String str2) {
            if (str == null) {
                throw new IllegalArgumentException("category must not be null");
            } else if (str2 == null) {
                throw new IllegalArgumentException("action must not be null");
            } else {
                MediaRouter.checkCallingThread();
                int size = this.mControlFilters.size();
                for (int i = 0; i < size; i++) {
                    IntentFilter intentFilter = (IntentFilter) this.mControlFilters.get(i);
                    if (intentFilter.hasCategory(str) && intentFilter.hasAction(str2)) {
                        return true;
                    }
                }
                return false;
            }
        }

        public boolean supportsControlRequest(@NonNull Intent intent) {
            if (intent == null) {
                throw new IllegalArgumentException("intent must not be null");
            }
            MediaRouter.checkCallingThread();
            ContentResolver contentResolver = MediaRouter.sGlobal.getContentResolver();
            int size = this.mControlFilters.size();
            for (int i = 0; i < size; i++) {
                if (((IntentFilter) this.mControlFilters.get(i)).match(contentResolver, intent, true, MediaRouter.TAG) >= 0) {
                    return true;
                }
            }
            return false;
        }

        public void sendControlRequest(@NonNull Intent intent, @Nullable ControlRequestCallback controlRequestCallback) {
            if (intent == null) {
                throw new IllegalArgumentException("intent must not be null");
            }
            MediaRouter.checkCallingThread();
            MediaRouter.sGlobal.sendControlRequest(this, intent, controlRequestCallback);
        }

        public int getPlaybackType() {
            return this.mPlaybackType;
        }

        public int getPlaybackStream() {
            return this.mPlaybackStream;
        }

        public int getDeviceType() {
            return this.mDeviceType;
        }

        @RestrictTo({Scope.LIBRARY_GROUP})
        public boolean isDefaultOrBluetooth() {
            boolean z = true;
            if (!isDefault()) {
                if (this.mDeviceType != 3) {
                    if (!isSystemMediaRouteProvider(this) || !supportsControlCategory(MediaControlIntent.CATEGORY_LIVE_AUDIO) || supportsControlCategory(MediaControlIntent.CATEGORY_LIVE_VIDEO)) {
                        z = false;
                    }
                    return z;
                }
            }
            return true;
        }

        boolean isSelectable() {
            return this.mDescriptor != null && this.mEnabled;
        }

        private static boolean isSystemMediaRouteProvider(RouteInfo routeInfo) {
            return TextUtils.equals(routeInfo.getProviderInstance().getMetadata().getPackageName(), "android");
        }

        public int getVolumeHandling() {
            return this.mVolumeHandling;
        }

        public int getVolume() {
            return this.mVolume;
        }

        public int getVolumeMax() {
            return this.mVolumeMax;
        }

        public boolean canDisconnect() {
            return this.mCanDisconnect;
        }

        public void requestSetVolume(int i) {
            MediaRouter.checkCallingThread();
            MediaRouter.sGlobal.requestSetVolume(this, Math.min(this.mVolumeMax, Math.max(0, i)));
        }

        public void requestUpdateVolume(int i) {
            MediaRouter.checkCallingThread();
            if (i != 0) {
                MediaRouter.sGlobal.requestUpdateVolume(this, i);
            }
        }

        @Nullable
        public Display getPresentationDisplay() {
            MediaRouter.checkCallingThread();
            if (this.mPresentationDisplayId >= 0 && this.mPresentationDisplay == null) {
                this.mPresentationDisplay = MediaRouter.sGlobal.getDisplay(this.mPresentationDisplayId);
            }
            return this.mPresentationDisplay;
        }

        @RestrictTo({Scope.LIBRARY_GROUP})
        public int getPresentationDisplayId() {
            return this.mPresentationDisplayId;
        }

        @Nullable
        public Bundle getExtras() {
            return this.mExtras;
        }

        @Nullable
        public IntentSender getSettingsIntent() {
            return this.mSettingsIntent;
        }

        public void select() {
            MediaRouter.checkCallingThread();
            MediaRouter.sGlobal.selectRoute(this);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MediaRouter.RouteInfo{ uniqueId=");
            stringBuilder.append(this.mUniqueId);
            stringBuilder.append(", name=");
            stringBuilder.append(this.mName);
            stringBuilder.append(", description=");
            stringBuilder.append(this.mDescription);
            stringBuilder.append(", iconUri=");
            stringBuilder.append(this.mIconUri);
            stringBuilder.append(", enabled=");
            stringBuilder.append(this.mEnabled);
            stringBuilder.append(", connecting=");
            stringBuilder.append(this.mConnecting);
            stringBuilder.append(", connectionState=");
            stringBuilder.append(this.mConnectionState);
            stringBuilder.append(", canDisconnect=");
            stringBuilder.append(this.mCanDisconnect);
            stringBuilder.append(", playbackType=");
            stringBuilder.append(this.mPlaybackType);
            stringBuilder.append(", playbackStream=");
            stringBuilder.append(this.mPlaybackStream);
            stringBuilder.append(", deviceType=");
            stringBuilder.append(this.mDeviceType);
            stringBuilder.append(", volumeHandling=");
            stringBuilder.append(this.mVolumeHandling);
            stringBuilder.append(", volume=");
            stringBuilder.append(this.mVolume);
            stringBuilder.append(", volumeMax=");
            stringBuilder.append(this.mVolumeMax);
            stringBuilder.append(", presentationDisplayId=");
            stringBuilder.append(this.mPresentationDisplayId);
            stringBuilder.append(", extras=");
            stringBuilder.append(this.mExtras);
            stringBuilder.append(", settingsIntent=");
            stringBuilder.append(this.mSettingsIntent);
            stringBuilder.append(", providerPackageName=");
            stringBuilder.append(this.mProvider.getPackageName());
            stringBuilder.append(" }");
            return stringBuilder.toString();
        }

        int maybeUpdateDescriptor(MediaRouteDescriptor mediaRouteDescriptor) {
            return this.mDescriptor != mediaRouteDescriptor ? updateDescriptor(mediaRouteDescriptor) : null;
        }

        int updateDescriptor(MediaRouteDescriptor mediaRouteDescriptor) {
            this.mDescriptor = mediaRouteDescriptor;
            int i = 0;
            if (mediaRouteDescriptor == null) {
                return 0;
            }
            if (!MediaRouter.equal(this.mName, mediaRouteDescriptor.getName())) {
                this.mName = mediaRouteDescriptor.getName();
                i = 1;
            }
            if (!MediaRouter.equal(this.mDescription, mediaRouteDescriptor.getDescription())) {
                this.mDescription = mediaRouteDescriptor.getDescription();
                i |= 1;
            }
            if (!MediaRouter.equal(this.mIconUri, mediaRouteDescriptor.getIconUri())) {
                this.mIconUri = mediaRouteDescriptor.getIconUri();
                i |= 1;
            }
            if (this.mEnabled != mediaRouteDescriptor.isEnabled()) {
                this.mEnabled = mediaRouteDescriptor.isEnabled();
                i |= 1;
            }
            if (this.mConnecting != mediaRouteDescriptor.isConnecting()) {
                this.mConnecting = mediaRouteDescriptor.isConnecting();
                i |= 1;
            }
            if (this.mConnectionState != mediaRouteDescriptor.getConnectionState()) {
                this.mConnectionState = mediaRouteDescriptor.getConnectionState();
                i |= 1;
            }
            if (!this.mControlFilters.equals(mediaRouteDescriptor.getControlFilters())) {
                this.mControlFilters.clear();
                this.mControlFilters.addAll(mediaRouteDescriptor.getControlFilters());
                i |= 1;
            }
            if (this.mPlaybackType != mediaRouteDescriptor.getPlaybackType()) {
                this.mPlaybackType = mediaRouteDescriptor.getPlaybackType();
                i |= 1;
            }
            if (this.mPlaybackStream != mediaRouteDescriptor.getPlaybackStream()) {
                this.mPlaybackStream = mediaRouteDescriptor.getPlaybackStream();
                i |= 1;
            }
            if (this.mDeviceType != mediaRouteDescriptor.getDeviceType()) {
                this.mDeviceType = mediaRouteDescriptor.getDeviceType();
                i |= 1;
            }
            if (this.mVolumeHandling != mediaRouteDescriptor.getVolumeHandling()) {
                this.mVolumeHandling = mediaRouteDescriptor.getVolumeHandling();
                i |= 3;
            }
            if (this.mVolume != mediaRouteDescriptor.getVolume()) {
                this.mVolume = mediaRouteDescriptor.getVolume();
                i |= 3;
            }
            if (this.mVolumeMax != mediaRouteDescriptor.getVolumeMax()) {
                this.mVolumeMax = mediaRouteDescriptor.getVolumeMax();
                i |= 3;
            }
            if (this.mPresentationDisplayId != mediaRouteDescriptor.getPresentationDisplayId()) {
                this.mPresentationDisplayId = mediaRouteDescriptor.getPresentationDisplayId();
                this.mPresentationDisplay = null;
                i |= 5;
            }
            if (!MediaRouter.equal(this.mExtras, mediaRouteDescriptor.getExtras())) {
                this.mExtras = mediaRouteDescriptor.getExtras();
                i |= 1;
            }
            if (!MediaRouter.equal(this.mSettingsIntent, mediaRouteDescriptor.getSettingsActivity())) {
                this.mSettingsIntent = mediaRouteDescriptor.getSettingsActivity();
                i |= 1;
            }
            if (this.mCanDisconnect == mediaRouteDescriptor.canDisconnectAndKeepPlaying()) {
                return i;
            }
            this.mCanDisconnect = mediaRouteDescriptor.canDisconnectAndKeepPlaying();
            return i | 5;
        }

        String getDescriptorId() {
            return this.mDescriptorId;
        }

        @RestrictTo({Scope.LIBRARY_GROUP})
        public MediaRouteProvider getProviderInstance() {
            return this.mProvider.getProviderInstance();
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public static class RouteGroup extends RouteInfo {
        private List<RouteInfo> mRoutes = new ArrayList();

        RouteGroup(ProviderInfo providerInfo, String str, String str2) {
            super(providerInfo, str, str2);
        }

        public int getRouteCount() {
            return this.mRoutes.size();
        }

        public RouteInfo getRouteAt(int i) {
            return (RouteInfo) this.mRoutes.get(i);
        }

        public List<RouteInfo> getRoutes() {
            return this.mRoutes;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(super.toString());
            stringBuilder.append('[');
            int size = this.mRoutes.size();
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(this.mRoutes.get(i));
            }
            stringBuilder.append(']');
            return stringBuilder.toString();
        }

        int maybeUpdateDescriptor(MediaRouteDescriptor mediaRouteDescriptor) {
            int i = 0;
            if (this.mDescriptor != mediaRouteDescriptor) {
                this.mDescriptor = mediaRouteDescriptor;
                if (mediaRouteDescriptor != null) {
                    List<String> groupMemberIds = mediaRouteDescriptor.getGroupMemberIds();
                    List arrayList = new ArrayList();
                    if (groupMemberIds.size() != this.mRoutes.size()) {
                        i = 1;
                    }
                    for (String access$000 : groupMemberIds) {
                        RouteInfo route = MediaRouter.sGlobal.getRoute(MediaRouter.sGlobal.getUniqueId(getProvider(), access$000));
                        if (route != null) {
                            arrayList.add(route);
                            if (i == 0 && !this.mRoutes.contains(route)) {
                                i = 1;
                            }
                        }
                    }
                    if (i != 0) {
                        this.mRoutes = arrayList;
                    }
                }
            }
            return super.updateDescriptor(mediaRouteDescriptor) | i;
        }
    }

    private MediaRouter(Context context) {
        this.mCallbackRecords = new ArrayList();
        this.mContext = context;
    }

    public static MediaRouter getInstance(@NonNull Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must not be null");
        }
        checkCallingThread();
        if (sGlobal == null) {
            sGlobal = new GlobalMediaRouter(context.getApplicationContext());
            sGlobal.start();
        }
        return sGlobal.getRouter(context);
    }

    public List<RouteInfo> getRoutes() {
        checkCallingThread();
        return sGlobal.getRoutes();
    }

    public List<ProviderInfo> getProviders() {
        checkCallingThread();
        return sGlobal.getProviders();
    }

    @NonNull
    public RouteInfo getDefaultRoute() {
        checkCallingThread();
        return sGlobal.getDefaultRoute();
    }

    public RouteInfo getBluetoothRoute() {
        checkCallingThread();
        return sGlobal.getBluetoothRoute();
    }

    @NonNull
    public RouteInfo getSelectedRoute() {
        checkCallingThread();
        return sGlobal.getSelectedRoute();
    }

    @NonNull
    public RouteInfo updateSelectedRoute(@NonNull MediaRouteSelector mediaRouteSelector) {
        if (mediaRouteSelector == null) {
            throw new IllegalArgumentException("selector must not be null");
        }
        checkCallingThread();
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("updateSelectedRoute: ");
            stringBuilder.append(mediaRouteSelector);
            Log.d(str, stringBuilder.toString());
        }
        RouteInfo selectedRoute = sGlobal.getSelectedRoute();
        if (selectedRoute.isDefaultOrBluetooth() || selectedRoute.matchesSelector(mediaRouteSelector) != null) {
            return selectedRoute;
        }
        selectedRoute = sGlobal.chooseFallbackRoute();
        sGlobal.selectRoute(selectedRoute);
        return selectedRoute;
    }

    public void selectRoute(@NonNull RouteInfo routeInfo) {
        if (routeInfo == null) {
            throw new IllegalArgumentException("route must not be null");
        }
        checkCallingThread();
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("selectRoute: ");
            stringBuilder.append(routeInfo);
            Log.d(str, stringBuilder.toString());
        }
        sGlobal.selectRoute(routeInfo);
    }

    public void unselect(int i) {
        if (i >= 0) {
            if (i <= 3) {
                checkCallingThread();
                RouteInfo chooseFallbackRoute = sGlobal.chooseFallbackRoute();
                if (sGlobal.getSelectedRoute() != chooseFallbackRoute) {
                    sGlobal.selectRoute(chooseFallbackRoute, i);
                    return;
                } else {
                    sGlobal.selectRoute(sGlobal.getDefaultRoute(), i);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Unsupported reason to unselect route");
    }

    public boolean isRouteAvailable(@NonNull MediaRouteSelector mediaRouteSelector, int i) {
        if (mediaRouteSelector == null) {
            throw new IllegalArgumentException("selector must not be null");
        }
        checkCallingThread();
        return sGlobal.isRouteAvailable(mediaRouteSelector, i);
    }

    public void addCallback(MediaRouteSelector mediaRouteSelector, Callback callback) {
        addCallback(mediaRouteSelector, callback, 0);
    }

    public void addCallback(@NonNull MediaRouteSelector mediaRouteSelector, @NonNull Callback callback, int i) {
        if (mediaRouteSelector == null) {
            throw new IllegalArgumentException("selector must not be null");
        } else if (callback == null) {
            throw new IllegalArgumentException("callback must not be null");
        } else {
            CallbackRecord callbackRecord;
            checkCallingThread();
            if (DEBUG) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("addCallback: selector=");
                stringBuilder.append(mediaRouteSelector);
                stringBuilder.append(", callback=");
                stringBuilder.append(callback);
                stringBuilder.append(", flags=");
                stringBuilder.append(Integer.toHexString(i));
                Log.d(str, stringBuilder.toString());
            }
            int findCallbackRecord = findCallbackRecord(callback);
            if (findCallbackRecord < 0) {
                callbackRecord = new CallbackRecord(this, callback);
                this.mCallbackRecords.add(callbackRecord);
            } else {
                callbackRecord = (CallbackRecord) this.mCallbackRecords.get(findCallbackRecord);
            }
            callback = null;
            if (((callbackRecord.mFlags ^ -1) & i) != 0) {
                callbackRecord.mFlags |= i;
                callback = true;
            }
            if (callbackRecord.mSelector.contains(mediaRouteSelector) == 0) {
                callbackRecord.mSelector = new Builder(callbackRecord.mSelector).addSelector(mediaRouteSelector).build();
                callback = true;
            }
            if (callback != null) {
                sGlobal.updateDiscoveryRequest();
            }
        }
    }

    public void removeCallback(@NonNull Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback must not be null");
        }
        checkCallingThread();
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("removeCallback: callback=");
            stringBuilder.append(callback);
            Log.d(str, stringBuilder.toString());
        }
        callback = findCallbackRecord(callback);
        if (callback >= null) {
            this.mCallbackRecords.remove(callback);
            sGlobal.updateDiscoveryRequest();
        }
    }

    private int findCallbackRecord(Callback callback) {
        int size = this.mCallbackRecords.size();
        for (int i = 0; i < size; i++) {
            if (((CallbackRecord) this.mCallbackRecords.get(i)).mCallback == callback) {
                return i;
            }
        }
        return -1;
    }

    public void addProvider(@NonNull MediaRouteProvider mediaRouteProvider) {
        if (mediaRouteProvider == null) {
            throw new IllegalArgumentException("providerInstance must not be null");
        }
        checkCallingThread();
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("addProvider: ");
            stringBuilder.append(mediaRouteProvider);
            Log.d(str, stringBuilder.toString());
        }
        sGlobal.addProvider(mediaRouteProvider);
    }

    public void removeProvider(@NonNull MediaRouteProvider mediaRouteProvider) {
        if (mediaRouteProvider == null) {
            throw new IllegalArgumentException("providerInstance must not be null");
        }
        checkCallingThread();
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("removeProvider: ");
            stringBuilder.append(mediaRouteProvider);
            Log.d(str, stringBuilder.toString());
        }
        sGlobal.removeProvider(mediaRouteProvider);
    }

    public void addRemoteControlClient(@NonNull Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("remoteControlClient must not be null");
        }
        checkCallingThread();
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("addRemoteControlClient: ");
            stringBuilder.append(obj);
            Log.d(str, stringBuilder.toString());
        }
        sGlobal.addRemoteControlClient(obj);
    }

    public void removeRemoteControlClient(@NonNull Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("remoteControlClient must not be null");
        }
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("removeRemoteControlClient: ");
            stringBuilder.append(obj);
            Log.d(str, stringBuilder.toString());
        }
        sGlobal.removeRemoteControlClient(obj);
    }

    public void setMediaSession(Object obj) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("addMediaSession: ");
            stringBuilder.append(obj);
            Log.d(str, stringBuilder.toString());
        }
        sGlobal.setMediaSession(obj);
    }

    public void setMediaSessionCompat(MediaSessionCompat mediaSessionCompat) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("addMediaSessionCompat: ");
            stringBuilder.append(mediaSessionCompat);
            Log.d(str, stringBuilder.toString());
        }
        sGlobal.setMediaSessionCompat(mediaSessionCompat);
    }

    public Token getMediaSessionToken() {
        return sGlobal.getMediaSessionToken();
    }

    static void checkCallingThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("The media router service must only be accessed on the application's main thread.");
        }
    }

    static <T> boolean equal(T t, T t2) {
        if (t != t2) {
            if (t == null || t2 == null || t.equals(t2) == null) {
                return null;
            }
        }
        return true;
    }
}
