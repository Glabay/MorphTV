package retrofit2;

import java.io.IOException;

public interface Converter<F, T> {
    T convert(F f) throws IOException;
}
