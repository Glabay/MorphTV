package com.github.se_bastiaan.torrentstream.exceptions;

public class DirectoryModifyException extends Exception {
    public DirectoryModifyException() {
        super("Could not create or delete save directory");
    }
}
