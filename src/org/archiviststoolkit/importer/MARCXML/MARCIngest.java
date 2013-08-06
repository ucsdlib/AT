package org.archiviststoolkit.importer.MARCXML;
import java.io.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import java.util.List;

import java.util.Vector;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;


import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.NameUtils;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.importer.ImportException;
import org.archiviststoolkit.importer.ImportExportLogDialog;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.exceptions.ValidationException;
import org.archiviststoolkit.mydomain.NamesDAO;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.mydomain.SubjectsDAO;
import org.archiviststoolkit.structure.FieldNotFoundException;
import org.archiviststoolkit.structure.MARCXML.CollectionType;
import org.archiviststoolkit.structure.MARCXML.ControlFieldType;
import org.archiviststoolkit.structure.MARCXML.DataFieldType;
import org.archiviststoolkit.structure.MARCXML.RecordType;
import org.archiviststoolkit.structure.MARCXML.SubfieldatafieldType;
import org.archiviststoolkit.util.StringHelper;


public class MARCIngest{
    static JAXBContext context = null;
    //static String report = "";
    public static HashMap actionMappings;
    public static DataFieldType resourceTitle = null;
    public static int resourceTitlePriority = 0;
    public static HashMap thesaurusMappings = null;
    public static String report;
    public static boolean error;
    
    public boolean doIngest(File fileName, Resources res,boolean importAll, InfiniteProgressPanel progressPanel) throws ImportException {
        try{
            //Resources res = new Resources();
            if(thesaurusMappings==null){
                thesaurusMappings = new HashMap();
                setThesaurusMappings(thesaurusMappings);
            }
            MARCIngest.error=false;
            report = "";
            resourceTitle=null;
            resourceTitlePriority=0;
            progressPanel.setTextLine("Loading MARCXML document into memory", 1);
            context = JAXBContext.newInstance("org.archiviststoolkit.structure.MARCXML");
            JAXBElement o = (JAXBElement)context.createUnmarshaller().unmarshal(fileName);
                
            CollectionType collectionElem = (CollectionType)o.getValue();

            List records = collectionElem.getRecord();
            RecordType record = (RecordType)records.get(0);
            //RecordTypeType type = record.getType();
            //String typeS = "";
            //if(type!= null)
            //{
                //typeS = type.value();
            //}
            //if((type==null) || (!typeS.equalsIgnoreCase("Bibliographic")))
            //{
                //MARCIngest.importError("Record type attribute must have a value of Bibliographic");
                //return false;
            //}
            
            List<ControlFieldType> controlFields = record.getControlfield();
            for(ControlFieldType controlField:controlFields)
            {
                // if process was cancelled then return
                if(progressPanel.isProcessCancelled()) {
                    return false;
                }
                if(importAll)
                    processControlElement(controlField,res, progressPanel);
                if(MARCIngest.error==true)
                    return false;
            }
            
            List<DataFieldType> dataFields = record.getDatafield();
            for(DataFieldType dataField:dataFields){
                // if process was cancelled then return
                if(progressPanel.isProcessCancelled()) {
                    return false;
                }

                boolean a = processElement(dataField,res,importAll);
                if(a==false){ 
                    importError("Presence of 773 tag indicates this is a child record and cannot be imported", progressPanel);
                    return false;
                }
            }
                        
            return true;
        }

        catch (JAXBException jabe){
            //throw new ImportException("Error loading finding aid into memory", jabe);
            jabe.printStackTrace();
            if(jabe.getMessage().startsWith("unexpected element")){
                importError("The file you are importing does not appear to be an MARCXML document.", progressPanel);
                return false;
            }
            else{
                importError("There appears to be a problem with the MARCXML document", progressPanel);
                return false;
            }
        }
        catch (ClassCastException cce){
            //throw new ImportException("Error loading finding aid into memory", jabe);
            cce.printStackTrace();
            importError("There appears to be a problem with the MARCXML document", progressPanel);
            return false;
        }        
    }
    
    
    
