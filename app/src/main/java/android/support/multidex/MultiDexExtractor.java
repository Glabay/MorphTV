package android.support.multidex;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import net.lingala.zip4j.util.InternalZipConstants;

final class MultiDexExtractor implements Closeable {
    private static final int BUFFER_SIZE = 16384;
    private static final String DEX_PREFIX = "classes";
    static final String DEX_SUFFIX = ".dex";
    private static final String EXTRACTED_NAME_EXT = ".classes";
    static final String EXTRACTED_SUFFIX = ".zip";
    private static final String KEY_CRC = "crc";
    private static final String KEY_DEX_CRC = "dex.crc.";
    private static final String KEY_DEX_NUMBER = "dex.number";
    private static final String KEY_DEX_TIME = "dex.time.";
    private static final String KEY_TIME_STAMP = "timestamp";
    private static final String LOCK_FILENAME = "MultiDex.lock";
    private static final int MAX_EXTRACT_ATTEMPTS = 3;
    private static final long NO_VALUE = -1;
    private static final String PREFS_FILE = "multidex.version";
    private static final String TAG = "MultiDex";
    private final FileLock cacheLock;
    private final File dexDir;
    private final FileChannel lockChannel;
    private final RandomAccessFile lockRaf;
    private final File sourceApk;
    private final long sourceCrc;

    /* renamed from: android.support.multidex.MultiDexExtractor$1 */
    class C00381 implements FileFilter {
        C00381() {
        }

        public boolean accept(File file) {
            return file.getName().equals(MultiDexExtractor.LOCK_FILENAME) ^ 1;
        }
    }

    private static class ExtractedDex extends File {
        public long crc = -1;

        public ExtractedDex(File file, String str) {
            super(file, str);
        }
    }

