package io.github.morpheustv.scrapers.resolvers;

import android.net.Uri;
import android.util.Base64;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.BaseResolver;
import io.github.morpheustv.scrapers.model.Source;
import java.util.concurrent.CopyOnWriteArrayList;

public class OnionplayResolver extends BaseResolver {
    public OnionplayResolver(Scraper scraper, BaseProvider baseProvider) {
        super(scraper, baseProvider, true);
    }

    public void resolve(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        if (str != null) {
            try {
                if (!str.isEmpty()) {
                    String str4 = new String(Base64.decode(Uri.parse(str).getQueryParameter("source"), 0), "UTF-8");
                    if (str4.isEmpty()) {
                        str = extractLinks(wvgethtml(str, str2, false)).iterator();
                        while (str.hasNext()) {
                            String str5 = (String) str.next();
                            if (str5.contains(".m3u8") || str5.contains(".mp4")) {
                                Source source = new Source(str3, "ONIONPLAY", BaseProvider.getQualityFromUrl(str3), this.PROVIDER_NAME, str5);
                                source.setReferer(str2);
                                addSource(copyOnWriteArrayList, source, false, str5.endsWith(".mp4"));
                            }
                        }
                    } else if (str4.contains(".m3u8") != null || str4.contains(".mp4") != null) {
                        String source2 = new Source(str3, "ONIONPLAY", BaseProvider.getQualityFromUrl(str3), this.PROVIDER_NAME, str4);
                        source2.setReferer(str2);
                        addSource(copyOnWriteArrayList, source2, false, true);
                    }
                }
            } catch (String str6) {
                str6.printStackTrace();
            }
        }
    }
}