    private void setThesaurusMappings(HashMap thesaurusMappings)
    {
        thesaurusMappings.put("0","Library of Congress Subject Headings");
        thesaurusMappings.put("1","LC subject headings for children's literature");
        thesaurusMappings.put("2","Medical Subject Headings");
        thesaurusMappings.put("3","National Agricultural Library subject authority file");
        thesaurusMappings.put("4","Source not specified");
        thesaurusMappings.put("5","Canadian Subject Headings");
        thesaurusMappings.put("6","R�pertoire de vedettes-mati�re");
        thesaurusMappings.put("7","Source specified in subfield $2");
        
    }
    public static void importError(String message, InfiniteProgressPanel progressPanel){
		progressPanel.close();
		ImportExportLogDialog dialog = new ImportExportLogDialog(message, ImportExportLogDialog.DIALOG_TYPE_IMPORT);
        dialog.showDialog();
        MARCIngest.error=true;
    }
    
    
    public static DataFieldType getResourceTitleTag(){
        return MARCIngest.resourceTitle;
    }
    
    public static int getResourceTitleTagPriority(){
        return MARCIngest.resourceTitlePriority;
    }
    
    public static boolean processElement(DataFieldType dataField,Resources res,boolean importAll){
        String tag = dataField.getTag();
        String className = "org.archiviststoolkit.importer.MARCXML."+"Handle"+tag+"Action";
        if(tag.equals("773")){
            return false;
        }
        if(importAll || tag.startsWith("1") || tag.startsWith("6") || tag.startsWith("7")){
    
        try{
            Class a = Class.forName(className,true,MARCXMLAction.class.getClassLoader());                            
            MARCXMLAction ob = (MARCXMLAction)a.newInstance();
            ob.processElement(res,dataField, null);
            return true;
        }
        catch (ClassNotFoundException cnfe){
            //cnfe.printStackTrace();
            System.out.println(className+ " not found");
        }
        catch (ClassCastException cce){
            //cnfe.printStackTrace();
            System.out.println(className+ " not cast to Action");
        }
        catch (InstantiationException cce){
            //cnfe.printStackTrace();
            System.out.println(className+ " instantiation exception");
        }
        catch (IllegalAccessException cce){
            //cnfe.printStackTrace();
            System.out.println(className+ " illegal access exception");
        } catch (UnsupportedRepeatingDataTypeException e) {
			//todo handle error better
			new ErrorDialog("", e).showDialog();
		}
		}
        return true;
    }

    public static boolean processControlElement(ControlFieldType controlField,Resources res, InfiniteProgressPanel progressPanel){
        String tag = controlField.getTag();
        String className = "org.archiviststoolkit.importer.MARCXML."+"Handle"+tag+"Action";

        try{
            Class a = Class.forName(className,true,MARCXMLAction.class.getClassLoader());                            
            MARCXMLAction ob = (MARCXMLAction)a.newInstance();
            ob.processElement(res,controlField, progressPanel);
            return true;
        }
        catch (ClassNotFoundException cnfe){
            //cnfe.printStackTrace();
            //System.out.println(className+ " not found");
        }
        catch (ClassCastException cce){
            //cnfe.printStackTrace();
            System.out.println(className+ " not cast to Action");
        }
        catch (InstantiationException cce){
            //cnfe.printStackTrace();
            System.out.println(className+ " instantiation exception");
        }
        catch (IllegalAccessException cce){
            //cnfe.printStackTrace();
            System.out.println(className+ " illegal access exception");
        } catch (UnsupportedRepeatingDataTypeException e) {
			//todo handle error better
			new ErrorDialog("", e).showDialog();
		}

		return false;
    }        
    
    public static String getSubCodeValue(DataFieldType dataField,String subCode)
    {
        String value="";
        List<SubfieldatafieldType> subFields = dataField.getSubfield();
        for(SubfieldatafieldType subField:subFields)
        {   
            String code = subField.getCode();
            if(code.equals(subCode)){
                value = subField.getValue();
                if(null!=value)
                    return value;
                else 
                    return "";
            }
        }
        return value;
    }
    public static String getSpecificSubCodeValuesAsDelimitedString(DataFieldType dataField,Vector<String> subCodes,String delimiter){
        String values="";

        boolean firstPass = true;
        for(String subCode:subCodes){
            Vector<String> vals = getSubCodeValues(dataField,subCode);
            for(String val:vals){
            	if(firstPass && val.length()>0){
            		values = val;
            		firstPass=false;
            	}
            	else{
            		if(val.length()>0)
            			values = values+delimiter+val;
            	}
            }
        }
        return values;
    }
    
