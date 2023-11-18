package okhttp3;

import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;

public final class FormBody extends RequestBody {
    private static final MediaType CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded");
    private final List<String> encodedNames;
    private final List<String> encodedValues;

    FormBody(List<String> list, List<String> list2) {
        this.encodedNames = Util.immutableList(list);
        this.encodedValues = Util.immutableList(list2);
    }

    public int size() {
        return this.encodedNames.size();
    }

    public String encodedName(int i) {
        return (String) this.encodedNames.get(i);
    }

    public String name(int i) {
        return HttpUrl.percentDecode(encodedName(i), true);
    }

    public String encodedValue(int i) {
        return (String) this.encodedValues.get(i);
    }

    public String value(int i) {
        return HttpUrl.percentDecode(encodedValue(i), true);
    }

    public MediaType contentType() {
        return CONTENT_TYPE;
    }

    public long contentLength() {
        return writeOrCountBytes(null, true);
    }

    public void writeTo(BufferedSink bufferedSink) throws IOException {
        writeOrCountBytes(bufferedSink, false);
    }

    private long writeOrCountBytes(@Nullable BufferedSink bufferedSink, boolean z) {
        if (z) {
            bufferedSink = new Buffer();
        } else {
            bufferedSink = bufferedSink.buffer();
        }
        int size = this.encodedNames.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                bufferedSink.writeByte(38);
            }
            bufferedSink.writeUtf8((String) this.encodedNames.get(i));
            bufferedSink.writeByte(61);
            bufferedSink.writeUtf8((String) this.encodedValues.get(i));
        }
        if (!z) {
            return 0;
        }
        long size2 = bufferedSink.size();
        bufferedSink.clear();
        return size2;
    }
}
