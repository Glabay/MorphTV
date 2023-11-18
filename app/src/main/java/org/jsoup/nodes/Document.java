package org.jsoup.nodes;

import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.android.gms.dynamite.ProviderConstants;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Tag;

public class Document extends Element {
    private String location;
    private OutputSettings outputSettings = new OutputSettings();
    private QuirksMode quirksMode = QuirksMode.noQuirks;
    private boolean updateMetaCharset = false;

    public static class OutputSettings implements Cloneable {
        private Charset charset = Charset.forName("UTF-8");
        private EscapeMode escapeMode = EscapeMode.base;
        private int indentAmount = 1;
        private boolean outline = false;
        private boolean prettyPrint = true;
        private Syntax syntax = Syntax.html;

        public enum Syntax {
            html,
            xml
        }

        public EscapeMode escapeMode() {
            return this.escapeMode;
        }

        public OutputSettings escapeMode(EscapeMode escapeMode) {
            this.escapeMode = escapeMode;
            return this;
        }

        public Charset charset() {
            return this.charset;
        }

        public OutputSettings charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public OutputSettings charset(String str) {
            charset(Charset.forName(str));
            return this;
        }

        CharsetEncoder encoder() {
            return this.charset.newEncoder();
        }

        public Syntax syntax() {
            return this.syntax;
        }

        public OutputSettings syntax(Syntax syntax) {
            this.syntax = syntax;
            return this;
        }

        public boolean prettyPrint() {
            return this.prettyPrint;
        }

        public OutputSettings prettyPrint(boolean z) {
            this.prettyPrint = z;
            return this;
        }

        public boolean outline() {
            return this.outline;
        }

        public OutputSettings outline(boolean z) {
            this.outline = z;
            return this;
        }

        public int indentAmount() {
            return this.indentAmount;
        }

        public OutputSettings indentAmount(int i) {
            Validate.isTrue(i >= 0);
            this.indentAmount = i;
            return this;
        }

