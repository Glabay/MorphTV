package ir.mahdi.mzip.rar;

import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.impl.FileVolumeManager;
import ir.mahdi.mzip.rar.rarfile.FileHeader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MVTest {
    public static void main(String[] strArr) {
        FileHeader nextFileHeader;
        StringBuilder stringBuilder;
        File file;
        OutputStream fileOutputStream;
        try {
            strArr = new Archive(new FileVolumeManager(new File("/home/rogiel/fs/home/ae721273-eade-45e7-8112-d14115ebae56/Village People - Y.M.C.A.mp3.part1.rar")));
        } catch (String[] strArr2) {
            strArr2.printStackTrace();
            strArr2 = null;
            if (strArr2 == null) {
                strArr2.getMainHeader().print();
                for (nextFileHeader = strArr2.nextFileHeader(); nextFileHeader != null; nextFileHeader = strArr2.nextFileHeader()) {
                    try {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("/home/rogiel/fs/test/");
                        stringBuilder.append(nextFileHeader.getFileNameString().trim());
                        file = new File(stringBuilder.toString());
                        System.out.println(file.getAbsolutePath());
                        fileOutputStream = new FileOutputStream(file);
                        strArr2.extractFile(nextFileHeader, fileOutputStream);
                        fileOutputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (RarException e2) {
                        e2.printStackTrace();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            }
        } catch (String[] strArr22) {
            strArr22.printStackTrace();
            strArr22 = null;
            if (strArr22 == null) {
                strArr22.getMainHeader().print();
                for (nextFileHeader = strArr22.nextFileHeader(); nextFileHeader != null; nextFileHeader = strArr22.nextFileHeader()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("/home/rogiel/fs/test/");
                    stringBuilder.append(nextFileHeader.getFileNameString().trim());
                    file = new File(stringBuilder.toString());
                    System.out.println(file.getAbsolutePath());
                    fileOutputStream = new FileOutputStream(file);
                    strArr22.extractFile(nextFileHeader, fileOutputStream);
                    fileOutputStream.close();
                }
            }
        }
        if (strArr22 == null) {
            strArr22.getMainHeader().print();
            for (nextFileHeader = strArr22.nextFileHeader(); nextFileHeader != null; nextFileHeader = strArr22.nextFileHeader()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("/home/rogiel/fs/test/");
                stringBuilder.append(nextFileHeader.getFileNameString().trim());
                file = new File(stringBuilder.toString());
                System.out.println(file.getAbsolutePath());
                fileOutputStream = new FileOutputStream(file);
                strArr22.extractFile(nextFileHeader, fileOutputStream);
                fileOutputStream.close();
            }
        }
    }
}
