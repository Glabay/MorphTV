package ir.mahdi.mzip.rar.exception;

public class RarException extends Exception {
    private static final long serialVersionUID = 1;
    private RarExceptionType type;

    public enum RarExceptionType {
        notImplementedYet,
        crcError,
        notRarArchive,
        badRarArchive,
        unkownError,
        headerNotInArchive,
        wrongHeaderType,
        ioError,
        rarEncryptedException
    }

    public RarException(Exception exception) {
        super(RarExceptionType.unkownError.name(), exception);
        this.type = RarExceptionType.unkownError;
    }

    public RarException(RarException rarException) {
        super(rarException.getMessage(), rarException);
        this.type = rarException.getType();
    }

    public RarException(RarExceptionType rarExceptionType) {
        super(rarExceptionType.name());
        this.type = rarExceptionType;
    }

    public RarExceptionType getType() {
        return this.type;
    }

    public void setType(RarExceptionType rarExceptionType) {
        this.type = rarExceptionType;
    }
}
