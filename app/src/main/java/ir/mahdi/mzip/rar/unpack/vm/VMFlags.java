package ir.mahdi.mzip.rar.unpack.vm;

public enum VMFlags {
    VM_FC(1),
    VM_FZ(2),
    VM_FS(Integer.MIN_VALUE);
    
    private int flag;

    private VMFlags(int i) {
        this.flag = i;
    }

    public static VMFlags findFlag(int i) {
        if (VM_FC.equals(i)) {
            return VM_FC;
        }
        if (VM_FS.equals(i)) {
            return VM_FS;
        }
        return VM_FZ.equals(i) != 0 ? VM_FZ : 0;
    }

    public boolean equals(int i) {
        return this.flag == i;
    }

    public int getFlag() {
        return this.flag;
    }
}
