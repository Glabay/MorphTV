package com.tonyodev.fetch2.downloader;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Downloader;
import com.tonyodev.fetch2.Downloader.Request;
import com.tonyodev.fetch2.Downloader.Response;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.FetchErrorUtils;
import com.tonyodev.fetch2.Logger;
import com.tonyodev.fetch2.database.DownloadInfo;
import com.tonyodev.fetch2.downloader.FileDownloader.Delegate;
import com.tonyodev.fetch2.exception.FetchException;
import com.tonyodev.fetch2.exception.FetchException.Code;
import com.tonyodev.fetch2.provider.NetworkInfoProvider;
import com.tonyodev.fetch2.util.AverageCalculator;
import com.tonyodev.fetch2.util.FetchErrorStrings;
import com.tonyodev.fetch2.util.FetchTypeConverterExtensions;
import com.tonyodev.fetch2.util.FetchUtils;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import net.lingala.zip4j.util.InternalZipConstants;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000 \u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0010\u0018\u00002\u00020\u0001:\u0002YZBE\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011¢\u0006\u0002\u0010\u0012J\b\u0010?\u001a\u00020@H\u0002J\u0010\u0010A\u001a\u00020@2\u0006\u0010B\u001a\u00020\tH\u0002J\u0018\u0010C\u001a\u00020@2\u0006\u0010B\u001a\u00020\t2\u0006\u0010D\u001a\u00020\tH\u0002J\u001e\u0010E\u001a\u00020@2\u0006\u0010F\u001a\u00020G2\f\u0010H\u001a\b\u0012\u0004\u0012\u00020-0,H\u0002J\b\u0010I\u001a\u00020\u0007H\u0002J\u0010\u0010J\u001a\u00020K2\u0006\u0010F\u001a\u00020GH\u0002J\u0018\u0010L\u001a\u00020\u00112\u0006\u0010B\u001a\u00020\t2\u0006\u0010D\u001a\u00020\tH\u0002J\u001e\u0010M\u001a\b\u0012\u0004\u0012\u00020-0,2\u0006\u0010N\u001a\u00020\t2\u0006\u0010F\u001a\u00020GH\u0002J\u0010\u0010O\u001a\u00020\u00112\u0006\u0010B\u001a\u00020\tH\u0002J\u0010\u0010P\u001a\u00020\t2\u0006\u0010B\u001a\u00020\tH\u0002J\u0018\u0010Q\u001a\u00020\u00072\u0006\u0010B\u001a\u00020\t2\u0006\u0010D\u001a\u00020\tH\u0002J\b\u0010R\u001a\u00020@H\u0002J\b\u0010S\u001a\u00020@H\u0016J\u0018\u0010T\u001a\u00020@2\u0006\u0010B\u001a\u00020\t2\u0006\u0010U\u001a\u00020\tH\u0002J \u0010V\u001a\u00020@2\u0006\u0010B\u001a\u00020\t2\u0006\u0010D\u001a\u00020\t2\u0006\u0010'\u001a\u00020\u0007H\u0002J\b\u0010W\u001a\u00020@H\u0002J\b\u0010X\u001a\u00020@H\u0002R\u0012\u0010\u0013\u001a\u00020\t8\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0017\u001a\u00020\u000f8\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u001c\u0010\u001c\u001a\u0004\u0018\u00010\u001dX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R\u0014\u0010\"\u001a\u00020\u00038VX\u0004¢\u0006\u0006\u001a\u0004\b#\u0010$R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010)\u001a\u0004\u0018\u00010*X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010+\u001a\b\u0012\u0004\u0012\u00020-0,X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010.\u001a\u00020\u000f8\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u0019\"\u0004\b0\u0010\u001bR\u000e\u00101\u001a\u000202X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u00103\u001a\u000204X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u0010\u00105\u001a\u0004\u0018\u000106X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0010\u00107\u001a\u0004\u0018\u000108X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u001e\u00109\u001a\u00020\u000f8\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b:\u0010\u0019\"\u0004\b;\u0010\u001bR\u0010\u0010<\u001a\u0004\u0018\u00010=X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000¨\u0006["}, d2 = {"Lcom/tonyodev/fetch2/downloader/ParallelFileDownloaderImpl;", "Lcom/tonyodev/fetch2/downloader/FileDownloader;", "initialDownload", "Lcom/tonyodev/fetch2/Download;", "downloader", "Lcom/tonyodev/fetch2/Downloader;", "progressReportingIntervalMillis", "", "downloadBufferSizeBytes", "", "logger", "Lcom/tonyodev/fetch2/Logger;", "networkInfoProvider", "Lcom/tonyodev/fetch2/provider/NetworkInfoProvider;", "retryOnNetworkGain", "", "fileTempDir", "", "(Lcom/tonyodev/fetch2/Download;Lcom/tonyodev/fetch2/Downloader;JILcom/tonyodev/fetch2/Logger;Lcom/tonyodev/fetch2/provider/NetworkInfoProvider;ZLjava/lang/String;)V", "actionsCounter", "actionsTotal", "averageDownloadedBytesPerSecond", "", "completedDownload", "getCompletedDownload", "()Z", "setCompletedDownload", "(Z)V", "delegate", "Lcom/tonyodev/fetch2/downloader/FileDownloader$Delegate;", "getDelegate", "()Lcom/tonyodev/fetch2/downloader/FileDownloader$Delegate;", "setDelegate", "(Lcom/tonyodev/fetch2/downloader/FileDownloader$Delegate;)V", "download", "getDownload", "()Lcom/tonyodev/fetch2/Download;", "downloadInfo", "Lcom/tonyodev/fetch2/database/DownloadInfo;", "downloaded", "estimatedTimeRemainingInMilliseconds", "executorService", "Ljava/util/concurrent/ExecutorService;", "fileSlices", "", "Lcom/tonyodev/fetch2/downloader/ParallelFileDownloaderImpl$FileSlice;", "interrupted", "getInterrupted", "setInterrupted", "lock", "Ljava/lang/Object;", "movingAverageCalculator", "Lcom/tonyodev/fetch2/util/AverageCalculator;", "outputStream", "Ljava/io/OutputStream;", "randomAccessFileOutput", "Ljava/io/RandomAccessFile;", "terminated", "getTerminated", "setTerminated", "throwable", "", "total", "deleteAllTempFiles", "", "deleteMetaFile", "id", "deleteTempFile", "position", "downloadSliceFiles", "request", "Lcom/tonyodev/fetch2/Downloader$Request;", "fileSlicesDownloadsList", "getAverageDownloadedBytesPerSecond", "getChuckInfo", "Lcom/tonyodev/fetch2/downloader/ParallelFileDownloaderImpl$FileSliceInfo;", "getDownloadedInfoFilePath", "getFileSliceList", "openingResponseCode", "getMetaFilePath", "getPreviousSliceCount", "getSavedDownloadedInfo", "incrementActionCompletedCount", "run", "saveCurrentSliceCount", "SliceCount", "saveDownloadedInfo", "throwExceptionIfFound", "waitAndPerformProgressReporting", "FileSlice", "FileSliceInfo", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: ParallelFileDownloaderImpl.kt */
public final class ParallelFileDownloaderImpl implements FileDownloader {
    private volatile int actionsCounter;
    private int actionsTotal;
    private double averageDownloadedBytesPerSecond;
    private volatile boolean completedDownload;
    @Nullable
    private Delegate delegate;
    private final int downloadBufferSizeBytes;
    private DownloadInfo downloadInfo = FetchTypeConverterExtensions.toDownloadInfo(this.initialDownload);
    private long downloaded;
    private final Downloader downloader;
    private long estimatedTimeRemainingInMilliseconds = -1;
    private ExecutorService executorService;
    private List<FileSlice> fileSlices = CollectionsKt.emptyList();
    private final String fileTempDir;
    private final Download initialDownload;
    private volatile boolean interrupted;
    private final Object lock = new Object();
    private final Logger logger;
    private final AverageCalculator movingAverageCalculator = new AverageCalculator(5);
    private final NetworkInfoProvider networkInfoProvider;
    private OutputStream outputStream;
    private final long progressReportingIntervalMillis;
    private RandomAccessFile randomAccessFileOutput;
    private final boolean retryOnNetworkGain;
    private volatile boolean terminated;
    private Throwable throwable;
    private long total = -1;

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B7\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\u0006¢\u0006\u0002\u0010\tJ\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0006HÆ\u0003J\t\u0010\u0019\u001a\u00020\u0006HÆ\u0003J\t\u0010\u001a\u001a\u00020\u0006HÆ\u0003J;\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u001c\u001a\u00020\u00122\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001e\u001a\u00020\u0003HÖ\u0001J\t\u0010\u001f\u001a\u00020 HÖ\u0001R\u001a\u0010\b\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0011\u0010\u0007\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0011\u001a\u00020\u00128F¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0013R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0010R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000b¨\u0006!"}, d2 = {"Lcom/tonyodev/fetch2/downloader/ParallelFileDownloaderImpl$FileSlice;", "", "id", "", "position", "startBytes", "", "endBytes", "downloaded", "(IIJJJ)V", "getDownloaded", "()J", "setDownloaded", "(J)V", "getEndBytes", "getId", "()I", "isDownloaded", "", "()Z", "getPosition", "getStartBytes", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "toString", "", "fetch2_release"}, k = 1, mv = {1, 1, 10})
    /* compiled from: ParallelFileDownloaderImpl.kt */
    public static final class FileSlice {
        private long downloaded;
        private final long endBytes;
        private final int id;
        private final int position;
        private final long startBytes;

