package net.lingala.zip4j.zip;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.EndCentralDirRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Zip4jUtil;

public class ZipEngine {
    private ZipModel zipModel;

    public ZipEngine(ZipModel zipModel) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null in ZipEngine constructor");
        }
        this.zipModel = zipModel;
    }

    public void addFiles(ArrayList arrayList, ZipParameters zipParameters, ProgressMonitor progressMonitor, boolean z) throws ZipException {
        if (arrayList != null) {
            if (zipParameters != null) {
                if (arrayList.size() <= 0) {
                    throw new ZipException("no files to add");
                }
                progressMonitor.setCurrentOperation(0);
                progressMonitor.setState(1);
                progressMonitor.setResult(1);
                if (z) {
                    progressMonitor.setTotalWork(calculateTotalWork(arrayList, zipParameters));
                    progressMonitor.setFileName(((File) arrayList.get(0)).getAbsolutePath());
                    final ArrayList arrayList2 = arrayList;
                    final ZipParameters zipParameters2 = zipParameters;
                    final ProgressMonitor progressMonitor2 = progressMonitor;
                    new Thread(this, InternalZipConstants.THREAD_NAME) {
                        final /* synthetic */ ZipEngine this$0;

                        public void run() {
                            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
                            /*
                            r4 = this;
                            r0 = r4.this$0;	 Catch:{ ZipException -> 0x000b }
                            r1 = r3;	 Catch:{ ZipException -> 0x000b }
                            r2 = r4;	 Catch:{ ZipException -> 0x000b }
                            r3 = r5;	 Catch:{ ZipException -> 0x000b }
                            r0.initAddFiles(r1, r2, r3);	 Catch:{ ZipException -> 0x000b }
                        L_0x000b:
                            return;
                            */
                            throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.zip.ZipEngine.1.run():void");
                        }
                    }.start();
                    return;
                }
                initAddFiles(arrayList, zipParameters, progressMonitor);
                return;
            }
        }
        throw new ZipException("one of the input parameters is null when adding files");
    }

    private void initAddFiles(java.util.ArrayList r13, net.lingala.zip4j.model.ZipParameters r14, net.lingala.zip4j.progress.ProgressMonitor r15) throws net.lingala.zip4j.exception.ZipException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r12 = this;
        if (r13 == 0) goto L_0x01b0;
    L_0x0002:
        if (r14 != 0) goto L_0x0006;
    L_0x0004:
        goto L_0x01b0;
    L_0x0006:
        r0 = r13.size();
        if (r0 > 0) goto L_0x0014;
    L_0x000c:
        r13 = new net.lingala.zip4j.exception.ZipException;
        r14 = "no files to add";
        r13.<init>(r14);
        throw r13;
    L_0x0014:
        r0 = r12.zipModel;
        r0 = r0.getEndCentralDirRecord();
        if (r0 != 0) goto L_0x0025;
    L_0x001c:
        r0 = r12.zipModel;
        r1 = r12.createEndOfCentralDirectoryRecord();
        r0.setEndCentralDirRecord(r1);
    L_0x0025:
        r0 = 0;
        r12.checkParameters(r14);	 Catch:{ ZipException -> 0x019d, Exception -> 0x0192, all -> 0x018e }
        r12.removeFilesIfExists(r13, r14, r15);	 Catch:{ ZipException -> 0x019d, Exception -> 0x0192, all -> 0x018e }
        r1 = r12.zipModel;	 Catch:{ ZipException -> 0x019d, Exception -> 0x0192, all -> 0x018e }
        r1 = r1.getZipFile();	 Catch:{ ZipException -> 0x019d, Exception -> 0x0192, all -> 0x018e }
        r1 = net.lingala.zip4j.util.Zip4jUtil.checkFileExists(r1);	 Catch:{ ZipException -> 0x019d, Exception -> 0x0192, all -> 0x018e }
        r2 = new net.lingala.zip4j.io.SplitOutputStream;	 Catch:{ ZipException -> 0x019d, Exception -> 0x0192, all -> 0x018e }
        r3 = new java.io.File;	 Catch:{ ZipException -> 0x019d, Exception -> 0x0192, all -> 0x018e }
        r4 = r12.zipModel;	 Catch:{ ZipException -> 0x019d, Exception -> 0x0192, all -> 0x018e }
        r4 = r4.getZipFile();	 Catch:{ ZipException -> 0x019d, Exception -> 0x0192, all -> 0x018e }
        r3.<init>(r4);	 Catch:{ ZipException -> 0x019d, Exception -> 0x0192, all -> 0x018e }
        r4 = r12.zipModel;	 Catch:{ ZipException -> 0x019d, Exception -> 0x0192, all -> 0x018e }
        r4 = r4.getSplitLength();	 Catch:{ ZipException -> 0x019d, Exception -> 0x0192, all -> 0x018e }
        r2.<init>(r3, r4);	 Catch:{ ZipException -> 0x019d, Exception -> 0x0192, all -> 0x018e }
        r3 = new net.lingala.zip4j.io.ZipOutputStream;	 Catch:{ ZipException -> 0x019d, Exception -> 0x0192, all -> 0x018e }
        r4 = r12.zipModel;	 Catch:{ ZipException -> 0x019d, Exception -> 0x0192, all -> 0x018e }
        r3.<init>(r2, r4);	 Catch:{ ZipException -> 0x019d, Exception -> 0x0192, all -> 0x018e }
        if (r1 == 0) goto L_0x0081;
    L_0x0055:
        r1 = r12.zipModel;	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
        r1 = r1.getEndCentralDirRecord();	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
        if (r1 != 0) goto L_0x0065;	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
    L_0x005d:
        r13 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
        r14 = "invalid end of central directory record";	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
        r13.<init>(r14);	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
        throw r13;	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
    L_0x0065:
        r1 = r12.zipModel;	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
        r1 = r1.getEndCentralDirRecord();	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
        r4 = r1.getOffsetOfStartOfCentralDir();	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
        r2.seek(r4);	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
        goto L_0x0081;	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
    L_0x0073:
        r13 = move-exception;	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
        r4 = r0;	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
        goto L_0x01a5;	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
    L_0x0077:
        r13 = move-exception;	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
        r4 = r0;	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
    L_0x0079:
        r0 = r3;	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
        goto L_0x0194;	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
    L_0x007c:
        r13 = move-exception;	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
        r4 = r0;	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
    L_0x007e:
        r0 = r3;	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
        goto L_0x019f;	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
    L_0x0081:
        r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
        r1 = new byte[r1];	 Catch:{ ZipException -> 0x007c, Exception -> 0x0077, all -> 0x0073 }
        r2 = 0;
        r4 = r0;
        r0 = 0;
    L_0x0088:
        r5 = r13.size();	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        if (r0 < r5) goto L_0x009f;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
    L_0x008e:
        r3.finish();	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r15.endProgressMonitorSuccess();	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        if (r4 == 0) goto L_0x0099;
    L_0x0096:
        r4.close();	 Catch:{ IOException -> 0x0099 }
    L_0x0099:
        if (r3 == 0) goto L_0x009e;
    L_0x009b:
        r3.close();	 Catch:{ IOException -> 0x009e }
    L_0x009e:
        return;
    L_0x009f:
        r5 = r15.isCancelAllTasks();	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r6 = 3;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        if (r5 == 0) goto L_0x00b7;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
    L_0x00a6:
        r15.setResult(r6);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r15.setState(r2);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        if (r4 == 0) goto L_0x00b1;
    L_0x00ae:
        r4.close();	 Catch:{ IOException -> 0x00b1 }
    L_0x00b1:
        if (r3 == 0) goto L_0x00b6;
    L_0x00b3:
        r3.close();	 Catch:{ IOException -> 0x00b6 }
    L_0x00b6:
        return;
    L_0x00b7:
        r5 = r14.clone();	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r5 = (net.lingala.zip4j.model.ZipParameters) r5;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r7 = r13.get(r0);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r7 = (java.io.File) r7;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r7 = r7.getAbsolutePath();	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r15.setFileName(r7);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r7 = r13.get(r0);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r7 = (java.io.File) r7;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r7 = r7.isDirectory();	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        if (r7 != 0) goto L_0x0124;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
    L_0x00d6:
        r7 = r5.isEncryptFiles();	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        if (r7 == 0) goto L_0x0111;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
    L_0x00dc:
        r7 = r5.getEncryptionMethod();	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        if (r7 != 0) goto L_0x0111;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
    L_0x00e2:
        r15.setCurrentOperation(r6);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r7 = r13.get(r0);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r7 = (java.io.File) r7;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r7 = r7.getAbsolutePath();	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r7 = net.lingala.zip4j.util.CRCUtil.computeFileCRC(r7, r15);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r7 = (int) r7;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r5.setSourceFileCRC(r7);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r15.setCurrentOperation(r2);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r7 = r15.isCancelAllTasks();	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        if (r7 == 0) goto L_0x0111;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
    L_0x0100:
        r15.setResult(r6);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r15.setState(r2);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        if (r4 == 0) goto L_0x010b;
    L_0x0108:
        r4.close();	 Catch:{ IOException -> 0x010b }
    L_0x010b:
        if (r3 == 0) goto L_0x0110;
    L_0x010d:
        r3.close();	 Catch:{ IOException -> 0x0110 }
    L_0x0110:
        return;
    L_0x0111:
        r7 = r13.get(r0);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r7 = (java.io.File) r7;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r7 = net.lingala.zip4j.util.Zip4jUtil.getFileLengh(r7);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r9 = 0;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        if (r11 != 0) goto L_0x0124;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
    L_0x0121:
        r5.setCompressionMethod(r2);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
    L_0x0124:
        r7 = r13.get(r0);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r7 = (java.io.File) r7;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r3.putNextEntry(r7, r5);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r5 = r13.get(r0);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r5 = (java.io.File) r5;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r5 = r5.isDirectory();	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        if (r5 == 0) goto L_0x013d;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
    L_0x0139:
        r3.closeEntry();	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        goto L_0x0158;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
    L_0x013d:
        r5 = new java.io.FileInputStream;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r7 = r13.get(r0);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r7 = (java.io.File) r7;	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
        r5.<init>(r7);	 Catch:{ ZipException -> 0x018b, Exception -> 0x0188, all -> 0x0186 }
    L_0x0148:
        r4 = r5.read(r1);	 Catch:{ ZipException -> 0x0182, Exception -> 0x017e, all -> 0x017b }
        r7 = -1;	 Catch:{ ZipException -> 0x0182, Exception -> 0x017e, all -> 0x017b }
        if (r4 != r7) goto L_0x015c;	 Catch:{ ZipException -> 0x0182, Exception -> 0x017e, all -> 0x017b }
    L_0x014f:
        r3.closeEntry();	 Catch:{ ZipException -> 0x0182, Exception -> 0x017e, all -> 0x017b }
        if (r5 == 0) goto L_0x0157;	 Catch:{ ZipException -> 0x0182, Exception -> 0x017e, all -> 0x017b }
    L_0x0154:
        r5.close();	 Catch:{ ZipException -> 0x0182, Exception -> 0x017e, all -> 0x017b }
    L_0x0157:
        r4 = r5;	 Catch:{ ZipException -> 0x0182, Exception -> 0x017e, all -> 0x017b }
    L_0x0158:
        r0 = r0 + 1;	 Catch:{ ZipException -> 0x0182, Exception -> 0x017e, all -> 0x017b }
        goto L_0x0088;	 Catch:{ ZipException -> 0x0182, Exception -> 0x017e, all -> 0x017b }
    L_0x015c:
        r7 = r15.isCancelAllTasks();	 Catch:{ ZipException -> 0x0182, Exception -> 0x017e, all -> 0x017b }
        if (r7 == 0) goto L_0x0173;	 Catch:{ ZipException -> 0x0182, Exception -> 0x017e, all -> 0x017b }
    L_0x0162:
        r15.setResult(r6);	 Catch:{ ZipException -> 0x0182, Exception -> 0x017e, all -> 0x017b }
        r15.setState(r2);	 Catch:{ ZipException -> 0x0182, Exception -> 0x017e, all -> 0x017b }
        if (r5 == 0) goto L_0x016d;
    L_0x016a:
        r5.close();	 Catch:{ IOException -> 0x016d }
    L_0x016d:
        if (r3 == 0) goto L_0x0172;
    L_0x016f:
        r3.close();	 Catch:{ IOException -> 0x0172 }
    L_0x0172:
        return;
    L_0x0173:
        r3.write(r1, r2, r4);	 Catch:{ ZipException -> 0x0182, Exception -> 0x017e, all -> 0x017b }
        r7 = (long) r4;	 Catch:{ ZipException -> 0x0182, Exception -> 0x017e, all -> 0x017b }
        r15.updateWorkCompleted(r7);	 Catch:{ ZipException -> 0x0182, Exception -> 0x017e, all -> 0x017b }
        goto L_0x0148;
    L_0x017b:
        r13 = move-exception;
        r4 = r5;
        goto L_0x01a5;
    L_0x017e:
        r13 = move-exception;
        r0 = r3;
        r4 = r5;
        goto L_0x0194;
    L_0x0182:
        r13 = move-exception;
        r0 = r3;
        r4 = r5;
        goto L_0x019f;
    L_0x0186:
        r13 = move-exception;
        goto L_0x01a5;
    L_0x0188:
        r13 = move-exception;
        goto L_0x0079;
    L_0x018b:
        r13 = move-exception;
        goto L_0x007e;
    L_0x018e:
        r13 = move-exception;
        r3 = r0;
        r4 = r3;
        goto L_0x01a5;
    L_0x0192:
        r13 = move-exception;
        r4 = r0;
    L_0x0194:
        r15.endProgressMonitorError(r13);	 Catch:{ all -> 0x01a3 }
        r14 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ all -> 0x01a3 }
        r14.<init>(r13);	 Catch:{ all -> 0x01a3 }
        throw r14;	 Catch:{ all -> 0x01a3 }
    L_0x019d:
        r13 = move-exception;	 Catch:{ all -> 0x01a3 }
        r4 = r0;	 Catch:{ all -> 0x01a3 }
    L_0x019f:
        r15.endProgressMonitorError(r13);	 Catch:{ all -> 0x01a3 }
        throw r13;	 Catch:{ all -> 0x01a3 }
    L_0x01a3:
        r13 = move-exception;
        r3 = r0;
    L_0x01a5:
        if (r4 == 0) goto L_0x01aa;
    L_0x01a7:
        r4.close();	 Catch:{ IOException -> 0x01aa }
    L_0x01aa:
        if (r3 == 0) goto L_0x01af;
    L_0x01ac:
        r3.close();	 Catch:{ IOException -> 0x01af }
    L_0x01af:
        throw r13;
    L_0x01b0:
        r13 = new net.lingala.zip4j.exception.ZipException;
        r14 = "one of the input parameters is null when adding files";
        r13.<init>(r14);
        throw r13;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.zip.ZipEngine.initAddFiles(java.util.ArrayList, net.lingala.zip4j.model.ZipParameters, net.lingala.zip4j.progress.ProgressMonitor):void");
    }

    public void addStreamToZip(java.io.InputStream r7, net.lingala.zip4j.model.ZipParameters r8) throws net.lingala.zip4j.exception.ZipException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r6 = this;
        if (r7 == 0) goto L_0x00a3;
    L_0x0002:
        if (r8 != 0) goto L_0x0006;
    L_0x0004:
        goto L_0x00a3;
    L_0x0006:
        r0 = 0;
        r6.checkParameters(r8);	 Catch:{ ZipException -> 0x009b, Exception -> 0x0094 }
        r1 = r6.zipModel;	 Catch:{ ZipException -> 0x009b, Exception -> 0x0094 }
        r1 = r1.getZipFile();	 Catch:{ ZipException -> 0x009b, Exception -> 0x0094 }
        r1 = net.lingala.zip4j.util.Zip4jUtil.checkFileExists(r1);	 Catch:{ ZipException -> 0x009b, Exception -> 0x0094 }
        r2 = new net.lingala.zip4j.io.SplitOutputStream;	 Catch:{ ZipException -> 0x009b, Exception -> 0x0094 }
        r3 = new java.io.File;	 Catch:{ ZipException -> 0x009b, Exception -> 0x0094 }
        r4 = r6.zipModel;	 Catch:{ ZipException -> 0x009b, Exception -> 0x0094 }
        r4 = r4.getZipFile();	 Catch:{ ZipException -> 0x009b, Exception -> 0x0094 }
        r3.<init>(r4);	 Catch:{ ZipException -> 0x009b, Exception -> 0x0094 }
        r4 = r6.zipModel;	 Catch:{ ZipException -> 0x009b, Exception -> 0x0094 }
        r4 = r4.getSplitLength();	 Catch:{ ZipException -> 0x009b, Exception -> 0x0094 }
        r2.<init>(r3, r4);	 Catch:{ ZipException -> 0x009b, Exception -> 0x0094 }
        r3 = new net.lingala.zip4j.io.ZipOutputStream;	 Catch:{ ZipException -> 0x009b, Exception -> 0x0094 }
        r4 = r6.zipModel;	 Catch:{ ZipException -> 0x009b, Exception -> 0x0094 }
        r3.<init>(r2, r4);	 Catch:{ ZipException -> 0x009b, Exception -> 0x0094 }
        if (r1 == 0) goto L_0x0059;
    L_0x0033:
        r1 = r6.zipModel;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        r1 = r1.getEndCentralDirRecord();	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        if (r1 != 0) goto L_0x0043;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
    L_0x003b:
        r7 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        r8 = "invalid end of central directory record";	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        r7.<init>(r8);	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        throw r7;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
    L_0x0043:
        r1 = r6.zipModel;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        r1 = r1.getEndCentralDirRecord();	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        r4 = r1.getOffsetOfStartOfCentralDir();	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        r2.seek(r4);	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        goto L_0x0059;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
    L_0x0051:
        r7 = move-exception;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        goto L_0x009d;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
    L_0x0053:
        r7 = move-exception;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        r0 = r3;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        goto L_0x0095;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
    L_0x0056:
        r7 = move-exception;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        r0 = r3;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        goto L_0x009c;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
    L_0x0059:
        r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        r1 = new byte[r1];	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        r3.putNextEntry(r0, r8);	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        r0 = r8.getFileNameInZip();	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        r2 = "/";	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        r0 = r0.endsWith(r2);	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        if (r0 != 0) goto L_0x0085;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
    L_0x006c:
        r8 = r8.getFileNameInZip();	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        r0 = "\\";	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        r8 = r8.endsWith(r0);	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        if (r8 != 0) goto L_0x0085;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
    L_0x0078:
        r8 = r7.read(r1);	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        r0 = -1;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        if (r8 != r0) goto L_0x0080;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
    L_0x007f:
        goto L_0x0085;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
    L_0x0080:
        r0 = 0;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        r3.write(r1, r0, r8);	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        goto L_0x0078;	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
    L_0x0085:
        r3.closeEntry();	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        r3.finish();	 Catch:{ ZipException -> 0x0056, Exception -> 0x0053, all -> 0x0051 }
        if (r3 == 0) goto L_0x0090;
    L_0x008d:
        r3.close();	 Catch:{ IOException -> 0x0090 }
    L_0x0090:
        return;
    L_0x0091:
        r7 = move-exception;
        r3 = r0;
        goto L_0x009d;
    L_0x0094:
        r7 = move-exception;
    L_0x0095:
        r8 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ all -> 0x0091 }
        r8.<init>(r7);	 Catch:{ all -> 0x0091 }
        throw r8;	 Catch:{ all -> 0x0091 }
    L_0x009b:
        r7 = move-exception;	 Catch:{ all -> 0x0091 }
    L_0x009c:
        throw r7;	 Catch:{ all -> 0x0091 }
    L_0x009d:
        if (r3 == 0) goto L_0x00a2;
    L_0x009f:
        r3.close();	 Catch:{ IOException -> 0x00a2 }
    L_0x00a2:
        throw r7;
    L_0x00a3:
        r7 = new net.lingala.zip4j.exception.ZipException;
        r8 = "one of the input parameters is null, cannot add stream to zip";
        r7.<init>(r8);
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.zip.ZipEngine.addStreamToZip(java.io.InputStream, net.lingala.zip4j.model.ZipParameters):void");
    }

    public void addFolderToZip(File file, ZipParameters zipParameters, ProgressMonitor progressMonitor, boolean z) throws ZipException {
        if (file != null) {
            if (zipParameters != null) {
                if (!Zip4jUtil.checkFileExists(file.getAbsolutePath())) {
                    throw new ZipException("input folder does not exist");
                } else if (!file.isDirectory()) {
                    throw new ZipException("input file is not a folder, user addFileToZip method to add files");
                } else if (Zip4jUtil.checkFileReadAccess(file.getAbsolutePath())) {
                    String absolutePath = zipParameters.isIncludeRootFolder() ? file.getAbsolutePath() != null ? file.getAbsoluteFile().getParentFile() != null ? file.getAbsoluteFile().getParentFile().getAbsolutePath() : "" : file.getParentFile() != null ? file.getParentFile().getAbsolutePath() : "" : file.getAbsolutePath();
                    zipParameters.setDefaultFolderPath(absolutePath);
                    ArrayList filesInDirectoryRec = Zip4jUtil.getFilesInDirectoryRec(file, zipParameters.isReadHiddenFiles());
                    if (zipParameters.isIncludeRootFolder()) {
                        if (filesInDirectoryRec == null) {
                            filesInDirectoryRec = new ArrayList();
                        }
                        filesInDirectoryRec.add(file);
                    }
                    addFiles(filesInDirectoryRec, zipParameters, progressMonitor, z);
                    return;
                } else {
                    progressMonitor = new StringBuffer("cannot read folder: ");
                    progressMonitor.append(file.getAbsolutePath());
                    throw new ZipException(progressMonitor.toString());
                }
            }
        }
        throw new ZipException("one of the input parameters is null, cannot add folder to zip");
    }

    private void checkParameters(ZipParameters zipParameters) throws ZipException {
        if (zipParameters == null) {
            throw new ZipException("cannot validate zip parameters");
        } else if (zipParameters.getCompressionMethod() != 0 && zipParameters.getCompressionMethod() != 8) {
            throw new ZipException("unsupported compression type");
        } else if (zipParameters.getCompressionMethod() == 8 && zipParameters.getCompressionLevel() < 0 && zipParameters.getCompressionLevel() > 9) {
            throw new ZipException("invalid compression level. compression level dor deflate should be in the range of 0-9");
        } else if (!zipParameters.isEncryptFiles()) {
            zipParameters.setAesKeyStrength(-1);
            zipParameters.setEncryptionMethod(-1);
        } else if (zipParameters.getEncryptionMethod() != 0 && zipParameters.getEncryptionMethod() != 99) {
            throw new ZipException("unsupported encryption method");
        } else if (zipParameters.getPassword() == null || zipParameters.getPassword().length <= null) {
            throw new ZipException("input password is empty or null");
        }
    }

    private void removeFilesIfExists(java.util.ArrayList r10, net.lingala.zip4j.model.ZipParameters r11, net.lingala.zip4j.progress.ProgressMonitor r12) throws net.lingala.zip4j.exception.ZipException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r9 = this;
        r0 = r9.zipModel;
        if (r0 == 0) goto L_0x00d3;
    L_0x0004:
        r0 = r9.zipModel;
        r0 = r0.getCentralDirectory();
        if (r0 == 0) goto L_0x00d3;
    L_0x000c:
        r0 = r9.zipModel;
        r0 = r0.getCentralDirectory();
        r0 = r0.getFileHeaders();
        if (r0 == 0) goto L_0x00d3;
    L_0x0018:
        r0 = r9.zipModel;
        r0 = r0.getCentralDirectory();
        r0 = r0.getFileHeaders();
        r0 = r0.size();
        if (r0 > 0) goto L_0x002a;
    L_0x0028:
        goto L_0x00d3;
    L_0x002a:
        r0 = 0;
        r1 = 0;
        r3 = r0;
        r2 = 0;
    L_0x002e:
        r4 = r10.size();	 Catch:{ IOException -> 0x00c6 }
        if (r2 < r4) goto L_0x003a;
    L_0x0034:
        if (r3 == 0) goto L_0x0039;
    L_0x0036:
        r3.close();	 Catch:{ IOException -> 0x0039 }
    L_0x0039:
        return;
    L_0x003a:
        r4 = r10.get(r2);	 Catch:{ IOException -> 0x00c6 }
        r4 = (java.io.File) r4;	 Catch:{ IOException -> 0x00c6 }
        r4 = r4.getAbsolutePath();	 Catch:{ IOException -> 0x00c6 }
        r5 = r11.getRootFolderInZip();	 Catch:{ IOException -> 0x00c6 }
        r6 = r11.getDefaultFolderPath();	 Catch:{ IOException -> 0x00c6 }
        r4 = net.lingala.zip4j.util.Zip4jUtil.getRelativeFileName(r4, r5, r6);	 Catch:{ IOException -> 0x00c6 }
        r5 = r9.zipModel;	 Catch:{ IOException -> 0x00c6 }
        r4 = net.lingala.zip4j.util.Zip4jUtil.getFileHeader(r5, r4);	 Catch:{ IOException -> 0x00c6 }
        if (r4 == 0) goto L_0x00c0;	 Catch:{ IOException -> 0x00c6 }
    L_0x0058:
        if (r3 == 0) goto L_0x005e;	 Catch:{ IOException -> 0x00c6 }
    L_0x005a:
        r3.close();	 Catch:{ IOException -> 0x00c6 }
        r3 = r0;	 Catch:{ IOException -> 0x00c6 }
    L_0x005e:
        r5 = new net.lingala.zip4j.util.ArchiveMaintainer;	 Catch:{ IOException -> 0x00c6 }
        r5.<init>();	 Catch:{ IOException -> 0x00c6 }
        r6 = 2;	 Catch:{ IOException -> 0x00c6 }
        r12.setCurrentOperation(r6);	 Catch:{ IOException -> 0x00c6 }
        r6 = r9.zipModel;	 Catch:{ IOException -> 0x00c6 }
        r4 = r5.initRemoveZipFile(r6, r4, r12);	 Catch:{ IOException -> 0x00c6 }
        r5 = r12.isCancelAllTasks();	 Catch:{ IOException -> 0x00c6 }
        if (r5 == 0) goto L_0x0080;	 Catch:{ IOException -> 0x00c6 }
    L_0x0073:
        r10 = 3;	 Catch:{ IOException -> 0x00c6 }
        r12.setResult(r10);	 Catch:{ IOException -> 0x00c6 }
        r12.setState(r1);	 Catch:{ IOException -> 0x00c6 }
        if (r3 == 0) goto L_0x007f;
    L_0x007c:
        r3.close();	 Catch:{ IOException -> 0x007f }
    L_0x007f:
        return;
    L_0x0080:
        r12.setCurrentOperation(r1);	 Catch:{ IOException -> 0x00c6 }
        if (r3 != 0) goto L_0x00c0;	 Catch:{ IOException -> 0x00c6 }
    L_0x0085:
        r5 = r9.prepareFileOutputStream();	 Catch:{ IOException -> 0x00c6 }
        if (r4 == 0) goto L_0x00bf;
    L_0x008b:
        r3 = "offsetCentralDir";	 Catch:{ IOException -> 0x00bc, all -> 0x00b9 }
        r3 = r4.get(r3);	 Catch:{ IOException -> 0x00bc, all -> 0x00b9 }
        if (r3 == 0) goto L_0x00bf;
    L_0x0093:
        r3 = "offsetCentralDir";	 Catch:{ NumberFormatException -> 0x00b1, Exception -> 0x00a9 }
        r3 = r4.get(r3);	 Catch:{ NumberFormatException -> 0x00b1, Exception -> 0x00a9 }
        r3 = (java.lang.String) r3;	 Catch:{ NumberFormatException -> 0x00b1, Exception -> 0x00a9 }
        r3 = java.lang.Long.parseLong(r3);	 Catch:{ NumberFormatException -> 0x00b1, Exception -> 0x00a9 }
        r6 = 0;
        r8 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1));
        if (r8 < 0) goto L_0x00bf;
    L_0x00a5:
        r5.seek(r3);	 Catch:{ IOException -> 0x00bc, all -> 0x00b9 }
        goto L_0x00bf;	 Catch:{ IOException -> 0x00bc, all -> 0x00b9 }
    L_0x00a9:
        r10 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ IOException -> 0x00bc, all -> 0x00b9 }
        r11 = "Error while parsing offset central directory. Cannot update already existing file header";	 Catch:{ IOException -> 0x00bc, all -> 0x00b9 }
        r10.<init>(r11);	 Catch:{ IOException -> 0x00bc, all -> 0x00b9 }
        throw r10;	 Catch:{ IOException -> 0x00bc, all -> 0x00b9 }
    L_0x00b1:
        r10 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ IOException -> 0x00bc, all -> 0x00b9 }
        r11 = "NumberFormatException while parsing offset central directory. Cannot update already existing file header";	 Catch:{ IOException -> 0x00bc, all -> 0x00b9 }
        r10.<init>(r11);	 Catch:{ IOException -> 0x00bc, all -> 0x00b9 }
        throw r10;	 Catch:{ IOException -> 0x00bc, all -> 0x00b9 }
    L_0x00b9:
        r10 = move-exception;
        r3 = r5;
        goto L_0x00cd;
    L_0x00bc:
        r10 = move-exception;
        r3 = r5;
        goto L_0x00c7;
    L_0x00bf:
        r3 = r5;
    L_0x00c0:
        r2 = r2 + 1;
        goto L_0x002e;
    L_0x00c4:
        r10 = move-exception;
        goto L_0x00cd;
    L_0x00c6:
        r10 = move-exception;
    L_0x00c7:
        r11 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ all -> 0x00c4 }
        r11.<init>(r10);	 Catch:{ all -> 0x00c4 }
        throw r11;	 Catch:{ all -> 0x00c4 }
    L_0x00cd:
        if (r3 == 0) goto L_0x00d2;
    L_0x00cf:
        r3.close();	 Catch:{ IOException -> 0x00d2 }
    L_0x00d2:
        throw r10;
    L_0x00d3:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.zip.ZipEngine.removeFilesIfExists(java.util.ArrayList, net.lingala.zip4j.model.ZipParameters, net.lingala.zip4j.progress.ProgressMonitor):void");
    }

    private RandomAccessFile prepareFileOutputStream() throws ZipException {
        String zipFile = this.zipModel.getZipFile();
        if (Zip4jUtil.isStringNotNullAndNotEmpty(zipFile)) {
            try {
                File file = new File(zipFile);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                return new RandomAccessFile(file, InternalZipConstants.WRITE_MODE);
            } catch (Throwable e) {
                throw new ZipException(e);
            }
        }
        throw new ZipException("invalid output path");
    }

    private EndCentralDirRecord createEndOfCentralDirectoryRecord() {
        EndCentralDirRecord endCentralDirRecord = new EndCentralDirRecord();
        endCentralDirRecord.setSignature(InternalZipConstants.ENDSIG);
        endCentralDirRecord.setNoOfThisDisk(0);
        endCentralDirRecord.setTotNoOfEntriesInCentralDir(0);
        endCentralDirRecord.setTotNoOfEntriesInCentralDirOnThisDisk(0);
        endCentralDirRecord.setOffsetOfStartOfCentralDir(0);
        return endCentralDirRecord;
    }

    private long calculateTotalWork(ArrayList arrayList, ZipParameters zipParameters) throws ZipException {
        if (arrayList == null) {
            throw new ZipException("file list is null, cannot calculate total work");
        }
        long j = 0;
        int i = 0;
        while (i < arrayList.size()) {
            if ((arrayList.get(i) instanceof File) && ((File) arrayList.get(i)).exists()) {
                long fileLengh;
                if (zipParameters.isEncryptFiles() && zipParameters.getEncryptionMethod() == 0) {
                    fileLengh = j + (Zip4jUtil.getFileLengh((File) arrayList.get(i)) * 2);
                } else {
                    fileLengh = j + Zip4jUtil.getFileLengh((File) arrayList.get(i));
                }
                if (!(this.zipModel.getCentralDirectory() == null || this.zipModel.getCentralDirectory().getFileHeaders() == null || this.zipModel.getCentralDirectory().getFileHeaders().size() <= 0)) {
                    FileHeader fileHeader = Zip4jUtil.getFileHeader(this.zipModel, Zip4jUtil.getRelativeFileName(((File) arrayList.get(i)).getAbsolutePath(), zipParameters.getRootFolderInZip(), zipParameters.getDefaultFolderPath()));
                    if (fileHeader != null) {
                        j = fileLengh + (Zip4jUtil.getFileLengh(new File(this.zipModel.getZipFile())) - fileHeader.getCompressedSize());
                    }
                }
                j = fileLengh;
            }
            i++;
        }
        return j;
    }
}
