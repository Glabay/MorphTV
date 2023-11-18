package com.android.morpheustv.sources.subtitles;

import android.os.Environment;
import com.android.morpheustv.helpers.FileExtensionFinder;
import com.android.morpheustv.helpers.Utils;
import com.android.morpheustv.sources.BaseSubtitleProvider;
import com.android.morpheustv.sources.SubtitleResult;
import io.github.morpheustv.scrapers.model.BaseProvider;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

public class LegendasDivx extends BaseSubtitleProvider {
    private static final int BUFFER_SIZE = 4096;
    public static String PASSWORD = "";
    public static String USERNAME = "";
    private static Map<String, String> cookies = null;
    private static final String subfilename = "morpheus_subtitle";
    private final String base_url = "https://www.legendasdivx.pt";

    public LegendasDivx() {
        super("LEGENDASDIVX");
    }

    public ArrayList<SubtitleResult> getSubtitles(String str, String str2, int i, int i2) {
        int i3 = i;
        int i4 = i2;
        ArrayList<SubtitleResult> arrayList = new ArrayList();
        try {
            cookies = getLoginCookies();
            if (cookies != null && cookies.size() > 0) {
                String str3 = "";
                if (i3 > 0 && i4 > 0) {
                    str3 = String.format("&query=S%02dE%02d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("https://www.legendasdivx.pt/modules.php?name=Downloads&file=jz&imdbid=");
                stringBuilder.append(str.replace(TtmlNode.TAG_TT, ""));
                stringBuilder.append(str3);
                stringBuilder.append("&order=hits+desc");
                Iterator it = Jsoup.connect(stringBuilder.toString()).ignoreContentType(true).referrer("https://www.legendasdivx.pt").cookies(cookies).method(Method.GET).userAgent(BaseProvider.UserAgent).execute().parse().select("div.sub_box").iterator();
                while (it.hasNext()) {
                    Element element = (Element) it.next();
                    Object obj = "";
                    String attr = element.select("img").first().attr("src");
                    if (attr.toLowerCase().contains("portugal")) {
                        obj = "por";
                    }
                    if (attr.toLowerCase().contains("brazil")) {
                        obj = "pob";
                    }
                    if (attr.toLowerCase().contains("inglaterra")) {
                        obj = "eng";
                    }
                    if (attr.toLowerCase().contains("espanha")) {
                        obj = "spa";
                    }
                    attr = element.select("div.sub_header").first().select("b").first().text();
                    List<String> asList = Arrays.asList(element.select("td.td_desc").first().html().replace("<br>", "\n").replace("<br />", "\n").split("\n"));
                    if (asList != null && asList.size() > 0) {
                        String trim = Jsoup.clean((String) asList.get(0), Whitelist.none()).trim();
                        for (String clean : asList) {
                            String clean2 = Jsoup.clean(clean2, Whitelist.none()).trim();
                            if (BaseProvider.cleantitle(clean2).startsWith(BaseProvider.cleantitle(attr))) {
                                if (i3 > 0 && i4 > 0) {
                                    if (clean2.toLowerCase().contains(String.format("S%02dE%02d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)}).toLowerCase())) {
                                    }
                                }
                                attr = clean2;
                                break;
                            }
                        }
                        attr = trim;
                    }
                    String attr2 = element.select("a.sub_download").first().attr("href");
                    if (str2.contains(obj)) {
                        SubtitleResult subtitleResult = new SubtitleResult("LEGENDASDIVX");
                        subtitleResult.setFilename(attr);
                        subtitleResult.setSubtitleID(attr2);
                        subtitleResult.setLanguage(obj);
                        subtitleResult.episode = i4;
                        subtitleResult.season = i3;
                        arrayList.add(subtitleResult);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public String downloadSubtitle(SubtitleResult subtitleResult) {
        return download_subtitle(subtitleResult, subfilename);
    }

    public String download_subtitle(SubtitleResult subtitleResult, String str) {
        try {
            if (cookies != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
                stringBuilder.append("/");
                stringBuilder.append(str);
                stringBuilder.append(".srt");
                String stringBuilder2 = stringBuilder.toString();
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("https://www.legendasdivx.pt/modules.php");
                stringBuilder3.append(subtitleResult.getSubtitleID());
                URL url = new URL(stringBuilder3.toString());
                InputStream byteArrayInputStream = new ByteArrayInputStream(Jsoup.connect(url.toString()).ignoreContentType(true).referrer(url.toString()).userAgent(BaseProvider.UserAgent).cookies(cookies).method(Method.GET).execute().bodyAsBytes());
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
                stringBuilder4.append("/");
                stringBuilder4.append(str);
                stringBuilder4.append("_tmp.zip");
                File file = new File(stringBuilder4.toString());
                if (file.exists()) {
                    file.delete();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] bArr = new byte[4096];
                while (true) {
                    int read = byteArrayInputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, read);
                }
                fileOutputStream.close();
                byteArrayInputStream.close();
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
                stringBuilder3.append("/");
                stringBuilder3.append(str);
                stringBuilder3.append("_tmp");
                File file2 = new File(stringBuilder3.toString());
                if (file2.exists() != null) {
                    Utils.deleteDir(file2);
                }
                file2.mkdir();
                str = new File(stringBuilder2);
                if (str.exists()) {
                    str.delete();
                }
                if (!Utils.unzip(file, file2)) {
                    Utils.unrar(file, file2);
                }
                List findFiles = new FileExtensionFinder(".srt").findFiles(file2);
                if (findFiles.size() == 0) {
                    findFiles = new FileExtensionFinder(".sub").findFiles(file2);
                }
                if (r2 != null && r2.size() > 0) {
                    for (File file3 : r2) {
                        String toLowerCase = file3.getName().toLowerCase();
                        if (toLowerCase.contains(".srt") || toLowerCase.contains(".sub")) {
                            if (subtitleResult.season > 0 && subtitleResult.episode > 0) {
                                if (toLowerCase.contains(String.format("S%02dE%02d", new Object[]{Integer.valueOf(subtitleResult.season), Integer.valueOf(subtitleResult.episode)}).toLowerCase())) {
                                }
                            }
                            FileUtils.copyFile(file3, str);
                            break;
                        }
                    }
                }
                if (file2.exists() != null) {
                    Utils.deleteDir(file2);
                }
                if (file.exists() != null) {
                    file.delete();
                }
                if (str.exists() != null) {
                    Utils.convert_utf8(str);
                    return stringBuilder2;
                }
            }
        } catch (SubtitleResult subtitleResult2) {
            subtitleResult2.printStackTrace();
        }
        return "";
    }

    private Map<String, String> getLoginCookies() {
        try {
            if (cookies == null) {
                cookies = new HashMap();
            }
            cookies = Jsoup.connect("https://www.legendasdivx.pt/sair.php?referer=login").ignoreContentType(true).ignoreHttpErrors(true).referrer("https://www.legendasdivx.pt").userAgent(BaseProvider.UserAgent).cookies(cookies).method(Method.GET).execute().cookies();
            if (!(USERNAME == null || USERNAME.isEmpty() || PASSWORD == null || PASSWORD.isEmpty())) {
                cookies = Jsoup.connect("https://www.legendasdivx.pt/forum/ucp.php?mode=login").cookies(cookies).referrer("https://www.legendasdivx.pt/forum/ucp.php?mode=login").ignoreContentType(true).followRedirects(true).data("sid", Jsoup.connect("https://www.legendasdivx.pt/forum/ucp.php?mode=login").cookies(cookies).referrer("https://www.legendasdivx.pt").userAgent(BaseProvider.UserAgent).ignoreContentType(true).execute().parse().select("input[name=sid]").val()).data("username", USERNAME).data("password", PASSWORD).data("redirect", "index.php").data("login", "Ligue-se").userAgent(BaseProvider.UserAgent).method(Method.POST).execute().cookies();
                return cookies;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
