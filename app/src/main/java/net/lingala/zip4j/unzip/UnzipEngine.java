package net.lingala.zip4j.unzip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.zip.CRC32;
import net.lingala.zip4j.crypto.AESDecrypter;
import net.lingala.zip4j.crypto.IDecrypter;
import net.lingala.zip4j.crypto.StandardDecrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jUtil;

public class UnzipEngine {
    private CRC32 crc;
    private int currSplitFileCounter = 0;
    private IDecrypter decrypter;
    private FileHeader fileHeader;
    private LocalFileHeader localFileHeader;
    private ZipModel zipModel;

    public UnzipEngine(ZipModel zipModel, FileHeader fileHeader) throws ZipException {
        if (zipModel != null) {
            if (fileHeader != null) {
                this.zipModel = zipModel;
                this.fileHeader = fileHeader;
                this.crc = new CRC32();
                return;
            }
        }
        throw new ZipException("Invalid parameters passed to StoreUnzip. One or more of the parameters were null");
    }

    public void unzipFile(ProgressMonitor progressMonitor, String str, String str2, UnzipParameters unzipParameters) throws ZipException {
        InputStream inputStream;
        OutputStream outputStream;
        Throwable e;
        Object obj;
        if (!(this.zipModel == null || this.fileHeader == null)) {
            if (Zip4jUtil.isStringNotNullAndNotEmpty(str)) {
                InputStream inputStream2 = null;
                try {
                    byte[] bArr = new byte[4096];
                    inputStream = getInputStream();
                    try {
                        outputStream = getOutputStream(str, str2);
                        do {
                            try {
                                int read = inputStream.read(bArr);
                                if (read == -1) {
                                    closeStreams(inputStream, outputStream);
                                    UnzipUtil.applyFileAttributes(this.fileHeader, new File(getOutputFileNameWithPath(str, str2)), unzipParameters);
                                    closeStreams(inputStream, outputStream);
                                    return;
                                }
                                outputStream.write(bArr, 0, read);
                                progressMonitor.updateWorkCompleted((long) read);
                            } catch (IOException e2) {
                                e = e2;
                            } catch (Exception e3) {
                                e = e3;
                            } catch (Throwable th) {
                                progressMonitor = th;
                            }
                        } while (!progressMonitor.isCancelAllTasks());
                        progressMonitor.setResult(3);
                        progressMonitor.setState(0);
                        closeStreams(inputStream, outputStream);
                        return;
                    } catch (IOException e4) {
                        e = e4;
                        obj = null;
                        inputStream2 = inputStream;
                        throw new ZipException(e);
                    } catch (Exception e5) {
                        e = e5;
                        outputStream = null;
                        inputStream2 = inputStream;
                        throw new ZipException(e);
                    } catch (Throwable th2) {
                        progressMonitor = th2;
                        outputStream = null;
                        closeStreams(inputStream, outputStream);
                        throw progressMonitor;
                    }
                } catch (IOException e6) {
                    e = e6;
                    obj = null;
                    throw new ZipException(e);
                } catch (Exception e7) {
                    e = e7;
                    outputStream = null;
                    throw new ZipException(e);
                } catch (Throwable th3) {
                    progressMonitor = th3;
                    inputStream = inputStream2;
                    closeStreams(inputStream, outputStream);
                    throw progressMonitor;
                }
            }
        }
        throw new ZipException("Invalid parameters passed during unzipping file. One or more of the parameters were null");
    }

