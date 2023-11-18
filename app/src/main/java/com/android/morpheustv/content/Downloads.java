package com.android.morpheustv.content;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.InputDeviceCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.morpheustv.BaseActivity;
import com.android.morpheustv.BaseActivity.DownloadMetadata;
import com.android.morpheustv.helpers.TaskManager;
import com.android.morpheustv.helpers.Tmdb;
import com.android.morpheustv.helpers.Tmdb.ImageResult;
import com.android.morpheustv.helpers.Trakt;
import com.android.morpheustv.helpers.Trakt.EpisodeResult;
import com.android.morpheustv.helpers.Trakt.OnActionListener;
import com.android.morpheustv.helpers.Trakt.SeasonResult;
import com.android.morpheustv.helpers.Utils;
import com.android.morpheustv.sources.SourceList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.noname.titan.R;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Func;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.RequestInfo;
import com.tonyodev.fetch2.Status;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Downloads extends BaseActivity {
    private ArrayAdapter<DownloadEntry> adapter;
    private String currentBackdrop = "";
    private ArrayList<DownloadEntry> currentDownloads;
    private int currentSelection = 0;
    private ListView list;
    ImageView mBackdrop;
    Runnable updateDownloadsRunnable = new C04052();

    /* renamed from: com.android.morpheustv.content.Downloads$1 */
    class C04041 implements Func<List<? extends Download>> {

        /* renamed from: com.android.morpheustv.content.Downloads$1$1 */
        class C04031 implements Comparator<DownloadEntry> {
            C04031() {
            }

            public int compare(DownloadEntry downloadEntry, DownloadEntry downloadEntry2) {
                return downloadEntry.metadata.titleWithSE.compareTo(downloadEntry2.metadata.titleWithSE);
            }
        }

        C04041() {
        }

        public void call(List<? extends Download> list) {
            Downloads.this.currentDownloads.clear();
            for (Download download : list) {
                DownloadMetadata downloadMetadata = Downloads.this.getDownloadMetadata(download.getId());
                if (downloadMetadata != null) {
                    Downloads.this.currentDownloads.add(new DownloadEntry(download, downloadMetadata));
                }
            }
            if (Downloads.this.currentDownloads.size() > null) {
                Collections.sort(Downloads.this.currentDownloads, new C04031());
                Downloads.this.currentSelection = 0;
                if (Downloads.this.list != null) {
                    Downloads.this.list.setSelection(Downloads.this.currentSelection);
                    Downloads.this.list.smoothScrollToPosition(Downloads.this.currentSelection);
                }
            }
            Downloads.this.adapter.notifyDataSetChanged();
            if (Downloads.this.list != null) {
                Downloads.this.list.requestFocus();
            }
        }
    }

    /* renamed from: com.android.morpheustv.content.Downloads$2 */
    class C04052 implements Runnable {
        C04052() {
        }

        public void run() {
            Downloads.this.mainHandler.removeCallbacks(Downloads.this.updateDownloadsRunnable);
            Downloads.this.adapter.notifyDataSetChanged();
        }
    }

    /* renamed from: com.android.morpheustv.content.Downloads$4 */
    class C04074 implements OnItemLongClickListener {
        C04074() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (Downloads.this.currentDownloads == null || Downloads.this.currentDownloads.size() <= null || i >= Downloads.this.currentDownloads.size()) {
                return null;
            }
            Downloads.this.currentSelection = i;
            Downloads.this.adapter.notifyDataSetChanged();
            Downloads.this.showContextMenu((DownloadEntry) Downloads.this.currentDownloads.get(i));
            return true;
        }
    }

    /* renamed from: com.android.morpheustv.content.Downloads$5 */
    class C04085 implements OnItemSelectedListener {
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        C04085() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            if (Downloads.this.currentDownloads != null && Downloads.this.currentDownloads.size() > null && i < Downloads.this.currentDownloads.size()) {
                Downloads.this.currentSelection = i;
                Downloads.this.adapter.notifyDataSetChanged();
            }
        }
    }

    /* renamed from: com.android.morpheustv.content.Downloads$6 */
    class C04096 implements OnItemClickListener {
        C04096() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (Downloads.this.currentDownloads != null && Downloads.this.currentDownloads.size() > null && i < Downloads.this.currentDownloads.size()) {
                DownloadEntry downloadEntry = (DownloadEntry) Downloads.this.currentDownloads.get(i);
                if (i == Downloads.this.currentSelection) {
                    view = new Intent(Downloads.this, SourceList.class);
                    view.putExtra("show", downloadEntry.metadata.show);
                    view.putExtra("movie", downloadEntry.metadata.movie);
                    view.putExtra("title", downloadEntry.metadata.titleWithSE);
                    view.putExtra("episode_title", downloadEntry.metadata.episode_title);
                    view.putExtra("year", downloadEntry.metadata.year);
                    view.putExtra("imdb", downloadEntry.metadata.imdb);
                    view.putExtra("tmdb", downloadEntry.metadata.tmdb);
                    view.putExtra("season", downloadEntry.metadata.season);
                    view.putExtra("episode", downloadEntry.metadata.episode);
                    view.putExtra("episodeId", downloadEntry.metadata.episodeId);
                    view.putExtra("overview", downloadEntry.metadata.overview);
                    view.putExtra("rating", downloadEntry.metadata.rating);
                    view.putExtra("isFromDownloads", 1);
                    view.putExtra("location", downloadEntry.download.getFile());
                    view.putExtra("filename", Utils.getFilenameFromUrl(downloadEntry.download.getUrl(), downloadEntry.metadata.titleWithSE));
                    Downloads.this.startActivity(view);
                } else {
                    Downloads.this.currentSelection = i;
                }
                Downloads.this.adapter.notifyDataSetChanged();
            }
        }
    }

    private static class DownloadEntry {
        public Download download;
        public ImageResult images = new ImageResult("", "");
        public DownloadMetadata metadata;

        public DownloadEntry(Download download, DownloadMetadata downloadMetadata) {
            this.download = download;
            this.metadata = downloadMetadata;
        }
    }

    class LoadPosterTask extends AsyncTask<Void, Void, Void> {
        DownloadEntry dwn = null;
        ImageView view = null;

        public LoadPosterTask(DownloadEntry downloadEntry, ImageView imageView) {
            this.view = imageView;
            this.dwn = downloadEntry;
        }

        protected Void doInBackground(Void... voidArr) {
            try {
                if (Downloads.this.isForeground != null) {
                    if (this.dwn.metadata.season <= null || this.dwn.metadata.season <= null) {
                        this.dwn.images = Tmdb.getMovieImages(Integer.valueOf(this.dwn.metadata.tmdb), "en,null");
                    } else {
                        this.dwn.images = Tmdb.getShowImages(Integer.valueOf(this.dwn.metadata.tmdb), "en,null");
                    }
                }
            } catch (Void[] voidArr2) {
                voidArr2.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void voidR) {
            if (Downloads.this.isForeground != null) {
                Downloads.this.setImage(this.dwn, this.view);
            }
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_downloads);
        this.mBackdrop = (ImageView) findViewById(R.id.backdropImage);
        initializeListView();
        refreshDownloads();
    }

    protected void onResume() {
        super.onResume();
        refreshDownloads();
    }

    private void refreshDownloads() {
        if (getFetchInstance() != null) {
            getFetchInstance().getDownloads(new C04041());
        }
    }

    private void refreshDownloadView(Download download) {
        Iterator it = this.currentDownloads.iterator();
        while (it.hasNext()) {
            DownloadEntry downloadEntry = (DownloadEntry) it.next();
            if (downloadEntry.download.getId() == download.getId()) {
                downloadEntry.download = download;
                break;
            }
        }
        this.mainHandler.postDelayed(this.updateDownloadsRunnable, 1000);
    }

    private void initializeListView() {
        this.currentDownloads = new ArrayList();
        this.list = (ListView) findViewById(R.id.downloadList);
        this.adapter = new ArrayAdapter<DownloadEntry>(this, R.layout.download_list_item, this.currentDownloads) {
            public View getView(int i, View view, ViewGroup viewGroup) {
                View inflate;
                Exception e;
                int i2 = i;
                try {
                    DownloadEntry downloadEntry = (DownloadEntry) Downloads.this.currentDownloads.get(i2);
                    inflate = view == null ? Downloads.this.getLayoutInflater().inflate(R.layout.download_list_item, null) : view;
                    try {
                        boolean isMovieWatched;
                        if (downloadEntry.metadata.season <= 0 || downloadEntry.metadata.episode <= 0) {
                            isMovieWatched = Trakt.isMovieWatched(downloadEntry.metadata.imdb);
                        } else {
                            isMovieWatched = Trakt.isEpisodeWatched(downloadEntry.metadata.imdb, downloadEntry.metadata.season, downloadEntry.metadata.episode);
                        }
                        if (isMovieWatched) {
                            inflate.findViewById(R.id.watched).setVisibility(0);
                        } else {
                            inflate.findViewById(R.id.watched).setVisibility(8);
                        }
                        TextView textView = (TextView) inflate.findViewById(R.id.media_name);
                        CharSequence charSequence = downloadEntry.metadata.titleWithSE;
                        if (downloadEntry.metadata.movie) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(charSequence);
                            stringBuilder.append(" (");
                            stringBuilder.append(String.valueOf(downloadEntry.metadata.year));
                            stringBuilder.append(")");
                            charSequence = stringBuilder.toString();
                        }
                        textView.setText(charSequence);
                        ((TextView) inflate.findViewById(R.id.source_info)).setText(downloadEntry.metadata.source.getSource());
                        ((TextView) inflate.findViewById(R.id.media_title)).setText(Utils.getFilenameFromUrl(downloadEntry.download.getUrl(), downloadEntry.metadata.titleWithSE));
                        TextView textView2 = (TextView) inflate.findViewById(R.id.downloadStatus);
                        textView2.setText(downloadEntry.download.getStatus().toString());
                        Status status = downloadEntry.download.getStatus();
                        int progress = downloadEntry.download.getProgress();
                        TextView textView3 = (TextView) inflate.findViewById(R.id.downloadProgress);
                        ProgressBar progressBar = (ProgressBar) inflate.findViewById(R.id.progressBar);
                        if (progress <= -1 || status == Status.COMPLETED) {
                            textView3.setText(String.format("%s", new Object[]{Utils.formatSize(Downloads.this, downloadEntry.download.getDownloaded())}));
                            progressBar.setVisibility(4);
                        } else {
                            r15 = new Object[3];
                            int i3 = progress;
                            r15[1] = Utils.formatSize(Downloads.this, downloadEntry.download.getDownloaded());
                            r15[2] = Utils.formatSize(Downloads.this, downloadEntry.download.getTotal());
                            textView3.setText(String.format("%s%%       %s/%s", r15));
                            progressBar.setMax(100);
                            progressBar.setProgress(i3);
                            progressBar.setVisibility(0);
                            if (status != Status.PAUSED) {
                                if (status != Status.FAILED) {
                                    progressBar.getProgressDrawable().setColorFilter(-1, Mode.SRC_IN);
                                }
                            }
                            progressBar.getProgressDrawable().setColorFilter(InputDeviceCompat.SOURCE_ANY, Mode.SRC_IN);
                        }
                        if (status == Status.COMPLETED) {
                            textView2.setTextColor(-16711936);
                        } else {
                            if (status != Status.PAUSED) {
                                if (status != Status.FAILED) {
                                    textView2.setTextColor(-1);
                                }
                            }
                            textView2.setTextColor(InputDeviceCompat.SOURCE_ANY);
                        }
                        if (Downloads.this.currentSelection == i2) {
                            inflate.setBackgroundResource(R.drawable.button_selector_fixed);
                        } else {
                            inflate.setBackgroundResource(0);
                        }
                        Downloads.this.loadImage(downloadEntry, (ImageView) inflate.findViewById(R.id.mediaPoster));
                    } catch (Exception e2) {
                        e = e2;
                        e.printStackTrace();
                        return inflate;
                    }
                } catch (Exception e3) {
                    e = e3;
                    inflate = view;
                    e.printStackTrace();
                    return inflate;
                }
                return inflate;
            }
        };
        this.list.setAdapter(this.adapter);
        this.list.setEmptyView(findViewById(R.id.empty_list_view));
        this.list.setOnItemLongClickListener(new C04074());
        this.list.setOnItemSelectedListener(new C04085());
        this.list.setOnItemClickListener(new C04096());
        this.list.requestFocus();
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 82) {
            return super.onKeyUp(i, keyEvent);
        }
        if (!(this.list == 0 || this.list.getSelectedItem() == 0)) {
            showContextMenu((DownloadEntry) this.list.getSelectedItem());
        }
        return true;
    }

    private void showContextMenu(final DownloadEntry downloadEntry) {
        Status status = downloadEntry.download.getStatus();
        final List arrayList = new ArrayList();
        if (status == Status.DOWNLOADING) {
            arrayList.add(getString(R.string.option_download_pause));
        }
        if (status == Status.PAUSED) {
            arrayList.add(getString(R.string.option_download_resume));
        }
        if (status == Status.FAILED || status == Status.CANCELLED) {
            arrayList.add(getString(R.string.option_download_retry));
        }
        if (!(status == Status.FAILED || status == Status.CANCELLED || status == Status.COMPLETED)) {
            if (downloadEntry.download.getPriority() != Priority.LOW) {
                arrayList.add(getString(R.string.option_download_priority_low));
            }
            if (downloadEntry.download.getPriority() != Priority.NORMAL) {
                arrayList.add(getString(R.string.option_download_priority_normal));
            }
            if (downloadEntry.download.getPriority() != Priority.HIGH) {
                arrayList.add(getString(R.string.option_download_priority_high));
            }
        }
        arrayList.add(getString(R.string.option_download_cancel));
        if (downloadEntry.metadata.season > 0 && downloadEntry.metadata.episode > 0) {
            arrayList.add(getString(R.string.gotoSeason));
            arrayList.add(getString(R.string.autofetch_dialog_option_next));
        }
        CharSequence[] charSequenceArr = (CharSequence[]) arrayList.toArray(new CharSequence[arrayList.size()]);
        Builder builder = new Builder(this);
        builder.setTitle(downloadEntry.metadata.titleWithSE);
        builder.setItems(charSequenceArr, new OnClickListener() {

            /* renamed from: com.android.morpheustv.content.Downloads$7$1 */
            class C04121 implements OnActionListener {

                /* renamed from: com.android.morpheustv.content.Downloads$7$1$2 */
                class C04112 implements Runnable {
                    C04112() {
                    }

                    public void run() {
                        Toast.makeText(Downloads.this, Downloads.this.getString(R.string.trakt_error), 0).show();
                    }
                }

                C04121() {
                }

                public void onSuccess(final Object obj) {
                    Downloads.this.runOnUiThread(new Runnable() {
                        public void run() {
                            ArrayList arrayList = (ArrayList) obj;
                            if (arrayList != null) {
                                Object obj;
                                String str;
                                String format;
                                String str2;
                                double d;
                                int i;
                                String str3;
                                int i2;
                                int i3;
                                Iterator it = arrayList.iterator();
                                while (it.hasNext()) {
                                    SeasonResult seasonResult = (SeasonResult) it.next();
                                    if (seasonResult.season.number.intValue() == downloadEntry.metadata.season) {
                                        for (EpisodeResult episodeResult : seasonResult.episodes) {
                                            if (episodeResult.episode.number.intValue() == downloadEntry.metadata.episode + 1) {
                                                obj = 1;
                                                break;
                                            }
                                        }
                                        obj = null;
                                        if (obj == null) {
                                            str = downloadEntry.metadata.titleSimple;
                                            format = String.format("%s S%02dE%02d", new Object[]{downloadEntry.metadata.titleSimple, Integer.valueOf(downloadEntry.metadata.season), Integer.valueOf(downloadEntry.metadata.episode + 1)});
                                            str2 = downloadEntry.metadata.overview;
                                            d = downloadEntry.metadata.rating;
                                            i = downloadEntry.metadata.year;
                                            str3 = downloadEntry.metadata.imdb;
                                            i2 = downloadEntry.metadata.tmdb;
                                            i3 = downloadEntry.metadata.episodeId;
                                            Downloads.this.downloadBestSource(downloadEntry.metadata.titleSimple, "", str, format, str2, d, i, str3, i2, i3, downloadEntry.metadata.season, downloadEntry.metadata.episode + 1);
                                            return;
                                        }
                                        Toast.makeText(Downloads.this, Downloads.this.getString(R.string.autofetch_dialog_option_not_exist), 0).show();
                                        return;
                                    }
                                }
                                obj = null;
                                if (obj == null) {
                                    Toast.makeText(Downloads.this, Downloads.this.getString(R.string.autofetch_dialog_option_not_exist), 0).show();
                                    return;
                                }
                                str = downloadEntry.metadata.titleSimple;
                                format = String.format("%s S%02dE%02d", new Object[]{downloadEntry.metadata.titleSimple, Integer.valueOf(downloadEntry.metadata.season), Integer.valueOf(downloadEntry.metadata.episode + 1)});
                                str2 = downloadEntry.metadata.overview;
                                d = downloadEntry.metadata.rating;
                                i = downloadEntry.metadata.year;
                                str3 = downloadEntry.metadata.imdb;
                                i2 = downloadEntry.metadata.tmdb;
                                i3 = downloadEntry.metadata.episodeId;
                                Downloads.this.downloadBestSource(downloadEntry.metadata.titleSimple, "", str, format, str2, d, i, str3, i2, i3, downloadEntry.metadata.season, downloadEntry.metadata.episode + 1);
                                return;
                            }
                            Toast.makeText(Downloads.this, Downloads.this.getString(R.string.autofetch_dialog_option_not_exist), 0).show();
                        }
                    });
                }

                public void onFail(int i) {
                    Downloads.this.runOnUiThread(new C04112());
                }
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (Downloads.this.getFetchInstance() != null) {
                    dialogInterface = ((CharSequence) arrayList.get(i)).toString();
                    if (dialogInterface.equals(Downloads.this.getString(R.string.option_download_pause)) != 0) {
                        Downloads.this.getFetchInstance().pause(new int[]{downloadEntry.download.getId()});
                    }
                    if (dialogInterface.equals(Downloads.this.getString(R.string.option_download_resume)) != 0) {
                        Downloads.this.getFetchInstance().resume(new int[]{downloadEntry.download.getId()});
                    }
                    if (dialogInterface.equals(Downloads.this.getString(R.string.option_download_retry)) != 0) {
                        Downloads.this.getFetchInstance().retry(new int[]{downloadEntry.download.getId()});
                    }
                    if (dialogInterface.equals(Downloads.this.getString(R.string.option_download_cancel)) != 0) {
                        Downloads.this.getFetchInstance().delete(new int[]{downloadEntry.download.getId()});
                        Downloads.this.setDownloadMetadata(downloadEntry.download.getId(), null);
                    }
                    if (dialogInterface.equals(Downloads.this.getString(R.string.option_download_priority_low)) != 0) {
                        i = new RequestInfo();
                        i.setPriority(Priority.LOW);
                        Downloads.this.getFetchInstance().updateRequest(downloadEntry.download.getId(), i, null, null);
                    }
                    if (dialogInterface.equals(Downloads.this.getString(R.string.option_download_priority_normal)) != 0) {
                        i = new RequestInfo();
                        i.setPriority(Priority.NORMAL);
                        Downloads.this.getFetchInstance().updateRequest(downloadEntry.download.getId(), i, null, null);
                    }
                    if (dialogInterface.equals(Downloads.this.getString(R.string.option_download_priority_high)) != 0) {
                        i = new RequestInfo();
                        i.setPriority(Priority.HIGH);
                        Downloads.this.getFetchInstance().updateRequest(downloadEntry.download.getId(), i, null, null);
                    }
                    if (dialogInterface.equals(Downloads.this.getString(R.string.gotoSeason)) != 0) {
                        i = new Intent(Downloads.this, SeasonList.class);
                        i.putExtra("title", downloadEntry.metadata.titleSimple);
                        i.putExtra("year", downloadEntry.metadata.year);
                        i.putExtra("imdb", downloadEntry.metadata.imdb);
                        i.putExtra("tmdb", downloadEntry.metadata.tmdb);
                        i.putExtra("overview", downloadEntry.metadata.overview);
                        i.putExtra("redirectToSeason", downloadEntry.metadata.season);
                        Downloads.this.startActivityForResult(i, 5000);
                    }
                    if (dialogInterface.equals(Downloads.this.getString(R.string.autofetch_dialog_option_next)) != null) {
                        Trakt.getInstance().getSeasonsShow(downloadEntry.metadata.imdb, new C04121());
                    }
                    Downloads.this.refreshDownloads();
                }
            }
        });
        builder.create().show();
    }

    public void onQueued(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download enqueued: ");
        stringBuilder.append(download.getUrl());
        Log.d("Downloads", stringBuilder.toString());
        refreshDownloads();
    }

    public void onCompleted(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download completed: ");
        stringBuilder.append(download.getUrl());
        Log.d("Downloads", stringBuilder.toString());
        refreshDownloadView(download);
    }

    public void onError(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download error:");
        stringBuilder.append(download.getUrl());
        Log.d("Downloads", stringBuilder.toString());
        refreshDownloadView(download);
    }

    public void onProgress(Download download, long j, long j2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download progress:");
        stringBuilder.append(String.valueOf(j));
        stringBuilder.append(" - ");
        stringBuilder.append(String.valueOf(j2));
        Log.d("Downloads", stringBuilder.toString());
        refreshDownloadView(download);
    }

    public void onPaused(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download paused:");
        stringBuilder.append(download.getUrl());
        Log.d("Downloads", stringBuilder.toString());
        refreshDownloadView(download);
    }

    public void onResumed(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download resumed:");
        stringBuilder.append(download.getUrl());
        Log.d("Downloads", stringBuilder.toString());
        refreshDownloadView(download);
    }

    public void onCancelled(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download cancelled:");
        stringBuilder.append(download.getUrl());
        Log.d("Downloads", stringBuilder.toString());
        refreshDownloadView(download);
    }

    public void onRemoved(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download removed:");
        stringBuilder.append(download.getUrl());
        Log.d("Downloads", stringBuilder.toString());
        refreshDownloads();
    }

    public void onDeleted(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download deleted:");
        stringBuilder.append(download.getUrl());
        Log.d("Downloads", stringBuilder.toString());
        refreshDownloads();
    }

    private void loadImage(DownloadEntry downloadEntry, ImageView imageView) {
        imageView.setTag(downloadEntry);
        if (downloadEntry.images == null || downloadEntry.images.posterUrl == null || !downloadEntry.images.posterUrl.isEmpty() || downloadEntry.images.isLoading) {
            setImage(downloadEntry, imageView);
            return;
        }
        new LoadPosterTask(downloadEntry, imageView).executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        downloadEntry.images.isLoading = true;
    }

    private void setImage(DownloadEntry downloadEntry, ImageView imageView) {
        try {
            if (imageView.getTag() == downloadEntry && downloadEntry.images != null && downloadEntry.images.posterUrl != null && !downloadEntry.images.posterUrl.isEmpty()) {
                downloadEntry.images.isLoading = false;
                imageView.setTag(null);
                Glide.with((FragmentActivity) this).load(downloadEntry.images.posterUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder((int) R.drawable.poster).fitCenter().into(imageView);
                DownloadEntry downloadEntry2 = (DownloadEntry) this.adapter.getItem(this.currentSelection);
                if (downloadEntry2 != null && downloadEntry2 == downloadEntry && downloadEntry.images != null && downloadEntry.images.backdropUrl != null && downloadEntry.images.backdropUrl.isEmpty() == null && this.currentBackdrop.equals(downloadEntry.images.backdropUrl) == null) {
                    this.currentBackdrop = downloadEntry.images.backdropUrl;
                    Glide.with((FragmentActivity) this).load(downloadEntry.images.backdropUrl).diskCacheStrategy((DiskCacheStrategy) DiskCacheStrategy.ALL).into(this.mBackdrop);
                }
            }
        } catch (DownloadEntry downloadEntry3) {
            downloadEntry3.printStackTrace();
        }
    }
}
