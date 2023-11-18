package ir.mahdi.mzip.rar.rarfile;

public enum SubBlockHeaderType {
    EA_HEAD((short) 256),
    UO_HEAD((short) 257),
    MAC_HEAD((short) 258),
    BEEA_HEAD((short) 259),
    NTACL_HEAD((short) 260),
    STREAM_HEAD((short) 261);
    
    private short subblocktype;

    private SubBlockHeaderType(short s) {
        this.subblocktype = s;
    }

    public static SubBlockHeaderType findSubblockHeaderType(short s) {
        if (EA_HEAD.equals(s)) {
            return EA_HEAD;
        }
        if (UO_HEAD.equals(s)) {
            return UO_HEAD;
        }
        if (MAC_HEAD.equals(s)) {
            return MAC_HEAD;
        }
        if (BEEA_HEAD.equals(s)) {
            return BEEA_HEAD;
        }
        if (NTACL_HEAD.equals(s)) {
            return NTACL_HEAD;
        }
        return STREAM_HEAD.equals(s) != (short) 0 ? STREAM_HEAD : (short) 0;
    }

    public boolean equals(short s) {
        return this.subblocktype == s;
    }

    public short getSubblocktype() {
        return this.subblocktype;
    }
}
