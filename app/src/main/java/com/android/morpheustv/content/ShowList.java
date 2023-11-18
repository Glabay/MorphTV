package com.android.morpheustv.content;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.morpheustv.BaseActivity;
import com.android.morpheustv.helpers.TaskManager;
import com.android.morpheustv.helpers.Tmdb;
import com.android.morpheustv.helpers.Trakt;
import com.android.morpheustv.helpers.Trakt.CompleteListener;
import com.android.morpheustv.helpers.Trakt.OnActionListener;
import com.android.morpheustv.helpers.Trakt.ShowResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.actions.SearchIntents;
import com.noname.titan.R;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class ShowList extends BaseActivity {
    public static final int CONTENT_COLLECTION = 3;
    public static final int CONTENT_POPULAR = 2;
    public static final int CONTENT_SEARCH = 4;
    public static final int CONTENT_TRENDING = 1;
    public static final int CONTENT_WATCHLIST = 5;
    private ArrayAdapter<ShowResult> adapter;
    private String currentBackdrop = "";
    private int currentContent = 1;
    private int currentPage = 1;
    private int currentSelection = 0;
    private boolean isForeground = true;
    GridView list;
    private boolean loading = false;
    private ImageView mBackdrop;
    private String searchQuery = "";
    private ArrayList<ShowResult> shows;

    /* renamed from: com.android.morpheustv.content.ShowList$3 */
    class C04753 implements OnScrollListener {
        public void onScrollStateChanged(AbsListView absListView, int i) {
        }

        C04753() {
        }

        public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            if (ShowList.this.currentContent != 3 && ShowList.this.currentContent != 5 && i + i2 == i3 && i3 != 0) {
                ShowList.this.loadContent(ShowList.this.currentContent, ShowList.this.currentPage + 1);
            }
        }
    }

    /* renamed from: com.android.morpheustv.content.ShowList$4 */
    class C04764 implements OnItemLongClickListener {
        C04764() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (ShowList.this.shows != null && ShowList.this.shows.size() > null && i < ShowList.this.shows.size()) {
                ShowList.this.showContextMenu((ShowResult) ShowList.this.shows.get(i));
            }
            return true;
        }
    }

    /* renamed from: com.android.morpheustv.content.ShowList$5 */
    class C04775 implements OnItemSelectedListener {
        C04775() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            if (ShowList.this.shows != null && ShowList.this.shows.size() > null && i < ShowList.this.shows.size()) {
                ShowResult showResult = (ShowResult) ShowList.this.shows.get(i);
                ShowList.this.currentSelection = i;
                ShowList.this.setOverview(showResult.show.overview);
                if (showResult.images == null || showResult.images.backdropUrl == null || showResult.images.backdropUrl.isEmpty() != null || ShowList.this.currentBackdrop.equals(showResult.images.backdropUrl) != null) {
                    ShowList.this.currentBackdrop = "";
                    ShowList.this.mBackdrop.setImageResource(null);
                } else {
                    ShowList.this.currentBackdrop = showResult.images.backdropUrl;
                    Glide.with(ShowList.this).load(showResult.images.backdropUrl).diskCacheStrategy((DiskCacheStrategy) DiskCacheStrategy.ALL).into(ShowList.this.mBackdrop);
                }
                ShowList.this.adapter.notifyDataSetChanged();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            ShowList.this.setOverview("");
        }
    }

    /* renamed from: com.android.morpheustv.content.ShowList$6 */
    class C04786 implements OnItemClickListener {
        C04786() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (ShowList.this.shows != null && ShowList.this.shows.size() > null && i < ShowList.this.shows.size()) {
                ShowResult showResult = (ShowResult) ShowList.this.shows.get(i);
                if (i == ShowList.this.currentSelection) {
                    view = new Intent(ShowList.this, SeasonList.class);
                    view.putExtra("title", showResult.show.title);
                    view.putExtra("year", showResult.show.year);
                    view.putExtra("imdb", showResult.show.ids.imdb);
                    view.putExtra("tmdb", showResult.show.ids.tmdb);
                    view.putExtra("overview", showResult.show.overview);
                    view.putExtra("tmdb", showResult.show.ids.tmdb);
                    view.putExtra("backdrop", showResult.images.backdropUrl);
                    ShowList.this.startActivityForResult(view, 5000);
                    return;
                }
                ShowList.this.currentSelection = i;
                ShowList.this.setOverview(showResult.show.overview);
                if (showResult.images == null || showResult.images.backdropUrl == null || showResult.images.backdropUrl.isEmpty() != null || ShowList.this.currentBackdrop.equals(showResult.images.backdropUrl) != null) {
                    ShowList.this.currentBackdrop = "";
                    ShowList.this.mBackdrop.setImageResource(null);
                } else {
                    ShowList.this.currentBackdrop = showResult.images.backdropUrl;
                    Glide.with(ShowList.this).load(showResult.images.backdropUrl).diskCacheStrategy((DiskCacheStrategy) DiskCacheStrategy.ALL).into(ShowList.this.mBackdrop);
                }
                ShowList.this.adapter.notifyDataSetChanged();
            }
        }
    }

    /* renamed from: com.android.morpheustv.content.ShowList$8 */
    class C04868 implements Comparator<ShowResult> {
        C04868() {
        }

        public int compare(ShowResult showResult, ShowResult showResult2) {
            return showResult.show.title.compareTo(showResult2.show.title);
        }
    }

    /* renamed from: com.android.morpheustv.content.ShowList$9 */
    class C04879 implements Comparator<ShowResult> {
        C04879() {
        }

        public int compare(ShowResult showResult, ShowResult showResult2) {
            return showResult.show.title.compareTo(showResult2.show.title);
        }
    }

    class LoadPosterTask extends AsyncTask<Void, Void, Void> {
        ShowResult show = null;
        ImageView view = null;

        public LoadPosterTask(ShowResult showResult, ImageView imageView) {
            this.view = imageView;
            this.show = showResult;
        }

        protected Void doInBackground(Void... voidArr) {
            try {
                if (ShowList.this.isForeground != null) {
                    this.show.images = Tmdb.getShowImages(this.show.show.ids.tmdb, "en,null");
                }
            } catch (Void[] voidArr2) {
                voidArr2.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void voidR) {
            if (ShowList.this.isForeground != null) {
                ShowList.this.setImage(this.show, this.view);
            }
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_media_list);
        this.mBackdrop = (ImageView) findViewById(R.id.backdropImage);
        this.currentContent = getIntent().getIntExtra("content", 1);
        this.searchQuery = getIntent().getStringExtra(SearchIntents.EXTRA_QUERY);
        rotateLoading();
        initializeListView();
        if (this.currentContent == 2) {
            setBreadcrumb(R.drawable.highly_rated, getString(R.string.shows_popular), null);
        }
        if (this.currentContent == 1) {
            setBreadcrumb(R.drawable.featured, getString(R.string.shows_trending), null);
        }
        if (this.currentContent == 3) {
            setBreadcrumb(R.drawable.trakt, getString(R.string.shows_collection), null);
        }
        if (this.currentContent == 5) {
            setBreadcrumb(R.drawable.trakt, getString(R.string.shows_watchlist), null);
        }
        if (this.currentContent == 4) {
            setBreadcrumb(R.drawable.search, getString(R.string.search), this.searchQuery);
        }
        loadContent(this.currentContent, this.currentPage);
    }

    protected void onResume() {
        super.onResume();
        this.isForeground = true;
        if (this.adapter != null) {
            Trakt.updateWatchedStatus(this.shows);
            this.adapter.notifyDataSetChanged();
        }
    }

    protected void onStop() {
        super.onStop();
        this.isForeground = false;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 82) {
            return super.onKeyUp(i, keyEvent);
        }
        if (!(this.list == 0 || this.list.getSelectedItem() == 0)) {
            showContextMenu((ShowResult) this.list.getSelectedItem());
        }
        return true;
    }

    public void rotateLoading() {
        Animation rotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setRepeatMode(-1);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        findViewById(R.id.loadingview).startAnimation(rotateAnimation);
    }

    public void setBreadcrumb(int i, String str, String str2) {
        ((TextView) findViewById(R.id.breadcrumb_title)).setText(str);
        if (i > 0) {
            ((ImageView) findViewById(R.id.breadcrumb_image)).setImageResource(i);
            ((ImageView) findViewById(R.id.breadcrumb_image)).setVisibility(0);
        } else {
            ((ImageView) findViewById(R.id.breadcrumb_image)).setVisibility(8);
        }
        if (str2 == null || str2.isEmpty()) {
            ((TextView) findViewById(R.id.breadcrumb_secondary_title)).setVisibility(8);
            return;
        }
        ((TextView) findViewById(R.id.breadcrumb_secondary_title)).setVisibility(0);
        ((TextView) findViewById(R.id.breadcrumb_secondary_title)).setText(str2);
    }

    private void loadContent(int i, final int i2) {
        if (!this.loading) {
            OnActionListener c04731 = new OnActionListener() {
                public void onSuccess(final Object obj) {
                    ShowList.this.runOnUiThread(new Runnable() {
                        public void run() {
                            ShowList.this.addItems((ArrayList) obj);
                            ShowList.this.currentPage = i2;
                            ShowList.this.loading = false;
                        }
                    });
                }

                public void onFail(final int i) {
                    ShowList.this.runOnUiThread(new Runnable() {

                        /* renamed from: com.android.morpheustv.content.ShowList$1$2$1 */
                        class C04711 implements CompleteListener {
                            C04711() {
                            }

                            public void OnSuccess() {
                                ShowList.this.loadContent(ShowList.this.currentContent, ShowList.this.currentPage);
                            }

                            public void OnFail() {
                                ShowList.this.finish();
                            }
                        }

                        public void run() {
                            ShowList.this.loading = false;
                            if (i == 401) {
                                Trakt.beginAuthFlow(ShowList.this, new C04711());
                            } else {
                                ShowList.this.showError(ShowList.this.getString(R.string.trakt_error));
                            }
                        }
                    });
                }
            };
            this.loading = true;
            if (i == 1) {
                Trakt.getInstance().getTrendingShows(i2, c04731);
            }
            if (i == 2) {
                Trakt.getInstance().getPopularShows(i2, c04731);
            }
            if (i == 3) {
                Trakt.getInstance().getShowCollection(c04731);
            }
            if (i == 5) {
                Trakt.getInstance().getShowsWatchlist(c04731);
            }
            if (i == 4) {
                Trakt.getInstance().searchShows(this.searchQuery, i2, c04731);
            }
        }
    }

    private void showError(String str) {
        if (this.shows == null || this.shows.isEmpty()) {
            findViewById(R.id.error_notification).setVisibility(0);
            ((TextView) findViewById(R.id.error_message)).setText(str);
            findViewById(R.id.continue_button).setVisibility(8);
            findViewById(R.id.empty_list_view).setVisibility(8);
        }
    }

    private void initializeListView() {
        this.shows = new ArrayList();
        this.list = (GridView) findViewById(R.id.medialist);
        this.adapter = new ArrayAdapter<ShowResult>(this, R.layout.movie_list_item, this.shows) {
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    try {
                        view = ShowList.this.getLayoutInflater().inflate(R.layout.movie_list_item, null);
                    } catch (int i2) {
                        i2.printStackTrace();
                    }
                }
                ShowResult showResult = (ShowResult) ShowList.this.shows.get(i2);
                ((TextView) view.findViewById(R.id.title)).setText(showResult.show.title);
                ((TextView) view.findViewById(R.id.year)).setText(String.valueOf(showResult.show.year));
                if (showResult.watched) {
                    view.findViewById(R.id.watched).setVisibility(0);
                } else {
                    view.findViewById(R.id.watched).setVisibility(8);
                }
                TextView textView = (TextView) view.findViewById(R.id.rating);
                View findViewById = view.findViewById(R.id.rating_container);
                if (showResult.show.rating != null) {
                    textView.setText(String.format("%.1f", new Object[]{showResult.show.rating}).replace(",", "."));
                    findViewById.setVisibility(0);
                } else {
                    findViewById.setVisibility(8);
                }
                ImageView imageView = (ImageView) view.findViewById(R.id.poster);
                imageView.setImageResource(R.drawable.poster);
                ShowList.this.loadImage(showResult, imageView);
                if (ShowList.this.currentSelection == i2) {
                    view.setBackgroundResource(R.drawable.button_selector_fixed);
                } else {
                    view.setBackgroundResource(0);
                }
                return view;
            }
        };
        this.list.setAdapter(this.adapter);
        this.list.setEmptyView(findViewById(R.id.empty_list_view));
        this.list.setOnScrollListener(new C04753());
        this.list.setOnItemLongClickListener(new C04764());
        this.list.setOnItemSelectedListener(new C04775());
        this.list.setOnItemClickListener(new C04786());
    }

    private void showContextMenu(final ShowResult showResult) {
        String string = getString(R.string.option_mark_watched);
        if (showResult.watched) {
            string = getString(R.string.option_mark_unwatched);
        }
        String string2 = getString(R.string.option_add_collection);
        if (this.currentContent == 3) {
            string2 = getString(R.string.option_del_collection);
        }
        String string3 = getString(R.string.option_add_watchlist);
        if (this.currentContent == 5) {
            string3 = getString(R.string.option_del_watchlist);
        }
        final CharSequence[] charSequenceArr = new CharSequence[]{string, string2, string3};
        Builder builder = new Builder(this);
        builder.setTitle(showResult.show.title);
        builder.setItems(charSequenceArr, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, final int i) {
                dialogInterface.dismiss();
                dialogInterface = new ProgressDialog(ShowList.this);
                dialogInterface.setIndeterminate(true);
                dialogInterface.setCancelable(false);
                dialogInterface.setTitle(charSequenceArr[i]);
                dialogInterface.setMessage(ShowList.this.getString(R.string.please_wait_mark_watched));
                dialogInterface.show();
                final OnActionListener c04801 = new OnActionListener() {

                    /* renamed from: com.android.morpheustv.content.ShowList$7$1$1 */
                    class C04791 implements Runnable {
                        C04791() {
                        }

                        public void run() {
                            ShowList.this.adapter.notifyDataSetChanged();
                            dialogInterface.dismiss();
                        }
                    }

                    public void onSuccess(Object obj) {
                        Trakt.syncShowCache(ShowList.this.getApplicationContext(), showResult.show.ids.imdb);
                        Trakt.updateWatchedStatus(ShowList.this.shows);
                        ShowList.this.runOnUiThread(new C04791());
                    }

                    public void onFail(int i) {
                        Context context = ShowList.this;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Operation failed with error ");
                        stringBuilder.append(String.valueOf(i));
                        Toast.makeText(context, stringBuilder.toString(), 0).show();
                        dialogInterface.dismiss();
                    }
                };
                OnActionListener c04842 = new OnActionListener() {

                    /* renamed from: com.android.morpheustv.content.ShowList$7$2$1 */
                    class C04811 implements Runnable {
                        C04811() {
                        }

                        public void run() {
                            ShowList.this.adapter.notifyDataSetChanged();
                            dialogInterface.dismiss();
                        }
                    }

                    public void onSuccess(Object obj) {
                        Trakt.syncShowCache(ShowList.this.getApplicationContext(), showResult.show.ids.imdb);
                        Trakt.updateWatchedStatus(ShowList.this.shows);
                        ShowList.this.runOnUiThread(new C04811());
                    }

                    public void onFail(final int i) {
                        ShowList.this.runOnUiThread(new Runnable() {

                            /* renamed from: com.android.morpheustv.content.ShowList$7$2$2$1 */
                            class C04821 implements CompleteListener {
                                C04821() {
                                }

                                public void OnSuccess() {
                                    switch (i) {
                                        case 0:
                                            showResult.watched ^= true;
                                            Trakt.getInstance().markShowWatched(showResult.show.ids, showResult.watched, c04801);
                                            ShowList.this.adapter.notifyDataSetChanged();
                                            return;
                                        case 1:
                                            if (ShowList.this.currentContent == 3) {
                                                Trakt.getInstance().showCollect(showResult.show.ids, false, c04801);
                                                ShowList.this.shows.remove(showResult);
                                                ShowList.this.adapter.notifyDataSetChanged();
                                                return;
                                            }
                                            Trakt.getInstance().showCollect(showResult.show.ids, true, c04801);
                                            return;
                                        case 2:
                                            if (ShowList.this.currentContent == 5) {
                                                Trakt.getInstance().showAddWatchlist(showResult.show.ids, false, c04801);
                                                ShowList.this.shows.remove(showResult);
                                                ShowList.this.adapter.notifyDataSetChanged();
                                                return;
                                            }
                                            Trakt.getInstance().showAddWatchlist(showResult.show.ids, true, c04801);
                                            return;
                                        default:
                                            return;
                                    }
                                }

                                public void OnFail() {
                                    Context context = ShowList.this;
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("Operation failed with error ");
                                    stringBuilder.append(String.valueOf(i));
                                    Toast.makeText(context, stringBuilder.toString(), 0).show();
                                    dialogInterface.dismiss();
                                }
                            }

                            public void run() {
                                if (i == 401) {
                                    Trakt.beginAuthFlow(ShowList.this, new C04821());
                                    return;
                                }
                                Context context = ShowList.this;
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Operation failed with error ");
                                stringBuilder.append(String.valueOf(i));
                                Toast.makeText(context, stringBuilder.toString(), 0).show();
                                dialogInterface.dismiss();
                            }
                        });
                    }
                };
                switch (i) {
                    case 0:
                        showResult.watched ^= 1;
                        Trakt.getInstance().markShowWatched(showResult.show.ids, showResult.watched, c04842);
                        ShowList.this.adapter.notifyDataSetChanged();
                        return;
                    case 1:
                        if (ShowList.this.currentContent == 3) {
                            Trakt.getInstance().showCollect(showResult.show.ids, false, c04842);
                            ShowList.this.shows.remove(showResult);
                            ShowList.this.adapter.notifyDataSetChanged();
                            return;
                        }
                        Trakt.getInstance().showCollect(showResult.show.ids, true, c04842);
                        return;
                    case 2:
                        if (ShowList.this.currentContent == 5) {
                            Trakt.getInstance().showAddWatchlist(showResult.show.ids, false, c04842);
                            ShowList.this.shows.remove(showResult);
                            ShowList.this.adapter.notifyDataSetChanged();
                            return;
                        }
                        Trakt.getInstance().showAddWatchlist(showResult.show.ids, true, c04842);
                        return;
                    default:
                        return;
                }
            }
        });
        builder.create().show();
    }

    private void setOverview(String str) {
        ((TextView) findViewById(R.id.overview)).setText(str);
    }

    private void addItems(ArrayList<ShowResult> arrayList) {
        if (arrayList != null) {
            try {
                if (arrayList.size() > 0) {
                    Collection arrayList2 = new ArrayList();
                    arrayList = arrayList.iterator();
                    while (arrayList.hasNext()) {
                        ShowResult showResult = (ShowResult) arrayList.next();
                        Object obj = null;
                        Iterator it = this.shows.iterator();
                        while (it.hasNext()) {
                            ShowResult showResult2 = (ShowResult) it.next();
                            if (showResult.show.ids != null && showResult.show.ids.imdb != null && showResult.show.ids.imdb.equals(showResult2.show.ids.imdb)) {
                                obj = 1;
                                break;
                            }
                        }
                        if (obj == null) {
                            arrayList2.add(showResult);
                        }
                    }
                    this.shows.addAll(arrayList2);
                    if (this.currentContent == 3) {
                        arrayList = new ArrayList();
                        arrayList2 = new ArrayList();
                        Iterator it2 = this.shows.iterator();
                        while (it2.hasNext()) {
                            ShowResult showResult3 = (ShowResult) it2.next();
                            if (showResult3.watched) {
                                arrayList.add(showResult3);
                            } else {
                                arrayList2.add(showResult3);
                            }
                        }
                        Collections.sort(arrayList, new C04868());
                        Collections.sort(arrayList2, new C04879());
                        this.shows.clear();
                        this.shows.addAll(arrayList2);
                        this.shows.addAll(arrayList);
                    }
                    this.adapter.notifyDataSetChanged();
                    this.list.requestFocus();
                    if (this.shows.size() > null && this.currentSelection < this.shows.size()) {
                        setOverview(((ShowResult) this.shows.get(this.currentSelection)).show.overview);
                    }
                }
            } catch (ArrayList<ShowResult> arrayList3) {
                arrayList3.printStackTrace();
                return;
            }
        }
        if (this.shows != null && this.shows.isEmpty() != null) {
            showError(getString(R.string.no_content_in_list_trakt));
        }
    }

    private void loadImage(ShowResult showResult, ImageView imageView) {
        imageView.setTag(showResult);
        if (showResult.images == null || showResult.images.posterUrl == null || !showResult.images.posterUrl.isEmpty() || showResult.images.isLoading) {
            setImage(showResult, imageView);
            return;
        }
        new LoadPosterTask(showResult, imageView).executeOnExecutor(TaskManager.EXECUTOR, new Void[0]);
        showResult.images.isLoading = true;
    }

    private void setImage(ShowResult showResult, ImageView imageView) {
        try {
            if (imageView.getTag() == showResult && showResult.images != null && showResult.images.posterUrl != null && !showResult.images.posterUrl.isEmpty()) {
                showResult.images.isLoading = false;
                imageView.setTag(null);
                Glide.with((FragmentActivity) this).load(showResult.images.posterUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder((int) R.drawable.poster).fitCenter().into(imageView);
                ShowResult showResult2 = (ShowResult) this.adapter.getItem(this.currentSelection);
                if (showResult2 != null && showResult2 == showResult && showResult.images != null && showResult.images.backdropUrl != null && showResult.images.backdropUrl.isEmpty() == null && this.currentBackdrop.equals(showResult.images.backdropUrl) == null) {
                    this.currentBackdrop = showResult.images.backdropUrl;
                    Glide.with((FragmentActivity) this).load(showResult.images.backdropUrl).diskCacheStrategy((DiskCacheStrategy) DiskCacheStrategy.ALL).into(this.mBackdrop);
                }
            }
        } catch (ShowResult showResult3) {
            showResult3.printStackTrace();
        }
    }
}
