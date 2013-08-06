package org.archiviststoolkit.importer.MARCXML;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.structure.MARCXML.DataFieldType;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class Handle700Action implements MARCXMLAction
{
    public void processElement(Resources resource, Object o, InfiniteProgressPanel progressPanel)
    {
        String function = "Creator";
        DataFieldType dataField = (DataFieldType)o;
        Names name = new Names();
        String indicator1 = dataField.getInd1();
        
        /*if(indicator1.equals("0"))
        {
            nameType = Names.PERSON_TYPE;
            name.setPersonalDirectOrder(true);
        }
        else if(indicator1.equals("1"))
        {
            nameType = Names.PERSON_TYPE;
            name.setPersonalDirectOrder(false);
        }
        else if(indicator1.equals("3"))
        {
            nameType = Names.FAMILY_TYPE;
            name.setPersonalDirectOrder(true);
        }*/
        
        //nameType = MARCIngest.addItemToList(Names.class,"nameType",nameType);
        //name.setNameType(nameType);
        Handle600Action._100_600_700_commonFunctions(dataField, name);

        /*if(indicator1.equals("0")||indicator1.equals("1"))
        {
        name.setPersonalPrimaryName(MARCIngest.getSubCodeValue(dataField,"a"));
        //name.setSortName(MARCIngest.getSubCodeValue(dataField,"a"));
        name.setNumber(MARCIngest.getSubCodeValue(dataField,"b"));        
        name.setPersonalTitle(MARCIngest.getSubCodeValue(dataField,"c"));
        name.setPersonalDates(MARCIngest.getSubCodeValue(dataField,"d"));
        name.setPersonalFullerForm(MARCIngest.getSubCodeValue(dataField,"q"));        
        }
        else if(indicator1.equals("3"))
        {
        name.setFamilyName(MARCIngest.getSubCodeValue(dataField,"a"));
        //name.setSortName(MARCIngest.getSubCodeValue(dataField,"a"));
        setQualifier(name,"Numeration","b",dataField);
        setQualifier(name,"Title","c",dataField);
        setQualifier(name,"Dates","d",dataField);
        setQualifier(name,"Fuller Form","q",dataField);

        }
        */
        
        if(indicator1.equals("0")||indicator1.equals("1")||indicator1.equals("3"))
        {
            /*
        	setQualifier(name,"Date of a Work","f",dataField);
            setQualifier(name,"Miscellanous Information","g",dataField);
            setQualifier(name,"Attribution Qualifier","j",dataField);
            setQualifier(name,"Medium","h",dataField);
            setQualifier(name,"Form subheading","k",dataField);
            setQualifier(name,"Language or work","l",dataField);
            setQualifier(name,"Medium of performance of music","m",dataField);
            setQualifier(name,"Name of part/section of a work","n",dataField);
            setQualifier(name,"Arranged statement for music","o",dataField);
            setQualifier(name,"Key for music","r",dataField);
            setQualifier(name,"Version","s",dataField);            
            setQualifier(name,"Title of a work","t",dataField);
            setQualifier(name,"Affiliation","u",dataField);
            */
            Handle600Action.setQualifier(name,"Form subdivision","v",dataField);//DIFF between tag:600
            Handle600Action.setQualifier(name,"General subdivision","x",dataField);
            Handle600Action.setQualifier(name,"Chronological subdivision","y",dataField);
            Handle600Action.setQualifier(name,"Geographic subdivision","z",dataField);
        
            String role = MARCIngest.getSubCodeValue(dataField,"e");
            if(role.length()==0)
                role = MARCIngest.getSubCodeValue(dataField,"4");

            if( role.startsWith("Auctioneer")||
                role.startsWith("BookSeller")|| 
                role.startsWith("Collector")||
                role.startsWith("Depositor")||
                role.startsWith("Donor")||
                role.startsWith("Former")||
                role.startsWith("Funder")||
                role.startsWith("Owner"))
                function = "Source"; 

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
        name.createSortName();
       System.out.println(name.getQualifier());     
            
    } */   
}