    public net.lingala.zip4j.io.ZipInputStream getInputStream() throws net.lingala.zip4j.exception.ZipException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r13 = this;
        r0 = r13.fileHeader;
        if (r0 != 0) goto L_0x000c;
    L_0x0004:
        r0 = new net.lingala.zip4j.exception.ZipException;
        r1 = "file header is null, cannot get inputstream";
        r0.<init>(r1);
        throw r0;
    L_0x000c:
        r0 = 0;
        r1 = "r";	 Catch:{ ZipException -> 0x011c, Exception -> 0x010d }
        r1 = r13.createFileHandler(r1);	 Catch:{ ZipException -> 0x011c, Exception -> 0x010d }
        r0 = "local header and file header do not match";	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2 = r13.checkLocalHeader();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        if (r2 != 0) goto L_0x0021;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
    L_0x001b:
        r2 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2.<init>(r0);	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        throw r2;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
    L_0x0021:
        r13.init(r1);	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = r13.localFileHeader;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2 = r0.getCompressedSize();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = r13.localFileHeader;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r4 = r0.getOffsetStartOfData();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = r13.localFileHeader;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = r0.isEncrypted();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r6 = 99;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        if (r0 == 0) goto L_0x009e;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
    L_0x003a:
        r0 = r13.localFileHeader;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = r0.getEncryptionMethod();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        if (r0 != r6) goto L_0x008e;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
    L_0x0042:
        r0 = r13.decrypter;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = r0 instanceof net.lingala.zip4j.crypto.AESDecrypter;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        if (r0 == 0) goto L_0x0074;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
    L_0x0048:
        r0 = r13.decrypter;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = (net.lingala.zip4j.crypto.AESDecrypter) r0;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = r0.getSaltLength();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r7 = r13.decrypter;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r7 = (net.lingala.zip4j.crypto.AESDecrypter) r7;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r7 = r7.getPasswordVerifierLength();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = r0 + r7;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = r0 + 10;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r7 = (long) r0;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r9 = r2 - r7;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = r13.decrypter;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = (net.lingala.zip4j.crypto.AESDecrypter) r0;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = r0.getSaltLength();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2 = r13.decrypter;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2 = (net.lingala.zip4j.crypto.AESDecrypter) r2;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2 = r2.getPasswordVerifierLength();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = r0 + r2;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2 = (long) r0;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r7 = r4 + r2;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r4 = r7;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        goto L_0x009f;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
    L_0x0074:
        r0 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2 = new java.lang.StringBuffer;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r3 = "invalid decryptor when trying to calculate compressed size for AES encrypted file: ";	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2.<init>(r3);	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r3 = r13.fileHeader;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r3 = r3.getFileName();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2.append(r3);	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2 = r2.toString();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0.<init>(r2);	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        throw r0;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
    L_0x008e:
        r0 = r13.localFileHeader;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = r0.getEncryptionMethod();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        if (r0 != 0) goto L_0x009e;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
    L_0x0096:
        r7 = 12;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r9 = r2 - r7;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2 = r4 + r7;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r4 = r2;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        goto L_0x009f;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
    L_0x009e:
        r9 = r2;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
    L_0x009f:
        r0 = r13.fileHeader;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = r0.getCompressionMethod();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2 = r13.fileHeader;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2 = r2.getEncryptionMethod();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        if (r2 != r6) goto L_0x00da;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
    L_0x00ad:
        r0 = r13.fileHeader;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = r0.getAesExtraDataRecord();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        if (r0 == 0) goto L_0x00c0;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
    L_0x00b5:
        r0 = r13.fileHeader;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = r0.getAesExtraDataRecord();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0 = r0.getCompressionMethod();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        goto L_0x00da;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
    L_0x00c0:
        r0 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2 = new java.lang.StringBuffer;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r3 = "AESExtraDataRecord does not exist for AES encrypted file: ";	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2.<init>(r3);	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r3 = r13.fileHeader;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r3 = r3.getFileName();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2.append(r3);	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2 = r2.toString();	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0.<init>(r2);	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        throw r0;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
    L_0x00da:
        r1.seek(r4);	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        if (r0 == 0) goto L_0x00fa;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
    L_0x00df:
        r2 = 8;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        if (r0 == r2) goto L_0x00eb;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
    L_0x00e3:
        r0 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2 = "compression type not supported";	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0.<init>(r2);	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        throw r0;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
    L_0x00eb:
        r0 = new net.lingala.zip4j.io.ZipInputStream;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r11 = new net.lingala.zip4j.io.InflaterInputStream;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2 = r11;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r3 = r1;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r6 = r9;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r8 = r13;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2.<init>(r3, r4, r6, r8);	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0.<init>(r11);	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        return r0;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
    L_0x00fa:
        r0 = new net.lingala.zip4j.io.ZipInputStream;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r11 = new net.lingala.zip4j.io.PartInputStream;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2 = r11;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r3 = r1;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r6 = r9;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r8 = r13;	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r2.<init>(r3, r4, r6, r8);	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        r0.<init>(r11);	 Catch:{ ZipException -> 0x010b, Exception -> 0x0109 }
        return r0;
    L_0x0109:
        r0 = move-exception;
        goto L_0x0111;
    L_0x010b:
        r0 = move-exception;
        goto L_0x0120;
    L_0x010d:
        r1 = move-exception;
        r12 = r1;
        r1 = r0;
        r0 = r12;
    L_0x0111:
        if (r1 == 0) goto L_0x0116;
    L_0x0113:
        r1.close();	 Catch:{ IOException -> 0x0116 }
    L_0x0116:
        r1 = new net.lingala.zip4j.exception.ZipException;
        r1.<init>(r0);
        throw r1;
    L_0x011c:
        r1 = move-exception;
        r12 = r1;
        r1 = r0;
        r0 = r12;
    L_0x0120:
        if (r1 == 0) goto L_0x0125;
    L_0x0122:
        r1.close();	 Catch:{ IOException -> 0x0125 }
    L_0x0125:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.unzip.UnzipEngine.getInputStream():net.lingala.zip4j.io.ZipInputStream");
    }

    private void init(RandomAccessFile randomAccessFile) throws ZipException {
        if (this.localFileHeader == null) {
            throw new ZipException("local file header is null, cannot initialize input stream");
        }
        try {
            initDecrypter(randomAccessFile);
        } catch (RandomAccessFile randomAccessFile2) {
            throw randomAccessFile2;
        } catch (Throwable e) {
            throw new ZipException(e);
        }
    }

    private void initDecrypter(RandomAccessFile randomAccessFile) throws ZipException {
        if (this.localFileHeader == null) {
            throw new ZipException("local file header is null, cannot init decrypter");
        } else if (!this.localFileHeader.isEncrypted()) {
        } else {
            if (this.localFileHeader.getEncryptionMethod() == 0) {
                this.decrypter = new StandardDecrypter(this.fileHeader, getStandardDecrypterHeaderBytes(randomAccessFile));
            } else if (this.localFileHeader.getEncryptionMethod() == 99) {
                this.decrypter = new AESDecrypter(this.localFileHeader, getAESSalt(randomAccessFile), getAESPasswordVerifier(randomAccessFile));
            } else {
                throw new ZipException("unsupported encryption method");
            }
        }
    }

    private byte[] getStandardDecrypterHeaderBytes(RandomAccessFile randomAccessFile) throws ZipException {
        try {
            byte[] bArr = new byte[12];
            randomAccessFile.seek(this.localFileHeader.getOffsetStartOfData());
            randomAccessFile.read(bArr, 0, 12);
            return bArr;
        } catch (Throwable e) {
            throw new ZipException(e);
        } catch (Throwable e2) {
            throw new ZipException(e2);
        }
    }

    private byte[] getAESSalt(RandomAccessFile randomAccessFile) throws ZipException {
        if (this.localFileHeader.getAesExtraDataRecord() == null) {
            return null;
        }
        try {
            byte[] bArr = new byte[calculateAESSaltLength(this.localFileHeader.getAesExtraDataRecord())];
            randomAccessFile.seek(this.localFileHeader.getOffsetStartOfData());
            randomAccessFile.read(bArr);
            return bArr;
        } catch (Throwable e) {
            throw new ZipException(e);
        }
    }

    private byte[] getAESPasswordVerifier(RandomAccessFile randomAccessFile) throws ZipException {
        try {
            byte[] bArr = new byte[2];
            randomAccessFile.read(bArr);
            return bArr;
        } catch (Throwable e) {
            throw new ZipException(e);
        }
    }

    private int calculateAESSaltLength(AESExtraDataRecord aESExtraDataRecord) throws ZipException {
        if (aESExtraDataRecord == null) {
            throw new ZipException("unable to determine salt length: AESExtraDataRecord is null");
        }
        switch (aESExtraDataRecord.getAesStrength()) {
            case 1:
                return 8;
            case 2:
                return 12;
            case 3:
                return 16;
            default:
                throw new ZipException("unable to determine salt length: invalid aes key strength");
        }
    }

    public void checkCRC() throws ZipException {
        if (this.fileHeader == null) {
            return;
        }
        StringBuffer stringBuffer;
        if (this.fileHeader.getEncryptionMethod() == 99) {
            if (this.decrypter != null && (this.decrypter instanceof AESDecrypter)) {
                Object calculatedAuthenticationBytes = ((AESDecrypter) this.decrypter).getCalculatedAuthenticationBytes();
                byte[] storedMac = ((AESDecrypter) this.decrypter).getStoredMac();
                Object obj = new byte[10];
                if (obj != null) {
                    if (storedMac != null) {
                        System.arraycopy(calculatedAuthenticationBytes, 0, obj, 0, 10);
                        if (!Arrays.equals(obj, storedMac)) {
                            stringBuffer = new StringBuffer("invalid CRC (MAC) for file: ");
                            stringBuffer.append(this.fileHeader.getFileName());
                            throw new ZipException(stringBuffer.toString());
                        }
                        return;
                    }
                }
                stringBuffer = new StringBuffer("CRC (MAC) check failed for ");
                stringBuffer.append(this.fileHeader.getFileName());
                throw new ZipException(stringBuffer.toString());
            }
        } else if ((this.crc.getValue() & InternalZipConstants.ZIP_64_LIMIT) != this.fileHeader.getCrc32()) {
            StringBuffer stringBuffer2 = new StringBuffer("invalid CRC for file: ");
            stringBuffer2.append(this.fileHeader.getFileName());
            String stringBuffer3 = stringBuffer2.toString();
            if (this.localFileHeader.isEncrypted() && this.localFileHeader.getEncryptionMethod() == 0) {
                stringBuffer = new StringBuffer(String.valueOf(stringBuffer3));
                stringBuffer.append(" - Wrong Password?");
                stringBuffer3 = stringBuffer.toString();
            }
            throw new ZipException(stringBuffer3);
        }
    }

    private boolean checkLocalHeader() throws net.lingala.zip4j.exception.ZipException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r5 = this;
        r0 = 0;
        r1 = r5.checkSplitFile();	 Catch:{ FileNotFoundException -> 0x005c }
        if (r1 != 0) goto L_0x0024;
    L_0x0007:
        r0 = new java.io.RandomAccessFile;	 Catch:{ FileNotFoundException -> 0x001f, all -> 0x001a }
        r2 = new java.io.File;	 Catch:{ FileNotFoundException -> 0x001f, all -> 0x001a }
        r3 = r5.zipModel;	 Catch:{ FileNotFoundException -> 0x001f, all -> 0x001a }
        r3 = r3.getZipFile();	 Catch:{ FileNotFoundException -> 0x001f, all -> 0x001a }
        r2.<init>(r3);	 Catch:{ FileNotFoundException -> 0x001f, all -> 0x001a }
        r3 = "r";	 Catch:{ FileNotFoundException -> 0x001f, all -> 0x001a }
        r0.<init>(r2, r3);	 Catch:{ FileNotFoundException -> 0x001f, all -> 0x001a }
        goto L_0x0025;
    L_0x001a:
        r0 = move-exception;
        r4 = r1;
        r1 = r0;
        r0 = r4;
        goto L_0x0063;
    L_0x001f:
        r0 = move-exception;
        r4 = r1;
        r1 = r0;
        r0 = r4;
        goto L_0x005d;
    L_0x0024:
        r0 = r1;
    L_0x0025:
        r1 = new net.lingala.zip4j.core.HeaderReader;	 Catch:{ FileNotFoundException -> 0x005c }
        r1.<init>(r0);	 Catch:{ FileNotFoundException -> 0x005c }
        r2 = r5.fileHeader;	 Catch:{ FileNotFoundException -> 0x005c }
        r1 = r1.readLocalFileHeader(r2);	 Catch:{ FileNotFoundException -> 0x005c }
        r5.localFileHeader = r1;	 Catch:{ FileNotFoundException -> 0x005c }
        r1 = r5.localFileHeader;	 Catch:{ FileNotFoundException -> 0x005c }
        if (r1 != 0) goto L_0x003e;	 Catch:{ FileNotFoundException -> 0x005c }
    L_0x0036:
        r1 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ FileNotFoundException -> 0x005c }
        r2 = "error reading local file header. Is this a valid zip file?";	 Catch:{ FileNotFoundException -> 0x005c }
        r1.<init>(r2);	 Catch:{ FileNotFoundException -> 0x005c }
        throw r1;	 Catch:{ FileNotFoundException -> 0x005c }
    L_0x003e:
        r1 = r5.localFileHeader;	 Catch:{ FileNotFoundException -> 0x005c }
        r1 = r1.getCompressionMethod();	 Catch:{ FileNotFoundException -> 0x005c }
        r2 = r5.fileHeader;	 Catch:{ FileNotFoundException -> 0x005c }
        r2 = r2.getCompressionMethod();	 Catch:{ FileNotFoundException -> 0x005c }
        if (r1 == r2) goto L_0x0053;
    L_0x004c:
        if (r0 == 0) goto L_0x0051;
    L_0x004e:
        r0.close();	 Catch:{ IOException -> 0x0051, IOException -> 0x0051 }
    L_0x0051:
        r0 = 0;
        return r0;
    L_0x0053:
        if (r0 == 0) goto L_0x0058;
    L_0x0055:
        r0.close();	 Catch:{ IOException -> 0x0058, IOException -> 0x0058 }
    L_0x0058:
        r0 = 1;
        return r0;
    L_0x005a:
        r1 = move-exception;
        goto L_0x0063;
    L_0x005c:
        r1 = move-exception;
    L_0x005d:
        r2 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ all -> 0x005a }
        r2.<init>(r1);	 Catch:{ all -> 0x005a }
        throw r2;	 Catch:{ all -> 0x005a }
    L_0x0063:
        if (r0 == 0) goto L_0x0068;
    L_0x0065:
        r0.close();	 Catch:{ IOException -> 0x0068, IOException -> 0x0068 }
    L_0x0068:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.unzip.UnzipEngine.checkLocalHeader():boolean");
    }

    private RandomAccessFile checkSplitFile() throws ZipException {
        if (!this.zipModel.isSplitArchive()) {
            return null;
        }
        String zipFile;
        int diskNumberStart = this.fileHeader.getDiskNumberStart();
        int i = diskNumberStart + 1;
        this.currSplitFileCounter = i;
        String zipFile2 = this.zipModel.getZipFile();
        if (diskNumberStart == this.zipModel.getEndCentralDirRecord().getNoOfThisDisk()) {
            zipFile = this.zipModel.getZipFile();
        } else if (diskNumberStart >= 9) {
            r0 = new StringBuffer(String.valueOf(zipFile2.substring(0, zipFile2.lastIndexOf("."))));
            r0.append(".z");
            r0.append(i);
            zipFile = r0.toString();
        } else {
            r0 = new StringBuffer(String.valueOf(zipFile2.substring(0, zipFile2.lastIndexOf("."))));
            r0.append(".z0");
            r0.append(i);
            zipFile = r0.toString();
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(zipFile, InternalZipConstants.READ_MODE);
            if (this.currSplitFileCounter == 1) {
                byte[] bArr = new byte[4];
                randomAccessFile.read(bArr);
                if (((long) Raw.readIntLittleEndian(bArr, 0)) != 134695760) {
                    throw new ZipException("invalid first part split file signature");
                }
            }
            return randomAccessFile;
        } catch (Throwable e) {
            throw new ZipException(e);
        } catch (Throwable e2) {
            throw new ZipException(e2);
        }
    }

    private RandomAccessFile createFileHandler(String str) throws ZipException {
        if (this.zipModel != null) {
            if (Zip4jUtil.isStringNotNullAndNotEmpty(this.zipModel.getZipFile())) {
                try {
                    if (this.zipModel.isSplitArchive()) {
                        return checkSplitFile();
                    }
                    return new RandomAccessFile(new File(this.zipModel.getZipFile()), str);
                } catch (Throwable e) {
                    throw new ZipException(e);
                } catch (Throwable e2) {
                    throw new ZipException(e2);
                }
            }
        }
        throw new ZipException("input parameter is null in getFilePointer");
    }

    private FileOutputStream getOutputStream(String str, String str2) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(str)) {
            try {
                File file = new File(getOutputFileNameWithPath(str, str2));
                if (file.getParentFile().exists() == null) {
                    file.getParentFile().mkdirs();
                }
                if (file.exists() != null) {
                    file.delete();
                }
                return new FileOutputStream(file);
            } catch (Throwable e) {
                throw new ZipException(e);
            }
        }
        throw new ZipException("invalid output path");
    }

    private String getOutputFileNameWithPath(String str, String str2) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(str2)) {
            str2 = this.fileHeader.getFileName();
        }
        StringBuffer stringBuffer = new StringBuffer(String.valueOf(str));
        stringBuffer.append(System.getProperty("file.separator"));
        stringBuffer.append(str2);
        return stringBuffer.toString();
    }

    public RandomAccessFile startNextSplitFile() throws IOException, FileNotFoundException {
        String zipFile = this.zipModel.getZipFile();
        if (this.currSplitFileCounter == this.zipModel.getEndCentralDirRecord().getNoOfThisDisk()) {
            zipFile = this.zipModel.getZipFile();
        } else if (this.currSplitFileCounter >= 9) {
            r1 = new StringBuffer(String.valueOf(zipFile.substring(0, zipFile.lastIndexOf("."))));
            r1.append(".z");
            r1.append(this.currSplitFileCounter + 1);
            zipFile = r1.toString();
        } else {
            r1 = new StringBuffer(String.valueOf(zipFile.substring(0, zipFile.lastIndexOf("."))));
            r1.append(".z0");
            r1.append(this.currSplitFileCounter + 1);
            zipFile = r1.toString();
        }
        this.currSplitFileCounter++;
        try {
            if (Zip4jUtil.checkFileExists(zipFile)) {
                return new RandomAccessFile(zipFile, InternalZipConstants.READ_MODE);
            }
            StringBuffer stringBuffer = new StringBuffer("zip split file does not exist: ");
            stringBuffer.append(zipFile);
            throw new IOException(stringBuffer.toString());
        } catch (ZipException e) {
            throw new IOException(e.getMessage());
        }
    }

    private void closeStreams(java.io.InputStream r3, java.io.OutputStream r4) throws net.lingala.zip4j.exception.ZipException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r2 = this;
        if (r3 == 0) goto L_0x0037;
    L_0x0002:
        r3.close();	 Catch:{ IOException -> 0x0008 }
        goto L_0x0037;
    L_0x0006:
        r3 = move-exception;
        goto L_0x002b;
    L_0x0008:
        r3 = move-exception;
        if (r3 == 0) goto L_0x0031;
    L_0x000b:
        r0 = r3.getMessage();	 Catch:{ all -> 0x0006 }
        r0 = net.lingala.zip4j.util.Zip4jUtil.isStringNotNullAndNotEmpty(r0);	 Catch:{ all -> 0x0006 }
        if (r0 == 0) goto L_0x0031;	 Catch:{ all -> 0x0006 }
    L_0x0015:
        r0 = r3.getMessage();	 Catch:{ all -> 0x0006 }
        r1 = " - Wrong Password?";	 Catch:{ all -> 0x0006 }
        r0 = r0.indexOf(r1);	 Catch:{ all -> 0x0006 }
        if (r0 < 0) goto L_0x0031;	 Catch:{ all -> 0x0006 }
    L_0x0021:
        r0 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ all -> 0x0006 }
        r3 = r3.getMessage();	 Catch:{ all -> 0x0006 }
        r0.<init>(r3);	 Catch:{ all -> 0x0006 }
        throw r0;	 Catch:{ all -> 0x0006 }
    L_0x002b:
        if (r4 == 0) goto L_0x0030;
    L_0x002d:
        r4.close();	 Catch:{ IOException -> 0x0030 }
    L_0x0030:
        throw r3;
    L_0x0031:
        if (r4 == 0) goto L_0x003a;
    L_0x0033:
        r4.close();	 Catch:{ IOException -> 0x003a }
        goto L_0x003a;
    L_0x0037:
        if (r4 == 0) goto L_0x003a;
    L_0x0039:
        goto L_0x0033;
    L_0x003a:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.unzip.UnzipEngine.closeStreams(java.io.InputStream, java.io.OutputStream):void");
    }

    public void updateCRC(int i) {
        this.crc.update(i);
    }

    public void updateCRC(byte[] bArr, int i, int i2) {
        if (bArr != null) {
            this.crc.update(bArr, i, i2);
        }
    }

    public FileHeader getFileHeader() {
        return this.fileHeader;
    }

    public IDecrypter getDecrypter() {
        return this.decrypter;
    }

    public ZipModel getZipModel() {
        return this.zipModel;
    }

    public LocalFileHeader getLocalFileHeader() {
        return this.localFileHeader;
    }
}
