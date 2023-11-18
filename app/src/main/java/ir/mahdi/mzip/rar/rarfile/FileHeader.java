package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;
import java.util.Calendar;
import java.util.Date;

public class FileHeader extends BlockHeader {
    private static final byte NEWLHD_SIZE = (byte) 32;
    private static final byte SALT_SIZE = (byte) 8;
    private Date aTime;
    private Date arcTime;
    private Date cTime;
    private int fileAttr;
    private final int fileCRC;
    private String fileName;
    private final byte[] fileNameBytes;
    private String fileNameW;
    private final int fileTime;
    private long fullPackSize;
    private long fullUnpackSize;
    private int highPackSize;
    private int highUnpackSize;
    private final HostSystem hostOS;
    private Date mTime;
    private short nameSize;
    private int recoverySectors = -1;
    private final byte[] salt = new byte[8];
    private byte[] subData;
    private int subFlags;
    private byte unpMethod;
    private long unpSize;
    private byte unpVersion;

    public FileHeader(BlockHeader blockHeader, byte[] bArr) {
        short s;
        super(blockHeader);
        int i = 0;
        this.unpSize = Raw.readIntLittleEndianAsLong(bArr, 0);
        this.hostOS = HostSystem.findHostSystem(bArr[4]);
        this.fileCRC = Raw.readIntLittleEndian(bArr, 5);
        this.fileTime = Raw.readIntLittleEndian(bArr, 9);
        this.unpVersion = (byte) (this.unpVersion | (bArr[13] & 255));
        this.unpMethod = (byte) (this.unpMethod | (bArr[14] & 255));
        this.nameSize = Raw.readShortLittleEndian(bArr, 15);
        this.fileAttr = Raw.readIntLittleEndian(bArr, 17);
        int i2 = 21;
        if (isLargeBlock()) {
            this.highPackSize = Raw.readIntLittleEndian(bArr, 21);
            this.highUnpackSize = Raw.readIntLittleEndian(bArr, 25);
            i2 = 29;
        } else {
            this.highPackSize = 0;
            this.highUnpackSize = 0;
            if (this.unpSize == -1) {
                this.unpSize = -1;
                this.highUnpackSize = Integer.MAX_VALUE;
            }
        }
        this.fullPackSize |= (long) this.highPackSize;
        this.fullPackSize <<= 32;
        this.fullPackSize |= (long) getPackSize();
        this.fullUnpackSize |= (long) this.highUnpackSize;
        this.fullUnpackSize <<= 32;
        this.fullUnpackSize += this.unpSize;
        short s2 = this.nameSize;
        short s3 = BaseBlock.LHD_EXTTIME;
        if (s2 <= BaseBlock.LHD_EXTTIME) {
            s3 = this.nameSize;
        }
        this.nameSize = s3;
        this.fileNameBytes = new byte[this.nameSize];
        int i3 = i2;
        for (s = (short) 0; s < this.nameSize; s++) {
            this.fileNameBytes[s] = bArr[i3];
            i3++;
        }
        if (isFileHeader()) {
            if (isUnicode()) {
                this.fileName = "";
                this.fileNameW = "";
                s = (short) 0;
                while (s < this.fileNameBytes.length && this.fileNameBytes[s] != (byte) 0) {
                    s++;
                }
                Object obj = new byte[s];
                System.arraycopy(this.fileNameBytes, 0, obj, 0, obj.length);
                this.fileName = new String(obj);
                if (s != this.nameSize) {
                    this.fileNameW = FileNameDecoder.decode(this.fileNameBytes, s + 1);
                }
            } else {
                this.fileName = new String(this.fileNameBytes);
                this.fileNameW = "";
            }
        }
        if (UnrarHeadertype.NewSubHeader.equals(this.headerType)) {
            i2 = (this.headerSize - 32) - this.nameSize;
            if (hasSalt()) {
                i2 -= 8;
            }
            if (i2 > 0) {
                this.subData = new byte[i2];
                for (int i4 = 0; i4 < i2; i4++) {
                    this.subData[i4] = bArr[i3];
                    i3++;
                }
            }
            if (NewSubHeaderType.SUBHEAD_TYPE_RR.byteEquals(this.fileNameBytes)) {
                this.recoverySectors = ((this.subData[8] + (this.subData[9] << 8)) + (this.subData[10] << 16)) + (this.subData[11] << 24);
            }
        }
        if (hasSalt()) {
            while (i < 8) {
                this.salt[i] = bArr[i3];
                i3++;
                i++;
            }
        }
        this.mTime = getDateDos(this.fileTime);
    }

