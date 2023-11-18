package io.github.morpheustv.scrapers.helper;

import io.github.morpheustv.scrapers.model.ScraperMediaInfo;
import io.github.morpheustv.scrapers.model.ScrapingStatus;
import io.github.morpheustv.scrapers.model.Source;

public interface ScraperListener {
    void onFinish(ScraperMediaInfo scraperMediaInfo, boolean z);

    void onSourceFound(Source source);

    void onStart(ScraperMediaInfo scraperMediaInfo);

    void onStatusUpdated(ScrapingStatus scrapingStatus);
}
