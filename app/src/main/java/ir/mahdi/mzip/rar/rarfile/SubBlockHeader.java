package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class SubBlockHeader extends BlockHeader {
    public static final short SubBlockHeaderSize = (short) 3;
    private byte level;
    private short subType;

    public SubBlockHeader(SubBlockHeader subBlockHeader) {
        super(subBlockHeader);
        this.subType = subBlockHeader.getSubType().getSubblocktype();
        this.level = subBlockHeader.getLevel();
    }

    public SubBlockHeader(BlockHeader blockHeader, byte[] bArr) {
        super(blockHeader);
        this.subType = Raw.readShortLittleEndian(bArr, null);
        this.level = (byte) (this.level | (bArr[2] & 255));
    }

    public byte getLevel() {
        return this.level;
    }

    public SubBlockHeaderType getSubType() {
        return SubBlockHeaderType.findSubblockHeaderType(this.subType);
    }

    public void print() {
        super.print();
    }
}
