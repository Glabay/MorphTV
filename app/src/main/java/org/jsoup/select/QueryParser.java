package org.jsoup.select;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.text.Typography;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.parser.TokenQueue;
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
import org.jsoup.select.Evaluator.IsEmpty;
import org.jsoup.select.Evaluator.IsFirstChild;
import org.jsoup.select.Evaluator.IsFirstOfType;
import org.jsoup.select.Evaluator.IsLastChild;
import org.jsoup.select.Evaluator.IsLastOfType;
import org.jsoup.select.Evaluator.IsNthChild;
import org.jsoup.select.Evaluator.IsNthLastChild;
import org.jsoup.select.Evaluator.IsNthLastOfType;
import org.jsoup.select.Evaluator.IsNthOfType;
import org.jsoup.select.Evaluator.IsOnlyChild;
import org.jsoup.select.Evaluator.IsOnlyOfType;
import org.jsoup.select.Evaluator.IsRoot;
import org.jsoup.select.Evaluator.Matches;
import org.jsoup.select.Evaluator.MatchesOwn;
import org.jsoup.select.Evaluator.Tag;
import org.jsoup.select.Evaluator.TagEndsWith;
import org.jsoup.select.Selector.SelectorParseException;

class QueryParser {
    private static final String[] AttributeEvals = new String[]{"=", "!=", "^=", "$=", "*=", "~="};
    private static final Pattern NTH_AB = Pattern.compile("((\\+|-)?(\\d+)?)n(\\s*(\\+|-)?\\s*\\d+)?", 2);
    private static final Pattern NTH_B = Pattern.compile("(\\+|-)?(\\d+)");
    private static final String[] combinators = new String[]{",", ">", "+", "~", StringUtils.SPACE};
    private List<Evaluator> evals = new ArrayList();
    private String query;
    private TokenQueue tq;

    private QueryParser(String str) {
        this.query = str;
        this.tq = new TokenQueue(str);
    }

    public static Evaluator parse(String str) {
        return new QueryParser(str).parse();
    }

    Evaluator parse() {
        this.tq.consumeWhitespace();
        if (this.tq.matchesAny(combinators)) {
            this.evals.add(new Root());
            combinator(this.tq.consume());
        } else {
            findElements();
        }
        while (!this.tq.isEmpty()) {
            boolean consumeWhitespace = this.tq.consumeWhitespace();
            if (this.tq.matchesAny(combinators)) {
                combinator(this.tq.consume());
            } else if (consumeWhitespace) {
                combinator(' ');
            } else {
                findElements();
            }
        }
        if (this.evals.size() == 1) {
            return (Evaluator) this.evals.get(0);
        }
        return new And(this.evals);
    }

    private void combinator(char c) {
        Evaluator evaluator;
        Object obj;
        Evaluator evaluator2;
        this.tq.consumeWhitespace();
        Evaluator parse = parse(consumeSubQuery());
        if (this.evals.size() == 1) {
            evaluator = (Evaluator) this.evals.get(0);
            if ((evaluator instanceof Or) && c != ',') {
                obj = 1;
                evaluator2 = evaluator;
                evaluator = ((Or) evaluator).rightMostEvaluator();
                this.evals.clear();
                if (c == Typography.greater) {
                    c = new And(parse, new ImmediateParent(evaluator));
                } else if (c == ' ') {
                    c = new And(parse, new Parent(evaluator));
                } else if (c == '+') {
                    c = new And(parse, new ImmediatePreviousSibling(evaluator));
                } else if (c == '~') {
                    c = new And(parse, new PreviousSibling(evaluator));
                } else if (c == ',') {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown combinator: ");
                    stringBuilder.append(c);
                    throw new SelectorParseException(stringBuilder.toString(), new Object[0]);
                } else if ((evaluator instanceof Or) == '\u0000') {
                    Or or = (Or) evaluator;
                    or.add(parse);
                    c = or;
                } else {
                    c = new Or();
                    c.add(evaluator);
                    c.add(parse);
                }
                if (obj != null) {
                    ((Or) evaluator2).replaceRightMostEvaluator(c);
                    c = evaluator2;
                }
                this.evals.add(c);
            }
        }
        evaluator = new And(this.evals);
        evaluator2 = evaluator;
        obj = null;
        this.evals.clear();
        if (c == Typography.greater) {
            c = new And(parse, new ImmediateParent(evaluator));
        } else if (c == ' ') {
            c = new And(parse, new Parent(evaluator));
        } else if (c == '+') {
            c = new And(parse, new ImmediatePreviousSibling(evaluator));
        } else if (c == '~') {
            c = new And(parse, new PreviousSibling(evaluator));
        } else if (c == ',') {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unknown combinator: ");
            stringBuilder2.append(c);
            throw new SelectorParseException(stringBuilder2.toString(), new Object[0]);
        } else if ((evaluator instanceof Or) == '\u0000') {
            c = new Or();
            c.add(evaluator);
            c.add(parse);
        } else {
            Or or2 = (Or) evaluator;
            or2.add(parse);
            c = or2;
        }
        if (obj != null) {
            ((Or) evaluator2).replaceRightMostEvaluator(c);
            c = evaluator2;
        }
        this.evals.add(c);
    }