        public FileSlice() {
            this(0, 0, 0, 0, 0, 31, null);
        }

        @NotNull
        public static /* bridge */ /* synthetic */ FileSlice copy$default(FileSlice fileSlice, int i, int i2, long j, long j2, long j3, int i3, Object obj) {
            FileSlice fileSlice2 = fileSlice;
            return fileSlice2.copy((i3 & 1) != 0 ? fileSlice2.id : i, (i3 & 2) != 0 ? fileSlice2.position : i2, (i3 & 4) != 0 ? fileSlice2.startBytes : j, (i3 & 8) != 0 ? fileSlice2.endBytes : j2, (i3 & 16) != 0 ? fileSlice2.downloaded : j3);
        }

        public final int component1() {
            return this.id;
        }

        public final int component2() {
            return this.position;
        }

        public final long component3() {
            return this.startBytes;
        }

        public final long component4() {
            return this.endBytes;
        }

        public final long component5() {
            return this.downloaded;
        }

        @NotNull
        public final FileSlice copy(int i, int i2, long j, long j2, long j3) {
            return new FileSlice(i, i2, j, j2, j3);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof FileSlice) {
                FileSlice fileSlice = (FileSlice) obj;
                if ((this.id == fileSlice.id ? 1 : null) != null) {
                    if ((this.position == fileSlice.position ? 1 : null) != null) {
                        if ((this.startBytes == fileSlice.startBytes ? 1 : null) != null) {
                            if ((this.endBytes == fileSlice.endBytes ? 1 : null) != null) {
                                if ((this.downloaded == fileSlice.downloaded ? 1 : null) != null) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }

        public int hashCode() {
            int i = ((this.id * 31) + this.position) * 31;
            long j = this.startBytes;
            i = (i + ((int) (j ^ (j >>> 32)))) * 31;
            j = this.endBytes;
            i = (i + ((int) (j ^ (j >>> 32)))) * 31;
            j = this.downloaded;
            return i + ((int) (j ^ (j >>> 32)));
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("FileSlice(id=");
            stringBuilder.append(this.id);
            stringBuilder.append(", position=");
            stringBuilder.append(this.position);
            stringBuilder.append(", startBytes=");
            stringBuilder.append(this.startBytes);
            stringBuilder.append(", endBytes=");
            stringBuilder.append(this.endBytes);
            stringBuilder.append(", downloaded=");
            stringBuilder.append(this.downloaded);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        public FileSlice(int i, int i2, long j, long j2, long j3) {
            this.id = i;
            this.position = i2;
            this.startBytes = j;
            this.endBytes = j2;
            this.downloaded = j3;
        }

        public final int getId() {
            return this.id;
        }

        public final int getPosition() {
            return this.position;
        }

        public final long getStartBytes() {
            return this.startBytes;
        }

        public final long getEndBytes() {
            return this.endBytes;
        }

        public /* synthetic */ FileSlice(int i, int i2, long j, long j2, long j3, int i3, DefaultConstructorMarker defaultConstructorMarker) {
            this((i3 & 1) != 0 ? 0 : i, (i3 & 2) != 0 ? 0 : i2, (i3 & 4) != 0 ? 0 : j, (i3 & 8) != 0 ? 0 : j2, (i3 & 16) != 0 ? 0 : j3);
        }

        public final long getDownloaded() {
            return this.downloaded;
        }

        public final void setDownloaded(long j) {
            this.downloaded = j;
        }

        public final boolean isDownloaded() {
            return this.startBytes + this.downloaded == this.endBytes;
        }
    }

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0014"}, d2 = {"Lcom/tonyodev/fetch2/downloader/ParallelFileDownloaderImpl$FileSliceInfo;", "", "slicingCount", "", "bytesPerFileSlice", "", "(IJ)V", "getBytesPerFileSlice", "()J", "getSlicingCount", "()I", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "fetch2_release"}, k = 1, mv = {1, 1, 10})
    /* compiled from: ParallelFileDownloaderImpl.kt */
    public static final class FileSliceInfo {
        private final long bytesPerFileSlice;
        private final int slicingCount;

        @NotNull
        public static /* bridge */ /* synthetic */ FileSliceInfo copy$default(FileSliceInfo fileSliceInfo, int i, long j, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                i = fileSliceInfo.slicingCount;
            }
            if ((i2 & 2) != 0) {
                j = fileSliceInfo.bytesPerFileSlice;
            }
            return fileSliceInfo.copy(i, j);
        }

        public final int component1() {
            return this.slicingCount;
        }

        public final long component2() {
            return this.bytesPerFileSlice;
        }

        @NotNull
        public final FileSliceInfo copy(int i, long j) {
            return new FileSliceInfo(i, j);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof FileSliceInfo) {
                FileSliceInfo fileSliceInfo = (FileSliceInfo) obj;
                if ((this.slicingCount == fileSliceInfo.slicingCount ? 1 : null) != null) {
                    if ((this.bytesPerFileSlice == fileSliceInfo.bytesPerFileSlice ? 1 : null) != null) {
                        return true;
                    }
                }
            }
            return false;
        }

        public int hashCode() {
            int i = this.slicingCount * 31;
            long j = this.bytesPerFileSlice;
            return i + ((int) (j ^ (j >>> 32)));
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("FileSliceInfo(slicingCount=");
            stringBuilder.append(this.slicingCount);
            stringBuilder.append(", bytesPerFileSlice=");
            stringBuilder.append(this.bytesPerFileSlice);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        public FileSliceInfo(int i, long j) {
            this.slicingCount = i;
            this.bytesPerFileSlice = j;
        }

        public final long getBytesPerFileSlice() {
            return this.bytesPerFileSlice;
        }

        public final int getSlicingCount() {
            return this.slicingCount;
        }
    }

    public ParallelFileDownloaderImpl(@NotNull Download download, @NotNull Downloader downloader, long j, int i, @NotNull Logger logger, @NotNull NetworkInfoProvider networkInfoProvider, boolean z, @NotNull String str) {
        Intrinsics.checkParameterIsNotNull(download, "initialDownload");
        Intrinsics.checkParameterIsNotNull(downloader, "downloader");
        Intrinsics.checkParameterIsNotNull(logger, "logger");
        Intrinsics.checkParameterIsNotNull(networkInfoProvider, "networkInfoProvider");
        Intrinsics.checkParameterIsNotNull(str, "fileTempDir");
        this.initialDownload = download;
        this.downloader = downloader;
        this.progressReportingIntervalMillis = j;
        this.downloadBufferSizeBytes = i;
        this.logger = logger;
        this.networkInfoProvider = networkInfoProvider;
        this.retryOnNetworkGain = z;
        this.fileTempDir = str;
    }

    public boolean getInterrupted() {
        return this.interrupted;
    }

    public void setInterrupted(boolean z) {
        this.interrupted = z;
    }

    public boolean getTerminated() {
        return this.terminated;
    }

    public void setTerminated(boolean z) {
        this.terminated = z;
    }

    public boolean getCompletedDownload() {
        return this.completedDownload;
    }

    public void setCompletedDownload(boolean z) {
        this.completedDownload = z;
    }

    @Nullable
    public Delegate getDelegate() {
        return this.delegate;
    }

    public void setDelegate(@Nullable Delegate delegate) {
        this.delegate = delegate;
    }

    @NotNull
    public Download getDownload() {
        this.downloadInfo.setDownloaded(this.downloaded);
        this.downloadInfo.setTotal(this.total);
        return this.downloadInfo;
    }

    public void run() {
        Response execute;
        Exception e;
        ExecutorService executorService;
        Error errorFromThrowable;
        int isNetworkAvailable;
        int i;
        Throwable th;
        Response response = (Response) null;
        Delegate delegate;
        RandomAccessFile randomAccessFile;
        OutputStream outputStream;
        try {
            Request requestForDownload$default = FetchUtils.getRequestForDownload$default(this.initialDownload, 0, 0, 6, null);
            execute = this.downloader.execute(requestForDownload$default);
            try {
                if (!getInterrupted() && !getTerminated() && execute != null && execute.isSuccessful()) {
                    this.total = execute.getContentLength();
                    if (this.total > 0) {
                        this.fileSlices = getFileSliceList(execute.getCode(), requestForDownload$default);
                        try {
                            this.downloader.disconnect(execute);
                        } catch (Exception e2) {
                            this.logger.mo4163e("FileDownloader", e2);
                        }
                        Collection arrayList = new ArrayList();
                        for (Object next : this.fileSlices) {
                            if ((((FileSlice) next).isDownloaded() ^ 1) != 0) {
                                arrayList.add(next);
                            }
                        }
                        List list = (List) arrayList;
                        if (!(getInterrupted() || getTerminated())) {
                            Delegate delegate2;
                            this.downloadInfo.setDownloaded(this.downloaded);
                            this.downloadInfo.setTotal(this.total);
                            Delegate delegate3 = getDelegate();
                            if (delegate3 != null) {
                                delegate3.onStarted(this.downloadInfo, this.estimatedTimeRemainingInMilliseconds, getAverageDownloadedBytesPerSecond());
                            }
                            if ((list.isEmpty() ^ 1) != 0) {
                                this.executorService = Executors.newFixedThreadPool(list.size());
                            }
                            downloadSliceFiles(requestForDownload$default, list);
                            waitAndPerformProgressReporting();
                            this.downloadInfo.setDownloaded(this.downloaded);
                            this.downloadInfo.setTotal(this.total);
                            if (!(getInterrupted() || getTerminated())) {
                                throwExceptionIfFound();
                                setCompletedDownload(true);
                                delegate2 = getDelegate();
                                if (delegate2 != null) {
                                    delegate2.onProgress(this.downloadInfo, this.estimatedTimeRemainingInMilliseconds, getAverageDownloadedBytesPerSecond());
                                }
                                delegate = getDelegate();
                                if (delegate != null) {
                                    delegate.onComplete(this.downloadInfo);
                                }
                                deleteAllTempFiles();
                            }
                            delegate = getDelegate();
                            if (delegate != null) {
                                delegate.saveDownloadProgress(this.downloadInfo);
                            }
                            if (!(getCompletedDownload() || getTerminated())) {
                                delegate2 = getDelegate();
                                if (delegate2 != null) {
                                    delegate2.onProgress(this.downloadInfo, this.estimatedTimeRemainingInMilliseconds, getAverageDownloadedBytesPerSecond());
                                }
                            }
                        }
                    } else {
                        throw new FetchException(FetchErrorStrings.EMPTY_RESPONSE_BODY, Code.EMPTY_RESPONSE_BODY);
                    }
                } else if (execute == null && !getInterrupted() && !getTerminated()) {
                    throw new FetchException(FetchErrorStrings.EMPTY_RESPONSE_BODY, Code.EMPTY_RESPONSE_BODY);
                } else if (execute != null && !execute.isSuccessful() && !getInterrupted() && !getTerminated()) {
                    throw new FetchException(FetchErrorStrings.RESPONSE_NOT_SUCCESSFUL, Code.REQUEST_NOT_SUCCESSFUL);
                } else if (!(getInterrupted() || getTerminated())) {
                    throw new FetchException("unknown", Code.UNKNOWN);
                }
                try {
                    executorService = this.executorService;
                    if (executorService != null) {
                        executorService.shutdown();
                    }
                } catch (Exception e22) {
                    this.logger.mo4163e("FileDownloader", e22);
                }
                try {
                    randomAccessFile = this.randomAccessFileOutput;
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                } catch (Exception e222) {
                    this.logger.mo4163e("FileDownloader", e222);
                }
                try {
                    outputStream = this.outputStream;
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (Exception e2222) {
                    this.logger.mo4163e("FileDownloader", e2222);
                }
                if (execute != null) {
                    try {
                        this.downloader.disconnect(execute);
                    } catch (Exception e3) {
                        e2222 = e3;
                        this.logger.mo4163e("FileDownloader", e2222);
                        setTerminated(true);
                    }
                }
            } catch (Exception e4) {
                e2222 = e4;
                try {
                    if (!(getInterrupted() || getTerminated())) {
                        this.logger.mo4163e("FileDownloader", e2222);
                        errorFromThrowable = FetchErrorUtils.getErrorFromThrowable(e2222);
                        errorFromThrowable.setThrowable(e2222);
                        if (this.retryOnNetworkGain) {
                            isNetworkAvailable = this.networkInfoProvider.isNetworkAvailable() ^ 1;
                            for (i = 1; i <= 10; i++) {
                                Thread.sleep(500);
                                if (!this.networkInfoProvider.isNetworkAvailable()) {
                                    isNetworkAvailable = 1;
                                    break;
                                }
                            }
                            if (isNetworkAvailable != 0) {
                                errorFromThrowable = Error.NO_NETWORK_CONNECTION;
                            }
                        }
                        this.downloadInfo.setDownloaded(this.downloaded);
                        this.downloadInfo.setTotal(this.total);
                        this.downloadInfo.setError(errorFromThrowable);
                        if (!getTerminated()) {
                            delegate = getDelegate();
                            if (delegate != null) {
                                delegate.onError(this.downloadInfo);
                            }
                        }
                    }
                } catch (InterruptedException e5) {
                    this.logger.mo4163e("FileDownloader", e5);
                } catch (Throwable th2) {
                    th = th2;
                }
                try {
                    executorService = this.executorService;
                    if (executorService != null) {
                        executorService.shutdown();
                    }
                } catch (Exception e22222) {
                    this.logger.mo4163e("FileDownloader", e22222);
                }
                try {
                    randomAccessFile = this.randomAccessFileOutput;
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                } catch (Exception e222222) {
                    this.logger.mo4163e("FileDownloader", e222222);
                }
                try {
                    outputStream = this.outputStream;
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (Exception e2222222) {
                    this.logger.mo4163e("FileDownloader", e2222222);
                }
                if (execute != null) {
                    try {
                        this.downloader.disconnect(execute);
                    } catch (Exception e6) {
                        e2222222 = e6;
                        this.logger.mo4163e("FileDownloader", e2222222);
                        setTerminated(true);
                    }
                }
                setTerminated(true);
            }
        } catch (Exception e7) {
            execute = response;
            e2222222 = e7;
            this.logger.mo4163e("FileDownloader", e2222222);
            errorFromThrowable = FetchErrorUtils.getErrorFromThrowable(e2222222);
            errorFromThrowable.setThrowable(e2222222);
            if (this.retryOnNetworkGain) {
                isNetworkAvailable = this.networkInfoProvider.isNetworkAvailable() ^ 1;
                for (i = 1; i <= 10; i++) {
                    Thread.sleep(500);
                    if (!this.networkInfoProvider.isNetworkAvailable()) {
                        isNetworkAvailable = 1;
                        break;
                    }
                }
                if (isNetworkAvailable != 0) {
                    errorFromThrowable = Error.NO_NETWORK_CONNECTION;
                }
            }
            this.downloadInfo.setDownloaded(this.downloaded);
            this.downloadInfo.setTotal(this.total);
            this.downloadInfo.setError(errorFromThrowable);
            if (getTerminated()) {
                delegate = getDelegate();
                if (delegate != null) {
                    delegate.onError(this.downloadInfo);
                }
            }
            executorService = this.executorService;
            if (executorService != null) {
                executorService.shutdown();
            }
            randomAccessFile = this.randomAccessFileOutput;
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
            outputStream = this.outputStream;
            if (outputStream != null) {
                outputStream.close();
            }
            if (execute != null) {
                this.downloader.disconnect(execute);
            }
            setTerminated(true);
        } catch (Throwable th3) {
            execute = response;
            th = th3;
            try {
                ExecutorService executorService2 = this.executorService;
                if (executorService2 != null) {
                    executorService2.shutdown();
                }
            } catch (Exception e72) {
                this.logger.mo4163e("FileDownloader", e72);
            }
            try {
                RandomAccessFile randomAccessFile2 = this.randomAccessFileOutput;
                if (randomAccessFile2 != null) {
                    randomAccessFile2.close();
                }
            } catch (Exception e722) {
                this.logger.mo4163e("FileDownloader", e722);
            }
            try {
                OutputStream outputStream2 = this.outputStream;
                if (outputStream2 != null) {
                    outputStream2.close();
                }
            } catch (Exception e7222) {
                this.logger.mo4163e("FileDownloader", e7222);
            }
            if (execute != null) {
                try {
                    this.downloader.disconnect(execute);
                } catch (Exception e72222) {
                    this.logger.mo4163e("FileDownloader", e72222);
                }
            }
            setTerminated(true);
            throw th;
        }
        setTerminated(true);
    }

    private final List<FileSlice> getFileSliceList(int i, Request request) {
        if (!FetchUtils.getFile(this.downloadInfo.getFile()).exists()) {
            deleteAllTempFiles();
        }
        int previousSliceCount = getPreviousSliceCount(r0.downloadInfo.getId());
        int i2 = 1;
        if (i == 206) {
            FileSliceInfo chuckInfo = getChuckInfo(request);
            if (previousSliceCount != chuckInfo.getSlicingCount()) {
                deleteAllTempFiles();
            }
            saveCurrentSliceCount(r0.downloadInfo.getId(), chuckInfo.getSlicingCount());
            List<FileSlice> arrayList = new ArrayList();
            int slicingCount = chuckInfo.getSlicingCount();
            if (1 > slicingCount) {
                return arrayList;
            }
            long j = 0;
            while (!getInterrupted() && !getTerminated()) {
                long j2;
                if (chuckInfo.getSlicingCount() == i2) {
                    j2 = r0.total;
                } else {
                    j2 = j + chuckInfo.getBytesPerFileSlice();
                }
                FileSliceInfo fileSliceInfo = chuckInfo;
                FileSlice fileSlice = r7;
                FileSlice fileSlice2 = new FileSlice(r0.downloadInfo.getId(), i2, j, j2, getSavedDownloadedInfo(r0.downloadInfo.getId(), i2));
                r0.downloaded += fileSlice.getDownloaded();
                arrayList.add(fileSlice);
                if (i2 == slicingCount) {
                    return arrayList;
                }
                i2++;
                j = j2;
                chuckInfo = fileSliceInfo;
            }
            return arrayList;
        }
        if (previousSliceCount != 1) {
            deleteAllTempFiles();
        }
        saveCurrentSliceCount(r0.downloadInfo.getId(), 1);
        FileSlice fileSlice3 = new FileSlice(r0.downloadInfo.getId(), 1, 0, r0.total, getSavedDownloadedInfo(r0.downloadInfo.getId(), 1));
        r0.downloaded += fileSlice3.getDownloaded();
        return CollectionsKt.listOf(fileSlice3);
    }

    private final int getPreviousSliceCount(int r3) {
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
        r0 = -1;
        r1 = r2.getTerminated();	 Catch:{ Exception -> 0x001c }
        if (r1 != 0) goto L_0x001c;	 Catch:{ Exception -> 0x001c }
    L_0x0007:
        r1 = r2.getInterrupted();	 Catch:{ Exception -> 0x001c }
        if (r1 != 0) goto L_0x001c;	 Catch:{ Exception -> 0x001c }
    L_0x000d:
        r3 = r2.getMetaFilePath(r3);	 Catch:{ Exception -> 0x001c }
        r3 = com.tonyodev.fetch2.util.FetchUtils.getSingleLineTextFromFile(r3);	 Catch:{ Exception -> 0x001c }
        if (r3 == 0) goto L_0x001c;	 Catch:{ Exception -> 0x001c }
    L_0x0017:
        r3 = java.lang.Integer.parseInt(r3);	 Catch:{ Exception -> 0x001c }
        r0 = r3;
    L_0x001c:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tonyodev.fetch2.downloader.ParallelFileDownloaderImpl.getPreviousSliceCount(int):int");
    }

    private final void saveCurrentSliceCount(int r2, int r3) {
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
        r1 = this;
        r0 = r1.getTerminated();	 Catch:{ Exception -> 0x0017 }
        if (r0 != 0) goto L_0x0017;	 Catch:{ Exception -> 0x0017 }
    L_0x0006:
        r0 = r1.getInterrupted();	 Catch:{ Exception -> 0x0017 }
        if (r0 != 0) goto L_0x0017;	 Catch:{ Exception -> 0x0017 }
    L_0x000c:
        r2 = r1.getMetaFilePath(r2);	 Catch:{ Exception -> 0x0017 }
        r3 = java.lang.String.valueOf(r3);	 Catch:{ Exception -> 0x0017 }
        com.tonyodev.fetch2.util.FetchUtils.writeTextToFile(r2, r3);	 Catch:{ Exception -> 0x0017 }
    L_0x0017:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tonyodev.fetch2.downloader.ParallelFileDownloaderImpl.saveCurrentSliceCount(int, int):void");
    }

    private final String getMetaFilePath(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(this.fileTempDir);
        stringBuilder.append(IOUtils.DIR_SEPARATOR_UNIX);
        stringBuilder.append(i);
        stringBuilder.append(".meta.txt");
        return stringBuilder.toString();
    }

    private final FileSliceInfo getChuckInfo(Request request) {
        request = this.downloader.getFileSlicingCount(request, this.total);
        request = request != null ? request.intValue() : -1;
        if (request != -1) {
            return new FileSliceInfo(request, (long) ((float) Math.ceil((double) (((float) this.total) / ((float) request)))));
        }
        FileSliceInfo fileSliceInfo;
        request = (((float) this.total) / 1149239296) * 1149239296;
        if (((((float) this.total) / 1024.0f) * 1024.0f) * 1024.0f >= 1.0f) {
            fileSliceInfo = new FileSliceInfo(6, (long) ((float) Math.ceil((double) (((float) this.total) / ((float) 6)))));
        } else if (request >= 1065353216) {
            fileSliceInfo = new FileSliceInfo(4, (long) ((float) Math.ceil((double) (((float) this.total) / ((float) 4)))));
        } else {
            fileSliceInfo = new FileSliceInfo(2, this.total);
        }
        return fileSliceInfo;
    }

    private final String getDownloadedInfoFilePath(int i, int i2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(this.fileTempDir);
        stringBuilder.append(IOUtils.DIR_SEPARATOR_UNIX);
        stringBuilder.append(i);
        stringBuilder.append(46);
        stringBuilder.append(i2);
        stringBuilder.append(".txt");
        return stringBuilder.toString();
    }

    private final void deleteTempFile(int r2, int r3) {
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
        r1 = this;
        r0 = r1.getInterrupted();	 Catch:{ Exception -> 0x001d }
        if (r0 != 0) goto L_0x001d;	 Catch:{ Exception -> 0x001d }
    L_0x0006:
        r0 = r1.getTerminated();	 Catch:{ Exception -> 0x001d }
        if (r0 != 0) goto L_0x001d;	 Catch:{ Exception -> 0x001d }
    L_0x000c:
        r2 = r1.getDownloadedInfoFilePath(r2, r3);	 Catch:{ Exception -> 0x001d }
        r2 = com.tonyodev.fetch2.util.FetchUtils.getFile(r2);	 Catch:{ Exception -> 0x001d }
        r3 = r2.exists();	 Catch:{ Exception -> 0x001d }
        if (r3 == 0) goto L_0x001d;	 Catch:{ Exception -> 0x001d }
    L_0x001a:
        r2.delete();	 Catch:{ Exception -> 0x001d }
    L_0x001d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tonyodev.fetch2.downloader.ParallelFileDownloaderImpl.deleteTempFile(int, int):void");
    }

    private final void deleteMetaFile(int r2) {
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
        r1 = this;
        r0 = r1.getTerminated();	 Catch:{ Exception -> 0x001d }
        if (r0 != 0) goto L_0x001d;	 Catch:{ Exception -> 0x001d }
    L_0x0006:
        r0 = r1.getInterrupted();	 Catch:{ Exception -> 0x001d }
        if (r0 != 0) goto L_0x001d;	 Catch:{ Exception -> 0x001d }
    L_0x000c:
        r2 = r1.getMetaFilePath(r2);	 Catch:{ Exception -> 0x001d }
        r2 = com.tonyodev.fetch2.util.FetchUtils.getFile(r2);	 Catch:{ Exception -> 0x001d }
        r0 = r2.exists();	 Catch:{ Exception -> 0x001d }
        if (r0 == 0) goto L_0x001d;	 Catch:{ Exception -> 0x001d }
    L_0x001a:
        r2.delete();	 Catch:{ Exception -> 0x001d }
    L_0x001d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tonyodev.fetch2.downloader.ParallelFileDownloaderImpl.deleteMetaFile(int):void");
    }

    private final long getSavedDownloadedInfo(int r4, int r5) {
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
        r3 = this;
        r0 = 0;
        r2 = r3.getTerminated();	 Catch:{ Exception -> 0x001d }
        if (r2 != 0) goto L_0x001d;	 Catch:{ Exception -> 0x001d }
    L_0x0008:
        r2 = r3.getInterrupted();	 Catch:{ Exception -> 0x001d }
        if (r2 != 0) goto L_0x001d;	 Catch:{ Exception -> 0x001d }
    L_0x000e:
        r4 = r3.getDownloadedInfoFilePath(r4, r5);	 Catch:{ Exception -> 0x001d }
        r4 = com.tonyodev.fetch2.util.FetchUtils.getSingleLineTextFromFile(r4);	 Catch:{ Exception -> 0x001d }
        if (r4 == 0) goto L_0x001d;	 Catch:{ Exception -> 0x001d }
    L_0x0018:
        r4 = java.lang.Long.parseLong(r4);	 Catch:{ Exception -> 0x001d }
        r0 = r4;
    L_0x001d:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tonyodev.fetch2.downloader.ParallelFileDownloaderImpl.getSavedDownloadedInfo(int, int):long");
    }

    private final void saveDownloadedInfo(int r2, int r3, long r4) {
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
        r1 = this;
        r0 = r1.getTerminated();	 Catch:{ Exception -> 0x0017 }
        if (r0 != 0) goto L_0x0017;	 Catch:{ Exception -> 0x0017 }
    L_0x0006:
        r0 = r1.getInterrupted();	 Catch:{ Exception -> 0x0017 }
        if (r0 != 0) goto L_0x0017;	 Catch:{ Exception -> 0x0017 }
    L_0x000c:
        r2 = r1.getDownloadedInfoFilePath(r2, r3);	 Catch:{ Exception -> 0x0017 }
        r3 = java.lang.String.valueOf(r4);	 Catch:{ Exception -> 0x0017 }
        com.tonyodev.fetch2.util.FetchUtils.writeTextToFile(r2, r3);	 Catch:{ Exception -> 0x0017 }
    L_0x0017:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tonyodev.fetch2.downloader.ParallelFileDownloaderImpl.saveDownloadedInfo(int, int, long):void");
    }

    private final long getAverageDownloadedBytesPerSecond() {
        if (this.averageDownloadedBytesPerSecond < ((double) 1)) {
            return 0;
        }
        return (long) Math.ceil(this.averageDownloadedBytesPerSecond);
    }

    private final void waitAndPerformProgressReporting() {
        long j = this.downloaded;
        long nanoTime = System.nanoTime();
        long nanoTime2 = System.nanoTime();
        while (this.actionsCounter != this.actionsTotal && !getInterrupted() && !getTerminated()) {
            Delegate delegate;
            this.downloadInfo.setDownloaded(this.downloaded);
            this.downloadInfo.setTotal(this.total);
            boolean hasIntervalTimeElapsed = FetchUtils.hasIntervalTimeElapsed(nanoTime2, System.nanoTime(), 1000);
            if (hasIntervalTimeElapsed) {
                this.movingAverageCalculator.add((double) (this.downloaded - j));
                this.averageDownloadedBytesPerSecond = AverageCalculator.getMovingAverageWithWeightOnRecentValues$default(this.movingAverageCalculator, 0, 1, null);
                this.estimatedTimeRemainingInMilliseconds = FetchUtils.calculateEstimatedTimeRemainingInMilliseconds(this.downloaded, this.total, getAverageDownloadedBytesPerSecond());
                j = this.downloaded;
                if (this.progressReportingIntervalMillis > 1000) {
                    delegate = getDelegate();
                    if (delegate != null) {
                        delegate.saveDownloadProgress(this.downloadInfo);
                    }
                }
            }
            if (FetchUtils.hasIntervalTimeElapsed(nanoTime, System.nanoTime(), this.progressReportingIntervalMillis)) {
                if (this.progressReportingIntervalMillis <= 1000) {
                    Delegate delegate2 = getDelegate();
                    if (delegate2 != null) {
                        delegate2.saveDownloadProgress(this.downloadInfo);
                    }
                }
                if (!getTerminated()) {
                    delegate = getDelegate();
                    if (delegate != null) {
                        delegate.onProgress(this.downloadInfo, this.estimatedTimeRemainingInMilliseconds, getAverageDownloadedBytesPerSecond());
                    }
                }
                nanoTime = System.nanoTime();
            }
            if (hasIntervalTimeElapsed) {
                nanoTime2 = System.nanoTime();
            }
        }
    }

    private final void downloadSliceFiles(Request request, List<FileSlice> list) {
        this.actionsCounter = 0;
        this.actionsTotal = list.size();
        this.outputStream = this.downloader.getRequestOutputStream(request, 0);
        if (this.outputStream == null) {
            this.randomAccessFileOutput = new RandomAccessFile(this.downloadInfo.getFile(), InternalZipConstants.WRITE_MODE);
            RandomAccessFile randomAccessFile = this.randomAccessFileOutput;
            if (randomAccessFile != null) {
                randomAccessFile.seek(0);
            }
        }
        for (FileSlice fileSlice : list) {
            if (!(getInterrupted() || getTerminated())) {
                ExecutorService executorService = this.executorService;
                if (executorService != null) {
                    executorService.execute(new ParallelFileDownloaderImpl$downloadSliceFiles$1(this, fileSlice, request));
                }
            }
        }
    }

    private final void deleteAllTempFiles() {
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
        r3 = this;
        r0 = r3.fileSlices;	 Catch:{ Exception -> 0x003f }
        r0 = r0.iterator();	 Catch:{ Exception -> 0x003f }
    L_0x0006:
        r1 = r0.hasNext();	 Catch:{ Exception -> 0x003f }
        if (r1 == 0) goto L_0x002a;	 Catch:{ Exception -> 0x003f }
    L_0x000c:
        r1 = r0.next();	 Catch:{ Exception -> 0x003f }
        r1 = (com.tonyodev.fetch2.downloader.ParallelFileDownloaderImpl.FileSlice) r1;	 Catch:{ Exception -> 0x003f }
        r2 = r3.getInterrupted();	 Catch:{ Exception -> 0x003f }
        if (r2 != 0) goto L_0x002a;	 Catch:{ Exception -> 0x003f }
    L_0x0018:
        r2 = r3.getTerminated();	 Catch:{ Exception -> 0x003f }
        if (r2 != 0) goto L_0x002a;	 Catch:{ Exception -> 0x003f }
    L_0x001e:
        r2 = r1.getId();	 Catch:{ Exception -> 0x003f }
        r1 = r1.getPosition();	 Catch:{ Exception -> 0x003f }
        r3.deleteTempFile(r2, r1);	 Catch:{ Exception -> 0x003f }
        goto L_0x0006;	 Catch:{ Exception -> 0x003f }
    L_0x002a:
        r0 = r3.getInterrupted();	 Catch:{ Exception -> 0x003f }
        if (r0 != 0) goto L_0x003f;	 Catch:{ Exception -> 0x003f }
    L_0x0030:
        r0 = r3.getTerminated();	 Catch:{ Exception -> 0x003f }
        if (r0 != 0) goto L_0x003f;	 Catch:{ Exception -> 0x003f }
    L_0x0036:
        r0 = r3.downloadInfo;	 Catch:{ Exception -> 0x003f }
        r0 = r0.getId();	 Catch:{ Exception -> 0x003f }
        r3.deleteMetaFile(r0);	 Catch:{ Exception -> 0x003f }
    L_0x003f:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tonyodev.fetch2.downloader.ParallelFileDownloaderImpl.deleteAllTempFiles():void");
    }

    private final void incrementActionCompletedCount() {
        synchronized (this.lock) {
            this.actionsCounter++;
            Unit unit = Unit.INSTANCE;
        }
    }

    private final void throwExceptionIfFound() {
        Throwable th = this.throwable;
        if (th != null) {
            throw th;
        }
    }
}