    public static String getAllSubCodeValuesAsDelimitedString(DataFieldType dataField,String delimiter){
        String values="";
        Vector<String> subCodes = MARCIngest.getAllSubCodeValues(dataField);
        boolean firstPass = true;
        for(String subCode:subCodes){
            if(firstPass && subCode.length()>0){
                values = subCode;
                firstPass=false;
            }
            else{
                if(subCode.length()>0)
                values = values+delimiter+subCode;
            }
        }
        return values;
    }    

    public static Vector<String> getSubCodeValues(DataFieldType dataField,String subCode)
    {
        Vector <String> values = new Vector<String>();
        List<SubfieldatafieldType> subFields = dataField.getSubfield();
        for(SubfieldatafieldType subField:subFields)
        {
            String code = subField.getCode();
            if(code.equals(subCode)){
                String value = subField.getValue();
                values.add(value);
            }
        }
        return values;
    }
    
    public static Vector<String> getAllSubCodeValues(DataFieldType dataField)
    {
        Vector <String> values = new Vector();
        List<SubfieldatafieldType> subFields = dataField.getSubfield();
        for(SubfieldatafieldType subField:subFields){
        values.add(subField.getValue());
        }
        return values;
    }
    public static String getDelimitedString(Vector<String> strings,String delim)
    {
        if(strings==null||strings.size()==0)
            return "";
        String value="";
        boolean first=true;
        for(String string:strings)
        {
            if(!first)
                value=value+delim;
            value=value+string;
            first=false;
        }
    return value;
    }
    public static Vector<String> arrayToVector(String[] values){
        Vector<String> valuesV = new Vector<String>();
        if(values==null || values.length==0)
            return valuesV;
        
        for(int a=0;a<values.length;a++){
            valuesV.add(values[a]);
        }
        return valuesV; 
    }
    public static void addSubjects(ArchDescription archDescription,
                                String subjectS,
                                String subjectTermType,
                                String subjectSource) throws PersistenceException, ValidationException, UnknownLookupListException {
    SubjectsDAO subjectDao = new SubjectsDAO();
    Subjects subject;
    ArchDescriptionSubjects accessionSubject;

        subject = subjectDao.lookupSubject(subjectS, subjectTermType, subjectSource, true);
        accessionSubject = new ArchDescriptionSubjects(subject, archDescription);
        archDescription.addSubject(accessionSubject);
    
    }    
    
    public static void addName(ArchDescription archDescription, Names name, String function,String role,String form) throws PersistenceException, UnknownLookupListException, NoSuchAlgorithmException, UnsupportedEncodingException {
        NamesDAO nameDao = new NamesDAO();
        ArchDescriptionNames archDescriptionName;
        function = MARCIngest.addItemToList(ArchDescriptionNames.class,"nameLinkFunction",function);
        role = StringHelper.cleanUpWhiteSpace(role);
        if(function!=null && (function.equalsIgnoreCase("Creator") || function.equalsIgnoreCase("Subject"))){
    		LookupListItems lli = LookupListUtils.getLookupListItem("Name link creator / subject role", role);
    		if(lli!=null)
    			role = lli.toString();
        }
        else if(function!=null && (function.equalsIgnoreCase("Source"))){
    		LookupListItems lli = LookupListUtils.getLookupListItem("Name link source role", role);
    		if(lli!=null)
    			role = lli.toString();
        }
        
        name.createSortName();
        
        if (name.getNameSource().length() == 0) {
            name.setNameSource("ingest");
        }
     
        NameUtils.setMd5Hash(name);

        name = nameDao.lookupName(name, true);
        
        archDescriptionName = new ArchDescriptionNames(name, archDescription);
        archDescriptionName.setNameLinkFunction(function);
        if(role!=null)
        	archDescriptionName.setRole(role);
        if(StringHelper.isNotEmpty(form)){
            form = MARCIngest.addItemToList(ArchDescriptionNames.class,"form",form);
            archDescriptionName.setForm(form);
        }
        try {
            archDescription.addName(archDescriptionName);
        } catch (DuplicateLinkException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
public static String addItemToList(Class clazz,String fieldName,String value){
    String valueS = "";
    try{
        valueS = LookupListUtils.addItemToListByField(clazz,fieldName,value);
    }
    catch (FieldNotFoundException fnfe){
        fnfe.printStackTrace();
    }
    catch (UnknownLookupListException ulle){
        ulle.printStackTrace();
    }
    if(null!=valueS)
    return valueS;
    else return "";
}
    
}