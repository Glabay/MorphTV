package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;
import com.google.common.primitives.Ints;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

@Beta
public final class InetAddresses {
    private static final Inet4Address ANY4 = ((Inet4Address) forString("0.0.0.0"));
    private static final int IPV4_PART_COUNT = 4;
    private static final int IPV6_PART_COUNT = 8;
    private static final Inet4Address LOOPBACK4 = ((Inet4Address) forString("127.0.0.1"));

    private InetAddresses() {
    }

    private static Inet4Address getInet4Address(byte[] bArr) {
        Preconditions.checkArgument(bArr.length == 4, "Byte array has invalid length for an IPv4 address: %s != 4.", new Object[]{Integer.valueOf(bArr.length)});
        return (Inet4Address) bytesToInetAddress(bArr);
    }

    public static InetAddress forString(String str) {
        byte[] ipStringToBytes = ipStringToBytes(str);
        if (ipStringToBytes != null) {
            return bytesToInetAddress(ipStringToBytes);
        }
        throw new IllegalArgumentException(String.format("'%s' is not an IP string literal.", new Object[]{str}));
    }

    public static boolean isInetAddress(String str) {
        return ipStringToBytes(str) != null ? true : null;
    }

    private static byte[] ipStringToBytes(String str) {
        Object obj = null;
        Object obj2 = null;
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt == '.') {
                obj2 = 1;
            } else if (charAt == ':') {
                if (obj2 != null) {
                    return null;
                }
                obj = 1;
            } else if (Character.digit(charAt, 16) == -1) {
                return null;
            }
        }
        if (obj != null) {
            if (obj2 != null) {
                str = convertDottedQuadToHex(str);
                if (str == null) {
                    return null;
                }
            }
            return textToNumericFormatV6(str);
        } else if (obj2 != null) {
            return textToNumericFormatV4(str);
        } else {
            return null;
        }
    }

    private static byte[] textToNumericFormatV4(java.lang.String r4) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = "\\.";
        r1 = 5;
        r4 = r4.split(r0, r1);
        r0 = r4.length;
        r1 = 0;
        r2 = 4;
        if (r0 == r2) goto L_0x000d;
    L_0x000c:
        return r1;
    L_0x000d:
        r0 = new byte[r2];
        r2 = 0;
    L_0x0010:
        r3 = r0.length;	 Catch:{ NumberFormatException -> 0x001f }
        if (r2 >= r3) goto L_0x001e;	 Catch:{ NumberFormatException -> 0x001f }
    L_0x0013:
        r3 = r4[r2];	 Catch:{ NumberFormatException -> 0x001f }
        r3 = parseOctet(r3);	 Catch:{ NumberFormatException -> 0x001f }
        r0[r2] = r3;	 Catch:{ NumberFormatException -> 0x001f }
        r2 = r2 + 1;
        goto L_0x0010;
    L_0x001e:
        return r0;
    L_0x001f:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.net.InetAddresses.textToNumericFormatV4(java.lang.String):byte[]");
    }

    private static byte[] textToNumericFormatV6(java.lang.String r8) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = ":";
        r1 = 10;
        r8 = r8.split(r0, r1);
        r0 = r8.length;
        r1 = 0;
        r2 = 3;
        if (r0 < r2) goto L_0x0090;
    L_0x000d:
        r0 = r8.length;
        r2 = 9;
        if (r0 <= r2) goto L_0x0014;
    L_0x0012:
        goto L_0x0090;
    L_0x0014:
        r0 = -1;
        r2 = 1;
        r0 = 1;
        r3 = -1;
    L_0x0018:
        r4 = r8.length;
        r4 = r4 - r2;
        if (r0 >= r4) goto L_0x002b;
    L_0x001c:
        r4 = r8[r0];
        r4 = r4.length();
        if (r4 != 0) goto L_0x0028;
    L_0x0024:
        if (r3 < 0) goto L_0x0027;
    L_0x0026:
        return r1;
    L_0x0027:
        r3 = r0;
    L_0x0028:
        r0 = r0 + 1;
        goto L_0x0018;
    L_0x002b:
        r0 = 0;
        if (r3 < 0) goto L_0x004e;
    L_0x002e:
        r4 = r8.length;
        r4 = r4 - r3;
        r4 = r4 - r2;
        r5 = r8[r0];
        r5 = r5.length();
        if (r5 != 0) goto L_0x003e;
    L_0x0039:
        r5 = r3 + -1;
        if (r5 == 0) goto L_0x003f;
    L_0x003d:
        return r1;
    L_0x003e:
        r5 = r3;
    L_0x003f:
        r6 = r8.length;
        r6 = r6 - r2;
        r6 = r8[r6];
        r6 = r6.length();
        if (r6 != 0) goto L_0x0050;
    L_0x0049:
        r4 = r4 + -1;
        if (r4 == 0) goto L_0x0050;
    L_0x004d:
        return r1;
    L_0x004e:
        r5 = r8.length;
        r4 = 0;
    L_0x0050:
        r6 = r5 + r4;
        r6 = 8 - r6;
        if (r3 < 0) goto L_0x0059;
    L_0x0056:
        if (r6 < r2) goto L_0x005b;
    L_0x0058:
        goto L_0x005c;
    L_0x0059:
        if (r6 == 0) goto L_0x005c;
    L_0x005b:
        return r1;
    L_0x005c:
        r2 = 16;
        r2 = java.nio.ByteBuffer.allocate(r2);
        r3 = 0;
    L_0x0063:
        if (r3 >= r5) goto L_0x0071;
    L_0x0065:
        r7 = r8[r3];	 Catch:{ NumberFormatException -> 0x008a }
        r7 = parseHextet(r7);	 Catch:{ NumberFormatException -> 0x008a }
        r2.putShort(r7);	 Catch:{ NumberFormatException -> 0x008a }
        r3 = r3 + 1;	 Catch:{ NumberFormatException -> 0x008a }
        goto L_0x0063;	 Catch:{ NumberFormatException -> 0x008a }
    L_0x0071:
        r3 = 0;	 Catch:{ NumberFormatException -> 0x008a }
    L_0x0072:
        if (r3 >= r6) goto L_0x007a;	 Catch:{ NumberFormatException -> 0x008a }
    L_0x0074:
        r2.putShort(r0);	 Catch:{ NumberFormatException -> 0x008a }
        r3 = r3 + 1;	 Catch:{ NumberFormatException -> 0x008a }
        goto L_0x0072;	 Catch:{ NumberFormatException -> 0x008a }
    L_0x007a:
        if (r4 <= 0) goto L_0x008b;	 Catch:{ NumberFormatException -> 0x008a }
    L_0x007c:
        r0 = r8.length;	 Catch:{ NumberFormatException -> 0x008a }
        r0 = r0 - r4;	 Catch:{ NumberFormatException -> 0x008a }
        r0 = r8[r0];	 Catch:{ NumberFormatException -> 0x008a }
        r0 = parseHextet(r0);	 Catch:{ NumberFormatException -> 0x008a }
        r2.putShort(r0);	 Catch:{ NumberFormatException -> 0x008a }
        r4 = r4 + -1;
        goto L_0x007a;
    L_0x008a:
        return r1;
    L_0x008b:
        r8 = r2.array();
        return r8;
    L_0x0090:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.net.InetAddresses.textToNumericFormatV6(java.lang.String):byte[]");
    }

    private static String convertDottedQuadToHex(String str) {
        int lastIndexOf = str.lastIndexOf(58) + 1;
        String substring = str.substring(0, lastIndexOf);
        str = textToNumericFormatV4(str.substring(lastIndexOf));
        if (str == null) {
            return null;
        }
        String toHexString = Integer.toHexString(((str[0] & 255) << 8) | (str[1] & 255));
        str = Integer.toHexString((str[3] & 255) | ((str[2] & 255) << 8));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(substring);
        stringBuilder.append(toHexString);
        stringBuilder.append(":");
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    private static byte parseOctet(String str) {
        int parseInt = Integer.parseInt(str);
        if (parseInt <= 255) {
            if (!str.startsWith("0") || str.length() <= 1) {
                return (byte) parseInt;
            }
        }
        throw new NumberFormatException();
    }

    private static short parseHextet(String str) {
        str = Integer.parseInt(str, 16);
        if (str <= 65535) {
            return (short) str;
        }
        throw new NumberFormatException();
    }

    private static InetAddress bytesToInetAddress(byte[] bArr) {
        try {
            return InetAddress.getByAddress(bArr);
        } catch (byte[] bArr2) {
            throw new AssertionError(bArr2);
        }
    }

    public static String toAddrString(InetAddress inetAddress) {
        Preconditions.checkNotNull(inetAddress);
        if (inetAddress instanceof Inet4Address) {
            return inetAddress.getHostAddress();
        }
        Preconditions.checkArgument(inetAddress instanceof Inet6Address);
        inetAddress = inetAddress.getAddress();
        int[] iArr = new int[8];
        for (int i = 0; i < iArr.length; i++) {
            int i2 = i * 2;
            iArr[i] = Ints.fromBytes((byte) 0, (byte) 0, inetAddress[i2], inetAddress[i2 + 1]);
        }
        compressLongestRunOfZeroes(iArr);
        return hextetsToIPv6String(iArr);
    }

    private static void compressLongestRunOfZeroes(int[] iArr) {
        int i = 0;
        int i2 = -1;
        int i3 = -1;
        int i4 = -1;
        while (i < iArr.length + 1) {
            if (i >= iArr.length || iArr[i] != 0) {
                if (i3 >= 0) {
                    int i5 = i - i3;
                    if (i5 > i2) {
                        i2 = i5;
                    } else {
                        i3 = i4;
                    }
                    i4 = i3;
                    i3 = -1;
                }
            } else if (i3 < 0) {
                i3 = i;
            }
            i++;
        }
        if (i2 >= 2) {
            Arrays.fill(iArr, i4, i2 + i4, -1);
        }
    }

    private static String hextetsToIPv6String(int[] iArr) {
        StringBuilder stringBuilder = new StringBuilder(39);
        int i = 0;
        Object obj = null;
        while (i < iArr.length) {
            Object obj2 = iArr[i] >= 0 ? 1 : null;
            if (obj2 != null) {
                if (obj != null) {
                    stringBuilder.append(':');
                }
                stringBuilder.append(Integer.toHexString(iArr[i]));
            } else if (i == 0 || obj != null) {
                stringBuilder.append("::");
            }
            i++;
            obj = obj2;
        }
        return stringBuilder.toString();
    }

    public static String toUriString(InetAddress inetAddress) {
        if (!(inetAddress instanceof Inet6Address)) {
            return toAddrString(inetAddress);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(toAddrString(inetAddress));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public static InetAddress forUriString(String str) {
        String substring;
        int i;
        Preconditions.checkNotNull(str);
        if (str.startsWith("[") && str.endsWith("]")) {
            substring = str.substring(1, str.length() - 1);
            i = 16;
        } else {
            i = 4;
            substring = str;
        }
        byte[] ipStringToBytes = ipStringToBytes(substring);
        if (ipStringToBytes != null) {
            if (ipStringToBytes.length == i) {
                return bytesToInetAddress(ipStringToBytes);
            }
        }
        throw new IllegalArgumentException(String.format("Not a valid URI IP literal: '%s'", new Object[]{str}));
    }

    public static boolean isUriInetAddress(java.lang.String r0) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        forUriString(r0);	 Catch:{ IllegalArgumentException -> 0x0005 }
        r0 = 1;
        return r0;
    L_0x0005:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.net.InetAddresses.isUriInetAddress(java.lang.String):boolean");
    }

    public static boolean isCompatIPv4Address(Inet6Address inet6Address) {
        if (!inet6Address.isIPv4CompatibleAddress()) {
            return false;
        }
        inet6Address = inet6Address.getAddress();
        if (inet6Address[12] == (byte) 0 && inet6Address[13] == (byte) 0 && inet6Address[14] == (byte) 0 && (inet6Address[15] == (byte) 0 || inet6Address[15] == 1)) {
            return false;
        }
        return true;
    }

    public static Inet4Address getCompatIPv4Address(Inet6Address inet6Address) {
        Preconditions.checkArgument(isCompatIPv4Address(inet6Address), "Address '%s' is not IPv4-compatible.", new Object[]{toAddrString(inet6Address)});
        return getInet4Address(Arrays.copyOfRange(inet6Address.getAddress(), 12, 16));
    }

    public static boolean is6to4Address(Inet6Address inet6Address) {
        inet6Address = inet6Address.getAddress();
        if (inet6Address[0] == (byte) 32 && inet6Address[1] == 2) {
            return true;
        }
        return false;
    }

    public static Inet4Address get6to4IPv4Address(Inet6Address inet6Address) {
        Preconditions.checkArgument(is6to4Address(inet6Address), "Address '%s' is not a 6to4 address.", new Object[]{toAddrString(inet6Address)});
        return getInet4Address(Arrays.copyOfRange(inet6Address.getAddress(), 2, 6));
    }

    public static boolean isTeredoAddress(Inet6Address inet6Address) {
        inet6Address = inet6Address.getAddress();
        if (inet6Address[0] == (byte) 32 && inet6Address[1] == (byte) 1 && inet6Address[2] == (byte) 0 && inet6Address[3] == null) {
            return true;
        }
        return false;
    }

    public static InetAddresses$TeredoInfo getTeredoInfo(Inet6Address inet6Address) {
        Object[] objArr = new Object[1];
        int i = 0;
        objArr[0] = toAddrString(inet6Address);
        Preconditions.checkArgument(isTeredoAddress(inet6Address), "Address '%s' is not a Teredo address.", objArr);
        inet6Address = inet6Address.getAddress();
        Inet4Address inet4Address = getInet4Address(Arrays.copyOfRange(inet6Address, 4, 8));
        int readShort = ByteStreams.newDataInput(inet6Address, 8).readShort() & 65535;
        int readShort2 = 65535 & (ByteStreams.newDataInput(inet6Address, 10).readShort() ^ -1);
        inet6Address = Arrays.copyOfRange(inet6Address, 12, 16);
        while (i < inet6Address.length) {
            inet6Address[i] = (byte) (inet6Address[i] ^ -1);
            i++;
        }
        return new InetAddresses$TeredoInfo(inet4Address, getInet4Address(inet6Address), readShort2, readShort);
    }

    public static boolean isIsatapAddress(Inet6Address inet6Address) {
        boolean z = false;
        if (isTeredoAddress(inet6Address)) {
            return false;
        }
        inet6Address = inet6Address.getAddress();
        if ((inet6Address[8] | 3) != 3) {
            return false;
        }
        if (inet6Address[9] == (byte) 0 && inet6Address[10] == (byte) 94 && inet6Address[11] == -2) {
            z = true;
        }
        return z;
    }

    public static Inet4Address getIsatapIPv4Address(Inet6Address inet6Address) {
        Preconditions.checkArgument(isIsatapAddress(inet6Address), "Address '%s' is not an ISATAP address.", new Object[]{toAddrString(inet6Address)});
        return getInet4Address(Arrays.copyOfRange(inet6Address.getAddress(), 12, 16));
    }

    public static boolean hasEmbeddedIPv4ClientAddress(Inet6Address inet6Address) {
        if (!(isCompatIPv4Address(inet6Address) || is6to4Address(inet6Address))) {
            if (isTeredoAddress(inet6Address) == null) {
                return null;
            }
        }
        return true;
    }

    public static Inet4Address getEmbeddedIPv4ClientAddress(Inet6Address inet6Address) {
        if (isCompatIPv4Address(inet6Address)) {
            return getCompatIPv4Address(inet6Address);
        }
        if (is6to4Address(inet6Address)) {
            return get6to4IPv4Address(inet6Address);
        }
        if (isTeredoAddress(inet6Address)) {
            return getTeredoInfo(inet6Address).getClient();
        }
        throw new IllegalArgumentException(String.format("'%s' has no embedded IPv4 address.", new Object[]{toAddrString(inet6Address)}));
    }

    public static boolean isMappedIPv4Address(String str) {
        str = ipStringToBytes(str);
        if (str == null || str.length != 16) {
            return false;
        }
        int i = 0;
        while (true) {
            int i2 = 10;
            if (i >= 10) {
                break;
            } else if (str[i] != (byte) 0) {
                return false;
            } else {
                i++;
            }
        }
        while (i2 < 12) {
            if (str[i2] != (byte) -1) {
                return false;
            }
            i2++;
        }
        return true;
    }

    public static Inet4Address getCoercedIPv4Address(InetAddress inetAddress) {
        if (inetAddress instanceof Inet4Address) {
            return (Inet4Address) inetAddress;
        }
        Object obj;
        byte[] address = inetAddress.getAddress();
        for (int i = 0; i < 15; i++) {
            if (address[i] != (byte) 0) {
                obj = null;
                break;
            }
        }
        obj = 1;
        if (obj != null && address[15] == (byte) 1) {
            return LOOPBACK4;
        }
        if (obj != null && address[15] == (byte) 0) {
            return ANY4;
        }
        long hashCode;
        Inet6Address inet6Address = (Inet6Address) inetAddress;
        if (hasEmbeddedIPv4ClientAddress(inet6Address)) {
            hashCode = (long) getEmbeddedIPv4ClientAddress(inet6Address).hashCode();
        } else {
            hashCode = ByteBuffer.wrap(inet6Address.getAddress(), 0, 8).getLong();
        }
        inetAddress = Hashing.murmur3_32().hashLong(hashCode).asInt() | -536870912;
        if (inetAddress == -1) {
            inetAddress = -2;
        }
        return getInet4Address(Ints.toByteArray(inetAddress));
    }

    public static int coerceToInteger(InetAddress inetAddress) {
        return ByteStreams.newDataInput(getCoercedIPv4Address(inetAddress).getAddress()).readInt();
    }

    public static Inet4Address fromInteger(int i) {
        return getInet4Address(Ints.toByteArray(i));
    }

    public static InetAddress fromLittleEndianByteArray(byte[] bArr) throws UnknownHostException {
        byte[] bArr2 = new byte[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr2[i] = bArr[(bArr.length - i) - 1];
        }
        return InetAddress.getByAddress(bArr2);
    }

    public static InetAddress decrement(InetAddress inetAddress) {
        byte[] address = inetAddress.getAddress();
        int length = address.length - 1;
        while (length >= 0 && address[length] == (byte) 0) {
            address[length] = (byte) -1;
            length--;
        }
        Preconditions.checkArgument(length >= 0, "Decrementing %s would wrap.", new Object[]{inetAddress});
        address[length] = (byte) (address[length] - 1);
        return bytesToInetAddress(address);
    }

    public static InetAddress increment(InetAddress inetAddress) {
        byte[] address = inetAddress.getAddress();
        int length = address.length - 1;
        while (length >= 0 && address[length] == (byte) -1) {
            address[length] = (byte) 0;
            length--;
        }
        Preconditions.checkArgument(length >= 0, "Incrementing %s would wrap.", new Object[]{inetAddress});
        address[length] = (byte) (address[length] + 1);
        return bytesToInetAddress(address);
    }

    public static boolean isMaximum(InetAddress inetAddress) {
        inetAddress = inetAddress.getAddress();
        for (byte b : inetAddress) {
            if (b != (byte) -1) {
                return false;
            }
        }
        return true;
    }
}
