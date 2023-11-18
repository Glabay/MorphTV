package org.jsoup.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.regex.Pattern;
import net.lingala.zip4j.util.InternalZipConstants;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

public final class DataUtil {
    private static final int UNICODE_BOM = 65279;
    static final int boundaryLength = 32;
    private static final int bufferSize = 131072;
    private static final Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*(?:\"|')?([^\\s,;\"']*)");
    static final String defaultCharset = "UTF-8";
    private static final char[] mimeBoundaryChars = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private DataUtil() {
    }

    public static Document load(File file, String str, String str2) throws IOException {
        return parseByteData(readFileToByteBuffer(file), str, str2, Parser.htmlParser());
    }

    public static Document load(InputStream inputStream, String str, String str2) throws IOException {
        return parseByteData(readToByteBuffer(inputStream), str, str2, Parser.htmlParser());
    }

    public static Document load(InputStream inputStream, String str, String str2, Parser parser) throws IOException {
        return parseByteData(readToByteBuffer(inputStream), str, str2, parser);
    }

    static void crossStreams(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[131072];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    static Document parseByteData(ByteBuffer byteBuffer, String str, String str2, Parser parser) {
        String charBuffer;
        str = detectCharsetFromBom(byteBuffer, str);
        Document document = null;
        if (str == null) {
            String attr;
            charBuffer = Charset.forName("UTF-8").decode(byteBuffer).toString();
            Document parseInput = parser.parseInput(charBuffer, str2);
            Element first = parseInput.select("meta[http-equiv=content-type], meta[charset]").first();
            if (first != null) {
                String charsetFromContentType = first.hasAttr("http-equiv") ? getCharsetFromContentType(first.attr("content")) : null;
                attr = (charsetFromContentType == null && first.hasAttr("charset")) ? first.attr("charset") : charsetFromContentType;
            } else {
                attr = null;
            }
            if (attr == null && parseInput.childNodeSize() > 0 && (parseInput.childNode(0) instanceof XmlDeclaration)) {
                XmlDeclaration xmlDeclaration = (XmlDeclaration) parseInput.childNode(0);
                if (xmlDeclaration.name().equals("xml")) {
                    attr = xmlDeclaration.attr("encoding");
                }
            }
            attr = validateCharset(attr);
            if (attr == null || attr.equals("UTF-8")) {
                document = parseInput;
            } else {
                str = attr.trim().replaceAll("[\"']", "");
                byteBuffer.rewind();
                charBuffer = Charset.forName(str).decode(byteBuffer).toString();
            }
        } else {
            Validate.notEmpty(str, "Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML");
            charBuffer = Charset.forName(str).decode(byteBuffer).toString();
        }
        if (document != null) {
            return document;
        }
        document = parser.parseInput(charBuffer, str2);
        document.outputSettings().charset(str);
        return document;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.nio.ByteBuffer readToByteBuffer(java.io.InputStream r6, int r7) throws java.io.IOException {
        /*
        r0 = 1;
        r1 = 0;
        if (r7 < 0) goto L_0x0006;
    L_0x0004:
        r2 = 1;
        goto L_0x0007;
    L_0x0006:
        r2 = 0;
    L_0x0007:
        r3 = "maxSize must be 0 (unlimited) or larger";
        org.jsoup.helper.Validate.isTrue(r2, r3);
        if (r7 <= 0) goto L_0x000f;
    L_0x000e:
        goto L_0x0010;
    L_0x000f:
        r0 = 0;
    L_0x0010:
        r2 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;
        r3 = new byte[r2];
        r4 = new java.io.ByteArrayOutputStream;
        r4.<init>(r2);
    L_0x0019:
        r2 = r6.read(r3);
        r5 = -1;
        if (r2 != r5) goto L_0x0021;
    L_0x0020:
        goto L_0x0028;
    L_0x0021:
        if (r0 == 0) goto L_0x0032;
    L_0x0023:
        if (r2 <= r7) goto L_0x0031;
    L_0x0025:
        r4.write(r3, r1, r7);
    L_0x0028:
        r6 = r4.toByteArray();
        r6 = java.nio.ByteBuffer.wrap(r6);
        return r6;
    L_0x0031:
        r7 = r7 - r2;
    L_0x0032:
        r4.write(r3, r1, r2);
        goto L_0x0019;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jsoup.helper.DataUtil.readToByteBuffer(java.io.InputStream, int):java.nio.ByteBuffer");
    }

    static ByteBuffer readToByteBuffer(InputStream inputStream) throws IOException {
        return readToByteBuffer(inputStream, 0);
    }

    static ByteBuffer readFileToByteBuffer(File file) throws IOException {
        RandomAccessFile randomAccessFile;
        try {
            randomAccessFile = new RandomAccessFile(file, InternalZipConstants.READ_MODE);
            try {
                file = new byte[((int) randomAccessFile.length())];
                randomAccessFile.readFully(file);
                file = ByteBuffer.wrap(file);
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
                return file;
            } catch (Throwable th) {
                file = th;
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
                throw file;
            }
        } catch (Throwable th2) {
            file = th2;
            randomAccessFile = null;
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
            throw file;
        }
    }

    static ByteBuffer emptyByteBuffer() {
        return ByteBuffer.allocate(0);
    }

    static String getCharsetFromContentType(String str) {
        if (str == null) {
            return null;
        }
        str = charsetPattern.matcher(str);
        if (str.find()) {
            return validateCharset(str.group(1).trim().replace("charset=", ""));
        }
        return null;
    }

    private static java.lang.String validateCharset(java.lang.String r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        if (r3 == 0) goto L_0x002b;
    L_0x0003:
        r1 = r3.length();
        if (r1 != 0) goto L_0x000a;
    L_0x0009:
        goto L_0x002b;
    L_0x000a:
        r3 = r3.trim();
        r1 = "[\"']";
        r2 = "";
        r3 = r3.replaceAll(r1, r2);
        r1 = java.nio.charset.Charset.isSupported(r3);	 Catch:{ IllegalCharsetNameException -> 0x002a }
        if (r1 == 0) goto L_0x001d;	 Catch:{ IllegalCharsetNameException -> 0x002a }
    L_0x001c:
        return r3;	 Catch:{ IllegalCharsetNameException -> 0x002a }
    L_0x001d:
        r1 = java.util.Locale.ENGLISH;	 Catch:{ IllegalCharsetNameException -> 0x002a }
        r3 = r3.toUpperCase(r1);	 Catch:{ IllegalCharsetNameException -> 0x002a }
        r1 = java.nio.charset.Charset.isSupported(r3);	 Catch:{ IllegalCharsetNameException -> 0x002a }
        if (r1 == 0) goto L_0x002a;
    L_0x0029:
        return r3;
    L_0x002a:
        return r0;
    L_0x002b:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jsoup.helper.DataUtil.validateCharset(java.lang.String):java.lang.String");
    }

    static String mimeBoundary() {
        StringBuilder stringBuilder = new StringBuilder(32);
        Random random = new Random();
        for (int i = 0; i < 32; i++) {
            stringBuilder.append(mimeBoundaryChars[random.nextInt(mimeBoundaryChars.length)]);
        }
        return stringBuilder.toString();
    }

    private static String detectCharsetFromBom(ByteBuffer byteBuffer, String str) {
        byteBuffer.mark();
        byte[] bArr = new byte[4];
        if (byteBuffer.remaining() >= bArr.length) {
            byteBuffer.get(bArr);
            byteBuffer.rewind();
        }
        if ((bArr[0] == (byte) 0 && bArr[1] == (byte) 0 && bArr[2] == (byte) -2 && bArr[3] == (byte) -1) || (bArr[0] == (byte) -1 && bArr[1] == (byte) -2 && bArr[2] == (byte) 0 && bArr[3] == (byte) 0)) {
            return "UTF-32";
        }
        if ((bArr[0] == (byte) -2 && bArr[1] == (byte) -1) || (bArr[0] == (byte) -1 && bArr[1] == (byte) -2)) {
            return "UTF-16";
        }
        if (bArr[0] != (byte) -17 || bArr[1] != (byte) -69 || bArr[2] != (byte) -65) {
            return str;
        }
        str = "UTF-8";
        byteBuffer.position(3);
        return str;
    }
}
