package ir.mahdi.mzip.rar.unpack;

import ir.mahdi.mzip.rar.unpack.vm.VMPreparedProgram;

public class UnpackFilter {
    private int BlockLength;
    private int BlockStart;
    private int ExecCount;
    private boolean NextWindow;
    private int ParentFilter;
    private VMPreparedProgram Prg = new VMPreparedProgram();

    public int getBlockLength() {
        return this.BlockLength;
    }

    public void setBlockLength(int i) {
        this.BlockLength = i;
    }

    public int getBlockStart() {
        return this.BlockStart;
    }

    public void setBlockStart(int i) {
        this.BlockStart = i;
    }

    public int getExecCount() {
        return this.ExecCount;
    }

    public void setExecCount(int i) {
        this.ExecCount = i;
    }

    public boolean isNextWindow() {
        return this.NextWindow;
    }

    public void setNextWindow(boolean z) {
        this.NextWindow = z;
    }

    public int getParentFilter() {
        return this.ParentFilter;
    }

    public void setParentFilter(int i) {
        this.ParentFilter = i;
    }

    public VMPreparedProgram getPrg() {
        return this.Prg;
    }

    public void setPrg(VMPreparedProgram vMPreparedProgram) {
        this.Prg = vMPreparedProgram;
    }
}
