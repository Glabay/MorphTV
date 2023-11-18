package ir.mahdi.mzip.rar.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import net.lingala.zip4j.util.InternalZipConstants;

public class ReadOnlyAccessFile extends RandomAccessFile implements IReadOnlyAccess {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    public ReadOnlyAccessFile(File file) throws FileNotFoundException {
        super(file, InternalZipConstants.READ_MODE);
    }

    public int readFully(byte[] bArr, int i) throws IOException {
        readFully(bArr, 0, i);
        return i;
    }

    public long getPosition() throws IOException {
        return getFilePointer();
    }

    public void setPosition(long j) throws IOException {
        seek(j);
    }
}
