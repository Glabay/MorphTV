package com.android.morpheustv.content;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.android.morpheustv.BaseActivity;
import com.android.morpheustv.BaseActivity.SpeechListener;
import com.google.android.gms.actions.SearchIntents;
import com.noname.titan.R;

public class MoviesMain extends BaseActivity {

    /* renamed from: com.android.morpheustv.content.MoviesMain$2 */
    class C04352 implements OnClickListener {
        C04352() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    /* renamed from: com.android.morpheustv.content.MoviesMain$3 */
    class C04363 implements OnClickListener {
        public void onClick(DialogInterface dialogInterface, int i) {
        }

        C04363() {
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_movie_main);
    }

    public void Trending(View view) {
        view = new Intent(this, MovieList.class);
        view.putExtra("content", 1);
        ContextCompat.startActivity(this, view, ActivityOptionsCompat.makeBasic().toBundle());
    }

    public void Popular(View view) {
        view = new Intent(this, MovieList.class);
        view.putExtra("content", 2);
        ContextCompat.startActivity(this, view, ActivityOptionsCompat.makeBasic().toBundle());
    }

    public void Collection(View view) {
        view = new Intent(this, MovieList.class);
        view.putExtra("content", 3);
        ContextCompat.startActivity(this, view, ActivityOptionsCompat.makeBasic().toBundle());
    }

    public void Watchlist(View view) {
        view = new Intent(this, MovieList.class);
        view.putExtra("content", 5);
        ContextCompat.startActivity(this, view, ActivityOptionsCompat.makeBasic().toBundle());
    }

    public void Search(final View view) {
        Builder builder = new Builder(this);
        builder.setTitle((CharSequence) "Search Movie");
        View inflate = getLayoutInflater().inflate(R.layout.input_view, null);
        final EditText editText = (EditText) inflate.findViewById(R.id.input);
        builder.setView(inflate);
        builder.setPositiveButton((CharSequence) "OK", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MoviesMain.this.doSearch(view, editText.getText().toString());
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton((CharSequence) "Cancel", new C04352());
        builder.setNeutralButton((CharSequence) "Voice", new C04363());
        final AlertDialog create = builder.create();
        create.setOnShowListener(new OnShowListener() {

            /* renamed from: com.android.morpheustv.content.MoviesMain$4$1 */
            class C04371 implements View.OnClickListener {
                C04371() {
                }

                public void onClick(View view) {
                    MoviesMain.this.startSpeechRecognition();
                }
            }

            public void onShow(DialogInterface dialogInterface) {
                create.getButton(-3).setOnClickListener(new C04371());
            }
        });
        editText.requestFocus();
        editText.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 3) {
                    return null;
                }
                create.getButton(-1).performClick();
                return true;
            }
        });
        ((InputMethodManager) getSystemService("input_method")).showSoftInput(editText, 1);
        this.currentSpeechPrompt = "Search Movie";
        this.currentSpeechListener = new SpeechListener() {
            public void onSpeechRecognized(String str) {
                editText.setText(str);
                MoviesMain.this.doSearch(view, editText.getText().toString());
                create.dismiss();
            }
        };
        create.show();
    }

    public void doSearch(View view, String str) {
        view = new Intent(this, MovieList.class);
        view.putExtra("content", 4);
        view.putExtra(SearchIntents.EXTRA_QUERY, str);
        ContextCompat.startActivity(this, view, ActivityOptionsCompat.makeBasic().toBundle());
    }
}
