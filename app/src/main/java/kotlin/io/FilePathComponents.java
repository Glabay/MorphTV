package kotlin.io;

import java.io.File;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\r\b\b\u0018\u00002\u00020\u0001B\u001d\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\u000f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005HÆ\u0003J#\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005HÆ\u0001J\u0013\u0010\u0019\u001a\u00020\b2\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001b\u001a\u00020\u0013HÖ\u0001J\u0016\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u001d\u001a\u00020\u00132\u0006\u0010\u001e\u001a\u00020\u0013J\t\u0010\u001f\u001a\u00020\rHÖ\u0001R\u0011\u0010\u0007\u001a\u00020\b8F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\r8F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0012\u001a\u00020\u00138F¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015¨\u0006 "}, d2 = {"Lkotlin/io/FilePathComponents;", "", "root", "Ljava/io/File;", "segments", "", "(Ljava/io/File;Ljava/util/List;)V", "isRooted", "", "()Z", "getRoot", "()Ljava/io/File;", "rootName", "", "getRootName", "()Ljava/lang/String;", "getSegments", "()Ljava/util/List;", "size", "", "getSize", "()I", "component1", "component2", "copy", "equals", "other", "hashCode", "subPath", "beginIndex", "endIndex", "toString", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
/* compiled from: FilePathComponents.kt */
public final class FilePathComponents {
    @NotNull
    private final File root;
    @NotNull
    private final List<File> segments;

    @NotNull
    public static /* synthetic */ FilePathComponents copy$default(FilePathComponents filePathComponents, File file, List list, int i, Object obj) {
        if ((i & 1) != 0) {
            file = filePathComponents.root;
        }
        if ((i & 2) != 0) {
            list = filePathComponents.segments;
        }
        return filePathComponents.copy(file, list);
    }

    @NotNull
    public final File component1() {
        return this.root;
    }

    @NotNull
    public final List<File> component2() {
        return this.segments;
    }

    @NotNull
    public final FilePathComponents copy(@NotNull File file, @NotNull List<? extends File> list) {
        Intrinsics.checkParameterIsNotNull(file, "root");
        Intrinsics.checkParameterIsNotNull(list, "segments");
        return new FilePathComponents(file, list);
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof FilePathComponents) {
                FilePathComponents filePathComponents = (FilePathComponents) obj;
                if (Intrinsics.areEqual(this.root, filePathComponents.root) && Intrinsics.areEqual(this.segments, filePathComponents.segments)) {
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        File file = this.root;
        int i = 0;
        int hashCode = (file != null ? file.hashCode() : 0) * 31;
        List list = this.segments;
        if (list != null) {
            i = list.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FilePathComponents(root=");
        stringBuilder.append(this.root);
        stringBuilder.append(", segments=");
        stringBuilder.append(this.segments);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public FilePathComponents(@NotNull File file, @NotNull List<? extends File> list) {
        Intrinsics.checkParameterIsNotNull(file, "root");
        Intrinsics.checkParameterIsNotNull(list, "segments");
        this.root = file;
        this.segments = list;
    }

    @NotNull
    public final File getRoot() {
        return this.root;
    }

    @NotNull
    public final List<File> getSegments() {
        return this.segments;
    }

    @NotNull
    public final String getRootName() {
        String path = this.root.getPath();
        Intrinsics.checkExpressionValueIsNotNull(path, "root.path");
        return path;
    }

    public final boolean isRooted() {
        String path = this.root.getPath();
        Intrinsics.checkExpressionValueIsNotNull(path, "root.path");
        return ((CharSequence) path).length() > 0;
    }

    public final int getSize() {
        return this.segments.size();
    }

    @NotNull
    public final File subPath(int i, int i2) {
        if (i >= 0 && i <= i2) {
            if (i2 <= getSize()) {
                Iterable subList = this.segments.subList(i, i2);
                i = File.separator;
                Intrinsics.checkExpressionValueIsNotNull(i, "File.separator");
                return new File(CollectionsKt___CollectionsKt.joinToString$default(subList, (CharSequence) i, null, null, 0, null, null, 62, null));
            }
        }
        throw ((Throwable) new IllegalArgumentException());
    }
}
