package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;

public abstract class ByteSource {
    private static final int BUF_SIZE = 4096;
    private static final byte[] countBuffer = new byte[4096];

    private final class AsCharSource extends CharSource {
        private final Charset charset;

        private AsCharSource(Charset charset) {
            this.charset = (Charset) Preconditions.checkNotNull(charset);
        }

        public Reader openStream() throws IOException {
            return new InputStreamReader(ByteSource.this.openStream(), this.charset);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(ByteSource.this.toString());
            stringBuilder.append(".asCharSource(");
            stringBuilder.append(this.charset);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private static class ByteArrayByteSource extends ByteSource {
        protected final byte[] bytes;

        protected ByteArrayByteSource(byte[] bArr) {
            this.bytes = (byte[]) Preconditions.checkNotNull(bArr);
        }

        public InputStream openStream() {
            return new ByteArrayInputStream(this.bytes);
        }

        public InputStream openBufferedStream() throws IOException {
            return openStream();
        }

        public boolean isEmpty() {
            return this.bytes.length == 0;
        }

        public long size() {
            return (long) this.bytes.length;
        }

        public byte[] read() {
            return (byte[]) this.bytes.clone();
        }

        public long copyTo(OutputStream outputStream) throws IOException {
            outputStream.write(this.bytes);
            return (long) this.bytes.length;
        }

        public <T> T read(ByteProcessor<T> byteProcessor) throws IOException {
            byteProcessor.processBytes(this.bytes, 0, this.bytes.length);
            return byteProcessor.getResult();
        }

        public HashCode hash(HashFunction hashFunction) throws IOException {
            return hashFunction.hashBytes(this.bytes);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ByteSource.wrap(");
            stringBuilder.append(truncate(BaseEncoding.base16().encode(this.bytes), 30, "..."));
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        private static String truncate(CharSequence charSequence, int i, String str) {
            Preconditions.checkNotNull(charSequence);
            int length = i - str.length();
            Preconditions.checkArgument(length >= 0, "maxLength (%s) must be >= length of the truncation indicator (%s)", Integer.valueOf(i), Integer.valueOf(str.length()));
            if (charSequence.length() <= i) {
                charSequence = charSequence.toString();
                if (charSequence.length() <= i) {
                    return charSequence;
                }
            }
            StringBuilder stringBuilder = new StringBuilder(i);
            stringBuilder.append(charSequence, 0, length);
            stringBuilder.append(str);
            return stringBuilder.toString();
        }
    }

    private static final class ConcatenatedByteSource extends ByteSource {
        private final Iterable<? extends ByteSource> sources;

        ConcatenatedByteSource(Iterable<? extends ByteSource> iterable) {
            this.sources = (Iterable) Preconditions.checkNotNull(iterable);
        }

        public InputStream openStream() throws IOException {
            return new MultiInputStream(this.sources.iterator());
        }

        public boolean isEmpty() throws IOException {
            for (ByteSource isEmpty : this.sources) {
                if (!isEmpty.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        public long size() throws IOException {
            long j = 0;
            for (ByteSource size : this.sources) {
                j += size.size();
            }
            return j;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ByteSource.concat(");
            stringBuilder.append(this.sources);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private static final class EmptyByteSource extends ByteArrayByteSource {
        private static final EmptyByteSource INSTANCE = new EmptyByteSource();

        public String toString() {
            return "ByteSource.empty()";
        }

        private EmptyByteSource() {
            super(new byte[0]);
        }

        public CharSource asCharSource(Charset charset) {
            Preconditions.checkNotNull(charset);
            return CharSource.empty();
        }

        public byte[] read() {
            return this.bytes;
        }
    }

    private final class SlicedByteSource extends ByteSource {
        private final long length;
        private final long offset;

        private SlicedByteSource(long j, long j2) {
            Preconditions.checkArgument(j >= 0 ? true : null, "offset (%s) may not be negative", Long.valueOf(j));
            Preconditions.checkArgument(j2 >= 0 ? true : null, "length (%s) may not be negative", Long.valueOf(j2));
            this.offset = j;
            this.length = j2;
        }

        public InputStream openStream() throws IOException {
            return sliceStream(ByteSource.this.openStream());
        }

        public InputStream openBufferedStream() throws IOException {
            return sliceStream(ByteSource.this.openBufferedStream());
        }

        private InputStream sliceStream(InputStream inputStream) throws IOException {
            Closer create;
            if (this.offset > 0) {
                try {
                    ByteStreams.skipFully(inputStream, this.offset);
                } catch (Throwable th) {
                    create.close();
                }
            }
            return ByteStreams.limit(inputStream, this.length);
        }

        public ByteSource slice(long j, long j2) {
            Preconditions.checkArgument(j >= 0, "offset (%s) may not be negative", Long.valueOf(j));
            Preconditions.checkArgument(j2 >= 0, "length (%s) may not be negative", Long.valueOf(j2));
            return ByteSource.this.slice(this.offset + j, Math.min(j2, this.length - j));
        }

        public boolean isEmpty() throws IOException {
            if (this.length != 0) {
                if (!super.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(ByteSource.this.toString());
            stringBuilder.append(".slice(");
            stringBuilder.append(this.offset);
            stringBuilder.append(", ");
            stringBuilder.append(this.length);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    public abstract InputStream openStream() throws IOException;

    protected ByteSource() {
    }

    public CharSource asCharSource(Charset charset) {
        return new AsCharSource(charset);
    }

    public InputStream openBufferedStream() throws IOException {
        InputStream openStream = openStream();
        return openStream instanceof BufferedInputStream ? (BufferedInputStream) openStream : new BufferedInputStream(openStream);
    }

    public ByteSource slice(long j, long j2) {
        return new SlicedByteSource(j, j2);
    }

    public boolean isEmpty() throws IOException {
        Closer create = Closer.create();
        try {
            boolean z = ((InputStream) create.register(openStream())).read() == -1;
            create.close();
            return z;
        } catch (Throwable th) {
            create.close();
        }
    }

    public long size() throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r3 = this;
        r0 = com.google.common.io.Closer.create();
        r1 = r3.openStream();	 Catch:{ IOException -> 0x001b, all -> 0x0016 }
        r1 = r0.register(r1);	 Catch:{ IOException -> 0x001b, all -> 0x0016 }
        r1 = (java.io.InputStream) r1;	 Catch:{ IOException -> 0x001b, all -> 0x0016 }
        r1 = r3.countBySkipping(r1);	 Catch:{ IOException -> 0x001b, all -> 0x0016 }
        r0.close();
        return r1;
    L_0x0016:
        r1 = move-exception;
        r0.close();
        throw r1;
    L_0x001b:
        r0.close();
        r0 = com.google.common.io.Closer.create();
        r1 = r3.openStream();	 Catch:{ Throwable -> 0x0036 }
        r1 = r0.register(r1);	 Catch:{ Throwable -> 0x0036 }
        r1 = (java.io.InputStream) r1;	 Catch:{ Throwable -> 0x0036 }
        r1 = r3.countByReading(r1);	 Catch:{ Throwable -> 0x0036 }
        r0.close();
        return r1;
    L_0x0034:
        r1 = move-exception;
        goto L_0x003c;
    L_0x0036:
        r1 = move-exception;
        r1 = r0.rethrow(r1);	 Catch:{ all -> 0x0034 }
        throw r1;	 Catch:{ all -> 0x0034 }
    L_0x003c:
        r0.close();
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.io.ByteSource.size():long");
    }

    private long countBySkipping(InputStream inputStream) throws IOException {
        long j = 0;
        while (true) {
            long j2;
            long skip = inputStream.skip((long) Math.min(inputStream.available(), Integer.MAX_VALUE));
            if (skip > 0) {
                j2 = j + skip;
            } else if (inputStream.read() == -1) {
                return j;
            } else {
                if (j == 0 && inputStream.available() == 0) {
                    throw new IOException();
                }
                j2 = j + 1;
            }
            j = j2;
        }
    }

    private long countByReading(InputStream inputStream) throws IOException {
        long j = 0;
        while (true) {
            long read = (long) inputStream.read(countBuffer);
            if (read == -1) {
                return j;
            }
            j += read;
        }
    }

    public long copyTo(OutputStream outputStream) throws IOException {
        Preconditions.checkNotNull(outputStream);
        Closer create = Closer.create();
        try {
            long copy = ByteStreams.copy((InputStream) create.register(openStream()), outputStream);
            create.close();
            return copy;
        } catch (Throwable th) {
            create.close();
        }
    }

    public long copyTo(ByteSink byteSink) throws IOException {
        Preconditions.checkNotNull(byteSink);
        Closer create = Closer.create();
        try {
            long copy = ByteStreams.copy((InputStream) create.register(openStream()), (OutputStream) create.register(byteSink.openStream()));
            create.close();
            return copy;
        } catch (Throwable th) {
            create.close();
        }
    }

    public byte[] read() throws IOException {
        Closer create = Closer.create();
        try {
            byte[] toByteArray = ByteStreams.toByteArray((InputStream) create.register(openStream()));
            create.close();
            return toByteArray;
        } catch (Throwable th) {
            create.close();
        }
    }

    @Beta
    public <T> T read(ByteProcessor<T> byteProcessor) throws IOException {
        Preconditions.checkNotNull(byteProcessor);
        Closer create = Closer.create();
        try {
            byteProcessor = ByteStreams.readBytes((InputStream) create.register(openStream()), byteProcessor);
            create.close();
            return byteProcessor;
        } catch (Throwable th) {
            create.close();
        }
    }

    public HashCode hash(HashFunction hashFunction) throws IOException {
        hashFunction = hashFunction.newHasher();
        copyTo(Funnels.asOutputStream(hashFunction));
        return hashFunction.hash();
    }

    public boolean contentEquals(ByteSource byteSource) throws IOException {
        Preconditions.checkNotNull(byteSource);
        byte[] bArr = new byte[4096];
        byte[] bArr2 = new byte[4096];
        Closer create = Closer.create();
        try {
            InputStream inputStream = (InputStream) create.register(openStream());
            InputStream inputStream2 = (InputStream) create.register(byteSource.openStream());
            int read;
            do {
                read = ByteStreams.read(inputStream, bArr, 0, 4096);
                if (read == ByteStreams.read(inputStream2, bArr2, 0, 4096)) {
                    if (!Arrays.equals(bArr, bArr2)) {
                    }
                }
                create.close();
                return false;
            } while (read == 4096);
            create.close();
            return true;
        } catch (Throwable th) {
            create.close();
        }
    }

    public static ByteSource concat(Iterable<? extends ByteSource> iterable) {
        return new ConcatenatedByteSource(iterable);
    }

    public static ByteSource concat(Iterator<? extends ByteSource> it) {
        return concat(ImmutableList.copyOf((Iterator) it));
    }

    public static ByteSource concat(ByteSource... byteSourceArr) {
        return concat(ImmutableList.copyOf((Object[]) byteSourceArr));
    }

    public static ByteSource wrap(byte[] bArr) {
        return new ByteArrayByteSource(bArr);
    }

    public static ByteSource empty() {
        return EmptyByteSource.INSTANCE;
    }
}
