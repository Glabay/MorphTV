package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class MainHeader extends BaseBlock {
    public static final short mainHeaderSize = (short) 6;
    public static final short mainHeaderSizeWithEnc = (short) 7;
    private byte encryptVersion;
    private short highPosAv;
    private int posAv;

    public MainHeader(BaseBlock baseBlock, byte[] bArr) {
        super(baseBlock);
        this.highPosAv = Raw.readShortLittleEndian(bArr, null);
        this.posAv = Raw.readIntLittleEndian(bArr, 2);
        if (hasEncryptVersion() != null) {
            this.encryptVersion = (byte) (this.encryptVersion | (bArr[6] & 255));
        }
    }

    public boolean hasArchCmt() {
        return (this.flags & 2) != 0;
    }

    public byte getEncryptVersion() {
        return this.encryptVersion;
    }

    public short getHighPosAv() {
        return this.highPosAv;
    }

    public int getPosAv() {
        return this.posAv;
    }

    public boolean isEncrypted() {
        return (this.flags & 128) != 0;
    }

    public boolean isMultiVolume() {
        return (this.flags & 1) != 0;
    }

    public boolean isFirstVolume() {
        return (this.flags & 256) != 0;
    }

    public void print() {
        super.print();
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("posav: ");
        stringBuilder2.append(getPosAv());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nhighposav: ");
        stringBuilder2.append(getHighPosAv());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nhasencversion: ");
        stringBuilder2.append(hasEncryptVersion());
        stringBuilder2.append(hasEncryptVersion() ? Byte.valueOf(getEncryptVersion()) : "");
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nhasarchcmt: ");
        stringBuilder2.append(hasArchCmt());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nisEncrypted: ");
        stringBuilder2.append(isEncrypted());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nisMultivolume: ");
        stringBuilder2.append(isMultiVolume());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nisFirstvolume: ");
        stringBuilder2.append(isFirstVolume());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nisSolid: ");
        stringBuilder2.append(isSolid());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nisLocked: ");
        stringBuilder2.append(isLocked());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nisProtected: ");
        stringBuilder2.append(isProtected());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("\nisAV: ");
        stringBuilder2.append(isAV());
        stringBuilder.append(stringBuilder2.toString());
    }

    public boolean isSolid() {
        return (this.flags & 8) != 0;
    }

    public boolean isLocked() {
        return (this.flags & 4) != 0;
    }

    public boolean isProtected() {
        return (this.flags & 64) != 0;
    }

    public boolean isAV() {
        return (this.flags & 32) != 0;
    }

    public boolean isNewNumbering() {
        return (this.flags & 16) != 0;
    }
}
