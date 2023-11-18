package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class EAHeader extends SubBlockHeader {
    public static final short EAHeaderSize = (short) 10;
    private int EACRC;
    private byte method;
    private int unpSize;
    private byte unpVer;

    public EAHeader(SubBlockHeader subBlockHeader, byte[] bArr) {
        super(subBlockHeader);
        this.unpSize = Raw.readIntLittleEndian(bArr, null);
        this.unpVer = (byte) (this.unpVer | (bArr[4] & 255));
        this.method = (byte) (this.method | (bArr[5] & 255));
        this.EACRC = Raw.readIntLittleEndian(bArr, 6);
    }

    public int getEACRC() {
        return this.EACRC;
    }

    public byte getMethod() {
        return this.method;
    }

    public int getUnpSize() {
        return this.unpSize;
    }

    public byte getUnpVer() {
        return this.unpVer;
    }

    public void print() {
        super.print();
    }
}
