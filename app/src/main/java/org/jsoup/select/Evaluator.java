package org.jsoup.select;

import java.util.regex.Pattern;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;

public abstract class Evaluator {

    public static final class AllElements extends Evaluator {
        public boolean matches(Element element, Element element2) {
            return true;
        }

        public String toString() {
            return "*";
        }
    }

    public static final class Attribute extends Evaluator {
        private String key;

        public Attribute(String str) {
            this.key = str;
        }

        public boolean matches(Element element, Element element2) {
            return element2.hasAttr(this.key);
        }

        public String toString() {
            return String.format("[%s]", new Object[]{this.key});
        }
    }

    public static abstract class AttributeKeyPair extends Evaluator {
        String key;
        String value;

        public AttributeKeyPair(String str, String str2) {
            Validate.notEmpty(str);
            Validate.notEmpty(str2);
            this.key = str.trim().toLowerCase();
            if (!((str2.startsWith("\"") == null || str2.endsWith("\"") == null) && (str2.startsWith("'") == null || str2.endsWith("'") == null))) {
                str2 = str2.substring(1, str2.length() - 1);
            }
            this.value = str2.trim().toLowerCase();
        }
    }

    public static final class AttributeStarting extends Evaluator {
        private String keyPrefix;

        public AttributeStarting(String str) {
            Validate.notEmpty(str);
            this.keyPrefix = str.toLowerCase();
        }

        public boolean matches(Element element, Element element2) {
            for (org.jsoup.nodes.Attribute key : element2.attributes().asList()) {
                if (key.getKey().toLowerCase().startsWith(this.keyPrefix) != null) {
                    return true;
                }
            }
            return null;
        }

        public String toString() {
            return String.format("[^%s]", new Object[]{this.keyPrefix});
        }
    }

    public static final class AttributeWithValue extends AttributeKeyPair {
        public AttributeWithValue(String str, String str2) {
            super(str, str2);
        }

        public boolean matches(Element element, Element element2) {
            return (element2.hasAttr(this.key) == null || this.value.equalsIgnoreCase(element2.attr(this.key).trim()) == null) ? null : true;
        }

        public String toString() {
            return String.format("[%s=%s]", new Object[]{this.key, this.value});
        }
    }

    public static final class AttributeWithValueContaining extends AttributeKeyPair {
        public AttributeWithValueContaining(String str, String str2) {
            super(str, str2);
        }

        public boolean matches(Element element, Element element2) {
            return (element2.hasAttr(this.key) == null || element2.attr(this.key).toLowerCase().contains(this.value) == null) ? null : true;
        }

        public String toString() {
            return String.format("[%s*=%s]", new Object[]{this.key, this.value});
        }
    }

    public static final class AttributeWithValueEnding extends AttributeKeyPair {
        public AttributeWithValueEnding(String str, String str2) {
            super(str, str2);
        }

        public boolean matches(Element element, Element element2) {
            return (element2.hasAttr(this.key) == null || element2.attr(this.key).toLowerCase().endsWith(this.value) == null) ? null : true;
        }

        public String toString() {
            return String.format("[%s$=%s]", new Object[]{this.key, this.value});
        }
    }

    public static final class AttributeWithValueMatching extends Evaluator {
        String key;
        Pattern pattern;

        public AttributeWithValueMatching(String str, Pattern pattern) {
            this.key = str.trim().toLowerCase();
            this.pattern = pattern;
        }

        public boolean matches(Element element, Element element2) {
            return (element2.hasAttr(this.key) == null || this.pattern.matcher(element2.attr(this.key)).find() == null) ? null : true;
        }

        public String toString() {
            return String.format("[%s~=%s]", new Object[]{this.key, this.pattern.toString()});
        }
    }

    public static final class AttributeWithValueNot extends AttributeKeyPair {
        public AttributeWithValueNot(String str, String str2) {
            super(str, str2);
        }

        public boolean matches(Element element, Element element2) {
            return this.value.equalsIgnoreCase(element2.attr(this.key)) ^ 1;
        }

        public String toString() {
            return String.format("[%s!=%s]", new Object[]{this.key, this.value});
        }
    }

    public static final class AttributeWithValueStarting extends AttributeKeyPair {
        public AttributeWithValueStarting(String str, String str2) {
            super(str, str2);
        }

        public boolean matches(Element element, Element element2) {
            return (element2.hasAttr(this.key) == null || element2.attr(this.key).toLowerCase().startsWith(this.value) == null) ? null : true;
        }

        public String toString() {
            return String.format("[%s^=%s]", new Object[]{this.key, this.value});
        }
    }

    public static final class Class extends Evaluator {
        private String className;

        public Class(String str) {
            this.className = str;
        }

        public boolean matches(Element element, Element element2) {
            return element2.hasClass(this.className);
        }

        public String toString() {
            return String.format(".%s", new Object[]{this.className});
        }
    }