    private String consumeSubQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        while (!this.tq.isEmpty()) {
            if (this.tq.matches("(")) {
                stringBuilder.append("(");
                stringBuilder.append(this.tq.chompBalanced('(', ')'));
                stringBuilder.append(")");
            } else if (this.tq.matches("[")) {
                stringBuilder.append("[");
                stringBuilder.append(this.tq.chompBalanced('[', ']'));
                stringBuilder.append("]");
            } else if (this.tq.matchesAny(combinators)) {
                break;
            } else {
                stringBuilder.append(this.tq.consume());
            }
        }
        return stringBuilder.toString();
    }

    private void findElements() {
        if (this.tq.matchChomp("#")) {
            byId();
        } else if (this.tq.matchChomp(".")) {
            byClass();
        } else {
            if (!this.tq.matchesWord()) {
                if (!this.tq.matches("*|")) {
                    if (this.tq.matches("[")) {
                        byAttribute();
                        return;
                    } else if (this.tq.matchChomp("*")) {
                        allElements();
                        return;
                    } else if (this.tq.matchChomp(":lt(")) {
                        indexLessThan();
                        return;
                    } else if (this.tq.matchChomp(":gt(")) {
                        indexGreaterThan();
                        return;
                    } else if (this.tq.matchChomp(":eq(")) {
                        indexEquals();
                        return;
                    } else if (this.tq.matches(":has(")) {
                        has();
                        return;
                    } else if (this.tq.matches(":contains(")) {
                        contains(false);
                        return;
                    } else if (this.tq.matches(":containsOwn(")) {
                        contains(true);
                        return;
                    } else if (this.tq.matches(":matches(")) {
                        matches(false);
                        return;
                    } else if (this.tq.matches(":matchesOwn(")) {
                        matches(true);
                        return;
                    } else if (this.tq.matches(":not(")) {
                        not();
                        return;
                    } else if (this.tq.matchChomp(":nth-child(")) {
                        cssNthChild(false, false);
                        return;
                    } else if (this.tq.matchChomp(":nth-last-child(")) {
                        cssNthChild(true, false);
                        return;
                    } else if (this.tq.matchChomp(":nth-of-type(")) {
                        cssNthChild(false, true);
                        return;
                    } else if (this.tq.matchChomp(":nth-last-of-type(")) {
                        cssNthChild(true, true);
                        return;
                    } else if (this.tq.matchChomp(":first-child")) {
                        this.evals.add(new IsFirstChild());
                        return;
                    } else if (this.tq.matchChomp(":last-child")) {
                        this.evals.add(new IsLastChild());
                        return;
                    } else if (this.tq.matchChomp(":first-of-type")) {
                        this.evals.add(new IsFirstOfType());
                        return;
                    } else if (this.tq.matchChomp(":last-of-type")) {
                        this.evals.add(new IsLastOfType());
                        return;
                    } else if (this.tq.matchChomp(":only-child")) {
                        this.evals.add(new IsOnlyChild());
                        return;
                    } else if (this.tq.matchChomp(":only-of-type")) {
                        this.evals.add(new IsOnlyOfType());
                        return;
                    } else if (this.tq.matchChomp(":empty")) {
                        this.evals.add(new IsEmpty());
                        return;
                    } else if (this.tq.matchChomp(":root")) {
                        this.evals.add(new IsRoot());
                        return;
                    } else {
                        throw new SelectorParseException("Could not parse query '%s': unexpected token at '%s'", this.query, this.tq.remainder());
                    }
                }
            }
            byTag();
        }
    }

    private void byId() {
        String consumeCssIdentifier = this.tq.consumeCssIdentifier();
        Validate.notEmpty(consumeCssIdentifier);
        this.evals.add(new Id(consumeCssIdentifier));
    }

    private void byClass() {
        String consumeCssIdentifier = this.tq.consumeCssIdentifier();
        Validate.notEmpty(consumeCssIdentifier);
        this.evals.add(new Class(consumeCssIdentifier.trim()));
    }

    private void byTag() {
        String consumeElementSelector = this.tq.consumeElementSelector();
        Validate.notEmpty(consumeElementSelector);
        if (consumeElementSelector.startsWith("*|")) {
            this.evals.add(new Or(new Tag(consumeElementSelector.trim().toLowerCase()), new TagEndsWith(consumeElementSelector.replace("*|", ":").trim().toLowerCase())));
            return;
        }
        if (consumeElementSelector.contains("|")) {
            consumeElementSelector = consumeElementSelector.replace("|", ":");
        }
        this.evals.add(new Tag(consumeElementSelector.trim()));
    }

    private void byAttribute() {
        TokenQueue tokenQueue = new TokenQueue(this.tq.chompBalanced('[', ']'));
        String consumeToAny = tokenQueue.consumeToAny(AttributeEvals);
        Validate.notEmpty(consumeToAny);
        tokenQueue.consumeWhitespace();
        if (tokenQueue.isEmpty()) {
            if (consumeToAny.startsWith("^")) {
                this.evals.add(new AttributeStarting(consumeToAny.substring(1)));
            } else {
                this.evals.add(new Attribute(consumeToAny));
            }
        } else if (tokenQueue.matchChomp("=")) {
            this.evals.add(new AttributeWithValue(consumeToAny, tokenQueue.remainder()));
        } else if (tokenQueue.matchChomp("!=")) {
            this.evals.add(new AttributeWithValueNot(consumeToAny, tokenQueue.remainder()));
        } else if (tokenQueue.matchChomp("^=")) {
            this.evals.add(new AttributeWithValueStarting(consumeToAny, tokenQueue.remainder()));
        } else if (tokenQueue.matchChomp("$=")) {
            this.evals.add(new AttributeWithValueEnding(consumeToAny, tokenQueue.remainder()));
        } else if (tokenQueue.matchChomp("*=")) {
            this.evals.add(new AttributeWithValueContaining(consumeToAny, tokenQueue.remainder()));
        } else if (tokenQueue.matchChomp("~=")) {
            this.evals.add(new AttributeWithValueMatching(consumeToAny, Pattern.compile(tokenQueue.remainder())));
        } else {
            throw new SelectorParseException("Could not parse attribute query '%s': unexpected token at '%s'", this.query, tokenQueue.remainder());
        }
    }

    private void allElements() {
        this.evals.add(new AllElements());
    }

    private void indexLessThan() {
        this.evals.add(new IndexLessThan(consumeIndex()));
    }

    private void indexGreaterThan() {
        this.evals.add(new IndexGreaterThan(consumeIndex()));
    }

    private void indexEquals() {
        this.evals.add(new IndexEquals(consumeIndex()));
    }

    private void cssNthChild(boolean z, boolean z2) {
        CharSequence toLowerCase = this.tq.chompTo(")").trim().toLowerCase();
        Matcher matcher = NTH_AB.matcher(toLowerCase);
        Matcher matcher2 = NTH_B.matcher(toLowerCase);
        int i = 2;
        int i2 = 0;
        if ("odd".equals(toLowerCase)) {
            i2 = 1;
        } else if (!"even".equals(toLowerCase)) {
            if (matcher.matches()) {
                i = matcher.group(3) != null ? Integer.parseInt(matcher.group(1).replaceFirst("^\\+", "")) : 1;
                if (matcher.group(4) != null) {
                    i2 = Integer.parseInt(matcher.group(4).replaceFirst("^\\+", ""));
                }
            } else if (matcher2.matches()) {
                i2 = Integer.parseInt(matcher2.group().replaceFirst("^\\+", ""));
                i = 0;
            } else {
                throw new SelectorParseException("Could not parse nth-index '%s': unexpected format", toLowerCase);
            }
        }
        if (z2) {
            if (z) {
                this.evals.add(new IsNthLastOfType(i, i2));
            } else {
                this.evals.add(new IsNthOfType(i, i2));
            }
        } else if (z) {
            this.evals.add(new IsNthLastChild(i, i2));
        } else {
            this.evals.add(new IsNthChild(i, i2));
        }
    }

    private int consumeIndex() {
        String trim = this.tq.chompTo(")").trim();
        Validate.isTrue(StringUtil.isNumeric(trim), "Index must be numeric");
        return Integer.parseInt(trim);
    }

    private void has() {
        this.tq.consume(":has");
        String chompBalanced = this.tq.chompBalanced('(', ')');
        Validate.notEmpty(chompBalanced, ":has(el) subselect must not be empty");
        this.evals.add(new Has(parse(chompBalanced)));
    }

    private void contains(boolean z) {
        this.tq.consume(z ? ":containsOwn" : ":contains");
        String unescape = TokenQueue.unescape(this.tq.chompBalanced('(', ')'));
        Validate.notEmpty(unescape, ":contains(text) query must not be empty");
        if (z) {
            this.evals.add(new ContainsOwnText(unescape));
        } else {
            this.evals.add(new ContainsText(unescape));
        }
    }

    private void matches(boolean z) {
        this.tq.consume(z ? ":matchesOwn" : ":matches");
        String chompBalanced = this.tq.chompBalanced('(', ')');
        Validate.notEmpty(chompBalanced, ":matches(regex) query must not be empty");
        if (z) {
            this.evals.add(new MatchesOwn(Pattern.compile(chompBalanced)));
        } else {
            this.evals.add(new Matches(Pattern.compile(chompBalanced)));
        }
    }

    private void not() {
        this.tq.consume(":not");
        String chompBalanced = this.tq.chompBalanced('(', ')');
        Validate.notEmpty(chompBalanced, ":not(selector) subselect must not be empty");
        this.evals.add(new Not(parse(chompBalanced)));
    }
}
