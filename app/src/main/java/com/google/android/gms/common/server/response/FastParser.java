package com.google.android.gms.common.server.response;

import com.google.android.exoplayer2.C0649C;
import com.google.android.gms.common.server.response.FastJsonResponse.Field;
import com.google.android.gms.common.util.Base64Utils;
import com.google.android.gms.common.util.JsonUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import kotlin.text.Typography;
import org.apache.commons.io.IOUtils;

public class FastParser<T extends FastJsonResponse> {
    private static final char[] zzwv = new char[]{'u', 'l', 'l'};
    private static final char[] zzww = new char[]{'r', 'u', 'e'};
    private static final char[] zzwx = new char[]{'r', 'u', 'e', Typography.quote};
    private static final char[] zzwy = new char[]{'a', 'l', 's', 'e'};
    private static final char[] zzwz = new char[]{'a', 'l', 's', 'e', Typography.quote};
    private static final char[] zzxa = new char[]{'\n'};
    private static final zza<Integer> zzxc = new zza();
    private static final zza<Long> zzxd = new zzb();
    private static final zza<Float> zzxe = new zzc();
    private static final zza<Double> zzxf = new zzd();
    private static final zza<Boolean> zzxg = new zze();
    private static final zza<String> zzxh = new zzf();
    private static final zza<BigInteger> zzxi = new zzg();
    private static final zza<BigDecimal> zzxj = new zzh();
    private final char[] zzwq = new char[1];
    private final char[] zzwr = new char[32];
    private final char[] zzws = new char[1024];
    private final StringBuilder zzwt = new StringBuilder(32);
    private final StringBuilder zzwu = new StringBuilder(1024);
    private final Stack<Integer> zzxb = new Stack();

    public static class ParseException extends Exception {
        public ParseException(String str) {
            super(str);
        }

        public ParseException(String str, Throwable th) {
            super(str, th);
        }

        public ParseException(Throwable th) {
            super(th);
        }
    }

    private interface zza<O> {
        O zzh(FastParser fastParser, BufferedReader bufferedReader) throws ParseException, IOException;
    }

    private final int zza(BufferedReader bufferedReader, char[] cArr) throws ParseException, IOException {
        char zzj = zzj(bufferedReader);
        if (zzj == '\u0000') {
            throw new ParseException("Unexpected EOF");
        } else if (zzj == ',') {
            throw new ParseException("Missing value");
        } else if (zzj == 'n') {
            zzb(bufferedReader, zzwv);
            return 0;
        } else {
            int i;
            bufferedReader.mark(1024);
            if (zzj == Typography.quote) {
                i = 0;
                int i2 = 0;
                while (i < cArr.length && bufferedReader.read(cArr, i, 1) != -1) {
                    char c = cArr[i];
                    if (Character.isISOControl(c)) {
                        throw new ParseException("Unexpected control character while reading string");
                    } else if (c == Typography.quote && i2 == 0) {
                        bufferedReader.reset();
                        bufferedReader.skip((long) (i + 1));
                        return i;
                    } else {
                        i2 = c == IOUtils.DIR_SEPARATOR_WINDOWS ? i2 ^ 1 : 0;
                        i++;
                    }
                }
            } else {
                cArr[0] = zzj;
                i = 1;
                while (i < cArr.length && bufferedReader.read(cArr, i, 1) != -1) {
                    if (!(cArr[i] == '}' || cArr[i] == ',' || Character.isWhitespace(cArr[i]))) {
                        if (cArr[i] != ']') {
                            i++;
                        }
                    }
                    bufferedReader.reset();
                    bufferedReader.skip((long) (i - 1));
                    cArr[i] = '\u0000';
                    return i;
                }
            }
            if (i == cArr.length) {
                throw new ParseException("Absurdly long value");
            }
            throw new ParseException("Unexpected EOF");
        }
    }

