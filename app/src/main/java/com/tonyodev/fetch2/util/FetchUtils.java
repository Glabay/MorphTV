package com.tonyodev.fetch2.util;

import android.content.Context;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.common.net.HttpHeaders;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Downloader.Request;
import com.tonyodev.fetch2.Status;
import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000L\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\f\u001a\u001e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001\u001a\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\u0001\u001a\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f\u001a\u000e\u0010\r\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f\u001a\u000e\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f\u001a\u000e\u0010\u000f\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f\u001a\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013\u001a\u001e\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u000b\u001a\u00020\f\u001a\u000e\u0010\u0019\u001a\u00020\u00132\u0006\u0010\u001a\u001a\u00020\u0016\u001a\u000e\u0010\u001b\u001a\u00020\u00162\u0006\u0010\u001c\u001a\u00020\u001d\u001a\u000e\u0010\u001e\u001a\u00020\u00132\u0006\u0010\u001f\u001a\u00020\u0016\u001a\"\u0010 \u001a\u00020!2\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\"\u001a\u00020\u00012\b\b\u0002\u0010#\u001a\u00020\u0001\u001a\u0010\u0010$\u001a\u0004\u0018\u00010\u00162\u0006\u0010\u001a\u001a\u00020\u0016\u001a\u0016\u0010%\u001a\u00020\u00062\u0006\u0010&\u001a\u00020\u00162\u0006\u0010\u0012\u001a\u00020\u0016\u001a\u001e\u0010'\u001a\u00020\n2\u0006\u0010(\u001a\u00020\u00012\u0006\u0010)\u001a\u00020\u00012\u0006\u0010*\u001a\u00020\u0001\u001a\u0016\u0010+\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u00162\u0006\u0010,\u001a\u00020\u0016¨\u0006-"}, d2 = {"calculateEstimatedTimeRemainingInMilliseconds", "", "downloadedBytes", "totalBytes", "downloadedBytesPerSecond", "calculateProgress", "", "downloaded", "total", "canCancelDownload", "", "download", "Lcom/tonyodev/fetch2/Download;", "canPauseDownload", "canResumeDownload", "canRetryDownload", "createFileIfPossible", "", "file", "Ljava/io/File;", "deleteRequestTempFiles", "fileTempDir", "", "downloader", "Lcom/tonyodev/fetch2/Downloader;", "getFile", "filePath", "getFileTempDir", "context", "Landroid/content/Context;", "getIncrementedFileIfOriginalExists", "originalPath", "getRequestForDownload", "Lcom/tonyodev/fetch2/Downloader$Request;", "rangeStart", "rangeEnd", "getSingleLineTextFromFile", "getUniqueId", "url", "hasIntervalTimeElapsed", "nanoStartTime", "nanoStopTime", "progressIntervalMilliseconds", "writeTextToFile", "text", "fetch2_release"}, k = 2, mv = {1, 1, 10})
@JvmName(name = "FetchUtils")
/* compiled from: FetchUtils.kt */
public final class FetchUtils {

    @Metadata(bv = {1, 0, 2}, k = 3, mv = {1, 1, 10})
    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$1 = new int[Status.values().length];

