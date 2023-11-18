package com.google.android.exoplayer2.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.google.android.exoplayer2.ControlDispatcher;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.DefaultEventListener;
import com.google.android.exoplayer2.Player.TextComponent;
import com.google.android.exoplayer2.Player.VideoComponent;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.Metadata.Entry;
import com.google.android.exoplayer2.metadata.id3.ApicFrame;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerControlView.VisibilityListener;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import java.util.List;

public class PlayerView extends FrameLayout {
    private static final int SURFACE_TYPE_NONE = 0;
    private static final int SURFACE_TYPE_SURFACE_VIEW = 1;
    private static final int SURFACE_TYPE_TEXTURE_VIEW = 2;
    private final ImageView artworkView;
    private final ComponentListener componentListener;
    private final AspectRatioFrameLayout contentFrame;
    private final PlayerControlView controller;
    private boolean controllerAutoShow;
    private boolean controllerHideDuringAds;
    private boolean controllerHideOnTouch;
    private int controllerShowTimeoutMs;
    private Bitmap defaultArtwork;
    private final FrameLayout overlayFrameLayout;
    private Player player;
    private final View shutterView;
    private final SubtitleView subtitleView;
    private final View surfaceView;
    private int textureViewRotation;
    private boolean useArtwork;
    private boolean useController;

    private final class ComponentListener extends DefaultEventListener implements TextOutput, VideoListener, OnLayoutChangeListener {
        private ComponentListener() {
        }

        public void onCues(List<Cue> list) {
            if (PlayerView.this.subtitleView != null) {
                PlayerView.this.subtitleView.onCues(list);
            }
        }

        public void onVideoSizeChanged(int i, int i2, int i3, float f) {
            if (PlayerView.this.contentFrame != null) {
                if (i2 != 0) {
                    if (i != 0) {
                        i = (((float) i) * f) / ((float) i2);
                        if ((PlayerView.this.surfaceView instanceof TextureView) != 0) {
                            if (i3 == 90 || i3 == 270) {
                                i = 1.0f / i;
                            }
                            if (PlayerView.this.textureViewRotation != 0) {
                                PlayerView.this.surfaceView.removeOnLayoutChangeListener(this);
                            }
                            PlayerView.this.textureViewRotation = i3;
                            if (PlayerView.this.textureViewRotation != 0) {
                                PlayerView.this.surfaceView.addOnLayoutChangeListener(this);
                            }
                            PlayerView.applyTextureViewRotation((TextureView) PlayerView.this.surfaceView, PlayerView.this.textureViewRotation);
                        }
                        PlayerView.this.contentFrame.setAspectRatio(i);
                    }
                }
                i = 1065353216;
                if ((PlayerView.this.surfaceView instanceof TextureView) != 0) {
                    i = 1.0f / i;
                    if (PlayerView.this.textureViewRotation != 0) {
                        PlayerView.this.surfaceView.removeOnLayoutChangeListener(this);
                    }
                    PlayerView.this.textureViewRotation = i3;
                    if (PlayerView.this.textureViewRotation != 0) {
                        PlayerView.this.surfaceView.addOnLayoutChangeListener(this);
                    }
                    PlayerView.applyTextureViewRotation((TextureView) PlayerView.this.surfaceView, PlayerView.this.textureViewRotation);
                }
                PlayerView.this.contentFrame.setAspectRatio(i);
            }
        }

        public void onRenderedFirstFrame() {
            if (PlayerView.this.shutterView != null) {
                PlayerView.this.shutterView.setVisibility(4);
            }
        }

        public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
            PlayerView.this.updateForCurrentTrackSelections();
        }

        public void onPlayerStateChanged(boolean z, int i) {
            if (PlayerView.this.isPlayingAd() && PlayerView.this.controllerHideDuringAds) {
                PlayerView.this.hideController();
            } else {
                PlayerView.this.maybeShowController(0);
            }
        }

