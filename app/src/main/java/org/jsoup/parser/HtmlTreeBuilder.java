package org.jsoup.parser;

import com.google.android.exoplayer2.text.ttml.TtmlNode;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class HtmlTreeBuilder extends TreeBuilder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String[] TagSearchButton = new String[]{"button"};
    private static final String[] TagSearchEndTags = new String[]{"dd", "dt", "li", "option", "optgroup", TtmlNode.TAG_P, "rp", "rt"};
    private static final String[] TagSearchList = new String[]{"ol", "ul"};
    private static final String[] TagSearchSelectScope = new String[]{"optgroup", "option"};
    private static final String[] TagSearchSpecial = new String[]{"address", "applet", "area", "article", "aside", "base", "basefont", "bgsound", "blockquote", TtmlNode.TAG_BODY, TtmlNode.TAG_BR, "button", "caption", TtmlNode.CENTER, "col", "colgroup", "command", "dd", "details", "dir", TtmlNode.TAG_DIV, "dl", "dt", "embed", "fieldset", "figcaption", "figure", "footer", "form", "frame", "frameset", "h1", "h2", "h3", "h4", "h5", "h6", TtmlNode.TAG_HEAD, "header", "hgroup", "hr", "html", "iframe", "img", "input", "isindex", "li", "link", "listing", "marquee", "menu", "meta", "nav", "noembed", "noframes", "noscript", "object", "ol", TtmlNode.TAG_P, "param", "plaintext", "pre", "script", "section", "select", TtmlNode.TAG_STYLE, "summary", "table", "tbody", "td", "textarea", "tfoot", "th", "thead", "title", "tr", "ul", "wbr", "xmp"};
    private static final String[] TagSearchTableScope = new String[]{"html", "table"};
    public static final String[] TagsSearchInScope = new String[]{"applet", "caption", "html", "table", "td", "th", "marquee", "object"};
    private boolean baseUriSetFromDoc = false;
    private Element contextElement;
    private EndTag emptyEnd = new EndTag();
    private FormElement formElement;
    private ArrayList<Element> formattingElements = new ArrayList();
    private boolean fosterInserts = false;
    private boolean fragmentParsing = false;
    private boolean framesetOk = true;
    private Element headElement;
    private HtmlTreeBuilderState originalState;
    private List<String> pendingTableCharacters = new ArrayList();
    private String[] specificScopeTarget = new String[]{null};
    private HtmlTreeBuilderState state;

    public /* bridge */ /* synthetic */ boolean processStartTag(String str, Attributes attributes) {
        return super.processStartTag(str, attributes);
    }

    HtmlTreeBuilder() {
    }

    ParseSettings defaultSettings() {
        return ParseSettings.htmlDefault;
    }

    Document parse(String str, String str2, ParseErrorList parseErrorList, ParseSettings parseSettings) {
        this.state = HtmlTreeBuilderState.Initial;
        this.baseUriSetFromDoc = false;
        return super.parse(str, str2, parseErrorList, parseSettings);
    }

    List<Node> parseFragment(String str, Element element, String str2, ParseErrorList parseErrorList, ParseSettings parseSettings) {
        this.state = HtmlTreeBuilderState.Initial;
        initialiseParse(str, str2, parseErrorList, parseSettings);
        this.contextElement = element;
        this.fragmentParsing = true;
        if (element != null) {
            if (element.ownerDocument() != null) {
                this.doc.quirksMode(element.ownerDocument().quirksMode());
            }
            str = element.tagName();
            if (StringUtil.in(str, new String[]{"title", "textarea"}) != null) {
                this.tokeniser.transition(TokeniserState.Rcdata);
            } else if (StringUtil.in(str, new String[]{"iframe", "noembed", "noframes", TtmlNode.TAG_STYLE, "xmp"}) != null) {
                this.tokeniser.transition(TokeniserState.Rawtext);
            } else if (str.equals("script") != null) {
                this.tokeniser.transition(TokeniserState.ScriptData);
            } else if (str.equals("noscript") != null) {
                this.tokeniser.transition(TokeniserState.Data);
            } else if (str.equals("plaintext") != null) {
                this.tokeniser.transition(TokeniserState.Data);
            } else {
                this.tokeniser.transition(TokeniserState.Data);
            }
            str = new Element(Tag.valueOf("html", parseSettings), str2);
            this.doc.appendChild(str);
            this.stack.add(str);
            resetInsertionMode();
            str2 = element.parents();
            str2.add(null, element);
            str2 = str2.iterator();
            while (str2.hasNext() != null) {
                Element element2 = (Element) str2.next();
                if ((element2 instanceof FormElement) != null) {
                    this.formElement = (FormElement) element2;
                    break;
                }
            }
        }
        str = null;
        runParser();
        if (element == null || str == null) {
            return this.doc.childNodes();
        }
        return str.childNodes();
    }

    protected boolean process(Token token) {
        this.currentToken = token;
        return this.state.process(token, this);
    }

    boolean process(Token token, HtmlTreeBuilderState htmlTreeBuilderState) {
        this.currentToken = token;
        return htmlTreeBuilderState.process(token, this);
    }

    void transition(HtmlTreeBuilderState htmlTreeBuilderState) {
        this.state = htmlTreeBuilderState;
    }

    HtmlTreeBuilderState state() {
        return this.state;
    }

    void markInsertionMode() {
        this.originalState = this.state;
    }

    HtmlTreeBuilderState originalState() {
        return this.originalState;
    }

    void framesetOk(boolean z) {
        this.framesetOk = z;
    }

    boolean framesetOk() {
        return this.framesetOk;
    }

    Document getDocument() {
        return this.doc;
    }

    String getBaseUri() {
        return this.baseUri;
    }

    void maybeSetBaseUri(Element element) {
        if (!this.baseUriSetFromDoc) {
            element = element.absUrl("href");
            if (element.length() != 0) {
                this.baseUri = element;
                this.baseUriSetFromDoc = true;
                this.doc.setBaseUri(element);
            }
        }
    }

    boolean isFragmentParsing() {
        return this.fragmentParsing;
    }

    void error(HtmlTreeBuilderState htmlTreeBuilderState) {
        if (this.errors.canAddError()) {
            this.errors.add(new ParseError(this.reader.pos(), "Unexpected token [%s] when in state [%s]", this.currentToken.tokenType(), htmlTreeBuilderState));
        }
    }

    Element insert(StartTag startTag) {
        if (startTag.isSelfClosing()) {
            startTag = insertEmpty(startTag);
            this.stack.add(startTag);
            this.tokeniser.transition(TokeniserState.Data);
            this.tokeniser.emit(this.emptyEnd.reset().name(startTag.tagName()));
            return startTag;
        }
        Element element = new Element(Tag.valueOf(startTag.name(), this.settings), this.baseUri, this.settings.normalizeAttributes(startTag.attributes));
        insert(element);
        return element;
    }

    Element insertStartTag(String str) {
        Element element = new Element(Tag.valueOf(str, this.settings), this.baseUri);
        insert(element);
        return element;
    }

    void insert(Element element) {
        insertNode(element);
        this.stack.add(element);
    }

    Element insertEmpty(StartTag startTag) {
        Tag valueOf = Tag.valueOf(startTag.name(), this.settings);
        Node element = new Element(valueOf, this.baseUri, startTag.attributes);
        insertNode(element);
        if (startTag.isSelfClosing() != null) {
            if (valueOf.isKnownTag() == null) {
                valueOf.setSelfClosing();
                this.tokeniser.acknowledgeSelfClosingFlag();
            } else if (valueOf.isSelfClosing() != null) {
                this.tokeniser.acknowledgeSelfClosingFlag();
            }
        }
        return element;
    }

    FormElement insertForm(StartTag startTag, boolean z) {
        Node formElement = new FormElement(Tag.valueOf(startTag.name(), this.settings), this.baseUri, startTag.attributes);
        setFormElement(formElement);
        insertNode(formElement);
        if (z) {
            this.stack.add(formElement);
        }
        return formElement;
    }

    void insert(Comment comment) {
        insertNode(new Comment(comment.getData(), this.baseUri));
    }

    void insert(Character character) {
        Node textNode;
        String tagName = currentElement().tagName();
        if (!tagName.equals("script")) {
            if (!tagName.equals(TtmlNode.TAG_STYLE)) {
                textNode = new TextNode(character.getData(), this.baseUri);
                currentElement().appendChild(textNode);
            }
        }
        textNode = new DataNode(character.getData(), this.baseUri);
        currentElement().appendChild(textNode);
    }

    private void insertNode(Node node) {
        if (this.stack.size() == 0) {
            this.doc.appendChild(node);
        } else if (isFosterInserts()) {
            insertInFosterParent(node);
        } else {
            currentElement().appendChild(node);
        }
        if (node instanceof Element) {
            Element element = (Element) node;
            if (element.tag().isFormListed() && this.formElement != null) {
                this.formElement.addElement(element);
            }
        }
    }

    Element pop() {
        return (Element) this.stack.remove(this.stack.size() - 1);
    }

    void push(Element element) {
        this.stack.add(element);
    }

    ArrayList<Element> getStack() {
        return this.stack;
    }

    boolean onStack(Element element) {
        return isElementInQueue(this.stack, element);
    }

    private boolean isElementInQueue(ArrayList<Element> arrayList, Element element) {
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            if (((Element) arrayList.get(size)) == element) {
                return true;
            }
        }
        return null;
    }

    Element getFromStack(String str) {
        for (int size = this.stack.size() - 1; size >= 0; size--) {
            Element element = (Element) this.stack.get(size);
            if (element.nodeName().equals(str)) {
                return element;
            }
        }
        return null;
    }

    boolean removeFromStack(Element element) {
        for (int size = this.stack.size() - 1; size >= 0; size--) {
            if (((Element) this.stack.get(size)) == element) {
                this.stack.remove(size);
                return true;
            }
        }
        return null;
    }

    void popStackToClose(String str) {
        int size = this.stack.size() - 1;
        while (size >= 0) {
            Element element = (Element) this.stack.get(size);
            this.stack.remove(size);
            if (!element.nodeName().equals(str)) {
                size--;
            } else {
                return;
            }
        }
    }

    void popStackToClose(String... strArr) {
        int size = this.stack.size() - 1;
        while (size >= 0) {
            Element element = (Element) this.stack.get(size);
            this.stack.remove(size);
            if (!StringUtil.in(element.nodeName(), strArr)) {
                size--;
            } else {
                return;
            }
        }
    }

    void popStackToBefore(String str) {
        int size = this.stack.size() - 1;
        while (size >= 0) {
            if (!((Element) this.stack.get(size)).nodeName().equals(str)) {
                this.stack.remove(size);
                size--;
            } else {
                return;
            }
        }
    }

    void clearStackToTableContext() {
        clearStackToContext("table");
    }

    void clearStackToTableBodyContext() {
        clearStackToContext("tbody", "tfoot", "thead");
    }

    void clearStackToTableRowContext() {
        clearStackToContext("tr");
    }

    private void clearStackToContext(String... strArr) {
        int size = this.stack.size() - 1;
        while (size >= 0) {
            Element element = (Element) this.stack.get(size);
            if (!StringUtil.in(element.nodeName(), strArr)) {
                if (!element.nodeName().equals("html")) {
                    this.stack.remove(size);
                    size--;
                } else {
                    return;
                }
            }
            return;
        }
    }

    Element aboveOnStack(Element element) {
        for (int size = this.stack.size() - 1; size >= 0; size--) {
            if (((Element) this.stack.get(size)) == element) {
                return (Element) this.stack.get(size - 1);
            }
        }
        return null;
    }

    void insertOnStackAfter(Element element, Element element2) {
        element = this.stack.lastIndexOf(element);
        Validate.isTrue(element != -1);
        this.stack.add(element + 1, element2);
    }

    void replaceOnStack(Element element, Element element2) {
        replaceInQueue(this.stack, element, element2);
    }

    private void replaceInQueue(ArrayList<Element> arrayList, Element element, Element element2) {
        element = arrayList.lastIndexOf(element);
        Validate.isTrue(element != -1);
        arrayList.set(element, element2);
    }

    void resetInsertionMode() {
        int size = this.stack.size() - 1;
        Object obj = null;
        while (size >= 0) {
            Element element = (Element) this.stack.get(size);
            if (size == 0) {
                element = this.contextElement;
                obj = 1;
            }
            String nodeName = element.nodeName();
            if ("select".equals(nodeName)) {
                transition(HtmlTreeBuilderState.InSelect);
                return;
            }
            if (!"td".equals(nodeName)) {
                if (!"th".equals(nodeName) || obj != null) {
                    if ("tr".equals(nodeName)) {
                        transition(HtmlTreeBuilderState.InRow);
                        return;
                    }
                    if (!("tbody".equals(nodeName) || "thead".equals(nodeName))) {
                        if (!"tfoot".equals(nodeName)) {
                            if ("caption".equals(nodeName)) {
                                transition(HtmlTreeBuilderState.InCaption);
                                return;
                            } else if ("colgroup".equals(nodeName)) {
                                transition(HtmlTreeBuilderState.InColumnGroup);
                                return;
                            } else if ("table".equals(nodeName)) {
                                transition(HtmlTreeBuilderState.InTable);
                                return;
                            } else if (TtmlNode.TAG_HEAD.equals(nodeName)) {
                                transition(HtmlTreeBuilderState.InBody);
                                return;
                            } else if (TtmlNode.TAG_BODY.equals(nodeName)) {
                                transition(HtmlTreeBuilderState.InBody);
                                return;
                            } else if ("frameset".equals(nodeName)) {
                                transition(HtmlTreeBuilderState.InFrameset);
                                return;
                            } else if ("html".equals(nodeName)) {
                                transition(HtmlTreeBuilderState.BeforeHead);
                                return;
                            } else if (obj != null) {
                                transition(HtmlTreeBuilderState.InBody);
                                return;
                            } else {
                                size--;
                            }
                        }
                    }
                    transition(HtmlTreeBuilderState.InTableBody);
                    return;
                }
            }
            transition(HtmlTreeBuilderState.InCell);
            return;
        }
    }

    private boolean inSpecificScope(String str, String[] strArr, String[] strArr2) {
        this.specificScopeTarget[0] = str;
        return inSpecificScope(this.specificScopeTarget, strArr, strArr2);
    }

    private boolean inSpecificScope(String[] strArr, String[] strArr2, String[] strArr3) {
        for (int size = this.stack.size() - 1; size >= 0; size--) {
            String nodeName = ((Element) this.stack.get(size)).nodeName();
            if (StringUtil.in(nodeName, strArr)) {
                return true;
            }
            if (StringUtil.in(nodeName, strArr2)) {
                return false;
            }
            if (strArr3 != null && StringUtil.in(nodeName, strArr3)) {
                return false;
            }
        }
        Validate.fail("Should not be reachable");
        return false;
    }

    boolean inScope(String[] strArr) {
        return inSpecificScope(strArr, TagsSearchInScope, null);
    }

    boolean inScope(String str) {
        return inScope(str, null);
    }

    boolean inScope(String str, String[] strArr) {
        return inSpecificScope(str, TagsSearchInScope, strArr);
    }

    boolean inListItemScope(String str) {
        return inScope(str, TagSearchList);
    }

    boolean inButtonScope(String str) {
        return inScope(str, TagSearchButton);
    }

    boolean inTableScope(String str) {
        return inSpecificScope(str, TagSearchTableScope, null);
    }

    boolean inSelectScope(String str) {
        for (int size = this.stack.size() - 1; size >= 0; size--) {
            String nodeName = ((Element) this.stack.get(size)).nodeName();
            if (nodeName.equals(str)) {
                return true;
            }
            if (!StringUtil.in(nodeName, TagSearchSelectScope)) {
                return false;
            }
        }
        Validate.fail("Should not be reachable");
        return false;
    }

    void setHeadElement(Element element) {
        this.headElement = element;
    }

    Element getHeadElement() {
        return this.headElement;
    }

    boolean isFosterInserts() {
        return this.fosterInserts;
    }

    void setFosterInserts(boolean z) {
        this.fosterInserts = z;
    }

    FormElement getFormElement() {
        return this.formElement;
    }

    void setFormElement(FormElement formElement) {
        this.formElement = formElement;
    }

    void newPendingTableCharacters() {
        this.pendingTableCharacters = new ArrayList();
    }

    List<String> getPendingTableCharacters() {
        return this.pendingTableCharacters;
    }

    void setPendingTableCharacters(List<String> list) {
        this.pendingTableCharacters = list;
    }

    void generateImpliedEndTags(String str) {
        while (str != null && !currentElement().nodeName().equals(str) && StringUtil.in(currentElement().nodeName(), TagSearchEndTags)) {
            pop();
        }
    }

    void generateImpliedEndTags() {
        generateImpliedEndTags(null);
    }

    boolean isSpecial(Element element) {
        return StringUtil.in(element.nodeName(), TagSearchSpecial);
    }

    Element lastFormattingElement() {
        return this.formattingElements.size() > 0 ? (Element) this.formattingElements.get(this.formattingElements.size() - 1) : null;
    }

    Element removeLastFormattingElement() {
        int size = this.formattingElements.size();
        return size > 0 ? (Element) this.formattingElements.remove(size - 1) : null;
    }

    void pushActiveFormattingElements(Element element) {
        int i = 0;
        for (int size = this.formattingElements.size() - 1; size >= 0; size--) {
            Element element2 = (Element) this.formattingElements.get(size);
            if (element2 == null) {
                break;
            }
            if (isSameFormattingElement(element, element2)) {
                i++;
            }
            if (i == 3) {
                this.formattingElements.remove(size);
                break;
            }
        }
        this.formattingElements.add(element);
    }

    private boolean isSameFormattingElement(Element element, Element element2) {
        return (!element.nodeName().equals(element2.nodeName()) || element.attributes().equals(element2.attributes()) == null) ? null : true;
    }

    void reconstructFormattingElements() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:37)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:61)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r7 = this;
        r0 = r7.lastFormattingElement();
        if (r0 == 0) goto L_0x0059;
    L_0x0006:
        r1 = r7.onStack(r0);
        if (r1 == 0) goto L_0x000d;
    L_0x000c:
        goto L_0x0059;
    L_0x000d:
        r1 = r7.formattingElements;
        r1 = r1.size();
        r2 = 1;
        r1 = r1 - r2;
        r3 = r0;
        r0 = r1;
    L_0x0017:
        r4 = 0;
        if (r0 != 0) goto L_0x001b;
    L_0x001a:
        goto L_0x002e;
    L_0x001b:
        r3 = r7.formattingElements;
        r0 = r0 + -1;
        r3 = r3.get(r0);
        r3 = (org.jsoup.nodes.Element) r3;
        if (r3 == 0) goto L_0x002d;
    L_0x0027:
        r5 = r7.onStack(r3);
        if (r5 == 0) goto L_0x0017;
    L_0x002d:
        r2 = 0;
    L_0x002e:
        if (r2 != 0) goto L_0x003b;
    L_0x0030:
        r2 = r7.formattingElements;
        r0 = r0 + 1;
        r2 = r2.get(r0);
        r2 = (org.jsoup.nodes.Element) r2;
        r3 = r2;
    L_0x003b:
        org.jsoup.helper.Validate.notNull(r3);
        r2 = r3.nodeName();
        r2 = r7.insertStartTag(r2);
        r5 = r2.attributes();
        r6 = r3.attributes();
        r5.addAll(r6);
        r5 = r7.formattingElements;
        r5.set(r0, r2);
        if (r0 != r1) goto L_0x002d;
    L_0x0058:
        return;
    L_0x0059:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jsoup.parser.HtmlTreeBuilder.reconstructFormattingElements():void");
    }

    void clearFormattingElementsToLastMarker() {
        while (!this.formattingElements.isEmpty()) {
            if (removeLastFormattingElement() == null) {
                return;
            }
        }
    }

    void removeFromActiveFormattingElements(Element element) {
        for (int size = this.formattingElements.size() - 1; size >= 0; size--) {
            if (((Element) this.formattingElements.get(size)) == element) {
                this.formattingElements.remove(size);
                return;
            }
        }
    }

    boolean isInActiveFormattingElements(Element element) {
        return isElementInQueue(this.formattingElements, element);
    }

    Element getActiveFormattingElement(String str) {
        int size = this.formattingElements.size() - 1;
        while (size >= 0) {
            Element element = (Element) this.formattingElements.get(size);
            if (element == null) {
                break;
            } else if (element.nodeName().equals(str)) {
                return element;
            } else {
                size--;
            }
        }
        return null;
    }

    void replaceActiveFormattingElement(Element element, Element element2) {
        replaceInQueue(this.formattingElements, element, element2);
    }

    void insertMarkerToFormattingElements() {
        this.formattingElements.add(null);
    }

    void insertInFosterParent(Node node) {
        Element element;
        Element fromStack = getFromStack("table");
        int i = 0;
        if (fromStack == null) {
            element = (Element) this.stack.get(0);
        } else if (fromStack.parent() != null) {
            element = fromStack.parent();
            i = 1;
        } else {
            element = aboveOnStack(fromStack);
        }
        if (i != 0) {
            Validate.notNull(fromStack);
            fromStack.before(node);
            return;
        }
        element.appendChild(node);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("TreeBuilder{currentToken=");
        stringBuilder.append(this.currentToken);
        stringBuilder.append(", state=");
        stringBuilder.append(this.state);
        stringBuilder.append(", currentElement=");
        stringBuilder.append(currentElement());
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
