package org.jsoup.nodes;

import com.google.android.exoplayer2.text.ttml.TtmlNode;
import de.timroes.axmlrpc.XMLRPCClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.Collector;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.jsoup.select.Evaluator.AllElements;
import org.jsoup.select.Evaluator.Attribute;
import org.jsoup.select.Evaluator.AttributeStarting;
import org.jsoup.select.Evaluator.AttributeWithValue;
import org.jsoup.select.Evaluator.AttributeWithValueContaining;
import org.jsoup.select.Evaluator.AttributeWithValueEnding;
import org.jsoup.select.Evaluator.AttributeWithValueMatching;
import org.jsoup.select.Evaluator.AttributeWithValueNot;
import org.jsoup.select.Evaluator.AttributeWithValueStarting;
import org.jsoup.select.Evaluator.Class;
import org.jsoup.select.Evaluator.ContainsOwnText;
import org.jsoup.select.Evaluator.ContainsText;
import org.jsoup.select.Evaluator.Id;
import org.jsoup.select.Evaluator.IndexEquals;
import org.jsoup.select.Evaluator.IndexGreaterThan;
import org.jsoup.select.Evaluator.IndexLessThan;
import org.jsoup.select.Evaluator.Matches;
import org.jsoup.select.Evaluator.MatchesOwn;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.jsoup.select.Selector;

public class Element extends Node {
    private static final Pattern classSplit = Pattern.compile("\\s+");
    private Tag tag;

    public Element(Tag tag, String str, Attributes attributes) {
        super(str, attributes);
        Validate.notNull(tag);
        this.tag = tag;
    }

    public Element(Tag tag, String str) {
        this(tag, str, new Attributes());
    }

    public String nodeName() {
        return this.tag.getName();
    }

    public String tagName() {
        return this.tag.getName();
    }

    public Element tagName(String str) {
        Validate.notEmpty(str, "Tag name must not be empty.");
        this.tag = Tag.valueOf(str, ParseSettings.preserveCase);
        return this;
    }

    public Tag tag() {
        return this.tag;
    }

    public boolean isBlock() {
        return this.tag.isBlock();
    }

    public String id() {
        return this.attributes.getIgnoreCase(TtmlNode.ATTR_ID);
    }

    public Element attr(String str, String str2) {
        super.attr(str, str2);
        return this;
    }

    public Element attr(String str, boolean z) {
        this.attributes.put(str, z);
        return this;
    }

    public Map<String, String> dataset() {
        return this.attributes.dataset();
    }

    public final Element parent() {
        return (Element) this.parentNode;
    }

    public Elements parents() {
        Elements elements = new Elements();
        accumulateParents(this, elements);
        return elements;
    }

    private static void accumulateParents(Element element, Elements elements) {
        element = element.parent();
        if (element != null && !element.tagName().equals("#root")) {
            elements.add(element);
            accumulateParents(element, elements);
        }
    }

    public Element child(int i) {
        return (Element) children().get(i);
    }

    public Elements children() {
        List arrayList = new ArrayList(this.childNodes.size());
        for (Node node : this.childNodes) {
            if (node instanceof Element) {
                arrayList.add((Element) node);
            }
        }
        return new Elements(arrayList);
    }

    public List<TextNode> textNodes() {
        List arrayList = new ArrayList();
        for (Node node : this.childNodes) {
            if (node instanceof TextNode) {
                arrayList.add((TextNode) node);
            }
        }
        return Collections.unmodifiableList(arrayList);
    }

    public List<DataNode> dataNodes() {
        List arrayList = new ArrayList();
        for (Node node : this.childNodes) {
            if (node instanceof DataNode) {
                arrayList.add((DataNode) node);
            }
        }
        return Collections.unmodifiableList(arrayList);
    }

    public Elements select(String str) {
        return Selector.select(str, this);
    }

    public Element appendChild(Node node) {
        Validate.notNull(node);
        reparentChild(node);
        ensureChildNodes();
        this.childNodes.add(node);
        node.setSiblingIndex(this.childNodes.size() - 1);
        return this;
    }

