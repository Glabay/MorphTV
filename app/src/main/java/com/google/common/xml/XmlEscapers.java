package com.google.common.xml;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import com.google.common.escape.Escapers.Builder;
import kotlin.text.Typography;
import org.apache.commons.lang3.CharUtils;

@GwtCompatible
@Beta
public class XmlEscapers {
    private static final char MAX_ASCII_CONTROL_CHAR = '\u001f';
    private static final char MIN_ASCII_CONTROL_CHAR = '\u0000';
    private static final Escaper XML_ATTRIBUTE_ESCAPER;
    private static final Escaper XML_CONTENT_ESCAPER;
    private static final Escaper XML_ESCAPER;

    private XmlEscapers() {
    }

    public static Escaper xmlContentEscaper() {
        return XML_CONTENT_ESCAPER;
    }

    public static Escaper xmlAttributeEscaper() {
        return XML_ATTRIBUTE_ESCAPER;
    }

    static {
        Builder builder = Escapers.builder();
        char c = '\u0000';
        builder.setSafeRange('\u0000', '�');
        builder.setUnsafeReplacement("�");
        while (c <= MAX_ASCII_CONTROL_CHAR) {
            if (!(c == '\t' || c == '\n' || c == CharUtils.CR)) {
                builder.addEscape(c, "�");
            }
            c = (char) (c + 1);
        }
        builder.addEscape(Typography.amp, "&amp;");
        builder.addEscape(Typography.less, "&lt;");
        builder.addEscape(Typography.greater, "&gt;");
        XML_CONTENT_ESCAPER = builder.build();
        builder.addEscape('\'', "&apos;");
        builder.addEscape(Typography.quote, "&quot;");
        XML_ESCAPER = builder.build();
        builder.addEscape('\t', "&#x9;");
        builder.addEscape('\n', "&#xA;");
        builder.addEscape(CharUtils.CR, "&#xD;");
        XML_ATTRIBUTE_ESCAPER = builder.build();
    }
}
