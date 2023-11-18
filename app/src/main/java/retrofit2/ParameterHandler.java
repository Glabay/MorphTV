package retrofit2;

import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import okhttp3.Headers;
import okhttp3.RequestBody;

abstract class ParameterHandler<T> {

    /* renamed from: retrofit2.ParameterHandler$1 */
    class C11711 extends ParameterHandler<Iterable<T>> {
        C11711() {
        }

        void apply(RequestBuilder requestBuilder, @Nullable Iterable<T> iterable) throws IOException {
            if (iterable != null) {
                for (T apply : iterable) {
                    ParameterHandler.this.apply(requestBuilder, apply);
                }
            }
        }
    }

    /* renamed from: retrofit2.ParameterHandler$2 */
    class C11722 extends ParameterHandler<Object> {
        C11722() {
        }

        void apply(RequestBuilder requestBuilder, @Nullable Object obj) throws IOException {
            if (obj != null) {
                int length = Array.getLength(obj);
                for (int i = 0; i < length; i++) {
                    ParameterHandler.this.apply(requestBuilder, Array.get(obj, i));
                }
            }
        }
    }

    static final class Body<T> extends ParameterHandler<T> {
        private final Converter<T, RequestBody> converter;

        Body(Converter<T, RequestBody> converter) {
            this.converter = converter;
        }

