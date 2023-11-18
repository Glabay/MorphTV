package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.LinkedList;
import java.util.Queue;

@Beta
public final class LineReader {
    private final char[] buf = new char[4096];
    private final CharBuffer cbuf = CharBuffer.wrap(this.buf);
    private final LineBuffer lineBuf = new C11601();
    private final Queue<String> lines = new LinkedList();
    private final Readable readable;
    private final Reader reader;

    /* renamed from: com.google.common.io.LineReader$1 */
    class C11601 extends LineBuffer {
        C11601() {
        }

        protected void handleLine(String str, String str2) {
            LineReader.this.lines.add(str);
        }
    }

    public LineReader(Readable readable) {
        this.readable = (Readable) Preconditions.checkNotNull(readable);
        this.reader = readable instanceof Reader ? (Reader) readable : null;
    }

    public String readLine() throws IOException {
        while (this.lines.peek() == null) {
            this.cbuf.clear();
            int read = this.reader != null ? this.reader.read(this.buf, 0, this.buf.length) : this.readable.read(this.cbuf);
            if (read == -1) {
                this.lineBuf.finish();
                break;
            }
            this.lineBuf.add(this.buf, 0, read);
        }
        return (String) this.lines.poll();
    }
}
