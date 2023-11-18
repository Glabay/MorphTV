package ir.mahdi.mzip.rar.unpack.vm;

public enum VMOpType {
    VM_OPREG(0),
    VM_OPINT(1),
    VM_OPREGMEM(2),
    VM_OPNONE(3);
    
    private int opType;

    private VMOpType(int i) {
        this.opType = i;
    }

    public static VMOpType findOpType(int i) {
        if (VM_OPREG.equals(i)) {
            return VM_OPREG;
        }
        if (VM_OPINT.equals(i)) {
            return VM_OPINT;
        }
        if (VM_OPREGMEM.equals(i)) {
            return VM_OPREGMEM;
        }
        return VM_OPNONE.equals(i) != 0 ? VM_OPNONE : 0;
    }

    public int getOpType() {
        return this.opType;
    }

    public boolean equals(int i) {
        return this.opType == i;
    }
}
