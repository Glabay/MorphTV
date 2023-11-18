package org.threeten.bp;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StreamCorruptedException;

final class Ser implements Externalizable {
    static final byte DURATION_TYPE = (byte) 1;
    static final byte INSTANT_TYPE = (byte) 2;
    static final byte LOCAL_DATE_TIME_TYPE = (byte) 4;
    static final byte LOCAL_DATE_TYPE = (byte) 3;
    static final byte LOCAL_TIME_TYPE = (byte) 5;
    static final byte MONTH_DAY_TYPE = (byte) 64;
    static final byte OFFSET_DATE_TIME_TYPE = (byte) 69;
    static final byte OFFSET_TIME_TYPE = (byte) 66;
    static final byte YEAR_MONTH_TYPE = (byte) 68;
    static final byte YEAR_TYPE = (byte) 67;
    static final byte ZONED_DATE_TIME_TYPE = (byte) 6;
    static final byte ZONE_OFFSET_TYPE = (byte) 8;
    static final byte ZONE_REGION_TYPE = (byte) 7;
    private static final long serialVersionUID = -7683839454370182990L;
    private Object object;
    private byte type;

    Ser(byte b, Object obj) {
        this.type = b;
        this.object = obj;
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        writeInternal(this.type, this.object, objectOutput);
    }

    static void writeInternal(byte b, Object obj, DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(b);
        if (b != (byte) 64) {
            switch (b) {
                case (byte) 1:
                    ((Duration) obj).writeExternal(dataOutput);
                    return;
                case (byte) 2:
                    ((Instant) obj).writeExternal(dataOutput);
                    return;
                case (byte) 3:
                    ((LocalDate) obj).writeExternal(dataOutput);
                    return;
                case (byte) 4:
                    ((LocalDateTime) obj).writeExternal(dataOutput);
                    return;
                case (byte) 5:
                    ((LocalTime) obj).writeExternal(dataOutput);
                    return;
                case (byte) 6:
                    ((ZonedDateTime) obj).writeExternal(dataOutput);
                    return;
                case (byte) 7:
                    ((ZoneRegion) obj).writeExternal(dataOutput);
                    return;
                case (byte) 8:
                    ((ZoneOffset) obj).writeExternal(dataOutput);
                    return;
                default:
                    switch (b) {
                        case (byte) 66:
                            ((OffsetTime) obj).writeExternal(dataOutput);
                            return;
                        case (byte) 67:
                            ((Year) obj).writeExternal(dataOutput);
                            return;
                        case (byte) 68:
                            ((YearMonth) obj).writeExternal(dataOutput);
                            return;
                        case (byte) 69:
                            ((OffsetDateTime) obj).writeExternal(dataOutput);
                            return;
                        default:
                            throw new InvalidClassException("Unknown serialized type");
                    }
            }
        }
        ((MonthDay) obj).writeExternal(dataOutput);
    }

    public void readExternal(ObjectInput objectInput) throws IOException {
        this.type = objectInput.readByte();
        this.object = readInternal(this.type, objectInput);
    }

    static Object read(DataInput dataInput) throws IOException {
        return readInternal(dataInput.readByte(), dataInput);
    }

    private static Object readInternal(byte b, DataInput dataInput) throws IOException {
        if (b == (byte) 64) {
            return MonthDay.readExternal(dataInput);
        }
        switch (b) {
            case (byte) 1:
                return Duration.readExternal(dataInput);
            case (byte) 2:
                return Instant.readExternal(dataInput);
            case (byte) 3:
                return LocalDate.readExternal(dataInput);
            case (byte) 4:
                return LocalDateTime.readExternal(dataInput);
            case (byte) 5:
                return LocalTime.readExternal(dataInput);
            case (byte) 6:
                return ZonedDateTime.readExternal(dataInput);
            case (byte) 7:
                return ZoneRegion.readExternal(dataInput);
            case (byte) 8:
                return ZoneOffset.readExternal(dataInput);
            default:
                switch (b) {
                    case (byte) 66:
                        return OffsetTime.readExternal(dataInput);
                    case (byte) 67:
                        return Year.readExternal(dataInput);
                    case (byte) 68:
                        return YearMonth.readExternal(dataInput);
                    case (byte) 69:
                        return OffsetDateTime.readExternal(dataInput);
                    default:
                        throw new StreamCorruptedException("Unknown serialized type");
                }
        }
    }

    private Object readResolve() {
        return this.object;
    }
}