    MultiDexExtractor(File file, File file2) throws IOException {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MultiDexExtractor(");
        stringBuilder.append(file.getPath());
        stringBuilder.append(", ");
        stringBuilder.append(file2.getPath());
        stringBuilder.append(")");
        Log.i(str, stringBuilder.toString());
        this.sourceApk = file;
        this.dexDir = file2;
        this.sourceCrc = getZipCrc(file);
        file = new File(file2, LOCK_FILENAME);
        this.lockRaf = new RandomAccessFile(file, InternalZipConstants.WRITE_MODE);
        try {
            this.lockChannel = this.lockRaf.getChannel();
            file2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Blocking on lock ");
            stringBuilder2.append(file.getPath());
            Log.i(file2, stringBuilder2.toString());
            this.cacheLock = this.lockChannel.lock();
            file2 = TAG;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(file.getPath());
            stringBuilder2.append(" locked");
            Log.i(file2, stringBuilder2.toString());
        } catch (File file3) {
            closeQuietly(this.lockChannel);
            throw file3;
        } catch (File file32) {
            closeQuietly(this.lockRaf);
            throw file32;
        }
    }

    List<? extends File> load(Context context, String str, boolean z) throws IOException {
        String str2 = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MultiDexExtractor.load(");
        stringBuilder.append(this.sourceApk.getPath());
        stringBuilder.append(", ");
        stringBuilder.append(z);
        stringBuilder.append(", ");
        stringBuilder.append(str);
        stringBuilder.append(")");
        Log.i(str2, stringBuilder.toString());
        if (this.cacheLock.isValid()) {
            if (z || isModified(context, this.sourceApk, this.sourceCrc, str)) {
                if (z) {
                    Log.i(TAG, "Forced extraction must be performed.");
                } else {
                    Log.i(TAG, "Detected that extraction must be performed.");
                }
                z = performExtractions();
                putStoredApkInfo(context, str, getTimeStamp(this.sourceApk), this.sourceCrc, z);
            } else {
                try {
                    z = loadExistingExtractions(context, str);
                } catch (boolean z2) {
                    Log.w(TAG, "Failed to reload existing extracted secondary dex files, falling back to fresh extraction", z2);
                    z2 = performExtractions();
                    putStoredApkInfo(context, str, getTimeStamp(this.sourceApk), this.sourceCrc, z2);
                }
            }
            context = TAG;
            str = new StringBuilder();
            str.append("load found ");
            str.append(z2.size());
            str.append(" secondary dex files");
            Log.i(context, str.toString());
            return z2;
        }
        throw new IllegalStateException("MultiDexExtractor was closed");
    }

    public void close() throws IOException {
        this.cacheLock.release();
        this.lockChannel.close();
        this.lockRaf.close();
    }

    private List<ExtractedDex> loadExistingExtractions(Context context, String str) throws IOException {
        String str2 = str;
        Log.i(TAG, "loading existing secondary dex files");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.sourceApk.getName());
        stringBuilder.append(EXTRACTED_NAME_EXT);
        String stringBuilder2 = stringBuilder.toString();
        SharedPreferences multiDexPreferences = getMultiDexPreferences(context);
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(str2);
        stringBuilder3.append(KEY_DEX_NUMBER);
        int i = multiDexPreferences.getInt(stringBuilder3.toString(), 1);
        List<ExtractedDex> arrayList = new ArrayList(i - 1);
        int i2 = 2;
        while (i2 <= i) {
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append(stringBuilder2);
            stringBuilder4.append(i2);
            stringBuilder4.append(EXTRACTED_SUFFIX);
            File extractedDex = new ExtractedDex(r0.dexDir, stringBuilder4.toString());
            if (extractedDex.isFile()) {
                extractedDex.crc = getZipCrc(extractedDex);
                stringBuilder4 = new StringBuilder();
                stringBuilder4.append(str2);
                stringBuilder4.append(KEY_DEX_CRC);
                stringBuilder4.append(i2);
                long j = multiDexPreferences.getLong(stringBuilder4.toString(), -1);
                stringBuilder4 = new StringBuilder();
                stringBuilder4.append(str2);
                stringBuilder4.append(KEY_DEX_TIME);
                stringBuilder4.append(i2);
                long j2 = multiDexPreferences.getLong(stringBuilder4.toString(), -1);
                long lastModified = extractedDex.lastModified();
                if (j2 == lastModified) {
                    String str3 = stringBuilder2;
                    SharedPreferences sharedPreferences = multiDexPreferences;
                    if (j == extractedDex.crc) {
                        arrayList.add(extractedDex);
                        i2++;
                        stringBuilder2 = str3;
                        multiDexPreferences = sharedPreferences;
                    }
                }
                StringBuilder stringBuilder5 = new StringBuilder();
                stringBuilder5.append("Invalid extracted dex: ");
                stringBuilder5.append(extractedDex);
                stringBuilder5.append(" (key \"");
                stringBuilder5.append(str2);
                stringBuilder5.append("\"), expected modification time: ");
                stringBuilder5.append(j2);
                stringBuilder5.append(", modification time: ");
                stringBuilder5.append(lastModified);
                stringBuilder5.append(", expected crc: ");
                stringBuilder5.append(j);
                stringBuilder5.append(", file crc: ");
                stringBuilder5.append(extractedDex.crc);
                throw new IOException(stringBuilder5.toString());
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Missing extracted secondary dex file '");
            stringBuilder.append(extractedDex.getPath());
            stringBuilder.append("'");
            throw new IOException(stringBuilder.toString());
        }
        return arrayList;
    }

    private static boolean isModified(Context context, File file, long j, String str) {
        context = getMultiDexPreferences(context);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(KEY_TIME_STAMP);
        if (context.getLong(stringBuilder.toString(), -1) == getTimeStamp(file)) {
            file = new StringBuilder();
            file.append(str);
            file.append(KEY_CRC);
            if (context.getLong(file.toString(), -1) == j) {
                return null;
            }
        }
        return true;
    }

    private static long getTimeStamp(File file) {
        long lastModified = file.lastModified();
        return lastModified == -1 ? lastModified - 1 : lastModified;
    }

    private static long getZipCrc(File file) throws IOException {
        long zipCrc = ZipUtil.getZipCrc(file);
        return zipCrc == -1 ? zipCrc - 1 : zipCrc;
    }

    private List<ExtractedDex> performExtractions() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.sourceApk.getName());
        stringBuilder.append(EXTRACTED_NAME_EXT);
        String stringBuilder2 = stringBuilder.toString();
        clearDexDir();
        List<ExtractedDex> arrayList = new ArrayList();
        ZipFile zipFile = new ZipFile(this.sourceApk);
        int i = 2;
        File extractedDex;
        Object obj;
        String str;
        StringBuilder stringBuilder3;
        try {
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append(DEX_PREFIX);
            stringBuilder4.append(2);
            stringBuilder4.append(DEX_SUFFIX);
            ZipEntry entry = zipFile.getEntry(stringBuilder4.toString());
            while (entry != null) {
                StringBuilder stringBuilder5 = new StringBuilder();
                stringBuilder5.append(stringBuilder2);
                stringBuilder5.append(i);
                stringBuilder5.append(EXTRACTED_SUFFIX);
                extractedDex = new ExtractedDex(this.dexDir, stringBuilder5.toString());
                arrayList.add(extractedDex);
                String str2 = TAG;
                StringBuilder stringBuilder6 = new StringBuilder();
                stringBuilder6.append("Extraction is needed for file ");
                stringBuilder6.append(extractedDex);
                Log.i(str2, stringBuilder6.toString());
                int i2 = 0;
                obj = null;
                while (i2 < 3 && obj == null) {
                    i2++;
                    extract(zipFile, entry, extractedDex, stringBuilder2);
                    extractedDex.crc = getZipCrc(extractedDex);
                    obj = 1;
                    str = TAG;
                    stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Extraction ");
                    stringBuilder3.append(obj != null ? "succeeded" : "failed");
                    stringBuilder3.append(" '");
                    stringBuilder3.append(extractedDex.getAbsolutePath());
                    stringBuilder3.append("': length ");
                    stringBuilder3.append(extractedDex.length());
                    stringBuilder3.append(" - crc: ");
                    stringBuilder3.append(extractedDex.crc);
                    Log.i(str, stringBuilder3.toString());
                    if (obj == null) {
                        extractedDex.delete();
                        if (extractedDex.exists()) {
                            str = TAG;
                            stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("Failed to delete corrupted secondary dex '");
                            stringBuilder3.append(extractedDex.getPath());
                            stringBuilder3.append("'");
                            Log.w(str, stringBuilder3.toString());
                        }
                    }
                }
                if (obj == null) {
                    StringBuilder stringBuilder7 = new StringBuilder();
                    stringBuilder7.append("Could not create zip file ");
                    stringBuilder7.append(extractedDex.getAbsolutePath());
                    stringBuilder7.append(" for secondary dex (");
                    stringBuilder7.append(i);
                    stringBuilder7.append(")");
                    throw new IOException(stringBuilder7.toString());
                }
                i++;
                stringBuilder4 = new StringBuilder();
                stringBuilder4.append(DEX_PREFIX);
                stringBuilder4.append(i);
                stringBuilder4.append(DEX_SUFFIX);
                entry = zipFile.getEntry(stringBuilder4.toString());
            }
            try {
                zipFile.close();
            } catch (Throwable e) {
                Log.w(TAG, "Failed to close resource", e);
            }
            return arrayList;
        } catch (Throwable e2) {
            str = TAG;
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Failed to read crc from ");
            stringBuilder3.append(extractedDex.getAbsolutePath());
            Log.w(str, stringBuilder3.toString(), e2);
            obj = null;
        } catch (Throwable th) {
            try {
                zipFile.close();
            } catch (Throwable e3) {
                Log.w(TAG, "Failed to close resource", e3);
            }
        }
    }

