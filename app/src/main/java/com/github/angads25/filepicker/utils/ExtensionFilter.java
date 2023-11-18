package com.github.angads25.filepicker.utils;

import com.github.angads25.filepicker.model.DialogProperties;
import java.io.File;
import java.io.FileFilter;
import java.util.Locale;

public class ExtensionFilter implements FileFilter {
    private DialogProperties properties;
    private final String[] validExtensions;

    public ExtensionFilter(DialogProperties dialogProperties) {
        if (dialogProperties.extensions != null) {
            this.validExtensions = dialogProperties.extensions;
        } else {
            this.validExtensions = new String[]{""};
        }
        this.properties = dialogProperties;
    }

    public boolean accept(File file) {
        if (file.isDirectory() && file.canRead()) {
            return true;
        }
        if (this.properties.selection_type == 1) {
            return false;
        }
        file = file.getName().toLowerCase(Locale.getDefault());
        for (String endsWith : this.validExtensions) {
            if (file.endsWith(endsWith)) {
                return true;
            }
        }
        return false;
    }
}
