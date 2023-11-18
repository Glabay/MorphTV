package ir.mahdi.mzip.rar.rarfile;

public enum UnrarHeadertype {
    MainHeader((byte) 115),
    MarkHeader((byte) 114),
    FileHeader((byte) 116),
    CommHeader((byte) 117),
    AvHeader((byte) 118),
    SubHeader((byte) 119),
    ProtectHeader((byte) 120),
    SignHeader((byte) 121),
    NewSubHeader((byte) 122),
    EndArcHeader((byte) 123);
    
    private byte headerByte;

    private UnrarHeadertype(byte b) {
        this.headerByte = b;
    }

    public static UnrarHeadertype findType(byte b) {
        if (MarkHeader.equals(b)) {
            return MarkHeader;
        }
        if (MainHeader.equals(b)) {
            return MainHeader;
        }
        if (FileHeader.equals(b)) {
            return FileHeader;
        }
        if (EndArcHeader.equals(b)) {
            return EndArcHeader;
        }
        if (NewSubHeader.equals(b)) {
            return NewSubHeader;
        }
        if (SubHeader.equals(b)) {
            return SubHeader;
        }
        if (SignHeader.equals(b)) {
            return SignHeader;
        }
        if (ProtectHeader.equals(b)) {
            return ProtectHeader;
        }
        if (MarkHeader.equals(b)) {
            return MarkHeader;
        }
        if (MainHeader.equals(b)) {
            return MainHeader;
        }
        if (FileHeader.equals(b)) {
            return FileHeader;
        }
        if (EndArcHeader.equals(b)) {
            return EndArcHeader;
        }
        if (CommHeader.equals(b)) {
            return CommHeader;
        }
        return AvHeader.equals(b) != (byte) 0 ? AvHeader : (byte) 0;
    }

    public boolean equals(byte b) {
        return this.headerByte == b;
    }

    public byte getHeaderByte() {
        return this.headerByte;
    }
}
