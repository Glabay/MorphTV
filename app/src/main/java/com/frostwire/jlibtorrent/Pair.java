package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.string_int_pair;
import com.frostwire.jlibtorrent.swig.string_string_pair;

public final class Pair<T1, T2> {
    public final T1 first;
    public final T2 second;

    public Pair(T1 t1, T2 t2) {
        this.first = t1;
        this.second = t2;
    }

    string_string_pair to_string_string_pair() {
        if (String.class.equals(this.first.getClass())) {
            if (String.class.equals(this.second.getClass())) {
                return new string_string_pair((String) this.first, (String) this.second);
            }
        }
        throw new IllegalArgumentException("Incompatible types");
    }

    string_int_pair to_string_int_pair() {
        if (String.class.equals(this.first.getClass())) {
            if (Integer.class.equals(this.second.getClass())) {
                return new string_int_pair((String) this.first, ((Integer) this.second).intValue());
            }
        }
        throw new IllegalArgumentException("Incompatible types");
    }
}
