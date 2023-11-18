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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class TVSubs extends BaseSubtitleProvider {
    private static final int BUFFER_SIZE = 4096;
    private static Map<String, String> cookies = new HashMap();
    private static final String subfilename = "morpheus_subtitle";
    private final String base_url = "http://tvsubs.net";

    public TVSubs() {
        super("TVSUBS");
        cookies.put("setlang", "en");
    }

    public ArrayList<SubtitleResult> getSubtitles(String str, String str2, int i, int i2) {
        ArrayList<SubtitleResult> arrayList = new ArrayList();
        if (i == 0 && i2 == 0) {
            try {
                if (str2.contains("eng") == null) {
                    return arrayList;
                }
            } catch (String str3) {
                str3.printStackTrace();
            }
        }
        str2 = Jsoup.connect("http://tvsubs.net/tvshows.html").ignoreContentType(true).method(Method.GET).userAgent(BaseProvider.UserAgent).cookies(cookies).referrer("http://tvsubs.net").execute();
        cookies.putAll(str2.cookies());
        str2 = str2.parse().select("ul.list1").last().select("li").iterator();
        while (str2.hasNext()) {
            Element element = (Element) str2.next();
            if (BaseProvider.cleantitle(element.select("a").text()).startsWith(BaseProvider.cleantitle(str3))) {
                str3 = element.select("a").attr("href");
                str3 = str3.substring(null, str3.lastIndexOf("-"));
                str2 = new StringBuilder();
                str2.append(str3);
                str2.append("-");
                str2.append(String.valueOf(i));
                str2.append(".html");
                str3 = str2.toString();
                str2 = new StringBuilder();
                str2.append("http://tvsubs.net/");
                str2.append(str3);
                str3 = Jsoup.connect(str2.toString()).ignoreContentType(true).referrer("http://tvsubs.net").method(Method.GET).userAgent(BaseProvider.UserAgent).cookies(cookies).execute();
                cookies.putAll(str3.cookies());
                str3 = str3.parse().select("ul.list1").last().select("li").iterator();
                while (str3.hasNext() != null) {
                    Element element2 = (Element) str3.next();
                    try {
                        if (Float.parseFloat(BaseProvider.cleantitle(element2.ownText())) == ((float) i2)) {
                            str2 = element2.select("a").attr("href");
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("http://tvsubs.net/");
                            stringBuilder.append(str2);
                            str2 = Jsoup.connect(stringBuilder.toString()).ignoreContentType(true).referrer("http://tvsubs.net").method(Method.GET).userAgent(BaseProvider.UserAgent).cookies(cookies).execute();
                            cookies.putAll(str2.cookies());
                            str2 = str2.parse().select("ul.list1").last().select("li").iterator();
                            while (str2.hasNext()) {
                                element = (Element) str2.next();
                                String text = element.select("a").text();
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("http://tvsubs.net/");
                                stringBuilder2.append(element.select("a").last().attr("href"));
                                String stringBuilder3 = stringBuilder2.toString();
                                SubtitleResult subtitleResult = new SubtitleResult("TVSUBS");
                                subtitleResult.setFilename(text);
                                subtitleResult.setSubtitleID(stringBuilder3);
                                subtitleResult.setLanguage("eng");
                                subtitleResult.episode = i2;
                                subtitleResult.season = i;
                                arrayList.add(subtitleResult);
                            }
                            return arrayList;
                        }
                    } catch (String str22) {
                        str22.printStackTrace();
                    }
                }
                return arrayList;
            }
        }
        return arrayList;
    }

    public String downloadSubtitle(SubtitleResult subtitleResult) {
        return download_subtitle(subtitleResult, subfilename);
    }

    public String download_subtitle(SubtitleResult subtitleResult, String str) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
            stringBuilder.append("/");
            stringBuilder.append(str);
            stringBuilder.append(".srt");
            String stringBuilder2 = stringBuilder.toString();
            URL url = new URL(subtitleResult.getSubtitleID());
            cookies.putAll(Jsoup.connect(url.toString()).ignoreContentType(true).followRedirects(true).referrer(url.toString()).userAgent(BaseProvider.UserAgent).method(Method.GET).cookies(cookies).execute().cookies());
            url = new URL(subtitleResult.getSubtitleID().replace("subtitle", "download"));
            InputStream byteArrayInputStream = new ByteArrayInputStream(Jsoup.connect(url.toString()).ignoreContentType(true).followRedirects(true).referrer(url.toString()).userAgent(BaseProvider.UserAgent).method(Method.GET).cookies(cookies).execute().bodyAsBytes());
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
            stringBuilder3.append("/");
            stringBuilder3.append(str);
            stringBuilder3.append("_tmp.zip");
            File file = new File(stringBuilder3.toString());
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
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
            stringBuilder4.append("/");
            stringBuilder4.append(str);
            stringBuilder4.append("_tmp");
            File file2 = new File(stringBuilder4.toString());
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
        } catch (SubtitleResult subtitleResult2) {
            subtitleResult2.printStackTrace();
        }
        return "";
    }
}
