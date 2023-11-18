package net.lingala.zip4j.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipModel;

public class Zip4jUtil {
    public static void setFileArchive(File file) throws ZipException {
    }

    public static void setFileHidden(File file) throws ZipException {
    }

    public static void setFileSystemMode(File file) throws ZipException {
    }

    public static boolean isStringNotNullAndNotEmpty(String str) {
        if (str != null) {
            if (str.trim().length() > null) {
                return true;
            }
        }
        return null;
    }

    public static boolean checkOutputFolder(java.lang.String r2) throws net.lingala.zip4j.exception.ZipException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = isStringNotNullAndNotEmpty(r2);
        if (r0 != 0) goto L_0x0013;
    L_0x0006:
        r2 = new net.lingala.zip4j.exception.ZipException;
        r0 = new java.lang.NullPointerException;
        r1 = "output path is null";
        r0.<init>(r1);
        r2.<init>(r0);
        throw r2;
    L_0x0013:
        r0 = new java.io.File;
        r0.<init>(r2);
        r2 = r0.exists();
        if (r2 == 0) goto L_0x003a;
    L_0x001e:
        r2 = r0.isDirectory();
        if (r2 != 0) goto L_0x002c;
    L_0x0024:
        r2 = new net.lingala.zip4j.exception.ZipException;
        r0 = "output folder is not valid";
        r2.<init>(r0);
        throw r2;
    L_0x002c:
        r2 = r0.canWrite();
        if (r2 != 0) goto L_0x0059;
    L_0x0032:
        r2 = new net.lingala.zip4j.exception.ZipException;
        r0 = "no write access to output folder";
        r2.<init>(r0);
        throw r2;
    L_0x003a:
        r0.mkdirs();	 Catch:{ Exception -> 0x005b }
        r2 = r0.isDirectory();	 Catch:{ Exception -> 0x005b }
        if (r2 != 0) goto L_0x004b;	 Catch:{ Exception -> 0x005b }
    L_0x0043:
        r2 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ Exception -> 0x005b }
        r0 = "output folder is not valid";	 Catch:{ Exception -> 0x005b }
        r2.<init>(r0);	 Catch:{ Exception -> 0x005b }
        throw r2;	 Catch:{ Exception -> 0x005b }
    L_0x004b:
        r2 = r0.canWrite();	 Catch:{ Exception -> 0x005b }
        if (r2 != 0) goto L_0x0059;	 Catch:{ Exception -> 0x005b }
    L_0x0051:
        r2 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ Exception -> 0x005b }
        r0 = "no write access to destination folder";	 Catch:{ Exception -> 0x005b }
        r2.<init>(r0);	 Catch:{ Exception -> 0x005b }
        throw r2;	 Catch:{ Exception -> 0x005b }
    L_0x0059:
        r2 = 1;
        return r2;
    L_0x005b:
        r2 = new net.lingala.zip4j.exception.ZipException;
        r0 = "Cannot create destination folder";
        r2.<init>(r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.util.Zip4jUtil.checkOutputFolder(java.lang.String):boolean");
    }

    public static boolean checkFileReadAccess(java.lang.String r3) throws net.lingala.zip4j.exception.ZipException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = isStringNotNullAndNotEmpty(r3);
        if (r0 != 0) goto L_0x000e;
    L_0x0006:
        r3 = new net.lingala.zip4j.exception.ZipException;
        r0 = "path is null";
        r3.<init>(r0);
        throw r3;
    L_0x000e:
        r0 = checkFileExists(r3);
        if (r0 != 0) goto L_0x0028;
    L_0x0014:
        r0 = new net.lingala.zip4j.exception.ZipException;
        r1 = new java.lang.StringBuffer;
        r2 = "file does not exist: ";
        r1.<init>(r2);
        r1.append(r3);
        r3 = r1.toString();
        r0.<init>(r3);
        throw r0;
    L_0x0028:
        r0 = new java.io.File;	 Catch:{ Exception -> 0x0032 }
        r0.<init>(r3);	 Catch:{ Exception -> 0x0032 }
        r3 = r0.canRead();	 Catch:{ Exception -> 0x0032 }
        return r3;
    L_0x0032:
        r3 = new net.lingala.zip4j.exception.ZipException;
        r0 = "cannot read zip file";
        r3.<init>(r0);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.util.Zip4jUtil.checkFileReadAccess(java.lang.String):boolean");
    }

    public static boolean checkFileWriteAccess(java.lang.String r3) throws net.lingala.zip4j.exception.ZipException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = isStringNotNullAndNotEmpty(r3);
        if (r0 != 0) goto L_0x000e;
    L_0x0006:
        r3 = new net.lingala.zip4j.exception.ZipException;
        r0 = "path is null";
        r3.<init>(r0);
        throw r3;
    L_0x000e:
        r0 = checkFileExists(r3);
        if (r0 != 0) goto L_0x0028;
    L_0x0014:
        r0 = new net.lingala.zip4j.exception.ZipException;
        r1 = new java.lang.StringBuffer;
        r2 = "file does not exist: ";
        r1.<init>(r2);
        r1.append(r3);
        r3 = r1.toString();
        r0.<init>(r3);
        throw r0;
    L_0x0028:
        r0 = new java.io.File;	 Catch:{ Exception -> 0x0032 }
        r0.<init>(r3);	 Catch:{ Exception -> 0x0032 }
        r3 = r0.canWrite();	 Catch:{ Exception -> 0x0032 }
        return r3;
    L_0x0032:
        r3 = new net.lingala.zip4j.exception.ZipException;
        r0 = "cannot read zip file";
        r3.<init>(r0);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.util.Zip4jUtil.checkFileWriteAccess(java.lang.String):boolean");
    }

    public static boolean checkFileExists(String str) throws ZipException {
        if (isStringNotNullAndNotEmpty(str)) {
            return checkFileExists(new File(str));
        }
        throw new ZipException("path is null");
    }

    public static boolean checkFileExists(File file) throws ZipException {
        if (file != null) {
            return file.exists();
        }
        throw new ZipException("cannot check if file exists: input file is null");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
    }

    public static void setFileReadOnly(File file) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null. cannot set read only file attribute");
        } else if (file.exists()) {
            file.setReadOnly();
        }
    }

    public static long getLastModifiedFileTime(File file, TimeZone timeZone) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null, cannot read last modified file time");
        } else if (file.exists() != null) {
            return file.lastModified();
        } else {
            throw new ZipException("input file does not exist, cannot read last modified file time");
        }
    }

    public static String getFileNameFromFilePath(File file) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null, cannot get file name");
        } else if (file.isDirectory()) {
            return null;
        } else {
            return file.getName();
        }
    }

    public static long getFileLengh(String str) throws ZipException {
        if (isStringNotNullAndNotEmpty(str)) {
            return getFileLengh(new File(str));
        }
        throw new ZipException("invalid file name");
    }

    public static long getFileLengh(File file) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null, cannot calculate file length");
        } else if (file.isDirectory()) {
            return -1;
        } else {
            return file.length();
        }
    }

    public static long javaToDosTime(long j) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        int i = instance.get(1);
        if (i < 1980) {
            return 2162688;
        }
        return (long) ((instance.get(13) >> 1) | ((((((i - 1980) << 25) | ((instance.get(2) + 1) << 21)) | (instance.get(5) << 16)) | (instance.get(11) << 11)) | (instance.get(12) << 5)));
    }

    public static long dosToJavaTme(int i) {
        int i2 = (i & 31) * 2;
        int i3 = (i >> 5) & 63;
        int i4 = (i >> 11) & 31;
        int i5 = (i >> 16) & 31;
        int i6 = ((i >> 21) & 15) - 1;
        int i7 = ((i >> 25) & 127) + 1980;
        i = Calendar.getInstance();
        i.set(i7, i6, i5, i4, i3, i2);
        i.set(14, 0);
        return i.getTime().getTime();
    }

    public static FileHeader getFileHeader(ZipModel zipModel, String str) throws ZipException {
        StringBuffer stringBuffer;
        if (zipModel == null) {
            stringBuffer = new StringBuffer("zip model is null, cannot determine file header for fileName: ");
            stringBuffer.append(str);
            throw new ZipException(stringBuffer.toString());
        } else if (isStringNotNullAndNotEmpty(str)) {
            FileHeader fileHeaderWithExactMatch = getFileHeaderWithExactMatch(zipModel, str);
            if (fileHeaderWithExactMatch != null) {
                return fileHeaderWithExactMatch;
            }
            str = str.replaceAll("\\\\", "/");
            fileHeaderWithExactMatch = getFileHeaderWithExactMatch(zipModel, str);
            return fileHeaderWithExactMatch == null ? getFileHeaderWithExactMatch(zipModel, str.replaceAll("/", "\\\\")) : fileHeaderWithExactMatch;
        } else {
            stringBuffer = new StringBuffer("file name is null, cannot determine file header for fileName: ");
            stringBuffer.append(str);
            throw new ZipException(stringBuffer.toString());
        }
    }

    public static FileHeader getFileHeaderWithExactMatch(ZipModel zipModel, String str) throws ZipException {
        StringBuffer stringBuffer;
        if (zipModel == null) {
            stringBuffer = new StringBuffer("zip model is null, cannot determine file header with exact match for fileName: ");
            stringBuffer.append(str);
            throw new ZipException(stringBuffer.toString());
        } else if (!isStringNotNullAndNotEmpty(str)) {
            stringBuffer = new StringBuffer("file name is null, cannot determine file header with exact match for fileName: ");
            stringBuffer.append(str);
            throw new ZipException(stringBuffer.toString());
        } else if (zipModel.getCentralDirectory() == null) {
            stringBuffer = new StringBuffer("central directory is null, cannot determine file header with exact match for fileName: ");
            stringBuffer.append(str);
            throw new ZipException(stringBuffer.toString());
        } else if (zipModel.getCentralDirectory().getFileHeaders() == null) {
            stringBuffer = new StringBuffer("file Headers are null, cannot determine file header with exact match for fileName: ");
            stringBuffer.append(str);
            throw new ZipException(stringBuffer.toString());
        } else if (zipModel.getCentralDirectory().getFileHeaders().size() <= 0) {
            return null;
        } else {
            zipModel = zipModel.getCentralDirectory().getFileHeaders();
            for (int i = 0; i < zipModel.size(); i++) {
                FileHeader fileHeader = (FileHeader) zipModel.get(i);
                String fileName = fileHeader.getFileName();
                if (isStringNotNullAndNotEmpty(fileName)) {
                    if (str.equalsIgnoreCase(fileName)) {
                        return fileHeader;
                    }
                }
            }
            return null;
        }
    }

    public static int getIndexOfFileHeader(ZipModel zipModel, FileHeader fileHeader) throws ZipException {
        if (zipModel != null) {
            if (fileHeader != null) {
                if (zipModel.getCentralDirectory() == null) {
                    throw new ZipException("central directory is null, ccannot determine index of file header");
                } else if (zipModel.getCentralDirectory().getFileHeaders() == null) {
                    throw new ZipException("file Headers are null, cannot determine index of file header");
                } else if (zipModel.getCentralDirectory().getFileHeaders().size() <= 0) {
                    return -1;
                } else {
                    fileHeader = fileHeader.getFileName();
                    if (isStringNotNullAndNotEmpty(fileHeader)) {
                        zipModel = zipModel.getCentralDirectory().getFileHeaders();
                        for (int i = 0; i < zipModel.size(); i++) {
                            String fileName = ((FileHeader) zipModel.get(i)).getFileName();
                            if (isStringNotNullAndNotEmpty(fileName)) {
                                if (fileHeader.equalsIgnoreCase(fileName)) {
                                    return i;
                                }
                            }
                        }
                        return -1;
                    }
                    throw new ZipException("file name in file header is empty or null, cannot determine index of file header");
                }
            }
        }
        throw new ZipException("input parameters is null, cannot determine index of file header");
    }

    public static ArrayList getFilesInDirectoryRec(File file, boolean z) throws ZipException {
        if (file == null) {
            throw new ZipException("input path is null, cannot read files in the directory");
        }
        ArrayList arrayList = new ArrayList();
        List asList = Arrays.asList(file.listFiles());
        if (file.canRead() == null) {
            return arrayList;
        }
        for (file = null; file < asList.size(); file++) {
            File file2 = (File) asList.get(file);
            if (file2.isHidden() && !z) {
                return arrayList;
            }
            arrayList.add(file2);
            if (file2.isDirectory()) {
                arrayList.addAll(getFilesInDirectoryRec(file2, z));
            }
        }
        return arrayList;
    }

    public static String getZipFileNameWithoutExt(String str) throws ZipException {
        if (isStringNotNullAndNotEmpty(str)) {
            if (str.indexOf(System.getProperty("file.separator")) >= 0) {
                str = str.substring(str.lastIndexOf(System.getProperty("file.separator")));
            }
            return str.indexOf(".") > 0 ? str.substring(0, str.lastIndexOf(".")) : str;
        } else {
            throw new ZipException("zip file name is empty or null, cannot determine zip file name");
        }
    }

    public static byte[] convertCharset(java.lang.String r2) throws net.lingala.zip4j.exception.ZipException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        r0 = (byte[]) r0;	 Catch:{ UnsupportedEncodingException -> 0x0031, Exception -> 0x002a }
        r0 = detectCharSet(r2);	 Catch:{ UnsupportedEncodingException -> 0x0031, Exception -> 0x002a }
        r1 = "Cp850";	 Catch:{ UnsupportedEncodingException -> 0x0031, Exception -> 0x002a }
        r1 = r0.equals(r1);	 Catch:{ UnsupportedEncodingException -> 0x0031, Exception -> 0x002a }
        if (r1 == 0) goto L_0x0016;	 Catch:{ UnsupportedEncodingException -> 0x0031, Exception -> 0x002a }
    L_0x000f:
        r0 = "Cp850";	 Catch:{ UnsupportedEncodingException -> 0x0031, Exception -> 0x002a }
        r0 = r2.getBytes(r0);	 Catch:{ UnsupportedEncodingException -> 0x0031, Exception -> 0x002a }
        goto L_0x0029;	 Catch:{ UnsupportedEncodingException -> 0x0031, Exception -> 0x002a }
    L_0x0016:
        r1 = "UTF8";	 Catch:{ UnsupportedEncodingException -> 0x0031, Exception -> 0x002a }
        r0 = r0.equals(r1);	 Catch:{ UnsupportedEncodingException -> 0x0031, Exception -> 0x002a }
        if (r0 == 0) goto L_0x0025;	 Catch:{ UnsupportedEncodingException -> 0x0031, Exception -> 0x002a }
    L_0x001e:
        r0 = "UTF8";	 Catch:{ UnsupportedEncodingException -> 0x0031, Exception -> 0x002a }
        r0 = r2.getBytes(r0);	 Catch:{ UnsupportedEncodingException -> 0x0031, Exception -> 0x002a }
        goto L_0x0029;	 Catch:{ UnsupportedEncodingException -> 0x0031, Exception -> 0x002a }
    L_0x0025:
        r0 = r2.getBytes();	 Catch:{ UnsupportedEncodingException -> 0x0031, Exception -> 0x002a }
    L_0x0029:
        return r0;
    L_0x002a:
        r2 = move-exception;
        r0 = new net.lingala.zip4j.exception.ZipException;
        r0.<init>(r2);
        throw r0;
    L_0x0031:
        r2 = r2.getBytes();
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.util.Zip4jUtil.convertCharset(java.lang.String):byte[]");
    }

    public static java.lang.String decodeFileName(byte[] r1, boolean r2) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        if (r2 == 0) goto L_0x0010;
    L_0x0002:
        r2 = new java.lang.String;	 Catch:{ UnsupportedEncodingException -> 0x000a }
        r0 = "UTF8";	 Catch:{ UnsupportedEncodingException -> 0x000a }
        r2.<init>(r1, r0);	 Catch:{ UnsupportedEncodingException -> 0x000a }
        return r2;
    L_0x000a:
        r2 = new java.lang.String;
        r2.<init>(r1);
        return r2;
    L_0x0010:
        r1 = getCp850EncodedString(r1);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.util.Zip4jUtil.decodeFileName(byte[], boolean):java.lang.String");
    }

    public static java.lang.String getCp850EncodedString(byte[] r2) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = new java.lang.String;	 Catch:{ UnsupportedEncodingException -> 0x0008 }
        r1 = "Cp850";	 Catch:{ UnsupportedEncodingException -> 0x0008 }
        r0.<init>(r2, r1);	 Catch:{ UnsupportedEncodingException -> 0x0008 }
        return r0;
    L_0x0008:
        r0 = new java.lang.String;
        r0.<init>(r2);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.util.Zip4jUtil.getCp850EncodedString(byte[]):java.lang.String");
    }

    public static String getAbsoluteFilePath(String str) throws ZipException {
        if (isStringNotNullAndNotEmpty(str)) {
            return new File(str).getAbsolutePath();
        }
        throw new ZipException("filePath is null or empty, cannot get absolute file path");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean checkArrayListTypes(java.util.ArrayList r3, int r4) throws net.lingala.zip4j.exception.ZipException {
        /*
        if (r3 != 0) goto L_0x000a;
    L_0x0002:
        r3 = new net.lingala.zip4j.exception.ZipException;
        r4 = "input arraylist is null, cannot check types";
        r3.<init>(r4);
        throw r3;
    L_0x000a:
        r0 = r3.size();
        r1 = 1;
        if (r0 > 0) goto L_0x0012;
    L_0x0011:
        return r1;
    L_0x0012:
        r0 = 0;
        switch(r4) {
            case 1: goto L_0x002b;
            case 2: goto L_0x0017;
            default: goto L_0x0016;
        };
    L_0x0016:
        goto L_0x0040;
    L_0x0017:
        r4 = 0;
    L_0x0018:
        r2 = r3.size();
        if (r4 < r2) goto L_0x001f;
    L_0x001e:
        goto L_0x0040;
    L_0x001f:
        r2 = r3.get(r4);
        r2 = r2 instanceof java.lang.String;
        if (r2 != 0) goto L_0x0028;
    L_0x0027:
        goto L_0x003b;
    L_0x0028:
        r4 = r4 + 1;
        goto L_0x0018;
    L_0x002b:
        r4 = 0;
    L_0x002c:
        r2 = r3.size();
        if (r4 < r2) goto L_0x0033;
    L_0x0032:
        goto L_0x0040;
    L_0x0033:
        r2 = r3.get(r4);
        r2 = r2 instanceof java.io.File;
        if (r2 != 0) goto L_0x003d;
    L_0x003b:
        r0 = 1;
        goto L_0x0040;
    L_0x003d:
        r4 = r4 + 1;
        goto L_0x002c;
    L_0x0040:
        r3 = r0 ^ 1;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.util.Zip4jUtil.checkArrayListTypes(java.util.ArrayList, int):boolean");
    }

    public static java.lang.String detectCharSet(java.lang.String r3) throws net.lingala.zip4j.exception.ZipException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        if (r3 != 0) goto L_0x000a;
    L_0x0002:
        r3 = new net.lingala.zip4j.exception.ZipException;
        r0 = "input string is null, cannot detect charset";
        r3.<init>(r0);
        throw r3;
    L_0x000a:
        r0 = "Cp850";	 Catch:{ UnsupportedEncodingException -> 0x003c, Exception -> 0x0039 }
        r0 = r3.getBytes(r0);	 Catch:{ UnsupportedEncodingException -> 0x003c, Exception -> 0x0039 }
        r1 = new java.lang.String;	 Catch:{ UnsupportedEncodingException -> 0x003c, Exception -> 0x0039 }
        r2 = "Cp850";	 Catch:{ UnsupportedEncodingException -> 0x003c, Exception -> 0x0039 }
        r1.<init>(r0, r2);	 Catch:{ UnsupportedEncodingException -> 0x003c, Exception -> 0x0039 }
        r0 = r3.equals(r1);	 Catch:{ UnsupportedEncodingException -> 0x003c, Exception -> 0x0039 }
        if (r0 == 0) goto L_0x0020;
    L_0x001d:
        r3 = "Cp850";
        return r3;
    L_0x0020:
        r0 = "UTF8";	 Catch:{ UnsupportedEncodingException -> 0x003c, Exception -> 0x0039 }
        r0 = r3.getBytes(r0);	 Catch:{ UnsupportedEncodingException -> 0x003c, Exception -> 0x0039 }
        r1 = new java.lang.String;	 Catch:{ UnsupportedEncodingException -> 0x003c, Exception -> 0x0039 }
        r2 = "UTF8";	 Catch:{ UnsupportedEncodingException -> 0x003c, Exception -> 0x0039 }
        r1.<init>(r0, r2);	 Catch:{ UnsupportedEncodingException -> 0x003c, Exception -> 0x0039 }
        r3 = r3.equals(r1);	 Catch:{ UnsupportedEncodingException -> 0x003c, Exception -> 0x0039 }
        if (r3 == 0) goto L_0x0036;
    L_0x0033:
        r3 = "UTF8";
        return r3;
    L_0x0036:
        r3 = net.lingala.zip4j.util.InternalZipConstants.CHARSET_DEFAULT;	 Catch:{ UnsupportedEncodingException -> 0x003c, Exception -> 0x0039 }
        return r3;
    L_0x0039:
        r3 = net.lingala.zip4j.util.InternalZipConstants.CHARSET_DEFAULT;
        return r3;
    L_0x003c:
        r3 = net.lingala.zip4j.util.InternalZipConstants.CHARSET_DEFAULT;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.util.Zip4jUtil.detectCharSet(java.lang.String):java.lang.String");
    }

    public static int getEncodedStringLength(String str) throws ZipException {
        if (isStringNotNullAndNotEmpty(str)) {
            return getEncodedStringLength(str, detectCharSet(str));
        }
        throw new ZipException("input string is null, cannot calculate encoded String length");
    }

    public static int getEncodedStringLength(java.lang.String r1, java.lang.String r2) throws net.lingala.zip4j.exception.ZipException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = isStringNotNullAndNotEmpty(r1);
        if (r0 != 0) goto L_0x000e;
    L_0x0006:
        r1 = new net.lingala.zip4j.exception.ZipException;
        r2 = "input string is null, cannot calculate encoded String length";
        r1.<init>(r2);
        throw r1;
    L_0x000e:
        r0 = isStringNotNullAndNotEmpty(r2);
        if (r0 != 0) goto L_0x001c;
    L_0x0014:
        r1 = new net.lingala.zip4j.exception.ZipException;
        r2 = "encoding is not defined, cannot calculate string length";
        r1.<init>(r2);
        throw r1;
    L_0x001c:
        r0 = "Cp850";	 Catch:{ UnsupportedEncodingException -> 0x0052, Exception -> 0x004b }
        r0 = r2.equals(r0);	 Catch:{ UnsupportedEncodingException -> 0x0052, Exception -> 0x004b }
        if (r0 == 0) goto L_0x002f;	 Catch:{ UnsupportedEncodingException -> 0x0052, Exception -> 0x004b }
    L_0x0024:
        r2 = "Cp850";	 Catch:{ UnsupportedEncodingException -> 0x0052, Exception -> 0x004b }
        r2 = r1.getBytes(r2);	 Catch:{ UnsupportedEncodingException -> 0x0052, Exception -> 0x004b }
        r2 = java.nio.ByteBuffer.wrap(r2);	 Catch:{ UnsupportedEncodingException -> 0x0052, Exception -> 0x004b }
        goto L_0x005a;	 Catch:{ UnsupportedEncodingException -> 0x0052, Exception -> 0x004b }
    L_0x002f:
        r0 = "UTF8";	 Catch:{ UnsupportedEncodingException -> 0x0052, Exception -> 0x004b }
        r0 = r2.equals(r0);	 Catch:{ UnsupportedEncodingException -> 0x0052, Exception -> 0x004b }
        if (r0 == 0) goto L_0x0042;	 Catch:{ UnsupportedEncodingException -> 0x0052, Exception -> 0x004b }
    L_0x0037:
        r2 = "UTF8";	 Catch:{ UnsupportedEncodingException -> 0x0052, Exception -> 0x004b }
        r2 = r1.getBytes(r2);	 Catch:{ UnsupportedEncodingException -> 0x0052, Exception -> 0x004b }
        r2 = java.nio.ByteBuffer.wrap(r2);	 Catch:{ UnsupportedEncodingException -> 0x0052, Exception -> 0x004b }
        goto L_0x005a;	 Catch:{ UnsupportedEncodingException -> 0x0052, Exception -> 0x004b }
    L_0x0042:
        r2 = r1.getBytes(r2);	 Catch:{ UnsupportedEncodingException -> 0x0052, Exception -> 0x004b }
        r2 = java.nio.ByteBuffer.wrap(r2);	 Catch:{ UnsupportedEncodingException -> 0x0052, Exception -> 0x004b }
        goto L_0x005a;
    L_0x004b:
        r1 = move-exception;
        r2 = new net.lingala.zip4j.exception.ZipException;
        r2.<init>(r1);
        throw r2;
    L_0x0052:
        r1 = r1.getBytes();
        r2 = java.nio.ByteBuffer.wrap(r1);
    L_0x005a:
        r1 = r2.limit();
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.util.Zip4jUtil.getEncodedStringLength(java.lang.String, java.lang.String):int");
    }

    public static boolean isSupportedCharset(java.lang.String r2) throws net.lingala.zip4j.exception.ZipException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = isStringNotNullAndNotEmpty(r2);
        if (r0 != 0) goto L_0x000e;
    L_0x0006:
        r2 = new net.lingala.zip4j.exception.ZipException;
        r0 = "charset is null or empty, cannot check if it is supported";
        r2.<init>(r0);
        throw r2;
    L_0x000e:
        r0 = new java.lang.String;	 Catch:{ UnsupportedEncodingException -> 0x0022, Exception -> 0x001b }
        r1 = "a";	 Catch:{ UnsupportedEncodingException -> 0x0022, Exception -> 0x001b }
        r1 = r1.getBytes();	 Catch:{ UnsupportedEncodingException -> 0x0022, Exception -> 0x001b }
        r0.<init>(r1, r2);	 Catch:{ UnsupportedEncodingException -> 0x0022, Exception -> 0x001b }
        r2 = 1;
        return r2;
    L_0x001b:
        r2 = move-exception;
        r0 = new net.lingala.zip4j.exception.ZipException;
        r0.<init>(r2);
        throw r0;
    L_0x0022:
        r2 = 0;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.util.Zip4jUtil.isSupportedCharset(java.lang.String):boolean");
    }

    public static ArrayList getSplitZipFiles(ZipModel zipModel) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("cannot get split zip files: zipmodel is null");
        } else if (zipModel.getEndCentralDirRecord() == null) {
            return null;
        } else {
            ArrayList arrayList = new ArrayList();
            String zipFile = zipModel.getZipFile();
            String name = new File(zipFile).getName();
            if (!isStringNotNullAndNotEmpty(zipFile)) {
                throw new ZipException("cannot get split zip files: zipfile is null");
            } else if (zipModel.isSplitArchive()) {
                int noOfThisDisk = zipModel.getEndCentralDirRecord().getNoOfThisDisk();
                if (noOfThisDisk == 0) {
                    arrayList.add(zipFile);
                    return arrayList;
                }
                for (int i = 0; i <= noOfThisDisk; i++) {
                    if (i == noOfThisDisk) {
                        arrayList.add(zipModel.getZipFile());
                    } else {
                        String str = ".z0";
                        if (i > 9) {
                            str = ".z";
                        }
                        StringBuffer stringBuffer = new StringBuffer(String.valueOf(name.indexOf(".") >= 0 ? zipFile.substring(0, zipFile.lastIndexOf(".")) : zipFile));
                        stringBuffer.append(str);
                        stringBuffer.append(i + 1);
                        arrayList.add(stringBuffer.toString());
                    }
                }
                return arrayList;
            } else {
                arrayList.add(zipFile);
                return arrayList;
            }
        }
    }

    public static String getRelativeFileName(String str, String str2, String str3) throws ZipException {
        if (isStringNotNullAndNotEmpty(str)) {
            if (isStringNotNullAndNotEmpty(str3)) {
                str3 = new File(str3).getPath();
                if (!str3.endsWith(InternalZipConstants.FILE_SEPARATOR)) {
                    StringBuffer stringBuffer = new StringBuffer(String.valueOf(str3));
                    stringBuffer.append(InternalZipConstants.FILE_SEPARATOR);
                    str3 = stringBuffer.toString();
                }
                str3 = str.substring(str3.length());
                if (str3.startsWith(System.getProperty("file.separator"))) {
                    str3 = str3.substring(1);
                }
                File file = new File(str);
                if (file.isDirectory() != null) {
                    str3 = new StringBuffer(String.valueOf(str3.replaceAll("\\\\", "/")));
                    str3.append("/");
                    str = str3.toString();
                } else {
                    str3 = new StringBuffer(String.valueOf(str3.substring(null, str3.lastIndexOf(file.getName())).replaceAll("\\\\", "/")));
                    str3.append(file.getName());
                    str = str3.toString();
                }
            } else {
                str3 = new File(str);
                if (str3.isDirectory()) {
                    str = new StringBuffer(String.valueOf(str3.getName()));
                    str.append("/");
                    str = str.toString();
                } else {
                    str = getFileNameFromFilePath(new File(str));
                }
            }
            if (isStringNotNullAndNotEmpty(str2) != null) {
                str3 = new StringBuffer(String.valueOf(str2));
                str3.append(str);
                str = str3.toString();
            }
            if (isStringNotNullAndNotEmpty(str) != null) {
                return str;
            }
            throw new ZipException("Error determining file name");
        }
        throw new ZipException("input file path/name is empty, cannot calculate relative file name");
    }

    public static long[] getAllHeaderSignatures() {
        return new long[]{InternalZipConstants.LOCSIG, 134695760, InternalZipConstants.CENSIG, InternalZipConstants.ENDSIG, InternalZipConstants.DIGSIG, InternalZipConstants.ARCEXTDATREC, 134695760, InternalZipConstants.ZIP64ENDCENDIRLOC, InternalZipConstants.ZIP64ENDCENDIRREC, 1, 39169};
    }
}
