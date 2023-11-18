package okio;

import java.io.OutputStream;

class Buffer$1 extends OutputStream {
    final /* synthetic */ Buffer this$0;

    public void close() {
    }

    public void flush() {
    }

    Buffer$1(Buffer buffer) {
        this.this$0 = buffer;
    }

    public void write(int i) {
        this.this$0.writeByte((byte) i);
    }

    public void write(byte[] bArr, int i, int i2) {
        this.this$0.write(bArr, i, i2);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.this$0);
        stringBuilder.append(".outputStream()");
        return stringBuilder.toString();
    }
}
