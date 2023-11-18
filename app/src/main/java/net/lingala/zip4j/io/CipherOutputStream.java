package net.lingala.zip4j.io;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.CRC32;
import net.lingala.zip4j.core.HeaderWriter;
import net.lingala.zip4j.crypto.AESEncrpyter;
import net.lingala.zip4j.crypto.IEncrypter;
import net.lingala.zip4j.crypto.StandardEncrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.CentralDirectory;
import net.lingala.zip4j.model.EndCentralDirRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jUtil;

public class CipherOutputStream extends BaseOutputStream {
    private long bytesWrittenForThisFile = 0;
    protected CRC32 crc = new CRC32();
    private IEncrypter encrypter;
    protected FileHeader fileHeader;
    protected LocalFileHeader localFileHeader;
    protected OutputStream outputStream;
    private byte[] pendingBuffer = new byte[16];
    private int pendingBufferLength = 0;
    private File sourceFile;
    private long totalBytesRead = 0;
    private long totalBytesWritten = 0;
    protected ZipModel zipModel;
    protected ZipParameters zipParameters;

    public CipherOutputStream(OutputStream outputStream, ZipModel zipModel) {
        this.outputStream = outputStream;
        initZipModel(zipModel);
    }

    public void putNextEntry(File file, ZipParameters zipParameters) throws ZipException {
        if (!zipParameters.isSourceExternalStream() && file == null) {
            throw new ZipException("input file is null");
        } else if (zipParameters.isSourceExternalStream() || Zip4jUtil.checkFileExists(file)) {
            try {
                this.sourceFile = file;
                this.zipParameters = (ZipParameters) zipParameters.clone();
                if (zipParameters.isSourceExternalStream() == null) {
                    if (this.sourceFile.isDirectory() != null) {
                        this.zipParameters.setEncryptFiles(false);
                        this.zipParameters.setEncryptionMethod(-1);
                        this.zipParameters.setCompressionMethod(0);
                    }
                } else if (Zip4jUtil.isStringNotNullAndNotEmpty(this.zipParameters.getFileNameInZip()) == null) {
                    throw new ZipException("file name is empty for external stream");
                } else if (!(this.zipParameters.getFileNameInZip().endsWith("/") == null && this.zipParameters.getFileNameInZip().endsWith("\\") == null)) {
                    this.zipParameters.setEncryptFiles(false);
                    this.zipParameters.setEncryptionMethod(-1);
                    this.zipParameters.setCompressionMethod(0);
                }
                createFileHeader();
                createLocalFileHeader();
                if (this.zipModel.isSplitArchive() != null && (this.zipModel.getCentralDirectory() == null || this.zipModel.getCentralDirectory().getFileHeaders() == null || this.zipModel.getCentralDirectory().getFileHeaders().size() == null)) {
                    file = new byte[4];
                    Raw.writeIntLittleEndian(file, 0, 134695760);
                    this.outputStream.write(file);
                    this.totalBytesWritten += 4;
                }
                if ((this.outputStream instanceof SplitOutputStream) != null) {
                    if (this.totalBytesWritten == 4) {
                        this.fileHeader.setOffsetLocalHeader(4);
                    } else {
                        this.fileHeader.setOffsetLocalHeader(((SplitOutputStream) this.outputStream).getFilePointer());
                    }
                } else if (this.totalBytesWritten == 4) {
                    this.fileHeader.setOffsetLocalHeader(4);
                } else {
                    this.fileHeader.setOffsetLocalHeader(this.totalBytesWritten);
                }
                this.totalBytesWritten += (long) new HeaderWriter().writeLocalFileHeader(this.zipModel, this.localFileHeader, this.outputStream);
                if (this.zipParameters.isEncryptFiles() != null) {
                    initEncrypter();
                    if (this.encrypter != null) {
                        if (zipParameters.getEncryptionMethod() == null) {
                            file = ((StandardEncrypter) this.encrypter).getHeaderBytes();
                            this.outputStream.write(file);
                            this.totalBytesWritten += (long) file.length;
                            this.bytesWrittenForThisFile += (long) file.length;
                        } else if (zipParameters.getEncryptionMethod() == 99) {
                            file = ((AESEncrpyter) this.encrypter).getSaltBytes();
                            zipParameters = ((AESEncrpyter) this.encrypter).getDerivedPasswordVerifier();
                            this.outputStream.write(file);
                            this.outputStream.write(zipParameters);
                            this.totalBytesWritten += (long) (file.length + zipParameters.length);
                            this.bytesWrittenForThisFile += (long) (file.length + zipParameters.length);
                        }
                    }
                }
                this.crc.reset();
            } catch (Throwable e) {
                throw new ZipException(e);
            } catch (File file2) {
                throw file2;
            } catch (Throwable e2) {
                throw new ZipException(e2);
            }
        } else {
            throw new ZipException("input file does not exist");
        }
    }

