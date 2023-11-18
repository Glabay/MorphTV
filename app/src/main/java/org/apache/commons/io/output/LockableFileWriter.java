package org.apache.commons.io.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

public class LockableFileWriter extends Writer {
    private static final String LCK = ".lck";
    private final File lockFile;
    private final Writer out;

    public LockableFileWriter(String str) throws IOException {
        this(str, false, null);
    }

    public LockableFileWriter(String str, boolean z) throws IOException {
        this(str, z, null);
    }

    public LockableFileWriter(String str, boolean z, String str2) throws IOException {
        this(new File(str), z, str2);
    }

    public LockableFileWriter(File file) throws IOException {
        this(file, false, null);
    }

    public LockableFileWriter(File file, boolean z) throws IOException {
        this(file, z, null);
    }

    @Deprecated
    public LockableFileWriter(File file, boolean z, String str) throws IOException {
        this(file, Charset.defaultCharset(), z, str);
    }

    public LockableFileWriter(File file, Charset charset) throws IOException {
        this(file, charset, false, null);
    }

    public LockableFileWriter(File file, String str) throws IOException {
        this(file, str, false, null);
    }

    public LockableFileWriter(File file, Charset charset, boolean z, String str) throws IOException {
        file = file.getAbsoluteFile();
        if (file.getParentFile() != null) {
            FileUtils.forceMkdir(file.getParentFile());
        }
        if (file.isDirectory()) {
            throw new IOException("File specified is a directory");
        }
        if (str == null) {
            str = System.getProperty("java.io.tmpdir");
        }
        File file2 = new File(str);
        FileUtils.forceMkdir(file2);
        testLockDir(file2);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(file.getName());
        stringBuilder.append(LCK);
        this.lockFile = new File(file2, stringBuilder.toString());
        createLock();
        this.out = initWriter(file, charset, z);
    }

    public LockableFileWriter(File file, String str, boolean z, String str2) throws IOException {
        this(file, Charsets.toCharset(str), z, str2);
    }

    private void testLockDir(File file) throws IOException {
        StringBuilder stringBuilder;
        if (!file.exists()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Could not find lockDir: ");
            stringBuilder.append(file.getAbsolutePath());
            throw new IOException(stringBuilder.toString());
        } else if (!file.canWrite()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Could not write to lockDir: ");
            stringBuilder.append(file.getAbsolutePath());
            throw new IOException(stringBuilder.toString());
        }
    }

    private void createLock() throws IOException {
        synchronized (LockableFileWriter.class) {
            if (this.lockFile.createNewFile()) {
                this.lockFile.deleteOnExit();
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Can't write file, lock ");
                stringBuilder.append(this.lockFile.getAbsolutePath());
                stringBuilder.append(" exists");
                throw new IOException(stringBuilder.toString());
            }
        }
    }

    private Writer initWriter(File file, Charset charset, boolean z) throws IOException {
        boolean exists = file.exists();
        try {
            return new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath(), z), Charsets.toCharset(charset));
        } catch (Charset charset2) {
            FileUtils.deleteQuietly(this.lockFile);
            if (!exists) {
                FileUtils.deleteQuietly(file);
            }
            throw charset2;
        }
    }

    public void close() throws IOException {
        try {
            this.out.close();
        } finally {
            this.lockFile.delete();
        }
    }

    public void write(int i) throws IOException {
        this.out.write(i);
    }

    public void write(char[] cArr) throws IOException {
        this.out.write(cArr);
    }

    public void write(char[] cArr, int i, int i2) throws IOException {
        this.out.write(cArr, i, i2);
    }

    public void write(String str) throws IOException {
        this.out.write(str);
    }

    public void write(String str, int i, int i2) throws IOException {
        this.out.write(str, i, i2);
    }

    public void flush() throws IOException {
        this.out.flush();
    }
}