    private final String zza(BufferedReader bufferedReader) throws ParseException, IOException {
        this.zzxb.push(Integer.valueOf(2));
        char zzj = zzj(bufferedReader);
        if (zzj == Typography.quote) {
            this.zzxb.push(Integer.valueOf(3));
            String zzb = zzb(bufferedReader, this.zzwr, this.zzwt, null);
            zzk(3);
            if (zzj(bufferedReader) == ':') {
                return zzb;
            }
            throw new ParseException("Expected key/value separator");
        } else if (zzj == ']') {
            zzk(2);
            zzk(1);
            zzk(5);
            return null;
        } else if (zzj != '}') {
            StringBuilder stringBuilder = new StringBuilder(19);
            stringBuilder.append("Unexpected token: ");
            stringBuilder.append(zzj);
            throw new ParseException(stringBuilder.toString());
        } else {
            zzk(2);
            return null;
        }
    }

    private final String zza(BufferedReader bufferedReader, char[] cArr, StringBuilder stringBuilder, char[] cArr2) throws ParseException, IOException {
        char zzj = zzj(bufferedReader);
        if (zzj == Typography.quote) {
            return zzb(bufferedReader, cArr, stringBuilder, cArr2);
        }
        if (zzj != 'n') {
            throw new ParseException("Expected string");
        }
        zzb(bufferedReader, zzwv);
        return null;
    }

    private final <T extends FastJsonResponse> ArrayList<T> zza(BufferedReader bufferedReader, Field<?, ?> field) throws ParseException, IOException {
        ArrayList<T> arrayList = new ArrayList();
        char zzj = zzj(bufferedReader);
        if (zzj == ']') {
            zzk(5);
            return arrayList;
        } else if (zzj == 'n') {
            zzb(bufferedReader, zzwv);
            zzk(5);
            return null;
        } else if (zzj != '{') {
            r10 = new StringBuilder(19);
            r10.append("Unexpected token: ");
            r10.append(zzj);
            throw new ParseException(r10.toString());
        } else {
            Stack stack = this.zzxb;
            while (true) {
                stack.push(Integer.valueOf(1));
                try {
                    FastJsonResponse newConcreteTypeInstance = field.newConcreteTypeInstance();
                    if (!zza(bufferedReader, newConcreteTypeInstance)) {
                        return arrayList;
                    }
                    arrayList.add(newConcreteTypeInstance);
                    zzj = zzj(bufferedReader);
                    if (zzj != ',') {
                        break;
                    } else if (zzj(bufferedReader) != '{') {
                        throw new ParseException("Expected start of next object in array");
                    } else {
                        stack = this.zzxb;
                    }
                } catch (Throwable e) {
                    throw new ParseException("Error instantiating inner object", e);
                } catch (Throwable e2) {
                    throw new ParseException("Error instantiating inner object", e2);
                }
            }
            if (zzj != ']') {
                r10 = new StringBuilder(19);
                r10.append("Unexpected token: ");
                r10.append(zzj);
                throw new ParseException(r10.toString());
            }
            zzk(5);
            return arrayList;
        }
    }

    private final <O> ArrayList<O> zza(BufferedReader bufferedReader, zza<O> zza) throws ParseException, IOException {
        char zzj = zzj(bufferedReader);
        if (zzj == 'n') {
            zzb(bufferedReader, zzwv);
            return null;
        } else if (zzj != '[') {
            throw new ParseException("Expected start of array");
        } else {
            this.zzxb.push(Integer.valueOf(5));
            ArrayList<O> arrayList = new ArrayList();
            while (true) {
                bufferedReader.mark(1024);
                char zzj2 = zzj(bufferedReader);
                if (zzj2 == '\u0000') {
                    throw new ParseException("Unexpected EOF");
                } else if (zzj2 != ',') {
                    if (zzj2 != ']') {
                        bufferedReader.reset();
                        arrayList.add(zza.zzh(this, bufferedReader));
                    } else {
                        zzk(5);
                        return arrayList;
                    }
                }
            }
        }
    }

