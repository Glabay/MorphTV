package android.support.v7.media;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaRouter;
import android.media.RemoteControlClient;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(16)
final class MediaRouterJellybean {
    public static final int ALL_ROUTE_TYPES = 8388611;
    public static final int DEVICE_OUT_BLUETOOTH = 896;
    public static final int ROUTE_TYPE_LIVE_AUDIO = 1;
    public static final int ROUTE_TYPE_LIVE_VIDEO = 2;
    public static final int ROUTE_TYPE_USER = 8388608;
    private static final String TAG = "MediaRouterJellybean";

    public interface Callback {
        void onRouteAdded(Object obj);

        void onRouteChanged(Object obj);

        void onRouteGrouped(Object obj, Object obj2, int i);

        void onRouteRemoved(Object obj);

        void onRouteSelected(int i, Object obj);

        void onRouteUngrouped(Object obj, Object obj2);

        void onRouteUnselected(int i, Object obj);

        void onRouteVolumeChanged(Object obj);
    }

    static class CallbackProxy<T extends Callback> extends android.media.MediaRouter.Callback {
        protected final T mCallback;

        public CallbackProxy(T t) {
            this.mCallback = t;
        }

        public void onRouteSelected(MediaRouter mediaRouter, int i, android.media.MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRouteSelected(i, routeInfo);
        }

        public void onRouteUnselected(MediaRouter mediaRouter, int i, android.media.MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRouteUnselected(i, routeInfo);
        }

        public void onRouteAdded(MediaRouter mediaRouter, android.media.MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRouteAdded(routeInfo);
        }

        public void onRouteRemoved(MediaRouter mediaRouter, android.media.MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRouteRemoved(routeInfo);
        }

        public void onRouteChanged(MediaRouter mediaRouter, android.media.MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRouteChanged(routeInfo);
        }

        public void onRouteGrouped(MediaRouter mediaRouter, android.media.MediaRouter.RouteInfo routeInfo, android.media.MediaRouter.RouteGroup routeGroup, int i) {
            this.mCallback.onRouteGrouped(routeInfo, routeGroup, i);
        }

        public void onRouteUngrouped(MediaRouter mediaRouter, android.media.MediaRouter.RouteInfo routeInfo, android.media.MediaRouter.RouteGroup routeGroup) {
            this.mCallback.onRouteUngrouped(routeInfo, routeGroup);
        }

        public void onRouteVolumeChanged(MediaRouter mediaRouter, android.media.MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRouteVolumeChanged(routeInfo);
        }
    }

    public static final class GetDefaultRouteWorkaround {
        private Method mGetSystemAudioRouteMethod;

        public GetDefaultRouteWorkaround() {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r3 = this;
            r3.<init>();
            r0 = android.os.Build.VERSION.SDK_INT;
            r1 = 16;
            if (r0 < r1) goto L_0x001e;
        L_0x0009:
            r0 = android.os.Build.VERSION.SDK_INT;
            r1 = 17;
            if (r0 <= r1) goto L_0x0010;
        L_0x000f:
            goto L_0x001e;
        L_0x0010:
            r0 = android.media.MediaRouter.class;	 Catch:{ NoSuchMethodException -> 0x001d }
            r1 = "getSystemAudioRoute";	 Catch:{ NoSuchMethodException -> 0x001d }
            r2 = 0;	 Catch:{ NoSuchMethodException -> 0x001d }
            r2 = new java.lang.Class[r2];	 Catch:{ NoSuchMethodException -> 0x001d }
            r0 = r0.getMethod(r1, r2);	 Catch:{ NoSuchMethodException -> 0x001d }
            r3.mGetSystemAudioRouteMethod = r0;	 Catch:{ NoSuchMethodException -> 0x001d }
        L_0x001d:
            return;
        L_0x001e:
            r0 = new java.lang.UnsupportedOperationException;
            r0.<init>();
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.media.MediaRouterJellybean.GetDefaultRouteWorkaround.<init>():void");
        }

        public java.lang.Object getDefaultRoute(java.lang.Object r4) {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r3 = this;
            r4 = (android.media.MediaRouter) r4;
            r0 = r3.mGetSystemAudioRouteMethod;
            r1 = 0;
            if (r0 == 0) goto L_0x0010;
        L_0x0007:
            r0 = r3.mGetSystemAudioRouteMethod;	 Catch:{ IllegalAccessException -> 0x0010, IllegalAccessException -> 0x0010 }
            r2 = new java.lang.Object[r1];	 Catch:{ IllegalAccessException -> 0x0010, IllegalAccessException -> 0x0010 }
            r0 = r0.invoke(r4, r2);	 Catch:{ IllegalAccessException -> 0x0010, IllegalAccessException -> 0x0010 }
            return r0;
        L_0x0010:
            r4 = r4.getRouteAt(r1);
            return r4;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.media.MediaRouterJellybean.GetDefaultRouteWorkaround.getDefaultRoute(java.lang.Object):java.lang.Object");
        }
    }

