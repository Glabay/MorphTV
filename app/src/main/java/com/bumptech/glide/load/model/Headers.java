package com.bumptech.glide.load.model;

import com.bumptech.glide.load.model.LazyHeaders.Builder;
import java.util.Collections;
import java.util.Map;

public interface Headers {
    public static final Headers DEFAULT = new Builder().build();
    @Deprecated
    public static final Headers NONE = new C05901();

    /* renamed from: com.bumptech.glide.load.model.Headers$1 */
    static class C05901 implements Headers {
        C05901() {
        }

        public Map<String, String> getHeaders() {
            return Collections.emptyMap();
        }
    }

    Map<String, String> getHeaders();
}
