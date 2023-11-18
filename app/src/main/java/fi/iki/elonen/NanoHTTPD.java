package fi.iki.elonen;

import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.google.common.base.Ascii;
import com.google.common.net.HttpHeaders;
import ir.mahdi.mzip.rar.unpack.decode.Compress;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import net.lingala.zip4j.util.InternalZipConstants;
import okhttp3.internal.http.StatusLine;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class NanoHTTPD {
    private static final Pattern CONTENT_DISPOSITION_ATTRIBUTE_PATTERN = Pattern.compile(CONTENT_DISPOSITION_ATTRIBUTE_REGEX);
    private static final String CONTENT_DISPOSITION_ATTRIBUTE_REGEX = "[ |\t]*([a-zA-Z]*)[ |\t]*=[ |\t]*['|\"]([^\"^']*)['|\"]";
    private static final Pattern CONTENT_DISPOSITION_PATTERN = Pattern.compile(CONTENT_DISPOSITION_REGEX, 2);
    private static final String CONTENT_DISPOSITION_REGEX = "([ |\t]*Content-Disposition[ |\t]*:)(.*)";
    private static final Pattern CONTENT_TYPE_PATTERN = Pattern.compile(CONTENT_TYPE_REGEX, 2);
    private static final String CONTENT_TYPE_REGEX = "([ |\t]*content-type[ |\t]*:)(.*)";
    private static final Logger LOG = Logger.getLogger(NanoHTTPD.class.getName());
    public static final String MIME_HTML = "text/html";
    public static final String MIME_PLAINTEXT = "text/plain";
    protected static Map<String, String> MIME_TYPES = null;
    private static final String QUERY_STRING_PARAMETER = "NanoHttpd.QUERY_STRING";
    public static final int SOCKET_READ_TIMEOUT = 5000;
    protected AsyncRunner asyncRunner;
    private final String hostname;
    private final int myPort;
    private volatile ServerSocket myServerSocket;
    private Thread myThread;
    private ServerSocketFactory serverSocketFactory;
    private TempFileManagerFactory tempFileManagerFactory;

    public interface AsyncRunner {
        void closeAll();

        void closed(ClientHandler clientHandler);

        void exec(ClientHandler clientHandler);
    }

    public class ClientHandler implements Runnable {
        private final Socket acceptSocket;
        private final InputStream inputStream;

        public ClientHandler(InputStream inputStream, Socket socket) {
            this.inputStream = inputStream;
            this.acceptSocket = socket;
        }

        public void close() {
            NanoHTTPD.safeClose(this.inputStream);
            NanoHTTPD.safeClose(this.acceptSocket);
        }

        public void run() {
            Object outputStream;
            Throwable e;
            Throwable th;
            try {
                outputStream = this.acceptSocket.getOutputStream();
                try {
                    HTTPSession hTTPSession = new HTTPSession(NanoHTTPD.this.tempFileManagerFactory.create(), this.inputStream, outputStream, this.acceptSocket.getInetAddress());
                    while (!this.acceptSocket.isClosed()) {
                        hTTPSession.execute();
                    }
                } catch (Exception e2) {
                    e = e2;
                    try {
                        if (!(((e instanceof SocketException) && "NanoHttpd Shutdown".equals(e.getMessage())) || (e instanceof SocketTimeoutException))) {
                            NanoHTTPD.LOG.log(Level.SEVERE, "Communication with the client broken, or an bug in the handler code", e);
                        }
                        NanoHTTPD.safeClose(outputStream);
                        NanoHTTPD.safeClose(this.inputStream);
                        NanoHTTPD.safeClose(this.acceptSocket);
                        NanoHTTPD.this.asyncRunner.closed(this);
                    } catch (Throwable th2) {
                        e = th2;
                        NanoHTTPD.safeClose(outputStream);
                        NanoHTTPD.safeClose(this.inputStream);
                        NanoHTTPD.safeClose(this.acceptSocket);
                        NanoHTTPD.this.asyncRunner.closed(this);
                        throw e;
                    }
                }
            } catch (Throwable e3) {
                th = e3;
                outputStream = null;
                e = th;
                NanoHTTPD.LOG.log(Level.SEVERE, "Communication with the client broken, or an bug in the handler code", e);
                NanoHTTPD.safeClose(outputStream);
                NanoHTTPD.safeClose(this.inputStream);
                NanoHTTPD.safeClose(this.acceptSocket);
                NanoHTTPD.this.asyncRunner.closed(this);
            } catch (Throwable e32) {
                th = e32;
                outputStream = null;
                e = th;
                NanoHTTPD.safeClose(outputStream);
                NanoHTTPD.safeClose(this.inputStream);
                NanoHTTPD.safeClose(this.acceptSocket);
                NanoHTTPD.this.asyncRunner.closed(this);
                throw e;
            }
            NanoHTTPD.safeClose(outputStream);
            NanoHTTPD.safeClose(this.inputStream);
            NanoHTTPD.safeClose(this.acceptSocket);
            NanoHTTPD.this.asyncRunner.closed(this);
        }
    }

    protected static class ContentType {
        private static final String ASCII_ENCODING = "US-ASCII";
        private static final Pattern BOUNDARY_PATTERN = Pattern.compile(BOUNDARY_REGEX, 2);
        private static final String BOUNDARY_REGEX = "[ |\t]*(boundary)[ |\t]*=[ |\t]*['|\"]?([^\"^'^;^,]*)['|\"]?";
        private static final Pattern CHARSET_PATTERN = Pattern.compile(CHARSET_REGEX, 2);
        private static final String CHARSET_REGEX = "[ |\t]*(charset)[ |\t]*=[ |\t]*['|\"]?([^\"^'^;^,]*)['|\"]?";
        private static final String CONTENT_REGEX = "[ |\t]*([^/^ ^;^,]+/[^ ^;^,]+)";
        private static final Pattern MIME_PATTERN = Pattern.compile(CONTENT_REGEX, 2);
        private static final String MULTIPART_FORM_DATA_HEADER = "multipart/form-data";
        private final String boundary;
        private final String contentType;
        private final String contentTypeHeader;
        private final String encoding;

        public ContentType(String str) {
            this.contentTypeHeader = str;
            if (str != null) {
                this.contentType = getDetailFromContentHeader(str, MIME_PATTERN, "", 1);
                this.encoding = getDetailFromContentHeader(str, CHARSET_PATTERN, null, 2);
            } else {
                this.contentType = "";
                this.encoding = "UTF-8";
            }
            if (MULTIPART_FORM_DATA_HEADER.equalsIgnoreCase(this.contentType)) {
                this.boundary = getDetailFromContentHeader(str, BOUNDARY_PATTERN, null, 2);
            } else {
                this.boundary = null;
            }
        }

        private String getDetailFromContentHeader(String str, Pattern pattern, String str2, int i) {
            str = pattern.matcher(str);
            return str.find() != null ? str.group(i) : str2;
        }

        public String getContentTypeHeader() {
            return this.contentTypeHeader;
        }

        public String getContentType() {
            return this.contentType;
        }

        public String getEncoding() {
            return this.encoding == null ? "US-ASCII" : this.encoding;
        }

        public String getBoundary() {
            return this.boundary;
        }

        public boolean isMultipart() {
            return MULTIPART_FORM_DATA_HEADER.equalsIgnoreCase(this.contentType);
        }

        public ContentType tryUTF8() {
            if (this.encoding != null) {
                return this;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.contentTypeHeader);
            stringBuilder.append("; charset=UTF-8");
            return new ContentType(stringBuilder.toString());
        }
    }

    public static class Cookie {
        /* renamed from: e */
        private final String f56e;
        /* renamed from: n */
        private final String f57n;
        /* renamed from: v */
        private final String f58v;

        public static String getHTTPTime(int i) {
            Calendar instance = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            instance.add(5, i);
            return simpleDateFormat.format(instance.getTime());
        }

        public Cookie(String str, String str2) {
            this(str, str2, 30);
        }

        public Cookie(String str, String str2, int i) {
            this.f57n = str;
            this.f58v = str2;
            this.f56e = getHTTPTime(i);
        }

        public Cookie(String str, String str2, String str3) {
            this.f57n = str;
            this.f58v = str2;
            this.f56e = str3;
        }

        public String getHTTPHeader() {
            return String.format("%s=%s; expires=%s", new Object[]{this.f57n, this.f58v, this.f56e});
        }
    }

    public class CookieHandler implements Iterable<String> {
        private final HashMap<String, String> cookies = new HashMap();
        private final ArrayList<Cookie> queue = new ArrayList();

        public CookieHandler(Map<String, String> map) {
            String str = (String) map.get("cookie");
            if (str != null) {
                for (String trim : str.split(";")) {
                    String[] split = trim.trim().split("=");
                    if (split.length == 2) {
                        this.cookies.put(split[0], split[1]);
                    }
                }
            }
        }

        public void delete(String str) {
            set(str, "-delete-", -30);
        }

        public Iterator<String> iterator() {
            return this.cookies.keySet().iterator();
        }

        public String read(String str) {
            return (String) this.cookies.get(str);
        }

        public void set(Cookie cookie) {
            this.queue.add(cookie);
        }

        public void set(String str, String str2, int i) {
            this.queue.add(new Cookie(str, str2, Cookie.getHTTPTime(i)));
        }

        public void unloadQueue(Response response) {
            Iterator it = this.queue.iterator();
            while (it.hasNext()) {
                response.addHeader(HttpHeaders.SET_COOKIE, ((Cookie) it.next()).getHTTPHeader());
            }
        }
    }

    public static class DefaultAsyncRunner implements AsyncRunner {
        private long requestCount;
        private final List<ClientHandler> running = Collections.synchronizedList(new ArrayList());

        public List<ClientHandler> getRunning() {
            return this.running;
        }

        public void closeAll() {
            Iterator it = new ArrayList(this.running).iterator();
            while (it.hasNext()) {
                ((ClientHandler) it.next()).close();
            }
        }

        public void closed(ClientHandler clientHandler) {
            this.running.remove(clientHandler);
        }

        public void exec(ClientHandler clientHandler) {
            this.requestCount++;
            Thread thread = new Thread(clientHandler);
            thread.setDaemon(true);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("NanoHttpd Request Processor (#");
            stringBuilder.append(this.requestCount);
            stringBuilder.append(")");
            thread.setName(stringBuilder.toString());
            this.running.add(clientHandler);
            thread.start();
        }
    }

    public interface ServerSocketFactory {
        ServerSocket create() throws IOException;
    }

    public static class DefaultServerSocketFactory implements ServerSocketFactory {
        public ServerSocket create() throws IOException {
            return new ServerSocket();
        }
    }

    public interface TempFile {
        void delete() throws Exception;

        String getName();

        OutputStream open() throws Exception;
    }

    public static class DefaultTempFile implements TempFile {
        private final File file;
        private final OutputStream fstream = new FileOutputStream(this.file);

        public DefaultTempFile(File file) throws IOException {
            this.file = File.createTempFile("NanoHTTPD-", "", file);
        }

        public void delete() throws Exception {
            NanoHTTPD.safeClose(this.fstream);
            if (!this.file.delete()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("could not delete temporary file: ");
                stringBuilder.append(this.file.getAbsolutePath());
                throw new Exception(stringBuilder.toString());
            }
        }

        public String getName() {
            return this.file.getAbsolutePath();
        }

        public OutputStream open() throws Exception {
            return this.fstream;
        }
    }

    public interface TempFileManager {
        void clear();

        TempFile createTempFile(String str) throws Exception;
    }

    public static class DefaultTempFileManager implements TempFileManager {
        private final List<TempFile> tempFiles;
        private final File tmpdir = new File(System.getProperty("java.io.tmpdir"));

        public DefaultTempFileManager() {
            if (!this.tmpdir.exists()) {
                this.tmpdir.mkdirs();
            }
            this.tempFiles = new ArrayList();
        }

        public void clear() {
            for (TempFile delete : this.tempFiles) {
                try {
                    delete.delete();
                } catch (Throwable e) {
                    NanoHTTPD.LOG.log(Level.WARNING, "could not delete file ", e);
                }
            }
            this.tempFiles.clear();
        }

        public TempFile createTempFile(String str) throws Exception {
            str = new DefaultTempFile(this.tmpdir);
            this.tempFiles.add(str);
            return str;
        }
    }

    public interface TempFileManagerFactory {
        TempFileManager create();
    }

    private class DefaultTempFileManagerFactory implements TempFileManagerFactory {
        private DefaultTempFileManagerFactory() {
        }

        public TempFileManager create() {
            return new DefaultTempFileManager();
        }
    }

    public interface IHTTPSession {
        void execute() throws IOException;

        CookieHandler getCookies();

        Map<String, String> getHeaders();

        InputStream getInputStream();

        Method getMethod();

        Map<String, List<String>> getParameters();

        @Deprecated
        Map<String, String> getParms();

        String getQueryParameterString();

        String getRemoteHostName();

        String getRemoteIpAddress();

        String getUri();

        void parseBody(Map<String, String> map) throws IOException, ResponseException;
    }

    protected class HTTPSession implements IHTTPSession {
        public static final int BUFSIZE = 8192;
        public static final int MAX_HEADER_SIZE = 1024;
        private static final int MEMORY_STORE_LIMIT = 1024;
        private static final int REQUEST_BUFFER_LEN = 512;
        private CookieHandler cookies;
        private Map<String, String> headers;
        private final BufferedInputStream inputStream;
        private Method method;
        private final OutputStream outputStream;
        private Map<String, List<String>> parms;
        private String protocolVersion;
        private String queryParameterString;
        private String remoteHostname;
        private String remoteIp;
        private int rlen;
        private int splitbyte;
        private final TempFileManager tempFileManager;
        private String uri;

        public HTTPSession(TempFileManager tempFileManager, InputStream inputStream, OutputStream outputStream) {
            this.tempFileManager = tempFileManager;
            this.inputStream = new BufferedInputStream(inputStream, 8192);
            this.outputStream = outputStream;
        }

        public HTTPSession(TempFileManager tempFileManager, InputStream inputStream, OutputStream outputStream, InetAddress inetAddress) {
            NanoHTTPD str;
            this.tempFileManager = tempFileManager;
            this.inputStream = new BufferedInputStream(inputStream, 8192);
            this.outputStream = outputStream;
            if (inetAddress.isLoopbackAddress() == null) {
                if (inetAddress.isAnyLocalAddress() == null) {
                    str = inetAddress.getHostAddress().toString();
                    this.remoteIp = str;
                    if (inetAddress.isLoopbackAddress() == null) {
                        if (inetAddress.isAnyLocalAddress() != null) {
                            str = inetAddress.getHostName().toString();
                            this.remoteHostname = str;
                            this.headers = new HashMap();
                        }
                    }
                    str = "localhost";
                    this.remoteHostname = str;
                    this.headers = new HashMap();
                }
            }
            str = "127.0.0.1";
            this.remoteIp = str;
            if (inetAddress.isLoopbackAddress() == null) {
                if (inetAddress.isAnyLocalAddress() != null) {
                    str = inetAddress.getHostName().toString();
                    this.remoteHostname = str;
                    this.headers = new HashMap();
                }
            }
            str = "localhost";
            this.remoteHostname = str;
            this.headers = new HashMap();
        }

        private void decodeHeader(BufferedReader bufferedReader, Map<String, String> map, Map<String, List<String>> map2, Map<String, String> map3) throws ResponseException {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    StringTokenizer stringTokenizer = new StringTokenizer(readLine);
                    if (stringTokenizer.hasMoreTokens()) {
                        map.put("method", stringTokenizer.nextToken());
                        if (stringTokenizer.hasMoreTokens()) {
                            readLine = stringTokenizer.nextToken();
                            int indexOf = readLine.indexOf(63);
                            if (indexOf >= 0) {
                                decodeParms(readLine.substring(indexOf + 1), map2);
                                map2 = NanoHTTPD.decodePercent(readLine.substring(0, indexOf));
                            } else {
                                map2 = NanoHTTPD.decodePercent(readLine);
                            }
                            if (stringTokenizer.hasMoreTokens()) {
                                this.protocolVersion = stringTokenizer.nextToken();
                            } else {
                                this.protocolVersion = "HTTP/1.1";
                                NanoHTTPD.LOG.log(Level.FINE, "no protocol version specified, strange. Assuming HTTP/1.1.");
                            }
                            readLine = bufferedReader.readLine();
                            while (readLine != null && !readLine.trim().isEmpty()) {
                                int indexOf2 = readLine.indexOf(58);
                                if (indexOf2 >= 0) {
                                    map3.put(readLine.substring(0, indexOf2).trim().toLowerCase(Locale.US), readLine.substring(indexOf2 + 1).trim());
                                }
                                readLine = bufferedReader.readLine();
                            }
                            map.put("uri", map2);
                            return;
                        }
                        throw new ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Missing URI. Usage: GET /example/file.html");
                    }
                    throw new ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Syntax error. Usage: GET /example/file.html");
                }
            } catch (BufferedReader bufferedReader2) {
                map2 = Status.INTERNAL_ERROR;
                map3 = new StringBuilder();
                map3.append("SERVER INTERNAL ERROR: IOException: ");
                map3.append(bufferedReader2.getMessage());
                throw new ResponseException(map2, map3.toString(), bufferedReader2);
            }
        }

        private void decodeMultipartFormData(ContentType contentType, ByteBuffer byteBuffer, Map<String, List<String>> map, Map<String, String> map2) throws ResponseException {
            ByteBuffer byteBuffer2 = byteBuffer;
            Map<String, List<String>> map3 = map;
            Map<String, String> map4 = map2;
            try {
                int[] boundaryPositions = getBoundaryPositions(byteBuffer2, contentType.getBoundary().getBytes());
                int i = 2;
                if (boundaryPositions.length < 2) {
                    throw new ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but contains less than two boundary strings.");
                }
                int i2 = 1024;
                byte[] bArr = new byte[1024];
                int i3 = 0;
                int i4 = 0;
                int i5 = 0;
                while (i4 < boundaryPositions.length - 1) {
                    byteBuffer2.position(boundaryPositions[i4]);
                    int remaining = byteBuffer.remaining() < i2 ? byteBuffer.remaining() : 1024;
                    byteBuffer2.get(bArr, i3, remaining);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bArr, i3, remaining), Charset.forName(contentType.getEncoding())), remaining);
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        if (readLine.contains(contentType.getBoundary())) {
                            CharSequence readLine2 = bufferedReader.readLine();
                            String str = null;
                            String str2 = str;
                            int i6 = i5;
                            String str3 = str2;
                            i3 = 2;
                            while (readLine2 != null && readLine2.trim().length() > 0) {
                                Matcher matcher = NanoHTTPD.CONTENT_DISPOSITION_PATTERN.matcher(readLine2);
                                if (matcher.matches()) {
                                    String str4;
                                    Matcher matcher2 = NanoHTTPD.CONTENT_DISPOSITION_ATTRIBUTE_PATTERN.matcher(matcher.group(i));
                                    int i7 = i6;
                                    while (matcher2.find()) {
                                        str4 = str;
                                        str = matcher2.group(1);
                                        if ("name".equalsIgnoreCase(str)) {
                                            str3 = matcher2.group(2);
                                        } else if ("filename".equalsIgnoreCase(str)) {
                                            str = matcher2.group(2);
                                            if (!str.isEmpty()) {
                                                if (i7 > 0) {
                                                    StringBuilder stringBuilder = new StringBuilder();
                                                    stringBuilder.append(str3);
                                                    i5 = i7 + 1;
                                                    stringBuilder.append(String.valueOf(i7));
                                                    i7 = i5;
                                                    str3 = stringBuilder.toString();
                                                } else {
                                                    i7++;
                                                }
                                            }
                                            map4 = map2;
                                        }
                                        str = str4;
                                        map4 = map2;
                                    }
                                    str4 = str;
                                    i6 = i7;
                                }
                                Matcher matcher3 = NanoHTTPD.CONTENT_TYPE_PATTERN.matcher(readLine2);
                                if (matcher3.matches()) {
                                    str2 = matcher3.group(2).trim();
                                }
                                i3++;
                                Object readLine3 = bufferedReader.readLine();
                                map4 = map2;
                                i = 2;
                            }
                            int i8 = 0;
                            while (true) {
                                i = i3 - 1;
                                if (i3 <= 0) {
                                    break;
                                }
                                i8 = scipOverNewLine(bArr, i8);
                                i3 = i;
                            }
                            if (i8 >= remaining - 4) {
                                throw new ResponseException(Status.INTERNAL_ERROR, "Multipart header size exceeds MAX_HEADER_SIZE.");
                            }
                            Map map5;
                            i = boundaryPositions[i4] + i8;
                            i4++;
                            i8 = boundaryPositions[i4] - 4;
                            byteBuffer2.position(i);
                            List list = (List) map3.get(str3);
                            if (list == null) {
                                list = new ArrayList();
                                map3.put(str3, list);
                            }
                            if (str2 == null) {
                                byte[] bArr2 = new byte[(i8 - i)];
                                byteBuffer2.get(bArr2);
                                list.add(new String(bArr2, contentType.getEncoding()));
                                map5 = map2;
                            } else {
                                String saveTmpFile = saveTmpFile(byteBuffer2, i, i8 - i, str);
                                map5 = map2;
                                if (map5.containsKey(str3)) {
                                    StringBuilder stringBuilder2;
                                    remaining = 2;
                                    while (true) {
                                        stringBuilder2 = new StringBuilder();
                                        stringBuilder2.append(str3);
                                        stringBuilder2.append(remaining);
                                        if (!map5.containsKey(stringBuilder2.toString())) {
                                            break;
                                        }
                                        remaining++;
                                    }
                                    stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append(str3);
                                    stringBuilder2.append(remaining);
                                    map5.put(stringBuilder2.toString(), saveTmpFile);
                                } else {
                                    map5.put(str3, saveTmpFile);
                                }
                                list.add(str);
                            }
                            Map map6 = map5;
                            i5 = i6;
                            i2 = 1024;
                            i = 2;
                            i3 = 0;
                        }
                    }
                    throw new ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but chunk does not start with boundary.");
                }
            } catch (ResponseException e) {
                throw e;
            } catch (Exception e2) {
                throw new ResponseException(Status.INTERNAL_ERROR, e2.toString());
            }
        }

        private int scipOverNewLine(byte[] bArr, int i) {
            while (bArr[i] != (byte) 10) {
                i++;
            }
            return i + 1;
        }

        private void decodeParms(String str, Map<String, List<String>> map) {
            if (str == null) {
                this.queryParameterString = "";
                return;
            }
            this.queryParameterString = str;
            StringTokenizer stringTokenizer = new StringTokenizer(str, "&");
            while (stringTokenizer.hasMoreTokens() != null) {
                Object trim;
                str = stringTokenizer.nextToken();
                int indexOf = str.indexOf(61);
                if (indexOf >= 0) {
                    trim = NanoHTTPD.decodePercent(str.substring(0, indexOf)).trim();
                    str = NanoHTTPD.decodePercent(str.substring(indexOf + 1));
                } else {
                    trim = NanoHTTPD.decodePercent(str).trim();
                    str = "";
                }
                List list = (List) map.get(trim);
                if (list == null) {
                    list = new ArrayList();
                    map.put(trim, list);
                }
                list.add(str);
            }
        }

        public void execute() throws java.io.IOException {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r7 = this;
            r0 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            r1 = 0;
            r2 = new byte[r0];	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r3 = 0;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r7.splitbyte = r3;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r7.rlen = r3;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4 = r7.inputStream;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4.mark(r0);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4 = r7.inputStream;	 Catch:{ SSLException -> 0x019b, IOException -> 0x0189, SocketException -> 0x0217, SocketTimeoutException -> 0x0215, ResponseException -> 0x01a0 }
            r4 = r4.read(r2, r3, r0);	 Catch:{ SSLException -> 0x019b, IOException -> 0x0189, SocketException -> 0x0217, SocketTimeoutException -> 0x0215, ResponseException -> 0x01a0 }
            r5 = -1;
            if (r4 != r5) goto L_0x002a;
        L_0x0018:
            r0 = r7.inputStream;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            fi.iki.elonen.NanoHTTPD.safeClose(r0);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = r7.outputStream;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            fi.iki.elonen.NanoHTTPD.safeClose(r0);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = new java.net.SocketException;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r2 = "NanoHttpd Shutdown";	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0.<init>(r2);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            throw r0;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x002a:
            if (r4 <= 0) goto L_0x004b;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x002c:
            r5 = r7.rlen;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r5 = r5 + r4;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r7.rlen = r5;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4 = r7.rlen;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4 = r7.findHeaderEnd(r2, r4);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r7.splitbyte = r4;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4 = r7.splitbyte;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            if (r4 <= 0) goto L_0x003e;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x003d:
            goto L_0x004b;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x003e:
            r4 = r7.inputStream;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r5 = r7.rlen;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r6 = r7.rlen;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r6 = 8192 - r6;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4 = r4.read(r2, r5, r6);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            goto L_0x002a;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x004b:
            r0 = r7.splitbyte;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4 = r7.rlen;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            if (r0 >= r4) goto L_0x005e;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x0051:
            r0 = r7.inputStream;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0.reset();	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = r7.inputStream;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4 = r7.splitbyte;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4 = (long) r4;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0.skip(r4);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x005e:
            r0 = new java.util.HashMap;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0.<init>();	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r7.parms = r0;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = r7.headers;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            if (r0 != 0) goto L_0x0071;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x0069:
            r0 = new java.util.HashMap;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0.<init>();	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r7.headers = r0;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            goto L_0x0076;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x0071:
            r0 = r7.headers;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0.clear();	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x0076:
            r0 = new java.io.BufferedReader;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4 = new java.io.InputStreamReader;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r5 = new java.io.ByteArrayInputStream;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r6 = r7.rlen;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r5.<init>(r2, r3, r6);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4.<init>(r5);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0.<init>(r4);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r2 = new java.util.HashMap;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r2.<init>();	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4 = r7.parms;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r5 = r7.headers;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r7.decodeHeader(r0, r2, r4, r5);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = r7.remoteIp;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            if (r0 == 0) goto L_0x00a9;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x0097:
            r0 = r7.headers;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4 = "remote-addr";	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r5 = r7.remoteIp;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0.put(r4, r5);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = r7.headers;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4 = "http-client-ip";	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r5 = r7.remoteIp;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0.put(r4, r5);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x00a9:
            r0 = "method";	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = r2.get(r0);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = (java.lang.String) r0;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = fi.iki.elonen.NanoHTTPD.Method.lookup(r0);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r7.method = r0;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = r7.method;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            if (r0 != 0) goto L_0x00e1;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x00bb:
            r0 = new fi.iki.elonen.NanoHTTPD$ResponseException;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r3 = fi.iki.elonen.NanoHTTPD.Response.Status.BAD_REQUEST;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4 = new java.lang.StringBuilder;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4.<init>();	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r5 = "BAD REQUEST: Syntax error. HTTP verb ";	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4.append(r5);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r5 = "method";	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r2 = r2.get(r5);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r2 = (java.lang.String) r2;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4.append(r2);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r2 = " unhandled.";	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4.append(r2);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r2 = r4.toString();	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0.<init>(r3, r2);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            throw r0;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x00e1:
            r0 = "uri";	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = r2.get(r0);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = (java.lang.String) r0;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r7.uri = r0;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = new fi.iki.elonen.NanoHTTPD$CookieHandler;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r2 = fi.iki.elonen.NanoHTTPD.this;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4 = r7.headers;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0.<init>(r4);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r7.cookies = r0;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = r7.headers;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r2 = "connection";	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = r0.get(r2);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = (java.lang.String) r0;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r2 = "HTTP/1.1";	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4 = r7.protocolVersion;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r2 = r2.equals(r4);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r4 = 1;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            if (r2 == 0) goto L_0x0117;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x010b:
            if (r0 == 0) goto L_0x0115;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x010d:
            r2 = "(?i).*close.*";	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = r0.matches(r2);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            if (r0 != 0) goto L_0x0117;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x0115:
            r0 = 1;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            goto L_0x0118;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x0117:
            r0 = 0;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x0118:
            r2 = fi.iki.elonen.NanoHTTPD.this;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r2 = r2.serve(r7);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            if (r2 != 0) goto L_0x0142;
        L_0x0120:
            r0 = new fi.iki.elonen.NanoHTTPD$ResponseException;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r1 = fi.iki.elonen.NanoHTTPD.Response.Status.INTERNAL_ERROR;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r3 = "SERVER INTERNAL ERROR: Serve() returned a null response.";	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r0.<init>(r1, r3);	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            throw r0;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
        L_0x012a:
            r0 = move-exception;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r1 = r2;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            goto L_0x0219;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
        L_0x012e:
            r0 = move-exception;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r1 = r2;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            goto L_0x01a1;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
        L_0x0132:
            r0 = move-exception;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r1 = r2;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            goto L_0x01c3;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
        L_0x0136:
            r0 = move-exception;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r1 = r2;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            goto L_0x01ec;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
        L_0x013a:
            r0 = move-exception;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r1 = r2;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            goto L_0x0216;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
        L_0x013e:
            r0 = move-exception;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r1 = r2;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            goto L_0x0218;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
        L_0x0142:
            r1 = r7.headers;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r5 = "accept-encoding";	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r1 = r1.get(r5);	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r1 = (java.lang.String) r1;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r5 = r7.cookies;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r5.unloadQueue(r2);	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r5 = r7.method;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r2.setRequestMethod(r5);	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r5 = fi.iki.elonen.NanoHTTPD.this;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r5 = r5.useGzipWhenAccepted(r2);	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            if (r5 == 0) goto L_0x0169;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
        L_0x015e:
            if (r1 == 0) goto L_0x0169;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
        L_0x0160:
            r5 = "gzip";	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r1 = r1.contains(r5);	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            if (r1 == 0) goto L_0x0169;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
        L_0x0168:
            r3 = 1;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
        L_0x0169:
            r2.setGzipEncoding(r3);	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r2.setKeepAlive(r0);	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r1 = r7.outputStream;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r2.send(r1);	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            if (r0 == 0) goto L_0x0181;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
        L_0x0176:
            r0 = r2.isCloseConnection();	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            if (r0 == 0) goto L_0x017d;
        L_0x017c:
            goto L_0x0181;
        L_0x017d:
            fi.iki.elonen.NanoHTTPD.safeClose(r2);
            goto L_0x01bc;
        L_0x0181:
            r0 = new java.net.SocketException;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r1 = "NanoHttpd Shutdown";	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            r0.<init>(r1);	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
            throw r0;	 Catch:{ SocketException -> 0x013e, SocketTimeoutException -> 0x013a, SSLException -> 0x0136, IOException -> 0x0132, ResponseException -> 0x012e, all -> 0x012a }
        L_0x0189:
            r0 = r7.inputStream;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            fi.iki.elonen.NanoHTTPD.safeClose(r0);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = r7.outputStream;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            fi.iki.elonen.NanoHTTPD.safeClose(r0);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0 = new java.net.SocketException;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r2 = "NanoHttpd Shutdown";	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            r0.<init>(r2);	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            throw r0;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x019b:
            r0 = move-exception;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
            throw r0;	 Catch:{ SocketException -> 0x0217, SocketTimeoutException -> 0x0215, SSLException -> 0x01eb, IOException -> 0x01c2, ResponseException -> 0x01a0 }
        L_0x019d:
            r0 = move-exception;
            goto L_0x0219;
        L_0x01a0:
            r0 = move-exception;
        L_0x01a1:
            r2 = r0.getStatus();	 Catch:{ all -> 0x019d }
            r3 = "text/plain";	 Catch:{ all -> 0x019d }
            r0 = r0.getMessage();	 Catch:{ all -> 0x019d }
            r0 = fi.iki.elonen.NanoHTTPD.newFixedLengthResponse(r2, r3, r0);	 Catch:{ all -> 0x019d }
            r2 = r7.outputStream;	 Catch:{ all -> 0x019d }
            r0.send(r2);	 Catch:{ all -> 0x019d }
            r0 = r7.outputStream;	 Catch:{ all -> 0x019d }
            fi.iki.elonen.NanoHTTPD.safeClose(r0);	 Catch:{ all -> 0x019d }
        L_0x01b9:
            fi.iki.elonen.NanoHTTPD.safeClose(r1);
        L_0x01bc:
            r0 = r7.tempFileManager;
            r0.clear();
            goto L_0x0214;
        L_0x01c2:
            r0 = move-exception;
        L_0x01c3:
            r2 = fi.iki.elonen.NanoHTTPD.Response.Status.INTERNAL_ERROR;	 Catch:{ all -> 0x019d }
            r3 = "text/plain";	 Catch:{ all -> 0x019d }
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x019d }
            r4.<init>();	 Catch:{ all -> 0x019d }
            r5 = "SERVER INTERNAL ERROR: IOException: ";	 Catch:{ all -> 0x019d }
            r4.append(r5);	 Catch:{ all -> 0x019d }
            r0 = r0.getMessage();	 Catch:{ all -> 0x019d }
            r4.append(r0);	 Catch:{ all -> 0x019d }
            r0 = r4.toString();	 Catch:{ all -> 0x019d }
            r0 = fi.iki.elonen.NanoHTTPD.newFixedLengthResponse(r2, r3, r0);	 Catch:{ all -> 0x019d }
            r2 = r7.outputStream;	 Catch:{ all -> 0x019d }
            r0.send(r2);	 Catch:{ all -> 0x019d }
            r0 = r7.outputStream;	 Catch:{ all -> 0x019d }
            fi.iki.elonen.NanoHTTPD.safeClose(r0);	 Catch:{ all -> 0x019d }
            goto L_0x01b9;	 Catch:{ all -> 0x019d }
        L_0x01eb:
            r0 = move-exception;	 Catch:{ all -> 0x019d }
        L_0x01ec:
            r2 = fi.iki.elonen.NanoHTTPD.Response.Status.INTERNAL_ERROR;	 Catch:{ all -> 0x019d }
            r3 = "text/plain";	 Catch:{ all -> 0x019d }
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x019d }
            r4.<init>();	 Catch:{ all -> 0x019d }
            r5 = "SSL PROTOCOL FAILURE: ";	 Catch:{ all -> 0x019d }
            r4.append(r5);	 Catch:{ all -> 0x019d }
            r0 = r0.getMessage();	 Catch:{ all -> 0x019d }
            r4.append(r0);	 Catch:{ all -> 0x019d }
            r0 = r4.toString();	 Catch:{ all -> 0x019d }
            r0 = fi.iki.elonen.NanoHTTPD.newFixedLengthResponse(r2, r3, r0);	 Catch:{ all -> 0x019d }
            r2 = r7.outputStream;	 Catch:{ all -> 0x019d }
            r0.send(r2);	 Catch:{ all -> 0x019d }
            r0 = r7.outputStream;	 Catch:{ all -> 0x019d }
            fi.iki.elonen.NanoHTTPD.safeClose(r0);	 Catch:{ all -> 0x019d }
            goto L_0x01b9;	 Catch:{ all -> 0x019d }
        L_0x0214:
            return;	 Catch:{ all -> 0x019d }
        L_0x0215:
            r0 = move-exception;	 Catch:{ all -> 0x019d }
        L_0x0216:
            throw r0;	 Catch:{ all -> 0x019d }
        L_0x0217:
            r0 = move-exception;	 Catch:{ all -> 0x019d }
        L_0x0218:
            throw r0;	 Catch:{ all -> 0x019d }
        L_0x0219:
            fi.iki.elonen.NanoHTTPD.safeClose(r1);
            r1 = r7.tempFileManager;
            r1.clear();
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: fi.iki.elonen.NanoHTTPD.HTTPSession.execute():void");
        }

        private int findHeaderEnd(byte[] bArr, int i) {
            int i2 = 0;
            while (true) {
                int i3 = i2 + 1;
                if (i3 >= i) {
                    return 0;
                }
                if (bArr[i2] == Ascii.CR && bArr[i3] == (byte) 10) {
                    int i4 = i2 + 3;
                    if (i4 < i && bArr[i2 + 2] == Ascii.CR && bArr[i4] == (byte) 10) {
                        return i2 + 4;
                    }
                }
                if (bArr[i2] == (byte) 10 && bArr[i3] == (byte) 10) {
                    return i2 + 2;
                }
                i2 = i3;
            }
        }

        private int[] getBoundaryPositions(ByteBuffer byteBuffer, byte[] bArr) {
            Object obj = new int[0];
            if (byteBuffer.remaining() < bArr.length) {
                return obj;
            }
            Object obj2 = new byte[(bArr.length + 4096)];
            int remaining = byteBuffer.remaining() < obj2.length ? byteBuffer.remaining() : obj2.length;
            byteBuffer.get(obj2, 0, remaining);
            remaining -= bArr.length;
            Object obj3 = obj;
            int i = 0;
            while (true) {
                Object obj4 = obj3;
                int i2 = 0;
                while (i2 < remaining) {
                    Object obj5 = obj4;
                    for (int i3 = 0; i3 < bArr.length; i3++) {
                        if (obj2[i2 + i3] != bArr[i3]) {
                            break;
                        }
                        if (i3 == bArr.length - 1) {
                            Object obj6 = new int[(obj5.length + 1)];
                            System.arraycopy(obj5, 0, obj6, 0, obj5.length);
                            obj6[obj5.length] = i + i2;
                            obj5 = obj6;
                        }
                    }
                    i2++;
                    obj4 = obj5;
                }
                i += remaining;
                System.arraycopy(obj2, obj2.length - bArr.length, obj2, 0, bArr.length);
                remaining = obj2.length - bArr.length;
                if (byteBuffer.remaining() < remaining) {
                    remaining = byteBuffer.remaining();
                }
                byteBuffer.get(obj2, bArr.length, remaining);
                if (remaining <= 0) {
                    return obj4;
                }
                obj3 = obj4;
            }
        }

        public CookieHandler getCookies() {
            return this.cookies;
        }

        public final Map<String, String> getHeaders() {
            return this.headers;
        }

        public final InputStream getInputStream() {
            return this.inputStream;
        }

        public final Method getMethod() {
            return this.method;
        }

        @Deprecated
        public final Map<String, String> getParms() {
            Map<String, String> hashMap = new HashMap();
            for (String str : this.parms.keySet()) {
                hashMap.put(str, ((List) this.parms.get(str)).get(0));
            }
            return hashMap;
        }

        public final Map<String, List<String>> getParameters() {
            return this.parms;
        }

        public String getQueryParameterString() {
            return this.queryParameterString;
        }

        private RandomAccessFile getTmpBucket() {
            try {
                return new RandomAccessFile(this.tempFileManager.createTempFile(null).getName(), InternalZipConstants.WRITE_MODE);
            } catch (Throwable e) {
                throw new Error(e);
            }
        }

        public final String getUri() {
            return this.uri;
        }

        public long getBodySize() {
            if (this.headers.containsKey("content-length")) {
                return Long.parseLong((String) this.headers.get("content-length"));
            }
            return this.splitbyte < this.rlen ? (long) (this.rlen - this.splitbyte) : 0;
        }

        public void parseBody(Map<String, String> map) throws IOException, ResponseException {
            Throwable th;
            HTTPSession hTTPSession = this;
            Map<String, String> map2 = map;
            Object obj;
            try {
                DataOutput dataOutputStream;
                ByteArrayOutputStream byteArrayOutputStream;
                long bodySize = getBodySize();
                if (bodySize < 1024) {
                    OutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                    dataOutputStream = new DataOutputStream(byteArrayOutputStream2);
                    byteArrayOutputStream = byteArrayOutputStream2;
                    obj = null;
                } else {
                    obj = getTmpBucket();
                    byteArrayOutputStream = null;
                    dataOutputStream = obj;
                }
                try {
                    ByteBuffer wrap;
                    byte[] bArr = new byte[512];
                    while (hTTPSession.rlen >= 0 && bodySize > 0) {
                        hTTPSession.rlen = hTTPSession.inputStream.read(bArr, 0, (int) Math.min(bodySize, 512));
                        long j = bodySize - ((long) hTTPSession.rlen);
                        if (hTTPSession.rlen > 0) {
                            dataOutputStream.write(bArr, 0, hTTPSession.rlen);
                        }
                        bodySize = j;
                    }
                    if (byteArrayOutputStream != null) {
                        wrap = ByteBuffer.wrap(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size());
                    } else {
                        wrap = obj.getChannel().map(MapMode.READ_ONLY, 0, obj.length());
                        obj.seek(0);
                    }
                    if (Method.POST.equals(hTTPSession.method)) {
                        ContentType contentType = new ContentType((String) hTTPSession.headers.get("content-type"));
                        if (!contentType.isMultipart()) {
                            byte[] bArr2 = new byte[wrap.remaining()];
                            wrap.get(bArr2);
                            String trim = new String(bArr2, contentType.getEncoding()).trim();
                            if ("application/x-www-form-urlencoded".equalsIgnoreCase(contentType.getContentType())) {
                                decodeParms(trim, hTTPSession.parms);
                            } else if (trim.length() != 0) {
                                map2.put("postData", trim);
                            }
                        } else if (contentType.getBoundary() == null) {
                            throw new ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but boundary missing. Usage: GET /example/file.html");
                        } else {
                            decodeMultipartFormData(contentType, wrap, hTTPSession.parms, map2);
                        }
                    } else if (Method.PUT.equals(hTTPSession.method)) {
                        map2.put("content", saveTmpFile(wrap, 0, wrap.limit(), null));
                    }
                    NanoHTTPD.safeClose(obj);
                } catch (Throwable th2) {
                    th = th2;
                    NanoHTTPD.safeClose(obj);
                    throw th;
                }
            } catch (Throwable th22) {
                th = th22;
                obj = null;
                NanoHTTPD.safeClose(obj);
                throw th;
            }
        }

        private String saveTmpFile(ByteBuffer byteBuffer, int i, int i2, String str) {
            String str2 = "";
            if (i2 > 0) {
                Object obj = null;
                try {
                    str = this.tempFileManager.createTempFile(str);
                    byteBuffer = byteBuffer.duplicate();
                    FileOutputStream fileOutputStream = new FileOutputStream(str.getName());
                    try {
                        FileChannel channel = fileOutputStream.getChannel();
                        byteBuffer.position(i).limit(i + i2);
                        channel.write(byteBuffer.slice());
                        str2 = str.getName();
                        NanoHTTPD.safeClose(fileOutputStream);
                    } catch (Exception e) {
                        byteBuffer = e;
                        obj = fileOutputStream;
                        try {
                            throw new Error(byteBuffer);
                        } catch (Throwable th) {
                            byteBuffer = th;
                            NanoHTTPD.safeClose(obj);
                            throw byteBuffer;
                        }
                    } catch (Throwable th2) {
                        byteBuffer = th2;
                        obj = fileOutputStream;
                        NanoHTTPD.safeClose(obj);
                        throw byteBuffer;
                    }
                } catch (Exception e2) {
                    byteBuffer = e2;
                    throw new Error(byteBuffer);
                }
            }
            return str2;
        }

        public String getRemoteIpAddress() {
            return this.remoteIp;
        }

        public String getRemoteHostName() {
            return this.remoteHostname;
        }
    }

    public enum Method {
        GET,
        PUT,
        POST,
        DELETE,
        HEAD,
        OPTIONS,
        TRACE,
        CONNECT,
        PATCH,
        PROPFIND,
        PROPPATCH,
        MKCOL,
        MOVE,
        COPY,
        LOCK,
        UNLOCK;

        static fi.iki.elonen.NanoHTTPD.Method lookup(java.lang.String r1) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r0 = 0;
            if (r1 != 0) goto L_0x0004;
        L_0x0003:
            return r0;
        L_0x0004:
            r1 = valueOf(r1);	 Catch:{ IllegalArgumentException -> 0x0009 }
            return r1;
        L_0x0009:
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: fi.iki.elonen.NanoHTTPD.Method.lookup(java.lang.String):fi.iki.elonen.NanoHTTPD$Method");
        }
    }

    public static class Response implements Closeable {
        private boolean chunkedTransfer;
        private long contentLength;
        private InputStream data;
        private boolean encodeAsGzip;
        private final Map<String, String> header = new C13691();
        private boolean keepAlive;
        private final Map<String, String> lowerCaseHeader = new HashMap();
        private String mimeType;
        private Method requestMethod;
        private IStatus status;

        /* renamed from: fi.iki.elonen.NanoHTTPD$Response$1 */
        class C13691 extends HashMap<String, String> {
            C13691() {
            }

            public String put(String str, String str2) {
                Response.this.lowerCaseHeader.put(str == null ? str : str.toLowerCase(), str2);
                return (String) super.put(str, str2);
            }
        }

        private static class ChunkedOutputStream extends FilterOutputStream {
            public ChunkedOutputStream(OutputStream outputStream) {
                super(outputStream);
            }

            public void write(int i) throws IOException {
                write(new byte[]{(byte) i}, 0, 1);
            }

            public void write(byte[] bArr) throws IOException {
                write(bArr, 0, bArr.length);
            }

            public void write(byte[] bArr, int i, int i2) throws IOException {
                if (i2 != 0) {
                    this.out.write(String.format("%x\r\n", new Object[]{Integer.valueOf(i2)}).getBytes());
                    this.out.write(bArr, i, i2);
                    this.out.write(IOUtils.LINE_SEPARATOR_WINDOWS.getBytes());
                }
            }

            public void finish() throws IOException {
                this.out.write("0\r\n\r\n".getBytes());
            }
        }

        public interface IStatus {
            String getDescription();

            int getRequestStatus();
        }

        public enum Status implements IStatus {
            SWITCH_PROTOCOL(101, "Switching Protocols"),
            OK(Callback.DEFAULT_DRAG_ANIMATION_DURATION, "OK"),
            CREATED(201, "Created"),
            ACCEPTED(202, "Accepted"),
            NO_CONTENT(204, "No Content"),
            PARTIAL_CONTENT(206, "Partial Content"),
            MULTI_STATUS(207, "Multi-Status"),
            REDIRECT(301, "Moved Permanently"),
            FOUND(302, "Found"),
            REDIRECT_SEE_OTHER(303, "See Other"),
            NOT_MODIFIED(304, "Not Modified"),
            TEMPORARY_REDIRECT(StatusLine.HTTP_TEMP_REDIRECT, "Temporary Redirect"),
            BAD_REQUEST(400, "Bad Request"),
            UNAUTHORIZED(401, "Unauthorized"),
            FORBIDDEN(403, "Forbidden"),
            NOT_FOUND(Compress.HUFF_TABLE_SIZE, "Not Found"),
            METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
            NOT_ACCEPTABLE(406, "Not Acceptable"),
            REQUEST_TIMEOUT(408, "Request Timeout"),
            CONFLICT(409, "Conflict"),
            GONE(410, "Gone"),
            LENGTH_REQUIRED(411, "Length Required"),
            PRECONDITION_FAILED(412, "Precondition Failed"),
            PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
            UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
            RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable"),
            EXPECTATION_FAILED(417, "Expectation Failed"),
            TOO_MANY_REQUESTS(429, "Too Many Requests"),
            INTERNAL_ERROR(500, "Internal Server Error"),
            NOT_IMPLEMENTED(501, "Not Implemented"),
            SERVICE_UNAVAILABLE(503, "Service Unavailable"),
            UNSUPPORTED_HTTP_VERSION(505, "HTTP Version Not Supported");
            
            private final String description;
            private final int requestStatus;

            private Status(int i, String str) {
                this.requestStatus = i;
                this.description = str;
            }

            public static Status lookup(int i) {
                for (Status status : values()) {
                    if (status.getRequestStatus() == i) {
                        return status;
                    }
                }
                return 0;
            }

            public String getDescription() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("");
                stringBuilder.append(this.requestStatus);
                stringBuilder.append(StringUtils.SPACE);
                stringBuilder.append(this.description);
                return stringBuilder.toString();
            }

            public int getRequestStatus() {
                return this.requestStatus;
            }
        }

        protected Response(IStatus iStatus, String str, InputStream inputStream, long j) {
            this.status = iStatus;
            this.mimeType = str;
            boolean z = false;
            if (inputStream == null) {
                this.data = new ByteArrayInputStream(new byte[0]);
                this.contentLength = 0;
            } else {
                this.data = inputStream;
                this.contentLength = j;
            }
            if (this.contentLength < null) {
                z = true;
            }
            this.chunkedTransfer = z;
            this.keepAlive = true;
        }

        public void close() throws IOException {
            if (this.data != null) {
                this.data.close();
            }
        }

        public void addHeader(String str, String str2) {
            this.header.put(str, str2);
        }

        public void closeConnection(boolean z) {
            if (z) {
                this.header.put("connection", "close");
            } else {
                this.header.remove("connection");
            }
        }

        public boolean isCloseConnection() {
            return "close".equals(getHeader("connection"));
        }

        public InputStream getData() {
            return this.data;
        }

        public String getHeader(String str) {
            return (String) this.lowerCaseHeader.get(str.toLowerCase());
        }

        public String getMimeType() {
            return this.mimeType;
        }

        public Method getRequestMethod() {
            return this.requestMethod;
        }

        public IStatus getStatus() {
            return this.status;
        }

        public void setGzipEncoding(boolean z) {
            this.encodeAsGzip = z;
        }

        public void setKeepAlive(boolean z) {
            this.keepAlive = z;
        }

        protected void send(OutputStream outputStream) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                if (this.status == null) {
                    throw new Error("sendResponse(): Status can't be null.");
                }
                PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream, new ContentType(this.mimeType).getEncoding())), false);
                printWriter.append("HTTP/1.1 ").append(this.status.getDescription()).append(" \r\n");
                if (this.mimeType != null) {
                    printHeader(printWriter, "Content-Type", this.mimeType);
                }
                if (getHeader("date") == null) {
                    printHeader(printWriter, HttpHeaders.DATE, simpleDateFormat.format(new Date()));
                }
                for (Entry entry : this.header.entrySet()) {
                    printHeader(printWriter, (String) entry.getKey(), (String) entry.getValue());
                }
                if (getHeader("connection") == null) {
                    printHeader(printWriter, HttpHeaders.CONNECTION, this.keepAlive ? "keep-alive" : "close");
                }
                if (getHeader("content-length") != null) {
                    this.encodeAsGzip = false;
                }
                if (this.encodeAsGzip) {
                    printHeader(printWriter, "Content-Encoding", "gzip");
                    setChunkedTransfer(true);
                }
                long j = this.data != null ? this.contentLength : 0;
                if (this.requestMethod != Method.HEAD && this.chunkedTransfer) {
                    printHeader(printWriter, HttpHeaders.TRANSFER_ENCODING, "chunked");
                } else if (!this.encodeAsGzip) {
                    j = sendContentLengthHeaderIfNotAlreadyPresent(printWriter, j);
                }
                printWriter.append(IOUtils.LINE_SEPARATOR_WINDOWS);
                printWriter.flush();
                sendBodyWithCorrectTransferAndEncoding(outputStream, j);
                outputStream.flush();
                NanoHTTPD.safeClose(this.data);
            } catch (OutputStream outputStream2) {
                NanoHTTPD.LOG.log(Level.SEVERE, "Could not send response to the client", outputStream2);
            }
        }

        protected void printHeader(PrintWriter printWriter, String str, String str2) {
            printWriter.append(str).append(": ").append(str2).append(IOUtils.LINE_SEPARATOR_WINDOWS);
        }

        protected long sendContentLengthHeaderIfNotAlreadyPresent(java.io.PrintWriter r5, long r6) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r4 = this;
            r0 = "content-length";
            r0 = r4.getHeader(r0);
            if (r0 == 0) goto L_0x0026;
        L_0x0008:
            r1 = java.lang.Long.parseLong(r0);	 Catch:{ NumberFormatException -> 0x000e }
            r6 = r1;
            goto L_0x0026;
        L_0x000e:
            r1 = fi.iki.elonen.NanoHTTPD.LOG;
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "content-length was no number ";
            r2.append(r3);
            r2.append(r0);
            r0 = r2.toString();
            r1.severe(r0);
        L_0x0026:
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r1 = "Content-Length: ";
            r0.append(r1);
            r0.append(r6);
            r1 = "\r\n";
            r0.append(r1);
            r0 = r0.toString();
            r5.print(r0);
            return r6;
            */
            throw new UnsupportedOperationException("Method not decompiled: fi.iki.elonen.NanoHTTPD.Response.sendContentLengthHeaderIfNotAlreadyPresent(java.io.PrintWriter, long):long");
        }

        private void sendBodyWithCorrectTransferAndEncoding(OutputStream outputStream, long j) throws IOException {
            if (this.requestMethod == Method.HEAD || !this.chunkedTransfer) {
                sendBodyWithCorrectEncoding(outputStream, j);
                return;
            }
            j = new ChunkedOutputStream(outputStream);
            sendBodyWithCorrectEncoding(j, -1);
            j.finish();
        }

        private void sendBodyWithCorrectEncoding(OutputStream outputStream, long j) throws IOException {
            if (this.encodeAsGzip) {
                j = new GZIPOutputStream(outputStream);
                sendBody(j, -1);
                j.finish();
                return;
            }
            sendBody(outputStream, j);
        }

        private void sendBody(OutputStream outputStream, long j) throws IOException {
            byte[] bArr = new byte[((int) 16384)];
            Object obj = j == -1 ? 1 : null;
            while (true) {
                if (j > 0 || obj != null) {
                    int read = this.data.read(bArr, 0, (int) (obj != null ? PlaybackStateCompat.ACTION_PREPARE : Math.min(j, PlaybackStateCompat.ACTION_PREPARE)));
                    if (read > 0) {
                        outputStream.write(bArr, 0, read);
                        if (obj == null) {
                            j -= (long) read;
                        }
                    } else {
                        return;
                    }
                }
                return;
            }
        }

        public void setChunkedTransfer(boolean z) {
            this.chunkedTransfer = z;
        }

        public void setData(InputStream inputStream) {
            this.data = inputStream;
        }

        public void setMimeType(String str) {
            this.mimeType = str;
        }

        public void setRequestMethod(Method method) {
            this.requestMethod = method;
        }

        public void setStatus(IStatus iStatus) {
            this.status = iStatus;
        }
    }

    public static final class ResponseException extends Exception {
        private static final long serialVersionUID = 6569838532917408380L;
        private final Status status;

        public ResponseException(Status status, String str) {
            super(str);
            this.status = status;
        }

        public ResponseException(Status status, String str, Exception exception) {
            super(str, exception);
            this.status = status;
        }

        public Status getStatus() {
            return this.status;
        }
    }

    public static class SecureServerSocketFactory implements ServerSocketFactory {
        private String[] sslProtocols;
        private SSLServerSocketFactory sslServerSocketFactory;

        public SecureServerSocketFactory(SSLServerSocketFactory sSLServerSocketFactory, String[] strArr) {
            this.sslServerSocketFactory = sSLServerSocketFactory;
            this.sslProtocols = strArr;
        }

        public ServerSocket create() throws IOException {
            SSLServerSocket sSLServerSocket = (SSLServerSocket) this.sslServerSocketFactory.createServerSocket();
            if (this.sslProtocols != null) {
                sSLServerSocket.setEnabledProtocols(this.sslProtocols);
            } else {
                sSLServerSocket.setEnabledProtocols(sSLServerSocket.getSupportedProtocols());
            }
            sSLServerSocket.setUseClientMode(false);
            sSLServerSocket.setWantClientAuth(false);
            sSLServerSocket.setNeedClientAuth(false);
            return sSLServerSocket;
        }
    }

    public class ServerRunnable implements Runnable {
        private IOException bindException;
        private boolean hasBinded = null;
        private final int timeout;

        public ServerRunnable(int i) {
            this.timeout = i;
        }

        public void run() {
            try {
                NanoHTTPD.this.myServerSocket.bind(NanoHTTPD.this.hostname != null ? new InetSocketAddress(NanoHTTPD.this.hostname, NanoHTTPD.this.myPort) : new InetSocketAddress(NanoHTTPD.this.myPort));
                this.hasBinded = true;
                do {
                    try {
                        Socket accept = NanoHTTPD.this.myServerSocket.accept();
                        if (this.timeout > 0) {
                            accept.setSoTimeout(this.timeout);
                        }
                        NanoHTTPD.this.asyncRunner.exec(NanoHTTPD.this.createClientHandler(accept, accept.getInputStream()));
                    } catch (Throwable e) {
                        NanoHTTPD.LOG.log(Level.FINE, "Communication with the client broken", e);
                    }
                } while (!NanoHTTPD.this.myServerSocket.isClosed());
            } catch (IOException e2) {
                this.bindException = e2;
            }
        }
    }

    public static Map<String, String> mimeTypes() {
        if (MIME_TYPES == null) {
            MIME_TYPES = new HashMap();
            loadMimeTypes(MIME_TYPES, "META-INF/nanohttpd/default-mimetypes.properties");
            loadMimeTypes(MIME_TYPES, "META-INF/nanohttpd/mimetypes.properties");
            if (MIME_TYPES.isEmpty()) {
                LOG.log(Level.WARNING, "no mime types found in the classpath! please provide mimetypes.properties");
            }
        }
        return MIME_TYPES;
    }

    private static void loadMimeTypes(java.util.Map<java.lang.String, java.lang.String> r10, java.lang.String r11) {
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
        r0 = fi.iki.elonen.NanoHTTPD.class;	 Catch:{ IOException -> 0x0053 }
        r0 = r0.getClassLoader();	 Catch:{ IOException -> 0x0053 }
        r0 = r0.getResources(r11);	 Catch:{ IOException -> 0x0053 }
    L_0x000a:
        r1 = r0.hasMoreElements();	 Catch:{ IOException -> 0x0053 }
        if (r1 == 0) goto L_0x006b;	 Catch:{ IOException -> 0x0053 }
    L_0x0010:
        r1 = r0.nextElement();	 Catch:{ IOException -> 0x0053 }
        r1 = (java.net.URL) r1;	 Catch:{ IOException -> 0x0053 }
        r2 = new java.util.Properties;	 Catch:{ IOException -> 0x0053 }
        r2.<init>();	 Catch:{ IOException -> 0x0053 }
        r3 = 0;
        r4 = r1.openStream();	 Catch:{ IOException -> 0x002e, all -> 0x002b }
        r2.load(r4);	 Catch:{ IOException -> 0x0029 }
    L_0x0023:
        safeClose(r4);	 Catch:{ IOException -> 0x0053 }
        goto L_0x004b;
    L_0x0027:
        r10 = move-exception;
        goto L_0x004f;
    L_0x0029:
        r3 = move-exception;
        goto L_0x0032;
    L_0x002b:
        r10 = move-exception;
        r4 = r3;
        goto L_0x004f;
    L_0x002e:
        r4 = move-exception;
        r9 = r4;
        r4 = r3;
        r3 = r9;
    L_0x0032:
        r5 = LOG;	 Catch:{ all -> 0x0027 }
        r6 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x0027 }
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0027 }
        r7.<init>();	 Catch:{ all -> 0x0027 }
        r8 = "could not load mimetypes from ";	 Catch:{ all -> 0x0027 }
        r7.append(r8);	 Catch:{ all -> 0x0027 }
        r7.append(r1);	 Catch:{ all -> 0x0027 }
        r1 = r7.toString();	 Catch:{ all -> 0x0027 }
        r5.log(r6, r1, r3);	 Catch:{ all -> 0x0027 }
        goto L_0x0023;
    L_0x004b:
        r10.putAll(r2);	 Catch:{ IOException -> 0x0053 }
        goto L_0x000a;	 Catch:{ IOException -> 0x0053 }
    L_0x004f:
        safeClose(r4);	 Catch:{ IOException -> 0x0053 }
        throw r10;	 Catch:{ IOException -> 0x0053 }
    L_0x0053:
        r10 = LOG;
        r0 = java.util.logging.Level.INFO;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "no mime types available at ";
        r1.append(r2);
        r1.append(r11);
        r11 = r1.toString();
        r10.log(r0, r11);
    L_0x006b:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: fi.iki.elonen.NanoHTTPD.loadMimeTypes(java.util.Map, java.lang.String):void");
    }

    public static SSLServerSocketFactory makeSSLSocketFactory(KeyStore keyStore, KeyManager[] keyManagerArr) throws IOException {
        try {
            TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            instance.init(keyStore);
            keyStore = SSLContext.getInstance("TLS");
            keyStore.init(keyManagerArr, instance.getTrustManagers(), null);
            return keyStore.getServerSocketFactory();
        } catch (KeyStore keyStore2) {
            throw new IOException(keyStore2.getMessage());
        }
    }

    public static SSLServerSocketFactory makeSSLSocketFactory(KeyStore keyStore, KeyManagerFactory keyManagerFactory) throws IOException {
        try {
            return makeSSLSocketFactory(keyStore, keyManagerFactory.getKeyManagers());
        } catch (KeyStore keyStore2) {
            throw new IOException(keyStore2.getMessage());
        }
    }

    public static SSLServerSocketFactory makeSSLSocketFactory(String str, char[] cArr) throws IOException {
        try {
            KeyStore instance = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream resourceAsStream = NanoHTTPD.class.getResourceAsStream(str);
            if (resourceAsStream == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to load keystore from classpath: ");
                stringBuilder.append(str);
                throw new IOException(stringBuilder.toString());
            }
            instance.load(resourceAsStream, cArr);
            KeyManagerFactory instance2 = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            instance2.init(instance, cArr);
            return makeSSLSocketFactory(instance, instance2);
        } catch (String str2) {
            throw new IOException(str2.getMessage());
        }
    }

    public static String getMimeTypeForFile(String str) {
        int lastIndexOf = str.lastIndexOf(46);
        str = lastIndexOf >= 0 ? (String) mimeTypes().get(str.substring(lastIndexOf + 1).toLowerCase()) : null;
        return str == null ? "application/octet-stream" : str;
    }

    private static final void safeClose(Object obj) {
        if (obj != null) {
            try {
                if (obj instanceof Closeable) {
                    ((Closeable) obj).close();
                } else if (obj instanceof Socket) {
                    ((Socket) obj).close();
                } else if (obj instanceof ServerSocket) {
                    ((ServerSocket) obj).close();
                } else {
                    throw new IllegalArgumentException("Unknown object to close");
                }
            } catch (Object obj2) {
                LOG.log(Level.SEVERE, "Could not close", obj2);
            }
        }
    }

    public NanoHTTPD(int i) {
        this(null, i);
    }

    public NanoHTTPD(String str, int i) {
        this.serverSocketFactory = new DefaultServerSocketFactory();
        this.hostname = str;
        this.myPort = i;
        setTempFileManagerFactory(new DefaultTempFileManagerFactory());
        setAsyncRunner(new DefaultAsyncRunner());
    }

    public synchronized void closeAllConnections() {
        stop();
    }

    protected ClientHandler createClientHandler(Socket socket, InputStream inputStream) {
        return new ClientHandler(inputStream, socket);
    }

    protected ServerRunnable createServerRunnable(int i) {
        return new ServerRunnable(i);
    }

    protected static Map<String, List<String>> decodeParameters(Map<String, String> map) {
        return decodeParameters((String) map.get(QUERY_STRING_PARAMETER));
    }

    protected static Map<String, List<String>> decodeParameters(String str) {
        Map<String, List<String>> hashMap = new HashMap();
        if (str != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(str, "&");
            while (stringTokenizer.hasMoreTokens() != null) {
                str = stringTokenizer.nextToken();
                int indexOf = str.indexOf(61);
                String trim = (indexOf >= 0 ? decodePercent(str.substring(0, indexOf)) : decodePercent(str)).trim();
                if (!hashMap.containsKey(trim)) {
                    hashMap.put(trim, new ArrayList());
                }
                str = indexOf >= 0 ? decodePercent(str.substring(indexOf + 1)) : null;
                if (str != null) {
                    ((List) hashMap.get(trim)).add(str);
                }
            }
        }
        return hashMap;
    }

    protected static String decodePercent(String str) {
        try {
            return URLDecoder.decode(str, InternalZipConstants.CHARSET_UTF8);
        } catch (String str2) {
            LOG.log(Level.WARNING, "Encoding not supported, ignored", str2);
            return null;
        }
    }

    protected boolean useGzipWhenAccepted(Response response) {
        return (response.getMimeType() == null || (!response.getMimeType().toLowerCase().contains("text/") && response.getMimeType().toLowerCase().contains("/json") == null)) ? null : true;
    }

    public final int getListeningPort() {
        return this.myServerSocket == null ? -1 : this.myServerSocket.getLocalPort();
    }

    public final boolean isAlive() {
        return wasStarted() && !this.myServerSocket.isClosed() && this.myThread.isAlive();
    }

    public ServerSocketFactory getServerSocketFactory() {
        return this.serverSocketFactory;
    }

    public void setServerSocketFactory(ServerSocketFactory serverSocketFactory) {
        this.serverSocketFactory = serverSocketFactory;
    }

    public String getHostname() {
        return this.hostname;
    }

    public TempFileManagerFactory getTempFileManagerFactory() {
        return this.tempFileManagerFactory;
    }

    public void makeSecure(SSLServerSocketFactory sSLServerSocketFactory, String[] strArr) {
        this.serverSocketFactory = new SecureServerSocketFactory(sSLServerSocketFactory, strArr);
    }

    public static Response newChunkedResponse(IStatus iStatus, String str, InputStream inputStream) {
        return new Response(iStatus, str, inputStream, -1);
    }

    public static Response newFixedLengthResponse(IStatus iStatus, String str, InputStream inputStream, long j) {
        return new Response(iStatus, str, inputStream, j);
    }

    public static Response newFixedLengthResponse(IStatus iStatus, String str, String str2) {
        ContentType contentType = new ContentType(str);
        if (str2 == null) {
            return newFixedLengthResponse(iStatus, str, new ByteArrayInputStream(new byte[0]), 0);
        }
        try {
            if (Charset.forName(contentType.getEncoding()).newEncoder().canEncode(str2) == null) {
                contentType = contentType.tryUTF8();
            }
            str = str2.getBytes(contentType.getEncoding());
        } catch (String str3) {
            LOG.log(Level.SEVERE, "encoding problem, responding nothing", str3);
            str3 = new byte[0];
        }
        return newFixedLengthResponse(iStatus, contentType.getContentTypeHeader(), new ByteArrayInputStream(str3), (long) str3.length);
    }

    public static Response newFixedLengthResponse(String str) {
        return newFixedLengthResponse(Status.OK, MIME_HTML, str);
    }

    public Response serve(IHTTPSession iHTTPSession) {
        Map hashMap = new HashMap();
        Method method = iHTTPSession.getMethod();
        if (Method.PUT.equals(method) || Method.POST.equals(method)) {
            try {
                iHTTPSession.parseBody(hashMap);
            } catch (IHTTPSession iHTTPSession2) {
                IStatus iStatus = Status.INTERNAL_ERROR;
                String str = MIME_PLAINTEXT;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("SERVER INTERNAL ERROR: IOException: ");
                stringBuilder.append(iHTTPSession2.getMessage());
                return newFixedLengthResponse(iStatus, str, stringBuilder.toString());
            } catch (IHTTPSession iHTTPSession22) {
                return newFixedLengthResponse(iHTTPSession22.getStatus(), MIME_PLAINTEXT, iHTTPSession22.getMessage());
            }
        }
        Map parms = iHTTPSession22.getParms();
        parms.put(QUERY_STRING_PARAMETER, iHTTPSession22.getQueryParameterString());
        return serve(iHTTPSession22.getUri(), method, iHTTPSession22.getHeaders(), parms, hashMap);
    }

    @Deprecated
    public Response serve(String str, Method method, Map<String, String> map, Map<String, String> map2, Map<String, String> map3) {
        return newFixedLengthResponse(Status.NOT_FOUND, MIME_PLAINTEXT, "Not Found");
    }

    public void setAsyncRunner(AsyncRunner asyncRunner) {
        this.asyncRunner = asyncRunner;
    }

    public void setTempFileManagerFactory(TempFileManagerFactory tempFileManagerFactory) {
        this.tempFileManagerFactory = tempFileManagerFactory;
    }

    public void start() throws IOException {
        start(5000);
    }

    public void start(int i) throws IOException {
        start(i, true);
    }

    public void start(int r3, boolean r4) throws java.io.IOException {
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
        r2 = this;
        r0 = r2.getServerSocketFactory();
        r0 = r0.create();
        r2.myServerSocket = r0;
        r0 = r2.myServerSocket;
        r1 = 1;
        r0.setReuseAddress(r1);
        r3 = r2.createServerRunnable(r3);
        r0 = new java.lang.Thread;
        r0.<init>(r3);
        r2.myThread = r0;
        r0 = r2.myThread;
        r0.setDaemon(r4);
        r4 = r2.myThread;
        r0 = "NanoHttpd Main Listener";
        r4.setName(r0);
        r4 = r2.myThread;
        r4.start();
    L_0x002c:
        r4 = r3.hasBinded;
        if (r4 != 0) goto L_0x003e;
    L_0x0032:
        r4 = r3.bindException;
        if (r4 != 0) goto L_0x003e;
    L_0x0038:
        r0 = 10;
        java.lang.Thread.sleep(r0);	 Catch:{ Throwable -> 0x002c }
        goto L_0x002c;
    L_0x003e:
        r4 = r3.bindException;
        if (r4 == 0) goto L_0x0049;
    L_0x0044:
        r3 = r3.bindException;
        throw r3;
    L_0x0049:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: fi.iki.elonen.NanoHTTPD.start(int, boolean):void");
    }

    public void stop() {
        try {
            safeClose(this.myServerSocket);
            this.asyncRunner.closeAll();
            if (this.myThread != null) {
                this.myThread.join();
            }
        } catch (Throwable e) {
            LOG.log(Level.SEVERE, "Could not stop all connections", e);
        }
    }

    public final boolean wasStarted() {
        return (this.myServerSocket == null || this.myThread == null) ? false : true;
    }
}
