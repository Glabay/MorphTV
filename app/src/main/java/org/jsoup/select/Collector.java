package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class Collector {

    private static class Accumulator implements NodeVisitor {
        private final Elements elements;
        private final Evaluator eval;
        private final Element root;

        public void tail(Node node, int i) {
        }

        Accumulator(Element element, Elements elements, Evaluator evaluator) {
            this.root = element;
            this.elements = elements;
            this.eval = evaluator;
        }

        public void head(Node node, int i) {
            if ((node instanceof Element) != 0) {
                Element element = (Element) node;
                if (this.eval.matches(this.root, element) != 0) {
                    this.elements.add(element);
                }
            }
        }
    }

    private Collector() {
    }

    public static Elements collect(Evaluator evaluator, Element element) {
        Elements elements = new Elements();
        new NodeTraversor(new Accumulator(element, elements, evaluator)).traverse(element);
        return elements;
    }
}
