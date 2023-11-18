package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class AppendableOutputStream<T extends Appendable> extends OutputStream {
    private final T appendable;

    public AppendableOutputStream(T t) {
        this.appendable = t;
    }

    public void write(int i) throws IOException {
        this.appendable.append((char) i);
    }

    public T getAppendable() {
        return this.appendable;
    }
}
