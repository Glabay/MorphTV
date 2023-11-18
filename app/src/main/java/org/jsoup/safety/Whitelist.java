package org.jsoup.safety;

import com.google.android.exoplayer2.text.ttml.TtmlNode;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

public class Whitelist {
    private Map<TagName, Set<AttributeKey>> attributes = new HashMap();
    private Map<TagName, Map<AttributeKey, AttributeValue>> enforcedAttributes = new HashMap();
    private boolean preserveRelativeLinks = false;
    private Map<TagName, Map<AttributeKey, Set<Protocol>>> protocols = new HashMap();
    private Set<TagName> tagNames = new HashSet();

    static abstract class TypedValue {
        private String value;

        TypedValue(String str) {
            Validate.notNull(str);
            this.value = str;
        }

        public int hashCode() {
            return 31 + (this.value == null ? 0 : this.value.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            TypedValue typedValue = (TypedValue) obj;
            if (this.value == null) {
                if (typedValue.value != null) {
                    return false;
                }
            } else if (this.value.equals(typedValue.value) == null) {
                return false;
            }
            return true;
        }

        public String toString() {
            return this.value;
        }
    }

    static class AttributeKey extends TypedValue {
        AttributeKey(String str) {
            super(str);
        }

        static AttributeKey valueOf(String str) {
            return new AttributeKey(str);
        }
    }

    static class AttributeValue extends TypedValue {
        AttributeValue(String str) {
            super(str);
        }

        static AttributeValue valueOf(String str) {
            return new AttributeValue(str);
        }
    }

    static class Protocol extends TypedValue {
        Protocol(String str) {
            super(str);
        }

        static Protocol valueOf(String str) {
            return new Protocol(str);
        }
    }

    static class TagName extends TypedValue {
        TagName(String str) {
            super(str);
        }

        static TagName valueOf(String str) {
            return new TagName(str);
        }
    }

    public static Whitelist none() {
        return new Whitelist();
    }

    public static Whitelist simpleText() {
        return new Whitelist().addTags("b", "em", "i", "strong", "u");
    }

    public static Whitelist basic() {
        return new Whitelist().addTags("a", "b", "blockquote", TtmlNode.TAG_BR, "cite", "code", "dd", "dl", "dt", "em", "i", "li", "ol", TtmlNode.TAG_P, "pre", "q", "small", TtmlNode.TAG_SPAN, "strike", "strong", "sub", "sup", "u", "ul").addAttributes("a", "href").addAttributes("blockquote", "cite").addAttributes("q", "cite").addProtocols("a", "href", "ftp", "http", "https", "mailto").addProtocols("blockquote", "cite", "http", "https").addProtocols("cite", "cite", "http", "https").addEnforcedAttribute("a", "rel", "nofollow");
    }

    public static Whitelist basicWithImages() {
        return basic().addTags("img").addAttributes("img", "align", "alt", "height", "src", "title", "width").addProtocols("img", "src", "http", "https");
    }

    public static Whitelist relaxed() {
        return new Whitelist().addTags("a", "b", "blockquote", TtmlNode.TAG_BR, "caption", "cite", "code", "col", "colgroup", "dd", TtmlNode.TAG_DIV, "dl", "dt", "em", "h1", "h2", "h3", "h4", "h5", "h6", "i", "img", "li", "ol", TtmlNode.TAG_P, "pre", "q", "small", TtmlNode.TAG_SPAN, "strike", "strong", "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u", "ul").addAttributes("a", "href", "title").addAttributes("blockquote", "cite").addAttributes("col", TtmlNode.TAG_SPAN, "width").addAttributes("colgroup", TtmlNode.TAG_SPAN, "width").addAttributes("img", "align", "alt", "height", "src", "title", "width").addAttributes("ol", TtmlNode.START, "type").addAttributes("q", "cite").addAttributes("table", "summary", "width").addAttributes("td", "abbr", "axis", "colspan", "rowspan", "width").addAttributes("th", "abbr", "axis", "colspan", "rowspan", "scope", "width").addAttributes("ul", "type").addProtocols("a", "href", "ftp", "http", "https", "mailto").addProtocols("blockquote", "cite", "http", "https").addProtocols("cite", "cite", "http", "https").addProtocols("img", "src", "http", "https").addProtocols("q", "cite", "http", "https");
    }

