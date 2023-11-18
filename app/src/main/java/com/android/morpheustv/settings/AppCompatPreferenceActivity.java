package com.android.morpheustv.settings;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public abstract class AppCompatPreferenceActivity extends PreferenceActivity {
    private AppCompatDelegate mDelegate;

    protected void onCreate(Bundle bundle) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(bundle);
        super.onCreate(bundle);
    }

    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        getDelegate().onPostCreate(bundle);
    }

    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    public MenuInflater getMenuInflater() {
        return getDelegate().getMenuInflater();
    }

    public void setContentView(@LayoutRes int i) {
        getDelegate().setContentView(i);
    }

    public void setContentView(View view) {
        getDelegate().setContentView(view);
    }

    public void setContentView(View view, LayoutParams layoutParams) {
        getDelegate().setContentView(view, layoutParams);
    }

    public void addContentView(View view, LayoutParams layoutParams) {
        getDelegate().addContentView(view, layoutParams);
    }

    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();
    }

    protected void onTitleChanged(CharSequence charSequence, int i) {
        super.onTitleChanged(charSequence, i);
        getDelegate().setTitle(charSequence);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        getDelegate().onConfigurationChanged(configuration);
    }

    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
    }

    public void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    private AppCompatDelegate getDelegate() {
        if (this.mDelegate == null) {
            this.mDelegate = AppCompatDelegate.create((Activity) this, null);
        }
        return this.mDelegate;
    }
}