    private final boolean zza(BufferedReader bufferedReader, FastJsonResponse fastJsonResponse) throws ParseException, IOException {
        Map fieldMappings = fastJsonResponse.getFieldMappings();
        Object zza = zza(bufferedReader);
        if (zza == null) {
            zzk(1);
            return false;
        }
        while (zza != null) {
            Field field = (Field) fieldMappings.get(zza);
            if (field == null) {
                zza = zzb(bufferedReader);
            } else {
                byte[] decode;
                StringBuilder stringBuilder;
                this.zzxb.push(Integer.valueOf(4));
                char zzj;
                switch (field.getTypeIn()) {
                    case 0:
                        if (!field.isTypeInArray()) {
                            fastJsonResponse.setInteger(field, zzd(bufferedReader));
                            break;
                        }
                        fastJsonResponse.setIntegers(field, zza(bufferedReader, zzxc));
                        break;
                    case 1:
                        if (!field.isTypeInArray()) {
                            fastJsonResponse.setBigInteger(field, zzf(bufferedReader));
                            break;
                        }
                        fastJsonResponse.setBigIntegers(field, zza(bufferedReader, zzxi));
                        break;
                    case 2:
                        if (!field.isTypeInArray()) {
                            fastJsonResponse.setLong(field, zze(bufferedReader));
                            break;
                        }
                        fastJsonResponse.setLongs(field, zza(bufferedReader, zzxd));
                        break;
                    case 3:
                        if (!field.isTypeInArray()) {
                            fastJsonResponse.setFloat(field, zzg(bufferedReader));
                            break;
                        }
                        fastJsonResponse.setFloats(field, zza(bufferedReader, zzxe));
                        break;
                    case 4:
                        if (!field.isTypeInArray()) {
                            fastJsonResponse.setDouble(field, zzh(bufferedReader));
                            break;
                        }
                        fastJsonResponse.setDoubles(field, zza(bufferedReader, zzxf));
                        break;
                    case 5:
                        if (!field.isTypeInArray()) {
                            fastJsonResponse.setBigDecimal(field, zzi(bufferedReader));
                            break;
                        }
                        fastJsonResponse.setBigDecimals(field, zza(bufferedReader, zzxj));
                        break;
                    case 6:
                        if (!field.isTypeInArray()) {
                            fastJsonResponse.setBoolean(field, zza(bufferedReader, false));
                            break;
                        }
                        fastJsonResponse.setBooleans(field, zza(bufferedReader, zzxg));
                        break;
                    case 7:
                        if (!field.isTypeInArray()) {
                            fastJsonResponse.setString(field, zzc(bufferedReader));
                            break;
                        }
                        fastJsonResponse.setStrings(field, zza(bufferedReader, zzxh));
                        break;
                    case 8:
                        decode = Base64Utils.decode(zza(bufferedReader, this.zzws, this.zzwu, zzxa));
                        break;
                    case 9:
                        decode = Base64Utils.decodeUrlSafe(zza(bufferedReader, this.zzws, this.zzwu, zzxa));
                        break;
                    case 10:
                        Map map;
                        zzj = zzj(bufferedReader);
                        if (zzj == 'n') {
                            zzb(bufferedReader, zzwv);
                            map = null;
                        } else if (zzj != '{') {
                            throw new ParseException("Expected start of a map object");
                        } else {
                            this.zzxb.push(Integer.valueOf(1));
                            map = new HashMap();
                            while (true) {
                                char zzj2 = zzj(bufferedReader);
                                if (zzj2 != '\u0000') {
                                    if (zzj2 == Typography.quote) {
                                        String zzb = zzb(bufferedReader, this.zzwr, this.zzwt, null);
                                        String str;
                                        String valueOf;
                                        if (zzj(bufferedReader) != ':') {
                                            str = "No map value found for key ";
                                            valueOf = String.valueOf(zzb);
                                            throw new ParseException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
                                        } else if (zzj(bufferedReader) != Typography.quote) {
                                            str = "Expected String value for key ";
                                            valueOf = String.valueOf(zzb);
                                            throw new ParseException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
                                        } else {
                                            map.put(zzb, zzb(bufferedReader, this.zzwr, this.zzwt, null));
                                            zzj2 = zzj(bufferedReader);
                                            if (zzj2 != ',') {
                                                if (zzj2 != '}') {
                                                    stringBuilder = new StringBuilder(48);
                                                    stringBuilder.append("Unexpected character while parsing string map: ");
                                                    stringBuilder.append(zzj2);
                                                    throw new ParseException(stringBuilder.toString());
                                                }
                                            }
                                        }
                                    } else if (zzj2 != '}') {
                                    }
                                    zzk(1);
                                } else {
                                    throw new ParseException("Unexpected EOF");
                                }
                            }
                        }
                        fastJsonResponse.setStringMap(field, map);
                        break;
                    case 11:
                        if (field.isTypeInArray()) {
                            zzj = zzj(bufferedReader);
                            if (zzj != 'n') {
                                this.zzxb.push(Integer.valueOf(5));
                                if (zzj == '[') {
                                    fastJsonResponse.addConcreteTypeArrayInternal(field, field.getOutputFieldName(), zza(bufferedReader, field));
                                    break;
                                }
                                throw new ParseException("Expected array start");
                            }
                            zzb(bufferedReader, zzwv);
                            fastJsonResponse.addConcreteTypeArrayInternal(field, field.getOutputFieldName(), null);
                            break;
                        }
                        zzj = zzj(bufferedReader);
                        if (zzj == 'n') {
                            zzb(bufferedReader, zzwv);
                            fastJsonResponse.addConcreteTypeInternal(field, field.getOutputFieldName(), null);
                            break;
                        }
                        this.zzxb.push(Integer.valueOf(1));
                        if (zzj != '{') {
                            throw new ParseException("Expected start of object");
                        }
                        try {
                            FastJsonResponse newConcreteTypeInstance = field.newConcreteTypeInstance();
                            zza(bufferedReader, newConcreteTypeInstance);
                            fastJsonResponse.addConcreteTypeInternal(field, field.getOutputFieldName(), newConcreteTypeInstance);
                            break;
                        } catch (Throwable e) {
                            throw new ParseException("Error instantiating inner object", e);
                        } catch (Throwable e2) {
                            throw new ParseException("Error instantiating inner object", e2);
                        }
                    default:
                        int typeIn = field.getTypeIn();
                        StringBuilder stringBuilder2 = new StringBuilder(30);
                        stringBuilder2.append("Invalid field type ");
                        stringBuilder2.append(typeIn);
                        throw new ParseException(stringBuilder2.toString());
                }
                fastJsonResponse.setDecodedBytes(field, decode);
                zzk(4);
                zzk(2);
                char zzj3 = zzj(bufferedReader);
                if (zzj3 == ',') {
                    zza = zza(bufferedReader);
                } else if (zzj3 != '}') {
                    stringBuilder = new StringBuilder(55);
                    stringBuilder.append("Expected end of object or field separator, but found: ");
                    stringBuilder.append(zzj3);
                    throw new ParseException(stringBuilder.toString());
                } else {
                    zza = null;
                }
            }
        }
        PostProcessor postProcessor = fastJsonResponse.getPostProcessor();
        if (postProcessor != null) {
            postProcessor.postProcess(fastJsonResponse);
        }
        zzk(1);
        return true;
    }

