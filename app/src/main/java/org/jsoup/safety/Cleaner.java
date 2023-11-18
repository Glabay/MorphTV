package org.jsoup.safety;

import java.util.Iterator;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public class Cleaner {
    private Whitelist whitelist;

    private final class CleaningVisitor implements NodeVisitor {
        private Element destination;
        private int numDiscarded;
        private final Element root;

        private CleaningVisitor(Element element, Element element2) {
            this.numDiscarded = null;
            this.root = element;
            this.destination = element2;
        }

        public void head(Node node, int i) {
            if ((node instanceof Element) != 0) {
                Element element = (Element) node;
                if (Cleaner.this.whitelist.isSafeTag(element.tagName())) {
                    node = Cleaner.this.createSafeElement(element);
                    i = node.el;
                    this.destination.appendChild(i);
                    this.numDiscarded += node.numAttribsDiscarded;
                    this.destination = i;
                } else if (node != this.root) {
                    this.numDiscarded++;
                }
            } else if ((node instanceof TextNode) != 0) {
                this.destination.appendChild(new TextNode(((TextNode) node).getWholeText(), node.baseUri()));
            } else if ((node instanceof DataNode) == 0 || Cleaner.this.whitelist.isSafeTag(node.parent().nodeName()) == 0) {
                this.numDiscarded++;
            } else {
                this.destination.appendChild(new DataNode(((DataNode) node).getWholeData(), node.baseUri()));
            }
        }

        public void tail(Node node, int i) {
            if ((node instanceof Element) != 0 && Cleaner.this.whitelist.isSafeTag(node.nodeName()) != null) {
                this.destination = this.destination.parent();
            }
        }
    }

    private static class ElementMeta {
        Element el;
        int numAttribsDiscarded;

        ElementMeta(Element element, int i) {
            this.el = element;
            this.numAttribsDiscarded = i;
        }
    }

    public Cleaner(Whitelist whitelist) {
        Validate.notNull(whitelist);
        this.whitelist = whitelist;
    }

    public Document clean(Document document) {
        Validate.notNull(document);
        Document createShell = Document.createShell(document.baseUri());
        if (document.body() != null) {
            copySafeNodes(document.body(), createShell.body());
        }
        return createShell;
    }

    public boolean isValid(Document document) {
        Validate.notNull(document);
        return copySafeNodes(document.body(), Document.createShell(document.baseUri()).body()) == null ? true : null;
    }

    private int copySafeNodes(Element element, Element element2) {
        Object cleaningVisitor = new CleaningVisitor(element, element2);
        new NodeTraversor(cleaningVisitor).traverse(element);
        return cleaningVisitor.numDiscarded;
    }

    private ElementMeta createSafeElement(Element element) {
        String tagName = element.tagName();
        Attributes attributes = new Attributes();
        Element element2 = new Element(Tag.valueOf(tagName), element.baseUri(), attributes);
        Iterator it = element.attributes().iterator();
        int i = 0;
        while (it.hasNext()) {
            Attribute attribute = (Attribute) it.next();
            if (this.whitelist.isSafeAttribute(tagName, element, attribute)) {
                attributes.put(attribute);
            } else {
                i++;
            }
        }
        attributes.addAll(this.whitelist.getEnforcedAttributes(tagName));
        return new ElementMeta(element2, i);
    }
}
