package com.android.morpheustv.helpers;

import android.util.Log;
import com.android.morpheustv.settings.Settings;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Iterator;
import org.jsoup.Jsoup;

public class AppUpdater {
    private static final String AptoideURL = " ";
    private static final String BetaURL = "http://titaniumtv.xyz/update-changelog.json";
    private static final String GithubURL = "";

    public interface AppUpdateListener {
        void onNoUpdatesAvailable();

        void onUpdateFound(UpdateData updateData);
    }

    public static class UpdateData {
        public Data data;

        public static class Data {
            public DataFile file;
            public Media media;

            public static class DataFile {
                public String path;
                public int vercode;
                public String vername;
            }

            public static class Media {
                public String news;
            }
        }
    }

    public static void CheckForUpdates(final AppUpdateListener appUpdateListener) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Log.d("AppUpdater", "Checking for app updates...");
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(" ");
                    arrayList.add(AppUpdater.BetaURL);
                    if (Settings.BETA_UPDATES == 1) {
                        arrayList.add("");
                    }
                    ArrayList arrayList2 = new ArrayList();
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        try {
                            arrayList2.add(new Gson().fromJson(Jsoup.connect((String) it.next()).ignoreContentType(true).validateTLSCertificates(false).execute().body(), UpdateData.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (appUpdateListener != null) {
                        UpdateData updateData = null;
                        Iterator it2 = arrayList2.iterator();
                        while (it2.hasNext()) {
                            UpdateData updateData2 = (UpdateData) it2.next();
                            if (updateData2.data.file.vercode > 90) {
                                if (updateData != null) {
                                    if (updateData2.data.file.vercode > updateData.data.file.vercode) {
                                    }
                                }
                                updateData = updateData2;
                            }
                        }
                        if (updateData != null) {
                            Log.d("AppUpdater", "New update found.");
                            appUpdateListener.onUpdateFound(updateData);
                            return;
                        }
                        Log.d("AppUpdater", "No updates");
                        appUpdateListener.onNoUpdatesAvailable();
                    }
                } catch (Exception e2) {
                    Log.d("AppUpdater", "Error checking updates.");
                    e2.printStackTrace();
                    if (appUpdateListener != null) {
                        appUpdateListener.onNoUpdatesAvailable();
                    }
                }
            }
        }).start();
    }
}
