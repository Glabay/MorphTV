package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.libtorrent;

public final class Ed25519 {
    public static final int PUBLIC_KEY_SIZE = 32;
    public static final int SCALAR_SIZE = 32;
    public static final int SECRET_KEY_SIZE = 64;
    public static final int SEED_SIZE = 32;
    public static final int SHARED_SECRET_SIZE = 32;
    public static final int SIGNATURE_SIZE = 64;

    private Ed25519() {
    }

    public static byte[] createSeed() {
        return Vectors.byte_vector2bytes(libtorrent.ed25519_create_seed());
    }

    public static Pair<byte[], byte[]> createKeypair(byte[] bArr) {
        if (bArr != null) {
            if (bArr.length == 32) {
                bArr = libtorrent.ed25519_create_keypair(Vectors.bytes2byte_vector(bArr));
                return new Pair(Vectors.byte_vector2bytes(bArr.getFirst()), Vectors.byte_vector2bytes(bArr.getSecond()));
            }
        }
        throw new IllegalArgumentException("seed must be not null and of size 32");
    }

    public static byte[] sign(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        if (bArr2 != null) {
            if (bArr2.length == 32) {
                if (bArr3 != null) {
                    if (bArr3.length == 64) {
                        return Vectors.byte_vector2bytes(libtorrent.ed25519_sign(Vectors.bytes2byte_vector(bArr), Vectors.bytes2byte_vector(bArr2), Vectors.bytes2byte_vector(bArr3)));
                    }
                }
                throw new IllegalArgumentException("secret key must be not null and of size 64");
            }
        }
        throw new IllegalArgumentException("public key must be not null and of size 32");
    }

    public static boolean verify(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        if (bArr != null) {
            if (bArr.length == 64) {
                if (bArr3 != null) {
                    if (bArr3.length == 32) {
                        return libtorrent.ed25519_verify(Vectors.bytes2byte_vector(bArr), Vectors.bytes2byte_vector(bArr2), Vectors.bytes2byte_vector(bArr3));
                    }
                }
                throw new IllegalArgumentException("public key must be not null and of size 32");
            }
        }
        throw new IllegalArgumentException("signature must be not null and of size 64");
    }

    public static byte[] addScalarPublic(byte[] bArr, byte[] bArr2) {
        if (bArr != null) {
            if (bArr.length == 32) {
                if (bArr2 != null) {
                    if (bArr2.length == 32) {
                        return Vectors.byte_vector2bytes(libtorrent.ed25519_add_scalar_public(Vectors.bytes2byte_vector(bArr), Vectors.bytes2byte_vector(bArr2)));
                    }
                }
                throw new IllegalArgumentException("scalar must be not null and of size 32");
            }
        }
        throw new IllegalArgumentException("public key must be not null and of size 32");
    }

    public static byte[] addScalarSecret(byte[] bArr, byte[] bArr2) {
        if (bArr != null) {
            if (bArr.length == 64) {
                if (bArr2 != null) {
                    if (bArr2.length == 32) {
                        return Vectors.byte_vector2bytes(libtorrent.ed25519_add_scalar_secret(Vectors.bytes2byte_vector(bArr), Vectors.bytes2byte_vector(bArr2)));
                    }
                }
                throw new IllegalArgumentException("scalar must be not null and of size 32");
            }
        }
        throw new IllegalArgumentException("public key must be not null and of size 64");
    }

    public byte[] keyExchange(byte[] bArr, byte[] bArr2) {
        if (bArr != null) {
            if (bArr.length == 32) {
                if (bArr2 != null) {
                    if (bArr2.length == 64) {
                        return Vectors.byte_vector2bytes(libtorrent.ed25519_key_exchange(Vectors.bytes2byte_vector(bArr), Vectors.bytes2byte_vector(bArr2)));
                    }
                }
                throw new IllegalArgumentException("private key must be not null and of size 64");
            }
        }
        throw new IllegalArgumentException("public key must be not null and of size 32");
    }
}
