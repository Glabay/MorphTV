package de.timroes.axmlrpc;

public class XMLRPCException extends Exception {
    public XMLRPCException(Exception exception) {
        super(exception);
    }

    public XMLRPCException(String str) {
        super(str);
    }

    public XMLRPCException(String str, Exception exception) {
        super(str, exception);
    }
}
