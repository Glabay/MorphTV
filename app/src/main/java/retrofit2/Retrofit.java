package retrofit2;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import okhttp3.Call$Factory;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.BuiltInConverters.ToStringConverter;

public final class Retrofit {
    final List<CallAdapter$Factory> adapterFactories;
    final HttpUrl baseUrl;
    final Call$Factory callFactory;
    @Nullable
    final Executor callbackExecutor;
    final List<Converter$Factory> converterFactories;
    private final Map<Method, ServiceMethod<?, ?>> serviceMethodCache = new ConcurrentHashMap();
    final boolean validateEagerly;

    public static final class Builder {
        private final List<CallAdapter$Factory> adapterFactories;
        private HttpUrl baseUrl;
        @Nullable
        private Call$Factory callFactory;
        @Nullable
        private Executor callbackExecutor;
        private final List<Converter$Factory> converterFactories;
        private final Platform platform;
        private boolean validateEagerly;

        Builder(Platform platform) {
            this.converterFactories = new ArrayList();
            this.adapterFactories = new ArrayList();
            this.platform = platform;
            this.converterFactories.add(new BuiltInConverters());
        }

        public Builder() {
            this(Platform.get());
        }

        Builder(Retrofit retrofit) {
            this.converterFactories = new ArrayList();
            this.adapterFactories = new ArrayList();
            this.platform = Platform.get();
            this.callFactory = retrofit.callFactory;
            this.baseUrl = retrofit.baseUrl;
            this.converterFactories.addAll(retrofit.converterFactories);
            this.adapterFactories.addAll(retrofit.adapterFactories);
            this.adapterFactories.remove(this.adapterFactories.size() - 1);
            this.callbackExecutor = retrofit.callbackExecutor;
            this.validateEagerly = retrofit.validateEagerly;
        }

        public Builder client(OkHttpClient okHttpClient) {
            return callFactory((Call$Factory) Utils.checkNotNull(okHttpClient, "client == null"));
        }

        public Builder callFactory(Call$Factory call$Factory) {
            this.callFactory = (Call$Factory) Utils.checkNotNull(call$Factory, "factory == null");
            return this;
        }

