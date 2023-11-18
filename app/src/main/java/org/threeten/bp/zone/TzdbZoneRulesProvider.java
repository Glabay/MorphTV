package org.threeten.bp.zone;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamCorruptedException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.threeten.bp.jdk8.Jdk8Methods;

public final class TzdbZoneRulesProvider extends ZoneRulesProvider {
    private Set<String> loadedUrls = new CopyOnWriteArraySet();
    private List<String> regionIds;
    private final ConcurrentNavigableMap<String, Version> versions = new ConcurrentSkipListMap();

    static class Version {
        private final String[] regionArray;
        private final AtomicReferenceArray<Object> ruleData;
        private final short[] ruleIndices;
        private final String versionId;

        Version(String str, String[] strArr, short[] sArr, AtomicReferenceArray<Object> atomicReferenceArray) {
            this.ruleData = atomicReferenceArray;
            this.versionId = str;
            this.regionArray = strArr;
            this.ruleIndices = sArr;
        }

        ZoneRules getRules(String str) {
            int binarySearch = Arrays.binarySearch(this.regionArray, str);
            if (binarySearch < 0) {
                return null;
            }
            try {
                return createRule(this.ruleIndices[binarySearch]);
            } catch (Throwable e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid binary time-zone data: TZDB:");
                stringBuilder.append(str);
                stringBuilder.append(", version: ");
                stringBuilder.append(this.versionId);
                throw new ZoneRulesException(stringBuilder.toString(), e);
            }
        }

        ZoneRules createRule(short s) throws Exception {
            Object obj = this.ruleData.get(s);
            if (obj instanceof byte[]) {
                obj = Ser.read(new DataInputStream(new ByteArrayInputStream((byte[]) obj)));
                this.ruleData.set(s, obj);
            }
            return (ZoneRules) obj;
        }

        public String toString() {
            return this.versionId;
        }
    }

    public String toString() {
        return "TZDB";
    }

    public TzdbZoneRulesProvider() {
        if (!load(ZoneRulesProvider.class.getClassLoader())) {
            throw new ZoneRulesException("No time-zone rules found for 'TZDB'");
        }
    }

    public TzdbZoneRulesProvider(URL url) {
        try {
            if (!load(url)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("No time-zone rules found: ");
                stringBuilder.append(url);
                throw new ZoneRulesException(stringBuilder.toString());
            }
        } catch (Throwable e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unable to load TZDB time-zone rules: ");
            stringBuilder2.append(url);
            throw new ZoneRulesException(stringBuilder2.toString(), e);
        }
    }

    public TzdbZoneRulesProvider(InputStream inputStream) {
        try {
            load(inputStream);
        } catch (InputStream inputStream2) {
            throw new ZoneRulesException("Unable to load TZDB time-zone rules", inputStream2);
        }
    }

    protected Set<String> provideZoneIds() {
        return new HashSet(this.regionIds);
    }

    protected ZoneRules provideRules(String str, boolean z) {
        Jdk8Methods.requireNonNull(str, "zoneId");
        z = ((Version) this.versions.lastEntry().getValue()).getRules(str);
        if (z) {
            return z;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown time-zone ID: ");
        stringBuilder.append(str);
        throw new ZoneRulesException(stringBuilder.toString());
    }

    protected NavigableMap<String, ZoneRules> provideVersions(String str) {
        NavigableMap treeMap = new TreeMap();
        for (Version version : this.versions.values()) {
            ZoneRules rules = version.getRules(str);
            if (rules != null) {
                treeMap.put(version.versionId, rules);
            }
        }
        return treeMap;
    }

    private boolean load(ClassLoader classLoader) {
        Object obj = null;
        try {
            classLoader = classLoader.getResources("org/threeten/bp/TZDB.dat");
            boolean z = false;
            while (classLoader.hasMoreElements()) {
                URL url = (URL) classLoader.nextElement();
                try {
                    z |= load(url);
                    URL url2 = url;
                } catch (Exception e) {
                    classLoader = e;
                    obj = url;
                }
            }
            return z;
        } catch (Exception e2) {
            classLoader = e2;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to load TZDB time-zone rules: ");
            stringBuilder.append(obj);
            throw new ZoneRulesException(stringBuilder.toString(), classLoader);
        }
    }

    private boolean load(URL url) throws ClassNotFoundException, IOException, ZoneRulesException {
        boolean z = false;
        if (this.loadedUrls.add(url.toExternalForm())) {
            InputStream inputStream = null;
            try {
                InputStream openStream = url.openStream();
                try {
                    z = false | load(openStream);
                    if (openStream != null) {
                        openStream.close();
                    }
                } catch (Throwable th) {
                    Throwable th2 = th;
                    inputStream = openStream;
                    url = th2;
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    throw url;
                }
            } catch (Throwable th3) {
                url = th3;
                if (inputStream != null) {
                    inputStream.close();
                }
                throw url;
            }
        }
        return z;
    }

    private boolean load(InputStream inputStream) throws IOException, StreamCorruptedException {
        boolean z = false;
        for (Version version : loadData(inputStream)) {
            Version version2 = (Version) this.versions.putIfAbsent(version.versionId, version);
            if (version2 == null || version2.versionId.equals(version.versionId)) {
                z = true;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Data already loaded for TZDB time-zone rules version: ");
                stringBuilder.append(version.versionId);
                throw new ZoneRulesException(stringBuilder.toString());
            }
        }
        return z;
    }

    private Iterable<Version> loadData(InputStream inputStream) throws IOException, StreamCorruptedException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        if (dataInputStream.readByte() != 1) {
            throw new StreamCorruptedException("File format not recognised");
        }
        if ("TZDB".equals(dataInputStream.readUTF()) == null) {
            throw new StreamCorruptedException("File format not recognised");
        }
        inputStream = dataInputStream.readShort();
        String[] strArr = new String[inputStream];
        for (int i = 0; i < inputStream; i++) {
            strArr[i] = dataInputStream.readUTF();
        }
        short readShort = dataInputStream.readShort();
        String[] strArr2 = new String[readShort];
        for (short s = (short) 0; s < readShort; s++) {
            strArr2[s] = dataInputStream.readUTF();
        }
        this.regionIds = Arrays.asList(strArr2);
        readShort = dataInputStream.readShort();
        Object[] objArr = new Object[readShort];
        for (short s2 = (short) 0; s2 < readShort; s2++) {
            byte[] bArr = new byte[dataInputStream.readShort()];
            dataInputStream.readFully(bArr);
            objArr[s2] = bArr;
        }
        AtomicReferenceArray atomicReferenceArray = new AtomicReferenceArray(objArr);
        Iterable hashSet = new HashSet(inputStream);
        for (int i2 = 0; i2 < inputStream; i2++) {
            short readShort2 = dataInputStream.readShort();
            String[] strArr3 = new String[readShort2];
            short[] sArr = new short[readShort2];
            for (short s3 = (short) 0; s3 < readShort2; s3++) {
                strArr3[s3] = strArr2[dataInputStream.readShort()];
                sArr[s3] = dataInputStream.readShort();
            }
            hashSet.add(new Version(strArr[i2], strArr3, sArr, atomicReferenceArray));
        }
        return hashSet;
    }
}