    private void initEncrypter() throws ZipException {
        if (this.zipParameters.isEncryptFiles()) {
            int encryptionMethod = this.zipParameters.getEncryptionMethod();
            if (encryptionMethod == 0) {
                this.encrypter = new StandardEncrypter(this.zipParameters.getPassword(), (this.localFileHeader.getLastModFileTime() & 65535) << 16);
            } else if (encryptionMethod != 99) {
                throw new ZipException("invalid encprytion method");
            } else {
                this.encrypter = new AESEncrpyter(this.zipParameters.getPassword(), this.zipParameters.getAesKeyStrength());
            }
            return;
        }
        this.encrypter = null;
    }

    private void initZipModel(ZipModel zipModel) {
        if (zipModel == null) {
            this.zipModel = new ZipModel();
        } else {
            this.zipModel = zipModel;
        }
        if (this.zipModel.getEndCentralDirRecord() == null) {
            this.zipModel.setEndCentralDirRecord(new EndCentralDirRecord());
        }
        if (this.zipModel.getCentralDirectory() == null) {
            this.zipModel.setCentralDirectory(new CentralDirectory());
        }
        if (this.zipModel.getCentralDirectory().getFileHeaders() == null) {
            this.zipModel.getCentralDirectory().setFileHeaders(new ArrayList());
        }
        if (this.zipModel.getLocalFileHeaderList() == null) {
            this.zipModel.setLocalFileHeaderList(new ArrayList());
        }
        if (!((this.outputStream instanceof SplitOutputStream) == null || ((SplitOutputStream) this.outputStream).isSplitZipFile() == null)) {
            this.zipModel.setSplitArchive(true);
            this.zipModel.setSplitLength(((SplitOutputStream) this.outputStream).getSplitLength());
        }
        this.zipModel.getEndCentralDirRecord().setSignature(InternalZipConstants.ENDSIG);
    }

    public void write(int i) throws IOException {
        write(new byte[]{(byte) i}, 0, 1);
    }

    public void write(byte[] bArr) throws IOException {
        if (bArr == null) {
            throw new NullPointerException();
        } else if (bArr.length != 0) {
            write(bArr, 0, bArr.length);
        }
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        if (i2 != 0) {
            if (this.zipParameters.isEncryptFiles() && this.zipParameters.getEncryptionMethod() == 99) {
                if (this.pendingBufferLength != 0) {
                    if (i2 >= 16 - this.pendingBufferLength) {
                        System.arraycopy(bArr, i, this.pendingBuffer, this.pendingBufferLength, 16 - this.pendingBufferLength);
                        encryptAndWrite(this.pendingBuffer, 0, this.pendingBuffer.length);
                        i = 16 - this.pendingBufferLength;
                        i2 -= i;
                        this.pendingBufferLength = 0;
                    } else {
                        System.arraycopy(bArr, i, this.pendingBuffer, this.pendingBufferLength, i2);
                        this.pendingBufferLength += i2;
                        return;
                    }
                }
                if (i2 != 0) {
                    int i3 = i2 % 16;
                    if (i3 != 0) {
                        System.arraycopy(bArr, (i2 + i) - i3, this.pendingBuffer, 0, i3);
                        this.pendingBufferLength = i3;
                        i2 -= this.pendingBufferLength;
                    }
                }
            }
            if (i2 != 0) {
                encryptAndWrite(bArr, i, i2);
            }
        }
    }

