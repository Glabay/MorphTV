package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.TreeTraverser;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.lingala.zip4j.util.InternalZipConstants;
import org.apache.commons.io.IOUtils;
import org.threeten.bp.chrono.HijrahDate;

@Beta
public final class Files {
    private static final TreeTraverser<File> FILE_TREE_TRAVERSER = new C11512();
    private static final int TEMP_DIR_ATTEMPTS = 10000;

    /* renamed from: com.google.common.io.Files$1 */
    static class C11501 implements LineProcessor<List<String>> {
        final List<String> result = Lists.newArrayList();

        C11501() {
        }

        public boolean processLine(String str) {
            this.result.add(str);
            return true;
        }

        public List<String> getResult() {
            return this.result;
        }
    }

    /* renamed from: com.google.common.io.Files$2 */
    static class C11512 extends TreeTraverser<File> {
        public String toString() {
            return "Files.fileTreeTraverser()";
        }

        C11512() {
        }

        public Iterable<File> children(File file) {
            if (file.isDirectory()) {
                file = file.listFiles();
                if (file != null) {
                    return Collections.unmodifiableList(Arrays.asList(file));
                }
            }
            return Collections.emptyList();
        }
    }

    private static final class FileByteSink extends ByteSink {
        private final File file;
        private final ImmutableSet<FileWriteMode> modes;

        private FileByteSink(File file, FileWriteMode... fileWriteModeArr) {
            this.file = (File) Preconditions.checkNotNull(file);
            this.modes = ImmutableSet.copyOf((Object[]) fileWriteModeArr);
        }

