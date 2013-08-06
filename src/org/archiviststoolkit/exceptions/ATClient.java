package org.archiviststoolkit.exceptions;


import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.utils.Options;
import org.archiviststoolkit.ApplicationFrame;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

public class ATClient {

    //todo change class name to be more descriptive
    public static String processReport(String message, String description, String stackTrace, String name, String email, String environment, String issueReportingURL) throws Exception
    {
        String endpoint="http://dlibprod.home.nyu.edu:8083/at/issueReport/ReportProcessor";
        
        //This is basically here to make the production issue reporting URL is in service
        //Otherwise we will use the development URL which we directly can control
        if(issueReportingURL!=null && issueReportingURL.length()>0){
        	if(checkURLStatus(issueReportingURL))
        		endpoint = issueReportingURL;
        }
        
        Report report = new Report();
        report.setMessage(message);
        report.setDescription(description);
        report.setStacktrace(stackTrace);
        report.setName(name);
        report.setEmail(email);
        System.out.println(environment);
        report.setEnvironment(environment);
    
        Service  service = new Service();
        Call     call    = (Call) service.createCall();
        QName    qn      = new QName( "urn:JIRAWS", "Report" );

        call.registerTypeMapping(Report.class, qn,
                      new org.apache.axis.encoding.ser.BeanSerializerFactory(Report.class, qn),        
                      new org.apache.axis.encoding.ser.BeanDeserializerFactory(Report.class, qn));        
        String result;
        try {
            call.setTargetEndpointAddress( new java.net.URL(endpoint));
            call.setOperationName( new QName("ReportProcessor", "processReport") );
            call.addParameter( "arg1", qn, ParameterMode.IN );
            call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING );

            result = (String) call.invoke( new Object[] { report } );
        } catch (AxisFault fault) {
            result = "Error : Your bug reported cannot be submitted. Please email your report to info@archiviststoolkit.org   --  " + fault.toString();
        }
        
        System.out.println(result);
        return result;
    }
    
    public static boolean checkURLStatus(String address){
    	try{
    	URL url  = new URL(address);
    	HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();
    	urlConnect.connect();
    	int code = urlConnect.getResponseCode();
    	// redirects and other codes could be valid; that is why we are not only looking for a 200
    	if (code>=400 && code <= 505)
    		return false;
    	else 
    		return true;
    	}
    	
    	catch (Exception e){
    		//e.printStackTrace();
    		return false;
    	}
    	
    }
    
    public static void main (String args[]){
    	if(ATClient.checkURLStatus("http://support.archiviststoolkit.org/issueReport")) {
    		System.out.println("OK");

            // try sending a dummy report.
            try {
                String message = "Test Of Issue Reporting System";
                String issueId = processReport(message,message,message,"Nathan","ns96", message,"http://support.archiviststoolkit.org/issueReport");
                System.out.println("Issue ID is : " + issueId);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    	else {
    		System.out.println("NOT OK");
        }
    		
    }
}
