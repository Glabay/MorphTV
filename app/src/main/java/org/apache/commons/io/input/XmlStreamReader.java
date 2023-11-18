package org.apache.commons.io.input;

import fi.iki.elonen.NanoHTTPD;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.commons.io.ByteOrderMark;

public class XmlStreamReader extends Reader {
    private static final ByteOrderMark[] BOMS = new ByteOrderMark[]{ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE};
    private static final int BUFFER_SIZE = 4096;
    private static final Pattern CHARSET_PATTERN = Pattern.compile("charset=[\"']?([.[^; \"']]*)[\"']?");
    private static final String EBCDIC = "CP1047";
    public static final Pattern ENCODING_PATTERN = Pattern.compile("<\\?xml.*encoding[\\s]*=[\\s]*((?:\".[^\"]*\")|(?:'.[^']*'))", 8);
    private static final String HTTP_EX_1 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL";
    private static final String HTTP_EX_2 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch";
    private static final String HTTP_EX_3 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME";
    private static final String RAW_EX_1 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch";
    private static final String RAW_EX_2 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM";
    private static final String US_ASCII = "US-ASCII";
    private static final String UTF_16 = "UTF-16";
    private static final String UTF_16BE = "UTF-16BE";
    private static final String UTF_16LE = "UTF-16LE";
    private static final String UTF_32 = "UTF-32";
    private static final String UTF_32BE = "UTF-32BE";
    private static final String UTF_32LE = "UTF-32LE";
    private static final String UTF_8 = "UTF-8";
    private static final ByteOrderMark[] XML_GUESS_BYTES = new ByteOrderMark[]{new ByteOrderMark("UTF-8", 60, 63, 120, 109), new ByteOrderMark("UTF-16BE", 0, 60, 0, 63), new ByteOrderMark("UTF-16LE", 60, 0, 63, 0), new ByteOrderMark(UTF_32BE, 0, 0, 0, 60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 0, 0, 109), new ByteOrderMark(UTF_32LE, 60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 0, 0, 109, 0, 0, 0), new ByteOrderMark(EBCDIC, 76, 111, 167, 148)};
    private final String defaultEncoding;
    private final String encoding;
    private final Reader reader;

    public String getDefaultEncoding() {
        return this.defaultEncoding;
    }

    public XmlStreamReader(File file) throws IOException {
        this(new FileInputStream(file));
    }

    public XmlStreamReader(InputStream inputStream) throws IOException {
        this(inputStream, true);
    }

    public XmlStreamReader(InputStream inputStream, boolean z) throws IOException {
        this(inputStream, z, null);
    }

    public XmlStreamReader(InputStream inputStream, boolean z, String str) throws IOException {
        this.defaultEncoding = str;
        str = new BOMInputStream(new BufferedInputStream(inputStream, 4096), false, BOMS);
        inputStream = new BOMInputStream(str, true, XML_GUESS_BYTES);
        this.encoding = doRawStream(str, inputStream, z);
        this.reader = new InputStreamReader(inputStream, this.encoding);
    }

    public XmlStreamReader(URL url) throws IOException {
        this(url.openConnection(), null);
    }

    public XmlStreamReader(URLConnection uRLConnection, String str) throws IOException {
        this.defaultEncoding = str;
        str = uRLConnection.getContentType();
        InputStream bOMInputStream = new BOMInputStream(new BufferedInputStream(uRLConnection.getInputStream(), 4096), false, BOMS);
        InputStream bOMInputStream2 = new BOMInputStream(bOMInputStream, true, XML_GUESS_BYTES);
        if ((uRLConnection instanceof HttpURLConnection) == null) {
            if (str == null) {
                this.encoding = doRawStream(bOMInputStream, bOMInputStream2, true);
                this.reader = new InputStreamReader(bOMInputStream2, this.encoding);
            }
        }
        this.encoding = doHttpStream(bOMInputStream, bOMInputStream2, str, true);
        this.reader = new InputStreamReader(bOMInputStream2, this.encoding);
    }

    public XmlStreamReader(InputStream inputStream, String str) throws IOException {
        this(inputStream, str, true);
    }

    public XmlStreamReader(InputStream inputStream, String str, boolean z, String str2) throws IOException {
        this.defaultEncoding = str2;
        str2 = new BOMInputStream(new BufferedInputStream(inputStream, 4096), false, BOMS);
        inputStream = new BOMInputStream(str2, true, XML_GUESS_BYTES);
        this.encoding = doHttpStream(str2, inputStream, str, z);
        this.reader = new InputStreamReader(inputStream, this.encoding);
    }

    public XmlStreamReader(InputStream inputStream, String str, boolean z) throws IOException {
        this(inputStream, str, z, null);
    }

    public String getEncoding() {
        return this.encoding;
    }

    public int read(char[] cArr, int i, int i2) throws IOException {
        return this.reader.read(cArr, i, i2);
    }

