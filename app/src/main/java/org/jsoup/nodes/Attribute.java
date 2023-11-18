package org.jsoup.nodes;

import com.android.morpheustv.service.BackgroundService;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map.Entry;
import kotlin.text.Typography;
import org.apache.commons.lang3.concurrent.AbstractCircuitBreaker;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Document.OutputSettings.Syntax;

public class Attribute implements Entry<String, String>, Cloneable {
    private static final String[] booleanAttributes = new String[]{"allowfullscreen", "async", "autofocus", "checked", "compact", "declare", BackgroundService.PRIMARY_NOTIFICATION_CHANNEL, "defer", "disabled", "formnovalidate", "hidden", "inert", "ismap", "itemscope", "multiple", "muted", "nohref", "noresize", "noshade", "novalidate", "nowrap", AbstractCircuitBreaker.PROPERTY_NAME, "readonly", "required", "reversed", "seamless", "selected", "sortable", "truespeed", "typemustmatch"};
    private String key;
    private String value;

    public Attribute(String str, String str2) {
        Validate.notEmpty(str);
        Validate.notNull(str2);
        this.key = str.trim();
        this.value = str2;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String str) {
        Validate.notEmpty(str);
        this.key = str.trim();
    }

    public String getValue() {
        return this.value;
    }

    public String setValue(String str) {
        Validate.notNull(str);
        String str2 = this.value;
        this.value = str;
        return str2;
    }

    public String html() {
        Appendable stringBuilder = new StringBuilder();
        try {
            html(stringBuilder, new Document("").outputSettings());
            return stringBuilder.toString();
        } catch (Throwable e) {
            throw new SerializationException(e);
        }
    }

    protected void html(Appendable appendable, OutputSettings outputSettings) throws IOException {
        appendable.append(this.key);
        if (!shouldCollapseAttribute(outputSettings)) {
            appendable.append("=\"");
            Entities.escape(appendable, this.value, outputSettings, true, false, false);
            appendable.append(Typography.quote);
        }
    }

    public String toString() {
        return html();
    }

    public static Attribute createFromEncoded(String str, String str2) {
        return new Attribute(str, Entities.unescape(str2, true));
    }

    protected boolean isDataAttribute() {
        return this.key.startsWith("data-") && this.key.length() > "data-".length();
    }

    protected final boolean shouldCollapseAttribute(OutputSettings outputSettings) {
        return (("".equals(this.value) || this.value.equalsIgnoreCase(this.key)) && outputSettings.syntax() == Syntax.html && isBooleanAttribute() != null) ? true : null;
    }

    protected boolean isBooleanAttribute() {
        return Arrays.binarySearch(booleanAttributes, this.key) >= 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
        r4 = this;
        r0 = 1;
        if (r4 != r5) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = r5 instanceof org.jsoup.nodes.Attribute;
        r2 = 0;
        if (r1 != 0) goto L_0x000a;
    L_0x0009:
        return r2;
    L_0x000a:
        r5 = (org.jsoup.nodes.Attribute) r5;
        r1 = r4.key;
        if (r1 == 0) goto L_0x001b;
    L_0x0010:
        r1 = r4.key;
        r3 = r5.key;
        r1 = r1.equals(r3);
        if (r1 != 0) goto L_0x0020;
    L_0x001a:
        goto L_0x001f;
    L_0x001b:
        r1 = r5.key;
        if (r1 == 0) goto L_0x0020;
    L_0x001f:
        return r2;
    L_0x0020:
        r1 = r4.value;
        if (r1 == 0) goto L_0x002f;
    L_0x0024:
        r1 = r4.value;
        r5 = r5.value;
        r5 = r1.equals(r5);
        if (r5 != 0) goto L_0x0035;
    L_0x002e:
        goto L_0x0034;
    L_0x002f:
        r5 = r5.value;
        if (r5 != 0) goto L_0x0034;
    L_0x0033:
        goto L_0x0035;
    L_0x0034:
        r0 = 0;
    L_0x0035:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jsoup.nodes.Attribute.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int i = 0;
        int hashCode = (this.key != null ? this.key.hashCode() : 0) * 31;
        if (this.value != null) {
            i = this.value.hashCode();
        }
        return hashCode + i;
    }

    public Attribute clone() {
        try {
            return (Attribute) super.clone();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
