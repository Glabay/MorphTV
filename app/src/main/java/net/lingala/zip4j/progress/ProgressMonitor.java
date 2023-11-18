package net.lingala.zip4j.progress;

import net.lingala.zip4j.exception.ZipException;

public class ProgressMonitor {
    public static final int OPERATION_ADD = 0;
    public static final int OPERATION_CALC_CRC = 3;
    public static final int OPERATION_EXTRACT = 1;
    public static final int OPERATION_MERGE = 4;
    public static final int OPERATION_NONE = -1;
    public static final int OPERATION_REMOVE = 2;
    public static final int RESULT_CANCELLED = 3;
    public static final int RESULT_ERROR = 2;
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_WORKING = 1;
    public static final int STATE_BUSY = 1;
    public static final int STATE_READY = 0;
    private boolean cancelAllTasks;
    private int currentOperation;
    private Throwable exception;
    private String fileName;
    private boolean pause;
    private int percentDone = 0;
    private int result;
    private int state;
    private long totalWork;
    private long workCompleted;

    public ProgressMonitor() {
        reset();
    }

    public int getState() {
        return this.state;
    }

    public void setState(int i) {
        this.state = i;
    }

    public long getTotalWork() {
        return this.totalWork;
    }

    public void setTotalWork(long j) {
        this.totalWork = j;
    }

    public long getWorkCompleted() {
        return this.workCompleted;
    }

    public void updateWorkCompleted(long r5) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r4 = this;
        r0 = r4.workCompleted;
        r2 = r0 + r5;
        r4.workCompleted = r2;
        r5 = r4.totalWork;
        r0 = 0;
        r2 = (r5 > r0 ? 1 : (r5 == r0 ? 0 : -1));
        if (r2 <= 0) goto L_0x0022;
    L_0x000e:
        r5 = r4.workCompleted;
        r0 = 100;
        r5 = r5 * r0;
        r0 = r4.totalWork;
        r5 = r5 / r0;
        r5 = (int) r5;
        r4.percentDone = r5;
        r5 = r4.percentDone;
        r6 = 100;
        if (r5 <= r6) goto L_0x0022;
    L_0x0020:
        r4.percentDone = r6;
    L_0x0022:
        r5 = r4.pause;
        if (r5 != 0) goto L_0x0027;
    L_0x0026:
        return;
    L_0x0027:
        r5 = 150; // 0x96 float:2.1E-43 double:7.4E-322;
        java.lang.Thread.sleep(r5);	 Catch:{ InterruptedException -> 0x0022 }
        goto L_0x0022;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.progress.ProgressMonitor.updateWorkCompleted(long):void");
    }

    public int getPercentDone() {
        return this.percentDone;
    }

    public void setPercentDone(int i) {
        this.percentDone = i;
    }

    public int getResult() {
        return this.result;
    }

    public void setResult(int i) {
        this.result = i;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public int getCurrentOperation() {
        return this.currentOperation;
    }

    public void setCurrentOperation(int i) {
        this.currentOperation = i;
    }

    public Throwable getException() {
        return this.exception;
    }

    public void setException(Throwable th) {
        this.exception = th;
    }

    public void endProgressMonitorSuccess() throws ZipException {
        reset();
        this.result = 0;
    }

    public void endProgressMonitorError(Throwable th) throws ZipException {
        reset();
        this.result = 2;
        this.exception = th;
    }

    public void reset() {
        this.currentOperation = -1;
        this.state = 0;
        this.fileName = null;
        this.totalWork = 0;
        this.workCompleted = 0;
        this.percentDone = 0;
    }

    public void fullReset() {
        reset();
        this.exception = null;
        this.result = 0;
    }

    public boolean isCancelAllTasks() {
        return this.cancelAllTasks;
    }

    public void cancelAllTasks() {
        this.cancelAllTasks = true;
    }

    public boolean isPause() {
        return this.pause;
    }

    public void setPause(boolean z) {
        this.pause = z;
    }
}