    public Element prependChild(Node node) {
        Validate.notNull(node);
        addChildren(0, node);
        return this;
    }

    public Element insertChildren(int i, Collection<? extends Node> collection) {
        Validate.notNull(collection, "Children collection to be inserted must not be null.");
        int childNodeSize = childNodeSize();
        if (i < 0) {
            i += childNodeSize + 1;
        }
        boolean z = i >= 0 && i <= childNodeSize;
        Validate.isTrue(z, "Insert position out of bounds.");
        ArrayList arrayList = new ArrayList(collection);
        addChildren(i, (Node[]) arrayList.toArray(new Node[arrayList.size()]));
        return this;
    }

    public Element appendElement(String str) {
        Node element = new Element(Tag.valueOf(str), baseUri());
        appendChild(element);
        return element;
    }

    public Element prependElement(String str) {
        Node element = new Element(Tag.valueOf(str), baseUri());
        prependChild(element);
        return element;
    }

    public Element appendText(String str) {
        Validate.notNull(str);
        appendChild(new TextNode(str, baseUri()));
        return this;
    }

    public Element prependText(String str) {
        Validate.notNull(str);
        prependChild(new TextNode(str, baseUri()));
        return this;
    }

    public Element append(String str) {
        Validate.notNull(str);
        str = Parser.parseFragment(str, this, baseUri());
        addChildren((Node[]) str.toArray(new Node[str.size()]));
        return this;
    }

    public Element prepend(String str) {
        Validate.notNull(str);
        str = Parser.parseFragment(str, this, baseUri());
        addChildren(0, (Node[]) str.toArray(new Node[str.size()]));
        return this;
    }

    public Element before(String str) {
        return (Element) super.before(str);
    }

    public Element before(Node node) {
        return (Element) super.before(node);
    }

    public Element after(String str) {
        return (Element) super.after(str);
    }

    public Element after(Node node) {
        return (Element) super.after(node);
    }

    public Element empty() {
        this.childNodes.clear();
        return this;
    }

    public Element wrap(String str) {
        return (Element) super.wrap(str);
    }

