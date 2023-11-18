package de.timroes.axmlrpc.serializer;

import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCRuntimeException;
import de.timroes.axmlrpc.XMLUtil;
import de.timroes.axmlrpc.xmlcreator.XmlElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class StructSerializer implements Serializer {
    private static final String STRUCT_MEMBER = "member";
    private static final String STRUCT_NAME = "name";
    private static final String STRUCT_VALUE = "value";

    public Object deserialize(Element element) throws XMLRPCException {
        Map hashMap = new HashMap();
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            Node item = element.getChildNodes().item(i);
            if (item.getNodeType() != (short) 3 || item.getNodeValue().trim().length() > 0) {
                if (item.getNodeType() != (short) 8) {
                    if (item.getNodeType() == (short) 1) {
                        if (STRUCT_MEMBER.equals(item.getNodeName())) {
                            Object obj = null;
                            Object obj2 = obj;
                            for (int i2 = 0; i2 < item.getChildNodes().getLength(); i2++) {
                                Node item2 = item.getChildNodes().item(i2);
                                if (item2.getNodeType() != (short) 3 || item2.getNodeValue().trim().length() > 0) {
                                    if (item2.getNodeType() != (short) 8) {
                                        if (STRUCT_NAME.equals(item2.getNodeName())) {
                                            if (obj != null) {
                                                throw new XMLRPCException("Name of a struct member cannot be set twice.");
                                            }
                                            obj = XMLUtil.getOnlyTextContent(item2.getChildNodes());
                                        } else if (item2.getNodeType() != (short) 1 || !"value".equals(item2.getNodeName())) {
                                            throw new XMLRPCException("A struct member must only contain one name and one value.");
                                        } else if (obj2 != null) {
                                            throw new XMLRPCException("Value of a struct member cannot be set twice.");
                                        } else {
                                            obj2 = SerializerHandler.getDefault().deserialize((Element) item2);
                                        }
                                    }
                                }
                            }
                            hashMap.put(obj, obj2);
                        }
                    }
                    throw new XMLRPCException("Only struct members allowed within a struct.");
                }
            }
        }
        return hashMap;
    }

    public XmlElement serialize(Object obj) {
        XmlElement xmlElement = new XmlElement(SerializerHandler.TYPE_STRUCT);
        try {
            for (Entry entry : ((Map) obj).entrySet()) {
                XmlElement xmlElement2 = new XmlElement(STRUCT_MEMBER);
                XmlElement xmlElement3 = new XmlElement(STRUCT_NAME);
                XmlElement xmlElement4 = new XmlElement("value");
                xmlElement3.setContent((String) entry.getKey());
                xmlElement4.addChildren(SerializerHandler.getDefault().serialize(entry.getValue()));
                xmlElement2.addChildren(xmlElement3);
                xmlElement2.addChildren(xmlElement4);
                xmlElement.addChildren(xmlElement2);
            }
            return xmlElement;
        } catch (Exception e) {
            throw new XMLRPCRuntimeException(e);
        }
    }
}
