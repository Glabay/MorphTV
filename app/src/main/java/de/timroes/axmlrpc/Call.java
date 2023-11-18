package de.timroes.axmlrpc;

import de.timroes.axmlrpc.serializer.SerializerHandler;
import de.timroes.axmlrpc.xmlcreator.SimpleXMLCreator;
import de.timroes.axmlrpc.xmlcreator.XmlElement;

public class Call {
    private String method;
    private Object[] params;

    public Call(String str) {
        this(str, null);
    }

    public Call(String str, Object[] objArr) {
        this.method = str;
        this.params = objArr;
    }

    public String getXML() throws XMLRPCException {
        SimpleXMLCreator simpleXMLCreator = new SimpleXMLCreator();
        XmlElement xmlElement = new XmlElement("methodCall");
        simpleXMLCreator.setRootElement(xmlElement);
        XmlElement xmlElement2 = new XmlElement("methodName");
        xmlElement2.setContent(this.method);
        xmlElement.addChildren(xmlElement2);
        if (this.params != null && this.params.length > 0) {
            xmlElement2 = new XmlElement("params");
            xmlElement.addChildren(xmlElement2);
            for (Object xMLParam : this.params) {
                xmlElement2.addChildren(getXMLParam(xMLParam));
            }
        }
        return simpleXMLCreator.toString();
    }

    private XmlElement getXMLParam(Object obj) throws XMLRPCException {
        XmlElement xmlElement = new XmlElement("param");
        XmlElement xmlElement2 = new XmlElement(XMLRPCClient.VALUE);
        xmlElement.addChildren(xmlElement2);
        xmlElement2.addChildren(SerializerHandler.getDefault().serialize(obj));
        return xmlElement;
    }
}
