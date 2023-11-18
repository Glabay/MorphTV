package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

@Beta
public final class ClassPath {
    private static final String CLASS_FILE_NAME_EXTENSION = ".class";
    private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(StringUtils.SPACE).omitEmptyStrings();
    private static final Predicate<ClassInfo> IS_TOP_LEVEL = new C11791();
    private static final Logger logger = Logger.getLogger(ClassPath.class.getName());
    private final ImmutableSet<ResourceInfo> resources;

    /* renamed from: com.google.common.reflect.ClassPath$1 */
    static class C11791 implements Predicate<ClassInfo> {
        C11791() {
        }

        public boolean apply(ClassInfo classInfo) {
            return classInfo.className.indexOf(36) == -1 ? true : null;
        }
    }

    @Beta
    public static class ResourceInfo {
        final ClassLoader loader;
        private final String resourceName;

        static ResourceInfo of(String str, ClassLoader classLoader) {
            if (str.endsWith(ClassPath.CLASS_FILE_NAME_EXTENSION)) {
                return new ClassInfo(str, classLoader);
            }
            return new ResourceInfo(str, classLoader);
        }

        ResourceInfo(String str, ClassLoader classLoader) {
            this.resourceName = (String) Preconditions.checkNotNull(str);
            this.loader = (ClassLoader) Preconditions.checkNotNull(classLoader);
        }

        public final URL url() {
            return (URL) Preconditions.checkNotNull(this.loader.getResource(this.resourceName), "Failed to load resource: %s", new Object[]{this.resourceName});
        }

        public final String getResourceName() {
            return this.resourceName;
        }

        public int hashCode() {
            return this.resourceName.hashCode();
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (!(obj instanceof ResourceInfo)) {
                return false;
            }
            ResourceInfo resourceInfo = (ResourceInfo) obj;
            if (this.resourceName.equals(resourceInfo.resourceName) && this.loader == resourceInfo.loader) {
                z = true;
            }
            return z;
        }

        public String toString() {
            return this.resourceName;
        }
    }

    @Beta
    public static final class ClassInfo extends ResourceInfo {
        private final String className;

        ClassInfo(String str, ClassLoader classLoader) {
            super(str, classLoader);
            this.className = ClassPath.getClassName(str);
        }

        public String getPackageName() {
            return Reflection.getPackageName(this.className);
        }

        public String getSimpleName() {
            int lastIndexOf = this.className.lastIndexOf(36);
            if (lastIndexOf != -1) {
                return CharMatcher.DIGIT.trimLeadingFrom(this.className.substring(lastIndexOf + 1));
            }
            String packageName = getPackageName();
            if (packageName.isEmpty()) {
                return this.className;
            }
            return this.className.substring(packageName.length() + 1);
        }

        public String getName() {
            return this.className;
        }

        public Class<?> load() {
            try {
                return this.loader.loadClass(this.className);
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public String toString() {
            return this.className;
        }
    }

    private ClassPath(ImmutableSet<ResourceInfo> immutableSet) {
        this.resources = immutableSet;
    }

    public static ClassPath from(ClassLoader classLoader) throws IOException {
        ClassPath$Scanner classPath$Scanner = new ClassPath$Scanner();
        classLoader = getClassPathEntries(classLoader).entrySet().iterator();
        while (classLoader.hasNext()) {
            Entry entry = (Entry) classLoader.next();
            classPath$Scanner.scan((URI) entry.getKey(), (ClassLoader) entry.getValue());
        }
        return new ClassPath(classPath$Scanner.getResources());
    }

    public ImmutableSet<ResourceInfo> getResources() {
        return this.resources;
    }

    public ImmutableSet<ClassInfo> getAllClasses() {
        return FluentIterable.from(this.resources).filter(ClassInfo.class).toSet();
    }

    public ImmutableSet<ClassInfo> getTopLevelClasses() {
        return FluentIterable.from(this.resources).filter(ClassInfo.class).filter(IS_TOP_LEVEL).toSet();
    }

    public ImmutableSet<ClassInfo> getTopLevelClasses(String str) {
        Preconditions.checkNotNull(str);
        Builder builder = ImmutableSet.builder();
        Iterator it = getTopLevelClasses().iterator();
        while (it.hasNext()) {
            ClassInfo classInfo = (ClassInfo) it.next();
            if (classInfo.getPackageName().equals(str)) {
                builder.add(classInfo);
            }
        }
        return builder.build();
    }

    public ImmutableSet<ClassInfo> getTopLevelClassesRecursive(String str) {
        Preconditions.checkNotNull(str);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append('.');
        str = stringBuilder.toString();
        Builder builder = ImmutableSet.builder();
        Iterator it = getTopLevelClasses().iterator();
        while (it.hasNext()) {
            ClassInfo classInfo = (ClassInfo) it.next();
            if (classInfo.getName().startsWith(str)) {
                builder.add(classInfo);
            }
        }
        return builder.build();
    }

    @VisibleForTesting
    static ImmutableMap<URI, ClassLoader> getClassPathEntries(ClassLoader classLoader) {
        Map newLinkedHashMap = Maps.newLinkedHashMap();
        ClassLoader parent = classLoader.getParent();
        if (parent != null) {
            newLinkedHashMap.putAll(getClassPathEntries(parent));
        }
        if (classLoader instanceof URLClassLoader) {
            URL[] uRLs = ((URLClassLoader) classLoader).getURLs();
            int length = uRLs.length;
            int i = 0;
            while (i < length) {
                try {
                    URI toURI = uRLs[i].toURI();
                    if (!newLinkedHashMap.containsKey(toURI)) {
                        newLinkedHashMap.put(toURI, classLoader);
                    }
                    i++;
                } catch (ClassLoader classLoader2) {
                    throw new IllegalArgumentException(classLoader2);
                }
            }
        }
        return ImmutableMap.copyOf(newLinkedHashMap);
    }

    @VisibleForTesting
    static String getClassName(String str) {
        return str.substring(0, str.length() - CLASS_FILE_NAME_EXTENSION.length()).replace(IOUtils.DIR_SEPARATOR_UNIX, '.');
    }
}
