package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;

abstract class Token {
    TokenType type;

    static final class Character extends Token {
        private String data;

        Character() {
            super();
            this.type = TokenType.Character;
        }

        Token reset() {
            this.data = null;
            return this;
        }

        Character data(String str) {
            this.data = str;
            return this;
        }

        String getData() {
            return this.data;
        }

        public String toString() {
            return getData();
        }
    }

    static final class Comment extends Token {
        boolean bogus;
        final StringBuilder data;

        Token reset() {
            Token.reset(this.data);
            this.bogus = false;
            return this;
        }

        Comment() {
            super();
            this.data = new StringBuilder();
            this.bogus = false;
            this.type = TokenType.Comment;
        }

        String getData() {
            return this.data.toString();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<!--");
            stringBuilder.append(getData());
            stringBuilder.append("-->");
            return stringBuilder.toString();
        }
    }

    static final class Doctype extends Token {
        boolean forceQuirks;
        final StringBuilder name;
        final StringBuilder publicIdentifier;
        final StringBuilder systemIdentifier;

        Doctype() {
            super();
            this.name = new StringBuilder();
            this.publicIdentifier = new StringBuilder();
            this.systemIdentifier = new StringBuilder();
            this.forceQuirks = false;
            this.type = TokenType.Doctype;
        }

        Token reset() {
            Token.reset(this.name);
            Token.reset(this.publicIdentifier);
            Token.reset(this.systemIdentifier);
            this.forceQuirks = false;
            return this;
        }

        String getName() {
            return this.name.toString();
        }

        String getPublicIdentifier() {
            return this.publicIdentifier.toString();
        }

        public String getSystemIdentifier() {
            return this.systemIdentifier.toString();
        }

        public boolean isForceQuirks() {
            return this.forceQuirks;
        }
    }

    static final class EOF extends Token {
        Token reset() {
            return this;
        }

        EOF() {
            super();
            this.type = TokenType.EOF;
        }
    }

    static abstract class Tag extends Token {
        Attributes attributes;
        private boolean hasEmptyAttributeValue = false;
        private boolean hasPendingAttributeValue = false;
        protected String normalName;
        private String pendingAttributeName;
        private StringBuilder pendingAttributeValue = new StringBuilder();
        private String pendingAttributeValueS;
        boolean selfClosing = false;
        protected String tagName;

        Tag() {
            super();
        }

        Tag reset() {
            this.tagName = null;
            this.normalName = null;
            this.pendingAttributeName = null;
            Token.reset(this.pendingAttributeValue);
            this.pendingAttributeValueS = null;
            this.hasEmptyAttributeValue = false;
            this.hasPendingAttributeValue = false;
            this.selfClosing = false;
            this.attributes = null;
            return this;
        }

        final void newAttribute() {
            if (this.attributes == null) {
                this.attributes = new Attributes();
            }
            if (this.pendingAttributeName != null) {
                Attribute attribute;
                if (this.hasPendingAttributeValue) {
                    attribute = new Attribute(this.pendingAttributeName, this.pendingAttributeValue.length() > 0 ? this.pendingAttributeValue.toString() : this.pendingAttributeValueS);
                } else if (this.hasEmptyAttributeValue) {
                    attribute = new Attribute(this.pendingAttributeName, "");
                } else {
                    attribute = new BooleanAttribute(this.pendingAttributeName);
                }
                this.attributes.put(attribute);
            }
            this.pendingAttributeName = null;
            this.hasEmptyAttributeValue = false;
            this.hasPendingAttributeValue = false;
            Token.reset(this.pendingAttributeValue);
            this.pendingAttributeValueS = null;
        }

        final void finaliseTag() {
            if (this.pendingAttributeName != null) {
                newAttribute();
            }
        }

        final String name() {
            boolean z;
            if (this.tagName != null) {
                if (this.tagName.length() != 0) {
                    z = false;
                    Validate.isFalse(z);
                    return this.tagName;
                }
            }
            z = true;
            Validate.isFalse(z);
            return this.tagName;
        }

        final String normalName() {
            return this.normalName;
        }

        final Tag name(String str) {
            this.tagName = str;
            this.normalName = str.toLowerCase();
            return this;
        }

