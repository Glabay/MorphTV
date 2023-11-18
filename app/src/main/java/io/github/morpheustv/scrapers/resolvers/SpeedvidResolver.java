package io.github.morpheustv.scrapers.resolvers;

import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.helper.TaskManager;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.BaseResolver;
import io.github.morpheustv.scrapers.model.Source;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import org.json.JSONArray;
import org.json.JSONObject;

public class SpeedvidResolver extends BaseResolver {
    public SpeedvidResolver(Scraper scraper, BaseProvider baseProvider) {
        super(scraper, baseProvider, true);
        this.resourceWhitelist = new String[]{"(.*chk_jschl?.*)", "(.*jquery.*.js.*)", "(.*speedvid.net.*.js.*)"};
    }

    public void resolve(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        try {
            wvgethtml(str, str2, false);
            str = wveval("(function() { return (jwplayer().getPlaylist()); })();");
            if (str != null && !str.isEmpty()) {
                str = new JSONArray(str).getJSONObject(0).getJSONArray("allSources");
                if (str.length() > 0) {
                    for (int i = 0; i < str.length(); i++) {
                        Source source;
                        JSONObject jSONObject = str.getJSONObject(i);
                        String optString = jSONObject.optString("file");
                        String optString2 = jSONObject.optString("label");
                        if (optString2 == null || optString2.isEmpty()) {
                            Source source2 = new Source(str3, "SPEEDVID", BaseProvider.getQualityFromUrl(str3), this.PROVIDER_NAME, optString);
                        } else {
                            source = new Source(str3, "SPEEDVID", getLabelQuality(optString2), this.PROVIDER_NAME, optString);
                        }
                        source.setReferer(str2);
                        addSource(copyOnWriteArrayList, source, false, true);
                    }
                }
            }
        } catch (String str4) {
            str4.printStackTrace();
        }
    }

    public Executor getExecutor() {
        return TaskManager.THEVIDEO_EXECUTOR;
    }
}
