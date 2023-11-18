package org.apache.commons.lang3.text;

import java.util.Map;

public abstract class StrLookup<V> {
    private static final StrLookup<String> NONE_LOOKUP = new MapStrLookup(null);
    private static final StrLookup<String> SYSTEM_PROPERTIES_LOOKUP = new SystemPropertiesStrLookup();

    static class MapStrLookup<V> extends StrLookup<V> {
        private final Map<String, V> map;

        MapStrLookup(Map<String, V> map) {
            this.map = map;
        }

        public String lookup(String str) {
            if (this.map == null) {
                return null;
            }
            str = this.map.get(str);
            if (str == null) {
                return null;
            }
            return str.toString();
        }
    }

    private static class SystemPropertiesStrLookup extends StrLookup<String> {
        private SystemPropertiesStrLookup() {
        }

        public java.lang.String lookup(java.lang.String r2) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
            /*
            r1 = this;
            r0 = r2.length();
            if (r0 <= 0) goto L_0x000b;
        L_0x0006:
            r2 = java.lang.System.getProperty(r2);	 Catch:{ SecurityException -> 0x000b }
            return r2;
        L_0x000b:
            r2 = 0;
            return r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.text.StrLookup.SystemPropertiesStrLookup.lookup(java.lang.String):java.lang.String");
        }
    }

    public abstract String lookup(String str);

    public static StrLookup<?> noneLookup() {
        return NONE_LOOKUP;
    }

    public static StrLookup<String> systemPropertiesLookup() {
        return SYSTEM_PROPERTIES_LOOKUP;
    }

    public static <V> StrLookup<V> mapLookup(Map<String, V> map) {
        return new MapStrLookup(map);
    }

    protected StrLookup() {
    }
}
