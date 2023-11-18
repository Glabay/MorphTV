package com.google.android.gms.common.server.response;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;
import com.google.android.gms.common.util.Base64Utils;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.common.util.MapUtils;
import com.google.android.gms.common.util.VisibleForTesting;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Class(creator = "SafeParcelResponseCreator")
@VisibleForTesting
public class SafeParcelResponse extends FastSafeParcelableJsonResponse {
    public static final Creator<SafeParcelResponse> CREATOR = new SafeParcelResponseCreator();
    private final String mClassName;
    @VersionField(getter = "getVersionCode", id = 1)
    private final int zzal;
    @Field(getter = "getFieldMappingDictionary", id = 3)
    private final FieldMappingDictionary zzwn;
    @Field(getter = "getParcel", id = 2)
    private final Parcel zzxq;
    private final int zzxr;
    private int zzxs;
    private int zzxt;

    @Constructor
    SafeParcelResponse(@Param(id = 1) int i, @Param(id = 2) Parcel parcel, @Param(id = 3) FieldMappingDictionary fieldMappingDictionary) {
        this.zzal = i;
        this.zzxq = (Parcel) Preconditions.checkNotNull(parcel);
        this.zzxr = 2;
        this.zzwn = fieldMappingDictionary;
        this.mClassName = this.zzwn == null ? null : this.zzwn.getRootClassName();
        this.zzxs = 2;
    }

    private SafeParcelResponse(SafeParcelable safeParcelable, FieldMappingDictionary fieldMappingDictionary, String str) {
        this.zzal = 1;
        this.zzxq = Parcel.obtain();
        safeParcelable.writeToParcel(this.zzxq, 0);
        this.zzxr = 1;
        this.zzwn = (FieldMappingDictionary) Preconditions.checkNotNull(fieldMappingDictionary);
        this.mClassName = (String) Preconditions.checkNotNull(str);
        this.zzxs = 2;
    }

    public SafeParcelResponse(FieldMappingDictionary fieldMappingDictionary) {
        this(fieldMappingDictionary, fieldMappingDictionary.getRootClassName());
    }

    public SafeParcelResponse(FieldMappingDictionary fieldMappingDictionary, String str) {
        this.zzal = 1;
        this.zzxq = Parcel.obtain();
        this.zzxr = 0;
        this.zzwn = (FieldMappingDictionary) Preconditions.checkNotNull(fieldMappingDictionary);
        this.mClassName = (String) Preconditions.checkNotNull(str);
        this.zzxs = 0;
    }

    public static HashMap<String, String> convertBundleToStringMap(Bundle bundle) {
        HashMap<String, String> hashMap = new HashMap();
        for (String str : bundle.keySet()) {
            hashMap.put(str, bundle.getString(str));
        }
        return hashMap;
    }

    public static Bundle convertStringMapToBundle(HashMap<String, String> hashMap) {
        Bundle bundle = new Bundle();
        for (String str : hashMap.keySet()) {
            bundle.putString(str, (String) hashMap.get(str));
        }
        return bundle;
    }

    public static <T extends FastJsonResponse & SafeParcelable> SafeParcelResponse from(T t) {
        String canonicalName = t.getClass().getCanonicalName();
        return new SafeParcelResponse((SafeParcelable) t, zza(t), canonicalName);
    }

