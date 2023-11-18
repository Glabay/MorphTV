package ir.mahdi.mzip.rar.unpack.vm;

public class VMPreparedCommand {
    private boolean ByteMode;
    private VMPreparedOperand Op1 = new VMPreparedOperand();
    private VMPreparedOperand Op2 = new VMPreparedOperand();
    private VMCommands OpCode;

    public boolean isByteMode() {
        return this.ByteMode;
    }

    public void setByteMode(boolean z) {
        this.ByteMode = z;
    }

    public VMPreparedOperand getOp1() {
        return this.Op1;
    }

    public void setOp1(VMPreparedOperand vMPreparedOperand) {
        this.Op1 = vMPreparedOperand;
    }

    public VMPreparedOperand getOp2() {
        return this.Op2;
    }

    public void setOp2(VMPreparedOperand vMPreparedOperand) {
        this.Op2 = vMPreparedOperand;
    }

    public VMCommands getOpCode() {
        return this.OpCode;
    }

    public void setOpCode(VMCommands vMCommands) {
        this.OpCode = vMCommands;
    }
}
