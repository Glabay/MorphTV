package net.lingala.zip4j.core;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.SplitOutputStream;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.Zip64EndCentralDirLocator;
import net.lingala.zip4j.model.Zip64EndCentralDirRecord;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jUtil;

public class HeaderWriter {
    private final int ZIP64_EXTRA_BUF = 50;

    public int writeLocalFileHeader(ZipModel zipModel, LocalFileHeader localFileHeader, OutputStream outputStream) throws ZipException {
        HeaderWriter headerWriter = this;
        LocalFileHeader localFileHeader2 = localFileHeader;
        if (localFileHeader2 == null) {
            throw new ZipException("input parameters are null, cannot write local file header");
        }
        try {
            Object obj;
            List arrayList = new ArrayList();
            byte[] bArr = new byte[2];
            Object obj2 = new byte[4];
            Object obj3 = new byte[8];
            byte[] bArr2 = new byte[8];
            Raw.writeIntLittleEndian(obj2, 0, localFileHeader.getSignature());
            copyByteArrayToArrayList(obj2, arrayList);
            Raw.writeShortLittleEndian(bArr, 0, (short) localFileHeader.getVersionNeededToExtract());
            copyByteArrayToArrayList(bArr, arrayList);
            copyByteArrayToArrayList(localFileHeader.getGeneralPurposeFlag(), arrayList);
            Raw.writeShortLittleEndian(bArr, 0, (short) localFileHeader.getCompressionMethod());
            copyByteArrayToArrayList(bArr, arrayList);
            Raw.writeIntLittleEndian(obj2, 0, localFileHeader.getLastModFileTime());
            copyByteArrayToArrayList(obj2, arrayList);
            Raw.writeIntLittleEndian(obj2, 0, (int) localFileHeader.getCrc32());
            copyByteArrayToArrayList(obj2, arrayList);
            if (localFileHeader.getUncompressedSize() + 50 >= InternalZipConstants.ZIP_64_LIMIT) {
                Raw.writeLongLittleEndian(obj3, 0, InternalZipConstants.ZIP_64_LIMIT);
                System.arraycopy(obj3, 0, obj2, 0, 4);
                copyByteArrayToArrayList(obj2, arrayList);
                copyByteArrayToArrayList(obj2, arrayList);
                zipModel.setZip64Format(true);
                localFileHeader2.setWriteComprSizeInZip64ExtraRecord(true);
                obj = 1;
            } else {
                ZipModel zipModel2 = zipModel;
                Raw.writeLongLittleEndian(obj3, 0, localFileHeader.getCompressedSize());
                System.arraycopy(obj3, 0, obj2, 0, 4);
                copyByteArrayToArrayList(obj2, arrayList);
                Raw.writeLongLittleEndian(obj3, 0, localFileHeader.getUncompressedSize());
                System.arraycopy(obj3, 0, obj2, 0, 4);
                copyByteArrayToArrayList(obj2, arrayList);
                localFileHeader2.setWriteComprSizeInZip64ExtraRecord(false);
                obj = null;
            }
            Raw.writeShortLittleEndian(bArr, 0, (short) localFileHeader.getFileNameLength());
            copyByteArrayToArrayList(bArr, arrayList);
            int i = obj != null ? 20 : 0;
            if (localFileHeader.getAesExtraDataRecord() != null) {
                i += 11;
            }
            Raw.writeShortLittleEndian(bArr, 0, (short) i);
            copyByteArrayToArrayList(bArr, arrayList);
            if (Zip4jUtil.isStringNotNullAndNotEmpty(zipModel.getFileNameCharset())) {
                copyByteArrayToArrayList(localFileHeader.getFileName().getBytes(zipModel.getFileNameCharset()), arrayList);
            } else {
                copyByteArrayToArrayList(Zip4jUtil.convertCharset(localFileHeader.getFileName()), arrayList);
            }
            if (obj != null) {
                Raw.writeShortLittleEndian(bArr, 0, (short) 1);
                copyByteArrayToArrayList(bArr, arrayList);
                Raw.writeShortLittleEndian(bArr, 0, (short) 16);
                copyByteArrayToArrayList(bArr, arrayList);
                Raw.writeLongLittleEndian(obj3, 0, localFileHeader.getUncompressedSize());
                copyByteArrayToArrayList(obj3, arrayList);
                copyByteArrayToArrayList(bArr2, arrayList);
            }
            if (localFileHeader.getAesExtraDataRecord() != null) {
                AESExtraDataRecord aesExtraDataRecord = localFileHeader.getAesExtraDataRecord();
                Raw.writeShortLittleEndian(bArr, 0, (short) ((int) aesExtraDataRecord.getSignature()));
                copyByteArrayToArrayList(bArr, arrayList);
                Raw.writeShortLittleEndian(bArr, 0, (short) aesExtraDataRecord.getDataSize());
                copyByteArrayToArrayList(bArr, arrayList);
                Raw.writeShortLittleEndian(bArr, 0, (short) aesExtraDataRecord.getVersionNumber());
                copyByteArrayToArrayList(bArr, arrayList);
                copyByteArrayToArrayList(aesExtraDataRecord.getVendorID().getBytes(), arrayList);
                copyByteArrayToArrayList(new byte[]{(byte) aesExtraDataRecord.getAesStrength()}, arrayList);
                Raw.writeShortLittleEndian(bArr, 0, (short) aesExtraDataRecord.getCompressionMethod());
                copyByteArrayToArrayList(bArr, arrayList);
            }
            byte[] byteArrayListToByteArray = byteArrayListToByteArray(arrayList);
            outputStream.write(byteArrayListToByteArray);
            return byteArrayListToByteArray.length;
        } catch (ZipException e) {
            throw e;
        } catch (Throwable e2) {
            throw new ZipException(e2);
        }
    }

