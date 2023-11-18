package org.jsoup.parser;

import java.util.ArrayList;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

abstract class TreeBuilder {
    protected String baseUri;
    protected Token currentToken;
    protected Document doc;
    private EndTag end = new EndTag();
    protected ParseErrorList errors;
    CharacterReader reader;
    protected ParseSettings settings;
    protected ArrayList<Element> stack;
    private StartTag start = new StartTag();
    Tokeniser tokeniser;

    abstract ParseSettings defaultSettings();

    protected abstract boolean process(Token token);

    TreeBuilder() {
    }

    protected void initialiseParse(String str, String str2, ParseErrorList parseErrorList, ParseSettings parseSettings) {
        Validate.notNull(str, "String input must not be null");
        Validate.notNull(str2, "BaseURI must not be null");
        this.doc = new Document(str2);
        this.settings = parseSettings;
        this.reader = new CharacterReader(str);
        this.errors = parseErrorList;
        this.tokeniser = new Tokeniser(this.reader, parseErrorList);
        this.stack = new ArrayList(32);
        this.baseUri = str2;
    }

    Document parse(String str, String str2, ParseErrorList parseErrorList, ParseSettings parseSettings) {
        initialiseParse(str, str2, parseErrorList, parseSettings);
        runParser();
        return this.doc;
    }

    protected void runParser() {
        Token read;
        do {
            read = this.tokeniser.read();
            process(read);
            read.reset();
        } while (read.type != TokenType.EOF);
    }

    protected boolean processStartTag(String str) {
        if (this.currentToken == this.start) {
            return process(new StartTag().name(str));
        }
        return process(this.start.reset().name(str));
    }

    public boolean processStartTag(String str, Attributes attributes) {
        if (this.currentToken == this.start) {
            return process(new StartTag().nameAttr(str, attributes));
        }
        this.start.reset();
        this.start.nameAttr(str, attributes);
        return process(this.start);
    }

    protected boolean processEndTag(String str) {
        if (this.currentToken == this.end) {
            return process(new EndTag().name(str));
        }
        return process(this.end.reset().name(str));
    }

    protected Element currentElement() {
        int size = this.stack.size();
        return size > 0 ? (Element) this.stack.get(size - 1) : null;
    }
}
