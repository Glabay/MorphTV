package de.timroes.axmlrpc;

public class XMLRPCServerException extends XMLRPCException {
    private int errornr;

    public XMLRPCServerException(String str, int i) {
        super(str);
        this.errornr = i;
    }

    public String getMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.getMessage());
        stringBuilder.append(" [");
        stringBuilder.append(this.errornr);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public int getErrorNr() {
        return this.errornr;
    }
}