    public int writeExtendedLocalHeader(LocalFileHeader localFileHeader, OutputStream outputStream) throws ZipException, IOException {
        if (localFileHeader != null) {
            if (outputStream != null) {
                List arrayList = new ArrayList();
                byte[] bArr = new byte[4];
                Raw.writeIntLittleEndian(bArr, 0, 134695760);
                copyByteArrayToArrayList(bArr, arrayList);
                Raw.writeIntLittleEndian(bArr, 0, (int) localFileHeader.getCrc32());
                copyByteArrayToArrayList(bArr, arrayList);
                long compressedSize = localFileHeader.getCompressedSize();
                if (compressedSize >= 2147483647L) {
                    compressedSize = 2147483647L;
                }
                Raw.writeIntLittleEndian(bArr, 0, (int) compressedSize);
                copyByteArrayToArrayList(bArr, arrayList);
                compressedSize = localFileHeader.getUncompressedSize();
                if (compressedSize >= 2147483647L) {
                    compressedSize = 2147483647L;
                }
                Raw.writeIntLittleEndian(bArr, 0, (int) compressedSize);
                copyByteArrayToArrayList(bArr, arrayList);
                localFileHeader = byteArrayListToByteArray(arrayList);
                outputStream.write(localFileHeader);
                return localFileHeader.length;
            }
        }
        throw new ZipException("input parameters is null, cannot write extended local header");
    }

    public void finalizeZipFile(ZipModel zipModel, OutputStream outputStream) throws ZipException {
        if (zipModel != null) {
            if (outputStream != null) {
                try {
                    processHeaderData(zipModel, outputStream);
                    long offsetOfStartOfCentralDir = zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir();
                    List arrayList = new ArrayList();
                    int writeCentralDirectory = writeCentralDirectory(zipModel, outputStream, arrayList);
                    if (zipModel.isZip64Format()) {
                        if (zipModel.getZip64EndCentralDirRecord() == null) {
                            zipModel.setZip64EndCentralDirRecord(new Zip64EndCentralDirRecord());
                        }
                        if (zipModel.getZip64EndCentralDirLocator() == null) {
                            zipModel.setZip64EndCentralDirLocator(new Zip64EndCentralDirLocator());
                        }
                        zipModel.getZip64EndCentralDirLocator().setOffsetZip64EndOfCentralDirRec(offsetOfStartOfCentralDir + ((long) writeCentralDirectory));
                        if (outputStream instanceof SplitOutputStream) {
                            zipModel.getZip64EndCentralDirLocator().setNoOfDiskStartOfZip64EndOfCentralDirRec(((SplitOutputStream) outputStream).getCurrSplitFileCounter());
                            zipModel.getZip64EndCentralDirLocator().setTotNumberOfDiscs(((SplitOutputStream) outputStream).getCurrSplitFileCounter() + 1);
                        } else {
                            zipModel.getZip64EndCentralDirLocator().setNoOfDiskStartOfZip64EndOfCentralDirRec(0);
                            zipModel.getZip64EndCentralDirLocator().setTotNumberOfDiscs(1);
                        }
                        writeZip64EndOfCentralDirectoryRecord(zipModel, outputStream, writeCentralDirectory, offsetOfStartOfCentralDir, arrayList);
                        writeZip64EndOfCentralDirectoryLocator(zipModel, outputStream, arrayList);
                    }
                    writeEndOfCentralDirectoryRecord(zipModel, outputStream, writeCentralDirectory, offsetOfStartOfCentralDir, arrayList);
                    writeZipHeaderBytes(zipModel, outputStream, byteArrayListToByteArray(arrayList));
                    return;
                } catch (ZipModel zipModel2) {
                    throw zipModel2;
                } catch (Throwable e) {
                    throw new ZipException(e);
                }
            }
        }
        throw new ZipException("input parameters is null, cannot finalize zip file");
    }

    public void finalizeZipFileWithoutValidations(ZipModel zipModel, OutputStream outputStream) throws ZipException {
        if (zipModel != null) {
            if (outputStream != null) {
                try {
                    List arrayList = new ArrayList();
                    long offsetOfStartOfCentralDir = zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir();
                    int writeCentralDirectory = writeCentralDirectory(zipModel, outputStream, arrayList);
                    if (zipModel.isZip64Format()) {
                        if (zipModel.getZip64EndCentralDirRecord() == null) {
                            zipModel.setZip64EndCentralDirRecord(new Zip64EndCentralDirRecord());
                        }
                        if (zipModel.getZip64EndCentralDirLocator() == null) {
                            zipModel.setZip64EndCentralDirLocator(new Zip64EndCentralDirLocator());
                        }
                        zipModel.getZip64EndCentralDirLocator().setOffsetZip64EndOfCentralDirRec(offsetOfStartOfCentralDir + ((long) writeCentralDirectory));
                        writeZip64EndOfCentralDirectoryRecord(zipModel, outputStream, writeCentralDirectory, offsetOfStartOfCentralDir, arrayList);
                        writeZip64EndOfCentralDirectoryLocator(zipModel, outputStream, arrayList);
                    }
                    writeEndOfCentralDirectoryRecord(zipModel, outputStream, writeCentralDirectory, offsetOfStartOfCentralDir, arrayList);
                    writeZipHeaderBytes(zipModel, outputStream, byteArrayListToByteArray(arrayList));
                    return;
                } catch (ZipModel zipModel2) {
                    throw zipModel2;
                } catch (Throwable e) {
                    throw new ZipException(e);
                }
            }
        }
        throw new ZipException("input parameters is null, cannot finalize zip file without validations");
    }

