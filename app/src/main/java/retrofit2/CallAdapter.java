package retrofit2;

import java.lang.reflect.Type;

public interface CallAdapter<R, T> {
    T adapt(Call<R> call);

    Type responseType();
}
