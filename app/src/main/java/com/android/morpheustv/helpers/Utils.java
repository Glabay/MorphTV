package com.android.morpheustv.helpers;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Base64;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.gms.cast.framework.media.NotificationOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noname.titan.R;
import ir.mahdi.mzip.rar.RarArchive;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.mozilla.universalchardet.UniversalDetector;

public class Utils {
    private static HashMap<String, String> cache = new HashMap();

    /* renamed from: com.android.morpheustv.helpers.Utils$1 */
    static class C05121 implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        C05121() {
        }
    }

    /* renamed from: com.android.morpheustv.helpers.Utils$2 */
    static class C05132 extends TypeToken<HashMap<String, String>> {
        C05132() {
        }
    }

    public static String getColorForMs(long j) {
        String str = "yellow";
        if (j < 2000) {
            str = "green";
        }
        return j > NotificationOptions.SKIP_STEP_TEN_SECONDS_IN_MS ? "red" : str;
    }

    public static double stringSimilarity(String str, String str2) {
        if (str.length() < str2.length()) {
            String str3 = str2;
            str2 = str;
            str = str3;
        }
        int length = str.length();
        if (length == 0) {
            return 0;
        }
        return ((double) (length - editDistance(str, str2))) / ((double) length);
    }

    private static int editDistance(String str, String str2) {
        str = str.toLowerCase();
        str2 = str2.toLowerCase();
        int[] iArr = new int[(str2.length() + 1)];
        for (int i = 0; i <= str.length(); i++) {
            int i2 = i;
            for (int i3 = 0; i3 <= str2.length(); i3++) {
                if (i == 0) {
                    iArr[i3] = i3;
                } else if (i3 > 0) {
                    int i4 = i3 - 1;
                    int i5 = iArr[i4];
                    if (str.charAt(i - 1) != str2.charAt(i4)) {
                        i5 = Math.min(Math.min(i5, i2), iArr[i3]) + 1;
                    }
                    iArr[i4] = i2;
                    i2 = i5;
                }
            }
            if (i > 0) {
                iArr[str2.length()] = i2;
            }
        }
        return iArr[str2.length()];
    }

    public static String getFilenameFromUrl(String str, String str2) {
        try {
            if (!str.startsWith("http")) {
                return str2;
            }
            str = URLDecoder.decode(FilenameUtils.getBaseName(new URL(str).getPath()), "UTF-8");
            return (str == null || str.isEmpty() || str.length() <= 15) ? str2 : str;
        } catch (String str3) {
            str3.printStackTrace();
            return str2;
        }
    }

    public static void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    public static boolean unzip(File file, File file2) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)));
        file = new byte[8192];
        boolean z = false;
        while (true) {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry != null) {
                File file3 = new File(file2, nextEntry.getName());
                File parentFile = nextEntry.isDirectory() ? file3 : file3.getParentFile();
                if (!parentFile.isDirectory() && !parentFile.mkdirs()) {
                    file2 = new StringBuilder();
                    file2.append("Failed to ensure directory: ");
                    file2.append(parentFile.getAbsolutePath());
                    throw new FileNotFoundException(file2.toString());
                } else if (!nextEntry.isDirectory()) {
                    FileOutputStream fileOutputStream = new FileOutputStream(file3);
                    while (true) {
                        try {
                            int read = zipInputStream.read(file);
                            if (read == -1) {
                                break;
                            }
                            fileOutputStream.write(file, 0, read);
                        } catch (Throwable th) {
                            zipInputStream.close();
                        }
                    }
                    fileOutputStream.close();
                    z = true;
                }
            } else {
                zipInputStream.close();
                return z;
            }
        }
    }

    public static void unrar(File file, File file2) {
        RarArchive rarArchive = new RarArchive();
        RarArchive.extractArchive(file, file2);
    }

    public static String formatSize(Context context, long j) {
        return Formatter.formatShortFileSize(context, j);
    }

    public static String humanReadableByteCount(long j, boolean z) {
        int i = z ? 1000 : 1024;
        if (j < ((long) i)) {
            z = new StringBuilder();
            z.append(j);
            z.append(" B");
            return z.toString();
        }
        i = (int) (Math.log((double) j) / Math.log((double) i));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("KMGTPE".charAt(i - 1));
        stringBuilder.append(z ? "" : "i");
        z = stringBuilder.toString();
        return String.format("%.1f %sB", new Object[]{Double.valueOf(j / Math.pow(r3, (double) i)), z});
    }

    public static java.lang.String getIP() {
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
        r0 = java.net.NetworkInterface.getNetworkInterfaces();	 Catch:{ Exception -> 0x0068 }
        r0 = java.util.Collections.list(r0);	 Catch:{ Exception -> 0x0068 }
        r0 = r0.iterator();	 Catch:{ Exception -> 0x0068 }
    L_0x000c:
        r1 = r0.hasNext();	 Catch:{ Exception -> 0x0068 }
        if (r1 == 0) goto L_0x0068;	 Catch:{ Exception -> 0x0068 }
    L_0x0012:
        r1 = r0.next();	 Catch:{ Exception -> 0x0068 }
        r1 = (java.net.NetworkInterface) r1;	 Catch:{ Exception -> 0x0068 }
        r2 = r1.getName();	 Catch:{ Exception -> 0x0068 }
        r2 = r2.toLowerCase();	 Catch:{ Exception -> 0x0068 }
        r3 = "wlan";	 Catch:{ Exception -> 0x0068 }
        r2 = r2.startsWith(r3);	 Catch:{ Exception -> 0x0068 }
        if (r2 != 0) goto L_0x0038;	 Catch:{ Exception -> 0x0068 }
    L_0x0028:
        r2 = r1.getName();	 Catch:{ Exception -> 0x0068 }
        r2 = r2.toLowerCase();	 Catch:{ Exception -> 0x0068 }
        r3 = "eth";	 Catch:{ Exception -> 0x0068 }
        r2 = r2.startsWith(r3);	 Catch:{ Exception -> 0x0068 }
        if (r2 == 0) goto L_0x000c;	 Catch:{ Exception -> 0x0068 }
    L_0x0038:
        r1 = r1.getInetAddresses();	 Catch:{ Exception -> 0x0068 }
        r1 = java.util.Collections.list(r1);	 Catch:{ Exception -> 0x0068 }
        r1 = r1.iterator();	 Catch:{ Exception -> 0x0068 }
    L_0x0044:
        r2 = r1.hasNext();	 Catch:{ Exception -> 0x0068 }
        if (r2 == 0) goto L_0x000c;	 Catch:{ Exception -> 0x0068 }
    L_0x004a:
        r2 = r1.next();	 Catch:{ Exception -> 0x0068 }
        r2 = (java.net.InetAddress) r2;	 Catch:{ Exception -> 0x0068 }
        r3 = r2.isLoopbackAddress();	 Catch:{ Exception -> 0x0068 }
        if (r3 != 0) goto L_0x0044;	 Catch:{ Exception -> 0x0068 }
    L_0x0056:
        r2 = r2.getHostAddress();	 Catch:{ Exception -> 0x0068 }
        r3 = 58;	 Catch:{ Exception -> 0x0068 }
        r3 = r2.indexOf(r3);	 Catch:{ Exception -> 0x0068 }
        if (r3 >= 0) goto L_0x0064;
    L_0x0062:
        r3 = 1;
        goto L_0x0065;
    L_0x0064:
        r3 = 0;
    L_0x0065:
        if (r3 == 0) goto L_0x0044;
    L_0x0067:
        return r2;
    L_0x0068:
        r0 = "127.0.0.1";
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.morpheustv.helpers.Utils.getIP():java.lang.String");
    }

    public static void disableSSLCertificateChecking() {
        TrustManager[] trustManagerArr = new TrustManager[]{new C05121()};
        try {
            SSLContext instance = SSLContext.getInstance("TLS");
            instance.init(null, trustManagerArr, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(instance.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
        }
    }

    public static void deleteCache(android.content.Context r0) {
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
        r0 = r0.getCacheDir();	 Catch:{ Exception -> 0x0007 }
        deleteDir(r0);	 Catch:{ Exception -> 0x0007 }
    L_0x0007:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.morpheustv.helpers.Utils.deleteCache(android.content.Context):void");
    }

    public static boolean deleteDir(File file) {
        if (file != null && file.isDirectory()) {
            String[] list = file.list();
            for (String file2 : list) {
                if (!deleteDir(new File(file, file2))) {
                    return false;
                }
            }
            return file.delete();
        } else if (file == null || !file.isFile()) {
            return false;
        } else {
            return file.delete();
        }
    }

    public static void saveCacheToDisk() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "/morpheustv/");
            if (!file.exists()) {
                file.mkdir();
            }
            if (file.exists()) {
                File file2 = new File(file, "cache.json");
                if (file2.exists()) {
                    file2.delete();
                }
                FileUtils.write(file2, new Gson().toJson(cache));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadCacheFromDisk() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "/morpheustv/");
            if (!file.exists()) {
                file.mkdir();
            }
            if (file.exists()) {
                File file2 = new File(file, "cache.json");
                if (file2.exists()) {
                    cache = (HashMap) new Gson().fromJson(FileUtils.readFileToString(file2), new C05132().getType());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cache == null) {
            cache = new HashMap();
        }
    }

    public static void saveCache(String str, String str2) {
        try {
            if (cache == null) {
                cache = new HashMap();
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CACHE_EXPIRE_");
            stringBuilder.append(str);
            String stringBuilder2 = stringBuilder.toString();
            if (str2 == null) {
                cache.remove(str);
                cache.remove(stringBuilder2);
                return;
            }
            cache.put(str, str2);
            cache.put(stringBuilder2, String.valueOf(System.currentTimeMillis() + 18000000));
        } catch (String str3) {
            str3.printStackTrace();
        }
    }

    public static String loadCache(String str) {
        try {
            if (cache == null) {
                cache = new HashMap();
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CACHE_EXPIRE_");
            stringBuilder.append(str);
            String stringBuilder2 = stringBuilder.toString();
            String str2 = (String) cache.get(stringBuilder2);
            if (str2 == null || str2.isEmpty()) {
                str2 = "0";
            }
            if (System.currentTimeMillis() < Long.parseLong(str2)) {
                return (String) cache.get(str);
            }
            cache.remove(str);
            cache.remove(stringBuilder2);
            return null;
        } catch (String str3) {
            str3.printStackTrace();
            return null;
        }
    }

    public static void copyAsset(android.content.Context r1, int r2, java.io.File r3) {
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
        r0 = 0;
        r1 = r1.getResources();	 Catch:{ IOException -> 0x0029, all -> 0x0026 }
        r1 = r1.openRawResource(r2);	 Catch:{ IOException -> 0x0029, all -> 0x0026 }
        r2 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x0022, all -> 0x0020 }
        r2.<init>(r3);	 Catch:{ IOException -> 0x0022, all -> 0x0020 }
        copyFile(r1, r2);	 Catch:{ IOException -> 0x001e, all -> 0x001c }
        if (r1 == 0) goto L_0x0016;
    L_0x0013:
        r1.close();	 Catch:{ IOException -> 0x0016 }
    L_0x0016:
        if (r2 == 0) goto L_0x0036;
    L_0x0018:
        r2.close();	 Catch:{ IOException -> 0x0036 }
        goto L_0x0036;
    L_0x001c:
        r3 = move-exception;
        goto L_0x0039;
    L_0x001e:
        r3 = move-exception;
        goto L_0x0024;
    L_0x0020:
        r3 = move-exception;
        goto L_0x003a;
    L_0x0022:
        r3 = move-exception;
        r2 = r0;
    L_0x0024:
        r0 = r1;
        goto L_0x002b;
    L_0x0026:
        r3 = move-exception;
        r1 = r0;
        goto L_0x003a;
    L_0x0029:
        r3 = move-exception;
        r2 = r0;
    L_0x002b:
        r3.printStackTrace();	 Catch:{ all -> 0x0037 }
        if (r0 == 0) goto L_0x0033;
    L_0x0030:
        r0.close();	 Catch:{ IOException -> 0x0033 }
    L_0x0033:
        if (r2 == 0) goto L_0x0036;
    L_0x0035:
        goto L_0x0018;
    L_0x0036:
        return;
    L_0x0037:
        r3 = move-exception;
        r1 = r0;
    L_0x0039:
        r0 = r2;
    L_0x003a:
        if (r1 == 0) goto L_0x003f;
    L_0x003c:
        r1.close();	 Catch:{ IOException -> 0x003f }
    L_0x003f:
        if (r0 == 0) goto L_0x0044;
    L_0x0041:
        r0.close();	 Catch:{ IOException -> 0x0044 }
    L_0x0044:
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.morpheustv.helpers.Utils.copyAsset(android.content.Context, int, java.io.File):void");
    }

    public static String buildTrackName(Format format) {
        if (MimeTypes.isVideo(format.sampleMimeType)) {
            format = joinWithSeparator(joinWithSeparator(joinWithSeparator(buildResolutionString(format), buildBitrateString(format)), buildTrackIdString(format)), buildSampleMimeTypeString(format));
        } else if (MimeTypes.isAudio(format.sampleMimeType)) {
            format = joinWithSeparator(joinWithSeparator(joinWithSeparator(joinWithSeparator(buildLanguageString(format), buildAudioPropertyString(format)), buildBitrateString(format)), buildTrackIdString(format)), buildSampleMimeTypeString(format));
        } else {
            format = joinWithSeparator(joinWithSeparator(joinWithSeparator(buildLanguageString(format), buildBitrateString(format)), buildTrackIdString(format)), buildSampleMimeTypeString(format));
        }
        return format.length() == 0 ? "unknown" : format;
    }

    private static String buildResolutionString(Format format) {
        if (format.width != -1) {
            if (format.height != -1) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(format.width);
                stringBuilder.append("x");
                stringBuilder.append(format.height);
                return stringBuilder.toString();
            }
        }
        return "";
    }

    private static String buildAudioPropertyString(Format format) {
        if (format.channelCount != -1) {
            if (format.sampleRate != -1) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(format.channelCount);
                stringBuilder.append("ch, ");
                stringBuilder.append(format.sampleRate);
                stringBuilder.append("Hz");
                return stringBuilder.toString();
            }
        }
        return "";
    }

    private static String buildLanguageString(Format format) {
        if (!TextUtils.isEmpty(format.language)) {
            if (!C0649C.LANGUAGE_UNDETERMINED.equals(format.language)) {
                return format.language;
            }
        }
        return "";
    }

    private static String buildBitrateString(Format format) {
        if (format.bitrate == -1) {
            return "";
        }
        return String.format(Locale.US, "%.2fMbit", new Object[]{Float.valueOf(((float) format.bitrate) / 1000000.0f)});
    }

    private static String joinWithSeparator(String str, String str2) {
        if (str.length() == 0) {
            return str2;
        }
        if (str2.length() == 0) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(", ");
        stringBuilder.append(str2);
        return stringBuilder.toString();
    }

    private static String buildTrackIdString(Format format) {
        if (format.id == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id:");
        stringBuilder.append(format.id);
        return stringBuilder.toString();
    }

    private static String buildSampleMimeTypeString(Format format) {
        return format.sampleMimeType == null ? "" : format.sampleMimeType;
    }

    public static String toBase64(String str) {
        return Base64.encodeToString(str.getBytes(), 8);
    }

    public static String fromBase64(String str) {
        return new String(Base64.decode(str, 8));
    }

    public static Map<String, String> splitQuery(URL url) {
        Map<String, String> linkedHashMap = new LinkedHashMap();
        try {
            for (String str : url.getQuery().split("&")) {
                int indexOf = str.indexOf("=");
                linkedHashMap.put(URLDecoder.decode(str.substring(0, indexOf), "UTF-8"), URLDecoder.decode(str.substring(indexOf + 1), "UTF-8"));
            }
        } catch (URL url2) {
            url2.printStackTrace();
        }
        return linkedHashMap;
    }

    public static String guessEncoding(byte[] bArr) {
        String str = "UTF-8";
        UniversalDetector universalDetector = new UniversalDetector(null);
        universalDetector.handleData(bArr, 0, bArr.length);
        universalDetector.dataEnd();
        bArr = universalDetector.getDetectedCharset();
        universalDetector.reset();
        return bArr == null ? str : bArr;
    }

    private static byte[] getFileBytes(File file) {
        byte[] bArr = new byte[((int) file.length())];
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            bufferedInputStream.read(bArr, null, bArr.length);
            bufferedInputStream.close();
            return bArr;
        } catch (File file2) {
            file2.printStackTrace();
            return null;
        }
    }

    private static boolean transform(File file, File file2) throws IOException {
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter = null;
        try {
            BufferedReader bufferedReader2;
            byte[] fileBytes = getFileBytes(file);
            boolean z = false;
            if (fileBytes == null || fileBytes.length <= 0) {
                file = null;
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), guessEncoding(fileBytes)));
                try {
                    file = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2), Charset.defaultCharset()));
                } catch (Throwable th) {
                    file2 = th;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (Throwable th2) {
                            if (bufferedWriter != null) {
                                bufferedWriter.close();
                            }
                        }
                    }
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    throw file2;
                }
                try {
                    file2 = new char[16384];
                    while (true) {
                        int read = bufferedReader.read(file2);
                        if (read == -1) {
                            break;
                        }
                        file.write(file2, 0, read);
                    }
                    z = true;
                    bufferedReader2 = bufferedReader;
                } catch (Throwable th3) {
                    file2 = th3;
                    bufferedWriter = file;
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    throw file2;
                }
            }
            if (bufferedReader2 != null) {
                try {
                    bufferedReader2.close();
                } catch (Throwable th4) {
                    if (file != null) {
                        file.close();
                    }
                }
            }
            if (file != null) {
                file.close();
            }
            return z;
        } catch (Throwable th5) {
            file2 = th5;
            bufferedReader = null;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            throw file2;
        }
    }

    public static void convert_utf8(File file) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(file.getAbsolutePath());
            stringBuilder.append("_tmputf8");
            File file2 = new File(stringBuilder.toString());
            if (transform(file, file2)) {
                file.delete();
                file2.renameTo(file.getAbsoluteFile());
            }
        } catch (File file3) {
            file3.printStackTrace();
        }
    }

    private File findFile(File file, String str, String str2) {
        if (file.isFile() && file.getAbsolutePath().contains(str) && file.getName().contains(str2)) {
            return file;
        }
        if (file.isDirectory()) {
            for (File findFile : file.listFiles()) {
                File findFile2 = findFile(findFile2, str, str2);
                if (findFile2 != null) {
                    return findFile2;
                }
            }
        }
        return null;
    }

    public static final String md5(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            str = instance.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : str) {
                String toHexString = Integer.toHexString(b & 255);
                while (toHexString.length() < 2) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("0");
                    stringBuilder2.append(toHexString);
                    toHexString = stringBuilder2.toString();
                }
                stringBuilder.append(toHexString);
            }
            return stringBuilder.toString();
        } catch (String str2) {
            str2.printStackTrace();
            return "";
        }
    }

    public static void showDialog(Context context, String str, String str2, OnClickListener onClickListener) {
        AlertDialog create = new Builder(context).create();
        create.setCancelable(false);
        create.setTitle(str);
        create.setMessage(str2);
        create.setButton(-1, context.getString(R.string.ok_button), onClickListener);
        create.show();
    }
}
