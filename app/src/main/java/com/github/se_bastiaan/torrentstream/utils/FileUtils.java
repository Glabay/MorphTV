package com.github.se_bastiaan.torrentstream.utils;

import java.io.File;

public final class FileUtils {
    private FileUtils() throws InstantiationException {
        throw new InstantiationException("This class is not created for instantiation");
    }

    public static boolean recursiveDelete(File file) {
        if (file.isDirectory()) {
            String[] list = file.list();
            int i = 0;
            if (list == null) {
                return false;
            }
            int length = list.length;
            while (i < length) {
                recursiveDelete(new File(file, list[i]));
                i++;
            }
        }
        return file.delete();
    }
}
