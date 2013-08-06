package org.archiviststoolkit.importer.MARCXML;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.importer.MARCXML.MARCXMLAction;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.structure.MARCXML.DataFieldType;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class Handle111Action implements MARCXMLAction
{
    public void processElement(Resources resource, Object o, InfiniteProgressPanel progressPanel){
        String function = "Creator";
        DataFieldType dataField = (DataFieldType)o;
        Names name = new Names();
        
        String type = Names.CORPORATE_BODY_TYPE;

        //String type = MARCIngest.addItemToList(Names.class,"nameType",name.CORPORATE_BODY_TYPE);
        name.setNameType(type);
        
        //name.setCorporatePrimaryName(MARCIngest.getSubCodeValue(dataField,"a"));
        

        /*Vector<String> subUnits = MARCIngest.getSubCodeValues(dataField,"e");
        int count=0;
        for(String string:subUnits){
            if (count==0)
                name.setCorporateSubordinate1(string);
            else
                name.setCorporateSubordinate2(name.getCorporateSubordinate2()+"|"+string);
        
            count++;
        }*/
        /*
        setQualifier(name,"Location of Meeting","c",dataField);
        setQualifier(name,"Data of meeeting of treaty signing","d",dataField);
        setQualifier(name,"Date of work","f",dataField);
        setQualifier(name,"Miscellaneous Information","g",dataField);
        setQualifier(name,"Medium","h",dataField);
        setQualifier(name,"Form subheading","k",dataField);
        setQualifier(name,"Language or work","l",dataField);
        name.setNumber(MARCIngest.getSubCodeValue(dataField,"n"));
        setQualifier(name,"Name of part/section of a work","p",dataField);
        setQualifier(name,"Name of meeting following jurisdiction name entry element","q",dataField);
        setQualifier(name,"Version","s",dataField);
        setQualifier(name,"Title of a work","t",dataField);
        setQualifier(name,"Affiliation","u",dataField);
        */
        Handle611Action._111_611_711_commonFunctions(dataField, name);
        Handle600Action.setQualifier(name,"Form subdivision","v",dataField);//DIFF between 611        
        Handle600Action.setQualifier(name,"General subdivision","x",dataField);
        Handle600Action.setQualifier(name,"Chronological subdivision","y",dataField);
        Handle600Action.setQualifier(name,"Geographic subdivision","z",dataField);

        String role = MARCIngest.getSubCodeValue(dataField,"4");
        
            
        if( role.startsWith("Auctioneer")||
            role.startsWith("BookSeller")|| 
            role.startsWith("Collector")||
            role.startsWith("Depositor")||
            role.startsWith("Donor")||
            role.startsWith("Former")||
            role.startsWith("Funder")||
            role.startsWith("Owner"))
                function = "source";
                

        try{
            MARCIngest.addName(resource,name,function,role,null);
        }
        catch (UnknownLookupListException ulle){
            ulle.printStackTrace();
        }
        catch (PersistenceException pe){
            pe.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public List getChildren(Object element)
    {
        return null;
    }
        
    /*private void setQualifier(Names name,String title,String subCode,DataFieldType dataField){
        String value = MARCIngest.getSubCodeValue(dataField,subCode);
        String str = name.getQualifier();
        str = str+title+"--"+value+"; ";
        if(value.length()>0)
            name.setQualifier(str);     

       System.out.println(name.getQualifier());     
            
    }*/
}