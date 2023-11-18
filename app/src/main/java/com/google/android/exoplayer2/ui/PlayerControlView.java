package com.google.android.exoplayer2.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.ControlDispatcher;
import com.google.android.exoplayer2.DefaultControlDispatcher;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.DefaultEventListener;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.Timeline.Period;
import com.google.android.exoplayer2.Timeline.Window;
import com.google.android.exoplayer2.ui.TimeBar.OnScrubListener;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.RepeatModeUtil;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Locale;
import org.mozilla.universalchardet.prober.distributionanalysis.Big5DistributionAnalysis;

public class PlayerControlView extends FrameLayout {
    public static final int DEFAULT_FAST_FORWARD_MS = 15000;
    public static final int DEFAULT_REPEAT_TOGGLE_MODES = 0;
    public static final int DEFAULT_REWIND_MS = 5000;
    public static final int DEFAULT_SHOW_TIMEOUT_MS = 5000;
    private static final long MAX_POSITION_FOR_SEEK_TO_PREVIOUS = 3000;
    public static final int MAX_WINDOWS_FOR_MULTI_WINDOW_TIME_BAR = 100;
    private long[] adGroupTimesMs;
    private final ComponentListener componentListener;
    private ControlDispatcher controlDispatcher;
    private final TextView durationView;
    private long[] extraAdGroupTimesMs;
    private boolean[] extraPlayedAdGroups;
    private final View fastForwardButton;
    private int fastForwardMs;
    private final StringBuilder formatBuilder;
    private final Formatter formatter;
    private final Runnable hideAction;
    private long hideAtMs;
    private boolean isAttachedToWindow;
    private boolean multiWindowTimeBar;
    private final View nextButton;
    private final View pauseButton;
    private final Period period;
    private final View playButton;
    @Nullable
    private PlaybackPreparer playbackPreparer;
    private boolean[] playedAdGroups;
    private Player player;
    private final TextView positionView;
    private final View previousButton;
    private final String repeatAllButtonContentDescription;
    private final Drawable repeatAllButtonDrawable;
    private final String repeatOffButtonContentDescription;
    private final Drawable repeatOffButtonDrawable;
    private final String repeatOneButtonContentDescription;
    private final Drawable repeatOneButtonDrawable;
    private final ImageView repeatToggleButton;
    private int repeatToggleModes;
    private final View rewindButton;
    private int rewindMs;
    private boolean scrubbing;
    private boolean showMultiWindowTimeBar;
    private boolean showShuffleButton;
    private int showTimeoutMs;
    private final View shuffleButton;
    private final TimeBar timeBar;
    private final Runnable updateProgressAction;
    private VisibilityListener visibilityListener;
    private final Window window;

    public interface VisibilityListener {
        void onVisibilityChange(int i);
    }

    /* renamed from: com.google.android.exoplayer2.ui.PlayerControlView$1 */
    class C07591 implements Runnable {
        C07591() {
        }

        public void run() {
            PlayerControlView.this.updateProgress();
        }
    }

    /* renamed from: com.google.android.exoplayer2.ui.PlayerControlView$2 */
    class C07602 implements Runnable {
        C07602() {
        }

        public void run() {
            PlayerControlView.this.hide();
        }
    }

    private final class ComponentListener extends DefaultEventListener implements OnScrubListener, OnClickListener {
        private ComponentListener() {
        }

        public void onScrubStart(TimeBar timeBar, long j) {
            PlayerControlView.this.removeCallbacks(PlayerControlView.this.hideAction);
            PlayerControlView.this.scrubbing = 1;
        }

        public void onScrubMove(TimeBar timeBar, long j) {
            if (PlayerControlView.this.positionView != null) {
                PlayerControlView.this.positionView.setText(Util.getStringForTime(PlayerControlView.this.formatBuilder, PlayerControlView.this.formatter, j));
            }
        }

        public void onScrubStop(TimeBar timeBar, long j, boolean z) {
            PlayerControlView.this.scrubbing = false;
            if (!(z || PlayerControlView.this.player == null)) {
                PlayerControlView.this.seekToTimeBarPosition(j);
            }
            PlayerControlView.this.hideAfterTimeout();
        }

