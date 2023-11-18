package org.jsoup.select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;

public class Elements extends ArrayList<Element> {
    public Elements(int i) {
        super(i);
    }

    public Elements(Collection<Element> collection) {
        super(collection);
    }

    public Elements(List<Element> list) {
        super(list);
    }

    public Elements(Element... elementArr) {
        super(Arrays.asList(elementArr));
    }

    public Elements clone() {
        Elements elements = new Elements(size());
        Iterator it = iterator();
        while (it.hasNext()) {
            elements.add(((Element) it.next()).clone());
        }
        return elements;
    }

    public String attr(String str) {
        Iterator it = iterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            if (element.hasAttr(str)) {
                return element.attr(str);
            }
        }
        return "";
    }

    public boolean hasAttr(String str) {
        Iterator it = iterator();
        while (it.hasNext()) {
            if (((Element) it.next()).hasAttr(str)) {
                return true;
            }
        }
        return null;
    }

    public Elements attr(String str, String str2) {
        Iterator it = iterator();
        while (it.hasNext()) {
            ((Element) it.next()).attr(str, str2);
        }
        return this;
    }

    public Elements removeAttr(String str) {
        Iterator it = iterator();
        while (it.hasNext()) {
            ((Element) it.next()).removeAttr(str);
        }
        return this;
    }

    public Elements addClass(String str) {
        Iterator it = iterator();
        while (it.hasNext()) {
            ((Element) it.next()).addClass(str);
        }
        return this;
    }

    public Elements removeClass(String str) {
        Iterator it = iterator();
        while (it.hasNext()) {
            ((Element) it.next()).removeClass(str);
        }
        return this;
    }

    public Elements toggleClass(String str) {
        Iterator it = iterator();
        while (it.hasNext()) {
            ((Element) it.next()).toggleClass(str);
        }
        return this;
    }

    public boolean hasClass(String str) {
        Iterator it = iterator();
        while (it.hasNext()) {
            if (((Element) it.next()).hasClass(str)) {
                return true;
            }
        }
        return null;
    }

    public String val() {
        return size() > 0 ? first().val() : "";
    }

    public Elements val(String str) {
        Iterator it = iterator();
        while (it.hasNext()) {
            ((Element) it.next()).val(str);
        }
        return this;
    }

    public String text() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator it = iterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            if (stringBuilder.length() != 0) {
                stringBuilder.append(StringUtils.SPACE);
            }
            stringBuilder.append(element.text());
        }
        return stringBuilder.toString();
    }

    public boolean hasText() {
        Iterator it = iterator();
        while (it.hasNext()) {
            if (((Element) it.next()).hasText()) {
                return true;
            }
        }
        return false;
    }

    public String html() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator it = iterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(element.html());
        }
        return stringBuilder.toString();
    }

    public String outerHtml() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator it = iterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(element.outerHtml());
        }
        return stringBuilder.toString();
    }

    public String toString() {
        return outerHtml();
    }

    public Elements tagName(String str) {
        Iterator it = iterator();
        while (it.hasNext()) {
            ((Element) it.next()).tagName(str);
        }
        return this;
    }

    public Elements html(String str) {
        Iterator it = iterator();
        while (it.hasNext()) {
            ((Element) it.next()).html(str);
        }
        return this;
    }

    public Elements prepend(String str) {
        Iterator it = iterator();
        while (it.hasNext()) {
            ((Element) it.next()).prepend(str);
        }
        return this;
    }

    public Elements append(String str) {
        Iterator it = iterator();
        while (it.hasNext()) {
            ((Element) it.next()).append(str);
        }
        return this;
    }

    public Elements before(String str) {
        Iterator it = iterator();
        while (it.hasNext()) {
            ((Element) it.next()).before(str);
        }
        return this;
    }

    public Elements after(String str) {
        Iterator it = iterator();
        while (it.hasNext()) {
            ((Element) it.next()).after(str);
        }
        return this;
    }

    public Elements wrap(String str) {
        Validate.notEmpty(str);
        Iterator it = iterator();
        while (it.hasNext()) {
            ((Element) it.next()).wrap(str);
        }
        return this;
    }

    public Elements unwrap() {
        Iterator it = iterator();
        while (it.hasNext()) {
            ((Element) it.next()).unwrap();
        }
        return this;
    }

    public Elements empty() {
        Iterator it = iterator();
        while (it.hasNext()) {
            ((Element) it.next()).empty();
        }
        return this;
    }

    public Elements remove() {
        Iterator it = iterator();
        while (it.hasNext()) {
            ((Element) it.next()).remove();
        }
        return this;
    }

    public Elements select(String str) {
        return Selector.select(str, (Iterable) this);
    }

    public Elements not(String str) {
        return Selector.filterOut(this, Selector.select(str, (Iterable) this));
    }

    public Elements eq(int i) {
        if (size() <= i) {
            return new Elements();
        }
        return new Elements((Element) get(i));
    }

    public boolean is(String str) {
        return select(str).isEmpty() ^ 1;
    }

    public Elements parents() {
        Collection linkedHashSet = new LinkedHashSet();
        Iterator it = iterator();
        while (it.hasNext()) {
            linkedHashSet.addAll(((Element) it.next()).parents());
        }
        return new Elements(linkedHashSet);
    }

    public Element first() {
        return isEmpty() ? null : (Element) get(0);
    }

    public Element last() {
        return isEmpty() ? null : (Element) get(size() - 1);
    }

    public Elements traverse(NodeVisitor nodeVisitor) {
        Validate.notNull(nodeVisitor);
        NodeTraversor nodeTraversor = new NodeTraversor(nodeVisitor);
        nodeVisitor = iterator();
        while (nodeVisitor.hasNext()) {
            nodeTraversor.traverse((Element) nodeVisitor.next());
        }
        return this;
    }

    public List<FormElement> forms() {
        List arrayList = new ArrayList();
        Iterator it = iterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            if (element instanceof FormElement) {
                arrayList.add((FormElement) element);
            }
        }
        return arrayList;
    }
}
