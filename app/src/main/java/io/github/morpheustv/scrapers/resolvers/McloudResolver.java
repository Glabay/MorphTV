package io.github.morpheustv.scrapers.resolvers;

import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.helper.Utils;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.BaseResolver;
import io.github.morpheustv.scrapers.model.Source;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jsoup.Jsoup;

public class McloudResolver extends BaseResolver {
    public McloudResolver(Scraper scraper, BaseProvider baseProvider) {
        super(scraper, baseProvider, true);
    }

    public void resolve(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        try {
            String wvmatchresourceafterjs = wvmatchresourceafterjs(str, "(.*.m3u8.*)", 8000, str2, "");
            String text = Jsoup.parse(wvgethtml()).select("title").first().text();
            if (wvmatchresourceafterjs != null && !wvmatchresourceafterjs.isEmpty()) {
                Source source = new Source(str3, "MCLOUD HLS", BaseProvider.getQualityFromUrl(text), r7.PROVIDER_NAME, wvmatchresourceafterjs, Utils.getFilenameFromUrl(wvmatchresourceafterjs, text), "");
                source.setReferer(str);
                addSource(copyOnWriteArrayList, source, false, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