    private static void putStoredApkInfo(Context context, String str, long j, long j2, List<ExtractedDex> list) {
        context = getMultiDexPreferences(context).edit();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(KEY_TIME_STAMP);
        context.putLong(stringBuilder.toString(), j);
        j = new StringBuilder();
        j.append(str);
        j.append(KEY_CRC);
        context.putLong(j.toString(), j2);
        j = new StringBuilder();
        j.append(str);
        j.append(KEY_DEX_NUMBER);
        context.putInt(j.toString(), list.size() + 1);
        int i = 2;
        for (ExtractedDex extractedDex : list) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append(KEY_DEX_CRC);
            stringBuilder2.append(i);
            context.putLong(stringBuilder2.toString(), extractedDex.crc);
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append(KEY_DEX_TIME);
            stringBuilder2.append(i);
            context.putLong(stringBuilder2.toString(), extractedDex.lastModified());
            i++;
        }
        context.commit();
    }

    private static SharedPreferences getMultiDexPreferences(Context context) {
        return context.getSharedPreferences(PREFS_FILE, VERSION.SDK_INT < 11 ? 0 : 4);
    }

    private void clearDexDir() {
        File[] listFiles = this.dexDir.listFiles(new C00381());
        if (listFiles == null) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to list secondary dex dir content (");
            stringBuilder.append(this.dexDir.getPath());
            stringBuilder.append(").");
            Log.w(str, stringBuilder.toString());
            return;
        }
        for (File file : listFiles) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Trying to delete old file ");
            stringBuilder2.append(file.getPath());
            stringBuilder2.append(" of size ");
            stringBuilder2.append(file.length());
            Log.i(str2, stringBuilder2.toString());
            if (file.delete()) {
                str2 = TAG;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Deleted old file ");
                stringBuilder2.append(file.getPath());
                Log.i(str2, stringBuilder2.toString());
            } else {
                str2 = TAG;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Failed to delete old file ");
                stringBuilder2.append(file.getPath());
                Log.w(str2, stringBuilder2.toString());
            }
        }
    }

    private static void extract(ZipFile zipFile, ZipEntry zipEntry, File file, String str) throws IOException, FileNotFoundException {
        zipFile = zipFile.getInputStream(zipEntry);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("tmp-");
        stringBuilder.append(str);
        str = File.createTempFile(stringBuilder.toString(), EXTRACTED_SUFFIX, file.getParentFile());
        String str2 = TAG;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Extracting ");
        stringBuilder2.append(str.getPath());
        Log.i(str2, stringBuilder2.toString());
        ZipOutputStream zipOutputStream;
        try {
            zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(str)));
            ZipEntry zipEntry2 = new ZipEntry("classes.dex");
            zipEntry2.setTime(zipEntry.getTime());
            zipOutputStream.putNextEntry(zipEntry2);
            zipEntry = new byte[16384];
            for (int read = zipFile.read(zipEntry); read != -1; read = zipFile.read(zipEntry)) {
                zipOutputStream.write(zipEntry, 0, read);
            }
            zipOutputStream.closeEntry();
            zipOutputStream.close();
            if (str.setReadOnly() == null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to mark readonly \"");
                stringBuilder.append(str.getAbsolutePath());
                stringBuilder.append("\" (tmp of \"");
                stringBuilder.append(file.getAbsolutePath());
                stringBuilder.append("\")");
                throw new IOException(stringBuilder.toString());
            }
            zipEntry = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Renaming to ");
            stringBuilder.append(file.getPath());
            Log.i(zipEntry, stringBuilder.toString());
            if (str.renameTo(file) == null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to rename \"");
                stringBuilder.append(str.getAbsolutePath());
                stringBuilder.append("\" to \"");
                stringBuilder.append(file.getAbsolutePath());
                stringBuilder.append("\"");
                throw new IOException(stringBuilder.toString());
            }
            closeQuietly(zipFile);
            str.delete();
        } catch (Throwable th) {
            closeQuietly(zipFile);
            str.delete();
        }
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (Closeable closeable2) {
            Log.w(TAG, "Failed to close resource", closeable2);
        }
    }
}
