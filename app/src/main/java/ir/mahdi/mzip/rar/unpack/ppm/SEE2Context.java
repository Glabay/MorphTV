package ir.mahdi.mzip.rar.unpack.ppm;

public class SEE2Context {
    public static final int size = 4;
    private int count;
    private int shift;
    private int summ;

    public void init(int i) {
        this.shift = 3;
        this.summ = (i << this.shift) & 65535;
        this.count = 4;
    }

    public int getMean() {
        int i = this.summ >>> this.shift;
        this.summ -= i;
        return i + (i == 0 ? 1 : 0);
    }

    public void update() {
        if (this.shift < 7) {
            int i = this.count - 1;
            this.count = i;
            if (i == 0) {
                this.summ += this.summ;
                int i2 = this.shift;
                this.shift = i2 + 1;
                this.count = 3 << i2;
            }
        }
        this.summ &= 65535;
        this.count &= 255;
        this.shift &= 255;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int i) {
        this.count = i & 255;
    }

    public int getShift() {
        return this.shift;
    }

    public void setShift(int i) {
        this.shift = i & 255;
    }

    public int getSumm() {
        return this.summ;
    }

    public void setSumm(int i) {
        this.summ = i & 65535;
    }

    public void incSumm(int i) {
        setSumm(getSumm() + i);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SEE2Context[");
        stringBuilder.append("\n  size=");
        stringBuilder.append(4);
        stringBuilder.append("\n  summ=");
        stringBuilder.append(this.summ);
        stringBuilder.append("\n  shift=");
        stringBuilder.append(this.shift);
        stringBuilder.append("\n  count=");
        stringBuilder.append(this.count);
        stringBuilder.append("\n]");
        return stringBuilder.toString();
    }
}
