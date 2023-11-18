package de.timroes.axmlrpc.serializer;

import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCRuntimeException;
import de.timroes.axmlrpc.XMLUtil;
import de.timroes.axmlrpc.xmlcreator.XmlElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ArraySerializer implements Serializer {
    private static final String ARRAY_DATA = "data";
    private static final String ARRAY_VALUE = "value";

    public Object deserialize(Element element) throws XMLRPCException {
        List arrayList = new ArrayList();
        element = XMLUtil.getOnlyChildElement(element.getChildNodes());
        if ("data".equals(element.getNodeName())) {
            for (int i = 0; i < element.getChildNodes().getLength(); i++) {
                Node item = element.getChildNodes().item(i);
                if (item != null && (item.getNodeType() != (short) 3 || item.getNodeValue().trim().length() > 0)) {
                    if (item.getNodeType() != (short) 8) {
                        if (item.getNodeType() != (short) 1) {
                            throw new XMLRPCException("Wrong element inside of array.");
                        }
                        arrayList.add(SerializerHandler.getDefault().deserialize((Element) item));
                    }
                }
            }
            return arrayList.toArray();
        }
        throw new XMLRPCException("The array must contain one data tag.");
    }

    public XmlElement serialize(Object obj) {
        if (obj instanceof Iterable) {
            obj = (Iterable) obj;
        } else {
            obj = Arrays.asList((Object[]) obj);
        }
        XmlElement xmlElement = new XmlElement(SerializerHandler.TYPE_ARRAY);
        XmlElement xmlElement2 = new XmlElement("data");
        xmlElement.addChildren(xmlElement2);
        try {
            for (Object next : r6) {
                XmlElement xmlElement3 = new XmlElement("value");
                xmlElement3.addChildren(SerializerHandler.getDefault().serialize(next));
                xmlElement2.addChildren(xmlElement3);
            }
            return xmlElement;
        } catch (Exception e) {
            throw new XMLRPCRuntimeException(e);
        }
    }
}
