package de.timroes.axmlrpc.xmlcreator;

public class SimpleXMLCreator {
    private XmlElement root;

    public void setRootElement(XmlElement xmlElement) {
        this.root = xmlElement;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        stringBuilder.append(this.root.toString());
        return stringBuilder.toString();
    }
}
