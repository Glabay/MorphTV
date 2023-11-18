package com.android.morpheustv.content;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import com.android.morpheustv.BaseActivity;
import com.noname.titan.R;

public class MyTVShows extends BaseActivity {
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_myshows_main);
    }

    public void Collection(View view) {
        view = new Intent(this, ShowList.class);
        view.putExtra("content", 3);
        ContextCompat.startActivity(this, view, ActivityOptionsCompat.makeBasic().toBundle());
    }

    public void Watchlist(View view) {
        view = new Intent(this, ShowList.class);
        view.putExtra("content", 5);
        ContextCompat.startActivity(this, view, ActivityOptionsCompat.makeBasic().toBundle());
    }

    public void Episodes(View view) {
        ContextCompat.startActivity(this, new Intent(this, NextEpisodesList.class), ActivityOptionsCompat.makeBasic().toBundle());
    }
}