    private final boolean zza(BufferedReader bufferedReader, boolean z) throws ParseException, IOException {
        while (true) {
            char zzj = zzj(bufferedReader);
            if (zzj != Typography.quote) {
                break;
            } else if (z) {
                throw new ParseException("No boolean value found in string");
            } else {
                z = true;
            }
        }
        if (zzj == 'f') {
            zzb(bufferedReader, z ? zzwz : zzwy);
            return false;
        } else if (zzj == 'n') {
            zzb(bufferedReader, zzwv);
            return false;
        } else if (zzj != 't') {
            StringBuilder stringBuilder = new StringBuilder(19);
            stringBuilder.append("Unexpected token: ");
            stringBuilder.append(zzj);
            throw new ParseException(stringBuilder.toString());
        } else {
            zzb(bufferedReader, z ? zzwx : zzww);
            return true;
        }
    }

    private final String zzb(BufferedReader bufferedReader) throws ParseException, IOException {
        bufferedReader.mark(1024);
        char zzj = zzj(bufferedReader);
        if (zzj != Typography.quote) {
            if (zzj != ',') {
                int i = 1;
                if (zzj == '[') {
                    this.zzxb.push(Integer.valueOf(5));
                    bufferedReader.mark(32);
                    if (zzj(bufferedReader) != ']') {
                        bufferedReader.reset();
                        int i2 = 0;
                        int i3 = 0;
                        while (i > 0) {
                            char zzj2 = zzj(bufferedReader);
                            if (zzj2 == '\u0000') {
                                throw new ParseException("Unexpected EOF while parsing array");
                            } else if (Character.isISOControl(zzj2)) {
                                throw new ParseException("Unexpected control character while reading array");
                            } else {
                                if (zzj2 == Typography.quote && i2 == 0) {
                                    i3 ^= 1;
                                }
                                if (zzj2 == '[' && i3 == 0) {
                                    i++;
                                }
                                if (zzj2 == ']' && i3 == 0) {
                                    i--;
                                }
                                i2 = (zzj2 != IOUtils.DIR_SEPARATOR_WINDOWS || i3 == 0) ? 0 : i2 ^ 1;
                            }
                        }
                    }
                    zzk(5);
                } else if (zzj != '{') {
                    bufferedReader.reset();
                    zza(bufferedReader, this.zzws);
                } else {
                    this.zzxb.push(Integer.valueOf(1));
                    bufferedReader.mark(32);
                    zzj = zzj(bufferedReader);
                    if (zzj != '}') {
                        if (zzj == Typography.quote) {
                            bufferedReader.reset();
                            zza(bufferedReader);
                            do {
                            } while (zzb(bufferedReader) != null);
                        } else {
                            StringBuilder stringBuilder = new StringBuilder(18);
                            stringBuilder.append("Unexpected token ");
                            stringBuilder.append(zzj);
                            throw new ParseException(stringBuilder.toString());
                        }
                    }
                    zzk(1);
                }
            } else {
                throw new ParseException("Missing value");
            }
        } else if (bufferedReader.read(this.zzwq) == -1) {
            throw new ParseException("Unexpected EOF while parsing string");
        } else {
            zzj = this.zzwq[0];
            int i4 = 0;
            do {
                if (zzj == Typography.quote) {
                    if (i4 != 0) {
                    }
                }
                i4 = zzj == IOUtils.DIR_SEPARATOR_WINDOWS ? i4 ^ 1 : 0;
                if (bufferedReader.read(this.zzwq) == -1) {
                    throw new ParseException("Unexpected EOF while parsing string");
                }
                zzj = this.zzwq[0];
            } while (!Character.isISOControl(zzj));
            throw new ParseException("Unexpected control character while reading string");
        }
        zzj = zzj(bufferedReader);
        if (zzj == ',') {
            zzk(2);
            return zza(bufferedReader);
        } else if (zzj != '}') {
            stringBuilder = new StringBuilder(18);
            stringBuilder.append("Unexpected token ");
            stringBuilder.append(zzj);
            throw new ParseException(stringBuilder.toString());
        } else {
            zzk(2);
            return null;
        }
    }

