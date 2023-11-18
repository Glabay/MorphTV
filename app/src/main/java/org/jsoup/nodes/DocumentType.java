package org.jsoup.nodes;

import java.io.IOException;
import kotlin.text.Typography;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Document.OutputSettings.Syntax;

public class DocumentType extends Node {
    private static final String NAME = "name";
    private static final String PUBLIC_ID = "publicId";
    private static final String SYSTEM_ID = "systemId";

    public String nodeName() {
        return "#doctype";
    }

    void outerHtmlTail(Appendable appendable, int i, OutputSettings outputSettings) {
    }

    public DocumentType(String str, String str2, String str3, String str4) {
        super(str4);
        attr(NAME, str);
        attr(PUBLIC_ID, str2);
        attr(SYSTEM_ID, str3);
    }

    void outerHtmlHead(Appendable appendable, int i, OutputSettings outputSettings) throws IOException {
        if (outputSettings.syntax() == Syntax.html && has(PUBLIC_ID) == 0 && has(SYSTEM_ID) == 0) {
            appendable.append("<!doctype");
        } else {
            appendable.append("<!DOCTYPE");
        }
        if (has(NAME) != 0) {
            appendable.append(StringUtils.SPACE).append(attr(NAME));
        }
        if (has(PUBLIC_ID) != 0) {
            appendable.append(" PUBLIC \"").append(attr(PUBLIC_ID)).append(Typography.quote);
        }
        if (has(SYSTEM_ID) != 0) {
            appendable.append(" \"").append(attr(SYSTEM_ID)).append(Typography.quote);
        }
        appendable.append(62);
    }

    private boolean has(String str) {
        return StringUtil.isBlank(attr(str)) ^ 1;
    }
}
