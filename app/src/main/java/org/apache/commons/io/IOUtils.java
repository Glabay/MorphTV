package org.apache.commons.io;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.output.StringBuilderWriter;

public class IOUtils {
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    public static final char DIR_SEPARATOR = File.separatorChar;
    public static final char DIR_SEPARATOR_UNIX = '/';
    public static final char DIR_SEPARATOR_WINDOWS = '\\';
    public static final int EOF = -1;
    public static final String LINE_SEPARATOR;
    public static final String LINE_SEPARATOR_UNIX = "\n";
    public static final String LINE_SEPARATOR_WINDOWS = "\r\n";
    private static final int SKIP_BUFFER_SIZE = 2048;
    private static byte[] SKIP_BYTE_BUFFER;
    private static char[] SKIP_CHAR_BUFFER;

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static {
        /*
        r0 = java.io.File.separatorChar;
        DIR_SEPARATOR = r0;
        r0 = new org.apache.commons.io.output.StringBuilderWriter;
        r1 = 4;
        r0.<init>(r1);
        r1 = 0;
        r2 = new java.io.PrintWriter;	 Catch:{ Throwable -> 0x0040 }
        r2.<init>(r0);	 Catch:{ Throwable -> 0x0040 }
        r2.println();	 Catch:{ Throwable -> 0x0027, all -> 0x0024 }
        r3 = r0.toString();	 Catch:{ Throwable -> 0x0027, all -> 0x0024 }
        LINE_SEPARATOR = r3;	 Catch:{ Throwable -> 0x0027, all -> 0x0024 }
        if (r2 == 0) goto L_0x001e;
    L_0x001b:
        r2.close();	 Catch:{ Throwable -> 0x0040 }
    L_0x001e:
        if (r0 == 0) goto L_0x0023;
    L_0x0020:
        r0.close();
    L_0x0023:
        return;
    L_0x0024:
        r3 = move-exception;
        r4 = r1;
        goto L_0x002d;
    L_0x0027:
        r3 = move-exception;
        throw r3;	 Catch:{ all -> 0x0029 }
    L_0x0029:
        r4 = move-exception;
        r5 = r4;
        r4 = r3;
        r3 = r5;
    L_0x002d:
        if (r2 == 0) goto L_0x003d;
    L_0x002f:
        if (r4 == 0) goto L_0x003a;
    L_0x0031:
        r2.close();	 Catch:{ Throwable -> 0x0035 }
        goto L_0x003d;
    L_0x0035:
        r2 = move-exception;
        r4.addSuppressed(r2);	 Catch:{ Throwable -> 0x0040 }
        goto L_0x003d;
    L_0x003a:
        r2.close();	 Catch:{ Throwable -> 0x0040 }
    L_0x003d:
        throw r3;	 Catch:{ Throwable -> 0x0040 }
    L_0x003e:
        r2 = move-exception;
        goto L_0x0042;
    L_0x0040:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x003e }
    L_0x0042:
        if (r0 == 0) goto L_0x0052;
    L_0x0044:
        if (r1 == 0) goto L_0x004f;
    L_0x0046:
        r0.close();	 Catch:{ Throwable -> 0x004a }
        goto L_0x0052;
    L_0x004a:
        r0 = move-exception;
        r1.addSuppressed(r0);
        goto L_0x0052;
    L_0x004f:
        r0.close();
    L_0x0052:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.IOUtils.<clinit>():void");
    }

    public static void close(URLConnection uRLConnection) {
        if (uRLConnection instanceof HttpURLConnection) {
            ((HttpURLConnection) uRLConnection).disconnect();
        }
    }

    @Deprecated
    public static void closeQuietly(Reader reader) {
        closeQuietly((Closeable) reader);
    }

    @Deprecated
    public static void closeQuietly(Writer writer) {
        closeQuietly((Closeable) writer);
    }

    @Deprecated
    public static void closeQuietly(InputStream inputStream) {
        closeQuietly((Closeable) inputStream);
    }

    @Deprecated
    public static void closeQuietly(OutputStream outputStream) {
        closeQuietly((Closeable) outputStream);
    }

    @java.lang.Deprecated
    public static void closeQuietly(java.io.Closeable r0) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        if (r0 == 0) goto L_0x0005;
    L_0x0002:
        r0.close();	 Catch:{ IOException -> 0x0005 }
    L_0x0005:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.IOUtils.closeQuietly(java.io.Closeable):void");
    }

    @Deprecated
    public static void closeQuietly(Closeable... closeableArr) {
        if (closeableArr != null) {
            for (Closeable closeQuietly : closeableArr) {
                closeQuietly(closeQuietly);
            }
        }
    }

    @java.lang.Deprecated
    public static void closeQuietly(java.net.Socket r0) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        if (r0 == 0) goto L_0x0005;
    L_0x0002:
        r0.close();	 Catch:{ IOException -> 0x0005 }
    L_0x0005:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.IOUtils.closeQuietly(java.net.Socket):void");
    }

    @java.lang.Deprecated
    public static void closeQuietly(java.nio.channels.Selector r0) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        if (r0 == 0) goto L_0x0005;
    L_0x0002:
        r0.close();	 Catch:{ IOException -> 0x0005 }
    L_0x0005:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.IOUtils.closeQuietly(java.nio.channels.Selector):void");
    }

    @java.lang.Deprecated
    public static void closeQuietly(java.net.ServerSocket r0) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        if (r0 == 0) goto L_0x0005;
    L_0x0002:
        r0.close();	 Catch:{ IOException -> 0x0005 }
    L_0x0005:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.IOUtils.closeQuietly(java.net.ServerSocket):void");
    }

    public static InputStream toBufferedInputStream(InputStream inputStream) throws IOException {
        return ByteArrayOutputStream.toBufferedInputStream(inputStream);
    }

    public static InputStream toBufferedInputStream(InputStream inputStream, int i) throws IOException {
        return ByteArrayOutputStream.toBufferedInputStream(inputStream, i);
    }

    public static BufferedReader toBufferedReader(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static BufferedReader toBufferedReader(Reader reader, int i) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, i);
    }

    public static BufferedReader buffer(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static BufferedReader buffer(Reader reader, int i) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, i);
    }

    public static BufferedWriter buffer(Writer writer) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer);
    }

    public static BufferedWriter buffer(Writer writer, int i) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer, i);
    }

    public static BufferedOutputStream buffer(OutputStream outputStream) {
        if (outputStream != null) {
            return outputStream instanceof BufferedOutputStream ? (BufferedOutputStream) outputStream : new BufferedOutputStream(outputStream);
        } else {
            throw new NullPointerException();
        }
    }

    public static BufferedOutputStream buffer(OutputStream outputStream, int i) {
        if (outputStream != null) {
            return outputStream instanceof BufferedOutputStream ? (BufferedOutputStream) outputStream : new BufferedOutputStream(outputStream, i);
        } else {
            throw new NullPointerException();
        }
    }

    public static BufferedInputStream buffer(InputStream inputStream) {
        if (inputStream != null) {
            return inputStream instanceof BufferedInputStream ? (BufferedInputStream) inputStream : new BufferedInputStream(inputStream);
        } else {
            throw new NullPointerException();
        }
    }

    public static BufferedInputStream buffer(InputStream inputStream, int i) {
        if (inputStream != null) {
            return inputStream instanceof BufferedInputStream ? (BufferedInputStream) inputStream : new BufferedInputStream(inputStream, i);
        } else {
            throw new NullPointerException();
        }
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        InputStream inputStream2;
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            copy(inputStream, byteArrayOutputStream);
            inputStream = byteArrayOutputStream.toByteArray();
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            return inputStream;
        } catch (Throwable th) {
            inputStream2.addSuppressed(th);
        }
    }

    public static byte[] toByteArray(InputStream inputStream, long j) throws IOException {
        if (j <= 2147483647L) {
            return toByteArray(inputStream, (int) j);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Size cannot be greater than Integer max value: ");
        stringBuilder.append(j);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static byte[] toByteArray(InputStream inputStream, int i) throws IOException {
        if (i < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Size must be equal or greater than zero: ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        int i2 = 0;
        if (i == 0) {
            return new byte[0];
        }
        byte[] bArr = new byte[i];
        while (i2 < i) {
            int read = inputStream.read(bArr, i2, i - i2);
            if (read == -1) {
                break;
            }
            i2 += read;
        }
        if (i2 == i) {
            return bArr;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Unexpected read size. current: ");
        stringBuilder2.append(i2);
        stringBuilder2.append(", expected: ");
        stringBuilder2.append(i);
        throw new IOException(stringBuilder2.toString());
    }

    @Deprecated
    public static byte[] toByteArray(Reader reader) throws IOException {
        return toByteArray(reader, Charset.defaultCharset());
    }

    public static byte[] toByteArray(Reader reader, Charset charset) throws IOException {
        Reader reader2;
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            copy(reader, byteArrayOutputStream, charset);
            reader = byteArrayOutputStream.toByteArray();
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            return reader;
        } catch (Charset charset2) {
            reader2.addSuppressed(charset2);
        }
    }

    public static byte[] toByteArray(Reader reader, String str) throws IOException {
        return toByteArray(reader, Charsets.toCharset(str));
    }

    @Deprecated
    public static byte[] toByteArray(String str) throws IOException {
        return str.getBytes(Charset.defaultCharset());
    }

    public static byte[] toByteArray(URI uri) throws IOException {
        return toByteArray(uri.toURL());
    }

    public static byte[] toByteArray(URL url) throws IOException {
        URLConnection openConnection = url.openConnection();
        try {
            byte[] toByteArray = toByteArray(openConnection);
            return toByteArray;
        } finally {
            close(openConnection);
        }
    }

    public static byte[] toByteArray(URLConnection uRLConnection) throws IOException {
        Throwable th;
        InputStream inputStream = uRLConnection.getInputStream();
        try {
            byte[] toByteArray = toByteArray(inputStream);
            if (inputStream != null) {
                inputStream.close();
            }
            return toByteArray;
        } catch (Throwable th2) {
            th = th2;
        }
        if (inputStream != null) {
            if (r1 != null) {
                try {
                    inputStream.close();
                } catch (URLConnection uRLConnection2) {
                    r1.addSuppressed(uRLConnection2);
                }
            } else {
                inputStream.close();
            }
        }
        throw th;
        throw th;
    }

    @Deprecated
    public static char[] toCharArray(InputStream inputStream) throws IOException {
        return toCharArray(inputStream, Charset.defaultCharset());
    }

    public static char[] toCharArray(InputStream inputStream, Charset charset) throws IOException {
        Writer charArrayWriter = new CharArrayWriter();
        copy(inputStream, charArrayWriter, charset);
        return charArrayWriter.toCharArray();
    }

    public static char[] toCharArray(InputStream inputStream, String str) throws IOException {
        return toCharArray(inputStream, Charsets.toCharset(str));
    }

    public static char[] toCharArray(Reader reader) throws IOException {
        Writer charArrayWriter = new CharArrayWriter();
        copy(reader, charArrayWriter);
        return charArrayWriter.toCharArray();
    }

    @Deprecated
    public static String toString(InputStream inputStream) throws IOException {
        return toString(inputStream, Charset.defaultCharset());
    }

    public static String toString(InputStream inputStream, Charset charset) throws IOException {
        InputStream inputStream2;
        Writer stringBuilderWriter = new StringBuilderWriter();
        try {
            copy(inputStream, stringBuilderWriter, charset);
            inputStream = stringBuilderWriter.toString();
            if (stringBuilderWriter != null) {
                stringBuilderWriter.close();
            }
            return inputStream;
        } catch (Charset charset2) {
            inputStream2.addSuppressed(charset2);
        }
    }

    public static String toString(InputStream inputStream, String str) throws IOException {
        return toString(inputStream, Charsets.toCharset(str));
    }

    public static String toString(Reader reader) throws IOException {
        Reader reader2;
        Writer stringBuilderWriter = new StringBuilderWriter();
        try {
            copy(reader, stringBuilderWriter);
            reader = stringBuilderWriter.toString();
            if (stringBuilderWriter != null) {
                stringBuilderWriter.close();
            }
            return reader;
        } catch (Throwable th) {
            reader2.addSuppressed(th);
        }
    }

    @Deprecated
    public static String toString(URI uri) throws IOException {
        return toString(uri, Charset.defaultCharset());
    }

    public static String toString(URI uri, Charset charset) throws IOException {
        return toString(uri.toURL(), Charsets.toCharset(charset));
    }

    public static String toString(URI uri, String str) throws IOException {
        return toString(uri, Charsets.toCharset(str));
    }

    @Deprecated
    public static String toString(URL url) throws IOException {
        return toString(url, Charset.defaultCharset());
    }

    public static String toString(URL url, Charset charset) throws IOException {
        InputStream openStream = url.openStream();
        try {
            charset = toString(openStream, charset);
            if (openStream != null) {
                openStream.close();
            }
            return charset;
        } catch (Throwable th) {
            charset = th;
        }
        if (openStream != null) {
            if (r0 != null) {
                try {
                    openStream.close();
                } catch (URL url2) {
                    r0.addSuppressed(url2);
                }
            } else {
                openStream.close();
            }
        }
        throw charset;
        throw charset;
    }

    public static String toString(URL url, String str) throws IOException {
        return toString(url, Charsets.toCharset(str));
    }

    @Deprecated
    public static String toString(byte[] bArr) throws IOException {
        return new String(bArr, Charset.defaultCharset());
    }

    public static String toString(byte[] bArr, String str) throws IOException {
        return new String(bArr, Charsets.toCharset(str));
    }

    public static String resourceToString(String str, Charset charset) throws IOException {
        return resourceToString(str, charset, null);
    }

    public static String resourceToString(String str, Charset charset, ClassLoader classLoader) throws IOException {
        return toString(resourceToURL(str, classLoader), charset);
    }

    public static byte[] resourceToByteArray(String str) throws IOException {
        return resourceToByteArray(str, null);
    }

    public static byte[] resourceToByteArray(String str, ClassLoader classLoader) throws IOException {
        return toByteArray(resourceToURL(str, classLoader));
    }

    public static URL resourceToURL(String str) throws IOException {
        return resourceToURL(str, null);
    }

    public static URL resourceToURL(String str, ClassLoader classLoader) throws IOException {
        classLoader = classLoader == null ? IOUtils.class.getResource(str) : classLoader.getResource(str);
        if (classLoader != null) {
            return classLoader;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Resource not found: ");
        stringBuilder.append(str);
        throw new IOException(stringBuilder.toString());
    }

    @Deprecated
    public static List<String> readLines(InputStream inputStream) throws IOException {
        return readLines(inputStream, Charset.defaultCharset());
    }

    public static List<String> readLines(InputStream inputStream, Charset charset) throws IOException {
        return readLines(new InputStreamReader(inputStream, Charsets.toCharset(charset)));
    }

    public static List<String> readLines(InputStream inputStream, String str) throws IOException {
        return readLines(inputStream, Charsets.toCharset(str));
    }

    public static List<String> readLines(Reader reader) throws IOException {
        reader = toBufferedReader(reader);
        List<String> arrayList = new ArrayList();
        for (Object readLine = reader.readLine(); readLine != null; readLine = reader.readLine()) {
            arrayList.add(readLine);
        }
        return arrayList;
    }

    public static LineIterator lineIterator(Reader reader) {
        return new LineIterator(reader);
    }

    public static LineIterator lineIterator(InputStream inputStream, Charset charset) throws IOException {
        return new LineIterator(new InputStreamReader(inputStream, Charsets.toCharset(charset)));
    }

    public static LineIterator lineIterator(InputStream inputStream, String str) throws IOException {
        return lineIterator(inputStream, Charsets.toCharset(str));
    }

    @Deprecated
    public static InputStream toInputStream(CharSequence charSequence) {
        return toInputStream(charSequence, Charset.defaultCharset());
    }

    public static InputStream toInputStream(CharSequence charSequence, Charset charset) {
        return toInputStream(charSequence.toString(), charset);
    }

    public static InputStream toInputStream(CharSequence charSequence, String str) throws IOException {
        return toInputStream(charSequence, Charsets.toCharset(str));
    }

    @Deprecated
    public static InputStream toInputStream(String str) {
        return toInputStream(str, Charset.defaultCharset());
    }

    public static InputStream toInputStream(String str, Charset charset) {
        return new ByteArrayInputStream(str.getBytes(Charsets.toCharset(charset)));
    }

    public static InputStream toInputStream(String str, String str2) throws IOException {
        return new ByteArrayInputStream(str.getBytes(Charsets.toCharset(str2)));
    }

    public static void write(byte[] bArr, OutputStream outputStream) throws IOException {
        if (bArr != null) {
            outputStream.write(bArr);
        }
    }

    public static void writeChunked(byte[] bArr, OutputStream outputStream) throws IOException {
        if (bArr != null) {
            int length = bArr.length;
            int i = 0;
            while (length > 0) {
                int min = Math.min(length, 4096);
                outputStream.write(bArr, i, min);
                length -= min;
                i += min;
            }
        }
    }

    @Deprecated
    public static void write(byte[] bArr, Writer writer) throws IOException {
        write(bArr, writer, Charset.defaultCharset());
    }

    public static void write(byte[] bArr, Writer writer, Charset charset) throws IOException {
        if (bArr != null) {
            writer.write(new String(bArr, Charsets.toCharset(charset)));
        }
    }

    public static void write(byte[] bArr, Writer writer, String str) throws IOException {
        write(bArr, writer, Charsets.toCharset(str));
    }

    public static void write(char[] cArr, Writer writer) throws IOException {
        if (cArr != null) {
            writer.write(cArr);
        }
    }

    public static void writeChunked(char[] cArr, Writer writer) throws IOException {
        if (cArr != null) {
            int length = cArr.length;
            int i = 0;
            while (length > 0) {
                int min = Math.min(length, 4096);
                writer.write(cArr, i, min);
                length -= min;
                i += min;
            }
        }
    }

    @Deprecated
    public static void write(char[] cArr, OutputStream outputStream) throws IOException {
        write(cArr, outputStream, Charset.defaultCharset());
    }

    public static void write(char[] cArr, OutputStream outputStream, Charset charset) throws IOException {
        if (cArr != null) {
            outputStream.write(new String(cArr).getBytes(Charsets.toCharset(charset)));
        }
    }

    public static void write(char[] cArr, OutputStream outputStream, String str) throws IOException {
        write(cArr, outputStream, Charsets.toCharset(str));
    }

    public static void write(CharSequence charSequence, Writer writer) throws IOException {
        if (charSequence != null) {
            write(charSequence.toString(), writer);
        }
    }

    @Deprecated
    public static void write(CharSequence charSequence, OutputStream outputStream) throws IOException {
        write(charSequence, outputStream, Charset.defaultCharset());
    }

    public static void write(CharSequence charSequence, OutputStream outputStream, Charset charset) throws IOException {
        if (charSequence != null) {
            write(charSequence.toString(), outputStream, charset);
        }
    }

    public static void write(CharSequence charSequence, OutputStream outputStream, String str) throws IOException {
        write(charSequence, outputStream, Charsets.toCharset(str));
    }

    public static void write(String str, Writer writer) throws IOException {
        if (str != null) {
            writer.write(str);
        }
    }

    @Deprecated
    public static void write(String str, OutputStream outputStream) throws IOException {
        write(str, outputStream, Charset.defaultCharset());
    }

    public static void write(String str, OutputStream outputStream, Charset charset) throws IOException {
        if (str != null) {
            outputStream.write(str.getBytes(Charsets.toCharset(charset)));
        }
    }

    public static void write(String str, OutputStream outputStream, String str2) throws IOException {
        write(str, outputStream, Charsets.toCharset(str2));
    }

    @Deprecated
    public static void write(StringBuffer stringBuffer, Writer writer) throws IOException {
        if (stringBuffer != null) {
            writer.write(stringBuffer.toString());
        }
    }

    @Deprecated
    public static void write(StringBuffer stringBuffer, OutputStream outputStream) throws IOException {
        write(stringBuffer, outputStream, (String) null);
    }

    @Deprecated
    public static void write(StringBuffer stringBuffer, OutputStream outputStream, String str) throws IOException {
        if (stringBuffer != null) {
            outputStream.write(stringBuffer.toString().getBytes(Charsets.toCharset(str)));
        }
    }

    @Deprecated
    public static void writeLines(Collection<?> collection, String str, OutputStream outputStream) throws IOException {
        writeLines((Collection) collection, str, outputStream, Charset.defaultCharset());
    }

    public static void writeLines(Collection<?> collection, String str, OutputStream outputStream, Charset charset) throws IOException {
        if (collection != null) {
            if (str == null) {
                str = LINE_SEPARATOR;
            }
            charset = Charsets.toCharset(charset);
            for (Object next : collection) {
                if (next != null) {
                    outputStream.write(next.toString().getBytes(charset));
                }
                outputStream.write(str.getBytes(charset));
            }
        }
    }

    public static void writeLines(Collection<?> collection, String str, OutputStream outputStream, String str2) throws IOException {
        writeLines((Collection) collection, str, outputStream, Charsets.toCharset(str2));
    }

    public static void writeLines(Collection<?> collection, String str, Writer writer) throws IOException {
        if (collection != null) {
            if (str == null) {
                str = LINE_SEPARATOR;
            }
            for (Object next : collection) {
                if (next != null) {
                    writer.write(next.toString());
                }
                writer.write(str);
            }
        }
    }

    public static int copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        inputStream = copyLarge(inputStream, outputStream);
        return inputStream > 2147483647L ? -1 : (int) inputStream;
    }

    public static long copy(InputStream inputStream, OutputStream outputStream, int i) throws IOException {
        return copyLarge(inputStream, outputStream, new byte[i]);
    }

    public static long copyLarge(InputStream inputStream, OutputStream outputStream) throws IOException {
        return copy(inputStream, outputStream, 4096);
    }

    public static long copyLarge(InputStream inputStream, OutputStream outputStream, byte[] bArr) throws IOException {
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (-1 == read) {
                return j;
            }
            outputStream.write(bArr, 0, read);
            j += (long) read;
        }
    }

    public static long copyLarge(InputStream inputStream, OutputStream outputStream, long j, long j2) throws IOException {
        return copyLarge(inputStream, outputStream, j, j2, new byte[4096]);
    }

    public static long copyLarge(InputStream inputStream, OutputStream outputStream, long j, long j2, byte[] bArr) throws IOException {
        if (j > 0) {
            skipFully(inputStream, j);
        }
        if (j2 == 0) {
            return 0;
        }
        j = bArr.length;
        int i = (j2 <= 0 || j2 >= ((long) j)) ? j : (int) j2;
        long j3 = 0;
        while (i > 0) {
            int read = inputStream.read(bArr, 0, i);
            if (-1 == read) {
                break;
            }
            outputStream.write(bArr, 0, read);
            long j4 = j3 + ((long) read);
            if (j2 > 0) {
                i = (int) Math.min(j2 - j4, (long) j);
            }
            j3 = j4;
        }
        return j3;
    }

    @Deprecated
    public static void copy(InputStream inputStream, Writer writer) throws IOException {
        copy(inputStream, writer, Charset.defaultCharset());
    }

    public static void copy(InputStream inputStream, Writer writer, Charset charset) throws IOException {
        copy(new InputStreamReader(inputStream, Charsets.toCharset(charset)), writer);
    }

    public static void copy(InputStream inputStream, Writer writer, String str) throws IOException {
        copy(inputStream, writer, Charsets.toCharset(str));
    }

    public static int copy(Reader reader, Writer writer) throws IOException {
        reader = copyLarge(reader, writer);
        return reader > 2147483647L ? -1 : (int) reader;
    }

    public static long copyLarge(Reader reader, Writer writer) throws IOException {
        return copyLarge(reader, writer, new char[4096]);
    }

    public static long copyLarge(Reader reader, Writer writer, char[] cArr) throws IOException {
        long j = 0;
        while (true) {
            int read = reader.read(cArr);
            if (-1 == read) {
                return j;
            }
            writer.write(cArr, 0, read);
            j += (long) read;
        }
    }

    public static long copyLarge(Reader reader, Writer writer, long j, long j2) throws IOException {
        return copyLarge(reader, writer, j, j2, new char[4096]);
    }

    public static long copyLarge(Reader reader, Writer writer, long j, long j2, char[] cArr) throws IOException {
        if (j > 0) {
            skipFully(reader, j);
        }
        if (j2 == 0) {
            return 0;
        }
        j = cArr.length;
        if (j2 > 0 && j2 < ((long) cArr.length)) {
            j = (int) j2;
        }
        long j3 = 0;
        while (j > null) {
            int read = reader.read(cArr, 0, j);
            if (-1 == read) {
                break;
            }
            writer.write(cArr, 0, read);
            long j4 = j3 + ((long) read);
            if (j2 > 0) {
                j = (int) Math.min(j2 - j4, (long) cArr.length);
            }
            j3 = j4;
        }
        return j3;
    }

    @Deprecated
    public static void copy(Reader reader, OutputStream outputStream) throws IOException {
        copy(reader, outputStream, Charset.defaultCharset());
    }

    public static void copy(Reader reader, OutputStream outputStream, Charset charset) throws IOException {
        Writer outputStreamWriter = new OutputStreamWriter(outputStream, Charsets.toCharset(charset));
        copy(reader, outputStreamWriter);
        outputStreamWriter.flush();
    }

    public static void copy(Reader reader, OutputStream outputStream, String str) throws IOException {
        copy(reader, outputStream, Charsets.toCharset(str));
    }

    public static boolean contentEquals(InputStream inputStream, InputStream inputStream2) throws IOException {
        boolean z = true;
        if (inputStream == inputStream2) {
            return true;
        }
        if (!(inputStream instanceof BufferedInputStream)) {
            inputStream = new BufferedInputStream(inputStream);
        }
        if (!(inputStream2 instanceof BufferedInputStream)) {
            inputStream2 = new BufferedInputStream(inputStream2);
        }
        for (int read = inputStream.read(); -1 != read; read = inputStream.read()) {
            if (read != inputStream2.read()) {
                return false;
            }
        }
        if (inputStream2.read() != -1) {
            z = false;
        }
        return z;
    }

    public static boolean contentEquals(Reader reader, Reader reader2) throws IOException {
        boolean z = true;
        if (reader == reader2) {
            return true;
        }
        reader = toBufferedReader(reader);
        reader2 = toBufferedReader(reader2);
        for (int read = reader.read(); -1 != read; read = reader.read()) {
            if (read != reader2.read()) {
                return false;
            }
        }
        if (reader2.read() != -1) {
            z = false;
        }
        return z;
    }

    public static boolean contentEqualsIgnoreEOL(Reader reader, Reader reader2) throws IOException {
        boolean z = true;
        if (reader == reader2) {
            return true;
        }
        reader = toBufferedReader(reader);
        reader2 = toBufferedReader(reader2);
        String readLine = reader.readLine();
        Object readLine2 = reader2.readLine();
        while (readLine != null && readLine2 != null && readLine.equals(readLine2)) {
            readLine = reader.readLine();
            readLine2 = reader2.readLine();
        }
        if (readLine != null) {
            z = readLine.equals(readLine2);
        } else if (readLine2 != null) {
            z = false;
        }
        return z;
    }

    public static long skip(InputStream inputStream, long j) throws IOException {
        if (j < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Skip count must be non-negative, actual: ");
            stringBuilder.append(j);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        if (SKIP_BYTE_BUFFER == null) {
            SKIP_BYTE_BUFFER = new byte[2048];
        }
        long j2 = j;
        while (j2 > 0) {
            long read = (long) inputStream.read(SKIP_BYTE_BUFFER, 0, (int) Math.min(j2, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH));
            if (read < 0) {
                break;
            }
            j2 -= read;
        }
        return j - j2;
    }

    public static long skip(ReadableByteChannel readableByteChannel, long j) throws IOException {
        if (j < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Skip count must be non-negative, actual: ");
            stringBuilder.append(j);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        ByteBuffer allocate = ByteBuffer.allocate((int) Math.min(j, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH));
        long j2 = j;
        while (j2 > 0) {
            allocate.position(0);
            allocate.limit((int) Math.min(j2, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH));
            int read = readableByteChannel.read(allocate);
            if (read == -1) {
                break;
            }
            j2 -= (long) read;
        }
        return j - j2;
    }

    public static long skip(Reader reader, long j) throws IOException {
        if (j < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Skip count must be non-negative, actual: ");
            stringBuilder.append(j);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        if (SKIP_CHAR_BUFFER == null) {
            SKIP_CHAR_BUFFER = new char[2048];
        }
        long j2 = j;
        while (j2 > 0) {
            long read = (long) reader.read(SKIP_CHAR_BUFFER, 0, (int) Math.min(j2, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH));
            if (read < 0) {
                break;
            }
            j2 -= read;
        }
        return j - j2;
    }

    public static void skipFully(InputStream inputStream, long j) throws IOException {
        if (j < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Bytes to skip must not be negative: ");
            stringBuilder.append(j);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        long skip = skip(inputStream, j);
        if (skip != j) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Bytes to skip: ");
            stringBuilder2.append(j);
            stringBuilder2.append(" actual: ");
            stringBuilder2.append(skip);
            throw new EOFException(stringBuilder2.toString());
        }
    }

    public static void skipFully(ReadableByteChannel readableByteChannel, long j) throws IOException {
        if (j < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Bytes to skip must not be negative: ");
            stringBuilder.append(j);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        long skip = skip(readableByteChannel, j);
        if (skip != j) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Bytes to skip: ");
            stringBuilder2.append(j);
            stringBuilder2.append(" actual: ");
            stringBuilder2.append(skip);
            throw new EOFException(stringBuilder2.toString());
        }
    }

    public static void skipFully(Reader reader, long j) throws IOException {
        long skip = skip(reader, j);
        if (skip != j) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Chars to skip: ");
            stringBuilder.append(j);
            stringBuilder.append(" actual: ");
            stringBuilder.append(skip);
            throw new EOFException(stringBuilder.toString());
        }
    }

    public static int read(Reader reader, char[] cArr, int i, int i2) throws IOException {
        if (i2 < 0) {
            cArr = new StringBuilder();
            cArr.append("Length must not be negative: ");
            cArr.append(i2);
            throw new IllegalArgumentException(cArr.toString());
        }
        int i3 = i2;
        while (i3 > 0) {
            int read = reader.read(cArr, (i2 - i3) + i, i3);
            if (-1 == read) {
                break;
            }
            i3 -= read;
        }
        return i2 - i3;
    }

    public static int read(Reader reader, char[] cArr) throws IOException {
        return read(reader, cArr, 0, cArr.length);
    }

    public static int read(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
        if (i2 < 0) {
            bArr = new StringBuilder();
            bArr.append("Length must not be negative: ");
            bArr.append(i2);
            throw new IllegalArgumentException(bArr.toString());
        }
        int i3 = i2;
        while (i3 > 0) {
            int read = inputStream.read(bArr, (i2 - i3) + i, i3);
            if (-1 == read) {
                break;
            }
            i3 -= read;
        }
        return i2 - i3;
    }

    public static int read(InputStream inputStream, byte[] bArr) throws IOException {
        return read(inputStream, bArr, 0, bArr.length);
    }

    public static int read(ReadableByteChannel readableByteChannel, ByteBuffer byteBuffer) throws IOException {
        int remaining = byteBuffer.remaining();
        while (byteBuffer.remaining() > 0) {
            if (-1 == readableByteChannel.read(byteBuffer)) {
                break;
            }
        }
        return remaining - byteBuffer.remaining();
    }

    public static void readFully(Reader reader, char[] cArr, int i, int i2) throws IOException {
        reader = read(reader, cArr, i, i2);
        if (reader != i2) {
            i = new StringBuilder();
            i.append("Length to read: ");
            i.append(i2);
            i.append(" actual: ");
            i.append(reader);
            throw new EOFException(i.toString());
        }
    }

    public static void readFully(Reader reader, char[] cArr) throws IOException {
        readFully(reader, cArr, 0, cArr.length);
    }

    public static void readFully(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
        inputStream = read(inputStream, bArr, i, i2);
        if (inputStream != i2) {
            i = new StringBuilder();
            i.append("Length to read: ");
            i.append(i2);
            i.append(" actual: ");
            i.append(inputStream);
            throw new EOFException(i.toString());
        }
    }

    public static void readFully(InputStream inputStream, byte[] bArr) throws IOException {
        readFully(inputStream, bArr, 0, bArr.length);
    }

    public static byte[] readFully(InputStream inputStream, int i) throws IOException {
        byte[] bArr = new byte[i];
        readFully(inputStream, bArr, 0, bArr.length);
        return bArr;
    }

    public static void readFully(ReadableByteChannel readableByteChannel, ByteBuffer byteBuffer) throws IOException {
        int remaining = byteBuffer.remaining();
        readableByteChannel = read(readableByteChannel, byteBuffer);
        if (readableByteChannel != remaining) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Length to read: ");
            stringBuilder.append(remaining);
            stringBuilder.append(" actual: ");
            stringBuilder.append(readableByteChannel);
            throw new EOFException(stringBuilder.toString());
        }
    }
}
