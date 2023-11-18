package io.github.morpheustv.scrapers.model;

import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.helper.TaskManager;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

public abstract class BaseResolver extends BaseProvider {
    public void resolve(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
    }

    public BaseResolver(Scraper scraper, BaseProvider baseProvider, boolean z) {
        super(scraper, baseProvider.PROVIDER_NAME, z);
        runningResolvers++;
    }

    public void initialize() {
        if (getScraper().getController() != null) {
            getScraper().getController().addProvider(this);
        }
    }

    public void destroy() {
        runningResolvers--;
        if (runningResolvers < 0) {
            runningResolvers = 0;
        }
        getScraper().getController().destroyWebview(this);
    }

    public Executor getExecutor() {
        return TaskManager.RESOLVER_EXECUTOR;
    }

    public void execute(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        final String str4 = str;
        final String str5 = str2;
        final String str6 = str3;
        final CopyOnWriteArrayList<Source> copyOnWriteArrayList2 = copyOnWriteArrayList;
        getExecutor().execute(new Runnable() {
            public void run() {
                BaseResolver.this.initialize();
                BaseResolver.this.resolve(str4, str5, str6, copyOnWriteArrayList2);
                BaseResolver.this.destroy();
            }
        });
    }
}
