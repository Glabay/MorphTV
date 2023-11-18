package org.jsoup.parser;

import android.support.v4.app.NotificationCompat;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.android.exoplayer2.util.MimeTypes;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.helper.Validate;

public class Tag {
    private static final String[] blockTags = new String[]{"html", TtmlNode.TAG_HEAD, TtmlNode.TAG_BODY, "frameset", "script", "noscript", TtmlNode.TAG_STYLE, "meta", "link", "title", "frame", "noframes", "section", "nav", "aside", "hgroup", "header", "footer", TtmlNode.TAG_P, "h1", "h2", "h3", "h4", "h5", "h6", "ul", "ol", "pre", TtmlNode.TAG_DIV, "blockquote", "hr", "address", "figure", "figcaption", "form", "fieldset", "ins", "del", "s", "dl", "dt", "dd", "li", "table", "caption", "thead", "tfoot", "tbody", "colgroup", "col", "tr", "th", "td", MimeTypes.BASE_TYPE_VIDEO, MimeTypes.BASE_TYPE_AUDIO, "canvas", "details", "menu", "plaintext", "template", "article", "main", "svg", "math"};
    private static final String[] emptyTags = new String[]{"meta", "link", "base", "frame", "img", TtmlNode.TAG_BR, "wbr", "embed", "hr", "input", "keygen", "col", "command", "device", "area", "basefont", "bgsound", "menuitem", "param", "source", "track"};
    private static final String[] formListedTags = new String[]{"button", "fieldset", "input", "keygen", "object", "output", "select", "textarea"};
    private static final String[] formSubmitTags = new String[]{"input", "keygen", "object", "select", "textarea"};
    private static final String[] formatAsInlineTags = new String[]{"title", "a", TtmlNode.TAG_P, "h1", "h2", "h3", "h4", "h5", "h6", "pre", "address", "li", "th", "td", "script", TtmlNode.TAG_STYLE, "ins", "del", "s"};
    private static final String[] inlineTags = new String[]{"object", "base", "font", TtmlNode.TAG_TT, "i", "b", "u", "big", "small", "em", "strong", "dfn", "code", "samp", "kbd", "var", "cite", "abbr", "time", "acronym", "mark", "ruby", "rt", "rp", "a", "img", TtmlNode.TAG_BR, "wbr", "map", "q", "sub", "sup", "bdo", "iframe", "embed", TtmlNode.TAG_SPAN, "input", "select", "textarea", "label", "button", "optgroup", "option", "legend", "datalist", "keygen", "output", NotificationCompat.CATEGORY_PROGRESS, "meter", "area", "param", "source", "track", "summary", "command", "device", "area", "basefont", "bgsound", "menuitem", "param", "source", "track", "data", "bdi"};
    private static final String[] preserveWhitespaceTags = new String[]{"pre", "plaintext", "title", "textarea"};
    private static final Map<String, Tag> tags = new HashMap();
    private boolean canContainBlock = true;
    private boolean canContainInline = true;
    private boolean empty = false;
    private boolean formList = false;
    private boolean formSubmit = false;
    private boolean formatAsBlock = true;
    private boolean isBlock = true;
    private boolean preserveWhitespace = false;
    private boolean selfClosing = false;
    private String tagName;

    static {
        int i = 0;
        for (String tag : blockTags) {
            register(new Tag(tag));
        }
        for (String tag2 : inlineTags) {
            Tag tag3 = new Tag(tag2);
            tag3.isBlock = false;
            tag3.canContainBlock = false;
            tag3.formatAsBlock = false;
            register(tag3);
        }
        for (Object obj : emptyTags) {
            tag3 = (Tag) tags.get(obj);
            Validate.notNull(tag3);
            tag3.canContainBlock = false;
            tag3.canContainInline = false;
            tag3.empty = true;
        }
        for (Object obj2 : formatAsInlineTags) {
            tag3 = (Tag) tags.get(obj2);
            Validate.notNull(tag3);
            tag3.formatAsBlock = false;
        }
        for (Object obj22 : preserveWhitespaceTags) {
            tag3 = (Tag) tags.get(obj22);
            Validate.notNull(tag3);
            tag3.preserveWhitespace = true;
        }
        for (Object obj222 : formListedTags) {
            tag3 = (Tag) tags.get(obj222);
            Validate.notNull(tag3);
            tag3.formList = true;
        }
        String[] strArr = formSubmitTags;
        int length = strArr.length;
        while (i < length) {
            Tag tag4 = (Tag) tags.get(strArr[i]);
            Validate.notNull(tag4);
            tag4.formSubmit = true;
            i++;
        }
    }

    private Tag(String str) {
        this.tagName = str;
    }

    public String getName() {
        return this.tagName;
    }

    public static Tag valueOf(String str, ParseSettings parseSettings) {
        Validate.notNull(str);
        Tag tag = (Tag) tags.get(str);
        if (tag != null) {
            return tag;
        }
        str = parseSettings.normalizeTag(str);
        Validate.notEmpty(str);
        tag = (Tag) tags.get(str);
        if (tag != null) {
            return tag;
        }
        tag = new Tag(str);
        tag.isBlock = null;
        tag.canContainBlock = true;
        return tag;
    }

    public static Tag valueOf(String str) {
        return valueOf(str, ParseSettings.preserveCase);
    }

    public boolean isBlock() {
        return this.isBlock;
    }

    public boolean formatAsBlock() {
        return this.formatAsBlock;
    }

    public boolean canContainBlock() {
        return this.canContainBlock;
    }

    public boolean isInline() {
        return this.isBlock ^ 1;
    }

    public boolean isData() {
        return (this.canContainInline || isEmpty()) ? false : true;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public boolean isSelfClosing() {
        if (!this.empty) {
            if (!this.selfClosing) {
                return false;
            }
        }
        return true;
    }

    public boolean isKnownTag() {
        return tags.containsKey(this.tagName);
    }

    public static boolean isKnownTag(String str) {
        return tags.containsKey(str);
    }

    public boolean preserveWhitespace() {
        return this.preserveWhitespace;
    }

    public boolean isFormListed() {
        return this.formList;
    }

    public boolean isFormSubmittable() {
        return this.formSubmit;
    }

    Tag setSelfClosing() {
        this.selfClosing = true;
        return this;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tag)) {
            return false;
        }
        Tag tag = (Tag) obj;
        if (!this.tagName.equals(tag.tagName) || this.canContainBlock != tag.canContainBlock || this.canContainInline != tag.canContainInline || this.empty != tag.empty || this.formatAsBlock != tag.formatAsBlock || this.isBlock != tag.isBlock || this.preserveWhitespace != tag.preserveWhitespace || this.selfClosing != tag.selfClosing || this.formList != tag.formList) {
            return false;
        }
        if (this.formSubmit != tag.formSubmit) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((((((((((((((this.tagName.hashCode() * 31) + this.isBlock) * 31) + this.formatAsBlock) * 31) + this.canContainBlock) * 31) + this.canContainInline) * 31) + this.empty) * 31) + this.selfClosing) * 31) + this.preserveWhitespace) * 31) + this.formList) * 31) + this.formSubmit;
    }

    public String toString() {
        return this.tagName;
    }

    private static void register(Tag tag) {
        tags.put(tag.tagName, tag);
    }
}