        public FileOutputStream openStream() throws IOException {
            return new FileOutputStream(this.file, this.modes.contains(FileWriteMode.APPEND));
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Files.asByteSink(");
            stringBuilder.append(this.file);
            stringBuilder.append(", ");
            stringBuilder.append(this.modes);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private static final class FileByteSource extends ByteSource {
        private final File file;

        private FileByteSource(File file) {
            this.file = (File) Preconditions.checkNotNull(file);
        }

        public FileInputStream openStream() throws IOException {
            return new FileInputStream(this.file);
        }

        public long size() throws IOException {
            if (this.file.isFile()) {
                return this.file.length();
            }
            throw new FileNotFoundException(this.file.toString());
        }

        public byte[] read() throws IOException {
            Closer create = Closer.create();
            try {
                FileInputStream fileInputStream = (FileInputStream) create.register(openStream());
                byte[] readFile = Files.readFile(fileInputStream, fileInputStream.getChannel().size());
                create.close();
                return readFile;
            } catch (Throwable th) {
                create.close();
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Files.asByteSource(");
            stringBuilder.append(this.file);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private enum FilePredicate implements Predicate<File> {
        IS_DIRECTORY {
            public String toString() {
                return "Files.isDirectory()";
            }

            public boolean apply(File file) {
                return file.isDirectory();
            }
        },
        IS_FILE {
            public String toString() {
                return "Files.isFile()";
            }

            public boolean apply(File file) {
                return file.isFile();
            }
        }
    }

    private Files() {
    }

    public static BufferedReader newReader(File file, Charset charset) throws FileNotFoundException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(charset);
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
    }

    public static BufferedWriter newWriter(File file, Charset charset) throws FileNotFoundException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(charset);
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
    }

    public static ByteSource asByteSource(File file) {
        return new FileByteSource(file);
    }

    static byte[] readFile(InputStream inputStream, long j) throws IOException {
        if (j <= 2147483647L) {
            return j == 0 ? ByteStreams.toByteArray(inputStream) : ByteStreams.toByteArray(inputStream, (int) j);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("file is too large to fit in a byte array: ");
            stringBuilder.append(j);
            stringBuilder.append(" bytes");
            throw new OutOfMemoryError(stringBuilder.toString());
        }
    }

    public static ByteSink asByteSink(File file, FileWriteMode... fileWriteModeArr) {
        return new FileByteSink(file, fileWriteModeArr);
    }

    public static CharSource asCharSource(File file, Charset charset) {
        return asByteSource(file).asCharSource(charset);
    }

    public static CharSink asCharSink(File file, Charset charset, FileWriteMode... fileWriteModeArr) {
        return asByteSink(file, fileWriteModeArr).asCharSink(charset);
    }

    private static FileWriteMode[] modes(boolean z) {
        if (!z) {
            return new FileWriteMode[0];
        }
        return new FileWriteMode[]{FileWriteMode.APPEND};
    }

    public static byte[] toByteArray(File file) throws IOException {
        return asByteSource(file).read();
    }

    public static String toString(File file, Charset charset) throws IOException {
        return asCharSource(file, charset).read();
    }

    public static void write(byte[] bArr, File file) throws IOException {
        asByteSink(file, new FileWriteMode[0]).write(bArr);
    }

    public static void copy(File file, OutputStream outputStream) throws IOException {
        asByteSource(file).copyTo(outputStream);
    }

    public static void copy(File file, File file2) throws IOException {
        Preconditions.checkArgument(file.equals(file2) ^ true, "Source %s and destination %s must be different", file, file2);
        asByteSource(file).copyTo(asByteSink(file2, new FileWriteMode[0]));
    }

    public static void write(CharSequence charSequence, File file, Charset charset) throws IOException {
        asCharSink(file, charset, new FileWriteMode[0]).write(charSequence);
    }

    public static void append(CharSequence charSequence, File file, Charset charset) throws IOException {
        write(charSequence, file, charset, true);
    }

    private static void write(CharSequence charSequence, File file, Charset charset, boolean z) throws IOException {
        asCharSink(file, charset, modes(z)).write(charSequence);
    }

    public static void copy(File file, Charset charset, Appendable appendable) throws IOException {
        asCharSource(file, charset).copyTo(appendable);
    }

    public static boolean equal(File file, File file2) throws IOException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(file2);
        if (file != file2) {
            if (!file.equals(file2)) {
                long length = file.length();
                long length2 = file2.length();
                if (length == 0 || length2 == 0 || length == length2) {
                    return asByteSource(file).contentEquals(asByteSource(file2));
                }
                return null;
            }
        }
        return true;
    }

    public static File createTempDir() {
        File file = new File(System.getProperty("java.io.tmpdir"));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.currentTimeMillis());
        stringBuilder.append("-");
        String stringBuilder2 = stringBuilder.toString();
        for (int i = 0; i < 10000; i++) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(stringBuilder2);
            stringBuilder3.append(i);
            File file2 = new File(file, stringBuilder3.toString());
            if (file2.mkdir()) {
                return file2;
            }
        }
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append("Failed to create directory within 10000 attempts (tried ");
        stringBuilder4.append(stringBuilder2);
        stringBuilder4.append("0 to ");
        stringBuilder4.append(stringBuilder2);
        stringBuilder4.append(HijrahDate.MAX_VALUE_OF_ERA);
        stringBuilder4.append(')');
        throw new IllegalStateException(stringBuilder4.toString());
    }

    public static void touch(File file) throws IOException {
        Preconditions.checkNotNull(file);
        if (!file.createNewFile() && !file.setLastModified(System.currentTimeMillis())) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to update modification time of ");
            stringBuilder.append(file);
            throw new IOException(stringBuilder.toString());
        }
    }

    public static void createParentDirs(File file) throws IOException {
        Preconditions.checkNotNull(file);
        File parentFile = file.getCanonicalFile().getParentFile();
        if (parentFile != null) {
            parentFile.mkdirs();
            if (!parentFile.isDirectory()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to create parent directories of ");
                stringBuilder.append(file);
                throw new IOException(stringBuilder.toString());
            }
        }
    }

    public static void move(File file, File file2) throws IOException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(file2);
        Preconditions.checkArgument(file.equals(file2) ^ true, "Source %s and destination %s must be different", file, file2);
        if (!file.renameTo(file2)) {
            copy(file, file2);
            if (!file.delete()) {
                StringBuilder stringBuilder;
                if (file2.delete()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to delete ");
                    stringBuilder.append(file);
                    throw new IOException(stringBuilder.toString());
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to delete ");
                stringBuilder.append(file2);
                throw new IOException(stringBuilder.toString());
            }
        }
    }

