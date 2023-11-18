package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;

@GwtCompatible
@Beta
public abstract class Escaper {
    private final Function<String, String> asFunction = new C11161();

    /* renamed from: com.google.common.escape.Escaper$1 */
    class C11161 implements Function<String, String> {
        C11161() {
        }

        public String apply(String str) {
            return Escaper.this.escape(str);
        }
    }

    public abstract String escape(String str);

    protected Escaper() {
    }

    public final Function<String, String> asFunction() {
        return this.asFunction;
    }
}