    private void encryptAndWrite(byte[] bArr, int i, int i2) throws IOException {
        if (this.encrypter != null) {
            try {
                this.encrypter.encryptData(bArr, i, i2);
            } catch (byte[] bArr2) {
                throw new IOException(bArr2.getMessage());
            }
        }
        this.outputStream.write(bArr2, i, i2);
        long j = (long) i2;
        this.totalBytesWritten += j;
        this.bytesWrittenForThisFile += j;
    }

    public void closeEntry() throws IOException, ZipException {
        if (this.pendingBufferLength != 0) {
            encryptAndWrite(this.pendingBuffer, 0, this.pendingBufferLength);
            this.pendingBufferLength = 0;
        }
        if (this.zipParameters.isEncryptFiles() && this.zipParameters.getEncryptionMethod() == 99) {
            if (this.encrypter instanceof AESEncrpyter) {
                this.outputStream.write(((AESEncrpyter) this.encrypter).getFinalMac());
                this.bytesWrittenForThisFile += 10;
                this.totalBytesWritten += 10;
            } else {
                throw new ZipException("invalid encrypter for AES encrypted file");
            }
        }
        this.fileHeader.setCompressedSize(this.bytesWrittenForThisFile);
        this.localFileHeader.setCompressedSize(this.bytesWrittenForThisFile);
        if (this.zipParameters.isSourceExternalStream()) {
            this.fileHeader.setUncompressedSize(this.totalBytesRead);
            if (this.localFileHeader.getUncompressedSize() != this.totalBytesRead) {
                this.localFileHeader.setUncompressedSize(this.totalBytesRead);
            }
        }
        long value = this.crc.getValue();
        if (this.fileHeader.isEncrypted() && this.fileHeader.getEncryptionMethod() == 99) {
            value = 0;
        }
        if (this.zipParameters.isEncryptFiles() && this.zipParameters.getEncryptionMethod() == 99) {
            this.fileHeader.setCrc32(0);
            this.localFileHeader.setCrc32(0);
        } else {
            this.fileHeader.setCrc32(value);
            this.localFileHeader.setCrc32(value);
        }
        this.zipModel.getLocalFileHeaderList().add(this.localFileHeader);
        this.zipModel.getCentralDirectory().getFileHeaders().add(this.fileHeader);
        this.totalBytesWritten += (long) new HeaderWriter().writeExtendedLocalHeader(this.localFileHeader, this.outputStream);
        this.crc.reset();
        this.bytesWrittenForThisFile = 0;
        this.encrypter = null;
        this.totalBytesRead = 0;
    }

    public void finish() throws IOException, ZipException {
        this.zipModel.getEndCentralDirRecord().setOffsetOfStartOfCentralDir(this.totalBytesWritten);
        new HeaderWriter().finalizeZipFile(this.zipModel, this.outputStream);
    }

    public void close() throws IOException {
        if (this.outputStream != null) {
            this.outputStream.close();
        }
    }

