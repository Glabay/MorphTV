package com.tonyodev.fetch2.downloader;

import com.tonyodev.fetch2.Downloader.Request;
import com.tonyodev.fetch2.downloader.ParallelFileDownloaderImpl.FileSlice;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 10})
/* compiled from: ParallelFileDownloaderImpl.kt */
final class ParallelFileDownloaderImpl$downloadSliceFiles$1 implements Runnable {
    final /* synthetic */ FileSlice $fileSlice;
    final /* synthetic */ Request $request;
    final /* synthetic */ ParallelFileDownloaderImpl this$0;

    ParallelFileDownloaderImpl$downloadSliceFiles$1(ParallelFileDownloaderImpl parallelFileDownloaderImpl, FileSlice fileSlice, Request request) {
        this.this$0 = parallelFileDownloaderImpl;
        this.$fileSlice = fileSlice;
        this.$request = request;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r23 = this;
        r1 = r23;
        r2 = r1.this$0;
        r2 = r2.downloadInfo;
        r3 = r2;
        r3 = (com.tonyodev.fetch2.Download) r3;
        r2 = r1.$fileSlice;
        r4 = r2.getStartBytes();
        r2 = r1.$fileSlice;
        r6 = r2.getDownloaded();
        r8 = r4 + r6;
        r6 = 0;
        r2 = 4;
        r10 = 0;
        r4 = r8;
        r8 = r2;
        r9 = r10;
        r2 = com.tonyodev.fetch2.util.FetchUtils.getRequestForDownload$default(r3, r4, r6, r8, r9);
        r3 = 0;
        r3 = (com.tonyodev.fetch2.Downloader.Response) r3;
        r4 = r1.this$0;	 Catch:{ Exception -> 0x0220 }
        r4 = r4.downloader;	 Catch:{ Exception -> 0x0220 }
        r2 = r4.execute(r2);	 Catch:{ Exception -> 0x0220 }
        r3 = r1.this$0;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r3 = r3.getTerminated();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        if (r3 != 0) goto L_0x01aa;
    L_0x0039:
        r3 = r1.this$0;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r3 = r3.getInterrupted();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        if (r3 != 0) goto L_0x01aa;
    L_0x0041:
        if (r2 == 0) goto L_0x01aa;
    L_0x0043:
        r3 = r2.isSuccessful();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r4 = 1;
        if (r3 != r4) goto L_0x01aa;
    L_0x004a:
        r3 = r1.this$0;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r3 = r3.downloadBufferSizeBytes;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r3 = new byte[r3];	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r4 = r2.getByteStream();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r5 = 0;
        r6 = -1;
        if (r4 == 0) goto L_0x0065;
    L_0x005a:
        r7 = r1.this$0;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r7 = r7.downloadBufferSizeBytes;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r4 = r4.read(r3, r5, r7);	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        goto L_0x0066;
    L_0x0065:
        r4 = -1;
    L_0x0066:
        r7 = r1.$fileSlice;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r7 = r7.getEndBytes();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r9 = r1.$fileSlice;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r9 = r9.getStartBytes();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r11 = r1.$fileSlice;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r11 = r11.getDownloaded();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r13 = 0;
        r13 = r9 + r11;
        r9 = r7 - r13;
        r7 = java.lang.System.nanoTime();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r11 = new kotlin.jvm.internal.Ref$IntRef;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r11.<init>();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r12 = new kotlin.jvm.internal.Ref$LongRef;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r12.<init>();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
    L_0x008b:
        r13 = 0;
        r15 = (r9 > r13 ? 1 : (r9 == r13 ? 0 : -1));
        if (r15 <= 0) goto L_0x0208;
    L_0x0091:
        if (r4 == r6) goto L_0x0208;
    L_0x0093:
        r13 = r1.this$0;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r13 = r13.getInterrupted();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        if (r13 != 0) goto L_0x0208;
    L_0x009b:
        r13 = r1.this$0;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r13 = r13.getTerminated();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        if (r13 != 0) goto L_0x0208;
    L_0x00a3:
        r13 = (long) r4;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r15 = (r13 > r9 ? 1 : (r13 == r9 ? 0 : -1));
        if (r15 > 0) goto L_0x00aa;
    L_0x00a8:
        r15 = r4;
        goto L_0x00ac;
    L_0x00aa:
        r4 = (int) r9;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r15 = -1;
    L_0x00ac:
        r11.element = r4;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r4 = r1.$fileSlice;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r13 = r4.getStartBytes();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r4 = r1.$fileSlice;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r16 = r4.getDownloaded();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r4 = 0;
        r19 = r7;
        r6 = r13 + r16;
        r12.element = r6;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r4 = r1.this$0;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r4 = r4.lock;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        monitor-enter(r4);	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r6 = r1.this$0;	 Catch:{ all -> 0x01a6 }
        r6 = r6.getInterrupted();	 Catch:{ all -> 0x01a6 }
        if (r6 != 0) goto L_0x012c;
    L_0x00d0:
        r6 = r1.this$0;	 Catch:{ all -> 0x01a6 }
        r6 = r6.getTerminated();	 Catch:{ all -> 0x01a6 }
        if (r6 != 0) goto L_0x012c;
    L_0x00d8:
        r6 = r1.this$0;	 Catch:{ all -> 0x01a6 }
        r6 = r6.outputStream;	 Catch:{ all -> 0x01a6 }
        if (r6 == 0) goto L_0x00f3;
    L_0x00e0:
        r7 = r1.this$0;	 Catch:{ all -> 0x01a6 }
        r7 = r7.downloader;	 Catch:{ all -> 0x01a6 }
        r8 = r1.$request;	 Catch:{ all -> 0x01a6 }
        r13 = r12.element;	 Catch:{ all -> 0x01a6 }
        r7.seekOutputStreamToPosition(r8, r6, r13);	 Catch:{ all -> 0x01a6 }
        r7 = r11.element;	 Catch:{ all -> 0x01a6 }
        r6.write(r3, r5, r7);	 Catch:{ all -> 0x01a6 }
        goto L_0x010d;
    L_0x00f3:
        r6 = r1.this$0;	 Catch:{ all -> 0x01a6 }
        r6 = r6.randomAccessFileOutput;	 Catch:{ all -> 0x01a6 }
        if (r6 == 0) goto L_0x0100;
    L_0x00fb:
        r7 = r12.element;	 Catch:{ all -> 0x01a6 }
        r6.seek(r7);	 Catch:{ all -> 0x01a6 }
    L_0x0100:
        r6 = r1.this$0;	 Catch:{ all -> 0x01a6 }
        r6 = r6.randomAccessFileOutput;	 Catch:{ all -> 0x01a6 }
        if (r6 == 0) goto L_0x010d;
    L_0x0108:
        r7 = r11.element;	 Catch:{ all -> 0x01a6 }
        r6.write(r3, r5, r7);	 Catch:{ all -> 0x01a6 }
    L_0x010d:
        r6 = r1.$fileSlice;	 Catch:{ all -> 0x01a6 }
        r7 = r6.getDownloaded();	 Catch:{ all -> 0x01a6 }
        r13 = r11.element;	 Catch:{ all -> 0x01a6 }
        r13 = (long) r13;	 Catch:{ all -> 0x01a6 }
        r21 = r9;
        r9 = r7 + r13;
        r6.setDownloaded(r9);	 Catch:{ all -> 0x01a6 }
        r6 = r1.this$0;	 Catch:{ all -> 0x01a6 }
        r7 = r6.downloaded;	 Catch:{ all -> 0x01a6 }
        r9 = r11.element;	 Catch:{ all -> 0x01a6 }
        r9 = (long) r9;	 Catch:{ all -> 0x01a6 }
        r13 = r7 + r9;
        r6.downloaded = r13;	 Catch:{ all -> 0x01a6 }
        goto L_0x012e;
    L_0x012c:
        r21 = r9;
    L_0x012e:
        r6 = kotlin.Unit.INSTANCE;	 Catch:{ all -> 0x01a6 }
        monitor-exit(r4);	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r4 = r1.this$0;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r4 = r4.getInterrupted();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        if (r4 != 0) goto L_0x019e;
    L_0x0139:
        r4 = r1.this$0;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r4 = r4.getTerminated();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        if (r4 != 0) goto L_0x019e;
    L_0x0141:
        r6 = java.lang.System.nanoTime();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r17 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r13 = r19;
        r4 = r15;
        r15 = r6;
        r6 = com.tonyodev.fetch2.util.FetchUtils.hasIntervalTimeElapsed(r13, r15, r17);	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        if (r6 == 0) goto L_0x016e;
    L_0x0151:
        r6 = r1.this$0;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r7 = r1.$fileSlice;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r7 = r7.getId();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r8 = r1.$fileSlice;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r8 = r8.getPosition();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r9 = r1.$fileSlice;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r9 = r9.getDownloaded();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r6.saveDownloadedInfo(r7, r8, r9);	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r7 = java.lang.System.nanoTime();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r19 = r7;
    L_0x016e:
        r6 = -1;
        if (r4 == r6) goto L_0x01a0;
    L_0x0171:
        r4 = r2.getByteStream();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        if (r4 == 0) goto L_0x0182;
    L_0x0177:
        r7 = r1.this$0;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r7 = r7.downloadBufferSizeBytes;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r4 = r4.read(r3, r5, r7);	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        goto L_0x0183;
    L_0x0182:
        r4 = -1;
    L_0x0183:
        r7 = r1.$fileSlice;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r7 = r7.getEndBytes();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r9 = r1.$fileSlice;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r9 = r9.getStartBytes();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r13 = r1.$fileSlice;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r13 = r13.getDownloaded();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r15 = 0;
        r15 = r9 + r13;
        r9 = r7 - r15;
        r7 = r19;
        goto L_0x008b;
    L_0x019e:
        r4 = r15;
        r6 = -1;
    L_0x01a0:
        r7 = r19;
        r9 = r21;
        goto L_0x008b;
    L_0x01a6:
        r0 = move-exception;
        r3 = r0;
        monitor-exit(r4);	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        throw r3;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
    L_0x01aa:
        if (r2 != 0) goto L_0x01c8;
    L_0x01ac:
        r3 = r1.this$0;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r3 = r3.getInterrupted();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        if (r3 != 0) goto L_0x01c8;
    L_0x01b4:
        r3 = r1.this$0;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r3 = r3.getTerminated();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        if (r3 != 0) goto L_0x01c8;
    L_0x01bc:
        r3 = new com.tonyodev.fetch2.exception.FetchException;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r4 = "empty_response_body";
        r5 = com.tonyodev.fetch2.exception.FetchException.Code.EMPTY_RESPONSE_BODY;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r3.<init>(r4, r5);	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r3 = (java.lang.Throwable) r3;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        throw r3;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
    L_0x01c8:
        if (r2 == 0) goto L_0x01ec;
    L_0x01ca:
        r3 = r2.isSuccessful();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        if (r3 != 0) goto L_0x01ec;
    L_0x01d0:
        r3 = r1.this$0;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r3 = r3.getInterrupted();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        if (r3 != 0) goto L_0x01ec;
    L_0x01d8:
        r3 = r1.this$0;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r3 = r3.getTerminated();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        if (r3 != 0) goto L_0x01ec;
    L_0x01e0:
        r3 = new com.tonyodev.fetch2.exception.FetchException;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r4 = "request_not_successful";
        r5 = com.tonyodev.fetch2.exception.FetchException.Code.REQUEST_NOT_SUCCESSFUL;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r3.<init>(r4, r5);	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r3 = (java.lang.Throwable) r3;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        throw r3;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
    L_0x01ec:
        r3 = r1.this$0;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r3 = r3.getInterrupted();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        if (r3 != 0) goto L_0x0208;
    L_0x01f4:
        r3 = r1.this$0;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r3 = r3.getTerminated();	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        if (r3 != 0) goto L_0x0208;
    L_0x01fc:
        r3 = new com.tonyodev.fetch2.exception.FetchException;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r4 = "unknown";
        r5 = com.tonyodev.fetch2.exception.FetchException.Code.UNKNOWN;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r3.<init>(r4, r5);	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        r3 = (java.lang.Throwable) r3;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
        throw r3;	 Catch:{ Exception -> 0x021a, all -> 0x0217 }
    L_0x0208:
        if (r2 == 0) goto L_0x0244;
    L_0x020a:
        r3 = r1.this$0;	 Catch:{ Exception -> 0x0214 }
        r3 = r3.downloader;	 Catch:{ Exception -> 0x0214 }
        r3.disconnect(r2);	 Catch:{ Exception -> 0x0214 }
        goto L_0x0244;
    L_0x0214:
        r0 = move-exception;
        r2 = r0;
        goto L_0x0237;
    L_0x0217:
        r0 = move-exception;
        r3 = r2;
        goto L_0x021e;
    L_0x021a:
        r0 = move-exception;
        r3 = r2;
        goto L_0x0221;
    L_0x021d:
        r0 = move-exception;
    L_0x021e:
        r2 = r0;
        goto L_0x024a;
    L_0x0220:
        r0 = move-exception;
    L_0x0221:
        r2 = r0;
        r4 = r1.this$0;	 Catch:{ all -> 0x021d }
        r2 = (java.lang.Throwable) r2;	 Catch:{ all -> 0x021d }
        r4.throwable = r2;	 Catch:{ all -> 0x021d }
        if (r3 == 0) goto L_0x0244;
    L_0x022b:
        r2 = r1.this$0;	 Catch:{ Exception -> 0x0235 }
        r2 = r2.downloader;	 Catch:{ Exception -> 0x0235 }
        r2.disconnect(r3);	 Catch:{ Exception -> 0x0235 }
        goto L_0x0244;
    L_0x0235:
        r0 = move-exception;
        r2 = r0;
    L_0x0237:
        r3 = r1.this$0;
        r3 = r3.logger;
        r4 = "FileDownloader";
        r2 = (java.lang.Throwable) r2;
        r3.mo4163e(r4, r2);
    L_0x0244:
        r2 = r1.this$0;
        r2.incrementActionCompletedCount();
        return;
    L_0x024a:
        if (r3 == 0) goto L_0x0265;
    L_0x024c:
        r4 = r1.this$0;	 Catch:{ Exception -> 0x0256 }
        r4 = r4.downloader;	 Catch:{ Exception -> 0x0256 }
        r4.disconnect(r3);	 Catch:{ Exception -> 0x0256 }
        goto L_0x0265;
    L_0x0256:
        r0 = move-exception;
        r3 = r0;
        r4 = r1.this$0;
        r4 = r4.logger;
        r5 = "FileDownloader";
        r3 = (java.lang.Throwable) r3;
        r4.mo4163e(r5, r3);
    L_0x0265:
        r3 = r1.this$0;
        r3.incrementActionCompletedCount();
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tonyodev.fetch2.downloader.ParallelFileDownloaderImpl$downloadSliceFiles$1.run():void");
    }
}
