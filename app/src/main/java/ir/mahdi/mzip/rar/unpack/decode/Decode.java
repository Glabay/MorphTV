package ir.mahdi.mzip.rar.unpack.decode;

public class Decode {
    private final int[] decodeLen = new int[16];
    protected int[] decodeNum = new int[2];
    private final int[] decodePos = new int[16];
    private int maxNum;

    public int[] getDecodeLen() {
        return this.decodeLen;
    }

    public int[] getDecodeNum() {
        return this.decodeNum;
    }

    public int[] getDecodePos() {
        return this.decodePos;
    }

    public int getMaxNum() {
        return this.maxNum;
    }

    public void setMaxNum(int i) {
        this.maxNum = i;
    }
}