    private static String zzb(BufferedReader bufferedReader, char[] cArr, StringBuilder stringBuilder, char[] cArr2) throws ParseException, IOException {
        Object obj;
        stringBuilder.setLength(0);
        bufferedReader.mark(cArr.length);
        int i = 0;
        Object obj2 = null;
        loop0:
        while (true) {
            int read = bufferedReader.read(cArr);
            if (read != -1) {
                obj = obj2;
                int i2 = i;
                i = 0;
                while (i < read) {
                    char c = cArr[i];
                    if (Character.isISOControl(c)) {
                        Object obj3;
                        if (cArr2 != null) {
                            for (char c2 : cArr2) {
                                if (c2 == c) {
                                    obj3 = 1;
                                    break;
                                }
                            }
                        }
                        obj3 = null;
                        if (obj3 == null) {
                            throw new ParseException("Unexpected control character while reading string");
                        }
                    }
                    if (c == Typography.quote && i2 == 0) {
                        break loop0;
                    }
                    if (c == IOUtils.DIR_SEPARATOR_WINDOWS) {
                        i2 ^= 1;
                        obj = 1;
                    } else {
                        i2 = 0;
                    }
                    i++;
                }
                stringBuilder.append(cArr, 0, read);
                bufferedReader.mark(cArr.length);
                i = i2;
                obj2 = obj;
            } else {
                throw new ParseException("Unexpected EOF while parsing string");
            }
        }
        stringBuilder.append(cArr, 0, i);
        bufferedReader.reset();
        bufferedReader.skip((long) (i + 1));
        return obj != null ? JsonUtils.unescapeString(stringBuilder.toString()) : stringBuilder.toString();
    }

