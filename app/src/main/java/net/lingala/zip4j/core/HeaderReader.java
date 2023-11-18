package net.lingala.zip4j.core;

import android.support.graphics.drawable.PathInterpolatorCompat;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.CentralDirectory;
import net.lingala.zip4j.model.DigitalSignature;
import net.lingala.zip4j.model.EndCentralDirRecord;
import net.lingala.zip4j.model.ExtraDataRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.Zip64EndCentralDirLocator;
import net.lingala.zip4j.model.Zip64EndCentralDirRecord;
import net.lingala.zip4j.model.Zip64ExtendedInfo;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jUtil;

public class HeaderReader {
    private RandomAccessFile zip4jRaf = null;
    private ZipModel zipModel;

    public HeaderReader(RandomAccessFile randomAccessFile) {
        this.zip4jRaf = randomAccessFile;
    }

    public ZipModel readAllHeaders() throws ZipException {
        return readAllHeaders(null);
    }

    public ZipModel readAllHeaders(String str) throws ZipException {
        this.zipModel = new ZipModel();
        this.zipModel.setFileNameCharset(str);
        this.zipModel.setEndCentralDirRecord(readEndOfCentralDirectoryRecord());
        this.zipModel.setZip64EndCentralDirLocator(readZip64EndCentralDirLocator());
        if (this.zipModel.isZip64Format() != null) {
            this.zipModel.setZip64EndCentralDirRecord(readZip64EndCentralDirRec());
            if (this.zipModel.getZip64EndCentralDirRecord() == null || this.zipModel.getZip64EndCentralDirRecord().getNoOfThisDisk() <= null) {
                this.zipModel.setSplitArchive(false);
            } else {
                this.zipModel.setSplitArchive(true);
            }
        }
        this.zipModel.setCentralDirectory(readCentralDirectory());
        return this.zipModel;
    }

