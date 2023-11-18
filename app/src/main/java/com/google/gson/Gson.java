package com.google.gson;

import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.SqlDateTypeAdapter;
import com.google.gson.internal.bind.TimeTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

public final class Gson {
    static final boolean DEFAULT_COMPLEX_MAP_KEYS = false;
    static final boolean DEFAULT_ESCAPE_HTML = true;
    static final boolean DEFAULT_JSON_NON_EXECUTABLE = false;
    static final boolean DEFAULT_LENIENT = false;
    static final boolean DEFAULT_PRETTY_PRINT = false;
    static final boolean DEFAULT_SERIALIZE_NULLS = false;
    static final boolean DEFAULT_SPECIALIZE_FLOAT_VALUES = false;
    private static final String JSON_NON_EXECUTABLE_PREFIX = ")]}'\n";
    private static final TypeToken<?> NULL_KEY_SURROGATE = TypeToken.get(Object.class);
    final List<TypeAdapterFactory> builderFactories;
    final List<TypeAdapterFactory> builderHierarchyFactories;
    private final ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>> calls;
    final boolean complexMapKeySerialization;
    private final ConstructorConstructor constructorConstructor;
    final String datePattern;
    final int dateStyle;
    final Excluder excluder;
    final List<TypeAdapterFactory> factories;
    final FieldNamingStrategy fieldNamingStrategy;
    final boolean generateNonExecutableJson;
    final boolean htmlSafe;
    final Map<Type, InstanceCreator<?>> instanceCreators;
    private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
    final boolean lenient;
    final LongSerializationPolicy longSerializationPolicy;
    final boolean prettyPrinting;
    final boolean serializeNulls;
    final boolean serializeSpecialFloatingPointValues;
    final int timeStyle;
    private final Map<TypeToken<?>, TypeAdapter<?>> typeTokenCache;

    /* renamed from: com.google.gson.Gson$1 */
    class C12681 extends TypeAdapter<Number> {
        C12681() {
        }