    private void createFileHeader() throws ZipException {
        this.fileHeader = new FileHeader();
        this.fileHeader.setSignature(33639248);
        this.fileHeader.setVersionMadeBy(20);
        this.fileHeader.setVersionNeededToExtract(20);
        if (this.zipParameters.isEncryptFiles() && this.zipParameters.getEncryptionMethod() == 99) {
            this.fileHeader.setCompressionMethod(99);
            this.fileHeader.setAesExtraDataRecord(generateAESExtraDataRecord(this.zipParameters));
        } else {
            this.fileHeader.setCompressionMethod(this.zipParameters.getCompressionMethod());
        }
        if (this.zipParameters.isEncryptFiles()) {
            this.fileHeader.setEncrypted(true);
            this.fileHeader.setEncryptionMethod(this.zipParameters.getEncryptionMethod());
        }
        if (this.zipParameters.isSourceExternalStream()) {
            this.fileHeader.setLastModFileTime((int) Zip4jUtil.javaToDosTime(System.currentTimeMillis()));
            if (Zip4jUtil.isStringNotNullAndNotEmpty(this.zipParameters.getFileNameInZip())) {
                String fileNameInZip = this.zipParameters.getFileNameInZip();
            } else {
                throw new ZipException("fileNameInZip is null or empty");
            }
        }
        this.fileHeader.setLastModFileTime((int) Zip4jUtil.javaToDosTime(Zip4jUtil.getLastModifiedFileTime(this.sourceFile, this.zipParameters.getTimeZone())));
        this.fileHeader.setUncompressedSize(this.sourceFile.length());
        fileNameInZip = Zip4jUtil.getRelativeFileName(this.sourceFile.getAbsolutePath(), this.zipParameters.getRootFolderInZip(), this.zipParameters.getDefaultFolderPath());
        if (Zip4jUtil.isStringNotNullAndNotEmpty(fileNameInZip)) {
            this.fileHeader.setFileName(fileNameInZip);
            if (Zip4jUtil.isStringNotNullAndNotEmpty(this.zipModel.getFileNameCharset())) {
                this.fileHeader.setFileNameLength(Zip4jUtil.getEncodedStringLength(fileNameInZip, this.zipModel.getFileNameCharset()));
            } else {
                this.fileHeader.setFileNameLength(Zip4jUtil.getEncodedStringLength(fileNameInZip));
            }
            if (this.outputStream instanceof SplitOutputStream) {
                this.fileHeader.setDiskNumberStart(((SplitOutputStream) this.outputStream).getCurrSplitFileCounter());
            } else {
                this.fileHeader.setDiskNumberStart(0);
            }
            byte[] bArr = new byte[4];
            bArr[0] = (byte) (!this.zipParameters.isSourceExternalStream() ? getFileAttributes(this.sourceFile) : 0);
            this.fileHeader.setExternalFileAttr(bArr);
            if (this.zipParameters.isSourceExternalStream()) {
                FileHeader fileHeader = this.fileHeader;
                boolean z = fileNameInZip.endsWith("/") || fileNameInZip.endsWith("\\");
                fileHeader.setDirectory(z);
            } else {
                this.fileHeader.setDirectory(this.sourceFile.isDirectory());
            }
            if (this.fileHeader.isDirectory()) {
                this.fileHeader.setCompressedSize(0);
                this.fileHeader.setUncompressedSize(0);
            } else if (!this.zipParameters.isSourceExternalStream()) {
                long fileLengh = Zip4jUtil.getFileLengh(this.sourceFile);
                if (this.zipParameters.getCompressionMethod() != 0) {
                    this.fileHeader.setCompressedSize(0);
                } else if (this.zipParameters.getEncryptionMethod() == 0) {
                    this.fileHeader.setCompressedSize(fileLengh + 12);
                } else if (this.zipParameters.getEncryptionMethod() == 99) {
                    int aesKeyStrength = this.zipParameters.getAesKeyStrength();
                    if (aesKeyStrength == 1) {
                        aesKeyStrength = 8;
                    } else if (aesKeyStrength != 3) {
                        throw new ZipException("invalid aes key strength, cannot determine key sizes");
                    } else {
                        aesKeyStrength = 16;
                    }
                    this.fileHeader.setCompressedSize(((fileLengh + ((long) aesKeyStrength)) + 10) + 2);
                } else {
                    this.fileHeader.setCompressedSize(0);
                }
                this.fileHeader.setUncompressedSize(fileLengh);
            }
            if (this.zipParameters.isEncryptFiles() && this.zipParameters.getEncryptionMethod() == 0) {
                this.fileHeader.setCrc32((long) this.zipParameters.getSourceFileCRC());
            }
            byte[] bArr2 = new byte[2];
            bArr2[0] = Raw.bitArrayToByte(generateGeneralPurposeBitArray(this.fileHeader.isEncrypted(), this.zipParameters.getCompressionMethod()));
            boolean isStringNotNullAndNotEmpty = Zip4jUtil.isStringNotNullAndNotEmpty(this.zipModel.getFileNameCharset());
            if (!(isStringNotNullAndNotEmpty && this.zipModel.getFileNameCharset().equalsIgnoreCase(InternalZipConstants.CHARSET_UTF8)) && (isStringNotNullAndNotEmpty || !Zip4jUtil.detectCharSet(this.fileHeader.getFileName()).equals(InternalZipConstants.CHARSET_UTF8))) {
                bArr2[1] = (byte) 0;
            } else {
                bArr2[1] = (byte) 8;
            }
            this.fileHeader.setGeneralPurposeFlag(bArr2);
            return;
        }
        throw new ZipException("fileName is null or empty. unable to create file header");
    }

