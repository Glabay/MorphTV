package android.support.v7.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaControllerCompat.Callback;
import android.support.v4.media.session.MediaSessionCompat.Token;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.app.OverlayListView.OverlayObject;
import android.support.v7.app.OverlayListView.OverlayObject.OnAnimationEndListener;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouter.RouteGroup;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.support.v7.mediarouter.C0266R;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MediaRouteControllerDialog extends AlertDialog {
    static final int BUTTON_DISCONNECT_RES_ID = 16908314;
    private static final int BUTTON_NEUTRAL_RES_ID = 16908315;
    static final int BUTTON_STOP_RES_ID = 16908313;
    static final int CONNECTION_TIMEOUT_MILLIS = ((int) TimeUnit.SECONDS.toMillis(30));
    static final boolean DEBUG = Log.isLoggable(TAG, 3);
    static final String TAG = "MediaRouteCtrlDialog";
    static final int VOLUME_UPDATE_DELAY_MILLIS = 500;
    private Interpolator mAccelerateDecelerateInterpolator;
    final AccessibilityManager mAccessibilityManager;
    int mArtIconBackgroundColor;
    Bitmap mArtIconBitmap;
    boolean mArtIconIsLoaded;
    Bitmap mArtIconLoadedBitmap;
    Uri mArtIconUri;
    private ImageView mArtView;
    private boolean mAttachedToWindow;
    private final MediaRouterCallback mCallback;
    private ImageButton mCloseButton;
    Context mContext;
    MediaControllerCallback mControllerCallback;
    private boolean mCreated;
    private FrameLayout mCustomControlLayout;
    private View mCustomControlView;
    FrameLayout mDefaultControlLayout;
    MediaDescriptionCompat mDescription;
    private LinearLayout mDialogAreaLayout;
    private int mDialogContentWidth;
    private Button mDisconnectButton;
    private View mDividerView;
    private FrameLayout mExpandableAreaLayout;
    private Interpolator mFastOutSlowInInterpolator;
    FetchArtTask mFetchArtTask;
    private MediaRouteExpandCollapseButton mGroupExpandCollapseButton;
    int mGroupListAnimationDurationMs;
    Runnable mGroupListFadeInAnimation;
    private int mGroupListFadeInDurationMs;
    private int mGroupListFadeOutDurationMs;
    private List<RouteInfo> mGroupMemberRoutes;
    Set<RouteInfo> mGroupMemberRoutesAdded;
    Set<RouteInfo> mGroupMemberRoutesAnimatingWithBitmap;
    private Set<RouteInfo> mGroupMemberRoutesRemoved;
    boolean mHasPendingUpdate;
    private Interpolator mInterpolator;
    boolean mIsGroupExpanded;
    boolean mIsGroupListAnimating;
    boolean mIsGroupListAnimationPending;
    private Interpolator mLinearOutSlowInInterpolator;
    MediaControllerCompat mMediaController;
    private LinearLayout mMediaMainControlLayout;
    boolean mPendingUpdateAnimationNeeded;
    private ImageButton mPlaybackControlButton;
    private RelativeLayout mPlaybackControlLayout;
    final RouteInfo mRoute;
    RouteInfo mRouteInVolumeSliderTouched;
    private TextView mRouteNameTextView;
    final MediaRouter mRouter;
    PlaybackStateCompat mState;
    private Button mStopCastingButton;
    private TextView mSubtitleView;
    private TextView mTitleView;
    VolumeChangeListener mVolumeChangeListener;
    private boolean mVolumeControlEnabled;
    private LinearLayout mVolumeControlLayout;
    VolumeGroupAdapter mVolumeGroupAdapter;
    OverlayListView mVolumeGroupList;
    private int mVolumeGroupListItemHeight;
    private int mVolumeGroupListItemIconSize;
    private int mVolumeGroupListMaxHeight;
    private final int mVolumeGroupListPaddingTop;
    SeekBar mVolumeSlider;
    Map<RouteInfo, SeekBar> mVolumeSliderMap;

    /* renamed from: android.support.v7.app.MediaRouteControllerDialog$1 */
    class C02321 implements Runnable {
        C02321() {
        }

        public void run() {
            MediaRouteControllerDialog.this.startGroupListFadeInAnimation();
        }
    }

    /* renamed from: android.support.v7.app.MediaRouteControllerDialog$2 */
    class C02332 implements OnClickListener {
        C02332() {
        }

        public void onClick(View view) {
            MediaRouteControllerDialog.this.dismiss();
        }
    }

    /* renamed from: android.support.v7.app.MediaRouteControllerDialog$3 */
    class C02343 implements OnClickListener {
        public void onClick(View view) {
        }

        C02343() {
        }
    }

    /* renamed from: android.support.v7.app.MediaRouteControllerDialog$4 */
    class C02354 implements OnClickListener {
        C02354() {
        }

        public void onClick(android.view.View r3) {
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
            r3 = android.support.v7.app.MediaRouteControllerDialog.this;
            r3 = r3.mMediaController;
            if (r3 == 0) goto L_0x002f;
        L_0x0006:
            r3 = android.support.v7.app.MediaRouteControllerDialog.this;
            r3 = r3.mMediaController;
            r3 = r3.getSessionActivity();
            if (r3 == 0) goto L_0x002f;
        L_0x0010:
            r3.send();	 Catch:{ CanceledException -> 0x0019 }
            r0 = android.support.v7.app.MediaRouteControllerDialog.this;	 Catch:{ CanceledException -> 0x0019 }
            r0.dismiss();	 Catch:{ CanceledException -> 0x0019 }
            goto L_0x002f;
        L_0x0019:
            r0 = "MediaRouteCtrlDialog";
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r1.append(r3);
            r3 = " was not sent, it had been canceled.";
            r1.append(r3);
            r3 = r1.toString();
            android.util.Log.e(r0, r3);
        L_0x002f:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.app.MediaRouteControllerDialog.4.onClick(android.view.View):void");
        }
    }

    /* renamed from: android.support.v7.app.MediaRouteControllerDialog$5 */
    class C02365 implements OnClickListener {
        C02365() {
        }

        public void onClick(View view) {
            MediaRouteControllerDialog.this.mIsGroupExpanded ^= true;
            if (MediaRouteControllerDialog.this.mIsGroupExpanded != null) {
                MediaRouteControllerDialog.this.mVolumeGroupList.setVisibility(0);
            }
            MediaRouteControllerDialog.this.loadInterpolator();
            MediaRouteControllerDialog.this.updateLayoutHeight(true);
        }
    }

    /* renamed from: android.support.v7.app.MediaRouteControllerDialog$9 */
    class C02409 implements AnimationListener {
        public void onAnimationEnd(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }

        C02409() {
        }

        public void onAnimationStart(Animation animation) {
            MediaRouteControllerDialog.this.mVolumeGroupList.startAnimationAll();
            MediaRouteControllerDialog.this.mVolumeGroupList.postDelayed(MediaRouteControllerDialog.this.mGroupListFadeInAnimation, (long) MediaRouteControllerDialog.this.mGroupListAnimationDurationMs);
        }
    }

    private final class ClickListener implements OnClickListener {
        ClickListener() {
        }

        public void onClick(View view) {
            view = view.getId();
            int i = 1;
            if (view != 16908313) {
                if (view != MediaRouteControllerDialog.BUTTON_DISCONNECT_RES_ID) {
                    if (view == C0266R.id.mr_control_playback_ctrl) {
                        if (MediaRouteControllerDialog.this.mMediaController != null && MediaRouteControllerDialog.this.mState != null) {
                            int i2 = 0;
                            if (MediaRouteControllerDialog.this.mState.getState() != 3) {
                                i = 0;
                            }
                            if (i != 0 && MediaRouteControllerDialog.this.isPauseActionSupported() != null) {
                                MediaRouteControllerDialog.this.mMediaController.getTransportControls().pause();
                                i2 = C0266R.string.mr_controller_pause;
                            } else if (i != 0 && MediaRouteControllerDialog.this.isStopActionSupported() != null) {
                                MediaRouteControllerDialog.this.mMediaController.getTransportControls().stop();
                                i2 = C0266R.string.mr_controller_stop;
                            } else if (i == 0 && MediaRouteControllerDialog.this.isPlayActionSupported() != null) {
                                MediaRouteControllerDialog.this.mMediaController.getTransportControls().play();
                                i2 = C0266R.string.mr_controller_play;
                            }
                            if (MediaRouteControllerDialog.this.mAccessibilityManager != null && MediaRouteControllerDialog.this.mAccessibilityManager.isEnabled() != null && i2 != 0) {
                                view = AccessibilityEvent.obtain(16384);
                                view.setPackageName(MediaRouteControllerDialog.this.mContext.getPackageName());
                                view.setClassName(getClass().getName());
                                view.getText().add(MediaRouteControllerDialog.this.mContext.getString(i2));
                                MediaRouteControllerDialog.this.mAccessibilityManager.sendAccessibilityEvent(view);
                                return;
                            }
                            return;
                        }
                        return;
                    } else if (view == C0266R.id.mr_close) {
                        MediaRouteControllerDialog.this.dismiss();
                        return;
                    } else {
                        return;
                    }
                }
            }
            if (MediaRouteControllerDialog.this.mRoute.isSelected()) {
                MediaRouter mediaRouter = MediaRouteControllerDialog.this.mRouter;
                if (view == 16908313) {
                    i = 2;
                }
                mediaRouter.unselect(i);
            }
            MediaRouteControllerDialog.this.dismiss();
        }
    }

    private class FetchArtTask extends AsyncTask<Void, Void, Bitmap> {
        private static final long SHOW_ANIM_TIME_THRESHOLD_MILLIS = 120;
        private int mBackgroundColor;
        private final Bitmap mIconBitmap;
        private final Uri mIconUri;
        private long mStartTimeMillis;

        FetchArtTask() {
            Uri uri = null;
            Bitmap iconBitmap = MediaRouteControllerDialog.this.mDescription == null ? null : MediaRouteControllerDialog.this.mDescription.getIconBitmap();
            if (MediaRouteControllerDialog.this.isBitmapRecycled(iconBitmap)) {
                Log.w(MediaRouteControllerDialog.TAG, "Can't fetch the given art bitmap because it's already recycled.");
                iconBitmap = null;
            }
            this.mIconBitmap = iconBitmap;
            if (MediaRouteControllerDialog.this.mDescription != null) {
                uri = MediaRouteControllerDialog.this.mDescription.getIconUri();
            }
            this.mIconUri = uri;
        }

        public Bitmap getIconBitmap() {
            return this.mIconBitmap;
        }

        public Uri getIconUri() {
            return this.mIconUri;
        }

        protected void onPreExecute() {
            this.mStartTimeMillis = SystemClock.uptimeMillis();
            MediaRouteControllerDialog.this.clearLoadedBitmap();
        }

        protected android.graphics.Bitmap doInBackground(java.lang.Void... r8) {
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
            r7 = this;
            r8 = r7.mIconBitmap;
            r0 = 0;
            r1 = 1;
            r2 = 0;
            if (r8 == 0) goto L_0x000b;
        L_0x0007:
            r8 = r7.mIconBitmap;
            goto L_0x00e0;
        L_0x000b:
            r8 = r7.mIconUri;
            if (r8 == 0) goto L_0x00df;
        L_0x000f:
            r8 = r7.mIconUri;	 Catch:{ IOException -> 0x00b8, all -> 0x00b5 }
            r8 = r7.openInputStreamByScheme(r8);	 Catch:{ IOException -> 0x00b8, all -> 0x00b5 }
            if (r8 != 0) goto L_0x0038;
        L_0x0017:
            r3 = "MediaRouteCtrlDialog";	 Catch:{ IOException -> 0x0035 }
            r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0035 }
            r4.<init>();	 Catch:{ IOException -> 0x0035 }
            r5 = "Unable to open: ";	 Catch:{ IOException -> 0x0035 }
            r4.append(r5);	 Catch:{ IOException -> 0x0035 }
            r5 = r7.mIconUri;	 Catch:{ IOException -> 0x0035 }
            r4.append(r5);	 Catch:{ IOException -> 0x0035 }
            r4 = r4.toString();	 Catch:{ IOException -> 0x0035 }
            android.util.Log.w(r3, r4);	 Catch:{ IOException -> 0x0035 }
            if (r8 == 0) goto L_0x0034;
        L_0x0031:
            r8.close();	 Catch:{ IOException -> 0x0034 }
        L_0x0034:
            return r2;
        L_0x0035:
            r3 = move-exception;
            goto L_0x00ba;
        L_0x0038:
            r3 = new android.graphics.BitmapFactory$Options;	 Catch:{ IOException -> 0x0035 }
            r3.<init>();	 Catch:{ IOException -> 0x0035 }
            r3.inJustDecodeBounds = r1;	 Catch:{ IOException -> 0x0035 }
            android.graphics.BitmapFactory.decodeStream(r8, r2, r3);	 Catch:{ IOException -> 0x0035 }
            r4 = r3.outWidth;	 Catch:{ IOException -> 0x0035 }
            if (r4 == 0) goto L_0x00af;	 Catch:{ IOException -> 0x0035 }
        L_0x0046:
            r4 = r3.outHeight;	 Catch:{ IOException -> 0x0035 }
            if (r4 != 0) goto L_0x004b;
        L_0x004a:
            goto L_0x00af;
        L_0x004b:
            r8.reset();	 Catch:{ IOException -> 0x004f }
            goto L_0x007f;
        L_0x004f:
            r8.close();	 Catch:{ IOException -> 0x0035 }
            r4 = r7.mIconUri;	 Catch:{ IOException -> 0x0035 }
            r4 = r7.openInputStreamByScheme(r4);	 Catch:{ IOException -> 0x0035 }
            if (r4 != 0) goto L_0x007e;
        L_0x005a:
            r8 = "MediaRouteCtrlDialog";	 Catch:{ IOException -> 0x007b, all -> 0x0078 }
            r3 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x007b, all -> 0x0078 }
            r3.<init>();	 Catch:{ IOException -> 0x007b, all -> 0x0078 }
            r5 = "Unable to open: ";	 Catch:{ IOException -> 0x007b, all -> 0x0078 }
            r3.append(r5);	 Catch:{ IOException -> 0x007b, all -> 0x0078 }
            r5 = r7.mIconUri;	 Catch:{ IOException -> 0x007b, all -> 0x0078 }
            r3.append(r5);	 Catch:{ IOException -> 0x007b, all -> 0x0078 }
            r3 = r3.toString();	 Catch:{ IOException -> 0x007b, all -> 0x0078 }
            android.util.Log.w(r8, r3);	 Catch:{ IOException -> 0x007b, all -> 0x0078 }
            if (r4 == 0) goto L_0x0077;
        L_0x0074:
            r4.close();	 Catch:{ IOException -> 0x0077 }
        L_0x0077:
            return r2;
        L_0x0078:
            r0 = move-exception;
            r8 = r4;
            goto L_0x00d9;
        L_0x007b:
            r3 = move-exception;
            r8 = r4;
            goto L_0x00ba;
        L_0x007e:
            r8 = r4;
        L_0x007f:
            r3.inJustDecodeBounds = r0;	 Catch:{ IOException -> 0x0035 }
            r4 = android.support.v7.app.MediaRouteControllerDialog.this;	 Catch:{ IOException -> 0x0035 }
            r5 = r3.outWidth;	 Catch:{ IOException -> 0x0035 }
            r6 = r3.outHeight;	 Catch:{ IOException -> 0x0035 }
            r4 = r4.getDesiredArtHeight(r5, r6);	 Catch:{ IOException -> 0x0035 }
            r5 = r3.outHeight;	 Catch:{ IOException -> 0x0035 }
            r5 = r5 / r4;	 Catch:{ IOException -> 0x0035 }
            r4 = java.lang.Integer.highestOneBit(r5);	 Catch:{ IOException -> 0x0035 }
            r4 = java.lang.Math.max(r1, r4);	 Catch:{ IOException -> 0x0035 }
            r3.inSampleSize = r4;	 Catch:{ IOException -> 0x0035 }
            r4 = r7.isCancelled();	 Catch:{ IOException -> 0x0035 }
            if (r4 == 0) goto L_0x00a4;
        L_0x009e:
            if (r8 == 0) goto L_0x00a3;
        L_0x00a0:
            r8.close();	 Catch:{ IOException -> 0x00a3 }
        L_0x00a3:
            return r2;
        L_0x00a4:
            r3 = android.graphics.BitmapFactory.decodeStream(r8, r2, r3);	 Catch:{ IOException -> 0x0035 }
            if (r8 == 0) goto L_0x00ad;
        L_0x00aa:
            r8.close();	 Catch:{ IOException -> 0x00ad }
        L_0x00ad:
            r8 = r3;
            goto L_0x00e0;
        L_0x00af:
            if (r8 == 0) goto L_0x00b4;
        L_0x00b1:
            r8.close();	 Catch:{ IOException -> 0x00b4 }
        L_0x00b4:
            return r2;
        L_0x00b5:
            r0 = move-exception;
            r8 = r2;
            goto L_0x00d9;
        L_0x00b8:
            r3 = move-exception;
            r8 = r2;
        L_0x00ba:
            r4 = "MediaRouteCtrlDialog";	 Catch:{ all -> 0x00d8 }
            r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d8 }
            r5.<init>();	 Catch:{ all -> 0x00d8 }
            r6 = "Unable to open: ";	 Catch:{ all -> 0x00d8 }
            r5.append(r6);	 Catch:{ all -> 0x00d8 }
            r6 = r7.mIconUri;	 Catch:{ all -> 0x00d8 }
            r5.append(r6);	 Catch:{ all -> 0x00d8 }
            r5 = r5.toString();	 Catch:{ all -> 0x00d8 }
            android.util.Log.w(r4, r5, r3);	 Catch:{ all -> 0x00d8 }
            if (r8 == 0) goto L_0x00df;
        L_0x00d4:
            r8.close();	 Catch:{ IOException -> 0x00df }
            goto L_0x00df;
        L_0x00d8:
            r0 = move-exception;
        L_0x00d9:
            if (r8 == 0) goto L_0x00de;
        L_0x00db:
            r8.close();	 Catch:{ IOException -> 0x00de }
        L_0x00de:
            throw r0;
        L_0x00df:
            r8 = r2;
        L_0x00e0:
            r3 = android.support.v7.app.MediaRouteControllerDialog.this;
            r3 = r3.isBitmapRecycled(r8);
            if (r3 == 0) goto L_0x00ff;
        L_0x00e8:
            r0 = "MediaRouteCtrlDialog";
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r3 = "Can't use recycled bitmap: ";
            r1.append(r3);
            r1.append(r8);
            r8 = r1.toString();
            android.util.Log.w(r0, r8);
            return r2;
        L_0x00ff:
            if (r8 == 0) goto L_0x0133;
        L_0x0101:
            r2 = r8.getWidth();
            r3 = r8.getHeight();
            if (r2 >= r3) goto L_0x0133;
        L_0x010b:
            r2 = new android.support.v7.graphics.Palette$Builder;
            r2.<init>(r8);
            r1 = r2.maximumColorCount(r1);
            r1 = r1.generate();
            r2 = r1.getSwatches();
            r2 = r2.isEmpty();
            if (r2 == 0) goto L_0x0123;
        L_0x0122:
            goto L_0x0131;
        L_0x0123:
            r1 = r1.getSwatches();
            r0 = r1.get(r0);
            r0 = (android.support.v7.graphics.Palette.Swatch) r0;
            r0 = r0.getRgb();
        L_0x0131:
            r7.mBackgroundColor = r0;
        L_0x0133:
            return r8;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.app.MediaRouteControllerDialog.FetchArtTask.doInBackground(java.lang.Void[]):android.graphics.Bitmap");
        }

        protected void onPostExecute(Bitmap bitmap) {
            MediaRouteControllerDialog.this.mFetchArtTask = null;
            if (!ObjectsCompat.equals(MediaRouteControllerDialog.this.mArtIconBitmap, this.mIconBitmap) || !ObjectsCompat.equals(MediaRouteControllerDialog.this.mArtIconUri, this.mIconUri)) {
                MediaRouteControllerDialog.this.mArtIconBitmap = this.mIconBitmap;
                MediaRouteControllerDialog.this.mArtIconLoadedBitmap = bitmap;
                MediaRouteControllerDialog.this.mArtIconUri = this.mIconUri;
                MediaRouteControllerDialog.this.mArtIconBackgroundColor = this.mBackgroundColor;
                boolean z = true;
                MediaRouteControllerDialog.this.mArtIconIsLoaded = true;
                long uptimeMillis = SystemClock.uptimeMillis() - this.mStartTimeMillis;
                bitmap = MediaRouteControllerDialog.this;
                if (uptimeMillis <= SHOW_ANIM_TIME_THRESHOLD_MILLIS) {
                    z = false;
                }
                bitmap.update(z);
            }
        }

        private InputStream openInputStreamByScheme(Uri uri) throws IOException {
            String toLowerCase = uri.getScheme().toLowerCase();
            if (!("android.resource".equals(toLowerCase) || "content".equals(toLowerCase))) {
                if (!"file".equals(toLowerCase)) {
                    uri = new URL(uri.toString()).openConnection();
                    uri.setConnectTimeout(MediaRouteControllerDialog.CONNECTION_TIMEOUT_MILLIS);
                    uri.setReadTimeout(MediaRouteControllerDialog.CONNECTION_TIMEOUT_MILLIS);
                    uri = uri.getInputStream();
                    if (uri != null) {
                        return null;
                    }
                    return new BufferedInputStream(uri);
                }
            }
            uri = MediaRouteControllerDialog.this.mContext.getContentResolver().openInputStream(uri);
            if (uri != null) {
                return new BufferedInputStream(uri);
            }
            return null;
        }
    }

    private final class MediaControllerCallback extends Callback {
        MediaControllerCallback() {
        }

        public void onSessionDestroyed() {
            if (MediaRouteControllerDialog.this.mMediaController != null) {
                MediaRouteControllerDialog.this.mMediaController.unregisterCallback(MediaRouteControllerDialog.this.mControllerCallback);
                MediaRouteControllerDialog.this.mMediaController = null;
            }
        }

        public void onPlaybackStateChanged(PlaybackStateCompat playbackStateCompat) {
            MediaRouteControllerDialog.this.mState = playbackStateCompat;
            MediaRouteControllerDialog.this.update(false);
        }

        public void onMetadataChanged(MediaMetadataCompat mediaMetadataCompat) {
            MediaRouteControllerDialog.this.mDescription = mediaMetadataCompat == null ? null : mediaMetadataCompat.getDescription();
            MediaRouteControllerDialog.this.updateArtIconIfNeeded();
            MediaRouteControllerDialog.this.update(false);
        }
    }

    private final class MediaRouterCallback extends MediaRouter.Callback {
        MediaRouterCallback() {
        }

        public void onRouteUnselected(MediaRouter mediaRouter, RouteInfo routeInfo) {
            MediaRouteControllerDialog.this.update(null);
        }

        public void onRouteChanged(MediaRouter mediaRouter, RouteInfo routeInfo) {
            MediaRouteControllerDialog.this.update(true);
        }

        public void onRouteVolumeChanged(MediaRouter mediaRouter, RouteInfo routeInfo) {
            SeekBar seekBar = (SeekBar) MediaRouteControllerDialog.this.mVolumeSliderMap.get(routeInfo);
            int volume = routeInfo.getVolume();
            if (MediaRouteControllerDialog.DEBUG) {
                String str = MediaRouteControllerDialog.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onRouteVolumeChanged(), route.getVolume:");
                stringBuilder.append(volume);
                Log.d(str, stringBuilder.toString());
            }
            if (seekBar != null && MediaRouteControllerDialog.this.mRouteInVolumeSliderTouched != routeInfo) {
                seekBar.setProgress(volume);
            }
        }
    }

    private class VolumeChangeListener implements OnSeekBarChangeListener {
        private final Runnable mStopTrackingTouch = new C02411();

        /* renamed from: android.support.v7.app.MediaRouteControllerDialog$VolumeChangeListener$1 */
        class C02411 implements Runnable {
            C02411() {
            }

            public void run() {
                if (MediaRouteControllerDialog.this.mRouteInVolumeSliderTouched != null) {
                    MediaRouteControllerDialog.this.mRouteInVolumeSliderTouched = null;
                    if (MediaRouteControllerDialog.this.mHasPendingUpdate) {
                        MediaRouteControllerDialog.this.update(MediaRouteControllerDialog.this.mPendingUpdateAnimationNeeded);
                    }
                }
            }
        }

        VolumeChangeListener() {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            if (MediaRouteControllerDialog.this.mRouteInVolumeSliderTouched != null) {
                MediaRouteControllerDialog.this.mVolumeSlider.removeCallbacks(this.mStopTrackingTouch);
            }
            MediaRouteControllerDialog.this.mRouteInVolumeSliderTouched = (RouteInfo) seekBar.getTag();
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            MediaRouteControllerDialog.this.mVolumeSlider.postDelayed(this.mStopTrackingTouch, 500);
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            if (z) {
                RouteInfo routeInfo = (RouteInfo) seekBar.getTag();
                if (MediaRouteControllerDialog.DEBUG) {
                    z = MediaRouteControllerDialog.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("onProgressChanged(): calling MediaRouter.RouteInfo.requestSetVolume(");
                    stringBuilder.append(i);
                    stringBuilder.append(")");
                    Log.d(z, stringBuilder.toString());
                }
                routeInfo.requestSetVolume(i);
            }
        }
    }

    private class VolumeGroupAdapter extends ArrayAdapter<RouteInfo> {
        final float mDisabledAlpha;

        public boolean isEnabled(int i) {
            return false;
        }

        public VolumeGroupAdapter(Context context, List<RouteInfo> list) {
            super(context, null, list);
            this.mDisabledAlpha = MediaRouterThemeHelper.getDisabledAlpha(context);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            int i2 = 0;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(C0266R.layout.mr_controller_volume_item, viewGroup, false);
            } else {
                MediaRouteControllerDialog.this.updateVolumeGroupItemHeight(view);
            }
            RouteInfo routeInfo = (RouteInfo) getItem(i);
            if (routeInfo != null) {
                int i3;
                boolean isEnabled = routeInfo.isEnabled();
                TextView textView = (TextView) view.findViewById(C0266R.id.mr_name);
                textView.setEnabled(isEnabled);
                textView.setText(routeInfo.getName());
                MediaRouteVolumeSlider mediaRouteVolumeSlider = (MediaRouteVolumeSlider) view.findViewById(C0266R.id.mr_volume_slider);
                MediaRouterThemeHelper.setVolumeSliderColor(viewGroup.getContext(), mediaRouteVolumeSlider, MediaRouteControllerDialog.this.mVolumeGroupList);
                mediaRouteVolumeSlider.setTag(routeInfo);
                MediaRouteControllerDialog.this.mVolumeSliderMap.put(routeInfo, mediaRouteVolumeSlider);
                mediaRouteVolumeSlider.setHideThumb(isEnabled ^ 1);
                mediaRouteVolumeSlider.setEnabled(isEnabled);
                if (isEnabled) {
                    if (MediaRouteControllerDialog.this.isVolumeControlAvailable(routeInfo) != null) {
                        mediaRouteVolumeSlider.setMax(routeInfo.getVolumeMax());
                        mediaRouteVolumeSlider.setProgress(routeInfo.getVolume());
                        mediaRouteVolumeSlider.setOnSeekBarChangeListener(MediaRouteControllerDialog.this.mVolumeChangeListener);
                    } else {
                        mediaRouteVolumeSlider.setMax(100);
                        mediaRouteVolumeSlider.setProgress(100);
                        mediaRouteVolumeSlider.setEnabled(false);
                    }
                }
                ImageView imageView = (ImageView) view.findViewById(C0266R.id.mr_volume_item_icon);
                if (isEnabled) {
                    i3 = 255;
                } else {
                    i3 = (int) (this.mDisabledAlpha * 255.0f);
                }
                imageView.setAlpha(i3);
                LinearLayout linearLayout = (LinearLayout) view.findViewById(C0266R.id.volume_item_container);
                if (MediaRouteControllerDialog.this.mGroupMemberRoutesAnimatingWithBitmap.contains(routeInfo)) {
                    i2 = 4;
                }
                linearLayout.setVisibility(i2);
                if (!(MediaRouteControllerDialog.this.mGroupMemberRoutesAdded == null || MediaRouteControllerDialog.this.mGroupMemberRoutesAdded.contains(routeInfo) == 0)) {
                    i = new AlphaAnimation(0.0f, 0.0f);
                    i.setDuration(0);
                    i.setFillEnabled(true);
                    i.setFillAfter(true);
                    view.clearAnimation();
                    view.startAnimation(i);
                }
            }
            return view;
        }
    }

    public View onCreateMediaControlView(Bundle bundle) {
        return null;
    }

    public MediaRouteControllerDialog(Context context) {
        this(context, 0);
    }

    public MediaRouteControllerDialog(Context context, int i) {
        context = MediaRouterThemeHelper.createThemedDialogContext(context, i, true);
        super(context, MediaRouterThemeHelper.createThemedDialogStyle(context));
        this.mVolumeControlEnabled = true;
        this.mGroupListFadeInAnimation = new C02321();
        this.mContext = getContext();
        this.mControllerCallback = new MediaControllerCallback();
        this.mRouter = MediaRouter.getInstance(this.mContext);
        this.mCallback = new MediaRouterCallback();
        this.mRoute = this.mRouter.getSelectedRoute();
        setMediaSession(this.mRouter.getMediaSessionToken());
        this.mVolumeGroupListPaddingTop = this.mContext.getResources().getDimensionPixelSize(C0266R.dimen.mr_controller_volume_group_list_padding_top);
        this.mAccessibilityManager = (AccessibilityManager) this.mContext.getSystemService("accessibility");
        if (VERSION.SDK_INT >= 21) {
            this.mLinearOutSlowInInterpolator = AnimationUtils.loadInterpolator(context, C0266R.interpolator.mr_linear_out_slow_in);
            this.mFastOutSlowInInterpolator = AnimationUtils.loadInterpolator(context, C0266R.interpolator.mr_fast_out_slow_in);
        }
        this.mAccelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
    }

    public RouteInfo getRoute() {
        return this.mRoute;
    }

    private RouteGroup getGroup() {
        return this.mRoute instanceof RouteGroup ? (RouteGroup) this.mRoute : null;
    }

    public View getMediaControlView() {
        return this.mCustomControlView;
    }

    public void setVolumeControlEnabled(boolean z) {
        if (this.mVolumeControlEnabled != z) {
            this.mVolumeControlEnabled = z;
            if (this.mCreated) {
                update(false);
            }
        }
    }

    public boolean isVolumeControlEnabled() {
        return this.mVolumeControlEnabled;
    }

    private void setMediaSession(Token token) {
        PlaybackStateCompat playbackStateCompat = null;
        if (this.mMediaController != null) {
            this.mMediaController.unregisterCallback(this.mControllerCallback);
            this.mMediaController = null;
        }
        if (token != null && this.mAttachedToWindow) {
            try {
                this.mMediaController = new MediaControllerCompat(this.mContext, token);
            } catch (Token token2) {
                Log.e(TAG, "Error creating media controller in setMediaSession.", token2);
            }
            if (this.mMediaController != null) {
                this.mMediaController.registerCallback(this.mControllerCallback);
            }
            if (this.mMediaController == null) {
                token2 = null;
            } else {
                token2 = this.mMediaController.getMetadata();
            }
            if (token2 == null) {
                token2 = null;
            } else {
                token2 = token2.getDescription();
            }
            this.mDescription = token2;
            if (this.mMediaController != null) {
                playbackStateCompat = this.mMediaController.getPlaybackState();
            }
            this.mState = playbackStateCompat;
            updateArtIconIfNeeded();
            update(null);
        }
    }

    public Token getMediaSession() {
        return this.mMediaController == null ? null : this.mMediaController.getSessionToken();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setBackgroundDrawableResource(17170445);
        setContentView(C0266R.layout.mr_controller_material_dialog_b);
        findViewById(BUTTON_NEUTRAL_RES_ID).setVisibility(8);
        OnClickListener clickListener = new ClickListener();
        this.mExpandableAreaLayout = (FrameLayout) findViewById(C0266R.id.mr_expandable_area);
        this.mExpandableAreaLayout.setOnClickListener(new C02332());
        this.mDialogAreaLayout = (LinearLayout) findViewById(C0266R.id.mr_dialog_area);
        this.mDialogAreaLayout.setOnClickListener(new C02343());
        int buttonTextColor = MediaRouterThemeHelper.getButtonTextColor(this.mContext);
        this.mDisconnectButton = (Button) findViewById(BUTTON_DISCONNECT_RES_ID);
        this.mDisconnectButton.setText(C0266R.string.mr_controller_disconnect);
        this.mDisconnectButton.setTextColor(buttonTextColor);
        this.mDisconnectButton.setOnClickListener(clickListener);
        this.mStopCastingButton = (Button) findViewById(BUTTON_STOP_RES_ID);
        this.mStopCastingButton.setText(C0266R.string.mr_controller_stop_casting);
        this.mStopCastingButton.setTextColor(buttonTextColor);
        this.mStopCastingButton.setOnClickListener(clickListener);
        this.mRouteNameTextView = (TextView) findViewById(C0266R.id.mr_name);
        this.mCloseButton = (ImageButton) findViewById(C0266R.id.mr_close);
        this.mCloseButton.setOnClickListener(clickListener);
        this.mCustomControlLayout = (FrameLayout) findViewById(C0266R.id.mr_custom_control);
        this.mDefaultControlLayout = (FrameLayout) findViewById(C0266R.id.mr_default_control);
        OnClickListener c02354 = new C02354();
        this.mArtView = (ImageView) findViewById(C0266R.id.mr_art);
        this.mArtView.setOnClickListener(c02354);
        findViewById(C0266R.id.mr_control_title_container).setOnClickListener(c02354);
        this.mMediaMainControlLayout = (LinearLayout) findViewById(C0266R.id.mr_media_main_control);
        this.mDividerView = findViewById(C0266R.id.mr_control_divider);
        this.mPlaybackControlLayout = (RelativeLayout) findViewById(C0266R.id.mr_playback_control);
        this.mTitleView = (TextView) findViewById(C0266R.id.mr_control_title);
        this.mSubtitleView = (TextView) findViewById(C0266R.id.mr_control_subtitle);
        this.mPlaybackControlButton = (ImageButton) findViewById(C0266R.id.mr_control_playback_ctrl);
        this.mPlaybackControlButton.setOnClickListener(clickListener);
        this.mVolumeControlLayout = (LinearLayout) findViewById(C0266R.id.mr_volume_control);
        this.mVolumeControlLayout.setVisibility(8);
        this.mVolumeSlider = (SeekBar) findViewById(C0266R.id.mr_volume_slider);
        this.mVolumeSlider.setTag(this.mRoute);
        this.mVolumeChangeListener = new VolumeChangeListener();
        this.mVolumeSlider.setOnSeekBarChangeListener(this.mVolumeChangeListener);
        this.mVolumeGroupList = (OverlayListView) findViewById(C0266R.id.mr_volume_group_list);
        this.mGroupMemberRoutes = new ArrayList();
        this.mVolumeGroupAdapter = new VolumeGroupAdapter(this.mVolumeGroupList.getContext(), this.mGroupMemberRoutes);
        this.mVolumeGroupList.setAdapter(this.mVolumeGroupAdapter);
        this.mGroupMemberRoutesAnimatingWithBitmap = new HashSet();
        MediaRouterThemeHelper.setMediaControlsBackgroundColor(this.mContext, this.mMediaMainControlLayout, this.mVolumeGroupList, getGroup() != null);
        MediaRouterThemeHelper.setVolumeSliderColor(this.mContext, (MediaRouteVolumeSlider) this.mVolumeSlider, this.mMediaMainControlLayout);
        this.mVolumeSliderMap = new HashMap();
        this.mVolumeSliderMap.put(this.mRoute, this.mVolumeSlider);
        this.mGroupExpandCollapseButton = (MediaRouteExpandCollapseButton) findViewById(C0266R.id.mr_group_expand_collapse);
        this.mGroupExpandCollapseButton.setOnClickListener(new C02365());
        loadInterpolator();
        this.mGroupListAnimationDurationMs = this.mContext.getResources().getInteger(C0266R.integer.mr_controller_volume_group_list_animation_duration_ms);
        this.mGroupListFadeInDurationMs = this.mContext.getResources().getInteger(C0266R.integer.mr_controller_volume_group_list_fade_in_duration_ms);
        this.mGroupListFadeOutDurationMs = this.mContext.getResources().getInteger(C0266R.integer.mr_controller_volume_group_list_fade_out_duration_ms);
        this.mCustomControlView = onCreateMediaControlView(bundle);
        if (this.mCustomControlView != null) {
            this.mCustomControlLayout.addView(this.mCustomControlView);
            this.mCustomControlLayout.setVisibility(0);
        }
        this.mCreated = true;
        updateLayout();
    }

    void updateLayout() {
        int dialogWidth = MediaRouteDialogHelper.getDialogWidth(this.mContext);
        getWindow().setLayout(dialogWidth, -2);
        View decorView = getWindow().getDecorView();
        this.mDialogContentWidth = (dialogWidth - decorView.getPaddingLeft()) - decorView.getPaddingRight();
        Resources resources = this.mContext.getResources();
        this.mVolumeGroupListItemIconSize = resources.getDimensionPixelSize(C0266R.dimen.mr_controller_volume_group_list_item_icon_size);
        this.mVolumeGroupListItemHeight = resources.getDimensionPixelSize(C0266R.dimen.mr_controller_volume_group_list_item_height);
        this.mVolumeGroupListMaxHeight = resources.getDimensionPixelSize(C0266R.dimen.mr_controller_volume_group_list_max_height);
        this.mArtIconBitmap = null;
        this.mArtIconUri = null;
        updateArtIconIfNeeded();
        update(false);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAttachedToWindow = true;
        this.mRouter.addCallback(MediaRouteSelector.EMPTY, this.mCallback, 2);
        setMediaSession(this.mRouter.getMediaSessionToken());
    }

    public void onDetachedFromWindow() {
        this.mRouter.removeCallback(this.mCallback);
        setMediaSession(null);
        this.mAttachedToWindow = false;
        super.onDetachedFromWindow();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 25) {
            if (i != 24) {
                return super.onKeyDown(i, keyEvent);
            }
        }
        this.mRoute.requestUpdateVolume(i == 25 ? -1 : 1);
        return true;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 25) {
            if (i != 24) {
                return super.onKeyUp(i, keyEvent);
            }
        }
        return true;
    }

    void update(boolean z) {
        if (this.mRouteInVolumeSliderTouched != null) {
            this.mHasPendingUpdate = true;
            this.mPendingUpdateAnimationNeeded = z | this.mPendingUpdateAnimationNeeded;
            return;
        }
        int i = 0;
        this.mHasPendingUpdate = false;
        this.mPendingUpdateAnimationNeeded = false;
        if (this.mRoute.isSelected()) {
            if (!this.mRoute.isDefaultOrBluetooth()) {
                if (this.mCreated) {
                    this.mRouteNameTextView.setText(this.mRoute.getName());
                    Button button = this.mDisconnectButton;
                    if (!this.mRoute.canDisconnect()) {
                        i = 8;
                    }
                    button.setVisibility(i);
                    if (this.mCustomControlView == null && this.mArtIconIsLoaded) {
                        if (isBitmapRecycled(this.mArtIconLoadedBitmap)) {
                            String str = TAG;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Can't set artwork image with recycled bitmap: ");
                            stringBuilder.append(this.mArtIconLoadedBitmap);
                            Log.w(str, stringBuilder.toString());
                        } else {
                            this.mArtView.setImageBitmap(this.mArtIconLoadedBitmap);
                            this.mArtView.setBackgroundColor(this.mArtIconBackgroundColor);
                        }
                        clearLoadedBitmap();
                    }
                    updateVolumeControlLayout();
                    updatePlaybackControlLayout();
                    updateLayoutHeight(z);
                    return;
                }
                return;
            }
        }
        dismiss();
    }

    private boolean isBitmapRecycled(Bitmap bitmap) {
        return (bitmap == null || bitmap.isRecycled() == null) ? null : true;
    }

    private boolean canShowPlaybackControlLayout() {
        return this.mCustomControlView == null && !(this.mDescription == null && this.mState == null);
    }

    private int getMainControllerHeight(boolean z) {
        if (!z && this.mVolumeControlLayout.getVisibility() != 0) {
            return 0;
        }
        int paddingTop = (this.mMediaMainControlLayout.getPaddingTop() + this.mMediaMainControlLayout.getPaddingBottom()) + 0;
        if (z) {
            paddingTop += this.mPlaybackControlLayout.getMeasuredHeight();
        }
        if (this.mVolumeControlLayout.getVisibility() == 0) {
            paddingTop += this.mVolumeControlLayout.getMeasuredHeight();
        }
        int i = paddingTop;
        return (!z || this.mVolumeControlLayout.getVisibility()) ? i : i + this.mDividerView.getMeasuredHeight();
    }

    private void updateMediaControlVisibility(boolean z) {
        View view = this.mDividerView;
        int i = 0;
        int i2 = (this.mVolumeControlLayout.getVisibility() == 0 && z) ? 0 : 8;
        view.setVisibility(i2);
        LinearLayout linearLayout = this.mMediaMainControlLayout;
        if (this.mVolumeControlLayout.getVisibility() == 8 && !z) {
            i = 8;
        }
        linearLayout.setVisibility(i);
    }

    void updateLayoutHeight(final boolean z) {
        this.mDefaultControlLayout.requestLayout();
        this.mDefaultControlLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                MediaRouteControllerDialog.this.mDefaultControlLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                if (MediaRouteControllerDialog.this.mIsGroupListAnimating) {
                    MediaRouteControllerDialog.this.mIsGroupListAnimationPending = true;
                } else {
                    MediaRouteControllerDialog.this.updateLayoutHeightInternal(z);
                }
            }
        });
    }

    void updateLayoutHeightInternal(boolean z) {
        int desiredArtHeight;
        int size;
        int i;
        Rect rect;
        int height;
        boolean z2;
        int max;
        int layoutHeight = getLayoutHeight(this.mMediaMainControlLayout);
        setLayoutHeight(this.mMediaMainControlLayout, -1);
        updateMediaControlVisibility(canShowPlaybackControlLayout());
        View decorView = getWindow().getDecorView();
        decorView.measure(MeasureSpec.makeMeasureSpec(getWindow().getAttributes().width, 1073741824), 0);
        setLayoutHeight(this.mMediaMainControlLayout, layoutHeight);
        if (this.mCustomControlView == null && (this.mArtView.getDrawable() instanceof BitmapDrawable)) {
            Bitmap bitmap = ((BitmapDrawable) this.mArtView.getDrawable()).getBitmap();
            if (bitmap != null) {
                desiredArtHeight = getDesiredArtHeight(bitmap.getWidth(), bitmap.getHeight());
                this.mArtView.setScaleType(bitmap.getWidth() >= bitmap.getHeight() ? ScaleType.FIT_XY : ScaleType.FIT_CENTER);
                layoutHeight = getMainControllerHeight(canShowPlaybackControlLayout());
                size = this.mGroupMemberRoutes.size();
                if (getGroup() != null) {
                    i = 0;
                } else {
                    i = this.mVolumeGroupListItemHeight * getGroup().getRoutes().size();
                }
                if (size > 0) {
                    i += this.mVolumeGroupListPaddingTop;
                }
                size = Math.min(i, this.mVolumeGroupListMaxHeight);
                if (this.mIsGroupExpanded) {
                    size = 0;
                }
                i = Math.max(desiredArtHeight, size) + layoutHeight;
                rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                height = rect.height() - (this.mDialogAreaLayout.getMeasuredHeight() - this.mDefaultControlLayout.getMeasuredHeight());
                if (this.mCustomControlView == null || desiredArtHeight <= 0 || i > height) {
                    if (getLayoutHeight(this.mVolumeGroupList) + this.mMediaMainControlLayout.getMeasuredHeight() >= this.mDefaultControlLayout.getMeasuredHeight()) {
                        this.mArtView.setVisibility(8);
                    }
                    i = size + layoutHeight;
                    desiredArtHeight = 0;
                } else {
                    this.mArtView.setVisibility(0);
                    setLayoutHeight(this.mArtView, desiredArtHeight);
                }
                if (canShowPlaybackControlLayout() || r5 > height) {
                    this.mPlaybackControlLayout.setVisibility(8);
                } else {
                    this.mPlaybackControlLayout.setVisibility(0);
                }
                z2 = true;
                updateMediaControlVisibility(this.mPlaybackControlLayout.getVisibility() != 0);
                if (this.mPlaybackControlLayout.getVisibility() == 0) {
                    z2 = false;
                }
                layoutHeight = getMainControllerHeight(z2);
                max = Math.max(desiredArtHeight, size) + layoutHeight;
                if (max > height) {
                    size -= max - height;
                    max = height;
                }
                this.mMediaMainControlLayout.clearAnimation();
                this.mVolumeGroupList.clearAnimation();
                this.mDefaultControlLayout.clearAnimation();
                if (z) {
                    setLayoutHeight(this.mMediaMainControlLayout, layoutHeight);
                    setLayoutHeight(this.mVolumeGroupList, size);
                    setLayoutHeight(this.mDefaultControlLayout, max);
                } else {
                    animateLayoutHeight(this.mMediaMainControlLayout, layoutHeight);
                    animateLayoutHeight(this.mVolumeGroupList, size);
                    animateLayoutHeight(this.mDefaultControlLayout, max);
                }
                setLayoutHeight(this.mExpandableAreaLayout, rect.height());
                rebuildVolumeGroupList(z);
            }
        }
        desiredArtHeight = 0;
        layoutHeight = getMainControllerHeight(canShowPlaybackControlLayout());
        size = this.mGroupMemberRoutes.size();
        if (getGroup() != null) {
            i = this.mVolumeGroupListItemHeight * getGroup().getRoutes().size();
        } else {
            i = 0;
        }
        if (size > 0) {
            i += this.mVolumeGroupListPaddingTop;
        }
        size = Math.min(i, this.mVolumeGroupListMaxHeight);
        if (this.mIsGroupExpanded) {
            size = 0;
        }
        i = Math.max(desiredArtHeight, size) + layoutHeight;
        rect = new Rect();
        decorView.getWindowVisibleDisplayFrame(rect);
        height = rect.height() - (this.mDialogAreaLayout.getMeasuredHeight() - this.mDefaultControlLayout.getMeasuredHeight());
        if (this.mCustomControlView == null) {
        }
        if (getLayoutHeight(this.mVolumeGroupList) + this.mMediaMainControlLayout.getMeasuredHeight() >= this.mDefaultControlLayout.getMeasuredHeight()) {
            this.mArtView.setVisibility(8);
        }
        i = size + layoutHeight;
        desiredArtHeight = 0;
        if (canShowPlaybackControlLayout()) {
        }
        this.mPlaybackControlLayout.setVisibility(8);
        z2 = true;
        if (this.mPlaybackControlLayout.getVisibility() != 0) {
        }
        updateMediaControlVisibility(this.mPlaybackControlLayout.getVisibility() != 0);
        if (this.mPlaybackControlLayout.getVisibility() == 0) {
            z2 = false;
        }
        layoutHeight = getMainControllerHeight(z2);
        max = Math.max(desiredArtHeight, size) + layoutHeight;
        if (max > height) {
            size -= max - height;
            max = height;
        }
        this.mMediaMainControlLayout.clearAnimation();
        this.mVolumeGroupList.clearAnimation();
        this.mDefaultControlLayout.clearAnimation();
        if (z) {
            setLayoutHeight(this.mMediaMainControlLayout, layoutHeight);
            setLayoutHeight(this.mVolumeGroupList, size);
            setLayoutHeight(this.mDefaultControlLayout, max);
        } else {
            animateLayoutHeight(this.mMediaMainControlLayout, layoutHeight);
            animateLayoutHeight(this.mVolumeGroupList, size);
            animateLayoutHeight(this.mDefaultControlLayout, max);
        }
        setLayoutHeight(this.mExpandableAreaLayout, rect.height());
        rebuildVolumeGroupList(z);
    }

    void updateVolumeGroupItemHeight(View view) {
        setLayoutHeight((LinearLayout) view.findViewById(C0266R.id.volume_item_container), this.mVolumeGroupListItemHeight);
        view = view.findViewById(C0266R.id.mr_volume_item_icon);
        LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = this.mVolumeGroupListItemIconSize;
        layoutParams.height = this.mVolumeGroupListItemIconSize;
        view.setLayoutParams(layoutParams);
    }

    private void animateLayoutHeight(final View view, final int i) {
        final int layoutHeight = getLayoutHeight(view);
        Animation c02387 = new Animation() {
            protected void applyTransformation(float f, Transformation transformation) {
                MediaRouteControllerDialog.setLayoutHeight(view, layoutHeight - ((int) (((float) (layoutHeight - i)) * f)));
            }
        };
        c02387.setDuration((long) this.mGroupListAnimationDurationMs);
        if (VERSION.SDK_INT >= 21) {
            c02387.setInterpolator(this.mInterpolator);
        }
        view.startAnimation(c02387);
    }

    void loadInterpolator() {
        if (VERSION.SDK_INT >= 21) {
            this.mInterpolator = this.mIsGroupExpanded ? this.mLinearOutSlowInInterpolator : this.mFastOutSlowInInterpolator;
        } else {
            this.mInterpolator = this.mAccelerateDecelerateInterpolator;
        }
    }

    private void updateVolumeControlLayout() {
        int i = 8;
        if (!isVolumeControlAvailable(this.mRoute)) {
            this.mVolumeControlLayout.setVisibility(8);
        } else if (this.mVolumeControlLayout.getVisibility() == 8) {
            this.mVolumeControlLayout.setVisibility(0);
            this.mVolumeSlider.setMax(this.mRoute.getVolumeMax());
            this.mVolumeSlider.setProgress(this.mRoute.getVolume());
            MediaRouteExpandCollapseButton mediaRouteExpandCollapseButton = this.mGroupExpandCollapseButton;
            if (getGroup() != null) {
                i = 0;
            }
            mediaRouteExpandCollapseButton.setVisibility(i);
        }
    }

    private void rebuildVolumeGroupList(boolean z) {
        List routes = getGroup() == null ? null : getGroup().getRoutes();
        if (routes == null) {
            this.mGroupMemberRoutes.clear();
            this.mVolumeGroupAdapter.notifyDataSetChanged();
        } else if (MediaRouteDialogHelper.listUnorderedEquals(this.mGroupMemberRoutes, routes)) {
            this.mVolumeGroupAdapter.notifyDataSetChanged();
        } else {
            Map itemBoundMap = z ? MediaRouteDialogHelper.getItemBoundMap(this.mVolumeGroupList, this.mVolumeGroupAdapter) : null;
            Map itemBitmapMap = z ? MediaRouteDialogHelper.getItemBitmapMap(this.mContext, this.mVolumeGroupList, this.mVolumeGroupAdapter) : null;
            this.mGroupMemberRoutesAdded = MediaRouteDialogHelper.getItemsAdded(this.mGroupMemberRoutes, routes);
            this.mGroupMemberRoutesRemoved = MediaRouteDialogHelper.getItemsRemoved(this.mGroupMemberRoutes, routes);
            this.mGroupMemberRoutes.addAll(0, this.mGroupMemberRoutesAdded);
            this.mGroupMemberRoutes.removeAll(this.mGroupMemberRoutesRemoved);
            this.mVolumeGroupAdapter.notifyDataSetChanged();
            if (z && this.mIsGroupExpanded && this.mGroupMemberRoutesAdded.size() + this.mGroupMemberRoutesRemoved.size() <= false) {
                animateGroupListItems(itemBoundMap, itemBitmapMap);
                return;
            }
            this.mGroupMemberRoutesAdded = null;
            this.mGroupMemberRoutesRemoved = null;
        }
    }

    private void animateGroupListItems(final Map<RouteInfo, Rect> map, final Map<RouteInfo, BitmapDrawable> map2) {
        this.mVolumeGroupList.setEnabled(false);
        this.mVolumeGroupList.requestLayout();
        this.mIsGroupListAnimating = true;
        this.mVolumeGroupList.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                MediaRouteControllerDialog.this.mVolumeGroupList.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                MediaRouteControllerDialog.this.animateGroupListItemsInternal(map, map2);
            }
        });
    }

    void animateGroupListItemsInternal(Map<RouteInfo, Rect> map, Map<RouteInfo, BitmapDrawable> map2) {
        Map<RouteInfo, Rect> map3 = map;
        if (this.mGroupMemberRoutesAdded != null) {
            if (r0.mGroupMemberRoutesRemoved != null) {
                int size = r0.mGroupMemberRoutesAdded.size() - r0.mGroupMemberRoutesRemoved.size();
                AnimationListener c02409 = new C02409();
                int firstVisiblePosition = r0.mVolumeGroupList.getFirstVisiblePosition();
                Object obj = null;
                for (int i = 0; i < r0.mVolumeGroupList.getChildCount(); i++) {
                    View childAt = r0.mVolumeGroupList.getChildAt(i);
                    RouteInfo routeInfo = (RouteInfo) r0.mVolumeGroupAdapter.getItem(firstVisiblePosition + i);
                    Rect rect = (Rect) map3.get(routeInfo);
                    int top = childAt.getTop();
                    int i2 = rect != null ? rect.top : (r0.mVolumeGroupListItemHeight * size) + top;
                    Animation animationSet = new AnimationSet(true);
                    if (r0.mGroupMemberRoutesAdded != null && r0.mGroupMemberRoutesAdded.contains(routeInfo)) {
                        Animation alphaAnimation = new AlphaAnimation(0.0f, 0.0f);
                        alphaAnimation.setDuration((long) r0.mGroupListFadeInDurationMs);
                        animationSet.addAnimation(alphaAnimation);
                        i2 = top;
                    }
                    Animation translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (i2 - top), 0.0f);
                    translateAnimation.setDuration((long) r0.mGroupListAnimationDurationMs);
                    animationSet.addAnimation(translateAnimation);
                    animationSet.setFillAfter(true);
                    animationSet.setFillEnabled(true);
                    animationSet.setInterpolator(r0.mInterpolator);
                    if (obj == null) {
                        animationSet.setAnimationListener(c02409);
                        obj = 1;
                    }
                    childAt.clearAnimation();
                    childAt.startAnimation(animationSet);
                    map3.remove(routeInfo);
                    map2.remove(routeInfo);
                }
                Map<RouteInfo, BitmapDrawable> map4 = map2;
                for (Entry entry : map2.entrySet()) {
                    OverlayObject interpolator;
                    final RouteInfo routeInfo2 = (RouteInfo) entry.getKey();
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) entry.getValue();
                    Rect rect2 = (Rect) map3.get(routeInfo2);
                    if (r0.mGroupMemberRoutesRemoved.contains(routeInfo2)) {
                        interpolator = new OverlayObject(bitmapDrawable, rect2).setAlphaAnimation(1.0f, 0.0f).setDuration((long) r0.mGroupListFadeOutDurationMs).setInterpolator(r0.mInterpolator);
                    } else {
                        interpolator = new OverlayObject(bitmapDrawable, rect2).setTranslateYAnimation(r0.mVolumeGroupListItemHeight * size).setDuration((long) r0.mGroupListAnimationDurationMs).setInterpolator(r0.mInterpolator).setAnimationEndListener(new OnAnimationEndListener() {
                            public void onAnimationEnd() {
                                MediaRouteControllerDialog.this.mGroupMemberRoutesAnimatingWithBitmap.remove(routeInfo2);
                                MediaRouteControllerDialog.this.mVolumeGroupAdapter.notifyDataSetChanged();
                            }
                        });
                        r0.mGroupMemberRoutesAnimatingWithBitmap.add(routeInfo2);
                    }
                    r0.mVolumeGroupList.addOverlayObject(interpolator);
                }
            }
        }
    }

    void startGroupListFadeInAnimation() {
        clearGroupListAnimation(true);
        this.mVolumeGroupList.requestLayout();
        this.mVolumeGroupList.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                MediaRouteControllerDialog.this.mVolumeGroupList.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                MediaRouteControllerDialog.this.startGroupListFadeInAnimationInternal();
            }
        });
    }

    void startGroupListFadeInAnimationInternal() {
        if (this.mGroupMemberRoutesAdded == null || this.mGroupMemberRoutesAdded.size() == 0) {
            finishAnimation(true);
        } else {
            fadeInAddedRoutes();
        }
    }

    void finishAnimation(boolean z) {
        this.mGroupMemberRoutesAdded = null;
        this.mGroupMemberRoutesRemoved = null;
        this.mIsGroupListAnimating = false;
        if (this.mIsGroupListAnimationPending) {
            this.mIsGroupListAnimationPending = false;
            updateLayoutHeight(z);
        }
        this.mVolumeGroupList.setEnabled(true);
    }

    private void fadeInAddedRoutes() {
        AnimationListener anonymousClass12 = new AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                MediaRouteControllerDialog.this.finishAnimation(true);
            }
        };
        int firstVisiblePosition = this.mVolumeGroupList.getFirstVisiblePosition();
        Object obj = null;
        for (int i = 0; i < this.mVolumeGroupList.getChildCount(); i++) {
            View childAt = this.mVolumeGroupList.getChildAt(i);
            if (this.mGroupMemberRoutesAdded.contains((RouteInfo) this.mVolumeGroupAdapter.getItem(firstVisiblePosition + i))) {
                Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                alphaAnimation.setDuration((long) this.mGroupListFadeInDurationMs);
                alphaAnimation.setFillEnabled(true);
                alphaAnimation.setFillAfter(true);
                if (obj == null) {
                    alphaAnimation.setAnimationListener(anonymousClass12);
                    obj = 1;
                }
                childAt.clearAnimation();
                childAt.startAnimation(alphaAnimation);
            }
        }
    }

    void clearGroupListAnimation(boolean z) {
        int firstVisiblePosition = this.mVolumeGroupList.getFirstVisiblePosition();
        for (int i = 0; i < this.mVolumeGroupList.getChildCount(); i++) {
            View childAt = this.mVolumeGroupList.getChildAt(i);
            RouteInfo routeInfo = (RouteInfo) this.mVolumeGroupAdapter.getItem(firstVisiblePosition + i);
            if (!z || this.mGroupMemberRoutesAdded == null || !this.mGroupMemberRoutesAdded.contains(routeInfo)) {
                ((LinearLayout) childAt.findViewById(C0266R.id.volume_item_container)).setVisibility(0);
                Animation animationSet = new AnimationSet(true);
                Animation alphaAnimation = new AlphaAnimation(1.0f, 1.0f);
                alphaAnimation.setDuration(0);
                animationSet.addAnimation(alphaAnimation);
                new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f).setDuration(0);
                animationSet.setFillAfter(true);
                animationSet.setFillEnabled(true);
                childAt.clearAnimation();
                childAt.startAnimation(animationSet);
            }
        }
        this.mVolumeGroupList.stopAnimationAll();
        if (!z) {
            finishAnimation(false);
        }
    }

    private void updatePlaybackControlLayout() {
        if (canShowPlaybackControlLayout()) {
            Object obj;
            Object obj2;
            Context context;
            int i;
            ImageButton imageButton;
            CharSequence charSequence = null;
            CharSequence title = this.mDescription == null ? null : this.mDescription.getTitle();
            int i2 = 1;
            int isEmpty = TextUtils.isEmpty(title) ^ 1;
            if (this.mDescription != null) {
                charSequence = this.mDescription.getSubtitle();
            }
            int isEmpty2 = TextUtils.isEmpty(charSequence) ^ 1;
            if (this.mRoute.getPresentationDisplayId() != -1) {
                this.mTitleView.setText(C0266R.string.mr_controller_casting_screen);
            } else {
                if (this.mState != null) {
                    if (this.mState.getState() != 0) {
                        if (isEmpty == 0 && isEmpty2 == 0) {
                            this.mTitleView.setText(C0266R.string.mr_controller_no_info_available);
                        } else {
                            if (isEmpty != 0) {
                                this.mTitleView.setText(title);
                                obj = 1;
                            } else {
                                obj = null;
                            }
                            if (isEmpty2 != 0) {
                                this.mSubtitleView.setText(charSequence);
                                obj2 = 1;
                                isEmpty2 = 8;
                                this.mTitleView.setVisibility(obj != null ? 0 : 8);
                                this.mSubtitleView.setVisibility(obj2 != null ? 0 : 8);
                                if (this.mState == null) {
                                    if (this.mState.getState() != 6) {
                                        if (this.mState.getState() == 3) {
                                            obj = null;
                                            context = this.mPlaybackControlButton.getContext();
                                            if (obj == null && isPauseActionSupported()) {
                                                i = C0266R.attr.mediaRoutePauseDrawable;
                                                isEmpty = C0266R.string.mr_controller_pause;
                                            } else if (obj == null && isStopActionSupported()) {
                                                i = C0266R.attr.mediaRouteStopDrawable;
                                                isEmpty = C0266R.string.mr_controller_stop;
                                            } else if (obj == null || !isPlayActionSupported()) {
                                                i = 0;
                                                isEmpty = 0;
                                                i2 = 0;
                                            } else {
                                                i = C0266R.attr.mediaRoutePlayDrawable;
                                                isEmpty = C0266R.string.mr_controller_play;
                                            }
                                            imageButton = this.mPlaybackControlButton;
                                            if (i2 != 0) {
                                                isEmpty2 = 0;
                                            }
                                            imageButton.setVisibility(isEmpty2);
                                            if (i2 != 0) {
                                                this.mPlaybackControlButton.setImageResource(MediaRouterThemeHelper.getThemeResource(context, i));
                                                this.mPlaybackControlButton.setContentDescription(context.getResources().getText(isEmpty));
                                            }
                                        }
                                    }
                                    obj = 1;
                                    context = this.mPlaybackControlButton.getContext();
                                    if (obj == null) {
                                    }
                                    if (obj == null) {
                                    }
                                    if (obj == null) {
                                    }
                                    i = 0;
                                    isEmpty = 0;
                                    i2 = 0;
                                    imageButton = this.mPlaybackControlButton;
                                    if (i2 != 0) {
                                        isEmpty2 = 0;
                                    }
                                    imageButton.setVisibility(isEmpty2);
                                    if (i2 != 0) {
                                        this.mPlaybackControlButton.setImageResource(MediaRouterThemeHelper.getThemeResource(context, i));
                                        this.mPlaybackControlButton.setContentDescription(context.getResources().getText(isEmpty));
                                    }
                                }
                            }
                            obj2 = null;
                            isEmpty2 = 8;
                            if (obj != null) {
                            }
                            this.mTitleView.setVisibility(obj != null ? 0 : 8);
                            if (obj2 != null) {
                            }
                            this.mSubtitleView.setVisibility(obj2 != null ? 0 : 8);
                            if (this.mState == null) {
                                if (this.mState.getState() != 6) {
                                    if (this.mState.getState() == 3) {
                                        obj = null;
                                        context = this.mPlaybackControlButton.getContext();
                                        if (obj == null) {
                                        }
                                        if (obj == null) {
                                        }
                                        if (obj == null) {
                                        }
                                        i = 0;
                                        isEmpty = 0;
                                        i2 = 0;
                                        imageButton = this.mPlaybackControlButton;
                                        if (i2 != 0) {
                                            isEmpty2 = 0;
                                        }
                                        imageButton.setVisibility(isEmpty2);
                                        if (i2 != 0) {
                                            this.mPlaybackControlButton.setImageResource(MediaRouterThemeHelper.getThemeResource(context, i));
                                            this.mPlaybackControlButton.setContentDescription(context.getResources().getText(isEmpty));
                                        }
                                    }
                                }
                                obj = 1;
                                context = this.mPlaybackControlButton.getContext();
                                if (obj == null) {
                                }
                                if (obj == null) {
                                }
                                if (obj == null) {
                                }
                                i = 0;
                                isEmpty = 0;
                                i2 = 0;
                                imageButton = this.mPlaybackControlButton;
                                if (i2 != 0) {
                                    isEmpty2 = 0;
                                }
                                imageButton.setVisibility(isEmpty2);
                                if (i2 != 0) {
                                    this.mPlaybackControlButton.setImageResource(MediaRouterThemeHelper.getThemeResource(context, i));
                                    this.mPlaybackControlButton.setContentDescription(context.getResources().getText(isEmpty));
                                }
                            }
                        }
                    }
                }
                this.mTitleView.setText(C0266R.string.mr_controller_no_media_selected);
            }
            obj = 1;
            obj2 = null;
            isEmpty2 = 8;
            if (obj != null) {
            }
            this.mTitleView.setVisibility(obj != null ? 0 : 8);
            if (obj2 != null) {
            }
            this.mSubtitleView.setVisibility(obj2 != null ? 0 : 8);
            if (this.mState == null) {
                if (this.mState.getState() != 6) {
                    if (this.mState.getState() == 3) {
                        obj = null;
                        context = this.mPlaybackControlButton.getContext();
                        if (obj == null) {
                        }
                        if (obj == null) {
                        }
                        if (obj == null) {
                        }
                        i = 0;
                        isEmpty = 0;
                        i2 = 0;
                        imageButton = this.mPlaybackControlButton;
                        if (i2 != 0) {
                            isEmpty2 = 0;
                        }
                        imageButton.setVisibility(isEmpty2);
                        if (i2 != 0) {
                            this.mPlaybackControlButton.setImageResource(MediaRouterThemeHelper.getThemeResource(context, i));
                            this.mPlaybackControlButton.setContentDescription(context.getResources().getText(isEmpty));
                        }
                    }
                }
                obj = 1;
                context = this.mPlaybackControlButton.getContext();
                if (obj == null) {
                }
                if (obj == null) {
                }
                if (obj == null) {
                }
                i = 0;
                isEmpty = 0;
                i2 = 0;
                imageButton = this.mPlaybackControlButton;
                if (i2 != 0) {
                    isEmpty2 = 0;
                }
                imageButton.setVisibility(isEmpty2);
                if (i2 != 0) {
                    this.mPlaybackControlButton.setImageResource(MediaRouterThemeHelper.getThemeResource(context, i));
                    this.mPlaybackControlButton.setContentDescription(context.getResources().getText(isEmpty));
                }
            }
        }
    }

    private boolean isPlayActionSupported() {
        return (this.mState.getActions() & 516) != 0;
    }

    private boolean isPauseActionSupported() {
        return (this.mState.getActions() & 514) != 0;
    }

    private boolean isStopActionSupported() {
        return (this.mState.getActions() & 1) != 0;
    }

    boolean isVolumeControlAvailable(RouteInfo routeInfo) {
        return this.mVolumeControlEnabled && routeInfo.getVolumeHandling() == 1;
    }

    private static int getLayoutHeight(View view) {
        return view.getLayoutParams().height;
    }

    static void setLayoutHeight(View view, int i) {
        LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = i;
        view.setLayoutParams(layoutParams);
    }

    private static boolean uriEquals(Uri uri, Uri uri2) {
        if (uri == null || !uri.equals(uri2)) {
            return (uri == null && uri2 == null) ? true : null;
        } else {
            return true;
        }
    }

    int getDesiredArtHeight(int i, int i2) {
        if (i >= i2) {
            return (int) (((((float) this.mDialogContentWidth) * ((float) i2)) / ((float) i)) + 0.5f);
        }
        return (int) (((((float) this.mDialogContentWidth) * 1091567616) / 1098907648) + 1056964608);
    }

    void updateArtIconIfNeeded() {
        if (this.mCustomControlView == null) {
            if (isIconChanged()) {
                if (this.mFetchArtTask != null) {
                    this.mFetchArtTask.cancel(true);
                }
                this.mFetchArtTask = new FetchArtTask();
                this.mFetchArtTask.execute(new Void[0]);
            }
        }
    }

    void clearLoadedBitmap() {
        this.mArtIconIsLoaded = false;
        this.mArtIconLoadedBitmap = null;
        this.mArtIconBackgroundColor = 0;
    }

    private boolean isIconChanged() {
        Uri uri = null;
        Bitmap iconBitmap = this.mDescription == null ? null : this.mDescription.getIconBitmap();
        if (this.mDescription != null) {
            uri = this.mDescription.getIconUri();
        }
        Bitmap iconBitmap2 = this.mFetchArtTask == null ? this.mArtIconBitmap : this.mFetchArtTask.getIconBitmap();
        Uri iconUri = this.mFetchArtTask == null ? this.mArtIconUri : this.mFetchArtTask.getIconUri();
        if (iconBitmap2 != iconBitmap) {
            return true;
        }
        if (iconBitmap2 != null || uriEquals(iconUri, r1)) {
            return false;
        }
        return true;
    }
}
