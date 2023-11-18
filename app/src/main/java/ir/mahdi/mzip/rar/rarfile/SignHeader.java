package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class SignHeader extends BaseBlock {
    public static final short signHeaderSize = (short) 8;
    private short arcNameSize = (short) 0;
    private int creationTime = 0;
    private short userNameSize = (short) 0;

    public SignHeader(BaseBlock baseBlock, byte[] bArr) {
        super(baseBlock);
        this.creationTime = Raw.readIntLittleEndian(bArr, 0);
        this.arcNameSize = Raw.readShortLittleEndian(bArr, 4);
        this.userNameSize = Raw.readShortLittleEndian(bArr, 6);
    }

    public short getArcNameSize() {
        return this.arcNameSize;
    }

    public int getCreationTime() {
        return this.creationTime;
    }

    public short getUserNameSize() {
        return this.userNameSize;
    }
}
