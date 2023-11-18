package net.lingala.zip4j.core;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.unzip.Unzip;
import net.lingala.zip4j.util.ArchiveMaintainer;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Zip4jUtil;
import net.lingala.zip4j.zip.ZipEngine;

public class ZipFile {
    private String file;
    private String fileNameCharset;
    private boolean isEncrypted;
    private int mode;
    private ProgressMonitor progressMonitor;
    private boolean runInThread;
    private ZipModel zipModel;

    public ZipFile(String str) throws ZipException {
        this(new File(str));
    }

    public ZipFile(File file) throws ZipException {
        if (file == null) {
            throw new ZipException("Input zip file parameter is not null", 1);
        }
        this.file = file.getPath();
        this.mode = 2;
        this.progressMonitor = new ProgressMonitor();
        this.runInThread = null;
    }

    public void createZipFile(File file, ZipParameters zipParameters) throws ZipException {
        ArrayList arrayList = new ArrayList();
        arrayList.add(file);
        createZipFile(arrayList, zipParameters, false, -1);
    }

    public void createZipFile(File file, ZipParameters zipParameters, boolean z, long j) throws ZipException {
        ArrayList arrayList = new ArrayList();
        arrayList.add(file);
        createZipFile(arrayList, zipParameters, z, j);
    }

    public void createZipFile(ArrayList arrayList, ZipParameters zipParameters) throws ZipException {
        createZipFile(arrayList, zipParameters, false, -1);
    }

