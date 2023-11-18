package io.github.morpheustv.scrapers.model;

import io.github.morpheustv.scrapers.helper.Utils;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class Source {
    private boolean castable;
    private String cookieString;
    private Map<String, String> cookies;
    private String extra_info;
    private String filename;
    private String originalUrl;
    private String provider;
    private boolean proxified;
    private String quality;
    private String referer;
    private long resolve_time;
    private String resolved_url;
    private long size;
    private String source;
    private String title;
    private boolean torrent;
    private long torrent_peers;
    private boolean torrent_ready;
    private long torrent_seeds;
    private String url;

    public Source() {
        this.torrent = false;
        this.size = 0;
        this.torrent_seeds = 0;
        this.torrent_peers = 0;
        this.proxified = false;
        this.torrent_ready = false;
    }

    public Source(String str, String str2, String str3, String str4, String str5) {
        this.torrent = false;
        this.size = 0;
        this.torrent_seeds = 0;
        this.torrent_peers = 0;
        this.proxified = false;
        this.torrent_ready = false;
        this.title = str;
        this.source = str2;
        this.quality = str3;
        this.provider = str4;
        this.url = str5;
        this.resolved_url = str5;
        this.originalUrl = str5;
        this.cookies = new HashMap();
        this.cookieString = "";
        this.filename = Utils.getFilenameFromUrl(str5, str);
        this.extra_info = "";
    }

    public Source(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        this(str, str2, str3, str4, str5);
        this.filename = str6;
        this.extra_info = str7;
    }

    public Source(String str, String str2, String str3, String str4, String str5, Map<String, String> map) {
        this(str, str2, str3, str4, str5);
        this.cookies = map;
    }

    public Source(String str, String str2, String str3, String str4, String str5, String str6) {
        this(str, str2, str3, str4, str5);
        this.cookieString = str6;
    }

    public boolean isTorrentReady() {
        return this.torrent_ready;
    }

    public void setTorrentReady(boolean z) {
        this.torrent_ready = z;
    }

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public void setOriginalUrl(String str) {
        this.originalUrl = str;
    }

    public boolean isProxified() {
        return this.proxified;
    }

    public void setProxified(boolean z) {
        this.proxified = z;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String str) {
        this.source = str;
    }

    public String getQuality() {
        return this.quality;
    }

    public void setQuality(String str) {
        this.quality = str;
    }

    public String getProvider() {
        return this.provider;
    }

    public void setProvider(String str) {
        this.provider = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public Map<String, String> getCookies() {
        return this.cookies;
    }

    public String getExtra_info() {
        return this.extra_info;
    }

    public void setExtra_info(String str) {
        this.extra_info = str;
    }

    public void setCookies(Map<String, String> map) {
        this.cookies = map;
    }

    public String getCookieString() {
        return this.cookieString;
    }

    public void setCookieString(String str) {
        this.cookieString = str;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String str) {
        this.filename = str;
    }

    public String getReferer() {
        return this.referer;
    }

    public void setReferer(String str) {
        this.referer = str;
    }

    public boolean isCastable() {
        return this.castable;
    }

    public void setCastable(boolean z) {
        this.castable = z;
    }

    public boolean isTorrent() {
        return this.torrent;
    }

    public void setTorrent(boolean z) {
        this.torrent = z;
    }

    public String getResolved_url() {
        return this.resolved_url;
    }

    public void setResolved_url(String str) {
        this.resolved_url = str;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long j) {
        this.size = j;
    }

    public long getTorrent_seeds() {
        return this.torrent_seeds;
    }

    public void setTorrent_seeds(long j) {
        this.torrent_seeds = j;
    }

    public long getTorrent_peers() {
        return this.torrent_peers;
    }

    public void setTorrent_peers(long j) {
        this.torrent_peers = j;
    }

    public long getResolve_time() {
        return this.resolve_time;
    }

    public void setResolve_time(long j) {
        this.resolve_time = j;
    }

    public String getCDNSourceName() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("_");
        stringBuilder.append(getSource().replace(StringUtils.SPACE, "_"));
        stringBuilder.append("_");
        return stringBuilder.toString();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Source{provider='");
        stringBuilder.append(this.provider);
        stringBuilder.append('\'');
        stringBuilder.append(", source='");
        stringBuilder.append(this.source);
        stringBuilder.append('\'');
        stringBuilder.append("\n, quality='");
        stringBuilder.append(this.quality);
        stringBuilder.append('\'');
        stringBuilder.append(", resolve_time='");
        stringBuilder.append(this.resolve_time);
        stringBuilder.append('\'');
        stringBuilder.append(", extra_info='");
        stringBuilder.append(this.extra_info);
        stringBuilder.append('\'');
        stringBuilder.append("\n, url='");
        stringBuilder.append(this.url);
        stringBuilder.append('\'');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
