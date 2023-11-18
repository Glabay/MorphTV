package org.threeten.bp.zone;

import com.google.android.gms.cast.CastStatusCodes;
import com.google.common.net.HttpHeaders;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Month;
import org.threeten.bp.Year;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.chrono.ChronoLocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalAdjusters;
import org.threeten.bp.zone.ZoneOffsetTransitionRule.TimeDefinition;

final class TzdbZoneRulesCompiler {
    private static final DateTimeFormatter TIME_PARSER = new DateTimeFormatterBuilder().appendValue(ChronoField.HOUR_OF_DAY).optionalStart().appendLiteral(':').appendValue(ChronoField.MINUTE_OF_HOUR, 2).optionalStart().appendLiteral(':').appendValue(ChronoField.SECOND_OF_MINUTE, 2).toFormatter();
    private final SortedMap<String, ZoneRules> builtZones = new TreeMap();
    private Map<Object, Object> deduplicateMap = new HashMap();
    private final SortedMap<LocalDate, Byte> leapSeconds = new TreeMap();
    private final File leapSecondsFile;
    private final Map<String, String> links = new HashMap();
    private final Map<String, List<TZDBRule>> rules = new HashMap();
    private final List<File> sourceFiles;
    private final boolean verbose;
    private final String version;
    private final Map<String, List<TZDBZone>> zones = new HashMap();

    static final class LeapSecondRule {
        final LocalDate leapDate;
        byte secondAdjustment;

        public LeapSecondRule(LocalDate localDate, byte b) {
            this.leapDate = localDate;
            this.secondAdjustment = b;
        }
    }

    abstract class TZDBMonthDayTime {
        boolean adjustForwards = true;
        int dayOfMonth = 1;
        DayOfWeek dayOfWeek;
        boolean endOfDay;
        Month month = Month.JANUARY;
        LocalTime time = LocalTime.MIDNIGHT;
        TimeDefinition timeDefinition = TimeDefinition.WALL;

        TZDBMonthDayTime() {
        }

        void adjustToFowards(int i) {
            if (!this.adjustForwards && this.dayOfMonth > 0) {
                i = LocalDate.of(i, this.month, this.dayOfMonth).minusDays(6);
                this.dayOfMonth = i.getDayOfMonth();
                this.month = i.getMonth();
                this.adjustForwards = true;
            }
        }
    }

    final class TZDBRule extends TZDBMonthDayTime {
        int endYear;
        int savingsAmount;
        int startYear;
        String text;

        TZDBRule() {
            super();
        }

        void addToBuilder(ZoneRulesBuilder zoneRulesBuilder) {
            adjustToFowards(CastStatusCodes.APPLICATION_NOT_FOUND);
            zoneRulesBuilder.addRuleToWindow(this.startYear, this.endYear, this.month, this.dayOfMonth, this.dayOfWeek, this.time, this.endOfDay, this.timeDefinition, this.savingsAmount);
        }
    }

    final class TZDBZone extends TZDBMonthDayTime {
        Integer fixedSavingsSecs;
        String savingsRule;
        ZoneOffset standardOffset;
        String text;
        Year year;

        TZDBZone() {
            super();
        }

        ZoneRulesBuilder addToBuilder(ZoneRulesBuilder zoneRulesBuilder, Map<String, List<TZDBRule>> map) {
            if (this.year != null) {
                zoneRulesBuilder.addWindow(this.standardOffset, toDateTime(this.year.getValue()), this.timeDefinition);
            } else {
                zoneRulesBuilder.addWindowForever(this.standardOffset);
            }
            if (this.fixedSavingsSecs != null) {
                zoneRulesBuilder.setFixedSavingsToWindow(this.fixedSavingsSecs.intValue());
            } else {
                List<TZDBRule> list = (List) map.get(this.savingsRule);
                if (list == null) {
                    map = new StringBuilder();
                    map.append("Rule not found: ");
                    map.append(this.savingsRule);
                    throw new IllegalArgumentException(map.toString());
                }
                for (TZDBRule addToBuilder : list) {
                    addToBuilder.addToBuilder(zoneRulesBuilder);
                }
            }
            return zoneRulesBuilder;
        }

        private LocalDateTime toDateTime(int i) {
            adjustToFowards(i);
            if (this.dayOfMonth == -1) {
                this.dayOfMonth = this.month.length(Year.isLeap((long) i));
                i = LocalDate.of(i, this.month, this.dayOfMonth);
                if (this.dayOfWeek != null) {
                    i = i.with(TemporalAdjusters.previousOrSame(this.dayOfWeek));
                }
            } else {
                i = LocalDate.of(i, this.month, this.dayOfMonth);
                if (this.dayOfWeek != null) {
                    i = i.with(TemporalAdjusters.nextOrSame(this.dayOfWeek));
                }
            }
            i = LocalDateTime.of((LocalDate) TzdbZoneRulesCompiler.this.deduplicate(i), this.time);
            return this.endOfDay ? i.plusDays(1) : i;
        }
    }

