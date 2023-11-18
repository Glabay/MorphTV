package com.android.morpheustv.sources.subtitles;

import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import com.android.morpheustv.helpers.Utils;
import com.android.morpheustv.sources.BaseSubtitleProvider;
import com.android.morpheustv.sources.SubtitleResult;
import de.timroes.axmlrpc.XMLRPCClient;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import org.apache.commons.io.IOUtils;

public class OpenSubtitles extends BaseSubtitleProvider {
    private static final String api_url = "http://api.opensubtitles.org/xml-rpc";
    private static final String subfilename = "morpheus_subtitle.srt";
    private static String token = "";
    private static final String useragent = "XBMC_Subtitles_v1";

    public OpenSubtitles() {
        super("OPENSUBTITLES");
    }

    public ArrayList<SubtitleResult> getSubtitles(String str, String str2, int i, int i2) {
        ArrayList<SubtitleResult> arrayList = new ArrayList();
        if (str2 != null) {
            try {
                if (!str2.isEmpty() && login()) {
                    str = search_subtitles(str2, str, i, i2);
                    if (str != null) {
                        for (int i22 : str) {
                            Map map = (Map) i22;
                            SubtitleResult subtitleResult = new SubtitleResult("OPENSUBTITLES");
                            subtitleResult.setSubtitleID(String.valueOf(map.get("IDSubtitleFile")));
                            subtitleResult.setFilename(String.valueOf(map.get("SubFileName")));
                            subtitleResult.setLanguage(String.valueOf(map.get("SubLanguageID")));
                            arrayList.add(subtitleResult);
                        }
                    }
                }
            } catch (String str3) {
                str3.printStackTrace();
            }
        }
        return arrayList;
    }

    public String downloadSubtitle(SubtitleResult subtitleResult) {
        return downloadSubtitle(subtitleResult.getSubtitleID(), subfilename, true);
    }

    public String downloadSubtitle(String str, String str2, boolean z) {
        return download_subtitle(str, str2, z);
    }

    private boolean login() {
        try {
            Object[] objArr = new Object[]{"", "", "en", useragent};
            XMLRPCClient xMLRPCClient = new XMLRPCClient(new URL(api_url));
            xMLRPCClient.setTimeout(10);
            Map map = (Map) xMLRPCClient.call("LogIn", objArr);
            String str = (String) map.get(NotificationCompat.CATEGORY_STATUS);
            token = (String) map.get("token");
            return true;
        } catch (Exception e) {
            token = "";
            e.printStackTrace();
            return false;
        }
    }

    private Object[] search_subtitles(String str, String str2, int i, int i2) {
        try {
            if (!(token == null || token.isEmpty())) {
                Map hashMap = new HashMap();
                hashMap.put("sublanguageid", str);
                hashMap.put("imdbid", str2.replace(TtmlNode.TAG_TT, ""));
                if (i > 0 && i2 > 0) {
                    hashMap.put("season", Integer.valueOf(i));
                    hashMap.put("episode", Integer.valueOf(i2));
                }
                str2 = new Object[]{hashMap};
                i2 = new Object[]{token, str2};
                str = new XMLRPCClient(new URL(api_url));
                str.setTimeout(10);
                return (Object[]) ((Map) str.call("SearchSubtitles", i2)).get("data");
            }
        } catch (String str3) {
            str3.printStackTrace();
        }
        return null;
    }

    private String download_subtitle(String str, String str2, boolean z) {
        String str3 = "";
        if (z) {
            try {
                logout();
                login();
            } catch (String str4) {
                str3 = "";
                str4.printStackTrace();
                return str3;
            }
        }
        if (!token || token.isEmpty()) {
            return str3;
        }
        new HashMap().put("data", str4);
        str4 = new Object[]{token, z};
        z = new XMLRPCClient(new URL(api_url));
        z.setTimeout(30);
        z = new GZIPInputStream(new ByteArrayInputStream(Base64.decode((String) ((Map) ((Object[]) ((Map) z.call("DownloadSubtitles", str4)).get("data"))[0]).get("data"), 0)));
        str4 = new StringBuilder();
        str4.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        str4.append("/");
        str4.append(str2);
        str3 = str4.toString();
        str4 = new File(str3);
        if (str4.exists() != null) {
            str4.delete();
        }
        str2 = new FileOutputStream(str4);
        IOUtils.copy(z, str2);
        str2.flush();
        str2.close();
        z.close();
        Utils.convert_utf8(str4);
        return str3;
    }

    private void logout() {
        try {
            Object[] objArr = new Object[]{token};
            XMLRPCClient xMLRPCClient = new XMLRPCClient(new URL(api_url));
            xMLRPCClient.setTimeout(10);
            xMLRPCClient.call("LogOut", objArr);
            token = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