    private void createLocalFileHeader() throws ZipException {
        if (this.fileHeader == null) {
            throw new ZipException("file header is null, cannot create local file header");
        }
        this.localFileHeader = new LocalFileHeader();
        this.localFileHeader.setSignature(67324752);
        this.localFileHeader.setVersionNeededToExtract(this.fileHeader.getVersionNeededToExtract());
        this.localFileHeader.setCompressionMethod(this.fileHeader.getCompressionMethod());
        this.localFileHeader.setLastModFileTime(this.fileHeader.getLastModFileTime());
        this.localFileHeader.setUncompressedSize(this.fileHeader.getUncompressedSize());
        this.localFileHeader.setFileNameLength(this.fileHeader.getFileNameLength());
        this.localFileHeader.setFileName(this.fileHeader.getFileName());
        this.localFileHeader.setEncrypted(this.fileHeader.isEncrypted());
        this.localFileHeader.setEncryptionMethod(this.fileHeader.getEncryptionMethod());
        this.localFileHeader.setAesExtraDataRecord(this.fileHeader.getAesExtraDataRecord());
        this.localFileHeader.setCrc32(this.fileHeader.getCrc32());
        this.localFileHeader.setCompressedSize(this.fileHeader.getCompressedSize());
        this.localFileHeader.setGeneralPurposeFlag((byte[]) this.fileHeader.getGeneralPurposeFlag().clone());
    }

    private int getFileAttributes(File file) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null, cannot get file attributes");
        } else if (!file.exists()) {
            return 0;
        } else {
            if (file.isDirectory()) {
                return file.isHidden() != null ? 18 : 16;
            } else {
                if (!file.canWrite() && file.isHidden()) {
                    return 3;
                }
                if (!file.canWrite()) {
                    return 1;
                }
                if (file.isHidden() != null) {
                    return 2;
                }
                return 0;
            }
        }
    }

    private int[] generateGeneralPurposeBitArray(boolean z, int i) {
        int[] iArr = new int[8];
        if (z) {
            iArr[0] = 1;
        } else {
            iArr[0] = 0;
        }
        if (i != 8) {
            iArr[1] = 0;
            iArr[true] = 0;
        }
        iArr[true] = 1;
        return iArr;
    }

    private AESExtraDataRecord generateAESExtraDataRecord(ZipParameters zipParameters) throws ZipException {
        if (zipParameters == null) {
            throw new ZipException("zip parameters are null, cannot generate AES Extra Data record");
        }
        AESExtraDataRecord aESExtraDataRecord = new AESExtraDataRecord();
        aESExtraDataRecord.setSignature(39169);
        aESExtraDataRecord.setDataSize(7);
        aESExtraDataRecord.setVendorID("AE");
        aESExtraDataRecord.setVersionNumber(2);
        if (zipParameters.getAesKeyStrength() == 1) {
            aESExtraDataRecord.setAesStrength(1);
        } else if (zipParameters.getAesKeyStrength() == 3) {
            aESExtraDataRecord.setAesStrength(3);
        } else {
            throw new ZipException("invalid AES key strength, cannot generate AES Extra data record");
        }
        aESExtraDataRecord.setCompressionMethod(zipParameters.getCompressionMethod());
        return aESExtraDataRecord;
    }

    public void decrementCompressedFileSize(int i) {
        if (i > 0) {
            long j = (long) i;
            if (j <= this.bytesWrittenForThisFile) {
                this.bytesWrittenForThisFile -= j;
            }
        }
    }

    protected void updateTotalBytesRead(int i) {
        if (i > 0) {
            this.totalBytesRead += (long) i;
        }
    }

    public void setSourceFile(File file) {
        this.sourceFile = file;
    }

    public File getSourceFile() {
        return this.sourceFile;
    }
}
