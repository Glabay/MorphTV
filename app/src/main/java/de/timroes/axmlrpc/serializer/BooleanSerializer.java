package de.timroes.axmlrpc.serializer;

import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLUtil;
import de.timroes.axmlrpc.xmlcreator.XmlElement;
import org.w3c.dom.Element;

public class BooleanSerializer implements Serializer {
    public Object deserialize(Element element) throws XMLRPCException {
        return XMLUtil.getOnlyTextContent(element.getChildNodes()).equals("1") != null ? Boolean.TRUE : Boolean.FALSE;
    }

    public XmlElement serialize(Object obj) {
        return XMLUtil.makeXmlTag(SerializerHandler.TYPE_BOOLEAN, ((Boolean) obj).booleanValue() == 1 ? "1" : "0");
    }
}
