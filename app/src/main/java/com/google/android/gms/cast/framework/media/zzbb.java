package com.google.android.gms.cast.framework.media;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.cast.framework.C0782R;
import java.util.ArrayList;
import java.util.List;

public final class zzbb extends ArrayAdapter<MediaTrack> implements OnClickListener {
    private final Context zzij;
    private int zzot;

    public zzbb(Context context, List<MediaTrack> list, int i) {
        List arrayList;
        int i2 = C0782R.layout.cast_tracks_chooser_dialog_row_layout;
        if (list == null) {
            arrayList = new ArrayList();
        }
        super(context, i2, arrayList);
        this.zzot = -1;
        this.zzij = context;
        this.zzot = i;
    }

    public final View getView(int i, View view, ViewGroup viewGroup) {
        zzbd zzbd;
        if (view == null) {
            view = ((LayoutInflater) this.zzij.getSystemService("layout_inflater")).inflate(C0782R.layout.cast_tracks_chooser_dialog_row_layout, viewGroup, false);
            zzbd = new zzbd(this, (TextView) view.findViewById(C0782R.id.text), (RadioButton) view.findViewById(C0782R.id.radio));
            view.setTag(zzbd);
        } else {
            zzbd = (zzbd) view.getTag();
        }
        if (zzbd == null) {
            return null;
        }
        CharSequence string;
        zzbd.zzov.setTag(Integer.valueOf(i));
        zzbd.zzov.setChecked(this.zzot == i);
        view.setOnClickListener(this);
        MediaTrack mediaTrack = (MediaTrack) getItem(i);
        CharSequence name = mediaTrack.getName();
        if (TextUtils.isEmpty(name)) {
            if (mediaTrack.getSubtype() == 2) {
                string = this.zzij.getString(C0782R.string.cast_tracks_chooser_dialog_closed_captions);
            } else {
                if (!TextUtils.isEmpty(mediaTrack.getLanguage())) {
                    CharSequence displayLanguage = MediaUtils.getTrackLanguage(mediaTrack).getDisplayLanguage();
                    if (!TextUtils.isEmpty(displayLanguage)) {
                        string = displayLanguage;
                    }
                }
                name = this.zzij.getString(C0782R.string.cast_tracks_chooser_dialog_default_track_name, new Object[]{Integer.valueOf(i + 1)});
            }
            zzbd.zzou.setText(string);
            return view;
        }
        string = name;
        zzbd.zzou.setText(string);
        return view;
    }

    public final void onClick(View view) {
        this.zzot = ((Integer) ((zzbd) view.getTag()).zzov.getTag()).intValue();
        notifyDataSetChanged();
    }

    public final MediaTrack zzbs() {
        return (this.zzot < 0 || this.zzot >= getCount()) ? null : (MediaTrack) getItem(this.zzot);
    }
}