        void apply(RequestBuilder requestBuilder, @Nullable T t) {
            if (t == null) {
                throw new IllegalArgumentException("Body parameter value must not be null.");
            }
            try {
                requestBuilder.setBody((RequestBody) this.converter.convert(t));
            } catch (RequestBuilder requestBuilder2) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to convert ");
                stringBuilder.append(t);
                stringBuilder.append(" to RequestBody");
                throw new RuntimeException(stringBuilder.toString(), requestBuilder2);
            }
        }
    }

    static final class Field<T> extends ParameterHandler<T> {
        private final boolean encoded;
        private final String name;
        private final Converter<T, String> valueConverter;

        Field(String str, Converter<T, String> converter, boolean z) {
            this.name = (String) Utils.checkNotNull(str, "name == null");
            this.valueConverter = converter;
            this.encoded = z;
        }

        void apply(RequestBuilder requestBuilder, @Nullable T t) throws IOException {
            if (t != null) {
                String str = (String) this.valueConverter.convert(t);
                if (str != null) {
                    requestBuilder.addFormField(this.name, str, this.encoded);
                }
            }
        }
    }

    static final class FieldMap<T> extends ParameterHandler<Map<String, T>> {
        private final boolean encoded;
        private final Converter<T, String> valueConverter;

        FieldMap(Converter<T, String> converter, boolean z) {
            this.valueConverter = converter;
            this.encoded = z;
        }

        void apply(RequestBuilder requestBuilder, @Nullable Map<String, T> map) throws IOException {
            if (map == null) {
                throw new IllegalArgumentException("Field map was null.");
            }
            map = map.entrySet().iterator();
            while (map.hasNext()) {
                Entry entry = (Entry) map.next();
                String str = (String) entry.getKey();
                if (str == null) {
                    throw new IllegalArgumentException("Field map contained null key.");
                }
                Object value = entry.getValue();
                if (value == null) {
                    map = new StringBuilder();
                    map.append("Field map contained null value for key '");
                    map.append(str);
                    map.append("'.");
                    throw new IllegalArgumentException(map.toString());
                }
                String str2 = (String) this.valueConverter.convert(value);
                if (str2 == null) {
                    map = new StringBuilder();
                    map.append("Field map value '");
                    map.append(value);
                    map.append("' converted to null by ");
                    map.append(this.valueConverter.getClass().getName());
                    map.append(" for key '");
                    map.append(str);
                    map.append("'.");
                    throw new IllegalArgumentException(map.toString());
                }
                requestBuilder.addFormField(str, str2, this.encoded);
            }
        }
    }

    static final class Header<T> extends ParameterHandler<T> {
        private final String name;
        private final Converter<T, String> valueConverter;

        Header(String str, Converter<T, String> converter) {
            this.name = (String) Utils.checkNotNull(str, "name == null");
            this.valueConverter = converter;
        }

        void apply(RequestBuilder requestBuilder, @Nullable T t) throws IOException {
            if (t != null) {
                String str = (String) this.valueConverter.convert(t);
                if (str != null) {
                    requestBuilder.addHeader(this.name, str);
                }
            }
        }
    }

    static final class HeaderMap<T> extends ParameterHandler<Map<String, T>> {
        private final Converter<T, String> valueConverter;

        HeaderMap(Converter<T, String> converter) {
            this.valueConverter = converter;
        }

        void apply(RequestBuilder requestBuilder, @Nullable Map<String, T> map) throws IOException {
            if (map == null) {
                throw new IllegalArgumentException("Header map was null.");
            }
            map = map.entrySet().iterator();
            while (map.hasNext()) {
                Entry entry = (Entry) map.next();
                String str = (String) entry.getKey();
                if (str == null) {
                    throw new IllegalArgumentException("Header map contained null key.");
                }
                Object value = entry.getValue();
                if (value == null) {
                    map = new StringBuilder();
                    map.append("Header map contained null value for key '");
                    map.append(str);
                    map.append("'.");
                    throw new IllegalArgumentException(map.toString());
                }
                requestBuilder.addHeader(str, (String) this.valueConverter.convert(value));
            }
        }
    }

    static final class Part<T> extends ParameterHandler<T> {
        private final Converter<T, RequestBody> converter;
        private final Headers headers;

        Part(Headers headers, Converter<T, RequestBody> converter) {
            this.headers = headers;
            this.converter = converter;
        }

        void apply(RequestBuilder requestBuilder, @Nullable T t) {
            if (t != null) {
                try {
                    requestBuilder.addPart(this.headers, (RequestBody) this.converter.convert(t));
                } catch (RequestBuilder requestBuilder2) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to convert ");
                    stringBuilder.append(t);
                    stringBuilder.append(" to RequestBody");
                    throw new RuntimeException(stringBuilder.toString(), requestBuilder2);
                }
            }
        }
    }

    static final class PartMap<T> extends ParameterHandler<Map<String, T>> {
        private final String transferEncoding;
        private final Converter<T, RequestBody> valueConverter;

        PartMap(Converter<T, RequestBody> converter, String str) {
            this.valueConverter = converter;
            this.transferEncoding = str;
        }

        void apply(RequestBuilder requestBuilder, @Nullable Map<String, T> map) throws IOException {
            if (map == null) {
                throw new IllegalArgumentException("Part map was null.");
            }
            map = map.entrySet().iterator();
            while (map.hasNext()) {
                Entry entry = (Entry) map.next();
                String str = (String) entry.getKey();
                if (str == null) {
                    throw new IllegalArgumentException("Part map contained null key.");
                }
                Object value = entry.getValue();
                if (value == null) {
                    map = new StringBuilder();
                    map.append("Part map contained null value for key '");
                    map.append(str);
                    map.append("'.");
                    throw new IllegalArgumentException(map.toString());
                }
                String[] strArr = new String[4];
                strArr[0] = HttpHeaders.CONTENT_DISPOSITION;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("form-data; name=\"");
                stringBuilder.append(str);
                stringBuilder.append("\"");
                strArr[1] = stringBuilder.toString();
                strArr[2] = "Content-Transfer-Encoding";
                strArr[3] = this.transferEncoding;
                requestBuilder.addPart(Headers.of(strArr), (RequestBody) this.valueConverter.convert(value));
            }
        }
    }

    static final class Path<T> extends ParameterHandler<T> {
        private final boolean encoded;
        private final String name;
        private final Converter<T, String> valueConverter;

        Path(String str, Converter<T, String> converter, boolean z) {
            this.name = (String) Utils.checkNotNull(str, "name == null");
            this.valueConverter = converter;
            this.encoded = z;
        }

        void apply(RequestBuilder requestBuilder, @Nullable T t) throws IOException {
            if (t == null) {
                t = new StringBuilder();
                t.append("Path parameter \"");
                t.append(this.name);
                t.append("\" value must not be null.");
                throw new IllegalArgumentException(t.toString());
            }
            requestBuilder.addPathParam(this.name, (String) this.valueConverter.convert(t), this.encoded);
        }
    }

    static final class Query<T> extends ParameterHandler<T> {
        private final boolean encoded;
        private final String name;
        private final Converter<T, String> valueConverter;

        Query(String str, Converter<T, String> converter, boolean z) {
            this.name = (String) Utils.checkNotNull(str, "name == null");
            this.valueConverter = converter;
            this.encoded = z;
        }

        void apply(RequestBuilder requestBuilder, @Nullable T t) throws IOException {
            if (t != null) {
                String str = (String) this.valueConverter.convert(t);
                if (str != null) {
                    requestBuilder.addQueryParam(this.name, str, this.encoded);
                }
            }
        }
    }

    static final class QueryMap<T> extends ParameterHandler<Map<String, T>> {
        private final boolean encoded;
        private final Converter<T, String> valueConverter;

        QueryMap(Converter<T, String> converter, boolean z) {
            this.valueConverter = converter;
            this.encoded = z;
        }

        void apply(RequestBuilder requestBuilder, @Nullable Map<String, T> map) throws IOException {
            if (map == null) {
                throw new IllegalArgumentException("Query map was null.");
            }
            map = map.entrySet().iterator();
            while (map.hasNext()) {
                Entry entry = (Entry) map.next();
                String str = (String) entry.getKey();
                if (str == null) {
                    throw new IllegalArgumentException("Query map contained null key.");
                }
                Object value = entry.getValue();
                if (value == null) {
                    map = new StringBuilder();
                    map.append("Query map contained null value for key '");
                    map.append(str);
                    map.append("'.");
                    throw new IllegalArgumentException(map.toString());
                }
                String str2 = (String) this.valueConverter.convert(value);
                if (str2 == null) {
                    map = new StringBuilder();
                    map.append("Query map value '");
                    map.append(value);
                    map.append("' converted to null by ");
                    map.append(this.valueConverter.getClass().getName());
                    map.append(" for key '");
                    map.append(str);
                    map.append("'.");
                    throw new IllegalArgumentException(map.toString());
                }
                requestBuilder.addQueryParam(str, str2, this.encoded);
            }
        }
    }

    static final class QueryName<T> extends ParameterHandler<T> {
        private final boolean encoded;
        private final Converter<T, String> nameConverter;

        QueryName(Converter<T, String> converter, boolean z) {
            this.nameConverter = converter;
            this.encoded = z;
        }

        void apply(RequestBuilder requestBuilder, @Nullable T t) throws IOException {
            if (t != null) {
                requestBuilder.addQueryParam((String) this.nameConverter.convert(t), null, this.encoded);
            }
        }
    }

    static final class RawPart extends ParameterHandler<okhttp3.MultipartBody.Part> {
        static final RawPart INSTANCE = new RawPart();

        private RawPart() {
        }

        void apply(RequestBuilder requestBuilder, @Nullable okhttp3.MultipartBody.Part part) throws IOException {
            if (part != null) {
                requestBuilder.addPart(part);
            }
        }
    }

    static final class RelativeUrl extends ParameterHandler<Object> {
        RelativeUrl() {
        }

        void apply(RequestBuilder requestBuilder, @Nullable Object obj) {
            Utils.checkNotNull(obj, "@Url parameter is null.");
            requestBuilder.setRelativeUrl(obj);
        }
    }

    abstract void apply(RequestBuilder requestBuilder, @Nullable T t) throws IOException;

    ParameterHandler() {
    }

    final ParameterHandler<Iterable<T>> iterable() {
        return new C11711();
    }

    final ParameterHandler<Object> array() {
        return new C11722();
    }
}
