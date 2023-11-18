package ir.mahdi.mzip.rar;

import ir.mahdi.mzip.rar.rarfile.FileHeader;
import java.io.File;
import java.io.IOException;

public class RarArchive {
    public static void extractArchive(String str, String str2) {
        if (str != null) {
            if (str2 != null) {
                File file = new File(str);
                StringBuilder stringBuilder;
                if (file.exists()) {
                    File file2 = new File(str2);
                    if (file2.exists()) {
                        if (file2.isDirectory()) {
                            extractArchive(file, file2);
                            return;
                        }
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("the destination must exist and point to a directory: ");
                    stringBuilder.append(str2);
                    throw new RuntimeException(stringBuilder.toString());
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("the archive does not exit: ");
                stringBuilder.append(str);
                throw new RuntimeException(stringBuilder.toString());
            }
        }
        throw new RuntimeException("archive and destination must me set");
    }

    public static void extractArchive(java.io.File r3, java.io.File r4) {
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
        r0 = new ir.mahdi.mzip.rar.Archive;	 Catch:{ RarException -> 0x0006, RarException -> 0x0006 }
        r0.<init>(r3);	 Catch:{ RarException -> 0x0006, RarException -> 0x0006 }
        goto L_0x0007;
    L_0x0006:
        r0 = 0;
    L_0x0007:
        if (r0 == 0) goto L_0x0038;
    L_0x0009:
        r3 = r0.isEncrypted();
        if (r3 == 0) goto L_0x0010;
    L_0x000f:
        return;
    L_0x0010:
        r3 = r0.nextFileHeader();
        if (r3 != 0) goto L_0x0017;
    L_0x0016:
        goto L_0x0038;
    L_0x0017:
        r1 = r3.isEncrypted();
        if (r1 == 0) goto L_0x001e;
    L_0x001d:
        goto L_0x0010;
    L_0x001e:
        r1 = r3.isDirectory();	 Catch:{ IOException -> 0x0010, IOException -> 0x0010 }
        if (r1 == 0) goto L_0x0028;	 Catch:{ IOException -> 0x0010, IOException -> 0x0010 }
    L_0x0024:
        createDirectory(r3, r4);	 Catch:{ IOException -> 0x0010, IOException -> 0x0010 }
        goto L_0x0010;	 Catch:{ IOException -> 0x0010, IOException -> 0x0010 }
    L_0x0028:
        r1 = createFile(r3, r4);	 Catch:{ IOException -> 0x0010, IOException -> 0x0010 }
        r2 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x0010, IOException -> 0x0010 }
        r2.<init>(r1);	 Catch:{ IOException -> 0x0010, IOException -> 0x0010 }
        r0.extractFile(r3, r2);	 Catch:{ IOException -> 0x0010, IOException -> 0x0010 }
        r2.close();	 Catch:{ IOException -> 0x0010, IOException -> 0x0010 }
        goto L_0x0010;
    L_0x0038:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: ir.mahdi.mzip.rar.RarArchive.extractArchive(java.io.File, java.io.File):void");
    }

    private static java.io.File createFile(ir.mahdi.mzip.rar.rarfile.FileHeader r2, java.io.File r3) {
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
        r0 = r2.isFileHeader();
        if (r0 == 0) goto L_0x0011;
    L_0x0006:
        r0 = r2.isUnicode();
        if (r0 == 0) goto L_0x0011;
    L_0x000c:
        r2 = r2.getFileNameW();
        goto L_0x0015;
    L_0x0011:
        r2 = r2.getFileNameString();
    L_0x0015:
        r0 = new java.io.File;
        r0.<init>(r3, r2);
        r1 = r0.exists();
        if (r1 != 0) goto L_0x0025;
    L_0x0020:
        r2 = makeFile(r3, r2);	 Catch:{ IOException -> 0x0025 }
        goto L_0x0026;
    L_0x0025:
        r2 = r0;
    L_0x0026:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: ir.mahdi.mzip.rar.RarArchive.createFile(ir.mahdi.mzip.rar.rarfile.FileHeader, java.io.File):java.io.File");
    }

    private static File makeFile(File file, String str) throws IOException {
        String[] split = str.split("\\\\");
        if (split == null) {
            return null;
        }
        String str2 = "";
        int length = split.length;
        if (length == 1) {
            return new File(file, str);
        }
        if (length <= 1) {
            return null;
        }
        for (str = null; str < split.length - 1; str++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(File.separator);
            stringBuilder.append(split[str]);
            str2 = stringBuilder.toString();
            new File(file, str2).mkdir();
        }
        str = new StringBuilder();
        str.append(str2);
        str.append(File.separator);
        str.append(split[split.length - 1]);
        File file2 = new File(file, str.toString());
        file2.createNewFile();
        return file2;
    }

    private static void createDirectory(FileHeader fileHeader, File file) {
        if (fileHeader.isDirectory() && fileHeader.isUnicode()) {
            if (!new File(file, fileHeader.getFileNameW()).exists()) {
                makeDirectory(file, fileHeader.getFileNameW());
            }
        } else if (fileHeader.isDirectory() && !fileHeader.isUnicode() && !new File(file, fileHeader.getFileNameString()).exists()) {
            makeDirectory(file, fileHeader.getFileNameString());
        }
    }

    private static void makeDirectory(File file, String str) {
        str = str.split("\\\\");
        if (str != null) {
            String str2 = "";
            for (String str3 : str) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str2);
                stringBuilder.append(File.separator);
                stringBuilder.append(str3);
                str2 = stringBuilder.toString();
                new File(file, str2).mkdir();
            }
        }
    }
}
