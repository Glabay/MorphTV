package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.entry.data_type;
import com.frostwire.jlibtorrent.swig.entry_vector;
import com.frostwire.jlibtorrent.swig.string_entry_map;
import com.frostwire.jlibtorrent.swig.string_vector;
import java.io.File;
import java.io.IOException;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Entry {
    /* renamed from: e */
    private final entry f25e;

    private static final class EntryList extends AbstractList<Entry> {
        /* renamed from: v */
        private final entry_vector f23v;

        public EntryList(entry_vector entry_vector) {
            this.f23v = entry_vector;
        }

        public Entry get(int i) {
            return new Entry(this.f23v.get(i));
        }

        public boolean add(Entry entry) {
            this.f23v.push_back(entry.swig());
            return true;
        }

        public int size() {
            return (int) this.f23v.size();
        }

        public void clear() {
            this.f23v.clear();
        }

        public boolean isEmpty() {
            return this.f23v.empty();
        }
    }

    private static final class EntryMap extends AbstractMap<String, Entry> {
        /* renamed from: m */
        private final string_entry_map f24m;

        public EntryMap(string_entry_map string_entry_map) {
            this.f24m = string_entry_map;
        }

        public Entry get(Object obj) {
            return this.f24m.has_key(obj.toString()) ? new Entry(this.f24m.get(obj.toString())) : null;
        }

        public Entry put(String str, Entry entry) {
            Entry entry2 = get((Object) str);
            this.f24m.set(str, entry.swig());
            return entry2;
        }

        public int size() {
            return (int) this.f24m.size();
        }

        public void clear() {
            this.f24m.clear();
        }

        public boolean containsKey(Object obj) {
            return this.f24m.has_key(obj.toString());
        }

        public boolean isEmpty() {
            return this.f24m.empty();
        }

        public Set<String> keySet() {
            Set hashSet = new HashSet();
            string_vector keys = this.f24m.keys();
            int size = (int) keys.size();
            for (int i = 0; i < size; i++) {
                hashSet.add(keys.get(i));
            }
            return hashSet;
        }

        public Set<java.util.Map.Entry<String, Entry>> entrySet() {
            throw new UnsupportedOperationException();
        }
    }

    public Entry(entry entry) {
        this.f25e = entry;
    }

    public Entry(String str) {
        this(new entry(str));
    }

    public Entry(long j) {
        this(new entry(j));
    }

    public entry swig() {
        return this.f25e;
    }

    public byte[] bencode() {
        return Vectors.byte_vector2bytes(this.f25e.bencode());
    }

    public String string() {
        return this.f25e.string();
    }

    public long integer() {
        return this.f25e.integer();
    }

    public List<Entry> list() {
        return new EntryList(this.f25e.list());
    }

    public Map<String, Entry> dictionary() {
        return new EntryMap(this.f25e.dict());
    }

    public String toString() {
        return this.f25e.to_string();
    }

    public static Entry bdecode(byte[] bArr) {
        return new Entry(entry.bdecode(Vectors.bytes2byte_vector(bArr)));
    }

    public static Entry bdecode(File file) throws IOException {
        return bdecode(Files.bytes(file));
    }

    public static Entry fromList(List<?> list) {
        entry entry = new entry(data_type.list_t);
        entry_vector list2 = entry.list();
        for (Object next : list) {
            if (next instanceof String) {
                list2.push_back(new entry((String) next));
            } else if (next instanceof Integer) {
                list2.push_back(new entry((long) ((Integer) next).intValue()));
            } else if (next instanceof Entry) {
                list2.push_back(((Entry) next).swig());
            } else if (next instanceof entry) {
                list2.push_back((entry) next);
            } else if (next instanceof List) {
                list2.push_back(fromList((List) next).swig());
            } else if (next instanceof Map) {
                list2.push_back(fromMap((Map) next).swig());
            } else {
                list2.push_back(new entry(next.toString()));
            }
        }
        return new Entry(entry);
    }

    public static Entry fromMap(Map<String, ?> map) {
        entry entry = new entry(data_type.dictionary_t);
        string_entry_map dict = entry.dict();
        for (String str : map.keySet()) {
            Object obj = map.get(str);
            if (obj instanceof String) {
                dict.set(str, new entry((String) obj));
            } else if (obj instanceof Integer) {
                dict.set(str, new entry((long) ((Integer) obj).intValue()));
            } else if (obj instanceof Entry) {
                dict.set(str, ((Entry) obj).swig());
            } else if (obj instanceof entry) {
                dict.set(str, (entry) obj);
            } else if (obj instanceof List) {
                dict.set(str, fromList((List) obj).swig());
            } else if (obj instanceof Map) {
                dict.set(str, fromMap((Map) obj).swig());
            } else {
                dict.set(str, new entry(obj.toString()));
            }
        }
        return new Entry(entry);
    }
}
