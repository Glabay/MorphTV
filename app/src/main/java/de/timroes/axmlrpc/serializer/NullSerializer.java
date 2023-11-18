package de.timroes.axmlrpc.serializer;

import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.xmlcreator.XmlElement;
import org.w3c.dom.Element;

public class NullSerializer implements Serializer {
    public Object deserialize(Element element) throws XMLRPCException {
        return null;
    }

    public XmlElement serialize(Object obj) {
        return new XmlElement(SerializerHandler.TYPE_NULL);
    }
}
