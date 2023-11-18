package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.io.input.ObservableInputStream.Observer;

public class MessageDigestCalculatingInputStream extends ObservableInputStream {
    private final MessageDigest messageDigest;

    public static class MessageDigestMaintainingObserver extends Observer {
        private final MessageDigest md;

        public MessageDigestMaintainingObserver(MessageDigest messageDigest) {
            this.md = messageDigest;
        }

        void data(int i) throws IOException {
            this.md.update((byte) i);
        }

        void data(byte[] bArr, int i, int i2) throws IOException {
            this.md.update(bArr, i, i2);
        }
    }

    public MessageDigestCalculatingInputStream(InputStream inputStream, MessageDigest messageDigest) {
        super(inputStream);
        this.messageDigest = messageDigest;
        add(new MessageDigestMaintainingObserver(messageDigest));
    }

    public MessageDigestCalculatingInputStream(InputStream inputStream, String str) throws NoSuchAlgorithmException {
        this(inputStream, MessageDigest.getInstance(str));
    }

    public MessageDigestCalculatingInputStream(InputStream inputStream) throws NoSuchAlgorithmException {
        this(inputStream, MessageDigest.getInstance("MD5"));
    }

    public MessageDigest getMessageDigest() {
        return this.messageDigest;
    }
}
