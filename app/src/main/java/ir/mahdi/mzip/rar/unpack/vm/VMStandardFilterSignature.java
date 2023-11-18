package ir.mahdi.mzip.rar.unpack.vm;

public class VMStandardFilterSignature {
    private int CRC;
    private int length;
    private VMStandardFilters type;

    public VMStandardFilterSignature(int i, int i2, VMStandardFilters vMStandardFilters) {
        this.length = i;
        this.CRC = i2;
        this.type = vMStandardFilters;
    }

    public int getCRC() {
        return this.CRC;
    }

    public void setCRC(int i) {
        this.CRC = i;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int i) {
        this.length = i;
    }

    public VMStandardFilters getType() {
        return this.type;
    }

    public void setType(VMStandardFilters vMStandardFilters) {
        this.type = vMStandardFilters;
    }
}
