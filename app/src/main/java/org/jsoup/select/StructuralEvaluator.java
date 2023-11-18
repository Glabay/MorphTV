package org.jsoup.select;

import java.util.Iterator;
import org.jsoup.nodes.Element;

abstract class StructuralEvaluator extends Evaluator {
    Evaluator evaluator;

    static class Has extends StructuralEvaluator {
        public Has(Evaluator evaluator) {
            this.evaluator = evaluator;
        }

        public boolean matches(Element element, Element element2) {
            Iterator it = element2.getAllElements().iterator();
            while (it.hasNext()) {
                Element element3 = (Element) it.next();
                if (element3 != element2 && this.evaluator.matches(element, element3)) {
                    return true;
                }
            }
            return null;
        }

        public String toString() {
            return String.format(":has(%s)", new Object[]{this.evaluator});
        }
    }

    static class ImmediateParent extends StructuralEvaluator {
        public ImmediateParent(Evaluator evaluator) {
            this.evaluator = evaluator;
        }

        public boolean matches(Element element, Element element2) {
            boolean z = false;
            if (element == element2) {
                return false;
            }
            element2 = element2.parent();
            if (!(element2 == null || this.evaluator.matches(element, element2) == null)) {
                z = true;
            }
            return z;
        }

        public String toString() {
            return String.format(":ImmediateParent%s", new Object[]{this.evaluator});
        }
    }

    static class ImmediatePreviousSibling extends StructuralEvaluator {
        public ImmediatePreviousSibling(Evaluator evaluator) {
            this.evaluator = evaluator;
        }

        public boolean matches(Element element, Element element2) {
            boolean z = false;
            if (element == element2) {
                return false;
            }
            element2 = element2.previousElementSibling();
            if (!(element2 == null || this.evaluator.matches(element, element2) == null)) {
                z = true;
            }
            return z;
        }

        public String toString() {
            return String.format(":prev%s", new Object[]{this.evaluator});
        }
    }

    static class Not extends StructuralEvaluator {
        public Not(Evaluator evaluator) {
            this.evaluator = evaluator;
        }

        public boolean matches(Element element, Element element2) {
            return this.evaluator.matches(element, element2) ^ 1;
        }

        public String toString() {
            return String.format(":not%s", new Object[]{this.evaluator});
        }
    }

    static class Parent extends StructuralEvaluator {
        public Parent(Evaluator evaluator) {
            this.evaluator = evaluator;
        }

        public boolean matches(Element element, Element element2) {
            if (element == element2) {
                return false;
            }
            for (element2 = element2.parent(); !this.evaluator.matches(element, element2); element2 = element2.parent()) {
                if (element2 == element) {
                    return false;
                }
            }
            return true;
        }

        public String toString() {
            return String.format(":parent%s", new Object[]{this.evaluator});
        }
    }

    static class PreviousSibling extends StructuralEvaluator {
        public PreviousSibling(Evaluator evaluator) {
            this.evaluator = evaluator;
        }

        public boolean matches(Element element, Element element2) {
            if (element == element2) {
                return false;
            }
            for (element2 = element2.previousElementSibling(); element2 != null; element2 = element2.previousElementSibling()) {
                if (this.evaluator.matches(element, element2)) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return String.format(":prev*%s", new Object[]{this.evaluator});
        }
    }

    static class Root extends Evaluator {
        public boolean matches(Element element, Element element2) {
            return element == element2;
        }

        Root() {
        }
    }

    StructuralEvaluator() {
    }
}