        public Builder baseUrl(String str) {
            Utils.checkNotNull(str, "baseUrl == null");
            HttpUrl parse = HttpUrl.parse(str);
            if (parse != null) {
                return baseUrl(parse);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Illegal URL: ");
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder baseUrl(HttpUrl httpUrl) {
            Utils.checkNotNull(httpUrl, "baseUrl == null");
            List pathSegments = httpUrl.pathSegments();
            if ("".equals(pathSegments.get(pathSegments.size() - 1))) {
                this.baseUrl = httpUrl;
                return this;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("baseUrl must end in /: ");
            stringBuilder.append(httpUrl);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder addConverterFactory(Converter$Factory converter$Factory) {
            this.converterFactories.add(Utils.checkNotNull(converter$Factory, "factory == null"));
            return this;
        }

        public Builder addCallAdapterFactory(CallAdapter$Factory callAdapter$Factory) {
            this.adapterFactories.add(Utils.checkNotNull(callAdapter$Factory, "factory == null"));
            return this;
        }

        public Builder callbackExecutor(Executor executor) {
            this.callbackExecutor = (Executor) Utils.checkNotNull(executor, "executor == null");
            return this;
        }

        public Builder validateEagerly(boolean z) {
            this.validateEagerly = z;
            return this;
        }

        public Retrofit build() {
            if (this.baseUrl == null) {
                throw new IllegalStateException("Base URL required.");
            }
            Call$Factory call$Factory = this.callFactory;
            if (call$Factory == null) {
                call$Factory = new OkHttpClient();
            }
            Call$Factory call$Factory2 = call$Factory;
            Executor executor = this.callbackExecutor;
            if (executor == null) {
                executor = this.platform.defaultCallbackExecutor();
            }
            Executor executor2 = executor;
            List arrayList = new ArrayList(this.adapterFactories);
            arrayList.add(this.platform.defaultCallAdapterFactory(executor2));
            return new Retrofit(call$Factory2, this.baseUrl, new ArrayList(this.converterFactories), arrayList, executor2, this.validateEagerly);
        }
    }

    Retrofit(Call$Factory call$Factory, HttpUrl httpUrl, List<Converter$Factory> list, List<CallAdapter$Factory> list2, @Nullable Executor executor, boolean z) {
        this.callFactory = call$Factory;
        this.baseUrl = httpUrl;
        this.converterFactories = Collections.unmodifiableList(list);
        this.adapterFactories = Collections.unmodifiableList(list2);
        this.callbackExecutor = executor;
        this.validateEagerly = z;
    }

    public <T> T create(final Class<T> cls) {
        Utils.validateServiceInterface(cls);
        if (this.validateEagerly) {
            eagerlyValidateMethods(cls);
        }
        return Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new InvocationHandler() {
            private final Platform platform = Platform.get();

            public Object invoke(Object obj, Method method, @Nullable Object[] objArr) throws Throwable {
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(this, objArr);
                }
                if (this.platform.isDefaultMethod(method)) {
                    return this.platform.invokeDefaultMethod(method, cls, obj, objArr);
                }
                obj = Retrofit.this.loadServiceMethod(method);
                return obj.callAdapter.adapt(new OkHttpCall(obj, objArr));
            }
        });
    }

    private void eagerlyValidateMethods(Class<?> cls) {
        Platform platform = Platform.get();
        for (Method method : cls.getDeclaredMethods()) {
            if (!platform.isDefaultMethod(method)) {
                loadServiceMethod(method);
            }
        }
    }

    ServiceMethod<?, ?> loadServiceMethod(Method method) {
        ServiceMethod<?, ?> serviceMethod = (ServiceMethod) this.serviceMethodCache.get(method);
        if (serviceMethod != null) {
            return serviceMethod;
        }
        ServiceMethod<?, ?> serviceMethod2;
        synchronized (this.serviceMethodCache) {
            serviceMethod2 = (ServiceMethod) this.serviceMethodCache.get(method);
            if (serviceMethod2 == null) {
                serviceMethod2 = new ServiceMethod$Builder(this, method).build();
                this.serviceMethodCache.put(method, serviceMethod2);
            }
        }
        return serviceMethod2;
    }

    public Call$Factory callFactory() {
        return this.callFactory;
    }

    public HttpUrl baseUrl() {
        return this.baseUrl;
    }

    public List<CallAdapter$Factory> callAdapterFactories() {
        return this.adapterFactories;
    }

    public CallAdapter<?, ?> callAdapter(Type type, Annotation[] annotationArr) {
        return nextCallAdapter(null, type, annotationArr);
    }

    public CallAdapter<?, ?> nextCallAdapter(@Nullable CallAdapter$Factory callAdapter$Factory, Type type, Annotation[] annotationArr) {
        Utils.checkNotNull(type, "returnType == null");
        Utils.checkNotNull(annotationArr, "annotations == null");
        int indexOf = this.adapterFactories.indexOf(callAdapter$Factory) + 1;
        int size = this.adapterFactories.size();
        for (int i = indexOf; i < size; i++) {
            CallAdapter<?, ?> callAdapter = ((CallAdapter$Factory) this.adapterFactories.get(i)).get(type, annotationArr, this);
            if (callAdapter != null) {
                return callAdapter;
            }
        }
        annotationArr = new StringBuilder("Could not locate call adapter for ");
        annotationArr.append(type);
        annotationArr.append(".\n");
        if (callAdapter$Factory != null) {
            annotationArr.append("  Skipped:");
            for (callAdapter$Factory = null; callAdapter$Factory < indexOf; callAdapter$Factory++) {
                annotationArr.append("\n   * ");
                annotationArr.append(((CallAdapter$Factory) this.adapterFactories.get(callAdapter$Factory)).getClass().getName());
            }
            annotationArr.append('\n');
        }
        annotationArr.append("  Tried:");
        callAdapter$Factory = this.adapterFactories.size();
        while (indexOf < callAdapter$Factory) {
            annotationArr.append("\n   * ");
            annotationArr.append(((CallAdapter$Factory) this.adapterFactories.get(indexOf)).getClass().getName());
            indexOf++;
        }
        throw new IllegalArgumentException(annotationArr.toString());
    }

    public List<Converter$Factory> converterFactories() {
        return this.converterFactories;
    }

    public <T> Converter<T, RequestBody> requestBodyConverter(Type type, Annotation[] annotationArr, Annotation[] annotationArr2) {
        return nextRequestBodyConverter(null, type, annotationArr, annotationArr2);
    }

    public <T> Converter<T, RequestBody> nextRequestBodyConverter(@Nullable Converter$Factory converter$Factory, Type type, Annotation[] annotationArr, Annotation[] annotationArr2) {
        Utils.checkNotNull(type, "type == null");
        Utils.checkNotNull(annotationArr, "parameterAnnotations == null");
        Utils.checkNotNull(annotationArr2, "methodAnnotations == null");
        int indexOf = this.converterFactories.indexOf(converter$Factory) + 1;
        int size = this.converterFactories.size();
        for (int i = indexOf; i < size; i++) {
            Converter<T, RequestBody> requestBodyConverter = ((Converter$Factory) this.converterFactories.get(i)).requestBodyConverter(type, annotationArr, annotationArr2, this);
            if (requestBodyConverter != null) {
                return requestBodyConverter;
            }
        }
        annotationArr = new StringBuilder("Could not locate RequestBody converter for ");
        annotationArr.append(type);
        annotationArr.append(".\n");
        if (converter$Factory != null) {
            annotationArr.append("  Skipped:");
            for (converter$Factory = null; converter$Factory < indexOf; converter$Factory++) {
                annotationArr.append("\n   * ");
                annotationArr.append(((Converter$Factory) this.converterFactories.get(converter$Factory)).getClass().getName());
            }
            annotationArr.append('\n');
        }
        annotationArr.append("  Tried:");
        converter$Factory = this.converterFactories.size();
        while (indexOf < converter$Factory) {
            annotationArr.append("\n   * ");
            annotationArr.append(((Converter$Factory) this.converterFactories.get(indexOf)).getClass().getName());
            indexOf++;
        }
        throw new IllegalArgumentException(annotationArr.toString());
    }

    public <T> Converter<ResponseBody, T> responseBodyConverter(Type type, Annotation[] annotationArr) {
        return nextResponseBodyConverter(null, type, annotationArr);
    }

    public <T> Converter<ResponseBody, T> nextResponseBodyConverter(@Nullable Converter$Factory converter$Factory, Type type, Annotation[] annotationArr) {
        Utils.checkNotNull(type, "type == null");
        Utils.checkNotNull(annotationArr, "annotations == null");
        int indexOf = this.converterFactories.indexOf(converter$Factory) + 1;
        int size = this.converterFactories.size();
        for (int i = indexOf; i < size; i++) {
            Converter<ResponseBody, T> responseBodyConverter = ((Converter$Factory) this.converterFactories.get(i)).responseBodyConverter(type, annotationArr, this);
            if (responseBodyConverter != null) {
                return responseBodyConverter;
            }
        }
        annotationArr = new StringBuilder("Could not locate ResponseBody converter for ");
        annotationArr.append(type);
        annotationArr.append(".\n");
        if (converter$Factory != null) {
            annotationArr.append("  Skipped:");
            for (converter$Factory = null; converter$Factory < indexOf; converter$Factory++) {
                annotationArr.append("\n   * ");
                annotationArr.append(((Converter$Factory) this.converterFactories.get(converter$Factory)).getClass().getName());
            }
            annotationArr.append('\n');
        }
        annotationArr.append("  Tried:");
        converter$Factory = this.converterFactories.size();
        while (indexOf < converter$Factory) {
            annotationArr.append("\n   * ");
            annotationArr.append(((Converter$Factory) this.converterFactories.get(indexOf)).getClass().getName());
            indexOf++;
        }
        throw new IllegalArgumentException(annotationArr.toString());
    }

    public <T> Converter<T, String> stringConverter(Type type, Annotation[] annotationArr) {
        Utils.checkNotNull(type, "type == null");
        Utils.checkNotNull(annotationArr, "annotations == null");
        int size = this.converterFactories.size();
        for (int i = 0; i < size; i++) {
            Converter<T, String> stringConverter = ((Converter$Factory) this.converterFactories.get(i)).stringConverter(type, annotationArr, this);
            if (stringConverter != null) {
                return stringConverter;
            }
        }
        return ToStringConverter.INSTANCE;
    }

    @Nullable
    public Executor callbackExecutor() {
        return this.callbackExecutor;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }
}
