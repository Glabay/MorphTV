package com.github.angads25.filepicker.model;

import java.io.File;

public class DialogProperties {
    public File error_dir = new File(DialogConfigs.DEFAULT_DIR);
    public String[] extensions = null;
    public File offset = new File(DialogConfigs.DEFAULT_DIR);
    public File root = new File(DialogConfigs.DEFAULT_DIR);
    public int selection_mode = 0;
    public int selection_type = 0;
}
