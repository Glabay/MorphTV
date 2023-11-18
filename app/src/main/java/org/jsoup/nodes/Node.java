package org.jsoup.nodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.SerializationException;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.parser.Parser;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public abstract class Node implements Cloneable {
    private static final List<Node> EMPTY_NODES = Collections.emptyList();
    Attributes attributes;
    String baseUri;
    List<Node> childNodes;
    Node parentNode;
    int siblingIndex;

    private static class OuterHtmlVisitor implements NodeVisitor {
        private Appendable accum;
        private OutputSettings out;

        OuterHtmlVisitor(Appendable appendable, OutputSettings outputSettings) {
            this.accum = appendable;
            this.out = outputSettings;
        }

        public void head(Node node, int i) {
            try {
                node.outerHtmlHead(this.accum, i, this.out);
            } catch (Throwable e) {
                throw new SerializationException(e);
            }
        }

        public void tail(Node node, int i) {
            if (!node.nodeName().equals("#text")) {
                try {
                    node.outerHtmlTail(this.accum, i, this.out);
                } catch (Throwable e) {
                    throw new SerializationException(e);
                }
            }
        }
    }

    public boolean equals(Object obj) {
        return this == obj;
    }

    public abstract String nodeName();

    abstract void outerHtmlHead(Appendable appendable, int i, OutputSettings outputSettings) throws IOException;

    abstract void outerHtmlTail(Appendable appendable, int i, OutputSettings outputSettings) throws IOException;

    protected Node(String str, Attributes attributes) {
        Validate.notNull(str);
        Validate.notNull(attributes);
        this.childNodes = EMPTY_NODES;
        this.baseUri = str.trim();
        this.attributes = attributes;
    }

    protected Node(String str) {
        this(str, new Attributes());
    }

    protected Node() {
        this.childNodes = EMPTY_NODES;
        this.attributes = null;
    }

    public String attr(String str) {
        Validate.notNull(str);
        String ignoreCase = this.attributes.getIgnoreCase(str);
        if (ignoreCase.length() > 0) {
            return ignoreCase;
        }
        return str.toLowerCase().startsWith("abs:") ? absUrl(str.substring("abs:".length())) : "";
    }

    public Attributes attributes() {
        return this.attributes;
    }

    public Node attr(String str, String str2) {
        this.attributes.put(str, str2);
        return this;
    }

    public boolean hasAttr(String str) {
        Validate.notNull(str);
        if (str.startsWith("abs:")) {
            String substring = str.substring("abs:".length());
            if (this.attributes.hasKeyIgnoreCase(substring) && !absUrl(substring).equals("")) {
                return true;
            }
        }
        return this.attributes.hasKeyIgnoreCase(str);
    }

    public Node removeAttr(String str) {
        Validate.notNull(str);
        this.attributes.removeIgnoreCase(str);
        return this;
    }

    public String baseUri() {
        return this.baseUri;
    }

    public void setBaseUri(final String str) {
        Validate.notNull(str);
        traverse(new NodeVisitor() {
            public void tail(Node node, int i) {
            }

            public void head(Node node, int i) {
                node.baseUri = str;
            }
        });
    }

    public String absUrl(String str) {
        Validate.notEmpty(str);
        if (hasAttr(str)) {
            return StringUtil.resolve(this.baseUri, attr(str));
        }
        return "";
    }

    public Node childNode(int i) {
        return (Node) this.childNodes.get(i);
    }

    public List<Node> childNodes() {
        return Collections.unmodifiableList(this.childNodes);
    }

    public List<Node> childNodesCopy() {
        List<Node> arrayList = new ArrayList(this.childNodes.size());
        for (Node clone : this.childNodes) {
            arrayList.add(clone.clone());
        }
        return arrayList;
    }

    public final int childNodeSize() {
        return this.childNodes.size();
    }

    protected Node[] childNodesAsArray() {
        return (Node[]) this.childNodes.toArray(new Node[childNodeSize()]);
    }

    public Node parent() {
        return this.parentNode;
    }

    public final Node parentNode() {
        return this.parentNode;
    }

    public Document ownerDocument() {
        if (this instanceof Document) {
            return (Document) this;
        }
        if (this.parentNode == null) {
            return null;
        }
        return this.parentNode.ownerDocument();
    }

    public void remove() {
        Validate.notNull(this.parentNode);
        this.parentNode.removeChild(this);
    }

    public Node before(String str) {
        addSiblingHtml(this.siblingIndex, str);
        return this;
    }

    public Node before(Node node) {
        Validate.notNull(node);
        Validate.notNull(this.parentNode);
        this.parentNode.addChildren(this.siblingIndex, node);
        return this;
    }

    public Node after(String str) {
        addSiblingHtml(this.siblingIndex + 1, str);
        return this;
    }

    public Node after(Node node) {
        Validate.notNull(node);
        Validate.notNull(this.parentNode);
        this.parentNode.addChildren(this.siblingIndex + 1, node);
        return this;
    }

    private void addSiblingHtml(int i, String str) {
        Validate.notNull(str);
        Validate.notNull(this.parentNode);
        str = Parser.parseFragment(str, parent() instanceof Element ? (Element) parent() : null, baseUri());
        this.parentNode.addChildren(i, (Node[]) str.toArray(new Node[str.size()]));
    }

    public Node wrap(String str) {
        Validate.notEmpty(str);
        str = Parser.parseFragment(str, parent() instanceof Element ? (Element) parent() : null, baseUri());
        int i = 0;
        Node node = (Node) str.get(0);
        if (node != null) {
            if (node instanceof Element) {
                Element element = (Element) node;
                Element deepChild = getDeepChild(element);
                this.parentNode.replaceChild(this, element);
                deepChild.addChildren(this);
                if (str.size() > 0) {
                    while (i < str.size()) {
                        Node node2 = (Node) str.get(i);
                        node2.parentNode.removeChild(node2);
                        element.appendChild(node2);
                        i++;
                    }
                }
                return this;
            }
        }
        return null;
    }

    public Node unwrap() {
        Validate.notNull(this.parentNode);
        Node node = this.childNodes.size() > 0 ? (Node) this.childNodes.get(0) : null;
        this.parentNode.addChildren(this.siblingIndex, childNodesAsArray());
        remove();
        return node;
    }

    private Element getDeepChild(Element element) {
        List children = element.children();
        return children.size() > 0 ? getDeepChild((Element) children.get(null)) : element;
    }

    public void replaceWith(Node node) {
        Validate.notNull(node);
        Validate.notNull(this.parentNode);
        this.parentNode.replaceChild(this, node);
    }

    protected void setParentNode(Node node) {
        if (this.parentNode != null) {
            this.parentNode.removeChild(this);
        }
        this.parentNode = node;
    }

    protected void replaceChild(Node node, Node node2) {
        Validate.isTrue(node.parentNode == this);
        Validate.notNull(node2);
        if (node2.parentNode != null) {
            node2.parentNode.removeChild(node2);
        }
        int i = node.siblingIndex;
        this.childNodes.set(i, node2);
        node2.parentNode = this;
        node2.setSiblingIndex(i);
        node.parentNode = null;
    }

    protected void removeChild(Node node) {
        Validate.isTrue(node.parentNode == this);
        int i = node.siblingIndex;
        this.childNodes.remove(i);
        reindexChildren(i);
        node.parentNode = null;
    }

    protected void addChildren(Node... nodeArr) {
        for (Node node : nodeArr) {
            reparentChild(node);
            ensureChildNodes();
            this.childNodes.add(node);
            node.setSiblingIndex(this.childNodes.size() - 1);
        }
    }

    protected void addChildren(int i, Node... nodeArr) {
        Validate.noNullElements(nodeArr);
        ensureChildNodes();
        for (int length = nodeArr.length - 1; length >= 0; length--) {
            Node node = nodeArr[length];
            reparentChild(node);
            this.childNodes.add(i, node);
            reindexChildren(i);
        }
    }

    protected void ensureChildNodes() {
        if (this.childNodes == EMPTY_NODES) {
            this.childNodes = new ArrayList(4);
        }
    }

    protected void reparentChild(Node node) {
        if (node.parentNode != null) {
            node.parentNode.removeChild(node);
        }
        node.setParentNode(this);
    }

    private void reindexChildren(int i) {
        while (i < this.childNodes.size()) {
            ((Node) this.childNodes.get(i)).setSiblingIndex(i);
            i++;
        }
    }

    public List<Node> siblingNodes() {
        if (this.parentNode == null) {
            return Collections.emptyList();
        }
        List<Node> list = this.parentNode.childNodes;
        List<Node> arrayList = new ArrayList(list.size() - 1);
        for (Node node : list) {
            if (node != this) {
                arrayList.add(node);
            }
        }
        return arrayList;
    }

    public Node nextSibling() {
        if (this.parentNode == null) {
            return null;
        }
        List list = this.parentNode.childNodes;
        int i = this.siblingIndex + 1;
        if (list.size() > i) {
            return (Node) list.get(i);
        }
        return null;
    }

    public Node previousSibling() {
        if (this.parentNode != null && this.siblingIndex > 0) {
            return (Node) this.parentNode.childNodes.get(this.siblingIndex - 1);
        }
        return null;
    }

    public int siblingIndex() {
        return this.siblingIndex;
    }

    protected void setSiblingIndex(int i) {
        this.siblingIndex = i;
    }

    public Node traverse(NodeVisitor nodeVisitor) {
        Validate.notNull(nodeVisitor);
        new NodeTraversor(nodeVisitor).traverse(this);
        return this;
    }

    public String outerHtml() {
        Appendable stringBuilder = new StringBuilder(128);
        outerHtml(stringBuilder);
        return stringBuilder.toString();
    }

    protected void outerHtml(Appendable appendable) {
        new NodeTraversor(new OuterHtmlVisitor(appendable, getOutputSettings())).traverse(this);
    }

    OutputSettings getOutputSettings() {
        return (ownerDocument() != null ? ownerDocument() : new Document("")).outputSettings();
    }

    public <T extends Appendable> T html(T t) {
        outerHtml(t);
        return t;
    }

    public String toString() {
        return outerHtml();
    }

    protected void indent(Appendable appendable, int i, OutputSettings outputSettings) throws IOException {
        appendable.append("\n").append(StringUtil.padding(i * outputSettings.indentAmount()));
    }

    public boolean hasSameValue(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null) {
            if (getClass() == obj.getClass()) {
                return outerHtml().equals(((Node) obj).outerHtml());
            }
        }
        return null;
    }

    public Node clone() {
        Node doClone = doClone(null);
        LinkedList linkedList = new LinkedList();
        linkedList.add(doClone);
        while (!linkedList.isEmpty()) {
            Node node = (Node) linkedList.remove();
            for (int i = 0; i < node.childNodes.size(); i++) {
                Node doClone2 = ((Node) node.childNodes.get(i)).doClone(node);
                node.childNodes.set(i, doClone2);
                linkedList.add(doClone2);
            }
        }
        return doClone;
    }

    protected Node doClone(Node node) {
        try {
            Node node2 = (Node) super.clone();
            node2.parentNode = node;
            if (node == null) {
                node = null;
            } else {
                node = this.siblingIndex;
            }
            node2.siblingIndex = node;
            node2.attributes = this.attributes != null ? this.attributes.clone() : null;
            node2.baseUri = this.baseUri;
            node2.childNodes = new ArrayList(this.childNodes.size());
            for (Node add : this.childNodes) {
                node2.childNodes.add(add);
            }
            return node2;
        } catch (Node node3) {
            throw new RuntimeException(node3);
        }
    }
}
