package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class UnixOwnersHeader extends SubBlockHeader {
    private String group;
    private int groupNameSize;
    private String owner;
    private int ownerNameSize;

    public UnixOwnersHeader(SubBlockHeader subBlockHeader, byte[] bArr) {
        super(subBlockHeader);
        this.ownerNameSize = Raw.readShortLittleEndian(bArr, 0) & 65535;
        this.groupNameSize = Raw.readShortLittleEndian(bArr, 2) & 65535;
        if (this.ownerNameSize + 4 < bArr.length) {
            Object obj = new byte[this.ownerNameSize];
            System.arraycopy(bArr, 4, obj, 0, this.ownerNameSize);
            this.owner = new String(obj);
        }
        int i = 4 + this.ownerNameSize;
        if (this.groupNameSize + i < bArr.length) {
            obj = new byte[this.groupNameSize];
            System.arraycopy(bArr, i, obj, 0, this.groupNameSize);
            this.group = new String(obj);
        }
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String str) {
        this.group = str;
    }

    public int getGroupNameSize() {
        return this.groupNameSize;
    }

    public void setGroupNameSize(int i) {
        this.groupNameSize = i;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String str) {
        this.owner = str;
    }

    public int getOwnerNameSize() {
        return this.ownerNameSize;
    }

    public void setOwnerNameSize(int i) {
        this.ownerNameSize = i;
    }

    public void print() {
        super.print();
    }
}
