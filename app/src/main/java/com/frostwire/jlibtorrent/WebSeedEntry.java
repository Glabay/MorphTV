package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.string_string_pair;
import com.frostwire.jlibtorrent.swig.string_string_pair_vector;
import com.frostwire.jlibtorrent.swig.web_seed_entry;
import com.frostwire.jlibtorrent.swig.web_seed_entry.type_t;
import java.util.ArrayList;

public final class WebSeedEntry {
    /* renamed from: e */
    private final web_seed_entry f35e;

    public enum Type {
        URL_SEED(type_t.url_seed.swigValue()),
        HTTP_SEED(type_t.http_seed.swigValue()),
        UNKNOWN(-1);
        
        private final int swigValue;

        private Type(int i) {
            this.swigValue = i;
        }

        public int swig() {
            return this.swigValue;
        }

        public static Type fromSwig(int i) {
            for (Type type : (Type[]) Type.class.getEnumConstants()) {
                if (type.swig() == i) {
                    return type;
                }
            }
            return UNKNOWN;
        }
    }

    public WebSeedEntry(web_seed_entry web_seed_entry) {
        this.f35e = web_seed_entry;
    }

    public web_seed_entry swig() {
        return this.f35e;
    }

    public String url() {
        return this.f35e.getUrl();
    }

    public String auth() {
        return this.f35e.getAuth();
    }

    public ArrayList<Pair<String, String>> extraHeaders() {
        string_string_pair_vector extra_headers = this.f35e.getExtra_headers();
        int size = (int) extra_headers.size();
        ArrayList<Pair<String, String>> arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            string_string_pair string_string_pair = extra_headers.get(i);
            arrayList.add(new Pair(string_string_pair.getFirst(), string_string_pair.getSecond()));
        }
        return arrayList;
    }

    public Type type() {
        return Type.fromSwig(this.f35e.getType());
    }
}
