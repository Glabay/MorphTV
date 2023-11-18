package de.timroes.axmlrpc.serializer;

import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLUtil;
import de.timroes.axmlrpc.xmlcreator.XmlElement;
import java.text.SimpleDateFormat;
import org.w3c.dom.Element;

public class DateTimeSerializer implements Serializer {
    private static final String DATETIME_FORMAT = "yyyyMMdd'T'HH:mm:ss";
    private static final SimpleDateFormat DATE_FORMATER = new SimpleDateFormat(DATETIME_FORMAT);

    public Object deserialize(Element element) throws XMLRPCException {
        try {
            return DATE_FORMATER.parse(XMLUtil.getOnlyTextContent(element.getChildNodes()));
        } catch (Element element2) {
            throw new XMLRPCException("Unable to parse given date.", element2);
        }
    }

    public XmlElement serialize(Object obj) {
        return XMLUtil.makeXmlTag(SerializerHandler.TYPE_DATETIME, DATE_FORMATER.format(obj));
    }
}
