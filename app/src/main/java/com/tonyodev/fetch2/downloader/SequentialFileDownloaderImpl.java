package com.tonyodev.fetch2.downloader;

import com.google.common.net.HttpHeaders;
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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.lingala.zip4j.util.InternalZipConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000|\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f¢\u0006\u0002\u0010\u0010J\b\u0010.\u001a\u00020\u0007H\u0002J\b\u0010/\u001a\u000200H\u0002J\b\u00101\u001a\u000202H\u0002J\b\u00103\u001a\u000204H\u0016J$\u00105\u001a\u0002042\u0006\u00106\u001a\u0002072\b\u00108\u001a\u0004\u0018\u0001092\b\u0010:\u001a\u0004\u0018\u00010;H\u0002R\u000e\u0010\u0011\u001a\u00020\u0012X\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0013\u001a\u00020\u000f8\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001c\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u0014\u0010\u001e\u001a\u00020\u00038VX\u0004¢\u0006\u0006\u001a\u0004\b\u001f\u0010 R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010%\u001a\u00020\u000f8\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\u0015\"\u0004\b'\u0010\u0017R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020)X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u001e\u0010*\u001a\u00020\u000f8\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b+\u0010\u0015\"\u0004\b,\u0010\u0017R\u000e\u0010-\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000¨\u0006<"}, d2 = {"Lcom/tonyodev/fetch2/downloader/SequentialFileDownloaderImpl;", "Lcom/tonyodev/fetch2/downloader/FileDownloader;", "initialDownload", "Lcom/tonyodev/fetch2/Download;", "downloader", "Lcom/tonyodev/fetch2/Downloader;", "progressReportingIntervalMillis", "", "downloadBufferSizeBytes", "", "logger", "Lcom/tonyodev/fetch2/Logger;", "networkInfoProvider", "Lcom/tonyodev/fetch2/provider/NetworkInfoProvider;", "retryOnNetworkGain", "", "(Lcom/tonyodev/fetch2/Download;Lcom/tonyodev/fetch2/Downloader;JILcom/tonyodev/fetch2/Logger;Lcom/tonyodev/fetch2/provider/NetworkInfoProvider;Z)V", "averageDownloadedBytesPerSecond", "", "completedDownload", "getCompletedDownload", "()Z", "setCompletedDownload", "(Z)V", "delegate", "Lcom/tonyodev/fetch2/downloader/FileDownloader$Delegate;", "getDelegate", "()Lcom/tonyodev/fetch2/downloader/FileDownloader$Delegate;", "setDelegate", "(Lcom/tonyodev/fetch2/downloader/FileDownloader$Delegate;)V", "download", "getDownload", "()Lcom/tonyodev/fetch2/Download;", "downloadInfo", "Lcom/tonyodev/fetch2/database/DownloadInfo;", "downloaded", "estimatedTimeRemainingInMilliseconds", "interrupted", "getInterrupted", "setInterrupted", "movingAverageCalculator", "Lcom/tonyodev/fetch2/util/AverageCalculator;", "terminated", "getTerminated", "setTerminated", "total", "getAverageDownloadedBytesPerSecond", "getFile", "Ljava/io/File;", "getRequest", "Lcom/tonyodev/fetch2/Downloader$Request;", "run", "", "writeToOutput", "input", "Ljava/io/BufferedInputStream;", "randomAccessFileOutput", "Ljava/io/RandomAccessFile;", "downloaderOutputStream", "Ljava/io/OutputStream;", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: SequentialFileDownloaderImpl.kt */
public final class SequentialFileDownloaderImpl implements FileDownloader {
    private double averageDownloadedBytesPerSecond;
    private volatile boolean completedDownload;
    @Nullable
    private Delegate delegate;
    private final int downloadBufferSizeBytes;
    private DownloadInfo downloadInfo = FetchTypeConverterExtensions.toDownloadInfo(this.initialDownload);
    private long downloaded;
    private final Downloader downloader;
    private long estimatedTimeRemainingInMilliseconds = -1;
    private final Download initialDownload;
    private volatile boolean interrupted;
    private final Logger logger;
    private final AverageCalculator movingAverageCalculator = new AverageCalculator(5);
    private final NetworkInfoProvider networkInfoProvider;
    private final long progressReportingIntervalMillis;
    private final boolean retryOnNetworkGain;
    private volatile boolean terminated;
    private long total = -1;

    public SequentialFileDownloaderImpl(@NotNull Download download, @NotNull Downloader downloader, long j, int i, @NotNull Logger logger, @NotNull NetworkInfoProvider networkInfoProvider, boolean z) {
        Intrinsics.checkParameterIsNotNull(download, "initialDownload");
        Intrinsics.checkParameterIsNotNull(downloader, "downloader");
        Intrinsics.checkParameterIsNotNull(logger, "logger");
        Intrinsics.checkParameterIsNotNull(networkInfoProvider, "networkInfoProvider");
        this.initialDownload = download;
        this.downloader = downloader;
        this.progressReportingIntervalMillis = j;
        this.downloadBufferSizeBytes = i;
        this.logger = logger;
        this.networkInfoProvider = networkInfoProvider;
        this.retryOnNetworkGain = z;
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
        Exception e;
        Error errorFromThrowable;
        int isNetworkAvailable;
        int i;
        Delegate delegate;
        Throwable th;
        RandomAccessFile randomAccessFile;
        BufferedInputStream bufferedInputStream;
        RandomAccessFile randomAccessFile2 = (RandomAccessFile) null;
        OutputStream outputStream = (OutputStream) null;
        BufferedInputStream bufferedInputStream2 = (BufferedInputStream) null;
        Response response = (Response) null;
        Response execute;
        try {
            File file = getFile();
            this.downloaded = this.initialDownload.getDownloaded();
            if (!(getInterrupted() || getTerminated())) {
                boolean isSuccessful;
                Request request = getRequest();
                execute = this.downloader.execute(request);
                if (execute != null) {
                    try {
                        isSuccessful = execute.isSuccessful();
                    } catch (Exception e2) {
                        e = e2;
                        try {
                            if (!(getInterrupted() || getTerminated())) {
                                this.logger.mo4163e("FileDownloader", e);
                                errorFromThrowable = FetchErrorUtils.getErrorFromThrowable(e);
                                errorFromThrowable.setThrowable(e);
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
                        } catch (InterruptedException e3) {
                            this.logger.mo4163e("FileDownloader", e3);
                        } catch (Throwable th2) {
                            th = th2;
                        }
                        if (randomAccessFile2 != null) {
                            try {
                                randomAccessFile2.close();
                            } catch (Exception e4) {
                                this.logger.mo4163e("FileDownloader", e4);
                            }
                        }
                        if (bufferedInputStream2 != null) {
                            try {
                                bufferedInputStream2.close();
                            } catch (Exception e42) {
                                this.logger.mo4163e("FileDownloader", e42);
                            }
                        }
                        if (execute != null) {
                            try {
                                this.downloader.disconnect(execute);
                            } catch (Exception e422) {
                                this.logger.mo4163e("FileDownloader", e422);
                            }
                        }
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (Exception e5) {
                                e422 = e5;
                                this.logger.mo4163e("FileDownloader", e422);
                                setTerminated(true);
                            }
                        }
                        setTerminated(true);
                    }
                }
                isSuccessful = false;
                if (getInterrupted() || getTerminated() || execute == null || !isSuccessful) {
                    if (execute == null) {
                        if (!(getInterrupted() || getTerminated())) {
                            throw new FetchException(FetchErrorStrings.EMPTY_RESPONSE_BODY, Code.EMPTY_RESPONSE_BODY);
                        }
                    }
                    if (!isSuccessful && !getInterrupted() && !getTerminated()) {
                        throw new FetchException(FetchErrorStrings.RESPONSE_NOT_SUCCESSFUL, Code.REQUEST_NOT_SUCCESSFUL);
                    } else if (!(getInterrupted() || getTerminated())) {
                        throw new FetchException("unknown", Code.UNKNOWN);
                    }
                }
                long j;
                long j2 = -1;
                if (execute.getContentLength() != -1) {
                    j2 = this.downloaded + execute.getContentLength();
                }
                this.total = j2;
                Logger logger;
                StringBuilder stringBuilder;
                if (execute.getCode() == 206) {
                    logger = this.logger;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("FileDownloader resuming Download ");
                    stringBuilder.append(getDownload());
                    logger.mo4160d(stringBuilder.toString());
                    j = this.downloaded;
                } else {
                    logger = this.logger;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("FileDownloader starting Download ");
                    stringBuilder.append(getDownload());
                    logger.mo4160d(stringBuilder.toString());
                    j = 0;
                }
                OutputStream requestOutputStream = this.downloader.getRequestOutputStream(request, j);
                if (requestOutputStream == null) {
                    try {
                        RandomAccessFile randomAccessFile3 = new RandomAccessFile(file, InternalZipConstants.WRITE_MODE);
                        try {
                            randomAccessFile3.seek(j);
                            randomAccessFile2 = randomAccessFile3;
                        } catch (Exception e6) {
                            randomAccessFile = randomAccessFile3;
                            outputStream = requestOutputStream;
                            e422 = e6;
                            randomAccessFile2 = randomAccessFile;
                            this.logger.mo4163e("FileDownloader", e422);
                            errorFromThrowable = FetchErrorUtils.getErrorFromThrowable(e422);
                            errorFromThrowable.setThrowable(e422);
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
                            if (randomAccessFile2 != null) {
                                randomAccessFile2.close();
                            }
                            if (bufferedInputStream2 != null) {
                                bufferedInputStream2.close();
                            }
                            if (execute != null) {
                                this.downloader.disconnect(execute);
                            }
                            if (outputStream != null) {
                                outputStream.close();
                            }
                            setTerminated(true);
                        } catch (Throwable th3) {
                            randomAccessFile = randomAccessFile3;
                            outputStream = requestOutputStream;
                            th = th3;
                            randomAccessFile2 = randomAccessFile;
                            if (randomAccessFile2 != null) {
                                try {
                                    randomAccessFile2.close();
                                } catch (Exception e62) {
                                    this.logger.mo4163e("FileDownloader", e62);
                                }
                            }
                            if (bufferedInputStream2 != null) {
                                try {
                                    bufferedInputStream2.close();
                                } catch (Exception e622) {
                                    this.logger.mo4163e("FileDownloader", e622);
                                }
                            }
                            if (execute != null) {
                                try {
                                    this.downloader.disconnect(execute);
                                } catch (Exception e6222) {
                                    this.logger.mo4163e("FileDownloader", e6222);
                                }
                            }
                            if (outputStream != null) {
                                try {
                                    outputStream.close();
                                } catch (Exception e62222) {
                                    this.logger.mo4163e("FileDownloader", e62222);
                                }
                            }
                            setTerminated(true);
                            throw th;
                        }
                    } catch (Exception e7) {
                        Exception exception = e7;
                        outputStream = requestOutputStream;
                        e422 = exception;
                        this.logger.mo4163e("FileDownloader", e422);
                        errorFromThrowable = FetchErrorUtils.getErrorFromThrowable(e422);
                        errorFromThrowable.setThrowable(e422);
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
                        if (randomAccessFile2 != null) {
                            randomAccessFile2.close();
                        }
                        if (bufferedInputStream2 != null) {
                            bufferedInputStream2.close();
                        }
                        if (execute != null) {
                            this.downloader.disconnect(execute);
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        setTerminated(true);
                    } catch (Throwable th4) {
                        Throwable th5 = th4;
                        outputStream = requestOutputStream;
                        th = th5;
                        if (randomAccessFile2 != null) {
                            randomAccessFile2.close();
                        }
                        if (bufferedInputStream2 != null) {
                            bufferedInputStream2.close();
                        }
                        if (execute != null) {
                            this.downloader.disconnect(execute);
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        setTerminated(true);
                        throw th;
                    }
                }
                if (!(getInterrupted() || getTerminated())) {
                    BufferedInputStream bufferedInputStream3 = new BufferedInputStream(execute.getByteStream(), this.downloadBufferSizeBytes);
                    try {
                        this.downloadInfo.setDownloaded(this.downloaded);
                        this.downloadInfo.setTotal(this.total);
                        if (!getTerminated()) {
                            Delegate delegate2 = getDelegate();
                            if (delegate2 != null) {
                                delegate2.onStarted(this.downloadInfo, this.estimatedTimeRemainingInMilliseconds, getAverageDownloadedBytesPerSecond());
                            }
                        }
                        writeToOutput(bufferedInputStream3, randomAccessFile2, requestOutputStream);
                        bufferedInputStream2 = bufferedInputStream3;
                    } catch (Exception e8) {
                        bufferedInputStream = bufferedInputStream3;
                        outputStream = requestOutputStream;
                        e422 = e8;
                        bufferedInputStream2 = bufferedInputStream;
                        this.logger.mo4163e("FileDownloader", e422);
                        errorFromThrowable = FetchErrorUtils.getErrorFromThrowable(e422);
                        errorFromThrowable.setThrowable(e422);
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
                        if (randomAccessFile2 != null) {
                            randomAccessFile2.close();
                        }
                        if (bufferedInputStream2 != null) {
                            bufferedInputStream2.close();
                        }
                        if (execute != null) {
                            this.downloader.disconnect(execute);
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        setTerminated(true);
                    } catch (Throwable th6) {
                        bufferedInputStream = bufferedInputStream3;
                        outputStream = requestOutputStream;
                        th = th6;
                        bufferedInputStream2 = bufferedInputStream;
                        if (randomAccessFile2 != null) {
                            randomAccessFile2.close();
                        }
                        if (bufferedInputStream2 != null) {
                            bufferedInputStream2.close();
                        }
                        if (execute != null) {
                            this.downloader.disconnect(execute);
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        setTerminated(true);
                        throw th;
                    }
                }
                outputStream = requestOutputStream;
                response = execute;
            }
            if (!getCompletedDownload()) {
                this.downloadInfo.setDownloaded(this.downloaded);
                this.downloadInfo.setTotal(this.total);
                if (!getTerminated()) {
                    Delegate delegate3 = getDelegate();
                    if (delegate3 != null) {
                        delegate3.onProgress(this.downloadInfo, this.estimatedTimeRemainingInMilliseconds, getAverageDownloadedBytesPerSecond());
                    }
                }
            }
            if (randomAccessFile2 != null) {
                try {
                    randomAccessFile2.close();
                } catch (Exception e622222) {
                    this.logger.mo4163e("FileDownloader", e622222);
                }
            }
            if (bufferedInputStream2 != null) {
                try {
                    bufferedInputStream2.close();
                } catch (Exception e6222222) {
                    this.logger.mo4163e("FileDownloader", e6222222);
                }
            }
            if (response != null) {
                try {
                    this.downloader.disconnect(response);
                } catch (Exception e4222) {
                    this.logger.mo4163e("FileDownloader", e4222);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e9) {
                    e4222 = e9;
                    this.logger.mo4163e("FileDownloader", e4222);
                    setTerminated(true);
                }
            }
        } catch (Exception e10) {
            execute = response;
            e4222 = e10;
            this.logger.mo4163e("FileDownloader", e4222);
            errorFromThrowable = FetchErrorUtils.getErrorFromThrowable(e4222);
            errorFromThrowable.setThrowable(e4222);
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
            if (randomAccessFile2 != null) {
                randomAccessFile2.close();
            }
            if (bufferedInputStream2 != null) {
                bufferedInputStream2.close();
            }
            if (execute != null) {
                this.downloader.disconnect(execute);
            }
            if (outputStream != null) {
                outputStream.close();
            }
            setTerminated(true);
        } catch (Throwable th7) {
            execute = response;
            th = th7;
            if (randomAccessFile2 != null) {
                randomAccessFile2.close();
            }
            if (bufferedInputStream2 != null) {
                bufferedInputStream2.close();
            }
            if (execute != null) {
                this.downloader.disconnect(execute);
            }
            if (outputStream != null) {
                outputStream.close();
            }
            setTerminated(true);
            throw th;
        }
        setTerminated(true);
    }

    private final void writeToOutput(BufferedInputStream bufferedInputStream, RandomAccessFile randomAccessFile, OutputStream outputStream) {
        BufferedInputStream bufferedInputStream2 = bufferedInputStream;
        RandomAccessFile randomAccessFile2 = randomAccessFile;
        OutputStream outputStream2 = outputStream;
        long j = this.downloaded;
        byte[] bArr = new byte[this.downloadBufferSizeBytes];
        long nanoTime = System.nanoTime();
        long nanoTime2 = System.nanoTime();
        int read = bufferedInputStream2.read(bArr, 0, this.downloadBufferSizeBytes);
        while (!getInterrupted() && !getTerminated() && read != -1) {
            byte[] bArr2;
            Delegate delegate;
            if (randomAccessFile2 != null) {
                randomAccessFile2.write(bArr, 0, read);
            }
            if (outputStream2 != null) {
                outputStream2.write(bArr, 0, read);
            }
            long j2;
            if (getTerminated()) {
                bArr2 = bArr;
                j2 = nanoTime;
            } else {
                byte[] bArr3 = bArr;
                j2 = nanoTime;
                r1.downloaded += (long) read;
                r1.downloadInfo.setDownloaded(r1.downloaded);
                r1.downloadInfo.setTotal(r1.total);
                boolean hasIntervalTimeElapsed = FetchUtils.hasIntervalTimeElapsed(nanoTime2, System.nanoTime(), 1000);
                if (hasIntervalTimeElapsed) {
                    r1.movingAverageCalculator.add((double) (r1.downloaded - j));
                    r1.averageDownloadedBytesPerSecond = AverageCalculator.getMovingAverageWithWeightOnRecentValues$default(r1.movingAverageCalculator, 0, 1, null);
                    r1.estimatedTimeRemainingInMilliseconds = FetchUtils.calculateEstimatedTimeRemainingInMilliseconds(r1.downloaded, r1.total, getAverageDownloadedBytesPerSecond());
                    j = r1.downloaded;
                    if (r1.progressReportingIntervalMillis > 1000) {
                        delegate = getDelegate();
                        if (delegate != null) {
                            delegate.saveDownloadProgress(r1.downloadInfo);
                        }
                    }
                }
                if (FetchUtils.hasIntervalTimeElapsed(j2, System.nanoTime(), r1.progressReportingIntervalMillis)) {
                    if (r1.progressReportingIntervalMillis <= 1000) {
                        delegate = getDelegate();
                        if (delegate != null) {
                            delegate.saveDownloadProgress(r1.downloadInfo);
                        }
                    }
                    if (!getTerminated()) {
                        Delegate delegate2 = getDelegate();
                        if (delegate2 != null) {
                            delegate2.onProgress(r1.downloadInfo, r1.estimatedTimeRemainingInMilliseconds, getAverageDownloadedBytesPerSecond());
                        }
                    }
                    j2 = System.nanoTime();
                }
                if (hasIntervalTimeElapsed) {
                    nanoTime2 = System.nanoTime();
                }
                bArr2 = bArr3;
                read = bufferedInputStream2.read(bArr2, 0, r1.downloadBufferSizeBytes);
                nanoTime = j2;
            }
            bArr = bArr2;
            randomAccessFile2 = randomAccessFile;
            outputStream2 = outputStream;
        }
        if (outputStream2 != null) {
            try {
                outputStream.flush();
            } catch (IOException e) {
                r1.logger.mo4163e("FileDownloader", e);
            }
        }
        if (read == -1 && !getInterrupted() && !getTerminated()) {
            r1.total = r1.downloaded;
            setCompletedDownload(true);
            r1.downloadInfo.setDownloaded(r1.downloaded);
            r1.downloadInfo.setTotal(r1.total);
            if (!getTerminated()) {
                delegate = getDelegate();
                if (delegate != null) {
                    delegate.onProgress(r1.downloadInfo, r1.estimatedTimeRemainingInMilliseconds, getAverageDownloadedBytesPerSecond());
                }
                Delegate delegate3 = getDelegate();
                if (delegate3 != null) {
                    delegate3.onComplete(r1.downloadInfo);
                }
            }
        }
    }

    private final File getFile() {
        File file = new File(this.initialDownload.getFile());
        if (!file.exists()) {
            Logger logger;
            StringBuilder stringBuilder;
            if (file.getParentFile() == null || file.getParentFile().exists()) {
                file.createNewFile();
                logger = this.logger;
                stringBuilder = new StringBuilder();
                stringBuilder.append("FileDownloader download file ");
                stringBuilder.append(file.getAbsolutePath());
                stringBuilder.append(" created");
                logger.mo4160d(stringBuilder.toString());
            } else if (file.getParentFile().mkdirs()) {
                file.createNewFile();
                logger = this.logger;
                stringBuilder = new StringBuilder();
                stringBuilder.append("FileDownloader download file ");
                stringBuilder.append(file.getAbsolutePath());
                stringBuilder.append(" created");
                logger.mo4160d(stringBuilder.toString());
            }
        }
        return file;
    }

    private final Request getRequest() {
        Map toMutableMap = MapsKt__MapsKt.toMutableMap(this.initialDownload.getHeaders());
        String str = HttpHeaders.RANGE;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("bytes=");
        stringBuilder.append(this.downloaded);
        stringBuilder.append('-');
        toMutableMap.put(str, stringBuilder.toString());
        return new Request(this.initialDownload.getId(), this.initialDownload.getUrl(), toMutableMap, this.initialDownload.getFile(), this.initialDownload.getTag());
    }

    private final long getAverageDownloadedBytesPerSecond() {
        if (this.averageDownloadedBytesPerSecond < ((double) 1)) {
            return 0;
        }
        return (long) Math.ceil(this.averageDownloadedBytesPerSecond);
    }
}
