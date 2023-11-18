package com.github.angads25.filepicker.controller.adapters;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.angads25.filepicker.C0619R;
import com.github.angads25.filepicker.controller.NotifyItemChecked;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.model.FileListItem;
import com.github.angads25.filepicker.model.MarkedItemList;
import com.github.angads25.filepicker.widget.MaterialCheckbox;
import com.github.angads25.filepicker.widget.OnCheckedChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FileListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FileListItem> listItem;
    private NotifyItemChecked notifyItemChecked;
    private DialogProperties properties;

    private class ViewHolder {
        MaterialCheckbox fmark;
        TextView name;
        TextView type;
        ImageView type_icon;

        ViewHolder(View view) {
            this.name = (TextView) view.findViewById(C0619R.id.fname);
            this.type = (TextView) view.findViewById(C0619R.id.ftype);
            this.type_icon = (ImageView) view.findViewById(C0619R.id.image_type);
            this.fmark = (MaterialCheckbox) view.findViewById(C0619R.id.file_mark);
        }
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public FileListAdapter(ArrayList<FileListItem> arrayList, Context context, DialogProperties dialogProperties) {
        this.listItem = arrayList;
        this.context = context;
        this.properties = dialogProperties;
    }

    public int getCount() {
        return this.listItem.size();
    }

    public FileListItem getItem(int i) {
        return (FileListItem) this.listItem.get(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(C0619R.layout.dialog_file_list_item, viewGroup, false);
            viewGroup = new ViewHolder(view);
            view.setTag(viewGroup);
        } else {
            viewGroup = (ViewHolder) view.getTag();
        }
        final FileListItem fileListItem = (FileListItem) this.listItem.get(i);
        if (MarkedItemList.hasItem(fileListItem.getLocation())) {
            view.setAnimation(AnimationUtils.loadAnimation(this.context, C0619R.anim.marked_item_animation));
        } else {
            view.setAnimation(AnimationUtils.loadAnimation(this.context, C0619R.anim.unmarked_item_animation));
        }
        if (fileListItem.isDirectory()) {
            viewGroup.type_icon.setImageResource(C0619R.mipmap.ic_type_folder);
            if (VERSION.SDK_INT >= 23) {
                viewGroup.type_icon.setColorFilter(this.context.getResources().getColor(C0619R.color.colorPrimary, this.context.getTheme()));
            } else {
                viewGroup.type_icon.setColorFilter(this.context.getResources().getColor(C0619R.color.colorPrimary));
            }
            if (this.properties.selection_type == 0) {
                viewGroup.fmark.setVisibility(4);
            } else {
                viewGroup.fmark.setVisibility(0);
            }
        } else {
            viewGroup.type_icon.setImageResource(C0619R.mipmap.ic_type_file);
            if (VERSION.SDK_INT >= 23) {
                viewGroup.type_icon.setColorFilter(this.context.getResources().getColor(C0619R.color.colorAccent, this.context.getTheme()));
            } else {
                viewGroup.type_icon.setColorFilter(this.context.getResources().getColor(C0619R.color.colorAccent));
            }
            if (this.properties.selection_type == 1) {
                viewGroup.fmark.setVisibility(4);
            } else {
                viewGroup.fmark.setVisibility(0);
            }
        }
        viewGroup.type_icon.setContentDescription(fileListItem.getFilename());
        viewGroup.name.setText(fileListItem.getFilename());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        Date date = new Date(fileListItem.getTime());
        if (i == 0 && fileListItem.getFilename().startsWith(this.context.getString(C0619R.string.label_parent_dir))) {
            viewGroup.type.setText(C0619R.string.label_parent_directory);
        } else {
            TextView textView = viewGroup.type;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.context.getString(C0619R.string.last_edit));
            stringBuilder.append(simpleDateFormat.format(date));
            stringBuilder.append(", ");
            stringBuilder.append(simpleDateFormat2.format(date));
            textView.setText(stringBuilder.toString());
        }
        if (viewGroup.fmark.getVisibility() == 0) {
            if (i == 0 && fileListItem.getFilename().startsWith(this.context.getString(C0619R.string.label_parent_dir)) != 0) {
                viewGroup.fmark.setVisibility(4);
            }
            if (MarkedItemList.hasItem(fileListItem.getLocation()) != 0) {
                viewGroup.fmark.setChecked(true);
            } else {
                viewGroup.fmark.setChecked(false);
            }
        }
        viewGroup.fmark.setOnCheckedChangedListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(MaterialCheckbox materialCheckbox, boolean z) {
                fileListItem.setMarked(z);
                if (fileListItem.isMarked() == null) {
                    MarkedItemList.removeSelectedItem(fileListItem.getLocation());
                } else if (FileListAdapter.this.properties.selection_mode == true) {
                    MarkedItemList.addSelectedItem(fileListItem);
                } else {
                    MarkedItemList.addSingleFile(fileListItem);
                }
                FileListAdapter.this.notifyItemChecked.notifyCheckBoxIsClicked();
            }
        });
        return view;
    }

    public void setNotifyItemCheckedListener(NotifyItemChecked notifyItemChecked) {
        this.notifyItemChecked = notifyItemChecked;
    }
}
