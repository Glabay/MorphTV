package com.android.morpheustv;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.InputDeviceCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.morpheustv.content.Downloads;
import com.android.morpheustv.content.Information;
import com.android.morpheustv.content.MoviesMain;
import com.android.morpheustv.content.NextEpisodesList;
import com.android.morpheustv.content.ShowsMain;
import com.android.morpheustv.helpers.AppUpdater;
import com.android.morpheustv.helpers.AppUpdater.AppUpdateListener;
import com.android.morpheustv.helpers.AppUpdater.UpdateData;
import com.android.morpheustv.helpers.InformationUpdater;
import com.android.morpheustv.helpers.InformationUpdater.InfoEntry;
import com.android.morpheustv.helpers.InformationUpdater.InfoEntryList;
import com.android.morpheustv.helpers.InformationUpdater.InformationListener;
import com.android.morpheustv.helpers.Utils;
import com.android.morpheustv.settings.Settings;
import com.android.morpheustv.settings.SettingsActivity;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.noname.titan.R;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.Func;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2.Status;
import java.io.File;
import org.apache.commons.lang3.StringUtils;

public class MainActivity extends BaseActivity implements AppUpdateListener, InformationListener {
    private static final int REQUEST_SETTINGS = 5001;
    private static final String TORRENT = "TORRENT";
    public FetchListener fetchListener = new C03904();
    LinearLayout infoButton;
    private long lastExitRequest = 0;
    Fetch updateFetch;
    LinearLayout updateProgress;
    UpdateData updateResponse;
    Button updatebutton;

    /* renamed from: com.android.morpheustv.MainActivity$1 */
    class C03871 implements Runnable {
        C03871() {
        }

