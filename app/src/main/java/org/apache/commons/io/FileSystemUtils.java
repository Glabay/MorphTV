package org.apache.commons.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import org.apache.commons.lang3.StringUtils;

@Deprecated
public class FileSystemUtils {
    private static final String DF;
    private static final int INIT_PROBLEM = -1;
    private static final FileSystemUtils INSTANCE = new FileSystemUtils();
    private static final int OS;
    private static final int OTHER = 0;
    private static final int POSIX_UNIX = 3;
    private static final int UNIX = 2;
    private static final int WINDOWS = 1;

    static {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = new org.apache.commons.io.FileSystemUtils;
        r0.<init>();
        INSTANCE = r0;
        r0 = "df";
        r1 = "os.name";	 Catch:{ Exception -> 0x009e }
        r1 = java.lang.System.getProperty(r1);	 Catch:{ Exception -> 0x009e }
        if (r1 != 0) goto L_0x0019;	 Catch:{ Exception -> 0x009e }
    L_0x0011:
        r1 = new java.io.IOException;	 Catch:{ Exception -> 0x009e }
        r2 = "os.name not found";	 Catch:{ Exception -> 0x009e }
        r1.<init>(r2);	 Catch:{ Exception -> 0x009e }
        throw r1;	 Catch:{ Exception -> 0x009e }
    L_0x0019:
        r2 = java.util.Locale.ENGLISH;	 Catch:{ Exception -> 0x009e }
        r1 = r1.toLowerCase(r2);	 Catch:{ Exception -> 0x009e }
        r2 = "windows";	 Catch:{ Exception -> 0x009e }
        r2 = r1.contains(r2);	 Catch:{ Exception -> 0x009e }
        r3 = 3;	 Catch:{ Exception -> 0x009e }
        if (r2 == 0) goto L_0x002b;	 Catch:{ Exception -> 0x009e }
    L_0x0028:
        r3 = 1;	 Catch:{ Exception -> 0x009e }
        goto L_0x009f;	 Catch:{ Exception -> 0x009e }
    L_0x002b:
        r2 = "linux";	 Catch:{ Exception -> 0x009e }
        r2 = r1.contains(r2);	 Catch:{ Exception -> 0x009e }
        if (r2 != 0) goto L_0x009c;	 Catch:{ Exception -> 0x009e }
    L_0x0033:
        r2 = "mpe/ix";	 Catch:{ Exception -> 0x009e }
        r2 = r1.contains(r2);	 Catch:{ Exception -> 0x009e }
        if (r2 != 0) goto L_0x009c;	 Catch:{ Exception -> 0x009e }
    L_0x003b:
        r2 = "freebsd";	 Catch:{ Exception -> 0x009e }
        r2 = r1.contains(r2);	 Catch:{ Exception -> 0x009e }
        if (r2 != 0) goto L_0x009c;	 Catch:{ Exception -> 0x009e }
    L_0x0043:
        r2 = "openbsd";	 Catch:{ Exception -> 0x009e }
        r2 = r1.contains(r2);	 Catch:{ Exception -> 0x009e }
        if (r2 != 0) goto L_0x009c;	 Catch:{ Exception -> 0x009e }
    L_0x004b:
        r2 = "irix";	 Catch:{ Exception -> 0x009e }
        r2 = r1.contains(r2);	 Catch:{ Exception -> 0x009e }
        if (r2 != 0) goto L_0x009c;	 Catch:{ Exception -> 0x009e }
    L_0x0053:
        r2 = "digital unix";	 Catch:{ Exception -> 0x009e }
        r2 = r1.contains(r2);	 Catch:{ Exception -> 0x009e }
        if (r2 != 0) goto L_0x009c;	 Catch:{ Exception -> 0x009e }
    L_0x005b:
        r2 = "unix";	 Catch:{ Exception -> 0x009e }
        r2 = r1.contains(r2);	 Catch:{ Exception -> 0x009e }
        if (r2 != 0) goto L_0x009c;	 Catch:{ Exception -> 0x009e }
    L_0x0063:
        r2 = "mac os x";	 Catch:{ Exception -> 0x009e }
        r2 = r1.contains(r2);	 Catch:{ Exception -> 0x009e }
        if (r2 == 0) goto L_0x006c;	 Catch:{ Exception -> 0x009e }
    L_0x006b:
        goto L_0x009c;	 Catch:{ Exception -> 0x009e }
    L_0x006c:
        r2 = "sun os";	 Catch:{ Exception -> 0x009e }
        r2 = r1.contains(r2);	 Catch:{ Exception -> 0x009e }
        if (r2 != 0) goto L_0x0098;	 Catch:{ Exception -> 0x009e }
    L_0x0074:
        r2 = "sunos";	 Catch:{ Exception -> 0x009e }
        r2 = r1.contains(r2);	 Catch:{ Exception -> 0x009e }
        if (r2 != 0) goto L_0x0098;	 Catch:{ Exception -> 0x009e }
    L_0x007c:
        r2 = "solaris";	 Catch:{ Exception -> 0x009e }
        r2 = r1.contains(r2);	 Catch:{ Exception -> 0x009e }
        if (r2 == 0) goto L_0x0085;	 Catch:{ Exception -> 0x009e }
    L_0x0084:
        goto L_0x0098;	 Catch:{ Exception -> 0x009e }
    L_0x0085:
        r2 = "hp-ux";	 Catch:{ Exception -> 0x009e }
        r2 = r1.contains(r2);	 Catch:{ Exception -> 0x009e }
        if (r2 != 0) goto L_0x009f;	 Catch:{ Exception -> 0x009e }
    L_0x008d:
        r2 = "aix";	 Catch:{ Exception -> 0x009e }
        r1 = r1.contains(r2);	 Catch:{ Exception -> 0x009e }
        if (r1 == 0) goto L_0x0096;	 Catch:{ Exception -> 0x009e }
    L_0x0095:
        goto L_0x009f;	 Catch:{ Exception -> 0x009e }
    L_0x0096:
        r3 = 0;	 Catch:{ Exception -> 0x009e }
        goto L_0x009f;	 Catch:{ Exception -> 0x009e }
    L_0x0098:
        r1 = "/usr/xpg4/bin/df";	 Catch:{ Exception -> 0x009e }
        r0 = r1;
        goto L_0x009f;
    L_0x009c:
        r3 = 2;
        goto L_0x009f;
    L_0x009e:
        r3 = -1;
    L_0x009f:
        OS = r3;
        DF = r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.FileSystemUtils.<clinit>():void");
    }