        public Double read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() != JsonToken.NULL) {
                return Double.valueOf(jsonReader.nextDouble());
            }
            jsonReader.nextNull();
            return null;
        }

        public void write(JsonWriter jsonWriter, Number number) throws IOException {
            if (number == null) {
                jsonWriter.nullValue();
                return;
            }
            Gson.checkValidFloatingPoint(number.doubleValue());
            jsonWriter.value(number);
        }
    }

    /* renamed from: com.google.gson.Gson$2 */
    class C12692 extends TypeAdapter<Number> {
        C12692() {
        }

        public Float read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() != JsonToken.NULL) {
                return Float.valueOf((float) jsonReader.nextDouble());
            }
            jsonReader.nextNull();
            return null;
        }

        public void write(JsonWriter jsonWriter, Number number) throws IOException {
            if (number == null) {
                jsonWriter.nullValue();
                return;
            }
            Gson.checkValidFloatingPoint((double) number.floatValue());
            jsonWriter.value(number);
        }
    }

    /* renamed from: com.google.gson.Gson$3 */
    static class C12703 extends TypeAdapter<Number> {
        C12703() {
        }

        public Number read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() != JsonToken.NULL) {
                return Long.valueOf(jsonReader.nextLong());
            }
            jsonReader.nextNull();
            return null;
        }

        public void write(JsonWriter jsonWriter, Number number) throws IOException {
            if (number == null) {
                jsonWriter.nullValue();
            } else {
                jsonWriter.value(number.toString());
            }
        }
    }

    static class FutureTypeAdapter<T> extends TypeAdapter<T> {
        private TypeAdapter<T> delegate;

        FutureTypeAdapter() {
        }

        public void setDelegate(TypeAdapter<T> typeAdapter) {
            if (this.delegate != null) {
                throw new AssertionError();
            }
            this.delegate = typeAdapter;
        }

        public T read(JsonReader jsonReader) throws IOException {
            if (this.delegate != null) {
                return this.delegate.read(jsonReader);
            }
            throw new IllegalStateException();
        }

        public void write(JsonWriter jsonWriter, T t) throws IOException {
            if (this.delegate == null) {
                throw new IllegalStateException();
            }
            this.delegate.write(jsonWriter, t);
        }
    }

    public Gson() {
        this(Excluder.DEFAULT, FieldNamingPolicy.IDENTITY, Collections.emptyMap(), false, false, false, true, false, false, false, LongSerializationPolicy.DEFAULT, null, 2, 2, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    Gson(Excluder excluder, FieldNamingStrategy fieldNamingStrategy, Map<Type, InstanceCreator<?>> map, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, LongSerializationPolicy longSerializationPolicy, String str, int i, int i2, List<TypeAdapterFactory> list, List<TypeAdapterFactory> list2, List<TypeAdapterFactory> list3) {
        Excluder excluder2 = excluder;
        FieldNamingStrategy fieldNamingStrategy2 = fieldNamingStrategy;
        Map<Type, InstanceCreator<?>> map2 = map;
        boolean z8 = z2;
        boolean z9 = z7;
        this.calls = new ThreadLocal();
        this.typeTokenCache = new ConcurrentHashMap();
        this.excluder = excluder2;
        this.fieldNamingStrategy = fieldNamingStrategy2;
        this.instanceCreators = map2;
        this.constructorConstructor = new ConstructorConstructor(map2);
        this.serializeNulls = z;
        this.complexMapKeySerialization = z8;
        this.generateNonExecutableJson = z3;
        this.htmlSafe = z4;
        this.prettyPrinting = z5;
        this.lenient = z6;
        this.serializeSpecialFloatingPointValues = z9;
        this.longSerializationPolicy = longSerializationPolicy;
        this.datePattern = str;
        this.dateStyle = i;
        this.timeStyle = i2;
        this.builderFactories = list;
        this.builderHierarchyFactories = list2;
        List arrayList = new ArrayList();
        arrayList.add(TypeAdapters.JSON_ELEMENT_FACTORY);
        arrayList.add(ObjectTypeAdapter.FACTORY);
        arrayList.add(excluder2);
        arrayList.addAll(list3);
        arrayList.add(TypeAdapters.STRING_FACTORY);
        arrayList.add(TypeAdapters.INTEGER_FACTORY);
        arrayList.add(TypeAdapters.BOOLEAN_FACTORY);
        arrayList.add(TypeAdapters.BYTE_FACTORY);
        arrayList.add(TypeAdapters.SHORT_FACTORY);
        TypeAdapter longAdapter = longAdapter(longSerializationPolicy);
        arrayList.add(TypeAdapters.newFactory(Long.TYPE, Long.class, longAdapter));
        arrayList.add(TypeAdapters.newFactory(Double.TYPE, Double.class, doubleAdapter(z9)));
        arrayList.add(TypeAdapters.newFactory(Float.TYPE, Float.class, floatAdapter(z9)));
        arrayList.add(TypeAdapters.NUMBER_FACTORY);
        arrayList.add(TypeAdapters.ATOMIC_INTEGER_FACTORY);
        arrayList.add(TypeAdapters.ATOMIC_BOOLEAN_FACTORY);
        arrayList.add(TypeAdapters.newFactory(AtomicLong.class, atomicLongAdapter(longAdapter)));
        arrayList.add(TypeAdapters.newFactory(AtomicLongArray.class, atomicLongArrayAdapter(longAdapter)));
        arrayList.add(TypeAdapters.ATOMIC_INTEGER_ARRAY_FACTORY);
        arrayList.add(TypeAdapters.CHARACTER_FACTORY);
        arrayList.add(TypeAdapters.STRING_BUILDER_FACTORY);
        arrayList.add(TypeAdapters.STRING_BUFFER_FACTORY);
        arrayList.add(TypeAdapters.newFactory(BigDecimal.class, TypeAdapters.BIG_DECIMAL));
        arrayList.add(TypeAdapters.newFactory(BigInteger.class, TypeAdapters.BIG_INTEGER));
        arrayList.add(TypeAdapters.URL_FACTORY);
        arrayList.add(TypeAdapters.URI_FACTORY);
        arrayList.add(TypeAdapters.UUID_FACTORY);
        arrayList.add(TypeAdapters.CURRENCY_FACTORY);
        arrayList.add(TypeAdapters.LOCALE_FACTORY);
        arrayList.add(TypeAdapters.INET_ADDRESS_FACTORY);
        arrayList.add(TypeAdapters.BIT_SET_FACTORY);
        arrayList.add(DateTypeAdapter.FACTORY);
        arrayList.add(TypeAdapters.CALENDAR_FACTORY);
        arrayList.add(TimeTypeAdapter.FACTORY);
        arrayList.add(SqlDateTypeAdapter.FACTORY);
        arrayList.add(TypeAdapters.TIMESTAMP_FACTORY);
        arrayList.add(ArrayTypeAdapter.FACTORY);
        arrayList.add(TypeAdapters.CLASS_FACTORY);
        arrayList.add(new CollectionTypeAdapterFactory(this.constructorConstructor));
        arrayList.add(new MapTypeAdapterFactory(this.constructorConstructor, z8));
        this.jsonAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(this.constructorConstructor);
        arrayList.add(this.jsonAdapterFactory);
        arrayList.add(TypeAdapters.ENUM_FACTORY);
        arrayList.add(new ReflectiveTypeAdapterFactory(this.constructorConstructor, fieldNamingStrategy2, excluder2, this.jsonAdapterFactory));
        this.factories = Collections.unmodifiableList(arrayList);
    }

    public GsonBuilder newBuilder() {
        return new GsonBuilder(this);
    }

    public Excluder excluder() {
        return this.excluder;
    }

    public FieldNamingStrategy fieldNamingStrategy() {
        return this.fieldNamingStrategy;
    }

    public boolean serializeNulls() {
        return this.serializeNulls;
    }

    public boolean htmlSafe() {
        return this.htmlSafe;
    }

    private TypeAdapter<Number> doubleAdapter(boolean z) {
        if (z) {
            return TypeAdapters.DOUBLE;
        }
        return new C12681();
    }

    private TypeAdapter<Number> floatAdapter(boolean z) {
        if (z) {
            return TypeAdapters.FLOAT;
        }
        return new C12692();
    }

    static void checkValidFloatingPoint(double d) {
        if (!Double.isNaN(d)) {
            if (!Double.isInfinite(d)) {
                return;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(d);
        stringBuilder.append(" is not a valid double value as per JSON specification. To override this behavior, use GsonBuilder.serializeSpecialFloatingPointValues() method.");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private static TypeAdapter<Number> longAdapter(LongSerializationPolicy longSerializationPolicy) {
        if (longSerializationPolicy == LongSerializationPolicy.DEFAULT) {
            return TypeAdapters.LONG;
        }
        return new C12703();
    }

    private static TypeAdapter<AtomicLong> atomicLongAdapter(final TypeAdapter<Number> typeAdapter) {
        return new TypeAdapter<AtomicLong>() {
            public void write(JsonWriter jsonWriter, AtomicLong atomicLong) throws IOException {
                typeAdapter.write(jsonWriter, Long.valueOf(atomicLong.get()));
            }

            public AtomicLong read(JsonReader jsonReader) throws IOException {
                return new AtomicLong(((Number) typeAdapter.read(jsonReader)).longValue());
            }
        }.nullSafe();
    }

    private static TypeAdapter<AtomicLongArray> atomicLongArrayAdapter(final TypeAdapter<Number> typeAdapter) {
        return new TypeAdapter<AtomicLongArray>() {
            public void write(JsonWriter jsonWriter, AtomicLongArray atomicLongArray) throws IOException {
                jsonWriter.beginArray();
                int length = atomicLongArray.length();
                for (int i = 0; i < length; i++) {
                    typeAdapter.write(jsonWriter, Long.valueOf(atomicLongArray.get(i)));
                }
                jsonWriter.endArray();
            }

            public AtomicLongArray read(JsonReader jsonReader) throws IOException {
                List arrayList = new ArrayList();
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    arrayList.add(Long.valueOf(((Number) typeAdapter.read(jsonReader)).longValue()));
                }
                jsonReader.endArray();
                jsonReader = arrayList.size();
                AtomicLongArray atomicLongArray = new AtomicLongArray(jsonReader);
                for (int i = 0; i < jsonReader; i++) {
                    atomicLongArray.set(i, ((Long) arrayList.get(i)).longValue());
                }
                return atomicLongArray;
            }
        }.nullSafe();
    }

    public <T> TypeAdapter<T> getAdapter(TypeToken<T> typeToken) {
        TypeAdapter<T> typeAdapter = (TypeAdapter) this.typeTokenCache.get(typeToken == null ? NULL_KEY_SURROGATE : typeToken);
        if (typeAdapter != null) {
            return typeAdapter;
        }
        Map map = (Map) this.calls.get();
        Object obj = null;
        if (map == null) {
            map = new HashMap();
            this.calls.set(map);
            obj = 1;
        }
        FutureTypeAdapter futureTypeAdapter = (FutureTypeAdapter) map.get(typeToken);
        if (futureTypeAdapter != null) {
            return futureTypeAdapter;
        }
        try {
            TypeAdapter<T> hasNext;
            futureTypeAdapter = new FutureTypeAdapter();
            map.put(typeToken, futureTypeAdapter);
            Iterator it = this.factories.iterator();
            while (true) {
                hasNext = it.hasNext();
                if (hasNext != null) {
                    hasNext = ((TypeAdapterFactory) it.next()).create(this, typeToken);
                    if (hasNext != null) {
                        break;
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("GSON (2.8.5) cannot handle ");
                stringBuilder.append(typeToken);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            futureTypeAdapter.setDelegate(hasNext);
            this.typeTokenCache.put(typeToken, hasNext);
            return hasNext;
        } finally {
            map.remove(typeToken);
            if (obj != null) {
                this.calls.remove();
            }
        }
    }

    public <T> TypeAdapter<T> getDelegateAdapter(TypeAdapterFactory typeAdapterFactory, TypeToken<T> typeToken) {
        if (!this.factories.contains(typeAdapterFactory)) {
            typeAdapterFactory = this.jsonAdapterFactory;
        }
        Object obj = null;
        for (TypeAdapterFactory typeAdapterFactory2 : this.factories) {
            if (obj != null) {
                TypeAdapter<T> create = typeAdapterFactory2.create(this, typeToken);
                if (create != null) {
                    return create;
                }
            } else if (typeAdapterFactory2 == typeAdapterFactory) {
                obj = 1;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("GSON cannot serialize ");
        stringBuilder.append(typeToken);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public <T> TypeAdapter<T> getAdapter(Class<T> cls) {
        return getAdapter(TypeToken.get((Class) cls));
    }

    public JsonElement toJsonTree(Object obj) {
        if (obj == null) {
            return JsonNull.INSTANCE;
        }
        return toJsonTree(obj, obj.getClass());
    }

    public JsonElement toJsonTree(Object obj, Type type) {
        JsonWriter jsonTreeWriter = new JsonTreeWriter();
        toJson(obj, type, jsonTreeWriter);
        return jsonTreeWriter.get();
    }

    public String toJson(Object obj) {
        if (obj == null) {
            return toJson(JsonNull.INSTANCE);
        }
        return toJson(obj, obj.getClass());
    }

    public String toJson(Object obj, Type type) {
        Appendable stringWriter = new StringWriter();
        toJson(obj, type, stringWriter);
        return stringWriter.toString();
    }

    public void toJson(Object obj, Appendable appendable) throws JsonIOException {
        if (obj != null) {
            toJson(obj, obj.getClass(), appendable);
        } else {
            toJson(JsonNull.INSTANCE, appendable);
        }
    }

    public void toJson(Object obj, Type type, Appendable appendable) throws JsonIOException {
        try {
            toJson(obj, type, newJsonWriter(Streams.writerForAppendable(appendable)));
        } catch (Throwable e) {
            throw new JsonIOException(e);
        }
    }

    public void toJson(Object obj, Type type, JsonWriter jsonWriter) throws JsonIOException {
        type = getAdapter(TypeToken.get(type));
        boolean isLenient = jsonWriter.isLenient();
        jsonWriter.setLenient(true);
        boolean isHtmlSafe = jsonWriter.isHtmlSafe();
        jsonWriter.setHtmlSafe(this.htmlSafe);
        boolean serializeNulls = jsonWriter.getSerializeNulls();
        jsonWriter.setSerializeNulls(this.serializeNulls);
        try {
            type.write(jsonWriter, obj);
            jsonWriter.setLenient(isLenient);
            jsonWriter.setHtmlSafe(isHtmlSafe);
            jsonWriter.setSerializeNulls(serializeNulls);
        } catch (Throwable e) {
            throw new JsonIOException(e);
        } catch (Object obj2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("AssertionError (GSON 2.8.5): ");
            stringBuilder.append(obj2.getMessage());
            throw new AssertionError(stringBuilder.toString(), obj2);
        } catch (Throwable th) {
            jsonWriter.setLenient(isLenient);
            jsonWriter.setHtmlSafe(isHtmlSafe);
            jsonWriter.setSerializeNulls(serializeNulls);
        }
    }

    public String toJson(JsonElement jsonElement) {
        Appendable stringWriter = new StringWriter();
        toJson(jsonElement, stringWriter);
        return stringWriter.toString();
    }

    public void toJson(JsonElement jsonElement, Appendable appendable) throws JsonIOException {
        try {
            toJson(jsonElement, newJsonWriter(Streams.writerForAppendable(appendable)));
        } catch (Throwable e) {
            throw new JsonIOException(e);
        }
    }

    public JsonWriter newJsonWriter(Writer writer) throws IOException {
        if (this.generateNonExecutableJson) {
            writer.write(JSON_NON_EXECUTABLE_PREFIX);
        }
        JsonWriter jsonWriter = new JsonWriter(writer);
        if (this.prettyPrinting != null) {
            jsonWriter.setIndent("  ");
        }
        jsonWriter.setSerializeNulls(this.serializeNulls);
        return jsonWriter;
    }

    public JsonReader newJsonReader(Reader reader) {
        JsonReader jsonReader = new JsonReader(reader);
        jsonReader.setLenient(this.lenient);
        return jsonReader;
    }

    public void toJson(JsonElement jsonElement, JsonWriter jsonWriter) throws JsonIOException {
        boolean isLenient = jsonWriter.isLenient();
        jsonWriter.setLenient(true);
        boolean isHtmlSafe = jsonWriter.isHtmlSafe();
        jsonWriter.setHtmlSafe(this.htmlSafe);
        boolean serializeNulls = jsonWriter.getSerializeNulls();
        jsonWriter.setSerializeNulls(this.serializeNulls);
        try {
            Streams.write(jsonElement, jsonWriter);
            jsonWriter.setLenient(isLenient);
            jsonWriter.setHtmlSafe(isHtmlSafe);
            jsonWriter.setSerializeNulls(serializeNulls);
        } catch (Throwable e) {
            throw new JsonIOException(e);
        } catch (JsonElement jsonElement2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("AssertionError (GSON 2.8.5): ");
            stringBuilder.append(jsonElement2.getMessage());
            throw new AssertionError(stringBuilder.toString(), jsonElement2);
        } catch (Throwable th) {
            jsonWriter.setLenient(isLenient);
            jsonWriter.setHtmlSafe(isHtmlSafe);
            jsonWriter.setSerializeNulls(serializeNulls);
        }
    }

    public <T> T fromJson(String str, Class<T> cls) throws JsonSyntaxException {
        return Primitives.wrap(cls).cast(fromJson(str, (Type) cls));
    }

    public <T> T fromJson(String str, Type type) throws JsonSyntaxException {
        if (str == null) {
            return null;
        }
        return fromJson(new StringReader(str), type);
    }

    public <T> T fromJson(Reader reader, Class<T> cls) throws JsonSyntaxException, JsonIOException {
        JsonReader newJsonReader = newJsonReader(reader);
        Object fromJson = fromJson(newJsonReader, (Type) cls);
        assertFullConsumption(fromJson, newJsonReader);
        return Primitives.wrap(cls).cast(fromJson);
    }

    public <T> T fromJson(Reader reader, Type type) throws JsonIOException, JsonSyntaxException {
        JsonReader newJsonReader = newJsonReader(reader);
        type = fromJson(newJsonReader, type);
        assertFullConsumption(type, newJsonReader);
        return type;
    }

    private static void assertFullConsumption(Object obj, JsonReader jsonReader) {
        if (obj != null) {
            try {
                if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                    throw new JsonIOException("JSON document was not fully consumed.");
                }
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            } catch (Throwable e2) {
                throw new JsonIOException(e2);
            }
        }
    }

    public <T> T fromJson(JsonReader jsonReader, Type type) throws JsonIOException, JsonSyntaxException {
        boolean isLenient = jsonReader.isLenient();
        boolean z = true;
        jsonReader.setLenient(true);
        try {
            jsonReader.peek();
            z = false;
            type = getAdapter(TypeToken.get(type)).read(jsonReader);
            jsonReader.setLenient(isLenient);
            return type;
        } catch (Throwable e) {
            if (z) {
                jsonReader.setLenient(isLenient);
                return null;
            }
            throw new JsonSyntaxException(e);
        } catch (Throwable e2) {
            throw new JsonSyntaxException(e2);
        } catch (Throwable e22) {
            throw new JsonSyntaxException(e22);
        } catch (Type type2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("AssertionError (GSON 2.8.5): ");
            stringBuilder.append(type2.getMessage());
            throw new AssertionError(stringBuilder.toString(), type2);
        } catch (Throwable th) {
            jsonReader.setLenient(isLenient);
        }
    }

    public <T> T fromJson(JsonElement jsonElement, Class<T> cls) throws JsonSyntaxException {
        return Primitives.wrap(cls).cast(fromJson(jsonElement, (Type) cls));
    }

    public <T> T fromJson(JsonElement jsonElement, Type type) throws JsonSyntaxException {
        return jsonElement == null ? null : fromJson(new JsonTreeReader(jsonElement), type);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{serializeNulls:");
        stringBuilder.append(this.serializeNulls);
        stringBuilder.append(",factories:");
        stringBuilder.append(this.factories);
        stringBuilder.append(",instanceCreators:");
        stringBuilder.append(this.constructorConstructor);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
