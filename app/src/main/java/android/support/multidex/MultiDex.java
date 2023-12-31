package android.support.multidex;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build.VERSION;
import android.util.Log;
import dalvik.system.DexFile;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipFile;

public final class MultiDex {
    private static final String CODE_CACHE_NAME = "code_cache";
    private static final String CODE_CACHE_SECONDARY_FOLDER_NAME = "secondary-dexes";
    private static final boolean IS_VM_MULTIDEX_CAPABLE = isVMMultidexCapable(System.getProperty("java.vm.version"));
    private static final int MAX_SUPPORTED_SDK_VERSION = 20;
    private static final int MIN_SDK_VERSION = 4;
    private static final String NO_KEY_PREFIX = "";
    private static final String OLD_SECONDARY_FOLDER_NAME = "secondary-dexes";
    static final String TAG = "MultiDex";
    private static final int VM_WITH_MULTIDEX_VERSION_MAJOR = 2;
    private static final int VM_WITH_MULTIDEX_VERSION_MINOR = 1;
    private static final Set<File> installedApk = new HashSet();

    private static final class V14 {
        private static final int EXTRACTED_SUFFIX_LENGTH = ".zip".length();
        private final ElementConstructor elementConstructor;

        private interface ElementConstructor {
            Object newInstance(File file, DexFile dexFile) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException;
        }

        private static class ICSElementConstructor implements ElementConstructor {
            private final Constructor<?> elementConstructor;

            ICSElementConstructor(Class<?> cls) throws SecurityException, NoSuchMethodException {
                this.elementConstructor = cls.getConstructor(new Class[]{File.class, ZipFile.class, DexFile.class});
                this.elementConstructor.setAccessible(true);
            }

            public Object newInstance(File file, DexFile dexFile) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
                return this.elementConstructor.newInstance(new Object[]{file, new ZipFile(file), dexFile});
            }
        }

        private static class JBMR11ElementConstructor implements ElementConstructor {
            private final Constructor<?> elementConstructor;

            JBMR11ElementConstructor(Class<?> cls) throws SecurityException, NoSuchMethodException {
                this.elementConstructor = cls.getConstructor(new Class[]{File.class, File.class, DexFile.class});
                this.elementConstructor.setAccessible(true);
            }

