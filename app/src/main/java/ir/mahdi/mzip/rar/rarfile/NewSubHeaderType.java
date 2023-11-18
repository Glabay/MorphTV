package ir.mahdi.mzip.rar.rarfile;

import java.util.Arrays;

public class NewSubHeaderType {
    public static final NewSubHeaderType SUBHEAD_TYPE_ACL = new NewSubHeaderType(new byte[]{(byte) 65, (byte) 67, (byte) 76});
    public static final NewSubHeaderType SUBHEAD_TYPE_AV = new NewSubHeaderType(new byte[]{(byte) 65, (byte) 86});
    public static final NewSubHeaderType SUBHEAD_TYPE_BEOSEA = new NewSubHeaderType(new byte[]{(byte) 69, (byte) 65, (byte) 66, (byte) 69});
    public static final NewSubHeaderType SUBHEAD_TYPE_CMT = new NewSubHeaderType(new byte[]{(byte) 67, (byte) 77, (byte) 84});
    public static final NewSubHeaderType SUBHEAD_TYPE_OS2EA = new NewSubHeaderType(new byte[]{(byte) 69, (byte) 65, (byte) 50});
    public static final NewSubHeaderType SUBHEAD_TYPE_RR = new NewSubHeaderType(new byte[]{(byte) 82, (byte) 82});
    public static final NewSubHeaderType SUBHEAD_TYPE_STREAM = new NewSubHeaderType(new byte[]{(byte) 83, (byte) 84, (byte) 77});
    public static final NewSubHeaderType SUBHEAD_TYPE_UOWNER = new NewSubHeaderType(new byte[]{(byte) 85, (byte) 79, (byte) 87});
    private byte[] headerTypes;

    private NewSubHeaderType(byte[] bArr) {
        this.headerTypes = bArr;
    }

    public boolean byteEquals(byte[] bArr) {
        return Arrays.equals(this.headerTypes, bArr);
    }

    public String toString() {
        return new String(this.headerTypes);
    }
}
