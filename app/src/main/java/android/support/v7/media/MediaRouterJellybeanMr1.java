package android.support.v7.media;

import android.hardware.display.DisplayManager;
import android.media.MediaRouter;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Display;
import java.lang.reflect.Method;

@RequiresApi(17)
final class MediaRouterJellybeanMr1 {
    private static final String TAG = "MediaRouterJellybeanMr1";

    public static final class ActiveScanWorkaround implements Runnable {
        private static final int WIFI_DISPLAY_SCAN_INTERVAL = 15000;
        private boolean mActivelyScanningWifiDisplays;
        private final DisplayManager mDisplayManager;
        private final Handler mHandler;
        private Method mScanWifiDisplaysMethod;

        public ActiveScanWorkaround(android.content.Context r3, android.os.Handler r4) {
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
            r2 = this;
            r2.<init>();
            r0 = android.os.Build.VERSION.SDK_INT;
            r1 = 17;
            if (r0 == r1) goto L_0x000f;
        L_0x0009:
            r3 = new java.lang.UnsupportedOperationException;
            r3.<init>();
            throw r3;
        L_0x000f:
            r0 = "display";
            r3 = r3.getSystemService(r0);
            r3 = (android.hardware.display.DisplayManager) r3;
            r2.mDisplayManager = r3;
            r2.mHandler = r4;
            r3 = android.hardware.display.DisplayManager.class;	 Catch:{ NoSuchMethodException -> 0x0028 }
            r4 = "scanWifiDisplays";	 Catch:{ NoSuchMethodException -> 0x0028 }
            r0 = 0;	 Catch:{ NoSuchMethodException -> 0x0028 }
            r0 = new java.lang.Class[r0];	 Catch:{ NoSuchMethodException -> 0x0028 }
            r3 = r3.getMethod(r4, r0);	 Catch:{ NoSuchMethodException -> 0x0028 }
            r2.mScanWifiDisplaysMethod = r3;	 Catch:{ NoSuchMethodException -> 0x0028 }
        L_0x0028:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.media.MediaRouterJellybeanMr1.ActiveScanWorkaround.<init>(android.content.Context, android.os.Handler):void");
        }

        public void setActiveScanRouteTypes(int i) {
            if ((i & 2) != 0) {
                if (this.mActivelyScanningWifiDisplays != 0) {
                    return;
                }
                if (this.mScanWifiDisplaysMethod != 0) {
                    this.mActivelyScanningWifiDisplays = true;
                    this.mHandler.post(this);
                    return;
                }
                Log.w(MediaRouterJellybeanMr1.TAG, "Cannot scan for wifi displays because the DisplayManager.scanWifiDisplays() method is not available on this device.");
            } else if (this.mActivelyScanningWifiDisplays != 0) {
                this.mActivelyScanningWifiDisplays = false;
                this.mHandler.removeCallbacks(this);
            }
        }

        public void run() {
            if (this.mActivelyScanningWifiDisplays) {
                try {
                    this.mScanWifiDisplaysMethod.invoke(this.mDisplayManager, new Object[0]);
                } catch (Throwable e) {
                    Log.w(MediaRouterJellybeanMr1.TAG, "Cannot scan for wifi displays.", e);
                } catch (Throwable e2) {
                    Log.w(MediaRouterJellybeanMr1.TAG, "Cannot scan for wifi displays.", e2);
                }
                this.mHandler.postDelayed(this, 15000);
            }
        }
    }

    public interface Callback extends android.support.v7.media.MediaRouterJellybean.Callback {
        void onRoutePresentationDisplayChanged(Object obj);
    }

    static class CallbackProxy<T extends Callback> extends CallbackProxy<T> {
        public CallbackProxy(T t) {
            super(t);
        }

        public void onRoutePresentationDisplayChanged(MediaRouter mediaRouter, android.media.MediaRouter.RouteInfo routeInfo) {
            ((Callback) this.mCallback).onRoutePresentationDisplayChanged(routeInfo);
        }
    }

    public static final class IsConnectingWorkaround {
        private Method mGetStatusCodeMethod;
        private int mStatusConnecting;

        public IsConnectingWorkaround() {
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
            r1 = 17;
            if (r0 == r1) goto L_0x000f;
        L_0x0009:
            r0 = new java.lang.UnsupportedOperationException;
            r0.<init>();
            throw r0;
        L_0x000f:
            r0 = android.media.MediaRouter.RouteInfo.class;	 Catch:{ NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b }
            r1 = "STATUS_CONNECTING";	 Catch:{ NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b }
            r0 = r0.getField(r1);	 Catch:{ NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b }
            r1 = 0;	 Catch:{ NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b }
            r0 = r0.getInt(r1);	 Catch:{ NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b }
            r3.mStatusConnecting = r0;	 Catch:{ NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b }
            r0 = android.media.MediaRouter.RouteInfo.class;	 Catch:{ NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b }
            r1 = "getStatusCode";	 Catch:{ NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b }
            r2 = 0;	 Catch:{ NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b }
            r2 = new java.lang.Class[r2];	 Catch:{ NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b }
            r0 = r0.getMethod(r1, r2);	 Catch:{ NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b }
            r3.mGetStatusCodeMethod = r0;	 Catch:{ NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b, NoSuchFieldException -> 0x002b }
        L_0x002b:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.media.MediaRouterJellybeanMr1.IsConnectingWorkaround.<init>():void");
        }

        public boolean isConnecting(java.lang.Object r4) {
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
            r4 = (android.media.MediaRouter.RouteInfo) r4;
            r0 = r3.mGetStatusCodeMethod;
            r1 = 0;
            if (r0 == 0) goto L_0x001b;
        L_0x0007:
            r0 = r3.mGetStatusCodeMethod;	 Catch:{ IllegalAccessException -> 0x001b, IllegalAccessException -> 0x001b }
            r2 = new java.lang.Object[r1];	 Catch:{ IllegalAccessException -> 0x001b, IllegalAccessException -> 0x001b }
            r4 = r0.invoke(r4, r2);	 Catch:{ IllegalAccessException -> 0x001b, IllegalAccessException -> 0x001b }
            r4 = (java.lang.Integer) r4;	 Catch:{ IllegalAccessException -> 0x001b, IllegalAccessException -> 0x001b }
            r4 = r4.intValue();	 Catch:{ IllegalAccessException -> 0x001b, IllegalAccessException -> 0x001b }
            r0 = r3.mStatusConnecting;	 Catch:{ IllegalAccessException -> 0x001b, IllegalAccessException -> 0x001b }
            if (r4 != r0) goto L_0x001a;
        L_0x0019:
            r1 = 1;
        L_0x001a:
            return r1;
        L_0x001b:
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.media.MediaRouterJellybeanMr1.IsConnectingWorkaround.isConnecting(java.lang.Object):boolean");
        }
    }

    public static final class RouteInfo {
        public static boolean isEnabled(Object obj) {
            return ((android.media.MediaRouter.RouteInfo) obj).isEnabled();
        }

        public static Display getPresentationDisplay(Object obj) {
            try {
                return ((android.media.MediaRouter.RouteInfo) obj).getPresentationDisplay();
            } catch (Object obj2) {
                Log.w(MediaRouterJellybeanMr1.TAG, "Cannot get presentation display for the route.", obj2);
                return null;
            }
        }
    }

    MediaRouterJellybeanMr1() {
    }

    public static Object createCallback(Callback callback) {
        return new CallbackProxy(callback);
    }
}