            public Object newInstance(File file, DexFile dexFile) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
                return this.elementConstructor.newInstance(new Object[]{file, file, dexFile});
            }
        }

        private static class JBMR2ElementConstructor implements ElementConstructor {
            private final Constructor<?> elementConstructor;

            JBMR2ElementConstructor(Class<?> cls) throws SecurityException, NoSuchMethodException {
                this.elementConstructor = cls.getConstructor(new Class[]{File.class, Boolean.TYPE, File.class, DexFile.class});
                this.elementConstructor.setAccessible(true);
            }

            public Object newInstance(File file, DexFile dexFile) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
                return this.elementConstructor.newInstance(new Object[]{file, Boolean.FALSE, file, dexFile});
            }
        }

        static void install(ClassLoader classLoader, List<? extends File> list) throws IOException, SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
            classLoader = MultiDex.findField(classLoader, "pathList").get(classLoader);
            list = new V14().makeDexElements(list);
            try {
                MultiDex.expandFieldArray(classLoader, "dexElements", list);
            } catch (Throwable e) {
                Log.w(MultiDex.TAG, "Failed find field 'dexElements' attempting 'pathElements'", e);
                MultiDex.expandFieldArray(classLoader, "pathElements", list);
            }
        }

        private V14() throws java.lang.ClassNotFoundException, java.lang.SecurityException, java.lang.NoSuchMethodException {
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
            r2 = this;
            r2.<init>();
            r0 = "dalvik.system.DexPathList$Element";
            r0 = java.lang.Class.forName(r0);
            r1 = new android.support.multidex.MultiDex$V14$ICSElementConstructor;	 Catch:{ NoSuchMethodException -> 0x000f }
            r1.<init>(r0);	 Catch:{ NoSuchMethodException -> 0x000f }
            goto L_0x001a;
        L_0x000f:
            r1 = new android.support.multidex.MultiDex$V14$JBMR11ElementConstructor;	 Catch:{ NoSuchMethodException -> 0x0015 }
            r1.<init>(r0);	 Catch:{ NoSuchMethodException -> 0x0015 }
            goto L_0x001a;
        L_0x0015:
            r1 = new android.support.multidex.MultiDex$V14$JBMR2ElementConstructor;
            r1.<init>(r0);
        L_0x001a:
            r2.elementConstructor = r1;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.multidex.MultiDex.V14.<init>():void");
        }

        private Object[] makeDexElements(List<? extends File> list) throws IOException, SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
            Object[] objArr = new Object[list.size()];
            for (int i = 0; i < objArr.length; i++) {
                File file = (File) list.get(i);
                objArr[i] = this.elementConstructor.newInstance(file, DexFile.loadDex(file.getPath(), optimizedPathFor(file), 0));
            }
            return objArr;
        }

        private static String optimizedPathFor(File file) {
            File parentFile = file.getParentFile();
            file = file.getName();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(file.substring(0, file.length() - EXTRACTED_SUFFIX_LENGTH));
            stringBuilder.append(".dex");
            return new File(parentFile, stringBuilder.toString()).getPath();
        }
    }

    private static final class V19 {
        private V19() {
        }

        static void install(ClassLoader classLoader, List<? extends File> list, File file) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IOException {
            classLoader = MultiDex.findField(classLoader, "pathList").get(classLoader);
            ArrayList arrayList = new ArrayList();
            MultiDex.expandFieldArray(classLoader, "dexElements", makeDexElements(classLoader, new ArrayList(list), file, arrayList));
            if (arrayList.size() > null) {
                list = arrayList.iterator();
                while (list.hasNext() != null) {
                    Log.w(MultiDex.TAG, "Exception in makeDexElement", (IOException) list.next());
                }
                list = MultiDex.findField(classLoader, "dexElementsSuppressedExceptions");
                IOException[] iOExceptionArr = (IOException[]) list.get(classLoader);
                if (iOExceptionArr == null) {
                    file = (IOException[]) arrayList.toArray(new IOException[arrayList.size()]);
                } else {
                    File file2 = new IOException[(arrayList.size() + iOExceptionArr.length)];
                    arrayList.toArray(file2);
                    System.arraycopy(iOExceptionArr, 0, file2, arrayList.size(), iOExceptionArr.length);
                    file = file2;
                }
                list.set(classLoader, file);
                classLoader = new IOException("I/O exception during makeDexElement");
                classLoader.initCause((Throwable) arrayList.get(0));
                throw classLoader;
            }
        }

        private static Object[] makeDexElements(Object obj, ArrayList<File> arrayList, File file, ArrayList<IOException> arrayList2) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            return (Object[]) MultiDex.findMethod(obj, "makeDexElements", ArrayList.class, File.class, ArrayList.class).invoke(obj, new Object[]{arrayList, file, arrayList2});
        }
    }

    private static final class V4 {
        private V4() {
        }

        static void install(ClassLoader classLoader, List<? extends File> list) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, IOException {
            int size = list.size();
            Field access$000 = MultiDex.findField(classLoader, "path");
            StringBuilder stringBuilder = new StringBuilder((String) access$000.get(classLoader));
            String[] strArr = new String[size];
            File[] fileArr = new File[size];
            ZipFile[] zipFileArr = new ZipFile[size];
            DexFile[] dexFileArr = new DexFile[size];
            list = list.listIterator();
            while (list.hasNext()) {
                File file = (File) list.next();
                String absolutePath = file.getAbsolutePath();
                stringBuilder.append(':');
                stringBuilder.append(absolutePath);
                int previousIndex = list.previousIndex();
                strArr[previousIndex] = absolutePath;
                fileArr[previousIndex] = file;
                zipFileArr[previousIndex] = new ZipFile(file);
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(absolutePath);
                stringBuilder2.append(".dex");
                dexFileArr[previousIndex] = DexFile.loadDex(absolutePath, stringBuilder2.toString(), 0);
            }
            access$000.set(classLoader, stringBuilder.toString());
            MultiDex.expandFieldArray(classLoader, "mPaths", strArr);
            MultiDex.expandFieldArray(classLoader, "mFiles", fileArr);
            MultiDex.expandFieldArray(classLoader, "mZips", zipFileArr);
            MultiDex.expandFieldArray(classLoader, "mDexs", dexFileArr);
        }
    }

    private MultiDex() {
    }

    public static void install(Context context) {
        Log.i(TAG, "Installing application");
        if (IS_VM_MULTIDEX_CAPABLE) {
            Log.i(TAG, "VM has multidex support, MultiDex support library is disabled.");
        } else if (VERSION.SDK_INT < 4) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MultiDex installation failed. SDK ");
            stringBuilder.append(VERSION.SDK_INT);
            stringBuilder.append(" is unsupported. Min SDK version is ");
            stringBuilder.append(4);
            stringBuilder.append(".");
            throw new RuntimeException(stringBuilder.toString());
        } else {
            try {
                ApplicationInfo applicationInfo = getApplicationInfo(context);
                if (applicationInfo == null) {
                    Log.i(TAG, "No ApplicationInfo available, i.e. running on a test Context: MultiDex support library is disabled.");
                    return;
                }
                doInstallation(context, new File(applicationInfo.sourceDir), new File(applicationInfo.dataDir), "secondary-dexes", "", true);
                Log.i(TAG, "install done");
            } catch (Context context2) {
                Log.e(TAG, "MultiDex installation failure", context2);
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("MultiDex installation failed (");
                stringBuilder2.append(context2.getMessage());
                stringBuilder2.append(").");
                throw new RuntimeException(stringBuilder2.toString());
            }
        }
    }

    public static void installInstrumentation(Context context, Context context2) {
        Log.i(TAG, "Installing instrumentation");
        if (IS_VM_MULTIDEX_CAPABLE) {
            Log.i(TAG, "VM has multidex support, MultiDex support library is disabled.");
        } else if (VERSION.SDK_INT < 4) {
            context2 = new StringBuilder();
            context2.append("MultiDex installation failed. SDK ");
            context2.append(VERSION.SDK_INT);
            context2.append(" is unsupported. Min SDK version is ");
            context2.append(4);
            context2.append(".");
            throw new RuntimeException(context2.toString());
        } else {
            try {
                ApplicationInfo applicationInfo = getApplicationInfo(context);
                if (applicationInfo == null) {
                    Log.i(TAG, "No ApplicationInfo available for instrumentation, i.e. running on a test Context: MultiDex support library is disabled.");
                    return;
                }
                ApplicationInfo applicationInfo2 = getApplicationInfo(context2);
                if (applicationInfo2 == null) {
                    Log.i(TAG, "No ApplicationInfo available, i.e. running on a test Context: MultiDex support library is disabled.");
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(context.getPackageName());
                stringBuilder.append(".");
                String stringBuilder2 = stringBuilder.toString();
                File file = new File(applicationInfo2.dataDir);
                File file2 = new File(applicationInfo.sourceDir);
                context = new StringBuilder();
                context.append(stringBuilder2);
                context.append("secondary-dexes");
                doInstallation(context2, file2, file, context.toString(), stringBuilder2, false);
                doInstallation(context2, new File(applicationInfo2.sourceDir), file, "secondary-dexes", "", false);
                Log.i(TAG, "Installation done");
            } catch (Context context3) {
                Log.e(TAG, "MultiDex installation failure", context3);
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("MultiDex installation failed (");
                stringBuilder3.append(context3.getMessage());
                stringBuilder3.append(").");
                throw new RuntimeException(stringBuilder3.toString());
            }
        }
    }

    private static void doInstallation(android.content.Context r5, java.io.File r6, java.io.File r7, java.lang.String r8, java.lang.String r9, boolean r10) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.IllegalAccessException, java.lang.NoSuchFieldException, java.lang.reflect.InvocationTargetException, java.lang.NoSuchMethodException, java.lang.SecurityException, java.lang.ClassNotFoundException, java.lang.InstantiationException {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = installedApk;
        monitor-enter(r0);
        r1 = installedApk;	 Catch:{ all -> 0x00af }
        r1 = r1.contains(r6);	 Catch:{ all -> 0x00af }
        if (r1 == 0) goto L_0x000d;	 Catch:{ all -> 0x00af }
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x00af }
        return;	 Catch:{ all -> 0x00af }
    L_0x000d:
        r1 = installedApk;	 Catch:{ all -> 0x00af }
        r1.add(r6);	 Catch:{ all -> 0x00af }
        r1 = android.os.Build.VERSION.SDK_INT;	 Catch:{ all -> 0x00af }
        r2 = 20;	 Catch:{ all -> 0x00af }
        if (r1 <= r2) goto L_0x0055;	 Catch:{ all -> 0x00af }
    L_0x0018:
        r1 = "MultiDex";	 Catch:{ all -> 0x00af }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00af }
        r3.<init>();	 Catch:{ all -> 0x00af }
        r4 = "MultiDex is not guaranteed to work in SDK version ";	 Catch:{ all -> 0x00af }
        r3.append(r4);	 Catch:{ all -> 0x00af }
        r4 = android.os.Build.VERSION.SDK_INT;	 Catch:{ all -> 0x00af }
        r3.append(r4);	 Catch:{ all -> 0x00af }
        r4 = ": SDK version higher than ";	 Catch:{ all -> 0x00af }
        r3.append(r4);	 Catch:{ all -> 0x00af }
        r3.append(r2);	 Catch:{ all -> 0x00af }
        r2 = " should be backed by ";	 Catch:{ all -> 0x00af }
        r3.append(r2);	 Catch:{ all -> 0x00af }
        r2 = "runtime with built-in multidex capabilty but it's not the ";	 Catch:{ all -> 0x00af }
        r3.append(r2);	 Catch:{ all -> 0x00af }
        r2 = "case here: java.vm.version=\"";	 Catch:{ all -> 0x00af }
        r3.append(r2);	 Catch:{ all -> 0x00af }
        r2 = "java.vm.version";	 Catch:{ all -> 0x00af }
        r2 = java.lang.System.getProperty(r2);	 Catch:{ all -> 0x00af }
        r3.append(r2);	 Catch:{ all -> 0x00af }
        r2 = "\"";	 Catch:{ all -> 0x00af }
        r3.append(r2);	 Catch:{ all -> 0x00af }
        r2 = r3.toString();	 Catch:{ all -> 0x00af }
        android.util.Log.w(r1, r2);	 Catch:{ all -> 0x00af }
    L_0x0055:
        r1 = r5.getClassLoader();	 Catch:{ RuntimeException -> 0x00a5 }
        if (r1 != 0) goto L_0x0064;
    L_0x005b:
        r5 = "MultiDex";	 Catch:{ all -> 0x00af }
        r6 = "Context class loader is null. Must be running in test mode. Skip patching.";	 Catch:{ all -> 0x00af }
        android.util.Log.e(r5, r6);	 Catch:{ all -> 0x00af }
        monitor-exit(r0);	 Catch:{ all -> 0x00af }
        return;
    L_0x0064:
        clearOldDexDir(r5);	 Catch:{ Throwable -> 0x0068 }
        goto L_0x0070;
    L_0x0068:
        r2 = move-exception;
        r3 = "MultiDex";	 Catch:{ all -> 0x00af }
        r4 = "Something went wrong when trying to clear old MultiDex extraction, continuing without cleaning.";	 Catch:{ all -> 0x00af }
        android.util.Log.w(r3, r4, r2);	 Catch:{ all -> 0x00af }
    L_0x0070:
        r7 = getDexDir(r5, r7, r8);	 Catch:{ all -> 0x00af }
        r8 = new android.support.multidex.MultiDexExtractor;	 Catch:{ all -> 0x00af }
        r8.<init>(r6, r7);	 Catch:{ all -> 0x00af }
        r6 = 0;
        r2 = 0;
        r2 = r8.load(r5, r9, r2);	 Catch:{ all -> 0x00a0 }
        installSecondaryDexes(r1, r7, r2);	 Catch:{ IOException -> 0x0083 }
        goto L_0x0096;
    L_0x0083:
        r2 = move-exception;
        if (r10 != 0) goto L_0x0087;
    L_0x0086:
        throw r2;	 Catch:{ all -> 0x00a0 }
    L_0x0087:
        r10 = "MultiDex";	 Catch:{ all -> 0x00a0 }
        r3 = "Failed to install extracted secondary dex files, retrying with forced extraction";	 Catch:{ all -> 0x00a0 }
        android.util.Log.w(r10, r3, r2);	 Catch:{ all -> 0x00a0 }
        r10 = 1;	 Catch:{ all -> 0x00a0 }
        r5 = r8.load(r5, r9, r10);	 Catch:{ all -> 0x00a0 }
        installSecondaryDexes(r1, r7, r5);	 Catch:{ all -> 0x00a0 }
    L_0x0096:
        r8.close();	 Catch:{ IOException -> 0x009a }
        goto L_0x009b;
    L_0x009a:
        r6 = move-exception;
    L_0x009b:
        if (r6 == 0) goto L_0x009e;
    L_0x009d:
        throw r6;	 Catch:{ all -> 0x00af }
    L_0x009e:
        monitor-exit(r0);	 Catch:{ all -> 0x00af }
        return;
    L_0x00a0:
        r5 = move-exception;
        r8.close();	 Catch:{ IOException -> 0x00a4 }
    L_0x00a4:
        throw r5;	 Catch:{ all -> 0x00af }
    L_0x00a5:
        r5 = move-exception;	 Catch:{ all -> 0x00af }
        r6 = "MultiDex";	 Catch:{ all -> 0x00af }
        r7 = "Failure while trying to obtain Context class loader. Must be running in test mode. Skip patching.";	 Catch:{ all -> 0x00af }
        android.util.Log.w(r6, r7, r5);	 Catch:{ all -> 0x00af }
        monitor-exit(r0);	 Catch:{ all -> 0x00af }
        return;	 Catch:{ all -> 0x00af }
    L_0x00af:
        r5 = move-exception;	 Catch:{ all -> 0x00af }
        monitor-exit(r0);	 Catch:{ all -> 0x00af }
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.multidex.MultiDex.doInstallation(android.content.Context, java.io.File, java.io.File, java.lang.String, java.lang.String, boolean):void");
    }

    private static ApplicationInfo getApplicationInfo(Context context) {
        try {
            return context.getApplicationInfo();
        } catch (Context context2) {
            Log.w(TAG, "Failure while trying to obtain ApplicationInfo from Context. Must be running in test mode. Skip patching.", context2);
            return null;
        }
    }

    static boolean isVMMultidexCapable(java.lang.String r5) {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = 0;
        if (r5 == 0) goto L_0x002c;
    L_0x0003:
        r1 = "(\\d+)\\.(\\d+)(\\.\\d+)?";
        r1 = java.util.regex.Pattern.compile(r1);
        r1 = r1.matcher(r5);
        r2 = r1.matches();
        if (r2 == 0) goto L_0x002c;
    L_0x0013:
        r2 = 1;
        r3 = r1.group(r2);	 Catch:{ NumberFormatException -> 0x002c }
        r3 = java.lang.Integer.parseInt(r3);	 Catch:{ NumberFormatException -> 0x002c }
        r4 = 2;	 Catch:{ NumberFormatException -> 0x002c }
        r1 = r1.group(r4);	 Catch:{ NumberFormatException -> 0x002c }
        r1 = java.lang.Integer.parseInt(r1);	 Catch:{ NumberFormatException -> 0x002c }
        if (r3 > r4) goto L_0x002b;
    L_0x0027:
        if (r3 != r4) goto L_0x002c;
    L_0x0029:
        if (r1 < r2) goto L_0x002c;
    L_0x002b:
        r0 = 1;
    L_0x002c:
        r1 = "MultiDex";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "VM with version ";
        r2.append(r3);
        r2.append(r5);
        if (r0 == 0) goto L_0x0040;
    L_0x003d:
        r5 = " has multidex support";
        goto L_0x0042;
    L_0x0040:
        r5 = " does not have multidex support";
    L_0x0042:
        r2.append(r5);
        r5 = r2.toString();
        android.util.Log.i(r1, r5);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.multidex.MultiDex.isVMMultidexCapable(java.lang.String):boolean");
    }

    private static void installSecondaryDexes(ClassLoader classLoader, File file, List<? extends File> list) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IOException, SecurityException, ClassNotFoundException, InstantiationException {
        if (!list.isEmpty()) {
            if (VERSION.SDK_INT >= 19) {
                V19.install(classLoader, list, file);
            } else if (VERSION.SDK_INT >= 14) {
                V14.install(classLoader, list);
            } else {
                V4.install(classLoader, list);
            }
        }
    }

    private static java.lang.reflect.Field findField(java.lang.Object r3, java.lang.String r4) throws java.lang.NoSuchFieldException {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = r3.getClass();
    L_0x0004:
        if (r0 == 0) goto L_0x001a;
    L_0x0006:
        r1 = r0.getDeclaredField(r4);	 Catch:{ NoSuchFieldException -> 0x0015 }
        r2 = r1.isAccessible();	 Catch:{ NoSuchFieldException -> 0x0015 }
        if (r2 != 0) goto L_0x0014;	 Catch:{ NoSuchFieldException -> 0x0015 }
    L_0x0010:
        r2 = 1;	 Catch:{ NoSuchFieldException -> 0x0015 }
        r1.setAccessible(r2);	 Catch:{ NoSuchFieldException -> 0x0015 }
    L_0x0014:
        return r1;
    L_0x0015:
        r0 = r0.getSuperclass();
        goto L_0x0004;
    L_0x001a:
        r0 = new java.lang.NoSuchFieldException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Field ";
        r1.append(r2);
        r1.append(r4);
        r4 = " not found in ";
        r1.append(r4);
        r3 = r3.getClass();
        r1.append(r3);
        r3 = r1.toString();
        r0.<init>(r3);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.multidex.MultiDex.findField(java.lang.Object, java.lang.String):java.lang.reflect.Field");
    }

    private static java.lang.reflect.Method findMethod(java.lang.Object r3, java.lang.String r4, java.lang.Class<?>... r5) throws java.lang.NoSuchMethodException {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = r3.getClass();
    L_0x0004:
        if (r0 == 0) goto L_0x001a;
    L_0x0006:
        r1 = r0.getDeclaredMethod(r4, r5);	 Catch:{ NoSuchMethodException -> 0x0015 }
        r2 = r1.isAccessible();	 Catch:{ NoSuchMethodException -> 0x0015 }
        if (r2 != 0) goto L_0x0014;	 Catch:{ NoSuchMethodException -> 0x0015 }
    L_0x0010:
        r2 = 1;	 Catch:{ NoSuchMethodException -> 0x0015 }
        r1.setAccessible(r2);	 Catch:{ NoSuchMethodException -> 0x0015 }
    L_0x0014:
        return r1;
    L_0x0015:
        r0 = r0.getSuperclass();
        goto L_0x0004;
    L_0x001a:
        r0 = new java.lang.NoSuchMethodException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Method ";
        r1.append(r2);
        r1.append(r4);
        r4 = " with parameters ";
        r1.append(r4);
        r4 = java.util.Arrays.asList(r5);
        r1.append(r4);
        r4 = " not found in ";
        r1.append(r4);
        r3 = r3.getClass();
        r1.append(r3);
        r3 = r1.toString();
        r0.<init>(r3);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.multidex.MultiDex.findMethod(java.lang.Object, java.lang.String, java.lang.Class[]):java.lang.reflect.Method");
    }

    private static void expandFieldArray(Object obj, String str, Object[] objArr) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        str = findField(obj, str);
        Object[] objArr2 = (Object[]) str.get(obj);
        Object[] objArr3 = (Object[]) Array.newInstance(objArr2.getClass().getComponentType(), objArr2.length + objArr.length);
        System.arraycopy(objArr2, 0, objArr3, 0, objArr2.length);
        System.arraycopy(objArr, 0, objArr3, objArr2.length, objArr.length);
        str.set(obj, objArr3);
    }

    private static void clearOldDexDir(Context context) throws Exception {
        File file = new File(context.getFilesDir(), "secondary-dexes");
        if (file.isDirectory() != null) {
            context = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Clearing old secondary dex dir (");
            stringBuilder.append(file.getPath());
            stringBuilder.append(").");
            Log.i(context, stringBuilder.toString());
            context = file.listFiles();
            if (context == null) {
                context = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to list secondary dex dir content (");
                stringBuilder.append(file.getPath());
                stringBuilder.append(").");
                Log.w(context, stringBuilder.toString());
                return;
            }
            for (File file2 : context) {
                String str = TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Trying to delete old file ");
                stringBuilder2.append(file2.getPath());
                stringBuilder2.append(" of size ");
                stringBuilder2.append(file2.length());
                Log.i(str, stringBuilder2.toString());
                if (file2.delete()) {
                    str = TAG;
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Deleted old file ");
                    stringBuilder2.append(file2.getPath());
                    Log.i(str, stringBuilder2.toString());
                } else {
                    str = TAG;
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Failed to delete old file ");
                    stringBuilder2.append(file2.getPath());
                    Log.w(str, stringBuilder2.toString());
                }
            }
            if (file.delete() == null) {
                context = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to delete secondary dex dir ");
                stringBuilder.append(file.getPath());
                Log.w(context, stringBuilder.toString());
            } else {
                context = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Deleted old secondary dex dir ");
                stringBuilder.append(file.getPath());
                Log.i(context, stringBuilder.toString());
            }
        }
    }

    private static java.io.File getDexDir(android.content.Context r2, java.io.File r3, java.lang.String r4) throws java.io.IOException {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = new java.io.File;
        r1 = "code_cache";
        r0.<init>(r3, r1);
        mkdirChecked(r0);	 Catch:{ IOException -> 0x000b }
        goto L_0x0019;
    L_0x000b:
        r0 = new java.io.File;
        r2 = r2.getFilesDir();
        r3 = "code_cache";
        r0.<init>(r2, r3);
        mkdirChecked(r0);
    L_0x0019:
        r2 = new java.io.File;
        r2.<init>(r0, r4);
        mkdirChecked(r2);
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.multidex.MultiDex.getDexDir(android.content.Context, java.io.File, java.lang.String):java.io.File");
    }

    private static void mkdirChecked(File file) throws IOException {
        file.mkdir();
        if (!file.isDirectory()) {
            StringBuilder stringBuilder;
            File parentFile = file.getParentFile();
            if (parentFile == null) {
                String str = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to create dir ");
                stringBuilder.append(file.getPath());
                stringBuilder.append(". Parent file is null.");
                Log.e(str, stringBuilder.toString());
            } else {
                String str2 = TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Failed to create dir ");
                stringBuilder2.append(file.getPath());
                stringBuilder2.append(". parent file is a dir ");
                stringBuilder2.append(parentFile.isDirectory());
                stringBuilder2.append(", a file ");
                stringBuilder2.append(parentFile.isFile());
                stringBuilder2.append(", exists ");
                stringBuilder2.append(parentFile.exists());
                stringBuilder2.append(", readable ");
                stringBuilder2.append(parentFile.canRead());
                stringBuilder2.append(", writable ");
                stringBuilder2.append(parentFile.canWrite());
                Log.e(str2, stringBuilder2.toString());
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to create directory ");
            stringBuilder.append(file.getPath());
            throw new IOException(stringBuilder.toString());
        }
    }
}
