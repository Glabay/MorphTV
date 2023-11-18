package ir.mahdi.mzip.rar;

import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.exception.RarException.RarExceptionType;
import ir.mahdi.mzip.rar.impl.FileVolumeManager;
import ir.mahdi.mzip.rar.io.IReadOnlyAccess;
import ir.mahdi.mzip.rar.rarfile.AVHeader;
import ir.mahdi.mzip.rar.rarfile.BaseBlock;
import ir.mahdi.mzip.rar.rarfile.BlockHeader;
import ir.mahdi.mzip.rar.rarfile.CommentHeader;
import ir.mahdi.mzip.rar.rarfile.EAHeader;
import ir.mahdi.mzip.rar.rarfile.EndArcHeader;
import ir.mahdi.mzip.rar.rarfile.FileHeader;
import ir.mahdi.mzip.rar.rarfile.MacInfoHeader;
import ir.mahdi.mzip.rar.rarfile.MainHeader;
import ir.mahdi.mzip.rar.rarfile.MarkHeader;
import ir.mahdi.mzip.rar.rarfile.ProtectHeader;
import ir.mahdi.mzip.rar.rarfile.SignHeader;
import ir.mahdi.mzip.rar.rarfile.SubBlockHeader;
import ir.mahdi.mzip.rar.rarfile.UnixOwnersHeader;
import ir.mahdi.mzip.rar.rarfile.UnrarHeadertype;
import ir.mahdi.mzip.rar.unpack.ComprDataIO;
import ir.mahdi.mzip.rar.unpack.Unpack;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Archive implements Closeable {
    private static Logger logger = Logger.getLogger(Archive.class.getName());
    private int currentHeaderIndex;
    private final ComprDataIO dataIO;
    private final List<BaseBlock> headers;
    private MarkHeader markHead;
    private MainHeader newMhd;
    private IReadOnlyAccess rof;
    private long totalPackedRead;
    private long totalPackedSize;
    private Unpack unpack;
    private final UnrarCallback unrarCallback;
    private Volume volume;
    private VolumeManager volumeManager;

    public Archive(VolumeManager volumeManager) throws RarException, IOException {
        this(volumeManager, null);
    }

    public Archive(VolumeManager volumeManager, UnrarCallback unrarCallback) throws RarException, IOException {
        this.headers = new ArrayList();
        this.markHead = null;
        this.newMhd = null;
        this.totalPackedSize = 0;
        this.totalPackedRead = 0;
        this.volumeManager = volumeManager;
        this.unrarCallback = unrarCallback;
        setVolume(this.volumeManager.nextArchive(this, null));
        this.dataIO = new ComprDataIO(this);
    }

    public Archive(File file) throws RarException, IOException {
        this(new FileVolumeManager(file), (UnrarCallback) null);
    }

    public Archive(File file, UnrarCallback unrarCallback) throws RarException, IOException {
        this(new FileVolumeManager(file), unrarCallback);
    }

    private void setFile(IReadOnlyAccess iReadOnlyAccess, long j) throws IOException {
        this.totalPackedSize = 0;
        this.totalPackedRead = 0;
        close();
        this.rof = iReadOnlyAccess;
        try {
            readHeaders(j);
        } catch (IReadOnlyAccess iReadOnlyAccess2) {
            logger.log(Level.WARNING, "exception in archive constructor maybe file is encrypted or currupt", iReadOnlyAccess2);
        }
        for (BaseBlock baseBlock : this.headers) {
            if (baseBlock.getHeaderType() == UnrarHeadertype.FileHeader) {
                this.totalPackedSize += ((FileHeader) baseBlock).getFullPackSize();
            }
        }
        if (this.unrarCallback != null) {
            this.unrarCallback.volumeProgressChanged(this.totalPackedRead, this.totalPackedSize);
        }
    }

    public void bytesReadRead(int i) {
        if (i > 0) {
            this.totalPackedRead += (long) i;
            if (this.unrarCallback != 0) {
                this.unrarCallback.volumeProgressChanged(this.totalPackedRead, this.totalPackedSize);
            }
        }
    }

    public IReadOnlyAccess getRof() {
        return this.rof;
    }

    public List<FileHeader> getFileHeaders() {
        List<FileHeader> arrayList = new ArrayList();
        for (BaseBlock baseBlock : this.headers) {
            if (baseBlock.getHeaderType().equals(UnrarHeadertype.FileHeader)) {
                arrayList.add((FileHeader) baseBlock);
            }
        }
        return arrayList;
    }

    public FileHeader nextFileHeader() {
        int size = this.headers.size();
        while (this.currentHeaderIndex < size) {
            List list = this.headers;
            int i = this.currentHeaderIndex;
            this.currentHeaderIndex = i + 1;
            BaseBlock baseBlock = (BaseBlock) list.get(i);
            if (baseBlock.getHeaderType() == UnrarHeadertype.FileHeader) {
                return (FileHeader) baseBlock;
            }
        }
        return null;
    }

    public UnrarCallback getUnrarCallback() {
        return this.unrarCallback;
    }

    public boolean isEncrypted() {
        if (this.newMhd != null) {
            return this.newMhd.isEncrypted();
        }
        throw new NullPointerException("mainheader is null");
    }

    private void readHeaders(long j) throws IOException, RarException {
        this.markHead = null;
        this.newMhd = null;
        this.headers.clear();
        int i = 0;
        this.currentHeaderIndex = 0;
        while (true) {
            int i2 = 7;
            byte[] bArr = new byte[7];
            long position = this.rof.getPosition();
            if (position < j) {
                if (this.rof.readFully(bArr, 7) != 0) {
                    BaseBlock baseBlock = new BaseBlock(bArr);
                    baseBlock.setPositionInFile(position);
                    byte[] bArr2;
                    switch (baseBlock.getHeaderType()) {
                        case MarkHeader:
                            this.markHead = new MarkHeader(baseBlock);
                            if (this.markHead.isSignature()) {
                                this.headers.add(this.markHead);
                                break;
                            }
                            throw new RarException(RarExceptionType.badRarArchive);
                        case MainHeader:
                            if (!baseBlock.hasEncryptVersion()) {
                                i2 = 6;
                            }
                            bArr = new byte[i2];
                            this.rof.readFully(bArr, i2);
                            MainHeader mainHeader = new MainHeader(baseBlock, bArr);
                            this.headers.add(mainHeader);
                            this.newMhd = mainHeader;
                            if (!this.newMhd.isEncrypted()) {
                                break;
                            }
                            throw new RarException(RarExceptionType.rarEncryptedException);
                        case SignHeader:
                            bArr2 = new byte[8];
                            this.rof.readFully(bArr2, 8);
                            this.headers.add(new SignHeader(baseBlock, bArr2));
                            break;
                        case AvHeader:
                            bArr = new byte[7];
                            this.rof.readFully(bArr, 7);
                            this.headers.add(new AVHeader(baseBlock, bArr));
                            break;
                        case CommHeader:
                            bArr2 = new byte[6];
                            this.rof.readFully(bArr2, 6);
                            CommentHeader commentHeader = new CommentHeader(baseBlock, bArr2);
                            this.headers.add(commentHeader);
                            this.rof.setPosition(commentHeader.getPositionInFile() + ((long) commentHeader.getHeaderSize()));
                            break;
                        case EndArcHeader:
                            Object endArcHeader;
                            if (baseBlock.hasArchiveDataCRC() != null) {
                                i = 4;
                            }
                            if (baseBlock.hasVolumeNumber() != null) {
                                i += 2;
                            }
                            if (i > 0) {
                                j = new byte[i];
                                this.rof.readFully(j, i);
                                endArcHeader = new EndArcHeader(baseBlock, j);
                            } else {
                                endArcHeader = new EndArcHeader(baseBlock, null);
                            }
                            this.headers.add(endArcHeader);
                            return;
                        default:
                            bArr = new byte[4];
                            this.rof.readFully(bArr, 4);
                            BlockHeader blockHeader = new BlockHeader(baseBlock, bArr);
                            int headerSize;
                            switch (blockHeader.getHeaderType()) {
                                case NewSubHeader:
                                case FileHeader:
                                    headerSize = (blockHeader.getHeaderSize() - 7) - 4;
                                    bArr2 = new byte[headerSize];
                                    this.rof.readFully(bArr2, headerSize);
                                    FileHeader fileHeader = new FileHeader(blockHeader, bArr2);
                                    this.headers.add(fileHeader);
                                    this.rof.setPosition((fileHeader.getPositionInFile() + ((long) fileHeader.getHeaderSize())) + fileHeader.getFullPackSize());
                                    break;
                                case ProtectHeader:
                                    headerSize = (blockHeader.getHeaderSize() - 7) - 4;
                                    bArr2 = new byte[headerSize];
                                    this.rof.readFully(bArr2, headerSize);
                                    ProtectHeader protectHeader = new ProtectHeader(blockHeader, bArr2);
                                    this.rof.setPosition((protectHeader.getPositionInFile() + ((long) protectHeader.getHeaderSize())) + ((long) protectHeader.getDataSize()));
                                    break;
                                case SubHeader:
                                    bArr = new byte[3];
                                    this.rof.readFully(bArr, 3);
                                    SubBlockHeader subBlockHeader = new SubBlockHeader(blockHeader, bArr);
                                    subBlockHeader.print();
                                    byte[] bArr3;
                                    switch (subBlockHeader.getSubType()) {
                                        case MAC_HEAD:
                                            bArr = new byte[8];
                                            this.rof.readFully(bArr, 8);
                                            MacInfoHeader macInfoHeader = new MacInfoHeader(subBlockHeader, bArr);
                                            macInfoHeader.print();
                                            this.headers.add(macInfoHeader);
                                            break;
                                        case BEEA_HEAD:
                                        case NTACL_HEAD:
                                        case STREAM_HEAD:
                                            break;
                                        case EA_HEAD:
                                            bArr3 = new byte[10];
                                            this.rof.readFully(bArr3, 10);
                                            EAHeader eAHeader = new EAHeader(subBlockHeader, bArr3);
                                            eAHeader.print();
                                            this.headers.add(eAHeader);
                                            break;
                                        case UO_HEAD:
                                            headerSize = ((subBlockHeader.getHeaderSize() - 7) - 4) - 3;
                                            bArr3 = new byte[headerSize];
                                            this.rof.readFully(bArr3, headerSize);
                                            UnixOwnersHeader unixOwnersHeader = new UnixOwnersHeader(subBlockHeader, bArr3);
                                            unixOwnersHeader.print();
                                            this.headers.add(unixOwnersHeader);
                                            break;
                                        default:
                                            break;
                                    }
                                default:
                                    logger.warning("Unknown Header");
                                    throw new RarException(RarExceptionType.notRarArchive);
                            }
                    }
                }
            }
            return;
        }
    }

    public void extractFile(FileHeader fileHeader, OutputStream outputStream) throws RarException {
        if (this.headers.contains(fileHeader)) {
            try {
                doExtractFile(fileHeader, outputStream);
                return;
            } catch (Exception e) {
                if ((e instanceof RarException) != null) {
                    throw ((RarException) e);
                }
                throw new RarException(e);
            }
        }
        throw new RarException(RarExceptionType.headerNotInArchive);
    }

    public InputStream getInputStream(final FileHeader fileHeader) throws RarException, IOException {
        InputStream pipedInputStream = new PipedInputStream(32768);
        final PipedOutputStream pipedOutputStream = new PipedOutputStream(pipedInputStream);
        new Thread(new Runnable() {
            public void run() {
                /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
                /*
                r3 = this;
                r0 = ir.mahdi.mzip.rar.Archive.this;	 Catch:{ RarException -> 0x0009, all -> 0x000f }
                r1 = r5;	 Catch:{ RarException -> 0x0009, all -> 0x000f }
                r2 = r1;	 Catch:{ RarException -> 0x0009, all -> 0x000f }
                r0.extractFile(r1, r2);	 Catch:{ RarException -> 0x0009, all -> 0x000f }
            L_0x0009:
                r0 = r1;	 Catch:{ IOException -> 0x0016 }
                r0.close();	 Catch:{ IOException -> 0x0016 }
                goto L_0x0016;
            L_0x000f:
                r0 = move-exception;
                r1 = r1;	 Catch:{ IOException -> 0x0015 }
                r1.close();	 Catch:{ IOException -> 0x0015 }
            L_0x0015:
                throw r0;
            L_0x0016:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: ir.mahdi.mzip.rar.Archive.1.run():void");
            }
        }).start();
        return pipedInputStream;
    }

    private void doExtractFile(FileHeader fileHeader, OutputStream outputStream) throws RarException, IOException {
        this.dataIO.init(outputStream);
        this.dataIO.init(fileHeader);
        this.dataIO.setUnpFileCRC(isOldFormat() ? 0 : -1);
        if (this.unpack == null) {
            this.unpack = new Unpack(this.dataIO);
        }
        if (fileHeader.isSolid() == null) {
            this.unpack.init(null);
        }
        this.unpack.setDestSize(fileHeader.getFullUnpackSize());
        try {
            long packedCRC;
            this.unpack.doUnpack(fileHeader.getUnpVersion(), fileHeader.isSolid());
            fileHeader = this.dataIO.getSubHeader();
            if (fileHeader.isSplitAfter() != null) {
                packedCRC = this.dataIO.getPackedCRC() ^ -1;
            } else {
                packedCRC = this.dataIO.getUnpFileCRC() ^ -1;
            }
            if (packedCRC != ((long) fileHeader.getFileCRC())) {
                throw new RarException(RarExceptionType.crcError);
            }
        } catch (Exception e) {
            this.unpack.cleanUp();
            if ((e instanceof RarException) != null) {
                throw ((RarException) e);
            }
            throw new RarException(e);
        }
    }

    public MainHeader getMainHeader() {
        return this.newMhd;
    }

    public boolean isOldFormat() {
        return this.markHead.isOldFormat();
    }

    public void close() throws IOException {
        if (this.rof != null) {
            this.rof.close();
            this.rof = null;
        }
        if (this.unpack != null) {
            this.unpack.cleanUp();
        }
    }

    public VolumeManager getVolumeManager() {
        return this.volumeManager;
    }

    public void setVolumeManager(VolumeManager volumeManager) {
        this.volumeManager = volumeManager;
    }

    public Volume getVolume() {
        return this.volume;
    }

    public void setVolume(Volume volume) throws IOException {
        this.volume = volume;
        setFile(volume.getReadOnlyAccess(), volume.getLength());
    }
}
