package kotlin.text;

import com.google.android.exoplayer2.C0649C;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\\\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\n\n\u0002\b\u0005\u001a4\u0010\u0000\u001a\u0004\u0018\u0001H\u0001\"\u0004\b\u0000\u0010\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u0002H\u00010\u0005H\b¢\u0006\u0004\b\u0006\u0010\u0007\u001a\r\u0010\b\u001a\u00020\t*\u00020\u0003H\b\u001a\u0015\u0010\b\u001a\u00020\t*\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\b\u001a\u000e\u0010\f\u001a\u0004\u0018\u00010\t*\u00020\u0003H\u0007\u001a\u0016\u0010\f\u001a\u0004\u0018\u00010\t*\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0007\u001a\r\u0010\r\u001a\u00020\u000e*\u00020\u0003H\b\u001a\u0015\u0010\r\u001a\u00020\u000e*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\b\u001a\u000e\u0010\u0011\u001a\u0004\u0018\u00010\u000e*\u00020\u0003H\u0007\u001a\u0016\u0010\u0011\u001a\u0004\u0018\u00010\u000e*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0007\u001a\r\u0010\u0012\u001a\u00020\u0013*\u00020\u0003H\b\u001a\r\u0010\u0014\u001a\u00020\u0015*\u00020\u0003H\b\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\b\u001a\u0013\u0010\u0016\u001a\u0004\u0018\u00010\u0015*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0017\u001a\u001b\u0010\u0016\u001a\u0004\u0018\u00010\u0015*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0007¢\u0006\u0002\u0010\u0018\u001a\r\u0010\u0019\u001a\u00020\u001a*\u00020\u0003H\b\u001a\u0013\u0010\u001b\u001a\u0004\u0018\u00010\u001a*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u001c\u001a\r\u0010\u001d\u001a\u00020\u001e*\u00020\u0003H\b\u001a\u0013\u0010\u001f\u001a\u0004\u0018\u00010\u001e*\u00020\u0003H\u0007¢\u0006\u0002\u0010 \u001a\r\u0010!\u001a\u00020\u0010*\u00020\u0003H\b\u001a\u0015\u0010!\u001a\u00020\u0010*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\b\u001a\u0013\u0010\"\u001a\u0004\u0018\u00010\u0010*\u00020\u0003H\u0007¢\u0006\u0002\u0010#\u001a\u001b\u0010\"\u001a\u0004\u0018\u00010\u0010*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0007¢\u0006\u0002\u0010$\u001a\r\u0010%\u001a\u00020&*\u00020\u0003H\b\u001a\u0015\u0010%\u001a\u00020&*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\b\u001a\u0013\u0010'\u001a\u0004\u0018\u00010&*\u00020\u0003H\u0007¢\u0006\u0002\u0010(\u001a\u001b\u0010'\u001a\u0004\u0018\u00010&*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0007¢\u0006\u0002\u0010)\u001a\r\u0010*\u001a\u00020+*\u00020\u0003H\b\u001a\u0015\u0010*\u001a\u00020+*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\b\u001a\u0013\u0010,\u001a\u0004\u0018\u00010+*\u00020\u0003H\u0007¢\u0006\u0002\u0010-\u001a\u001b\u0010,\u001a\u0004\u0018\u00010+*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0007¢\u0006\u0002\u0010.\u001a\u0015\u0010/\u001a\u00020\u0003*\u00020\u00152\u0006\u0010\u000f\u001a\u00020\u0010H\b\u001a\u0015\u0010/\u001a\u00020\u0003*\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u0010H\b\u001a\u0015\u0010/\u001a\u00020\u0003*\u00020&2\u0006\u0010\u000f\u001a\u00020\u0010H\b\u001a\u0015\u0010/\u001a\u00020\u0003*\u00020+2\u0006\u0010\u000f\u001a\u00020\u0010H\b¨\u00060"}, d2 = {"screenFloatValue", "T", "str", "", "parse", "Lkotlin/Function1;", "screenFloatValue$StringsKt__StringNumberConversionsKt", "(Ljava/lang/String;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "toBigDecimal", "Ljava/math/BigDecimal;", "mathContext", "Ljava/math/MathContext;", "toBigDecimalOrNull", "toBigInteger", "Ljava/math/BigInteger;", "radix", "", "toBigIntegerOrNull", "toBoolean", "", "toByte", "", "toByteOrNull", "(Ljava/lang/String;)Ljava/lang/Byte;", "(Ljava/lang/String;I)Ljava/lang/Byte;", "toDouble", "", "toDoubleOrNull", "(Ljava/lang/String;)Ljava/lang/Double;", "toFloat", "", "toFloatOrNull", "(Ljava/lang/String;)Ljava/lang/Float;", "toInt", "toIntOrNull", "(Ljava/lang/String;)Ljava/lang/Integer;", "(Ljava/lang/String;I)Ljava/lang/Integer;", "toLong", "", "toLongOrNull", "(Ljava/lang/String;)Ljava/lang/Long;", "(Ljava/lang/String;I)Ljava/lang/Long;", "toShort", "", "toShortOrNull", "(Ljava/lang/String;)Ljava/lang/Short;", "(Ljava/lang/String;I)Ljava/lang/Short;", "toString", "kotlin-stdlib"}, k = 5, mv = {1, 1, 10}, xi = 1, xs = "kotlin/text/StringsKt")
/* compiled from: StringNumberConversions.kt */
class StringsKt__StringNumberConversionsKt extends StringsKt__StringBuilderKt {
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String toString(byte b, int i) {
        b = Integer.toString(b, CharsKt.checkRadix(CharsKt.checkRadix(i)));
        Intrinsics.checkExpressionValueIsNotNull(b, "java.lang.Integer.toStri…(this, checkRadix(radix))");
        return b;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String toString(short s, int i) {
        s = Integer.toString(s, CharsKt.checkRadix(CharsKt.checkRadix(i)));
        Intrinsics.checkExpressionValueIsNotNull(s, "java.lang.Integer.toStri…(this, checkRadix(radix))");
        return s;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String toString(int i, int i2) {
        i = Integer.toString(i, CharsKt.checkRadix(i2));
        Intrinsics.checkExpressionValueIsNotNull(i, "java.lang.Integer.toStri…(this, checkRadix(radix))");
        return i;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String toString(long j, int i) {
        j = Long.toString(j, CharsKt.checkRadix(i));
        Intrinsics.checkExpressionValueIsNotNull(j, "java.lang.Long.toString(this, checkRadix(radix))");
        return j;
    }

    @InlineOnly
    private static final boolean toBoolean(@NotNull String str) {
        return Boolean.parseBoolean(str);
    }

    @InlineOnly
    private static final byte toByte(@NotNull String str) {
        return Byte.parseByte(str);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte toByte(@NotNull String str, int i) {
        return Byte.parseByte(str, CharsKt.checkRadix(i));
    }

    @InlineOnly
    private static final short toShort(@NotNull String str) {
        return Short.parseShort(str);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short toShort(@NotNull String str, int i) {
        return Short.parseShort(str, CharsKt.checkRadix(i));
    }

    @InlineOnly
    private static final int toInt(@NotNull String str) {
        return Integer.parseInt(str);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int toInt(@NotNull String str, int i) {
        return Integer.parseInt(str, CharsKt.checkRadix(i));
    }

    @InlineOnly
    private static final long toLong(@NotNull String str) {
        return Long.parseLong(str);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final long toLong(@NotNull String str, int i) {
        return Long.parseLong(str, CharsKt.checkRadix(i));
    }

    @InlineOnly
    private static final float toFloat(@NotNull String str) {
        return Float.parseFloat(str);
    }

    @InlineOnly
    private static final double toDouble(@NotNull String str) {
        return Double.parseDouble(str);
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Byte toByteOrNull(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        return StringsKt.toByteOrNull(str, 10);
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Byte toByteOrNull(@NotNull String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        str = StringsKt.toIntOrNull(str, i);
        if (str == null) {
            return null;
        }
        str = str.intValue();
        if (str >= -128) {
            if (str <= 127) {
                return Byte.valueOf((byte) str);
            }
        }
        return null;
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Short toShortOrNull(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        return StringsKt.toShortOrNull(str, 10);
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Short toShortOrNull(@NotNull String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        str = StringsKt.toIntOrNull(str, i);
        if (str == null) {
            return null;
        }
        str = str.intValue();
        if (str >= -32768) {
            if (str <= 32767) {
                return Short.valueOf((short) str);
            }
        }
        return null;
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Integer toIntOrNull(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        return StringsKt.toIntOrNull(str, 10);
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Integer toIntOrNull(@NotNull String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        CharsKt.checkRadix(i);
        int length = str.length();
        if (length == 0) {
            return null;
        }
        int i2;
        Object obj;
        int i3;
        int digitOf;
        int i4 = 0;
        char charAt = str.charAt(0);
        int i5 = -2147483647;
        if (charAt >= '0') {
            i2 = 0;
        } else if (length == 1) {
            return null;
        } else {
            if (charAt == '-') {
                i5 = Integer.MIN_VALUE;
                i2 = 1;
                obj = 1;
                i3 = i5 / i;
                length--;
                if (i2 <= length) {
                    while (true) {
                        digitOf = CharsKt.digitOf(str.charAt(i2), i);
                        if (digitOf < 0 && i4 >= i3) {
                            i4 *= i;
                            if (i4 >= i5 + digitOf) {
                                i4 -= digitOf;
                                if (i2 != length) {
                                    break;
                                }
                                i2++;
                            } else {
                                return null;
                            }
                        }
                        return null;
                    }
                }
                return obj == null ? Integer.valueOf(i4) : Integer.valueOf(-i4);
            } else if (charAt != '+') {
                return null;
            } else {
                i2 = 1;
            }
        }
        obj = null;
        i3 = i5 / i;
        length--;
        if (i2 <= length) {
            while (true) {
                digitOf = CharsKt.digitOf(str.charAt(i2), i);
                if (digitOf < 0) {
                    return null;
                }
                i4 *= i;
                if (i4 >= i5 + digitOf) {
                    return null;
                }
                i4 -= digitOf;
                if (i2 != length) {
                    break;
                }
                i2++;
            }
        }
        if (obj == null) {
        }
        return obj == null ? Integer.valueOf(i4) : Integer.valueOf(-i4);
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Long toLongOrNull(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        return StringsKt.toLongOrNull(str, 10);
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Long toLongOrNull(@NotNull String str, int i) {
        String str2 = str;
        int i2 = i;
        Intrinsics.checkParameterIsNotNull(str2, "$receiver");
        CharsKt.checkRadix(i);
        int length = str.length();
        Long l = null;
        if (length == 0) {
            return null;
        }
        Object obj;
        long j;
        long j2;
        long j3;
        int digitOf;
        int i3;
        long j4;
        long j5;
        int i4 = 0;
        char charAt = str2.charAt(0);
        long j6 = C0649C.TIME_UNSET;
        if (charAt < '0') {
            if (length == 1) {
                return null;
            }
            if (charAt == '-') {
                j6 = Long.MIN_VALUE;
                i4 = 1;
                obj = 1;
                j = (long) i2;
                j2 = j6 / j;
                j3 = 0;
                length--;
                if (i4 <= length) {
                    while (true) {
                        digitOf = CharsKt.digitOf(str2.charAt(i4), i2);
                        if (digitOf < 0 && j3 >= j2) {
                            j3 *= j;
                            i3 = i4;
                            j4 = (long) digitOf;
                            if (j3 >= j6 + j4) {
                                j5 = j3 - j4;
                                i4 = i3;
                                if (i4 != length) {
                                    break;
                                }
                                i4++;
                                l = null;
                                j3 = j5;
                            } else {
                                return null;
                            }
                        }
                        return l;
                    }
                    j3 = j5;
                }
                return obj == null ? Long.valueOf(j3) : Long.valueOf(-j3);
            } else if (charAt != '+') {
                return null;
            } else {
                i4 = 1;
            }
        }
        obj = null;
        j = (long) i2;
        j2 = j6 / j;
        j3 = 0;
        length--;
        if (i4 <= length) {
            while (true) {
                digitOf = CharsKt.digitOf(str2.charAt(i4), i2);
                if (digitOf < 0) {
                    return l;
                }
                j3 *= j;
                i3 = i4;
                j4 = (long) digitOf;
                if (j3 >= j6 + j4) {
                    return null;
                }
                j5 = j3 - j4;
                i4 = i3;
                if (i4 != length) {
                    break;
                }
                i4++;
                l = null;
                j3 = j5;
            }
            j3 = j5;
        }
        if (obj == null) {
        }
        return obj == null ? Long.valueOf(j3) : Long.valueOf(-j3);
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger toBigInteger(@NotNull String str) {
        return new BigInteger(str);
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger toBigInteger(@NotNull String str, int i) {
        return new BigInteger(str, CharsKt.checkRadix(i));
    }

    @SinceKotlin(version = "1.2")
    @Nullable
    public static final BigInteger toBigIntegerOrNull(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        return StringsKt.toBigIntegerOrNull(str, 10);
    }

    @SinceKotlin(version = "1.2")
    @Nullable
    public static final BigInteger toBigIntegerOrNull(@NotNull String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        CharsKt.checkRadix(i);
        int length = str.length();
        int i2 = 0;
        switch (length) {
            case 0:
                return null;
            case 1:
                if (CharsKt.digitOf(str.charAt(0), i) < 0) {
                    return null;
                }
                break;
            default:
                if (str.charAt(0) == '-') {
                    i2 = 1;
                }
                while (i2 < length) {
                    if (CharsKt.digitOf(str.charAt(i2), i) < 0) {
                        return null;
                    }
                    i2++;
                }
                break;
        }
        return new BigInteger(str, CharsKt.checkRadix(i));
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(@NotNull String str) {
        return new BigDecimal(str);
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(@NotNull String str, MathContext mathContext) {
        return new BigDecimal(str, mathContext);
    }

    private static final <T> T screenFloatValue$StringsKt__StringNumberConversionsKt(java.lang.String r3, kotlin.jvm.functions.Function1<? super java.lang.String, ? extends T> r4) {
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
        r0 = 0;
        r1 = kotlin.text.ScreenFloatValueRegEx.value;	 Catch:{ NumberFormatException -> 0x0011 }
        r2 = r3;	 Catch:{ NumberFormatException -> 0x0011 }
        r2 = (java.lang.CharSequence) r2;	 Catch:{ NumberFormatException -> 0x0011 }
        r1 = r1.matches(r2);	 Catch:{ NumberFormatException -> 0x0011 }
        if (r1 == 0) goto L_0x0011;	 Catch:{ NumberFormatException -> 0x0011 }
    L_0x000c:
        r3 = r4.invoke(r3);	 Catch:{ NumberFormatException -> 0x0011 }
        r0 = r3;
    L_0x0011:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.text.StringsKt__StringNumberConversionsKt.screenFloatValue$StringsKt__StringNumberConversionsKt(java.lang.String, kotlin.jvm.functions.Function1):T");
    }

    @kotlin.SinceKotlin(version = "1.1")
    @org.jetbrains.annotations.Nullable
    public static final java.lang.Float toFloatOrNull(@org.jetbrains.annotations.NotNull java.lang.String r3) {
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
        r0 = "$receiver";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r3, r0);
        r0 = 0;
        r1 = kotlin.text.ScreenFloatValueRegEx.value;	 Catch:{ NumberFormatException -> 0x001a }
        r2 = r3;	 Catch:{ NumberFormatException -> 0x001a }
        r2 = (java.lang.CharSequence) r2;	 Catch:{ NumberFormatException -> 0x001a }
        r1 = r1.matches(r2);	 Catch:{ NumberFormatException -> 0x001a }
        if (r1 == 0) goto L_0x001a;	 Catch:{ NumberFormatException -> 0x001a }
    L_0x0011:
        r3 = java.lang.Float.parseFloat(r3);	 Catch:{ NumberFormatException -> 0x001a }
        r3 = java.lang.Float.valueOf(r3);	 Catch:{ NumberFormatException -> 0x001a }
        r0 = r3;
    L_0x001a:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.text.StringsKt__StringNumberConversionsKt.toFloatOrNull(java.lang.String):java.lang.Float");
    }

    @kotlin.SinceKotlin(version = "1.1")
    @org.jetbrains.annotations.Nullable
    public static final java.lang.Double toDoubleOrNull(@org.jetbrains.annotations.NotNull java.lang.String r3) {
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
        r0 = "$receiver";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r3, r0);
        r0 = 0;
        r1 = kotlin.text.ScreenFloatValueRegEx.value;	 Catch:{ NumberFormatException -> 0x001a }
        r2 = r3;	 Catch:{ NumberFormatException -> 0x001a }
        r2 = (java.lang.CharSequence) r2;	 Catch:{ NumberFormatException -> 0x001a }
        r1 = r1.matches(r2);	 Catch:{ NumberFormatException -> 0x001a }
        if (r1 == 0) goto L_0x001a;	 Catch:{ NumberFormatException -> 0x001a }
    L_0x0011:
        r1 = java.lang.Double.parseDouble(r3);	 Catch:{ NumberFormatException -> 0x001a }
        r3 = java.lang.Double.valueOf(r1);	 Catch:{ NumberFormatException -> 0x001a }
        r0 = r3;
    L_0x001a:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.text.StringsKt__StringNumberConversionsKt.toDoubleOrNull(java.lang.String):java.lang.Double");
    }

    @kotlin.SinceKotlin(version = "1.2")
    @org.jetbrains.annotations.Nullable
    public static final java.math.BigDecimal toBigDecimalOrNull(@org.jetbrains.annotations.NotNull java.lang.String r3) {
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
        r0 = "$receiver";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r3, r0);
        r0 = 0;
        r1 = kotlin.text.ScreenFloatValueRegEx.value;	 Catch:{ NumberFormatException -> 0x0017 }
        r2 = r3;	 Catch:{ NumberFormatException -> 0x0017 }
        r2 = (java.lang.CharSequence) r2;	 Catch:{ NumberFormatException -> 0x0017 }
        r1 = r1.matches(r2);	 Catch:{ NumberFormatException -> 0x0017 }
        if (r1 == 0) goto L_0x0017;	 Catch:{ NumberFormatException -> 0x0017 }
    L_0x0011:
        r1 = new java.math.BigDecimal;	 Catch:{ NumberFormatException -> 0x0017 }
        r1.<init>(r3);	 Catch:{ NumberFormatException -> 0x0017 }
        r0 = r1;
    L_0x0017:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.text.StringsKt__StringNumberConversionsKt.toBigDecimalOrNull(java.lang.String):java.math.BigDecimal");
    }

    @kotlin.SinceKotlin(version = "1.2")
    @org.jetbrains.annotations.Nullable
    public static final java.math.BigDecimal toBigDecimalOrNull(@org.jetbrains.annotations.NotNull java.lang.String r3, @org.jetbrains.annotations.NotNull java.math.MathContext r4) {
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
        r0 = "$receiver";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r3, r0);
        r0 = "mathContext";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r4, r0);
        r0 = 0;
        r1 = kotlin.text.ScreenFloatValueRegEx.value;	 Catch:{ NumberFormatException -> 0x001c }
        r2 = r3;	 Catch:{ NumberFormatException -> 0x001c }
        r2 = (java.lang.CharSequence) r2;	 Catch:{ NumberFormatException -> 0x001c }
        r1 = r1.matches(r2);	 Catch:{ NumberFormatException -> 0x001c }
        if (r1 == 0) goto L_0x001c;	 Catch:{ NumberFormatException -> 0x001c }
    L_0x0016:
        r1 = new java.math.BigDecimal;	 Catch:{ NumberFormatException -> 0x001c }
        r1.<init>(r3, r4);	 Catch:{ NumberFormatException -> 0x001c }
        r0 = r1;
    L_0x001c:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.text.StringsKt__StringNumberConversionsKt.toBigDecimalOrNull(java.lang.String, java.math.MathContext):java.math.BigDecimal");
    }
}
