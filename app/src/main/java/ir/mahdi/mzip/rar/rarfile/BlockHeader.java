package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class BlockHeader extends BaseBlock {
    public static final short blockHeaderSize = (short) 4;
    private int dataSize;
    private int packSize;

    public BlockHeader(BlockHeader blockHeader) {
        super((BaseBlock) blockHeader);
        this.packSize = blockHeader.getDataSize();
        this.dataSize = this.packSize;
        this.positionInFile = blockHeader.getPositionInFile();
    }

    public BlockHeader(BaseBlock baseBlock, byte[] bArr) {
        super(baseBlock);
        this.packSize = Raw.readIntLittleEndian(bArr, null);
        this.dataSize = this.packSize;
    }

    public int getDataSize() {
        return this.dataSize;
    }

    public int getPackSize() {
        return this.packSize;
    }

    public void print() {
        super.print();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DataSize: ");
        stringBuilder.append(getDataSize());
        stringBuilder.append(" packSize: ");
        stringBuilder.append(getPackSize());
        stringBuilder.toString();
    }
}