        final boolean isSelfClosing() {
            return this.selfClosing;
        }

        final Attributes getAttributes() {
            return this.attributes;
        }

        final void appendTagName(String str) {
            if (this.tagName != null) {
                str = this.tagName.concat(str);
            }
            this.tagName = str;
            this.normalName = this.tagName.toLowerCase();
        }

        final void appendTagName(char c) {
            appendTagName(String.valueOf(c));
        }

        final void appendAttributeName(String str) {
            if (this.pendingAttributeName != null) {
                str = this.pendingAttributeName.concat(str);
            }
            this.pendingAttributeName = str;
        }

        final void appendAttributeName(char c) {
            appendAttributeName(String.valueOf(c));
        }

        final void appendAttributeValue(String str) {
            ensureAttributeValue();
            if (this.pendingAttributeValue.length() == 0) {
                this.pendingAttributeValueS = str;
            } else {
                this.pendingAttributeValue.append(str);
            }
        }

        final void appendAttributeValue(char c) {
            ensureAttributeValue();
            this.pendingAttributeValue.append(c);
        }

        final void appendAttributeValue(char[] cArr) {
            ensureAttributeValue();
            this.pendingAttributeValue.append(cArr);
        }

        final void appendAttributeValue(int[] iArr) {
            ensureAttributeValue();
            for (int appendCodePoint : iArr) {
                this.pendingAttributeValue.appendCodePoint(appendCodePoint);
            }
        }

        final void setEmptyAttributeValue() {
            this.hasEmptyAttributeValue = true;
        }

        private void ensureAttributeValue() {
            this.hasPendingAttributeValue = true;
            if (this.pendingAttributeValueS != null) {
                this.pendingAttributeValue.append(this.pendingAttributeValueS);
                this.pendingAttributeValueS = null;
            }
        }
    }

    static final class EndTag extends Tag {
        EndTag() {
            this.type = TokenType.EndTag;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("</");
            stringBuilder.append(name());
            stringBuilder.append(">");
            return stringBuilder.toString();
        }
    }

    static final class StartTag extends Tag {
        StartTag() {
            this.attributes = new Attributes();
            this.type = TokenType.StartTag;
        }

        Tag reset() {
            super.reset();
            this.attributes = new Attributes();
            return this;
        }

        StartTag nameAttr(String str, Attributes attributes) {
            this.tagName = str;
            this.attributes = attributes;
            this.normalName = this.tagName.toLowerCase();
            return this;
        }

        public String toString() {
            if (this.attributes == null || this.attributes.size() <= 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("<");
                stringBuilder.append(name());
                stringBuilder.append(">");
                return stringBuilder.toString();
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("<");
            stringBuilder.append(name());
            stringBuilder.append(StringUtils.SPACE);
            stringBuilder.append(this.attributes.toString());
            stringBuilder.append(">");
            return stringBuilder.toString();
        }
    }

    enum TokenType {
        Doctype,
        StartTag,
        EndTag,
        Comment,
        Character,
        EOF
    }

    abstract Token reset();

    private Token() {
    }

    String tokenType() {
        return getClass().getSimpleName();
    }

    static void reset(StringBuilder stringBuilder) {
        if (stringBuilder != null) {
            stringBuilder.delete(0, stringBuilder.length());
        }
    }

    final boolean isDoctype() {
        return this.type == TokenType.Doctype;
    }

    final Doctype asDoctype() {
        return (Doctype) this;
    }

    final boolean isStartTag() {
        return this.type == TokenType.StartTag;
    }

    final StartTag asStartTag() {
        return (StartTag) this;
    }

    final boolean isEndTag() {
        return this.type == TokenType.EndTag;
    }

    final EndTag asEndTag() {
        return (EndTag) this;
    }

    final boolean isComment() {
        return this.type == TokenType.Comment;
    }

    final Comment asComment() {
        return (Comment) this;
    }

    final boolean isCharacter() {
        return this.type == TokenType.Character;
    }

    final Character asCharacter() {
        return (Character) this;
    }

    final boolean isEOF() {
        return this.type == TokenType.EOF;
    }
}
