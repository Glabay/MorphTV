package com.android.morpheustv.settings;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity.Header;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.morpheustv.helpers.Utils;
import com.noname.titan.R;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SettingsActivity extends AppCompatPreferenceActivity {
    private static OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new C05311();

    /* renamed from: com.android.morpheustv.settings.SettingsActivity$1 */
    static class C05311 implements OnPreferenceChangeListener {
        C05311() {
        }

        public boolean onPreferenceChange(Preference preference, Object obj) {
            CharSequence replace = obj.toString().replace(":", "");
            if ((obj instanceof Set) != null) {
                replace = replace.replace("[", "").replace("]", "");
            }
            preference.setSummary(replace);
            return true;
        }
    }

    @TargetApi(11)
    public static class AutoPlayPreferenceFragment extends PreferenceFragment {
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            addPreferencesFromResource(R.xml.pref_autoplay);
            setHasOptionsMenu(true);
            SettingsActivity.bindPreferenceSummaryToSet(findPreference("prefSourceAutoSelQualityMoviesList"));
            SettingsActivity.bindPreferenceSummaryToSet(findPreference("prefSourceAutoSelHostMoviesList"));
            SettingsActivity.bindPreferenceSummaryToSet(findPreference("prefSourceAutoSelQualityShowsList"));
            SettingsActivity.bindPreferenceSummaryToSet(findPreference("prefSourceAutoSelHostShowsList"));
        }

        public boolean onOptionsItemSelected(MenuItem menuItem) {
            if (menuItem.getItemId() == 16908332) {
                return true;
            }
            return super.onOptionsItemSelected(menuItem);
        }
    }

    @TargetApi(11)
    public static class DownloadsPreferenceFragment extends PreferenceFragment {
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            addPreferencesFromResource(R.xml.pref_downloads);
            setHasOptionsMenu(true);
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefDownloadsDownloadFolder"));
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefMaxSimulDownloads"));
        }

        public boolean onOptionsItemSelected(MenuItem menuItem) {
            if (menuItem.getItemId() == 16908332) {
                return true;
            }
            return super.onOptionsItemSelected(menuItem);
        }
    }

    @TargetApi(11)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            addPreferencesFromResource(R.xml.pref_scrapers);
            setHasOptionsMenu(true);
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefMaxThreads"));
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefTimeout"));
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefTimeoutThread"));
        }

        public boolean onOptionsItemSelected(MenuItem menuItem) {
            if (menuItem.getItemId() == 16908332) {
                return true;
            }
            return super.onOptionsItemSelected(menuItem);
        }
    }

    @TargetApi(11)
    public static class NotificationsPreferenceFragment extends PreferenceFragment {
        int cnt = 0;

        /* renamed from: com.android.morpheustv.settings.SettingsActivity$NotificationsPreferenceFragment$1 */
        class C05321 implements OnPreferenceClickListener {
            C05321() {
            }

            public boolean onPreferenceClick(Preference preference) {
                preference = NotificationsPreferenceFragment.this;
                preference.cnt++;
                if (NotificationsPreferenceFragment.this.cnt >= 15) {
                    NotificationsPreferenceFragment.this.cnt = 0;
                    if (Settings.loadInt(NotificationsPreferenceFragment.this.getActivity(), "prefAlternateUpdater") == null) {
                        Settings.saveInt(NotificationsPreferenceFragment.this.getActivity(), "prefAlternateUpdater", 1);
                        Toast.makeText(NotificationsPreferenceFragment.this.getActivity(), "Beta update server enabled.", 1).show();
                    } else {
                        Settings.saveInt(NotificationsPreferenceFragment.this.getActivity(), "prefAlternateUpdater", 0);
                        Toast.makeText(NotificationsPreferenceFragment.this.getActivity(), "Beta update server disabled.", 1).show();
                    }
                }
                return false;
            }
        }

        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            addPreferencesFromResource(R.xml.pref_notifications);
            findPreference("prefDownloadNotifications2").setOnPreferenceClickListener(new C05321());
            setHasOptionsMenu(true);
        }

        public boolean onOptionsItemSelected(MenuItem menuItem) {
            if (menuItem.getItemId() == 16908332) {
                return true;
            }
            return super.onOptionsItemSelected(menuItem);
        }
    }

    @TargetApi(11)
    public static class PlayerPreferenceFragment extends PreferenceFragment {

        /* renamed from: com.android.morpheustv.settings.SettingsActivity$PlayerPreferenceFragment$1 */
        class C05341 implements OnPreferenceChangeListener {

            /* renamed from: com.android.morpheustv.settings.SettingsActivity$PlayerPreferenceFragment$1$1 */
            class C05331 implements OnClickListener {
                C05331() {
                }

                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(null);
                }
            }

            C05341() {
            }

            public boolean onPreferenceChange(Preference preference, Object obj) {
                Utils.showDialog(PlayerPreferenceFragment.this.getActivity(), PlayerPreferenceFragment.this.getString(R.string.reboot_required), PlayerPreferenceFragment.this.getString(R.string.pref_title_cast_app_reboot), new C05331());
                return true;
            }
        }

        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            addPreferencesFromResource(R.xml.pref_player);
            setHasOptionsMenu(true);
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefBufferForPlayback"));
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefMinBufferSize"));
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefMaxBufferSize"));
            ListPreference listPreference = (ListPreference) findPreference("prefCastReceiver");
            SettingsActivity.bindPreferenceSummaryToEntryName(listPreference);
            listPreference.setOnPreferenceChangeListener(new C05341());
        }

        public boolean onOptionsItemSelected(MenuItem menuItem) {
            if (menuItem.getItemId() == 16908332) {
                return true;
            }
            return super.onOptionsItemSelected(menuItem);
        }
    }

    @TargetApi(11)
    public static class SourcesPreferenceFragment extends PreferenceFragment {
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            addPreferencesFromResource(R.xml.pref_sources);
            setHasOptionsMenu(true);
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefSourceGroup"));
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefTimeoutVerify"));
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefMaxSourcesProvider"));
        }

        public boolean onOptionsItemSelected(MenuItem menuItem) {
            if (menuItem.getItemId() == 16908332) {
                return true;
            }
            return super.onOptionsItemSelected(menuItem);
        }
    }

    @TargetApi(11)
    public static class SubtitlesPreferenceFragment extends PreferenceFragment {
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            addPreferencesFromResource(R.xml.pref_subtitles);
            setHasOptionsMenu(true);
            SettingsActivity.bindPreferenceSummaryToSet(findPreference("prefSubtitleSecondaryLangs"));
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefSubtitlePrimaryLang"));
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefSubtitlesColor"));
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefSubtitlesTextSize"));
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefSubtitlesDistance"));
        }

        public boolean onOptionsItemSelected(MenuItem menuItem) {
            if (menuItem.getItemId() == 16908332) {
                return true;
            }
            return super.onOptionsItemSelected(menuItem);
        }
    }

    @TargetApi(11)
    public static class TorrentsPreferenceFragment extends PreferenceFragment {
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            addPreferencesFromResource(R.xml.pref_torrents);
            setHasOptionsMenu(true);
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefTorrentsDownloadFolder"));
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefTorrentsMaxConnections"));
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefTorrentsDownloadLimit"));
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefTorrentsUploadLimit"));
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefTorrentsSizeMovies"));
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("prefTorrentsSizeShows"));
        }

        public boolean onOptionsItemSelected(MenuItem menuItem) {
            if (menuItem.getItemId() == 16908332) {
                return true;
            }
            return super.onOptionsItemSelected(menuItem);
        }
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
    }

    private static void bindPreferenceSummaryToEntryName(ListPreference listPreference) {
        listPreference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(listPreference, listPreference.getEntry());
    }

    private static void bindPreferenceSummaryToSet(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getStringSet(preference.getKey(), new HashSet(Arrays.asList(new String[]{"por", "pob", "eng"}))));
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setupActionBar();
    }

    private void setupActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @TargetApi(11)
    public void onBuildHeaders(List<Header> list) {
        loadHeadersFromResource(R.xml.pref_headers, list);
    }

    protected boolean isValidFragment(String str) {
        if (!(PreferenceFragment.class.getName().equals(str) || GeneralPreferenceFragment.class.getName().equals(str) || SourcesPreferenceFragment.class.getName().equals(str) || SubtitlesPreferenceFragment.class.getName().equals(str) || TorrentsPreferenceFragment.class.getName().equals(str) || DownloadsPreferenceFragment.class.getName().equals(str) || NotificationsPreferenceFragment.class.getName().equals(str) || AutoPlayPreferenceFragment.class.getName().equals(str))) {
            if (PlayerPreferenceFragment.class.getName().equals(str) == null) {
                return null;
            }
        }
        return true;
    }
}
