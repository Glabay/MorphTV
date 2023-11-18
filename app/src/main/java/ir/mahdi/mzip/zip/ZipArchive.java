package ir.mahdi.mzip.zip;

import java.io.File;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;

public class ZipArchive {
    public static void zip(String str, String str2, String str3) {
        try {
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setCompressionMethod(8);
            zipParameters.setCompressionLevel(5);
            if (str3.length() > 0) {
                zipParameters.setEncryptFiles(true);
                zipParameters.setEncryptionMethod(99);
                zipParameters.setAesKeyStrength(3);
                zipParameters.setPassword(str3);
            }
            str3 = new ZipFile(str2);
            File file = new File(str);
            if (file.isFile() != null) {
                str3.addFile(file, zipParameters);
            } else if (file.isDirectory() != null) {
                str3.addFolder(file, zipParameters);
            }
        } catch (String str4) {
            str4.printStackTrace();
        }
    }

    public static void unzip(String str, String str2, String str3) {
        try {
            ZipFile zipFile = new ZipFile(str);
            if (zipFile.isEncrypted() != null) {
                zipFile.setPassword(str3);
            }
            zipFile.extractAll(str2);
        } catch (String str4) {
            str4.printStackTrace();
        }
    }
}
