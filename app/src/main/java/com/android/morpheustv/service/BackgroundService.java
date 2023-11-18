package com.android.morpheustv.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v7.app.MediaRouteButton;
import android.util.Log;
import android.widget.RemoteViews;
import com.android.morpheustv.BaseActivity.DownloadMetadata;
import com.android.morpheustv.KillableActivity;
import com.android.morpheustv.MainActivity;
import com.android.morpheustv.content.Downloads;
import com.android.morpheustv.helpers.Trakt;
import com.android.morpheustv.helpers.Trakt.DeviceTokenResponse;
import com.android.morpheustv.helpers.Trakt.OnActionListener;
import com.android.morpheustv.helpers.Trakt.TraktSyncListener;
import com.android.morpheustv.helpers.Utils;
import com.android.morpheustv.settings.Settings;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.RemoteMediaClient.Listener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient.ProgressListener;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.noname.titan.R;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Status;
import com.uwetrottmann.trakt5.entities.EpisodeIds;
import com.uwetrottmann.trakt5.entities.MovieIds;
import java.util.HashMap;
import java.util.Map.Entry;

public class BackgroundService extends Service implements FetchListener, TraktSyncListener, SessionManagerListener<CastSession>, Listener, ProgressListener, CastStateListener {
    public static final String DOWNLOADS_NOTIFICATION_CHANNEL = "downloads";
    public static int FOREGROUND_SERVICE_ID = 159;
    private static final String LOG_TAG = "BackgroundService";
    public static final String PRIMARY_NOTIFICATION_CHANNEL = "default";
    public static String START_SERVICE_ACTION = "BackgroundService.START";
    public static String STOP_SERVICE_ACTION = "BackgroundService.STOP";
    public static String WEBSERVER_SERVICE_ACTION = "BackgroundService.WEBSERVER_MESSAGE";
    private static String downloadStatus = "";
    public static boolean isRunning = false;
    public static Fetch mainFetch = null;
    private static String traktStatus = "";
    public static boolean updatingStatus = false;
    private static String webserverStatus = "";
    HashMap<Integer, Long> downloadSpeeds;
    public MediaRouteButton mCastButton;
    public CastContext mCastContext;
    public CastSession mCastSession;
    private HttpServer proxyServer;
    public RemoteMediaClient remoteMediaClient;
    private WakeLock wakeLock;
    private WifiLock wifiLock;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (!(intent == null || intent.getAction() == 0)) {
            if (intent.getAction().equals(START_SERVICE_ACTION) != 0) {
                Log.i(LOG_TAG, "Received Start Foreground Intent ");
                startBackgroundService();
            } else if (intent.getAction().equals(STOP_SERVICE_ACTION) != 0) {
                Log.i(LOG_TAG, "Received Stop Foreground Intent");
                stopBackgroundService();
            } else if (intent.getAction().equals(WEBSERVER_SERVICE_ACTION) != 0) {
                Log.i(LOG_TAG, "Received Webserver Message Intent");
                webserverStatus = intent.getStringExtra("Message");
                updateMainNotification();
            }
        }
        return 1;
    }

    public void startBackgroundService() {
        if (isRunning) {
            Log.i(LOG_TAG, "BackgroundService is already running!");
        } else {
            Log.i(LOG_TAG, "Starting BackgroundService...");
            isRunning = true;
            webserverStatus = getString(R.string.server_offline);
            traktStatus = "";
            downloadStatus = "";
            Settings.load(this);
            acquireWakeLocks();
            startProxyServer();
            startDownloadManager();
            startTraktSyncThread();
            setupCastListeners();
        }
        if (Settings.NOTIFICATION_MAIN) {
            startForeground(FOREGROUND_SERVICE_ID, buildNotification());
        } else {
            stopForeground(true);
        }
    }

    public void stopBackgroundService() {
        if (isRunning) {
            Log.i(LOG_TAG, "Stopping BackgroundService...");
            isRunning = false;
            releaseWakeLocks();
            stopProxyServer();
            stopDownloadManager();
            stopTraktSyncThread();
            removeCastListeners();
            stopForeground(true);
            clearAllNotifications();
            stopSelf();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "In onDestroy");
        stopBackgroundService();
        sendBroadcast(new Intent(KillableActivity.KILL_ACTION));
    }

    public void clearAllNotifications() {
        NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    public Notification buildNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.addFlags(270532608);
        int i = 0;
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
        Intent intent2 = new Intent(this, BackgroundService.class);
        intent2.setAction(STOP_SERVICE_ACTION);
        PendingIntent service = PendingIntent.getService(this, 0, intent2, ErrorDialogData.BINDER_CRASH);
        if (VERSION.SDK_INT >= 26) {
            try {
                NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_NOTIFICATION_CHANNEL, "Service Notifications", 2);
                notificationChannel.setLightColor(-16711936);
                notificationChannel.setLockscreenVisibility(1);
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(notificationChannel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.main_notification);
        remoteViews.setTextViewText(R.id.contentTitle, getString(R.string.server_running));
        remoteViews.setTextViewText(R.id.webserverStatus, webserverStatus);
        remoteViews.setTextViewText(R.id.traktStatus, traktStatus);
        remoteViews.setTextViewText(R.id.downloadStatus, downloadStatus);
        remoteViews.setViewVisibility(R.id.traktContainer, traktStatus.isEmpty() ? 8 : 0);
        if (downloadStatus.isEmpty()) {
            i = 8;
        }
        remoteViews.setViewVisibility(R.id.downloadsContainer, i);
        remoteViews.setOnClickPendingIntent(R.id.stopAction, service);
        return new Builder(this, PRIMARY_NOTIFICATION_CHANNEL).setCustomContentView(remoteViews).setCustomBigContentView(remoteViews).setSmallIcon(R.drawable.ic_stat_tap_and_play).setContentIntent(activity).setOngoing(true).setDefaults(4).setPriority(1).setChannelId(PRIMARY_NOTIFICATION_CHANNEL).setOnlyAlertOnce(true).build();
    }

    public void updateDownloadNotification(Download download, long j) {
        if (Settings.NOTIFICATION_DOWNLOADS) {
            NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("dwmeta_");
            stringBuilder.append(String.valueOf(download.getId()));
            DownloadMetadata downloadMetadata = (DownloadMetadata) Settings.loadObject(this, stringBuilder.toString(), DownloadMetadata.class);
            if (downloadMetadata == null || j <= 0) {
                notificationManager.cancel(download.getId());
                return;
            }
            StringBuilder stringBuilder2;
            if (VERSION.SDK_INT >= 26) {
                try {
                    NotificationChannel notificationChannel = new NotificationChannel(DOWNLOADS_NOTIFICATION_CHANNEL, "Download Notifications", 2);
                    notificationChannel.setLightColor(-16776961);
                    notificationChannel.setLockscreenVisibility(1);
                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Intent intent = new Intent(this, Downloads.class);
            intent.putExtra("isFromNotification", true);
            intent.putExtra("downloadId", download.getId());
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addFlags(270532608);
            PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.download_item_notification);
            remoteViews.setTextViewText(R.id.media_name, downloadMetadata.titleWithSE);
            if (downloadMetadata.movie) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(downloadMetadata.titleSimple);
                stringBuilder2.append(" (");
                stringBuilder2.append(String.valueOf(downloadMetadata.year));
                stringBuilder2.append(")");
                remoteViews.setTextViewText(R.id.media_name, stringBuilder2.toString());
            }
            remoteViews.setTextViewText(R.id.source_info, downloadMetadata.source.getSource());
            remoteViews.setTextViewText(R.id.media_title, Utils.getFilenameFromUrl(download.getUrl(), downloadMetadata.titleWithSE));
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(download.getStatus().toString());
            stringBuilder2.append(" @ ");
            stringBuilder2.append(Utils.formatSize(this, j));
            stringBuilder2.append("/s");
            remoteViews.setTextViewText(R.id.downloadStatus, stringBuilder2.toString());
            j = download.getProgress();
            Status status = download.getStatus();
            if (j <= -1 || status == Status.COMPLETED) {
                remoteViews.setTextViewText(R.id.downloadProgress, String.format("%s", new Object[]{Utils.formatSize(this, download.getDownloaded())}));
            } else {
                remoteViews.setTextViewText(R.id.downloadProgress, String.format("%s%%       %s/%s", new Object[]{Integer.valueOf(download.getProgress()), Utils.formatSize(this, download.getDownloaded()), Utils.formatSize(this, download.getTotal())}));
            }
            remoteViews.setProgressBar(R.id.progressBar, 100, download.getProgress(), j <= -1 ? 1 : null);
            notificationManager.notify(download.getId(), new Builder(this, DOWNLOADS_NOTIFICATION_CHANNEL).setCustomContentView(remoteViews).setSmallIcon(R.drawable.downloads_icon).setContentIntent(activity).setOngoing(true).setDefaults(4).setPriority(0).setChannelId(DOWNLOADS_NOTIFICATION_CHANNEL).setOnlyAlertOnce(true).build());
        }
    }

    public void updateMainNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
        if (notificationManager == null) {
            return;
        }
        if (Settings.NOTIFICATION_MAIN) {
            notificationManager.notify(FOREGROUND_SERVICE_ID, buildNotification());
        } else {
            notificationManager.cancel(FOREGROUND_SERVICE_ID);
        }
    }

    public void acquireWakeLocks() {
        Log.i(LOG_TAG, "Setting CPU and WIFI wake locks...");
        PowerManager powerManager = (PowerManager) getSystemService("power");
        if (powerManager != null) {
            this.wakeLock = powerManager.newWakeLock(1, LOG_TAG);
            this.wakeLock.acquire();
        }
        WifiManager wifiManager = (WifiManager) getSystemService("wifi");
        if (wifiManager != null) {
            this.wifiLock = wifiManager.createWifiLock(3, LOG_TAG);
            this.wifiLock.acquire();
        }
    }

    public void releaseWakeLocks() {
        if (this.wifiLock != null || this.wakeLock != null) {
            Log.i(LOG_TAG, "Releasing CPU and WIFI wake locks...");
            if (this.wakeLock != null) {
                this.wakeLock.release();
            }
            if (this.wifiLock != null) {
                this.wifiLock.release();
            }
            this.wakeLock = null;
            this.wifiLock = null;
        }
    }

    public void startProxyServer() {
        try {
            stopProxyServer();
            this.proxyServer = new HttpServer(this, 16737);
            this.proxyServer.start();
            updateMainNotification();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Listening at : http://");
            stringBuilder.append(Utils.getIP());
            stringBuilder.append(":16737/");
            Log.d("WebServer", stringBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopProxyServer() {
        try {
            if (this.proxyServer != null) {
                this.proxyServer.stop();
            }
            this.proxyServer = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startDownloadManager() {
        mainFetch = (Fetch) new Fetch.Builder(this, "MainFetch").setDownloadConcurrentLimit(Settings.MAX_SIMULTANEOUS_DOWNLOADS).setProgressReportingInterval(2000).enableLogging(true).build();
        if (Settings.DOWNLOADS_CELLULAR) {
            mainFetch.setGlobalNetworkType(NetworkType.ALL);
        } else {
            mainFetch.setGlobalNetworkType(NetworkType.WIFI_ONLY);
        }
        mainFetch.addListener(this);
        updateMainNotification();
    }

    public void stopDownloadManager() {
        if (!(mainFetch == null || mainFetch.isClosed())) {
            mainFetch.removeListener(this);
            mainFetch.close();
        }
        mainFetch = null;
    }

    public void updateDownloadSpeed(Download download, long j) {
        try {
            if (mainFetch != null) {
                if (this.downloadSpeeds == null) {
                    this.downloadSpeeds = new HashMap();
                }
                long j2 = 0;
                if (j > 0) {
                    this.downloadSpeeds.put(Integer.valueOf(download.getId()), Long.valueOf(j));
                } else {
                    this.downloadSpeeds.remove(Integer.valueOf(download.getId()));
                }
                if (this.downloadSpeeds.size() > 0) {
                    for (Entry value : this.downloadSpeeds.entrySet()) {
                        j2 += ((Long) value.getValue()).longValue();
                    }
                    downloadStatus = String.format("%s %d %s @ %s/s", new Object[]{getString(R.string.downloading), Integer.valueOf(this.downloadSpeeds.size()), getString(R.string.files), Utils.formatSize(this, j2)});
                } else {
                    downloadStatus = "";
                }
                updateMainNotification();
                updateDownloadNotification(download, j);
            }
        } catch (Download download2) {
            download2.printStackTrace();
        }
    }

    public void onQueued(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download enqueued: ");
        stringBuilder.append(download.getUrl());
        Log.d("BackgroundListener", stringBuilder.toString());
        updateDownloadSpeed(download, 0);
    }

    public void onCompleted(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download completed: ");
        stringBuilder.append(download.getUrl());
        Log.d("BackgroundListener", stringBuilder.toString());
        updateDownloadSpeed(download, 0);
    }

    public void onError(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download error:");
        stringBuilder.append(download.getUrl());
        Log.d("BackgroundListener", stringBuilder.toString());
        updateDownloadSpeed(download, 0);
    }

    public void onProgress(Download download, long j, long j2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download progress:");
        stringBuilder.append(String.valueOf(j));
        stringBuilder.append(" - ");
        stringBuilder.append(String.valueOf(j2));
        Log.d("BackgroundListener", stringBuilder.toString());
        updateDownloadSpeed(download, j2);
    }

    public void onPaused(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download paused:");
        stringBuilder.append(download.getUrl());
        Log.d("BackgroundListener", stringBuilder.toString());
        updateDownloadSpeed(download, 0);
    }

    public void onResumed(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download resumed:");
        stringBuilder.append(download.getUrl());
        Log.d("BackgroundListener", stringBuilder.toString());
        updateDownloadSpeed(download, 0);
    }

    public void onCancelled(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download cancelled:");
        stringBuilder.append(download.getUrl());
        Log.d("BackgroundListener", stringBuilder.toString());
        updateDownloadSpeed(download, 0);
    }

    public void onRemoved(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download removed:");
        stringBuilder.append(download.getUrl());
        Log.d("BackgroundListener", stringBuilder.toString());
        updateDownloadSpeed(download, 0);
    }

    public void onDeleted(Download download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download deleted:");
        stringBuilder.append(download.getUrl());
        Log.d("BackgroundListener", stringBuilder.toString());
        updateDownloadSpeed(download, 0);
    }

    public void startTraktSyncThread() {
        Trakt.userToken = (DeviceTokenResponse) Settings.loadObject(this, "trakt_token", DeviceTokenResponse.class);
        Trakt.startSyncThread(this);
        Trakt.addSyncListener(this);
    }

    public void stopTraktSyncThread() {
        Trakt.removeListener(this);
        Trakt.killSyncThread();
    }

    public void onTraktSyncStart() {
        traktStatus = getString(R.string.service_content_trakt_progress);
        updateMainNotification();
    }

    public void onTraktSyncProgress(String str) {
        traktStatus = str;
        updateMainNotification();
    }

    public void onTraktSyncComplete(boolean z) {
        if (z) {
            traktStatus = getString(true);
        } else {
            traktStatus = getString(true);
        }
        updateMainNotification();
    }

    public void setupCastListeners() {
        try {
            this.mCastContext = CastContext.getSharedInstance(this);
            this.mCastSession = this.mCastContext.getSessionManager().getCurrentCastSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.mCastContext != null) {
            this.mCastContext.addCastStateListener(this);
            this.mCastContext.getSessionManager().addSessionManagerListener(this, CastSession.class);
        }
    }

    public void removeCastListeners() {
        if (this.mCastContext != null) {
            this.mCastContext.removeCastStateListener(this);
            this.mCastContext.getSessionManager().removeSessionManagerListener(this, CastSession.class);
        }
        if (this.remoteMediaClient != null) {
            this.remoteMediaClient.removeListener(this);
            this.remoteMediaClient.removeProgressListener(this);
        }
    }

    public RemoteMediaClient getMediaClient() {
        try {
            if (this.mCastSession != null && (this.mCastSession.isConnected() || this.mCastSession.isConnecting())) {
                if (this.remoteMediaClient != null) {
                    this.remoteMediaClient.removeListener(this);
                    this.remoteMediaClient.removeProgressListener(this);
                }
                this.remoteMediaClient = this.mCastSession.getRemoteMediaClient();
                if (this.remoteMediaClient != null) {
                    this.remoteMediaClient.addProgressListener(this, 1000);
                    this.remoteMediaClient.addListener(this);
                }
                return this.remoteMediaClient;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onCastStateChanged(int i) {
        if (i != 1) {
            Log.d("Casting", "Casting devices available...");
        }
    }

    public void onSessionEnded(CastSession castSession, int i) {
        onApplicationDisconnected();
    }

    public void onSessionResumed(CastSession castSession, boolean z) {
        onApplicationConnected(castSession);
    }

    public void onSessionResumeFailed(CastSession castSession, int i) {
        onApplicationDisconnected();
    }

    public void onSessionStarted(CastSession castSession, String str) {
        onApplicationConnected(castSession);
    }

    public void onSessionStartFailed(CastSession castSession, int i) {
        onApplicationDisconnected();
    }

    public void onSessionStarting(CastSession castSession) {
        Log.d("CastingService", "onSessionStarting");
        getMediaClient();
    }

    public void onSessionEnding(CastSession castSession) {
        Log.d("CastingService", "onSessionEnding");
    }

    public void onSessionResuming(CastSession castSession, String str) {
        Log.d("CastingService", "onSessionResuming");
        getMediaClient();
    }

    public void onSessionSuspended(CastSession castSession, int i) {
        Log.d("CastingService", "onSessionSuspended");
    }

    public void onStatusUpdated() {
        Log.d("CastingService", "onStatusUpdated");
    }

    public void onMetadataUpdated() {
        Log.d("CastingService", "onMetadataUpdated");
    }

    public void onQueueStatusUpdated() {
        Log.d("CastingService", "onQueueStatusUpdated");
    }

    public void onPreloadStatusUpdated() {
        Log.d("CastingService", "onPreloadStatusUpdated");
    }

    public void onSendingRemoteMediaRequest() {
        Log.d("CastingService", "onSendingRemoteMediaRequest");
    }

    public void onAdBreakStatusUpdated() {
        Log.d("CastingService", "onAdBreakStatusUpdated");
    }

    public void onProgressUpdated(long j, long j2) {
        Log.d("CastingService", "onProgressUpdated");
        if (saveCastPosition(j)) {
            updateWatchedStatus(j, j2);
        }
    }

    public boolean saveCastPosition(long j) {
        String loadString = Settings.loadString(this, "CastMediaKey");
        if (loadString == null) {
            return 0;
        }
        Settings.saveLong(this, loadString, j);
        return 1;
    }

    public void setCastMediaKey(String str, int i, int i2, int i3, String str2) {
        Settings.saveString(this, "CastMediaKey", str);
        Settings.saveInt(this, "CastMediaKeySeason", i);
        Settings.saveInt(this, "CastMediaKeyEpisode", i2);
        Settings.saveInt(this, "CastMediaKeyTraktId", i3);
        Settings.saveString(this, "CastMediaKeyImdb", str2);
    }

    public void updateWatchedStatus(long j, long j2) {
        if (j > 0 && j2 > 0 && j >= (j2 / 5) * 4 && updatingStatus == null) {
            final String loadString = Settings.loadString(this, "CastMediaKey");
            j = Settings.loadInt(this, "CastMediaKeySeason");
            int loadInt = Settings.loadInt(this, "CastMediaKeyEpisode");
            j2 = Settings.loadInt(this, "CastMediaKeyTraktId");
            String loadString2 = Settings.loadString(this, "CastMediaKeyImdb");
            if (j2 <= null) {
                if (loadString2 == null) {
                    updatingStatus = 0;
                    return;
                }
            }
            Log.d("CastingService", "Marking media watched...");
            final int i = j;
            final int i2 = loadInt;
            final String str = loadString2;
            OnActionListener c05291 = new OnActionListener() {
                public void onSuccess(Object obj) {
                    Log.d("CastingService", "Media marked as watched successfully");
                    if (i <= null || i2 <= null) {
                        Trakt.syncMovieCache(BackgroundService.this);
                    } else {
                        Trakt.syncShowCache(BackgroundService.this, str);
                    }
                    BackgroundService.this.setCastMediaKey(loadString, i, i2, 0, null);
                    BackgroundService.updatingStatus = null;
                }

                public void onFail(int i) {
                    Log.d("CastingService", "Fail to mark media as watched");
                    BackgroundService.updatingStatus = false;
                }
            };
            if (j <= null || loadInt <= 0) {
                j = new MovieIds();
                j.imdb = loadString2;
                Trakt.getInstance().markMovieWatched(j, true, c05291);
            } else {
                j = new EpisodeIds();
                j.trakt = Integer.valueOf(j2);
                Trakt.getInstance().markEpisodeWatched(j, true, c05291);
            }
            updatingStatus = true;
        }
    }

    private void onApplicationConnected(CastSession castSession) {
        Log.d("CastingService", "onApplicationConnected");
        this.mCastSession = castSession;
        getMediaClient();
    }

    private void onApplicationDisconnected() {
        Log.d("CastingService", "onApplicationDisconnected");
        this.mCastSession = null;
        getMediaClient();
    }
}
