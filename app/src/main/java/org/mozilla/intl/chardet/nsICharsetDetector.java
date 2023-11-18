package org.mozilla.intl.chardet;

public interface nsICharsetDetector {
    boolean DoIt(byte[] bArr, int i, boolean z);

    void Done();

    void Init(nsICharsetDetectionObserver nsicharsetdetectionobserver);
}
