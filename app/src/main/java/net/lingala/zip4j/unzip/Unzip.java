package net.lingala.zip4j.unzip;

import java.io.File;
import java.util.ArrayList;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.CentralDirectory;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Zip4jUtil;

public class Unzip {
    private ZipModel zipModel;

    public Unzip(ZipModel zipModel) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("ZipModel is null");
        }
        this.zipModel = zipModel;
    }

    public void extractAll(UnzipParameters unzipParameters, String str, ProgressMonitor progressMonitor, boolean z) throws ZipException {
        CentralDirectory centralDirectory = this.zipModel.getCentralDirectory();
        if (centralDirectory != null) {
            if (centralDirectory.getFileHeaders() != null) {
                final ArrayList fileHeaders = centralDirectory.getFileHeaders();
                progressMonitor.setCurrentOperation(1);
                progressMonitor.setTotalWork(calculateTotalWork(fileHeaders));
                progressMonitor.setState(1);
                if (z) {
                    final UnzipParameters unzipParameters2 = unzipParameters;
                    final ProgressMonitor progressMonitor2 = progressMonitor;
                    final String str2 = str;
                    new Thread(this, InternalZipConstants.THREAD_NAME) {
                        final /* synthetic */ Unzip this$0;

                        public void run() {
                            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
                            /*
                            r5 = this;
                            r0 = r5.this$0;	 Catch:{ ZipException -> 0x0012 }
                            r1 = r5;	 Catch:{ ZipException -> 0x0012 }
                            r2 = r6;	 Catch:{ ZipException -> 0x0012 }
                            r3 = r7;	 Catch:{ ZipException -> 0x0012 }
                            r4 = r8;	 Catch:{ ZipException -> 0x0012 }
                            r0.initExtractAll(r1, r2, r3, r4);	 Catch:{ ZipException -> 0x0012 }
                            r0 = r7;	 Catch:{ ZipException -> 0x0012 }
                            r0.endProgressMonitorSuccess();	 Catch:{ ZipException -> 0x0012 }
                        L_0x0012:
                            return;
                            */
                            throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.unzip.Unzip.1.run():void");
                        }
                    }.start();
                    return;
                }
                initExtractAll(fileHeaders, unzipParameters, progressMonitor, str);
                return;
            }
        }
        throw new ZipException("invalid central directory in zipModel");
    }

    private void initExtractAll(ArrayList arrayList, UnzipParameters unzipParameters, ProgressMonitor progressMonitor, String str) throws ZipException {
        for (int i = 0; i < arrayList.size(); i++) {
            initExtractFile((FileHeader) arrayList.get(i), str, unzipParameters, null, progressMonitor);
            if (progressMonitor.isCancelAllTasks()) {
                progressMonitor.setResult(3);
                progressMonitor.setState(0);
                return;
            }
        }
    }

    public void extractFile(FileHeader fileHeader, String str, UnzipParameters unzipParameters, String str2, ProgressMonitor progressMonitor, boolean z) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("fileHeader is null");
        }
        progressMonitor.setCurrentOperation(1);
        progressMonitor.setTotalWork(fileHeader.getCompressedSize());
        progressMonitor.setState(1);
        progressMonitor.setPercentDone(0);
        progressMonitor.setFileName(fileHeader.getFileName());
        if (z) {
            final FileHeader fileHeader2 = fileHeader;
            final String str3 = str;
            final UnzipParameters unzipParameters2 = unzipParameters;
            final String str4 = str2;
            final ProgressMonitor progressMonitor2 = progressMonitor;
            new Thread(this, InternalZipConstants.THREAD_NAME) {
                final /* synthetic */ Unzip this$0;

                public void run() {
                    /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
                    /*
                    r6 = this;
                    r0 = r6.this$0;	 Catch:{ ZipException -> 0x0014 }
                    r1 = r4;	 Catch:{ ZipException -> 0x0014 }
                    r2 = r5;	 Catch:{ ZipException -> 0x0014 }
                    r3 = r6;	 Catch:{ ZipException -> 0x0014 }
                    r4 = r7;	 Catch:{ ZipException -> 0x0014 }
                    r5 = r8;	 Catch:{ ZipException -> 0x0014 }
                    r0.initExtractFile(r1, r2, r3, r4, r5);	 Catch:{ ZipException -> 0x0014 }
                    r0 = r8;	 Catch:{ ZipException -> 0x0014 }
                    r0.endProgressMonitorSuccess();	 Catch:{ ZipException -> 0x0014 }
                L_0x0014:
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.unzip.Unzip.2.run():void");
                }
            }.start();
            return;
        }
        initExtractFile(fileHeader, str, unzipParameters, str2, progressMonitor);
        progressMonitor.endProgressMonitorSuccess();
    }

    private void initExtractFile(FileHeader fileHeader, String str, UnzipParameters unzipParameters, String str2, ProgressMonitor progressMonitor) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("fileHeader is null");
        }
        try {
            progressMonitor.setFileName(fileHeader.getFileName());
            if (!str.endsWith(InternalZipConstants.FILE_SEPARATOR)) {
                StringBuffer stringBuffer = new StringBuffer(String.valueOf(str));
                stringBuffer.append(InternalZipConstants.FILE_SEPARATOR);
                str = stringBuffer.toString();
            }
            if (fileHeader.isDirectory()) {
                fileHeader = fileHeader.getFileName();
                if (Zip4jUtil.isStringNotNullAndNotEmpty(fileHeader) != null) {
                    unzipParameters = new StringBuffer(String.valueOf(str));
                    unzipParameters.append(fileHeader);
                    str = new File(unzipParameters.toString());
                    if (str.exists() == null) {
                        str.mkdirs();
                    }
                } else {
                    return;
                }
            }
            checkOutputDirectoryStructure(fileHeader, str, str2);
            new UnzipEngine(this.zipModel, fileHeader).unzipFile(progressMonitor, str, str2, unzipParameters);
        } catch (Throwable e) {
            progressMonitor.endProgressMonitorError(e);
            throw new ZipException(e);
        } catch (FileHeader fileHeader2) {
            progressMonitor.endProgressMonitorError(fileHeader2);
            throw fileHeader2;
        } catch (Throwable e2) {
            progressMonitor.endProgressMonitorError(e2);
            throw new ZipException(e2);
        } catch (Throwable e22) {
            progressMonitor.endProgressMonitorError(e22);
            throw new ZipException(e22);
        }
    }

    public ZipInputStream getInputStream(FileHeader fileHeader) throws ZipException {
        return new UnzipEngine(this.zipModel, fileHeader).getInputStream();
    }

    private void checkOutputDirectoryStructure(FileHeader fileHeader, String str, String str2) throws ZipException {
        if (fileHeader != null) {
            if (Zip4jUtil.isStringNotNullAndNotEmpty(str)) {
                fileHeader = fileHeader.getFileName();
                if (Zip4jUtil.isStringNotNullAndNotEmpty(str2)) {
                    fileHeader = str2;
                }
                if (Zip4jUtil.isStringNotNullAndNotEmpty(fileHeader) != null) {
                    str2 = new StringBuffer(String.valueOf(str));
                    str2.append(fileHeader);
                    try {
                        str = new File(new File(str2.toString()).getParent());
                        if (str.exists() == null) {
                            str.mkdirs();
                        }
                        return;
                    } catch (Throwable e) {
                        throw new ZipException(e);
                    }
                }
                return;
            }
        }
        throw new ZipException("Cannot check output directory structure...one of the parameters was null");
    }

    private long calculateTotalWork(ArrayList arrayList) throws ZipException {
        if (arrayList == null) {
            throw new ZipException("fileHeaders is null, cannot calculate total work");
        }
        long j = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            long compressedSize;
            FileHeader fileHeader = (FileHeader) arrayList.get(i);
            if (fileHeader.getZip64ExtendedInfo() == null || fileHeader.getZip64ExtendedInfo().getUnCompressedSize() <= 0) {
                compressedSize = j + fileHeader.getCompressedSize();
            } else {
                compressedSize = j + fileHeader.getZip64ExtendedInfo().getCompressedSize();
            }
            j = compressedSize;
        }
        return j;
    }
}
