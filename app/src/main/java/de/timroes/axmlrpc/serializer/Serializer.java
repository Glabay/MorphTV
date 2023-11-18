package de.timroes.axmlrpc.serializer;

import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.xmlcreator.XmlElement;
import org.w3c.dom.Element;

public interface Serializer {
    Object deserialize(Element element) throws XMLRPCException;

    XmlElement serialize(Object obj);
}
