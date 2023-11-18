package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class AVHeader extends BaseBlock {
    public static final int avHeaderSize = 7;
    private int avInfoCRC;
    private byte avVersion;
    private byte method;
    private byte unpackVersion;

    public AVHeader(BaseBlock baseBlock, byte[] bArr) {
        super(baseBlock);
        this.unpackVersion = (byte) (this.unpackVersion | (bArr[0] & 255));
        this.method = (byte) (this.method | (bArr[1] & 255));
        this.avVersion = (byte) (this.avVersion | (bArr[2] & 255));
        this.avInfoCRC = Raw.readIntLittleEndian(bArr, 3);
    }

    public int getAvInfoCRC() {
        return this.avInfoCRC;
    }

    public byte getAvVersion() {
        return this.avVersion;
    }

    public byte getMethod() {
        return this.method;
    }

    public byte getUnpackVersion() {
        return this.unpackVersion;
    }
}
