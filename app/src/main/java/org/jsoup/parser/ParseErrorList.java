package org.jsoup.parser;

import java.util.ArrayList;

class ParseErrorList extends ArrayList<ParseError> {
    private static final int INITIAL_CAPACITY = 16;
    private final int maxSize;

    ParseErrorList(int i, int i2) {
        super(i);
        this.maxSize = i2;
    }

    boolean canAddError() {
        return size() < this.maxSize;
    }

    int getMaxSize() {
        return this.maxSize;
    }

    static ParseErrorList noTracking() {
        return new ParseErrorList(0, 0);
    }

    static ParseErrorList tracking(int i) {
        return new ParseErrorList(16, i);
    }
}
