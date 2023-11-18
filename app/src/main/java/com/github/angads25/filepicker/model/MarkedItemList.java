package com.github.angads25.filepicker.model;

import java.util.HashMap;
import java.util.Set;

public class MarkedItemList {
    private static HashMap<String, FileListItem> ourInstance = new HashMap();

    public static void addSelectedItem(FileListItem fileListItem) {
        ourInstance.put(fileListItem.getLocation(), fileListItem);
    }

    public static void removeSelectedItem(String str) {
        ourInstance.remove(str);
    }

    public static boolean hasItem(String str) {
        return ourInstance.containsKey(str);
    }

    public static void clearSelectionList() {
        ourInstance = new HashMap();
    }

    public static void addSingleFile(FileListItem fileListItem) {
        ourInstance = new HashMap();
        ourInstance.put(fileListItem.getLocation(), fileListItem);
    }

    public static String[] getSelectedPaths() {
        Set<String> keySet = ourInstance.keySet();
        String[] strArr = new String[keySet.size()];
        int i = 0;
        for (String str : keySet) {
            int i2 = i + 1;
            strArr[i] = str;
            i = i2;
        }
        return strArr;
    }

    public static int getFileCount() {
        return ourInstance.size();
    }
}