        public void onPositionDiscontinuity(int i) {
            if (PlayerView.this.isPlayingAd() != 0 && PlayerView.this.controllerHideDuringAds != 0) {
                PlayerView.this.hideController();
            }
        }

        public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            PlayerView.applyTextureViewRotation((TextureView) view, PlayerView.this.textureViewRotation);
        }
    }

    @SuppressLint({"InlinedApi"})
    private boolean isDpadKey(int i) {
        if (!(i == 19 || i == 270 || i == 22 || i == 271 || i == 20 || i == 269 || i == 21 || i == 268)) {
            if (i != 23) {
                return false;
            }
        }
        return true;
    }

    public PlayerView(Context context) {
        this(context, null);
    }

    public PlayerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PlayerView(Context context, AttributeSet attributeSet, int i) {
        ViewGroup viewGroup = this;
        Context context2 = context;
        AttributeSet attributeSet2 = attributeSet;
        super(context, attributeSet, i);
        if (isInEditMode()) {
            viewGroup.contentFrame = null;
            viewGroup.shutterView = null;
            viewGroup.surfaceView = null;
            viewGroup.artworkView = null;
            viewGroup.subtitleView = null;
            viewGroup.controller = null;
            viewGroup.componentListener = null;
            viewGroup.overlayFrameLayout = null;
            View imageView = new ImageView(context2);
            if (Util.SDK_INT >= 23) {
                configureEditModeLogoV23(getResources(), imageView);
            } else {
                configureEditModeLogo(getResources(), imageView);
            }
            addView(imageView);
            return;
        }
        boolean hasValue;
        int color;
        boolean z;
        int resourceId;
        boolean z2;
        int i2;
        int i3;
        boolean z3;
        boolean z4;
        boolean z5;
        int i4;
        boolean z6;
        int i5 = C0762R.layout.exo_player_view;
        if (attributeSet2 != null) {
            TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet2, C0762R.styleable.PlayerView, 0, 0);
            try {
                hasValue = obtainStyledAttributes.hasValue(C0762R.styleable.PlayerView_shutter_background_color);
                color = obtainStyledAttributes.getColor(C0762R.styleable.PlayerView_shutter_background_color, 0);
                i5 = obtainStyledAttributes.getResourceId(C0762R.styleable.PlayerView_player_layout_id, i5);
                z = obtainStyledAttributes.getBoolean(C0762R.styleable.PlayerView_use_artwork, true);
                resourceId = obtainStyledAttributes.getResourceId(C0762R.styleable.PlayerView_default_artwork, 0);
                z2 = obtainStyledAttributes.getBoolean(C0762R.styleable.PlayerView_use_controller, true);
                i2 = obtainStyledAttributes.getInt(C0762R.styleable.PlayerView_surface_type, 1);
                i3 = obtainStyledAttributes.getInt(C0762R.styleable.PlayerView_resize_mode, 0);
                int i6 = obtainStyledAttributes.getInt(C0762R.styleable.PlayerView_show_timeout, 5000);
                boolean z7 = obtainStyledAttributes.getBoolean(C0762R.styleable.PlayerView_hide_on_touch, true);
                int i7 = i5;
                boolean z8 = obtainStyledAttributes.getBoolean(C0762R.styleable.PlayerView_auto_show, true);
                z3 = obtainStyledAttributes.getBoolean(C0762R.styleable.PlayerView_hide_during_ads, true);
                obtainStyledAttributes.recycle();
                z4 = z8;
                boolean z9 = z2;
                z2 = z3;
                i5 = i7;
                z5 = z9;
                boolean z10 = z7;
                i4 = i6;
                z6 = z10;
            } catch (Throwable th) {
                Throwable th2 = th;
                obtainStyledAttributes.recycle();
            }
        } else {
            i3 = 0;
            z6 = true;
            i4 = 5000;
            z4 = true;
            hasValue = false;
            color = 0;
            z = true;
            resourceId = 0;
            z2 = true;
            i2 = 1;
            z5 = true;
        }
        LayoutInflater.from(context).inflate(i5, viewGroup);
        viewGroup.componentListener = new ComponentListener();
        setDescendantFocusability(262144);
        viewGroup.contentFrame = (AspectRatioFrameLayout) findViewById(C0762R.id.exo_content_frame);
        if (viewGroup.contentFrame != null) {
            setResizeModeRaw(viewGroup.contentFrame, i3);
        }
        viewGroup.shutterView = findViewById(C0762R.id.exo_shutter);
        if (viewGroup.shutterView != null && r10) {
            viewGroup.shutterView.setBackgroundColor(color);
        }
        if (viewGroup.contentFrame == null || i2 == 0) {
            viewGroup.surfaceView = null;
        } else {
            LayoutParams layoutParams = new LayoutParams(-1, -1);
            viewGroup.surfaceView = i2 == 2 ? new TextureView(context2) : new SurfaceView(context2);
            viewGroup.surfaceView.setLayoutParams(layoutParams);
            viewGroup.contentFrame.addView(viewGroup.surfaceView, 0);
        }
        viewGroup.overlayFrameLayout = (FrameLayout) findViewById(C0762R.id.exo_overlay);
        viewGroup.artworkView = (ImageView) findViewById(C0762R.id.exo_artwork);
        z3 = z && viewGroup.artworkView != null;
        viewGroup.useArtwork = z3;
        if (resourceId != 0) {
            viewGroup.defaultArtwork = BitmapFactory.decodeResource(context.getResources(), resourceId);
        }
        viewGroup.subtitleView = (SubtitleView) findViewById(C0762R.id.exo_subtitles);
        if (viewGroup.subtitleView != null) {
            viewGroup.subtitleView.setUserDefaultStyle();
            viewGroup.subtitleView.setUserDefaultTextSize();
        }
        PlayerControlView playerControlView = (PlayerControlView) findViewById(C0762R.id.exo_controller);
        View findViewById = findViewById(C0762R.id.exo_controller_placeholder);
        if (playerControlView != null) {
            viewGroup.controller = playerControlView;
            hasValue = false;
        } else if (findViewById != null) {
            hasValue = false;
            viewGroup.controller = new PlayerControlView(context2, null, 0, attributeSet2);
            viewGroup.controller.setLayoutParams(findViewById.getLayoutParams());
            ViewGroup viewGroup2 = (ViewGroup) findViewById.getParent();
            int indexOfChild = viewGroup2.indexOfChild(findViewById);
            viewGroup2.removeView(findViewById);
            viewGroup2.addView(viewGroup.controller, indexOfChild);
        } else {
            hasValue = false;
            viewGroup.controller = null;
        }
        if (viewGroup.controller == null) {
            i4 = 0;
        }
        viewGroup.controllerShowTimeoutMs = i4;
        viewGroup.controllerHideOnTouch = z6;
        viewGroup.controllerAutoShow = z4;
        viewGroup.controllerHideDuringAds = z2;
        if (z5 && viewGroup.controller != null) {
            hasValue = true;
        }
        viewGroup.useController = hasValue;
        hideController();
    }

    public static void switchTargetView(@NonNull Player player, @Nullable PlayerView playerView, @Nullable PlayerView playerView2) {
        if (playerView != playerView2) {
            if (playerView2 != null) {
                playerView2.setPlayer(player);
            }
            if (playerView != null) {
                playerView.setPlayer(null);
            }
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        if (this.player != player) {
            VideoComponent videoComponent;
            TextComponent textComponent;
            if (this.player != null) {
                this.player.removeListener(this.componentListener);
                videoComponent = this.player.getVideoComponent();
                if (videoComponent != null) {
                    videoComponent.removeVideoListener(this.componentListener);
                    if (this.surfaceView instanceof TextureView) {
                        videoComponent.clearVideoTextureView((TextureView) this.surfaceView);
                    } else if (this.surfaceView instanceof SurfaceView) {
                        videoComponent.clearVideoSurfaceView((SurfaceView) this.surfaceView);
                    }
                }
                textComponent = this.player.getTextComponent();
                if (textComponent != null) {
                    textComponent.removeTextOutput(this.componentListener);
                }
            }
            this.player = player;
            if (this.useController) {
                this.controller.setPlayer(player);
            }
            if (this.shutterView != null) {
                this.shutterView.setVisibility(0);
            }
            if (this.subtitleView != null) {
                this.subtitleView.setCues(null);
            }
            if (player != null) {
                videoComponent = player.getVideoComponent();
                if (videoComponent != null) {
                    if (this.surfaceView instanceof TextureView) {
                        videoComponent.setVideoTextureView((TextureView) this.surfaceView);
                    } else if (this.surfaceView instanceof SurfaceView) {
                        videoComponent.setVideoSurfaceView((SurfaceView) this.surfaceView);
                    }
                    videoComponent.addVideoListener(this.componentListener);
                }
                textComponent = player.getTextComponent();
                if (textComponent != null) {
                    textComponent.addTextOutput(this.componentListener);
                }
                player.addListener(this.componentListener);
                maybeShowController(false);
                updateForCurrentTrackSelections();
            } else {
                hideController();
                hideArtwork();
            }
        }
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        if (this.surfaceView instanceof SurfaceView) {
            this.surfaceView.setVisibility(i);
        }
    }

    public void setResizeMode(int i) {
        Assertions.checkState(this.contentFrame != null);
        this.contentFrame.setResizeMode(i);
    }

    public boolean getUseArtwork() {
        return this.useArtwork;
    }

    public void setUseArtwork(boolean z) {
        boolean z2;
        if (z) {
            if (this.artworkView == null) {
                z2 = false;
                Assertions.checkState(z2);
                if (this.useArtwork != z) {
                    this.useArtwork = z;
                    updateForCurrentTrackSelections();
                }
            }
        }
        z2 = true;
        Assertions.checkState(z2);
        if (this.useArtwork != z) {
            this.useArtwork = z;
            updateForCurrentTrackSelections();
        }
    }

    public Bitmap getDefaultArtwork() {
        return this.defaultArtwork;
    }

    public void setDefaultArtwork(Bitmap bitmap) {
        if (this.defaultArtwork != bitmap) {
            this.defaultArtwork = bitmap;
            updateForCurrentTrackSelections();
        }
    }

    public boolean getUseController() {
        return this.useController;
    }

    public void setUseController(boolean z) {
        boolean z2;
        if (z) {
            if (this.controller == null) {
                z2 = false;
                Assertions.checkState(z2);
                if (this.useController == z) {
                    this.useController = z;
                    if (z) {
                        this.controller.setPlayer(this.player);
                    } else if (this.controller) {
                        this.controller.hide();
                        this.controller.setPlayer(null);
                    }
                }
            }
        }
        z2 = true;
        Assertions.checkState(z2);
        if (this.useController == z) {
            this.useController = z;
            if (z) {
                this.controller.setPlayer(this.player);
            } else if (this.controller) {
                this.controller.hide();
                this.controller.setPlayer(null);
            }
        }
    }

    public void setShutterBackgroundColor(int i) {
        if (this.shutterView != null) {
            this.shutterView.setBackgroundColor(i);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (this.player == null || !this.player.isPlayingAd()) {
            boolean z = false;
            Object obj = (isDpadKey(keyEvent.getKeyCode()) && this.useController && !this.controller.isVisible()) ? 1 : null;
            maybeShowController(true);
            if (!(obj == null && !dispatchMediaKeyEvent(keyEvent) && super.dispatchKeyEvent(keyEvent) == null)) {
                z = true;
            }
            return z;
        }
        this.overlayFrameLayout.requestFocus();
        return super.dispatchKeyEvent(keyEvent);
    }

    public boolean dispatchMediaKeyEvent(KeyEvent keyEvent) {
        return (!this.useController || this.controller.dispatchMediaKeyEvent(keyEvent) == null) ? null : true;
    }

    public void showController() {
        showController(shouldShowControllerIndefinitely());
    }

    public void hideController() {
        if (this.controller != null) {
            this.controller.hide();
        }
    }

    public int getControllerShowTimeoutMs() {
        return this.controllerShowTimeoutMs;
    }

    public void setControllerShowTimeoutMs(int i) {
        Assertions.checkState(this.controller != null);
        this.controllerShowTimeoutMs = i;
        if (this.controller.isVisible() != 0) {
            showController();
        }
    }

    public boolean getControllerHideOnTouch() {
        return this.controllerHideOnTouch;
    }

    public void setControllerHideOnTouch(boolean z) {
        Assertions.checkState(this.controller != null);
        this.controllerHideOnTouch = z;
    }

    public boolean getControllerAutoShow() {
        return this.controllerAutoShow;
    }

    public void setControllerAutoShow(boolean z) {
        this.controllerAutoShow = z;
    }

    public void setControllerHideDuringAds(boolean z) {
        this.controllerHideDuringAds = z;
    }

    public void setControllerVisibilityListener(VisibilityListener visibilityListener) {
        Assertions.checkState(this.controller != null);
        this.controller.setVisibilityListener(visibilityListener);
    }

    public void setPlaybackPreparer(@Nullable PlaybackPreparer playbackPreparer) {
        Assertions.checkState(this.controller != null);
        this.controller.setPlaybackPreparer(playbackPreparer);
    }

    public void setControlDispatcher(@Nullable ControlDispatcher controlDispatcher) {
        Assertions.checkState(this.controller != null);
        this.controller.setControlDispatcher(controlDispatcher);
    }

    public void setRewindIncrementMs(int i) {
        Assertions.checkState(this.controller != null);
        this.controller.setRewindIncrementMs(i);
    }

    public void setFastForwardIncrementMs(int i) {
        Assertions.checkState(this.controller != null);
        this.controller.setFastForwardIncrementMs(i);
    }

    public void setRepeatToggleModes(int i) {
        Assertions.checkState(this.controller != null);
        this.controller.setRepeatToggleModes(i);
    }

    public void setShowShuffleButton(boolean z) {
        Assertions.checkState(this.controller != null);
        this.controller.setShowShuffleButton(z);
    }

    public void setShowMultiWindowTimeBar(boolean z) {
        Assertions.checkState(this.controller != null);
        this.controller.setShowMultiWindowTimeBar(z);
    }

    public View getVideoSurfaceView() {
        return this.surfaceView;
    }

    public FrameLayout getOverlayFrameLayout() {
        return this.overlayFrameLayout;
    }

    public SubtitleView getSubtitleView() {
        return this.subtitleView;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.useController && this.player != null) {
            if (motionEvent.getActionMasked() == null) {
                if (this.controller.isVisible() == null) {
                    maybeShowController(true);
                } else if (this.controllerHideOnTouch != null) {
                    this.controller.hide();
                }
                return true;
            }
        }
        return null;
    }

    public boolean onTrackballEvent(MotionEvent motionEvent) {
        if (this.useController != null) {
            if (this.player != null) {
                maybeShowController(true);
                return true;
            }
        }
        return null;
    }

    private void maybeShowController(boolean z) {
        if (!(isPlayingAd() && this.controllerHideDuringAds) && this.useController) {
            Object obj = (!this.controller.isVisible() || this.controller.getShowTimeoutMs() > 0) ? null : 1;
            boolean shouldShowControllerIndefinitely = shouldShowControllerIndefinitely();
            if (z || obj != null || shouldShowControllerIndefinitely) {
                showController(shouldShowControllerIndefinitely);
            }
        }
    }

    private boolean shouldShowControllerIndefinitely() {
        boolean z = true;
        if (this.player == null) {
            return true;
        }
        int playbackState = this.player.getPlaybackState();
        if (this.controllerAutoShow) {
            if (!(playbackState == 1 || playbackState == 4)) {
                if (!this.player.getPlayWhenReady()) {
                }
            }
            return z;
        }
        z = false;
        return z;
    }

    private void showController(boolean z) {
        if (this.useController) {
            this.controller.setShowTimeoutMs(z ? false : this.controllerShowTimeoutMs);
            this.controller.show();
        }
    }

    private boolean isPlayingAd() {
        return this.player != null && this.player.isPlayingAd() && this.player.getPlayWhenReady();
    }

    private void updateForCurrentTrackSelections() {
        if (this.player != null) {
            TrackSelectionArray currentTrackSelections = this.player.getCurrentTrackSelections();
            int i = 0;
            while (i < currentTrackSelections.length) {
                if (this.player.getRendererType(i) != 2 || currentTrackSelections.get(i) == null) {
                    i++;
                } else {
                    hideArtwork();
                    return;
                }
            }
            if (this.shutterView != null) {
                this.shutterView.setVisibility(0);
            }
            if (this.useArtwork) {
                for (i = 0; i < currentTrackSelections.length; i++) {
                    TrackSelection trackSelection = currentTrackSelections.get(i);
                    if (trackSelection != null) {
                        int i2 = 0;
                        while (i2 < trackSelection.length()) {
                            Metadata metadata = trackSelection.getFormat(i2).metadata;
                            if (metadata == null || !setArtworkFromMetadata(metadata)) {
                                i2++;
                            } else {
                                return;
                            }
                        }
                        continue;
                    }
                }
                if (setArtworkFromBitmap(this.defaultArtwork)) {
                    return;
                }
            }
            hideArtwork();
        }
    }

    private boolean setArtworkFromMetadata(Metadata metadata) {
        for (int i = 0; i < metadata.length(); i++) {
            Entry entry = metadata.get(i);
            if (entry instanceof ApicFrame) {
                metadata = ((ApicFrame) entry).pictureData;
                return setArtworkFromBitmap(BitmapFactory.decodeByteArray(metadata, 0, metadata.length));
            }
        }
        return false;
    }

    private boolean setArtworkFromBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if (width > 0 && height > 0) {
                if (this.contentFrame != null) {
                    this.contentFrame.setAspectRatio(((float) width) / ((float) height));
                }
                this.artworkView.setImageBitmap(bitmap);
                this.artworkView.setVisibility(0);
                return true;
            }
        }
        return false;
    }

    private void hideArtwork() {
        if (this.artworkView != null) {
            this.artworkView.setImageResource(17170445);
            this.artworkView.setVisibility(4);
        }
    }

    @TargetApi(23)
    private static void configureEditModeLogoV23(Resources resources, ImageView imageView) {
        imageView.setImageDrawable(resources.getDrawable(C0762R.drawable.exo_edit_mode_logo, null));
        imageView.setBackgroundColor(resources.getColor(C0762R.color.exo_edit_mode_background_color, null));
    }

    private static void configureEditModeLogo(Resources resources, ImageView imageView) {
        imageView.setImageDrawable(resources.getDrawable(C0762R.drawable.exo_edit_mode_logo));
        imageView.setBackgroundColor(resources.getColor(C0762R.color.exo_edit_mode_background_color));
    }

    private static void setResizeModeRaw(AspectRatioFrameLayout aspectRatioFrameLayout, int i) {
        aspectRatioFrameLayout.setResizeMode(i);
    }

    private static void applyTextureViewRotation(TextureView textureView, int i) {
        float width = (float) textureView.getWidth();
        float height = (float) textureView.getHeight();
        if (!(width == 0.0f || height == 0.0f)) {
            if (i != 0) {
                Matrix matrix = new Matrix();
                float f = width / 2.0f;
                float f2 = height / 2.0f;
                matrix.postRotate((float) i, f, f2);
                i = new RectF(0.0f, 0.0f, width, height);
                RectF rectF = new RectF();
                matrix.mapRect(rectF, i);
                matrix.postScale(width / rectF.width(), height / rectF.height(), f, f2);
                textureView.setTransform(matrix);
                return;
            }
        }
        textureView.setTransform(0);
    }
}
