package com.google.android.gms.cast.framework.media;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.cast.MediaTrack.Builder;
import com.google.android.gms.cast.framework.C0782R;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.Session;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class TracksChooserDialogFragment extends DialogFragment {
    private long[] zzdm;
    private RemoteMediaClient zzhp;
    @VisibleForTesting
    private boolean zzok;
    @VisibleForTesting
    private List<MediaTrack> zzol;
    @VisibleForTesting
    private List<MediaTrack> zzom;
    private Dialog zzon;
    private MediaInfo zzoo;
    private long[] zzop;

    private TracksChooserDialogFragment(MediaInfo mediaInfo, long[] jArr) {
        this.zzoo = mediaInfo;
        this.zzop = jArr;
    }

    @NonNull
    public static TracksChooserDialogFragment newInstance() {
        return new TracksChooserDialogFragment();
    }

    @Deprecated
    public static TracksChooserDialogFragment newInstance(MediaInfo mediaInfo, long[] jArr) {
        return new TracksChooserDialogFragment(mediaInfo, jArr);
    }

    private static int zza(List<MediaTrack> list, long[] jArr, int i) {
        if (jArr == null || list == null) {
            return i;
        }
        for (int i2 = 0; i2 < list.size(); i2++) {
            for (long j : jArr) {
                if (j == ((MediaTrack) list.get(i2)).getId()) {
                    return i2;
                }
            }
        }
        return i;
    }

    @NonNull
    private static ArrayList<MediaTrack> zza(List<MediaTrack> list, int i) {
        ArrayList<MediaTrack> arrayList = new ArrayList();
        if (list != null) {
            for (MediaTrack mediaTrack : list) {
                if (mediaTrack.getType() == i) {
                    arrayList.add(mediaTrack);
                }
            }
        }
        return arrayList;
    }

    private final void zza(zzbb zzbb, zzbb zzbb2) {
        if (this.zzok) {
            if (this.zzhp.hasMediaSession()) {
                List arrayList = new ArrayList();
                MediaTrack zzbs = zzbb.zzbs();
                if (!(zzbs == null || zzbs.getId() == -1)) {
                    arrayList.add(Long.valueOf(zzbs.getId()));
                }
                zzbs = zzbb2.zzbs();
                if (zzbs != null) {
                    arrayList.add(Long.valueOf(zzbs.getId()));
                }
                if (this.zzdm != null && this.zzdm.length > 0) {
                    HashSet hashSet = new HashSet();
                    for (MediaTrack id : this.zzom) {
                        hashSet.add(Long.valueOf(id.getId()));
                    }
                    for (MediaTrack id2 : this.zzol) {
                        hashSet.add(Long.valueOf(id2.getId()));
                    }
                    for (long j : this.zzdm) {
                        if (!hashSet.contains(Long.valueOf(j))) {
                            arrayList.add(Long.valueOf(j));
                        }
                    }
                }
                long[] jArr = new long[arrayList.size()];
                for (int i = 0; i < arrayList.size(); i++) {
                    jArr[i] = ((Long) arrayList.get(i)).longValue();
                }
                Arrays.sort(jArr);
                this.zzhp.setActiveMediaTracks(jArr);
                zzbr();
                return;
            }
        }
        zzbr();
    }

    private final void zzbr() {
        if (this.zzon != null) {
            this.zzon.cancel();
            this.zzon = null;
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.zzok = true;
        this.zzom = new ArrayList();
        this.zzol = new ArrayList();
        this.zzdm = new long[0];
        Session currentCastSession = CastContext.getSharedInstance(getContext()).getSessionManager().getCurrentCastSession();
        if (currentCastSession != null) {
            if (currentCastSession.isConnected()) {
                this.zzhp = currentCastSession.getRemoteMediaClient();
                if (this.zzhp != null && this.zzhp.hasMediaSession()) {
                    if (this.zzhp.getMediaInfo() != null) {
                        long[] jArr;
                        MediaInfo mediaInfo;
                        List mediaTracks;
                        if (this.zzop != null) {
                            jArr = this.zzop;
                        } else {
                            MediaStatus mediaStatus = this.zzhp.getMediaStatus();
                            if (mediaStatus != null) {
                                jArr = mediaStatus.getActiveTrackIds();
                            }
                            mediaInfo = this.zzoo == null ? this.zzoo : this.zzhp.getMediaInfo();
                            if (mediaInfo != null) {
                                this.zzok = false;
                                return;
                            }
                            mediaTracks = mediaInfo.getMediaTracks();
                            if (mediaTracks != null) {
                                this.zzok = false;
                                return;
                            }
                            this.zzom = zza(mediaTracks, 2);
                            this.zzol = zza(mediaTracks, 1);
                            if (!this.zzol.isEmpty()) {
                                this.zzol.add(0, new Builder(-1, 1).setName(getActivity().getString(C0782R.string.cast_tracks_chooser_dialog_none)).setSubtype(2).setContentId("").build());
                            }
                            return;
                        }
                        this.zzdm = jArr;
                        if (this.zzoo == null) {
                        }
                        if (mediaInfo != null) {
                            mediaTracks = mediaInfo.getMediaTracks();
                            if (mediaTracks != null) {
                                this.zzom = zza(mediaTracks, 2);
                                this.zzol = zza(mediaTracks, 1);
                                if (this.zzol.isEmpty()) {
                                    this.zzol.add(0, new Builder(-1, 1).setName(getActivity().getString(C0782R.string.cast_tracks_chooser_dialog_none)).setSubtype(2).setContentId("").build());
                                }
                                return;
                            }
                            this.zzok = false;
                            return;
                        }
                        this.zzok = false;
                        return;
                    }
                }
                this.zzok = false;
                return;
            }
        }
        this.zzok = false;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        int zza = zza(this.zzol, this.zzdm, 0);
        int zza2 = zza(this.zzom, this.zzdm, -1);
        Object zzbb = new zzbb(getActivity(), this.zzol, zza);
        Object zzbb2 = new zzbb(getActivity(), this.zzom, zza2);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View inflate = getActivity().getLayoutInflater().inflate(C0782R.layout.cast_tracks_chooser_dialog_layout, null);
        ListView listView = (ListView) inflate.findViewById(C0782R.id.text_list_view);
        ListView listView2 = (ListView) inflate.findViewById(C0782R.id.audio_list_view);
        TabHost tabHost = (TabHost) inflate.findViewById(C0782R.id.tab_host);
        tabHost.setup();
        if (zzbb.getCount() == 0) {
            listView.setVisibility(4);
        } else {
            listView.setAdapter(zzbb);
            TabSpec newTabSpec = tabHost.newTabSpec("textTab");
            newTabSpec.setContent(C0782R.id.text_list_view);
            newTabSpec.setIndicator(getActivity().getString(C0782R.string.cast_tracks_chooser_dialog_subtitles));
            tabHost.addTab(newTabSpec);
        }
        if (zzbb2.getCount() <= 1) {
            listView2.setVisibility(4);
        } else {
            listView2.setAdapter(zzbb2);
            newTabSpec = tabHost.newTabSpec("audioTab");
            newTabSpec.setContent(C0782R.id.audio_list_view);
            newTabSpec.setIndicator(getActivity().getString(C0782R.string.cast_tracks_chooser_dialog_audio));
            tabHost.addTab(newTabSpec);
        }
        builder.setView(inflate).setPositiveButton(getActivity().getString(C0782R.string.cast_tracks_chooser_dialog_ok), new zzba(this, zzbb, zzbb2)).setNegativeButton(C0782R.string.cast_tracks_chooser_dialog_cancel, new zzaz(this));
        if (this.zzon != null) {
            this.zzon.cancel();
            this.zzon = null;
        }
        this.zzon = builder.create();
        return this.zzon;
    }

    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
