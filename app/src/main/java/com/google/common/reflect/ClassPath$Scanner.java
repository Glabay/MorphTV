package com.google.common.reflect;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.ImmutableSortedSet.Builder;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath.ResourceInfo;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import org.apache.commons.io.IOUtils;

@VisibleForTesting
final class ClassPath$Scanner {
    private final Builder<ResourceInfo> resources = new Builder(Ordering.usingToString());
    private final Set<URI> scannedUris = Sets.newHashSet();

    ClassPath$Scanner() {
    }

    ImmutableSortedSet<ResourceInfo> getResources() {
        return this.resources.build();
    }

    void scan(URI uri, ClassLoader classLoader) throws IOException {
        if (uri.getScheme().equals("file") && this.scannedUris.add(uri)) {
            scanFrom(new File(uri), classLoader);
        }
    }

    @VisibleForTesting
    void scanFrom(File file, ClassLoader classLoader) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                scanDirectory(file, classLoader);
            } else {
                scanJar(file, classLoader);
            }
        }
    }

    private void scanDirectory(File file, ClassLoader classLoader) throws IOException {
        scanDirectory(file, classLoader, "", ImmutableSet.of());
    }

    private void scanDirectory(File file, ClassLoader classLoader, String str, ImmutableSet<File> immutableSet) throws IOException {
        Object canonicalFile = file.getCanonicalFile();
        if (!immutableSet.contains(canonicalFile)) {
            File[] listFiles = file.listFiles();
            if (listFiles == null) {
                classLoader = ClassPath.access$100();
                str = new StringBuilder();
                str.append("Cannot read directory ");
                str.append(file);
                classLoader.warning(str.toString());
                return;
            }
            file = ImmutableSet.builder().addAll((Iterable) immutableSet).add(canonicalFile).build();
            for (File file2 : listFiles) {
                String name = file2.getName();
                if (file2.isDirectory()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(name);
                    stringBuilder.append("/");
                    scanDirectory(file2, classLoader, stringBuilder.toString(), file);
                } else {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(str);
                    stringBuilder2.append(name);
                    String stringBuilder3 = stringBuilder2.toString();
                    if (!stringBuilder3.equals("META-INF/MANIFEST.MF")) {
                        this.resources.add(ResourceInfo.of(stringBuilder3, classLoader));
                    }
                }
            }
        }
    }

    private void scanJar(java.io.File r5, java.lang.ClassLoader r6) throws java.io.IOException {
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
        r4 = this;
        r0 = new java.util.jar.JarFile;	 Catch:{ IOException -> 0x005b }
        r0.<init>(r5);	 Catch:{ IOException -> 0x005b }
        r1 = r0.getManifest();	 Catch:{ all -> 0x0056 }
        r5 = getClassPathFromManifest(r5, r1);	 Catch:{ all -> 0x0056 }
        r5 = r5.iterator();	 Catch:{ all -> 0x0056 }
    L_0x0011:
        r1 = r5.hasNext();	 Catch:{ all -> 0x0056 }
        if (r1 == 0) goto L_0x0021;	 Catch:{ all -> 0x0056 }
    L_0x0017:
        r1 = r5.next();	 Catch:{ all -> 0x0056 }
        r1 = (java.net.URI) r1;	 Catch:{ all -> 0x0056 }
        r4.scan(r1, r6);	 Catch:{ all -> 0x0056 }
        goto L_0x0011;	 Catch:{ all -> 0x0056 }
    L_0x0021:
        r5 = r0.entries();	 Catch:{ all -> 0x0056 }
    L_0x0025:
        r1 = r5.hasMoreElements();	 Catch:{ all -> 0x0056 }
        if (r1 == 0) goto L_0x0052;	 Catch:{ all -> 0x0056 }
    L_0x002b:
        r1 = r5.nextElement();	 Catch:{ all -> 0x0056 }
        r1 = (java.util.jar.JarEntry) r1;	 Catch:{ all -> 0x0056 }
        r2 = r1.isDirectory();	 Catch:{ all -> 0x0056 }
        if (r2 != 0) goto L_0x0025;	 Catch:{ all -> 0x0056 }
    L_0x0037:
        r2 = r1.getName();	 Catch:{ all -> 0x0056 }
        r3 = "META-INF/MANIFEST.MF";	 Catch:{ all -> 0x0056 }
        r2 = r2.equals(r3);	 Catch:{ all -> 0x0056 }
        if (r2 == 0) goto L_0x0044;	 Catch:{ all -> 0x0056 }
    L_0x0043:
        goto L_0x0025;	 Catch:{ all -> 0x0056 }
    L_0x0044:
        r2 = r4.resources;	 Catch:{ all -> 0x0056 }
        r1 = r1.getName();	 Catch:{ all -> 0x0056 }
        r1 = com.google.common.reflect.ClassPath.ResourceInfo.of(r1, r6);	 Catch:{ all -> 0x0056 }
        r2.add(r1);	 Catch:{ all -> 0x0056 }
        goto L_0x0025;
    L_0x0052:
        r0.close();	 Catch:{ IOException -> 0x0055 }
    L_0x0055:
        return;
    L_0x0056:
        r5 = move-exception;
        r0.close();	 Catch:{ IOException -> 0x005a }
    L_0x005a:
        throw r5;
    L_0x005b:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.reflect.ClassPath$Scanner.scanJar(java.io.File, java.lang.ClassLoader):void");
    }

    @com.google.common.annotations.VisibleForTesting
    static com.google.common.collect.ImmutableSet<java.net.URI> getClassPathFromManifest(java.io.File r5, @javax.annotation.Nullable java.util.jar.Manifest r6) {
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
        if (r6 != 0) goto L_0x0007;
    L_0x0002:
        r5 = com.google.common.collect.ImmutableSet.of();
        return r5;
    L_0x0007:
        r0 = com.google.common.collect.ImmutableSet.builder();
        r6 = r6.getMainAttributes();
        r1 = java.util.jar.Attributes.Name.CLASS_PATH;
        r1 = r1.toString();
        r6 = r6.getValue(r1);
        if (r6 == 0) goto L_0x0054;
    L_0x001b:
        r1 = com.google.common.reflect.ClassPath.access$200();
        r6 = r1.split(r6);
        r6 = r6.iterator();
    L_0x0027:
        r1 = r6.hasNext();
        if (r1 == 0) goto L_0x0054;
    L_0x002d:
        r1 = r6.next();
        r1 = (java.lang.String) r1;
        r2 = getClassPathEntry(r5, r1);	 Catch:{ URISyntaxException -> 0x003b }
        r0.add(r2);
        goto L_0x0027;
    L_0x003b:
        r2 = com.google.common.reflect.ClassPath.access$100();
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Invalid Class-Path entry: ";
        r3.append(r4);
        r3.append(r1);
        r1 = r3.toString();
        r2.warning(r1);
        goto L_0x0027;
    L_0x0054:
        r5 = r0.build();
        return r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.reflect.ClassPath$Scanner.getClassPathFromManifest(java.io.File, java.util.jar.Manifest):com.google.common.collect.ImmutableSet<java.net.URI>");
    }

    @VisibleForTesting
    static URI getClassPathEntry(File file, String str) throws URISyntaxException {
        URI uri = new URI(str);
        if (uri.isAbsolute()) {
            return uri;
        }
        return new File(file.getParentFile(), str.replace(IOUtils.DIR_SEPARATOR_UNIX, File.separatorChar)).toURI();
    }
}
