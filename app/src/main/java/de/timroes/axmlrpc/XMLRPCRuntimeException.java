package de.timroes.axmlrpc;

public class XMLRPCRuntimeException extends RuntimeException {
    public XMLRPCRuntimeException(String str) {
        super(str);
    }

    public XMLRPCRuntimeException(Exception exception) {
        super(exception);
    }
}
