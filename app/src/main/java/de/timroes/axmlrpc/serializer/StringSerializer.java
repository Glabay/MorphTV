package de.timroes.axmlrpc.serializer;

import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLUtil;
import de.timroes.axmlrpc.xmlcreator.XmlElement;
import org.w3c.dom.Element;

public class StringSerializer implements Serializer {
    private boolean decodeStrings;
    private boolean encodeStrings;

    public StringSerializer(boolean z, boolean z2) {
        this.decodeStrings = z2;
        this.encodeStrings = z;
    }

    public Object deserialize(Element element) throws XMLRPCException {
        element = XMLUtil.getOnlyTextContent(element.getChildNodes());
        return this.decodeStrings ? element.replaceAll("&lt;", "<").replaceAll("&amp;", "&") : element;
    }

    public XmlElement serialize(Object obj) {
        obj = obj.toString();
        if (this.encodeStrings) {
            obj = obj.replaceAll("&", "&amp;").replaceAll("<", "&lt;");
        }
        return XMLUtil.makeXmlTag(SerializerHandler.TYPE_STRING, obj);
    }
}
