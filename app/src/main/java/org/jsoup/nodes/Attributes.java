package org.jsoup.nodes;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document.OutputSettings;

public class Attributes implements Iterable<Attribute>, Cloneable {
    protected static final String dataPrefix = "data-";
    private LinkedHashMap<String, Attribute> attributes = null;

    private class Dataset extends AbstractMap<String, String> {

        private class DatasetIterator implements Iterator<Entry<String, String>> {
            private Attribute attr;
            private Iterator<Attribute> attrIter;

            private DatasetIterator() {
                this.attrIter = Attributes.this.attributes.values().iterator();
            }

            public boolean hasNext() {
                while (this.attrIter.hasNext()) {
                    this.attr = (Attribute) this.attrIter.next();
                    if (this.attr.isDataAttribute()) {
                        return true;
                    }
                }
                return false;
            }

            public Entry<String, String> next() {
                return new Attribute(this.attr.getKey().substring(Attributes.dataPrefix.length()), this.attr.getValue());
            }

            public void remove() {
                Attributes.this.attributes.remove(this.attr.getKey());
            }
        }

        private class EntrySet extends AbstractSet<Entry<String, String>> {
            private EntrySet() {
            }

            public Iterator<Entry<String, String>> iterator() {
                return new DatasetIterator();
            }

            public int size() {
                int i = 0;
                while (new DatasetIterator().hasNext()) {
                    i++;
                }
                return i;
            }
        }

        private Dataset() {
            if (Attributes.this.attributes == null) {
                Attributes.this.attributes = new LinkedHashMap(2);
            }
        }

        public Set<Entry<String, String>> entrySet() {
            return new EntrySet();
        }

        public String put(String str, String str2) {
            str = Attributes.dataKey(str);
            String value = Attributes.this.hasKey(str) ? ((Attribute) Attributes.this.attributes.get(str)).getValue() : null;
            Attributes.this.attributes.put(str, new Attribute(str, str2));
            return value;
        }
    }

    public String get(String str) {
        Validate.notEmpty(str);
        if (this.attributes == null) {
            return "";
        }
        Attribute attribute = (Attribute) this.attributes.get(str);
        return attribute != null ? attribute.getValue() : "";
    }

    public String getIgnoreCase(String str) {
        Validate.notEmpty(str);
        if (this.attributes == null) {
            return "";
        }
        for (String str2 : this.attributes.keySet()) {
            if (str2.equalsIgnoreCase(str)) {
                return ((Attribute) this.attributes.get(str2)).getValue();
            }
        }
        return "";
    }

    public void put(String str, String str2) {
        put(new Attribute(str, str2));
    }

    public void put(String str, boolean z) {
        if (z) {
            put(new BooleanAttribute(str));
        } else {
            remove(str);
        }
    }

    public void put(Attribute attribute) {
        Validate.notNull(attribute);
        if (this.attributes == null) {
            this.attributes = new LinkedHashMap(2);
        }
        this.attributes.put(attribute.getKey(), attribute);
    }

    public void remove(String str) {
        Validate.notEmpty(str);
        if (this.attributes != null) {
            this.attributes.remove(str);
        }
    }

    public void removeIgnoreCase(String str) {
        Validate.notEmpty(str);
        if (this.attributes != null) {
            for (String str2 : this.attributes.keySet()) {
                if (str2.equalsIgnoreCase(str)) {
                    this.attributes.remove(str2);
                }
            }
        }
    }

    public boolean hasKey(String str) {
        return (this.attributes == null || this.attributes.containsKey(str) == null) ? null : true;
    }

    public boolean hasKeyIgnoreCase(String str) {
        if (this.attributes == null) {
            return false;
        }
        for (String equalsIgnoreCase : this.attributes.keySet()) {
            if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        if (this.attributes == null) {
            return 0;
        }
        return this.attributes.size();
    }

    public void addAll(Attributes attributes) {
        if (attributes.size() != 0) {
            if (this.attributes == null) {
                this.attributes = new LinkedHashMap(attributes.size());
            }
            this.attributes.putAll(attributes.attributes);
        }
    }

    public Iterator<Attribute> iterator() {
        if (this.attributes != null) {
            if (!this.attributes.isEmpty()) {
                return this.attributes.values().iterator();
            }
        }
        return Collections.emptyList().iterator();
    }

    public List<Attribute> asList() {
        if (this.attributes == null) {
            return Collections.emptyList();
        }
        List arrayList = new ArrayList(this.attributes.size());
        for (Entry value : this.attributes.entrySet()) {
            arrayList.add(value.getValue());
        }
        return Collections.unmodifiableList(arrayList);
    }

    public Map<String, String> dataset() {
        return new Dataset();
    }

    public String html() {
        Appendable stringBuilder = new StringBuilder();
        try {
            html(stringBuilder, new Document("").outputSettings());
            return stringBuilder.toString();
        } catch (Throwable e) {
            throw new SerializationException(e);
        }
    }

    void html(Appendable appendable, OutputSettings outputSettings) throws IOException {
        if (this.attributes != null) {
            for (Entry value : this.attributes.entrySet()) {
                Attribute attribute = (Attribute) value.getValue();
                appendable.append(StringUtils.SPACE);
                attribute.html(appendable, outputSettings);
            }
        }
    }

    public String toString() {
        return html();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Attributes)) {
            return false;
        }
        Attributes attributes = (Attributes) obj;
        if (this.attributes != null) {
            if (this.attributes.equals(attributes.attributes) == null) {
            }
            return z;
        } else if (attributes.attributes == null) {
            return z;
        }
        z = false;
        return z;
    }

    public int hashCode() {
        return this.attributes != null ? this.attributes.hashCode() : 0;
    }

    public Attributes clone() {
        if (this.attributes == null) {
            return new Attributes();
        }
        try {
            Attributes attributes = (Attributes) super.clone();
            attributes.attributes = new LinkedHashMap(this.attributes.size());
            Iterator it = iterator();
            while (it.hasNext()) {
                Attribute attribute = (Attribute) it.next();
                attributes.attributes.put(attribute.getKey(), attribute.clone());
            }
            return attributes;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static String dataKey(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dataPrefix);
        stringBuilder.append(str);
        return stringBuilder.toString();
    }
}