        public void onPlayerStateChanged(boolean z, int i) {
            PlayerControlView.this.updatePlayPauseButton();
            PlayerControlView.this.updateProgress();
        }

        public void onRepeatModeChanged(int i) {
            PlayerControlView.this.updateRepeatModeButton();
            PlayerControlView.this.updateNavigation();
        }

        public void onShuffleModeEnabledChanged(boolean z) {
            PlayerControlView.this.updateShuffleButton();
            PlayerControlView.this.updateNavigation();
        }

        public void onPositionDiscontinuity(int i) {
            PlayerControlView.this.updateNavigation();
            PlayerControlView.this.updateProgress();
        }

        public void onTimelineChanged(Timeline timeline, Object obj, int i) {
            PlayerControlView.this.updateNavigation();
            PlayerControlView.this.updateTimeBarMode();
            PlayerControlView.this.updateProgress();
        }

        public void onClick(View view) {
            if (PlayerControlView.this.player != null) {
                if (PlayerControlView.this.nextButton == view) {
                    PlayerControlView.this.next();
                } else if (PlayerControlView.this.previousButton == view) {
                    PlayerControlView.this.previous();
                } else if (PlayerControlView.this.fastForwardButton == view) {
                    PlayerControlView.this.fastForward();
                } else if (PlayerControlView.this.rewindButton == view) {
                    PlayerControlView.this.rewind();
                } else if (PlayerControlView.this.playButton == view) {
                    if (PlayerControlView.this.player.getPlaybackState() == 1) {
                        if (PlayerControlView.this.playbackPreparer != null) {
                            PlayerControlView.this.playbackPreparer.preparePlayback();
                        }
                    } else if (PlayerControlView.this.player.getPlaybackState() == 4) {
                        PlayerControlView.this.controlDispatcher.dispatchSeekTo(PlayerControlView.this.player, PlayerControlView.this.player.getCurrentWindowIndex(), C0649C.TIME_UNSET);
                    }
                    PlayerControlView.this.controlDispatcher.dispatchSetPlayWhenReady(PlayerControlView.this.player, true);
                } else if (PlayerControlView.this.pauseButton == view) {
                    PlayerControlView.this.controlDispatcher.dispatchSetPlayWhenReady(PlayerControlView.this.player, false);
                } else if (PlayerControlView.this.repeatToggleButton == view) {
                    PlayerControlView.this.controlDispatcher.dispatchSetRepeatMode(PlayerControlView.this.player, RepeatModeUtil.getNextRepeatMode(PlayerControlView.this.player.getRepeatMode(), PlayerControlView.this.repeatToggleModes));
                } else if (PlayerControlView.this.shuffleButton == view) {
                    PlayerControlView.this.controlDispatcher.dispatchSetShuffleModeEnabled(PlayerControlView.this.player, true ^ PlayerControlView.this.player.getShuffleModeEnabled());
                }
            }
            PlayerControlView.this.hideAfterTimeout();
        }
    }

