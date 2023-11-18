package org.jsoup.helper;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class W3CDom {
    protected DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    protected static class W3CBuilder implements NodeVisitor {
        private static final String xmlnsKey = "xmlns";
        private static final String xmlnsPrefix = "xmlns:";
        private Element dest;
        private final Document doc;
        private final HashMap<String, String> namespaces = new HashMap();

        public W3CBuilder(Document document) {
            this.doc = document;
        }

        public void head(Node node, int i) {
            if ((node instanceof org.jsoup.nodes.Element) != 0) {
                org.jsoup.nodes.Element element = (org.jsoup.nodes.Element) node;
                i = this.doc.createElementNS((String) this.namespaces.get(updateNamespaces(element)), element.tagName());
                copyAttributes(element, i);
                if (this.dest == null) {
                    this.doc.appendChild(i);
                } else {
                    this.dest.appendChild(i);
                }
                this.dest = i;
            } else if ((node instanceof TextNode) != 0) {
                this.dest.appendChild(this.doc.createTextNode(((TextNode) node).getWholeText()));
            } else if ((node instanceof Comment) != 0) {
                this.dest.appendChild(this.doc.createComment(((Comment) node).getData()));
            } else if ((node instanceof DataNode) != 0) {
                this.dest.appendChild(this.doc.createTextNode(((DataNode) node).getWholeData()));
            }
        }

        public void tail(Node node, int i) {
            if ((node instanceof org.jsoup.nodes.Element) != null && (this.dest.getParentNode() instanceof Element) != null) {
                this.dest = (Element) this.dest.getParentNode();
            }
        }

        private void copyAttributes(Node node, Element element) {
            node = node.attributes().iterator();
            while (node.hasNext()) {
                Attribute attribute = (Attribute) node.next();
                String replaceAll = attribute.getKey().replaceAll("[^-a-zA-Z0-9_:.]", "");
                if (replaceAll.matches("[a-zA-Z_:]{1}[-a-zA-Z0-9_:.]*")) {
                    element.setAttribute(replaceAll, attribute.getValue());
                }
            }
        }

        private String updateNamespaces(org.jsoup.nodes.Element element) {
            Iterator it = element.attributes().iterator();
            while (it.hasNext()) {
                Object obj;
                Attribute attribute = (Attribute) it.next();
                String key = attribute.getKey();
                if (key.equals(xmlnsKey)) {
                    obj = "";
                } else if (key.startsWith(xmlnsPrefix)) {
                    obj = key.substring(xmlnsPrefix.length());
                }
                this.namespaces.put(obj, attribute.getValue());
            }
            int indexOf = element.tagName().indexOf(":");
            return indexOf > 0 ? element.tagName().substring(0, indexOf) : "";
        }
    }

    public Document fromJsoup(org.jsoup.nodes.Document document) {
        Validate.notNull(document);
        try {
            this.factory.setNamespaceAware(true);
            Document newDocument = this.factory.newDocumentBuilder().newDocument();
            convert(document, newDocument);
            return newDocument;
        } catch (org.jsoup.nodes.Document document2) {
            throw new IllegalStateException(document2);
        }
    }

    public void convert(org.jsoup.nodes.Document document, Document document2) {
        if (!StringUtil.isBlank(document.location())) {
            document2.setDocumentURI(document.location());
        }
        new NodeTraversor(new W3CBuilder(document2)).traverse(document.child(0));
    }

    public String asString(Document document) {
        try {
            Source dOMSource = new DOMSource(document);
            document = new StringWriter();
            TransformerFactory.newInstance().newTransformer().transform(dOMSource, new StreamResult(document));
            return document.toString();
        } catch (Document document2) {
            throw new IllegalStateException(document2);
        }
    }
}
