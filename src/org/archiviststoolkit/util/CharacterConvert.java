/**
 * Archivists' Toolkit(TM) Copyright � 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
 * All rights reserved.
 *
 * This software is free. You can redistribute it and / or modify it under the terms of the Educational Community License (ECL)
 * version 1.0 (http://www.opensource.org/licenses/ecl1.php)
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the ECL license for more details about permissions and limitations.
 *
 *
 * Archivists' Toolkit(TM)
 * http://www.archiviststoolkit.org
 * info@archiviststoolkit.org
 *
 */

package org.archiviststoolkit.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import javax.xml.transform.dom.DOMSource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class CharacterConvert {
    private boolean nameSpaceDeclared = false;
    private boolean docTypeRemoved = false;

    public CharacterConvert() {
    }

    public String doConversion(File fileName, int validationDocumentType) {
        String line = null;
        //String newFileName = fileName.getParent()+"\\modifiedEAD.xml";
        String newFileName = fileName.getParent() + "/modifiedEAD.xml";


        try {
            FileOutputStream fos = new FileOutputStream(newFileName);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            Writer out = new BufferedWriter(osw);
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            while ((line = in.readLine()) != null) {
                //System.out.println("LINE"+line);
                if (validationDocumentType == XMLUtil.DTD_BASED) {
                    if (!nameSpaceDeclared) {
                        line = addNamespace(line);
                        //System.out.println("LINE1.3"+line);

                    }
                    if (!docTypeRemoved) {
                        line = removeDoctype(line);
                        //System.out.println("LINE1.4"+line);

                    }
                }
                //System.out.println("LINE1.5"+line);

                line = line.replace("xmlns:xlink", "xmlns:ns2");
                line = line.replaceAll("xlink:", "ns2:");
                //System.out.println("LINE2"+line);

                line = CharacterConvert.replaceAttribute(line, "linktype", "ns2:type");
                line = CharacterConvert.replaceAttribute(line, "href", "ns2:href");
                line = CharacterConvert.replaceAttribute(line, "show", "ns2:show");
                line = CharacterConvert.replaceAttribute(line, "actuate", "ns2:actuate");
                line = CharacterConvert.replaceAttribute(line, "title", "ns2:title");
                line = CharacterConvert.replaceAttribute(line, "role", "ns2:role");

                //System.out.println("LINE3"+line);

                /*line=line.replace("c01","c");
                line=line.replace("c02","c");
                line=line.replace("c03","c");
                line=line.replace("c04","c");
                line=line.replace("c05","c");
                line=line.replace("c06","c");
                line=line.replace("c07","c");
                line=line.replace("c08","c");
                line=line.replace("c09","c");
                line=line.replace("c10","c");
                line=line.replace("c11","c");
                line=line.replace("c12","c");
                */
                line = unNumberCs(line);
                //System.out.println("LINE4"+line);

                // make sure we don't have tags on seperate lines
                /*String newline = line.trim();

                if(newline.startsWith("<") && !newline.endsWith(">")) {
                    out.write(newline + " ");
                } else {
                    out.write(line + "\n");
                }*/

                out.write(line + "\n");
            }
            in.close();
            out.close();//System.exit(0);
            return newFileName;
        }
        catch (IOException io) {
            io.printStackTrace();
            return null;
        }

    }

    private String removeDoctype(String line) {
        if (!(line.contains("<!DOCTYPE")))
            return line;
        else {
            int startTag;
            int endTag;
            startTag = line.indexOf("<!DOCTYPE");
            endTag = line.indexOf(">", startTag);
            String docType = line.substring(startTag, endTag + 1);
            String newLine = line.replace(docType, "");
            docTypeRemoved = true;
            return newLine;
        }

    }

    private String addNamespace(String line) {
        if (!(line.contains("<ead")) && !(line.contains("<ead ")))
            return line;
        else {
            int startTag;
            int endTag;
            startTag = line.indexOf("<ead>");
            if (startTag == -1)
                startTag = line.indexOf("<ead ");
            if (startTag == -1)
                return line;
            else {
                endTag = line.indexOf(">", startTag);
                String eadElement = line.substring(startTag, endTag + 1);
                String newLine = line.replace(eadElement, "<ead xsi:schemaLocation=\"urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns2=\"http://www.w3.org/1999/xlink\" xmlns=\"urn:isbn:1-931666-22-9\">");
                nameSpaceDeclared = true;
                return newLine;
            }
        }

    }


    public static String replaceAttribute(String element, String attribute, String newAttribute) {
        Pattern p1 = Pattern.compile("<[^>]* " + attribute + ".*>");
        Matcher m1 = p1.matcher(element);
        if (m1.find()) {
            return (element.replaceAll(" " + attribute, " " + newAttribute));
        } else
            return element;


    }

    public static void main(String args[]) {
        CharacterConvert cc = new CharacterConvert();
        String tempstring = CharacterConvert.replaceAttribute("<try href = 'good'><archref>hi</archref></try>", "href", "ns2:href");
        //String tempstring = CharacterConvert.replaceAttribute("<try role  = '987'> role </try>","role", "xlink:role");
        System.out.println(tempstring);

        /*
        String line = "<!DOCTYPE ead PUBLIC \"+//ISBN 1-931666-00-8//DTD ead.dtd (Encoded Archival Description (EAD) Version 2002)//EN\" \"ead.dtd\">abcde";
        String temp = cc.removeDoctype(line);
        */

        String temp = "<ns2:c01 level =\"collection\">dfgfdgf</ns2:c01>";
        String temp2 = "</c01>";
        String temp3 = "<c01>";
        String temp4 = "sldfk<c01></c01>sdlfk";
        String temp45 = "sldfk<c01>c05</c01>sdlfk";
        String temp46 = "sldfk<c01>ksdflj sdfkjds f</c01>sdlfk";
        String temp47 = "<c01 level =\"collection\">dfgfdgf</c01>";
        String temp5 = "<try>c05</try>";
        String temp6 = "abcdefg";

        /*
        System.out.println(cc.unNumberCs(temp));
        System.out.println(cc.unNumberCs(temp2));
        System.out.println(cc.unNumberCs(temp3));
        System.out.println(cc.unNumberCs(temp4));
        System.out.println(cc.unNumberCs(temp45));
        System.out.println(cc.unNumberCs(temp46));
        System.out.println(cc.unNumberCs(temp47));
        System.out.println(cc.unNumberCs(temp5));
        System.out.println(cc.unNumberCs(temp6));
		*/

    }

    public String unNumberCs(String element) {

        Pattern p1 = Pattern.compile("<.*c.*>*");
        //Pattern p1 = Pattern.compile("<.*c.*>"); original buggy code? 7/14/2009
        Matcher m1 = p1.matcher(element);
        Pattern p2 = Pattern.compile("<c[0|1][1-9]");
        Pattern p3 = Pattern.compile(":c[0|1][1-9]");
        Pattern p4 = Pattern.compile("/c[0|1][1-9]");

        if (m1.find()) {
            Matcher m2 = p2.matcher(element);
            if (m2.find()) {
                String temp2 = m2.replaceAll("<c");
                element = temp2;
            }

        }

        m1 = p1.matcher(element);

        if (m1.find()) {
            Matcher m3 = p3.matcher(element);
            if (m3.find()) {
                String temp3 = m3.replaceAll(":c");
                element = temp3;
            }
        }

        m1 = p1.matcher(element);

        if (m1.find()) {
            Matcher m4 = p4.matcher(element);
            if (m4.find()) {
                String temp4 = m4.replaceAll("/c");
                element = temp4;
            }
        }

        return element;
    }


}
