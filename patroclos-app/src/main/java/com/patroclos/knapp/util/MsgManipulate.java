package com.patroclos.knapp.util;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

@Component
public class MsgManipulate {
	
	private static Logger log = LoggerFactory.getLogger(MsgManipulate.class);
	

	public String processMsg(String msg) {

		String newMsg = null;

		try {
			newMsg = prepareSend(msg);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return newMsg;
	}

	public String prepareSend(String msgIn) throws Throwable {
		
		NodeList nodes = null;
		
		try {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(msgIn)));

		XPath xpath = XPathFactory.newInstance().newXPath();

		XPathExpression expr = xpath
				.compile("/UC_STOCK_LEVEL_IFD/CTRL_SEG/*[self::TRNVER or self::CLIENT_ID or self::ROUTE_ID]");

		Object result = expr.evaluate(doc, XPathConstants.NODESET);

		 nodes = (NodeList) result;
		
		} catch (Exception e) {
			log.info("XML Error - prepareSend !", e);
			e.printStackTrace();
		} 

		return formatNewXML(nodes);

	}

	public String formatNewXML(NodeList childs) throws Throwable {

		String msg = null;

		try {

			Document newXmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

			Element root = newXmlDocument.createElement("UC_STOCK_LEVEL_IFD");
			newXmlDocument.appendChild(root);

			Element rootElement = newXmlDocument.createElement("CTRL_SEG");
			root.appendChild(rootElement);

			for (int i = 0; i < childs.getLength(); i++) {
				Node nodeAux = childs.item(i);
				Node copyNode = newXmlDocument.importNode(nodeAux, true);
				rootElement.appendChild(copyNode);
			}

			DOMImplementationLS domImplementationLS = (DOMImplementationLS) newXmlDocument.getImplementation();
			LSSerializer lsSerializer = domImplementationLS.createLSSerializer();

			msg = lsSerializer.writeToString(newXmlDocument).replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();

		} catch (Exception e) {
			log.info("XML Error - formatNewXML !", e);
			e.printStackTrace();
		}

		return msg;

	}

	public String beautyXML(String xml) throws Throwable {

		StringWriter stringWriter = null;
		StreamResult streamResult = null;

		try {

			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new ByteArrayInputStream(xml.getBytes())));

			XPath xPath = XPathFactory.newInstance().newXPath();
			NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']", document,
					XPathConstants.NODESET);

			for (int i = 0; i < nodeList.getLength(); ++i) {
				Node node = nodeList.item(i);
				node.getParentNode().removeChild(node);
			}

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			stringWriter = new StringWriter();
			streamResult = new StreamResult(stringWriter);

			transformer.transform(new DOMSource(document), streamResult);

		} catch (Exception e) {
			log.info("XML Error - beautyXML !", e);
			e.printStackTrace();
		}

		return stringWriter.toString();
	}
	
	 public String getMsgExpectIn() throws Throwable {
	    	
		 String xmlStr = 
   			  "<UC_STOCK_LEVEL_IFD>"
   			+ "  <CTRL_SEG>"
   			+ "    <TRNNAM>UC_STOCK_LEVEL</TRNNAM>"
   			+ "      <TRNVER>20180100</TRNVER>"
   			+ "      <UUID>0de01919-81eb-4cc7-a51d-15f6085fc1a4</UUID>"
   			+ "      <WH_ID>WHS01</WH_ID>"
   			+ "      <CLIENT_ID>CLIE01</CLIENT_ID>"
   			+ "      <ISO_2_CTRY_NAME>GB</ISO_2_CTRY_NAME>"
   			+ "      <REQUEST_ID>bc2a55e8-5a07-4af6-85fd-8290d3ccfb51</REQUEST_ID>"
   			+ "      <ROUTE_ID>186</ROUTE_ID>"
   			+ "  </CTRL_SEG>"
   			+ "</UC_STOCK_LEVEL_IFD>";
		 
		 
    	return formatSt(this.beautyXML(xmlStr));
    }

	public String formatSt(String msg) {

		String newFormat = msg.replaceAll("\\r\\n", "");

		return newFormat;
	}
}