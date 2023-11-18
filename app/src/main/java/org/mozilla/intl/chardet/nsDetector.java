package org.mozilla.intl.chardet;

public class nsDetector extends nsPSMDetector implements nsICharsetDetector {
    nsICharsetDetectionObserver mObserver = null;

    public nsDetector(int i) {
        super(i);
    }

    public boolean DoIt(byte[] bArr, int i, boolean z) {
        if (bArr != null) {
            if (!z) {
                HandleData(bArr, i);
                return this.mDone;
            }
        }
        return false;
    }

    public void Done() {
        DataEnd();
    }

    public void Init(nsICharsetDetectionObserver nsicharsetdetectionobserver) {
        this.mObserver = nsicharsetdetectionobserver;
    }

    public void Report(String str) {
        if (this.mObserver != null) {
            this.mObserver.Notify(str);
        }
    }

    public boolean isAscii(byte[] bArr, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            if ((bArr[i2] & 128) != 0) {
                return false;
            }
        }
        return true;
    }
}
