package de.timroes.axmlrpc;

import de.timroes.axmlrpc.xmlcreator.XmlElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtil {
    public static Element getOnlyChildElement(NodeList nodeList) throws XMLRPCException {
        Element element = null;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            if (item.getNodeType() != (short) 3 || item.getNodeValue().trim().length() > 0) {
                if (item.getNodeType() != (short) 8) {
                    if (item.getNodeType() != (short) 1) {
                        throw new XMLRPCException("Only element nodes allowed.");
                    } else if (element != null) {
                        throw new XMLRPCException("Element has more than one children.");
                    } else {
                        element = (Element) item;
                    }
                }
            }
        }
        return element;
    }

    public static String getOnlyTextContent(NodeList nodeList) throws XMLRPCException {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            if (item.getNodeType() != (short) 8) {
                if (item.getNodeType() != (short) 3) {
                    throw new XMLRPCException("Element must contain only text elements.");
                }
                stringBuilder.append(item.getNodeValue());
            }
        }
        return stringBuilder.toString();
    }

    public static boolean hasChildElement(NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == (short) 1) {
                return true;
            }
        }
        return false;
    }

    public static XmlElement makeXmlTag(String str, String str2) {
        XmlElement xmlElement = new XmlElement(str);
        xmlElement.setContent(str2);
        return xmlElement;
    }
}