        public OutputSettings clone() {
            try {
                OutputSettings outputSettings = (OutputSettings) super.clone();
                outputSettings.charset(this.charset.name());
                outputSettings.escapeMode = EscapeMode.valueOf(this.escapeMode.name());
                return outputSettings;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    public enum QuirksMode {
        noQuirks,
        quirks,
        limitedQuirks
    }

    public String nodeName() {
        return "#document";
    }

    public Document(String str) {
        super(Tag.valueOf("#root", ParseSettings.htmlDefault), str);
        this.location = str;
    }

    public static Document createShell(String str) {
        Validate.notNull(str);
        Document document = new Document(str);
        str = document.appendElement("html");
        str.appendElement(TtmlNode.TAG_HEAD);
        str.appendElement(TtmlNode.TAG_BODY);
        return document;
    }

    public String location() {
        return this.location;
    }

    public Element head() {
        return findFirstElementByTagName(TtmlNode.TAG_HEAD, this);
    }

    public Element body() {
        return findFirstElementByTagName(TtmlNode.TAG_BODY, this);
    }

    public String title() {
        Element first = getElementsByTag("title").first();
        return first != null ? StringUtil.normaliseWhitespace(first.text()).trim() : "";
    }

    public void title(String str) {
        Validate.notNull(str);
        Element first = getElementsByTag("title").first();
        if (first == null) {
            head().appendElement("title").text(str);
        } else {
            first.text(str);
        }
    }

    public Element createElement(String str) {
        return new Element(Tag.valueOf(str, ParseSettings.preserveCase), baseUri());
    }

    public Document normalise() {
        Element findFirstElementByTagName = findFirstElementByTagName("html", this);
        if (findFirstElementByTagName == null) {
            findFirstElementByTagName = appendElement("html");
        }
        if (head() == null) {
            findFirstElementByTagName.prependElement(TtmlNode.TAG_HEAD);
        }
        if (body() == null) {
            findFirstElementByTagName.appendElement(TtmlNode.TAG_BODY);
        }
        normaliseTextNodes(head());
        normaliseTextNodes(findFirstElementByTagName);
        normaliseTextNodes(this);
        normaliseStructure(TtmlNode.TAG_HEAD, findFirstElementByTagName);
        normaliseStructure(TtmlNode.TAG_BODY, findFirstElementByTagName);
        ensureMetaCharsetElement();
        return this;
    }

    private void normaliseTextNodes(Element element) {
        List arrayList = new ArrayList();
        for (Node node : element.childNodes) {
            Node node2;
            if (node2 instanceof TextNode) {
                TextNode textNode = (TextNode) node2;
                if (!textNode.isBlank()) {
                    arrayList.add(textNode);
                }
            }
        }
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            node2 = (Node) arrayList.get(size);
            element.removeChild(node2);
            body().prependChild(new TextNode(StringUtils.SPACE, ""));
            body().prependChild(node2);
        }
    }

    private void normaliseStructure(String str, Element element) {
        str = getElementsByTag(str);
        Node first = str.first();
        int i = 1;
        if (str.size() > 1) {
            List<Node> arrayList = new ArrayList();
            while (i < str.size()) {
                Node node = (Node) str.get(i);
                for (Node add : node.childNodes) {
                    arrayList.add(add);
                }
                node.remove();
                i++;
            }
            for (Node appendChild : arrayList) {
                first.appendChild(appendChild);
            }
        }
        if (first.parent().equals(element) == null) {
            element.appendChild(first);
        }
    }

    private Element findFirstElementByTagName(String str, Node node) {
        if (node.nodeName().equals(str)) {
            return (Element) node;
        }
        for (Node findFirstElementByTagName : node.childNodes) {
            Element findFirstElementByTagName2 = findFirstElementByTagName(str, findFirstElementByTagName);
            if (findFirstElementByTagName2 != null) {
                return findFirstElementByTagName2;
            }
        }
        return null;
    }

    public String outerHtml() {
        return super.html();
    }

    public Element text(String str) {
        body().text(str);
        return this;
    }

    public void charset(Charset charset) {
        updateMetaCharsetElement(true);
        this.outputSettings.charset(charset);
        ensureMetaCharsetElement();
    }

    public Charset charset() {
        return this.outputSettings.charset();
    }

    public void updateMetaCharsetElement(boolean z) {
        this.updateMetaCharset = z;
    }

    public boolean updateMetaCharsetElement() {
        return this.updateMetaCharset;
    }

    public Document clone() {
        Document document = (Document) super.clone();
        document.outputSettings = this.outputSettings.clone();
        return document;
    }

    private void ensureMetaCharsetElement() {
        if (this.updateMetaCharset) {
            Syntax syntax = outputSettings().syntax();
            if (syntax == Syntax.html) {
                Element first = select("meta[charset]").first();
                if (first != null) {
                    first.attr("charset", charset().displayName());
                } else {
                    first = head();
                    if (first != null) {
                        first.appendElement("meta").attr("charset", charset().displayName());
                    }
                }
                select("meta[name=charset]").remove();
            } else if (syntax == Syntax.xml) {
                Node node = (Node) childNodes().get(0);
                if (node instanceof XmlDeclaration) {
                    XmlDeclaration xmlDeclaration = (XmlDeclaration) node;
                    if (xmlDeclaration.name().equals("xml")) {
                        xmlDeclaration.attr("encoding", charset().displayName());
                        if (xmlDeclaration.attr(ProviderConstants.API_COLNAME_FEATURE_VERSION) != null) {
                            xmlDeclaration.attr(ProviderConstants.API_COLNAME_FEATURE_VERSION, "1.0");
                            return;
                        }
                        return;
                    }
                    node = new XmlDeclaration("xml", this.baseUri, false);
                    node.attr(ProviderConstants.API_COLNAME_FEATURE_VERSION, "1.0");
                    node.attr("encoding", charset().displayName());
                    prependChild(node);
                    return;
                }
                node = new XmlDeclaration("xml", this.baseUri, false);
                node.attr(ProviderConstants.API_COLNAME_FEATURE_VERSION, "1.0");
                node.attr("encoding", charset().displayName());
                prependChild(node);
            }
        }
    }

    public OutputSettings outputSettings() {
        return this.outputSettings;
    }

    public Document outputSettings(OutputSettings outputSettings) {
        Validate.notNull(outputSettings);
        this.outputSettings = outputSettings;
        return this;
    }

    public QuirksMode quirksMode() {
        return this.quirksMode;
    }

    public Document quirksMode(QuirksMode quirksMode) {
        this.quirksMode = quirksMode;
        return this;
    }
}
