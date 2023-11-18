package de.timroes.axmlrpc.xmlcreator;

import java.util.ArrayList;
import java.util.List;

public class XmlElement {
    private List<XmlElement> children = new ArrayList();
    private String content;
    private String name;

    public XmlElement(String str) {
        this.name = str;
    }

    public void addChildren(XmlElement xmlElement) {
        this.children.add(xmlElement);
    }

    public void setContent(String str) {
        this.content = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.content != null && this.content.length() > 0) {
            stringBuilder.append("\n<");
            stringBuilder.append(this.name);
            stringBuilder.append(">");
            stringBuilder.append(this.content);
            stringBuilder.append("</");
            stringBuilder.append(this.name);
            stringBuilder.append(">\n");
            return stringBuilder.toString();
        } else if (this.children.size() > 0) {
            stringBuilder.append("\n<");
            stringBuilder.append(this.name);
            stringBuilder.append(">");
            for (XmlElement xmlElement : this.children) {
                stringBuilder.append(xmlElement.toString());
            }
            stringBuilder.append("</");
            stringBuilder.append(this.name);
            stringBuilder.append(">\n");
            return stringBuilder.toString();
        } else {
            stringBuilder.append("\n<");
            stringBuilder.append(this.name);
            stringBuilder.append("/>\n");
            return stringBuilder.toString();
        }
    }
}