    public void close() throws IOException {
        this.reader.close();
    }

    private String doRawStream(BOMInputStream bOMInputStream, BOMInputStream bOMInputStream2, boolean z) throws IOException {
        bOMInputStream = bOMInputStream.getBOMCharsetName();
        String bOMCharsetName = bOMInputStream2.getBOMCharsetName();
        try {
            return calculateRawEncoding(bOMInputStream, bOMCharsetName, getXmlProlog(bOMInputStream2, bOMCharsetName));
        } catch (BOMInputStream bOMInputStream3) {
            if (z) {
                return doLenientDetection(null, bOMInputStream3);
            }
            throw bOMInputStream3;
        }
    }

    private String doHttpStream(BOMInputStream bOMInputStream, BOMInputStream bOMInputStream2, String str, boolean z) throws IOException {
        String bOMCharsetName = bOMInputStream.getBOMCharsetName();
        String bOMCharsetName2 = bOMInputStream2.getBOMCharsetName();
        try {
            return calculateHttpEncoding(str, bOMCharsetName, bOMCharsetName2, getXmlProlog(bOMInputStream2, bOMCharsetName2), z);
        } catch (BOMInputStream bOMInputStream3) {
            if (z) {
                return doLenientDetection(str, bOMInputStream3);
            }
            throw bOMInputStream3;
        }
    }

    private String doLenientDetection(String str, XmlStreamReaderException xmlStreamReaderException) throws IOException {
        if (str != null && str.startsWith(NanoHTTPD.MIME_HTML)) {
            str = str.substring(NanoHTTPD.MIME_HTML.length());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("text/xml");
            stringBuilder.append(str);
            try {
                return calculateHttpEncoding(stringBuilder.toString(), xmlStreamReaderException.getBomEncoding(), xmlStreamReaderException.getXmlGuessEncoding(), xmlStreamReaderException.getXmlEncoding(), true);
            } catch (String str2) {
                xmlStreamReaderException = str2;
            }
        }
        str2 = xmlStreamReaderException.getXmlEncoding();
        if (str2 == null) {
            str2 = xmlStreamReaderException.getContentTypeEncoding();
        }
        if (str2 == null) {
            str2 = this.defaultEncoding == null ? "UTF-8" : this.defaultEncoding;
        }
        return str2;
    }

    String calculateRawEncoding(String str, String str2, String str3) throws IOException {
        if (str == null) {
            if (str2 != null) {
                if (str3 != null) {
                    return (str3.equals("UTF-16") == null || (str2.equals("UTF-16BE") == null && str2.equals("UTF-16LE") == null)) ? str3 : str2;
                }
            }
            return this.defaultEncoding == null ? "UTF-8" : this.defaultEncoding;
        } else if (!str.equals("UTF-8")) {
            if (!str.equals("UTF-16BE")) {
                if (!str.equals("UTF-16LE")) {
                    if (!str.equals(UTF_32BE)) {
                        if (!str.equals(UTF_32LE)) {
                            throw new XmlStreamReaderException(MessageFormat.format(RAW_EX_2, new Object[]{str, str2, str3}), str, str2, str3);
                        }
                    }
                    if (str2 != null && !str2.equals(str)) {
                        throw new XmlStreamReaderException(MessageFormat.format(RAW_EX_1, new Object[]{str, str2, str3}), str, str2, str3);
                    } else if (str3 == null || str3.equals(UTF_32) || str3.equals(str)) {
                        return str;
                    } else {
                        throw new XmlStreamReaderException(MessageFormat.format(RAW_EX_1, new Object[]{str, str2, str3}), str, str2, str3);
                    }
                }
            }
            if (str2 != null && !str2.equals(str)) {
                throw new XmlStreamReaderException(MessageFormat.format(RAW_EX_1, new Object[]{str, str2, str3}), str, str2, str3);
            } else if (str3 == null || str3.equals("UTF-16") || str3.equals(str)) {
                return str;
            } else {
                throw new XmlStreamReaderException(MessageFormat.format(RAW_EX_1, new Object[]{str, str2, str3}), str, str2, str3);
            }
        } else if (str2 != null && !str2.equals("UTF-8")) {
            throw new XmlStreamReaderException(MessageFormat.format(RAW_EX_1, new Object[]{str, str2, str3}), str, str2, str3);
        } else if (str3 == null || str3.equals("UTF-8")) {
            return str;
        } else {
            throw new XmlStreamReaderException(MessageFormat.format(RAW_EX_1, new Object[]{str, str2, str3}), str, str2, str3);
        }
    }