    private void writeZipHeaderBytes(ZipModel zipModel, OutputStream outputStream, byte[] bArr) throws ZipException {
        if (bArr == null) {
            throw new ZipException("invalid buff to write as zip headers");
        }
        try {
            if ((outputStream instanceof SplitOutputStream) && ((SplitOutputStream) outputStream).checkBuffSizeAndStartNextSplitFile(bArr.length)) {
                finalizeZipFile(zipModel, outputStream);
            } else {
                outputStream.write(bArr);
            }
        } catch (Throwable e) {
            throw new ZipException(e);
        }
    }

    private void processHeaderData(ZipModel zipModel, OutputStream outputStream) throws ZipException {
        int i = 0;
        try {
            if (outputStream instanceof SplitOutputStream) {
                zipModel.getEndCentralDirRecord().setOffsetOfStartOfCentralDir(((SplitOutputStream) outputStream).getFilePointer());
                i = ((SplitOutputStream) outputStream).getCurrSplitFileCounter();
            }
            if (zipModel.isZip64Format() != null) {
                if (zipModel.getZip64EndCentralDirRecord() == null) {
                    zipModel.setZip64EndCentralDirRecord(new Zip64EndCentralDirRecord());
                }
                if (zipModel.getZip64EndCentralDirLocator() == null) {
                    zipModel.setZip64EndCentralDirLocator(new Zip64EndCentralDirLocator());
                }
                zipModel.getZip64EndCentralDirLocator().setNoOfDiskStartOfZip64EndOfCentralDirRec(i);
                zipModel.getZip64EndCentralDirLocator().setTotNumberOfDiscs(i + 1);
            }
            zipModel.getEndCentralDirRecord().setNoOfThisDisk(i);
            zipModel.getEndCentralDirRecord().setNoOfThisDiskStartOfCentralDir(i);
        } catch (Throwable e) {
            throw new ZipException(e);
        }
    }

