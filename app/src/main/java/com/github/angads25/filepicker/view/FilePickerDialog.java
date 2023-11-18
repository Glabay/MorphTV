package com.github.angads25.filepicker.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.angads25.filepicker.C0619R;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.controller.NotifyItemChecked;
import com.github.angads25.filepicker.controller.adapters.FileListAdapter;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.model.FileListItem;
import com.github.angads25.filepicker.model.MarkedItemList;
import com.github.angads25.filepicker.utils.ExtensionFilter;
import com.github.angads25.filepicker.utils.Utility;
import com.github.angads25.filepicker.widget.MaterialCheckbox;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilePickerDialog extends Dialog implements OnItemClickListener, OnItemLongClickListener {
    public static final int EXTERNAL_READ_PERMISSION_GRANT = 112;
    private DialogSelectionListener callbacks;
    private Context context;
    private TextView dir_path;
    private TextView dname;
    private ExtensionFilter filter;
    private ArrayList<FileListItem> internalList;
    private ListView listView;
    private FileListAdapter mFileListAdapter;
    private String negativeBtnNameStr = null;
    private String positiveBtnNameStr = null;
    private DialogProperties properties;
    private Button select;
    private TextView title;
    private String titleStr = null;

    /* renamed from: com.github.angads25.filepicker.view.FilePickerDialog$1 */
    class C06211 implements OnClickListener {
        C06211() {
        }

        public void onClick(View view) {
            view = MarkedItemList.getSelectedPaths();
            if (FilePickerDialog.this.callbacks != null) {
                FilePickerDialog.this.callbacks.onSelectedFilePaths(view);
            }
            FilePickerDialog.this.dismiss();
        }
    }

    /* renamed from: com.github.angads25.filepicker.view.FilePickerDialog$2 */
    class C06222 implements OnClickListener {
        C06222() {
        }

        public void onClick(View view) {
            FilePickerDialog.this.cancel();
        }
    }

    /* renamed from: com.github.angads25.filepicker.view.FilePickerDialog$3 */
    class C06233 implements NotifyItemChecked {
        C06233() {
        }

        public void notifyCheckBoxIsClicked() {
            FilePickerDialog.this.positiveBtnNameStr = FilePickerDialog.this.positiveBtnNameStr == null ? FilePickerDialog.this.context.getResources().getString(C0619R.string.choose_button_label) : FilePickerDialog.this.positiveBtnNameStr;
            int fileCount = MarkedItemList.getFileCount();
            if (fileCount == 0) {
                FilePickerDialog.this.select.setEnabled(false);
                if (VERSION.SDK_INT >= 23) {
                    fileCount = FilePickerDialog.this.context.getResources().getColor(C0619R.color.colorAccent, FilePickerDialog.this.context.getTheme());
                } else {
                    fileCount = FilePickerDialog.this.context.getResources().getColor(C0619R.color.colorAccent);
                }
                FilePickerDialog.this.select.setTextColor(Color.argb(128, Color.red(fileCount), Color.green(fileCount), Color.blue(fileCount)));
                FilePickerDialog.this.select.setText(FilePickerDialog.this.positiveBtnNameStr);
            } else {
                int color;
                FilePickerDialog.this.select.setEnabled(true);
                if (VERSION.SDK_INT >= 23) {
                    color = FilePickerDialog.this.context.getResources().getColor(C0619R.color.colorAccent, FilePickerDialog.this.context.getTheme());
                } else {
                    color = FilePickerDialog.this.context.getResources().getColor(C0619R.color.colorAccent);
                }
                FilePickerDialog.this.select.setTextColor(color);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(FilePickerDialog.this.positiveBtnNameStr);
                stringBuilder.append(" (");
                stringBuilder.append(fileCount);
                stringBuilder.append(") ");
                CharSequence stringBuilder2 = stringBuilder.toString();
                if (FilePickerDialog.this.properties.selection_mode == 0) {
                    stringBuilder2 = FilePickerDialog.this.positiveBtnNameStr;
                }
                FilePickerDialog.this.select.setText(stringBuilder2);
            }
            if (FilePickerDialog.this.properties.selection_mode == 0) {
                FilePickerDialog.this.mFileListAdapter.notifyDataSetChanged();
            }
        }
    }

    public FilePickerDialog(Context context) {
        super(context);
        this.context = context;
        this.properties = new DialogProperties();
        this.filter = new ExtensionFilter(this.properties);
        this.internalList = new ArrayList();
    }

    public FilePickerDialog(Context context, DialogProperties dialogProperties) {
        super(context);
        this.context = context;
        this.properties = dialogProperties;
        this.filter = new ExtensionFilter(dialogProperties);
        this.internalList = new ArrayList();
    }

    public FilePickerDialog(Context context, DialogProperties dialogProperties, int i) {
        super(context, i);
        this.context = context;
        this.properties = dialogProperties;
        this.filter = new ExtensionFilter(dialogProperties);
        this.internalList = new ArrayList();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(C0619R.layout.dialog_main);
        this.listView = (ListView) findViewById(C0619R.id.fileList);
        this.select = (Button) findViewById(C0619R.id.select);
        if (MarkedItemList.getFileCount() == null) {
            this.select.setEnabled(false);
            if (VERSION.SDK_INT >= 23) {
                bundle = this.context.getResources().getColor(C0619R.color.colorAccent, this.context.getTheme());
            } else {
                bundle = this.context.getResources().getColor(C0619R.color.colorAccent);
            }
            this.select.setTextColor(Color.argb(128, Color.red(bundle), Color.green(bundle), Color.blue(bundle)));
        }
        this.dname = (TextView) findViewById(C0619R.id.dname);
        this.title = (TextView) findViewById(C0619R.id.title);
        this.dir_path = (TextView) findViewById(C0619R.id.dir_path);
        Button button = (Button) findViewById(C0619R.id.cancel);
        if (this.negativeBtnNameStr != null) {
            button.setText(this.negativeBtnNameStr);
        }
        this.select.setOnClickListener(new C06211());
        button.setOnClickListener(new C06222());
        this.mFileListAdapter = new FileListAdapter(this.internalList, this.context, this.properties);
        this.mFileListAdapter.setNotifyItemCheckedListener(new C06233());
        this.listView.setAdapter(this.mFileListAdapter);
        setTitle();
    }

    private void setTitle() {
        if (this.title != null) {
            if (this.dname != null) {
                if (this.titleStr != null) {
                    if (this.title.getVisibility() == 4) {
                        this.title.setVisibility(0);
                    }
                    this.title.setText(this.titleStr);
                    if (this.dname.getVisibility() == 0) {
                        this.dname.setVisibility(4);
                    }
                } else {
                    if (this.title.getVisibility() == 0) {
                        this.title.setVisibility(4);
                    }
                    if (this.dname.getVisibility() == 4) {
                        this.dname.setVisibility(0);
                    }
                }
            }
        }
    }

    protected void onStart() {
        super.onStart();
        this.positiveBtnNameStr = this.positiveBtnNameStr == null ? this.context.getResources().getString(C0619R.string.choose_button_label) : this.positiveBtnNameStr;
        this.select.setText(this.positiveBtnNameStr);
        if (Utility.checkStorageAccessPermissions(this.context)) {
            File file;
            this.internalList.clear();
            if (this.properties.offset.isDirectory() && validateOffsetPath()) {
                file = new File(this.properties.offset.getAbsolutePath());
                FileListItem fileListItem = new FileListItem();
                fileListItem.setFilename(this.context.getString(C0619R.string.label_parent_dir));
                fileListItem.setDirectory(true);
                fileListItem.setLocation(file.getParentFile().getAbsolutePath());
                fileListItem.setTime(file.lastModified());
                this.internalList.add(fileListItem);
            } else if (this.properties.root.exists() && this.properties.root.isDirectory()) {
                file = new File(this.properties.root.getAbsolutePath());
            } else {
                file = new File(this.properties.error_dir.getAbsolutePath());
            }
            this.dname.setText(file.getName());
            this.dir_path.setText(file.getAbsolutePath());
            setTitle();
            this.internalList = Utility.prepareFileListEntries(this.internalList, file, this.filter);
            this.mFileListAdapter.notifyDataSetChanged();
            this.listView.setOnItemClickListener(this);
            this.listView.setOnItemLongClickListener(this);
        }
    }

    private boolean validateOffsetPath() {
        String absolutePath = this.properties.offset.getAbsolutePath();
        CharSequence absolutePath2 = this.properties.root.getAbsolutePath();
        return !absolutePath.equals(absolutePath2) && absolutePath.contains(absolutePath2);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (this.internalList.size() > i) {
            FileListItem fileListItem = (FileListItem) this.internalList.get(i);
            if (fileListItem.isDirectory() == 0) {
                ((MaterialCheckbox) view.findViewById(C0619R.id.file_mark)).performClick();
            } else if (new File(fileListItem.getLocation()).canRead() != null) {
                view = new File(fileListItem.getLocation());
                this.dname.setText(view.getName());
                setTitle();
                this.dir_path.setText(view.getAbsolutePath());
                this.internalList.clear();
                if (view.getName().equals(this.properties.root.getName()) == null) {
                    adapterView = new FileListItem();
                    adapterView.setFilename(this.context.getString(C0619R.string.label_parent_dir));
                    adapterView.setDirectory(1);
                    adapterView.setLocation(view.getParentFile().getAbsolutePath());
                    adapterView.setTime(view.lastModified());
                    this.internalList.add(adapterView);
                }
                this.internalList = Utility.prepareFileListEntries(this.internalList, view, this.filter);
                this.mFileListAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this.context, C0619R.string.error_dir_access, 0).show();
            }
        }
    }

    public DialogProperties getProperties() {
        return this.properties;
    }

    public void setProperties(DialogProperties dialogProperties) {
        this.properties = dialogProperties;
        this.filter = new ExtensionFilter(dialogProperties);
    }

    public void setDialogSelectionListener(DialogSelectionListener dialogSelectionListener) {
        this.callbacks = dialogSelectionListener;
    }

    public void setTitle(CharSequence charSequence) {
        if (charSequence != null) {
            this.titleStr = charSequence.toString();
        } else {
            this.titleStr = null;
        }
        setTitle();
    }

    public void setPositiveBtnName(CharSequence charSequence) {
        if (charSequence != null) {
            this.positiveBtnNameStr = charSequence.toString();
        } else {
            this.positiveBtnNameStr = null;
        }
    }

    public void setNegativeBtnName(CharSequence charSequence) {
        if (charSequence != null) {
            this.negativeBtnNameStr = charSequence.toString();
        } else {
            this.negativeBtnNameStr = null;
        }
    }

    public void markFiles(List<String> list) {
        if (list != null && list.size() > 0) {
            if (this.properties.selection_mode == 0) {
                File file = new File((String) list.get(0));
                switch (this.properties.selection_type) {
                    case null:
                        if (file.exists() != null && file.isFile() != null) {
                            list = new FileListItem();
                            list.setFilename(file.getName());
                            list.setDirectory(file.isDirectory());
                            list.setMarked(true);
                            list.setTime(file.lastModified());
                            list.setLocation(file.getAbsolutePath());
                            MarkedItemList.addSelectedItem(list);
                            return;
                        }
                        return;
                    case 1:
                        if (file.exists() != null && file.isDirectory() != null) {
                            list = new FileListItem();
                            list.setFilename(file.getName());
                            list.setDirectory(file.isDirectory());
                            list.setMarked(true);
                            list.setTime(file.lastModified());
                            list.setLocation(file.getAbsolutePath());
                            MarkedItemList.addSelectedItem(list);
                            return;
                        }
                        return;
                    case 2:
                        if (file.exists() != null) {
                            list = new FileListItem();
                            list.setFilename(file.getName());
                            list.setDirectory(file.isDirectory());
                            list.setMarked(true);
                            list.setTime(file.lastModified());
                            list.setLocation(file.getAbsolutePath());
                            MarkedItemList.addSelectedItem(list);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
            for (String str : list) {
                File file2;
                FileListItem fileListItem;
                switch (this.properties.selection_type) {
                    case 0:
                        file2 = new File(str);
                        if (file2.exists() && file2.isFile()) {
                            fileListItem = new FileListItem();
                            fileListItem.setFilename(file2.getName());
                            fileListItem.setDirectory(file2.isDirectory());
                            fileListItem.setMarked(true);
                            fileListItem.setTime(file2.lastModified());
                            fileListItem.setLocation(file2.getAbsolutePath());
                            MarkedItemList.addSelectedItem(fileListItem);
                            break;
                        }
                    case 1:
                        file2 = new File(str);
                        if (file2.exists() && file2.isDirectory()) {
                            fileListItem = new FileListItem();
                            fileListItem.setFilename(file2.getName());
                            fileListItem.setDirectory(file2.isDirectory());
                            fileListItem.setMarked(true);
                            fileListItem.setTime(file2.lastModified());
                            fileListItem.setLocation(file2.getAbsolutePath());
                            MarkedItemList.addSelectedItem(fileListItem);
                            break;
                        }
                    case 2:
                        file2 = new File(str);
                        if (file2.exists() && (file2.isFile() || file2.isDirectory())) {
                            fileListItem = new FileListItem();
                            fileListItem.setFilename(file2.getName());
                            fileListItem.setDirectory(file2.isDirectory());
                            fileListItem.setMarked(true);
                            fileListItem.setTime(file2.lastModified());
                            fileListItem.setLocation(file2.getAbsolutePath());
                            MarkedItemList.addSelectedItem(fileListItem);
                            break;
                        }
                    default:
                        break;
                }
            }
        }
    }

    public void show() {
        if (Utility.checkStorageAccessPermissions(this.context)) {
            super.show();
            this.positiveBtnNameStr = this.positiveBtnNameStr == null ? this.context.getResources().getString(C0619R.string.choose_button_label) : this.positiveBtnNameStr;
            this.select.setText(this.positiveBtnNameStr);
            if (this.properties.selection_mode != 0) {
                int fileCount = MarkedItemList.getFileCount();
                if (fileCount == 0) {
                    this.select.setText(this.positiveBtnNameStr);
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.positiveBtnNameStr);
                stringBuilder.append(" (");
                stringBuilder.append(fileCount);
                stringBuilder.append(") ");
                this.select.setText(stringBuilder.toString());
            }
        } else if (VERSION.SDK_INT >= 23) {
            ((Activity) this.context).requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 112);
        }
    }

    public void onBackPressed() {
        String charSequence = this.dname.getText().toString();
        if (this.internalList.size() > 0) {
            File file = new File(((FileListItem) this.internalList.get(0)).getLocation());
            if (!charSequence.equals(this.properties.root.getName())) {
                if (file.canRead()) {
                    this.dname.setText(file.getName());
                    this.dir_path.setText(file.getAbsolutePath());
                    this.internalList.clear();
                    if (!file.getName().equals(this.properties.root.getName())) {
                        FileListItem fileListItem = new FileListItem();
                        fileListItem.setFilename(this.context.getString(C0619R.string.label_parent_dir));
                        fileListItem.setDirectory(true);
                        fileListItem.setLocation(file.getParentFile().getAbsolutePath());
                        fileListItem.setTime(file.lastModified());
                        this.internalList.add(fileListItem);
                    }
                    this.internalList = Utility.prepareFileListEntries(this.internalList, file, this.filter);
                    this.mFileListAdapter.notifyDataSetChanged();
                    setTitle();
                    return;
                }
            }
            super.onBackPressed();
            setTitle();
            return;
        }
        super.onBackPressed();
    }

    public void dismiss() {
        MarkedItemList.clearSelectionList();
        this.internalList.clear();
        super.dismiss();
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (this.internalList.size() <= i || ((FileListItem) this.internalList.get(i)).isDirectory() == null) {
            return null;
        }
        MaterialCheckbox materialCheckbox = (MaterialCheckbox) view.findViewById(C0619R.id.file_mark);
        materialCheckbox.performClick();
        if (materialCheckbox.isChecked() != null) {
            this.select.requestFocus();
        }
        return true;
    }
}
