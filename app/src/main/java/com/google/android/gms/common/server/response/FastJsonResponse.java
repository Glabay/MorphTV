package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.util.Log;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Objects.ToStringHelper;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;
import com.google.android.gms.common.server.converter.ConverterWrapper;
import com.google.android.gms.common.util.Base64Utils;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.common.util.MapUtils;
import com.google.android.gms.common.util.VisibleForTesting;
import com.tonyodev.fetch2.util.FetchDefaults;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class FastJsonResponse {
    protected static final String QUOTE = "\"";
    private int zzwk;
    private byte[] zzwl;
    private boolean zzwm;

    public interface FieldConverter<I, O> {
        O convert(I i);

        I convertBack(O o);

        int getTypeIn();

        int getTypeOut();
    }

    @Class(creator = "FieldCreator")
    @VisibleForTesting
    public static class Field<I, O> extends AbstractSafeParcelable {
        public static final FieldCreator CREATOR = new FieldCreator();
        protected final Class<? extends FastJsonResponse> mConcreteType;
        @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field(getter = "getConcreteTypeName", id = 8)
        protected final String mConcreteTypeName;
        @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field(getter = "getOutputFieldName", id = 6)
        protected final String mOutputFieldName;
        @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field(getter = "getSafeParcelableFieldId", id = 7)
        protected final int mSafeParcelableFieldId;
        @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field(getter = "getTypeIn", id = 2)
        protected final int mTypeIn;
        @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field(getter = "isTypeInArray", id = 3)
        protected final boolean mTypeInArray;
        @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field(getter = "getTypeOut", id = 4)
        protected final int mTypeOut;
        @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field(getter = "isTypeOutArray", id = 5)
        protected final boolean mTypeOutArray;
        @VersionField(getter = "getVersionCode", id = 1)
        private final int zzal;
        private FieldMappingDictionary zzwn;
        @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field(getter = "getWrappedConverter", id = 9, type = "com.google.android.gms.common.server.converter.ConverterWrapper")
        private FieldConverter<I, O> zzwo;

        @Constructor
        Field(@Param(id = 1) int i, @Param(id = 2) int i2, @Param(id = 3) boolean z, @Param(id = 4) int i3, @Param(id = 5) boolean z2, @Param(id = 6) String str, @Param(id = 7) int i4, @Param(id = 8) String str2, @Param(id = 9) ConverterWrapper converterWrapper) {
            this.zzal = i;
            this.mTypeIn = i2;
            this.mTypeInArray = z;
            this.mTypeOut = i3;
            this.mTypeOutArray = z2;
            this.mOutputFieldName = str;
            this.mSafeParcelableFieldId = i4;
            if (str2 == null) {
                this.mConcreteType = null;
                this.mConcreteTypeName = null;
            } else {
                this.mConcreteType = SafeParcelResponse.class;
                this.mConcreteTypeName = str2;
            }
            if (converterWrapper == null) {
                this.zzwo = null;
            } else {
                this.zzwo = converterWrapper.unwrap();
            }
        }

        protected Field(int i, boolean z, int i2, boolean z2, String str, int i3, Class<? extends FastJsonResponse> cls, FieldConverter<I, O> fieldConverter) {
            this.zzal = 1;
            this.mTypeIn = i;
            this.mTypeInArray = z;
            this.mTypeOut = i2;
            this.mTypeOutArray = z2;
            this.mOutputFieldName = str;
            this.mSafeParcelableFieldId = i3;
            this.mConcreteType = cls;
            this.mConcreteTypeName = cls == null ? null : cls.getCanonicalName();
            this.zzwo = fieldConverter;
        }

        public static Field<byte[], byte[]> forBase64(String str) {
            return new Field(8, false, 8, false, str, -1, null, null);
        }

        @VisibleForTesting
        public static Field<byte[], byte[]> forBase64(String str, int i) {
            return new Field(8, false, 8, false, str, i, null, null);
        }

        @VisibleForTesting
        public static Field<byte[], byte[]> forBase64UrlSafe(String str) {
            return new Field(9, false, 9, false, str, -1, null, null);
        }

        @VisibleForTesting
        public static Field<byte[], byte[]> forBase64UrlSafe(String str, int i) {
            return new Field(9, false, 9, false, str, i, null, null);
        }

        public static Field<BigDecimal, BigDecimal> forBigDecimal(String str) {
            return new Field(5, false, 5, false, str, -1, null, null);
        }

        @VisibleForTesting
        public static Field<BigDecimal, BigDecimal> forBigDecimal(String str, int i) {
            return new Field(5, false, 5, false, str, i, null, null);
        }

        public static Field<ArrayList<BigDecimal>, ArrayList<BigDecimal>> forBigDecimals(String str) {
            return new Field(5, true, 5, true, str, -1, null, null);
        }

        public static Field<ArrayList<BigDecimal>, ArrayList<BigDecimal>> forBigDecimals(String str, int i) {
            return new Field(5, true, 5, true, str, i, null, null);
        }

        public static Field<BigInteger, BigInteger> forBigInteger(String str) {
            return new Field(1, false, 1, false, str, -1, null, null);
        }

        public static Field<BigInteger, BigInteger> forBigInteger(String str, int i) {
            return new Field(1, false, 1, false, str, i, null, null);
        }

        public static Field<ArrayList<BigInteger>, ArrayList<BigInteger>> forBigIntegers(String str) {
            return new Field(0, true, 1, true, str, -1, null, null);
        }

        public static Field<ArrayList<BigInteger>, ArrayList<BigInteger>> forBigIntegers(String str, int i) {
            return new Field(0, true, 1, true, str, i, null, null);
        }

        public static Field<Boolean, Boolean> forBoolean(String str) {
            return new Field(6, false, 6, false, str, -1, null, null);
        }

        public static Field<Boolean, Boolean> forBoolean(String str, int i) {
            return new Field(6, false, 6, false, str, i, null, null);
        }

        public static Field<ArrayList<Boolean>, ArrayList<Boolean>> forBooleans(String str) {
            return new Field(6, true, 6, true, str, -1, null, null);
        }

        public static Field<ArrayList<Boolean>, ArrayList<Boolean>> forBooleans(String str, int i) {
            return new Field(6, true, 6, true, str, i, null, null);
        }

        public static <T extends FastJsonResponse> Field<T, T> forConcreteType(String str, int i, Class<T> cls) {
            return new Field(11, false, 11, false, str, i, cls, null);
        }

        public static <T extends FastJsonResponse> Field<T, T> forConcreteType(String str, Class<T> cls) {
            return new Field(11, false, 11, false, str, -1, cls, null);
        }

        public static <T extends FastJsonResponse> Field<ArrayList<T>, ArrayList<T>> forConcreteTypeArray(String str, int i, Class<T> cls) {
            return new Field(11, true, 11, true, str, i, cls, null);
        }

        public static <T extends FastJsonResponse> Field<ArrayList<T>, ArrayList<T>> forConcreteTypeArray(String str, Class<T> cls) {
            return new Field(11, true, 11, true, str, -1, cls, null);
        }

        public static Field<Double, Double> forDouble(String str) {
            return new Field(4, false, 4, false, str, -1, null, null);
        }

        public static Field<Double, Double> forDouble(String str, int i) {
            return new Field(4, false, 4, false, str, i, null, null);
        }

        public static Field<ArrayList<Double>, ArrayList<Double>> forDoubles(String str) {
            return new Field(4, true, 4, true, str, -1, null, null);
        }

        public static Field<ArrayList<Double>, ArrayList<Double>> forDoubles(String str, int i) {
            return new Field(4, true, 4, true, str, i, null, null);
        }

        public static Field<Float, Float> forFloat(String str) {
            return new Field(3, false, 3, false, str, -1, null, null);
        }

        public static Field<Float, Float> forFloat(String str, int i) {
            return new Field(3, false, 3, false, str, i, null, null);
        }

        public static Field<ArrayList<Float>, ArrayList<Float>> forFloats(String str) {
            return new Field(3, true, 3, true, str, -1, null, null);
        }

        public static Field<ArrayList<Float>, ArrayList<Float>> forFloats(String str, int i) {
            return new Field(3, true, 3, true, str, i, null, null);
        }

        public static Field<Integer, Integer> forInteger(String str) {
            return new Field(0, false, 0, false, str, -1, null, null);
        }

        @VisibleForTesting
        public static Field<Integer, Integer> forInteger(String str, int i) {
            return new Field(0, false, 0, false, str, i, null, null);
        }

        public static Field<ArrayList<Integer>, ArrayList<Integer>> forIntegers(String str) {
            return new Field(0, true, 0, true, str, -1, null, null);
        }

        @VisibleForTesting
        public static Field<ArrayList<Integer>, ArrayList<Integer>> forIntegers(String str, int i) {
            return new Field(0, true, 0, true, str, i, null, null);
        }

        @VisibleForTesting
        public static Field<Long, Long> forLong(String str) {
            return new Field(2, false, 2, false, str, -1, null, null);
        }

        public static Field<Long, Long> forLong(String str, int i) {
            return new Field(2, false, 2, false, str, i, null, null);
        }

        @VisibleForTesting
        public static Field<ArrayList<Long>, ArrayList<Long>> forLongs(String str) {
            return new Field(2, true, 2, true, str, -1, null, null);
        }

        public static Field<ArrayList<Long>, ArrayList<Long>> forLongs(String str, int i) {
            return new Field(2, true, 2, true, str, i, null, null);
        }

        public static Field<String, String> forString(String str) {
            return new Field(7, false, 7, false, str, -1, null, null);
        }

        public static Field<String, String> forString(String str, int i) {
            return new Field(7, false, 7, false, str, i, null, null);
        }

        public static Field<HashMap<String, String>, HashMap<String, String>> forStringMap(String str) {
            return new Field(10, false, 10, false, str, -1, null, null);
        }

        public static Field<HashMap<String, String>, HashMap<String, String>> forStringMap(String str, int i) {
            return new Field(10, false, 10, false, str, i, null, null);
        }

        public static Field<ArrayList<String>, ArrayList<String>> forStrings(String str) {
            return new Field(7, true, 7, true, str, -1, null, null);
        }

        public static Field<ArrayList<String>, ArrayList<String>> forStrings(String str, int i) {
            return new Field(7, true, 7, true, str, i, null, null);
        }

        public static Field withConverter(String str, int i, FieldConverter<?, ?> fieldConverter, boolean z) {
            return new Field(fieldConverter.getTypeIn(), z, fieldConverter.getTypeOut(), false, str, i, null, fieldConverter);
        }

        public static <T extends FieldConverter> Field withConverter(String str, int i, Class<T> cls, boolean z) {
            try {
                return withConverter(str, i, (FieldConverter) cls.newInstance(), z);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            } catch (Throwable e2) {
                throw new RuntimeException(e2);
            }
        }

        public static Field withConverter(String str, FieldConverter<?, ?> fieldConverter, boolean z) {
            return withConverter(str, -1, (FieldConverter) fieldConverter, z);
        }

        public static <T extends FieldConverter> Field withConverter(String str, Class<T> cls, boolean z) {
            return withConverter(str, -1, (Class) cls, z);
        }

        private final String zzcz() {
            return this.mConcreteTypeName == null ? null : this.mConcreteTypeName;
        }

        private final ConverterWrapper zzda() {
            return this.zzwo == null ? null : ConverterWrapper.wrap(this.zzwo);
        }

        public O convert(I i) {
            return this.zzwo.convert(i);
        }

        public I convertBack(O o) {
            return this.zzwo.convertBack(o);
        }

        public Field<I, O> copyForDictionary() {
            return new Field(this.zzal, this.mTypeIn, this.mTypeInArray, this.mTypeOut, this.mTypeOutArray, this.mOutputFieldName, this.mSafeParcelableFieldId, this.mConcreteTypeName, zzda());
        }

        public Class<? extends FastJsonResponse> getConcreteType() {
            return this.mConcreteType;
        }

        public Map<String, Field<?, ?>> getConcreteTypeFieldMappingFromDictionary() {
            Preconditions.checkNotNull(this.mConcreteTypeName);
            Preconditions.checkNotNull(this.zzwn);
            return this.zzwn.getFieldMapping(this.mConcreteTypeName);
        }

        public String getOutputFieldName() {
            return this.mOutputFieldName;
        }

        public int getSafeParcelableFieldId() {
            return this.mSafeParcelableFieldId;
        }

        public int getTypeIn() {
            return this.mTypeIn;
        }

        public int getTypeOut() {
            return this.mTypeOut;
        }

        public int getVersionCode() {
            return this.zzal;
        }

        public boolean hasConverter() {
            return this.zzwo != null;
        }

        public boolean isTypeInArray() {
            return this.mTypeInArray;
        }

        public boolean isTypeOutArray() {
            return this.mTypeOutArray;
        }

        public boolean isValidSafeParcelableFieldId() {
            return this.mSafeParcelableFieldId != -1;
        }

        public FastJsonResponse newConcreteTypeInstance() throws InstantiationException, IllegalAccessException {
            if (this.mConcreteType != SafeParcelResponse.class) {
                return (FastJsonResponse) this.mConcreteType.newInstance();
            }
            Preconditions.checkNotNull(this.zzwn, "The field mapping dictionary must be set if the concrete type is a SafeParcelResponse object.");
            return new SafeParcelResponse(this.zzwn, this.mConcreteTypeName);
        }

        public void setFieldMappingDictionary(FieldMappingDictionary fieldMappingDictionary) {
            this.zzwn = fieldMappingDictionary;
        }

        public String toString() {
            ToStringHelper add = Objects.toStringHelper(this).add("versionCode", Integer.valueOf(this.zzal)).add("typeIn", Integer.valueOf(this.mTypeIn)).add("typeInArray", Boolean.valueOf(this.mTypeInArray)).add("typeOut", Integer.valueOf(this.mTypeOut)).add("typeOutArray", Boolean.valueOf(this.mTypeOutArray)).add("outputFieldName", this.mOutputFieldName).add("safeParcelFieldId", Integer.valueOf(this.mSafeParcelableFieldId)).add("concreteTypeName", zzcz());
            Class concreteType = getConcreteType();
            if (concreteType != null) {
                add.add("concreteType.class", concreteType.getCanonicalName());
            }
            if (this.zzwo != null) {
                add.add("converterName", this.zzwo.getClass().getCanonicalName());
            }
            return add.toString();
        }

        public void writeToParcel(Parcel parcel, int i) {
            int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
            SafeParcelWriter.writeInt(parcel, 1, getVersionCode());
            SafeParcelWriter.writeInt(parcel, 2, getTypeIn());
            SafeParcelWriter.writeBoolean(parcel, 3, isTypeInArray());
            SafeParcelWriter.writeInt(parcel, 4, getTypeOut());
            SafeParcelWriter.writeBoolean(parcel, 5, isTypeOutArray());
            SafeParcelWriter.writeString(parcel, 6, getOutputFieldName(), false);
            SafeParcelWriter.writeInt(parcel, 7, getSafeParcelableFieldId());
            SafeParcelWriter.writeString(parcel, 8, zzcz(), false);
            SafeParcelWriter.writeParcelable(parcel, 9, zzda(), i, false);
            SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
        }
    }

    public interface FieldType {
        public static final int BASE64 = 8;
        public static final int BASE64_URL_SAFE = 9;
        public static final int BIG_DECIMAL = 5;
        public static final int BIG_INTEGER = 1;
        public static final int BOOLEAN = 6;
        public static final int CONCRETE_TYPE = 11;
        public static final int DOUBLE = 4;
        public static final int FLOAT = 3;
        public static final int INT = 0;
        public static final int LONG = 2;
        public static final int STRING = 7;
        public static final int STRING_MAP = 10;
    }

    public static java.io.InputStream getUnzippedStream(byte[] r1) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = new java.io.ByteArrayInputStream;
        r0.<init>(r1);
        r1 = com.google.android.gms.common.util.IOUtils.isGzipByteBuffer(r1);
        if (r1 == 0) goto L_0x0011;
    L_0x000b:
        r1 = new java.util.zip.GZIPInputStream;	 Catch:{ IOException -> 0x0011 }
        r1.<init>(r0);	 Catch:{ IOException -> 0x0011 }
        return r1;
    L_0x0011:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.FastJsonResponse.getUnzippedStream(byte[]):java.io.InputStream");
    }

    private final <I, O> void zza(Field<I, O> field, I i) {
        String outputFieldName = field.getOutputFieldName();
        Object convert = field.convert(i);
        switch (field.getTypeOut()) {
            case 0:
                if (zzb(outputFieldName, convert)) {
                    setIntegerInternal(field, outputFieldName, ((Integer) convert).intValue());
                    break;
                }
                break;
            case 1:
                setBigIntegerInternal(field, outputFieldName, (BigInteger) convert);
                return;
            case 2:
                if (zzb(outputFieldName, convert)) {
                    setLongInternal(field, outputFieldName, ((Long) convert).longValue());
                    return;
                }
                break;
            case 4:
                if (zzb(outputFieldName, convert)) {
                    setDoubleInternal(field, outputFieldName, ((Double) convert).doubleValue());
                    return;
                }
                break;
            case 5:
                setBigDecimalInternal(field, outputFieldName, (BigDecimal) convert);
                return;
            case 6:
                if (zzb(outputFieldName, convert)) {
                    setBooleanInternal(field, outputFieldName, ((Boolean) convert).booleanValue());
                    return;
                }
                break;
            case 7:
                setStringInternal(field, outputFieldName, (String) convert);
                return;
            case 8:
            case 9:
                if (zzb(outputFieldName, convert)) {
                    setDecodedBytesInternal(field, outputFieldName, (byte[]) convert);
                    return;
                }
                break;
            default:
                int typeOut = field.getTypeOut();
                StringBuilder stringBuilder = new StringBuilder(44);
                stringBuilder.append("Unsupported type for conversion: ");
                stringBuilder.append(typeOut);
                throw new IllegalStateException(stringBuilder.toString());
        }
    }

    private static void zza(StringBuilder stringBuilder, Field field, Object obj) {
        String fastJsonResponse;
        if (field.getTypeIn() == 11) {
            fastJsonResponse = ((FastJsonResponse) field.getConcreteType().cast(obj)).toString();
        } else if (field.getTypeIn() == 7) {
            stringBuilder.append(QUOTE);
            stringBuilder.append(JsonUtils.escapeString((String) obj));
            fastJsonResponse = QUOTE;
        } else {
            stringBuilder.append(obj);
            return;
        }
        stringBuilder.append(fastJsonResponse);
    }

    private static <O> boolean zzb(String str, O o) {
        if (o != null) {
            return true;
        }
        if (Log.isLoggable("FastJsonResponse", 6)) {
            StringBuilder stringBuilder = new StringBuilder(String.valueOf(str).length() + 58);
            stringBuilder.append("Output field (");
            stringBuilder.append(str);
            stringBuilder.append(") has a null value, but expected a primitive");
            Log.e("FastJsonResponse", stringBuilder.toString());
        }
        return false;
    }

    public <T extends FastJsonResponse> void addConcreteType(String str, T t) {
        throw new UnsupportedOperationException("Concrete type not supported");
    }

    public <T extends FastJsonResponse> void addConcreteTypeArray(String str, ArrayList<T> arrayList) {
        throw new UnsupportedOperationException("Concrete type array not supported");
    }

    public <T extends FastJsonResponse> void addConcreteTypeArrayInternal(Field<?, ?> field, String str, ArrayList<T> arrayList) {
        addConcreteTypeArray(str, arrayList);
    }

    public <T extends FastJsonResponse> void addConcreteTypeInternal(Field<?, ?> field, String str, T t) {
        addConcreteType(str, t);
    }

    public HashMap<String, Object> getConcreteTypeArrays() {
        return null;
    }

    public HashMap<String, Object> getConcreteTypes() {
        return null;
    }

    public abstract Map<String, Field<?, ?>> getFieldMappings();

    protected Object getFieldValue(Field field) {
        String outputFieldName = field.getOutputFieldName();
        if (field.getConcreteType() == null) {
            return getValueObject(field.getOutputFieldName());
        }
        Preconditions.checkState(getValueObject(field.getOutputFieldName()) == null, "Concrete field shouldn't be value object: %s", field.getOutputFieldName());
        Map concreteTypeArrays = field.isTypeOutArray() ? getConcreteTypeArrays() : getConcreteTypes();
        if (concreteTypeArrays != null) {
            return concreteTypeArrays.get(outputFieldName);
        }
        try {
            char toUpperCase = Character.toUpperCase(outputFieldName.charAt(0));
            outputFieldName = outputFieldName.substring(1);
            StringBuilder stringBuilder = new StringBuilder(String.valueOf(outputFieldName).length() + 4);
            stringBuilder.append("get");
            stringBuilder.append(toUpperCase);
            stringBuilder.append(outputFieldName);
            return getClass().getMethod(stringBuilder.toString(), new Class[0]).invoke(this, new Object[0]);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    protected <O, I> I getOriginalValue(Field<I, O> field, Object obj) {
        return field.zzwo != null ? field.convertBack(obj) : obj;
    }

    public PostProcessor<? extends FastJsonResponse> getPostProcessor() {
        return null;
    }

    public byte[] getResponseBody() {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r8 = this;
        r0 = r8.zzwm;
        com.google.android.gms.common.internal.Preconditions.checkState(r0);
        r0 = 0;
        r1 = new java.util.zip.GZIPInputStream;	 Catch:{ IOException -> 0x003b }
        r2 = new java.io.ByteArrayInputStream;	 Catch:{ IOException -> 0x003b }
        r3 = r8.zzwl;	 Catch:{ IOException -> 0x003b }
        r2.<init>(r3);	 Catch:{ IOException -> 0x003b }
        r1.<init>(r2);	 Catch:{ IOException -> 0x003b }
        r0 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r2 = new byte[r0];	 Catch:{ IOException -> 0x0034, all -> 0x0032 }
        r3 = new java.io.ByteArrayOutputStream;	 Catch:{ IOException -> 0x0034, all -> 0x0032 }
        r3.<init>();	 Catch:{ IOException -> 0x0034, all -> 0x0032 }
    L_0x001b:
        r4 = 0;	 Catch:{ IOException -> 0x0034, all -> 0x0032 }
        r5 = r1.read(r2, r4, r0);	 Catch:{ IOException -> 0x0034, all -> 0x0032 }
        r6 = -1;	 Catch:{ IOException -> 0x0034, all -> 0x0032 }
        if (r5 == r6) goto L_0x0027;	 Catch:{ IOException -> 0x0034, all -> 0x0032 }
    L_0x0023:
        r3.write(r2, r4, r5);	 Catch:{ IOException -> 0x0034, all -> 0x0032 }
        goto L_0x001b;	 Catch:{ IOException -> 0x0034, all -> 0x0032 }
    L_0x0027:
        r3.flush();	 Catch:{ IOException -> 0x0034, all -> 0x0032 }
        r0 = r3.toByteArray();	 Catch:{ IOException -> 0x0034, all -> 0x0032 }
        r1.close();	 Catch:{ IOException -> 0x0031 }
    L_0x0031:
        return r0;
    L_0x0032:
        r0 = move-exception;
        goto L_0x0043;
    L_0x0034:
        r0 = r1;
        goto L_0x003b;
    L_0x0036:
        r1 = move-exception;
        r7 = r1;
        r1 = r0;
        r0 = r7;
        goto L_0x0043;
    L_0x003b:
        r1 = r8.zzwl;	 Catch:{ all -> 0x0036 }
        if (r0 == 0) goto L_0x0042;
    L_0x003f:
        r0.close();	 Catch:{ IOException -> 0x0042 }
    L_0x0042:
        return r1;
    L_0x0043:
        if (r1 == 0) goto L_0x0048;
    L_0x0045:
        r1.close();	 Catch:{ IOException -> 0x0048 }
    L_0x0048:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.FastJsonResponse.getResponseBody():byte[]");
    }

    public int getResponseCode() {
        Preconditions.checkState(this.zzwm);
        return this.zzwk;
    }

    protected abstract Object getValueObject(String str);

    protected boolean isConcreteTypeArrayFieldSet(String str) {
        throw new UnsupportedOperationException("Concrete type arrays not supported");
    }

    protected boolean isConcreteTypeFieldSet(String str) {
        throw new UnsupportedOperationException("Concrete types not supported");
    }

    protected boolean isFieldSet(Field field) {
        return field.getTypeOut() == 11 ? field.isTypeOutArray() ? isConcreteTypeArrayFieldSet(field.getOutputFieldName()) : isConcreteTypeFieldSet(field.getOutputFieldName()) : isPrimitiveFieldSet(field.getOutputFieldName());
    }

    protected abstract boolean isPrimitiveFieldSet(String str);

    public <T extends com.google.android.gms.common.server.response.FastJsonResponse> void parseNetworkResponse(int r1, byte[] r2) throws com.google.android.gms.common.server.response.FastParser.ParseException {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = this;
        r0.zzwk = r1;
        r0.zzwl = r2;
        r1 = 1;
        r0.zzwm = r1;
        r1 = getUnzippedStream(r2);
        r2 = new com.google.android.gms.common.server.response.FastParser;	 Catch:{ all -> 0x0017 }
        r2.<init>();	 Catch:{ all -> 0x0017 }
        r2.parse(r1, r0);	 Catch:{ all -> 0x0017 }
        r1.close();	 Catch:{ IOException -> 0x0016 }
    L_0x0016:
        return;
    L_0x0017:
        r2 = move-exception;
        r1.close();	 Catch:{ IOException -> 0x001b }
    L_0x001b:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.FastJsonResponse.parseNetworkResponse(int, byte[]):void");
    }

    public final <O> void setBigDecimal(Field<BigDecimal, O> field, BigDecimal bigDecimal) {
        if (field.zzwo != null) {
            zza(field, bigDecimal);
        } else {
            setBigDecimalInternal(field, field.getOutputFieldName(), bigDecimal);
        }
    }

    protected void setBigDecimal(String str, BigDecimal bigDecimal) {
        throw new UnsupportedOperationException("BigDecimal not supported");
    }

    protected void setBigDecimalInternal(Field<?, ?> field, String str, BigDecimal bigDecimal) {
        setBigDecimal(str, bigDecimal);
    }

    public final <O> void setBigDecimals(Field<ArrayList<BigDecimal>, O> field, ArrayList<BigDecimal> arrayList) {
        if (field.zzwo != null) {
            zza(field, arrayList);
        } else {
            setBigDecimalsInternal(field, field.getOutputFieldName(), arrayList);
        }
    }

    protected void setBigDecimals(String str, ArrayList<BigDecimal> arrayList) {
        throw new UnsupportedOperationException("BigDecimal list not supported");
    }

    protected void setBigDecimalsInternal(Field<?, ?> field, String str, ArrayList<BigDecimal> arrayList) {
        setBigDecimals(str, (ArrayList) arrayList);
    }

    public final <O> void setBigInteger(Field<BigInteger, O> field, BigInteger bigInteger) {
        if (field.zzwo != null) {
            zza(field, bigInteger);
        } else {
            setBigIntegerInternal(field, field.getOutputFieldName(), bigInteger);
        }
    }

    protected void setBigInteger(String str, BigInteger bigInteger) {
        throw new UnsupportedOperationException("BigInteger not supported");
    }

    protected void setBigIntegerInternal(Field<?, ?> field, String str, BigInteger bigInteger) {
        setBigInteger(str, bigInteger);
    }

    public final <O> void setBigIntegers(Field<ArrayList<BigInteger>, O> field, ArrayList<BigInteger> arrayList) {
        if (field.zzwo != null) {
            zza(field, arrayList);
        } else {
            setBigIntegersInternal(field, field.getOutputFieldName(), arrayList);
        }
    }

    protected void setBigIntegers(String str, ArrayList<BigInteger> arrayList) {
        throw new UnsupportedOperationException("BigInteger list not supported");
    }

    protected void setBigIntegersInternal(Field<?, ?> field, String str, ArrayList<BigInteger> arrayList) {
        setBigIntegers(str, (ArrayList) arrayList);
    }

    public final <O> void setBoolean(Field<Boolean, O> field, boolean z) {
        if (field.zzwo != null) {
            zza(field, Boolean.valueOf(z));
        } else {
            setBooleanInternal(field, field.getOutputFieldName(), z);
        }
    }

    protected void setBoolean(String str, boolean z) {
        throw new UnsupportedOperationException("Boolean not supported");
    }

    protected void setBooleanInternal(Field<?, ?> field, String str, boolean z) {
        setBoolean(str, z);
    }

    public final <O> void setBooleans(Field<ArrayList<Boolean>, O> field, ArrayList<Boolean> arrayList) {
        if (field.zzwo != null) {
            zza(field, arrayList);
        } else {
            setBooleansInternal(field, field.getOutputFieldName(), arrayList);
        }
    }

    protected void setBooleans(String str, ArrayList<Boolean> arrayList) {
        throw new UnsupportedOperationException("Boolean list not supported");
    }

    protected void setBooleansInternal(Field<?, ?> field, String str, ArrayList<Boolean> arrayList) {
        setBooleans(str, (ArrayList) arrayList);
    }

    public final <O> void setDecodedBytes(Field<byte[], O> field, byte[] bArr) {
        if (field.zzwo != null) {
            zza(field, bArr);
        } else {
            setDecodedBytesInternal(field, field.getOutputFieldName(), bArr);
        }
    }

    protected void setDecodedBytes(String str, byte[] bArr) {
        throw new UnsupportedOperationException("byte[] not supported");
    }

    protected void setDecodedBytesInternal(Field<?, ?> field, String str, byte[] bArr) {
        setDecodedBytes(str, bArr);
    }

    public final <O> void setDouble(Field<Double, O> field, double d) {
        if (field.zzwo != null) {
            zza(field, Double.valueOf(d));
        } else {
            setDoubleInternal(field, field.getOutputFieldName(), d);
        }
    }

    protected void setDouble(String str, double d) {
        throw new UnsupportedOperationException("Double not supported");
    }

    protected void setDoubleInternal(Field<?, ?> field, String str, double d) {
        setDouble(str, d);
    }

    public final <O> void setDoubles(Field<ArrayList<Double>, O> field, ArrayList<Double> arrayList) {
        if (field.zzwo != null) {
            zza(field, arrayList);
        } else {
            setDoublesInternal(field, field.getOutputFieldName(), arrayList);
        }
    }

    protected void setDoubles(String str, ArrayList<Double> arrayList) {
        throw new UnsupportedOperationException("Double list not supported");
    }

    protected void setDoublesInternal(Field<?, ?> field, String str, ArrayList<Double> arrayList) {
        setDoubles(str, (ArrayList) arrayList);
    }

    public final <O> void setFloat(Field<Float, O> field, float f) {
        if (field.zzwo != null) {
            zza(field, Float.valueOf(f));
        } else {
            setFloatInternal(field, field.getOutputFieldName(), f);
        }
    }

    protected void setFloat(String str, float f) {
        throw new UnsupportedOperationException("Float not supported");
    }

    protected void setFloatInternal(Field<?, ?> field, String str, float f) {
        setFloat(str, f);
    }

    public final <O> void setFloats(Field<ArrayList<Float>, O> field, ArrayList<Float> arrayList) {
        if (field.zzwo != null) {
            zza(field, arrayList);
        } else {
            setFloatsInternal(field, field.getOutputFieldName(), arrayList);
        }
    }

    protected void setFloats(String str, ArrayList<Float> arrayList) {
        throw new UnsupportedOperationException("Float list not supported");
    }

    protected void setFloatsInternal(Field<?, ?> field, String str, ArrayList<Float> arrayList) {
        setFloats(str, (ArrayList) arrayList);
    }

    public final <O> void setInteger(Field<Integer, O> field, int i) {
        if (field.zzwo != null) {
            zza(field, Integer.valueOf(i));
        } else {
            setIntegerInternal(field, field.getOutputFieldName(), i);
        }
    }

    protected void setInteger(String str, int i) {
        throw new UnsupportedOperationException("Integer not supported");
    }

    protected void setIntegerInternal(Field<?, ?> field, String str, int i) {
        setInteger(str, i);
    }

    public final <O> void setIntegers(Field<ArrayList<Integer>, O> field, ArrayList<Integer> arrayList) {
        if (field.zzwo != null) {
            zza(field, arrayList);
        } else {
            setIntegersInternal(field, field.getOutputFieldName(), arrayList);
        }
    }

    protected void setIntegers(String str, ArrayList<Integer> arrayList) {
        throw new UnsupportedOperationException("Integer list not supported");
    }

    protected void setIntegersInternal(Field<?, ?> field, String str, ArrayList<Integer> arrayList) {
        setIntegers(str, (ArrayList) arrayList);
    }

    public final <O> void setLong(Field<Long, O> field, long j) {
        if (field.zzwo != null) {
            zza(field, Long.valueOf(j));
        } else {
            setLongInternal(field, field.getOutputFieldName(), j);
        }
    }

    protected void setLong(String str, long j) {
        throw new UnsupportedOperationException("Long not supported");
    }

    protected void setLongInternal(Field<?, ?> field, String str, long j) {
        setLong(str, j);
    }

    public final <O> void setLongs(Field<ArrayList<Long>, O> field, ArrayList<Long> arrayList) {
        if (field.zzwo != null) {
            zza(field, arrayList);
        } else {
            setLongsInternal(field, field.getOutputFieldName(), arrayList);
        }
    }

    protected void setLongs(String str, ArrayList<Long> arrayList) {
        throw new UnsupportedOperationException("Long list not supported");
    }

    protected void setLongsInternal(Field<?, ?> field, String str, ArrayList<Long> arrayList) {
        setLongs(str, (ArrayList) arrayList);
    }

    public final <O> void setString(Field<String, O> field, String str) {
        if (field.zzwo != null) {
            zza(field, str);
        } else {
            setStringInternal(field, field.getOutputFieldName(), str);
        }
    }

    protected void setString(String str, String str2) {
        throw new UnsupportedOperationException("String not supported");
    }

    protected void setStringInternal(Field<?, ?> field, String str, String str2) {
        setString(str, str2);
    }

    public final <O> void setStringMap(Field<Map<String, String>, O> field, Map<String, String> map) {
        if (field.zzwo != null) {
            zza(field, map);
        } else {
            setStringMapInternal(field, field.getOutputFieldName(), map);
        }
    }

    protected void setStringMap(String str, Map<String, String> map) {
        throw new UnsupportedOperationException("String map not supported");
    }

    protected void setStringMapInternal(Field<?, ?> field, String str, Map<String, String> map) {
        setStringMap(str, (Map) map);
    }

    public final <O> void setStrings(Field<ArrayList<String>, O> field, ArrayList<String> arrayList) {
        if (field.zzwo != null) {
            zza(field, arrayList);
        } else {
            setStringsInternal(field, field.getOutputFieldName(), arrayList);
        }
    }

    protected void setStrings(String str, ArrayList<String> arrayList) {
        throw new UnsupportedOperationException("String list not supported");
    }

    protected void setStringsInternal(Field<?, ?> field, String str, ArrayList<String> arrayList) {
        setStrings(str, (ArrayList) arrayList);
    }

    public String toString() {
        Map fieldMappings = getFieldMappings();
        StringBuilder stringBuilder = new StringBuilder(100);
        for (String str : fieldMappings.keySet()) {
            String str2;
            Field field = (Field) fieldMappings.get(str2);
            if (isFieldSet(field)) {
                Object originalValue = getOriginalValue(field, getFieldValue(field));
                stringBuilder.append(stringBuilder.length() == 0 ? "{" : ",");
                stringBuilder.append(QUOTE);
                stringBuilder.append(str2);
                stringBuilder.append("\":");
                if (originalValue == null) {
                    str2 = "null";
                } else {
                    switch (field.getTypeOut()) {
                        case 8:
                            stringBuilder.append(QUOTE);
                            str2 = Base64Utils.encode((byte[]) originalValue);
                            break;
                        case 9:
                            stringBuilder.append(QUOTE);
                            str2 = Base64Utils.encodeUrlSafe((byte[]) originalValue);
                            break;
                        case 10:
                            MapUtils.writeStringMapToJson(stringBuilder, (HashMap) originalValue);
                            continue;
                        default:
                            if (field.isTypeInArray()) {
                                ArrayList arrayList = (ArrayList) originalValue;
                                stringBuilder.append("[");
                                int size = arrayList.size();
                                for (int i = 0; i < size; i++) {
                                    if (i > 0) {
                                        stringBuilder.append(",");
                                    }
                                    Object obj = arrayList.get(i);
                                    if (obj != null) {
                                        zza(stringBuilder, field, obj);
                                    }
                                }
                                str2 = "]";
                                break;
                            }
                            zza(stringBuilder, field, originalValue);
                            continue;
                    }
                    stringBuilder.append(str2);
                    str2 = QUOTE;
                }
                stringBuilder.append(str2);
            }
        }
        stringBuilder.append(stringBuilder.length() > 0 ? "}" : FetchDefaults.EMPTY_JSON_OBJECT_STRING);
        return stringBuilder.toString();
    }
}
