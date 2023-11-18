package com.google.android.exoplayer2.extractor.mp4;

import android.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.nio.ByteBuffer;
import java.util.UUID;

public final class PsshAtomUtil {
    private static final String TAG = "PsshAtomUtil";

    private static class PsshAtom {
        private final byte[] schemeData;
        private final UUID uuid;
        private final int version;

        public PsshAtom(UUID uuid, int i, byte[] bArr) {
            this.uuid = uuid;
            this.version = i;
            this.schemeData = bArr;
        }
    }

    private PsshAtomUtil() {
    }

    public static byte[] buildPsshAtom(UUID uuid, byte[] bArr) {
        return buildPsshAtom(uuid, null, bArr);
    }

    public static byte[] buildPsshAtom(UUID uuid, UUID[] uuidArr, byte[] bArr) {
        Object obj = uuidArr != null ? 1 : null;
        int length = bArr != null ? bArr.length : 0;
        int i = length + 32;
        if (obj != null) {
            i += (uuidArr.length * 16) + 4;
        }
        ByteBuffer allocate = ByteBuffer.allocate(i);
        allocate.putInt(i);
        allocate.putInt(Atom.TYPE_pssh);
        allocate.putInt(obj != null ? 16777216 : 0);
        allocate.putLong(uuid.getMostSignificantBits());
        allocate.putLong(uuid.getLeastSignificantBits());
        if (obj != null) {
            allocate.putInt(uuidArr.length);
            for (UUID uuid2 : uuidArr) {
                allocate.putLong(uuid2.getMostSignificantBits());
                allocate.putLong(uuid2.getLeastSignificantBits());
            }
        }
        if (length != 0) {
            allocate.putInt(bArr.length);
            allocate.put(bArr);
        }
        return allocate.array();
    }

    public static UUID parseUuid(byte[] bArr) {
        bArr = parsePsshAtom(bArr);
        if (bArr == null) {
            return null;
        }
        return bArr.uuid;
    }

    public static int parseVersion(byte[] bArr) {
        bArr = parsePsshAtom(bArr);
        if (bArr == null) {
            return -1;
        }
        return bArr.version;
    }

    public static byte[] parseSchemeSpecificData(byte[] bArr, UUID uuid) {
        bArr = parsePsshAtom(bArr);
        if (bArr == null) {
            return null;
        }
        if (uuid == null || uuid.equals(bArr.uuid)) {
            return bArr.schemeData;
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UUID mismatch. Expected: ");
        stringBuilder.append(uuid);
        stringBuilder.append(", got: ");
        stringBuilder.append(bArr.uuid);
        stringBuilder.append(".");
        Log.w(str, stringBuilder.toString());
        return null;
    }

    private static PsshAtom parsePsshAtom(byte[] bArr) {
        ParsableByteArray parsableByteArray = new ParsableByteArray(bArr);
        if (parsableByteArray.limit() < 32) {
            return null;
        }
        parsableByteArray.setPosition(0);
        if (parsableByteArray.readInt() != parsableByteArray.bytesLeft() + 4 || parsableByteArray.readInt() != Atom.TYPE_pssh) {
            return null;
        }
        int parseFullAtomVersion = Atom.parseFullAtomVersion(parsableByteArray.readInt());
        if (parseFullAtomVersion > 1) {
            bArr = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported pssh version: ");
            stringBuilder.append(parseFullAtomVersion);
            Log.w(bArr, stringBuilder.toString());
            return null;
        }
        UUID uuid = new UUID(parsableByteArray.readLong(), parsableByteArray.readLong());
        if (parseFullAtomVersion == 1) {
            parsableByteArray.skipBytes(parsableByteArray.readUnsignedIntToInt() * 16);
        }
        int readUnsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
        if (readUnsignedIntToInt != parsableByteArray.bytesLeft()) {
            return null;
        }
        byte[] bArr2 = new byte[readUnsignedIntToInt];
        parsableByteArray.readBytes(bArr2, 0, readUnsignedIntToInt);
        return new PsshAtom(uuid, parseFullAtomVersion, bArr2);
    }
}
