package ir.mahdi.mzip.rar;

import java.io.IOException;

public interface VolumeManager {
    Volume nextArchive(Archive archive, Volume volume) throws IOException;
}
