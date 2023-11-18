package org.jsoup.select;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;

public class Selector {
    private final Evaluator evaluator;
    private final Element root;

    public static class SelectorParseException extends IllegalStateException {
        public SelectorParseException(String str, Object... objArr) {
            super(String.format(str, objArr));
        }
    }

    private Selector(String str, Element element) {
        Validate.notNull(str);
        str = str.trim();
        Validate.notEmpty(str);
        Validate.notNull(element);
        this.evaluator = QueryParser.parse(str);
        this.root = element;
    }

    private Selector(Evaluator evaluator, Element element) {
        Validate.notNull(evaluator);
        Validate.notNull(element);
        this.evaluator = evaluator;
        this.root = element;
    }

    public static Elements select(String str, Element element) {
        return new Selector(str, element).select();
    }

    public static Elements select(Evaluator evaluator, Element element) {
        return new Selector(evaluator, element).select();
    }

    public static Elements select(String str, Iterable<Element> iterable) {
        Validate.notEmpty(str);
        Validate.notNull(iterable);
        Evaluator parse = QueryParser.parse(str);
        List arrayList = new ArrayList();
        IdentityHashMap identityHashMap = new IdentityHashMap();
        for (Element select : iterable) {
            Iterator it = select(parse, select).iterator();
            while (it.hasNext()) {
                Element element = (Element) it.next();
                if (!identityHashMap.containsKey(element)) {
                    arrayList.add(element);
                    identityHashMap.put(element, Boolean.TRUE);
                }
            }
        }
        return new Elements(arrayList);
    }

    private Elements select() {
        return Collector.collect(this.evaluator, this.root);
    }

    static Elements filterOut(Collection<Element> collection, Collection<Element> collection2) {
        Elements elements = new Elements();
        for (Element element : collection) {
            Object obj = null;
            for (Element equals : collection2) {
                if (element.equals(equals)) {
                    obj = 1;
                    break;
                }
            }
            if (obj == null) {
                elements.add(element);
            }
        }
        return elements;
    }
}
