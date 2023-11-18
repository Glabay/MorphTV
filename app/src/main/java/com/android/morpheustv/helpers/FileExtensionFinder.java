package com.android.morpheustv.helpers;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class FileExtensionFinder implements FileFilter {
    private final String fileName;
    private final List<File> foundFiles = new ArrayList();

    public FileExtensionFinder(String str) {
        this.fileName = str;
    }

    public boolean accept(File file) {
        if (!file.isDirectory()) {
            if (file.getName().toLowerCase().endsWith(this.fileName) == null) {
                return null;
            }
        }
        return true;
    }

    public List<File> findFiles(File... fileArr) {
        for (File file : fileArr) {
            if (file.isDirectory()) {
                findFiles(file.listFiles(this));
            } else if (file.getName().toLowerCase().endsWith(this.fileName)) {
                this.foundFiles.add(file);
            }
        }
        return this.foundFiles;
    }
}
