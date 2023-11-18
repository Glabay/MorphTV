package io.github.morpheustv.scrapers.resolvers;

import android.support.graphics.drawable.PathInterpolatorCompat;
import com.google.common.net.HttpHeaders;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.BaseResolver;
import io.github.morpheustv.scrapers.model.Source;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

public class VidcloudResolver extends BaseResolver {
    public VidcloudResolver(Scraper scraper, BaseProvider baseProvider) {
        super(scraper, baseProvider, false);
    }

    public void resolve(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        if (str != null) {
            try {
                if (str.isEmpty() == null) {
                    str2 = Pattern.compile("(?://|\\.)((?:vcstream|loadvid)\\.(?:to|online))/(?:embed/)?([0-9a-zA-Z]+)").matcher(str);
                    if (str2.find() && str2.groupCount() > 1) {
                        str2 = str2.group(2);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("https://vcstream.to/player?fid=");
                        stringBuilder.append(str2);
                        stringBuilder.append("&page=embed");
                        str2 = new JSONObject(Jsoup.connect(stringBuilder.toString()).validateTLSCertificates(false).timeout(PathInterpolatorCompat.MAX_NUM_POINTS).followRedirects(true).ignoreContentType(true).ignoreHttpErrors(true).header(HttpHeaders.X_REQUESTED_WITH, "XMLHttpRequest").referrer(str).method(Method.GET).execute().body()).optString("html");
                        if (str2 != null && !str2.isEmpty()) {
                            str2 = str2.replace("\n", "");
                            if (str2.contains("sources:")) {
                                str2 = str2.substring(str2.indexOf("sources:") + 8);
                                str2 = str2.substring(0, str2.indexOf("}]") + 2);
                            } else {
                                str2 = str2.substring(str2.indexOf("updateSrc(") + 10);
                                str2 = str2.substring(0, str2.indexOf(");"));
                            }
                            JSONArray jSONArray = new JSONArray(str2.trim());
                            if (jSONArray.length() > null) {
                                for (str2 = null; str2 < jSONArray.length(); str2++) {
                                    JSONObject jSONObject = jSONArray.getJSONObject(str2);
                                    String optString = jSONObject.optString("src");
                                    String optString2 = jSONObject.optString("name", str);
                                    String str4 = "VIDCLOUD";
                                    if (optString.contains(".m3u8")) {
                                        StringBuilder stringBuilder2 = new StringBuilder();
                                        stringBuilder2.append(str4);
                                        stringBuilder2.append(" HLS");
                                        str4 = stringBuilder2.toString();
                                    }
                                    Source source = new Source(str3, str4, BaseProvider.getQualityFromUrl(optString2), this.PROVIDER_NAME, optString);
                                    source.setReferer(str);
                                    addSource(copyOnWriteArrayList, source, false, true);
                                }
                            }
                        }
                    }
                }
            } catch (String str5) {
                str5.printStackTrace();
            }
        }
    }
}
