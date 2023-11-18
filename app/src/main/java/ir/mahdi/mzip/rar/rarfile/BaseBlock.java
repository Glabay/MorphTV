package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class BaseBlock {
    public static final short BaseBlockSize = (short) 7;
    public static final short EARC_DATACRC = (short) 2;
    public static final short EARC_NEXT_VOLUME = (short) 1;
    public static final short EARC_REVSPACE = (short) 4;
    public static final short EARC_VOLNUMBER = (short) 8;
    public static final short LHD_COMMENT = (short) 8;
    public static final short LHD_DIRECTORY = (short) 224;
    public static final short LHD_EXTFLAGS = (short) 8192;
    public static final short LHD_EXTTIME = (short) 4096;
    public static final short LHD_LARGE = (short) 256;
    public static final short LHD_PASSWORD = (short) 4;
    public static final short LHD_SALT = (short) 1024;
    public static final short LHD_SOLID = (short) 16;
    public static final short LHD_SPLIT_AFTER = (short) 2;
    public static final short LHD_SPLIT_BEFORE = (short) 1;
    public static final short LHD_UNICODE = (short) 512;
    public static final short LHD_VERSION = (short) 2048;
    public static final short LHD_WINDOW1024 = (short) 128;
    public static final short LHD_WINDOW128 = (short) 32;
    public static final short LHD_WINDOW2048 = (short) 160;
    public static final short LHD_WINDOW256 = (short) 64;
    public static final short LHD_WINDOW4096 = (short) 192;
    public static final short LHD_WINDOW512 = (short) 96;
    public static final short LHD_WINDOW64 = (short) 0;
    public static final short LHD_WINDOWMASK = (short) 224;
    public static final short LONG_BLOCK = Short.MIN_VALUE;
    public static final short MHD_AV = (short) 32;
    public static final short MHD_COMMENT = (short) 2;
    public static final short MHD_ENCRYPTVER = (short) 512;
    public static final short MHD_FIRSTVOLUME = (short) 256;
    public static final short MHD_LOCK = (short) 4;
    public static final short MHD_NEWNUMBERING = (short) 16;
    public static final short MHD_PACK_COMMENT = (short) 16;
    public static final short MHD_PASSWORD = (short) 128;
    public static final short MHD_PROTECT = (short) 64;
    public static final short MHD_SOLID = (short) 8;
    public static final short MHD_VOLUME = (short) 1;
    public static final short SKIP_IF_UNKNOWN = (short) 16384;
    protected short flags = (short) 0;
    protected short headCRC = (short) 0;
    protected short headerSize = (short) 0;
    protected byte headerType = (byte) 0;
    protected long positionInFile;

    public BaseBlock(BaseBlock baseBlock) {
        this.flags = baseBlock.getFlags();
        this.headCRC = baseBlock.getHeadCRC();
        this.headerType = baseBlock.getHeaderType().getHeaderByte();
        this.headerSize = baseBlock.getHeaderSize();
        this.positionInFile = baseBlock.getPositionInFile();
    }

    public BaseBlock(byte[] bArr) {
        this.headCRC = Raw.readShortLittleEndian(bArr, 0);
        this.headerType = (byte) (this.headerType | (bArr[2] & 255));
        this.flags = Raw.readShortLittleEndian(bArr, 3);
        this.headerSize = Raw.readShortLittleEndian(bArr, 5);
    }

    public boolean hasArchiveDataCRC() {
        return (this.flags & 2) != 0;
    }

    public boolean hasVolumeNumber() {
        return (this.flags & 8) != 0;
    }

    public boolean hasEncryptVersion() {
        return (this.flags & 512) != 0;
    }

    public boolean isSubBlock() {
        boolean z = true;
        if (UnrarHeadertype.SubHeader.equals(this.headerType)) {
            return true;
        }
        if (!UnrarHeadertype.NewSubHeader.equals(this.headerType) || (this.flags & 16) == 0) {
            z = false;
        }
        return z;
    }

    public long getPositionInFile() {
        return this.positionInFile;
    }

    public void setPositionInFile(long j) {
        this.positionInFile = j;
    }

    public short getFlags() {
        return this.flags;
    }

    public short getHeadCRC() {
        return this.headCRC;
    }

    public short getHeaderSize() {
        return this.headerSize;
    }

    public UnrarHeadertype getHeaderType() {
        return UnrarHeadertype.findType(this.headerType);
    }

    public void print() {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("HeaderType: ");
        stringBuilder2.append(getHeaderType());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nHeadCRC: ");
        stringBuilder2.append(Integer.toHexString(getHeadCRC()));
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nFlags: ");
        stringBuilder2.append(Integer.toHexString(getFlags()));
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nHeaderSize: ");
        stringBuilder2.append(getHeaderSize());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nPosition in file: ");
        stringBuilder2.append(getPositionInFile());
        stringBuilder.append(stringBuilder2.toString());
    }
}
