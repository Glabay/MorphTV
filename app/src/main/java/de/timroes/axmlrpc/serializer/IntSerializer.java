package de.timroes.axmlrpc.serializer;

import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLUtil;
import de.timroes.axmlrpc.xmlcreator.XmlElement;
import org.w3c.dom.Element;

public class IntSerializer implements Serializer {
    public Object deserialize(Element element) throws XMLRPCException {
        return Integer.valueOf(Integer.parseInt(XMLUtil.getOnlyTextContent(element.getChildNodes())));
    }

    public XmlElement serialize(Object obj) {
        return XMLUtil.makeXmlTag(SerializerHandler.TYPE_INT, obj.toString());
    }
}
