package org.apache.commons.io;

import java.io.File;
import java.io.IOException;

public class FileDeleteStrategy {
    public static final FileDeleteStrategy FORCE = new ForceFileDeleteStrategy();
    public static final FileDeleteStrategy NORMAL = new FileDeleteStrategy("Normal");
    private final String name;

    static class ForceFileDeleteStrategy extends FileDeleteStrategy {
        ForceFileDeleteStrategy() {
            super("Force");
        }

        protected boolean doDelete(File file) throws IOException {
            FileUtils.forceDelete(file);
            return true;
        }
    }

    protected FileDeleteStrategy(String str) {
        this.name = str;
    }

    public boolean deleteQuietly(java.io.File r2) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r1 = this;
        if (r2 == 0) goto L_0x0010;
    L_0x0002:
        r0 = r2.exists();
        if (r0 != 0) goto L_0x0009;
    L_0x0008:
        goto L_0x0010;
    L_0x0009:
        r2 = r1.doDelete(r2);	 Catch:{ IOException -> 0x000e }
        return r2;
    L_0x000e:
        r2 = 0;
        return r2;
    L_0x0010:
        r2 = 1;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.FileDeleteStrategy.deleteQuietly(java.io.File):boolean");
    }

    public void delete(File file) throws IOException {
        if (file.exists() && !doDelete(file)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Deletion failed: ");
            stringBuilder.append(file);
            throw new IOException(stringBuilder.toString());
        }
    }

    protected boolean doDelete(File file) throws IOException {
        return file.delete();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FileDeleteStrategy[");
        stringBuilder.append(this.name);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
