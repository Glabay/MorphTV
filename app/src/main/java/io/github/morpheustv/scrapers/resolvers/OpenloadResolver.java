package io.github.morpheustv.scrapers.resolvers;

import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.helper.TaskManager;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.BaseResolver;
import io.github.morpheustv.scrapers.model.Source;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class OpenloadResolver extends BaseResolver {
    public OpenloadResolver(Scraper scraper, BaseProvider baseProvider) {
        super(scraper, baseProvider, true);
    }

    public void resolve(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        try {
            String attr;
            String wvmatchresourceafterjs = wvmatchresourceafterjs(str, "(.+openload.co/stream/.+)", 6000, str2, "document.getElementById('videooverlay').click()");
            try {
                Element first = Jsoup.parse(wvgethtml()).select("meta[name=description]").first();
                attr = first != null ? first.attr("content") : str3;
            } catch (Exception e) {
                e.printStackTrace();
                attr = str3;
            }
            if (wvmatchresourceafterjs != null && !wvmatchresourceafterjs.isEmpty()) {
                addSource(copyOnWriteArrayList, new Source(str3, "OPENLOAD", BaseProvider.getQualityFromUrl(attr), r7.PROVIDER_NAME, wvmatchresourceafterjs, attr, ""), false, true);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public Executor getExecutor() {
        return TaskManager.OPENLOAD_EXECUTOR;
    }
}
