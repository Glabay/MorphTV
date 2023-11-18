package com.android.morpheustv.sources;

import android.content.Context;
import android.support.v4.view.InputDeviceCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.morpheustv.helpers.Utils;
import com.android.morpheustv.settings.Settings;
import com.noname.titan.R;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.Source;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SourceListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private int currentChildSelection = 0;
    private int currentGroupSelection = 0;
    private List<ListGroup> groups;

    /* renamed from: com.android.morpheustv.sources.SourceListAdapter$1 */
    class C05651 implements Comparator<ListGroup> {
        C05651() {
        }

        public int compare(ListGroup listGroup, ListGroup listGroup2) {
            if (Settings.GROUP_SOURCES.toLowerCase().equals(SourceListAdapter.this._context.getString(R.string.pref_sourcelist_grouping_size).toLowerCase())) {
                return -Long.compare(listGroup.title_value, listGroup2.title_value);
            }
            return listGroup.title.compareTo(listGroup2.title);
        }
    }

    static class ListGroup {
        public List<Source> sources;
        public String title;
        public long title_value;

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

    public SourceListAdapter(Context context) {
        this._context = context;
        this.groups = new ArrayList();
    }

    public void addSource(Source source) {
        String source2 = source.getSource();
        if (Settings.GROUP_SOURCES.toLowerCase().equals(this._context.getString(R.string.pref_sourcelist_grouping_provider).toLowerCase())) {
            source2 = source.getProvider();
        }
        if (Settings.GROUP_SOURCES.toLowerCase().equals(this._context.getString(R.string.pref_sourcelist_grouping_quality).toLowerCase())) {
            source2 = source.getQuality();
        }
        long j = 0;
        if (Settings.GROUP_SOURCES.toLowerCase().equals(this._context.getString(R.string.pref_sourcelist_grouping_size).toLowerCase())) {
            source2 = source.getSize() > 0 ? Utils.formatSize(this._context, source.getSize()) : BaseProvider.UNKNOWN_QUALITY;
            j = source.getSize();
        }
        for (ListGroup listGroup : this.groups) {
            if (listGroup.title.equals(source2)) {
                listGroup.sources.add(source);
                return;
            }
        }
        ListGroup listGroup2 = new ListGroup();
        listGroup2.title = source2;
        listGroup2.title_value = j;
        listGroup2.sources = new ArrayList();
        listGroup2.sources.add(source);
        this.groups.add(listGroup2);
    }

    public void setSources(List<Source> list) {
        for (ListGroup listGroup : this.groups) {
            if (listGroup.sources != null) {
                listGroup.sources.clear();
            }
        }
        for (Source addSource : list) {
            addSource(addSource);
        }
        list = new ArrayList();
        for (ListGroup listGroup2 : this.groups) {
            if (!(listGroup2.sources == null || listGroup2.sources.isEmpty())) {
                list.add(listGroup2);
            }
        }
        this.groups = list;
        Collections.sort(this.groups, new C05651());
        notifyDataSetChanged();
    }

    public int getCurrentGroupSelection() {
        return this.currentGroupSelection;
    }

    public int getCurrentChildSelection() {
        return this.currentChildSelection;
    }

    public void setCurrentSelection(int i, int i2) {
        this.currentChildSelection = i;
        this.currentGroupSelection = i2;
    }

    public Source getChild(int i, int i2) {
        return (Source) ((ListGroup) this.groups.get(i)).sources.get(i2);
    }

    public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(R.layout.source_list_item, null);
        }
        i = getChild(i, i2);
        ((TextView) view.findViewById(R.id.provider)).setText(i.getProvider());
        TextView textView = (TextView) view.findViewById(R.id.extrainfo);
        z = Utils.getColorForMs(i.getResolve_time());
        int i3 = 2;
        textView.setText(Html.fromHtml(String.format("<font color=\"%s\">%s</font>", new Object[]{z, i.getExtra_info()})));
        ((TextView) view.findViewById(R.id.source)).setText(i.getSource());
        ((TextView) view.findViewById(R.id.filename)).setText(i.getFilename());
        ImageView imageView = (ImageView) view.findViewById(R.id.quality);
        i = i.getQuality();
        viewGroup = i.hashCode();
        if (viewGroup != 2300) {
            if (viewGroup != 2641) {
                if (viewGroup != 46737913) {
                    if (viewGroup == 433141802) {
                        if (i.equals(BaseProvider.UNKNOWN_QUALITY) != 0) {
                            i3 = 0;
                            switch (i3) {
                                case 0:
                                    imageView.setImageResource(R.drawable.ukn_quality);
                                    break;
                                case 1:
                                    imageView.setImageResource(R.drawable.dvd_quality);
                                    break;
                                case 2:
                                    imageView.setImageResource(R.drawable.hd_quality);
                                    break;
                                case 3:
                                    imageView.setImageResource(R.drawable.fullhd_quality);
                                    break;
                                default:
                                    imageView.setImageResource(0);
                                    break;
                            }
                            return view;
                        }
                    }
                } else if (i.equals(BaseProvider.FULLHD) != 0) {
                    i3 = 3;
                    switch (i3) {
                        case 0:
                            imageView.setImageResource(R.drawable.ukn_quality);
                            break;
                        case 1:
                            imageView.setImageResource(R.drawable.dvd_quality);
                            break;
                        case 2:
                            imageView.setImageResource(R.drawable.hd_quality);
                            break;
                        case 3:
                            imageView.setImageResource(R.drawable.fullhd_quality);
                            break;
                        default:
                            imageView.setImageResource(0);
                            break;
                    }
                    return view;
                }
            } else if (i.equals(BaseProvider.SD) != 0) {
                i3 = 1;
                switch (i3) {
                    case 0:
                        imageView.setImageResource(R.drawable.ukn_quality);
                        break;
                    case 1:
                        imageView.setImageResource(R.drawable.dvd_quality);
                        break;
                    case 2:
                        imageView.setImageResource(R.drawable.hd_quality);
                        break;
                    case 3:
                        imageView.setImageResource(R.drawable.fullhd_quality);
                        break;
                    default:
                        imageView.setImageResource(0);
                        break;
                }
                return view;
            }
        } else if (i.equals(BaseProvider.HD) != 0) {
            switch (i3) {
                case 0:
                    imageView.setImageResource(R.drawable.ukn_quality);
                    break;
                case 1:
                    imageView.setImageResource(R.drawable.dvd_quality);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.hd_quality);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.fullhd_quality);
                    break;
                default:
                    imageView.setImageResource(0);
                    break;
            }
            return view;
        }
        i3 = -1;
        switch (i3) {
            case 0:
                imageView.setImageResource(R.drawable.ukn_quality);
                break;
            case 1:
                imageView.setImageResource(R.drawable.dvd_quality);
                break;
            case 2:
                imageView.setImageResource(R.drawable.hd_quality);
                break;
            case 3:
                imageView.setImageResource(R.drawable.fullhd_quality);
                break;
            default:
                imageView.setImageResource(0);
                break;
        }
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
        textView2.setText(Html.fromHtml(getCounterString(i.sources)));
        textView2.setTextColor(InputDeviceCompat.SOURCE_ANY);
        return view;
    }

    public static String getCounterString(List<Source> list) {
        long j = 0;
        long j2 = j;
        long j3 = j2;
        for (Source source : list) {
            if (source.getResolve_time() > 0) {
                long j4 = j2 + 1;
                j2 = j3 + source.getResolve_time();
                j = j2 / j4;
                j3 = j2;
                j2 = j4;
            }
        }
        if (j > 0) {
            String colorForMs = Utils.getColorForMs(j);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("(<font color=\"");
            stringBuilder.append(colorForMs);
            stringBuilder.append("\">~%dms</font>)&nbsp;&nbsp;<b>%d</b>");
            return String.format(stringBuilder.toString(), new Object[]{Long.valueOf(j), Integer.valueOf(list.size())});
        }
        return String.format("<b>%d</b>", new Object[]{Integer.valueOf(list.size())});
    }
}
