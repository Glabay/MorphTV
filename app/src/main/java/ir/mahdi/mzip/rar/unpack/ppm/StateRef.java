package ir.mahdi.mzip.rar.unpack.ppm;

public class StateRef {
    private int freq;
    private int successor;
    private int symbol;

    public int getSymbol() {
        return this.symbol;
    }

    public void setSymbol(int i) {
        this.symbol = i & 255;
    }

    public int getFreq() {
        return this.freq;
    }

    public void setFreq(int i) {
        this.freq = i & 255;
    }

    public void incFreq(int i) {
        this.freq = (this.freq + i) & 255;
    }

    public void decFreq(int i) {
        this.freq = (this.freq - i) & 255;
    }

    public void setValues(State state) {
        setFreq(state.getFreq());
        setSuccessor(state.getSuccessor());
        setSymbol(state.getSymbol());
    }

    public int getSuccessor() {
        return this.successor;
    }

    public void setSuccessor(int i) {
        this.successor = i;
    }

    public void setSuccessor(PPMContext pPMContext) {
        setSuccessor(pPMContext.getAddress());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("State[");
        stringBuilder.append("\n  symbol=");
        stringBuilder.append(getSymbol());
        stringBuilder.append("\n  freq=");
        stringBuilder.append(getFreq());
        stringBuilder.append("\n  successor=");
        stringBuilder.append(getSuccessor());
        stringBuilder.append("\n]");
        return stringBuilder.toString();
    }
}