    public static void main(String[] strArr) {
        String[] strArr2 = strArr;
        if (strArr2.length < 2) {
            outputHelp();
            return;
        }
        File file = null;
        File file2 = file;
        String str = file2;
        int i = 0;
        boolean z = false;
        boolean z2 = false;
        while (i < strArr2.length) {
            String str2 = strArr2[i];
            if (!str2.startsWith("-")) {
                break;
            }
            if ("-srcdir".equals(str2)) {
                if (file == null) {
                    i++;
                    if (i < strArr2.length) {
                        file = new File(strArr2[i]);
                    }
                }
                outputHelp();
                return;
            } else if ("-dstdir".equals(str2)) {
                if (file2 == null) {
                    i++;
                    if (i < strArr2.length) {
                        file2 = new File(strArr2[i]);
                    }
                }
                outputHelp();
                return;
            } else if ("-version".equals(str2)) {
                if (str == null) {
                    i++;
                    if (i < strArr2.length) {
                        str = strArr2[i];
                    }
                }
                outputHelp();
                return;
            } else if (!"-unpacked".equals(str2)) {
                if ("-verbose".equals(str2)) {
                    if (!z2) {
                        z2 = true;
                    }
                } else if (!"-help".equals(str2)) {
                    PrintStream printStream = System.out;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unrecognised option: ");
                    stringBuilder.append(str2);
                    printStream.println(stringBuilder.toString());
                }
                outputHelp();
                return;
            } else if (z) {
                outputHelp();
                return;
            } else {
                z = true;
            }
            i++;
        }
        if (file == null) {
            printStream = System.out;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Source directory must be specified using -srcdir: ");
            stringBuilder.append(file);
            printStream.println(stringBuilder.toString());
        } else if (file.isDirectory()) {
            if (file2 == null) {
                file2 = file;
            }
            List asList = Arrays.asList(Arrays.copyOfRange(strArr2, i, strArr2.length));
            if (asList.isEmpty()) {
                System.out.println("Source filenames not specified, using default set");
                System.out.println("(africa antarctica asia australasia backward etcetera europe northamerica southamerica)");
                asList = Arrays.asList(new String[]{"africa", "antarctica", "asia", "australasia", "backward", "etcetera", "europe", "northamerica", "southamerica"});
            }
            List arrayList = new ArrayList();
            if (str != null) {
                File file3 = new File(file, str);
                if (file3.isDirectory()) {
                    arrayList.add(file3);
                } else {
                    printStream = System.out;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Version does not represent a valid source directory : ");
                    stringBuilder.append(file3);
                    printStream.println(stringBuilder.toString());
                    return;
                }
            }
            for (File file4 : file.listFiles()) {
                if (file4.isDirectory() && file4.getName().matches("[12][0-9][0-9][0-9][A-Za-z0-9._-]+")) {
                    arrayList.add(file4);
                }
            }
            if (arrayList.isEmpty()) {
                printStream = System.out;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Source directory contains no valid source folders: ");
                stringBuilder.append(file);
                printStream.println(stringBuilder.toString());
            } else if (!file2.exists() && !file2.mkdirs()) {
                printStream = System.out;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Destination directory could not be created: ");
                stringBuilder.append(file2);
                printStream.println(stringBuilder.toString());
            } else if (file2.isDirectory()) {
                process(arrayList, asList, file2, z, z2);
            } else {
                printStream = System.out;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Destination is not a directory: ");
                stringBuilder.append(file2);
                printStream.println(stringBuilder.toString());
            }
        } else {
            printStream = System.out;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Source does not exist or is not a directory: ");
            stringBuilder.append(file);
            printStream.println(stringBuilder.toString());
        }
    }

    private static void outputHelp() {
        System.out.println("Usage: TzdbZoneRulesCompiler <options> <tzdb source filenames>");
        System.out.println("where options include:");
        System.out.println("   -srcdir <directory>   Where to find source directories (required)");
        System.out.println("   -dstdir <directory>   Where to output generated files (default srcdir)");
        System.out.println("   -version <version>    Specify the version, such as 2009a (optional)");
        System.out.println("   -unpacked             Generate dat files without jar files");
        System.out.println("   -help                 Print this usage message");
        System.out.println("   -verbose              Output verbose information during compilation");
        System.out.println(" There must be one directory for each version in srcdir");
        System.out.println(" Each directory must have the name of the version, such as 2009a");
        System.out.println(" Each directory must contain the unpacked tzdb files, such as asia or europe");
        System.out.println(" Directories must match the regex [12][0-9][0-9][0-9][A-Za-z0-9._-]+");
        System.out.println(" There will be one jar file for each version and one combined jar in dstdir");
        System.out.println(" If the version is specified, only that version is processed");
    }