    private int writeCentralDirectory(ZipModel zipModel, OutputStream outputStream, List list) throws ZipException {
        if (zipModel != null) {
            if (outputStream != null) {
                int i = 0;
                if (!(zipModel.getCentralDirectory() == null || zipModel.getCentralDirectory().getFileHeaders() == null)) {
                    if (zipModel.getCentralDirectory().getFileHeaders().size() > 0) {
                        int i2 = 0;
                        while (i < zipModel.getCentralDirectory().getFileHeaders().size()) {
                            i2 += writeFileHeader(zipModel, (FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(i), outputStream, list);
                            i++;
                        }
                        return i2;
                    }
                }
                return 0;
            }
        }
        throw new ZipException("input parameters is null, cannot write central directory");
    }

    private int writeFileHeader(ZipModel zipModel, FileHeader fileHeader, OutputStream outputStream, List list) throws ZipException {
        HeaderWriter headerWriter = this;
        List list2 = list;
        if (fileHeader != null) {
            if (outputStream != null) {
                try {
                    byte[] bArr;
                    Object obj;
                    Object obj2;
                    int i;
                    byte[] bytes;
                    AESExtraDataRecord aesExtraDataRecord;
                    byte[] bArr2 = new byte[2];
                    int i2 = 4;
                    Object obj3 = new byte[4];
                    Object obj4 = new byte[8];
                    byte[] bArr3 = new byte[2];
                    byte[] bArr4 = new byte[4];
                    Raw.writeIntLittleEndian(obj3, 0, fileHeader.getSignature());
                    copyByteArrayToArrayList(obj3, list2);
                    Raw.writeShortLittleEndian(bArr2, 0, (short) fileHeader.getVersionMadeBy());
                    copyByteArrayToArrayList(bArr2, list2);
                    Raw.writeShortLittleEndian(bArr2, 0, (short) fileHeader.getVersionNeededToExtract());
                    copyByteArrayToArrayList(bArr2, list2);
                    copyByteArrayToArrayList(fileHeader.getGeneralPurposeFlag(), list2);
                    Raw.writeShortLittleEndian(bArr2, 0, (short) fileHeader.getCompressionMethod());
                    copyByteArrayToArrayList(bArr2, list2);
                    Raw.writeIntLittleEndian(obj3, 0, fileHeader.getLastModFileTime());
                    copyByteArrayToArrayList(obj3, list2);
                    Raw.writeIntLittleEndian(obj3, 0, (int) fileHeader.getCrc32());
                    copyByteArrayToArrayList(obj3, list2);
                    byte[] bArr5 = bArr2;
                    if (fileHeader.getCompressedSize() < InternalZipConstants.ZIP_64_LIMIT) {
                        if (fileHeader.getUncompressedSize() + 50 < InternalZipConstants.ZIP_64_LIMIT) {
                            Raw.writeLongLittleEndian(obj4, 0, fileHeader.getCompressedSize());
                            System.arraycopy(obj4, 0, obj3, 0, 4);
                            copyByteArrayToArrayList(obj3, list2);
                            Raw.writeLongLittleEndian(obj4, 0, fileHeader.getUncompressedSize());
                            System.arraycopy(obj4, 0, obj3, 0, 4);
                            copyByteArrayToArrayList(obj3, list2);
                            obj3 = null;
                            bArr = bArr5;
                            Raw.writeShortLittleEndian(bArr, 0, (short) fileHeader.getFileNameLength());
                            copyByteArrayToArrayList(bArr, list2);
                            obj = new byte[4];
                            if (fileHeader.getOffsetLocalHeader() <= InternalZipConstants.ZIP_64_LIMIT) {
                                Raw.writeLongLittleEndian(obj4, 0, InternalZipConstants.ZIP_64_LIMIT);
                                System.arraycopy(obj4, 0, obj, 0, 4);
                                obj2 = 1;
                            } else {
                                Raw.writeLongLittleEndian(obj4, 0, fileHeader.getOffsetLocalHeader());
                                System.arraycopy(obj4, 0, obj, 0, 4);
                                obj2 = null;
                            }
                            if (obj3 == null) {
                                if (obj2 != null) {
                                    i = 0;
                                    if (fileHeader.getAesExtraDataRecord() != null) {
                                        i += 11;
                                    }
                                    Raw.writeShortLittleEndian(bArr, 0, (short) i);
                                    copyByteArrayToArrayList(bArr, list2);
                                    copyByteArrayToArrayList(bArr3, list2);
                                    Raw.writeShortLittleEndian(bArr, 0, (short) fileHeader.getDiskNumberStart());
                                    copyByteArrayToArrayList(bArr, list2);
                                    copyByteArrayToArrayList(bArr3, list2);
                                    if (fileHeader.getExternalFileAttr() == null) {
                                        copyByteArrayToArrayList(fileHeader.getExternalFileAttr(), list2);
                                    } else {
                                        copyByteArrayToArrayList(bArr4, list2);
                                    }
                                    copyByteArrayToArrayList(obj, list2);
                                    if (Zip4jUtil.isStringNotNullAndNotEmpty(zipModel.getFileNameCharset())) {
                                        copyByteArrayToArrayList(Zip4jUtil.convertCharset(fileHeader.getFileName()), list2);
                                        i = 46 + Zip4jUtil.getEncodedStringLength(fileHeader.getFileName());
                                    } else {
                                        bytes = fileHeader.getFileName().getBytes(zipModel.getFileNameCharset());
                                        copyByteArrayToArrayList(bytes, list2);
                                        i = 46 + bytes.length;
                                    }
                                    if (!(obj3 == null && obj2 == null)) {
                                        zipModel.setZip64Format(true);
                                        Raw.writeShortLittleEndian(bArr, 0, (short) 1);
                                        copyByteArrayToArrayList(bArr, list2);
                                        i += 2;
                                        i2 = obj3 == null ? 16 : 0;
                                        if (obj2 != null) {
                                            i2 += 8;
                                        }
                                        Raw.writeShortLittleEndian(bArr, 0, (short) i2);
                                        copyByteArrayToArrayList(bArr, list2);
                                        i += 2;
                                        if (obj3 != null) {
                                            Raw.writeLongLittleEndian(obj4, 0, fileHeader.getUncompressedSize());
                                            copyByteArrayToArrayList(obj4, list2);
                                            i += 8;
                                            Raw.writeLongLittleEndian(obj4, 0, fileHeader.getCompressedSize());
                                            copyByteArrayToArrayList(obj4, list2);
                                            i += 8;
                                        }
                                        if (obj2 != null) {
                                            Raw.writeLongLittleEndian(obj4, 0, fileHeader.getOffsetLocalHeader());
                                            copyByteArrayToArrayList(obj4, list2);
                                            i += 8;
                                        }
                                    }
                                    if (fileHeader.getAesExtraDataRecord() == null) {
                                        return i;
                                    }
                                    aesExtraDataRecord = fileHeader.getAesExtraDataRecord();
                                    Raw.writeShortLittleEndian(bArr, 0, (short) ((int) aesExtraDataRecord.getSignature()));
                                    copyByteArrayToArrayList(bArr, list2);
                                    Raw.writeShortLittleEndian(bArr, 0, (short) aesExtraDataRecord.getDataSize());
                                    copyByteArrayToArrayList(bArr, list2);
                                    Raw.writeShortLittleEndian(bArr, 0, (short) aesExtraDataRecord.getVersionNumber());
                                    copyByteArrayToArrayList(bArr, list2);
                                    copyByteArrayToArrayList(aesExtraDataRecord.getVendorID().getBytes(), list2);
                                    copyByteArrayToArrayList(new byte[]{(byte) aesExtraDataRecord.getAesStrength()}, list2);
                                    Raw.writeShortLittleEndian(bArr, 0, (short) aesExtraDataRecord.getCompressionMethod());
                                    copyByteArrayToArrayList(bArr, list2);
                                    return i + 11;
                                }
                            }
                            if (obj3 != null) {
                                i2 = 20;
                            }
                            i = obj2 == null ? i2 + 8 : i2;
                            if (fileHeader.getAesExtraDataRecord() != null) {
                                i += 11;
                            }
                            Raw.writeShortLittleEndian(bArr, 0, (short) i);
                            copyByteArrayToArrayList(bArr, list2);
                            copyByteArrayToArrayList(bArr3, list2);
                            Raw.writeShortLittleEndian(bArr, 0, (short) fileHeader.getDiskNumberStart());
                            copyByteArrayToArrayList(bArr, list2);
                            copyByteArrayToArrayList(bArr3, list2);
                            if (fileHeader.getExternalFileAttr() == null) {
                                copyByteArrayToArrayList(bArr4, list2);
                            } else {
                                copyByteArrayToArrayList(fileHeader.getExternalFileAttr(), list2);
                            }
                            copyByteArrayToArrayList(obj, list2);
                            if (Zip4jUtil.isStringNotNullAndNotEmpty(zipModel.getFileNameCharset())) {
                                copyByteArrayToArrayList(Zip4jUtil.convertCharset(fileHeader.getFileName()), list2);
                                i = 46 + Zip4jUtil.getEncodedStringLength(fileHeader.getFileName());
                            } else {
                                bytes = fileHeader.getFileName().getBytes(zipModel.getFileNameCharset());
                                copyByteArrayToArrayList(bytes, list2);
                                i = 46 + bytes.length;
                            }
                            zipModel.setZip64Format(true);
                            Raw.writeShortLittleEndian(bArr, 0, (short) 1);
                            copyByteArrayToArrayList(bArr, list2);
                            i += 2;
                            if (obj3 == null) {
                            }
                            if (obj2 != null) {
                                i2 += 8;
                            }
                            Raw.writeShortLittleEndian(bArr, 0, (short) i2);
                            copyByteArrayToArrayList(bArr, list2);
                            i += 2;
                            if (obj3 != null) {
                                Raw.writeLongLittleEndian(obj4, 0, fileHeader.getUncompressedSize());
                                copyByteArrayToArrayList(obj4, list2);
                                i += 8;
                                Raw.writeLongLittleEndian(obj4, 0, fileHeader.getCompressedSize());
                                copyByteArrayToArrayList(obj4, list2);
                                i += 8;
                            }
                            if (obj2 != null) {
                                Raw.writeLongLittleEndian(obj4, 0, fileHeader.getOffsetLocalHeader());
                                copyByteArrayToArrayList(obj4, list2);
                                i += 8;
                            }
                            if (fileHeader.getAesExtraDataRecord() == null) {
                                return i;
                            }
                            aesExtraDataRecord = fileHeader.getAesExtraDataRecord();
                            Raw.writeShortLittleEndian(bArr, 0, (short) ((int) aesExtraDataRecord.getSignature()));
                            copyByteArrayToArrayList(bArr, list2);
                            Raw.writeShortLittleEndian(bArr, 0, (short) aesExtraDataRecord.getDataSize());
                            copyByteArrayToArrayList(bArr, list2);
                            Raw.writeShortLittleEndian(bArr, 0, (short) aesExtraDataRecord.getVersionNumber());
                            copyByteArrayToArrayList(bArr, list2);
                            copyByteArrayToArrayList(aesExtraDataRecord.getVendorID().getBytes(), list2);
                            copyByteArrayToArrayList(new byte[]{(byte) aesExtraDataRecord.getAesStrength()}, list2);
                            Raw.writeShortLittleEndian(bArr, 0, (short) aesExtraDataRecord.getCompressionMethod());
                            copyByteArrayToArrayList(bArr, list2);
                            return i + 11;
                        }
                    }
                    Raw.writeLongLittleEndian(obj4, 0, InternalZipConstants.ZIP_64_LIMIT);
                    System.arraycopy(obj4, 0, obj3, 0, 4);
                    copyByteArrayToArrayList(obj3, list2);
                    copyByteArrayToArrayList(obj3, list2);
                    obj3 = 1;
                    bArr = bArr5;
                    Raw.writeShortLittleEndian(bArr, 0, (short) fileHeader.getFileNameLength());
                    copyByteArrayToArrayList(bArr, list2);
                    obj = new byte[4];
                    if (fileHeader.getOffsetLocalHeader() <= InternalZipConstants.ZIP_64_LIMIT) {
                        Raw.writeLongLittleEndian(obj4, 0, fileHeader.getOffsetLocalHeader());
                        System.arraycopy(obj4, 0, obj, 0, 4);
                        obj2 = null;
                    } else {
                        Raw.writeLongLittleEndian(obj4, 0, InternalZipConstants.ZIP_64_LIMIT);
                        System.arraycopy(obj4, 0, obj, 0, 4);
                        obj2 = 1;
                    }
                    if (obj3 == null) {
                        if (obj2 != null) {
                            i = 0;
                            if (fileHeader.getAesExtraDataRecord() != null) {
                                i += 11;
                            }
                            Raw.writeShortLittleEndian(bArr, 0, (short) i);
                            copyByteArrayToArrayList(bArr, list2);
                            copyByteArrayToArrayList(bArr3, list2);
                            Raw.writeShortLittleEndian(bArr, 0, (short) fileHeader.getDiskNumberStart());
                            copyByteArrayToArrayList(bArr, list2);
                            copyByteArrayToArrayList(bArr3, list2);
                            if (fileHeader.getExternalFileAttr() == null) {
                                copyByteArrayToArrayList(fileHeader.getExternalFileAttr(), list2);
                            } else {
                                copyByteArrayToArrayList(bArr4, list2);
                            }
                            copyByteArrayToArrayList(obj, list2);
                            if (Zip4jUtil.isStringNotNullAndNotEmpty(zipModel.getFileNameCharset())) {
                                bytes = fileHeader.getFileName().getBytes(zipModel.getFileNameCharset());
                                copyByteArrayToArrayList(bytes, list2);
                                i = 46 + bytes.length;
                            } else {
                                copyByteArrayToArrayList(Zip4jUtil.convertCharset(fileHeader.getFileName()), list2);
                                i = 46 + Zip4jUtil.getEncodedStringLength(fileHeader.getFileName());
                            }
                            zipModel.setZip64Format(true);
                            Raw.writeShortLittleEndian(bArr, 0, (short) 1);
                            copyByteArrayToArrayList(bArr, list2);
                            i += 2;
                            if (obj3 == null) {
                            }
                            if (obj2 != null) {
                                i2 += 8;
                            }
                            Raw.writeShortLittleEndian(bArr, 0, (short) i2);
                            copyByteArrayToArrayList(bArr, list2);
                            i += 2;
                            if (obj3 != null) {
                                Raw.writeLongLittleEndian(obj4, 0, fileHeader.getUncompressedSize());
                                copyByteArrayToArrayList(obj4, list2);
                                i += 8;
                                Raw.writeLongLittleEndian(obj4, 0, fileHeader.getCompressedSize());
                                copyByteArrayToArrayList(obj4, list2);
                                i += 8;
                            }
                            if (obj2 != null) {
                                Raw.writeLongLittleEndian(obj4, 0, fileHeader.getOffsetLocalHeader());
                                copyByteArrayToArrayList(obj4, list2);
                                i += 8;
                            }
                            if (fileHeader.getAesExtraDataRecord() == null) {
                                return i;
                            }
                            aesExtraDataRecord = fileHeader.getAesExtraDataRecord();
                            Raw.writeShortLittleEndian(bArr, 0, (short) ((int) aesExtraDataRecord.getSignature()));
                            copyByteArrayToArrayList(bArr, list2);
                            Raw.writeShortLittleEndian(bArr, 0, (short) aesExtraDataRecord.getDataSize());
                            copyByteArrayToArrayList(bArr, list2);
                            Raw.writeShortLittleEndian(bArr, 0, (short) aesExtraDataRecord.getVersionNumber());
                            copyByteArrayToArrayList(bArr, list2);
                            copyByteArrayToArrayList(aesExtraDataRecord.getVendorID().getBytes(), list2);
                            copyByteArrayToArrayList(new byte[]{(byte) aesExtraDataRecord.getAesStrength()}, list2);
                            Raw.writeShortLittleEndian(bArr, 0, (short) aesExtraDataRecord.getCompressionMethod());
                            copyByteArrayToArrayList(bArr, list2);
                            return i + 11;
                        }
                    }
                    if (obj3 != null) {
                        i2 = 20;
                    }
                    if (obj2 == null) {
                    }
                    if (fileHeader.getAesExtraDataRecord() != null) {
                        i += 11;
                    }
                    Raw.writeShortLittleEndian(bArr, 0, (short) i);
                    copyByteArrayToArrayList(bArr, list2);
                    copyByteArrayToArrayList(bArr3, list2);
                    Raw.writeShortLittleEndian(bArr, 0, (short) fileHeader.getDiskNumberStart());
                    copyByteArrayToArrayList(bArr, list2);
                    copyByteArrayToArrayList(bArr3, list2);
                    if (fileHeader.getExternalFileAttr() == null) {
                        copyByteArrayToArrayList(bArr4, list2);
                    } else {
                        copyByteArrayToArrayList(fileHeader.getExternalFileAttr(), list2);
                    }
                    copyByteArrayToArrayList(obj, list2);
                    if (Zip4jUtil.isStringNotNullAndNotEmpty(zipModel.getFileNameCharset())) {
                        copyByteArrayToArrayList(Zip4jUtil.convertCharset(fileHeader.getFileName()), list2);
                        i = 46 + Zip4jUtil.getEncodedStringLength(fileHeader.getFileName());
                    } else {
                        bytes = fileHeader.getFileName().getBytes(zipModel.getFileNameCharset());
                        copyByteArrayToArrayList(bytes, list2);
                        i = 46 + bytes.length;
                    }
                    zipModel.setZip64Format(true);
                    Raw.writeShortLittleEndian(bArr, 0, (short) 1);
                    copyByteArrayToArrayList(bArr, list2);
                    i += 2;
                    if (obj3 == null) {
                    }
                    if (obj2 != null) {
                        i2 += 8;
                    }
                    Raw.writeShortLittleEndian(bArr, 0, (short) i2);
                    copyByteArrayToArrayList(bArr, list2);
                    i += 2;
                    if (obj3 != null) {
                        Raw.writeLongLittleEndian(obj4, 0, fileHeader.getUncompressedSize());
                        copyByteArrayToArrayList(obj4, list2);
                        i += 8;
                        Raw.writeLongLittleEndian(obj4, 0, fileHeader.getCompressedSize());
                        copyByteArrayToArrayList(obj4, list2);
                        i += 8;
                    }
                    if (obj2 != null) {
                        Raw.writeLongLittleEndian(obj4, 0, fileHeader.getOffsetLocalHeader());
                        copyByteArrayToArrayList(obj4, list2);
                        i += 8;
                    }
                    if (fileHeader.getAesExtraDataRecord() == null) {
                        return i;
                    }
                    aesExtraDataRecord = fileHeader.getAesExtraDataRecord();
                    Raw.writeShortLittleEndian(bArr, 0, (short) ((int) aesExtraDataRecord.getSignature()));
                    copyByteArrayToArrayList(bArr, list2);
                    Raw.writeShortLittleEndian(bArr, 0, (short) aesExtraDataRecord.getDataSize());
                    copyByteArrayToArrayList(bArr, list2);
                    Raw.writeShortLittleEndian(bArr, 0, (short) aesExtraDataRecord.getVersionNumber());
                    copyByteArrayToArrayList(bArr, list2);
                    copyByteArrayToArrayList(aesExtraDataRecord.getVendorID().getBytes(), list2);
                    copyByteArrayToArrayList(new byte[]{(byte) aesExtraDataRecord.getAesStrength()}, list2);
                    Raw.writeShortLittleEndian(bArr, 0, (short) aesExtraDataRecord.getCompressionMethod());
                    copyByteArrayToArrayList(bArr, list2);
                    return i + 11;
                } catch (Throwable e) {
                    throw new ZipException(e);
                }
            }
        }
        throw new ZipException("input parameters is null, cannot write local file header");
    }

    private void writeZip64EndOfCentralDirectoryRecord(ZipModel zipModel, OutputStream outputStream, int i, long j, List list) throws ZipException {
        if (zipModel != null) {
            if (outputStream != null) {
                try {
                    byte[] bArr = new byte[2];
                    outputStream = new byte[2];
                    byte[] bArr2 = new byte[4];
                    byte[] bArr3 = new byte[8];
                    Raw.writeIntLittleEndian(bArr2, 0, 101075792);
                    copyByteArrayToArrayList(bArr2, list);
                    Raw.writeLongLittleEndian(bArr3, 0, 44);
                    copyByteArrayToArrayList(bArr3, list);
                    if (zipModel.getCentralDirectory() == null || zipModel.getCentralDirectory().getFileHeaders() == null || zipModel.getCentralDirectory().getFileHeaders().size() <= 0) {
                        copyByteArrayToArrayList(outputStream, list);
                        copyByteArrayToArrayList(outputStream, list);
                    } else {
                        Raw.writeShortLittleEndian(bArr, 0, (short) ((FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(0)).getVersionMadeBy());
                        copyByteArrayToArrayList(bArr, list);
                        Raw.writeShortLittleEndian(bArr, 0, (short) ((FileHeader) zipModel.getCentralDirectory().getFileHeaders().get(0)).getVersionNeededToExtract());
                        copyByteArrayToArrayList(bArr, list);
                    }
                    Raw.writeIntLittleEndian(bArr2, 0, zipModel.getEndCentralDirRecord().getNoOfThisDisk());
                    copyByteArrayToArrayList(bArr2, list);
                    Raw.writeIntLittleEndian(bArr2, 0, zipModel.getEndCentralDirRecord().getNoOfThisDiskStartOfCentralDir());
                    copyByteArrayToArrayList(bArr2, list);
                    if (zipModel.getCentralDirectory() != null) {
                        if (zipModel.getCentralDirectory().getFileHeaders() != null) {
                            outputStream = zipModel.getCentralDirectory().getFileHeaders().size();
                            if (zipModel.isSplitArchive()) {
                                countNumberOfFileHeaderEntriesOnDisk(zipModel.getCentralDirectory().getFileHeaders(), zipModel.getEndCentralDirRecord().getNoOfThisDisk());
                                zipModel = null;
                            } else {
                                zipModel = outputStream;
                            }
                            Raw.writeLongLittleEndian(bArr3, 0, (long) zipModel);
                            copyByteArrayToArrayList(bArr3, list);
                            Raw.writeLongLittleEndian(bArr3, 0, (long) outputStream);
                            copyByteArrayToArrayList(bArr3, list);
                            Raw.writeLongLittleEndian(bArr3, 0, (long) i);
                            copyByteArrayToArrayList(bArr3, list);
                            Raw.writeLongLittleEndian(bArr3, 0, j);
                            copyByteArrayToArrayList(bArr3, list);
                            return;
                        }
                    }
                    throw new ZipException("invalid central directory/file headers, cannot write end of central directory record");
                } catch (ZipModel zipModel2) {
                    throw zipModel2;
                } catch (Throwable e) {
                    throw new ZipException(e);
                }
            }
        }
        throw new ZipException("zip model or output stream is null, cannot write zip64 end of central directory record");
    }

    private void writeZip64EndOfCentralDirectoryLocator(ZipModel zipModel, OutputStream outputStream, List list) throws ZipException {
        if (zipModel != null) {
            if (outputStream != null) {
                try {
                    outputStream = new byte[4];
                    byte[] bArr = new byte[8];
                    Raw.writeIntLittleEndian(outputStream, 0, 117853008);
                    copyByteArrayToArrayList(outputStream, list);
                    Raw.writeIntLittleEndian(outputStream, 0, zipModel.getZip64EndCentralDirLocator().getNoOfDiskStartOfZip64EndOfCentralDirRec());
                    copyByteArrayToArrayList(outputStream, list);
                    Raw.writeLongLittleEndian(bArr, 0, zipModel.getZip64EndCentralDirLocator().getOffsetZip64EndOfCentralDirRec());
                    copyByteArrayToArrayList(bArr, list);
                    Raw.writeIntLittleEndian(outputStream, 0, zipModel.getZip64EndCentralDirLocator().getTotNumberOfDiscs());
                    copyByteArrayToArrayList(outputStream, list);
                    return;
                } catch (ZipModel zipModel2) {
                    throw zipModel2;
                } catch (Throwable e) {
                    throw new ZipException(e);
                }
            }
        }
        throw new ZipException("zip model or output stream is null, cannot write zip64 end of central directory locator");
    }

    private void writeEndOfCentralDirectoryRecord(ZipModel zipModel, OutputStream outputStream, int i, long j, List list) throws ZipException {
        if (zipModel != null) {
            if (outputStream != null) {
                try {
                    outputStream = new byte[2];
                    Object obj = new byte[4];
                    Object obj2 = new byte[8];
                    Raw.writeIntLittleEndian(obj, 0, (int) zipModel.getEndCentralDirRecord().getSignature());
                    copyByteArrayToArrayList(obj, list);
                    Raw.writeShortLittleEndian(outputStream, 0, (short) zipModel.getEndCentralDirRecord().getNoOfThisDisk());
                    copyByteArrayToArrayList(outputStream, list);
                    Raw.writeShortLittleEndian(outputStream, 0, (short) zipModel.getEndCentralDirRecord().getNoOfThisDiskStartOfCentralDir());
                    copyByteArrayToArrayList(outputStream, list);
                    if (zipModel.getCentralDirectory() != null) {
                        if (zipModel.getCentralDirectory().getFileHeaders() != null) {
                            int size = zipModel.getCentralDirectory().getFileHeaders().size();
                            Raw.writeShortLittleEndian(outputStream, 0, (short) (zipModel.isSplitArchive() ? countNumberOfFileHeaderEntriesOnDisk(zipModel.getCentralDirectory().getFileHeaders(), zipModel.getEndCentralDirRecord().getNoOfThisDisk()) : size));
                            copyByteArrayToArrayList(outputStream, list);
                            Raw.writeShortLittleEndian(outputStream, 0, (short) size);
                            copyByteArrayToArrayList(outputStream, list);
                            Raw.writeIntLittleEndian(obj, 0, i);
                            copyByteArrayToArrayList(obj, list);
                            if (j > InternalZipConstants.ZIP_64_LIMIT) {
                                Raw.writeLongLittleEndian(obj2, 0, InternalZipConstants.ZIP_64_LIMIT);
                                System.arraycopy(obj2, 0, obj, 0, 4);
                                copyByteArrayToArrayList(obj, list);
                            } else {
                                Raw.writeLongLittleEndian(obj2, 0, j);
                                System.arraycopy(obj2, 0, obj, 0, 4);
                                copyByteArrayToArrayList(obj, list);
                            }
                            i = zipModel.getEndCentralDirRecord().getComment() != 0 ? zipModel.getEndCentralDirRecord().getCommentLength() : 0;
                            Raw.writeShortLittleEndian(outputStream, 0, (short) i);
                            copyByteArrayToArrayList(outputStream, list);
                            if (i > 0) {
                                copyByteArrayToArrayList(zipModel.getEndCentralDirRecord().getCommentBytes(), list);
                                return;
                            }
                            return;
                        }
                    }
                    throw new ZipException("invalid central directory/file headers, cannot write end of central directory record");
                } catch (Throwable e) {
                    throw new ZipException(e);
                }
            }
        }
        throw new ZipException("zip model or output stream is null, cannot write end of central directory record");
    }

    public void updateLocalFileHeader(LocalFileHeader localFileHeader, long j, int i, ZipModel zipModel, byte[] bArr, int i2, SplitOutputStream splitOutputStream) throws ZipException {
        int i3 = i;
        int i4 = i2;
        if (localFileHeader != null && j >= 0) {
            if (zipModel != null) {
                try {
                    SplitOutputStream splitOutputStream2;
                    Object obj;
                    if (i4 != splitOutputStream.getCurrSplitFileCounter()) {
                        String stringBuffer;
                        File file = new File(zipModel.getZipFile());
                        String parent = file.getParent();
                        String zipFileNameWithoutExt = Zip4jUtil.getZipFileNameWithoutExt(file.getName());
                        StringBuffer stringBuffer2 = new StringBuffer(String.valueOf(parent));
                        stringBuffer2.append(System.getProperty("file.separator"));
                        parent = stringBuffer2.toString();
                        if (i4 < 9) {
                            stringBuffer2 = new StringBuffer(String.valueOf(parent));
                            stringBuffer2.append(zipFileNameWithoutExt);
                            stringBuffer2.append(".z0");
                            stringBuffer2.append(i4 + 1);
                            stringBuffer = stringBuffer2.toString();
                        } else {
                            stringBuffer2 = new StringBuffer(String.valueOf(parent));
                            stringBuffer2.append(zipFileNameWithoutExt);
                            stringBuffer2.append(".z");
                            stringBuffer2.append(i4 + 1);
                            stringBuffer = stringBuffer2.toString();
                        }
                        splitOutputStream2 = new SplitOutputStream(new File(stringBuffer));
                        obj = 1;
                    } else {
                        splitOutputStream2 = splitOutputStream;
                        obj = null;
                    }
                    long filePointer = splitOutputStream2.getFilePointer();
                    if (i3 == 14) {
                        splitOutputStream2.seek(j + ((long) i3));
                        splitOutputStream2.write(bArr);
                    } else if (i3 == 18 || i3 == 22) {
                        updateCompressedSizeInLocalFileHeader(splitOutputStream2, localFileHeader, j, (long) i3, bArr, zipModel.isZip64Format());
                    }
                    if (obj != null) {
                        splitOutputStream2.close();
                        return;
                    } else {
                        splitOutputStream.seek(filePointer);
                        return;
                    }
                } catch (Throwable e) {
                    throw new ZipException(e);
                }
            }
        }
        throw new ZipException("invalid input parameters, cannot update local file header");
    }

    private void updateCompressedSizeInLocalFileHeader(SplitOutputStream splitOutputStream, LocalFileHeader localFileHeader, long j, long j2, byte[] bArr, boolean z) throws ZipException {
        if (splitOutputStream == null) {
            throw new ZipException("invalid output stream, cannot update compressed size for local file header");
        }
        try {
            if (!localFileHeader.isWriteComprSizeInZip64ExtraRecord()) {
                splitOutputStream.seek(j + j2);
                splitOutputStream.write(bArr);
            } else if (!bArr.length) {
                throw new ZipException("attempting to write a non 8-byte compressed size block for a zip64 file");
            } else {
                long fileNameLength = ((((((((j + j2) + 4) + 4) + 2) + 2) + ((long) localFileHeader.getFileNameLength())) + 2) + 2) + 8;
                splitOutputStream.seek(j2 == 22 ? fileNameLength + 8 : fileNameLength);
                splitOutputStream.write(bArr);
            }
        } catch (Throwable e) {
            throw new ZipException(e);
        }
    }

    private void copyByteArrayToArrayList(byte[] bArr, List list) throws ZipException {
        if (list != null) {
            if (bArr != null) {
                for (byte b : bArr) {
                    list.add(Byte.toString(b));
                }
                return;
            }
        }
        throw new ZipException("one of the input parameters is null, cannot copy byte array to array list");
    }

    private byte[] byteArrayListToByteArray(List list) throws ZipException {
        if (list == null) {
            throw new ZipException("input byte array list is null, cannot conver to byte array");
        } else if (list.size() <= 0) {
            return null;
        } else {
            byte[] bArr = new byte[list.size()];
            for (int i = 0; i < list.size(); i++) {
                bArr[i] = Byte.parseByte((String) list.get(i));
            }
            return bArr;
        }
    }

    private int countNumberOfFileHeaderEntriesOnDisk(ArrayList arrayList, int i) throws ZipException {
        if (arrayList == null) {
            throw new ZipException("file headers are null, cannot calculate number of entries on this disk");
        }
        int i2 = 0;
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            if (((FileHeader) arrayList.get(i3)).getDiskNumberStart() == i) {
                i2++;
            }
        }
        return i2;
    }
}
