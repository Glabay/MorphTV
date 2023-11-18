package com.github.angads25.filepicker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.preference.Preference;
import android.preference.Preference.BaseSavedState;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.AttributeSet;
import android.view.View;
import com.github.angads25.filepicker.C0619R;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogProperties;
import java.io.File;

public class FilePickerPreference extends Preference implements DialogSelectionListener, OnPreferenceClickListener {
    private FilePickerDialog mDialog;
    private DialogProperties properties = new DialogProperties();
    private String titleText = null;

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new C06241();
        Bundle dialogBundle;

        /* renamed from: com.github.angads25.filepicker.view.FilePickerPreference$SavedState$1 */
        static class C06241 implements Creator<SavedState> {
            C06241() {
            }

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        }

        public SavedState(Parcel parcel) {
            super(parcel);
            this.dialogBundle = parcel.readBundle(getClass().getClassLoader());
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeBundle(this.dialogBundle);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }

    public FilePickerPreference(Context context) {
        super(context);
        setOnPreferenceClickListener(this);
    }

    public FilePickerPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initProperties(attributeSet);
        setOnPreferenceClickListener(this);
    }

    public FilePickerPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initProperties(attributeSet);
        setOnPreferenceClickListener(this);
    }

    protected Object onGetDefaultValue(TypedArray typedArray, int i) {
        return super.onGetDefaultValue(typedArray, i);
    }

    protected void onSetInitialValue(boolean z, Object obj) {
        super.onSetInitialValue(z, obj);
    }

    protected void onBindView(View view) {
        super.onBindView(view);
    }

    protected Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        if (this.mDialog != null) {
            if (this.mDialog.isShowing()) {
                Parcelable savedState = new SavedState(onSaveInstanceState);
                savedState.dialogBundle = this.mDialog.onSaveInstanceState();
                return savedState;
            }
        }
        return onSaveInstanceState;
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable != null) {
            if (parcelable instanceof SavedState) {
                SavedState savedState = (SavedState) parcelable;
                super.onRestoreInstanceState(savedState.getSuperState());
                showDialog(savedState.dialogBundle);
                return;
            }
        }
        super.onRestoreInstanceState(parcelable);
    }

    private void showDialog(Bundle bundle) {
        this.mDialog = new FilePickerDialog(getContext());
        setProperties(this.properties);
        this.mDialog.setDialogSelectionListener(this);
        if (bundle != null) {
            this.mDialog.onRestoreInstanceState(bundle);
        }
        this.mDialog.setTitle(this.titleText);
        this.mDialog.show();
    }

    public void onSelectedFilePaths(String[] strArr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String append : strArr) {
            stringBuilder.append(append);
            stringBuilder.append(":");
        }
        strArr = stringBuilder.toString();
        if (isPersistent()) {
            persistString(strArr);
        }
        try {
            getOnPreferenceChangeListener().onPreferenceChange(this, strArr);
        } catch (String[] strArr2) {
            strArr2.printStackTrace();
        }
    }

    public boolean onPreferenceClick(Preference preference) {
        showDialog(null);
        return null;
    }

    public void setProperties(DialogProperties dialogProperties) {
        this.mDialog.setProperties(dialogProperties);
    }

    private void initProperties(AttributeSet attributeSet) {
        attributeSet = getContext().getTheme().obtainStyledAttributes(attributeSet, C0619R.styleable.FilePickerPreference, 0, 0);
        int indexCount = attributeSet.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = attributeSet.getIndex(i);
            if (index == C0619R.styleable.FilePickerPreference_selection_mode) {
                this.properties.selection_mode = attributeSet.getInteger(C0619R.styleable.FilePickerPreference_selection_mode, 0);
            } else if (index == C0619R.styleable.FilePickerPreference_selection_type) {
                this.properties.selection_type = attributeSet.getInteger(C0619R.styleable.FilePickerPreference_selection_type, 0);
            } else if (index == C0619R.styleable.FilePickerPreference_root_dir) {
                r3 = attributeSet.getString(C0619R.styleable.FilePickerPreference_root_dir);
                if (!(r3 == null || r3.equals(""))) {
                    this.properties.root = new File(r3);
                }
            } else if (index == C0619R.styleable.FilePickerPreference_error_dir) {
                r3 = attributeSet.getString(C0619R.styleable.FilePickerPreference_error_dir);
                if (!(r3 == null || r3.equals(""))) {
                    this.properties.error_dir = new File(r3);
                }
            } else if (index == C0619R.styleable.FilePickerPreference_offset_dir) {
                r3 = attributeSet.getString(C0619R.styleable.FilePickerPreference_offset_dir);
                if (!(r3 == null || r3.equals(""))) {
                    this.properties.offset = new File(r3);
                }
            } else if (index == C0619R.styleable.FilePickerPreference_extensions) {
                r3 = attributeSet.getString(C0619R.styleable.FilePickerPreference_extensions);
                if (!(r3 == null || r3.equals(""))) {
                    this.properties.extensions = r3.split(":");
                }
            } else if (index == C0619R.styleable.FilePickerPreference_title_text) {
                this.titleText = attributeSet.getString(C0619R.styleable.FilePickerPreference_title_text);
            }
        }
        attributeSet.recycle();
    }
}
