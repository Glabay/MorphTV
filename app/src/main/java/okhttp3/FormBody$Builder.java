package okhttp3;

import java.util.ArrayList;
import java.util.List;

public final class FormBody$Builder {
    private final List<String> names = new ArrayList();
    private final List<String> values = new ArrayList();

    public FormBody$Builder add(String str, String str2) {
        this.names.add(HttpUrl.canonicalize(str, " \"':;<=>@[]^`{}|/\\?#&!$(),~", false, false, true, true));
        this.values.add(HttpUrl.canonicalize(str2, " \"':;<=>@[]^`{}|/\\?#&!$(),~", false, false, true, true));
        return this;
    }

    public FormBody$Builder addEncoded(String str, String str2) {
        this.names.add(HttpUrl.canonicalize(str, " \"':;<=>@[]^`{}|/\\?#&!$(),~", true, false, true, true));
        this.values.add(HttpUrl.canonicalize(str2, " \"':;<=>@[]^`{}|/\\?#&!$(),~", true, false, true, true));
        return this;
    }

    public FormBody build() {
        return new FormBody(this.names, this.values);
    }
}
