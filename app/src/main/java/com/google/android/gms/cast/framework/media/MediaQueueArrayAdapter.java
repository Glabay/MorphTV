package com.google.android.gms.cast.framework.media;

import android.content.Context;
import android.widget.ArrayAdapter;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.framework.media.MediaQueue.Callback;

public class MediaQueueArrayAdapter extends ArrayAdapter<MediaQueueItem> {
    private final MediaQueue zzlt;
    private final Callback zzlu = new zza();

    private class zza extends Callback {
        private final /* synthetic */ MediaQueueArrayAdapter zzlv;

        private zza(MediaQueueArrayAdapter mediaQueueArrayAdapter) {
            this.zzlv = mediaQueueArrayAdapter;
        }

        public final void itemsInsertedInRange(int i, int i2) {
            this.zzlv.notifyDataSetChanged();
        }

        public final void itemsReloaded() {
            this.zzlv.notifyDataSetChanged();
        }

        public final void itemsRemovedAtIndexes(int[] iArr) {
            this.zzlv.notifyDataSetChanged();
        }

        public final void itemsUpdatedAtIndexes(int[] iArr) {
            this.zzlv.notifyDataSetChanged();
        }

        public final void mediaQueueChanged() {
            this.zzlv.notifyDataSetChanged();
        }

        public final void mediaQueueWillChange() {
        }
    }

    public MediaQueueArrayAdapter(MediaQueue mediaQueue, Context context, int i) {
        super(context, i);
        this.zzlt = mediaQueue;
        this.zzlt.registerCallback(this.zzlu);
    }

    public boolean areAllItemsEnabled() {
        return false;
    }

    public void dispose() {
        this.zzlt.unregisterCallback(this.zzlu);
    }

    public int getCount() {
        return this.zzlt.getItemCount();
    }

    public MediaQueueItem getItem(int i) {
        return this.zzlt.getItemAtIndex(i);
    }

    public long getItemId(int i) {
        return (long) this.zzlt.itemIdAtIndex(i);
    }

    public MediaQueue getMediaQueue() {
        return this.zzlt;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isEmpty() {
        return this.zzlt.getItemCount() == 0;
    }

    public boolean isEnabled(int i) {
        return this.zzlt.getItemAtIndex(i, false) != null;
    }
}
