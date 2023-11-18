package retrofit2;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import okhttp3.Call$Factory;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;

final class ServiceMethod<R, T> {
    static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
    static final Pattern PARAM_NAME_REGEX = Pattern.compile(PARAM);
    static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{([a-zA-Z][a-zA-Z0-9_-]*)\\}");
    private final HttpUrl baseUrl;
    final CallAdapter<R, T> callAdapter;
    final Call$Factory callFactory;
    private final MediaType contentType;
    private final boolean hasBody;
    private final Headers headers;
    private final String httpMethod;
    private final boolean isFormEncoded;
    private final boolean isMultipart;
    private final ParameterHandler<?>[] parameterHandlers;
    private final String relativeUrl;
    private final Converter<ResponseBody, R> responseConverter;

    ServiceMethod(ServiceMethod$Builder<R, T> serviceMethod$Builder) {
        this.callFactory = serviceMethod$Builder.retrofit.callFactory();
        this.callAdapter = serviceMethod$Builder.callAdapter;
        this.baseUrl = serviceMethod$Builder.retrofit.baseUrl();
        this.responseConverter = serviceMethod$Builder.responseConverter;
        this.httpMethod = serviceMethod$Builder.httpMethod;
        this.relativeUrl = serviceMethod$Builder.relativeUrl;
        this.headers = serviceMethod$Builder.headers;
        this.contentType = serviceMethod$Builder.contentType;
        this.hasBody = serviceMethod$Builder.hasBody;
        this.isFormEncoded = serviceMethod$Builder.isFormEncoded;
        this.isMultipart = serviceMethod$Builder.isMultipart;
        this.parameterHandlers = serviceMethod$Builder.parameterHandlers;
    }

    Request toRequest(@Nullable Object... objArr) throws IOException {
        RequestBuilder requestBuilder = new RequestBuilder(this.httpMethod, this.baseUrl, this.relativeUrl, this.headers, this.contentType, this.hasBody, this.isFormEncoded, this.isMultipart);
        ParameterHandler[] parameterHandlerArr = this.parameterHandlers;
        int length = objArr != null ? objArr.length : 0;
        if (length != parameterHandlerArr.length) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Argument count (");
            stringBuilder.append(length);
            stringBuilder.append(") doesn't match expected count (");
            stringBuilder.append(parameterHandlerArr.length);
            stringBuilder.append(")");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        for (int i = 0; i < length; i++) {
            parameterHandlerArr[i].apply(requestBuilder, objArr[i]);
        }
        return requestBuilder.build();
    }

    R toResponse(ResponseBody responseBody) throws IOException {
        return this.responseConverter.convert(responseBody);
    }

    static Set<String> parsePathParameters(String str) {
        str = PARAM_URL_REGEX.matcher(str);
        Set<String> linkedHashSet = new LinkedHashSet();
        while (str.find()) {
            linkedHashSet.add(str.group(1));
        }
        return linkedHashSet;
    }

    static Class<?> boxIfPrimitive(Class<?> cls) {
        if (Boolean.TYPE == cls) {
            return Boolean.class;
        }
        if (Byte.TYPE == cls) {
            return Byte.class;
        }
        if (Character.TYPE == cls) {
            return Character.class;
        }
        if (Double.TYPE == cls) {
            return Double.class;
        }
        if (Float.TYPE == cls) {
            return Float.class;
        }
        if (Integer.TYPE == cls) {
            return Integer.class;
        }
        if (Long.TYPE == cls) {
            return Long.class;
        }
        return Short.TYPE == cls ? Short.class : cls;
    }
}
