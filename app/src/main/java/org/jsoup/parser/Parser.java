package org.jsoup.parser;

import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class Parser {
    private static final int DEFAULT_MAX_ERRORS = 0;
    private ParseErrorList errors;
    private int maxErrors = 0;
    private ParseSettings settings;
    private TreeBuilder treeBuilder;

    public Parser(TreeBuilder treeBuilder) {
        this.treeBuilder = treeBuilder;
        this.settings = treeBuilder.defaultSettings();
    }

    public Document parseInput(String str, String str2) {
        this.errors = isTrackErrors() ? ParseErrorList.tracking(this.maxErrors) : ParseErrorList.noTracking();
        return this.treeBuilder.parse(str, str2, this.errors, this.settings);
    }

    public TreeBuilder getTreeBuilder() {
        return this.treeBuilder;
    }

    public Parser setTreeBuilder(TreeBuilder treeBuilder) {
        this.treeBuilder = treeBuilder;
        return this;
    }

    public boolean isTrackErrors() {
        return this.maxErrors > 0;
    }

    public Parser setTrackErrors(int i) {
        this.maxErrors = i;
        return this;
    }

    public List<ParseError> getErrors() {
        return this.errors;
    }

    public Parser settings(ParseSettings parseSettings) {
        this.settings = parseSettings;
        return this;
    }

    public ParseSettings settings() {
        return this.settings;
    }

    public static Document parse(String str, String str2) {
        TreeBuilder htmlTreeBuilder = new HtmlTreeBuilder();
        return htmlTreeBuilder.parse(str, str2, ParseErrorList.noTracking(), htmlTreeBuilder.defaultSettings());
    }

    public static List<Node> parseFragment(String str, Element element, String str2) {
        HtmlTreeBuilder htmlTreeBuilder = new HtmlTreeBuilder();
        return htmlTreeBuilder.parseFragment(str, element, str2, ParseErrorList.noTracking(), htmlTreeBuilder.defaultSettings());
    }

    public static List<Node> parseXmlFragment(String str, String str2) {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        return xmlTreeBuilder.parseFragment(str, str2, ParseErrorList.noTracking(), xmlTreeBuilder.defaultSettings());
    }

    public static Document parseBodyFragment(String str, String str2) {
        Document createShell = Document.createShell(str2);
        Element body = createShell.body();
        str = parseFragment(str, body, str2);
        Node[] nodeArr = (Node[]) str.toArray(new Node[str.size()]);
        for (str2 = nodeArr.length - 1; str2 > null; str2--) {
            nodeArr[str2].remove();
        }
        for (Node appendChild : nodeArr) {
            body.appendChild(appendChild);
        }
        return createShell;
    }

    public static String unescapeEntities(String str, boolean z) {
        return new Tokeniser(new CharacterReader(str), ParseErrorList.noTracking()).unescapeEntities(z);
    }

    public static Document parseBodyFragmentRelaxed(String str, String str2) {
        return parse(str, str2);
    }

    public static Parser htmlParser() {
        return new Parser(new HtmlTreeBuilder());
    }

    public static Parser xmlParser() {
        return new Parser(new XmlTreeBuilder());
    }
}