    String calculateHttpEncoding(String str, String str2, String str3, String str4, boolean z) throws IOException {
        if (z && str4 != null) {
            return str4;
        }
        String contentTypeMime = getContentTypeMime(str);
        String contentTypeEncoding = getContentTypeEncoding(str);
        str = isAppXml(contentTypeMime);
        z = isTextXml(contentTypeMime);
        if (str == null && !z) {
            throw new XmlStreamReaderException(MessageFormat.format(HTTP_EX_3, new Object[]{contentTypeMime, contentTypeEncoding, str2, str3, str4}), contentTypeMime, contentTypeEncoding, str2, str3, str4);
        } else if (contentTypeEncoding != null) {
            if (contentTypeEncoding.equals("UTF-16BE") == null) {
                if (contentTypeEncoding.equals("UTF-16LE") == null) {
                    if (contentTypeEncoding.equals("UTF-16") == null) {
                        if (contentTypeEncoding.equals(UTF_32BE) == null) {
                            if (contentTypeEncoding.equals(UTF_32LE) == null) {
                                if (contentTypeEncoding.equals(UTF_32) == null) {
                                    return contentTypeEncoding;
                                }
                                if (str2 != null && str2.startsWith(UTF_32) != null) {
                                    return str2;
                                }
                                throw new XmlStreamReaderException(MessageFormat.format(HTTP_EX_2, new Object[]{contentTypeMime, contentTypeEncoding, str2, str3, str4}), contentTypeMime, contentTypeEncoding, str2, str3, str4);
                            }
                        }
                        if (str2 == null) {
                            return contentTypeEncoding;
                        }
                        throw new XmlStreamReaderException(MessageFormat.format(HTTP_EX_1, new Object[]{contentTypeMime, contentTypeEncoding, str2, str3, str4}), contentTypeMime, contentTypeEncoding, str2, str3, str4);
                    } else if (str2 != null && str2.startsWith("UTF-16") != null) {
                        return str2;
                    } else {
                        throw new XmlStreamReaderException(MessageFormat.format(HTTP_EX_2, new Object[]{contentTypeMime, contentTypeEncoding, str2, str3, str4}), contentTypeMime, contentTypeEncoding, str2, str3, str4);
                    }
                }
            }
            if (str2 == null) {
                return contentTypeEncoding;
            }
            throw new XmlStreamReaderException(MessageFormat.format(HTTP_EX_1, new Object[]{contentTypeMime, contentTypeEncoding, str2, str3, str4}), contentTypeMime, contentTypeEncoding, str2, str3, str4);
        } else if (str != null) {
            return calculateRawEncoding(str2, str3, str4);
        } else {
            return this.defaultEncoding == null ? "US-ASCII" : this.defaultEncoding;
        }
    }

    static String getContentTypeMime(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(";");
        if (indexOf >= 0) {
            str = str.substring(0, indexOf);
        }
        return str.trim();
    }

    static String getContentTypeEncoding(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(";");
        if (indexOf <= -1) {
            return null;
        }
        str = CHARSET_PATTERN.matcher(str.substring(indexOf + 1));
        str = str.find() ? str.group(1) : null;
        if (str != null) {
            return str.toUpperCase(Locale.US);
        }
        return null;
    }

    private static String getXmlProlog(InputStream inputStream, String str) throws IOException {
        if (str != null) {
            byte[] bArr = new byte[4096];
            inputStream.mark(4096);
            int read = inputStream.read(bArr, 0, 4096);
            String str2 = "";
            int i = -1;
            int i2 = 0;
            int i3 = 4096;
            while (read != -1 && i == -1 && i2 < 4096) {
                i2 += read;
                i3 -= read;
                read = inputStream.read(bArr, i2, i3);
                str2 = new String(bArr, 0, i2, str);
                i = str2.indexOf(62);
            }
            if (i == -1) {
                if (read == -1) {
                    throw new IOException("Unexpected end of XML stream");
                }
                str = new StringBuilder();
                str.append("XML prolog or ROOT element not found on first ");
                str.append(i2);
                str.append(" bytes");
                throw new IOException(str.toString());
            } else if (i2 > 0) {
                inputStream.reset();
                inputStream = new BufferedReader(new StringReader(str2.substring(0, i + 1)));
                str = new StringBuffer();
                for (String readLine = inputStream.readLine(); readLine != null; readLine = inputStream.readLine()) {
                    str.append(readLine);
                }
                inputStream = ENCODING_PATTERN.matcher(str);
                if (inputStream.find() != null) {
                    inputStream = inputStream.group(1).toUpperCase();
                    return inputStream.substring(1, inputStream.length() - 1);
                }
            }
        }
        return null;
    }

    static boolean isAppXml(String str) {
        return (str == null || !(str.equals("application/xml") || str.equals("application/xml-dtd") || str.equals("application/xml-external-parsed-entity") || (str.startsWith("application/") && str.endsWith("+xml") != null))) ? null : true;
    }

    static boolean isTextXml(String str) {
        return (str == null || !(str.equals("text/xml") || str.equals("text/xml-external-parsed-entity") || (str.startsWith("text/") && str.endsWith("+xml") != null))) ? null : true;
    }
}
