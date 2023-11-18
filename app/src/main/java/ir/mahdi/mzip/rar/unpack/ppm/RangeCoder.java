package ir.mahdi.mzip.rar.unpack.ppm;

import android.support.v4.media.session.PlaybackStateCompat;
import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.unpack.Unpack;
import java.io.IOException;

public class RangeCoder {
    public static final int BOT = 32768;
    public static final int TOP = 16777216;
    private static final long uintMask = 4294967295L;
    private long code;
    private long low;
    private long range;
    private final SubRange subRange = new SubRange();
    private Unpack unpackRead;

    public static class SubRange {
        private long highCount;
        private long lowCount;
        private long scale;

        public long getHighCount() {
            return this.highCount;
        }

        public void setHighCount(long j) {
            this.highCount = j & 4294967295L;
        }

        public long getLowCount() {
            return this.lowCount & 4294967295L;
        }

        public void setLowCount(long j) {
            this.lowCount = j & 4294967295L;
        }

        public long getScale() {
            return this.scale;
        }

        public void setScale(long j) {
            this.scale = j & 4294967295L;
        }

        public void incScale(int i) {
            setScale(getScale() + ((long) i));
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SubRange[");
            stringBuilder.append("\n  lowCount=");
            stringBuilder.append(this.lowCount);
            stringBuilder.append("\n  highCount=");
            stringBuilder.append(this.highCount);
            stringBuilder.append("\n  scale=");
            stringBuilder.append(this.scale);
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public SubRange getSubRange() {
        return this.subRange;
    }

    public void initDecoder(Unpack unpack) throws IOException, RarException {
        this.unpackRead = unpack;
        this.code = 0;
        this.low = 0;
        this.range = 4294967295L;
        for (unpack = null; unpack < 4; unpack++) {
            this.code = ((this.code << 8) | ((long) getChar())) & 4294967295L;
        }
    }

    public int getCurrentCount() {
        this.range = (this.range / this.subRange.getScale()) & 4294967295L;
        return (int) ((this.code - this.low) / this.range);
    }

    public long getCurrentShiftCount(int i) {
        this.range >>>= i;
        return ((this.code - this.low) / this.range) & 4294967295L;
    }

    public void decode() {
        this.low = (this.low + (this.range * this.subRange.getLowCount())) & 4294967295L;
        this.range = (this.range * (this.subRange.getHighCount() - this.subRange.getLowCount())) & 4294967295L;
    }

    private int getChar() throws IOException, RarException {
        return this.unpackRead.getChar();
    }

    public void ariDecNormalize() throws IOException, RarException {
        Object obj = null;
        while (true) {
            if ((this.low ^ (this.low + this.range)) >= 16777216) {
                obj = this.range < PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID ? 1 : null;
                if (obj == null) {
                    return;
                }
            }
            if (obj != null) {
                this.range = ((-this.low) & 32767) & 4294967295L;
                obj = null;
            }
            this.code = ((this.code << 8) | ((long) getChar())) & 4294967295L;
            this.range = (this.range << 8) & 4294967295L;
            this.low = (this.low << 8) & 4294967295L;
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RangeCoder[");
        stringBuilder.append("\n  low=");
        stringBuilder.append(this.low);
        stringBuilder.append("\n  code=");
        stringBuilder.append(this.code);
        stringBuilder.append("\n  range=");
        stringBuilder.append(this.range);
        stringBuilder.append("\n  subrange=");
        stringBuilder.append(this.subRange);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
