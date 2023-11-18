package de.timroes.axmlrpc.serializer;

import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLUtil;
import de.timroes.axmlrpc.xmlcreator.XmlElement;
import java.math.BigDecimal;
import org.w3c.dom.Element;

public class DoubleSerializer implements Serializer {
    public Object deserialize(Element element) throws XMLRPCException {
        return Double.valueOf(XMLUtil.getOnlyTextContent(element.getChildNodes()));
    }

    public XmlElement serialize(Object obj) {
        return XMLUtil.makeXmlTag(SerializerHandler.TYPE_DOUBLE, BigDecimal.valueOf(((Number) obj).doubleValue()).toPlainString());
    }
}