    private EndCentralDirRecord readEndOfCentralDirectoryRecord() throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("random access file was null", 3);
        }
        try {
            byte[] bArr = new byte[4];
            long length = this.zip4jRaf.length() - 22;
            EndCentralDirRecord endCentralDirRecord = new EndCentralDirRecord();
            int i = 0;
            while (true) {
                long j = length - 1;
                this.zip4jRaf.seek(length);
                i++;
                if (((long) Raw.readLeInt(this.zip4jRaf, bArr)) == InternalZipConstants.ENDSIG) {
                    break;
                } else if (i > PathInterpolatorCompat.MAX_NUM_POINTS) {
                    break;
                } else {
                    length = j;
                }
            }
            if (((long) Raw.readIntLittleEndian(bArr, 0)) != InternalZipConstants.ENDSIG) {
                throw new ZipException("zip headers not found. probably not a zip file");
            }
            bArr = new byte[4];
            byte[] bArr2 = new byte[2];
            endCentralDirRecord.setSignature(InternalZipConstants.ENDSIG);
            readIntoBuff(this.zip4jRaf, bArr2);
            endCentralDirRecord.setNoOfThisDisk(Raw.readShortLittleEndian(bArr2, 0));
            readIntoBuff(this.zip4jRaf, bArr2);
            endCentralDirRecord.setNoOfThisDiskStartOfCentralDir(Raw.readShortLittleEndian(bArr2, 0));
            readIntoBuff(this.zip4jRaf, bArr2);
            endCentralDirRecord.setTotNoOfEntriesInCentralDirOnThisDisk(Raw.readShortLittleEndian(bArr2, 0));
            readIntoBuff(this.zip4jRaf, bArr2);
            endCentralDirRecord.setTotNoOfEntriesInCentralDir(Raw.readShortLittleEndian(bArr2, 0));
            readIntoBuff(this.zip4jRaf, bArr);
            endCentralDirRecord.setSizeOfCentralDir(Raw.readIntLittleEndian(bArr, 0));
            readIntoBuff(this.zip4jRaf, bArr);
            endCentralDirRecord.setOffsetOfStartOfCentralDir(Raw.readLongLittleEndian(getLongByteFromIntByte(bArr), 0));
            readIntoBuff(this.zip4jRaf, bArr2);
            int readShortLittleEndian = Raw.readShortLittleEndian(bArr2, 0);
            endCentralDirRecord.setCommentLength(readShortLittleEndian);
            if (readShortLittleEndian > 0) {
                bArr = new byte[readShortLittleEndian];
                readIntoBuff(this.zip4jRaf, bArr);
                endCentralDirRecord.setComment(new String(bArr));
                endCentralDirRecord.setCommentBytes(bArr);
            } else {
                endCentralDirRecord.setComment(null);
            }
            if (endCentralDirRecord.getNoOfThisDisk() > 0) {
                this.zipModel.setSplitArchive(true);
            } else {
                this.zipModel.setSplitArchive(false);
            }
            return endCentralDirRecord;
        } catch (Throwable e) {
            throw new ZipException("Probably not a zip file or a corrupted zip file", e, 4);
        }
    }

    private CentralDirectory readCentralDirectory() throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("random access file was null", 3);
        } else if (r1.zipModel.getEndCentralDirRecord() == null) {
            throw new ZipException("EndCentralRecord was null, maybe a corrupt zip file");
        } else {
            try {
                CentralDirectory centralDirectory = new CentralDirectory();
                ArrayList arrayList = new ArrayList();
                EndCentralDirRecord endCentralDirRecord = r1.zipModel.getEndCentralDirRecord();
                long offsetOfStartOfCentralDir = endCentralDirRecord.getOffsetOfStartOfCentralDir();
                int totNoOfEntriesInCentralDir = endCentralDirRecord.getTotNoOfEntriesInCentralDir();
                if (r1.zipModel.isZip64Format()) {
                    offsetOfStartOfCentralDir = r1.zipModel.getZip64EndCentralDirRecord().getOffsetStartCenDirWRTStartDiskNo();
                    totNoOfEntriesInCentralDir = (int) r1.zipModel.getZip64EndCentralDirRecord().getTotNoOfEntriesInCentralDir();
                }
                r1.zip4jRaf.seek(offsetOfStartOfCentralDir);
                Object obj = new byte[4];
                Object obj2 = new byte[2];
                byte[] bArr = new byte[8];
                int i = 0;
                int i2 = 0;
                while (i2 < totNoOfEntriesInCentralDir) {
                    FileHeader fileHeader = new FileHeader();
                    readIntoBuff(r1.zip4jRaf, obj);
                    int readIntLittleEndian = Raw.readIntLittleEndian(obj, i);
                    boolean z = true;
                    if (((long) readIntLittleEndian) != InternalZipConstants.CENSIG) {
                        StringBuffer stringBuffer = new StringBuffer("Expected central directory entry not found (#");
                        stringBuffer.append(i2 + 1);
                        stringBuffer.append(")");
                        throw new ZipException(stringBuffer.toString());
                    }
                    fileHeader.setSignature(readIntLittleEndian);
                    readIntoBuff(r1.zip4jRaf, obj2);
                    fileHeader.setVersionMadeBy(Raw.readShortLittleEndian(obj2, i));
                    readIntoBuff(r1.zip4jRaf, obj2);
                    fileHeader.setVersionNeededToExtract(Raw.readShortLittleEndian(obj2, i));
                    readIntoBuff(r1.zip4jRaf, obj2);
                    fileHeader.setFileNameUTF8Encoded((Raw.readShortLittleEndian(obj2, i) & 2048) != 0);
                    byte b = obj2[i];
                    if ((b & 1) != 0) {
                        fileHeader.setEncrypted(true);
                    }
                    fileHeader.setGeneralPurposeFlag((byte[]) obj2.clone());
                    fileHeader.setDataDescriptorExists((b >> 3) == 1);
                    readIntoBuff(r1.zip4jRaf, obj2);
                    fileHeader.setCompressionMethod(Raw.readShortLittleEndian(obj2, i));
                    readIntoBuff(r1.zip4jRaf, obj);
                    fileHeader.setLastModFileTime(Raw.readIntLittleEndian(obj, i));
                    readIntoBuff(r1.zip4jRaf, obj);
                    fileHeader.setCrc32((long) Raw.readIntLittleEndian(obj, i));
                    fileHeader.setCrcBuff((byte[]) obj.clone());
                    readIntoBuff(r1.zip4jRaf, obj);
                    fileHeader.setCompressedSize(Raw.readLongLittleEndian(getLongByteFromIntByte(obj), i));
                    readIntoBuff(r1.zip4jRaf, obj);
                    fileHeader.setUncompressedSize(Raw.readLongLittleEndian(getLongByteFromIntByte(obj), i));
                    readIntoBuff(r1.zip4jRaf, obj2);
                    readIntLittleEndian = Raw.readShortLittleEndian(obj2, i);
                    fileHeader.setFileNameLength(readIntLittleEndian);
                    readIntoBuff(r1.zip4jRaf, obj2);
                    fileHeader.setExtraFieldLength(Raw.readShortLittleEndian(obj2, i));
                    readIntoBuff(r1.zip4jRaf, obj2);
                    int readShortLittleEndian = Raw.readShortLittleEndian(obj2, i);
                    fileHeader.setFileComment(new String(obj2));
                    readIntoBuff(r1.zip4jRaf, obj2);
                    fileHeader.setDiskNumberStart(Raw.readShortLittleEndian(obj2, i));
                    readIntoBuff(r1.zip4jRaf, obj2);
                    fileHeader.setInternalFileAttr((byte[]) obj2.clone());
                    readIntoBuff(r1.zip4jRaf, obj);
                    fileHeader.setExternalFileAttr((byte[]) obj.clone());
                    readIntoBuff(r1.zip4jRaf, obj);
                    int i3 = i2;
                    fileHeader.setOffsetLocalHeader(Raw.readLongLittleEndian(getLongByteFromIntByte(obj), i) & InternalZipConstants.ZIP_64_LIMIT);
                    if (readIntLittleEndian > 0) {
                        String str;
                        bArr = new byte[readIntLittleEndian];
                        readIntoBuff(r1.zip4jRaf, bArr);
                        if (Zip4jUtil.isStringNotNullAndNotEmpty(r1.zipModel.getFileNameCharset())) {
                            str = new String(bArr, r1.zipModel.getFileNameCharset());
                        } else {
                            str = Zip4jUtil.decodeFileName(bArr, fileHeader.isFileNameUTF8Encoded());
                        }
                        if (str == null) {
                            throw new ZipException("fileName is null when reading central directory");
                        }
                        StringBuffer stringBuffer2 = new StringBuffer(":");
                        stringBuffer2.append(System.getProperty("file.separator"));
                        if (str.indexOf(stringBuffer2.toString()) >= 0) {
                            stringBuffer2 = new StringBuffer(":");
                            stringBuffer2.append(System.getProperty("file.separator"));
                            str = str.substring(str.indexOf(stringBuffer2.toString()) + 2);
                        }
                        fileHeader.setFileName(str);
                        if (!(str.endsWith("/") || str.endsWith("\\"))) {
                            z = false;
                        }
                        fileHeader.setDirectory(z);
                    } else {
                        fileHeader.setFileName(null);
                    }
                    readAndSaveExtraDataRecord(fileHeader);
                    readAndSaveZip64ExtendedInfo(fileHeader);
                    readAndSaveAESExtraDataRecord(fileHeader);
                    if (readShortLittleEndian > 0) {
                        bArr = new byte[readShortLittleEndian];
                        readIntoBuff(r1.zip4jRaf, bArr);
                        fileHeader.setFileComment(new String(bArr));
                    }
                    arrayList.add(fileHeader);
                    i2 = i3 + 1;
                    i = 0;
                }
                centralDirectory.setFileHeaders(arrayList);
                DigitalSignature digitalSignature = new DigitalSignature();
                readIntoBuff(r1.zip4jRaf, obj);
                totNoOfEntriesInCentralDir = Raw.readIntLittleEndian(obj, i);
                if (((long) totNoOfEntriesInCentralDir) != InternalZipConstants.DIGSIG) {
                    return centralDirectory;
                }
                digitalSignature.setHeaderSignature(totNoOfEntriesInCentralDir);
                readIntoBuff(r1.zip4jRaf, obj2);
                totNoOfEntriesInCentralDir = Raw.readShortLittleEndian(obj2, i);
                digitalSignature.setSizeOfData(totNoOfEntriesInCentralDir);
                if (totNoOfEntriesInCentralDir > 0) {
                    byte[] bArr2 = new byte[totNoOfEntriesInCentralDir];
                    readIntoBuff(r1.zip4jRaf, bArr2);
                    digitalSignature.setSignatureData(new String(bArr2));
                }
                return centralDirectory;
            } catch (Throwable e) {
                throw new ZipException(e);
            }
        }
    }

    private void readAndSaveExtraDataRecord(FileHeader fileHeader) throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("invalid file handler when trying to read extra data record");
        } else if (fileHeader == null) {
            throw new ZipException("file header is null");
        } else {
            int extraFieldLength = fileHeader.getExtraFieldLength();
            if (extraFieldLength > 0) {
                fileHeader.setExtraDataRecords(readExtraDataRecords(extraFieldLength));
            }
        }
    }

    private void readAndSaveExtraDataRecord(LocalFileHeader localFileHeader) throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("invalid file handler when trying to read extra data record");
        } else if (localFileHeader == null) {
            throw new ZipException("file header is null");
        } else {
            int extraFieldLength = localFileHeader.getExtraFieldLength();
            if (extraFieldLength > 0) {
                localFileHeader.setExtraDataRecords(readExtraDataRecords(extraFieldLength));
            }
        }
    }

    private ArrayList readExtraDataRecords(int i) throws ZipException {
        if (i <= 0) {
            return null;
        }
        try {
            Object obj = new byte[i];
            this.zip4jRaf.read(obj);
            ArrayList arrayList = new ArrayList();
            int i2 = 0;
            while (i2 < i) {
                ExtraDataRecord extraDataRecord = new ExtraDataRecord();
                extraDataRecord.setHeader((long) Raw.readShortLittleEndian(obj, i2));
                i2 += 2;
                int readShortLittleEndian = Raw.readShortLittleEndian(obj, i2);
                if (readShortLittleEndian + 2 > i) {
                    readShortLittleEndian = Raw.readShortBigEndian(obj, i2);
                    if (readShortLittleEndian + 2 > i) {
                        break;
                    }
                }
                extraDataRecord.setSizeOfData(readShortLittleEndian);
                i2 += 2;
                if (readShortLittleEndian > 0) {
                    Object obj2 = new byte[readShortLittleEndian];
                    System.arraycopy(obj, i2, obj2, 0, readShortLittleEndian);
                    extraDataRecord.setData(obj2);
                }
                i2 += readShortLittleEndian;
                arrayList.add(extraDataRecord);
            }
            if (arrayList.size() > 0) {
                return arrayList;
            }
            return null;
        } catch (Throwable e) {
            throw new ZipException(e);
        }
    }

    private Zip64EndCentralDirLocator readZip64EndCentralDirLocator() throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("invalid file handler when trying to read Zip64EndCentralDirLocator");
        }
        try {
            Zip64EndCentralDirLocator zip64EndCentralDirLocator = new Zip64EndCentralDirLocator();
            setFilePointerToReadZip64EndCentralDirLoc();
            byte[] bArr = new byte[4];
            byte[] bArr2 = new byte[8];
            readIntoBuff(this.zip4jRaf, bArr);
            long readIntLittleEndian = (long) Raw.readIntLittleEndian(bArr, 0);
            if (readIntLittleEndian == InternalZipConstants.ZIP64ENDCENDIRLOC) {
                this.zipModel.setZip64Format(true);
                zip64EndCentralDirLocator.setSignature(readIntLittleEndian);
                readIntoBuff(this.zip4jRaf, bArr);
                zip64EndCentralDirLocator.setNoOfDiskStartOfZip64EndOfCentralDirRec(Raw.readIntLittleEndian(bArr, 0));
                readIntoBuff(this.zip4jRaf, bArr2);
                zip64EndCentralDirLocator.setOffsetZip64EndOfCentralDirRec(Raw.readLongLittleEndian(bArr2, 0));
                readIntoBuff(this.zip4jRaf, bArr);
                zip64EndCentralDirLocator.setTotNumberOfDiscs(Raw.readIntLittleEndian(bArr, 0));
                return zip64EndCentralDirLocator;
            }
            this.zipModel.setZip64Format(false);
            return null;
        } catch (Throwable e) {
            throw new ZipException(e);
        }
    }

    private Zip64EndCentralDirRecord readZip64EndCentralDirRec() throws ZipException {
        if (this.zipModel.getZip64EndCentralDirLocator() == null) {
            throw new ZipException("invalid zip64 end of central directory locator");
        }
        long offsetZip64EndOfCentralDirRec = this.zipModel.getZip64EndCentralDirLocator().getOffsetZip64EndOfCentralDirRec();
        if (offsetZip64EndOfCentralDirRec < 0) {
            throw new ZipException("invalid offset for start of end of central directory record");
        }
        try {
            this.zip4jRaf.seek(offsetZip64EndOfCentralDirRec);
            Zip64EndCentralDirRecord zip64EndCentralDirRecord = new Zip64EndCentralDirRecord();
            byte[] bArr = new byte[2];
            byte[] bArr2 = new byte[4];
            byte[] bArr3 = new byte[8];
            readIntoBuff(this.zip4jRaf, bArr2);
            long readIntLittleEndian = (long) Raw.readIntLittleEndian(bArr2, 0);
            if (readIntLittleEndian != InternalZipConstants.ZIP64ENDCENDIRREC) {
                throw new ZipException("invalid signature for zip64 end of central directory record");
            }
            zip64EndCentralDirRecord.setSignature(readIntLittleEndian);
            readIntoBuff(this.zip4jRaf, bArr3);
            zip64EndCentralDirRecord.setSizeOfZip64EndCentralDirRec(Raw.readLongLittleEndian(bArr3, 0));
            readIntoBuff(this.zip4jRaf, bArr);
            zip64EndCentralDirRecord.setVersionMadeBy(Raw.readShortLittleEndian(bArr, 0));
            readIntoBuff(this.zip4jRaf, bArr);
            zip64EndCentralDirRecord.setVersionNeededToExtract(Raw.readShortLittleEndian(bArr, 0));
            readIntoBuff(this.zip4jRaf, bArr2);
            zip64EndCentralDirRecord.setNoOfThisDisk(Raw.readIntLittleEndian(bArr2, 0));
            readIntoBuff(this.zip4jRaf, bArr2);
            zip64EndCentralDirRecord.setNoOfThisDiskStartOfCentralDir(Raw.readIntLittleEndian(bArr2, 0));
            readIntoBuff(this.zip4jRaf, bArr3);
            zip64EndCentralDirRecord.setTotNoOfEntriesInCentralDirOnThisDisk(Raw.readLongLittleEndian(bArr3, 0));
            readIntoBuff(this.zip4jRaf, bArr3);
            zip64EndCentralDirRecord.setTotNoOfEntriesInCentralDir(Raw.readLongLittleEndian(bArr3, 0));
            readIntoBuff(this.zip4jRaf, bArr3);
            zip64EndCentralDirRecord.setSizeOfCentralDir(Raw.readLongLittleEndian(bArr3, 0));
            readIntoBuff(this.zip4jRaf, bArr3);
            zip64EndCentralDirRecord.setOffsetStartCenDirWRTStartDiskNo(Raw.readLongLittleEndian(bArr3, 0));
            long sizeOfZip64EndCentralDirRec = zip64EndCentralDirRecord.getSizeOfZip64EndCentralDirRec() - 44;
            if (sizeOfZip64EndCentralDirRec > 0) {
                bArr = new byte[((int) sizeOfZip64EndCentralDirRec)];
                readIntoBuff(this.zip4jRaf, bArr);
                zip64EndCentralDirRecord.setExtensibleDataSector(bArr);
            }
            return zip64EndCentralDirRecord;
        } catch (Throwable e) {
            throw new ZipException(e);
        }
    }

    private void readAndSaveZip64ExtendedInfo(FileHeader fileHeader) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("file header is null in reading Zip64 Extended Info");
        }
        if (fileHeader.getExtraDataRecords() != null) {
            if (fileHeader.getExtraDataRecords().size() > 0) {
                Zip64ExtendedInfo readZip64ExtendedInfo = readZip64ExtendedInfo(fileHeader.getExtraDataRecords(), fileHeader.getUncompressedSize(), fileHeader.getCompressedSize(), fileHeader.getOffsetLocalHeader(), fileHeader.getDiskNumberStart());
                if (readZip64ExtendedInfo != null) {
                    fileHeader.setZip64ExtendedInfo(readZip64ExtendedInfo);
                    if (readZip64ExtendedInfo.getUnCompressedSize() != -1) {
                        fileHeader.setUncompressedSize(readZip64ExtendedInfo.getUnCompressedSize());
                    }
                    if (readZip64ExtendedInfo.getCompressedSize() != -1) {
                        fileHeader.setCompressedSize(readZip64ExtendedInfo.getCompressedSize());
                    }
                    if (readZip64ExtendedInfo.getOffsetLocalHeader() != -1) {
                        fileHeader.setOffsetLocalHeader(readZip64ExtendedInfo.getOffsetLocalHeader());
                    }
                    if (readZip64ExtendedInfo.getDiskNumberStart() != -1) {
                        fileHeader.setDiskNumberStart(readZip64ExtendedInfo.getDiskNumberStart());
                    }
                }
            }
        }
    }

    private void readAndSaveZip64ExtendedInfo(LocalFileHeader localFileHeader) throws ZipException {
        if (localFileHeader == null) {
            throw new ZipException("file header is null in reading Zip64 Extended Info");
        }
        if (localFileHeader.getExtraDataRecords() != null) {
            if (localFileHeader.getExtraDataRecords().size() > 0) {
                Zip64ExtendedInfo readZip64ExtendedInfo = readZip64ExtendedInfo(localFileHeader.getExtraDataRecords(), localFileHeader.getUncompressedSize(), localFileHeader.getCompressedSize(), -1, -1);
                if (readZip64ExtendedInfo != null) {
                    localFileHeader.setZip64ExtendedInfo(readZip64ExtendedInfo);
                    if (readZip64ExtendedInfo.getUnCompressedSize() != -1) {
                        localFileHeader.setUncompressedSize(readZip64ExtendedInfo.getUnCompressedSize());
                    }
                    if (readZip64ExtendedInfo.getCompressedSize() != -1) {
                        localFileHeader.setCompressedSize(readZip64ExtendedInfo.getCompressedSize());
                    }
                }
            }
        }
    }

    private Zip64ExtendedInfo readZip64ExtendedInfo(ArrayList arrayList, long j, long j2, long j3, int i) throws ZipException {
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            ExtraDataRecord extraDataRecord = (ExtraDataRecord) arrayList.get(i2);
            if (extraDataRecord != null) {
                if (extraDataRecord.getHeader() == 1) {
                    Zip64ExtendedInfo zip64ExtendedInfo = new Zip64ExtendedInfo();
                    Object data = extraDataRecord.getData();
                    if (extraDataRecord.getSizeOfData() > 0) {
                        int i3;
                        Object obj;
                        Object obj2;
                        Object obj3 = new byte[8];
                        Object obj4 = new byte[4];
                        Object obj5 = 1;
                        if ((j & 65535) != 65535 || extraDataRecord.getSizeOfData() <= 0) {
                            i3 = 0;
                            obj = null;
                        } else {
                            System.arraycopy(data, 0, obj3, 0, 8);
                            zip64ExtendedInfo.setUnCompressedSize(Raw.readLongLittleEndian(obj3, 0));
                            i3 = 8;
                            obj = 1;
                        }
                        if ((j2 & 65535) != 65535 || i3 >= extraDataRecord.getSizeOfData()) {
                            obj2 = obj;
                        } else {
                            System.arraycopy(data, i3, obj3, 0, 8);
                            zip64ExtendedInfo.setCompressedSize(Raw.readLongLittleEndian(obj3, 0));
                            i3 += 8;
                            obj2 = 1;
                        }
                        if ((j3 & 65535) == 65535 && i3 < extraDataRecord.getSizeOfData()) {
                            System.arraycopy(data, i3, obj3, 0, 8);
                            zip64ExtendedInfo.setOffsetLocalHeader(Raw.readLongLittleEndian(obj3, 0));
                            i3 += 8;
                            obj2 = 1;
                        }
                        if ((i & 65535) != 65535 || i3 >= extraDataRecord.getSizeOfData()) {
                            obj5 = obj2;
                        } else {
                            System.arraycopy(data, i3, obj4, 0, 4);
                            zip64ExtendedInfo.setDiskNumberStart(Raw.readIntLittleEndian(obj4, 0));
                        }
                        if (obj5 != null) {
                            return zip64ExtendedInfo;
                        }
                    }
                    return null;
                }
            }
        }
        return null;
    }

    private void setFilePointerToReadZip64EndCentralDirLoc() throws ZipException {
        try {
            byte[] bArr = new byte[4];
            long length = this.zip4jRaf.length() - 22;
            while (true) {
                long j = length - 1;
                this.zip4jRaf.seek(length);
                if (((long) Raw.readLeInt(this.zip4jRaf, bArr)) == InternalZipConstants.ENDSIG) {
                    this.zip4jRaf.seek(((((this.zip4jRaf.getFilePointer() - 4) - 4) - 8) - 4) - 4);
                    return;
                }
                length = j;
            }
        } catch (Throwable e) {
            throw new ZipException(e);
        }
    }

    public LocalFileHeader readLocalFileHeader(FileHeader fileHeader) throws ZipException {
        HeaderReader headerReader = this;
        if (fileHeader != null) {
            if (headerReader.zip4jRaf != null) {
                long offsetLocalHeader = fileHeader.getOffsetLocalHeader();
                if (fileHeader.getZip64ExtendedInfo() != null && fileHeader.getZip64ExtendedInfo().getOffsetLocalHeader() > 0) {
                    offsetLocalHeader = fileHeader.getOffsetLocalHeader();
                }
                if (offsetLocalHeader < 0) {
                    throw new ZipException("invalid local header offset");
                }
                try {
                    headerReader.zip4jRaf.seek(offsetLocalHeader);
                    LocalFileHeader localFileHeader = new LocalFileHeader();
                    byte[] bArr = new byte[2];
                    Object obj = new byte[4];
                    byte[] bArr2 = new byte[8];
                    readIntoBuff(headerReader.zip4jRaf, obj);
                    int readIntLittleEndian = Raw.readIntLittleEndian(obj, 0);
                    if (((long) readIntLittleEndian) != InternalZipConstants.LOCSIG) {
                        StringBuffer stringBuffer = new StringBuffer("invalid local header signature for file: ");
                        stringBuffer.append(fileHeader.getFileName());
                        throw new ZipException(stringBuffer.toString());
                    }
                    localFileHeader.setSignature(readIntLittleEndian);
                    readIntoBuff(headerReader.zip4jRaf, bArr);
                    localFileHeader.setVersionNeededToExtract(Raw.readShortLittleEndian(bArr, 0));
                    readIntoBuff(headerReader.zip4jRaf, bArr);
                    localFileHeader.setFileNameUTF8Encoded((Raw.readShortLittleEndian(bArr, 0) & 2048) != 0);
                    byte b = bArr[0];
                    if ((b & 1) != 0) {
                        localFileHeader.setEncrypted(true);
                    }
                    localFileHeader.setGeneralPurposeFlag(bArr);
                    String toBinaryString = Integer.toBinaryString(b);
                    if (toBinaryString.length() >= 4) {
                        localFileHeader.setDataDescriptorExists(toBinaryString.charAt(3) == '1');
                    }
                    readIntoBuff(headerReader.zip4jRaf, bArr);
                    localFileHeader.setCompressionMethod(Raw.readShortLittleEndian(bArr, 0));
                    readIntoBuff(headerReader.zip4jRaf, obj);
                    localFileHeader.setLastModFileTime(Raw.readIntLittleEndian(obj, 0));
                    readIntoBuff(headerReader.zip4jRaf, obj);
                    localFileHeader.setCrc32((long) Raw.readIntLittleEndian(obj, 0));
                    localFileHeader.setCrcBuff((byte[]) obj.clone());
                    readIntoBuff(headerReader.zip4jRaf, obj);
                    localFileHeader.setCompressedSize(Raw.readLongLittleEndian(getLongByteFromIntByte(obj), 0));
                    readIntoBuff(headerReader.zip4jRaf, obj);
                    localFileHeader.setUncompressedSize(Raw.readLongLittleEndian(getLongByteFromIntByte(obj), 0));
                    readIntoBuff(headerReader.zip4jRaf, bArr);
                    int readShortLittleEndian = Raw.readShortLittleEndian(bArr, 0);
                    localFileHeader.setFileNameLength(readShortLittleEndian);
                    readIntoBuff(headerReader.zip4jRaf, bArr);
                    int readShortLittleEndian2 = Raw.readShortLittleEndian(bArr, 0);
                    localFileHeader.setExtraFieldLength(readShortLittleEndian2);
                    int i = 30;
                    if (readShortLittleEndian > 0) {
                        byte[] bArr3 = new byte[readShortLittleEndian];
                        readIntoBuff(headerReader.zip4jRaf, bArr3);
                        String decodeFileName = Zip4jUtil.decodeFileName(bArr3, localFileHeader.isFileNameUTF8Encoded());
                        if (decodeFileName == null) {
                            throw new ZipException("file name is null, cannot assign file name to local file header");
                        }
                        StringBuffer stringBuffer2 = new StringBuffer(":");
                        stringBuffer2.append(System.getProperty("file.separator"));
                        if (decodeFileName.indexOf(stringBuffer2.toString()) >= 0) {
                            stringBuffer2 = new StringBuffer(":");
                            stringBuffer2.append(System.getProperty("file.separator"));
                            decodeFileName = decodeFileName.substring(decodeFileName.indexOf(stringBuffer2.toString()) + 2);
                        }
                        localFileHeader.setFileName(decodeFileName);
                        i = 30 + readShortLittleEndian;
                    } else {
                        localFileHeader.setFileName(null);
                    }
                    readAndSaveExtraDataRecord(localFileHeader);
                    localFileHeader.setOffsetStartOfData(offsetLocalHeader + ((long) (i + readShortLittleEndian2)));
                    localFileHeader.setPassword(fileHeader.getPassword());
                    readAndSaveZip64ExtendedInfo(localFileHeader);
                    readAndSaveAESExtraDataRecord(localFileHeader);
                    if (localFileHeader.isEncrypted() && localFileHeader.getEncryptionMethod() != 99) {
                        if ((b & 64) == 64) {
                            localFileHeader.setEncryptionMethod(1);
                        } else {
                            localFileHeader.setEncryptionMethod(0);
                        }
                    }
                    if (localFileHeader.getCrc32() <= 0) {
                        localFileHeader.setCrc32(fileHeader.getCrc32());
                        localFileHeader.setCrcBuff(fileHeader.getCrcBuff());
                    }
                    if (localFileHeader.getCompressedSize() <= 0) {
                        localFileHeader.setCompressedSize(fileHeader.getCompressedSize());
                    }
                    if (localFileHeader.getUncompressedSize() <= 0) {
                        localFileHeader.setUncompressedSize(fileHeader.getUncompressedSize());
                    }
                    return localFileHeader;
                } catch (Throwable e) {
                    throw new ZipException(e);
                }
            }
        }
        throw new ZipException("invalid read parameters for local header");
    }

    private void readAndSaveAESExtraDataRecord(FileHeader fileHeader) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("file header is null in reading Zip64 Extended Info");
        }
        if (fileHeader.getExtraDataRecords() != null) {
            if (fileHeader.getExtraDataRecords().size() > 0) {
                AESExtraDataRecord readAESExtraDataRecord = readAESExtraDataRecord(fileHeader.getExtraDataRecords());
                if (readAESExtraDataRecord != null) {
                    fileHeader.setAesExtraDataRecord(readAESExtraDataRecord);
                    fileHeader.setEncryptionMethod(99);
                }
            }
        }
    }

    private void readAndSaveAESExtraDataRecord(LocalFileHeader localFileHeader) throws ZipException {
        if (localFileHeader == null) {
            throw new ZipException("file header is null in reading Zip64 Extended Info");
        }
        if (localFileHeader.getExtraDataRecords() != null) {
            if (localFileHeader.getExtraDataRecords().size() > 0) {
                AESExtraDataRecord readAESExtraDataRecord = readAESExtraDataRecord(localFileHeader.getExtraDataRecords());
                if (readAESExtraDataRecord != null) {
                    localFileHeader.setAesExtraDataRecord(readAESExtraDataRecord);
                    localFileHeader.setEncryptionMethod(99);
                }
            }
        }
    }

    private AESExtraDataRecord readAESExtraDataRecord(ArrayList arrayList) throws ZipException {
        if (arrayList == null) {
            return null;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            ExtraDataRecord extraDataRecord = (ExtraDataRecord) arrayList.get(i);
            if (extraDataRecord != null) {
                if (extraDataRecord.getHeader() == 39169) {
                    if (extraDataRecord.getData() == null) {
                        throw new ZipException("corrput AES extra data records");
                    }
                    arrayList = new AESExtraDataRecord();
                    arrayList.setSignature(39169);
                    arrayList.setDataSize(extraDataRecord.getSizeOfData());
                    Object data = extraDataRecord.getData();
                    arrayList.setVersionNumber(Raw.readShortLittleEndian(data, 0));
                    Object obj = new byte[2];
                    System.arraycopy(data, 2, obj, 0, 2);
                    arrayList.setVendorID(new String(obj));
                    arrayList.setAesStrength(data[4] & 255);
                    arrayList.setCompressionMethod(Raw.readShortLittleEndian(data, 5));
                    return arrayList;
                }
            }
        }
        return null;
    }

    private byte[] readIntoBuff(RandomAccessFile randomAccessFile, byte[] bArr) throws ZipException {
        try {
            if (randomAccessFile.read(bArr, 0, bArr.length) != -1) {
                return bArr;
            }
            throw new ZipException("unexpected end of file when reading short buff");
        } catch (Throwable e) {
            throw new ZipException("IOException when reading short buff", e);
        }
    }

    private byte[] getLongByteFromIntByte(byte[] bArr) throws ZipException {
        if (bArr == null) {
            throw new ZipException("input parameter is null, cannot expand to 8 bytes");
        } else if (bArr.length != 4) {
            throw new ZipException("invalid byte length, cannot expand to 8 bytes");
        } else {
            byte[] bArr2 = new byte[8];
            bArr2[0] = bArr[0];
            bArr2[1] = bArr[1];
            bArr2[2] = bArr[2];
            bArr2[3] = bArr[3];
            return bArr2;
        }
    }
}
