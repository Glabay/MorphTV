package com.android.morpheustv.helpers;

import com.android.morpheustv.settings.Settings;
import com.frostwire.jlibtorrent.AnnounceEntry;
import com.frostwire.jlibtorrent.TorrentHandle;
import com.github.se_bastiaan.torrentstream.StreamStatus;
import com.github.se_bastiaan.torrentstream.Torrent;
import com.github.se_bastiaan.torrentstream.TorrentOptions.Builder;
import com.github.se_bastiaan.torrentstream.TorrentStream;
import com.github.se_bastiaan.torrentstream.listeners.TorrentListener;
import io.github.morpheustv.scrapers.model.Source;
import java.io.File;

public class TorrentHelper {
    private static TorrentListener internalListener = new C04981();
    private static TorrentStream torrentStream;

    /* renamed from: com.android.morpheustv.helpers.TorrentHelper$1 */
    static class C04981 implements TorrentListener {
        public void onStreamError(Torrent torrent, Exception exception) {
        }

        public void onStreamProgress(Torrent torrent, StreamStatus streamStatus) {
        }

        public void onStreamReady(Torrent torrent) {
        }

        public void onStreamStarted(Torrent torrent) {
        }

        public void onStreamStopped() {
        }

        C04981() {
        }

        public void onStreamPrepared(Torrent torrent) {
            TorrentHelper.addExtraTrackers();
        }
    }

    public static void stopTorrentStream(Source source, TorrentListener torrentListener) {
        if (source != null) {
            source.setTorrentReady(false);
            source.setUrl(source.getOriginalUrl());
        }
        if (torrentStream != null) {
            try {
                if (torrentStream.isStreaming() != null) {
                    torrentStream.stopStream();
                }
                removeListener(torrentListener);
                removeListener(internalListener);
            } catch (Source source2) {
                source2.printStackTrace();
            }
        }
    }

    public static boolean startTorrentStream(String str, TorrentListener torrentListener) {
        try {
            stopTorrentStream(null, torrentListener);
            Builder builder = new Builder();
            builder.saveLocation(new File(Settings.TORRENT_DOWNLOAD_FOLDER)).removeFilesAfterStop(Boolean.valueOf(Settings.TORRENT_REMOVE_DOWNLOADS)).maxConnections(Integer.valueOf(Settings.TORRENT_MAX_CONNECTIONS)).maxActiveDHT(Integer.valueOf(Settings.TORRENT_MAX_CONNECTIONS / 2));
            if (Settings.TORRENT_MAX_DOWNLOAD > 0) {
                builder.maxDownloadSpeed(Integer.valueOf(Settings.TORRENT_MAX_DOWNLOAD));
            }
            if (Settings.TORRENT_MAX_UPLOAD > 0) {
                builder.maxUploadSpeed(Integer.valueOf(Settings.TORRENT_MAX_UPLOAD));
            }
            TorrentStream.init(builder.build());
            torrentStream = TorrentStream.getInstance();
            addListener(torrentListener);
            addListener(internalListener);
            torrentStream.startStream(str);
            return true;
        } catch (String str2) {
            str2.printStackTrace();
            return null;
        }
    }

    public static void addListener(TorrentListener torrentListener) {
        if (torrentStream != null) {
            torrentStream.removeListener(torrentListener);
            torrentStream.addListener(torrentListener);
        }
    }

    public static void removeListener(TorrentListener torrentListener) {
        if (torrentStream != null) {
            torrentStream.removeListener(torrentListener);
        }
    }

    public static Torrent getCurrentTorrent() {
        return torrentStream != null ? torrentStream.getCurrentTorrent() : null;
    }

    public static void addExtraTrackers() {
        if (torrentStream != null) {
            Torrent currentTorrent = getCurrentTorrent();
            if (currentTorrent != null && currentTorrent.getTorrentHandle() != null) {
                TorrentHandle torrentHandle = currentTorrent.getTorrentHandle();
                torrentHandle.addTracker(new AnnounceEntry("udp://glotorrents.pw:6969/announce"));
                torrentHandle.addTracker(new AnnounceEntry("udp://tracker.opentrackr.org:1337/announce"));
                torrentHandle.addTracker(new AnnounceEntry("udp://torrent.gresille.org:80/announce"));
                torrentHandle.addTracker(new AnnounceEntry("udp://tracker.openbittorrent.com:80"));
                torrentHandle.addTracker(new AnnounceEntry("udp://tracker.coppersurfer.tk:6969"));
                torrentHandle.addTracker(new AnnounceEntry("udp://tracker.leechers-paradise.org:6969"));
                torrentHandle.addTracker(new AnnounceEntry("udp://p4p.arenabg.ch:1337"));
                torrentHandle.addTracker(new AnnounceEntry("udp://tracker.internetwarriors.net:1337"));
                torrentHandle.forceReannounce();
                torrentHandle.forceDHTAnnounce();
            }
        }
    }
}