    public static final class ContainsOwnText extends Evaluator {
        private String searchText;

        public ContainsOwnText(String str) {
            this.searchText = str.toLowerCase();
        }

        public boolean matches(Element element, Element element2) {
            return element2.ownText().toLowerCase().contains(this.searchText);
        }

        public String toString() {
            return String.format(":containsOwn(%s", new Object[]{this.searchText});
        }
    }

    public static final class ContainsText extends Evaluator {
        private String searchText;

        public ContainsText(String str) {
            this.searchText = str.toLowerCase();
        }

        public boolean matches(Element element, Element element2) {
            return element2.text().toLowerCase().contains(this.searchText);
        }

        public String toString() {
            return String.format(":contains(%s", new Object[]{this.searchText});
        }
    }

    public static abstract class CssNthEvaluator extends Evaluator {
        /* renamed from: a */
        protected final int f74a;
        /* renamed from: b */
        protected final int f75b;

        protected abstract int calculatePosition(Element element, Element element2);

        protected abstract String getPseudoClass();

        public CssNthEvaluator(int i, int i2) {
            this.f74a = i;
            this.f75b = i2;
        }

        public CssNthEvaluator(int i) {
            this(0, i);
        }

        public boolean matches(Element element, Element element2) {
            Element parent = element2.parent();
            if (parent != null) {
                if (!(parent instanceof Document)) {
                    element = calculatePosition(element, element2);
                    boolean z = true;
                    if (this.f74a == null) {
                        if (element != this.f75b) {
                            z = false;
                        }
                        return z;
                    }
                    if ((element - this.f75b) * this.f74a < null || (element - this.f75b) % this.f74a != null) {
                        z = false;
                    }
                    return z;
                }
            }
            return false;
        }

        public String toString() {
            if (this.f74a == 0) {
                return String.format(":%s(%d)", new Object[]{getPseudoClass(), Integer.valueOf(this.f75b)});
            } else if (this.f75b == 0) {
                return String.format(":%s(%dn)", new Object[]{getPseudoClass(), Integer.valueOf(this.f74a)});
            } else {
                return String.format(":%s(%dn%+d)", new Object[]{getPseudoClass(), Integer.valueOf(this.f74a), Integer.valueOf(this.f75b)});
            }
        }
    }

    public static final class Id extends Evaluator {
        private String id;

        public Id(String str) {
            this.id = str;
        }

        public boolean matches(Element element, Element element2) {
            return this.id.equals(element2.id());
        }

        public String toString() {
            return String.format("#%s", new Object[]{this.id});
        }
    }

    public static abstract class IndexEvaluator extends Evaluator {
        int index;

        public IndexEvaluator(int i) {
            this.index = i;
        }
    }

    public static final class IndexEquals extends IndexEvaluator {
        public IndexEquals(int i) {
            super(i);
        }

        public boolean matches(Element element, Element element2) {
            return element2.elementSiblingIndex().intValue() == this.index ? true : null;
        }

        public String toString() {
            return String.format(":eq(%d)", new Object[]{Integer.valueOf(this.index)});
        }
    }

    public static final class IndexGreaterThan extends IndexEvaluator {
        public IndexGreaterThan(int i) {
            super(i);
        }

        public boolean matches(Element element, Element element2) {
            return element2.elementSiblingIndex().intValue() > this.index ? true : null;
        }

        public String toString() {
            return String.format(":gt(%d)", new Object[]{Integer.valueOf(this.index)});
        }
    }

    public static final class IndexLessThan extends IndexEvaluator {
        public IndexLessThan(int i) {
            super(i);
        }

        public boolean matches(Element element, Element element2) {
            return element2.elementSiblingIndex().intValue() < this.index ? true : null;
        }

        public String toString() {
            return String.format(":lt(%d)", new Object[]{Integer.valueOf(this.index)});
        }
    }

    public static final class IsEmpty extends Evaluator {
        public String toString() {
            return ":empty";
        }

        public boolean matches(Element element, Element element2) {
            for (Node node : element2.childNodes()) {
                if (!(node instanceof Comment) && !(node instanceof XmlDeclaration) && (node instanceof DocumentType) == null) {
                    return null;
                }
            }
            return true;
        }
    }

    public static final class IsFirstChild extends Evaluator {
        public String toString() {
            return ":first-child";
        }

        public boolean matches(Element element, Element element2) {
            element = element2.parent();
            return (element != null && (element instanceof Document) == null && element2.elementSiblingIndex().intValue() == null) ? true : null;
        }
    }

    public static class IsNthOfType extends CssNthEvaluator {
        protected String getPseudoClass() {
            return "nth-of-type";
        }

        public IsNthOfType(int i, int i2) {
            super(i, i2);
        }

