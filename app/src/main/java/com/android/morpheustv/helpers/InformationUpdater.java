package com.android.morpheustv.helpers;

import android.util.Log;
import com.google.gson.Gson;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jsoup.Jsoup;

public class InformationUpdater {
    public static InfoEntryList lastInfoEntries;

    public interface InformationListener {
        void onInformationFound(InfoEntryList infoEntryList);

        void onNoInformationAvailable();
    }

    public static class InfoEntry {
        public String CONTENT;
        public String ID;
        public boolean IMPORTANT;
        public String TITLE;
    }

    public static class InfoEntryList {
        public List<InfoEntry> entries;
    }

    public static void CheckForInformation(final InformationListener informationListener) {
        new Thread(new Runnable() {

            /* renamed from: com.android.morpheustv.helpers.InformationUpdater$1$1 */
            class C04961 implements Comparator<InfoEntry> {
                C04961() {
                }

                public int compare(InfoEntry infoEntry, InfoEntry infoEntry2) {
                    return infoEntry2.ID.compareToIgnoreCase(infoEntry.ID);
                }
            }

            public void run() {
                try {
                    Log.d("InformationUpdater", "Checking for information updates...");
                    InformationUpdater.lastInfoEntries = (InfoEntryList) new Gson().fromJson(Jsoup.connect("https://titaniumtv.xyz/news.json").ignoreContentType(true).validateTLSCertificates(false).execute().body(), InfoEntryList.class);
                    if (!(InformationUpdater.lastInfoEntries == null || InformationUpdater.lastInfoEntries.entries == null)) {
                        Collections.sort(InformationUpdater.lastInfoEntries.entries, new C04961());
                    }
                    if (informationListener == null) {
                        return;
                    }
                    if (InformationUpdater.lastInfoEntries.entries == null || InformationUpdater.lastInfoEntries.entries.size() <= 0) {
                        Log.d("InformationUpdater", "No information available.");
                        informationListener.onNoInformationAvailable();
                        return;
                    }
                    Log.d("InformationUpdater", "Information found.");
                    informationListener.onInformationFound(InformationUpdater.lastInfoEntries);
                } catch (Exception e) {
                    Log.d("InformationUpdater", "Error checking information.");
                    e.printStackTrace();
                    if (informationListener != null) {
                        informationListener.onNoInformationAvailable();
                    }
                }
            }
        }).start();
    }
}
