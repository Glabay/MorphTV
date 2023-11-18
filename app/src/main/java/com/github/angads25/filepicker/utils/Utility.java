package com.github.angads25.filepicker.utils;

import android.content.Context;
import android.os.Build.VERSION;
import com.github.angads25.filepicker.model.FileListItem;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class Utility {
    public static boolean checkStorageAccessPermissions(Context context) {
        boolean z = true;
        if (VERSION.SDK_INT < 23) {
            return true;
        }
        if (context.checkCallingOrSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != null) {
            z = false;
        }
        return z;
    }

    public static ArrayList<FileListItem> prepareFileListEntries(ArrayList<FileListItem> arrayList, File file, ExtensionFilter extensionFilter) {
        try {
            for (File file2 : file.listFiles(extensionFilter)) {
                if (file2.canRead()) {
                    FileListItem fileListItem = new FileListItem();
                    fileListItem.setFilename(file2.getName());
                    fileListItem.setDirectory(file2.isDirectory());
                    fileListItem.setLocation(file2.getAbsolutePath());
                    fileListItem.setTime(file2.lastModified());
                    arrayList.add(fileListItem);
                }
            }
            Collections.sort(arrayList);
            return arrayList;
        } catch (ArrayList<FileListItem> arrayList2) {
            arrayList2.printStackTrace();
            return new ArrayList();
        }
    }

    private boolean hasSupportLibraryInClasspath() {
        try {
            Class.forName("com.android.support:appcompat-v7");
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
