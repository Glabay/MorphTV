package com.android.morpheustv.content;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.morpheustv.BaseActivity;
import com.android.morpheustv.helpers.InformationUpdater;
import com.android.morpheustv.helpers.InformationUpdater.InfoEntry;
import com.noname.titan.R;
import java.util.ArrayList;

public class Information extends BaseActivity {
    private ArrayAdapter<InfoEntry> adapter;
    private int currentSelection = 0;
    private boolean inList = true;
    private ArrayList<InfoEntry> infoEntries;
    private ListView list;

    /* renamed from: com.android.morpheustv.content.Information$2 */
    class C04152 implements OnItemSelectedListener {
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        C04152() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            if (Information.this.infoEntries != null && Information.this.infoEntries.size() > null && i < Information.this.infoEntries.size()) {
                Information.this.currentSelection = i;
                Information.this.adapter.notifyDataSetChanged();
            }
        }
    }

    /* renamed from: com.android.morpheustv.content.Information$3 */
    class C04163 implements OnItemClickListener {
        C04163() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (Information.this.infoEntries != null && Information.this.infoEntries.size() > null && i < Information.this.infoEntries.size()) {
                InfoEntry infoEntry = (InfoEntry) Information.this.infoEntries.get(i);
                if (i == Information.this.currentSelection) {
                    Information.this.showContentMessage(infoEntry);
                } else {
                    Information.this.currentSelection = i;
                }
                Information.this.adapter.notifyDataSetChanged();
            }
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initializeListView();
    }

    private void initializeListView() {
        setContentView(R.layout.activity_information);
        this.inList = true;
        this.infoEntries = new ArrayList();
        if (InformationUpdater.lastInfoEntries != null) {
            this.infoEntries.addAll(InformationUpdater.lastInfoEntries.entries);
        }
        this.list = (ListView) findViewById(R.id.entryList);
        this.adapter = new ArrayAdapter<InfoEntry>(this, R.layout.infoentry_list_item, this.infoEntries) {
            public View getView(int i, View view, ViewGroup viewGroup) {
                try {
                    InfoEntry infoEntry = (InfoEntry) Information.this.infoEntries.get(i);
                    if (view == null) {
                        view = Information.this.getLayoutInflater().inflate(R.layout.infoentry_list_item, null);
                    }
                    ((TextView) view.findViewById(R.id.entry_title)).setText(infoEntry.TITLE);
                    if (Information.this.currentSelection == i) {
                        view.setBackgroundResource(R.drawable.button_selector_fixed);
                    } else {
                        view.setBackgroundResource(0);
                    }
                } catch (int i2) {
                    i2.printStackTrace();
                }
                return view;
            }
        };
        this.list.setAdapter(this.adapter);
        this.list.setEmptyView(findViewById(R.id.empty_list_view));
        this.list.setOnItemSelectedListener(new C04152());
        this.list.setOnItemClickListener(new C04163());
        this.list.requestFocus();
        this.adapter.notifyDataSetChanged();
    }

    public void showContentMessage(InfoEntry infoEntry) {
        setContentView(R.layout.activity_info_message);
        this.inList = false;
        ((TextView) findViewById(R.id.breadcrumb_title)).setText(infoEntry.TITLE);
        ((TextView) findViewById(R.id.entry_content)).setText(infoEntry.CONTENT);
    }

    public void onBackPressed() {
        if (this.inList) {
            super.onBackPressed();
        } else {
            initializeListView();
        }
    }
}
