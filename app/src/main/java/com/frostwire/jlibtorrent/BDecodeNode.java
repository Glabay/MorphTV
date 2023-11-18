package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.bdecode_node;
import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.error_code;

public final class BDecodeNode {
    private final byte_vector buffer;
    /* renamed from: n */
    private final bdecode_node f18n;

    public BDecodeNode(bdecode_node bdecode_node) {
        this(bdecode_node, null);
    }

    public BDecodeNode(bdecode_node bdecode_node, byte_vector byte_vector) {
        this.f18n = bdecode_node;
        this.buffer = byte_vector;
    }

    public bdecode_node swig() {
        return this.f18n;
    }

    public byte_vector buffer() {
        return this.buffer;
    }

    public String toString() {
        return bdecode_node.to_string(this.f18n, false, 2);
    }

    public static BDecodeNode bdecode(byte[] bArr) {
        bArr = Vectors.bytes2byte_vector(bArr);
        bdecode_node bdecode_node = new bdecode_node();
        error_code error_code = new error_code();
        if (bdecode_node.bdecode(bArr, bdecode_node, error_code) == 0) {
            return new BDecodeNode(bdecode_node, bArr);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Can't decode data: ");
        stringBuilder.append(error_code.message());
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}
