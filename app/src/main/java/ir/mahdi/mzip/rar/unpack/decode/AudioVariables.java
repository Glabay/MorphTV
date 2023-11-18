package ir.mahdi.mzip.rar.unpack.decode;

public class AudioVariables {
    int byteCount;
    int d1;
    int d2;
    int d3;
    int d4;
    int[] dif = new int[11];
    int k1;
    int k2;
    int k3;
    int k4;
    int k5;
    int lastChar;
    int lastDelta;

    public int getByteCount() {
        return this.byteCount;
    }

    public void setByteCount(int i) {
        this.byteCount = i;
    }

    public int getD1() {
        return this.d1;
    }

    public void setD1(int i) {
        this.d1 = i;
    }

    public int getD2() {
        return this.d2;
    }

    public void setD2(int i) {
        this.d2 = i;
    }

    public int getD3() {
        return this.d3;
    }

    public void setD3(int i) {
        this.d3 = i;
    }

    public int getD4() {
        return this.d4;
    }

    public void setD4(int i) {
        this.d4 = i;
    }

    public int[] getDif() {
        return this.dif;
    }

    public void setDif(int[] iArr) {
        this.dif = iArr;
    }

    public int getK1() {
        return this.k1;
    }

    public void setK1(int i) {
        this.k1 = i;
    }

    public int getK2() {
        return this.k2;
    }

    public void setK2(int i) {
        this.k2 = i;
    }

    public int getK3() {
        return this.k3;
    }

    public void setK3(int i) {
        this.k3 = i;
    }

    public int getK4() {
        return this.k4;
    }

    public void setK4(int i) {
        this.k4 = i;
    }

    public int getK5() {
        return this.k5;
    }

    public void setK5(int i) {
        this.k5 = i;
    }

    public int getLastChar() {
        return this.lastChar;
    }

    public void setLastChar(int i) {
        this.lastChar = i;
    }

    public int getLastDelta() {
        return this.lastDelta;
    }

    public void setLastDelta(int i) {
        this.lastDelta = i;
    }
}
