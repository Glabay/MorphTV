package ir.mahdi.mzip.rar.unpack;

import com.google.common.base.Ascii;
import ir.mahdi.mzip.rar.Archive;
import ir.mahdi.mzip.rar.UnrarCallback;
import ir.mahdi.mzip.rar.Volume;
import ir.mahdi.mzip.rar.crc.RarCRC;
import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.exception.RarException.RarExceptionType;
import ir.mahdi.mzip.rar.io.ReadOnlyAccessInputStream;
import ir.mahdi.mzip.rar.rarfile.FileHeader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ComprDataIO {
    private final Archive archive;
    private long curPackRead;
    private long curPackWrite;
    private long curUnpRead;
    private long curUnpWrite;
    private char currentCommand;
    private int decryption;
    private int encryption;
    private InputStream inputStream;
    private int lastPercent;
    private boolean nextVolumeMissing;
    private OutputStream outputStream;
    private long packFileCRC;
    private boolean packVolume;
    private long packedCRC;
    private long processedArcSize;
    private boolean skipUnpCRC;
    private FileHeader subHead;
    private boolean testMode;
    private long totalArcSize;
    private long totalPackRead;
    private long unpArcSize;
    private long unpFileCRC;
    private long unpPackedSize;
    private boolean unpVolume;

    public ComprDataIO(Archive archive) {
        this.archive = archive;
    }

    public void init(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.unpPackedSize = 0;
        this.testMode = false;
        this.skipUnpCRC = false;
        this.packVolume = false;
        this.unpVolume = false;
        this.nextVolumeMissing = false;
        this.encryption = 0;
        this.decryption = 0;
        this.totalPackRead = 0;
        this.curUnpWrite = 0;
        this.curUnpRead = 0;
        this.curPackWrite = 0;
        this.curPackRead = 0;
        this.packedCRC = -1;
        this.unpFileCRC = -1;
        this.packFileCRC = -1;
        this.lastPercent = -1;
        this.subHead = null;
        this.currentCommand = '\u0000';
        this.totalArcSize = 0;
        this.processedArcSize = 0;
    }

    public void init(FileHeader fileHeader) throws IOException {
        long positionInFile = fileHeader.getPositionInFile() + ((long) fileHeader.getHeaderSize());
        this.unpPackedSize = fileHeader.getFullPackSize();
        this.inputStream = new ReadOnlyAccessInputStream(this.archive.getRof(), positionInFile, positionInFile + this.unpPackedSize);
        this.subHead = fileHeader;
        this.curUnpRead = 0;
        this.curPackWrite = 0;
        this.packedCRC = -1;
    }

    public int unpRead(byte[] bArr, int i, int i2) throws IOException, RarException {
        int i3 = 0;
        int i4 = 0;
        while (i2 > 0) {
            i4 = this.inputStream.read(bArr, i, ((long) i2) > this.unpPackedSize ? (int) this.unpPackedSize : i2);
            if (i4 >= 0) {
                if (this.subHead.isSplitAfter()) {
                    this.packedCRC = (long) RarCRC.checkCrc((int) this.packedCRC, bArr, i, i4);
                }
                long j = (long) i4;
                this.curUnpRead += j;
                i3 += i4;
                i += i4;
                i2 -= i4;
                this.unpPackedSize -= j;
                this.archive.bytesReadRead(i4);
                if (this.unpPackedSize != 0 || !this.subHead.isSplitAfter()) {
                    break;
                }
                Volume nextArchive = this.archive.getVolumeManager().nextArchive(this.archive, this.archive.getVolume());
                if (nextArchive == null) {
                    this.nextVolumeMissing = 1;
                    return -1;
                }
                FileHeader subHeader = getSubHeader();
                if (subHeader.getUnpVersion() < Ascii.DC4 || subHeader.getFileCRC() == -1 || getPackedCRC() == ((long) (subHeader.getFileCRC() ^ -1))) {
                    UnrarCallback unrarCallback = this.archive.getUnrarCallback();
                    if (unrarCallback != null && !unrarCallback.isNextVolumeReady(nextArchive)) {
                        return -1;
                    }
                    this.archive.setVolume(nextArchive);
                    FileHeader nextFileHeader = this.archive.nextFileHeader();
                    if (nextFileHeader == null) {
                        return -1;
                    }
                    init(nextFileHeader);
                } else {
                    throw new RarException(RarExceptionType.crcError);
                }
            }
            throw new EOFException();
        }
        if (i4 == -1) {
            i3 = i4;
        }
        return i3;
    }

    public void unpWrite(byte[] bArr, int i, int i2) throws IOException {
        if (!this.testMode) {
            this.outputStream.write(bArr, i, i2);
        }
        this.curUnpWrite += (long) i2;
        if (!this.skipUnpCRC) {
            if (this.archive.isOldFormat()) {
                this.unpFileCRC = (long) RarCRC.checkOldCrc((short) ((int) this.unpFileCRC), bArr, i2);
            } else {
                this.unpFileCRC = (long) RarCRC.checkCrc((int) this.unpFileCRC, bArr, i, i2);
            }
        }
    }

    public void setPackedSizeToRead(long j) {
        this.unpPackedSize = j;
    }

    public void setTestMode(boolean z) {
        this.testMode = z;
    }

    public void setSkipUnpCRC(boolean z) {
        this.skipUnpCRC = z;
    }

    public long getCurPackRead() {
        return this.curPackRead;
    }

    public void setCurPackRead(long j) {
        this.curPackRead = j;
    }

    public long getCurPackWrite() {
        return this.curPackWrite;
    }

    public void setCurPackWrite(long j) {
        this.curPackWrite = j;
    }

    public long getCurUnpRead() {
        return this.curUnpRead;
    }

    public void setCurUnpRead(long j) {
        this.curUnpRead = j;
    }

    public long getCurUnpWrite() {
        return this.curUnpWrite;
    }

    public void setCurUnpWrite(long j) {
        this.curUnpWrite = j;
    }

    public int getDecryption() {
        return this.decryption;
    }

    public void setDecryption(int i) {
        this.decryption = i;
    }

    public int getEncryption() {
        return this.encryption;
    }

    public void setEncryption(int i) {
        this.encryption = i;
    }

    public boolean isNextVolumeMissing() {
        return this.nextVolumeMissing;
    }

    public void setNextVolumeMissing(boolean z) {
        this.nextVolumeMissing = z;
    }

    public long getPackedCRC() {
        return this.packedCRC;
    }

    public void setPackedCRC(long j) {
        this.packedCRC = j;
    }

    public long getPackFileCRC() {
        return this.packFileCRC;
    }

    public void setPackFileCRC(long j) {
        this.packFileCRC = j;
    }

    public boolean isPackVolume() {
        return this.packVolume;
    }

    public void setPackVolume(boolean z) {
        this.packVolume = z;
    }

    public long getProcessedArcSize() {
        return this.processedArcSize;
    }

    public void setProcessedArcSize(long j) {
        this.processedArcSize = j;
    }

    public long getTotalArcSize() {
        return this.totalArcSize;
    }

    public void setTotalArcSize(long j) {
        this.totalArcSize = j;
    }

    public long getTotalPackRead() {
        return this.totalPackRead;
    }

    public void setTotalPackRead(long j) {
        this.totalPackRead = j;
    }

    public long getUnpArcSize() {
        return this.unpArcSize;
    }

    public void setUnpArcSize(long j) {
        this.unpArcSize = j;
    }

    public long getUnpFileCRC() {
        return this.unpFileCRC;
    }

    public void setUnpFileCRC(long j) {
        this.unpFileCRC = j;
    }

    public boolean isUnpVolume() {
        return this.unpVolume;
    }

    public void setUnpVolume(boolean z) {
        this.unpVolume = z;
    }

    public FileHeader getSubHeader() {
        return this.subHead;
    }

    public void setSubHeader(FileHeader fileHeader) {
        this.subHead = fileHeader;
    }
}
