package org.archiviststoolkit.util;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.archiviststoolkit.importer.ImportException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLUtil {
	private static final String EAD_XML_SCHEMA = "conf/jaxb/ead.xsd";
	private static final String EAD_DTD_SCHEMA = "conf/ead.dtd";
	public static final int DTD_BASED = 0;
	public static final int XMLSCHEMA_BASED = 1;
	private static boolean debug = false;
	
	
	public static int validateFile(File file) throws SAXException, ParserConfigurationException, IOException, ImportException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = dbf.newDocumentBuilder();
		XMLErrorHandler errorHandler = new XMLErrorHandler();
		parser.setErrorHandler(errorHandler);
		try {
			Document dom = parser.parse(file);
			if(dom.getDoctype()!=null){
				File newFileWithDTD = writeXML(dom);
				validateXMLFile_DTD(newFileWithDTD,dom);
				//validateXMLFile_DTD(file,dom);
				return DTD_BASED;
			}
			else{
			Node firstNode = dom.getFirstChild();
			if(firstNode!=null){
					if(firstNode.getNodeName().equalsIgnoreCase("ead")){
						if(getAttribute(firstNode,"xmlns").equals("urn:isbn:1-931666-22-9")){
							validateXMLFile_EADSchema(file);
							return XMLSCHEMA_BASED;
						}
						else if(getAttribute(firstNode,"xmlns").length()>0){
							throw new ImportException("The namespace must be urn:isbn:1-931666-22-9 as defined in EAD2002");
						}
						else{
							//throw new ImportException("This file does not reference an EAD2002 DTD or XML schema and cannot be validated");
							File newFileWithDTD = writeXML(dom);
							validateXMLFile_DTD(newFileWithDTD,dom);
							return DTD_BASED;
						}
					}
				}
			}
		}

		catch (SAXParseException spe) {
			spe.printStackTrace();
			throw spe;
		}
	return -1;
	}
	
	public static File writeXML(Document dom) throws ImportException{
		TransformerFactory t = TransformerFactory.newInstance();
		try {
			Transformer transformer = t.newTransformer();
			File outputFile = new File ("modifiedEAD.xml");
			Source ds = new DOMSource(dom);
			Result rs = new StreamResult(outputFile.toURI().getPath());
			File dtd = new File(EAD_DTD_SCHEMA);
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtd.getAbsolutePath());
			transformer.transform(ds, rs);
			return outputFile;
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			throw new ImportException("There was a problem with the XML parser",e);
		} catch (TransformerException e) {
			e.printStackTrace();
			throw new ImportException("There was a problem with the XML parser",e);
		}
		
	}
	public static String getAttribute(Node node, String name){
		if(node.getAttributes().getLength()>0){
			if(node.getAttributes().getNamedItem(name)!=null)
				return (node.getAttributes().getNamedItem(name).getNodeValue());
		}
		return "";
	}

	
	
	public static void validateXMLFile_DTD(File file, Document dom1)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		if (debug) {
			System.out.println(dbf.isExpandEntityReferences());
		}
		dbf.setValidating(true);
		DocumentBuilder parser = dbf.newDocumentBuilder();
		XMLErrorHandler errorHandler = new XMLErrorHandler();
		parser.setErrorHandler(errorHandler);
		try {
			Document dom = parser.parse(file);
			if (debug) {
				System.out.println("Validated with DTD");
			}
		}

		catch (SAXParseException spe) {
			System.out.println(spe.getLineNumber());
			throw spe;
		}
	}

	public static void validateXMLFile_EADSchema(File file)
			throws IOException, ParserConfigurationException, SAXException {
		//DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		//DocumentBuilder parser = dbf.newDocumentBuilder();
		XMLErrorHandler errorHandler = new XMLErrorHandler();

		//Document dom = parser.parse(file);

		SchemaFactory factory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		Source schemaFile = new StreamSource(new File(EAD_XML_SCHEMA));
		Schema schema = factory.newSchema(schemaFile);

		Validator validator = schema.newValidator();
		validator.setErrorHandler(errorHandler);
		// validate the DOM tree 

		//validator.validate(new DOMSource(dom));
		validator.validate(new StreamSource(file));
		
		if (debug) {
			System.out.println("Validated with XML Schema");
		}


	}

	public static void main(String args[]) {	
		try {
			//validateFile(new File ("C:\\ATProject\\sampleData\\EAD\\bell.xml"));
			//validateFile(new File ("C:\\try.xml"));
			//validateFile(new File ("C:\\Documents and Settings\\Jason\\Desktop\\EAD1.0Test\\EAD1.0Test\\rexro175woDoctype.xml"));
			//validateXMLFile_EADSchema("C:\\ATProject\\sampleData\\EAD\\bellM2V2.xml");
			//validateXMLFile_EADSchema("C:\\ATProject\\sampleData\\EAD\\bellM2V.xml");
			validateFile(new File("C:\\ATProject\\sampleData\\EAD\\test.xml"));
			//validateXMLFile_DTD(new File("C:\\ATProject\\sampleData\\EAD\\bell.xml"));
		} catch (SAXParseException spe) {
			System.out.println("Line " + spe.getLineNumber() + ": "
					+ spe.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