    private final void zzb(BufferedReader bufferedReader, char[] cArr) throws ParseException, IOException {
        int i = 0;
        while (i < cArr.length) {
            int read = bufferedReader.read(this.zzwr, 0, cArr.length - i);
            if (read == -1) {
                throw new ParseException("Unexpected EOF");
            }
            for (int i2 = 0; i2 < read; i2++) {
                if (cArr[i2 + i] != this.zzwr[i2]) {
                    throw new ParseException("Unexpected character");
                }
            }
            i += read;
        }
    }

    private final String zzc(BufferedReader bufferedReader) throws ParseException, IOException {
        return zza(bufferedReader, this.zzwr, this.zzwt, null);
    }

    private final int zzd(BufferedReader bufferedReader) throws ParseException, IOException {
        int zza = zza(bufferedReader, this.zzws);
        if (zza == 0) {
            return 0;
        }
        char[] cArr = this.zzws;
        if (zza > 0) {
            int i;
            Object obj;
            int i2;
            int i3;
            if (cArr[0] == '-') {
                i = 1;
                obj = 1;
                i2 = Integer.MIN_VALUE;
            } else {
                i = 0;
                obj = null;
                i2 = -2147483647;
            }
            if (i < zza) {
                i3 = i + 1;
                i = Character.digit(cArr[i], 10);
                if (i < 0) {
                    throw new ParseException("Unexpected non-digit character");
                }
                i = -i;
            } else {
                i3 = i;
                i = 0;
            }
            while (i3 < zza) {
                int i4 = i3 + 1;
                i3 = Character.digit(cArr[i3], 10);
                if (i3 < 0) {
                    throw new ParseException("Unexpected non-digit character");
                } else if (i < -214748364) {
                    throw new ParseException("Number too large");
                } else {
                    i *= 10;
                    if (i < i2 + i3) {
                        throw new ParseException("Number too large");
                    }
                    i -= i3;
                    i3 = i4;
                }
            }
            if (obj == null) {
                return -i;
            }
            if (i3 > 1) {
                return i;
            }
            throw new ParseException("No digits to parse");
        }
        throw new ParseException("No number to parse");
    }

    private final long zze(BufferedReader bufferedReader) throws ParseException, IOException {
        int zza = zza(bufferedReader, this.zzws);
        if (zza == 0) {
            return 0;
        }
        char[] cArr = r0.zzws;
        if (zza > 0) {
            long j;
            Object obj;
            int i;
            int digit;
            long j2;
            int i2 = 0;
            if (cArr[0] == '-') {
                j = Long.MIN_VALUE;
                i2 = 1;
                obj = 1;
            } else {
                j = C0649C.TIME_UNSET;
                obj = null;
            }
            if (i2 < zza) {
                i = i2 + 1;
                digit = Character.digit(cArr[i2], 10);
                if (digit < 0) {
                    throw new ParseException("Unexpected non-digit character");
                }
                j2 = (long) (-digit);
            } else {
                j2 = 0;
                i = i2;
            }
            while (i < zza) {
                digit = i + 1;
                i = Character.digit(cArr[i], 10);
                if (i < 0) {
                    throw new ParseException("Unexpected non-digit character");
                } else if (j2 < -922337203685477580L) {
                    throw new ParseException("Number too large");
                } else {
                    j2 *= 10;
                    long j3 = (long) i;
                    if (j2 < j + j3) {
                        throw new ParseException("Number too large");
                    }
                    i = digit;
                    j2 -= j3;
                }
            }
            if (obj == null) {
                return -j2;
            }
            if (i > 1) {
                return j2;
            }
            throw new ParseException("No digits to parse");
        }
        throw new ParseException("No number to parse");
    }

    private final BigInteger zzf(BufferedReader bufferedReader) throws ParseException, IOException {
        int zza = zza(bufferedReader, this.zzws);
        return zza == 0 ? null : new BigInteger(new String(this.zzws, 0, zza));
    }

    private final float zzg(BufferedReader bufferedReader) throws ParseException, IOException {
        int zza = zza(bufferedReader, this.zzws);
        return zza == 0 ? 0.0f : Float.parseFloat(new String(this.zzws, 0, zza));
    }