    public String cssSelector() {
        if (id().length() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("#");
            stringBuilder.append(id());
            return stringBuilder.toString();
        }
        StringBuilder stringBuilder2 = new StringBuilder(tagName().replace(':', '|'));
        String join = StringUtil.join(classNames(), ".");
        if (join.length() > 0) {
            stringBuilder2.append('.');
            stringBuilder2.append(join);
        }
        if (parent() != null) {
            if (!(parent() instanceof Document)) {
                stringBuilder2.insert(0, " > ");
                if (parent().select(stringBuilder2.toString()).size() > 1) {
                    stringBuilder2.append(String.format(":nth-child(%d)", new Object[]{Integer.valueOf(elementSiblingIndex().intValue() + 1)}));
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append(parent().cssSelector());
                stringBuilder.append(stringBuilder2.toString());
                return stringBuilder.toString();
            }
        }
        return stringBuilder2.toString();
    }

    public Elements siblingElements() {
        if (this.parentNode == null) {
            return new Elements(0);
        }
        List<Element> children = parent().children();
        Elements elements = new Elements(children.size() - 1);
        for (Element element : children) {
            if (element != this) {
                elements.add(element);
            }
        }
        return elements;
    }

    public Element nextElementSibling() {
        if (this.parentNode == null) {
            return null;
        }
        List children = parent().children();
        Integer indexInList = indexInList(this, children);
        Validate.notNull(indexInList);
        if (children.size() > indexInList.intValue() + 1) {
            return (Element) children.get(indexInList.intValue() + 1);
        }
        return null;
    }

    public Element previousElementSibling() {
        if (this.parentNode == null) {
            return null;
        }
        List children = parent().children();
        Integer indexInList = indexInList(this, children);
        Validate.notNull(indexInList);
        if (indexInList.intValue() > 0) {
            return (Element) children.get(indexInList.intValue() - 1);
        }
        return null;
    }

    public Element firstElementSibling() {
        List children = parent().children();
        return children.size() > 1 ? (Element) children.get(0) : null;
    }

    public Integer elementSiblingIndex() {
        if (parent() == null) {
            return Integer.valueOf(0);
        }
        return indexInList(this, parent().children());
    }

    public Element lastElementSibling() {
        List children = parent().children();
        return children.size() > 1 ? (Element) children.get(children.size() - 1) : null;
    }

    private static <E extends Element> Integer indexInList(Element element, List<E> list) {
        Validate.notNull(element);
        Validate.notNull(list);
        for (int i = 0; i < list.size(); i++) {
            if (((Element) list.get(i)) == element) {
                return Integer.valueOf(i);
            }
        }
        return null;
    }

    public Elements getElementsByTag(String str) {
        Validate.notEmpty(str);
        return Collector.collect(new Evaluator.Tag(str.toLowerCase().trim()), this);
    }

    public Element getElementById(String str) {
        Validate.notEmpty(str);
        str = Collector.collect(new Id(str), this);
        return str.size() > 0 ? (Element) str.get(0) : null;
    }

    public Elements getElementsByClass(String str) {
        Validate.notEmpty(str);
        return Collector.collect(new Class(str), this);
    }

    public Elements getElementsByAttribute(String str) {
        Validate.notEmpty(str);
        return Collector.collect(new Attribute(str.trim()), this);
    }

    public Elements getElementsByAttributeStarting(String str) {
        Validate.notEmpty(str);
        return Collector.collect(new AttributeStarting(str.trim()), this);
    }

    public Elements getElementsByAttributeValue(String str, String str2) {
        return Collector.collect(new AttributeWithValue(str, str2), this);
    }

    public Elements getElementsByAttributeValueNot(String str, String str2) {
        return Collector.collect(new AttributeWithValueNot(str, str2), this);
    }

    public Elements getElementsByAttributeValueStarting(String str, String str2) {
        return Collector.collect(new AttributeWithValueStarting(str, str2), this);
    }

    public Elements getElementsByAttributeValueEnding(String str, String str2) {
        return Collector.collect(new AttributeWithValueEnding(str, str2), this);
    }

    public Elements getElementsByAttributeValueContaining(String str, String str2) {
        return Collector.collect(new AttributeWithValueContaining(str, str2), this);
    }

    public Elements getElementsByAttributeValueMatching(String str, Pattern pattern) {
        return Collector.collect(new AttributeWithValueMatching(str, pattern), this);
    }

    public Elements getElementsByAttributeValueMatching(String str, String str2) {
        try {
            return getElementsByAttributeValueMatching(str, Pattern.compile(str2));
        } catch (String str3) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Pattern syntax error: ");
            stringBuilder.append(str2);
            throw new IllegalArgumentException(stringBuilder.toString(), str3);
        }
    }

    public Elements getElementsByIndexLessThan(int i) {
        return Collector.collect(new IndexLessThan(i), this);
    }

    public Elements getElementsByIndexGreaterThan(int i) {
        return Collector.collect(new IndexGreaterThan(i), this);
    }

    public Elements getElementsByIndexEquals(int i) {
        return Collector.collect(new IndexEquals(i), this);
    }

    public Elements getElementsContainingText(String str) {
        return Collector.collect(new ContainsText(str), this);
    }

    public Elements getElementsContainingOwnText(String str) {
        return Collector.collect(new ContainsOwnText(str), this);
    }

    public Elements getElementsMatchingText(Pattern pattern) {
        return Collector.collect(new Matches(pattern), this);
    }