    public static final class RouteCategory {
        public static CharSequence getName(Object obj, Context context) {
            return ((android.media.MediaRouter.RouteCategory) obj).getName(context);
        }

        public static List getRoutes(Object obj) {
            List arrayList = new ArrayList();
            ((android.media.MediaRouter.RouteCategory) obj).getRoutes(arrayList);
            return arrayList;
        }

        public static int getSupportedTypes(Object obj) {
            return ((android.media.MediaRouter.RouteCategory) obj).getSupportedTypes();
        }

        public static boolean isGroupable(Object obj) {
            return ((android.media.MediaRouter.RouteCategory) obj).isGroupable();
        }
    }

    public static final class RouteGroup {
        public static List getGroupedRoutes(Object obj) {
            android.media.MediaRouter.RouteGroup routeGroup = (android.media.MediaRouter.RouteGroup) obj;
            int routeCount = routeGroup.getRouteCount();
            List arrayList = new ArrayList(routeCount);
            for (int i = 0; i < routeCount; i++) {
                arrayList.add(routeGroup.getRouteAt(i));
            }
            return arrayList;
        }
    }

    public static final class RouteInfo {
        public static CharSequence getName(Object obj, Context context) {
            return ((android.media.MediaRouter.RouteInfo) obj).getName(context);
        }

        public static CharSequence getStatus(Object obj) {
            return ((android.media.MediaRouter.RouteInfo) obj).getStatus();
        }

        public static int getSupportedTypes(Object obj) {
            return ((android.media.MediaRouter.RouteInfo) obj).getSupportedTypes();
        }

        public static Object getCategory(Object obj) {
            return ((android.media.MediaRouter.RouteInfo) obj).getCategory();
        }

        public static Drawable getIconDrawable(Object obj) {
            return ((android.media.MediaRouter.RouteInfo) obj).getIconDrawable();
        }

        public static int getPlaybackType(Object obj) {
            return ((android.media.MediaRouter.RouteInfo) obj).getPlaybackType();
        }

        public static int getPlaybackStream(Object obj) {
            return ((android.media.MediaRouter.RouteInfo) obj).getPlaybackStream();
        }

        public static int getVolume(Object obj) {
            return ((android.media.MediaRouter.RouteInfo) obj).getVolume();
        }

        public static int getVolumeMax(Object obj) {
            return ((android.media.MediaRouter.RouteInfo) obj).getVolumeMax();
        }

        public static int getVolumeHandling(Object obj) {
            return ((android.media.MediaRouter.RouteInfo) obj).getVolumeHandling();
        }

        public static Object getTag(Object obj) {
            return ((android.media.MediaRouter.RouteInfo) obj).getTag();
        }

        public static void setTag(Object obj, Object obj2) {
            ((android.media.MediaRouter.RouteInfo) obj).setTag(obj2);
        }

        public static void requestSetVolume(Object obj, int i) {
            ((android.media.MediaRouter.RouteInfo) obj).requestSetVolume(i);
        }

        public static void requestUpdateVolume(Object obj, int i) {
            ((android.media.MediaRouter.RouteInfo) obj).requestUpdateVolume(i);
        }

        public static Object getGroup(Object obj) {
            return ((android.media.MediaRouter.RouteInfo) obj).getGroup();
        }

        public static boolean isGroup(Object obj) {
            return obj instanceof android.media.MediaRouter.RouteGroup;
        }
    }

    public static final class SelectRouteWorkaround {
        private Method mSelectRouteIntMethod;

