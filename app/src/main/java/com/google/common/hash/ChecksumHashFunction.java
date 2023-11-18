package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.zip.Checksum;

final class ChecksumHashFunction extends AbstractStreamingHashFunction implements Serializable {
    private static final long serialVersionUID = 0;
    private final int bits;
    private final Supplier<? extends Checksum> checksumSupplier;
    private final String toString;

    private final class ChecksumHasher extends AbstractByteHasher {
        private final Checksum checksum;

        private ChecksumHasher(Checksum checksum) {
            this.checksum = (Checksum) Preconditions.checkNotNull(checksum);
        }

        protected void update(byte b) {
            this.checksum.update(b);
        }

        protected void update(byte[] bArr, int i, int i2) {
            this.checksum.update(bArr, i, i2);
        }

        public HashCode hash() {
            long value = this.checksum.getValue();
            if (ChecksumHashFunction.this.bits == 32) {
                return HashCode.fromInt((int) value);
            }
            return HashCode.fromLong(value);
        }
    }

    ChecksumHashFunction(Supplier<? extends Checksum> supplier, int i, String str) {
        boolean z;
        this.checksumSupplier = (Supplier) Preconditions.checkNotNull(supplier);
        if (i != 32) {
            if (i != 64) {
                z = false;
                Preconditions.checkArgument(z, "bits (%s) must be either 32 or 64", new Object[]{Integer.valueOf(i)});
                this.bits = i;
                this.toString = (String) Preconditions.checkNotNull(str);
            }
        }
        z = true;
        Preconditions.checkArgument(z, "bits (%s) must be either 32 or 64", new Object[]{Integer.valueOf(i)});
        this.bits = i;
        this.toString = (String) Preconditions.checkNotNull(str);
    }

    public int bits() {
        return this.bits;
    }

    public Hasher newHasher() {
        return new ChecksumHasher((Checksum) this.checksumSupplier.get());
    }

    public String toString() {
        return this.toString;
    }
}
