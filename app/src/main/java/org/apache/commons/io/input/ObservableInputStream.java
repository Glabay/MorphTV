package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ObservableInputStream extends ProxyInputStream {
    private final List<Observer> observers = new ArrayList();

    public static abstract class Observer {
        void closed() throws IOException {
        }

        void data(int i) throws IOException {
        }

        void data(byte[] bArr, int i, int i2) throws IOException {
        }

        void finished() throws IOException {
        }

        void error(IOException iOException) throws IOException {
            throw iOException;
        }
    }

    public ObservableInputStream(InputStream inputStream) {
        super(inputStream);
    }

    public void add(Observer observer) {
        this.observers.add(observer);
    }

    public void remove(Observer observer) {
        this.observers.remove(observer);
    }

    public void removeAllObservers() {
        this.observers.clear();
    }

    public int read() throws IOException {
        int read;
        IOException iOException;
        try {
            read = super.read();
            iOException = null;
        } catch (IOException e) {
            iOException = e;
            read = 0;
        }
        if (iOException != null) {
            noteError(iOException);
        } else if (read == -1) {
            noteFinished();
        } else {
            noteDataByte(read);
        }
        return read;
    }

    public int read(byte[] bArr) throws IOException {
        int read;
        IOException iOException;
        try {
            read = super.read(bArr);
            iOException = null;
        } catch (IOException e) {
            iOException = e;
            read = 0;
        }
        if (iOException != null) {
            noteError(iOException);
        } else if (read == -1) {
            noteFinished();
        } else if (read > 0) {
            noteDataBytes(bArr, 0, read);
        }
        return read;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        IOException iOException;
        try {
            i2 = super.read(bArr, i, i2);
            iOException = null;
        } catch (IOException e) {
            iOException = e;
            i2 = 0;
        }
        if (iOException != null) {
            noteError(iOException);
        } else if (i2 == -1) {
            noteFinished();
        } else if (i2 > 0) {
            noteDataBytes(bArr, i, i2);
        }
        return i2;
    }

    protected void noteDataBytes(byte[] bArr, int i, int i2) throws IOException {
        for (Observer data : getObservers()) {
            data.data(bArr, i, i2);
        }
    }

    protected void noteFinished() throws IOException {
        for (Observer finished : getObservers()) {
            finished.finished();
        }
    }

    protected void noteDataByte(int i) throws IOException {
        for (Observer data : getObservers()) {
            data.data(i);
        }
    }

    protected void noteError(IOException iOException) throws IOException {
        for (Observer error : getObservers()) {
            error.error(iOException);
        }
    }

    protected void noteClosed() throws IOException {
        for (Observer closed : getObservers()) {
            closed.closed();
        }
    }

    protected List<Observer> getObservers() {
        return this.observers;
    }

    public void close() throws IOException {
        IOException iOException;
        try {
            super.close();
            iOException = null;
        } catch (IOException e) {
            iOException = e;
        }
        if (iOException == null) {
            noteClosed();
        } else {
            noteError(iOException);
        }
    }

    public void consume() throws IOException {
        do {
        } while (read(new byte[8192]) != -1);
    }
}