    public static FieldMappingDictionary generateDictionary(Class<? extends FastJsonResponse> cls) {
        String str;
        String valueOf;
        try {
            return zza((FastJsonResponse) cls.newInstance());
        } catch (Throwable e) {
            str = "Could not instantiate an object of type ";
            valueOf = String.valueOf(cls.getCanonicalName());
            throw new IllegalStateException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), e);
        } catch (Throwable e2) {
            str = "Could not access object of type ";
            valueOf = String.valueOf(cls.getCanonicalName());
            throw new IllegalStateException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), e2);
        }
    }

    private static FieldMappingDictionary zza(FastJsonResponse fastJsonResponse) {
        FieldMappingDictionary fieldMappingDictionary = new FieldMappingDictionary(fastJsonResponse.getClass());
        zza(fieldMappingDictionary, fastJsonResponse);
        fieldMappingDictionary.copyInternalFieldMappings();
        fieldMappingDictionary.linkFields();
        return fieldMappingDictionary;
    }

    private static void zza(FieldMappingDictionary fieldMappingDictionary, FastJsonResponse fastJsonResponse) {
        String str;
        Class cls = fastJsonResponse.getClass();
        if (!fieldMappingDictionary.hasFieldMappingForClass(cls)) {
            Map fieldMappings = fastJsonResponse.getFieldMappings();
            fieldMappingDictionary.put(cls, fieldMappings);
            for (String str2 : fieldMappings.keySet()) {
                String str22;
                FastJsonResponse.Field field = (FastJsonResponse.Field) fieldMappings.get(str22);
                Class concreteType = field.getConcreteType();
                if (concreteType != null) {
                    try {
                        zza(fieldMappingDictionary, (FastJsonResponse) concreteType.newInstance());
                    } catch (Throwable e) {
                        str = "Could not instantiate an object of type ";
                        str22 = String.valueOf(field.getConcreteType().getCanonicalName());
                        throw new IllegalStateException(str22.length() != 0 ? str.concat(str22) : new String(str), e);
                    } catch (Throwable e2) {
                        str = "Could not access object of type ";
                        str22 = String.valueOf(field.getConcreteType().getCanonicalName());
                        throw new IllegalStateException(str22.length() != 0 ? str.concat(str22) : new String(str), e2);
                    }
                }
            }
        }
    }

    private static void zza(StringBuilder stringBuilder, int i, Object obj) {
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                stringBuilder.append(obj);
                return;
            case 7:
                stringBuilder.append("\"");
                stringBuilder.append(JsonUtils.escapeString(obj.toString()));
                stringBuilder.append("\"");
                return;
            case 8:
                stringBuilder.append("\"");
                stringBuilder.append(Base64Utils.encode((byte[]) obj));
                stringBuilder.append("\"");
                return;
            case 9:
                stringBuilder.append("\"");
                stringBuilder.append(Base64Utils.encodeUrlSafe((byte[]) obj));
                stringBuilder.append("\"");
                return;
            case 10:
                MapUtils.writeStringMapToJson(stringBuilder, (HashMap) obj);
                return;
            case 11:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                StringBuilder stringBuilder2 = new StringBuilder(26);
                stringBuilder2.append("Unknown type = ");
                stringBuilder2.append(i);
                throw new IllegalArgumentException(stringBuilder2.toString());
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void zza(java.lang.StringBuilder r10, java.util.Map<java.lang.String, com.google.android.gms.common.server.response.FastJsonResponse.Field<?, ?>> r11, android.os.Parcel r12) {
        /*
        r9 = this;
        r0 = new android.util.SparseArray;
        r0.<init>();
        r11 = r11.entrySet();
        r11 = r11.iterator();
    L_0x000d:
        r1 = r11.hasNext();
        if (r1 == 0) goto L_0x0027;
    L_0x0013:
        r1 = r11.next();
        r1 = (java.util.Map.Entry) r1;
        r2 = r1.getValue();
        r2 = (com.google.android.gms.common.server.response.FastJsonResponse.Field) r2;
        r2 = r2.getSafeParcelableFieldId();
        r0.put(r2, r1);
        goto L_0x000d;
    L_0x0027:
        r11 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r10.append(r11);
        r11 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.validateObjectHeader(r12);
        r1 = 1;
        r2 = 0;
        r3 = 0;
    L_0x0033:
        r4 = r12.dataPosition();
        if (r4 >= r11) goto L_0x024b;
    L_0x0039:
        r4 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readHeader(r12);
        r5 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.getFieldId(r4);
        r5 = r0.get(r5);
        r5 = (java.util.Map.Entry) r5;
        if (r5 == 0) goto L_0x0033;
    L_0x0049:
        if (r3 == 0) goto L_0x0050;
    L_0x004b:
        r3 = ",";
        r10.append(r3);
    L_0x0050:
        r3 = r5.getKey();
        r3 = (java.lang.String) r3;
        r5 = r5.getValue();
        r5 = (com.google.android.gms.common.server.response.FastJsonResponse.Field) r5;
        r6 = "\"";
        r10.append(r6);
        r10.append(r3);
        r3 = "\":";
        r10.append(r3);
        r3 = r5.hasConverter();
        if (r3 == 0) goto L_0x00ed;
    L_0x006f:
        r3 = r5.getTypeOut();
        switch(r3) {
            case 0: goto L_0x00dc;
            case 1: goto L_0x00d7;
            case 2: goto L_0x00ce;
            case 3: goto L_0x00c5;
            case 4: goto L_0x00bc;
            case 5: goto L_0x00b7;
            case 6: goto L_0x00ae;
            case 7: goto L_0x00a9;
            case 8: goto L_0x00a4;
            case 9: goto L_0x00a4;
            case 10: goto L_0x009b;
            case 11: goto L_0x0093;
            default: goto L_0x0076;
        };
    L_0x0076:
        r10 = new java.lang.IllegalArgumentException;
        r11 = r5.getTypeOut();
        r12 = 36;
        r0 = new java.lang.StringBuilder;
        r0.<init>(r12);
        r12 = "Unknown field out type = ";
        r0.append(r12);
        r0.append(r11);
        r11 = r0.toString();
        r10.<init>(r11);
        throw r10;
    L_0x0093:
        r10 = new java.lang.IllegalArgumentException;
        r11 = "Method does not accept concrete type.";
        r10.<init>(r11);
        throw r10;
    L_0x009b:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBundle(r12, r4);
        r3 = convertBundleToStringMap(r3);
        goto L_0x00e4;
    L_0x00a4:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createByteArray(r12, r4);
        goto L_0x00e4;
    L_0x00a9:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createString(r12, r4);
        goto L_0x00e4;
    L_0x00ae:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readBoolean(r12, r4);
        r3 = java.lang.Boolean.valueOf(r3);
        goto L_0x00e4;
    L_0x00b7:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBigDecimal(r12, r4);
        goto L_0x00e4;
    L_0x00bc:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readDouble(r12, r4);
        r3 = java.lang.Double.valueOf(r3);
        goto L_0x00e4;
    L_0x00c5:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readFloat(r12, r4);
        r3 = java.lang.Float.valueOf(r3);
        goto L_0x00e4;
    L_0x00ce:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readLong(r12, r4);
        r3 = java.lang.Long.valueOf(r3);
        goto L_0x00e4;
    L_0x00d7:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBigInteger(r12, r4);
        goto L_0x00e4;
    L_0x00dc:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readInt(r12, r4);
        r3 = java.lang.Integer.valueOf(r3);
    L_0x00e4:
        r3 = r9.getOriginalValue(r5, r3);
        r9.zzb(r10, r5, r3);
        goto L_0x0248;
    L_0x00ed:
        r3 = r5.isTypeOutArray();
        if (r3 == 0) goto L_0x016f;
    L_0x00f3:
        r3 = "[";
        r10.append(r3);
        r3 = r5.getTypeOut();
        switch(r3) {
            case 0: goto L_0x0164;
            case 1: goto L_0x015c;
            case 2: goto L_0x0154;
            case 3: goto L_0x014c;
            case 4: goto L_0x0144;
            case 5: goto L_0x013f;
            case 6: goto L_0x0137;
            case 7: goto L_0x012f;
            case 8: goto L_0x0127;
            case 9: goto L_0x0127;
            case 10: goto L_0x0127;
            case 11: goto L_0x0107;
            default: goto L_0x00ff;
        };
    L_0x00ff:
        r10 = new java.lang.IllegalStateException;
        r11 = "Unknown field type out.";
        r10.<init>(r11);
        throw r10;
    L_0x0107:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createParcelArray(r12, r4);
        r4 = r3.length;
        r6 = 0;
    L_0x010d:
        if (r6 >= r4) goto L_0x016b;
    L_0x010f:
        if (r6 <= 0) goto L_0x0116;
    L_0x0111:
        r7 = ",";
        r10.append(r7);
    L_0x0116:
        r7 = r3[r6];
        r7.setDataPosition(r2);
        r7 = r5.getConcreteTypeFieldMappingFromDictionary();
        r8 = r3[r6];
        r9.zza(r10, r7, r8);
        r6 = r6 + 1;
        goto L_0x010d;
    L_0x0127:
        r10 = new java.lang.UnsupportedOperationException;
        r11 = "List of type BASE64, BASE64_URL_SAFE, or STRING_MAP is not supported";
        r10.<init>(r11);
        throw r10;
    L_0x012f:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createStringArray(r12, r4);
        com.google.android.gms.common.util.ArrayUtils.writeStringArray(r10, r3);
        goto L_0x016b;
    L_0x0137:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBooleanArray(r12, r4);
        com.google.android.gms.common.util.ArrayUtils.writeArray(r10, r3);
        goto L_0x016b;
    L_0x013f:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBigDecimalArray(r12, r4);
        goto L_0x0160;
    L_0x0144:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createDoubleArray(r12, r4);
        com.google.android.gms.common.util.ArrayUtils.writeArray(r10, r3);
        goto L_0x016b;
    L_0x014c:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createFloatArray(r12, r4);
        com.google.android.gms.common.util.ArrayUtils.writeArray(r10, r3);
        goto L_0x016b;
    L_0x0154:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createLongArray(r12, r4);
        com.google.android.gms.common.util.ArrayUtils.writeArray(r10, r3);
        goto L_0x016b;
    L_0x015c:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBigIntegerArray(r12, r4);
    L_0x0160:
        com.google.android.gms.common.util.ArrayUtils.writeArray(r10, r3);
        goto L_0x016b;
    L_0x0164:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createIntArray(r12, r4);
        com.google.android.gms.common.util.ArrayUtils.writeArray(r10, r3);
    L_0x016b:
        r3 = "]";
        goto L_0x0210;
    L_0x016f:
        r3 = r5.getTypeOut();
        switch(r3) {
            case 0: goto L_0x0241;
            case 1: goto L_0x0239;
            case 2: goto L_0x0231;
            case 3: goto L_0x0229;
            case 4: goto L_0x0221;
            case 5: goto L_0x021c;
            case 6: goto L_0x0214;
            case 7: goto L_0x01fe;
            case 8: goto L_0x01f0;
            case 9: goto L_0x01e2;
            case 10: goto L_0x018e;
            case 11: goto L_0x017e;
            default: goto L_0x0176;
        };
    L_0x0176:
        r10 = new java.lang.IllegalStateException;
        r11 = "Unknown field type out";
        r10.<init>(r11);
        throw r10;
    L_0x017e:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createParcel(r12, r4);
        r3.setDataPosition(r2);
        r4 = r5.getConcreteTypeFieldMappingFromDictionary();
        r9.zza(r10, r4, r3);
        goto L_0x0248;
    L_0x018e:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBundle(r12, r4);
        r4 = r3.keySet();
        r4.size();
        r5 = "{";
        r10.append(r5);
        r4 = r4.iterator();
        r5 = 1;
    L_0x01a3:
        r6 = r4.hasNext();
        if (r6 == 0) goto L_0x01df;
    L_0x01a9:
        r6 = r4.next();
        r6 = (java.lang.String) r6;
        if (r5 != 0) goto L_0x01b6;
    L_0x01b1:
        r5 = ",";
        r10.append(r5);
    L_0x01b6:
        r5 = "\"";
        r10.append(r5);
        r10.append(r6);
        r5 = "\"";
        r10.append(r5);
        r5 = ":";
        r10.append(r5);
        r5 = "\"";
        r10.append(r5);
        r5 = r3.getString(r6);
        r5 = com.google.android.gms.common.util.JsonUtils.escapeString(r5);
        r10.append(r5);
        r5 = "\"";
        r10.append(r5);
        r5 = 0;
        goto L_0x01a3;
    L_0x01df:
        r3 = "}";
        goto L_0x0210;
    L_0x01e2:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createByteArray(r12, r4);
        r4 = "\"";
        r10.append(r4);
        r3 = com.google.android.gms.common.util.Base64Utils.encodeUrlSafe(r3);
        goto L_0x020b;
    L_0x01f0:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createByteArray(r12, r4);
        r4 = "\"";
        r10.append(r4);
        r3 = com.google.android.gms.common.util.Base64Utils.encode(r3);
        goto L_0x020b;
    L_0x01fe:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createString(r12, r4);
        r4 = "\"";
        r10.append(r4);
        r3 = com.google.android.gms.common.util.JsonUtils.escapeString(r3);
    L_0x020b:
        r10.append(r3);
        r3 = "\"";
    L_0x0210:
        r10.append(r3);
        goto L_0x0248;
    L_0x0214:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readBoolean(r12, r4);
        r10.append(r3);
        goto L_0x0248;
    L_0x021c:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBigDecimal(r12, r4);
        goto L_0x023d;
    L_0x0221:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readDouble(r12, r4);
        r10.append(r3);
        goto L_0x0248;
    L_0x0229:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readFloat(r12, r4);
        r10.append(r3);
        goto L_0x0248;
    L_0x0231:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readLong(r12, r4);
        r10.append(r3);
        goto L_0x0248;
    L_0x0239:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBigInteger(r12, r4);
    L_0x023d:
        r10.append(r3);
        goto L_0x0248;
    L_0x0241:
        r3 = com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readInt(r12, r4);
        r10.append(r3);
    L_0x0248:
        r3 = 1;
        goto L_0x0033;
    L_0x024b:
        r0 = r12.dataPosition();
        if (r0 == r11) goto L_0x026a;
    L_0x0251:
        r10 = new com.google.android.gms.common.internal.safeparcel.SafeParcelReader$ParseException;
        r0 = 37;
        r1 = new java.lang.StringBuilder;
        r1.<init>(r0);
        r0 = "Overread allowed size end=";
        r1.append(r0);
        r1.append(r11);
        r11 = r1.toString();
        r10.<init>(r11, r12);
        throw r10;
    L_0x026a:
        r11 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r10.append(r11);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.response.SafeParcelResponse.zza(java.lang.StringBuilder, java.util.Map, android.os.Parcel):void");
    }

    private final void zzb(FastJsonResponse.Field<?, ?> field) {
        if (!field.isValidSafeParcelableFieldId()) {
            throw new IllegalStateException("Field does not have a valid safe parcelable field id.");
        } else if (this.zzxq == null) {
            throw new IllegalStateException("Internal Parcel object is null.");
        } else {
            switch (this.zzxs) {
                case 0:
                    this.zzxt = SafeParcelWriter.beginObjectHeader(this.zzxq);
                    this.zzxs = 1;
                    return;
                case 1:
                    return;
                case 2:
                    throw new IllegalStateException("Attempted to parse JSON with a SafeParcelResponse object that is already filled with data.");
                default:
                    throw new IllegalStateException("Unknown parse state in SafeParcelResponse.");
            }
        }
    }

    private final void zzb(StringBuilder stringBuilder, FastJsonResponse.Field<?, ?> field, Object obj) {
        if (field.isTypeInArray()) {
            ArrayList arrayList = (ArrayList) obj;
            stringBuilder.append("[");
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                if (i != 0) {
                    stringBuilder.append(",");
                }
                zza(stringBuilder, field.getTypeIn(), arrayList.get(i));
            }
            stringBuilder.append("]");
            return;
        }
        zza(stringBuilder, field.getTypeIn(), obj);
    }

    public <T extends FastJsonResponse> void addConcreteTypeArrayInternal(FastJsonResponse.Field<?, ?> field, String str, ArrayList<T> arrayList) {
        zzb(field);
        List arrayList2 = new ArrayList();
        arrayList.size();
        ArrayList arrayList3 = arrayList;
        int size = arrayList3.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList3.get(i);
            i++;
            arrayList2.add(((SafeParcelResponse) ((FastJsonResponse) obj)).getParcel());
        }
        SafeParcelWriter.writeParcelList(this.zzxq, field.getSafeParcelableFieldId(), arrayList2, true);
    }

    public <T extends FastJsonResponse> void addConcreteTypeInternal(FastJsonResponse.Field<?, ?> field, String str, T t) {
        zzb(field);
        SafeParcelWriter.writeParcel(this.zzxq, field.getSafeParcelableFieldId(), ((SafeParcelResponse) t).getParcel(), true);
    }

    public Map<String, FastJsonResponse.Field<?, ?>> getFieldMappings() {
        return this.zzwn == null ? null : this.zzwn.getFieldMapping(this.mClassName);
    }

    public Parcel getParcel() {
        switch (this.zzxs) {
            case 0:
                this.zzxt = SafeParcelWriter.beginObjectHeader(this.zzxq);
                break;
            case 1:
                break;
            default:
                break;
        }
        SafeParcelWriter.finishObjectHeader(this.zzxq, this.zzxt);
        this.zzxs = 2;
        return this.zzxq;
    }

    public Object getValueObject(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    public int getVersionCode() {
        return this.zzal;
    }

    public <T extends SafeParcelable> T inflate(Creator<T> creator) {
        Parcel parcel = getParcel();
        parcel.setDataPosition(0);
        return (SafeParcelable) creator.createFromParcel(parcel);
    }

    public boolean isPrimitiveFieldSet(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    protected void setBigDecimalInternal(FastJsonResponse.Field<?, ?> field, String str, BigDecimal bigDecimal) {
        zzb(field);
        SafeParcelWriter.writeBigDecimal(this.zzxq, field.getSafeParcelableFieldId(), bigDecimal, true);
    }

    protected void setBigDecimalsInternal(FastJsonResponse.Field<?, ?> field, String str, ArrayList<BigDecimal> arrayList) {
        zzb(field);
        int size = arrayList.size();
        BigDecimal[] bigDecimalArr = new BigDecimal[size];
        for (int i = 0; i < size; i++) {
            bigDecimalArr[i] = (BigDecimal) arrayList.get(i);
        }
        SafeParcelWriter.writeBigDecimalArray(this.zzxq, field.getSafeParcelableFieldId(), bigDecimalArr, true);
    }

    protected void setBigIntegerInternal(FastJsonResponse.Field<?, ?> field, String str, BigInteger bigInteger) {
        zzb(field);
        SafeParcelWriter.writeBigInteger(this.zzxq, field.getSafeParcelableFieldId(), bigInteger, true);
    }

    protected void setBigIntegersInternal(FastJsonResponse.Field<?, ?> field, String str, ArrayList<BigInteger> arrayList) {
        zzb(field);
        int size = arrayList.size();
        BigInteger[] bigIntegerArr = new BigInteger[size];
        for (int i = 0; i < size; i++) {
            bigIntegerArr[i] = (BigInteger) arrayList.get(i);
        }
        SafeParcelWriter.writeBigIntegerArray(this.zzxq, field.getSafeParcelableFieldId(), bigIntegerArr, true);
    }

    protected void setBooleanInternal(FastJsonResponse.Field<?, ?> field, String str, boolean z) {
        zzb(field);
        SafeParcelWriter.writeBoolean(this.zzxq, field.getSafeParcelableFieldId(), z);
    }

    protected void setBooleansInternal(FastJsonResponse.Field<?, ?> field, String str, ArrayList<Boolean> arrayList) {
        zzb(field);
        int size = arrayList.size();
        boolean[] zArr = new boolean[size];
        for (int i = 0; i < size; i++) {
            zArr[i] = ((Boolean) arrayList.get(i)).booleanValue();
        }
        SafeParcelWriter.writeBooleanArray(this.zzxq, field.getSafeParcelableFieldId(), zArr, true);
    }

    protected void setDecodedBytesInternal(FastJsonResponse.Field<?, ?> field, String str, byte[] bArr) {
        zzb(field);
        SafeParcelWriter.writeByteArray(this.zzxq, field.getSafeParcelableFieldId(), bArr, true);
    }

    protected void setDoubleInternal(FastJsonResponse.Field<?, ?> field, String str, double d) {
        zzb(field);
        SafeParcelWriter.writeDouble(this.zzxq, field.getSafeParcelableFieldId(), d);
    }

    protected void setDoublesInternal(FastJsonResponse.Field<?, ?> field, String str, ArrayList<Double> arrayList) {
        zzb(field);
        int size = arrayList.size();
        double[] dArr = new double[size];
        for (int i = 0; i < size; i++) {
            dArr[i] = ((Double) arrayList.get(i)).doubleValue();
        }
        SafeParcelWriter.writeDoubleArray(this.zzxq, field.getSafeParcelableFieldId(), dArr, true);
    }

    protected void setFloatInternal(FastJsonResponse.Field<?, ?> field, String str, float f) {
        zzb(field);
        SafeParcelWriter.writeFloat(this.zzxq, field.getSafeParcelableFieldId(), f);
    }

    protected void setFloatsInternal(FastJsonResponse.Field<?, ?> field, String str, ArrayList<Float> arrayList) {
        zzb(field);
        int size = arrayList.size();
        float[] fArr = new float[size];
        for (int i = 0; i < size; i++) {
            fArr[i] = ((Float) arrayList.get(i)).floatValue();
        }
        SafeParcelWriter.writeFloatArray(this.zzxq, field.getSafeParcelableFieldId(), fArr, true);
    }

    protected void setIntegerInternal(FastJsonResponse.Field<?, ?> field, String str, int i) {
        zzb(field);
        SafeParcelWriter.writeInt(this.zzxq, field.getSafeParcelableFieldId(), i);
    }

    protected void setIntegersInternal(FastJsonResponse.Field<?, ?> field, String str, ArrayList<Integer> arrayList) {
        zzb(field);
        int size = arrayList.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = ((Integer) arrayList.get(i)).intValue();
        }
        SafeParcelWriter.writeIntArray(this.zzxq, field.getSafeParcelableFieldId(), iArr, true);
    }

    protected void setLongInternal(FastJsonResponse.Field<?, ?> field, String str, long j) {
        zzb(field);
        SafeParcelWriter.writeLong(this.zzxq, field.getSafeParcelableFieldId(), j);
    }

    protected void setLongsInternal(FastJsonResponse.Field<?, ?> field, String str, ArrayList<Long> arrayList) {
        zzb(field);
        int size = arrayList.size();
        long[] jArr = new long[size];
        for (int i = 0; i < size; i++) {
            jArr[i] = ((Long) arrayList.get(i)).longValue();
        }
        SafeParcelWriter.writeLongArray(this.zzxq, field.getSafeParcelableFieldId(), jArr, true);
    }

    protected void setStringInternal(FastJsonResponse.Field<?, ?> field, String str, String str2) {
        zzb(field);
        SafeParcelWriter.writeString(this.zzxq, field.getSafeParcelableFieldId(), str2, true);
    }

    protected void setStringMapInternal(FastJsonResponse.Field<?, ?> field, String str, Map<String, String> map) {
        zzb(field);
        Bundle bundle = new Bundle();
        for (String str2 : map.keySet()) {
            bundle.putString(str2, (String) map.get(str2));
        }
        SafeParcelWriter.writeBundle(this.zzxq, field.getSafeParcelableFieldId(), bundle, true);
    }

    protected void setStringsInternal(FastJsonResponse.Field<?, ?> field, String str, ArrayList<String> arrayList) {
        zzb(field);
        int size = arrayList.size();
        String[] strArr = new String[size];
        for (int i = 0; i < size; i++) {
            strArr[i] = (String) arrayList.get(i);
        }
        SafeParcelWriter.writeStringArray(this.zzxq, field.getSafeParcelableFieldId(), strArr, true);
    }

    public String toString() {
        Preconditions.checkNotNull(this.zzwn, "Cannot convert to JSON on client side.");
        Parcel parcel = getParcel();
        parcel.setDataPosition(0);
        StringBuilder stringBuilder = new StringBuilder(100);
        zza(stringBuilder, this.zzwn.getFieldMapping(this.mClassName), parcel);
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        Parcelable parcelable;
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, getVersionCode());
        SafeParcelWriter.writeParcel(parcel, 2, getParcel(), false);
        switch (this.zzxr) {
            case 0:
                parcelable = null;
                break;
            case 1:
            case 2:
                parcelable = this.zzwn;
                break;
            default:
                i = this.zzxr;
                StringBuilder stringBuilder = new StringBuilder(34);
                stringBuilder.append("Invalid creation type: ");
                stringBuilder.append(i);
                throw new IllegalStateException(stringBuilder.toString());
        }
        SafeParcelWriter.writeParcelable(parcel, 3, parcelable, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
