package com.android.morpheustv.player;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import com.android.morpheustv.helpers.Utils;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.SelectionOverride;
import com.google.android.exoplayer2.trackselection.RandomTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection.Factory;
import com.noname.titan.R;
import java.util.Arrays;

public class TrackSelectionHelper implements OnClickListener, DialogInterface.OnClickListener {
    private static final Factory FIXED_FACTORY = new FixedTrackSelection.Factory();
    private static final Factory RANDOM_FACTORY = new RandomTrackSelection.Factory();
    private final Factory adaptiveTrackSelectionFactory;
    private CheckedTextView defaultView;
    private CheckedTextView disableView;
    private CheckedTextView enableRandomAdaptationView;
    private boolean isDisabled;
    private SelectionOverride override;
    private int rendererIndex;
    private final MappingTrackSelector selector;
    private TrackGroupArray trackGroups;
    private boolean[] trackGroupsAdaptive;
    private MappedTrackInfo trackInfo;
    private CheckedTextView[][] trackViews;

    public TrackSelectionHelper(MappingTrackSelector mappingTrackSelector, Factory factory) {
        this.selector = mappingTrackSelector;
        this.adaptiveTrackSelectionFactory = factory;
    }

    public void showSelectionDialog(Activity activity, CharSequence charSequence, MappedTrackInfo mappedTrackInfo, int i) {
        this.trackInfo = mappedTrackInfo;
        this.rendererIndex = i;
        this.trackGroups = mappedTrackInfo.getTrackGroups(i);
        this.trackGroupsAdaptive = new boolean[this.trackGroups.length];
        int i2 = 0;
        while (i2 < this.trackGroups.length) {
            boolean[] zArr = this.trackGroupsAdaptive;
            boolean z = true;
            if (this.adaptiveTrackSelectionFactory == null || mappedTrackInfo.getAdaptiveSupport(i, i2, false) == 0 || this.trackGroups.get(i2).length <= 1) {
                z = false;
            }
            zArr[i2] = z;
            i2++;
        }
        this.isDisabled = this.selector.getRendererDisabled(i);
        this.override = this.selector.getSelectionOverride(i, this.trackGroups);
        mappedTrackInfo = new Builder(activity);
        mappedTrackInfo.setTitle(charSequence).setView(buildView(mappedTrackInfo.getContext())).setPositiveButton(17039370, this).setNegativeButton(17039360, null).create().show();
    }

    @SuppressLint({"InflateParams"})
    private View buildView(Context context) {
        LayoutInflater from = LayoutInflater.from(context);
        View inflate = from.inflate(R.layout.track_selection_dialog, null);
        ViewGroup viewGroup = (ViewGroup) inflate.findViewById(R.id.root);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(new int[]{16843534});
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        obtainStyledAttributes.recycle();
        this.disableView = (CheckedTextView) from.inflate(17367055, viewGroup, false);
        this.disableView.setBackgroundResource(resourceId);
        this.disableView.setText(R.string.selection_disabled);
        this.disableView.setFocusable(true);
        this.disableView.setOnClickListener(this);
        viewGroup.addView(this.disableView);
        this.defaultView = (CheckedTextView) from.inflate(17367055, viewGroup, false);
        this.defaultView.setBackgroundResource(resourceId);
        this.defaultView.setText(R.string.selection_default);
        this.defaultView.setFocusable(true);
        this.defaultView.setOnClickListener(this);
        int i = R.layout.list_divider;
        viewGroup.addView(from.inflate(R.layout.list_divider, viewGroup, false));
        viewGroup.addView(this.defaultView);
        this.trackViews = new CheckedTextView[this.trackGroups.length][];
        int i2 = 0;
        int i3 = 0;
        while (i2 < r0.trackGroups.length) {
            TrackGroup trackGroup = r0.trackGroups.get(i2);
            boolean z = r0.trackGroupsAdaptive[i2];
            i3 |= z;
            r0.trackViews[i2] = new CheckedTextView[trackGroup.length];
            int i4 = 0;
            while (i4 < trackGroup.length) {
                if (i4 == 0) {
                    viewGroup.addView(from.inflate(i, viewGroup, false));
                }
                CheckedTextView checkedTextView = (CheckedTextView) from.inflate(z ? 17367056 : 17367055, viewGroup, false);
                checkedTextView.setBackgroundResource(resourceId);
                checkedTextView.setText(Utils.buildTrackName(trackGroup.getFormat(i4)));
                if (r0.trackInfo.getTrackFormatSupport(r0.rendererIndex, i2, i4) == 4) {
                    checkedTextView.setFocusable(true);
                    checkedTextView.setTag(Pair.create(Integer.valueOf(i2), Integer.valueOf(i4)));
                    checkedTextView.setOnClickListener(r0);
                } else {
                    checkedTextView.setFocusable(false);
                    checkedTextView.setEnabled(false);
                }
                r0.trackViews[i2][i4] = checkedTextView;
                viewGroup.addView(checkedTextView);
                i4++;
                i = R.layout.list_divider;
            }
            i2++;
            i = R.layout.list_divider;
        }
        if (i3 != 0) {
            r0.enableRandomAdaptationView = (CheckedTextView) from.inflate(17367056, viewGroup, false);
            r0.enableRandomAdaptationView.setBackgroundResource(resourceId);
            r0.enableRandomAdaptationView.setText(R.string.enable_random_adaptation);
            r0.enableRandomAdaptationView.setOnClickListener(r0);
            viewGroup.addView(from.inflate(R.layout.list_divider, viewGroup, false));
            viewGroup.addView(r0.enableRandomAdaptationView);
        }
        updateViews();
        return inflate;
    }

