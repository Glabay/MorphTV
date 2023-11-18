package ir.mahdi.mzip.rar;

import ir.mahdi.mzip.rar.io.IReadOnlyAccess;
import java.io.IOException;

public interface Volume {
    Archive getArchive();

    long getLength();

    IReadOnlyAccess getReadOnlyAccess() throws IOException;
}
