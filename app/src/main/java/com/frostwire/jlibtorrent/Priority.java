package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.int_vector;

public enum Priority {
    IGNORE(0),
    NORMAL(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    UNKNOWN(-1);
    
    private final int swigValue;

    private Priority(int i) {
        this.swigValue = i;
    }

    public int swig() {
        return this.swigValue;
    }

    public static Priority fromSwig(int i) {
        for (Priority priority : (Priority[]) Priority.class.getEnumConstants()) {
            if (priority.swig() == i) {
                return priority;
            }
        }
        return UNKNOWN;
    }

    public static Priority[] array(Priority priority, int i) {
        Priority[] priorityArr = new Priority[i];
        for (int i2 = 0; i2 < i; i2++) {
            priorityArr[i2] = priority;
        }
        return priorityArr;
    }

    static int_vector array2int_vector(Priority[] priorityArr) {
        int_vector int_vector = new int_vector();
        for (Priority priority : priorityArr) {
            Priority priority2;
            if (priority2 == UNKNOWN) {
                priority2 = IGNORE;
            }
            int_vector.push_back(priority2.swig());
        }
        return int_vector;
    }

    static byte_vector array2byte_vector(Priority[] priorityArr) {
        byte_vector byte_vector = new byte_vector();
        for (Priority priority : priorityArr) {
            Priority priority2;
            if (priority2 == UNKNOWN) {
                priority2 = IGNORE;
            }
            byte_vector.push_back((byte) priority2.swig());
        }
        return byte_vector;
    }
}
