package com.google.android.gms.cast.framework.media;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.framework.media.MediaQueue.Callback;

public abstract class MediaQueueRecyclerViewAdapter<VH extends ViewHolder> extends Adapter<VH> {
    private final MediaQueue zzlt;
    private final Callback zzlu = new zza();

    private class zza extends Callback {
        private final /* synthetic */ MediaQueueRecyclerViewAdapter zzlw;

        private zza(MediaQueueRecyclerViewAdapter mediaQueueRecyclerViewAdapter) {
            this.zzlw = mediaQueueRecyclerViewAdapter;
        }

        public final void itemsInsertedInRange(int i, int i2) {
            this.zzlw.notifyItemRangeInserted(i, i2);
        }

        public final void itemsReloaded() {
            this.zzlw.notifyDataSetChanged();
        }

        public final void itemsRemovedAtIndexes(int[] iArr) {
            if (iArr.length > 1) {
                this.zzlw.notifyDataSetChanged();
                return;
            }
            for (int notifyItemRemoved : iArr) {
                this.zzlw.notifyItemRemoved(notifyItemRemoved);
            }
        }

        public final void itemsUpdatedAtIndexes(int[] iArr) {
            for (int notifyItemChanged : iArr) {
                this.zzlw.notifyItemChanged(notifyItemChanged);
            }
        }

        public final void mediaQueueChanged() {
        }

        public final void mediaQueueWillChange() {
        }
    }

    public MediaQueueRecyclerViewAdapter(MediaQueue mediaQueue) {
        this.zzlt = mediaQueue;
        this.zzlt.registerCallback(this.zzlu);
    }

    public void dispose() {
        this.zzlt.unregisterCallback(this.zzlu);
    }

    public MediaQueueItem getItem(int i) {
        return this.zzlt.getItemAtIndex(i);
    }

    public int getItemCount() {
        return this.zzlt.getItemCount();
    }

    public long getItemId(int i) {
        return (long) this.zzlt.itemIdAtIndex(i);
    }

    public MediaQueue getMediaQueue() {
        return this.zzlt;
    }
}