    private void updateViews() {
        this.disableView.setChecked(this.isDisabled);
        CheckedTextView checkedTextView = this.defaultView;
        boolean z = false;
        boolean z2 = !this.isDisabled && this.override == null;
        checkedTextView.setChecked(z2);
        int i = 0;
        while (i < this.trackViews.length) {
            for (int i2 = 0; i2 < this.trackViews[i].length; i2++) {
                CheckedTextView checkedTextView2 = this.trackViews[i][i2];
                boolean z3 = this.override != null && this.override.groupIndex == i && this.override.containsTrack(i2);
                checkedTextView2.setChecked(z3);
            }
            i++;
        }
        if (this.enableRandomAdaptationView != null) {
            boolean z4 = (this.isDisabled || this.override == null || this.override.length <= 1) ? false : true;
            this.enableRandomAdaptationView.setEnabled(z4);
            this.enableRandomAdaptationView.setFocusable(z4);
            if (z4) {
                checkedTextView = this.enableRandomAdaptationView;
                if (!this.isDisabled && (this.override.factory instanceof RandomTrackSelection.Factory)) {
                    z = true;
                }
                checkedTextView.setChecked(z);
            }
        }
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.selector.setRendererDisabled(this.rendererIndex, this.isDisabled);
        if (this.override != null) {
            this.selector.setSelectionOverride(this.rendererIndex, this.trackGroups, this.override);
        } else {
            this.selector.clearSelectionOverrides(this.rendererIndex);
        }
    }

    public void onClick(View view) {
        if (view == this.disableView) {
            this.isDisabled = true;
            this.override = null;
        } else if (view == this.defaultView) {
            this.isDisabled = false;
            this.override = null;
        } else if (view == this.enableRandomAdaptationView) {
            setOverride(this.override.groupIndex, this.override.tracks, this.enableRandomAdaptationView.isChecked() ^ true);
        } else {
            this.isDisabled = false;
            Pair pair = (Pair) view.getTag();
            int intValue = ((Integer) pair.first).intValue();
            int intValue2 = ((Integer) pair.second).intValue();
            if (this.trackGroupsAdaptive[intValue] && this.override != null) {
                if (this.override.groupIndex == intValue) {
                    view = ((CheckedTextView) view).isChecked();
                    int i = this.override.length;
                    if (view == null) {
                        setOverride(intValue, getTracksAdding(this.override, intValue2), this.enableRandomAdaptationView.isChecked());
                    } else if (i == 1) {
                        this.override = null;
                        this.isDisabled = true;
                    } else {
                        setOverride(intValue, getTracksRemoving(this.override, intValue2), this.enableRandomAdaptationView.isChecked());
                    }
                }
            }
            this.override = new SelectionOverride(FIXED_FACTORY, intValue, intValue2);
        }
        updateViews();
    }

    private void setOverride(int i, int[] iArr, boolean z) {
        z = iArr.length == 1 ? FIXED_FACTORY : z ? RANDOM_FACTORY : this.adaptiveTrackSelectionFactory;
        this.override = new SelectionOverride(z, i, iArr);
    }

    private static int[] getTracksAdding(SelectionOverride selectionOverride, int i) {
        selectionOverride = selectionOverride.tracks;
        selectionOverride = Arrays.copyOf(selectionOverride, selectionOverride.length + 1);
        selectionOverride[selectionOverride.length - 1] = i;
        return selectionOverride;
    }

    private static int[] getTracksRemoving(SelectionOverride selectionOverride, int i) {
        int[] iArr = new int[(selectionOverride.length - 1)];
        int i2 = 0;
        for (int i3 = 0; i3 < iArr.length + 1; i3++) {
            int i4 = selectionOverride.tracks[i3];
            if (i4 != i) {
                int i5 = i2 + 1;
                iArr[i2] = i4;
                i2 = i5;
            }
        }
        return iArr;
    }
}