    private final double zzh(BufferedReader bufferedReader) throws ParseException, IOException {
        int zza = zza(bufferedReader, this.zzws);
        return zza == 0 ? 0.0d : Double.parseDouble(new String(this.zzws, 0, zza));
    }

    private final BigDecimal zzi(BufferedReader bufferedReader) throws ParseException, IOException {
        int zza = zza(bufferedReader, this.zzws);
        return zza == 0 ? null : new BigDecimal(new String(this.zzws, 0, zza));
    }

    private final char zzj(BufferedReader bufferedReader) throws ParseException, IOException {
        if (bufferedReader.read(this.zzwq) == -1) {
            return '\u0000';
        }
        while (Character.isWhitespace(this.zzwq[0])) {
            if (bufferedReader.read(this.zzwq) == -1) {
                return '\u0000';
            }
        }
        return this.zzwq[0];
    }

    private final void zzk(int i) throws ParseException {
        if (this.zzxb.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(46);
            stringBuilder.append("Expected state ");
            stringBuilder.append(i);
            stringBuilder.append(" but had empty stack");
            throw new ParseException(stringBuilder.toString());
        }
        int intValue = ((Integer) this.zzxb.pop()).intValue();
        if (intValue != i) {
            StringBuilder stringBuilder2 = new StringBuilder(46);
            stringBuilder2.append("Expected state ");
            stringBuilder2.append(i);
            stringBuilder2.append(" but had ");
            stringBuilder2.append(intValue);
            throw new ParseException(stringBuilder2.toString());
        }
    }

