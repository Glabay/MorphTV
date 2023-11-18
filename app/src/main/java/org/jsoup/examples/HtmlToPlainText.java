package org.jsoup.examples;

import com.google.android.exoplayer2.text.ttml.TtmlNode;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public class HtmlToPlainText {
    private static final int timeout = 5000;
    private static final String userAgent = "Mozilla/5.0 (jsoup)";

    private class FormattingVisitor implements NodeVisitor {
        private static final int maxWidth = 80;
        private StringBuilder accum;
        private int width;

        private FormattingVisitor() {
            this.width = null;
            this.accum = new StringBuilder();
        }

        public void head(Node node, int i) {
            i = node.nodeName();
            if (node instanceof TextNode) {
                append(((TextNode) node).text());
            } else if (i.equals("li") != null) {
                append("\n * ");
            } else if (i.equals("dt") != null) {
                append("  ");
            } else if (StringUtil.in(i, new String[]{TtmlNode.TAG_P, "h1", "h2", "h3", "h4", "h5", "tr"}) != null) {
                append("\n");
            }
        }

        public void tail(Node node, int i) {
            i = node.nodeName();
            if (StringUtil.in(i, TtmlNode.TAG_BR, "dd", "dt", TtmlNode.TAG_P, "h1", "h2", "h3", "h4", "h5")) {
                append("\n");
            } else if (i.equals("a") != 0) {
                append(String.format(" <%s>", new Object[]{node.absUrl("href")}));
            }
        }

        private void append(String str) {
            if (str.startsWith("\n")) {
                this.width = 0;
            }
            if (!str.equals(StringUtils.SPACE) || (this.accum.length() != 0 && !StringUtil.in(this.accum.substring(this.accum.length() - 1), StringUtils.SPACE, "\n"))) {
                if (str.length() + this.width > 80) {
                    str = str.split("\\s+");
                    int i = 0;
                    while (i < str.length) {
                        StringBuilder stringBuilder;
                        String str2 = str[i];
                        if ((i == str.length - 1 ? 1 : null) == null) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(str2);
                            stringBuilder.append(StringUtils.SPACE);
                            str2 = stringBuilder.toString();
                        }
                        if (str2.length() + this.width > 80) {
                            stringBuilder = this.accum;
                            stringBuilder.append("\n");
                            stringBuilder.append(str2);
                            this.width = str2.length();
                        } else {
                            this.accum.append(str2);
                            this.width += str2.length();
                        }
                        i++;
                    }
                } else {
                    this.accum.append(str);
                    this.width += str.length();
                }
            }
        }

        public String toString() {
            return this.accum.toString();
        }
    }

    public static void main(String... strArr) throws IOException {
        boolean z;
        String str;
        Element element;
        HtmlToPlainText htmlToPlainText;
        if (strArr.length != 1) {
            if (strArr.length != 2) {
                z = false;
                Validate.isTrue(z, "usage: java -cp jsoup.jar org.jsoup.examples.HtmlToPlainText url [selector]");
                str = strArr[0];
                strArr = strArr.length != 2 ? strArr[1] : null;
                element = Jsoup.connect(str).userAgent(userAgent).timeout(5000).get();
                htmlToPlainText = new HtmlToPlainText();
                if (strArr == null) {
                    strArr = element.select(strArr).iterator();
                    while (strArr.hasNext()) {
                        System.out.println(htmlToPlainText.getPlainText((Element) strArr.next()));
                    }
                }
                System.out.println(htmlToPlainText.getPlainText(element));
                return;
            }
        }
        z = true;
        Validate.isTrue(z, "usage: java -cp jsoup.jar org.jsoup.examples.HtmlToPlainText url [selector]");
        str = strArr[0];
        if (strArr.length != 2) {
        }
        element = Jsoup.connect(str).userAgent(userAgent).timeout(5000).get();
        htmlToPlainText = new HtmlToPlainText();
        if (strArr == null) {
            System.out.println(htmlToPlainText.getPlainText(element));
            return;
        }
        strArr = element.select(strArr).iterator();
        while (strArr.hasNext()) {
            System.out.println(htmlToPlainText.getPlainText((Element) strArr.next()));
        }
    }

    public String getPlainText(Element element) {
        Object formattingVisitor = new FormattingVisitor();
        new NodeTraversor(formattingVisitor).traverse(element);
        return formattingVisitor.toString();
    }
}
