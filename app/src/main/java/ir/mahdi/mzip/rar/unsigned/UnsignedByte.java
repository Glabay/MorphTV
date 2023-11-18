package ir.mahdi.mzip.rar.unsigned;

public class UnsignedByte {
    public static short add(byte b, byte b2) {
        return (short) (b + b2);
    }

    public static byte intToByte(int i) {
        return (byte) (i & 255);
    }

    public static byte longToByte(long j) {
        return (byte) ((int) (j & 255));
    }

    public static byte shortToByte(short s) {
        return (byte) (s & 255);
    }

    public static short sub(byte b, byte b2) {
        return (short) (b - b2);
    }

    public static void main(String[] strArr) {
        System.out.println(add((byte) -2, (byte) 1));
        System.out.println(add((byte) -1, (byte) 1));
        System.out.println(add(Byte.MAX_VALUE, (byte) 1));
        System.out.println(add((byte) -1, (byte) -1));
        System.out.println(sub((byte) -2, (byte) 1));
        System.out.println(sub((byte) 0, (byte) 1));
        System.out.println(sub(Byte.MIN_VALUE, (byte) 1));
        System.out.println(1);
    }
}
