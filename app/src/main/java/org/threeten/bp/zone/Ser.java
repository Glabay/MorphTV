package org.threeten.bp.zone;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StreamCorruptedException;
import org.threeten.bp.ZoneOffset;

final class Ser implements Externalizable {
    static final byte SZR = (byte) 1;
    static final byte ZOT = (byte) 2;
    static final byte ZOTRULE = (byte) 3;
    private static final long serialVersionUID = -8885321777449118786L;
    private Object object;
    private byte type;

    Ser(byte b, Object obj) {
        this.type = b;
        this.object = obj;
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        writeInternal(this.type, this.object, objectOutput);
    }

    static void write(Object obj, DataOutput dataOutput) throws IOException {
        writeInternal((byte) 1, obj, dataOutput);
    }

    private static void writeInternal(byte b, Object obj, DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(b);
        switch (b) {
            case (byte) 1:
                ((StandardZoneRules) obj).writeExternal(dataOutput);
                return;
            case (byte) 2:
                ((ZoneOffsetTransition) obj).writeExternal(dataOutput);
                return;
            case (byte) 3:
                ((ZoneOffsetTransitionRule) obj).writeExternal(dataOutput);
                return;
            default:
                throw new InvalidClassException("Unknown serialized type");
        }
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.type = objectInput.readByte();
        this.object = readInternal(this.type, objectInput);
    }

    static Object read(DataInput dataInput) throws IOException, ClassNotFoundException {
        return readInternal(dataInput.readByte(), dataInput);
    }

    private static Object readInternal(byte b, DataInput dataInput) throws IOException, ClassNotFoundException {
        switch (b) {
            case (byte) 1:
                return StandardZoneRules.readExternal(dataInput);
            case (byte) 2:
                return ZoneOffsetTransition.readExternal(dataInput);
            case (byte) 3:
                return ZoneOffsetTransitionRule.readExternal(dataInput);
            default:
                throw new StreamCorruptedException("Unknown serialized type");
        }
    }

    private Object readResolve() {
        return this.object;
    }

    static void writeOffset(ZoneOffset zoneOffset, DataOutput dataOutput) throws IOException {
        zoneOffset = zoneOffset.getTotalSeconds();
        int i = zoneOffset % 900 == 0 ? zoneOffset / 900 : 127;
        dataOutput.writeByte(i);
        if (i == 127) {
            dataOutput.writeInt(zoneOffset);
        }
    }

    static ZoneOffset readOffset(DataInput dataInput) throws IOException {
        byte readByte = dataInput.readByte();
        return readByte == Byte.MAX_VALUE ? ZoneOffset.ofTotalSeconds(dataInput.readInt()) : ZoneOffset.ofTotalSeconds(readByte * 900);
    }

    static void writeEpochSec(long j, DataOutput dataOutput) throws IOException {
        if (j < -4575744000L || j >= 10413792000L || j % 900 != 0) {
            dataOutput.writeByte(255);
            dataOutput.writeLong(j);
            return;
        }
        j = (int) ((j + 4575744000L) / 900);
        dataOutput.writeByte((j >>> 16) & 255);
        dataOutput.writeByte((j >>> 8) & 255);
        dataOutput.writeByte(j & 255);
    }

    static long readEpochSec(DataInput dataInput) throws IOException {
        int readByte = dataInput.readByte() & 255;
        if (readByte == 255) {
            return dataInput.readLong();
        }
        return (((long) (((readByte << 16) + ((dataInput.readByte() & 255) << 8)) + (dataInput.readByte() & 255))) * 900) - 4575744000L;
    }
}
