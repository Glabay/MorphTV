package net.lingala.zip4j.crypto.PBKDF2;

class PBKDF2HexFormatter {
    PBKDF2HexFormatter() {
    }

    public boolean fromString(PBKDF2Parameters pBKDF2Parameters, String str) {
        if (pBKDF2Parameters != null) {
            if (str != null) {
                str = str.split(":");
                if (str != null) {
                    if (str.length == 3) {
                        byte[] hex2bin = BinTools.hex2bin(str[0]);
                        int parseInt = Integer.parseInt(str[1]);
                        str = BinTools.hex2bin(str[2]);
                        pBKDF2Parameters.setSalt(hex2bin);
                        pBKDF2Parameters.setIterationCount(parseInt);
                        pBKDF2Parameters.setDerivedKey(str);
                        return false;
                    }
                }
                return true;
            }
        }
        return true;
    }

    public String toString(PBKDF2Parameters pBKDF2Parameters) {
        StringBuffer stringBuffer = new StringBuffer(String.valueOf(BinTools.bin2hex(pBKDF2Parameters.getSalt())));
        stringBuffer.append(":");
        stringBuffer.append(String.valueOf(pBKDF2Parameters.getIterationCount()));
        stringBuffer.append(":");
        stringBuffer.append(BinTools.bin2hex(pBKDF2Parameters.getDerivedKey()));
        return stringBuffer.toString();
    }
}
