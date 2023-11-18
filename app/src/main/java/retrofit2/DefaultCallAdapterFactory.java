package retrofit2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

final class DefaultCallAdapterFactory extends CallAdapter$Factory {
    static final CallAdapter$Factory INSTANCE = new DefaultCallAdapterFactory();

    DefaultCallAdapterFactory() {
    }

    public CallAdapter<?, ?> get(Type type, Annotation[] annotationArr, Retrofit retrofit) {
        if (getRawType(type) != Call.class) {
            return null;
        }
        type = Utils.getCallResponseType(type);
        return new CallAdapter<Object, Call<?>>() {
            public Call<Object> adapt(Call<Object> call) {
                return call;
            }

            public Type responseType() {
                return type;
            }
        };
    }
}
