package org.jsoup.parser;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

public class XmlTreeBuilder extends TreeBuilder {
    public /* bridge */ /* synthetic */ boolean processStartTag(String str, Attributes attributes) {
        return super.processStartTag(str, attributes);
    }

    ParseSettings defaultSettings() {
        return ParseSettings.preserveCase;
    }

    Document parse(String str, String str2) {
        return parse(str, str2, ParseErrorList.noTracking(), ParseSettings.preserveCase);
    }

    protected void initialiseParse(String str, String str2, ParseErrorList parseErrorList, ParseSettings parseSettings) {
        super.initialiseParse(str, str2, parseErrorList, parseSettings);
        this.stack.add(this.doc);
        this.doc.outputSettings().syntax(Syntax.xml);
    }

    protected boolean process(Token token) {
        switch (token.type) {
            case StartTag:
                insert(token.asStartTag());
                break;
            case EndTag:
                popStackToClose(token.asEndTag());
                break;
            case Comment:
                insert(token.asComment());
                break;
            case Character:
                insert(token.asCharacter());
                break;
            case Doctype:
                insert(token.asDoctype());
                break;
            case EOF:
                break;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected token type: ");
                stringBuilder.append(token.type);
                Validate.fail(stringBuilder.toString());
                break;
        }
        return true;
    }

    private void insertNode(Node node) {
        currentElement().appendChild(node);
    }

    Element insert(StartTag startTag) {
        Tag valueOf = Tag.valueOf(startTag.name(), this.settings);
        Node element = new Element(valueOf, this.baseUri, this.settings.normalizeAttributes(startTag.attributes));
        insertNode(element);
        if (startTag.isSelfClosing() != null) {
            this.tokeniser.acknowledgeSelfClosingFlag();
            if (valueOf.isKnownTag() == null) {
                valueOf.setSelfClosing();
            }
        } else {
            this.stack.add(element);
        }
        return element;
    }

    void insert(Comment comment) {
        Node comment2 = new Comment(comment.getData(), this.baseUri);
        if (comment.bogus != null) {
            comment = comment2.getData();
            if (comment.length() > 1 && (comment.startsWith("!") || comment.startsWith("?"))) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("<");
                stringBuilder.append(comment.substring(1, comment.length() - 1));
                stringBuilder.append(">");
                Element child = Jsoup.parse(stringBuilder.toString(), this.baseUri, Parser.xmlParser()).child(0);
                Node xmlDeclaration = new XmlDeclaration(this.settings.normalizeTag(child.tagName()), comment2.baseUri(), comment.startsWith("!"));
                xmlDeclaration.attributes().addAll(child.attributes());
                comment2 = xmlDeclaration;
            }
        }
        insertNode(comment2);
    }

    void insert(Character character) {
        insertNode(new TextNode(character.getData(), this.baseUri));
    }

    void insert(Doctype doctype) {
        insertNode(new DocumentType(this.settings.normalizeTag(doctype.getName()), doctype.getPublicIdentifier(), doctype.getSystemIdentifier(), this.baseUri));
    }

    private void popStackToClose(EndTag endTag) {
        Element element;
        endTag = endTag.name();
        for (int size = this.stack.size() - 1; size >= 0; size--) {
            element = (Element) this.stack.get(size);
            if (element.nodeName().equals(endTag)) {
                break;
            }
        }
        element = null;
        if (element != null) {
            for (endTag = this.stack.size() - 1; endTag >= null; endTag--) {
                Element element2 = (Element) this.stack.get(endTag);
                this.stack.remove(endTag);
                if (element2 == element) {
                    break;
                }
            }
        }
    }

    List<Node> parseFragment(String str, String str2, ParseErrorList parseErrorList, ParseSettings parseSettings) {
        initialiseParse(str, str2, parseErrorList, parseSettings);
        runParser();
        return this.doc.childNodes();
    }
}
