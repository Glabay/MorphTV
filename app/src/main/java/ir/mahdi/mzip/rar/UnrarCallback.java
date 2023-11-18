package ir.mahdi.mzip.rar;

public interface UnrarCallback {
    boolean isNextVolumeReady(Volume volume);

    void volumeProgressChanged(long j, long j2);
}
