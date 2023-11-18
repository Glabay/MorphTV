package ir.mahdi.mzip.rar.rarfile;

import com.google.common.base.Ascii;
import ir.mahdi.mzip.rar.io.Raw;

public class MarkHeader extends BaseBlock {
    private boolean oldFormat = null;

    public MarkHeader(BaseBlock baseBlock) {
        super(baseBlock);
    }

    public boolean isValid() {
        boolean z = false;
        if (getHeadCRC() != (short) 24914 || getHeaderType() != UnrarHeadertype.MarkHeader || getFlags() != (short) 6689) {
            return false;
        }
        if (getHeaderSize() == (short) 7) {
            z = true;
        }
        return z;
    }

    public boolean isSignature() {
        byte[] bArr = new byte[7];
        Raw.writeShortLittleEndian(bArr, 0, this.headCRC);
        bArr[2] = this.headerType;
        Raw.writeShortLittleEndian(bArr, 3, this.flags);
        Raw.writeShortLittleEndian(bArr, 5, this.headerSize);
        if (bArr[0] == (byte) 82) {
            if (bArr[1] == (byte) 69 && bArr[2] == (byte) 126 && bArr[3] == (byte) 94) {
                this.oldFormat = true;
                return true;
            } else if (bArr[1] == (byte) 97 && bArr[2] == (byte) 114 && bArr[3] == (byte) 33 && bArr[4] == Ascii.SUB && bArr[5] == (byte) 7 && bArr[6] == (byte) 0) {
                this.oldFormat = false;
                return true;
            }
        }
        return false;
    }

    public boolean isOldFormat() {
        return this.oldFormat;
    }

    public void print() {
        super.print();
    }
}