    public static String readFirstLine(File file, Charset charset) throws IOException {
        return asCharSource(file, charset).readFirstLine();
    }

    public static List<String> readLines(File file, Charset charset) throws IOException {
        return (List) readLines(file, charset, new C11501());
    }

    public static <T> T readLines(File file, Charset charset, LineProcessor<T> lineProcessor) throws IOException {
        return asCharSource(file, charset).readLines(lineProcessor);
    }

    public static <T> T readBytes(File file, ByteProcessor<T> byteProcessor) throws IOException {
        return asByteSource(file).read(byteProcessor);
    }

    public static HashCode hash(File file, HashFunction hashFunction) throws IOException {
        return asByteSource(file).hash(hashFunction);
    }

    public static MappedByteBuffer map(File file) throws IOException {
        Preconditions.checkNotNull(file);
        return map(file, MapMode.READ_ONLY);
    }

    public static MappedByteBuffer map(File file, MapMode mapMode) throws IOException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(mapMode);
        if (file.exists()) {
            return map(file, mapMode, file.length());
        }
        throw new FileNotFoundException(file.toString());
    }

    public static MappedByteBuffer map(File file, MapMode mapMode, long j) throws FileNotFoundException, IOException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(mapMode);
        Closer create = Closer.create();
        try {
            file = map((RandomAccessFile) create.register(new RandomAccessFile(file, mapMode == MapMode.READ_ONLY ? InternalZipConstants.READ_MODE : InternalZipConstants.WRITE_MODE)), mapMode, j);
            create.close();
            return file;
        } catch (Throwable th) {
            create.close();
        }
    }

    private static MappedByteBuffer map(RandomAccessFile randomAccessFile, MapMode mapMode, long j) throws IOException {
        Closer create = Closer.create();
        try {
            randomAccessFile = ((FileChannel) create.register(randomAccessFile.getChannel())).map(mapMode, 0, j);
            create.close();
            return randomAccessFile;
        } catch (Throwable th) {
            create.close();
        }
    }

    public static String simplifyPath(String str) {
        Preconditions.checkNotNull(str);
        if (str.length() == 0) {
            return ".";
        }
        Iterable<String> split = Splitter.on((char) IOUtils.DIR_SEPARATOR_UNIX).omitEmptyStrings().split(str);
        Iterable arrayList = new ArrayList();
        for (String str2 : split) {
            if (!str2.equals(".")) {
                if (!str2.equals("..")) {
                    arrayList.add(str2);
                } else if (arrayList.size() <= 0 || ((String) arrayList.get(arrayList.size() - 1)).equals("..")) {
                    arrayList.add("..");
                } else {
                    arrayList.remove(arrayList.size() - 1);
                }
            }
        }
        String join = Joiner.on((char) IOUtils.DIR_SEPARATOR_UNIX).join(arrayList);
        if (str.charAt(0) == 47) {
            str = new StringBuilder();
            str.append("/");
            str.append(join);
            join = str.toString();
        }
        while (join.startsWith("/../") != null) {
            join = join.substring(3);
        }
        if (join.equals("/..") != null) {
            join = "/";
        } else if ("".equals(join) != null) {
            join = ".";
        }
        return join;
    }

    public static String getFileExtension(String str) {
        Preconditions.checkNotNull(str);
        str = new File(str).getName();
        int lastIndexOf = str.lastIndexOf(46);
        if (lastIndexOf == -1) {
            return "";
        }
        return str.substring(lastIndexOf + 1);
    }

    public static String getNameWithoutExtension(String str) {
        Preconditions.checkNotNull(str);
        str = new File(str).getName();
        int lastIndexOf = str.lastIndexOf(46);
        return lastIndexOf == -1 ? str : str.substring(0, lastIndexOf);
    }

    public static TreeTraverser<File> fileTreeTraverser() {
        return FILE_TREE_TRAVERSER;
    }

    public static Predicate<File> isDirectory() {
        return FilePredicate.IS_DIRECTORY;
    }

    public static Predicate<File> isFile() {
        return FilePredicate.IS_FILE;
    }
}