    @Deprecated
    public static long freeSpace(String str) throws IOException {
        return INSTANCE.freeSpaceOS(str, OS, false, -1);
    }

    @Deprecated
    public static long freeSpaceKb(String str) throws IOException {
        return freeSpaceKb(str, -1);
    }

    @Deprecated
    public static long freeSpaceKb(String str, long j) throws IOException {
        return INSTANCE.freeSpaceOS(str, OS, true, j);
    }

    @Deprecated
    public static long freeSpaceKb() throws IOException {
        return freeSpaceKb(-1);
    }

    @Deprecated
    public static long freeSpaceKb(long j) throws IOException {
        return freeSpaceKb(new File(".").getAbsolutePath(), j);
    }

    long freeSpaceOS(String str, int i, boolean z, long j) throws IOException {
        if (str == null) {
            throw new IllegalArgumentException("Path must not be null");
        }
        switch (i) {
            case 0:
                throw new IllegalStateException("Unsupported operating system");
            case 1:
                return z ? freeSpaceWindows(str, j) / true : freeSpaceWindows(str, j);
            case 2:
                return freeSpaceUnix(str, z, false, j);
            case 3:
                return freeSpaceUnix(str, z, true, j);
            default:
                throw new IllegalStateException("Exception caught when determining operating system");
        }
    }

