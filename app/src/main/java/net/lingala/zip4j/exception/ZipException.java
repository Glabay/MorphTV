package net.lingala.zip4j.exception;

public class ZipException extends Exception {
    private static final long serialVersionUID = 1;
    private int code = -1;

    public ZipException(String str) {
        super(str);
    }

    public ZipException(String str, Throwable th) {
        super(str, th);
    }

    public ZipException(String str, int i) {
        super(str);
        this.code = i;
    }

    public ZipException(String str, Throwable th, int i) {
        super(str, th);
        this.code = i;
    }

    public ZipException(Throwable th) {
        super(th);
    }

    public ZipException(Throwable th, int i) {
        super(th);
        this.code = i;
    }

    public int getCode() {
        return this.code;
    }
}