    public void print() {
        super.print();
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("unpSize: ");
        stringBuilder2.append(getUnpSize());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nHostOS: ");
        stringBuilder2.append(this.hostOS.name());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nMDate: ");
        stringBuilder2.append(this.mTime);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nFileName: ");
        stringBuilder2.append(getFileNameString());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nunpMethod: ");
        stringBuilder2.append(Integer.toHexString(getUnpMethod()));
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nunpVersion: ");
        stringBuilder2.append(Integer.toHexString(getUnpVersion()));
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nfullpackedsize: ");
        stringBuilder2.append(getFullPackSize());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nfullunpackedsize: ");
        stringBuilder2.append(getFullUnpackSize());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nisEncrypted: ");
        stringBuilder2.append(isEncrypted());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nisfileHeader: ");
        stringBuilder2.append(isFileHeader());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nisSolid: ");
        stringBuilder2.append(isSolid());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nisSplitafter: ");
        stringBuilder2.append(isSplitAfter());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nisSplitBefore:");
        stringBuilder2.append(isSplitBefore());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nunpSize: ");
        stringBuilder2.append(getUnpSize());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\ndataSize: ");
        stringBuilder2.append(getDataSize());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nisUnicode: ");
        stringBuilder2.append(isUnicode());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nhasVolumeNumber: ");
        stringBuilder2.append(hasVolumeNumber());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nhasArchiveDataCRC: ");
        stringBuilder2.append(hasArchiveDataCRC());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nhasSalt: ");
        stringBuilder2.append(hasSalt());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nhasEncryptVersions: ");
        stringBuilder2.append(hasEncryptVersion());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nisSubBlock: ");
        stringBuilder2.append(isSubBlock());
        stringBuilder.append(stringBuilder2.toString());
    }

    private Date getDateDos(int i) {
        Calendar instance = Calendar.getInstance();
        instance.set(1, (i >>> 25) + 1980);
        instance.set(2, ((i >>> 21) & 15) - 1);
        instance.set(5, (i >>> 16) & 31);
        instance.set(11, (i >>> 11) & 31);
        instance.set(12, (i >>> 5) & 63);
        instance.set(13, (i & 31) * 2);
        return instance.getTime();
    }

    public Date getArcTime() {
        return this.arcTime;
    }

    public void setArcTime(Date date) {
        this.arcTime = date;
    }

    public Date getATime() {
        return this.aTime;
    }

    public void setATime(Date date) {
        this.aTime = date;
    }

    public Date getCTime() {
        return this.cTime;
    }

    public void setCTime(Date date) {
        this.cTime = date;
    }

    public int getFileAttr() {
        return this.fileAttr;
    }

    public void setFileAttr(int i) {
        this.fileAttr = i;
    }

    public int getFileCRC() {
        return this.fileCRC;
    }

    public byte[] getFileNameByteArray() {
        return this.fileNameBytes;
    }

    public String getFileNameString() {
        return this.fileName;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public String getFileNameW() {
        return this.fileNameW;
    }

    public void setFileNameW(String str) {
        this.fileNameW = str;
    }

    public int getHighPackSize() {
        return this.highPackSize;
    }

    public int getHighUnpackSize() {
        return this.highUnpackSize;
    }

    public HostSystem getHostOS() {
        return this.hostOS;
    }

    public Date getMTime() {
        return this.mTime;
    }

    public void setMTime(Date date) {
        this.mTime = date;
    }

    public short getNameSize() {
        return this.nameSize;
    }

    public int getRecoverySectors() {
        return this.recoverySectors;
    }

    public byte[] getSalt() {
        return this.salt;
    }

    public byte[] getSubData() {
        return this.subData;
    }

    public int getSubFlags() {
        return this.subFlags;
    }

    public byte getUnpMethod() {
        return this.unpMethod;
    }

    public long getUnpSize() {
        return this.unpSize;
    }

    public byte getUnpVersion() {
        return this.unpVersion;
    }

    public long getFullPackSize() {
        return this.fullPackSize;
    }

    public long getFullUnpackSize() {
        return this.fullUnpackSize;
    }

    public String toString() {
        return super.toString();
    }

    public boolean isSplitAfter() {
        return (this.flags & 2) != 0;
    }

    public boolean isSplitBefore() {
        return (this.flags & 1) != 0;
    }

    public boolean isSolid() {
        return (this.flags & 16) != 0;
    }

    public boolean isEncrypted() {
        return (this.flags & 4) != 0;
    }

    public boolean isUnicode() {
        return (this.flags & 512) != 0;
    }

    public boolean isFileHeader() {
        return UnrarHeadertype.FileHeader.equals(this.headerType);
    }

    public boolean hasSalt() {
        return (this.flags & 1024) != 0;
    }

    public boolean isLargeBlock() {
        return (this.flags & 256) != 0;
    }

    public boolean isDirectory() {
        return (this.flags & 224) == 224;
    }
}
