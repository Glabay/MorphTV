package net.lingala.zip4j.util;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.progress.ProgressMonitor;

public class ArchiveMaintainer {
    public HashMap removeZipFile(ZipModel zipModel, FileHeader fileHeader, ProgressMonitor progressMonitor, boolean z) throws ZipException {
        if (z) {
            final ZipModel zipModel2 = zipModel;
            final FileHeader fileHeader2 = fileHeader;
            final ProgressMonitor progressMonitor2 = progressMonitor;
            new Thread(this, InternalZipConstants.THREAD_NAME) {
                final /* synthetic */ ArchiveMaintainer this$0;

                public void run() {
                    /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
                    /*
                    r4 = this;
                    r0 = r4.this$0;	 Catch:{ ZipException -> 0x0010 }
                    r1 = r3;	 Catch:{ ZipException -> 0x0010 }
                    r2 = r4;	 Catch:{ ZipException -> 0x0010 }
                    r3 = r5;	 Catch:{ ZipException -> 0x0010 }
                    r0.initRemoveZipFile(r1, r2, r3);	 Catch:{ ZipException -> 0x0010 }
                    r0 = r5;	 Catch:{ ZipException -> 0x0010 }
                    r0.endProgressMonitorSuccess();	 Catch:{ ZipException -> 0x0010 }
                L_0x0010:
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.util.ArchiveMaintainer.1.run():void");
                }
            }.start();
            return null;
        }
        zipModel = initRemoveZipFile(zipModel, fileHeader, progressMonitor);
        progressMonitor.endProgressMonitorSuccess();
        return zipModel;
    }

    public java.util.HashMap initRemoveZipFile(net.lingala.zip4j.model.ZipModel r35, net.lingala.zip4j.model.FileHeader r36, net.lingala.zip4j.progress.ProgressMonitor r37) throws net.lingala.zip4j.exception.ZipException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r34 = this;
        r9 = r34;
        r10 = r35;
        r1 = r36;
        r11 = r37;
        if (r1 == 0) goto L_0x04bc;
    L_0x000a:
        if (r10 != 0) goto L_0x000e;
    L_0x000c:
        goto L_0x04bc;
    L_0x000e:
        r12 = new java.util.HashMap;
        r12.<init>();
        r15 = 1;
        r8 = net.lingala.zip4j.util.Zip4jUtil.getIndexOfFileHeader(r35, r36);	 Catch:{ ZipException -> 0x0489, Exception -> 0x0479, all -> 0x0471 }
        if (r8 >= 0) goto L_0x003d;
    L_0x001a:
        r1 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ ZipException -> 0x0034, Exception -> 0x002b, all -> 0x0022 }
        r2 = "file header not found in zip model, cannot remove file";	 Catch:{ ZipException -> 0x0034, Exception -> 0x002b, all -> 0x0022 }
        r1.<init>(r2);	 Catch:{ ZipException -> 0x0034, Exception -> 0x002b, all -> 0x0022 }
        throw r1;	 Catch:{ ZipException -> 0x0034, Exception -> 0x002b, all -> 0x0022 }
    L_0x0022:
        r0 = move-exception;
        r1 = r0;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r7 = 0;
        r14 = 0;
        goto L_0x049a;
    L_0x002b:
        r0 = move-exception;
        r1 = r0;
        r4 = 0;
        r5 = 0;
        r7 = 0;
        r13 = 0;
        r14 = 0;
        goto L_0x0480;
    L_0x0034:
        r0 = move-exception;
        r1 = r0;
        r4 = 0;
        r5 = 0;
        r7 = 0;
        r13 = 0;
        r14 = 0;
        goto L_0x0490;
    L_0x003d:
        r2 = r35.isSplitArchive();	 Catch:{ ZipException -> 0x0489, Exception -> 0x0479, all -> 0x0471 }
        if (r2 == 0) goto L_0x004b;
    L_0x0043:
        r1 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ ZipException -> 0x0034, Exception -> 0x002b, all -> 0x0022 }
        r2 = "This is a split archive. Zip file format does not allow updating split/spanned files";	 Catch:{ ZipException -> 0x0034, Exception -> 0x002b, all -> 0x0022 }
        r1.<init>(r2);	 Catch:{ ZipException -> 0x0034, Exception -> 0x002b, all -> 0x0022 }
        throw r1;	 Catch:{ ZipException -> 0x0034, Exception -> 0x002b, all -> 0x0022 }
    L_0x004b:
        r2 = java.lang.System.currentTimeMillis();	 Catch:{ ZipException -> 0x0489, Exception -> 0x0479, all -> 0x0471 }
        r4 = new java.lang.StringBuffer;	 Catch:{ ZipException -> 0x0489, Exception -> 0x0479, all -> 0x0471 }
        r5 = r35.getZipFile();	 Catch:{ ZipException -> 0x0489, Exception -> 0x0479, all -> 0x0471 }
        r5 = java.lang.String.valueOf(r5);	 Catch:{ ZipException -> 0x0489, Exception -> 0x0479, all -> 0x0471 }
        r4.<init>(r5);	 Catch:{ ZipException -> 0x0489, Exception -> 0x0479, all -> 0x0471 }
        r5 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;	 Catch:{ ZipException -> 0x0489, Exception -> 0x0479, all -> 0x0471 }
        r2 = r2 % r5;	 Catch:{ ZipException -> 0x0489, Exception -> 0x0479, all -> 0x0471 }
        r4.append(r2);	 Catch:{ ZipException -> 0x0489, Exception -> 0x0479, all -> 0x0471 }
        r2 = r4.toString();	 Catch:{ ZipException -> 0x0489, Exception -> 0x0479, all -> 0x0471 }
        r3 = new java.io.File;	 Catch:{ ZipException -> 0x046b, Exception -> 0x0465, all -> 0x045e }
        r3.<init>(r2);	 Catch:{ ZipException -> 0x046b, Exception -> 0x0465, all -> 0x045e }
        r7 = r2;
    L_0x006c:
        r2 = r3.exists();	 Catch:{ ZipException -> 0x0454, Exception -> 0x044a, all -> 0x0440 }
        if (r2 != 0) goto L_0x03fd;
    L_0x0072:
        r6 = new net.lingala.zip4j.io.SplitOutputStream;	 Catch:{ FileNotFoundException -> 0x03ec, ZipException -> 0x03e5, Exception -> 0x03de, all -> 0x03d5 }
        r2 = new java.io.File;	 Catch:{ FileNotFoundException -> 0x03ec, ZipException -> 0x03e5, Exception -> 0x03de, all -> 0x03d5 }
        r2.<init>(r7);	 Catch:{ FileNotFoundException -> 0x03ec, ZipException -> 0x03e5, Exception -> 0x03de, all -> 0x03d5 }
        r6.<init>(r2);	 Catch:{ FileNotFoundException -> 0x03ec, ZipException -> 0x03e5, Exception -> 0x03de, all -> 0x03d5 }
        r4 = new java.io.File;	 Catch:{ ZipException -> 0x03cb, Exception -> 0x03c1, all -> 0x03b8 }
        r2 = r35.getZipFile();	 Catch:{ ZipException -> 0x03cb, Exception -> 0x03c1, all -> 0x03b8 }
        r4.<init>(r2);	 Catch:{ ZipException -> 0x03cb, Exception -> 0x03c1, all -> 0x03b8 }
        r2 = "r";	 Catch:{ ZipException -> 0x03ab, Exception -> 0x039e, all -> 0x0395 }
        r5 = r9.createFileHandler(r10, r2);	 Catch:{ ZipException -> 0x03ab, Exception -> 0x039e, all -> 0x0395 }
        r2 = new net.lingala.zip4j.core.HeaderReader;	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
        r2.<init>(r5);	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
        r2 = r2.readLocalFileHeader(r1);	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
        if (r2 != 0) goto L_0x00bc;
    L_0x0096:
        r1 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        r2 = "invalid local file header, cannot remove file from archive";	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        r1.<init>(r2);	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        throw r1;	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
    L_0x009e:
        r0 = move-exception;
        r1 = r0;
        r14 = r4;
        r2 = r6;
        r4 = r7;
        r3 = 0;
        goto L_0x0499;
    L_0x00a6:
        r0 = move-exception;
        r1 = r0;
        r14 = r6;
        r13 = 0;
        r33 = r7;
        r7 = r4;
        r4 = r33;
        goto L_0x0480;
    L_0x00b1:
        r0 = move-exception;
        r1 = r0;
        r14 = r6;
        r13 = 0;
        r33 = r7;
        r7 = r4;
        r4 = r33;
        goto L_0x0490;
    L_0x00bc:
        r2 = r36.getOffsetLocalHeader();	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
        r16 = r36.getZip64ExtendedInfo();	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
        r17 = -1;
        if (r16 == 0) goto L_0x00df;
    L_0x00c8:
        r14 = r36.getZip64ExtendedInfo();	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        r19 = r14.getOffsetLocalHeader();	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        r14 = (r19 > r17 ? 1 : (r19 == r17 ? 0 : -1));	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        if (r14 == 0) goto L_0x00df;	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
    L_0x00d4:
        r1 = r36.getZip64ExtendedInfo();	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        r1 = r1.getOffsetLocalHeader();	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        r19 = r1;
        goto L_0x00e1;
    L_0x00df:
        r19 = r2;
    L_0x00e1:
        r1 = r35.getEndCentralDirRecord();	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
        r1 = r1.getOffsetOfStartOfCentralDir();	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
        r3 = r35.isZip64Format();	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
        if (r3 == 0) goto L_0x00fd;
    L_0x00ef:
        r3 = r35.getZip64EndCentralDirRecord();	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        if (r3 == 0) goto L_0x00fd;	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
    L_0x00f5:
        r1 = r35.getZip64EndCentralDirRecord();	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        r1 = r1.getOffsetStartCenDirWRTStartDiskNo();	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
    L_0x00fd:
        r21 = r1;
        r1 = r35.getCentralDirectory();	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
        r1 = r1.getFileHeaders();	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
        r2 = r1.size();	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
        r2 = r2 - r15;	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
        r23 = 1;	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
        if (r8 != r2) goto L_0x0115;	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
    L_0x0110:
        r2 = r21 - r23;	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
        r25 = r2;	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
        goto L_0x0149;	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
    L_0x0115:
        r2 = r8 + 1;	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
        r2 = r1.get(r2);	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
        r2 = (net.lingala.zip4j.model.FileHeader) r2;	 Catch:{ ZipException -> 0x038b, Exception -> 0x0381, all -> 0x0379 }
        if (r2 == 0) goto L_0x0147;
    L_0x011f:
        r25 = r2.getOffsetLocalHeader();	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        r3 = 0;	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        r27 = r25 - r23;	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        r3 = r2.getZip64ExtendedInfo();	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        if (r3 == 0) goto L_0x0144;	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
    L_0x012c:
        r3 = r2.getZip64ExtendedInfo();	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        r25 = r3.getOffsetLocalHeader();	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        r3 = (r25 > r17 ? 1 : (r25 == r17 ? 0 : -1));	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        if (r3 == 0) goto L_0x0144;	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
    L_0x0138:
        r2 = r2.getZip64ExtendedInfo();	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        r2 = r2.getOffsetLocalHeader();	 Catch:{ ZipException -> 0x00b1, Exception -> 0x00a6, all -> 0x009e }
        r14 = 0;
        r25 = r2 - r23;
        goto L_0x0149;
    L_0x0144:
        r25 = r27;
        goto L_0x0149;
    L_0x0147:
        r25 = r17;
    L_0x0149:
        r2 = 0;
        r14 = (r19 > r2 ? 1 : (r19 == r2 ? 0 : -1));
        if (r14 < 0) goto L_0x0364;
    L_0x014f:
        r14 = (r25 > r2 ? 1 : (r25 == r2 ? 0 : -1));
        if (r14 >= 0) goto L_0x0155;
    L_0x0153:
        goto L_0x0364;
    L_0x0155:
        if (r8 != 0) goto L_0x01b7;
    L_0x0157:
        r1 = r35.getCentralDirectory();	 Catch:{ ZipException -> 0x01a7, Exception -> 0x0197, all -> 0x0186 }
        r1 = r1.getFileHeaders();	 Catch:{ ZipException -> 0x01a7, Exception -> 0x0197, all -> 0x0186 }
        r1 = r1.size();	 Catch:{ ZipException -> 0x01a7, Exception -> 0x0197, all -> 0x0186 }
        if (r1 <= r15) goto L_0x017c;
    L_0x0165:
        r27 = r25 + r23;
        r1 = r9;
        r2 = r5;
        r3 = r6;
        r14 = r4;
        r29 = r5;
        r4 = r27;
        r30 = r6;
        r31 = r7;
        r6 = r21;
        r13 = r8;
        r8 = r11;
        r1.copyFile(r2, r3, r4, r6, r8);	 Catch:{ ZipException -> 0x01dc, Exception -> 0x01d6, all -> 0x01d4 }
        goto L_0x01fd;
    L_0x017c:
        r14 = r4;
        r29 = r5;
        r30 = r6;
        r31 = r7;
        r13 = r8;
        goto L_0x01fd;
    L_0x0186:
        r0 = move-exception;
        r14 = r4;
        r29 = r5;
        r30 = r6;
        r31 = r7;
    L_0x018e:
        r1 = r0;
        r7 = r29;
        r2 = r30;
        r4 = r31;
        goto L_0x0477;
    L_0x0197:
        r0 = move-exception;
        r14 = r4;
        r29 = r5;
        r30 = r6;
        r31 = r7;
        r1 = r0;
        r7 = r14;
    L_0x01a1:
        r14 = r30;
        r4 = r31;
        goto L_0x047f;
    L_0x01a7:
        r0 = move-exception;
        r14 = r4;
        r29 = r5;
        r30 = r6;
        r31 = r7;
        r1 = r0;
        r7 = r14;
    L_0x01b1:
        r14 = r30;
        r4 = r31;
        goto L_0x048f;
    L_0x01b7:
        r14 = r4;
        r29 = r5;
        r30 = r6;
        r31 = r7;
        r13 = r8;
        r1 = r1.size();	 Catch:{ ZipException -> 0x035b, Exception -> 0x0352, all -> 0x034a }
        r1 = r1 - r15;
        if (r13 != r1) goto L_0x01e2;
    L_0x01c6:
        r4 = 0;
        r1 = r9;
        r2 = r29;
        r3 = r30;
        r6 = r19;
        r8 = r11;
        r1.copyFile(r2, r3, r4, r6, r8);	 Catch:{ ZipException -> 0x01dc, Exception -> 0x01d6, all -> 0x01d4 }
        goto L_0x01fd;
    L_0x01d4:
        r0 = move-exception;
        goto L_0x018e;
    L_0x01d6:
        r0 = move-exception;
        r1 = r0;
        r7 = r14;
        r5 = r29;
        goto L_0x01a1;
    L_0x01dc:
        r0 = move-exception;
        r1 = r0;
        r7 = r14;
        r5 = r29;
        goto L_0x01b1;
    L_0x01e2:
        r4 = 0;
        r1 = r9;
        r2 = r29;
        r3 = r30;
        r6 = r19;
        r8 = r11;
        r1.copyFile(r2, r3, r4, r6, r8);	 Catch:{ ZipException -> 0x035b, Exception -> 0x0352, all -> 0x034a }
        r1 = 0;	 Catch:{ ZipException -> 0x035b, Exception -> 0x0352, all -> 0x034a }
        r4 = r25 + r23;	 Catch:{ ZipException -> 0x035b, Exception -> 0x0352, all -> 0x034a }
        r1 = r9;	 Catch:{ ZipException -> 0x035b, Exception -> 0x0352, all -> 0x034a }
        r2 = r29;	 Catch:{ ZipException -> 0x035b, Exception -> 0x0352, all -> 0x034a }
        r3 = r30;	 Catch:{ ZipException -> 0x035b, Exception -> 0x0352, all -> 0x034a }
        r6 = r21;	 Catch:{ ZipException -> 0x035b, Exception -> 0x0352, all -> 0x034a }
        r8 = r11;	 Catch:{ ZipException -> 0x035b, Exception -> 0x0352, all -> 0x034a }
        r1.copyFile(r2, r3, r4, r6, r8);	 Catch:{ ZipException -> 0x035b, Exception -> 0x0352, all -> 0x034a }
    L_0x01fd:
        r1 = r37.isCancelAllTasks();	 Catch:{ ZipException -> 0x035b, Exception -> 0x0352, all -> 0x034a }
        if (r1 == 0) goto L_0x0249;	 Catch:{ ZipException -> 0x035b, Exception -> 0x0352, all -> 0x034a }
    L_0x0203:
        r1 = 3;	 Catch:{ ZipException -> 0x035b, Exception -> 0x0352, all -> 0x034a }
        r11.setResult(r1);	 Catch:{ ZipException -> 0x035b, Exception -> 0x0352, all -> 0x034a }
        r3 = 0;
        r11.setState(r3);	 Catch:{ ZipException -> 0x0240, Exception -> 0x0237, all -> 0x022e }
        r1 = r29;
        if (r1 == 0) goto L_0x0212;
    L_0x020f:
        r1.close();	 Catch:{ IOException -> 0x021a }
    L_0x0212:
        r2 = r30;	 Catch:{ IOException -> 0x021a }
        if (r2 == 0) goto L_0x0222;	 Catch:{ IOException -> 0x021a }
    L_0x0216:
        r2.close();	 Catch:{ IOException -> 0x021a }
        goto L_0x0222;
    L_0x021a:
        r1 = new net.lingala.zip4j.exception.ZipException;
        r2 = "cannot close input stream or output stream when trying to delete a file from zip file";
        r1.<init>(r2);
        throw r1;
    L_0x0222:
        r1 = new java.io.File;
        r4 = r31;
        r1.<init>(r4);
        r1.delete();
        r7 = 0;
        return r7;
    L_0x022e:
        r0 = move-exception;
        r1 = r29;
        r2 = r30;
        r4 = r31;
        goto L_0x037f;
    L_0x0237:
        r0 = move-exception;
        r1 = r29;
        r2 = r30;
        r4 = r31;
        goto L_0x0374;
    L_0x0240:
        r0 = move-exception;
        r1 = r29;
        r2 = r30;
        r4 = r31;
        goto L_0x0377;
    L_0x0249:
        r1 = r29;
        r2 = r30;
        r4 = r31;
        r3 = 0;
        r5 = r35.getEndCentralDirRecord();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r6 = r2;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r6 = (net.lingala.zip4j.io.SplitOutputStream) r6;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r6 = r6.getFilePointer();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5.setOffsetOfStartOfCentralDir(r6);	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5 = r35.getEndCentralDirRecord();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r6 = r35.getEndCentralDirRecord();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r6 = r6.getTotNoOfEntriesInCentralDir();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r6 = r6 - r15;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5.setTotNoOfEntriesInCentralDir(r6);	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5 = r35.getEndCentralDirRecord();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r6 = r35.getEndCentralDirRecord();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r6 = r6.getTotNoOfEntriesInCentralDirOnThisDisk();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r6 = r6 - r15;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5.setTotNoOfEntriesInCentralDirOnThisDisk(r6);	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5 = r35.getCentralDirectory();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5 = r5.getFileHeaders();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5.remove(r13);	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
    L_0x0289:
        r5 = r35.getCentralDirectory();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5 = r5.getFileHeaders();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5 = r5.size();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        if (r13 < r5) goto L_0x02d8;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
    L_0x0297:
        r5 = new net.lingala.zip4j.core.HeaderWriter;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5.<init>();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5.finalizeZipFile(r10, r2);	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r3 = "offsetCentralDir";	 Catch:{ ZipException -> 0x02d2, Exception -> 0x02cc, all -> 0x02c7 }
        r5 = r35.getEndCentralDirRecord();	 Catch:{ ZipException -> 0x02d2, Exception -> 0x02cc, all -> 0x02c7 }
        r5 = r5.getOffsetOfStartOfCentralDir();	 Catch:{ ZipException -> 0x02d2, Exception -> 0x02cc, all -> 0x02c7 }
        r5 = java.lang.Long.toString(r5);	 Catch:{ ZipException -> 0x02d2, Exception -> 0x02cc, all -> 0x02c7 }
        r12.put(r3, r5);	 Catch:{ ZipException -> 0x02d2, Exception -> 0x02cc, all -> 0x02c7 }
        if (r1 == 0) goto L_0x02b5;
    L_0x02b2:
        r1.close();	 Catch:{ IOException -> 0x02bb }
    L_0x02b5:
        if (r2 == 0) goto L_0x02c3;	 Catch:{ IOException -> 0x02bb }
    L_0x02b7:
        r2.close();	 Catch:{ IOException -> 0x02bb }
        goto L_0x02c3;
    L_0x02bb:
        r1 = new net.lingala.zip4j.exception.ZipException;
        r2 = "cannot close input stream or output stream when trying to delete a file from zip file";
        r1.<init>(r2);
        throw r1;
    L_0x02c3:
        r9.restoreFileName(r14, r4);
        return r12;
    L_0x02c7:
        r0 = move-exception;
        r7 = r1;
        r3 = 1;
        goto L_0x039b;
    L_0x02cc:
        r0 = move-exception;
        r5 = r1;
        r7 = r14;
        r13 = 1;
        goto L_0x0389;
    L_0x02d2:
        r0 = move-exception;
        r5 = r1;
        r7 = r14;
        r13 = 1;
        goto L_0x0393;
    L_0x02d8:
        r5 = r35.getCentralDirectory();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5 = r5.getFileHeaders();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5 = r5.get(r13);	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5 = (net.lingala.zip4j.model.FileHeader) r5;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5 = r5.getOffsetLocalHeader();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r7 = r35.getCentralDirectory();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r7 = r7.getFileHeaders();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r7 = r7.get(r13);	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r7 = (net.lingala.zip4j.model.FileHeader) r7;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r7 = r7.getZip64ExtendedInfo();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        if (r7 == 0) goto L_0x032e;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
    L_0x02fe:
        r7 = r35.getCentralDirectory();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r7 = r7.getFileHeaders();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r7 = r7.get(r13);	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r7 = (net.lingala.zip4j.model.FileHeader) r7;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r7 = r7.getZip64ExtendedInfo();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r7 = r7.getOffsetLocalHeader();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r16 = (r7 > r17 ? 1 : (r7 == r17 ? 0 : -1));	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        if (r16 == 0) goto L_0x032e;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
    L_0x0318:
        r5 = r35.getCentralDirectory();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5 = r5.getFileHeaders();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5 = r5.get(r13);	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5 = (net.lingala.zip4j.model.FileHeader) r5;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5 = r5.getZip64ExtendedInfo();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5 = r5.getOffsetLocalHeader();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
    L_0x032e:
        r7 = r35.getCentralDirectory();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r7 = r7.getFileHeaders();	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r7 = r7.get(r13);	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r7 = (net.lingala.zip4j.model.FileHeader) r7;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r8 = 0;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r21 = r25 - r19;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r27 = r5 - r21;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5 = r27 - r23;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r7.setOffsetLocalHeader(r5);	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r13 = r13 + 1;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        goto L_0x0289;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
    L_0x034a:
        r0 = move-exception;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r1 = r29;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r2 = r30;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r4 = r31;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        goto L_0x037e;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
    L_0x0352:
        r0 = move-exception;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r1 = r29;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r2 = r30;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r4 = r31;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r3 = 0;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        goto L_0x0374;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
    L_0x035b:
        r0 = move-exception;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r1 = r29;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r2 = r30;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r4 = r31;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r3 = 0;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        goto L_0x0377;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
    L_0x0364:
        r14 = r4;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r1 = r5;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r2 = r6;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r4 = r7;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r3 = 0;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r6 = "invalid offset for start and end of local file, cannot remove file";	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        r5.<init>(r6);	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
        throw r5;	 Catch:{ ZipException -> 0x0376, Exception -> 0x0373, all -> 0x0371 }
    L_0x0371:
        r0 = move-exception;
        goto L_0x037f;
    L_0x0373:
        r0 = move-exception;
    L_0x0374:
        r5 = r1;
        goto L_0x0387;
    L_0x0376:
        r0 = move-exception;
    L_0x0377:
        r5 = r1;
        goto L_0x0391;
    L_0x0379:
        r0 = move-exception;
        r14 = r4;
        r1 = r5;
        r2 = r6;
        r4 = r7;
    L_0x037e:
        r3 = 0;
    L_0x037f:
        r7 = r1;
        goto L_0x039b;
    L_0x0381:
        r0 = move-exception;
        r14 = r4;
        r1 = r5;
        r2 = r6;
        r4 = r7;
        r3 = 0;
    L_0x0387:
        r7 = r14;
        r13 = 0;
    L_0x0389:
        r1 = r0;
        goto L_0x03a8;
    L_0x038b:
        r0 = move-exception;
        r14 = r4;
        r1 = r5;
        r2 = r6;
        r4 = r7;
        r3 = 0;
    L_0x0391:
        r7 = r14;
        r13 = 0;
    L_0x0393:
        r1 = r0;
        goto L_0x03b5;
    L_0x0395:
        r0 = move-exception;
        r14 = r4;
        r2 = r6;
        r4 = r7;
        r3 = 0;
        r7 = 0;
    L_0x039b:
        r1 = r0;
        goto L_0x049a;
    L_0x039e:
        r0 = move-exception;
        r14 = r4;
        r2 = r6;
        r4 = r7;
        r3 = 0;
        r7 = 0;
        r1 = r0;
        r5 = r7;
        r7 = r14;
        r13 = 0;
    L_0x03a8:
        r14 = r2;
        goto L_0x0480;
    L_0x03ab:
        r0 = move-exception;
        r14 = r4;
        r2 = r6;
        r4 = r7;
        r3 = 0;
        r7 = 0;
        r1 = r0;
        r5 = r7;
        r7 = r14;
        r13 = 0;
    L_0x03b5:
        r14 = r2;
        goto L_0x0490;
    L_0x03b8:
        r0 = move-exception;
        r2 = r6;
        r4 = r7;
        r3 = 0;
        r7 = 0;
        r1 = r0;
        r14 = r7;
        goto L_0x049a;
    L_0x03c1:
        r0 = move-exception;
        r2 = r6;
        r4 = r7;
        r3 = 0;
        r7 = 0;
        r1 = r0;
        r14 = r2;
        r5 = r7;
        goto L_0x047f;
    L_0x03cb:
        r0 = move-exception;
        r2 = r6;
        r4 = r7;
        r3 = 0;
        r7 = 0;
        r1 = r0;
        r14 = r2;
        r5 = r7;
        goto L_0x048f;
    L_0x03d5:
        r0 = move-exception;
        r4 = r7;
        r3 = 0;
        r7 = 0;
    L_0x03d9:
        r1 = r0;
        r2 = r7;
        r14 = r2;
        goto L_0x049a;
    L_0x03de:
        r0 = move-exception;
        r4 = r7;
        r3 = 0;
        r7 = 0;
    L_0x03e2:
        r1 = r0;
        goto L_0x0469;
    L_0x03e5:
        r0 = move-exception;
        r4 = r7;
        r3 = 0;
        r7 = 0;
    L_0x03e9:
        r1 = r0;
        goto L_0x046f;
    L_0x03ec:
        r0 = move-exception;
        r4 = r7;
        r3 = 0;
        r7 = 0;
        r1 = r0;
        r2 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ ZipException -> 0x03fb, Exception -> 0x03f9, all -> 0x03f7 }
        r2.<init>(r1);	 Catch:{ ZipException -> 0x03fb, Exception -> 0x03f9, all -> 0x03f7 }
        throw r2;	 Catch:{ ZipException -> 0x03fb, Exception -> 0x03f9, all -> 0x03f7 }
    L_0x03f7:
        r0 = move-exception;
        goto L_0x03d9;
    L_0x03f9:
        r0 = move-exception;
        goto L_0x03e2;
    L_0x03fb:
        r0 = move-exception;
        goto L_0x03e9;
    L_0x03fd:
        r4 = r7;
        r13 = r8;
        r3 = 0;
        r7 = 0;
        r16 = java.lang.System.currentTimeMillis();	 Catch:{ ZipException -> 0x043c, Exception -> 0x0438, all -> 0x0433 }
        r2 = new java.lang.StringBuffer;	 Catch:{ ZipException -> 0x043c, Exception -> 0x0438, all -> 0x0433 }
        r8 = r35.getZipFile();	 Catch:{ ZipException -> 0x043c, Exception -> 0x0438, all -> 0x0433 }
        r8 = java.lang.String.valueOf(r8);	 Catch:{ ZipException -> 0x043c, Exception -> 0x0438, all -> 0x0433 }
        r2.<init>(r8);	 Catch:{ ZipException -> 0x043c, Exception -> 0x0438, all -> 0x0433 }
        r32 = r4;
        r3 = r16 % r5;	 Catch:{ ZipException -> 0x0431, Exception -> 0x042f, all -> 0x042d }
        r2.append(r3);	 Catch:{ ZipException -> 0x0431, Exception -> 0x042f, all -> 0x042d }
        r2 = r2.toString();	 Catch:{ ZipException -> 0x0431, Exception -> 0x042f, all -> 0x042d }
        r3 = new java.io.File;	 Catch:{ ZipException -> 0x042a, Exception -> 0x0428, all -> 0x0426 }
        r3.<init>(r2);	 Catch:{ ZipException -> 0x042a, Exception -> 0x0428, all -> 0x0426 }
        r7 = r2;
        r8 = r13;
        goto L_0x006c;
    L_0x0426:
        r0 = move-exception;
        goto L_0x0460;
    L_0x0428:
        r0 = move-exception;
        goto L_0x0467;
    L_0x042a:
        r0 = move-exception;
        goto L_0x046d;
    L_0x042d:
        r0 = move-exception;
        goto L_0x0444;
    L_0x042f:
        r0 = move-exception;
        goto L_0x044e;
    L_0x0431:
        r0 = move-exception;
        goto L_0x0458;
    L_0x0433:
        r0 = move-exception;
        r32 = r4;
        r1 = r0;
        goto L_0x0462;
    L_0x0438:
        r0 = move-exception;
        r32 = r4;
        goto L_0x03e2;
    L_0x043c:
        r0 = move-exception;
        r32 = r4;
        goto L_0x03e9;
    L_0x0440:
        r0 = move-exception;
        r32 = r7;
        r7 = 0;
    L_0x0444:
        r1 = r0;
        r2 = r7;
        r14 = r2;
        r4 = r32;
        goto L_0x0477;
    L_0x044a:
        r0 = move-exception;
        r32 = r7;
        r7 = 0;
    L_0x044e:
        r1 = r0;
        r5 = r7;
        r14 = r5;
        r4 = r32;
        goto L_0x047f;
    L_0x0454:
        r0 = move-exception;
        r32 = r7;
        r7 = 0;
    L_0x0458:
        r1 = r0;
        r5 = r7;
        r14 = r5;
        r4 = r32;
        goto L_0x048f;
    L_0x045e:
        r0 = move-exception;
        r7 = 0;
    L_0x0460:
        r1 = r0;
        r4 = r2;
    L_0x0462:
        r2 = r7;
        r14 = r2;
        goto L_0x0477;
    L_0x0465:
        r0 = move-exception;
        r7 = 0;
    L_0x0467:
        r1 = r0;
        r4 = r2;
    L_0x0469:
        r5 = r7;
        goto L_0x047e;
    L_0x046b:
        r0 = move-exception;
        r7 = 0;
    L_0x046d:
        r1 = r0;
        r4 = r2;
    L_0x046f:
        r5 = r7;
        goto L_0x048e;
    L_0x0471:
        r0 = move-exception;
        r7 = 0;
        r1 = r0;
        r2 = r7;
        r4 = r2;
        r14 = r4;
    L_0x0477:
        r3 = 0;
        goto L_0x049a;
    L_0x0479:
        r0 = move-exception;
        r7 = 0;
        r1 = r0;
        r4 = r7;
        r5 = r4;
    L_0x047e:
        r14 = r5;
    L_0x047f:
        r13 = 0;
    L_0x0480:
        r11.endProgressMonitorError(r1);	 Catch:{ all -> 0x0494 }
        r2 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ all -> 0x0494 }
        r2.<init>(r1);	 Catch:{ all -> 0x0494 }
        throw r2;	 Catch:{ all -> 0x0494 }
    L_0x0489:
        r0 = move-exception;	 Catch:{ all -> 0x0494 }
        r7 = 0;	 Catch:{ all -> 0x0494 }
        r1 = r0;	 Catch:{ all -> 0x0494 }
        r4 = r7;	 Catch:{ all -> 0x0494 }
        r5 = r4;	 Catch:{ all -> 0x0494 }
    L_0x048e:
        r14 = r5;	 Catch:{ all -> 0x0494 }
    L_0x048f:
        r13 = 0;	 Catch:{ all -> 0x0494 }
    L_0x0490:
        r11.endProgressMonitorError(r1);	 Catch:{ all -> 0x0494 }
        throw r1;	 Catch:{ all -> 0x0494 }
    L_0x0494:
        r0 = move-exception;
        r1 = r0;
        r3 = r13;
        r2 = r14;
        r14 = r7;
    L_0x0499:
        r7 = r5;
    L_0x049a:
        if (r7 == 0) goto L_0x049f;
    L_0x049c:
        r7.close();	 Catch:{ IOException -> 0x04a5 }
    L_0x049f:
        if (r2 == 0) goto L_0x04ad;	 Catch:{ IOException -> 0x04a5 }
    L_0x04a1:
        r2.close();	 Catch:{ IOException -> 0x04a5 }
        goto L_0x04ad;
    L_0x04a5:
        r1 = new net.lingala.zip4j.exception.ZipException;
        r2 = "cannot close input stream or output stream when trying to delete a file from zip file";
        r1.<init>(r2);
        throw r1;
    L_0x04ad:
        if (r3 == 0) goto L_0x04b3;
    L_0x04af:
        r9.restoreFileName(r14, r4);
        goto L_0x04bb;
    L_0x04b3:
        r2 = new java.io.File;
        r2.<init>(r4);
        r2.delete();
    L_0x04bb:
        throw r1;
    L_0x04bc:
        r1 = new net.lingala.zip4j.exception.ZipException;
        r2 = "input parameters is null in maintain zip file, cannot remove file from archive";
        r1.<init>(r2);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.util.ArchiveMaintainer.initRemoveZipFile(net.lingala.zip4j.model.ZipModel, net.lingala.zip4j.model.FileHeader, net.lingala.zip4j.progress.ProgressMonitor):java.util.HashMap");
    }

    private void restoreFileName(File file, String str) throws ZipException {
        if (!file.delete()) {
            throw new ZipException("cannot delete old zip file");
        } else if (new File(str).renameTo(file) == null) {
            throw new ZipException("cannot rename modified zip file");
        }
    }

    private void copyFile(RandomAccessFile randomAccessFile, OutputStream outputStream, long j, long j2, ProgressMonitor progressMonitor) throws ZipException {
        RandomAccessFile randomAccessFile2 = randomAccessFile;
        OutputStream outputStream2 = outputStream;
        long j3 = j;
        ProgressMonitor progressMonitor2 = progressMonitor;
        if (randomAccessFile2 != null) {
            if (outputStream2 != null) {
                long j4 = 0;
                if (j3 < 0) {
                    throw new ZipException("starting offset is negative, cannot copy file");
                } else if (j2 < 0) {
                    throw new ZipException("end offset is negative, cannot copy file");
                } else if (j3 > j2) {
                    throw new ZipException("start offset is greater than end offset, cannot copy file");
                } else if (j3 != j2) {
                    if (progressMonitor.isCancelAllTasks()) {
                        progressMonitor2.setResult(3);
                        progressMonitor2.setState(0);
                        return;
                    }
                    try {
                        byte[] bArr;
                        randomAccessFile2.seek(j3);
                        long j5 = j2 - j3;
                        if (j5 < PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM) {
                            bArr = new byte[((int) j5)];
                        } else {
                            bArr = new byte[4096];
                        }
                        while (true) {
                            int read = randomAccessFile2.read(bArr);
                            if (read == -1) {
                                break;
                            }
                            outputStream2.write(bArr, 0, read);
                            long j6 = (long) read;
                            progressMonitor2.updateWorkCompleted(j6);
                            if (progressMonitor.isCancelAllTasks()) {
                                progressMonitor2.setResult(3);
                                return;
                            }
                            long j7 = j4 + j6;
                            if (j7 == j5) {
                                break;
                            }
                            if (j7 + ((long) bArr.length) > j5) {
                                bArr = new byte[((int) (j5 - j7))];
                            }
                            j4 = j7;
                        }
                        return;
                    } catch (Throwable e) {
                        throw new ZipException(e);
                    } catch (Throwable e2) {
                        throw new ZipException(e2);
                    }
                } else {
                    return;
                }
            }
        }
        throw new ZipException("input or output stream is null, cannot copy file");
    }

    private RandomAccessFile createFileHandler(ZipModel zipModel, String str) throws ZipException {
        if (zipModel != null) {
            if (Zip4jUtil.isStringNotNullAndNotEmpty(zipModel.getZipFile())) {
                try {
                    return new RandomAccessFile(new File(zipModel.getZipFile()), str);
                } catch (Throwable e) {
                    throw new ZipException(e);
                }
            }
        }
        throw new ZipException("input parameter is null in getFilePointer, cannot create file handler to remove file");
    }

    public void mergeSplitZipFiles(ZipModel zipModel, File file, ProgressMonitor progressMonitor, boolean z) throws ZipException {
        if (z) {
            final ZipModel zipModel2 = zipModel;
            final File file2 = file;
            final ProgressMonitor progressMonitor2 = progressMonitor;
            new Thread(this, InternalZipConstants.THREAD_NAME) {
                final /* synthetic */ ArchiveMaintainer this$0;

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
                    r0.initMergeSplitZipFile(r1, r2, r3);	 Catch:{ ZipException -> 0x000b }
                L_0x000b:
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.util.ArchiveMaintainer.2.run():void");
                }
            }.start();
            return;
        }
        initMergeSplitZipFile(zipModel, file, progressMonitor);
    }

