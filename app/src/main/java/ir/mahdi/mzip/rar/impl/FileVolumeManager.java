package ir.mahdi.mzip.rar.impl;

import ir.mahdi.mzip.rar.Archive;
import ir.mahdi.mzip.rar.Volume;
import ir.mahdi.mzip.rar.VolumeManager;
import ir.mahdi.mzip.rar.util.VolumeHelper;
import java.io.File;
import java.io.IOException;

public class FileVolumeManager implements VolumeManager {
    private final File firstVolume;

    public FileVolumeManager(File file) {
        this.firstVolume = file;
    }

    public Volume nextArchive(Archive archive, Volume volume) throws IOException {
        if (volume == null) {
            return new FileVolume(archive, this.firstVolume);
        }
        boolean z;
        FileVolume fileVolume = (FileVolume) volume;
        if (archive.getMainHeader().isNewNumbering()) {
            if (!archive.isOldFormat()) {
                z = false;
                return new FileVolume(archive, new File(VolumeHelper.nextVolumeName(fileVolume.getFile().getAbsolutePath(), z)));
            }
        }
        z = true;
        return new FileVolume(archive, new File(VolumeHelper.nextVolumeName(fileVolume.getFile().getAbsolutePath(), z)));
    }
}
