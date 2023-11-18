package ir.mahdi.mzip.rar.unpack.vm;

public class VMPreparedOperand {
    private int Base;
    private int Data;
    private VMOpType Type;
    private int offset;

    public int getBase() {
        return this.Base;
    }

    public void setBase(int i) {
        this.Base = i;
    }

    public int getData() {
        return this.Data;
    }

    public void setData(int i) {
        this.Data = i;
    }

    public VMOpType getType() {
        return this.Type;
    }

    public void setType(VMOpType vMOpType) {
        this.Type = vMOpType;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int i) {
        this.offset = i;
    }
}
