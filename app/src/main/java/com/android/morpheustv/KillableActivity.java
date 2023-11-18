package com.android.morpheustv;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class KillableActivity extends AppCompatActivity {
    public static String KILL_ACTION = "KillSwitch";
    private final BroadcastReceiver killAppBroadcast = new C03851();

    /* renamed from: com.android.morpheustv.KillableActivity$1 */
    class C03851 extends BroadcastReceiver {
        C03851() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(KillableActivity.KILL_ACTION) != null) {
                KillableActivity.this.finish();
            }
        }
    }

    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        registerReceiver(this.killAppBroadcast, new IntentFilter(KILL_ACTION));
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.killAppBroadcast);
    }
}