    long freeSpaceWindows(String str, long j) throws IOException {
        String normalize = FilenameUtils.normalize(str, false);
        if (normalize == null) {
            throw new IllegalArgumentException(str);
        }
        if (normalize.length() > null && normalize.charAt(0) != 34) {
            str = new StringBuilder();
            str.append("\"");
            str.append(normalize);
            str.append("\"");
            normalize = str.toString();
        }
        str = new String[3];
        str[0] = "cmd.exe";
        str[1] = "/C";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("dir /a /-c ");
        stringBuilder.append(normalize);
        str[2] = stringBuilder.toString();
        str = performCommand(str, Integer.MAX_VALUE, j);
        for (j = str.size() - 1; j >= null; j--) {
            String str2 = (String) str.get(j);
            if (str2.length() > 0) {
                return parseDir(str2, normalize);
            }
        }
        j = new StringBuilder();
        j.append("Command line 'dir /-c' did not return any info for path '");
        j.append(normalize);
        j.append("'");
        throw new IOException(j.toString());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    long parseDir(java.lang.String r8, java.lang.String r9) throws java.io.IOException {
        /*
        r7 = this;
        r0 = r8.length();
        r0 = r0 + -1;
    L_0x0006:
        r1 = 0;
        if (r0 < 0) goto L_0x0019;
    L_0x0009:
        r2 = r8.charAt(r0);
        r2 = java.lang.Character.isDigit(r2);
        if (r2 == 0) goto L_0x0016;
    L_0x0013:
        r2 = r0 + 1;
        goto L_0x001a;
    L_0x0016:
        r0 = r0 + -1;
        goto L_0x0006;
    L_0x0019:
        r2 = 0;
    L_0x001a:
        r3 = 46;
        r4 = 44;
        if (r0 < 0) goto L_0x0034;
    L_0x0020:
        r5 = r8.charAt(r0);
        r6 = java.lang.Character.isDigit(r5);
        if (r6 != 0) goto L_0x0031;
    L_0x002a:
        if (r5 == r4) goto L_0x0031;
    L_0x002c:
        if (r5 == r3) goto L_0x0031;
    L_0x002e:
        r5 = r0 + 1;
        goto L_0x0035;
    L_0x0031:
        r0 = r0 + -1;
        goto L_0x001a;
    L_0x0034:
        r5 = 0;
    L_0x0035:
        if (r0 >= 0) goto L_0x0053;
    L_0x0037:
        r8 = new java.io.IOException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Command line 'dir /-c' did not return valid info for path '";
        r0.append(r1);
        r0.append(r9);
        r9 = "'";
        r0.append(r9);
        r9 = r0.toString();
        r8.<init>(r9);
        throw r8;
    L_0x0053:
        r0 = new java.lang.StringBuilder;
        r8 = r8.substring(r5, r2);
        r0.<init>(r8);
    L_0x005c:
        r8 = r0.length();
        if (r1 >= r8) goto L_0x0079;
    L_0x0062:
        r8 = r0.charAt(r1);
        if (r8 == r4) goto L_0x0071;
    L_0x0068:
        r8 = r0.charAt(r1);
        if (r8 != r3) goto L_0x006f;
    L_0x006e:
        goto L_0x0071;
    L_0x006f:
        r8 = r1;
        goto L_0x0076;
    L_0x0071:
        r8 = r1 + -1;
        r0.deleteCharAt(r1);
    L_0x0076:
        r1 = r8 + 1;
        goto L_0x005c;
    L_0x0079:
        r8 = r0.toString();
        r8 = r7.parseBytes(r8, r9);
        return r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.FileSystemUtils.parseDir(java.lang.String, java.lang.String):long");
    }

    long freeSpaceUnix(String str, boolean z, boolean z2, long j) throws IOException {
        if (str.isEmpty()) {
            throw new IllegalArgumentException("Path must not be empty");
        }
        String str2 = "-";
        if (z) {
            z = new StringBuilder();
            z.append(str2);
            z.append("k");
            str2 = z.toString();
        }
        if (z2) {
            z = new StringBuilder();
            z.append(str2);
            z.append("P");
            str2 = z.toString();
        }
        z = performCommand(str2.length() > true ? new String[]{DF, str2, str} : new String[]{DF, str}, 3, j);
        if (z.size() < true) {
            j = new StringBuilder();
            j.append("Command line '");
            j.append(DF);
            j.append("' did not return info as expected for path '");
            j.append(str);
            j.append("'- response was ");
            j.append(z);
            throw new IOException(j.toString());
        }
        j = new StringTokenizer((String) z.get(1), StringUtils.SPACE);
        if (j.countTokens() >= true) {
            j.nextToken();
        } else if (!j.countTokens() || z.size() < true) {
            z2 = new StringBuilder();
            z2.append("Command line '");
            z2.append(DF);
            z2.append("' did not return data as expected for path '");
            z2.append(str);
            z2.append("'- check path is valid");
            throw new IOException(z2.toString());
        } else {
            j = new StringTokenizer((String) z.get(2), StringUtils.SPACE);
        }
        j.nextToken();
        j.nextToken();
        return parseBytes(j.nextToken(), str);
    }

    long parseBytes(String str, String str2) throws IOException {
        try {
            long parseLong = Long.parseLong(str);
            if (parseLong >= 0) {
                return parseLong;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Command line '");
            stringBuilder.append(DF);
            stringBuilder.append("' did not find free space in response for path '");
            stringBuilder.append(str2);
            stringBuilder.append("'- check path is valid");
            throw new IOException(stringBuilder.toString());
        } catch (String str3) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Command line '");
            stringBuilder2.append(DF);
            stringBuilder2.append("' did not return numeric data as expected for path '");
            stringBuilder2.append(str2);
            stringBuilder2.append("'- check path is valid");
            throw new IOException(stringBuilder2.toString(), str3);
        }
    }

    List<String> performCommand(String[] strArr, int i, long j) throws IOException {
        Process openProcess;
        InputStream inputStream;
        OutputStream outputStream;
        InputStream errorStream;
        Reader bufferedReader;
        StringBuilder stringBuilder;
        List<String> arrayList = new ArrayList(20);
        Process process = null;
        try {
            Thread start = ThreadMonitor.start(j);
            openProcess = openProcess(strArr);
            try {
                inputStream = openProcess.getInputStream();
                try {
                    outputStream = openProcess.getOutputStream();
                    try {
                        errorStream = openProcess.getErrorStream();
                        try {
                            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));
                        } catch (InterruptedException e) {
                            i = e;
                            bufferedReader = null;
                            process = openProcess;
                            try {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("Command line threw an InterruptedException for command ");
                                stringBuilder.append(Arrays.asList(strArr));
                                stringBuilder.append(" timeout=");
                                stringBuilder.append(j);
                                throw new IOException(stringBuilder.toString(), i);
                            } catch (Throwable th) {
                                strArr = th;
                                openProcess = process;
                                IOUtils.closeQuietly(inputStream);
                                IOUtils.closeQuietly(outputStream);
                                IOUtils.closeQuietly(errorStream);
                                IOUtils.closeQuietly(bufferedReader);
                                if (openProcess != null) {
                                    openProcess.destroy();
                                }
                                throw strArr;
                            }
                        } catch (Throwable th2) {
                            strArr = th2;
                            bufferedReader = null;
                            IOUtils.closeQuietly(inputStream);
                            IOUtils.closeQuietly(outputStream);
                            IOUtils.closeQuietly(errorStream);
                            IOUtils.closeQuietly(bufferedReader);
                            if (openProcess != null) {
                                openProcess.destroy();
                            }
                            throw strArr;
                        }
                        try {
                            for (String readLine = bufferedReader.readLine(); readLine != null && arrayList.size() < i; readLine = bufferedReader.readLine()) {
                                arrayList.add(readLine.toLowerCase(Locale.ENGLISH).trim());
                            }
                            openProcess.waitFor();
                            ThreadMonitor.stop(start);
                            StringBuilder stringBuilder2;
                            if (openProcess.exitValue() != 0) {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("Command line returned OS error code '");
                                stringBuilder2.append(openProcess.exitValue());
                                stringBuilder2.append("' for command ");
                                stringBuilder2.append(Arrays.asList(strArr));
                                throw new IOException(stringBuilder2.toString());
                            } else if (arrayList.isEmpty() != 0) {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("Command line did not return any info for command ");
                                stringBuilder2.append(Arrays.asList(strArr));
                                throw new IOException(stringBuilder2.toString());
                            } else {
                                bufferedReader.close();
                                inputStream.close();
                                if (outputStream != null) {
                                    try {
                                        outputStream.close();
                                        outputStream = null;
                                    } catch (InterruptedException e2) {
                                        i = e2;
                                        inputStream = null;
                                        bufferedReader = inputStream;
                                        process = openProcess;
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append("Command line threw an InterruptedException for command ");
                                        stringBuilder.append(Arrays.asList(strArr));
                                        stringBuilder.append(" timeout=");
                                        stringBuilder.append(j);
                                        throw new IOException(stringBuilder.toString(), i);
                                    } catch (Throwable th3) {
                                        strArr = th3;
                                        inputStream = null;
                                        bufferedReader = inputStream;
                                        IOUtils.closeQuietly(inputStream);
                                        IOUtils.closeQuietly(outputStream);
                                        IOUtils.closeQuietly(errorStream);
                                        IOUtils.closeQuietly(bufferedReader);
                                        if (openProcess != null) {
                                            openProcess.destroy();
                                        }
                                        throw strArr;
                                    }
                                }
                                if (errorStream != null) {
                                    errorStream.close();
                                    errorStream = null;
                                }
                                IOUtils.closeQuietly(null);
                                IOUtils.closeQuietly(outputStream);
                                IOUtils.closeQuietly(errorStream);
                                IOUtils.closeQuietly(null);
                                if (openProcess != null) {
                                    openProcess.destroy();
                                }
                                return arrayList;
                            }
                        } catch (InterruptedException e3) {
                            i = e3;
                            process = openProcess;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Command line threw an InterruptedException for command ");
                            stringBuilder.append(Arrays.asList(strArr));
                            stringBuilder.append(" timeout=");
                            stringBuilder.append(j);
                            throw new IOException(stringBuilder.toString(), i);
                        } catch (Throwable th4) {
                            strArr = th4;
                            IOUtils.closeQuietly(inputStream);
                            IOUtils.closeQuietly(outputStream);
                            IOUtils.closeQuietly(errorStream);
                            IOUtils.closeQuietly(bufferedReader);
                            if (openProcess != null) {
                                openProcess.destroy();
                            }
                            throw strArr;
                        }
                    } catch (InterruptedException e4) {
                        i = e4;
                        errorStream = null;
                        bufferedReader = errorStream;
                        process = openProcess;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Command line threw an InterruptedException for command ");
                        stringBuilder.append(Arrays.asList(strArr));
                        stringBuilder.append(" timeout=");
                        stringBuilder.append(j);
                        throw new IOException(stringBuilder.toString(), i);
                    } catch (Throwable th5) {
                        strArr = th5;
                        errorStream = null;
                        bufferedReader = errorStream;
                        IOUtils.closeQuietly(inputStream);
                        IOUtils.closeQuietly(outputStream);
                        IOUtils.closeQuietly(errorStream);
                        IOUtils.closeQuietly(bufferedReader);
                        if (openProcess != null) {
                            openProcess.destroy();
                        }
                        throw strArr;
                    }
                } catch (InterruptedException e5) {
                    i = e5;
                    outputStream = null;
                    errorStream = outputStream;
                    bufferedReader = errorStream;
                    process = openProcess;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Command line threw an InterruptedException for command ");
                    stringBuilder.append(Arrays.asList(strArr));
                    stringBuilder.append(" timeout=");
                    stringBuilder.append(j);
                    throw new IOException(stringBuilder.toString(), i);
                } catch (Throwable th6) {
                    strArr = th6;
                    outputStream = null;
                    errorStream = outputStream;
                    bufferedReader = errorStream;
                    IOUtils.closeQuietly(inputStream);
                    IOUtils.closeQuietly(outputStream);
                    IOUtils.closeQuietly(errorStream);
                    IOUtils.closeQuietly(bufferedReader);
                    if (openProcess != null) {
                        openProcess.destroy();
                    }
                    throw strArr;
                }
            } catch (InterruptedException e6) {
                i = e6;
                inputStream = null;
                outputStream = inputStream;
                errorStream = outputStream;
                bufferedReader = errorStream;
                process = openProcess;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Command line threw an InterruptedException for command ");
                stringBuilder.append(Arrays.asList(strArr));
                stringBuilder.append(" timeout=");
                stringBuilder.append(j);
                throw new IOException(stringBuilder.toString(), i);
            } catch (Throwable th7) {
                strArr = th7;
                inputStream = null;
                outputStream = inputStream;
                errorStream = outputStream;
                bufferedReader = errorStream;
                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly(outputStream);
                IOUtils.closeQuietly(errorStream);
                IOUtils.closeQuietly(bufferedReader);
                if (openProcess != null) {
                    openProcess.destroy();
                }
                throw strArr;
            }
        } catch (InterruptedException e7) {
            i = e7;
            inputStream = null;
            outputStream = inputStream;
            errorStream = outputStream;
            bufferedReader = errorStream;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Command line threw an InterruptedException for command ");
            stringBuilder.append(Arrays.asList(strArr));
            stringBuilder.append(" timeout=");
            stringBuilder.append(j);
            throw new IOException(stringBuilder.toString(), i);
        } catch (Throwable th8) {
            strArr = th8;
            openProcess = null;
            inputStream = openProcess;
            outputStream = inputStream;
            errorStream = outputStream;
            bufferedReader = errorStream;
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(errorStream);
            IOUtils.closeQuietly(bufferedReader);
            if (openProcess != null) {
                openProcess.destroy();
            }
            throw strArr;
        }
    }

    Process openProcess(String[] strArr) throws IOException {
        return Runtime.getRuntime().exec(strArr);
    }
}
