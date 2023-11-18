package org.apache.commons.lang3.reflect;

import org.apache.commons.lang3.BooleanUtils;

public class InheritanceUtils {
    public static int distance(Class<?> cls, Class<?> cls2) {
        int i = -1;
        if (cls != null) {
            if (cls2 != null) {
                if (cls.equals(cls2)) {
                    return null;
                }
                cls = cls.getSuperclass();
                int toInteger = BooleanUtils.toInteger(cls2.equals(cls));
                if (toInteger == 1) {
                    return toInteger;
                }
                toInteger += distance(cls, cls2);
                if (toInteger > 0) {
                    i = toInteger + 1;
                }
                return i;
            }
        }
        return -1;
    }
}
