package okhttp3;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public final class CertificatePinner$Builder {
    private final List<CertificatePinner$Pin> pins = new ArrayList();

    public CertificatePinner$Builder add(String str, String... strArr) {
        if (str == null) {
            throw new NullPointerException("pattern == null");
        }
        for (String certificatePinner$Pin : strArr) {
            this.pins.add(new CertificatePinner$Pin(str, certificatePinner$Pin));
        }
        return this;
    }

    public CertificatePinner build() {
        return new CertificatePinner(new LinkedHashSet(this.pins), null);
    }
}
