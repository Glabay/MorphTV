package com.frostwire.jlibtorrent;

public final class IntSeries {
    private final long[] buffer;
    private int end;
    private int head;
    private int size;

    IntSeries(long[] jArr) {
        if (jArr == null) {
            throw new IllegalArgumentException("buffer to hold data can't be null");
        } else if (jArr.length == 0) {
            throw new IllegalArgumentException("buffer to hold data can't be of size 0");
        } else {
            this.buffer = jArr;
            this.head = -1;
            this.end = -1;
            this.size = null;
        }
    }

    IntSeries(int i) {
        this(new long[i]);
    }

    void add(long j) {
        if (this.head == -1) {
            this.end = 0;
            this.head = 0;
            this.buffer[this.end] = j;
            this.size = 1;
            return;
        }
        this.end = (this.end + 1) % this.buffer.length;
        this.buffer[this.end] = j;
        if (this.end <= this.head) {
            this.head = (this.head + 1) % this.buffer.length;
        }
        if (this.head <= this.end) {
            this.size = (this.end - this.head) + 1;
        } else {
            this.size = this.buffer.length;
        }
    }

    public long get(int i) {
        return this.buffer[(this.head + i) % this.size];
    }

    public long last() {
        return this.end != 0 ? this.buffer[this.end] : 0;
    }

    public int size() {
        return this.size;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ ");
        for (int i = 0; i < this.buffer.length; i++) {
            stringBuilder.append(this.buffer[i]);
            if (i != this.buffer.length - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(" ]");
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("{ head: ");
        stringBuilder2.append(this.head);
        stringBuilder2.append(", end: ");
        stringBuilder2.append(this.end);
        stringBuilder2.append(", size: ");
        stringBuilder2.append(size());
        stringBuilder2.append(", buffer: ");
        stringBuilder2.append(stringBuilder.toString());
        stringBuilder2.append(" }");
        return stringBuilder2.toString();
    }
}
