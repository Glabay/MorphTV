package org.jsoup.nodes;

import java.io.IOException;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document.OutputSettings;

public class TextNode extends Node {
    private static final String TEXT_KEY = "text";
    String text;

    public String nodeName() {
        return "#text";
    }

    void outerHtmlTail(Appendable appendable, int i, OutputSettings outputSettings) {
    }

    public TextNode(String str, String str2) {
        this.baseUri = str2;
        this.text = str;
    }

    public String text() {
        return normaliseWhitespace(getWholeText());
    }

    public TextNode text(String str) {
        this.text = str;
        if (this.attributes != null) {
            this.attributes.put("text", str);
        }
        return this;
    }

    public String getWholeText() {
        return this.attributes == null ? this.text : this.attributes.get("text");
    }

    public boolean isBlank() {
        return StringUtil.isBlank(getWholeText());
    }

    public TextNode splitText(int i) {
        Validate.isTrue(i >= 0, "Split offset must be not be negative");
        Validate.isTrue(i < this.text.length(), "Split offset must not be greater than current text length");
        String substring = getWholeText().substring(0, i);
        i = getWholeText().substring(i);
        text(substring);
        TextNode textNode = new TextNode(i, baseUri());
        if (parent() != 0) {
            parent().addChildren(siblingIndex() + 1, textNode);
        }
        return textNode;
    }

    void outerHtmlHead(Appendable appendable, int i, OutputSettings outputSettings) throws IOException {
        if (outputSettings.prettyPrint() && ((siblingIndex() == 0 && (this.parentNode instanceof Element) && ((Element) this.parentNode).tag().formatAsBlock() && !isBlank()) || (outputSettings.outline() && siblingNodes().size() > 0 && !isBlank()))) {
            indent(appendable, i, outputSettings);
        }
        boolean z = (outputSettings.prettyPrint() == 0 || (parent() instanceof Element) == 0 || Element.preserveWhitespace(parent()) != 0) ? false : true;
        Entities.escape(appendable, getWholeText(), outputSettings, false, z, false);
    }

    public String toString() {
        return outerHtml();
    }

    public static TextNode createFromEncoded(String str, String str2) {
        return new TextNode(Entities.unescape(str), str2);
    }

    static String normaliseWhitespace(String str) {
        return StringUtil.normaliseWhitespace(str);
    }

    static String stripLeadingWhitespace(String str) {
        return str.replaceFirst("^\\s+", "");
    }

    static boolean lastCharIsWhitespace(StringBuilder stringBuilder) {
        return stringBuilder.length() != 0 && stringBuilder.charAt(stringBuilder.length() - 1) == 32;
    }

    private void ensureAttributes() {
        if (this.attributes == null) {
            this.attributes = new Attributes();
            this.attributes.put("text", this.text);
        }
    }

    public String attr(String str) {
        ensureAttributes();
        return super.attr(str);
    }

    public Attributes attributes() {
        ensureAttributes();
        return super.attributes();
    }

    public Node attr(String str, String str2) {
        ensureAttributes();
        return super.attr(str, str2);
    }

    public boolean hasAttr(String str) {
        ensureAttributes();
        return super.hasAttr(str);
    }

    public Node removeAttr(String str) {
        ensureAttributes();
        return super.removeAttr(str);
    }

    public String absUrl(String str) {
        ensureAttributes();
        return super.absUrl(str);
    }
}
