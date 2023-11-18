package org.threeten.bp.chrono;

import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StreamCorruptedException;

final class Ser implements Externalizable {
    static final byte CHRONO_LOCALDATETIME_TYPE = (byte) 12;
    static final byte CHRONO_TYPE = (byte) 11;
    static final byte CHRONO_ZONEDDATETIME_TYPE = (byte) 13;
    static final byte HIJRAH_DATE_TYPE = (byte) 3;
    static final byte HIJRAH_ERA_TYPE = (byte) 4;
    static final byte JAPANESE_DATE_TYPE = (byte) 1;
    static final byte JAPANESE_ERA_TYPE = (byte) 2;
    static final byte MINGUO_DATE_TYPE = (byte) 5;
    static final byte MINGUO_ERA_TYPE = (byte) 6;
    static final byte THAIBUDDHIST_DATE_TYPE = (byte) 7;
    static final byte THAIBUDDHIST_ERA_TYPE = (byte) 8;
    private static final long serialVersionUID = 7857518227608961174L;
    private Object object;
    private byte type;

    Ser(byte b, Object obj) {
        this.type = b;
        this.object = obj;
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        writeInternal(this.type, this.object, objectOutput);
    }

    private static void writeInternal(byte b, Object obj, ObjectOutput objectOutput) throws IOException {
        objectOutput.writeByte(b);
        switch (b) {
            case (byte) 1:
                ((JapaneseDate) obj).writeExternal(objectOutput);
                return;
            case (byte) 2:
                ((JapaneseEra) obj).writeExternal(objectOutput);
                return;
            case (byte) 3:
                ((HijrahDate) obj).writeExternal(objectOutput);
                return;
            case (byte) 4:
                ((HijrahEra) obj).writeExternal(objectOutput);
                return;
            case (byte) 5:
                ((MinguoDate) obj).writeExternal(objectOutput);
                return;
            case (byte) 6:
                ((MinguoEra) obj).writeExternal(objectOutput);
                return;
            case (byte) 7:
                ((ThaiBuddhistDate) obj).writeExternal(objectOutput);
                return;
            case (byte) 8:
                ((ThaiBuddhistEra) obj).writeExternal(objectOutput);
                return;
            case (byte) 11:
                ((Chronology) obj).writeExternal(objectOutput);
                return;
            case (byte) 12:
                ((ChronoLocalDateTimeImpl) obj).writeExternal(objectOutput);
                return;
            case (byte) 13:
                ((ChronoZonedDateTimeImpl) obj).writeExternal(objectOutput);
                return;
            default:
                throw new InvalidClassException("Unknown serialized type");
        }
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.type = objectInput.readByte();
        this.object = readInternal(this.type, objectInput);
    }

    static Object read(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return readInternal(objectInput.readByte(), objectInput);
    }

    private static Object readInternal(byte b, ObjectInput objectInput) throws IOException, ClassNotFoundException {
        switch (b) {
            case (byte) 1:
                return JapaneseDate.readExternal(objectInput);
            case (byte) 2:
                return JapaneseEra.readExternal(objectInput);
            case (byte) 3:
                return HijrahDate.readExternal(objectInput);
            case (byte) 4:
                return HijrahEra.readExternal(objectInput);
            case (byte) 5:
                return MinguoDate.readExternal(objectInput);
            case (byte) 6:
                return MinguoEra.readExternal(objectInput);
            case (byte) 7:
                return ThaiBuddhistDate.readExternal(objectInput);
            case (byte) 8:
                return ThaiBuddhistEra.readExternal(objectInput);
            case (byte) 11:
                return Chronology.readExternal(objectInput);
            case (byte) 12:
                return ChronoLocalDateTimeImpl.readExternal(objectInput);
            case (byte) 13:
                return ChronoZonedDateTimeImpl.readExternal(objectInput);
            default:
                throw new StreamCorruptedException("Unknown serialized type");
        }
    }

    private Object readResolve() {
        return this.object;
    }
}
