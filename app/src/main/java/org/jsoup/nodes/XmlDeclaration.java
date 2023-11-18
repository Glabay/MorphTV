package org.jsoup.nodes;

import java.io.IOException;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document.OutputSettings;

public class XmlDeclaration extends Node {
    private final boolean isProcessingInstruction;
    private final String name;

    public String nodeName() {
        return "#declaration";
    }

    void outerHtmlTail(Appendable appendable, int i, OutputSettings outputSettings) {
    }

    public XmlDeclaration(String str, String str2, boolean z) {
        super(str2);
        Validate.notNull(str);
        this.name = str;
        this.isProcessingInstruction = z;
    }

    public String name() {
        return this.name;
    }

    public String getWholeDeclaration() {
        return this.attributes.html().trim();
    }

    void outerHtmlHead(Appendable appendable, int i, OutputSettings outputSettings) throws IOException {
        appendable.append("<").append(this.isProcessingInstruction ? "!" : "?").append(this.name);
        this.attributes.html(appendable, outputSettings);
        appendable.append(this.isProcessingInstruction != 0 ? "!" : "?").append(">");
    }

    public String toString() {
        return outerHtml();
    }
}
