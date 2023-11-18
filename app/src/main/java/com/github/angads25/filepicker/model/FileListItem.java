package com.github.angads25.filepicker.model;

import java.util.Locale;

public class FileListItem implements Comparable<FileListItem> {
    private boolean directory;
    private String filename;
    private String location;
    private boolean marked;
    private long time;

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String str) {
        this.filename = str;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String str) {
        this.location = str;
    }

    public boolean isDirectory() {
        return this.directory;
    }

    public void setDirectory(boolean z) {
        this.directory = z;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long j) {
        this.time = j;
    }

    public boolean isMarked() {
        return this.marked;
    }

    public void setMarked(boolean z) {
        this.marked = z;
    }

    public int compareTo(FileListItem fileListItem) {
        if (fileListItem.isDirectory() && isDirectory()) {
            return this.filename.toLowerCase().compareTo(fileListItem.getFilename().toLowerCase(Locale.getDefault()));
        }
        if (fileListItem.isDirectory() || isDirectory()) {
            return (fileListItem.isDirectory() == null || isDirectory() != null) ? -1 : 1;
        } else {
            return this.filename.toLowerCase().compareTo(fileListItem.getFilename().toLowerCase(Locale.getDefault()));
        }
    }
}
