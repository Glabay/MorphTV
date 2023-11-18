package de.timroes.axmlrpc;

import de.timroes.axmlrpc.serializer.SerializerHandler;
import java.io.InputStream;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;

class ResponseParser {
    private static final String FAULT_CODE = "faultCode";
    private static final String FAULT_STRING = "faultString";

    ResponseParser() {
    }

    public Object parse(InputStream inputStream) throws XMLRPCException {
        try {
            DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
            newInstance.setNamespaceAware(true);
            inputStream = newInstance.newDocumentBuilder().parse(inputStream).getDocumentElement();
            if (inputStream.getNodeName().equals("methodResponse")) {
                inputStream = XMLUtil.getOnlyChildElement(inputStream.getChildNodes());
                if (inputStream.getNodeName().equals("params")) {
                    inputStream = XMLUtil.getOnlyChildElement(inputStream.getChildNodes());
                    if (inputStream.getNodeName().equals("param")) {
                        return getReturnValueFromElement(inputStream);
                    }
                    throw new XMLRPCException("The params tag must contain a param tag.");
                } else if (inputStream.getNodeName().equals("fault")) {
                    Map map = (Map) getReturnValueFromElement(inputStream);
                    throw new XMLRPCServerException((String) map.get(FAULT_STRING), ((Integer) map.get(FAULT_CODE)).intValue());
                } else {
                    throw new XMLRPCException("The methodResponse tag must contain a fault or params tag.");
                }
            }
            throw new XMLRPCException("MethodResponse root tag is missing.");
        } catch (InputStream inputStream2) {
            if (inputStream2 instanceof XMLRPCServerException) {
                throw ((XMLRPCServerException) inputStream2);
            }
            throw new XMLRPCException("Error getting result from server.", inputStream2);
        }
    }

    private Object getReturnValueFromElement(Element element) throws XMLRPCException {
        return SerializerHandler.getDefault().deserialize(XMLUtil.getOnlyChildElement(element.getChildNodes()));
    }
}
