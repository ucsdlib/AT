package org.archiviststoolkit.util;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class XMLErrorHandler implements ErrorHandler{
	public void error(SAXParseException saxe)throws SAXParseException{
		throw saxe;
	}
	public void fatalError(SAXParseException saxe)throws SAXParseException{
		throw saxe;
	}
	public void warning(SAXParseException saxe)throws SAXParseException{
		throw saxe;
	}
}
