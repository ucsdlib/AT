package org.archiviststoolkit.importer.MARCXML;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import java.util.Vector;

import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.structure.MARCXML.DataFieldType;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class Handle611Action implements MARCXMLAction
{
    public void processElement(Resources resource, Object o, InfiniteProgressPanel progressPanel){
        String function = "Subject";
        DataFieldType dataField = (DataFieldType)o;
        Names name = new Names();
        
        String type = Names.CORPORATE_BODY_TYPE;
        //String type = MARCIngest.addItemToList(Names.class,"nameType",name.CORPORATE_BODY_TYPE);
        name.setNameType(type);
        
        /*name.setCorporatePrimaryName(MARCIngest.getSubCodeValue(dataField,"a"));
        

        Vector<String> subUnits = MARCIngest.getSubCodeValues(dataField,"e");
        int count=0;
        for(String string:subUnits){
            if (count==0)
                name.setCorporateSubordinate1(string);
            else
                name.setCorporateSubordinate2(name.getCorporateSubordinate2()+"|"+string);
        
            count++;
        }
        */
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
        //setQualifier(name,"General subdivision","x",dataField);
        //setQualifier(name,"Chronological subdivision","y",dataField);
        //setQualifier(name,"Geographic subdivision","z",dataField);
        
        Handle611Action._111_611_711_commonFunctions(dataField, name);
        String formField="";
         formField = Handle600Action.setForm(formField,"Form subdivision","v",dataField);
         formField = Handle600Action.setForm(formField,"General subdivision","x",dataField);
         formField = Handle600Action.setForm(formField,"Chronological subdivision","y",dataField);
         formField = Handle600Action.setForm(formField,"Geographic subdivision","z",dataField);


        String role = MARCIngest.getSubCodeValue(dataField,"4");
        
        try{
            MARCIngest.addName(resource,name,function,role,formField);
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
    
    /*private String setForm(String field,String title,String subCode,DataFieldType dataField){
        String value = MARCIngest.getSubCodeValue(dataField,subCode);
        //String str = name.getQualifier();
        //str = str+title+"--"+value+"; ";
        if(value.length()>0){
            field = field+title+"--"+value+"; ";
        }
        return field;
    } */
    
    public static void _111_611_711_commonFunctions(DataFieldType dataField, Names name){
        name.setCorporatePrimaryName(MARCIngest.getSubCodeValue(dataField,"a"));
        
        Vector<String> subUnits = MARCIngest.getSubCodeValues(dataField,"e");
        int count=0;
        for(String string:subUnits){
            if (count==0)
                name.setCorporateSubordinate1(string);
            else
                name.setCorporateSubordinate2(name.getCorporateSubordinate2()+"|"+string);
        
            count++;
        }
        Handle600Action.setQualifier(name,"Location of Meeting","c",dataField);
        Handle600Action.setQualifier(name,"Data of meeeting of treaty signing","d",dataField);
        Handle600Action.setQualifier(name,"Date of work","f",dataField);
        Handle600Action.setQualifier(name,"Miscellaneous Information","g",dataField);
        Handle600Action.setQualifier(name,"Medium","h",dataField);
        Handle600Action.setQualifier(name,"Form subheading","k",dataField);
        Handle600Action.setQualifier(name,"Language or work","l",dataField);
        name.setNumber(MARCIngest.getSubCodeValue(dataField,"n"));
        Handle600Action.setQualifier(name,"Name of part/section of a work","p",dataField);
        Handle600Action.setQualifier(name,"Name of meeting following jurisdiction name entry element","q",dataField);
        Handle600Action.setQualifier(name,"Version","s",dataField);
        Handle600Action.setQualifier(name,"Title of a work","t",dataField);
        Handle600Action.setQualifier(name,"Affiliation","u",dataField);    
    }
}