package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import de.timroes.axmlrpc.serializer.SerializerHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@GwtCompatible(emulated = true)
public final class Doubles {
    public static final int BYTES = 8;
    @GwtIncompatible("regular expressions")
    static final Pattern FLOATING_POINT_PATTERN = fpPattern();

    public static boolean isFinite(double d) {
        int i = 0;
        int i2 = Double.NEGATIVE_INFINITY < d ? 1 : 0;
        if (d < Double.POSITIVE_INFINITY) {
            i = 1;
        }
        return i2 & i;
    }

    private Doubles() {
    }

    public static int hashCode(double d) {
        return Double.valueOf(d).hashCode();
    }

    public static int compare(double d, double d2) {
        return Double.compare(d, d2);
    }

    public static boolean contains(double[] dArr, double d) {
        for (double d2 : dArr) {
            if (d2 == d) {
                return 1;
            }
        }
        return false;
    }

    public static int indexOf(double[] dArr, double d) {
        return indexOf(dArr, d, 0, dArr.length);
    }

    private static int indexOf(double[] dArr, double d, int i, int i2) {
        while (i < i2) {
            if (dArr[i] == d) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int indexOf(double[] dArr, double[] dArr2) {
        Preconditions.checkNotNull(dArr, SerializerHandler.TYPE_ARRAY);
        Preconditions.checkNotNull(dArr2, "target");
        if (dArr2.length == 0) {
            return 0;
        }
        int i = 0;
        while (i < (dArr.length - dArr2.length) + 1) {
            int i2 = 0;
            while (i2 < dArr2.length) {
                if (dArr[i + i2] != dArr2[i2]) {
                    i++;
                } else {
                    i2++;
                }
            }
            return i;
        }
        return -1;
    }

    public static int lastIndexOf(double[] dArr, double d) {
        return lastIndexOf(dArr, d, 0, dArr.length);
    }

    private static int lastIndexOf(double[] dArr, double d, int i, int i2) {
        for (i2--; i2 >= i; i2--) {
            if (dArr[i2] == d) {
                return i2;
            }
        }
        return -1;
    }

    public static double min(double... dArr) {
        Preconditions.checkArgument(dArr.length > 0);
        double d = dArr[0];
        for (int i = 1; i < dArr.length; i++) {
            d = Math.min(d, dArr[i]);
        }
        return d;
    }

    public static double max(double... dArr) {
        Preconditions.checkArgument(dArr.length > 0);
        double d = dArr[0];
        for (int i = 1; i < dArr.length; i++) {
            d = Math.max(d, dArr[i]);
        }
        return d;
    }

    public static double[] concat(double[]... dArr) {
        int i = 0;
        for (double[] length : dArr) {
            i += length.length;
        }
        Object obj = new double[i];
        int i2 = 0;
        for (Object obj2 : dArr) {
            System.arraycopy(obj2, 0, obj, i2, obj2.length);
            i2 += obj2.length;
        }
        return obj;
    }

    @Beta
    public static Converter<String, Double> stringConverter() {
        return Doubles$DoubleConverter.INSTANCE;
    }

    public static double[] ensureCapacity(double[] dArr, int i, int i2) {
        Preconditions.checkArgument(i >= 0, "Invalid minLength: %s", Integer.valueOf(i));
        Preconditions.checkArgument(i2 >= 0, "Invalid padding: %s", Integer.valueOf(i2));
        return dArr.length < i ? copyOf(dArr, i + i2) : dArr;
    }

    private static double[] copyOf(double[] dArr, int i) {
        Object obj = new double[i];
        System.arraycopy(dArr, 0, obj, 0, Math.min(dArr.length, i));
        return obj;
    }

    public static String join(String str, double... dArr) {
        Preconditions.checkNotNull(str);
        if (dArr.length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(dArr.length * 12);
        stringBuilder.append(dArr[0]);
        for (int i = 1; i < dArr.length; i++) {
            stringBuilder.append(str);
            stringBuilder.append(dArr[i]);
        }
        return stringBuilder.toString();
    }

    public static Comparator<double[]> lexicographicalComparator() {
        return Doubles$LexicographicalComparator.INSTANCE;
    }

    public static double[] toArray(Collection<? extends Number> collection) {
        if (collection instanceof Doubles$DoubleArrayAsList) {
            return ((Doubles$DoubleArrayAsList) collection).toDoubleArray();
        }
        collection = collection.toArray();
        int length = collection.length;
        double[] dArr = new double[length];
        for (int i = 0; i < length; i++) {
            dArr[i] = ((Number) Preconditions.checkNotNull(collection[i])).doubleValue();
        }
        return dArr;
    }

    public static List<Double> asList(double... dArr) {
        if (dArr.length == 0) {
            return Collections.emptyList();
        }
        return new Doubles$DoubleArrayAsList(dArr);
    }

    @GwtIncompatible("regular expressions")
    private static Pattern fpPattern() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(?:\\d++(?:\\.\\d*+)?|\\.\\d++)");
        stringBuilder.append("(?:[eE][+-]?\\d++)?[fFdD]?");
        String stringBuilder2 = stringBuilder.toString();
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("0[xX]");
        stringBuilder3.append("(?:\\p{XDigit}++(?:\\.\\p{XDigit}*+)?|\\.\\p{XDigit}++)");
        stringBuilder3.append("[pP][+-]?\\d++[fFdD]?");
        String stringBuilder4 = stringBuilder3.toString();
        stringBuilder3 = new StringBuilder();
        stringBuilder3.append("[+-]?(?:NaN|Infinity|");
        stringBuilder3.append(stringBuilder2);
        stringBuilder3.append("|");
        stringBuilder3.append(stringBuilder4);
        stringBuilder3.append(")");
        return Pattern.compile(stringBuilder3.toString());
    }

    @com.google.common.annotations.GwtIncompatible("regular expressions")
    @javax.annotation.Nullable
    @com.google.common.annotations.Beta
    public static java.lang.Double tryParse(java.lang.String r2) {
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
        r0 = FLOATING_POINT_PATTERN;
        r0 = r0.matcher(r2);
        r0 = r0.matches();
        if (r0 == 0) goto L_0x0015;
    L_0x000c:
        r0 = java.lang.Double.parseDouble(r2);	 Catch:{ NumberFormatException -> 0x0015 }
        r2 = java.lang.Double.valueOf(r0);	 Catch:{ NumberFormatException -> 0x0015 }
        return r2;
    L_0x0015:
        r2 = 0;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.primitives.Doubles.tryParse(java.lang.String):java.lang.Double");
    }
}
