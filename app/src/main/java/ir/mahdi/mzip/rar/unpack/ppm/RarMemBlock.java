package ir.mahdi.mzip.rar.unpack.ppm;

import ir.mahdi.mzip.rar.io.Raw;

public class RarMemBlock extends Pointer {
    public static final int size = 12;
    private int NU;
    private int next;
    private int prev;
    private int stamp;

    public RarMemBlock(byte[] bArr) {
        super(bArr);
    }

    public void insertAt(RarMemBlock rarMemBlock) {
        RarMemBlock rarMemBlock2 = new RarMemBlock(this.mem);
        setPrev(rarMemBlock.getAddress());
        rarMemBlock2.setAddress(getPrev());
        setNext(rarMemBlock2.getNext());
        rarMemBlock2.setNext(this);
        rarMemBlock2.setAddress(getNext());
        rarMemBlock2.setPrev(this);
    }

    public void remove() {
        RarMemBlock rarMemBlock = new RarMemBlock(this.mem);
        rarMemBlock.setAddress(getPrev());
        rarMemBlock.setNext(getNext());
        rarMemBlock.setAddress(getNext());
        rarMemBlock.setPrev(getPrev());
    }

    public int getNext() {
        if (this.mem != null) {
            this.next = Raw.readIntLittleEndian(this.mem, this.pos + 4);
        }
        return this.next;
    }

    public void setNext(int i) {
        this.next = i;
        if (this.mem != null) {
            Raw.writeIntLittleEndian(this.mem, this.pos + 4, i);
        }
    }

    public void setNext(RarMemBlock rarMemBlock) {
        setNext(rarMemBlock.getAddress());
    }

    public int getNU() {
        if (this.mem != null) {
            this.NU = Raw.readShortLittleEndian(this.mem, this.pos + 2) & 65535;
        }
        return this.NU;
    }

    public void setNU(int i) {
        this.NU = 65535 & i;
        if (this.mem != null) {
            Raw.writeShortLittleEndian(this.mem, this.pos + 2, (short) i);
        }
    }

    public int getPrev() {
        if (this.mem != null) {
            this.prev = Raw.readIntLittleEndian(this.mem, this.pos + 8);
        }
        return this.prev;
    }

    public void setPrev(int i) {
        this.prev = i;
        if (this.mem != null) {
            Raw.writeIntLittleEndian(this.mem, this.pos + 8, i);
        }
    }

    public void setPrev(RarMemBlock rarMemBlock) {
        setPrev(rarMemBlock.getAddress());
    }

    public int getStamp() {
        if (this.mem != null) {
            this.stamp = Raw.readShortLittleEndian(this.mem, this.pos) & 65535;
        }
        return this.stamp;
    }

    public void setStamp(int i) {
        this.stamp = i;
        if (this.mem != null) {
            Raw.writeShortLittleEndian(this.mem, this.pos, (short) i);
        }
    }
}
