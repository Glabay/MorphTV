package org.jsoup.select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;

abstract class CombiningEvaluator extends Evaluator {
    final ArrayList<Evaluator> evaluators;
    int num;

    static final class And extends CombiningEvaluator {
        And(Collection<Evaluator> collection) {
            super(collection);
        }

        And(Evaluator... evaluatorArr) {
            this(Arrays.asList(evaluatorArr));
        }

        public boolean matches(Element element, Element element2) {
            for (int i = 0; i < this.num; i++) {
                if (!((Evaluator) this.evaluators.get(i)).matches(element, element2)) {
                    return false;
                }
            }
            return true;
        }

        public String toString() {
            return StringUtil.join(this.evaluators, StringUtils.SPACE);
        }
    }

    static final class Or extends CombiningEvaluator {
        Or(Collection<Evaluator> collection) {
            if (this.num > 1) {
                this.evaluators.add(new And((Collection) collection));
            } else {
                this.evaluators.addAll(collection);
            }
            updateNumEvaluators();
        }

        Or(Evaluator... evaluatorArr) {
            this(Arrays.asList(evaluatorArr));
        }

        Or() {
        }

        public void add(Evaluator evaluator) {
            this.evaluators.add(evaluator);
            updateNumEvaluators();
        }

        public boolean matches(Element element, Element element2) {
            for (int i = 0; i < this.num; i++) {
                if (((Evaluator) this.evaluators.get(i)).matches(element, element2)) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return String.format(":or%s", new Object[]{this.evaluators});
        }
    }

    CombiningEvaluator() {
        this.num = 0;
        this.evaluators = new ArrayList();
    }

    CombiningEvaluator(Collection<Evaluator> collection) {
        this();
        this.evaluators.addAll(collection);
        updateNumEvaluators();
    }

    Evaluator rightMostEvaluator() {
        return this.num > 0 ? (Evaluator) this.evaluators.get(this.num - 1) : null;
    }

    void replaceRightMostEvaluator(Evaluator evaluator) {
        this.evaluators.set(this.num - 1, evaluator);
    }

    void updateNumEvaluators() {
        this.num = this.evaluators.size();
    }
}
