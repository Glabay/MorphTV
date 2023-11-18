package net.lingala.zip4j.unzip;

import java.io.File;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.util.Zip4jUtil;

public class UnzipUtil {
    public static void applyFileAttributes(FileHeader fileHeader, File file) throws ZipException {
        applyFileAttributes(fileHeader, file, null);
    }

    public static void applyFileAttributes(FileHeader fileHeader, File file, UnzipParameters unzipParameters) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("cannot set file properties: file header is null");
        } else if (file == null) {
            throw new ZipException("cannot set file properties: output file is null");
        } else if (Zip4jUtil.checkFileExists(file)) {
            if (unzipParameters == null || !unzipParameters.isIgnoreDateTimeAttributes()) {
                setFileLastModifiedTime(fileHeader, file);
            }
            if (unzipParameters == null) {
                setFileAttributes(fileHeader, file, true, true, true, true);
            } else if (unzipParameters.isIgnoreAllFileAttributes()) {
                setFileAttributes(fileHeader, file, false, false, false, false);
            } else {
                setFileAttributes(fileHeader, file, unzipParameters.isIgnoreReadOnlyFileAttribute() ^ 1, unzipParameters.isIgnoreHiddenFileAttribute() ^ 1, unzipParameters.isIgnoreArchiveFileAttribute() ^ 1, unzipParameters.isIgnoreSystemFileAttribute() ^ 1);
            }
        } else {
            throw new ZipException("cannot set file properties: file doesnot exist");
        }
    }

    private static void setFileAttributes(FileHeader fileHeader, File file, boolean z, boolean z2, boolean z3, boolean z4) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("invalid file header. cannot set file attributes");
        }
        fileHeader = fileHeader.getExternalFileAttr();
        if (fileHeader != null) {
            fileHeader = fileHeader[0];
            if (fileHeader != 18) {
                if (fileHeader != 38) {
                    if (fileHeader != true) {
                        if (fileHeader != true) {
                            switch (fileHeader) {
                                case 1:
                                    if (z) {
                                        Zip4jUtil.setFileReadOnly(file);
                                        break;
                                    }
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    if (z) {
                                        Zip4jUtil.setFileReadOnly(file);
                                    }
                                    if (z2) {
                                        Zip4jUtil.setFileHidden(file);
                                        break;
                                    }
                                    break;
                                default:
                                    switch (fileHeader) {
                                        case 32:
                                            break;
                                        case 33:
                                            if (z3) {
                                                Zip4jUtil.setFileArchive(file);
                                            }
                                            if (z) {
                                                Zip4jUtil.setFileReadOnly(file);
                                                break;
                                            }
                                            break;
                                        case 34:
                                            break;
                                        case 35:
                                            if (z3) {
                                                Zip4jUtil.setFileArchive(file);
                                            }
                                            if (z) {
                                                Zip4jUtil.setFileReadOnly(file);
                                            }
                                            if (z2) {
                                                Zip4jUtil.setFileHidden(file);
                                                break;
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                            }
                        }
                        if (z3) {
                            Zip4jUtil.setFileArchive(file);
                        }
                        if (z2) {
                            Zip4jUtil.setFileHidden(file);
                        }
                    }
                    if (z3) {
                        Zip4jUtil.setFileArchive(file);
                    }
                } else {
                    if (z) {
                        Zip4jUtil.setFileReadOnly(file);
                    }
                    if (z2) {
                        Zip4jUtil.setFileHidden(file);
                    }
                    if (z4) {
                        Zip4jUtil.setFileSystemMode(file);
                    }
                }
            }
            if (z2) {
                Zip4jUtil.setFileHidden(file);
            }
        }
    }

    private static void setFileLastModifiedTime(FileHeader fileHeader, File file) throws ZipException {
        if (fileHeader.getLastModFileTime() > 0 && file.exists()) {
            file.setLastModified(Zip4jUtil.dosToJavaTme(fileHeader.getLastModFileTime()));
        }
    }
}
