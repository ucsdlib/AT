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

public class Handle610Action implements MARCXMLAction
{
    public void processElement(Resources resource, Object o, InfiniteProgressPanel progressPanel){
        String function = "Subject";
        DataFieldType dataField = (DataFieldType)o;
        Names name = new Names();
        
        String type = Names.CORPORATE_BODY_TYPE;
        
        //String type = MARCIngest.addItemToList(Names.class,"nameType",name.CORPORATE_BODY_TYPE);
        name.setNameType(type);
        
        name.setCorporatePrimaryName(MARCIngest.getSubCodeValue(dataField,"a"));
        
        String role = MARCIngest.getSubCodeValue(dataField,"e");

        /*
        Vector<String> subUnits = MARCIngest.getSubCodeValues(dataField,"b");
        int count=0;
        boolean firstCorporateSubordinate2=true;
        for(String string:subUnits){
            if (count==0)
                name.setCorporateSubordinate1(string);
            else{
                if(firstCorporateSubordinate2){
                    name.setCorporateSubordinate2(string);
                    firstCorporateSubordinate2=false;
                }
                else
                    name.setCorporateSubordinate2(name.getCorporateSubordinate2()+"|"+string);
            }
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
        setQualifier(name,"Medium of performance of music","m",dataField);
        name.setNumber(MARCIngest.getSubCodeValue(dataField,"n"));
        setQualifier(name,"Arranged statement for music","o",dataField);
        setQualifier(name,"Name of part/section of a work","p",dataField);
        setQualifier(name,"Key for music","r",dataField);
        setQualifier(name,"Version","s",dataField);        
        setQualifier(name,"Title of a work","t",dataField);
        setQualifier(name,"Affiliation","u",dataField);
        */
        //setQualifier(name,"General subdivision","x",dataField);
        //setQualifier(name,"Chronological subdivision","y",dataField);
        //setQualifier(name,"Geographic subdivision","z",dataField);
         
         Handle610Action._110_610_710_commonFunctions(dataField, name);

         String formField="";
         formField = Handle600Action.setForm(formField,"Form subdivision","v",dataField);
         formField = Handle600Action.setForm(formField,"General subdivision","x",dataField);
         formField = Handle600Action.setForm(formField,"Chronological subdivision","y",dataField);
         formField = Handle600Action.setForm(formField,"Geographic subdivision","z",dataField);
        
        

        if(role.length()==0)
            role = MARCIngest.getSubCodeValue(dataField,"4");

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
            
    }

    private String setForm(String field,String title,String subCode,DataFieldType dataField){
        String value = MARCIngest.getSubCodeValue(dataField,subCode);
        //String str = name.getQualifier();
        //str = str+title+"--"+value+"; ";
        if(value.length()>0){
            field = field+title+"--"+value+"; ";
        }
        return field;
    }    */
    
    public static void _110_610_710_commonFunctions(DataFieldType dataField, Names name){
        name.setCorporatePrimaryName(MARCIngest.getSubCodeValue(dataField,"a"));
    
        Vector<String> subUnits = MARCIngest.getSubCodeValues(dataField,"b");
        int count=0;
        boolean firstCorporateSubordinate2=true;
        for(String string:subUnits){
            if (count==0)
                name.setCorporateSubordinate1(string);
            else{
                if(firstCorporateSubordinate2){
                    name.setCorporateSubordinate2(string);
                    firstCorporateSubordinate2=false;
                }
                else
                    name.setCorporateSubordinate2(name.getCorporateSubordinate2()+"|"+string);
            }
            count++;
        }

        Handle600Action.setQualifier(name,"Location of Meeting","c",dataField);
        Handle600Action.setQualifier(name,"Data of meeeting of treaty signing","d",dataField);
        Handle600Action.setQualifier(name,"Date of work","f",dataField);
        Handle600Action.setQualifier(name,"Miscellaneous Information","g",dataField);
        Handle600Action.setQualifier(name,"Medium","h",dataField);
        Handle600Action.setQualifier(name,"Form subheading","k",dataField);
        Handle600Action.setQualifier(name,"Language or work","l",dataField);
        Handle600Action.setQualifier(name,"Medium of performance of music","m",dataField);
        name.setNumber(MARCIngest.getSubCodeValue(dataField,"n"));
        Handle600Action.setQualifier(name,"Arranged statement for music","o",dataField);
        Handle600Action.setQualifier(name,"Name of part/section of a work","p",dataField);
        Handle600Action.setQualifier(name,"Key for music","r",dataField);
        Handle600Action.setQualifier(name,"Version","s",dataField);        
        Handle600Action.setQualifier(name,"Title of a work","t",dataField);
        Handle600Action.setQualifier(name,"Affiliation","u",dataField);
        
        
        
    }
    
}