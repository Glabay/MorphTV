package com.android.morpheustv;

import android.os.StrictMode;
import android.os.StrictMode.VmPolicy.Builder;
import android.support.multidex.MultiDexApplication;

public class MyApplication extends MultiDexApplication {
    public void onCreate() {
        super.onCreate();
        StrictMode.setVmPolicy(new Builder().build());
    }
}