    public Whitelist addTags(String... strArr) {
        Validate.notNull(strArr);
        for (String str : strArr) {
            Validate.notEmpty(str);
            this.tagNames.add(TagName.valueOf(str));
        }
        return this;
    }

    public Whitelist removeTags(String... strArr) {
        Validate.notNull(strArr);
        for (String str : strArr) {
            Validate.notEmpty(str);
            TagName valueOf = TagName.valueOf(str);
            if (this.tagNames.remove(valueOf)) {
                this.attributes.remove(valueOf);
                this.enforcedAttributes.remove(valueOf);
                this.protocols.remove(valueOf);
            }
        }
        return this;
    }

    public Whitelist addAttributes(String str, String... strArr) {
        Validate.notEmpty(str);
        Validate.notNull(strArr);
        Validate.isTrue(strArr.length > 0, "No attributes supplied.");
        str = TagName.valueOf(str);
        if (!this.tagNames.contains(str)) {
            this.tagNames.add(str);
        }
        Collection hashSet = new HashSet();
        for (String str2 : strArr) {
            Validate.notEmpty(str2);
            hashSet.add(AttributeKey.valueOf(str2));
        }
        if (this.attributes.containsKey(str) != null) {
            ((Set) this.attributes.get(str)).addAll(hashSet);
        } else {
            this.attributes.put(str, hashSet);
        }
        return this;
    }

    public Whitelist removeAttributes(String str, String... strArr) {
        Validate.notEmpty(str);
        Validate.notNull(strArr);
        Validate.isTrue(strArr.length > 0, "No attributes supplied.");
        TagName valueOf = TagName.valueOf(str);
        Collection hashSet = new HashSet();
        for (String str2 : strArr) {
            Validate.notEmpty(str2);
            hashSet.add(AttributeKey.valueOf(str2));
        }
        if (!(this.tagNames.contains(valueOf) == null || this.attributes.containsKey(valueOf) == null)) {
            Set set = (Set) this.attributes.get(valueOf);
            set.removeAll(hashSet);
            if (set.isEmpty() != null) {
                this.attributes.remove(valueOf);
            }
        }
        if (str.equals(":all") != null) {
            for (TagName tagName : this.attributes.keySet()) {
                Set set2 = (Set) this.attributes.get(tagName);
                set2.removeAll(hashSet);
                if (set2.isEmpty()) {
                    this.attributes.remove(tagName);
                }
            }
        }
        return this;
    }

    public Whitelist addEnforcedAttribute(String str, String str2, String str3) {
        Validate.notEmpty(str);
        Validate.notEmpty(str2);
        Validate.notEmpty(str3);
        str = TagName.valueOf(str);
        if (!this.tagNames.contains(str)) {
            this.tagNames.add(str);
        }
        str2 = AttributeKey.valueOf(str2);
        str3 = AttributeValue.valueOf(str3);
        if (this.enforcedAttributes.containsKey(str)) {
            ((Map) this.enforcedAttributes.get(str)).put(str2, str3);
        } else {
            Map hashMap = new HashMap();
            hashMap.put(str2, str3);
            this.enforcedAttributes.put(str, hashMap);
        }
        return this;
    }

    public Whitelist removeEnforcedAttribute(String str, String str2) {
        Validate.notEmpty(str);
        Validate.notEmpty(str2);
        str = TagName.valueOf(str);
        if (this.tagNames.contains(str) && this.enforcedAttributes.containsKey(str)) {
            Map map = (Map) this.enforcedAttributes.get(str);
            map.remove(AttributeKey.valueOf(str2));
            if (map.isEmpty() != null) {
                this.enforcedAttributes.remove(str);
            }
        }
        return this;
    }