        public SelectRouteWorkaround() {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r5 = this;
            r5.<init>();
            r0 = android.os.Build.VERSION.SDK_INT;
            r1 = 16;
            if (r0 < r1) goto L_0x0028;
        L_0x0009:
            r0 = android.os.Build.VERSION.SDK_INT;
            r1 = 17;
            if (r0 <= r1) goto L_0x0010;
        L_0x000f:
            goto L_0x0028;
        L_0x0010:
            r0 = android.media.MediaRouter.class;	 Catch:{ NoSuchMethodException -> 0x0027 }
            r1 = "selectRouteInt";	 Catch:{ NoSuchMethodException -> 0x0027 }
            r2 = 2;	 Catch:{ NoSuchMethodException -> 0x0027 }
            r2 = new java.lang.Class[r2];	 Catch:{ NoSuchMethodException -> 0x0027 }
            r3 = 0;	 Catch:{ NoSuchMethodException -> 0x0027 }
            r4 = java.lang.Integer.TYPE;	 Catch:{ NoSuchMethodException -> 0x0027 }
            r2[r3] = r4;	 Catch:{ NoSuchMethodException -> 0x0027 }
            r3 = 1;	 Catch:{ NoSuchMethodException -> 0x0027 }
            r4 = android.media.MediaRouter.RouteInfo.class;	 Catch:{ NoSuchMethodException -> 0x0027 }
            r2[r3] = r4;	 Catch:{ NoSuchMethodException -> 0x0027 }
            r0 = r0.getMethod(r1, r2);	 Catch:{ NoSuchMethodException -> 0x0027 }
            r5.mSelectRouteIntMethod = r0;	 Catch:{ NoSuchMethodException -> 0x0027 }
        L_0x0027:
            return;
        L_0x0028:
            r0 = new java.lang.UnsupportedOperationException;
            r0.<init>();
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.media.MediaRouterJellybean.SelectRouteWorkaround.<init>():void");
        }

        public void selectRoute(Object obj, int i, Object obj2) {
            MediaRouter mediaRouter = (MediaRouter) obj;
            android.media.MediaRouter.RouteInfo routeInfo = (android.media.MediaRouter.RouteInfo) obj2;
            if ((routeInfo.getSupportedTypes() & 8388608) == 0) {
                if (this.mSelectRouteIntMethod != null) {
                    try {
                        this.mSelectRouteIntMethod.invoke(mediaRouter, new Object[]{Integer.valueOf(i), routeInfo});
                        return;
                    } catch (Throwable e) {
                        Log.w(MediaRouterJellybean.TAG, "Cannot programmatically select non-user route.  Media routing may not work.", e);
                    } catch (Throwable e2) {
                        Log.w(MediaRouterJellybean.TAG, "Cannot programmatically select non-user route.  Media routing may not work.", e2);
                    }
                } else {
                    Log.w(MediaRouterJellybean.TAG, "Cannot programmatically select non-user route because the platform is missing the selectRouteInt() method.  Media routing may not work.");
                }
            }
            mediaRouter.selectRoute(i, routeInfo);
        }
    }

    public static final class UserRouteInfo {
        public static void setName(Object obj, CharSequence charSequence) {
            ((android.media.MediaRouter.UserRouteInfo) obj).setName(charSequence);
        }

        public static void setStatus(Object obj, CharSequence charSequence) {
            ((android.media.MediaRouter.UserRouteInfo) obj).setStatus(charSequence);
        }

        public static void setIconDrawable(Object obj, Drawable drawable) {
            ((android.media.MediaRouter.UserRouteInfo) obj).setIconDrawable(drawable);
        }

        public static void setPlaybackType(Object obj, int i) {
            ((android.media.MediaRouter.UserRouteInfo) obj).setPlaybackType(i);
        }

        public static void setPlaybackStream(Object obj, int i) {
            ((android.media.MediaRouter.UserRouteInfo) obj).setPlaybackStream(i);
        }

        public static void setVolume(Object obj, int i) {
            ((android.media.MediaRouter.UserRouteInfo) obj).setVolume(i);
        }

        public static void setVolumeMax(Object obj, int i) {
            ((android.media.MediaRouter.UserRouteInfo) obj).setVolumeMax(i);
        }

        public static void setVolumeHandling(Object obj, int i) {
            ((android.media.MediaRouter.UserRouteInfo) obj).setVolumeHandling(i);
        }

        public static void setVolumeCallback(Object obj, Object obj2) {
            ((android.media.MediaRouter.UserRouteInfo) obj).setVolumeCallback((android.media.MediaRouter.VolumeCallback) obj2);
        }

        public static void setRemoteControlClient(Object obj, Object obj2) {
            ((android.media.MediaRouter.UserRouteInfo) obj).setRemoteControlClient((RemoteControlClient) obj2);
        }
    }

    public interface VolumeCallback {
        void onVolumeSetRequest(Object obj, int i);

        void onVolumeUpdateRequest(Object obj, int i);
    }

    static class VolumeCallbackProxy<T extends VolumeCallback> extends android.media.MediaRouter.VolumeCallback {
        protected final T mCallback;

        public VolumeCallbackProxy(T t) {
            this.mCallback = t;
        }

