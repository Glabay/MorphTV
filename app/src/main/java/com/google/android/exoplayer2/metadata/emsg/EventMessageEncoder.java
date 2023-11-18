package com.google.android.exoplayer2.metadata.emsg;

import android.support.annotation.Nullable;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class EventMessageEncoder {
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(512);
    private final DataOutputStream dataOutputStream = new DataOutputStream(this.byteArrayOutputStream);

    @Nullable
    public byte[] encode(EventMessage eventMessage, long j) {
        Assertions.checkArgument(j >= 0);
        this.byteArrayOutputStream.reset();
        try {
            writeNullTerminatedString(this.dataOutputStream, eventMessage.schemeIdUri);
            writeNullTerminatedString(this.dataOutputStream, eventMessage.value != null ? eventMessage.value : "");
            writeUnsignedInt(this.dataOutputStream, j);
            writeUnsignedInt(this.dataOutputStream, Util.scaleLargeTimestamp(eventMessage.presentationTimeUs, j, C0649C.MICROS_PER_SECOND));
            writeUnsignedInt(this.dataOutputStream, Util.scaleLargeTimestamp(eventMessage.durationMs, j, 1000));
            writeUnsignedInt(this.dataOutputStream, eventMessage.id);
            this.dataOutputStream.write(eventMessage.messageData);
            this.dataOutputStream.flush();
            return this.byteArrayOutputStream.toByteArray();
        } catch (EventMessage eventMessage2) {
            throw new RuntimeException(eventMessage2);
        }
    }

    private static void writeNullTerminatedString(DataOutputStream dataOutputStream, String str) throws IOException {
        dataOutputStream.writeBytes(str);
        dataOutputStream.writeByte(null);
    }

    private static void writeUnsignedInt(DataOutputStream dataOutputStream, long j) throws IOException {
        dataOutputStream.writeByte(((int) (j >>> 24)) & 255);
        dataOutputStream.writeByte(((int) (j >>> 16)) & 255);
        dataOutputStream.writeByte(((int) (j >>> 8)) & 255);
        dataOutputStream.writeByte(((int) j) & 255);
    }
}
