package de.timroes.axmlrpc.serializer;

import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLUtil;
import de.timroes.axmlrpc.xmlcreator.XmlElement;
import de.timroes.base64.Base64;
import org.w3c.dom.Element;

public class Base64Serializer implements Serializer {
    public Object deserialize(Element element) throws XMLRPCException {
        return Base64.decode(XMLUtil.getOnlyTextContent(element.getChildNodes()));
    }

    public XmlElement serialize(Object obj) {
        return XMLUtil.makeXmlTag(SerializerHandler.TYPE_BASE64, Base64.encode((Byte[]) obj));
    }
}
