package okhttp3.internal.cache;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Flushable;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.io.FileSystem;
import okhttp3.internal.platform.Platform;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import org.apache.commons.lang3.StringUtils;

public final class DiskLruCache implements Closeable, Flushable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final long ANY_SEQUENCE_NUMBER = -1;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final Pattern LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,120}");
    static final String MAGIC = "libcore.io.DiskLruCache";
    private static final String READ = "READ";
    private static final String REMOVE = "REMOVE";
    static final String VERSION_1 = "1";
    private final int appVersion;
    private final Runnable cleanupRunnable = new DiskLruCache$1(this);
    boolean closed;
    final File directory;
    private final Executor executor;
    final FileSystem fileSystem;
    boolean hasJournalErrors;
    boolean initialized;
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    BufferedSink journalWriter;
    final LinkedHashMap<String, DiskLruCache$Entry> lruEntries = new LinkedHashMap(0, 0.75f, true);
    private long maxSize;
    boolean mostRecentRebuildFailed;
    boolean mostRecentTrimFailed;
    private long nextSequenceNumber = 0;
    int redundantOpCount;
    private long size = 0;
    final int valueCount;

    public final class Snapshot implements Closeable {
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;
        private final Source[] sources;

        Snapshot(String str, long j, Source[] sourceArr, long[] jArr) {
            this.key = str;
            this.sequenceNumber = j;
            this.sources = sourceArr;
            this.lengths = jArr;
        }

        public String key() {
            return this.key;
        }

        @Nullable
        public DiskLruCache$Editor edit() throws IOException {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }

        public Source getSource(int i) {
            return this.sources[i];
        }

        public long getLength(int i) {
            return this.lengths[i];
        }

        public void close() {
            for (Closeable closeQuietly : this.sources) {
                Util.closeQuietly(closeQuietly);
            }
        }
    }

    DiskLruCache(FileSystem fileSystem, File file, int i, int i2, long j, Executor executor) {
        this.fileSystem = fileSystem;
        this.directory = file;
        this.appVersion = i;
        this.journalFile = new File(file, JOURNAL_FILE);
        this.journalFileTmp = new File(file, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(file, JOURNAL_FILE_BACKUP);
        this.valueCount = i2;
        this.maxSize = j;
        this.executor = executor;
    }

    public synchronized void initialize() throws IOException {
        if (!this.initialized) {
            if (this.fileSystem.exists(this.journalFileBackup)) {
                if (this.fileSystem.exists(this.journalFile)) {
                    this.fileSystem.delete(this.journalFileBackup);
                } else {
                    this.fileSystem.rename(this.journalFileBackup, this.journalFile);
                }
            }
            if (this.fileSystem.exists(this.journalFile)) {
                try {
                    readJournal();
                    processJournal();
                    this.initialized = true;
                    return;
                } catch (Throwable e) {
                    Platform platform = Platform.get();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("DiskLruCache ");
                    stringBuilder.append(this.directory);
                    stringBuilder.append(" is corrupt: ");
                    stringBuilder.append(e.getMessage());
                    stringBuilder.append(", removing");
                    platform.log(5, stringBuilder.toString(), e);
                    delete();
                } finally {
                    this.closed = false;
                }
            }
            rebuildJournal();
            this.initialized = true;
        }
    }

    public static DiskLruCache create(FileSystem fileSystem, File file, int i, int i2, long j) {
        if (j <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        } else if (i2 <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        } else {
            return new DiskLruCache(fileSystem, file, i, i2, j, new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.threadFactory("OkHttp DiskLruCache", true)));
        }
    }

    private void readJournal() throws java.io.IOException {
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
        r8 = this;
        r0 = r8.fileSystem;
        r1 = r8.journalFile;
        r0 = r0.source(r1);
        r0 = okio.Okio.buffer(r0);
        r1 = r0.readUtf8LineStrict();	 Catch:{ all -> 0x00ad }
        r2 = r0.readUtf8LineStrict();	 Catch:{ all -> 0x00ad }
        r3 = r0.readUtf8LineStrict();	 Catch:{ all -> 0x00ad }
        r4 = r0.readUtf8LineStrict();	 Catch:{ all -> 0x00ad }
        r5 = r0.readUtf8LineStrict();	 Catch:{ all -> 0x00ad }
        r6 = "libcore.io.DiskLruCache";	 Catch:{ all -> 0x00ad }
        r6 = r6.equals(r1);	 Catch:{ all -> 0x00ad }
        if (r6 == 0) goto L_0x0079;	 Catch:{ all -> 0x00ad }
    L_0x0028:
        r6 = "1";	 Catch:{ all -> 0x00ad }
        r6 = r6.equals(r2);	 Catch:{ all -> 0x00ad }
        if (r6 == 0) goto L_0x0079;	 Catch:{ all -> 0x00ad }
    L_0x0030:
        r6 = r8.appVersion;	 Catch:{ all -> 0x00ad }
        r6 = java.lang.Integer.toString(r6);	 Catch:{ all -> 0x00ad }
        r3 = r6.equals(r3);	 Catch:{ all -> 0x00ad }
        if (r3 == 0) goto L_0x0079;	 Catch:{ all -> 0x00ad }
    L_0x003c:
        r3 = r8.valueCount;	 Catch:{ all -> 0x00ad }
        r3 = java.lang.Integer.toString(r3);	 Catch:{ all -> 0x00ad }
        r3 = r3.equals(r4);	 Catch:{ all -> 0x00ad }
        if (r3 == 0) goto L_0x0079;	 Catch:{ all -> 0x00ad }
    L_0x0048:
        r3 = "";	 Catch:{ all -> 0x00ad }
        r3 = r3.equals(r5);	 Catch:{ all -> 0x00ad }
        if (r3 != 0) goto L_0x0051;
    L_0x0050:
        goto L_0x0079;
    L_0x0051:
        r1 = 0;
    L_0x0052:
        r2 = r0.readUtf8LineStrict();	 Catch:{ EOFException -> 0x005c }
        r8.readJournalLine(r2);	 Catch:{ EOFException -> 0x005c }
        r1 = r1 + 1;
        goto L_0x0052;
    L_0x005c:
        r2 = r8.lruEntries;	 Catch:{ all -> 0x00ad }
        r2 = r2.size();	 Catch:{ all -> 0x00ad }
        r1 = r1 - r2;	 Catch:{ all -> 0x00ad }
        r8.redundantOpCount = r1;	 Catch:{ all -> 0x00ad }
        r1 = r0.exhausted();	 Catch:{ all -> 0x00ad }
        if (r1 != 0) goto L_0x006f;	 Catch:{ all -> 0x00ad }
    L_0x006b:
        r8.rebuildJournal();	 Catch:{ all -> 0x00ad }
        goto L_0x0075;	 Catch:{ all -> 0x00ad }
    L_0x006f:
        r1 = r8.newJournalWriter();	 Catch:{ all -> 0x00ad }
        r8.journalWriter = r1;	 Catch:{ all -> 0x00ad }
    L_0x0075:
        okhttp3.internal.Util.closeQuietly(r0);
        return;
    L_0x0079:
        r3 = new java.io.IOException;	 Catch:{ all -> 0x00ad }
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ad }
        r6.<init>();	 Catch:{ all -> 0x00ad }
        r7 = "unexpected journal header: [";	 Catch:{ all -> 0x00ad }
        r6.append(r7);	 Catch:{ all -> 0x00ad }
        r6.append(r1);	 Catch:{ all -> 0x00ad }
        r1 = ", ";	 Catch:{ all -> 0x00ad }
        r6.append(r1);	 Catch:{ all -> 0x00ad }
        r6.append(r2);	 Catch:{ all -> 0x00ad }
        r1 = ", ";	 Catch:{ all -> 0x00ad }
        r6.append(r1);	 Catch:{ all -> 0x00ad }
        r6.append(r4);	 Catch:{ all -> 0x00ad }
        r1 = ", ";	 Catch:{ all -> 0x00ad }
        r6.append(r1);	 Catch:{ all -> 0x00ad }
        r6.append(r5);	 Catch:{ all -> 0x00ad }
        r1 = "]";	 Catch:{ all -> 0x00ad }
        r6.append(r1);	 Catch:{ all -> 0x00ad }
        r1 = r6.toString();	 Catch:{ all -> 0x00ad }
        r3.<init>(r1);	 Catch:{ all -> 0x00ad }
        throw r3;	 Catch:{ all -> 0x00ad }
    L_0x00ad:
        r1 = move-exception;
        okhttp3.internal.Util.closeQuietly(r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache.readJournal():void");
    }

    private BufferedSink newJournalWriter() throws FileNotFoundException {
        return Okio.buffer(new DiskLruCache$2(this, this.fileSystem.appendingSink(this.journalFile)));
    }

    private void readJournalLine(String str) throws IOException {
        int indexOf = str.indexOf(32);
        if (indexOf == -1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unexpected journal line: ");
            stringBuilder.append(str);
            throw new IOException(stringBuilder.toString());
        }
        String substring;
        int i = indexOf + 1;
        int indexOf2 = str.indexOf(32, i);
        if (indexOf2 == -1) {
            substring = str.substring(i);
            if (indexOf == REMOVE.length() && str.startsWith(REMOVE)) {
                this.lruEntries.remove(substring);
                return;
            }
        }
        substring = str.substring(i, indexOf2);
        DiskLruCache$Entry diskLruCache$Entry = (DiskLruCache$Entry) this.lruEntries.get(substring);
        if (diskLruCache$Entry == null) {
            diskLruCache$Entry = new DiskLruCache$Entry(this, substring);
            this.lruEntries.put(substring, diskLruCache$Entry);
        }
        if (indexOf2 != -1 && indexOf == CLEAN.length() && str.startsWith(CLEAN)) {
            str = str.substring(indexOf2 + 1).split(StringUtils.SPACE);
            diskLruCache$Entry.readable = true;
            diskLruCache$Entry.currentEditor = null;
            diskLruCache$Entry.setLengths(str);
        } else if (indexOf2 == -1 && indexOf == DIRTY.length() && str.startsWith(DIRTY)) {
            diskLruCache$Entry.currentEditor = new DiskLruCache$Editor(this, diskLruCache$Entry);
        } else if (!(indexOf2 == -1 && indexOf == READ.length() && str.startsWith(READ))) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("unexpected journal line: ");
            stringBuilder.append(str);
            throw new IOException(stringBuilder.toString());
        }
    }

    private void processJournal() throws IOException {
        this.fileSystem.delete(this.journalFileTmp);
        Iterator it = this.lruEntries.values().iterator();
        while (it.hasNext()) {
            DiskLruCache$Entry diskLruCache$Entry = (DiskLruCache$Entry) it.next();
            int i = 0;
            if (diskLruCache$Entry.currentEditor == null) {
                while (i < this.valueCount) {
                    this.size += diskLruCache$Entry.lengths[i];
                    i++;
                }
            } else {
                diskLruCache$Entry.currentEditor = null;
                while (i < this.valueCount) {
                    this.fileSystem.delete(diskLruCache$Entry.cleanFiles[i]);
                    this.fileSystem.delete(diskLruCache$Entry.dirtyFiles[i]);
                    i++;
                }
                it.remove();
            }
        }
    }

    synchronized void rebuildJournal() throws IOException {
        if (this.journalWriter != null) {
            this.journalWriter.close();
        }
        BufferedSink buffer = Okio.buffer(this.fileSystem.sink(this.journalFileTmp));
        try {
            buffer.writeUtf8(MAGIC).writeByte(10);
            buffer.writeUtf8(VERSION_1).writeByte(10);
            buffer.writeDecimalLong((long) this.appVersion).writeByte(10);
            buffer.writeDecimalLong((long) this.valueCount).writeByte(10);
            buffer.writeByte(10);
            for (DiskLruCache$Entry diskLruCache$Entry : this.lruEntries.values()) {
                if (diskLruCache$Entry.currentEditor != null) {
                    buffer.writeUtf8(DIRTY).writeByte(32);
                    buffer.writeUtf8(diskLruCache$Entry.key);
                    buffer.writeByte(10);
                } else {
                    buffer.writeUtf8(CLEAN).writeByte(32);
                    buffer.writeUtf8(diskLruCache$Entry.key);
                    diskLruCache$Entry.writeLengths(buffer);
                    buffer.writeByte(10);
                }
            }
            if (this.fileSystem.exists(this.journalFile)) {
                this.fileSystem.rename(this.journalFile, this.journalFileBackup);
            }
            this.fileSystem.rename(this.journalFileTmp, this.journalFile);
            this.fileSystem.delete(this.journalFileBackup);
            this.journalWriter = newJournalWriter();
            this.hasJournalErrors = false;
            this.mostRecentRebuildFailed = false;
        } finally {
            buffer.close();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized okhttp3.internal.cache.DiskLruCache.Snapshot get(java.lang.String r4) throws java.io.IOException {
        /*
        r3 = this;
        monitor-enter(r3);
        r3.initialize();	 Catch:{ all -> 0x0050 }
        r3.checkNotClosed();	 Catch:{ all -> 0x0050 }
        r3.validateKey(r4);	 Catch:{ all -> 0x0050 }
        r0 = r3.lruEntries;	 Catch:{ all -> 0x0050 }
        r0 = r0.get(r4);	 Catch:{ all -> 0x0050 }
        r0 = (okhttp3.internal.cache.DiskLruCache$Entry) r0;	 Catch:{ all -> 0x0050 }
        r1 = 0;
        if (r0 == 0) goto L_0x004e;
    L_0x0015:
        r2 = r0.readable;	 Catch:{ all -> 0x0050 }
        if (r2 != 0) goto L_0x001a;
    L_0x0019:
        goto L_0x004e;
    L_0x001a:
        r0 = r0.snapshot();	 Catch:{ all -> 0x0050 }
        if (r0 != 0) goto L_0x0022;
    L_0x0020:
        monitor-exit(r3);
        return r1;
    L_0x0022:
        r1 = r3.redundantOpCount;	 Catch:{ all -> 0x0050 }
        r1 = r1 + 1;
        r3.redundantOpCount = r1;	 Catch:{ all -> 0x0050 }
        r1 = r3.journalWriter;	 Catch:{ all -> 0x0050 }
        r2 = "READ";
        r1 = r1.writeUtf8(r2);	 Catch:{ all -> 0x0050 }
        r2 = 32;
        r1 = r1.writeByte(r2);	 Catch:{ all -> 0x0050 }
        r4 = r1.writeUtf8(r4);	 Catch:{ all -> 0x0050 }
        r1 = 10;
        r4.writeByte(r1);	 Catch:{ all -> 0x0050 }
        r4 = r3.journalRebuildRequired();	 Catch:{ all -> 0x0050 }
        if (r4 == 0) goto L_0x004c;
    L_0x0045:
        r4 = r3.executor;	 Catch:{ all -> 0x0050 }
        r1 = r3.cleanupRunnable;	 Catch:{ all -> 0x0050 }
        r4.execute(r1);	 Catch:{ all -> 0x0050 }
    L_0x004c:
        monitor-exit(r3);
        return r0;
    L_0x004e:
        monitor-exit(r3);
        return r1;
    L_0x0050:
        r4 = move-exception;
        monitor-exit(r3);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache.get(java.lang.String):okhttp3.internal.cache.DiskLruCache$Snapshot");
    }

    @Nullable
    public DiskLruCache$Editor edit(String str) throws IOException {
        return edit(str, -1);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    synchronized okhttp3.internal.cache.DiskLruCache$Editor edit(java.lang.String r6, long r7) throws java.io.IOException {
        /*
        r5 = this;
        monitor-enter(r5);
        r5.initialize();	 Catch:{ all -> 0x0074 }
        r5.checkNotClosed();	 Catch:{ all -> 0x0074 }
        r5.validateKey(r6);	 Catch:{ all -> 0x0074 }
        r0 = r5.lruEntries;	 Catch:{ all -> 0x0074 }
        r0 = r0.get(r6);	 Catch:{ all -> 0x0074 }
        r0 = (okhttp3.internal.cache.DiskLruCache$Entry) r0;	 Catch:{ all -> 0x0074 }
        r1 = -1;
        r3 = (r7 > r1 ? 1 : (r7 == r1 ? 0 : -1));
        r1 = 0;
        if (r3 == 0) goto L_0x0023;
    L_0x0019:
        if (r0 == 0) goto L_0x0021;
    L_0x001b:
        r2 = r0.sequenceNumber;	 Catch:{ all -> 0x0074 }
        r4 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1));
        if (r4 == 0) goto L_0x0023;
    L_0x0021:
        monitor-exit(r5);
        return r1;
    L_0x0023:
        if (r0 == 0) goto L_0x002b;
    L_0x0025:
        r7 = r0.currentEditor;	 Catch:{ all -> 0x0074 }
        if (r7 == 0) goto L_0x002b;
    L_0x0029:
        monitor-exit(r5);
        return r1;
    L_0x002b:
        r7 = r5.mostRecentTrimFailed;	 Catch:{ all -> 0x0074 }
        if (r7 != 0) goto L_0x006b;
    L_0x002f:
        r7 = r5.mostRecentRebuildFailed;	 Catch:{ all -> 0x0074 }
        if (r7 == 0) goto L_0x0034;
    L_0x0033:
        goto L_0x006b;
    L_0x0034:
        r7 = r5.journalWriter;	 Catch:{ all -> 0x0074 }
        r8 = "DIRTY";
        r7 = r7.writeUtf8(r8);	 Catch:{ all -> 0x0074 }
        r8 = 32;
        r7 = r7.writeByte(r8);	 Catch:{ all -> 0x0074 }
        r7 = r7.writeUtf8(r6);	 Catch:{ all -> 0x0074 }
        r8 = 10;
        r7.writeByte(r8);	 Catch:{ all -> 0x0074 }
        r7 = r5.journalWriter;	 Catch:{ all -> 0x0074 }
        r7.flush();	 Catch:{ all -> 0x0074 }
        r7 = r5.hasJournalErrors;	 Catch:{ all -> 0x0074 }
        if (r7 == 0) goto L_0x0056;
    L_0x0054:
        monitor-exit(r5);
        return r1;
    L_0x0056:
        if (r0 != 0) goto L_0x0062;
    L_0x0058:
        r0 = new okhttp3.internal.cache.DiskLruCache$Entry;	 Catch:{ all -> 0x0074 }
        r0.<init>(r5, r6);	 Catch:{ all -> 0x0074 }
        r7 = r5.lruEntries;	 Catch:{ all -> 0x0074 }
        r7.put(r6, r0);	 Catch:{ all -> 0x0074 }
    L_0x0062:
        r6 = new okhttp3.internal.cache.DiskLruCache$Editor;	 Catch:{ all -> 0x0074 }
        r6.<init>(r5, r0);	 Catch:{ all -> 0x0074 }
        r0.currentEditor = r6;	 Catch:{ all -> 0x0074 }
        monitor-exit(r5);
        return r6;
    L_0x006b:
        r6 = r5.executor;	 Catch:{ all -> 0x0074 }
        r7 = r5.cleanupRunnable;	 Catch:{ all -> 0x0074 }
        r6.execute(r7);	 Catch:{ all -> 0x0074 }
        monitor-exit(r5);
        return r1;
    L_0x0074:
        r6 = move-exception;
        monitor-exit(r5);
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache.edit(java.lang.String, long):okhttp3.internal.cache.DiskLruCache$Editor");
    }

    public File getDirectory() {
        return this.directory;
    }

    public synchronized long getMaxSize() {
        return this.maxSize;
    }

    public synchronized void setMaxSize(long j) {
        this.maxSize = j;
        if (this.initialized != null) {
            this.executor.execute(this.cleanupRunnable);
        }
    }

    public synchronized long size() throws IOException {
        initialize();
        return this.size;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    synchronized void completeEdit(okhttp3.internal.cache.DiskLruCache$Editor r12, boolean r13) throws java.io.IOException {
        /*
        r11 = this;
        monitor-enter(r11);
        r0 = r12.entry;	 Catch:{ all -> 0x00ff }
        r1 = r0.currentEditor;	 Catch:{ all -> 0x00ff }
        if (r1 == r12) goto L_0x000d;
    L_0x0007:
        r12 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x00ff }
        r12.<init>();	 Catch:{ all -> 0x00ff }
        throw r12;	 Catch:{ all -> 0x00ff }
    L_0x000d:
        r1 = 0;
        if (r13 == 0) goto L_0x004d;
    L_0x0010:
        r2 = r0.readable;	 Catch:{ all -> 0x00ff }
        if (r2 != 0) goto L_0x004d;
    L_0x0014:
        r2 = 0;
    L_0x0015:
        r3 = r11.valueCount;	 Catch:{ all -> 0x00ff }
        if (r2 >= r3) goto L_0x004d;
    L_0x0019:
        r3 = r12.written;	 Catch:{ all -> 0x00ff }
        r3 = r3[r2];	 Catch:{ all -> 0x00ff }
        if (r3 != 0) goto L_0x0039;
    L_0x001f:
        r12.abort();	 Catch:{ all -> 0x00ff }
        r12 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x00ff }
        r13 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ff }
        r13.<init>();	 Catch:{ all -> 0x00ff }
        r0 = "Newly created entry didn't create value for index ";
        r13.append(r0);	 Catch:{ all -> 0x00ff }
        r13.append(r2);	 Catch:{ all -> 0x00ff }
        r13 = r13.toString();	 Catch:{ all -> 0x00ff }
        r12.<init>(r13);	 Catch:{ all -> 0x00ff }
        throw r12;	 Catch:{ all -> 0x00ff }
    L_0x0039:
        r3 = r11.fileSystem;	 Catch:{ all -> 0x00ff }
        r4 = r0.dirtyFiles;	 Catch:{ all -> 0x00ff }
        r4 = r4[r2];	 Catch:{ all -> 0x00ff }
        r3 = r3.exists(r4);	 Catch:{ all -> 0x00ff }
        if (r3 != 0) goto L_0x004a;
    L_0x0045:
        r12.abort();	 Catch:{ all -> 0x00ff }
        monitor-exit(r11);
        return;
    L_0x004a:
        r2 = r2 + 1;
        goto L_0x0015;
    L_0x004d:
        r12 = r11.valueCount;	 Catch:{ all -> 0x00ff }
        if (r1 >= r12) goto L_0x0088;
    L_0x0051:
        r12 = r0.dirtyFiles;	 Catch:{ all -> 0x00ff }
        r12 = r12[r1];	 Catch:{ all -> 0x00ff }
        if (r13 == 0) goto L_0x0080;
    L_0x0057:
        r2 = r11.fileSystem;	 Catch:{ all -> 0x00ff }
        r2 = r2.exists(r12);	 Catch:{ all -> 0x00ff }
        if (r2 == 0) goto L_0x0085;
    L_0x005f:
        r2 = r0.cleanFiles;	 Catch:{ all -> 0x00ff }
        r2 = r2[r1];	 Catch:{ all -> 0x00ff }
        r3 = r11.fileSystem;	 Catch:{ all -> 0x00ff }
        r3.rename(r12, r2);	 Catch:{ all -> 0x00ff }
        r12 = r0.lengths;	 Catch:{ all -> 0x00ff }
        r3 = r12[r1];	 Catch:{ all -> 0x00ff }
        r12 = r11.fileSystem;	 Catch:{ all -> 0x00ff }
        r5 = r12.size(r2);	 Catch:{ all -> 0x00ff }
        r12 = r0.lengths;	 Catch:{ all -> 0x00ff }
        r12[r1] = r5;	 Catch:{ all -> 0x00ff }
        r7 = r11.size;	 Catch:{ all -> 0x00ff }
        r12 = 0;
        r9 = r7 - r3;
        r2 = r9 + r5;
        r11.size = r2;	 Catch:{ all -> 0x00ff }
        goto L_0x0085;
    L_0x0080:
        r2 = r11.fileSystem;	 Catch:{ all -> 0x00ff }
        r2.delete(r12);	 Catch:{ all -> 0x00ff }
    L_0x0085:
        r1 = r1 + 1;
        goto L_0x004d;
    L_0x0088:
        r12 = r11.redundantOpCount;	 Catch:{ all -> 0x00ff }
        r1 = 1;
        r12 = r12 + r1;
        r11.redundantOpCount = r12;	 Catch:{ all -> 0x00ff }
        r12 = 0;
        r0.currentEditor = r12;	 Catch:{ all -> 0x00ff }
        r12 = r0.readable;	 Catch:{ all -> 0x00ff }
        r12 = r12 | r13;
        r2 = 10;
        r3 = 32;
        if (r12 == 0) goto L_0x00c5;
    L_0x009a:
        r0.readable = r1;	 Catch:{ all -> 0x00ff }
        r12 = r11.journalWriter;	 Catch:{ all -> 0x00ff }
        r1 = "CLEAN";
        r12 = r12.writeUtf8(r1);	 Catch:{ all -> 0x00ff }
        r12.writeByte(r3);	 Catch:{ all -> 0x00ff }
        r12 = r11.journalWriter;	 Catch:{ all -> 0x00ff }
        r1 = r0.key;	 Catch:{ all -> 0x00ff }
        r12.writeUtf8(r1);	 Catch:{ all -> 0x00ff }
        r12 = r11.journalWriter;	 Catch:{ all -> 0x00ff }
        r0.writeLengths(r12);	 Catch:{ all -> 0x00ff }
        r12 = r11.journalWriter;	 Catch:{ all -> 0x00ff }
        r12.writeByte(r2);	 Catch:{ all -> 0x00ff }
        if (r13 == 0) goto L_0x00e3;
    L_0x00ba:
        r12 = r11.nextSequenceNumber;	 Catch:{ all -> 0x00ff }
        r1 = 1;
        r3 = r12 + r1;
        r11.nextSequenceNumber = r3;	 Catch:{ all -> 0x00ff }
        r0.sequenceNumber = r12;	 Catch:{ all -> 0x00ff }
        goto L_0x00e3;
    L_0x00c5:
        r12 = r11.lruEntries;	 Catch:{ all -> 0x00ff }
        r13 = r0.key;	 Catch:{ all -> 0x00ff }
        r12.remove(r13);	 Catch:{ all -> 0x00ff }
        r12 = r11.journalWriter;	 Catch:{ all -> 0x00ff }
        r13 = "REMOVE";
        r12 = r12.writeUtf8(r13);	 Catch:{ all -> 0x00ff }
        r12.writeByte(r3);	 Catch:{ all -> 0x00ff }
        r12 = r11.journalWriter;	 Catch:{ all -> 0x00ff }
        r13 = r0.key;	 Catch:{ all -> 0x00ff }
        r12.writeUtf8(r13);	 Catch:{ all -> 0x00ff }
        r12 = r11.journalWriter;	 Catch:{ all -> 0x00ff }
        r12.writeByte(r2);	 Catch:{ all -> 0x00ff }
    L_0x00e3:
        r12 = r11.journalWriter;	 Catch:{ all -> 0x00ff }
        r12.flush();	 Catch:{ all -> 0x00ff }
        r12 = r11.size;	 Catch:{ all -> 0x00ff }
        r0 = r11.maxSize;	 Catch:{ all -> 0x00ff }
        r2 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1));
        if (r2 > 0) goto L_0x00f6;
    L_0x00f0:
        r12 = r11.journalRebuildRequired();	 Catch:{ all -> 0x00ff }
        if (r12 == 0) goto L_0x00fd;
    L_0x00f6:
        r12 = r11.executor;	 Catch:{ all -> 0x00ff }
        r13 = r11.cleanupRunnable;	 Catch:{ all -> 0x00ff }
        r12.execute(r13);	 Catch:{ all -> 0x00ff }
    L_0x00fd:
        monitor-exit(r11);
        return;
    L_0x00ff:
        r12 = move-exception;
        monitor-exit(r11);
        throw r12;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache.completeEdit(okhttp3.internal.cache.DiskLruCache$Editor, boolean):void");
    }

    boolean journalRebuildRequired() {
        return this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean remove(java.lang.String r7) throws java.io.IOException {
        /*
        r6 = this;
        monitor-enter(r6);
        r6.initialize();	 Catch:{ all -> 0x0029 }
        r6.checkNotClosed();	 Catch:{ all -> 0x0029 }
        r6.validateKey(r7);	 Catch:{ all -> 0x0029 }
        r0 = r6.lruEntries;	 Catch:{ all -> 0x0029 }
        r7 = r0.get(r7);	 Catch:{ all -> 0x0029 }
        r7 = (okhttp3.internal.cache.DiskLruCache$Entry) r7;	 Catch:{ all -> 0x0029 }
        r0 = 0;
        if (r7 != 0) goto L_0x0017;
    L_0x0015:
        monitor-exit(r6);
        return r0;
    L_0x0017:
        r7 = r6.removeEntry(r7);	 Catch:{ all -> 0x0029 }
        if (r7 == 0) goto L_0x0027;
    L_0x001d:
        r1 = r6.size;	 Catch:{ all -> 0x0029 }
        r3 = r6.maxSize;	 Catch:{ all -> 0x0029 }
        r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        if (r5 > 0) goto L_0x0027;
    L_0x0025:
        r6.mostRecentTrimFailed = r0;	 Catch:{ all -> 0x0029 }
    L_0x0027:
        monitor-exit(r6);
        return r7;
    L_0x0029:
        r7 = move-exception;
        monitor-exit(r6);
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache.remove(java.lang.String):boolean");
    }

    boolean removeEntry(DiskLruCache$Entry diskLruCache$Entry) throws IOException {
        if (diskLruCache$Entry.currentEditor != null) {
            diskLruCache$Entry.currentEditor.detach();
        }
        for (int i = 0; i < this.valueCount; i++) {
            this.fileSystem.delete(diskLruCache$Entry.cleanFiles[i]);
            this.size -= diskLruCache$Entry.lengths[i];
            diskLruCache$Entry.lengths[i] = 0;
        }
        this.redundantOpCount++;
        this.journalWriter.writeUtf8(REMOVE).writeByte(32).writeUtf8(diskLruCache$Entry.key).writeByte(10);
        this.lruEntries.remove(diskLruCache$Entry.key);
        if (journalRebuildRequired() != null) {
            this.executor.execute(this.cleanupRunnable);
        }
        return true;
    }

    public synchronized boolean isClosed() {
        return this.closed;
    }

    private synchronized void checkNotClosed() {
        if (isClosed()) {
            throw new IllegalStateException("cache is closed");
        }
    }

    public synchronized void flush() throws IOException {
        if (this.initialized) {
            checkNotClosed();
            trimToSize();
            this.journalWriter.flush();
        }
    }

    public synchronized void close() throws IOException {
        if (this.initialized) {
            if (!this.closed) {
                for (DiskLruCache$Entry diskLruCache$Entry : (DiskLruCache$Entry[]) this.lruEntries.values().toArray(new DiskLruCache$Entry[this.lruEntries.size()])) {
                    if (diskLruCache$Entry.currentEditor != null) {
                        diskLruCache$Entry.currentEditor.abort();
                    }
                }
                trimToSize();
                this.journalWriter.close();
                this.journalWriter = null;
                this.closed = true;
                return;
            }
        }
        this.closed = true;
    }

    void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            removeEntry((DiskLruCache$Entry) this.lruEntries.values().iterator().next());
        }
        this.mostRecentTrimFailed = false;
    }

    public void delete() throws IOException {
        close();
        this.fileSystem.deleteContents(this.directory);
    }

    public synchronized void evictAll() throws IOException {
        initialize();
        for (DiskLruCache$Entry removeEntry : (DiskLruCache$Entry[]) this.lruEntries.values().toArray(new DiskLruCache$Entry[this.lruEntries.size()])) {
            removeEntry(removeEntry);
        }
        this.mostRecentTrimFailed = false;
    }

    private void validateKey(String str) {
        if (!LEGAL_KEY_PATTERN.matcher(str).matches()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("keys must match regex [a-z0-9_-]{1,120}: \"");
            stringBuilder.append(str);
            stringBuilder.append("\"");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public synchronized Iterator<Snapshot> snapshots() throws IOException {
        initialize();
        return new DiskLruCache$3(this);
    }
}
