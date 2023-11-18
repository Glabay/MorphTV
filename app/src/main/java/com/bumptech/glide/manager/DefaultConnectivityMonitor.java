package com.bumptech.glide.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import com.bumptech.glide.manager.ConnectivityMonitor.ConnectivityListener;

class DefaultConnectivityMonitor implements ConnectivityMonitor {
    private final BroadcastReceiver connectivityReceiver = new C05971();
    private final Context context;
    private boolean isConnected;
    private boolean isRegistered;
    private final ConnectivityListener listener;

    /* renamed from: com.bumptech.glide.manager.DefaultConnectivityMonitor$1 */
    class C05971 extends BroadcastReceiver {
        C05971() {
        }

        public void onReceive(Context context, Intent intent) {
            intent = DefaultConnectivityMonitor.this.isConnected;
            DefaultConnectivityMonitor.this.isConnected = DefaultConnectivityMonitor.this.isConnected(context);
            if (intent != DefaultConnectivityMonitor.this.isConnected) {
                DefaultConnectivityMonitor.this.listener.onConnectivityChanged(DefaultConnectivityMonitor.this.isConnected);
            }
        }
    }

    public void onDestroy() {
    }

    public DefaultConnectivityMonitor(Context context, ConnectivityListener connectivityListener) {
        this.context = context.getApplicationContext();
        this.listener = connectivityListener;
    }

    private void register() {
        if (!this.isRegistered) {
            this.isConnected = isConnected(this.context);
            this.context.registerReceiver(this.connectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            this.isRegistered = true;
        }
    }

    private void unregister() {
        if (this.isRegistered) {
            this.context.unregisterReceiver(this.connectivityReceiver);
            this.isRegistered = false;
        }
    }

    private boolean isConnected(Context context) {
        context = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return (context == null || context.isConnected() == null) ? null : true;
    }

    public void onStart() {
        register();
    }

    public void onStop() {
        unregister();
    }
}