    @SuppressLint({"InlinedApi"})
    private static boolean isHandledMediaKey(int i) {
        if (!(i == 90 || i == 89 || i == 85 || i == Big5DistributionAnalysis.LOWBYTE_END_1 || i == 127 || i == 87)) {
            if (i != 88) {
                return false;
            }
        }
        return true;
    }

    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.ui");
    }

    public PlayerControlView(Context context) {
        this(context, null);
    }

    public PlayerControlView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PlayerControlView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, attributeSet);
    }

    public PlayerControlView(Context context, AttributeSet attributeSet, int i, AttributeSet attributeSet2) {
        super(context, attributeSet, i);
        this.updateProgressAction = new C07591();
        this.hideAction = new C07602();
        attributeSet = C0762R.layout.exo_player_control_view;
        this.rewindMs = 5000;
        this.fastForwardMs = 15000;
        this.showTimeoutMs = 5000;
        this.repeatToggleModes = 0;
        this.hideAtMs = C0649C.TIME_UNSET;
        this.showShuffleButton = false;
        if (attributeSet2 != null) {
            attributeSet2 = context.getTheme().obtainStyledAttributes(attributeSet2, C0762R.styleable.PlayerControlView, 0, 0);
            try {
                this.rewindMs = attributeSet2.getInt(C0762R.styleable.PlayerControlView_rewind_increment, this.rewindMs);
                this.fastForwardMs = attributeSet2.getInt(C0762R.styleable.PlayerControlView_fastforward_increment, this.fastForwardMs);
                this.showTimeoutMs = attributeSet2.getInt(C0762R.styleable.PlayerControlView_show_timeout, this.showTimeoutMs);
                attributeSet = attributeSet2.getResourceId(C0762R.styleable.PlayerControlView_controller_layout_id, attributeSet);
                this.repeatToggleModes = getRepeatToggleModes(attributeSet2, this.repeatToggleModes);
                this.showShuffleButton = attributeSet2.getBoolean(C0762R.styleable.PlayerControlView_show_shuffle_button, this.showShuffleButton);
            } finally {
                attributeSet2.recycle();
            }
        }
        this.period = new Period();
        this.window = new Window();
        this.formatBuilder = new StringBuilder();
        this.formatter = new Formatter(this.formatBuilder, Locale.getDefault());
        this.adGroupTimesMs = new long[0];
        this.playedAdGroups = new boolean[0];
        this.extraAdGroupTimesMs = new long[0];
        this.extraPlayedAdGroups = new boolean[0];
        this.componentListener = new ComponentListener();
        this.controlDispatcher = new DefaultControlDispatcher();
        LayoutInflater.from(context).inflate(attributeSet, this);
        setDescendantFocusability(262144);
        this.durationView = (TextView) findViewById(C0762R.id.exo_duration);
        this.positionView = (TextView) findViewById(C0762R.id.exo_position);
        this.timeBar = (TimeBar) findViewById(C0762R.id.exo_progress);
        if (this.timeBar != null) {
            this.timeBar.addListener(this.componentListener);
        }
        this.playButton = findViewById(C0762R.id.exo_play);
        if (this.playButton != null) {
            this.playButton.setOnClickListener(this.componentListener);
        }
        this.pauseButton = findViewById(C0762R.id.exo_pause);
        if (this.pauseButton != null) {
            this.pauseButton.setOnClickListener(this.componentListener);
        }
        this.previousButton = findViewById(C0762R.id.exo_prev);
        if (this.previousButton != null) {
            this.previousButton.setOnClickListener(this.componentListener);
        }
        this.nextButton = findViewById(C0762R.id.exo_next);
        if (this.nextButton != null) {
            this.nextButton.setOnClickListener(this.componentListener);
        }
        this.rewindButton = findViewById(C0762R.id.exo_rew);
        if (this.rewindButton != null) {
            this.rewindButton.setOnClickListener(this.componentListener);
        }
        this.fastForwardButton = findViewById(C0762R.id.exo_ffwd);
        if (this.fastForwardButton != null) {
            this.fastForwardButton.setOnClickListener(this.componentListener);
        }
        this.repeatToggleButton = (ImageView) findViewById(C0762R.id.exo_repeat_toggle);
        if (this.repeatToggleButton != null) {
            this.repeatToggleButton.setOnClickListener(this.componentListener);
        }
        this.shuffleButton = findViewById(C0762R.id.exo_shuffle);
        if (this.shuffleButton != null) {
            this.shuffleButton.setOnClickListener(this.componentListener);
        }
        context = context.getResources();
        this.repeatOffButtonDrawable = context.getDrawable(C0762R.drawable.exo_controls_repeat_off);
        this.repeatOneButtonDrawable = context.getDrawable(C0762R.drawable.exo_controls_repeat_one);
        this.repeatAllButtonDrawable = context.getDrawable(C0762R.drawable.exo_controls_repeat_all);
        this.repeatOffButtonContentDescription = context.getString(C0762R.string.exo_controls_repeat_off_description);
        this.repeatOneButtonContentDescription = context.getString(C0762R.string.exo_controls_repeat_one_description);
        this.repeatAllButtonContentDescription = context.getString(C0762R.string.exo_controls_repeat_all_description);
    }

    private static int getRepeatToggleModes(TypedArray typedArray, int i) {
        return typedArray.getInt(C0762R.styleable.PlayerControlView_repeat_toggle_modes, i);
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        if (this.player != player) {
            if (this.player != null) {
                this.player.removeListener(this.componentListener);
            }
            this.player = player;
            if (player != null) {
                player.addListener(this.componentListener);
            }
            updateAll();
        }
    }

    public void setShowMultiWindowTimeBar(boolean z) {
        this.showMultiWindowTimeBar = z;
        updateTimeBarMode();
    }

    public void setExtraAdGroupMarkers(@Nullable long[] jArr, @Nullable boolean[] zArr) {
        boolean z = false;
        if (jArr == null) {
            this.extraAdGroupTimesMs = new long[0];
            this.extraPlayedAdGroups = new boolean[0];
        } else {
            if (jArr.length == zArr.length) {
                z = true;
            }
            Assertions.checkArgument(z);
            this.extraAdGroupTimesMs = jArr;
            this.extraPlayedAdGroups = zArr;
        }
        updateProgress();
    }

    public void setVisibilityListener(VisibilityListener visibilityListener) {
        this.visibilityListener = visibilityListener;
    }

    public void setPlaybackPreparer(@Nullable PlaybackPreparer playbackPreparer) {
        this.playbackPreparer = playbackPreparer;
    }

    public void setControlDispatcher(@Nullable ControlDispatcher controlDispatcher) {
        if (controlDispatcher == null) {
            controlDispatcher = new DefaultControlDispatcher();
        }
        this.controlDispatcher = controlDispatcher;
    }

    public void setRewindIncrementMs(int i) {
        this.rewindMs = i;
        updateNavigation();
    }

    public void setFastForwardIncrementMs(int i) {
        this.fastForwardMs = i;
        updateNavigation();
    }

    public int getShowTimeoutMs() {
        return this.showTimeoutMs;
    }

    public void setShowTimeoutMs(int i) {
        this.showTimeoutMs = i;
        if (isVisible() != 0) {
            hideAfterTimeout();
        }
    }

    public int getRepeatToggleModes() {
        return this.repeatToggleModes;
    }

    public void setRepeatToggleModes(int i) {
        this.repeatToggleModes = i;
        if (this.player != null) {
            int repeatMode = this.player.getRepeatMode();
            if (i == 0 && repeatMode != 0) {
                this.controlDispatcher.dispatchSetRepeatMode(this.player, 0);
            } else if (i == 1 && repeatMode == 2) {
                this.controlDispatcher.dispatchSetRepeatMode(this.player, 1);
            } else if (i == 2 && repeatMode == 1) {
                this.controlDispatcher.dispatchSetRepeatMode(this.player, 2);
            }
        }
    }

    public boolean getShowShuffleButton() {
        return this.showShuffleButton;
    }

    public void setShowShuffleButton(boolean z) {
        this.showShuffleButton = z;
        updateShuffleButton();
    }

    public void show() {
        if (!isVisible()) {
            setVisibility(0);
            if (this.visibilityListener != null) {
                this.visibilityListener.onVisibilityChange(getVisibility());
            }
            updateAll();
            requestPlayPauseFocus();
        }
        hideAfterTimeout();
    }

    public void hide() {
        if (isVisible()) {
            setVisibility(8);
            if (this.visibilityListener != null) {
                this.visibilityListener.onVisibilityChange(getVisibility());
            }
            removeCallbacks(this.updateProgressAction);
            removeCallbacks(this.hideAction);
            this.hideAtMs = C0649C.TIME_UNSET;
        }
    }

    public boolean isVisible() {
        return getVisibility() == 0;
    }

    private void hideAfterTimeout() {
        removeCallbacks(this.hideAction);
        if (this.showTimeoutMs > 0) {
            this.hideAtMs = SystemClock.uptimeMillis() + ((long) this.showTimeoutMs);
            if (this.isAttachedToWindow) {
                postDelayed(this.hideAction, (long) this.showTimeoutMs);
                return;
            }
            return;
        }
        this.hideAtMs = C0649C.TIME_UNSET;
    }

    private void updateAll() {
        updatePlayPauseButton();
        updateNavigation();
        updateRepeatModeButton();
        updateShuffleButton();
        updateProgress();
    }

    private void updatePlayPauseButton() {
        if (isVisible()) {
            if (this.isAttachedToWindow) {
                int i;
                boolean isPlaying = isPlaying();
                int i2 = 8;
                int i3 = 1;
                if (this.playButton != null) {
                    i = (isPlaying && this.playButton.isFocused()) ? 1 : 0;
                    i |= 0;
                    this.playButton.setVisibility(isPlaying ? 8 : 0);
                } else {
                    i = 0;
                }
                if (this.pauseButton != null) {
                    if (isPlaying || !this.pauseButton.isFocused()) {
                        i3 = 0;
                    }
                    i |= i3;
                    View view = this.pauseButton;
                    if (isPlaying) {
                        i2 = 0;
                    }
                    view.setVisibility(i2);
                }
                if (i != 0) {
                    requestPlayPauseFocus();
                }
            }
        }
    }

    private void updateNavigation() {
        if (isVisible()) {
            if (this.isAttachedToWindow) {
                boolean z;
                boolean z2;
                boolean z3;
                Timeline currentTimeline = this.player != null ? this.player.getCurrentTimeline() : null;
                boolean z4 = true;
                Object obj = (currentTimeline == null || currentTimeline.isEmpty()) ? null : 1;
                if (obj == null || this.player.isPlayingAd()) {
                    z = false;
                    z2 = false;
                    z3 = false;
                } else {
                    currentTimeline.getWindow(this.player.getCurrentWindowIndex(), this.window);
                    z = this.window.isSeekable;
                    if (!z && this.window.isDynamic) {
                        if (this.player.getPreviousWindowIndex() == -1) {
                            z3 = false;
                            if (!this.window.isDynamic) {
                                if (this.player.getNextWindowIndex() != -1) {
                                    z2 = false;
                                }
                            }
                            z2 = true;
                        }
                    }
                    z3 = true;
                    if (this.window.isDynamic) {
                        if (this.player.getNextWindowIndex() != -1) {
                            z2 = false;
                        }
                    }
                    z2 = true;
                }
                setButtonEnabled(z3, this.previousButton);
                setButtonEnabled(z2, this.nextButton);
                z2 = this.fastForwardMs > 0 && z;
                setButtonEnabled(z2, this.fastForwardButton);
                if (this.rewindMs <= 0 || !z) {
                    z4 = false;
                }
                setButtonEnabled(z4, this.rewindButton);
                if (this.timeBar != null) {
                    this.timeBar.setEnabled(z);
                }
            }
        }
    }

    private void updateRepeatModeButton() {
        if (isVisible() && this.isAttachedToWindow) {
            if (this.repeatToggleButton != null) {
                if (this.repeatToggleModes == 0) {
                    this.repeatToggleButton.setVisibility(8);
                } else if (this.player == null) {
                    setButtonEnabled(false, this.repeatToggleButton);
                } else {
                    setButtonEnabled(true, this.repeatToggleButton);
                    switch (this.player.getRepeatMode()) {
                        case 0:
                            this.repeatToggleButton.setImageDrawable(this.repeatOffButtonDrawable);
                            this.repeatToggleButton.setContentDescription(this.repeatOffButtonContentDescription);
                            break;
                        case 1:
                            this.repeatToggleButton.setImageDrawable(this.repeatOneButtonDrawable);
                            this.repeatToggleButton.setContentDescription(this.repeatOneButtonContentDescription);
                            break;
                        case 2:
                            this.repeatToggleButton.setImageDrawable(this.repeatAllButtonDrawable);
                            this.repeatToggleButton.setContentDescription(this.repeatAllButtonContentDescription);
                            break;
                        default:
                            break;
                    }
                    this.repeatToggleButton.setVisibility(0);
                }
            }
        }
    }

    private void updateShuffleButton() {
        if (isVisible() && this.isAttachedToWindow) {
            if (this.shuffleButton != null) {
                if (!this.showShuffleButton) {
                    this.shuffleButton.setVisibility(8);
                } else if (this.player == null) {
                    setButtonEnabled(false, this.shuffleButton);
                } else {
                    this.shuffleButton.setAlpha(this.player.getShuffleModeEnabled() ? 1.0f : 0.3f);
                    this.shuffleButton.setEnabled(true);
                    this.shuffleButton.setVisibility(0);
                }
            }
        }
    }

    private void updateTimeBarMode() {
        if (this.player != null) {
            boolean z = this.showMultiWindowTimeBar && canShowMultiWindowTimeBar(this.player.getCurrentTimeline(), this.window);
            this.multiWindowTimeBar = z;
        }
    }

    private void updateProgress() {
        PlayerControlView playerControlView = this;
        if (isVisible()) {
            if (playerControlView.isAttachedToWindow) {
                long j;
                long j2;
                long usToMs;
                long contentPosition;
                int length;
                int i = 1;
                if (playerControlView.player != null) {
                    long j3;
                    int i2;
                    Timeline currentTimeline = playerControlView.player.getCurrentTimeline();
                    if (currentTimeline.isEmpty()) {
                        j3 = 0;
                        i2 = 0;
                        j = 0;
                    } else {
                        int currentWindowIndex = playerControlView.player.getCurrentWindowIndex();
                        int i3 = playerControlView.multiWindowTimeBar ? 0 : currentWindowIndex;
                        int windowCount = playerControlView.multiWindowTimeBar ? currentTimeline.getWindowCount() - 1 : currentWindowIndex;
                        j3 = 0;
                        i2 = 0;
                        j = 0;
                        while (i3 <= windowCount) {
                            if (i3 == currentWindowIndex) {
                                j = j3;
                            }
                            currentTimeline.getWindow(i3, playerControlView.window);
                            if (playerControlView.window.durationUs == C0649C.TIME_UNSET) {
                                Assertions.checkState(playerControlView.multiWindowTimeBar ^ i);
                                break;
                            }
                            int i4 = playerControlView.window.firstPeriodIndex;
                            while (i4 <= playerControlView.window.lastPeriodIndex) {
                                int i5;
                                currentTimeline.getPeriod(i4, playerControlView.period);
                                int adGroupCount = playerControlView.period.getAdGroupCount();
                                i = i2;
                                i2 = 0;
                                while (i2 < adGroupCount) {
                                    long adGroupTimeUs = playerControlView.period.getAdGroupTimeUs(i2);
                                    if (adGroupTimeUs != Long.MIN_VALUE) {
                                        j2 = adGroupTimeUs;
                                    } else if (playerControlView.period.durationUs == C0649C.TIME_UNSET) {
                                        i5 = currentWindowIndex;
                                        i2++;
                                        currentWindowIndex = i5;
                                    } else {
                                        j2 = playerControlView.period.durationUs;
                                    }
                                    long positionInWindowUs = j2 + playerControlView.period.getPositionInWindowUs();
                                    if (positionInWindowUs >= 0 && positionInWindowUs <= playerControlView.window.durationUs) {
                                        if (i == playerControlView.adGroupTimesMs.length) {
                                            int length2 = playerControlView.adGroupTimesMs.length == 0 ? 1 : playerControlView.adGroupTimesMs.length * 2;
                                            playerControlView.adGroupTimesMs = Arrays.copyOf(playerControlView.adGroupTimesMs, length2);
                                            playerControlView.playedAdGroups = Arrays.copyOf(playerControlView.playedAdGroups, length2);
                                        }
                                        i5 = currentWindowIndex;
                                        playerControlView.adGroupTimesMs[i] = C0649C.usToMs(j3 + positionInWindowUs);
                                        playerControlView.playedAdGroups[i] = playerControlView.period.hasPlayedAdGroup(i2);
                                        i++;
                                        i2++;
                                        currentWindowIndex = i5;
                                    }
                                    i5 = currentWindowIndex;
                                    i2++;
                                    currentWindowIndex = i5;
                                }
                                i5 = currentWindowIndex;
                                i4++;
                                i2 = i;
                            }
                            i3++;
                            j3 += playerControlView.window.durationUs;
                            currentWindowIndex = currentWindowIndex;
                            i = 1;
                        }
                    }
                    j2 = C0649C.usToMs(j3);
                    usToMs = C0649C.usToMs(j);
                    if (playerControlView.player.isPlayingAd()) {
                        contentPosition = usToMs + playerControlView.player.getContentPosition();
                        j = contentPosition;
                    } else {
                        contentPosition = usToMs + playerControlView.player.getCurrentPosition();
                        j = usToMs + playerControlView.player.getBufferedPosition();
                    }
                    if (playerControlView.timeBar != null) {
                        length = playerControlView.extraAdGroupTimesMs.length;
                        i = i2 + length;
                        if (i > playerControlView.adGroupTimesMs.length) {
                            playerControlView.adGroupTimesMs = Arrays.copyOf(playerControlView.adGroupTimesMs, i);
                            playerControlView.playedAdGroups = Arrays.copyOf(playerControlView.playedAdGroups, i);
                        }
                        System.arraycopy(playerControlView.extraAdGroupTimesMs, 0, playerControlView.adGroupTimesMs, i2, length);
                        System.arraycopy(playerControlView.extraPlayedAdGroups, 0, playerControlView.playedAdGroups, i2, length);
                        playerControlView.timeBar.setAdGroupTimesMs(playerControlView.adGroupTimesMs, playerControlView.playedAdGroups, i);
                    }
                } else {
                    j2 = 0;
                    contentPosition = 0;
                    j = 0;
                }
                if (playerControlView.durationView != null) {
                    playerControlView.durationView.setText(Util.getStringForTime(playerControlView.formatBuilder, playerControlView.formatter, j2));
                }
                if (!(playerControlView.positionView == null || playerControlView.scrubbing)) {
                    playerControlView.positionView.setText(Util.getStringForTime(playerControlView.formatBuilder, playerControlView.formatter, contentPosition));
                }
                if (playerControlView.timeBar != null) {
                    playerControlView.timeBar.setPosition(contentPosition);
                    playerControlView.timeBar.setBufferedPosition(j);
                    playerControlView.timeBar.setDuration(j2);
                }
                removeCallbacks(playerControlView.updateProgressAction);
                if (playerControlView.player == null) {
                    length = 1;
                    i = 1;
                } else {
                    i = playerControlView.player.getPlaybackState();
                    length = 1;
                }
                if (!(i == length || i == 4)) {
                    j2 = 1000;
                    if (playerControlView.player.getPlayWhenReady() && i == 3) {
                        float f = playerControlView.player.getPlaybackParameters().speed;
                        if (f > 0.1f) {
                            if (f <= 5.0f) {
                                usToMs = (long) (1000 / Math.max(1, Math.round(1.0f / f)));
                                long j4 = usToMs - (contentPosition % usToMs);
                                if (j4 < usToMs / 5) {
                                    j4 += usToMs;
                                }
                                j2 = f == 1.0f ? j4 : (long) (((float) j4) / f);
                            } else {
                                j2 = 200;
                            }
                        }
                    }
                    postDelayed(playerControlView.updateProgressAction, j2);
                }
            }
        }
    }

    private void requestPlayPauseFocus() {
        boolean isPlaying = isPlaying();
        if (!isPlaying && this.playButton != null) {
            this.playButton.requestFocus();
        } else if (isPlaying && this.pauseButton != null) {
            this.pauseButton.requestFocus();
        }
    }

    private void setButtonEnabled(boolean z, View view) {
        if (view != null) {
            view.setEnabled(z);
            view.setAlpha(z ? true : true);
            view.setVisibility(false);
        }
    }

    private void previous() {
        Timeline currentTimeline = this.player.getCurrentTimeline();
        if (!currentTimeline.isEmpty()) {
            currentTimeline.getWindow(this.player.getCurrentWindowIndex(), this.window);
            int previousWindowIndex = this.player.getPreviousWindowIndex();
            if (previousWindowIndex == -1 || (this.player.getCurrentPosition() > MAX_POSITION_FOR_SEEK_TO_PREVIOUS && (!this.window.isDynamic || this.window.isSeekable))) {
                seekTo(0);
            } else {
                seekTo(previousWindowIndex, C0649C.TIME_UNSET);
            }
        }
    }

    private void next() {
        Timeline currentTimeline = this.player.getCurrentTimeline();
        if (!currentTimeline.isEmpty()) {
            int currentWindowIndex = this.player.getCurrentWindowIndex();
            int nextWindowIndex = this.player.getNextWindowIndex();
            if (nextWindowIndex != -1) {
                seekTo(nextWindowIndex, C0649C.TIME_UNSET);
            } else if (currentTimeline.getWindow(currentWindowIndex, this.window, false).isDynamic) {
                seekTo(currentWindowIndex, C0649C.TIME_UNSET);
            }
        }
    }

    private void rewind() {
        if (this.rewindMs > 0) {
            seekTo(Math.max(this.player.getCurrentPosition() - ((long) this.rewindMs), 0));
        }
    }

    private void fastForward() {
        if (this.fastForwardMs > 0) {
            long duration = this.player.getDuration();
            long currentPosition = this.player.getCurrentPosition() + ((long) this.fastForwardMs);
            if (duration != C0649C.TIME_UNSET) {
                currentPosition = Math.min(currentPosition, duration);
            }
            seekTo(currentPosition);
        }
    }

    private void seekTo(long j) {
        seekTo(this.player.getCurrentWindowIndex(), j);
    }

    private void seekTo(int i, long j) {
        if (this.controlDispatcher.dispatchSeekTo(this.player, i, j) == 0) {
            updateProgress();
        }
    }

    private void seekToTimeBarPosition(long j) {
        int currentWindowIndex;
        Timeline currentTimeline = this.player.getCurrentTimeline();
        if (!this.multiWindowTimeBar || currentTimeline.isEmpty()) {
            currentWindowIndex = this.player.getCurrentWindowIndex();
        } else {
            long durationMs;
            int windowCount = currentTimeline.getWindowCount();
            currentWindowIndex = 0;
            while (true) {
                durationMs = currentTimeline.getWindow(currentWindowIndex, this.window).getDurationMs();
                if (j < durationMs) {
                    break;
                } else if (currentWindowIndex == windowCount - 1) {
                    break;
                } else {
                    currentWindowIndex++;
                    j -= durationMs;
                }
            }
            j = durationMs;
        }
        seekTo(currentWindowIndex, j);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.isAttachedToWindow = true;
        if (this.hideAtMs != C0649C.TIME_UNSET) {
            long uptimeMillis = this.hideAtMs - SystemClock.uptimeMillis();
            if (uptimeMillis <= 0) {
                hide();
            } else {
                postDelayed(this.hideAction, uptimeMillis);
            }
        } else if (isVisible()) {
            hideAfterTimeout();
        }
        updateAll();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.isAttachedToWindow = false;
        removeCallbacks(this.updateProgressAction);
        removeCallbacks(this.hideAction);
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (!dispatchMediaKeyEvent(keyEvent)) {
            if (super.dispatchKeyEvent(keyEvent) == null) {
                return null;
            }
        }
        return true;
    }

    public boolean dispatchMediaKeyEvent(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (this.player != null) {
            if (isHandledMediaKey(keyCode)) {
                if (keyEvent.getAction() == 0) {
                    if (keyCode != 90) {
                        if (keyCode != 89) {
                            if (keyEvent.getRepeatCount() == null) {
                                switch (keyCode) {
                                    case 85:
                                        this.controlDispatcher.dispatchSetPlayWhenReady(this.player, this.player.getPlayWhenReady() ^ true);
                                        break;
                                    case 87:
                                        next();
                                        break;
                                    case 88:
                                        previous();
                                        break;
                                    case Big5DistributionAnalysis.LOWBYTE_END_1 /*126*/:
                                        this.controlDispatcher.dispatchSetPlayWhenReady(this.player, true);
                                        break;
                                    case 127:
                                        this.controlDispatcher.dispatchSetPlayWhenReady(this.player, false);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        rewind();
                    } else {
                        fastForward();
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean isPlaying() {
        if (this.player == null || this.player.getPlaybackState() == 4 || this.player.getPlaybackState() == 1 || !this.player.getPlayWhenReady()) {
            return false;
        }
        return true;
    }

    private static boolean canShowMultiWindowTimeBar(Timeline timeline, Window window) {
        if (timeline.getWindowCount() > 100) {
            return false;
        }
        int windowCount = timeline.getWindowCount();
        for (int i = 0; i < windowCount; i++) {
            if (timeline.getWindow(i, window).durationUs == C0649C.TIME_UNSET) {
                return false;
            }
        }
        return true;
    }
}
