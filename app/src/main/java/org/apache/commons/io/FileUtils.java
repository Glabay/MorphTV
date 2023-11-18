package org.apache.commons.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.CRC32;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class FileUtils {
    public static final File[] EMPTY_FILE_ARRAY = new File[0];
    private static final long FILE_COPY_BUFFER_SIZE = 31457280;
    public static final long ONE_EB = 1152921504606846976L;
    public static final BigInteger ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);
    public static final long ONE_GB = 1073741824;
    public static final BigInteger ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);
    public static final long ONE_KB = 1024;
    public static final BigInteger ONE_KB_BI = BigInteger.valueOf(1024);
    public static final long ONE_MB = 1048576;
    public static final BigInteger ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);
    public static final long ONE_PB = 1125899906842624L;
    public static final BigInteger ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);
    public static final long ONE_TB = 1099511627776L;
    public static final BigInteger ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);
    public static final BigInteger ONE_YB = ONE_KB_BI.multiply(ONE_ZB);
    public static final BigInteger ONE_ZB = BigInteger.valueOf(1024).multiply(BigInteger.valueOf(ONE_EB));

    public static File getFile(File file, String... strArr) {
        if (file == null) {
            throw new NullPointerException("directory must not be null");
        } else if (strArr == null) {
            throw new NullPointerException("names must not be null");
        } else {
            int length = strArr.length;
            int i = 0;
            while (i < length) {
                i++;
                file = new File(file, strArr[i]);
            }
            return file;
        }
    }

    public static File getFile(String... strArr) {
        if (strArr == null) {
            throw new NullPointerException("names must not be null");
        }
        File file = null;
        for (String str : strArr) {
            if (file == null) {
                file = new File(str);
            } else {
                file = new File(file, str);
            }
        }
        return file;
    }

    public static String getTempDirectoryPath() {
        return System.getProperty("java.io.tmpdir");
    }

    public static File getTempDirectory() {
        return new File(getTempDirectoryPath());
    }

    public static String getUserDirectoryPath() {
        return System.getProperty("user.home");
    }

    public static File getUserDirectory() {
        return new File(getUserDirectoryPath());
    }

    public static FileInputStream openInputStream(File file) throws IOException {
        StringBuilder stringBuilder;
        if (!file.exists()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("File '");
            stringBuilder.append(file);
            stringBuilder.append("' does not exist");
            throw new FileNotFoundException(stringBuilder.toString());
        } else if (file.isDirectory()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("File '");
            stringBuilder.append(file);
            stringBuilder.append("' exists but is a directory");
            throw new IOException(stringBuilder.toString());
        } else if (file.canRead()) {
            return new FileInputStream(file);
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("File '");
            stringBuilder.append(file);
            stringBuilder.append("' cannot be read");
            throw new IOException(stringBuilder.toString());
        }
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        return openOutputStream(file, false);
    }

    public static FileOutputStream openOutputStream(File file, boolean z) throws IOException {
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (!(parentFile == null || parentFile.mkdirs() || parentFile.isDirectory())) {
                z = new StringBuilder();
                z.append("Directory '");
                z.append(parentFile);
                z.append("' could not be created");
                throw new IOException(z.toString());
            }
        } else if (file.isDirectory()) {
            r0 = new StringBuilder();
            r0.append("File '");
            r0.append(file);
            r0.append("' exists but is a directory");
            throw new IOException(r0.toString());
        } else if (!file.canWrite()) {
            r0 = new StringBuilder();
            r0.append("File '");
            r0.append(file);
            r0.append("' cannot be written to");
            throw new IOException(r0.toString());
        }
        return new FileOutputStream(file, z);
    }

    public static String byteCountToDisplaySize(BigInteger bigInteger) {
        StringBuilder stringBuilder;
        if (bigInteger.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) > 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(String.valueOf(bigInteger.divide(ONE_EB_BI)));
            stringBuilder.append(" EB");
            return stringBuilder.toString();
        } else if (bigInteger.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) > 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(String.valueOf(bigInteger.divide(ONE_PB_BI)));
            stringBuilder.append(" PB");
            return stringBuilder.toString();
        } else if (bigInteger.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) > 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(String.valueOf(bigInteger.divide(ONE_TB_BI)));
            stringBuilder.append(" TB");
            return stringBuilder.toString();
        } else if (bigInteger.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) > 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(String.valueOf(bigInteger.divide(ONE_GB_BI)));
            stringBuilder.append(" GB");
            return stringBuilder.toString();
        } else if (bigInteger.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) > 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(String.valueOf(bigInteger.divide(ONE_MB_BI)));
            stringBuilder.append(" MB");
            return stringBuilder.toString();
        } else if (bigInteger.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) > 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(String.valueOf(bigInteger.divide(ONE_KB_BI)));
            stringBuilder.append(" KB");
            return stringBuilder.toString();
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(String.valueOf(bigInteger));
            stringBuilder.append(" bytes");
            return stringBuilder.toString();
        }
    }

    public static String byteCountToDisplaySize(long j) {
        return byteCountToDisplaySize(BigInteger.valueOf(j));
    }

    public static void touch(File file) throws IOException {
        if (!file.exists()) {
            openOutputStream(file).close();
        }
        if (!file.setLastModified(System.currentTimeMillis())) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to set the last modification time for ");
            stringBuilder.append(file);
            throw new IOException(stringBuilder.toString());
        }
    }

    public static File[] convertFileCollectionToFileArray(Collection<File> collection) {
        return (File[]) collection.toArray(new File[collection.size()]);
    }

    private static void innerListFiles(Collection<File> collection, File file, IOFileFilter iOFileFilter, boolean z) {
        file = file.listFiles(iOFileFilter);
        if (file != null) {
            for (File file2 : file) {
                if (file2.isDirectory()) {
                    if (z) {
                        collection.add(file2);
                    }
                    innerListFiles(collection, file2, iOFileFilter, z);
                } else {
                    collection.add(file2);
                }
            }
        }
    }

    public static Collection<File> listFiles(File file, IOFileFilter iOFileFilter, IOFileFilter iOFileFilter2) {
        validateListFilesParameters(file, iOFileFilter);
        iOFileFilter = setUpEffectiveFileFilter(iOFileFilter);
        iOFileFilter2 = setUpEffectiveDirFilter(iOFileFilter2);
        Collection<File> linkedList = new LinkedList();
        innerListFiles(linkedList, file, FileFilterUtils.or(iOFileFilter, iOFileFilter2), false);
        return linkedList;
    }

    private static void validateListFilesParameters(File file, IOFileFilter iOFileFilter) {
        if (!file.isDirectory()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Parameter 'directory' is not a directory: ");
            stringBuilder.append(file);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (iOFileFilter == null) {
            throw new NullPointerException("Parameter 'fileFilter' is null");
        }
    }

    private static IOFileFilter setUpEffectiveFileFilter(IOFileFilter iOFileFilter) {
        return FileFilterUtils.and(iOFileFilter, FileFilterUtils.notFileFilter(DirectoryFileFilter.INSTANCE));
    }

    private static IOFileFilter setUpEffectiveDirFilter(IOFileFilter iOFileFilter) {
        if (iOFileFilter == null) {
            return FalseFileFilter.INSTANCE;
        }
        return FileFilterUtils.and(iOFileFilter, DirectoryFileFilter.INSTANCE);
    }

    public static Collection<File> listFilesAndDirs(File file, IOFileFilter iOFileFilter, IOFileFilter iOFileFilter2) {
        validateListFilesParameters(file, iOFileFilter);
        iOFileFilter = setUpEffectiveFileFilter(iOFileFilter);
        iOFileFilter2 = setUpEffectiveDirFilter(iOFileFilter2);
        Collection<File> linkedList = new LinkedList();
        if (file.isDirectory()) {
            linkedList.add(file);
        }
        innerListFiles(linkedList, file, FileFilterUtils.or(iOFileFilter, iOFileFilter2), true);
        return linkedList;
    }

    public static Iterator<File> iterateFiles(File file, IOFileFilter iOFileFilter, IOFileFilter iOFileFilter2) {
        return listFiles(file, iOFileFilter, iOFileFilter2).iterator();
    }

    public static Iterator<File> iterateFilesAndDirs(File file, IOFileFilter iOFileFilter, IOFileFilter iOFileFilter2) {
        return listFilesAndDirs(file, iOFileFilter, iOFileFilter2).iterator();
    }

    private static String[] toSuffixes(String[] strArr) {
        String[] strArr2 = new String[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(".");
            stringBuilder.append(strArr[i]);
            strArr2[i] = stringBuilder.toString();
        }
        return strArr2;
    }

    public static Collection<File> listFiles(File file, String[] strArr, boolean z) {
        IOFileFilter iOFileFilter;
        if (strArr == null) {
            iOFileFilter = TrueFileFilter.INSTANCE;
        } else {
            iOFileFilter = new SuffixFileFilter(toSuffixes(strArr));
        }
        return listFiles(file, iOFileFilter, z ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE);
    }

    public static Iterator<File> iterateFiles(File file, String[] strArr, boolean z) {
        return listFiles(file, strArr, z).iterator();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean contentEquals(java.io.File r8, java.io.File r9) throws java.io.IOException {
        /*
        r0 = r8.exists();
        r1 = r9.exists();
        r2 = 0;
        if (r0 == r1) goto L_0x000c;
    L_0x000b:
        return r2;
    L_0x000c:
        r1 = 1;
        if (r0 != 0) goto L_0x0010;
    L_0x000f:
        return r1;
    L_0x0010:
        r0 = r8.isDirectory();
        if (r0 != 0) goto L_0x0083;
    L_0x0016:
        r0 = r9.isDirectory();
        if (r0 == 0) goto L_0x001e;
    L_0x001c:
        goto L_0x0083;
    L_0x001e:
        r3 = r8.length();
        r5 = r9.length();
        r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r0 == 0) goto L_0x002b;
    L_0x002a:
        return r2;
    L_0x002b:
        r0 = r8.getCanonicalFile();
        r2 = r9.getCanonicalFile();
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x003a;
    L_0x0039:
        return r1;
    L_0x003a:
        r0 = new java.io.FileInputStream;
        r0.<init>(r8);
        r8 = 0;
        r1 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x0070 }
        r1.<init>(r9);	 Catch:{ Throwable -> 0x0070 }
        r9 = org.apache.commons.io.IOUtils.contentEquals(r0, r1);	 Catch:{ Throwable -> 0x0057, all -> 0x0054 }
        if (r1 == 0) goto L_0x004e;
    L_0x004b:
        r1.close();	 Catch:{ Throwable -> 0x0070 }
    L_0x004e:
        if (r0 == 0) goto L_0x0053;
    L_0x0050:
        r0.close();
    L_0x0053:
        return r9;
    L_0x0054:
        r9 = move-exception;
        r2 = r8;
        goto L_0x005d;
    L_0x0057:
        r9 = move-exception;
        throw r9;	 Catch:{ all -> 0x0059 }
    L_0x0059:
        r2 = move-exception;
        r7 = r2;
        r2 = r9;
        r9 = r7;
    L_0x005d:
        if (r1 == 0) goto L_0x006d;
    L_0x005f:
        if (r2 == 0) goto L_0x006a;
    L_0x0061:
        r1.close();	 Catch:{ Throwable -> 0x0065 }
        goto L_0x006d;
    L_0x0065:
        r1 = move-exception;
        r2.addSuppressed(r1);	 Catch:{ Throwable -> 0x0070 }
        goto L_0x006d;
    L_0x006a:
        r1.close();	 Catch:{ Throwable -> 0x0070 }
    L_0x006d:
        throw r9;	 Catch:{ Throwable -> 0x0070 }
    L_0x006e:
        r9 = move-exception;
        goto L_0x0072;
    L_0x0070:
        r8 = move-exception;
        throw r8;	 Catch:{ all -> 0x006e }
    L_0x0072:
        if (r0 == 0) goto L_0x0082;
    L_0x0074:
        if (r8 == 0) goto L_0x007f;
    L_0x0076:
        r0.close();	 Catch:{ Throwable -> 0x007a }
        goto L_0x0082;
    L_0x007a:
        r0 = move-exception;
        r8.addSuppressed(r0);
        goto L_0x0082;
    L_0x007f:
        r0.close();
    L_0x0082:
        throw r9;
    L_0x0083:
        r8 = new java.io.IOException;
        r9 = "Can't compare directories, only files";
        r8.<init>(r9);
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.FileUtils.contentEquals(java.io.File, java.io.File):boolean");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean contentEqualsIgnoreEOL(java.io.File r4, java.io.File r5, java.lang.String r6) throws java.io.IOException {
        /*
        r0 = r4.exists();
        r1 = r5.exists();
        if (r0 == r1) goto L_0x000c;
    L_0x000a:
        r4 = 0;
        return r4;
    L_0x000c:
        r1 = 1;
        if (r0 != 0) goto L_0x0010;
    L_0x000f:
        return r1;
    L_0x0010:
        r0 = r4.isDirectory();
        if (r0 != 0) goto L_0x00a4;
    L_0x0016:
        r0 = r5.isDirectory();
        if (r0 == 0) goto L_0x001e;
    L_0x001c:
        goto L_0x00a4;
    L_0x001e:
        r0 = r4.getCanonicalFile();
        r2 = r5.getCanonicalFile();
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x002d;
    L_0x002c:
        return r1;
    L_0x002d:
        if (r6 != 0) goto L_0x003e;
    L_0x002f:
        r0 = new java.io.InputStreamReader;
        r1 = new java.io.FileInputStream;
        r1.<init>(r4);
        r4 = java.nio.charset.Charset.defaultCharset();
        r0.<init>(r1, r4);
        goto L_0x0048;
    L_0x003e:
        r0 = new java.io.InputStreamReader;
        r1 = new java.io.FileInputStream;
        r1.<init>(r4);
        r0.<init>(r1, r6);
    L_0x0048:
        r4 = 0;
        if (r6 != 0) goto L_0x005e;
    L_0x004b:
        r6 = new java.io.InputStreamReader;	 Catch:{ Throwable -> 0x005c }
        r1 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x005c }
        r1.<init>(r5);	 Catch:{ Throwable -> 0x005c }
        r5 = java.nio.charset.Charset.defaultCharset();	 Catch:{ Throwable -> 0x005c }
        r6.<init>(r1, r5);	 Catch:{ Throwable -> 0x005c }
        goto L_0x0069;
    L_0x005a:
        r5 = move-exception;
        goto L_0x0093;
    L_0x005c:
        r4 = move-exception;
        goto L_0x0092;
    L_0x005e:
        r1 = new java.io.InputStreamReader;	 Catch:{ Throwable -> 0x005c }
        r2 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x005c }
        r2.<init>(r5);	 Catch:{ Throwable -> 0x005c }
        r1.<init>(r2, r6);	 Catch:{ Throwable -> 0x005c }
        r6 = r1;
    L_0x0069:
        r5 = org.apache.commons.io.IOUtils.contentEqualsIgnoreEOL(r0, r6);	 Catch:{ Throwable -> 0x007b, all -> 0x0078 }
        if (r6 == 0) goto L_0x0072;
    L_0x006f:
        r6.close();	 Catch:{ Throwable -> 0x005c }
    L_0x0072:
        if (r0 == 0) goto L_0x0077;
    L_0x0074:
        r0.close();
    L_0x0077:
        return r5;
    L_0x0078:
        r5 = move-exception;
        r1 = r4;
        goto L_0x0081;
    L_0x007b:
        r5 = move-exception;
        throw r5;	 Catch:{ all -> 0x007d }
    L_0x007d:
        r1 = move-exception;
        r3 = r1;
        r1 = r5;
        r5 = r3;
    L_0x0081:
        if (r6 == 0) goto L_0x0091;
    L_0x0083:
        if (r1 == 0) goto L_0x008e;
    L_0x0085:
        r6.close();	 Catch:{ Throwable -> 0x0089 }
        goto L_0x0091;
    L_0x0089:
        r6 = move-exception;
        r1.addSuppressed(r6);	 Catch:{ Throwable -> 0x005c }
        goto L_0x0091;
    L_0x008e:
        r6.close();	 Catch:{ Throwable -> 0x005c }
    L_0x0091:
        throw r5;	 Catch:{ Throwable -> 0x005c }
    L_0x0092:
        throw r4;	 Catch:{ all -> 0x005a }
    L_0x0093:
        if (r0 == 0) goto L_0x00a3;
    L_0x0095:
        if (r4 == 0) goto L_0x00a0;
    L_0x0097:
        r0.close();	 Catch:{ Throwable -> 0x009b }
        goto L_0x00a3;
    L_0x009b:
        r6 = move-exception;
        r4.addSuppressed(r6);
        goto L_0x00a3;
    L_0x00a0:
        r0.close();
    L_0x00a3:
        throw r5;
    L_0x00a4:
        r4 = new java.io.IOException;
        r5 = "Can't compare directories, only files";
        r4.<init>(r5);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.FileUtils.contentEqualsIgnoreEOL(java.io.File, java.io.File, java.lang.String):boolean");
    }

    public static File toFile(URL url) {
        if (url != null) {
            if ("file".equalsIgnoreCase(url.getProtocol())) {
                return new File(decodeUrl(url.getFile().replace(IOUtils.DIR_SEPARATOR_UNIX, File.separatorChar)));
            }
        }
        return null;
    }

    static java.lang.String decodeUrl(java.lang.String r8) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        if (r8 == 0) goto L_0x009e;
    L_0x0002:
        r0 = 37;
        r1 = r8.indexOf(r0);
        if (r1 < 0) goto L_0x009e;
    L_0x000a:
        r1 = r8.length();
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = java.nio.ByteBuffer.allocate(r1);
        r4 = 0;
    L_0x0018:
        if (r4 >= r1) goto L_0x009a;
    L_0x001a:
        r5 = r8.charAt(r4);
        if (r5 != r0) goto L_0x008e;
    L_0x0020:
        r5 = r4 + 1;
        r6 = r4 + 3;
        r5 = r8.substring(r5, r6);	 Catch:{ RuntimeException -> 0x0075, all -> 0x005a }
        r7 = 16;	 Catch:{ RuntimeException -> 0x0075, all -> 0x005a }
        r5 = java.lang.Integer.parseInt(r5, r7);	 Catch:{ RuntimeException -> 0x0075, all -> 0x005a }
        r5 = (byte) r5;	 Catch:{ RuntimeException -> 0x0075, all -> 0x005a }
        r3.put(r5);	 Catch:{ RuntimeException -> 0x0075, all -> 0x005a }
        if (r6 >= r1) goto L_0x003f;
    L_0x0034:
        r4 = r8.charAt(r6);	 Catch:{ RuntimeException -> 0x003d, all -> 0x005a }
        if (r4 == r0) goto L_0x003b;
    L_0x003a:
        goto L_0x003f;
    L_0x003b:
        r4 = r6;
        goto L_0x0020;
    L_0x003d:
        r4 = r6;
        goto L_0x0075;
    L_0x003f:
        r4 = r3.position();
        if (r4 <= 0) goto L_0x0058;
    L_0x0045:
        r3.flip();
        r4 = java.nio.charset.StandardCharsets.UTF_8;
        r4 = r4.decode(r3);
        r4 = r4.toString();
        r2.append(r4);
        r3.clear();
    L_0x0058:
        r4 = r6;
        goto L_0x0018;
    L_0x005a:
        r8 = move-exception;
        r0 = r3.position();
        if (r0 <= 0) goto L_0x0074;
    L_0x0061:
        r3.flip();
        r0 = java.nio.charset.StandardCharsets.UTF_8;
        r0 = r0.decode(r3);
        r0 = r0.toString();
        r2.append(r0);
        r3.clear();
    L_0x0074:
        throw r8;
    L_0x0075:
        r5 = r3.position();
        if (r5 <= 0) goto L_0x008e;
    L_0x007b:
        r3.flip();
        r5 = java.nio.charset.StandardCharsets.UTF_8;
        r5 = r5.decode(r3);
        r5 = r5.toString();
        r2.append(r5);
        r3.clear();
    L_0x008e:
        r5 = r4 + 1;
        r4 = r8.charAt(r4);
        r2.append(r4);
        r4 = r5;
        goto L_0x0018;
    L_0x009a:
        r8 = r2.toString();
    L_0x009e:
        return r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.FileUtils.decodeUrl(java.lang.String):java.lang.String");
    }

    public static File[] toFiles(URL[] urlArr) {
        if (urlArr != null) {
            if (urlArr.length != 0) {
                File[] fileArr = new File[urlArr.length];
                for (int i = 0; i < urlArr.length; i++) {
                    URL url = urlArr[i];
                    if (url != null) {
                        if (url.getProtocol().equals("file")) {
                            fileArr[i] = toFile(url);
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("URL could not be converted to a File: ");
                            stringBuilder.append(url);
                            throw new IllegalArgumentException(stringBuilder.toString());
                        }
                    }
                }
                return fileArr;
            }
        }
        return EMPTY_FILE_ARRAY;
    }

    public static URL[] toURLs(File[] fileArr) throws IOException {
        URL[] urlArr = new URL[fileArr.length];
        for (int i = 0; i < urlArr.length; i++) {
            urlArr[i] = fileArr[i].toURI().toURL();
        }
        return urlArr;
    }

    public static void copyFileToDirectory(File file, File file2) throws IOException {
        copyFileToDirectory(file, file2, true);
    }

    public static void copyFileToDirectory(File file, File file2, boolean z) throws IOException {
        if (file2 == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!file2.exists() || file2.isDirectory()) {
            copyFile(file, new File(file2, file.getName()), z);
        } else {
            z = new StringBuilder();
            z.append("Destination '");
            z.append(file2);
            z.append("' is not a directory");
            throw new IllegalArgumentException(z.toString());
        }
    }

    public static void copyFile(File file, File file2) throws IOException {
        copyFile(file, file2, true);
    }

    public static void copyFile(File file, File file2, boolean z) throws IOException {
        checkFileRequirements(file, file2);
        if (file.isDirectory()) {
            z = new StringBuilder();
            z.append("Source '");
            z.append(file);
            z.append("' exists but is a directory");
            throw new IOException(z.toString());
        } else if (file.getCanonicalPath().equals(file2.getCanonicalPath())) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Source '");
            stringBuilder.append(file);
            stringBuilder.append("' and destination '");
            stringBuilder.append(file2);
            stringBuilder.append("' are the same");
            throw new IOException(stringBuilder.toString());
        } else {
            File parentFile = file2.getParentFile();
            if (parentFile != null && !parentFile.mkdirs() && !parentFile.isDirectory()) {
                file2 = new StringBuilder();
                file2.append("Destination '");
                file2.append(parentFile);
                file2.append("' directory cannot be created");
                throw new IOException(file2.toString());
            } else if (!file2.exists() || file2.canWrite()) {
                doCopyFile(file, file2, z);
            } else {
                z = new StringBuilder();
                z.append("Destination '");
                z.append(file2);
                z.append("' exists but is read-only");
                throw new IOException(z.toString());
            }
        }
    }

    public static long copyFile(File file, OutputStream outputStream) throws IOException {
        InputStream fileInputStream = new FileInputStream(file);
        try {
            file = IOUtils.copyLarge(fileInputStream, outputStream);
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            return file;
        } catch (Throwable th) {
            file = th;
        }
        throw file;
        if (fileInputStream != null) {
            if (outputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Throwable th2) {
                    outputStream.addSuppressed(th2);
                }
            } else {
                fileInputStream.close();
            }
        }
        throw file;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void doCopyFile(java.io.File r20, java.io.File r21, boolean r22) throws java.io.IOException {
        /*
        r1 = r20;
        r2 = r21;
        r3 = r21.exists();
        if (r3 == 0) goto L_0x002c;
    L_0x000a:
        r3 = r21.isDirectory();
        if (r3 == 0) goto L_0x002c;
    L_0x0010:
        r1 = new java.io.IOException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Destination '";
        r3.append(r4);
        r3.append(r2);
        r2 = "' exists but is a directory";
        r3.append(r2);
        r2 = r3.toString();
        r1.<init>(r2);
        throw r1;
    L_0x002c:
        r3 = new java.io.FileInputStream;
        r3.<init>(r1);
        r4 = 0;
        r11 = r3.getChannel();	 Catch:{ Throwable -> 0x0117 }
        r12 = new java.io.FileOutputStream;	 Catch:{ Throwable -> 0x00fd, all -> 0x00f9 }
        r12.<init>(r2);	 Catch:{ Throwable -> 0x00fd, all -> 0x00f9 }
        r13 = r12.getChannel();	 Catch:{ Throwable -> 0x00e2, all -> 0x00de }
        r14 = r11.size();	 Catch:{ Throwable -> 0x00c7, all -> 0x00c3 }
        r16 = 0;
        r18 = r16;
    L_0x0047:
        r5 = (r18 > r14 ? 1 : (r18 == r14 ? 0 : -1));
        if (r5 >= 0) goto L_0x006a;
    L_0x004b:
        r5 = r14 - r18;
        r7 = 31457280; // 0x1e00000 float:8.2284605E-38 double:1.55419614E-316;
        r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r9 <= 0) goto L_0x0056;
    L_0x0054:
        r9 = r7;
        goto L_0x0057;
    L_0x0056:
        r9 = r5;
    L_0x0057:
        r5 = r13;
        r6 = r11;
        r7 = r18;
        r5 = r5.transferFrom(r6, r7, r9);	 Catch:{ Throwable -> 0x00c7, all -> 0x00c3 }
        r7 = (r5 > r16 ? 1 : (r5 == r16 ? 0 : -1));
        if (r7 != 0) goto L_0x0064;
    L_0x0063:
        goto L_0x006a;
    L_0x0064:
        r7 = 0;
        r7 = r18 + r5;
        r18 = r7;
        goto L_0x0047;
    L_0x006a:
        if (r13 == 0) goto L_0x006f;
    L_0x006c:
        r13.close();	 Catch:{ Throwable -> 0x00e2, all -> 0x00de }
    L_0x006f:
        if (r12 == 0) goto L_0x0074;
    L_0x0071:
        r12.close();	 Catch:{ Throwable -> 0x00fd, all -> 0x00f9 }
    L_0x0074:
        if (r11 == 0) goto L_0x0079;
    L_0x0076:
        r11.close();	 Catch:{ Throwable -> 0x0117 }
    L_0x0079:
        if (r3 == 0) goto L_0x007e;
    L_0x007b:
        r3.close();
    L_0x007e:
        r3 = r20.length();
        r5 = r21.length();
        r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r7 == 0) goto L_0x00b9;
    L_0x008a:
        r7 = new java.io.IOException;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "Failed to copy full contents from '";
        r8.append(r9);
        r8.append(r1);
        r1 = "' to '";
        r8.append(r1);
        r8.append(r2);
        r1 = "' Expected length: ";
        r8.append(r1);
        r8.append(r3);
        r1 = " Actual: ";
        r8.append(r1);
        r8.append(r5);
        r1 = r8.toString();
        r7.<init>(r1);
        throw r7;
    L_0x00b9:
        if (r22 == 0) goto L_0x00c2;
    L_0x00bb:
        r3 = r20.lastModified();
        r2.setLastModified(r3);
    L_0x00c2:
        return;
    L_0x00c3:
        r0 = move-exception;
        r1 = r0;
        r2 = r4;
        goto L_0x00cd;
    L_0x00c7:
        r0 = move-exception;
        r1 = r0;
        throw r1;	 Catch:{ all -> 0x00ca }
    L_0x00ca:
        r0 = move-exception;
        r2 = r1;
        r1 = r0;
    L_0x00cd:
        if (r13 == 0) goto L_0x00dd;
    L_0x00cf:
        if (r2 == 0) goto L_0x00da;
    L_0x00d1:
        r13.close();	 Catch:{ Throwable -> 0x00d5, all -> 0x00de }
        goto L_0x00dd;
    L_0x00d5:
        r0 = move-exception;
        r2.addSuppressed(r0);	 Catch:{ Throwable -> 0x00e2, all -> 0x00de }
        goto L_0x00dd;
    L_0x00da:
        r13.close();	 Catch:{ Throwable -> 0x00e2, all -> 0x00de }
    L_0x00dd:
        throw r1;	 Catch:{ Throwable -> 0x00e2, all -> 0x00de }
    L_0x00de:
        r0 = move-exception;
        r1 = r0;
        r2 = r4;
        goto L_0x00e8;
    L_0x00e2:
        r0 = move-exception;
        r1 = r0;
        throw r1;	 Catch:{ all -> 0x00e5 }
    L_0x00e5:
        r0 = move-exception;
        r2 = r1;
        r1 = r0;
    L_0x00e8:
        if (r12 == 0) goto L_0x00f8;
    L_0x00ea:
        if (r2 == 0) goto L_0x00f5;
    L_0x00ec:
        r12.close();	 Catch:{ Throwable -> 0x00f0, all -> 0x00f9 }
        goto L_0x00f8;
    L_0x00f0:
        r0 = move-exception;
        r2.addSuppressed(r0);	 Catch:{ Throwable -> 0x00fd, all -> 0x00f9 }
        goto L_0x00f8;
    L_0x00f5:
        r12.close();	 Catch:{ Throwable -> 0x00fd, all -> 0x00f9 }
    L_0x00f8:
        throw r1;	 Catch:{ Throwable -> 0x00fd, all -> 0x00f9 }
    L_0x00f9:
        r0 = move-exception;
        r1 = r0;
        r2 = r4;
        goto L_0x0103;
    L_0x00fd:
        r0 = move-exception;
        r1 = r0;
        throw r1;	 Catch:{ all -> 0x0100 }
    L_0x0100:
        r0 = move-exception;
        r2 = r1;
        r1 = r0;
    L_0x0103:
        if (r11 == 0) goto L_0x0113;
    L_0x0105:
        if (r2 == 0) goto L_0x0110;
    L_0x0107:
        r11.close();	 Catch:{ Throwable -> 0x010b }
        goto L_0x0113;
    L_0x010b:
        r0 = move-exception;
        r2.addSuppressed(r0);	 Catch:{ Throwable -> 0x0117 }
        goto L_0x0113;
    L_0x0110:
        r11.close();	 Catch:{ Throwable -> 0x0117 }
    L_0x0113:
        throw r1;	 Catch:{ Throwable -> 0x0117 }
    L_0x0114:
        r0 = move-exception;
        r1 = r0;
        goto L_0x011a;
    L_0x0117:
        r0 = move-exception;
        r4 = r0;
        throw r4;	 Catch:{ all -> 0x0114 }
    L_0x011a:
        if (r3 == 0) goto L_0x012a;
    L_0x011c:
        if (r4 == 0) goto L_0x0127;
    L_0x011e:
        r3.close();	 Catch:{ Throwable -> 0x0122 }
        goto L_0x012a;
    L_0x0122:
        r0 = move-exception;
        r4.addSuppressed(r0);
        goto L_0x012a;
    L_0x0127:
        r3.close();
    L_0x012a:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.FileUtils.doCopyFile(java.io.File, java.io.File, boolean):void");
    }

    public static void copyDirectoryToDirectory(File file, File file2) throws IOException {
        if (file == null) {
            throw new NullPointerException("Source must not be null");
        } else if (file.exists() && !file.isDirectory()) {
            r0 = new StringBuilder();
            r0.append("Source '");
            r0.append(file2);
            r0.append("' is not a directory");
            throw new IllegalArgumentException(r0.toString());
        } else if (file2 == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!file2.exists() || file2.isDirectory()) {
            copyDirectory(file, new File(file2, file.getName()), true);
        } else {
            r0 = new StringBuilder();
            r0.append("Destination '");
            r0.append(file2);
            r0.append("' is not a directory");
            throw new IllegalArgumentException(r0.toString());
        }
    }

    public static void copyDirectory(File file, File file2) throws IOException {
        copyDirectory(file, file2, true);
    }

    public static void copyDirectory(File file, File file2, boolean z) throws IOException {
        copyDirectory(file, file2, null, z);
    }

    public static void copyDirectory(File file, File file2, FileFilter fileFilter) throws IOException {
        copyDirectory(file, file2, fileFilter, true);
    }

    public static void copyDirectory(File file, File file2, FileFilter fileFilter, boolean z) throws IOException {
        checkFileRequirements(file, file2);
        if (!file.isDirectory()) {
            fileFilter = new StringBuilder();
            fileFilter.append("Source '");
            fileFilter.append(file);
            fileFilter.append("' exists but is not a directory");
            throw new IOException(fileFilter.toString());
        } else if (file.getCanonicalPath().equals(file2.getCanonicalPath())) {
            z = new StringBuilder();
            z.append("Source '");
            z.append(file);
            z.append("' and destination '");
            z.append(file2);
            z.append("' are the same");
            throw new IOException(z.toString());
        } else {
            List list = null;
            if (file2.getCanonicalPath().startsWith(file.getCanonicalPath())) {
                File[] listFiles = fileFilter == null ? file.listFiles() : file.listFiles(fileFilter);
                if (listFiles != null && listFiles.length > 0) {
                    list = new ArrayList(listFiles.length);
                    for (File name : listFiles) {
                        list.add(new File(file2, name.getName()).getCanonicalPath());
                    }
                }
            }
            doCopyDirectory(file, file2, fileFilter, z, list);
        }
    }

    private static void checkFileRequirements(File file, File file2) throws FileNotFoundException {
        if (file == null) {
            throw new NullPointerException("Source must not be null");
        } else if (file2 == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (file.exists() == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Source '");
            stringBuilder.append(file);
            stringBuilder.append("' does not exist");
            throw new FileNotFoundException(stringBuilder.toString());
        }
    }

    private static void doCopyDirectory(File file, File file2, FileFilter fileFilter, boolean z, List<String> list) throws IOException {
        File[] listFiles = fileFilter == null ? file.listFiles() : file.listFiles(fileFilter);
        if (listFiles == null) {
            fileFilter = new StringBuilder();
            fileFilter.append("Failed to list contents of ");
            fileFilter.append(file);
            throw new IOException(fileFilter.toString());
        }
        if (file2.exists()) {
            if (!file2.isDirectory()) {
                fileFilter = new StringBuilder();
                fileFilter.append("Destination '");
                fileFilter.append(file2);
                fileFilter.append("' exists but is not a directory");
                throw new IOException(fileFilter.toString());
            }
        } else if (!(file2.mkdirs() || file2.isDirectory())) {
            fileFilter = new StringBuilder();
            fileFilter.append("Destination '");
            fileFilter.append(file2);
            fileFilter.append("' directory cannot be created");
            throw new IOException(fileFilter.toString());
        }
        if (file2.canWrite()) {
            for (File file3 : listFiles) {
                File file4 = new File(file2, file3.getName());
                if (list == null || !list.contains(file3.getCanonicalPath())) {
                    if (file3.isDirectory()) {
                        doCopyDirectory(file3, file4, fileFilter, z, list);
                    } else {
                        doCopyFile(file3, file4, z);
                    }
                }
            }
            if (z) {
                file2.setLastModified(file.lastModified());
                return;
            }
            return;
        }
        fileFilter = new StringBuilder();
        fileFilter.append("Destination '");
        fileFilter.append(file2);
        fileFilter.append("' cannot be written to");
        throw new IOException(fileFilter.toString());
    }

    public static void copyURLToFile(URL url, File file) throws IOException {
        copyInputStreamToFile(url.openStream(), file);
    }

    public static void copyURLToFile(URL url, File file, int i, int i2) throws IOException {
        url = url.openConnection();
        url.setConnectTimeout(i);
        url.setReadTimeout(i2);
        copyInputStreamToFile(url.getInputStream(), file);
    }

    public static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {
        try {
            copyToFile(inputStream, file);
            if (inputStream != null) {
                inputStream.close();
                return;
            }
            return;
        } catch (Throwable th) {
            file = th;
        }
        if (inputStream != null) {
            if (r0 != null) {
                try {
                    inputStream.close();
                } catch (InputStream inputStream2) {
                    r0.addSuppressed(inputStream2);
                }
            } else {
                inputStream2.close();
            }
        }
        throw file;
        throw file;
    }

    public static void copyToFile(InputStream inputStream, File file) throws IOException {
        Throwable th;
        Throwable th2;
        try {
            OutputStream openOutputStream = openOutputStream(file);
            try {
                IOUtils.copy(inputStream, openOutputStream);
                if (openOutputStream != null) {
                    openOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                    return;
                }
                return;
            } catch (Throwable th22) {
                Throwable th3 = th22;
                th22 = th;
                th = th3;
            }
            if (openOutputStream != null) {
                if (th22 != null) {
                    openOutputStream.close();
                } else {
                    openOutputStream.close();
                }
            }
            throw th;
            throw th;
        } catch (File file2) {
            File file3 = file2;
            try {
            } catch (InputStream inputStream2) {
                file3.addSuppressed(inputStream2);
            }
        }
    }

    public static void copyToDirectory(File file, File file2) throws IOException {
        if (file == null) {
            throw new NullPointerException("Source must not be null");
        } else if (file.isFile()) {
            copyFileToDirectory(file, file2);
        } else if (file.isDirectory()) {
            copyDirectoryToDirectory(file, file2);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The source ");
            stringBuilder.append(file);
            stringBuilder.append(" does not exist");
            throw new IOException(stringBuilder.toString());
        }
    }

    public static void copyToDirectory(Iterable<File> iterable, File file) throws IOException {
        if (iterable == null) {
            throw new NullPointerException("Sources must not be null");
        }
        for (File copyFileToDirectory : iterable) {
            copyFileToDirectory(copyFileToDirectory, file);
        }
    }

    public static void deleteDirectory(File file) throws IOException {
        if (file.exists()) {
            if (!isSymlink(file)) {
                cleanDirectory(file);
            }
            if (!file.delete()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to delete directory ");
                stringBuilder.append(file);
                stringBuilder.append(".");
                throw new IOException(stringBuilder.toString());
            }
        }
    }

    public static boolean deleteQuietly(java.io.File r2) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        if (r2 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = r2.isDirectory();	 Catch:{ Exception -> 0x000d }
        if (r1 == 0) goto L_0x000d;	 Catch:{ Exception -> 0x000d }
    L_0x000a:
        cleanDirectory(r2);	 Catch:{ Exception -> 0x000d }
    L_0x000d:
        r2 = r2.delete();	 Catch:{ Exception -> 0x0012 }
        return r2;
    L_0x0012:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.FileUtils.deleteQuietly(java.io.File):boolean");
    }

    public static boolean directoryContains(File file, File file2) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("Directory must not be null");
        } else if (file.isDirectory()) {
            if (file2 != null && file.exists()) {
                if (file2.exists()) {
                    return FilenameUtils.directoryContains(file.getCanonicalPath(), file2.getCanonicalPath());
                }
            }
            return false;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Not a directory: ");
            stringBuilder.append(file);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static void cleanDirectory(File file) throws IOException {
        IOException iOException = null;
        for (File forceDelete : verifiedListFiles(file)) {
            try {
                forceDelete(forceDelete);
            } catch (IOException e) {
                iOException = e;
            }
        }
        if (iOException != null) {
            throw iOException;
        }
    }

    private static File[] verifiedListFiles(File file) throws IOException {
        StringBuilder stringBuilder;
        if (!file.exists()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(file);
            stringBuilder.append(" does not exist");
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                return listFiles;
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Failed to list contents of ");
            stringBuilder2.append(file);
            throw new IOException(stringBuilder2.toString());
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(file);
            stringBuilder.append(" is not a directory");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static boolean waitFor(java.io.File r10, int r11) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = java.lang.System.currentTimeMillis();
        r2 = (long) r11;
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r2 = r2 * r4;
        r4 = r0 + r2;
        r11 = 0;
        r0 = 0;
    L_0x000d:
        r1 = r10.exists();	 Catch:{ all -> 0x0041 }
        r2 = 1;	 Catch:{ all -> 0x0041 }
        if (r1 != 0) goto L_0x0037;	 Catch:{ all -> 0x0041 }
    L_0x0014:
        r6 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x0041 }
        r1 = 0;
        r8 = r4 - r6;
        r6 = 0;
        r1 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1));
        if (r1 >= 0) goto L_0x002b;
    L_0x0021:
        if (r0 == 0) goto L_0x002a;
    L_0x0023:
        r10 = java.lang.Thread.currentThread();
        r10.interrupt();
    L_0x002a:
        return r11;
    L_0x002b:
        r6 = 100;
        r6 = java.lang.Math.min(r6, r8);	 Catch:{ InterruptedException -> 0x0035, Exception -> 0x0037 }
        java.lang.Thread.sleep(r6);	 Catch:{ InterruptedException -> 0x0035, Exception -> 0x0037 }
        goto L_0x000d;
    L_0x0035:
        r0 = 1;
        goto L_0x000d;
    L_0x0037:
        if (r0 == 0) goto L_0x0040;
    L_0x0039:
        r10 = java.lang.Thread.currentThread();
        r10.interrupt();
    L_0x0040:
        return r2;
    L_0x0041:
        r10 = move-exception;
        if (r0 == 0) goto L_0x004b;
    L_0x0044:
        r11 = java.lang.Thread.currentThread();
        r11.interrupt();
    L_0x004b:
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.FileUtils.waitFor(java.io.File, int):boolean");
    }

    public static String readFileToString(File file, Charset charset) throws IOException {
        Charset charset2;
        InputStream openInputStream = openInputStream(file);
        try {
            charset = IOUtils.toString(openInputStream, Charsets.toCharset(charset));
            if (openInputStream != null) {
                openInputStream.close();
            }
            return charset;
        } catch (File file2) {
            charset2.addSuppressed(file2);
        }
    }

    public static String readFileToString(File file, String str) throws IOException {
        return readFileToString(file, Charsets.toCharset(str));
    }

    @Deprecated
    public static String readFileToString(File file) throws IOException {
        return readFileToString(file, Charset.defaultCharset());
    }

    public static byte[] readFileToByteArray(File file) throws IOException {
        File file2;
        InputStream openInputStream = openInputStream(file);
        try {
            long length = file.length();
            file = length > 0 ? IOUtils.toByteArray(openInputStream, length) : IOUtils.toByteArray(openInputStream);
            if (openInputStream != null) {
                openInputStream.close();
            }
            return file;
        } catch (Throwable th) {
            file2.addSuppressed(th);
        }
    }

    public static List<String> readLines(File file, Charset charset) throws IOException {
        Charset charset2;
        InputStream openInputStream = openInputStream(file);
        try {
            charset = IOUtils.readLines(openInputStream, Charsets.toCharset(charset));
            if (openInputStream != null) {
                openInputStream.close();
            }
            return charset;
        } catch (File file2) {
            charset2.addSuppressed(file2);
        }
    }

    public static List<String> readLines(File file, String str) throws IOException {
        return readLines(file, Charsets.toCharset(str));
    }

    @Deprecated
    public static List<String> readLines(File file) throws IOException {
        return readLines(file, Charset.defaultCharset());
    }

    public static LineIterator lineIterator(File file, String str) throws IOException {
        try {
            file = openInputStream(file);
            try {
                return IOUtils.lineIterator((InputStream) file, str);
            } catch (IOException e) {
                str = e;
                if (file != null) {
                    try {
                        file.close();
                    } catch (File file2) {
                        str.addSuppressed(file2);
                    }
                }
                throw str;
            }
        } catch (IOException e2) {
            str = e2;
            file2 = null;
            if (file2 != null) {
                file2.close();
            }
            throw str;
        }
    }

    public static LineIterator lineIterator(File file) throws IOException {
        return lineIterator(file, null);
    }

    public static void writeStringToFile(File file, String str, Charset charset) throws IOException {
        writeStringToFile(file, str, charset, false);
    }

    public static void writeStringToFile(File file, String str, String str2) throws IOException {
        writeStringToFile(file, str, str2, false);
    }

    public static void writeStringToFile(File file, String str, Charset charset, boolean z) throws IOException {
        OutputStream openOutputStream = openOutputStream(file, z);
        try {
            IOUtils.write(str, openOutputStream, charset);
            if (openOutputStream != null) {
                openOutputStream.close();
                return;
            }
            return;
        } catch (Throwable th) {
            str = th;
        }
        if (openOutputStream != null) {
            if (charset != null) {
                try {
                    openOutputStream.close();
                } catch (File file2) {
                    charset.addSuppressed(file2);
                }
            } else {
                openOutputStream.close();
            }
        }
        throw str;
        throw str;
    }

    public static void writeStringToFile(File file, String str, String str2, boolean z) throws IOException {
        writeStringToFile(file, str, Charsets.toCharset(str2), z);
    }

    @Deprecated
    public static void writeStringToFile(File file, String str) throws IOException {
        writeStringToFile(file, str, Charset.defaultCharset(), false);
    }

    @Deprecated
    public static void writeStringToFile(File file, String str, boolean z) throws IOException {
        writeStringToFile(file, str, Charset.defaultCharset(), z);
    }

    @Deprecated
    public static void write(File file, CharSequence charSequence) throws IOException {
        write(file, charSequence, Charset.defaultCharset(), false);
    }

    @Deprecated
    public static void write(File file, CharSequence charSequence, boolean z) throws IOException {
        write(file, charSequence, Charset.defaultCharset(), z);
    }

    public static void write(File file, CharSequence charSequence, Charset charset) throws IOException {
        write(file, charSequence, charset, false);
    }

    public static void write(File file, CharSequence charSequence, String str) throws IOException {
        write(file, charSequence, str, false);
    }

    public static void write(File file, CharSequence charSequence, Charset charset, boolean z) throws IOException {
        writeStringToFile(file, charSequence == null ? null : charSequence.toString(), charset, z);
    }

    public static void write(File file, CharSequence charSequence, String str, boolean z) throws IOException {
        write(file, charSequence, Charsets.toCharset(str), z);
    }

    public static void writeByteArrayToFile(File file, byte[] bArr) throws IOException {
        writeByteArrayToFile(file, bArr, false);
    }

    public static void writeByteArrayToFile(File file, byte[] bArr, boolean z) throws IOException {
        writeByteArrayToFile(file, bArr, 0, bArr.length, z);
    }

    public static void writeByteArrayToFile(File file, byte[] bArr, int i, int i2) throws IOException {
        writeByteArrayToFile(file, bArr, i, i2, false);
    }

    public static void writeByteArrayToFile(File file, byte[] bArr, int i, int i2, boolean z) throws IOException {
        file = openOutputStream(file, z);
        try {
            file.write(bArr, i, i2);
            if (file != null) {
                file.close();
                return;
            }
            return;
        } catch (Throwable th) {
            bArr = th;
        }
        if (file != null) {
            if (i != 0) {
                try {
                    file.close();
                } catch (File file2) {
                    i.addSuppressed(file2);
                }
            } else {
                file2.close();
            }
        }
        throw bArr;
        throw bArr;
    }

    public static void writeLines(File file, String str, Collection<?> collection) throws IOException {
        writeLines(file, str, collection, null, false);
    }

    public static void writeLines(File file, String str, Collection<?> collection, boolean z) throws IOException {
        writeLines(file, str, collection, null, z);
    }

    public static void writeLines(File file, Collection<?> collection) throws IOException {
        writeLines(file, null, collection, null, false);
    }

    public static void writeLines(File file, Collection<?> collection, boolean z) throws IOException {
        writeLines(file, null, collection, null, z);
    }

    public static void writeLines(File file, String str, Collection<?> collection, String str2) throws IOException {
        writeLines(file, str, collection, str2, false);
    }

    public static void writeLines(File file, String str, Collection<?> collection, String str2, boolean z) throws IOException {
        OutputStream bufferedOutputStream = new BufferedOutputStream(openOutputStream(file, z));
        try {
            IOUtils.writeLines((Collection) collection, str2, bufferedOutputStream, str);
            if (bufferedOutputStream != null) {
                bufferedOutputStream.close();
                return;
            }
            return;
        } catch (Throwable th) {
            file = th;
        }
        throw file;
        if (bufferedOutputStream != null) {
            if (str != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Collection<?> collection2) {
                    str.addSuppressed(collection2);
                }
            } else {
                bufferedOutputStream.close();
            }
        }
        throw file;
    }

    public static void writeLines(File file, Collection<?> collection, String str) throws IOException {
        writeLines(file, null, collection, str, false);
    }

    public static void writeLines(File file, Collection<?> collection, String str, boolean z) throws IOException {
        writeLines(file, null, collection, str, z);
    }

    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
            return;
        }
        boolean exists = file.exists();
        if (!file.delete()) {
            if (exists) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to delete file: ");
                stringBuilder.append(file);
                throw new IOException(stringBuilder.toString());
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("File does not exist: ");
            stringBuilder2.append(file);
            throw new FileNotFoundException(stringBuilder2.toString());
        }
    }

    public static void forceDeleteOnExit(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectoryOnExit(file);
        } else {
            file.deleteOnExit();
        }
    }

    private static void deleteDirectoryOnExit(File file) throws IOException {
        if (file.exists()) {
            file.deleteOnExit();
            if (!isSymlink(file)) {
                cleanDirectoryOnExit(file);
            }
        }
    }

    private static void cleanDirectoryOnExit(File file) throws IOException {
        IOException iOException = null;
        for (File forceDeleteOnExit : verifiedListFiles(file)) {
            try {
                forceDeleteOnExit(forceDeleteOnExit);
            } catch (IOException e) {
                iOException = e;
            }
        }
        if (iOException != null) {
            throw iOException;
        }
    }

    public static void forceMkdir(File file) throws IOException {
        StringBuilder stringBuilder;
        if (file.exists()) {
            if (!file.isDirectory()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("File ");
                stringBuilder.append(file);
                stringBuilder.append(" exists and is not a directory. Unable to create directory.");
                throw new IOException(stringBuilder.toString());
            }
        } else if (!file.mkdirs() && !file.isDirectory()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to create directory ");
            stringBuilder.append(file);
            throw new IOException(stringBuilder.toString());
        }
    }

    public static void forceMkdirParent(File file) throws IOException {
        file = file.getParentFile();
        if (file != null) {
            forceMkdir(file);
        }
    }

    public static long sizeOf(File file) {
        if (!file.exists()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(file);
            stringBuilder.append(" does not exist");
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (file.isDirectory()) {
            return sizeOfDirectory0(file);
        } else {
            return file.length();
        }
    }

    public static BigInteger sizeOfAsBigInteger(File file) {
        if (!file.exists()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(file);
            stringBuilder.append(" does not exist");
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (file.isDirectory()) {
            return sizeOfDirectoryBig0(file);
        } else {
            return BigInteger.valueOf(file.length());
        }
    }

    public static long sizeOfDirectory(File file) {
        checkDirectory(file);
        return sizeOfDirectory0(file);
    }

    private static long sizeOfDirectory0(java.io.File r10) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r10 = r10.listFiles();
        r0 = 0;
        if (r10 != 0) goto L_0x0009;
    L_0x0008:
        return r0;
    L_0x0009:
        r2 = r10.length;
        r3 = 0;
        r4 = r0;
    L_0x000c:
        if (r3 >= r2) goto L_0x0027;
    L_0x000e:
        r6 = r10[r3];
        r7 = isSymlink(r6);	 Catch:{ IOException -> 0x0024 }
        if (r7 != 0) goto L_0x0024;	 Catch:{ IOException -> 0x0024 }
    L_0x0016:
        r6 = sizeOf0(r6);	 Catch:{ IOException -> 0x0024 }
        r8 = 0;
        r8 = r4 + r6;
        r4 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1));
        if (r4 >= 0) goto L_0x0023;
    L_0x0021:
        r4 = r8;
        goto L_0x0027;
    L_0x0023:
        r4 = r8;
    L_0x0024:
        r3 = r3 + 1;
        goto L_0x000c;
    L_0x0027:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.FileUtils.sizeOfDirectory0(java.io.File):long");
    }

    private static long sizeOf0(File file) {
        if (file.isDirectory()) {
            return sizeOfDirectory0(file);
        }
        return file.length();
    }

    public static BigInteger sizeOfDirectoryAsBigInteger(File file) {
        checkDirectory(file);
        return sizeOfDirectoryBig0(file);
    }

    private static java.math.BigInteger sizeOfDirectoryBig0(java.io.File r5) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r5 = r5.listFiles();
        if (r5 != 0) goto L_0x0009;
    L_0x0006:
        r5 = java.math.BigInteger.ZERO;
        return r5;
    L_0x0009:
        r0 = java.math.BigInteger.ZERO;
        r1 = r5.length;
        r2 = 0;
    L_0x000d:
        if (r2 >= r1) goto L_0x0023;
    L_0x000f:
        r3 = r5[r2];
        r4 = isSymlink(r3);	 Catch:{ IOException -> 0x0020 }
        if (r4 != 0) goto L_0x0020;	 Catch:{ IOException -> 0x0020 }
    L_0x0017:
        r3 = sizeOfBig0(r3);	 Catch:{ IOException -> 0x0020 }
        r3 = r0.add(r3);	 Catch:{ IOException -> 0x0020 }
        r0 = r3;
    L_0x0020:
        r2 = r2 + 1;
        goto L_0x000d;
    L_0x0023:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.FileUtils.sizeOfDirectoryBig0(java.io.File):java.math.BigInteger");
    }

    private static BigInteger sizeOfBig0(File file) {
        if (file.isDirectory()) {
            return sizeOfDirectoryBig0(file);
        }
        return BigInteger.valueOf(file.length());
    }

    private static void checkDirectory(File file) {
        StringBuilder stringBuilder;
        if (!file.exists()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(file);
            stringBuilder.append(" does not exist");
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (!file.isDirectory()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(file);
            stringBuilder.append(" is not a directory");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static boolean isFileNewer(File file, File file2) {
        if (file2 == null) {
            throw new IllegalArgumentException("No specified reference file");
        } else if (file2.exists()) {
            return isFileNewer(file, file2.lastModified());
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The reference file '");
            stringBuilder.append(file2);
            stringBuilder.append("' doesn't exist");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static boolean isFileNewer(File file, Date date) {
        if (date != null) {
            return isFileNewer(file, date.getTime());
        }
        throw new IllegalArgumentException("No specified date");
    }

    public static boolean isFileNewer(File file, long j) {
        if (file == null) {
            throw new IllegalArgumentException("No specified file");
        }
        boolean z = false;
        if (!file.exists()) {
            return false;
        }
        if (file.lastModified() > j) {
            z = true;
        }
        return z;
    }

    public static boolean isFileOlder(File file, File file2) {
        if (file2 == null) {
            throw new IllegalArgumentException("No specified reference file");
        } else if (file2.exists()) {
            return isFileOlder(file, file2.lastModified());
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The reference file '");
            stringBuilder.append(file2);
            stringBuilder.append("' doesn't exist");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static boolean isFileOlder(File file, Date date) {
        if (date != null) {
            return isFileOlder(file, date.getTime());
        }
        throw new IllegalArgumentException("No specified date");
    }

    public static boolean isFileOlder(File file, long j) {
        if (file == null) {
            throw new IllegalArgumentException("No specified file");
        }
        boolean z = false;
        if (!file.exists()) {
            return false;
        }
        if (file.lastModified() < j) {
            z = true;
        }
        return z;
    }

    public static long checksumCRC32(File file) throws IOException {
        Object crc32 = new CRC32();
        checksum(file, crc32);
        return crc32.getValue();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.zip.Checksum checksum(java.io.File r2, java.util.zip.Checksum r3) throws java.io.IOException {
        /*
        r0 = r2.isDirectory();
        if (r0 == 0) goto L_0x000e;
    L_0x0006:
        r2 = new java.lang.IllegalArgumentException;
        r3 = "Checksums can't be computed on directories";
        r2.<init>(r3);
        throw r2;
    L_0x000e:
        r0 = new java.util.zip.CheckedInputStream;
        r1 = new java.io.FileInputStream;
        r1.<init>(r2);
        r0.<init>(r1, r3);
        r2 = 0;
        r1 = new org.apache.commons.io.output.NullOutputStream;	 Catch:{ Throwable -> 0x0029 }
        r1.<init>();	 Catch:{ Throwable -> 0x0029 }
        org.apache.commons.io.IOUtils.copy(r0, r1);	 Catch:{ Throwable -> 0x0029 }
        if (r0 == 0) goto L_0x0026;
    L_0x0023:
        r0.close();
    L_0x0026:
        return r3;
    L_0x0027:
        r3 = move-exception;
        goto L_0x002b;
    L_0x0029:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x0027 }
    L_0x002b:
        if (r0 == 0) goto L_0x003b;
    L_0x002d:
        if (r2 == 0) goto L_0x0038;
    L_0x002f:
        r0.close();	 Catch:{ Throwable -> 0x0033 }
        goto L_0x003b;
    L_0x0033:
        r0 = move-exception;
        r2.addSuppressed(r0);
        goto L_0x003b;
    L_0x0038:
        r0.close();
    L_0x003b:
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.FileUtils.checksum(java.io.File, java.util.zip.Checksum):java.util.zip.Checksum");
    }

    public static void moveDirectory(File file, File file2) throws IOException {
        if (file == null) {
            throw new NullPointerException("Source must not be null");
        } else if (file2 == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!file.exists()) {
            r0 = new StringBuilder();
            r0.append("Source '");
            r0.append(file);
            r0.append("' does not exist");
            throw new FileNotFoundException(r0.toString());
        } else if (!file.isDirectory()) {
            r0 = new StringBuilder();
            r0.append("Source '");
            r0.append(file);
            r0.append("' is not a directory");
            throw new IOException(r0.toString());
        } else if (file2.exists()) {
            r0 = new StringBuilder();
            r0.append("Destination '");
            r0.append(file2);
            r0.append("' already exists");
            throw new FileExistsException(r0.toString());
        } else if (!file.renameTo(file2)) {
            String canonicalPath = file2.getCanonicalPath();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(file.getCanonicalPath());
            stringBuilder.append(File.separator);
            if (canonicalPath.startsWith(stringBuilder.toString())) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Cannot move directory: ");
                stringBuilder.append(file);
                stringBuilder.append(" to a subdirectory of itself: ");
                stringBuilder.append(file2);
                throw new IOException(stringBuilder.toString());
            }
            copyDirectory(file, file2);
            deleteDirectory(file);
            if (file.exists()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to delete original directory '");
                stringBuilder.append(file);
                stringBuilder.append("' after copy to '");
                stringBuilder.append(file2);
                stringBuilder.append("'");
                throw new IOException(stringBuilder.toString());
            }
        }
    }

    public static void moveDirectoryToDirectory(File file, File file2, boolean z) throws IOException {
        if (file == null) {
            throw new NullPointerException("Source must not be null");
        } else if (file2 == null) {
            throw new NullPointerException("Destination directory must not be null");
        } else {
            if (!file2.exists() && z) {
                file2.mkdirs();
            }
            if (!file2.exists()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Destination directory '");
                stringBuilder.append(file2);
                stringBuilder.append("' does not exist [createDestDir=");
                stringBuilder.append(z);
                stringBuilder.append("]");
                throw new FileNotFoundException(stringBuilder.toString());
            } else if (file2.isDirectory()) {
                moveDirectory(file, new File(file2, file.getName()));
            } else {
                z = new StringBuilder();
                z.append("Destination '");
                z.append(file2);
                z.append("' is not a directory");
                throw new IOException(z.toString());
            }
        }
    }

    public static void moveFile(File file, File file2) throws IOException {
        if (file == null) {
            throw new NullPointerException("Source must not be null");
        } else if (file2 == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!file.exists()) {
            r0 = new StringBuilder();
            r0.append("Source '");
            r0.append(file);
            r0.append("' does not exist");
            throw new FileNotFoundException(r0.toString());
        } else if (file.isDirectory()) {
            r0 = new StringBuilder();
            r0.append("Source '");
            r0.append(file);
            r0.append("' is a directory");
            throw new IOException(r0.toString());
        } else if (file2.exists()) {
            r0 = new StringBuilder();
            r0.append("Destination '");
            r0.append(file2);
            r0.append("' already exists");
            throw new FileExistsException(r0.toString());
        } else if (file2.isDirectory()) {
            r0 = new StringBuilder();
            r0.append("Destination '");
            r0.append(file2);
            r0.append("' is a directory");
            throw new IOException(r0.toString());
        } else if (!file.renameTo(file2)) {
            copyFile(file, file2);
            if (!file.delete()) {
                deleteQuietly(file2);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to delete original file '");
                stringBuilder.append(file);
                stringBuilder.append("' after copy to '");
                stringBuilder.append(file2);
                stringBuilder.append("'");
                throw new IOException(stringBuilder.toString());
            }
        }
    }

    public static void moveFileToDirectory(File file, File file2, boolean z) throws IOException {
        if (file == null) {
            throw new NullPointerException("Source must not be null");
        } else if (file2 == null) {
            throw new NullPointerException("Destination directory must not be null");
        } else {
            if (!file2.exists() && z) {
                file2.mkdirs();
            }
            if (!file2.exists()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Destination directory '");
                stringBuilder.append(file2);
                stringBuilder.append("' does not exist [createDestDir=");
                stringBuilder.append(z);
                stringBuilder.append("]");
                throw new FileNotFoundException(stringBuilder.toString());
            } else if (file2.isDirectory()) {
                moveFile(file, new File(file2, file.getName()));
            } else {
                z = new StringBuilder();
                z.append("Destination '");
                z.append(file2);
                z.append("' is not a directory");
                throw new IOException(z.toString());
            }
        }
    }

    public static void moveToDirectory(File file, File file2, boolean z) throws IOException {
        if (file == null) {
            throw new NullPointerException("Source must not be null");
        } else if (file2 == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!file.exists()) {
            z = new StringBuilder();
            z.append("Source '");
            z.append(file);
            z.append("' does not exist");
            throw new FileNotFoundException(z.toString());
        } else if (file.isDirectory()) {
            moveDirectoryToDirectory(file, file2, z);
        } else {
            moveFileToDirectory(file, file2, z);
        }
    }

    public static boolean isSymlink(File file) throws IOException {
        if (file != null) {
            return Files.isSymbolicLink(file.toPath());
        }
        throw new NullPointerException("File must not be null");
    }
}
