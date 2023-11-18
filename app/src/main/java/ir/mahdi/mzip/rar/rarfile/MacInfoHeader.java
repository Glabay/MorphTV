package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class MacInfoHeader extends SubBlockHeader {
    public static final short MacInfoHeaderSize = (short) 8;
    private int fileCreator;
    private int fileType;

    public MacInfoHeader(SubBlockHeader subBlockHeader, byte[] bArr) {
        super(subBlockHeader);
        this.fileType = Raw.readIntLittleEndian(bArr, null);
        this.fileCreator = Raw.readIntLittleEndian(bArr, 4);
    }

    public int getFileCreator() {
        return this.fileCreator;
    }

    public void setFileCreator(int i) {
        this.fileCreator = i;
    }

    public int getFileType() {
        return this.fileType;
    }

    public void setFileType(int i) {
        this.fileType = i;
    }

    public void print() {
        super.print();
    }
}