        public void run() {
            try {
                MainActivity.this.rotateLoading();
                MainActivity.this.findViewById(R.id.trakt_sync_notification).setVisibility(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.android.morpheustv.MainActivity$2 */
    class C03882 implements Runnable {
        C03882() {
        }

        public void run() {
            try {
                MainActivity.this.rotateLoading();
                MainActivity.this.findViewById(R.id.trakt_sync_notification).setVisibility(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.android.morpheustv.MainActivity$3 */
    class C03893 implements Runnable {
        C03893() {
        }

        public void run() {
            try {
                MainActivity.this.findViewById(R.id.trakt_sync_notification).setVisibility(8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.android.morpheustv.MainActivity$4 */
    class C03904 implements FetchListener {
        C03904() {
        }

        public void onDeleted(Download download) {
            MainActivity.this.updatebutton.setVisibility(8);
            MainActivity.this.updateProgress.setVisibility(8);
        }

        public void onRemoved(Download download) {
            MainActivity.this.updatebutton.setVisibility(8);
            MainActivity.this.updateProgress.setVisibility(8);
        }

        public void onCancelled(Download download) {
            MainActivity.this.showError(MainActivity.this.getString(R.string.error_downloading));
        }

        public void onResumed(Download download) {
            MainActivity.this.showDownloadStatus(download, 0);
        }

        public void onPaused(Download download) {
            MainActivity.this.showDownloadStatus(download, 0);
        }

        public void onProgress(Download download, long j, long j2) {
            MainActivity.this.showDownloadStatus(download, j2);
        }

        public void onError(Download download) {
            MainActivity.this.showError(MainActivity.this.getString(R.string.error_downloading));
        }

        public void onCompleted(Download download) {
            download = Uri.fromFile(new File(download.getFile())).toString();
            Intent intent = new Intent();
            intent.addFlags(ErrorDialogData.BINDER_CRASH);
            intent.setAction("android.intent.action.INSTALL_PACKAGE");
            intent.setDataAndType(Uri.parse(download), "application/vnd.android.package-archive");
            MainActivity.this.startActivity(intent);
            MainActivity.this.updatebutton.setVisibility(8);
            MainActivity.this.updateProgress.setVisibility(8);
        }

        public void onQueued(Download download) {
            MainActivity.this.showDownloadStatus(download, 0);
        }
    }

    /* renamed from: com.android.morpheustv.MainActivity$5 */
    class C03935 implements OnClickListener {

        /* renamed from: com.android.morpheustv.MainActivity$5$1 */
        class C03911 implements DialogInterface.OnClickListener {
            C03911() {
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.updateFetch.deleteAll();
                MainActivity.this.updatebutton.setVisibility(8);
                MainActivity.this.updateProgress.setVisibility(8);
                AppUpdater.CheckForUpdates(MainActivity.this);
                dialogInterface.dismiss();
            }
        }

        /* renamed from: com.android.morpheustv.MainActivity$5$2 */
        class C03922 implements DialogInterface.OnClickListener {
            C03922() {
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }

        C03935() {
        }

        public void onClick(View view) {
            if (MainActivity.this.updateFetch != null) {
                view = new Builder(MainActivity.this).create();
                view.setCancelable(false);
                view.setTitle(MainActivity.this.getString(R.string.update_found_title));
                view.setMessage(MainActivity.this.getString(R.string.update_cancel_msg));
                view.setButton(-1, MainActivity.this.getString(R.string.yes), new C03911());
                view.setButton(-2, MainActivity.this.getString(R.string.no), new C03922());
                view.show();
            }
        }
    }

    /* renamed from: com.android.morpheustv.MainActivity$6 */
    class C03946 implements Func<Download> {
        C03946() {
        }

        public void call(Download download) {
            MainActivity.this.showDownloadStatus(download, 0);
        }
    }

    /* renamed from: com.android.morpheustv.MainActivity$7 */
    class C03957 implements Func<Error> {
        C03957() {
        }

        public void call(Error error) {
            MainActivity.this.showError(MainActivity.this.getString(R.string.error_downloading));
        }
    }

    /* renamed from: com.android.morpheustv.MainActivity$9 */
    class C04009 implements Runnable {
        C04009() {
        }

        public void run() {
            MainActivity.this.updatebutton.setVisibility(8);
            MainActivity.this.updateProgress.setVisibility(8);
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        this.updateProgress = (LinearLayout) findViewById(R.id.updateProgress);
        this.updatebutton = (Button) findViewById(R.id.update_button);
        this.infoButton = (LinearLayout) findViewById(R.id.btnInfo);
        TextView textView = (TextView) findViewById(R.id.app_version);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.app_name));
        stringBuilder.append("   v");
        stringBuilder.append(BuildConfig.VERSION_NAME);
        textView.setText(stringBuilder.toString());
        if (VERSION.SDK_INT >= 23) {
            bundle = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.ACCESS_WIFI_STATE", "android.permission.WAKE_LOCK", "android.permission.RECORD_AUDIO"};
            if (hasPermissions(bundle)) {
                doInitAfterPermissions();
                return;
            } else {
                ActivityCompat.requestPermissions(this, bundle, 5000);
                return;
            }
        }
        doInitAfterPermissions();
    }

    public void onBackPressed() {
        if (this.lastExitRequest <= 0 || System.currentTimeMillis() - this.lastExitRequest >= 5000) {
            this.lastExitRequest = System.currentTimeMillis();
            Toast.makeText(this, R.string.pressBackAgainToExit, 0).show();
            return;
        }
        stopBackgroundService();
        super.onBackPressed();
    }

    public void doInitAfterPermissions() {
        Utils.disableSSLCertificateChecking();
        Utils.deleteCache(this);
        CookieManager.getInstance().setAcceptCookie(true);
        Utils.loadCacheFromDisk();
    }

    public void rotateLoading() {
        Animation rotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setRepeatMode(-1);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        View findViewById = findViewById(R.id.synclogo);
        if (findViewById == null) {
            return;
        }
        if (findViewById.getAnimation() == null || (findViewById.getAnimation() != null && findViewById.getAnimation().hasEnded())) {
            findViewById.startAnimation(rotateAnimation);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        Utils.saveCacheToDisk();
    }

    public void Movies(View view) {
        ContextCompat.startActivity(this, new Intent(this, MoviesMain.class), ActivityOptionsCompat.makeBasic().toBundle());
    }

    public void Shows(View view) {
        ContextCompat.startActivity(this, new Intent(this, ShowsMain.class), ActivityOptionsCompat.makeBasic().toBundle());
    }

    public void Downloads(View view) {
        ContextCompat.startActivity(this, new Intent(this, Downloads.class), ActivityOptionsCompat.makeBasic().toBundle());
    }

    public void Information(View view) {
        ContextCompat.startActivity(this, new Intent(this, Information.class), ActivityOptionsCompat.makeBasic().toBundle());
    }

    public void WatchNext(View view) {
        ContextCompat.startActivity(this, new Intent(this, NextEpisodesList.class), ActivityOptionsCompat.makeBasic().toBundle());
    }

    public void Settings(View view) {
        startActivityForResult(new Intent(this, SettingsActivity.class), REQUEST_SETTINGS);
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        startBackgroundService();
    }

    protected void onResume() {
        super.onResume();
        AppUpdater.CheckForUpdates(this);
        InformationUpdater.CheckForInformation(this);
        onTraktSyncComplete(false);
    }

    protected void onPause() {
        super.onPause();
        onTraktSyncComplete(false);
    }

    private boolean hasPermissions(String... strArr) {
        if (VERSION.SDK_INT >= 23 && strArr != null) {
            for (String checkSelfPermission : strArr) {
                if (ContextCompat.checkSelfPermission(this, checkSelfPermission) != 0) {
                    return false;
                }
            }
        }
        return 1;
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 5000) {
            strArr = null;
            if (iArr.length > 0 && iArr[0] == 0) {
                strArr = 1;
            }
            if (strArr != null) {
                doInitAfterPermissions();
            } else {
                finish();
            }
        }
    }

    public void onTraktSyncStart() {
        runOnUiThread(new C03871());
    }

    public void onTraktSyncProgress(String str) {
        runOnUiThread(new C03882());
    }

    public void onTraktSyncComplete(boolean z) {
        runOnUiThread(new C03893());
    }

    public void showDownloadStatus(Download download, long j) {
        this.updatebutton.setVisibility(8);
        this.updateProgress.setVisibility(0);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView textView = (TextView) findViewById(R.id.downloadProgress);
        TextView textView2 = (TextView) findViewById(R.id.downloadStatus);
        Status status = download.getStatus();
        if (status != Status.COMPLETED) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(download.getStatus().toString());
            stringBuilder.append(" @ ");
            stringBuilder.append(Utils.formatSize(this, j));
            stringBuilder.append("/s");
            textView2.setText(stringBuilder.toString());
        } else {
            textView2.setText(download.getStatus().toString());
        }
        j = download.getProgress();
        if (j <= -1 || status == Status.COMPLETED) {
            textView.setText(String.format("%s", new Object[]{Utils.formatSize(this, download.getDownloaded())}));
            progressBar.setVisibility(4);
        } else {
            textView.setText(String.format("%s%%       %s/%s", new Object[]{Integer.valueOf(j), Utils.formatSize(this, download.getDownloaded()), Utils.formatSize(this, download.getTotal())}));
            progressBar.setMax(100);
            progressBar.setProgress(j);
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
        this.updateProgress.setOnClickListener(new C03935());
    }

    public void showError(String str) {
        Toast.makeText(this, str, 1).show();
        this.updatebutton.setVisibility(8);
        this.updateProgress.setVisibility(8);
        AppUpdater.CheckForUpdates(this);
    }

    public void downloadAPK(UpdateData updateData) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MorpheusTV-b");
        stringBuilder.append(String.valueOf(updateData.data.file.vercode));
        String stringBuilder2 = stringBuilder.toString();
        if (this.updateFetch != null) {
            this.updateFetch.removeListener(this.fetchListener);
            this.updateFetch.deleteAll();
            this.updateFetch.close();
        }
        this.updateFetch = (Fetch) new Fetch.Builder(this, "ApkFetch").setDownloadConcurrentLimit(1).setProgressReportingInterval(1000).enableLogging(true).build();
        this.updateFetch.setGlobalNetworkType(NetworkType.ALL);
        this.updateFetch.deleteAll();
        this.updateFetch.addListener(this.fetchListener);
        updateData = updateData.data.file.path;
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        stringBuilder3.append("/");
        stringBuilder3.append(stringBuilder2);
        stringBuilder3.append(".apk");
        Request request = new Request(updateData, stringBuilder3.toString());
        request.setPriority(Priority.HIGH);
        request.setNetworkType(NetworkType.ALL);
        this.updateFetch.enqueue(request, new C03946(), new C03957());
    }

    public void onUpdateFound(final UpdateData updateData) {
        runOnUiThread(new Runnable() {

            /* renamed from: com.android.morpheustv.MainActivity$8$1 */
            class C03981 implements OnClickListener {

                /* renamed from: com.android.morpheustv.MainActivity$8$1$1 */
                class C03961 implements DialogInterface.OnClickListener {
                    C03961() {
                    }

                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.this.updatebutton.setVisibility(8);
                        MainActivity.this.downloadAPK(updateData);
                        dialogInterface.dismiss();
                    }
                }

                /* renamed from: com.android.morpheustv.MainActivity$8$1$2 */
                class C03972 implements DialogInterface.OnClickListener {
                    C03972() {
                    }

                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }

                C03981() {
                }

                public void onClick(View view) {
                    view = new Builder(MainActivity.this).create();
                    view.setCancelable(false);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(MainActivity.this.getString(R.string.update_found_title));
                    stringBuilder.append(StringUtils.SPACE);
                    stringBuilder.append(updateData.data.file.vername);
                    view.setTitle(stringBuilder.toString());
                    view.setMessage(updateData.data.media.news);
                    view.setButton(-1, MainActivity.this.getString(R.string.update_btn_update), new C03961());
                    view.setButton(-2, MainActivity.this.getString(R.string.cancel), new C03972());
                    view.show();
                }
            }

            public void run() {
                MainActivity.this.updateResponse = updateData;
                Button button = MainActivity.this.updatebutton;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(MainActivity.this.getString(R.string.update_found_title));
                stringBuilder.append(StringUtils.SPACE);
                stringBuilder.append(updateData.data.file.vername);
                button.setText(stringBuilder.toString());
                MainActivity.this.updatebutton.setVisibility(0);
                MainActivity.this.updateProgress.setVisibility(8);
                MainActivity.this.updatebutton.setOnClickListener(new C03981());
            }
        });
    }

    public void onNoUpdatesAvailable() {
        runOnUiThread(new C04009());
    }

    public boolean shouldShowImportantEntry(InfoEntry infoEntry) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ignoreImportantEntry_");
        stringBuilder.append(infoEntry.ID);
        return Settings.loadInt(this, stringBuilder.toString()) == null ? true : null;
    }

    public void setIgnoreImportantEntry(InfoEntry infoEntry) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ignoreImportantEntry_");
        stringBuilder.append(infoEntry.ID);
        Settings.saveInt(this, stringBuilder.toString(), 1);
    }

    public void onInformationFound(final InfoEntryList infoEntryList) {
        runOnUiThread(new Runnable() {

            /* renamed from: com.android.morpheustv.MainActivity$10$1 */
            class C03861 implements DialogInterface.OnClickListener {
                C03861() {
                }

                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }

            public void run() {
                MainActivity.this.infoButton.setVisibility(0);
                if (infoEntryList.entries != null && infoEntryList.entries.size() > 0) {
                    for (InfoEntry infoEntry : infoEntryList.entries) {
                        if (infoEntry.IMPORTANT && MainActivity.this.shouldShowImportantEntry(infoEntry)) {
                            AlertDialog create = new Builder(MainActivity.this).create();
                            create.setCancelable(false);
                            create.setTitle(infoEntry.TITLE);
                            create.setMessage(infoEntry.CONTENT);
                            create.setButton(-1, MainActivity.this.getString(R.string.ok_button), new C03861());
                            create.show();
                            MainActivity.this.setIgnoreImportantEntry(infoEntry);
                        }
                    }
                }
            }
        });
    }

    public void onNoInformationAvailable() {
        runOnUiThread(new Runnable() {
            public void run() {
                MainActivity.this.infoButton.setVisibility(8);
            }
        });
    }
}