        static {
            $EnumSwitchMapping$0 = new int[Status.values().length];
            $EnumSwitchMapping$0[Status.DOWNLOADING.ordinal()] = 1;
            $EnumSwitchMapping$0[Status.QUEUED.ordinal()] = 2;
            $EnumSwitchMapping$1[Status.PAUSED.ordinal()] = 1;
            $EnumSwitchMapping$2 = new int[Status.values().length];
            $EnumSwitchMapping$2[Status.FAILED.ordinal()] = 1;
            $EnumSwitchMapping$2[Status.CANCELLED.ordinal()] = 2;
            $EnumSwitchMapping$3 = new int[Status.values().length];
            $EnumSwitchMapping$3[Status.COMPLETED.ordinal()] = 1;
            $EnumSwitchMapping$3[Status.NONE.ordinal()] = 2;
            $EnumSwitchMapping$3[Status.FAILED.ordinal()] = 3;
        }
    }

    public static final int calculateProgress(long j, long j2) {
        return j2 < 1 ? -1 : j < 1 ? 0 : j >= j2 ? 100 : (int) ((((double) j) / ((double) j2)) * ((double) 100));
    }

    public static final boolean canPauseDownload(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        switch (download.getStatus()) {
            case DOWNLOADING:
            case QUEUED:
                return true;
            default:
                return null;
        }
    }

    public static final boolean canResumeDownload(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        return WhenMappings.$EnumSwitchMapping$1[download.getStatus().ordinal()] == 1;
    }

    public static final boolean canRetryDownload(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        switch (download.getStatus()) {
            case FAILED:
            case CANCELLED:
                return true;
            default:
                return null;
        }
    }

    public static final boolean canCancelDownload(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        switch (download.getStatus()) {
            case COMPLETED:
            case NONE:
            case FAILED:
                return null;
            default:
                return true;
        }
    }

    public static final long calculateEstimatedTimeRemainingInMilliseconds(long j, long j2, long j3) {
        if (j2 >= 1) {
            if (j >= 1) {
                if (j3 >= 1) {
                    return ((long) Math.abs(Math.ceil(((double) (j2 - j)) / ((double) j3)))) * ((long) 1000);
                }
            }
        }
        return -1;
    }

    public static final boolean hasIntervalTimeElapsed(long j, long j2, long j3) {
        return TimeUnit.NANOSECONDS.toMillis(j2 - j) >= j3 ? 1 : 0;
    }

    public static final int getUniqueId(@NotNull String str, @NotNull String str2) {
        Intrinsics.checkParameterIsNotNull(str, ImagesContract.URL);
        Intrinsics.checkParameterIsNotNull(str2, "file");
        return (str.hashCode() * 31) + str2.hashCode();
    }

    @NotNull
    public static final File getIncrementedFileIfOriginalExists(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "originalPath");
        File file = new File(str);
        if (file.exists() != null) {
            str = new StringBuilder();
            str.append("");
            str.append(file.getParent());
            str.append(IOUtils.DIR_SEPARATOR_UNIX);
            str = str.toString();
            String extension = FilesKt__UtilsKt.getExtension(file);
            String nameWithoutExtension = FilesKt__UtilsKt.getNameWithoutExtension(file);
            int i = 0;
            while (file.exists()) {
                i++;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("");
                stringBuilder.append(nameWithoutExtension);
                stringBuilder.append(" (");
                stringBuilder.append(i);
                stringBuilder.append(") ");
                String stringBuilder2 = stringBuilder.toString();
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("");
                stringBuilder3.append(str);
                stringBuilder3.append("");
                stringBuilder3.append(stringBuilder2);
                stringBuilder3.append('.');
                stringBuilder3.append(extension);
                file = new File(stringBuilder3.toString());
            }
        }
        return file;
    }

    public static final void createFileIfPossible(@org.jetbrains.annotations.NotNull java.io.File r1) {
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
        r0 = "file";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r1, r0);
        r0 = r1.exists();	 Catch:{ IOException -> 0x002c }
        if (r0 != 0) goto L_0x002c;	 Catch:{ IOException -> 0x002c }
    L_0x000b:
        r0 = r1.getParentFile();	 Catch:{ IOException -> 0x002c }
        if (r0 == 0) goto L_0x0029;	 Catch:{ IOException -> 0x002c }
    L_0x0011:
        r0 = r1.getParentFile();	 Catch:{ IOException -> 0x002c }
        r0 = r0.exists();	 Catch:{ IOException -> 0x002c }
        if (r0 != 0) goto L_0x0029;	 Catch:{ IOException -> 0x002c }
    L_0x001b:
        r0 = r1.getParentFile();	 Catch:{ IOException -> 0x002c }
        r0 = r0.mkdirs();	 Catch:{ IOException -> 0x002c }
        if (r0 == 0) goto L_0x002c;	 Catch:{ IOException -> 0x002c }
    L_0x0025:
        r1.createNewFile();	 Catch:{ IOException -> 0x002c }
        goto L_0x002c;	 Catch:{ IOException -> 0x002c }
    L_0x0029:
        r1.createNewFile();	 Catch:{ IOException -> 0x002c }
    L_0x002c:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tonyodev.fetch2.util.FetchUtils.createFileIfPossible(java.io.File):void");
    }

    @NotNull
    public static final String getFileTempDir(@NotNull Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        context = context.getFilesDir();
        Intrinsics.checkExpressionValueIsNotNull(context, "context.filesDir");
        stringBuilder.append(context.getAbsoluteFile());
        stringBuilder.append("/_fetchData/temp");
        return stringBuilder.toString();
    }

    @NotNull
    public static /* bridge */ /* synthetic */ Request getRequestForDownload$default(Download download, long j, long j2, int i, Object obj) {
        if ((i & 2) != null) {
            j = -1;
        }
        if ((i & 4) != 0) {
            j2 = -1;
        }
        return getRequestForDownload(download, j, j2);
    }

    @NotNull
    public static final Request getRequestForDownload(@NotNull Download download, long j, long j2) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        if (j == -1) {
            j = 0;
        }
        j2 = j2 == -1 ? "" : String.valueOf(j2);
        Map toMutableMap = MapsKt__MapsKt.toMutableMap(download.getHeaders());
        String str = HttpHeaders.RANGE;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("bytes=");
        stringBuilder.append(j);
        stringBuilder.append(45);
        stringBuilder.append(j2);
        toMutableMap.put(str, stringBuilder.toString());
        return new Request(download.getId(), download.getUrl(), toMutableMap, download.getFile(), download.getTag());
    }

    @NotNull
    public static final File getFile(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "filePath");
        File file = new File(str);
        if (file.exists() == null) {
            if (file.getParentFile() == null || file.getParentFile().exists() != null) {
                file.createNewFile();
            } else if (file.getParentFile().mkdirs() != null) {
                file.createNewFile();
            }
        }
        return file;
    }

    public static final void writeTextToFile(@org.jetbrains.annotations.NotNull java.lang.String r2, @org.jetbrains.annotations.NotNull java.lang.String r3) {
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
        r0 = "filePath";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r2, r0);
        r0 = "text";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r3, r0);
        r2 = getFile(r2);
        r0 = r2.exists();
        if (r0 == 0) goto L_0x002c;
    L_0x0014:
        r0 = new java.io.BufferedWriter;
        r1 = new java.io.FileWriter;
        r1.<init>(r2);
        r1 = (java.io.Writer) r1;
        r0.<init>(r1);
        r0.write(r3);	 Catch:{ Exception -> 0x0023, all -> 0x0027 }
    L_0x0023:
        r0.close();	 Catch:{ Exception -> 0x002c }
        goto L_0x002c;
    L_0x0027:
        r2 = move-exception;
        r0.close();	 Catch:{ Exception -> 0x002b }
    L_0x002b:
        throw r2;
    L_0x002c:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tonyodev.fetch2.util.FetchUtils.writeTextToFile(java.lang.String, java.lang.String):void");
    }

    @org.jetbrains.annotations.Nullable
    public static final java.lang.String getSingleLineTextFromFile(@org.jetbrains.annotations.NotNull java.lang.String r2) {
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
        r0 = "filePath";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r2, r0);
        r2 = getFile(r2);
        r0 = r2.exists();
        if (r0 == 0) goto L_0x002b;
    L_0x000f:
        r0 = new java.io.BufferedReader;
        r1 = new java.io.FileReader;
        r1.<init>(r2);
        r1 = (java.io.Reader) r1;
        r0.<init>(r1);
        r2 = r0.readLine();	 Catch:{ Exception -> 0x0028, all -> 0x0023 }
        r0.close();	 Catch:{ Exception -> 0x0022 }
    L_0x0022:
        return r2;
    L_0x0023:
        r2 = move-exception;
        r0.close();	 Catch:{ Exception -> 0x0027 }
    L_0x0027:
        throw r2;
    L_0x0028:
        r0.close();	 Catch:{ Exception -> 0x002b }
    L_0x002b:
        r2 = 0;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tonyodev.fetch2.util.FetchUtils.getSingleLineTextFromFile(java.lang.String):java.lang.String");
    }

    public static final void deleteRequestTempFiles(@org.jetbrains.annotations.NotNull java.lang.String r8, @org.jetbrains.annotations.NotNull com.tonyodev.fetch2.Downloader r9, @org.jetbrains.annotations.NotNull com.tonyodev.fetch2.Download r10) {
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
        r0 = "fileTempDir";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r8, r0);
        r0 = "downloader";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r9, r0);
        r0 = "download";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r10, r0);
        r2 = 0;
        r4 = 0;
        r6 = 6;
        r7 = 0;
        r1 = r10;
        r0 = getRequestForDownload$default(r1, r2, r4, r6, r7);	 Catch:{ Exception -> 0x0072 }
        r9 = r9.getDirectoryForFileDownloaderTypeParallel(r0);	 Catch:{ Exception -> 0x0072 }
        if (r9 == 0) goto L_0x0021;	 Catch:{ Exception -> 0x0072 }
    L_0x0020:
        r8 = r9;	 Catch:{ Exception -> 0x0072 }
    L_0x0021:
        r8 = getFile(r8);	 Catch:{ Exception -> 0x0072 }
        r9 = r8.exists();	 Catch:{ Exception -> 0x0072 }
        if (r9 == 0) goto L_0x0072;	 Catch:{ Exception -> 0x0072 }
    L_0x002b:
        r8 = r8.listFiles();	 Catch:{ Exception -> 0x0072 }
        r9 = r8.length;	 Catch:{ Exception -> 0x0072 }
        r0 = 0;	 Catch:{ Exception -> 0x0072 }
        r1 = 0;	 Catch:{ Exception -> 0x0072 }
    L_0x0032:
        if (r1 >= r9) goto L_0x0072;	 Catch:{ Exception -> 0x0072 }
    L_0x0034:
        r2 = r8[r1];	 Catch:{ Exception -> 0x0072 }
        r3 = "tempFile";	 Catch:{ Exception -> 0x0072 }
        kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r2, r3);	 Catch:{ Exception -> 0x0072 }
        r3 = r2.getName();	 Catch:{ Exception -> 0x0072 }
        r4 = "tempFile.name";	 Catch:{ Exception -> 0x0072 }
        kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r3, r4);	 Catch:{ Exception -> 0x0072 }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0072 }
        r4.<init>();	 Catch:{ Exception -> 0x0072 }
        r5 = "";	 Catch:{ Exception -> 0x0072 }
        r4.append(r5);	 Catch:{ Exception -> 0x0072 }
        r5 = r10.getId();	 Catch:{ Exception -> 0x0072 }
        r4.append(r5);	 Catch:{ Exception -> 0x0072 }
        r5 = 46;	 Catch:{ Exception -> 0x0072 }
        r4.append(r5);	 Catch:{ Exception -> 0x0072 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0072 }
        r5 = 2;	 Catch:{ Exception -> 0x0072 }
        r6 = 0;	 Catch:{ Exception -> 0x0072 }
        r3 = kotlin.text.StringsKt.startsWith$default(r3, r4, r0, r5, r6);	 Catch:{ Exception -> 0x0072 }
        if (r3 == 0) goto L_0x006f;	 Catch:{ Exception -> 0x0072 }
    L_0x0066:
        r3 = r2.exists();	 Catch:{ Exception -> 0x0072 }
        if (r3 == 0) goto L_0x006f;
    L_0x006c:
        r2.delete();	 Catch:{ Exception -> 0x006f }
    L_0x006f:
        r1 = r1 + 1;
        goto L_0x0032;
    L_0x0072:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tonyodev.fetch2.util.FetchUtils.deleteRequestTempFiles(java.lang.String, com.tonyodev.fetch2.Downloader, com.tonyodev.fetch2.Download):void");
    }
}
