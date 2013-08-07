package org.archiviststoolkit.plugin.utils;

import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

/**
 * Archivists' Toolkit(TM) Copyright 2005-2010 Regents of the University of California, New York University, & Five Colleges, Inc.
 * All rights reserved.
 * <p/>
 * This software is free. You can redistribute it and / or modify it under the terms of the Educational Community License (ECL)
 * version 1.0 (http://www.opensource.org/licenses/ecl1.php)
 * <p/>
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the ECL license for more details about permissions and limitations.
 * <p/>
 * <p/>
 * Archivists' Toolkit(TM)
 * http://www.archiviststoolkit.org
 * info@archiviststoolkit.org
 * <p/>
 * Created by IntelliJ IDEA.
 * <p/>
 * This is a simple class which have methods to connecting to the
 * handle system and noid minter at NYU
 *
 * @author: Nathan Stevens
 * Date: Sep 24, 2010
 * Time: 10:01:33 AM
 */
public class WebServiceHelper {
    // The location of the test and development handle rest service
    public static String RESTFUL_HANDLE_URL = "https://dlibprod.home.nyu.edu/id/handle/2333.1";
    public static String RESTFUL_HANDLE_TEST_URL = "https://dlibprod.home.nyu.edu/id/handle/10676";
    public static String RESTFUL_HANDLE_DUMMY_URL = "dummy/dummy";
    public static String RESTFUL_HANDLE_FILE_URL = "file"; // used when URI should be read from file

    //used when URI is constructed using the filename when importing list of files
    public static String RESTFUL_HANDLE_FILENAME_URL = "filename";

    // The default url when creating the handles
    private static String DEFAULT_PAGE_URL = "http://dlib.nyu.edu/object-in-processing";


    /**
     * Method to generate a handle by using the restful interface to the
     * handle server. Those handles will be assessable using the url like
     * <p/>
     * http://hdl.handle.net/prefix/handle
     *
     * @param handleServiceUrl The URL of the Restful handle service
     * @param handleUrl        The url this handle will be bound to i.e. http://some_location/item
     * @param description      A brief description of this handle
     */
    public static String createHandle(String handleServiceUrl, String handleUrl, String description,
                                        String userid, String password) throws Exception {
        String handle = ""; // this is the handle that was returned from server

        // set the correct url
        if (handleServiceUrl == null) {
            handleServiceUrl = RESTFUL_HANDLE_TEST_URL;
        }

        // Get the HTTP client and set the authentication credentials
        String host = getHostFromURL(handleServiceUrl);

        HttpClient httpclient = new HttpClient();

        httpclient.getState().setCredentials(
                new AuthScope(host, 443, AuthScope.ANY_REALM),
                new UsernamePasswordCredentials(userid, password)
        );

        // do automatic authentication if the server request it.
        httpclient.getParams().setAuthenticationPreemptive(true);

        // create the xml that is sent to the restful handle service
        String xml = getHandleXML(handleUrl, description);

        // Prepare HTTP post method.
        PostMethod post = new PostMethod(handleServiceUrl);

        post.setRequestEntity(new StringRequestEntity(xml, "text/xml", null));

        // Execute request
        try {
            int statusCode = httpclient.executeMethod(post);

            // Display status code
            String statusMessage = "Status code: " + statusCode +
                    "\nStatus text: " + post.getStatusText();

            System.out.println(statusMessage);

            // Display response
            System.out.println("Response body: ");
            System.out.println(post.getResponseBodyAsString());

            // if status code doesn't equal to success throw exception
            if (statusCode == HttpStatus.SC_CREATED) {
                handle = getHandleFromXML(post.getResponseBodyAsString());
            } else {
                post.releaseConnection();
                throw new Exception(statusMessage);
            }
        } finally {
            // Release current connection to the server
            post.releaseConnection();
        }

        return handle;
    }

    /**
     * Method to extract the host name from the handle service
     * url
     *
     * @param handleServiceUrl
     * @return The host name
     */
    private static String getHostFromURL(String handleServiceUrl) {
        String[] sa = handleServiceUrl.split("/id");
        String host = "";

        // remove the http part from host name
        if(sa[0].contains("https")) {
            host = sa[0].replace("https://", "");
        } else {
            host = sa[0].replace("http://", "");
        }

        return host;
    }

    /**
     * Method used to extract the created handle from the xml text
     * return by the handle service.
     *
     * @param returnedXML The returned xml
     * @return The handle that was returned
     */
    private static String getHandleFromXML(String returnedXML) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(returnedXML));

            Document doc = db.parse(is);

            // get the root element then the url this handle should be bound to
            Element root = doc.getDocumentElement();
            Element handleElement = (Element) root.getElementsByTagName("hs:location").item(0);

            return handleElement.getTextContent();
        } catch(Exception e) {
            return "";
        }
    }

    /**
     * Generate xml to submit to the restful handle server interface
     * with the specified url
     *
     * @return xml The xml which needs to be submitted to handle server
     */
    public static String getHandleXML(String url, String description) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<hs:info xmlns:hs=\"info:nyu/dl/v1.0/identifiers/handle\">" +
                "<hs:binding>" + url + "</hs:binding>" +
                "<hs:description>" + description + "</hs:description>" +
                "</hs:info>";

        return xml;
    }

    // Main method for testing this class
    public static void main(String[] args) {
        try {
            // test creation of a handle
            createHandle(RESTFUL_HANDLE_TEST_URL, DEFAULT_PAGE_URL, "WebService.java -- TEST HANDLE", "test", "test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