    private void initMergeSplitZipFile(net.lingala.zip4j.model.ZipModel r29, java.io.File r30, net.lingala.zip4j.progress.ProgressMonitor r31) throws net.lingala.zip4j.exception.ZipException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r28 = this;
        r9 = r28;
        r10 = r29;
        r11 = r31;
        if (r10 != 0) goto L_0x0013;
    L_0x0008:
        r1 = new net.lingala.zip4j.exception.ZipException;
        r2 = "one of the input parameters is null, cannot merge split zip file";
        r1.<init>(r2);
        r11.endProgressMonitorError(r1);
        throw r1;
    L_0x0013:
        r1 = r29.isSplitArchive();
        if (r1 != 0) goto L_0x0024;
    L_0x0019:
        r1 = new net.lingala.zip4j.exception.ZipException;
        r2 = "archive not a split zip file";
        r1.<init>(r2);
        r11.endProgressMonitorError(r1);
        throw r1;
    L_0x0024:
        r12 = new java.util.ArrayList;
        r12.<init>();
        r1 = 0;
        r2 = r29.getEndCentralDirRecord();	 Catch:{ IOException -> 0x016d, Exception -> 0x0160, all -> 0x015b }
        r13 = r2.getNoOfThisDisk();	 Catch:{ IOException -> 0x016d, Exception -> 0x0160, all -> 0x015b }
        if (r13 > 0) goto L_0x003c;	 Catch:{ IOException -> 0x016d, Exception -> 0x0160, all -> 0x015b }
    L_0x0034:
        r2 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ IOException -> 0x016d, Exception -> 0x0160, all -> 0x015b }
        r3 = "corrupt zip model, archive not a split zip file";	 Catch:{ IOException -> 0x016d, Exception -> 0x0160, all -> 0x015b }
        r2.<init>(r3);	 Catch:{ IOException -> 0x016d, Exception -> 0x0160, all -> 0x015b }
        throw r2;	 Catch:{ IOException -> 0x016d, Exception -> 0x0160, all -> 0x015b }
    L_0x003c:
        r2 = r30;	 Catch:{ IOException -> 0x016d, Exception -> 0x0160, all -> 0x015b }
        r14 = r9.prepareOutputStreamForMerge(r2);	 Catch:{ IOException -> 0x016d, Exception -> 0x0160, all -> 0x015b }
        r15 = 0;
        r2 = r1;
        r1 = 0;
        r4 = 0;
        r8 = 0;
    L_0x0048:
        if (r8 <= r13) goto L_0x007f;
    L_0x004a:
        r3 = r29.clone();	 Catch:{ IOException -> 0x007a, Exception -> 0x0075, all -> 0x0070 }
        r3 = (net.lingala.zip4j.model.ZipModel) r3;	 Catch:{ IOException -> 0x007a, Exception -> 0x0075, all -> 0x0070 }
        r6 = r3.getEndCentralDirRecord();	 Catch:{ IOException -> 0x007a, Exception -> 0x0075, all -> 0x0070 }
        r6.setOffsetOfStartOfCentralDir(r4);	 Catch:{ IOException -> 0x007a, Exception -> 0x0075, all -> 0x0070 }
        r9.updateSplitZipModel(r3, r12, r1);	 Catch:{ IOException -> 0x007a, Exception -> 0x0075, all -> 0x0070 }
        r1 = new net.lingala.zip4j.core.HeaderWriter;	 Catch:{ IOException -> 0x007a, Exception -> 0x0075, all -> 0x0070 }
        r1.<init>();	 Catch:{ IOException -> 0x007a, Exception -> 0x0075, all -> 0x0070 }
        r1.finalizeZipFileWithoutValidations(r3, r14);	 Catch:{ IOException -> 0x007a, Exception -> 0x0075, all -> 0x0070 }
        r31.endProgressMonitorSuccess();	 Catch:{ IOException -> 0x007a, Exception -> 0x0075, all -> 0x0070 }
        if (r14 == 0) goto L_0x006a;
    L_0x0067:
        r14.close();	 Catch:{ IOException -> 0x006a }
    L_0x006a:
        if (r2 == 0) goto L_0x006f;
    L_0x006c:
        r2.close();	 Catch:{ IOException -> 0x006f }
    L_0x006f:
        return;
    L_0x0070:
        r0 = move-exception;
        r1 = r0;
        r3 = r2;
        goto L_0x017c;
    L_0x0075:
        r0 = move-exception;
        r1 = r0;
        r3 = r2;
        goto L_0x0164;
    L_0x007a:
        r0 = move-exception;
        r1 = r0;
        r3 = r2;
        goto L_0x0171;
    L_0x007f:
        r3 = r9.createSplitZipFileHandler(r10, r8);	 Catch:{ IOException -> 0x007a, Exception -> 0x0075, all -> 0x0070 }
        r2 = new java.lang.Long;	 Catch:{ IOException -> 0x0158, Exception -> 0x0155, all -> 0x0152 }
        r6 = r3.length();	 Catch:{ IOException -> 0x0158, Exception -> 0x0155, all -> 0x0152 }
        r2.<init>(r6);	 Catch:{ IOException -> 0x0158, Exception -> 0x0155, all -> 0x0152 }
        r6 = 4;
        if (r8 != 0) goto L_0x00d2;
    L_0x008f:
        r7 = r29.getCentralDirectory();	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        if (r7 == 0) goto L_0x00d2;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
    L_0x0095:
        r7 = r29.getCentralDirectory();	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        r7 = r7.getFileHeaders();	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        if (r7 == 0) goto L_0x00d2;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
    L_0x009f:
        r7 = r29.getCentralDirectory();	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        r7 = r7.getFileHeaders();	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        r7 = r7.size();	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        if (r7 <= 0) goto L_0x00d2;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
    L_0x00ad:
        r7 = new byte[r6];	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        r19 = r7;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        r6 = 0;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        r3.seek(r6);	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        r6 = r19;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        r3.read(r6);	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        r6 = net.lingala.zip4j.util.Raw.readIntLittleEndian(r6, r15);	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        r6 = (long) r6;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        r16 = 134695760; // 0x8074b50 float:4.0713614E-34 double:6.65485477E-316;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        r19 = (r6 > r16 ? 1 : (r6 == r16 ? 0 : -1));	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        if (r19 != 0) goto L_0x00d2;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
    L_0x00c7:
        r1 = 1;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        r1 = 4;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        r16 = 1;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        goto L_0x00d5;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
    L_0x00cc:
        r0 = move-exception;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        goto L_0x0163;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
    L_0x00cf:
        r0 = move-exception;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        goto L_0x0170;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
    L_0x00d2:
        r16 = r1;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        r1 = 0;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
    L_0x00d5:
        if (r8 != r13) goto L_0x00e4;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
    L_0x00d7:
        r2 = new java.lang.Long;	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        r6 = r29.getEndCentralDirRecord();	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        r6 = r6.getOffsetOfStartOfCentralDir();	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
        r2.<init>(r6);	 Catch:{ IOException -> 0x00cf, Exception -> 0x00cc }
    L_0x00e4:
        r6 = r2;
        r1 = (long) r1;
        r17 = r6.longValue();	 Catch:{ IOException -> 0x0158, Exception -> 0x0155, all -> 0x0152 }
        r21 = r1;
        r1 = r9;
        r2 = r3;
        r7 = r3;
        r3 = r14;
        r23 = r4;
        r4 = r21;
        r15 = r6;
        r25 = r7;
        r19 = 0;
        r6 = r17;
        r17 = r8;
        r8 = r11;
        r1.copyFile(r2, r3, r4, r6, r8);	 Catch:{ IOException -> 0x014c, Exception -> 0x0146, all -> 0x0140 }
        r1 = r15.longValue();	 Catch:{ IOException -> 0x014c, Exception -> 0x0146, all -> 0x0140 }
        r3 = 0;	 Catch:{ IOException -> 0x014c, Exception -> 0x0146, all -> 0x0140 }
        r3 = r1 - r21;	 Catch:{ IOException -> 0x014c, Exception -> 0x0146, all -> 0x0140 }
        r1 = r23 + r3;	 Catch:{ IOException -> 0x014c, Exception -> 0x0146, all -> 0x0140 }
        r3 = r31.isCancelAllTasks();	 Catch:{ IOException -> 0x014c, Exception -> 0x0146, all -> 0x0140 }
        if (r3 == 0) goto L_0x0125;	 Catch:{ IOException -> 0x014c, Exception -> 0x0146, all -> 0x0140 }
    L_0x0110:
        r1 = 3;	 Catch:{ IOException -> 0x014c, Exception -> 0x0146, all -> 0x0140 }
        r11.setResult(r1);	 Catch:{ IOException -> 0x014c, Exception -> 0x0146, all -> 0x0140 }
        r3 = 0;	 Catch:{ IOException -> 0x014c, Exception -> 0x0146, all -> 0x0140 }
        r11.setState(r3);	 Catch:{ IOException -> 0x014c, Exception -> 0x0146, all -> 0x0140 }
        if (r14 == 0) goto L_0x011d;
    L_0x011a:
        r14.close();	 Catch:{ IOException -> 0x011d }
    L_0x011d:
        r4 = r25;
        if (r4 == 0) goto L_0x0124;
    L_0x0121:
        r4.close();	 Catch:{ IOException -> 0x0124 }
    L_0x0124:
        return;
    L_0x0125:
        r4 = r25;
        r3 = 0;
        r12.add(r15);	 Catch:{ IOException -> 0x013e, Exception -> 0x013c, all -> 0x013a }
        r4.close();	 Catch:{ IOException -> 0x012e, Exception -> 0x013c, all -> 0x013a }
    L_0x012e:
        r8 = r17 + 1;
        r15 = 0;
        r26 = r1;
        r2 = r4;
        r4 = r26;
        r1 = r16;
        goto L_0x0048;
    L_0x013a:
        r0 = move-exception;
        goto L_0x0143;
    L_0x013c:
        r0 = move-exception;
        goto L_0x0149;
    L_0x013e:
        r0 = move-exception;
        goto L_0x014f;
    L_0x0140:
        r0 = move-exception;
        r4 = r25;
    L_0x0143:
        r1 = r0;
        r3 = r4;
        goto L_0x017c;
    L_0x0146:
        r0 = move-exception;
        r4 = r25;
    L_0x0149:
        r1 = r0;
        r3 = r4;
        goto L_0x0164;
    L_0x014c:
        r0 = move-exception;
        r4 = r25;
    L_0x014f:
        r1 = r0;
        r3 = r4;
        goto L_0x0171;
    L_0x0152:
        r0 = move-exception;
        r4 = r3;
        goto L_0x015e;
    L_0x0155:
        r0 = move-exception;
        r4 = r3;
        goto L_0x0163;
    L_0x0158:
        r0 = move-exception;
        r4 = r3;
        goto L_0x0170;
    L_0x015b:
        r0 = move-exception;
        r3 = r1;
        r14 = r3;
    L_0x015e:
        r1 = r0;
        goto L_0x017c;
    L_0x0160:
        r0 = move-exception;
        r3 = r1;
        r14 = r3;
    L_0x0163:
        r1 = r0;
    L_0x0164:
        r11.endProgressMonitorError(r1);	 Catch:{ all -> 0x017a }
        r2 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ all -> 0x017a }
        r2.<init>(r1);	 Catch:{ all -> 0x017a }
        throw r2;	 Catch:{ all -> 0x017a }
    L_0x016d:
        r0 = move-exception;	 Catch:{ all -> 0x017a }
        r3 = r1;	 Catch:{ all -> 0x017a }
        r14 = r3;	 Catch:{ all -> 0x017a }
    L_0x0170:
        r1 = r0;	 Catch:{ all -> 0x017a }
    L_0x0171:
        r11.endProgressMonitorError(r1);	 Catch:{ all -> 0x017a }
        r2 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ all -> 0x017a }
        r2.<init>(r1);	 Catch:{ all -> 0x017a }
        throw r2;	 Catch:{ all -> 0x017a }
    L_0x017a:
        r0 = move-exception;
        goto L_0x015e;
    L_0x017c:
        if (r14 == 0) goto L_0x0181;
    L_0x017e:
        r14.close();	 Catch:{ IOException -> 0x0181 }
    L_0x0181:
        if (r3 == 0) goto L_0x0186;
    L_0x0183:
        r3.close();	 Catch:{ IOException -> 0x0186 }
    L_0x0186:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.util.ArchiveMaintainer.initMergeSplitZipFile(net.lingala.zip4j.model.ZipModel, java.io.File, net.lingala.zip4j.progress.ProgressMonitor):void");
    }

    private RandomAccessFile createSplitZipFileHandler(ZipModel zipModel, int i) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null, cannot create split file handler");
        } else if (i < 0) {
            throw new ZipException("invlaid part number, cannot create split file handler");
        } else {
            try {
                String zipFile = zipModel.getZipFile();
                if (i == zipModel.getEndCentralDirRecord().getNoOfThisDisk()) {
                    zipModel = zipModel.getZipFile();
                } else if (i >= 9) {
                    zipModel = new StringBuffer(String.valueOf(zipFile.substring(0, zipFile.lastIndexOf("."))));
                    zipModel.append(".z");
                    zipModel.append(i + 1);
                    zipModel = zipModel.toString();
                } else {
                    zipModel = new StringBuffer(String.valueOf(zipFile.substring(0, zipFile.lastIndexOf("."))));
                    zipModel.append(".z0");
                    zipModel.append(i + 1);
                    zipModel = zipModel.toString();
                }
                File file = new File(zipModel);
                if (Zip4jUtil.checkFileExists(file)) {
                    return new RandomAccessFile(file, InternalZipConstants.READ_MODE);
                }
                StringBuffer stringBuffer = new StringBuffer("split file does not exist: ");
                stringBuffer.append(zipModel);
                throw new ZipException(stringBuffer.toString());
            } catch (Throwable e) {
                throw new ZipException(e);
            } catch (Throwable e2) {
                throw new ZipException(e2);
            }
        }
    }

    private OutputStream prepareOutputStreamForMerge(File file) throws ZipException {
        if (file == null) {
            throw new ZipException("outFile is null, cannot create outputstream");
        }
        try {
            return new FileOutputStream(file);
        } catch (Throwable e) {
            throw new ZipException(e);
        } catch (Throwable e2) {
            throw new ZipException(e2);
        }
    }

    private void updateSplitZipModel(ZipModel zipModel, ArrayList arrayList, boolean z) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null, cannot update split zip model");
        }
        zipModel.setSplitArchive(false);
        updateSplitFileHeader(zipModel, arrayList, z);
        updateSplitEndCentralDirectory(zipModel);
        if (zipModel.isZip64Format()) {
            updateSplitZip64EndCentralDirLocator(zipModel, arrayList);
            updateSplitZip64EndCentralDirRec(zipModel, arrayList);
        }
    }

    private void updateSplitFileHeader(ZipModel zipModel, ArrayList arrayList, boolean z) throws ZipException {
        try {
            if (zipModel.getCentralDirectory() == null) {
                throw new ZipException("corrupt zip model - getCentralDirectory, cannot update split zip model");
            }
            int size = zipModel.getCentralDirectory().getFileHeaders().size();
            z = z ? true : false;
            for (int i = 0; i < size; i++) {
                long j = 0;
                int i2 = 0;
                while (i2 < ((FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(i)).getDiskNumberStart()) {
                    i2++;
                    j += ((Long) arrayList.get(i2)).longValue();
                }
                ((FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(i)).setOffsetLocalHeader((((FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(i)).getOffsetLocalHeader() + j) - ((long) z));
                ((FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(i)).setDiskNumberStart(0);
            }
        } catch (ZipModel zipModel2) {
            throw zipModel2;
        } catch (Throwable e) {
            throw new ZipException(e);
        }
    }

    private void updateSplitEndCentralDirectory(ZipModel zipModel) throws ZipException {
        if (zipModel == null) {
            try {
                throw new ZipException("zip model is null - cannot update end of central directory for split zip model");
            } catch (ZipModel zipModel2) {
                throw zipModel2;
            } catch (Throwable e) {
                throw new ZipException(e);
            }
        } else if (zipModel2.getCentralDirectory() == null) {
            throw new ZipException("corrupt zip model - getCentralDirectory, cannot update split zip model");
        } else {
            zipModel2.getEndCentralDirRecord().setNoOfThisDisk(0);
            zipModel2.getEndCentralDirRecord().setNoOfThisDiskStartOfCentralDir(0);
            zipModel2.getEndCentralDirRecord().setTotNoOfEntriesInCentralDir(zipModel2.getCentralDirectory().getFileHeaders().size());
            zipModel2.getEndCentralDirRecord().setTotNoOfEntriesInCentralDirOnThisDisk(zipModel2.getCentralDirectory().getFileHeaders().size());
        }
    }

    private void updateSplitZip64EndCentralDirLocator(ZipModel zipModel, ArrayList arrayList) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null, cannot update split Zip64 end of central directory locator");
        } else if (zipModel.getZip64EndCentralDirLocator() != null) {
            int i = 0;
            zipModel.getZip64EndCentralDirLocator().setNoOfDiskStartOfZip64EndOfCentralDirRec(0);
            long j = 0;
            while (i < arrayList.size()) {
                i++;
                j += ((Long) arrayList.get(i)).longValue();
            }
            zipModel.getZip64EndCentralDirLocator().setOffsetZip64EndOfCentralDirRec(zipModel.getZip64EndCentralDirLocator().getOffsetZip64EndOfCentralDirRec() + j);
            zipModel.getZip64EndCentralDirLocator().setTotNumberOfDiscs(1);
        }
    }

    private void updateSplitZip64EndCentralDirRec(ZipModel zipModel, ArrayList arrayList) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null, cannot update split Zip64 end of central directory record");
        } else if (zipModel.getZip64EndCentralDirRecord() != null) {
            int i = 0;
            zipModel.getZip64EndCentralDirRecord().setNoOfThisDisk(0);
            zipModel.getZip64EndCentralDirRecord().setNoOfThisDiskStartOfCentralDir(0);
            zipModel.getZip64EndCentralDirRecord().setTotNoOfEntriesInCentralDirOnThisDisk((long) zipModel.getEndCentralDirRecord().getTotNoOfEntriesInCentralDir());
            long j = 0;
            while (i < arrayList.size()) {
                i++;
                j += ((Long) arrayList.get(i)).longValue();
            }
            zipModel.getZip64EndCentralDirRecord().setOffsetStartCenDirWRTStartDiskNo(zipModel.getZip64EndCentralDirRecord().getOffsetStartCenDirWRTStartDiskNo() + j);
        }
    }

    public void setComment(net.lingala.zip4j.model.ZipModel r5, java.lang.String r6) throws net.lingala.zip4j.exception.ZipException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r4 = this;
        if (r6 != 0) goto L_0x000a;
    L_0x0002:
        r5 = new net.lingala.zip4j.exception.ZipException;
        r6 = "comment is null, cannot update Zip file with comment";
        r5.<init>(r6);
        throw r5;
    L_0x000a:
        if (r5 != 0) goto L_0x0014;
    L_0x000c:
        r5 = new net.lingala.zip4j.exception.ZipException;
        r6 = "zipModel is null, cannot update Zip file with comment";
        r5.<init>(r6);
        throw r5;
    L_0x0014:
        r0 = r6.getBytes();
        r1 = r6.length();
        r2 = "windows-1254";
        r2 = net.lingala.zip4j.util.Zip4jUtil.isSupportedCharset(r2);
        if (r2 == 0) goto L_0x0047;
    L_0x0024:
        r0 = new java.lang.String;	 Catch:{ UnsupportedEncodingException -> 0x003f }
        r1 = "windows-1254";	 Catch:{ UnsupportedEncodingException -> 0x003f }
        r1 = r6.getBytes(r1);	 Catch:{ UnsupportedEncodingException -> 0x003f }
        r2 = "windows-1254";	 Catch:{ UnsupportedEncodingException -> 0x003f }
        r0.<init>(r1, r2);	 Catch:{ UnsupportedEncodingException -> 0x003f }
        r1 = "windows-1254";	 Catch:{ UnsupportedEncodingException -> 0x003f }
        r1 = r0.getBytes(r1);	 Catch:{ UnsupportedEncodingException -> 0x003f }
        r2 = r0.length();	 Catch:{ UnsupportedEncodingException -> 0x003f }
        r6 = r0;
        r0 = r1;
        r1 = r2;
        goto L_0x0047;
    L_0x003f:
        r0 = r6.getBytes();
        r1 = r6.length();
    L_0x0047:
        r2 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        if (r1 <= r2) goto L_0x0054;
    L_0x004c:
        r5 = new net.lingala.zip4j.exception.ZipException;
        r6 = "comment length exceeds maximum length";
        r5.<init>(r6);
        throw r5;
    L_0x0054:
        r2 = r5.getEndCentralDirRecord();
        r2.setComment(r6);
        r6 = r5.getEndCentralDirRecord();
        r6.setCommentBytes(r0);
        r6 = r5.getEndCentralDirRecord();
        r6.setCommentLength(r1);
        r6 = 0;
        r0 = new net.lingala.zip4j.core.HeaderWriter;	 Catch:{ FileNotFoundException -> 0x00b0, IOException -> 0x00a9 }
        r0.<init>();	 Catch:{ FileNotFoundException -> 0x00b0, IOException -> 0x00a9 }
        r1 = new net.lingala.zip4j.io.SplitOutputStream;	 Catch:{ FileNotFoundException -> 0x00b0, IOException -> 0x00a9 }
        r2 = r5.getZipFile();	 Catch:{ FileNotFoundException -> 0x00b0, IOException -> 0x00a9 }
        r1.<init>(r2);	 Catch:{ FileNotFoundException -> 0x00b0, IOException -> 0x00a9 }
        r6 = r5.isZip64Format();	 Catch:{ FileNotFoundException -> 0x00a4, IOException -> 0x00a1, all -> 0x009e }
        if (r6 == 0) goto L_0x008a;	 Catch:{ FileNotFoundException -> 0x00a4, IOException -> 0x00a1, all -> 0x009e }
    L_0x007e:
        r6 = r5.getZip64EndCentralDirRecord();	 Catch:{ FileNotFoundException -> 0x00a4, IOException -> 0x00a1, all -> 0x009e }
        r2 = r6.getOffsetStartCenDirWRTStartDiskNo();	 Catch:{ FileNotFoundException -> 0x00a4, IOException -> 0x00a1, all -> 0x009e }
        r1.seek(r2);	 Catch:{ FileNotFoundException -> 0x00a4, IOException -> 0x00a1, all -> 0x009e }
        goto L_0x0095;	 Catch:{ FileNotFoundException -> 0x00a4, IOException -> 0x00a1, all -> 0x009e }
    L_0x008a:
        r6 = r5.getEndCentralDirRecord();	 Catch:{ FileNotFoundException -> 0x00a4, IOException -> 0x00a1, all -> 0x009e }
        r2 = r6.getOffsetOfStartOfCentralDir();	 Catch:{ FileNotFoundException -> 0x00a4, IOException -> 0x00a1, all -> 0x009e }
        r1.seek(r2);	 Catch:{ FileNotFoundException -> 0x00a4, IOException -> 0x00a1, all -> 0x009e }
    L_0x0095:
        r0.finalizeZipFileWithoutValidations(r5, r1);	 Catch:{ FileNotFoundException -> 0x00a4, IOException -> 0x00a1, all -> 0x009e }
        if (r1 == 0) goto L_0x009d;
    L_0x009a:
        r1.close();	 Catch:{ IOException -> 0x009d }
    L_0x009d:
        return;
    L_0x009e:
        r5 = move-exception;
        r6 = r1;
        goto L_0x00b7;
    L_0x00a1:
        r5 = move-exception;
        r6 = r1;
        goto L_0x00aa;
    L_0x00a4:
        r5 = move-exception;
        r6 = r1;
        goto L_0x00b1;
    L_0x00a7:
        r5 = move-exception;
        goto L_0x00b7;
    L_0x00a9:
        r5 = move-exception;
    L_0x00aa:
        r0 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ all -> 0x00a7 }
        r0.<init>(r5);	 Catch:{ all -> 0x00a7 }
        throw r0;	 Catch:{ all -> 0x00a7 }
    L_0x00b0:
        r5 = move-exception;	 Catch:{ all -> 0x00a7 }
    L_0x00b1:
        r0 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ all -> 0x00a7 }
        r0.<init>(r5);	 Catch:{ all -> 0x00a7 }
        throw r0;	 Catch:{ all -> 0x00a7 }
    L_0x00b7:
        if (r6 == 0) goto L_0x00bc;
    L_0x00b9:
        r6.close();	 Catch:{ IOException -> 0x00bc }
    L_0x00bc:
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.util.ArchiveMaintainer.setComment(net.lingala.zip4j.model.ZipModel, java.lang.String):void");
    }

    public void initProgressMonitorForRemoveOp(ZipModel zipModel, FileHeader fileHeader, ProgressMonitor progressMonitor) throws ZipException {
        if (!(zipModel == null || fileHeader == null)) {
            if (progressMonitor != null) {
                progressMonitor.setCurrentOperation(2);
                progressMonitor.setFileName(fileHeader.getFileName());
                progressMonitor.setTotalWork(calculateTotalWorkForRemoveOp(zipModel, fileHeader));
                progressMonitor.setState(1);
                return;
            }
        }
        throw new ZipException("one of the input parameters is null, cannot calculate total work");
    }

    private long calculateTotalWorkForRemoveOp(ZipModel zipModel, FileHeader fileHeader) throws ZipException {
        return Zip4jUtil.getFileLengh(new File(zipModel.getZipFile())) - fileHeader.getCompressedSize();
    }

    public void initProgressMonitorForMergeOp(ZipModel zipModel, ProgressMonitor progressMonitor) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null, cannot calculate total work for merge op");
        }
        progressMonitor.setCurrentOperation(4);
        progressMonitor.setFileName(zipModel.getZipFile());
        progressMonitor.setTotalWork(calculateTotalWorkForMergeOp(zipModel));
        progressMonitor.setState(1);
    }

    private long calculateTotalWorkForMergeOp(ZipModel zipModel) throws ZipException {
        if (!zipModel.isSplitArchive()) {
            return 0;
        }
        int noOfThisDisk = zipModel.getEndCentralDirRecord().getNoOfThisDisk();
        String zipFile = zipModel.getZipFile();
        long j = 0;
        int i = 0;
        while (i <= noOfThisDisk) {
            String zipFile2;
            if (zipModel.getEndCentralDirRecord().getNoOfThisDisk() == 0) {
                zipFile2 = zipModel.getZipFile();
            } else {
                StringBuffer stringBuffer = new StringBuffer(String.valueOf(zipFile.substring(0, zipFile.lastIndexOf("."))));
                stringBuffer.append(".z0");
                stringBuffer.append(1);
                zipFile2 = stringBuffer.toString();
            }
            i++;
            j += Zip4jUtil.getFileLengh(new File(zipFile2));
        }
        return j;
    }
}