    public Elements getElementsMatchingText(String str) {
        try {
            return getElementsMatchingText(Pattern.compile(str));
        } catch (Throwable e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Pattern syntax error: ");
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString(), e);
        }
    }

    public Elements getElementsMatchingOwnText(Pattern pattern) {
        return Collector.collect(new MatchesOwn(pattern), this);
    }

    public Elements getElementsMatchingOwnText(String str) {
        try {
            return getElementsMatchingOwnText(Pattern.compile(str));
        } catch (Throwable e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Pattern syntax error: ");
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString(), e);
        }
    }

    public Elements getAllElements() {
        return Collector.collect(new AllElements(), this);
    }

    public String text() {
        final StringBuilder stringBuilder = new StringBuilder();
        new NodeTraversor(new NodeVisitor() {
            public void tail(Node node, int i) {
            }

            public void head(Node node, int i) {
                if ((node instanceof TextNode) != 0) {
                    Element.appendNormalisedText(stringBuilder, (TextNode) node);
                } else if ((node instanceof Element) != 0) {
                    Element element = (Element) node;
                    if (stringBuilder.length() <= 0) {
                        return;
                    }
                    if (!(element.isBlock() == 0 && element.tag.getName().equals(TtmlNode.TAG_BR) == null) && TextNode.lastCharIsWhitespace(stringBuilder) == null) {
                        stringBuilder.append(StringUtils.SPACE);
                    }
                }
            }
        }).traverse(this);
        return stringBuilder.toString().trim();
    }

    public String ownText() {
        StringBuilder stringBuilder = new StringBuilder();
        ownText(stringBuilder);
        return stringBuilder.toString().trim();
    }

    private void ownText(StringBuilder stringBuilder) {
        for (Node node : this.childNodes) {
            if (node instanceof TextNode) {
                appendNormalisedText(stringBuilder, (TextNode) node);
            } else if (node instanceof Element) {
                appendWhitespaceIfBr((Element) node, stringBuilder);
            }
        }
    }

    private static void appendNormalisedText(StringBuilder stringBuilder, TextNode textNode) {
        String wholeText = textNode.getWholeText();
        if (preserveWhitespace(textNode.parentNode) != null) {
            stringBuilder.append(wholeText);
        } else {
            StringUtil.appendNormalisedWhitespace(stringBuilder, wholeText, TextNode.lastCharIsWhitespace(stringBuilder));
        }
    }

    private static void appendWhitespaceIfBr(Element element, StringBuilder stringBuilder) {
        if (element.tag.getName().equals(TtmlNode.TAG_BR) != null && TextNode.lastCharIsWhitespace(stringBuilder) == null) {
            stringBuilder.append(StringUtils.SPACE);
        }
    }

    static boolean preserveWhitespace(Node node) {
        boolean z = false;
        if (node == null || !(node instanceof Element)) {
            return false;
        }
        Element element = (Element) node;
        if (element.tag.preserveWhitespace() || !(element.parent() == null || element.parent().tag.preserveWhitespace() == null)) {
            z = true;
        }
        return z;
    }

    public Element text(String str) {
        Validate.notNull(str);
        empty();
        appendChild(new TextNode(str, this.baseUri));
        return this;
    }

    public boolean hasText() {
        for (Node node : this.childNodes) {
            if (node instanceof TextNode) {
                if (!((TextNode) node).isBlank()) {
                    return true;
                }
            } else if ((node instanceof Element) && ((Element) node).hasText()) {
                return true;
            }
        }
        return false;
    }

    public String data() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Node node : this.childNodes) {
            if (node instanceof DataNode) {
                stringBuilder.append(((DataNode) node).getWholeData());
            } else if (node instanceof Element) {
                stringBuilder.append(((Element) node).data());
            }
        }
        return stringBuilder.toString();
    }

    public String className() {
        return attr("class").trim();
    }

    public Set<String> classNames() {
        Set<String> linkedHashSet = new LinkedHashSet(Arrays.asList(classSplit.split(className())));
        linkedHashSet.remove("");
        return linkedHashSet;
    }

    public Element classNames(Set<String> set) {
        Validate.notNull(set);
        this.attributes.put("class", StringUtil.join((Collection) set, StringUtils.SPACE));
        return this;
    }

    public boolean hasClass(String str) {
        String str2 = this.attributes.get("class");
        int length = str2.length();
        int length2 = str.length();
        if (length != 0) {
            if (length >= length2) {
                if (length == length2) {
                    return str.equalsIgnoreCase(str2);
                }
                Object obj = null;
                int i = 0;
                for (int i2 = 0; i2 < length; i2++) {
                    if (Character.isWhitespace(str2.charAt(i2))) {
                        if (obj == null) {
                            continue;
                        } else if (i2 - i == length2 && str2.regionMatches(true, i, str, 0, length2)) {
                            return true;
                        } else {
                            obj = null;
                        }
                    } else if (obj == null) {
                        i = i2;
                        obj = 1;
                    }
                }
                if (obj == null || length - i != length2) {
                    return false;
                }
                return str2.regionMatches(true, i, str, 0, length2);
            }
        }
        return false;
    }

    public Element addClass(String str) {
        Validate.notNull(str);
        Set classNames = classNames();
        classNames.add(str);
        classNames(classNames);
        return this;
    }

    public Element removeClass(String str) {
        Validate.notNull(str);
        Set classNames = classNames();
        classNames.remove(str);
        classNames(classNames);
        return this;
    }

    public Element toggleClass(String str) {
        Validate.notNull(str);
        Set classNames = classNames();
        if (classNames.contains(str)) {
            classNames.remove(str);
        } else {
            classNames.add(str);
        }
        classNames(classNames);
        return this;
    }

    public String val() {
        if (tagName().equals("textarea")) {
            return text();
        }
        return attr(XMLRPCClient.VALUE);
    }

    public Element val(String str) {
        if (tagName().equals("textarea")) {
            text(str);
        } else {
            attr(XMLRPCClient.VALUE, str);
        }
        return this;
    }

    void outerHtmlHead(Appendable appendable, int i, OutputSettings outputSettings) throws IOException {
        if (outputSettings.prettyPrint() && (this.tag.formatAsBlock() || ((parent() != null && parent().tag().formatAsBlock()) || outputSettings.outline()))) {
            if (!(appendable instanceof StringBuilder)) {
                indent(appendable, i, outputSettings);
            } else if (((StringBuilder) appendable).length() > 0) {
                indent(appendable, i, outputSettings);
            }
        }
        appendable.append("<").append(tagName());
        this.attributes.html(appendable, outputSettings);
        if (this.childNodes.isEmpty() == 0 || this.tag.isSelfClosing() == 0) {
            appendable.append(">");
        } else if (outputSettings.syntax() != Syntax.html || this.tag.isEmpty() == 0) {
            appendable.append(" />");
        } else {
            appendable.append(62);
        }
    }

    void outerHtmlTail(Appendable appendable, int i, OutputSettings outputSettings) throws IOException {
        if (!this.childNodes.isEmpty() || !this.tag.isSelfClosing()) {
            if (outputSettings.prettyPrint() && !this.childNodes.isEmpty() && (this.tag.formatAsBlock() || (outputSettings.outline() && (this.childNodes.size() > 1 || (this.childNodes.size() == 1 && !(this.childNodes.get(0) instanceof TextNode)))))) {
                indent(appendable, i, outputSettings);
            }
            appendable.append("</").append(tagName()).append(">");
        }
    }

    public String html() {
        StringBuilder stringBuilder = new StringBuilder();
        html(stringBuilder);
        return getOutputSettings().prettyPrint() ? stringBuilder.toString().trim() : stringBuilder.toString();
    }

    private void html(StringBuilder stringBuilder) {
        for (Node outerHtml : this.childNodes) {
            outerHtml.outerHtml(stringBuilder);
        }
    }

    public <T extends Appendable> T html(T t) {
        for (Node outerHtml : this.childNodes) {
            outerHtml.outerHtml(t);
        }
        return t;
    }

    public Element html(String str) {
        empty();
        append(str);
        return this;
    }

    public String toString() {
        return outerHtml();
    }

    public Element clone() {
        return (Element) super.clone();
    }
}