        protected int calculatePosition(Element element, Element element2) {
            element = element2.parent().children().iterator();
            int i = 0;
            while (element.hasNext()) {
                Element element3 = (Element) element.next();
                if (element3.tag().equals(element2.tag())) {
                    i++;
                    continue;
                }
                if (element3 == element2) {
                    break;
                }
            }
            return i;
        }
    }

    public static final class IsFirstOfType extends IsNthOfType {
        public String toString() {
            return ":first-of-type";
        }

        public IsFirstOfType() {
            super(0, 1);
        }
    }

    public static final class IsLastChild extends Evaluator {
        public String toString() {
            return ":last-child";
        }

        public boolean matches(Element element, Element element2) {
            element = element2.parent();
            if (element == null || (element instanceof Document) || element2.elementSiblingIndex().intValue() != element.children().size() - 1) {
                return false;
            }
            return true;
        }
    }

    public static class IsNthLastOfType extends CssNthEvaluator {
        protected String getPseudoClass() {
            return "nth-last-of-type";
        }

        public IsNthLastOfType(int i, int i2) {
            super(i, i2);
        }

        protected int calculatePosition(Element element, Element element2) {
            element = element2.parent().children();
            int i = 0;
            for (int intValue = element2.elementSiblingIndex().intValue(); intValue < element.size(); intValue++) {
                if (((Element) element.get(intValue)).tag().equals(element2.tag())) {
                    i++;
                }
            }
            return i;
        }
    }

    public static final class IsLastOfType extends IsNthLastOfType {
        public String toString() {
            return ":last-of-type";
        }

        public IsLastOfType() {
            super(0, 1);
        }
    }

    public static final class IsNthChild extends CssNthEvaluator {
        protected String getPseudoClass() {
            return "nth-child";
        }

        public IsNthChild(int i, int i2) {
            super(i, i2);
        }

        protected int calculatePosition(Element element, Element element2) {
            return element2.elementSiblingIndex().intValue() + 1;
        }
    }

    public static final class IsNthLastChild extends CssNthEvaluator {
        protected String getPseudoClass() {
            return "nth-last-child";
        }

        public IsNthLastChild(int i, int i2) {
            super(i, i2);
        }

        protected int calculatePosition(Element element, Element element2) {
            return element2.parent().children().size() - element2.elementSiblingIndex().intValue();
        }
    }

    public static final class IsOnlyChild extends Evaluator {
        public String toString() {
            return ":only-child";
        }

        public boolean matches(Element element, Element element2) {
            element = element2.parent();
            return (element != null && (element instanceof Document) == null && element2.siblingElements().size() == null) ? true : null;
        }
    }

    public static final class IsOnlyOfType extends Evaluator {
        public String toString() {
            return ":only-of-type";
        }

        public boolean matches(Element element, Element element2) {
            element = element2.parent();
            if (element != null) {
                if (!(element instanceof Document)) {
                    element = element.children().iterator();
                    int i = 0;
                    while (element.hasNext()) {
                        if (((Element) element.next()).tag().equals(element2.tag())) {
                            i++;
                        }
                    }
                    element = true;
                    if (i != 1) {
                        element = null;
                    }
                    return element;
                }
            }
            return false;
        }
    }

    public static final class IsRoot extends Evaluator {
        public String toString() {
            return ":root";
        }

        public boolean matches(Element element, Element element2) {
            if (element instanceof Document) {
                element = element.child(0);
            }
            return element2 == element;
        }
    }

    public static final class Matches extends Evaluator {
        private Pattern pattern;

        public Matches(Pattern pattern) {
            this.pattern = pattern;
        }

        public boolean matches(Element element, Element element2) {
            return this.pattern.matcher(element2.text()).find();
        }

        public String toString() {
            return String.format(":matches(%s", new Object[]{this.pattern});
        }
    }

    public static final class MatchesOwn extends Evaluator {
        private Pattern pattern;

        public MatchesOwn(Pattern pattern) {
            this.pattern = pattern;
        }

        public boolean matches(Element element, Element element2) {
            return this.pattern.matcher(element2.ownText()).find();
        }

        public String toString() {
            return String.format(":matchesOwn(%s", new Object[]{this.pattern});
        }
    }

    public static final class Tag extends Evaluator {
        private String tagName;

        public Tag(String str) {
            this.tagName = str;
        }

        public boolean matches(Element element, Element element2) {
            return element2.tagName().equalsIgnoreCase(this.tagName);
        }

        public String toString() {
            return String.format("%s", new Object[]{this.tagName});
        }
    }

    public static final class TagEndsWith extends Evaluator {
        private String tagName;

        public TagEndsWith(String str) {
            this.tagName = str;
        }

        public boolean matches(Element element, Element element2) {
            return element2.tagName().endsWith(this.tagName);
        }

        public String toString() {
            return String.format("%s", new Object[]{this.tagName});
        }
    }

    public abstract boolean matches(Element element, Element element2);

    protected Evaluator() {
    }
}
