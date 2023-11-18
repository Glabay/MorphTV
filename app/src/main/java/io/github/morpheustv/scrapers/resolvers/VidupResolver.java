package io.github.morpheustv.scrapers.resolvers;

import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.helper.TaskManager;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.BaseResolver;
import io.github.morpheustv.scrapers.model.Source;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

public class VidupResolver extends BaseResolver {
    public VidupResolver(Scraper scraper, BaseProvider baseProvider) {
        super(scraper, baseProvider, true);
    }

    public void resolve(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        VidupResolver vidupResolver = this;
        try {
            Matcher matcher = Pattern.compile("title\\s*:\\s*'([^']+)").matcher(wvgethtml(str));
            String group = (!matcher.find() || matcher.groupCount() <= 0) ? str3 : matcher.group(1);
            JSONArray jSONArray = new JSONArray(wveval("(function() { return (jwplayer().getPlaylist()); })();")).getJSONObject(0).getJSONArray("allSources");
            if (jSONArray.length() > 0) {
                for (int i = 0; i < jSONArray.length(); i++) {
                    Source source;
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    String optString = jSONObject.optString("file");
                    String optString2 = jSONObject.optString("label");
                    if (optString2 == null || optString2.isEmpty()) {
                        source = new Source(str3, "VIDUP", BaseProvider.getQualityFromUrl(group), vidupResolver.PROVIDER_NAME, optString);
                    } else {
                        Source source2 = new Source(str3, "VIDUP", getLabelQuality(optString2), vidupResolver.PROVIDER_NAME, optString);
                    }
                    source.setReferer(str2);
                    addSource(copyOnWriteArrayList, source, false, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Executor getExecutor() {
        return TaskManager.THEVIDEO_EXECUTOR;
    }
}