    public Whitelist preserveRelativeLinks(boolean z) {
        this.preserveRelativeLinks = z;
        return this;
    }

    public Whitelist addProtocols(String str, String str2, String... strArr) {
        Validate.notEmpty(str);
        Validate.notEmpty(str2);
        Validate.notNull(strArr);
        str = TagName.valueOf(str);
        str2 = AttributeKey.valueOf(str2);
        if (this.protocols.containsKey(str)) {
            str = (Map) this.protocols.get(str);
        } else {
            HashMap hashMap = new HashMap();
            this.protocols.put(str, hashMap);
            str = hashMap;
        }
        if (str.containsKey(str2)) {
            str = (Set) str.get(str2);
        } else {
            HashSet hashSet = new HashSet();
            str.put(str2, hashSet);
            str = hashSet;
        }
        for (String str3 : strArr) {
            Validate.notEmpty(str3);
            str.add(Protocol.valueOf(str3));
        }
        return this;
    }

    public Whitelist removeProtocols(String str, String str2, String... strArr) {
        Validate.notEmpty(str);
        Validate.notEmpty(str2);
        Validate.notNull(strArr);
        str = TagName.valueOf(str);
        str2 = AttributeKey.valueOf(str2);
        if (this.protocols.containsKey(str)) {
            Map map = (Map) this.protocols.get(str);
            if (map.containsKey(str2)) {
                Set set = (Set) map.get(str2);
                for (String str3 : strArr) {
                    Validate.notEmpty(str3);
                    set.remove(Protocol.valueOf(str3));
                }
                if (set.isEmpty() != null) {
                    map.remove(str2);
                    if (map.isEmpty() != null) {
                        this.protocols.remove(str);
                    }
                }
            }
        }
        return this;
    }

    protected boolean isSafeTag(String str) {
        return this.tagNames.contains(TagName.valueOf(str));
    }

    protected boolean isSafeAttribute(String str, Element element, Attribute attribute) {
        TagName valueOf = TagName.valueOf(str);
        AttributeKey valueOf2 = AttributeKey.valueOf(attribute.getKey());
        boolean z = false;
        if (!this.attributes.containsKey(valueOf) || !((Set) this.attributes.get(valueOf)).contains(valueOf2)) {
            if (str.equals(":all") == null && isSafeAttribute(":all", element, attribute) != null) {
                z = true;
            }
            return z;
        } else if (this.protocols.containsKey(valueOf) == null) {
            return true;
        } else {
            Map map = (Map) this.protocols.get(valueOf);
            if (!(map.containsKey(valueOf2) && testValidProtocol(element, attribute, (Set) map.get(valueOf2)) == null)) {
                z = true;
            }
            return z;
        }
    }

    private boolean testValidProtocol(Element element, Attribute attribute, Set<Protocol> set) {
        String absUrl = element.absUrl(attribute.getKey());
        if (absUrl.length() == 0) {
            absUrl = attribute.getValue();
        }
        if (!this.preserveRelativeLinks) {
            attribute.setValue(absUrl);
        }
        for (Protocol protocol : set) {
            set = protocol.toString();
            if (!set.equals("#")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(set);
                stringBuilder.append(":");
                if (absUrl.toLowerCase().startsWith(stringBuilder.toString()) != null) {
                    return true;
                }
            } else if (isValidAnchor(absUrl) != null) {
                return true;
            }
        }
        return null;
    }

    private boolean isValidAnchor(String str) {
        return (str.startsWith("#") && str.matches(".*\\s.*") == null) ? true : null;
    }

    Attributes getEnforcedAttributes(String str) {
        Attributes attributes = new Attributes();
        str = TagName.valueOf(str);
        if (this.enforcedAttributes.containsKey(str)) {
            for (Entry entry : ((Map) this.enforcedAttributes.get(str)).entrySet()) {
                attributes.put(((AttributeKey) entry.getKey()).toString(), ((AttributeValue) entry.getValue()).toString());
            }
        }
        return attributes;
    }
}