        public void onVolumeSetRequest(android.media.MediaRouter.RouteInfo routeInfo, int i) {
            this.mCallback.onVolumeSetRequest(routeInfo, i);
        }

        public void onVolumeUpdateRequest(android.media.MediaRouter.RouteInfo routeInfo, int i) {
            this.mCallback.onVolumeUpdateRequest(routeInfo, i);
        }
    }

    MediaRouterJellybean() {
    }

    public static Object getMediaRouter(Context context) {
        return context.getSystemService("media_router");
    }

    public static List getRoutes(Object obj) {
        MediaRouter mediaRouter = (MediaRouter) obj;
        int routeCount = mediaRouter.getRouteCount();
        List arrayList = new ArrayList(routeCount);
        for (int i = 0; i < routeCount; i++) {
            arrayList.add(mediaRouter.getRouteAt(i));
        }
        return arrayList;
    }

    public static List getCategories(Object obj) {
        MediaRouter mediaRouter = (MediaRouter) obj;
        int categoryCount = mediaRouter.getCategoryCount();
        List arrayList = new ArrayList(categoryCount);
        for (int i = 0; i < categoryCount; i++) {
            arrayList.add(mediaRouter.getCategoryAt(i));
        }
        return arrayList;
    }

    public static Object getSelectedRoute(Object obj, int i) {
        return ((MediaRouter) obj).getSelectedRoute(i);
    }

    public static void selectRoute(Object obj, int i, Object obj2) {
        ((MediaRouter) obj).selectRoute(i, (android.media.MediaRouter.RouteInfo) obj2);
    }

    public static void addCallback(Object obj, int i, Object obj2) {
        ((MediaRouter) obj).addCallback(i, (android.media.MediaRouter.Callback) obj2);
    }

    public static void removeCallback(Object obj, Object obj2) {
        ((MediaRouter) obj).removeCallback((android.media.MediaRouter.Callback) obj2);
    }

    public static Object createRouteCategory(Object obj, String str, boolean z) {
        return ((MediaRouter) obj).createRouteCategory(str, z);
    }

    public static Object createUserRoute(Object obj, Object obj2) {
        return ((MediaRouter) obj).createUserRoute((android.media.MediaRouter.RouteCategory) obj2);
    }

    public static void addUserRoute(Object obj, Object obj2) {
        ((MediaRouter) obj).addUserRoute((android.media.MediaRouter.UserRouteInfo) obj2);
    }

    public static void removeUserRoute(Object obj, Object obj2) {
        ((MediaRouter) obj).removeUserRoute((android.media.MediaRouter.UserRouteInfo) obj2);
    }

    public static Object createCallback(Callback callback) {
        return new CallbackProxy(callback);
    }

    public static Object createVolumeCallback(VolumeCallback volumeCallback) {
        return new VolumeCallbackProxy(volumeCallback);
    }

    static boolean checkRoutedToBluetooth(android.content.Context r6) {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = 0;
        r1 = "audio";	 Catch:{ Exception -> 0x0033 }
        r6 = r6.getSystemService(r1);	 Catch:{ Exception -> 0x0033 }
        r6 = (android.media.AudioManager) r6;	 Catch:{ Exception -> 0x0033 }
        r1 = r6.getClass();	 Catch:{ Exception -> 0x0033 }
        r2 = "getDevicesForStream";	 Catch:{ Exception -> 0x0033 }
        r3 = 1;	 Catch:{ Exception -> 0x0033 }
        r4 = new java.lang.Class[r3];	 Catch:{ Exception -> 0x0033 }
        r5 = java.lang.Integer.TYPE;	 Catch:{ Exception -> 0x0033 }
        r4[r0] = r5;	 Catch:{ Exception -> 0x0033 }
        r1 = r1.getDeclaredMethod(r2, r4);	 Catch:{ Exception -> 0x0033 }
        r2 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x0033 }
        r4 = 3;	 Catch:{ Exception -> 0x0033 }
        r4 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x0033 }
        r2[r0] = r4;	 Catch:{ Exception -> 0x0033 }
        r6 = r1.invoke(r6, r2);	 Catch:{ Exception -> 0x0033 }
        r6 = (java.lang.Integer) r6;	 Catch:{ Exception -> 0x0033 }
        r6 = r6.intValue();	 Catch:{ Exception -> 0x0033 }
        r6 = r6 & 896;
        if (r6 == 0) goto L_0x0032;
    L_0x0031:
        r0 = 1;
    L_0x0032:
        return r0;
    L_0x0033:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.media.MediaRouterJellybean.checkRoutedToBluetooth(android.content.Context):boolean");
    }
}