    private static void process(List<File> list, List<String> list2, File file, boolean z, boolean z2) {
        Map map;
        Iterator it;
        StringBuilder stringBuilder;
        Exception e;
        Exception exception;
        PrintStream printStream;
        File file2 = file;
        boolean z3 = z2;
        Map hashMap = new HashMap();
        Map treeMap = new TreeMap();
        Set treeSet = new TreeSet();
        Set hashSet = new HashSet();
        Iterator it2 = list.iterator();
        SortedMap sortedMap = null;
        while (it2.hasNext()) {
            File file3 = (File) it2.next();
            List arrayList = new ArrayList();
            for (String file4 : list2) {
                File file5 = new File(file3, file4);
                if (file5.exists()) {
                    arrayList.add(file5);
                }
            }
            if (!arrayList.isEmpty()) {
                File file6 = new File(file3, "leapseconds");
                if (!file6.exists()) {
                    PrintStream printStream2 = System.out;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Version ");
                    stringBuilder2.append(file3.getName());
                    stringBuilder2.append(" does not include leap seconds information.");
                    printStream2.println(stringBuilder2.toString());
                    file6 = null;
                }
                String name = file3.getName();
                TzdbZoneRulesCompiler tzdbZoneRulesCompiler = new TzdbZoneRulesCompiler(name, arrayList, file6, z3);
                tzdbZoneRulesCompiler.setDeduplicateMap(hashMap);
                try {
                    tzdbZoneRulesCompiler.compile();
                    SortedMap zones = tzdbZoneRulesCompiler.getZones();
                    SortedMap leapSeconds = tzdbZoneRulesCompiler.getLeapSeconds();
                    if (z) {
                        map = hashMap;
                        it = it2;
                    } else {
                        stringBuilder = new StringBuilder();
                        map = hashMap;
                        try {
                            stringBuilder.append("threeten-TZDB-");
                            stringBuilder.append(name);
                            stringBuilder.append(".jar");
                            file5 = new File(file2, stringBuilder.toString());
                            if (z3) {
                                PrintStream printStream3 = System.out;
                                stringBuilder = new StringBuilder();
                                it = it2;
                                try {
                                    stringBuilder.append("Outputting file: ");
                                    stringBuilder.append(file5);
                                    printStream3.println(stringBuilder.toString());
                                } catch (Exception e2) {
                                    e = e2;
                                    exception = e;
                                    printStream = System.out;
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("Failed: ");
                                    stringBuilder.append(exception.toString());
                                    printStream.println(stringBuilder.toString());
                                    exception.printStackTrace();
                                    System.exit(1);
                                    hashMap = map;
                                    it2 = it;
                                }
                            } else {
                                it = it2;
                            }
                            outputFile(file5, name, zones, leapSeconds);
                        } catch (Exception e3) {
                            e = e3;
                            it = it2;
                            exception = e;
                            printStream = System.out;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Failed: ");
                            stringBuilder.append(exception.toString());
                            printStream.println(stringBuilder.toString());
                            exception.printStackTrace();
                            System.exit(1);
                            hashMap = map;
                            it2 = it;
                        }
                    }
                    treeMap.put(name, zones);
                    treeSet.addAll(zones.keySet());
                    hashSet.addAll(zones.values());
                    if (tzdbZoneRulesCompiler.getMostRecentLeapSecond() != null && (sortedMap == null || tzdbZoneRulesCompiler.getMostRecentLeapSecond().compareTo((ChronoLocalDate) sortedMap.lastKey()) > 0)) {
                        sortedMap = leapSeconds;
                    }
                } catch (Exception e4) {
                    e = e4;
                    map = hashMap;
                    it = it2;
                    exception = e;
                    printStream = System.out;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed: ");
                    stringBuilder.append(exception.toString());
                    printStream.println(stringBuilder.toString());
                    exception.printStackTrace();
                    System.exit(1);
                    hashMap = map;
                    it2 = it;
                }
                hashMap = map;
                it2 = it;
            }
        }
        if (z) {
            if (z3) {
                PrintStream printStream4 = System.out;
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Outputting combined files: ");
                stringBuilder3.append(file2);
                printStream4.println(stringBuilder3.toString());
            }
            outputFilesDat(file2, treeMap, treeSet, hashSet, sortedMap);
            return;
        }
        File file7 = new File(file2, "threeten-TZDB-all.jar");
        if (z3) {
            PrintStream printStream5 = System.out;
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Outputting combined file: ");
            stringBuilder3.append(file7);
            printStream5.println(stringBuilder3.toString());
        }
        outputFile(file7, treeMap, treeSet, hashSet, sortedMap);
    }

