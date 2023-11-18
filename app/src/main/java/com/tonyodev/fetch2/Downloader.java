package com.tonyodev.fetch2;

import com.google.android.gms.common.internal.ImagesContract;
import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001:\u0003\u0017\u0018\u0019J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0012\u0010\u0006\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0007\u001a\u00020\bH&J\u0012\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0007\u001a\u00020\bH&J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020\bH&J\u001f\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\u0010H&¢\u0006\u0002\u0010\u0011J\u001a\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u0010H&J \u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0010H&¨\u0006\u001a"}, d2 = {"Lcom/tonyodev/fetch2/Downloader;", "Ljava/io/Closeable;", "disconnect", "", "response", "Lcom/tonyodev/fetch2/Downloader$Response;", "execute", "request", "Lcom/tonyodev/fetch2/Downloader$Request;", "getDirectoryForFileDownloaderTypeParallel", "", "getFileDownloaderType", "Lcom/tonyodev/fetch2/Downloader$FileDownloaderType;", "getFileSlicingCount", "", "contentLength", "", "(Lcom/tonyodev/fetch2/Downloader$Request;J)Ljava/lang/Integer;", "getRequestOutputStream", "Ljava/io/OutputStream;", "filePointerOffset", "seekOutputStreamToPosition", "outputStream", "FileDownloaderType", "Request", "Response", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: Downloader.kt */
public interface Downloader extends Closeable {

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004¨\u0006\u0005"}, d2 = {"Lcom/tonyodev/fetch2/Downloader$FileDownloaderType;", "", "(Ljava/lang/String;I)V", "SEQUENTIAL", "PARALLEL", "fetch2_release"}, k = 1, mv = {1, 1, 10})
    /* compiled from: Downloader.kt */
    public enum FileDownloaderType {
    }

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010$\n\u0002\b\f\b\u0016\u0018\u00002\u00020\u0001B;\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0007\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\b\u0010\t\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\nR\u0011\u0010\b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001d\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0007¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0013\u0010\t\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\f¨\u0006\u0013"}, d2 = {"Lcom/tonyodev/fetch2/Downloader$Request;", "", "id", "", "url", "", "headers", "", "file", "tag", "(ILjava/lang/String;Ljava/util/Map;Ljava/lang/String;Ljava/lang/String;)V", "getFile", "()Ljava/lang/String;", "getHeaders", "()Ljava/util/Map;", "getId", "()I", "getTag", "getUrl", "fetch2_release"}, k = 1, mv = {1, 1, 10})
    /* compiled from: Downloader.kt */
    public static class Request {
        @NotNull
        private final String file;
        @NotNull
        private final Map<String, String> headers;
        private final int id;
        @Nullable
        private final String tag;
        @NotNull
        private final String url;

        public Request(int i, @NotNull String str, @NotNull Map<String, String> map, @NotNull String str2, @Nullable String str3) {
            Intrinsics.checkParameterIsNotNull(str, ImagesContract.URL);
            Intrinsics.checkParameterIsNotNull(map, "headers");
            Intrinsics.checkParameterIsNotNull(str2, "file");
            this.id = i;
            this.url = str;
            this.headers = map;
            this.file = str2;
            this.tag = str3;
        }

        public final int getId() {
            return this.id;
        }

        @NotNull
        public final String getUrl() {
            return this.url;
        }

        @NotNull
        public final Map<String, String> getHeaders() {
            return this.headers;
        }

        @NotNull
        public final String getFile() {
            return this.file;
        }

        @Nullable
        public final String getTag() {
            return this.tag;
        }
    }

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0016\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fR\u0013\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0013R\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015¨\u0006\u0016"}, d2 = {"Lcom/tonyodev/fetch2/Downloader$Response;", "", "code", "", "isSuccessful", "", "contentLength", "", "byteStream", "Ljava/io/InputStream;", "request", "Lcom/tonyodev/fetch2/Downloader$Request;", "(IZJLjava/io/InputStream;Lcom/tonyodev/fetch2/Downloader$Request;)V", "getByteStream", "()Ljava/io/InputStream;", "getCode", "()I", "getContentLength", "()J", "()Z", "getRequest", "()Lcom/tonyodev/fetch2/Downloader$Request;", "fetch2_release"}, k = 1, mv = {1, 1, 10})
    /* compiled from: Downloader.kt */
    public static class Response {
        @Nullable
        private final InputStream byteStream;
        private final int code;
        private final long contentLength;
        private final boolean isSuccessful;
        @NotNull
        private final Request request;

        public Response(int i, boolean z, long j, @Nullable InputStream inputStream, @NotNull Request request) {
            Intrinsics.checkParameterIsNotNull(request, "request");
            this.code = i;
            this.isSuccessful = z;
            this.contentLength = j;
            this.byteStream = inputStream;
            this.request = request;
        }

        public final int getCode() {
            return this.code;
        }

        public final boolean isSuccessful() {
            return this.isSuccessful;
        }

        public final long getContentLength() {
            return this.contentLength;
        }

        @Nullable
        public final InputStream getByteStream() {
            return this.byteStream;
        }

        @NotNull
        public final Request getRequest() {
            return this.request;
        }
    }

    void disconnect(@NotNull Response response);

    @Nullable
    Response execute(@NotNull Request request);

    @Nullable
    String getDirectoryForFileDownloaderTypeParallel(@NotNull Request request);

    @NotNull
    FileDownloaderType getFileDownloaderType(@NotNull Request request);

    @Nullable
    Integer getFileSlicingCount(@NotNull Request request, long j);

    @Nullable
    OutputStream getRequestOutputStream(@NotNull Request request, long j);

    void seekOutputStreamToPosition(@NotNull Request request, @NotNull OutputStream outputStream, long j);
}
