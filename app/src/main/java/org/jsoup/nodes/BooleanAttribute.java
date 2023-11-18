package org.jsoup.nodes;

public class BooleanAttribute extends Attribute {
    protected boolean isBooleanAttribute() {
        return true;
    }

    public BooleanAttribute(String str) {
        super(str, "");
    }
}
