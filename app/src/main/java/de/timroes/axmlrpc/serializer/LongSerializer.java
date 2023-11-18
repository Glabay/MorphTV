package de.timroes.axmlrpc.serializer;

import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLUtil;
import de.timroes.axmlrpc.xmlcreator.XmlElement;
import org.w3c.dom.Element;

class LongSerializer implements Serializer {
    LongSerializer() {
    }

    public Object deserialize(Element element) throws XMLRPCException {
        return Long.valueOf(Long.parseLong(XMLUtil.getOnlyTextContent(element.getChildNodes())));
    }

    public XmlElement serialize(Object obj) {
        return XMLUtil.makeXmlTag(SerializerHandler.TYPE_LONG, ((Long) obj).toString());
    }
}