    private static void outputFilesDat(File file, Map<String, SortedMap<String, ZoneRules>> map, Set<String> set, Set<ZoneRules> set2, SortedMap<LocalDate, Byte> sortedMap) {
        sortedMap = new File(file, "TZDB.dat");
        sortedMap.delete();
        file = null;
        try {
            OutputStream fileOutputStream = new FileOutputStream(sortedMap);
            try {
                outputTzdbDat(fileOutputStream, map, set, set2);
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (File file2) {
                        map = System.out;
                        set = new StringBuilder();
                        set.append("Failed: ");
                        set.append(file2.toString());
                        map.println(set.toString());
                        file2.printStackTrace();
                        System.exit(1);
                    }
                }
            } catch (File file22) {
                map = file22;
                file22 = fileOutputStream;
                if (file22 != null) {
                    file22.close();
                }
                throw map;
            }
        } catch (Throwable th) {
            map = th;
            if (file22 != null) {
                file22.close();
            }
            throw map;
        }
    }

    private static void outputFile(File file, String str, SortedMap<String, ZoneRules> sortedMap, SortedMap<LocalDate, Byte> sortedMap2) {
        Map treeMap = new TreeMap();
        treeMap.put(str, sortedMap);
        outputFile(file, treeMap, new TreeSet(sortedMap.keySet()), new HashSet(sortedMap.values()), sortedMap2);
    }

    private static void outputFile(java.io.File r2, java.util.Map<java.lang.String, java.util.SortedMap<java.lang.String, org.threeten.bp.zone.ZoneRules>> r3, java.util.Set<java.lang.String> r4, java.util.Set<org.threeten.bp.zone.ZoneRules> r5, java.util.SortedMap<org.threeten.bp.LocalDate, java.lang.Byte> r6) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r6 = 0;
        r0 = new java.util.jar.JarOutputStream;	 Catch:{ Exception -> 0x001c }
        r1 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x001c }
        r1.<init>(r2);	 Catch:{ Exception -> 0x001c }
        r0.<init>(r1);	 Catch:{ Exception -> 0x001c }
        outputTzdbEntry(r0, r3, r4, r5);	 Catch:{ Exception -> 0x0017, all -> 0x0014 }
        if (r0 == 0) goto L_0x0043;
    L_0x0010:
        r0.close();	 Catch:{ IOException -> 0x0043 }
        goto L_0x0043;
    L_0x0014:
        r2 = move-exception;
        r6 = r0;
        goto L_0x0044;
    L_0x0017:
        r2 = move-exception;
        r6 = r0;
        goto L_0x001d;
    L_0x001a:
        r2 = move-exception;
        goto L_0x0044;
    L_0x001c:
        r2 = move-exception;
    L_0x001d:
        r3 = java.lang.System.out;	 Catch:{ all -> 0x001a }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x001a }
        r4.<init>();	 Catch:{ all -> 0x001a }
        r5 = "Failed: ";	 Catch:{ all -> 0x001a }
        r4.append(r5);	 Catch:{ all -> 0x001a }
        r5 = r2.toString();	 Catch:{ all -> 0x001a }
        r4.append(r5);	 Catch:{ all -> 0x001a }
        r4 = r4.toString();	 Catch:{ all -> 0x001a }
        r3.println(r4);	 Catch:{ all -> 0x001a }
        r2.printStackTrace();	 Catch:{ all -> 0x001a }
        r2 = 1;	 Catch:{ all -> 0x001a }
        java.lang.System.exit(r2);	 Catch:{ all -> 0x001a }
        if (r6 == 0) goto L_0x0043;
    L_0x0040:
        r6.close();	 Catch:{ IOException -> 0x0043 }
    L_0x0043:
        return;
    L_0x0044:
        if (r6 == 0) goto L_0x0049;
    L_0x0046:
        r6.close();	 Catch:{ IOException -> 0x0049 }
    L_0x0049:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.zone.TzdbZoneRulesCompiler.outputFile(java.io.File, java.util.Map, java.util.Set, java.util.Set, java.util.SortedMap):void");
    }

    private static void outputTzdbEntry(JarOutputStream jarOutputStream, Map<String, SortedMap<String, ZoneRules>> map, Set<String> set, Set<ZoneRules> set2) {
        try {
            jarOutputStream.putNextEntry(new ZipEntry("org/threeten/bp/TZDB.dat"));
            outputTzdbDat(jarOutputStream, map, set, set2);
            jarOutputStream.closeEntry();
        } catch (JarOutputStream jarOutputStream2) {
            map = System.out;
            set = new StringBuilder();
            set.append("Failed: ");
            set.append(jarOutputStream2.toString());
            map.println(set.toString());
            jarOutputStream2.printStackTrace();
            System.exit(1);
        }
    }

    private static void outputTzdbDat(OutputStream outputStream, Map<String, SortedMap<String, ZoneRules>> map, Set<String> set, Set<ZoneRules> set2) throws IOException {
        int binarySearch;
        int indexOf;
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeByte(1);
        dataOutputStream.writeUTF("TZDB");
        String[] strArr = (String[]) map.keySet().toArray(new String[map.size()]);
        dataOutputStream.writeShort(strArr.length);
        for (String writeUTF : strArr) {
            dataOutputStream.writeUTF(writeUTF);
        }
        strArr = (String[]) set.toArray(new String[set.size()]);
        dataOutputStream.writeShort(strArr.length);
        for (String writeUTF2 : strArr) {
            String writeUTF22;
            dataOutputStream.writeUTF(writeUTF22);
        }
        set = new ArrayList(set2);
        dataOutputStream.writeShort(set.size());
        set2 = new ByteArrayOutputStream(1024);
        Iterator it = set.iterator();
        while (it.hasNext()) {
            ZoneRules zoneRules = (ZoneRules) it.next();
            set2.reset();
            Object dataOutputStream2 = new DataOutputStream(set2);
            Ser.write(zoneRules, dataOutputStream2);
            dataOutputStream2.close();
            byte[] toByteArray = set2.toByteArray();
            dataOutputStream.writeShort(toByteArray.length);
            dataOutputStream.write(toByteArray);
        }
        set2 = map.keySet().iterator();
        while (set2.hasNext()) {
            writeUTF22 = (String) set2.next();
            dataOutputStream.writeShort(((SortedMap) map.get(writeUTF22)).size());
            for (Entry entry : ((SortedMap) map.get(writeUTF22)).entrySet()) {
                binarySearch = Arrays.binarySearch(strArr, entry.getKey());
                indexOf = set.indexOf(entry.getValue());
                dataOutputStream.writeShort(binarySearch);
                dataOutputStream.writeShort(indexOf);
            }
        }
        dataOutputStream.flush();
    }

    public TzdbZoneRulesCompiler(String str, List<File> list, File file, boolean z) {
        this.version = str;
        this.sourceFiles = list;
        this.leapSecondsFile = file;
        this.verbose = z;
    }

    public void compile() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Compiling TZDB version ");
        stringBuilder.append(this.version);
        printVerbose(stringBuilder.toString());
        parseFiles();
        parseLeapSecondsFile();
        buildZoneRules();
        stringBuilder = new StringBuilder();
        stringBuilder.append("Compiled TZDB version ");
        stringBuilder.append(this.version);
        printVerbose(stringBuilder.toString());
    }

    public SortedMap<String, ZoneRules> getZones() {
        return this.builtZones;
    }

    public SortedMap<LocalDate, Byte> getLeapSeconds() {
        return this.leapSeconds;
    }

    private LocalDate getMostRecentLeapSecond() {
        return this.leapSeconds.isEmpty() ? null : (LocalDate) this.leapSeconds.lastKey();
    }

    void setDeduplicateMap(Map<Object, Object> map) {
        this.deduplicateMap = map;
    }

    private void parseFiles() throws Exception {
        for (File file : this.sourceFiles) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Parsing file: ");
            stringBuilder.append(file);
            printVerbose(stringBuilder.toString());
            parseFile(file);
        }
    }

    private void parseLeapSecondsFile() throws java.lang.Exception {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r8 = this;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Parsing leap second file: ";
        r0.append(r1);
        r1 = r8.leapSecondsFile;
        r0.append(r1);
        r0 = r0.toString();
        r8.printVerbose(r0);
        r0 = 0;
        r1 = 1;
        r2 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0069, all -> 0x0065 }
        r3 = new java.io.FileReader;	 Catch:{ Exception -> 0x0069, all -> 0x0065 }
        r4 = r8.leapSecondsFile;	 Catch:{ Exception -> 0x0069, all -> 0x0065 }
        r3.<init>(r4);	 Catch:{ Exception -> 0x0069, all -> 0x0065 }
        r2.<init>(r3);	 Catch:{ Exception -> 0x0069, all -> 0x0065 }
    L_0x0024:
        r3 = r2.readLine();	 Catch:{ Exception -> 0x0063 }
        if (r3 == 0) goto L_0x005d;
    L_0x002a:
        r0 = 35;
        r0 = r3.indexOf(r0);	 Catch:{ Exception -> 0x0058 }
        if (r0 < 0) goto L_0x0038;	 Catch:{ Exception -> 0x0058 }
    L_0x0032:
        r4 = 0;	 Catch:{ Exception -> 0x0058 }
        r0 = r3.substring(r4, r0);	 Catch:{ Exception -> 0x0058 }
        goto L_0x0039;
    L_0x0038:
        r0 = r3;
    L_0x0039:
        r3 = r0.trim();	 Catch:{ Exception -> 0x0063 }
        r3 = r3.length();	 Catch:{ Exception -> 0x0063 }
        if (r3 != 0) goto L_0x0044;	 Catch:{ Exception -> 0x0063 }
    L_0x0043:
        goto L_0x0055;	 Catch:{ Exception -> 0x0063 }
    L_0x0044:
        r3 = r8.parseLeapSecondRule(r0);	 Catch:{ Exception -> 0x0063 }
        r4 = r8.leapSeconds;	 Catch:{ Exception -> 0x0063 }
        r5 = r3.leapDate;	 Catch:{ Exception -> 0x0063 }
        r3 = r3.secondAdjustment;	 Catch:{ Exception -> 0x0063 }
        r3 = java.lang.Byte.valueOf(r3);	 Catch:{ Exception -> 0x0063 }
        r4.put(r5, r3);	 Catch:{ Exception -> 0x0063 }
    L_0x0055:
        r1 = r1 + 1;
        goto L_0x0024;
    L_0x0058:
        r0 = move-exception;
        r7 = r3;
        r3 = r0;
        r0 = r7;
        goto L_0x006c;
    L_0x005d:
        if (r2 == 0) goto L_0x0062;
    L_0x005f:
        r2.close();	 Catch:{ Exception -> 0x0062 }
    L_0x0062:
        return;
    L_0x0063:
        r3 = move-exception;
        goto L_0x006c;
    L_0x0065:
        r1 = move-exception;
        r2 = r0;
        r0 = r1;
        goto L_0x009b;
    L_0x0069:
        r2 = move-exception;
        r3 = r2;
        r2 = r0;
    L_0x006c:
        r4 = new java.lang.Exception;	 Catch:{ all -> 0x009a }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x009a }
        r5.<init>();	 Catch:{ all -> 0x009a }
        r6 = "Failed while processing file '";	 Catch:{ all -> 0x009a }
        r5.append(r6);	 Catch:{ all -> 0x009a }
        r6 = r8.leapSecondsFile;	 Catch:{ all -> 0x009a }
        r5.append(r6);	 Catch:{ all -> 0x009a }
        r6 = "' on line ";	 Catch:{ all -> 0x009a }
        r5.append(r6);	 Catch:{ all -> 0x009a }
        r5.append(r1);	 Catch:{ all -> 0x009a }
        r1 = " '";	 Catch:{ all -> 0x009a }
        r5.append(r1);	 Catch:{ all -> 0x009a }
        r5.append(r0);	 Catch:{ all -> 0x009a }
        r0 = "'";	 Catch:{ all -> 0x009a }
        r5.append(r0);	 Catch:{ all -> 0x009a }
        r0 = r5.toString();	 Catch:{ all -> 0x009a }
        r4.<init>(r0, r3);	 Catch:{ all -> 0x009a }
        throw r4;	 Catch:{ all -> 0x009a }
    L_0x009a:
        r0 = move-exception;
    L_0x009b:
        if (r2 == 0) goto L_0x00a0;
    L_0x009d:
        r2.close();	 Catch:{ Exception -> 0x00a0 }
    L_0x00a0:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.zone.TzdbZoneRulesCompiler.parseLeapSecondsFile():void");
    }

    private LeapSecondRule parseLeapSecondRule(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, " \t");
        if (!stringTokenizer.nextToken().equals("Leap")) {
            throw new IllegalArgumentException("Unknown line");
        } else if (stringTokenizer.countTokens() < 6) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid leap second line in file: ");
            stringBuilder.append(this.leapSecondsFile);
            stringBuilder.append(", line: ");
            stringBuilder.append(str);
            printVerbose(stringBuilder.toString());
            throw new IllegalArgumentException("Invalid leap second line");
        } else {
            byte b;
            StringBuilder stringBuilder2;
            str = LocalDate.of(Integer.parseInt(stringTokenizer.nextToken()), parseMonth(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()));
            String nextToken = stringTokenizer.nextToken();
            String nextToken2 = stringTokenizer.nextToken();
            if (nextToken2.equals("+")) {
                if ("23:59:60".equals(nextToken)) {
                    b = (byte) 1;
                } else {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Leap seconds can only be inserted at 23:59:60 - Date:");
                    stringBuilder2.append(str);
                    throw new IllegalArgumentException(stringBuilder2.toString());
                }
            } else if (!nextToken2.equals("-")) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Invalid adjustment '");
                stringBuilder2.append(nextToken2);
                stringBuilder2.append("' in leap second rule for ");
                stringBuilder2.append(str);
                throw new IllegalArgumentException(stringBuilder2.toString());
            } else if ("23:59:59".equals(nextToken)) {
                b = (byte) -1;
            } else {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Leap seconds can only be removed at 23:59:59 - Date:");
                stringBuilder2.append(str);
                throw new IllegalArgumentException(stringBuilder2.toString());
            }
            String nextToken3 = stringTokenizer.nextToken();
            if ("S".equalsIgnoreCase(nextToken3)) {
                return new LeapSecondRule(str, b);
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Only stationary ('S') leap seconds are supported, not '");
            stringBuilder2.append(nextToken3);
            stringBuilder2.append("'");
            throw new IllegalArgumentException(stringBuilder2.toString());
        }
    }

    private void parseFile(File file) throws Exception {
        BufferedReader bufferedReader;
        int i;
        Throwable e;
        BufferedReader bufferedReader2 = null;
        String str;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            List list = null;
            str = list;
            i = 1;
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    try {
                        int indexOf = readLine.indexOf(35);
                        str = indexOf >= 0 ? readLine.substring(0, indexOf) : readLine;
                        if (str.trim().length() != 0) {
                            StringTokenizer stringTokenizer = new StringTokenizer(str, " \t");
                            if (list != null && Character.isWhitespace(str.charAt(0)) && stringTokenizer.hasMoreTokens()) {
                                if (!parseZoneLine(stringTokenizer, list)) {
                                }
                            } else if (stringTokenizer.hasMoreTokens()) {
                                String nextToken = stringTokenizer.nextToken();
                                StringBuilder stringBuilder;
                                if (nextToken.equals("Zone")) {
                                    if (stringTokenizer.countTokens() < 3) {
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append("Invalid Zone line in file: ");
                                        stringBuilder.append(file);
                                        stringBuilder.append(", line: ");
                                        stringBuilder.append(str);
                                        printVerbose(stringBuilder.toString());
                                        throw new IllegalArgumentException("Invalid Zone line");
                                    }
                                    list = new ArrayList();
                                    this.zones.put(stringTokenizer.nextToken(), list);
                                    if (parseZoneLine(stringTokenizer, list)) {
                                    }
                                } else if (nextToken.equals("Rule")) {
                                    if (stringTokenizer.countTokens() < 9) {
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append("Invalid Rule line in file: ");
                                        stringBuilder.append(file);
                                        stringBuilder.append(", line: ");
                                        stringBuilder.append(str);
                                        printVerbose(stringBuilder.toString());
                                        throw new IllegalArgumentException("Invalid Rule line");
                                    }
                                    parseRuleLine(stringTokenizer);
                                } else if (!nextToken.equals(HttpHeaders.LINK)) {
                                    throw new IllegalArgumentException("Unknown line");
                                } else if (stringTokenizer.countTokens() < 2) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("Invalid Link line in file: ");
                                    stringBuilder.append(file);
                                    stringBuilder.append(", line: ");
                                    stringBuilder.append(str);
                                    printVerbose(stringBuilder.toString());
                                    throw new IllegalArgumentException("Invalid Link line");
                                } else {
                                    this.links.put(stringTokenizer.nextToken(), stringTokenizer.nextToken());
                                }
                            } else {
                                continue;
                            }
                            list = null;
                        }
                        i++;
                    } catch (Exception e2) {
                        e = e2;
                        bufferedReader2 = bufferedReader;
                        str = readLine;
                    } catch (Throwable th) {
                        file = th;
                    }
                } catch (Exception e3) {
                    e = e3;
                    bufferedReader2 = bufferedReader;
                } catch (Throwable th2) {
                    file = th2;
                }
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        } catch (Throwable e4) {
            str = null;
            e = e4;
            i = 1;
            try {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Failed while processing file '");
                stringBuilder2.append(file);
                stringBuilder2.append("' on line ");
                stringBuilder2.append(i);
                stringBuilder2.append(" '");
                stringBuilder2.append(str);
                stringBuilder2.append("'");
                throw new Exception(stringBuilder2.toString(), e);
            } catch (Throwable th3) {
                file = th3;
                bufferedReader = bufferedReader2;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw file;
            }
        }
    }

    private void parseRuleLine(StringTokenizer stringTokenizer) {
        TZDBMonthDayTime tZDBRule = new TZDBRule();
        String nextToken = stringTokenizer.nextToken();
        if (!this.rules.containsKey(nextToken)) {
            this.rules.put(nextToken, new ArrayList());
        }
        ((List) this.rules.get(nextToken)).add(tZDBRule);
        tZDBRule.startYear = parseYear(stringTokenizer.nextToken(), 0);
        tZDBRule.endYear = parseYear(stringTokenizer.nextToken(), tZDBRule.startYear);
        if (tZDBRule.startYear > tZDBRule.endYear) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Year order invalid: ");
            stringBuilder.append(tZDBRule.startYear);
            stringBuilder.append(" > ");
            stringBuilder.append(tZDBRule.endYear);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        parseOptional(stringTokenizer.nextToken());
        parseMonthDayTime(stringTokenizer, tZDBRule);
        tZDBRule.savingsAmount = parsePeriod(stringTokenizer.nextToken());
        tZDBRule.text = parseOptional(stringTokenizer.nextToken());
    }

    private boolean parseZoneLine(java.util.StringTokenizer r5, java.util.List<org.threeten.bp.zone.TzdbZoneRulesCompiler.TZDBZone> r6) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r4 = this;
        r0 = new org.threeten.bp.zone.TzdbZoneRulesCompiler$TZDBZone;
        r0.<init>();
        r6.add(r0);
        r6 = r5.nextToken();
        r6 = r4.parseOffset(r6);
        r0.standardOffset = r6;
        r6 = r5.nextToken();
        r6 = r4.parseOptional(r6);
        r1 = 0;
        r2 = 0;
        if (r6 != 0) goto L_0x0027;
    L_0x001e:
        r6 = java.lang.Integer.valueOf(r1);
        r0.fixedSavingsSecs = r6;
        r0.savingsRule = r2;
        goto L_0x0038;
    L_0x0027:
        r3 = r4.parsePeriod(r6);	 Catch:{ Exception -> 0x0034 }
        r3 = java.lang.Integer.valueOf(r3);	 Catch:{ Exception -> 0x0034 }
        r0.fixedSavingsSecs = r3;	 Catch:{ Exception -> 0x0034 }
        r0.savingsRule = r2;	 Catch:{ Exception -> 0x0034 }
        goto L_0x0038;
    L_0x0034:
        r0.fixedSavingsSecs = r2;
        r0.savingsRule = r6;
    L_0x0038:
        r6 = r5.nextToken();
        r0.text = r6;
        r6 = r5.hasMoreTokens();
        if (r6 == 0) goto L_0x005c;
    L_0x0044:
        r6 = r5.nextToken();
        r6 = java.lang.Integer.parseInt(r6);
        r6 = org.threeten.bp.Year.of(r6);
        r0.year = r6;
        r6 = r5.hasMoreTokens();
        if (r6 == 0) goto L_0x005b;
    L_0x0058:
        r4.parseMonthDayTime(r5, r0);
    L_0x005b:
        return r1;
    L_0x005c:
        r5 = 1;
        return r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.zone.TzdbZoneRulesCompiler.parseZoneLine(java.util.StringTokenizer, java.util.List):boolean");
    }

    private void parseMonthDayTime(StringTokenizer stringTokenizer, TZDBMonthDayTime tZDBMonthDayTime) {
        tZDBMonthDayTime.month = parseMonth(stringTokenizer.nextToken());
        if (stringTokenizer.hasMoreTokens()) {
            String nextToken = stringTokenizer.nextToken();
            if (nextToken.startsWith("last")) {
                tZDBMonthDayTime.dayOfMonth = -1;
                tZDBMonthDayTime.dayOfWeek = parseDayOfWeek(nextToken.substring(4));
                tZDBMonthDayTime.adjustForwards = false;
            } else {
                int indexOf = nextToken.indexOf(">=");
                if (indexOf > 0) {
                    tZDBMonthDayTime.dayOfWeek = parseDayOfWeek(nextToken.substring(0, indexOf));
                    nextToken = nextToken.substring(indexOf + 2);
                } else {
                    indexOf = nextToken.indexOf("<=");
                    if (indexOf > 0) {
                        tZDBMonthDayTime.dayOfWeek = parseDayOfWeek(nextToken.substring(0, indexOf));
                        tZDBMonthDayTime.adjustForwards = false;
                        nextToken = nextToken.substring(indexOf + 2);
                    }
                }
                tZDBMonthDayTime.dayOfMonth = Integer.parseInt(nextToken);
            }
            if (stringTokenizer.hasMoreTokens()) {
                stringTokenizer = stringTokenizer.nextToken();
                int parseSecs = parseSecs(stringTokenizer);
                if (parseSecs == 86400) {
                    tZDBMonthDayTime.endOfDay = true;
                    parseSecs = 0;
                }
                tZDBMonthDayTime.time = (LocalTime) deduplicate(LocalTime.ofSecondOfDay((long) parseSecs));
                tZDBMonthDayTime.timeDefinition = parseTimeDefinition(stringTokenizer.charAt(stringTokenizer.length() - 1));
            }
        }
    }

    private int parseYear(String str, int i) {
        str = str.toLowerCase();
        if (matches(str, "minimum")) {
            return Year.MIN_VALUE;
        }
        if (matches(str, "maximum")) {
            return Year.MAX_VALUE;
        }
        if (str.equals("only")) {
            return i;
        }
        return Integer.parseInt(str);
    }

    private Month parseMonth(String str) {
        str = str.toLowerCase();
        for (Month month : Month.values()) {
            if (matches(str, month.name().toLowerCase())) {
                return month;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown month: ");
        stringBuilder.append(str);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private DayOfWeek parseDayOfWeek(String str) {
        str = str.toLowerCase();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if (matches(str, dayOfWeek.name().toLowerCase())) {
                return dayOfWeek;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown day-of-week: ");
        stringBuilder.append(str);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private boolean matches(String str, String str2) {
        return str.startsWith(str2.substring(0, 3)) && str2.startsWith(str) && str.length() <= str2.length();
    }

    private String parseOptional(String str) {
        return str.equals("-") ? null : str;
    }

    private int parseSecs(String str) {
        if (str.equals("-")) {
            return null;
        }
        boolean startsWith = str.startsWith("-");
        ParsePosition parsePosition = new ParsePosition(startsWith);
        TemporalAccessor parseUnresolved = TIME_PARSER.parseUnresolved(str, parsePosition);
        if (parseUnresolved != null) {
            if (parsePosition.getErrorIndex() < 0) {
                long longValue;
                long j = parseUnresolved.getLong(ChronoField.HOUR_OF_DAY);
                Long l = null;
                str = parseUnresolved.isSupported(ChronoField.MINUTE_OF_HOUR) != null ? Long.valueOf(parseUnresolved.getLong(ChronoField.MINUTE_OF_HOUR)) : null;
                if (parseUnresolved.isSupported(ChronoField.SECOND_OF_MINUTE)) {
                    l = Long.valueOf(parseUnresolved.getLong(ChronoField.SECOND_OF_MINUTE));
                }
                j = (j * 60) * 60;
                long j2 = 0;
                if (str != null) {
                    longValue = str.longValue();
                } else {
                    longValue = 0;
                }
                long j3 = j + (longValue * 60);
                if (l != null) {
                    j2 = l.longValue();
                }
                str = (int) (j3 + j2);
                if (startsWith) {
                    str = -str;
                }
                return str;
            }
        }
        throw new IllegalArgumentException(str);
    }

    private ZoneOffset parseOffset(String str) {
        return ZoneOffset.ofTotalSeconds(parseSecs(str));
    }

    private int parsePeriod(String str) {
        return parseSecs(str);
    }

    private TimeDefinition parseTimeDefinition(char c) {
        if (c != 'G') {
            if (c != 'S') {
                if (!(c == 'U' || c == 'Z' || c == 'g')) {
                    if (c != 's') {
                        if (!(c == 'u' || c == 'z')) {
                            return TimeDefinition.WALL;
                        }
                    }
                }
            }
            return TimeDefinition.STANDARD;
        }
        return TimeDefinition.UTC;
    }

    private void buildZoneRules() throws Exception {
        for (String str : this.zones.keySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Building zone ");
            stringBuilder.append(str);
            printVerbose(stringBuilder.toString());
            String str2 = (String) deduplicate(str2);
            List<TZDBZone> list = (List) this.zones.get(str2);
            ZoneRulesBuilder zoneRulesBuilder = new ZoneRulesBuilder();
            for (TZDBZone addToBuilder : list) {
                zoneRulesBuilder = addToBuilder.addToBuilder(zoneRulesBuilder, this.rules);
            }
            this.builtZones.put(str2, deduplicate(zoneRulesBuilder.toRules(str2, this.deduplicateMap)));
        }
        for (String str22 : this.links.keySet()) {
            str22 = (String) deduplicate(str22);
            String str3 = (String) this.links.get(str22);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Linking alias ");
            stringBuilder2.append(str22);
            stringBuilder2.append(" to ");
            stringBuilder2.append(str3);
            printVerbose(stringBuilder2.toString());
            Object obj = (ZoneRules) this.builtZones.get(str3);
            if (obj == null) {
                str3 = (String) this.links.get(str3);
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Relinking alias ");
                stringBuilder2.append(str22);
                stringBuilder2.append(" to ");
                stringBuilder2.append(str3);
                printVerbose(stringBuilder2.toString());
                obj = (ZoneRules) this.builtZones.get(str3);
                if (obj == null) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Alias '");
                    stringBuilder2.append(str22);
                    stringBuilder2.append("' links to invalid zone '");
                    stringBuilder2.append(str3);
                    stringBuilder2.append("' for '");
                    stringBuilder2.append(this.version);
                    stringBuilder2.append("'");
                    throw new IllegalArgumentException(stringBuilder2.toString());
                }
            }
            this.builtZones.put(str22, obj);
        }
        this.builtZones.remove("UTC");
        this.builtZones.remove("GMT");
        this.builtZones.remove("GMT0");
        this.builtZones.remove("GMT+0");
        this.builtZones.remove("GMT-0");
    }

    <T> T deduplicate(T t) {
        if (!this.deduplicateMap.containsKey(t)) {
            this.deduplicateMap.put(t, t);
        }
        return this.deduplicateMap.get(t);
    }

    private void printVerbose(String str) {
        if (this.verbose) {
            System.out.println(str);
        }
    }
}
