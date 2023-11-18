package org.apache.commons.io;

import java.nio.ByteOrder;

public final class ByteOrderParser {
    private ByteOrderParser() {
    }

    public static ByteOrder parseByteOrder(String str) {
        if (ByteOrder.BIG_ENDIAN.toString().equals(str)) {
            return ByteOrder.BIG_ENDIAN;
        }
        if (ByteOrder.LITTLE_ENDIAN.toString().equals(str)) {
            return ByteOrder.LITTLE_ENDIAN;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported byte order setting: ");
        stringBuilder.append(str);
        stringBuilder.append(", expeced one of ");
        stringBuilder.append(ByteOrder.LITTLE_ENDIAN);
        stringBuilder.append(", ");
        stringBuilder.append(ByteOrder.BIG_ENDIAN);
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}