    public void parse(java.io.InputStream r5, T r6) throws com.google.android.gms.common.server.response.FastParser.ParseException {
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
        r4 = this;
        r0 = new java.io.BufferedReader;
        r1 = new java.io.InputStreamReader;
        r1.<init>(r5);
        r5 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0.<init>(r1, r5);
        r5 = r4.zzxb;	 Catch:{ IOException -> 0x009f }
        r1 = 0;	 Catch:{ IOException -> 0x009f }
        r2 = java.lang.Integer.valueOf(r1);	 Catch:{ IOException -> 0x009f }
        r5.push(r2);	 Catch:{ IOException -> 0x009f }
        r5 = r4.zzj(r0);	 Catch:{ IOException -> 0x009f }
        if (r5 == 0) goto L_0x0095;	 Catch:{ IOException -> 0x009f }
    L_0x001c:
        r2 = 91;	 Catch:{ IOException -> 0x009f }
        r3 = 1;	 Catch:{ IOException -> 0x009f }
        if (r5 == r2) goto L_0x004b;	 Catch:{ IOException -> 0x009f }
    L_0x0021:
        r2 = 123; // 0x7b float:1.72E-43 double:6.1E-322;	 Catch:{ IOException -> 0x009f }
        if (r5 == r2) goto L_0x003e;	 Catch:{ IOException -> 0x009f }
    L_0x0025:
        r6 = new com.google.android.gms.common.server.response.FastParser$ParseException;	 Catch:{ IOException -> 0x009f }
        r1 = 19;	 Catch:{ IOException -> 0x009f }
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x009f }
        r2.<init>(r1);	 Catch:{ IOException -> 0x009f }
        r1 = "Unexpected token: ";	 Catch:{ IOException -> 0x009f }
        r2.append(r1);	 Catch:{ IOException -> 0x009f }
        r2.append(r5);	 Catch:{ IOException -> 0x009f }
        r5 = r2.toString();	 Catch:{ IOException -> 0x009f }
        r6.<init>(r5);	 Catch:{ IOException -> 0x009f }
        throw r6;	 Catch:{ IOException -> 0x009f }
    L_0x003e:
        r5 = r4.zzxb;	 Catch:{ IOException -> 0x009f }
        r2 = java.lang.Integer.valueOf(r3);	 Catch:{ IOException -> 0x009f }
        r5.push(r2);	 Catch:{ IOException -> 0x009f }
        r4.zza(r0, r6);	 Catch:{ IOException -> 0x009f }
        goto L_0x0086;	 Catch:{ IOException -> 0x009f }
    L_0x004b:
        r5 = r4.zzxb;	 Catch:{ IOException -> 0x009f }
        r2 = 5;	 Catch:{ IOException -> 0x009f }
        r2 = java.lang.Integer.valueOf(r2);	 Catch:{ IOException -> 0x009f }
        r5.push(r2);	 Catch:{ IOException -> 0x009f }
        r5 = r6.getFieldMappings();	 Catch:{ IOException -> 0x009f }
        r2 = r5.size();	 Catch:{ IOException -> 0x009f }
        if (r2 == r3) goto L_0x0067;	 Catch:{ IOException -> 0x009f }
    L_0x005f:
        r5 = new com.google.android.gms.common.server.response.FastParser$ParseException;	 Catch:{ IOException -> 0x009f }
        r6 = "Object array response class must have a single Field";	 Catch:{ IOException -> 0x009f }
        r5.<init>(r6);	 Catch:{ IOException -> 0x009f }
        throw r5;	 Catch:{ IOException -> 0x009f }
    L_0x0067:
        r5 = r5.entrySet();	 Catch:{ IOException -> 0x009f }
        r5 = r5.iterator();	 Catch:{ IOException -> 0x009f }
        r5 = r5.next();	 Catch:{ IOException -> 0x009f }
        r5 = (java.util.Map.Entry) r5;	 Catch:{ IOException -> 0x009f }
        r5 = r5.getValue();	 Catch:{ IOException -> 0x009f }
        r5 = (com.google.android.gms.common.server.response.FastJsonResponse.Field) r5;	 Catch:{ IOException -> 0x009f }
        r2 = r4.zza(r0, r5);	 Catch:{ IOException -> 0x009f }
        r3 = r5.getOutputFieldName();	 Catch:{ IOException -> 0x009f }
        r6.addConcreteTypeArrayInternal(r5, r3, r2);	 Catch:{ IOException -> 0x009f }
    L_0x0086:
        r4.zzk(r1);	 Catch:{ IOException -> 0x009f }
        r0.close();	 Catch:{ IOException -> 0x008d }
        return;
    L_0x008d:
        r5 = "FastParser";
        r6 = "Failed to close reader while parsing.";
        android.util.Log.w(r5, r6);
        return;
    L_0x0095:
        r5 = new com.google.android.gms.common.server.response.FastParser$ParseException;	 Catch:{ IOException -> 0x009f }
        r6 = "No data to parse";	 Catch:{ IOException -> 0x009f }
        r5.<init>(r6);	 Catch:{ IOException -> 0x009f }
        throw r5;	 Catch:{ IOException -> 0x009f }
    L_0x009d:
        r5 = move-exception;
        goto L_0x00a6;
    L_0x009f:
        r5 = move-exception;
        r6 = new com.google.android.gms.common.server.response.FastParser$ParseException;	 Catch:{ all -> 0x009d }
        r6.<init>(r5);	 Catch:{ all -> 0x009d }
        throw r6;	 Catch:{ all -> 0x009d }
    L_0x00a6:
        r0.close();	 Catch:{ IOException -> 0x00aa }
        goto L_0x00b1;
    L_0x00aa:
        r6 = "FastParser";
        r0 = "Failed to close reader while parsing.";
        android.util.Log.w(r6, r0);
    L_0x00b1:
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.FastParser.parse(java.io.InputStream, com.google.android.gms.common.server.response.FastJsonResponse):void");
    }

    public void parse(java.lang.String r2, T r3) throws com.google.android.gms.common.server.response.FastParser.ParseException {
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
        r1 = this;
        r0 = new java.io.ByteArrayInputStream;
        r2 = r2.getBytes();
        r0.<init>(r2);
        r1.parse(r0, r3);	 Catch:{ all -> 0x0018 }
        r0.close();	 Catch:{ IOException -> 0x0010 }
        return;
    L_0x0010:
        r2 = "FastParser";
        r3 = "Failed to close the input stream while parsing.";
        android.util.Log.w(r2, r3);
        return;
    L_0x0018:
        r2 = move-exception;
        r0.close();	 Catch:{ IOException -> 0x001d }
        goto L_0x0024;
    L_0x001d:
        r3 = "FastParser";
        r0 = "Failed to close the input stream while parsing.";
        android.util.Log.w(r3, r0);
    L_0x0024:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.FastParser.parse(java.lang.String, com.google.android.gms.common.server.response.FastJsonResponse):void");
    }
}
