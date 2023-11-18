package com.android.morpheustv.sources;

import android.content.Context;
import android.support.v4.view.InputDeviceCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.noname.titan.R;
import java.util.ArrayList;
import java.util.List;

public class SubtitleListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private SubtitleResult currentSelection;
    private List<ListGroup> groups = new ArrayList();

    static class ListGroup {
        public List<SubtitleResult> sources;
        public String title;

        ListGroup() {
        }
    }

    public long getChildId(int i, int i2) {
        return (long) i2;
    }

    public long getGroupId(int i) {
        return (long) i;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    public SubtitleListAdapter(Context context) {
        this._context = context;
    }

    public void addSource(SubtitleResult subtitleResult) {
        String str = subtitleResult.provider;
        for (ListGroup listGroup : this.groups) {
            if (listGroup.title.equals(str)) {
                listGroup.sources.add(subtitleResult);
                return;
            }
        }
        ListGroup listGroup2 = new ListGroup();
        listGroup2.title = str;
        listGroup2.sources = new ArrayList();
        listGroup2.sources.add(subtitleResult);
        this.groups.add(listGroup2);
    }

    public void setSources(List<SubtitleResult> list) {
        this.groups.clear();
        for (SubtitleResult addSource : list) {
            addSource(addSource);
        }
        notifyDataSetChanged();
    }

    public void setCurrentSelection(SubtitleResult subtitleResult) {
        this.currentSelection = subtitleResult;
        notifyDataSetChanged();
    }

    public SubtitleResult getCurrentSelection() {
        return this.currentSelection;
    }

    public SubtitleResult getChild(int i, int i2) {
        return (SubtitleResult) ((ListGroup) this.groups.get(i)).sources.get(i2);
    }

    public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(R.layout.subtitle_list_item, null);
        }
        i = getChild(i, i2);
        ((TextView) view.findViewById(R.id.filename)).setText(i.getFilename());
        ((TextView) view.findViewById(R.id.source)).setText(i.getLanguage());
        return view;
    }

    public int getChildrenCount(int i) {
        return ((ListGroup) this.groups.get(i)).sources.size();
    }

    public ListGroup getGroup(int i) {
        return (ListGroup) this.groups.get(i);
    }

    public int getGroupCount() {
        return this.groups.size();
    }

    public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
        i = getGroup(i);
        z = i.title;
        if (view == null) {
            view = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(R.layout.source_list_group, null);
        }
        TextView textView = (TextView) view.findViewById(R.id.lblListHeaderLeft);
        textView.setTypeface(null, 1);
        textView.setText(z);
        TextView textView2 = (TextView) view.findViewById(true);
        textView2.setTypeface(null, 0);
        textView2.setText(String.valueOf(i.sources.size()));
        textView2.setTextColor(InputDeviceCompat.SOURCE_ANY);
        return view;
    }
}