    public void createZipFile(ArrayList arrayList, ZipParameters zipParameters, boolean z, long j) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(this.file)) {
            throw new ZipException("zip file path is empty");
        } else if (Zip4jUtil.checkFileExists(this.file)) {
            zipParameters = new StringBuffer("zip file: ");
            zipParameters.append(this.file);
            zipParameters.append(" already exists. To add files to existing zip file use addFile method");
            throw new ZipException(zipParameters.toString());
        } else if (arrayList == null) {
            throw new ZipException("input file ArrayList is null, cannot create zip file");
        } else if (Zip4jUtil.checkArrayListTypes(arrayList, 1)) {
            createNewZipModel();
            this.zipModel.setSplitArchive(z);
            this.zipModel.setSplitLength(j);
            addFiles(arrayList, zipParameters);
        } else {
            throw new ZipException("One or more elements in the input ArrayList is not of type File");
        }
    }

    public void createZipFileFromFolder(String str, ZipParameters zipParameters, boolean z, long j) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(str)) {
            createZipFileFromFolder(new File(str), zipParameters, z, j);
            return;
        }
        throw new ZipException("folderToAdd is empty or null, cannot create Zip File from folder");
    }

    public void createZipFileFromFolder(File file, ZipParameters zipParameters, boolean z, long j) throws ZipException {
        if (file == null) {
            throw new ZipException("folderToAdd is null, cannot create zip file from folder");
        } else if (zipParameters == null) {
            throw new ZipException("input parameters are null, cannot create zip file from folder");
        } else if (Zip4jUtil.checkFileExists(this.file)) {
            zipParameters = new StringBuffer("zip file: ");
            zipParameters.append(this.file);
            zipParameters.append(" already exists. To add files to existing zip file use addFolder method");
            throw new ZipException(zipParameters.toString());
        } else {
            createNewZipModel();
            this.zipModel.setSplitArchive(z);
            if (z) {
                this.zipModel.setSplitLength(j);
            }
            addFolder(file, zipParameters, false);
        }
    }

    public void addFile(File file, ZipParameters zipParameters) throws ZipException {
        ArrayList arrayList = new ArrayList();
        arrayList.add(file);
        addFiles(arrayList, zipParameters);
    }

    public void addFiles(ArrayList arrayList, ZipParameters zipParameters) throws ZipException {
        checkZipModel();
        if (this.zipModel == null) {
            throw new ZipException("internal error: zip model is null");
        } else if (arrayList == null) {
            throw new ZipException("input file ArrayList is null, cannot add files");
        } else if (!Zip4jUtil.checkArrayListTypes(arrayList, 1)) {
            throw new ZipException("One or more elements in the input ArrayList is not of type File");
        } else if (zipParameters == null) {
            throw new ZipException("input parameters are null, cannot add files to zip");
        } else if (this.progressMonitor.getState() == 1) {
            throw new ZipException("invalid operation - Zip4j is in busy state");
        } else if (Zip4jUtil.checkFileExists(this.file) && this.zipModel.isSplitArchive()) {
            throw new ZipException("Zip file already exists. Zip file format does not allow updating split/spanned files");
        } else {
            new ZipEngine(this.zipModel).addFiles(arrayList, zipParameters, this.progressMonitor, this.runInThread);
        }
    }

    public void addFolder(String str, ZipParameters zipParameters) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(str)) {
            addFolder(new File(str), zipParameters);
            return;
        }
        throw new ZipException("input path is null or empty, cannot add folder to zip file");
    }

    public void addFolder(File file, ZipParameters zipParameters) throws ZipException {
        if (file == null) {
            throw new ZipException("input path is null, cannot add folder to zip file");
        } else if (zipParameters == null) {
            throw new ZipException("input parameters are null, cannot add folder to zip file");
        } else {
            addFolder(file, zipParameters, true);
        }
    }

    private void addFolder(File file, ZipParameters zipParameters, boolean z) throws ZipException {
        checkZipModel();
        if (this.zipModel == null) {
            throw new ZipException("internal error: zip model is null");
        } else if (z && this.zipModel.isSplitArchive()) {
            throw new ZipException("This is a split archive. Zip file format does not allow updating split/spanned files");
        } else {
            new ZipEngine(this.zipModel).addFolderToZip(file, zipParameters, this.progressMonitor, this.runInThread);
        }
    }

    public void addStream(InputStream inputStream, ZipParameters zipParameters) throws ZipException {
        if (inputStream == null) {
            throw new ZipException("inputstream is null, cannot add file to zip");
        } else if (zipParameters == null) {
            throw new ZipException("zip parameters are null");
        } else {
            setRunInThread(false);
            checkZipModel();
            if (this.zipModel == null) {
                throw new ZipException("internal error: zip model is null");
            } else if (Zip4jUtil.checkFileExists(this.file) && this.zipModel.isSplitArchive()) {
                throw new ZipException("Zip file already exists. Zip file format does not allow updating split/spanned files");
            } else {
                new ZipEngine(this.zipModel).addStreamToZip(inputStream, zipParameters);
            }
        }
    }

    private void readZipInfo() throws net.lingala.zip4j.exception.ZipException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r5 = this;
        r0 = r5.file;
        r0 = net.lingala.zip4j.util.Zip4jUtil.checkFileExists(r0);
        if (r0 != 0) goto L_0x0010;
    L_0x0008:
        r0 = new net.lingala.zip4j.exception.ZipException;
        r1 = "zip file does not exist";
        r0.<init>(r1);
        throw r0;
    L_0x0010:
        r0 = r5.file;
        r0 = net.lingala.zip4j.util.Zip4jUtil.checkFileReadAccess(r0);
        if (r0 != 0) goto L_0x0020;
    L_0x0018:
        r0 = new net.lingala.zip4j.exception.ZipException;
        r1 = "no read access for the input zip file";
        r0.<init>(r1);
        throw r0;
    L_0x0020:
        r0 = r5.mode;
        r1 = 2;
        if (r0 == r1) goto L_0x002d;
    L_0x0025:
        r0 = new net.lingala.zip4j.exception.ZipException;
        r1 = "Invalid mode";
        r0.<init>(r1);
        throw r0;
    L_0x002d:
        r0 = 0;
        r1 = new java.io.RandomAccessFile;	 Catch:{ FileNotFoundException -> 0x0065, all -> 0x0060 }
        r2 = new java.io.File;	 Catch:{ FileNotFoundException -> 0x0065, all -> 0x0060 }
        r3 = r5.file;	 Catch:{ FileNotFoundException -> 0x0065, all -> 0x0060 }
        r2.<init>(r3);	 Catch:{ FileNotFoundException -> 0x0065, all -> 0x0060 }
        r3 = "r";	 Catch:{ FileNotFoundException -> 0x0065, all -> 0x0060 }
        r1.<init>(r2, r3);	 Catch:{ FileNotFoundException -> 0x0065, all -> 0x0060 }
        r0 = r5.zipModel;	 Catch:{ FileNotFoundException -> 0x005e }
        if (r0 != 0) goto L_0x0058;	 Catch:{ FileNotFoundException -> 0x005e }
    L_0x0040:
        r0 = new net.lingala.zip4j.core.HeaderReader;	 Catch:{ FileNotFoundException -> 0x005e }
        r0.<init>(r1);	 Catch:{ FileNotFoundException -> 0x005e }
        r2 = r5.fileNameCharset;	 Catch:{ FileNotFoundException -> 0x005e }
        r0 = r0.readAllHeaders(r2);	 Catch:{ FileNotFoundException -> 0x005e }
        r5.zipModel = r0;	 Catch:{ FileNotFoundException -> 0x005e }
        r0 = r5.zipModel;	 Catch:{ FileNotFoundException -> 0x005e }
        if (r0 == 0) goto L_0x0058;	 Catch:{ FileNotFoundException -> 0x005e }
    L_0x0051:
        r0 = r5.zipModel;	 Catch:{ FileNotFoundException -> 0x005e }
        r2 = r5.file;	 Catch:{ FileNotFoundException -> 0x005e }
        r0.setZipFile(r2);	 Catch:{ FileNotFoundException -> 0x005e }
    L_0x0058:
        if (r1 == 0) goto L_0x005d;
    L_0x005a:
        r1.close();	 Catch:{ IOException -> 0x005d }
    L_0x005d:
        return;
    L_0x005e:
        r0 = move-exception;
        goto L_0x0069;
    L_0x0060:
        r1 = move-exception;
        r4 = r1;
        r1 = r0;
        r0 = r4;
        goto L_0x0070;
    L_0x0065:
        r1 = move-exception;
        r4 = r1;
        r1 = r0;
        r0 = r4;
    L_0x0069:
        r2 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ all -> 0x006f }
        r2.<init>(r0);	 Catch:{ all -> 0x006f }
        throw r2;	 Catch:{ all -> 0x006f }
    L_0x006f:
        r0 = move-exception;
    L_0x0070:
        if (r1 == 0) goto L_0x0075;
    L_0x0072:
        r1.close();	 Catch:{ IOException -> 0x0075 }
    L_0x0075:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.core.ZipFile.readZipInfo():void");
    }

    public void extractAll(String str) throws ZipException {
        extractAll(str, null);
    }

    public void extractAll(String str, UnzipParameters unzipParameters) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(str)) {
            throw new ZipException("output path is null or invalid");
        } else if (Zip4jUtil.checkOutputFolder(str)) {
            if (this.zipModel == null) {
                readZipInfo();
            }
            if (this.zipModel == null) {
                throw new ZipException("Internal error occurred when extracting zip file");
            } else if (this.progressMonitor.getState() == 1) {
                throw new ZipException("invalid operation - Zip4j is in busy state");
            } else {
                new Unzip(this.zipModel).extractAll(unzipParameters, str, this.progressMonitor, this.runInThread);
            }
        } else {
            throw new ZipException("invalid output path");
        }
    }

    public void extractFile(FileHeader fileHeader, String str) throws ZipException {
        extractFile(fileHeader, str, null);
    }

    public void extractFile(FileHeader fileHeader, String str, UnzipParameters unzipParameters) throws ZipException {
        extractFile(fileHeader, str, unzipParameters, null);
    }

    public void extractFile(FileHeader fileHeader, String str, UnzipParameters unzipParameters, String str2) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("input file header is null, cannot extract file");
        } else if (Zip4jUtil.isStringNotNullAndNotEmpty(str)) {
            readZipInfo();
            if (this.progressMonitor.getState() == 1) {
                throw new ZipException("invalid operation - Zip4j is in busy state");
            }
            fileHeader.extractFile(this.zipModel, str, unzipParameters, str2, this.progressMonitor, this.runInThread);
        } else {
            throw new ZipException("destination path is empty or null, cannot extract file");
        }
    }

    public void extractFile(String str, String str2) throws ZipException {
        extractFile(str, str2, null);
    }

    public void extractFile(String str, String str2, UnzipParameters unzipParameters) throws ZipException {
        extractFile(str, str2, unzipParameters, null);
    }

    public void extractFile(String str, String str2, UnzipParameters unzipParameters, String str3) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(str)) {
            throw new ZipException("file to extract is null or empty, cannot extract file");
        } else if (Zip4jUtil.isStringNotNullAndNotEmpty(str2)) {
            readZipInfo();
            FileHeader fileHeader = Zip4jUtil.getFileHeader(this.zipModel, str);
            if (fileHeader == null) {
                throw new ZipException("file header not found for given file name, cannot extract file");
            } else if (this.progressMonitor.getState() == 1) {
                throw new ZipException("invalid operation - Zip4j is in busy state");
            } else {
                fileHeader.extractFile(this.zipModel, str2, unzipParameters, str3, this.progressMonitor, this.runInThread);
            }
        } else {
            throw new ZipException("destination string path is empty or null, cannot extract file");
        }
    }

    public void setPassword(String str) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(str)) {
            setPassword(str.toCharArray());
            return;
        }
        throw new NullPointerException();
    }

    public void setPassword(char[] cArr) throws ZipException {
        if (this.zipModel == null) {
            readZipInfo();
            if (this.zipModel == null) {
                throw new ZipException("Zip Model is null");
            }
        }
        if (this.zipModel.getCentralDirectory() != null) {
            if (this.zipModel.getCentralDirectory().getFileHeaders() != null) {
                int i = 0;
                while (i < this.zipModel.getCentralDirectory().getFileHeaders().size()) {
                    if (this.zipModel.getCentralDirectory().getFileHeaders().get(i) != null && ((FileHeader) this.zipModel.getCentralDirectory().getFileHeaders().get(i)).isEncrypted()) {
                        ((FileHeader) this.zipModel.getCentralDirectory().getFileHeaders().get(i)).setPassword(cArr);
                    }
                    i++;
                }
                return;
            }
        }
        throw new ZipException("invalid zip file");
    }

    public List getFileHeaders() throws ZipException {
        readZipInfo();
        if (this.zipModel != null) {
            if (this.zipModel.getCentralDirectory() != null) {
                return this.zipModel.getCentralDirectory().getFileHeaders();
            }
        }
        return null;
    }

    public FileHeader getFileHeader(String str) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(str)) {
            readZipInfo();
            if (this.zipModel != null) {
                if (this.zipModel.getCentralDirectory() != null) {
                    return Zip4jUtil.getFileHeader(this.zipModel, str);
                }
            }
            return null;
        }
        throw new ZipException("input file name is emtpy or null, cannot get FileHeader");
    }

    public boolean isEncrypted() throws ZipException {
        if (this.zipModel == null) {
            readZipInfo();
            if (this.zipModel == null) {
                throw new ZipException("Zip Model is null");
            }
        }
        if (this.zipModel.getCentralDirectory() != null) {
            if (this.zipModel.getCentralDirectory().getFileHeaders() != null) {
                ArrayList fileHeaders = this.zipModel.getCentralDirectory().getFileHeaders();
                for (int i = 0; i < fileHeaders.size(); i++) {
                    FileHeader fileHeader = (FileHeader) fileHeaders.get(i);
                    if (fileHeader != null && fileHeader.isEncrypted()) {
                        this.isEncrypted = true;
                        break;
                    }
                }
                return this.isEncrypted;
            }
        }
        throw new ZipException("invalid zip file");
    }

    public boolean isSplitArchive() throws ZipException {
        if (this.zipModel == null) {
            readZipInfo();
            if (this.zipModel == null) {
                throw new ZipException("Zip Model is null");
            }
        }
        return this.zipModel.isSplitArchive();
    }

    public void removeFile(String str) throws ZipException {
        if (Zip4jUtil.isStringNotNullAndNotEmpty(str)) {
            if (this.zipModel == null && Zip4jUtil.checkFileExists(this.file)) {
                readZipInfo();
            }
            if (this.zipModel.isSplitArchive()) {
                throw new ZipException("Zip file format does not allow updating split/spanned files");
            }
            FileHeader fileHeader = Zip4jUtil.getFileHeader(this.zipModel, str);
            if (fileHeader == null) {
                StringBuffer stringBuffer = new StringBuffer("could not find file header for file: ");
                stringBuffer.append(str);
                throw new ZipException(stringBuffer.toString());
            }
            removeFile(fileHeader);
            return;
        }
        throw new ZipException("file name is empty or null, cannot remove file");
    }

    public void removeFile(FileHeader fileHeader) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("file header is null, cannot remove file");
        }
        if (this.zipModel == null && Zip4jUtil.checkFileExists(this.file)) {
            readZipInfo();
        }
        if (this.zipModel.isSplitArchive()) {
            throw new ZipException("Zip file format does not allow updating split/spanned files");
        }
        ArchiveMaintainer archiveMaintainer = new ArchiveMaintainer();
        archiveMaintainer.initProgressMonitorForRemoveOp(this.zipModel, fileHeader, this.progressMonitor);
        archiveMaintainer.removeZipFile(this.zipModel, fileHeader, this.progressMonitor, this.runInThread);
    }

    public void mergeSplitFiles(File file) throws ZipException {
        if (file == null) {
            throw new ZipException("outputZipFile is null, cannot merge split files");
        } else if (file.exists()) {
            throw new ZipException("output Zip File already exists");
        } else {
            checkZipModel();
            if (this.zipModel == null) {
                throw new ZipException("zip model is null, corrupt zip file?");
            }
            ArchiveMaintainer archiveMaintainer = new ArchiveMaintainer();
            archiveMaintainer.initProgressMonitorForMergeOp(this.zipModel, this.progressMonitor);
            archiveMaintainer.mergeSplitZipFiles(this.zipModel, file, this.progressMonitor, this.runInThread);
        }
    }

    public void setComment(String str) throws ZipException {
        if (str == null) {
            throw new ZipException("input comment is null, cannot update zip file");
        } else if (Zip4jUtil.checkFileExists(this.file)) {
            readZipInfo();
            if (this.zipModel == null) {
                throw new ZipException("zipModel is null, cannot update zip file");
            } else if (this.zipModel.getEndCentralDirRecord() == null) {
                throw new ZipException("end of central directory is null, cannot set comment");
            } else {
                new ArchiveMaintainer().setComment(this.zipModel, str);
            }
        } else {
            throw new ZipException("zip file does not exist, cannot set comment for zip file");
        }
    }

    public String getComment() throws ZipException {
        return getComment(null);
    }

    public String getComment(String str) throws ZipException {
        if (str == null) {
            if (Zip4jUtil.isSupportedCharset(InternalZipConstants.CHARSET_COMMENTS_DEFAULT) != null) {
                str = InternalZipConstants.CHARSET_COMMENTS_DEFAULT;
            } else {
                str = InternalZipConstants.CHARSET_DEFAULT;
            }
        }
        if (Zip4jUtil.checkFileExists(this.file)) {
            checkZipModel();
            if (this.zipModel == null) {
                throw new ZipException("zip model is null, cannot read comment");
            } else if (this.zipModel.getEndCentralDirRecord() == null) {
                throw new ZipException("end of central directory record is null, cannot read comment");
            } else {
                if (this.zipModel.getEndCentralDirRecord().getCommentBytes() != null) {
                    if (this.zipModel.getEndCentralDirRecord().getCommentBytes().length > 0) {
                        try {
                            return new String(this.zipModel.getEndCentralDirRecord().getCommentBytes(), str);
                        } catch (Throwable e) {
                            throw new ZipException(e);
                        }
                    }
                }
                return null;
            }
        }
        throw new ZipException("zip file does not exist, cannot read comment");
    }

    private void checkZipModel() throws ZipException {
        if (this.zipModel != null) {
            return;
        }
        if (Zip4jUtil.checkFileExists(this.file)) {
            readZipInfo();
        } else {
            createNewZipModel();
        }
    }

    private void createNewZipModel() {
        this.zipModel = new ZipModel();
        this.zipModel.setZipFile(this.file);
        this.zipModel.setFileNameCharset(this.fileNameCharset);
    }

    public void setFileNameCharset(String str) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(str)) {
            throw new ZipException("null or empty charset name");
        } else if (Zip4jUtil.isSupportedCharset(str)) {
            this.fileNameCharset = str;
        } else {
            StringBuffer stringBuffer = new StringBuffer("unsupported charset: ");
            stringBuffer.append(str);
            throw new ZipException(stringBuffer.toString());
        }
    }

    public ZipInputStream getInputStream(FileHeader fileHeader) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("FileHeader is null, cannot get InputStream");
        }
        checkZipModel();
        if (this.zipModel != null) {
            return new Unzip(this.zipModel).getInputStream(fileHeader);
        }
        throw new ZipException("zip model is null, cannot get inputstream");
    }

    public boolean isValidZipFile() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r1 = this;
        r1.readZipInfo();	 Catch:{ Exception -> 0x0005 }
        r0 = 1;
        return r0;
    L_0x0005:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.core.ZipFile.isValidZipFile():boolean");
    }

    public ArrayList getSplitZipFiles() throws ZipException {
        checkZipModel();
        return Zip4jUtil.getSplitZipFiles(this.zipModel);
    }

    public ProgressMonitor getProgressMonitor() {
        return this.progressMonitor;
    }

    public boolean isRunInThread() {
        return this.runInThread;
    }

    public void setRunInThread(boolean z) {
        this.runInThread = z;
    }

    public File getFile() {
        return new File(this.file);
    }
}
