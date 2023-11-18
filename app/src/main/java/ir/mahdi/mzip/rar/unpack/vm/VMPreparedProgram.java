package ir.mahdi.mzip.rar.unpack.vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class VMPreparedProgram {
    private List<VMPreparedCommand> AltCmd;
    private List<VMPreparedCommand> Cmd;
    private int CmdCount;
    private int FilteredDataOffset;
    private int FilteredDataSize;
    private Vector<Byte> GlobalData;
    private int[] InitR;
    private Vector<Byte> StaticData;

    public VMPreparedProgram() {
        this.Cmd = new ArrayList();
        this.AltCmd = new ArrayList();
        this.GlobalData = new Vector();
        this.StaticData = new Vector();
        this.InitR = new int[7];
        this.AltCmd = null;
    }

    public List<VMPreparedCommand> getAltCmd() {
        return this.AltCmd;
    }

    public void setAltCmd(List<VMPreparedCommand> list) {
        this.AltCmd = list;
    }

    public List<VMPreparedCommand> getCmd() {
        return this.Cmd;
    }

    public void setCmd(List<VMPreparedCommand> list) {
        this.Cmd = list;
    }

    public int getCmdCount() {
        return this.CmdCount;
    }

    public void setCmdCount(int i) {
        this.CmdCount = i;
    }

    public int getFilteredDataOffset() {
        return this.FilteredDataOffset;
    }

    public void setFilteredDataOffset(int i) {
        this.FilteredDataOffset = i;
    }

    public int getFilteredDataSize() {
        return this.FilteredDataSize;
    }

    public void setFilteredDataSize(int i) {
        this.FilteredDataSize = i;
    }

    public Vector<Byte> getGlobalData() {
        return this.GlobalData;
    }

    public void setGlobalData(Vector<Byte> vector) {
        this.GlobalData = vector;
    }

    public int[] getInitR() {
        return this.InitR;
    }

    public void setInitR(int[] iArr) {
        this.InitR = iArr;
    }

    public Vector<Byte> getStaticData() {
        return this.StaticData;
    }

    public void setStaticData(Vector<Byte> vector) {
        this.StaticData = vector;
    }
}
