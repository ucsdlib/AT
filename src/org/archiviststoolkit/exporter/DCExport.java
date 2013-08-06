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
 */

package org.archiviststoolkit.exporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

import java.util.Vector;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.archiviststoolkit.model.ArchDescriptionNames;
import org.archiviststoolkit.model.ArchDescriptionNotes;
import org.archiviststoolkit.model.ArchDescriptionRepeatingData;
import org.archiviststoolkit.model.ArchDescriptionSubjects;
import org.archiviststoolkit.model.DigitalObjects;
import org.archiviststoolkit.model.FileVersions;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.mydomain.DigitalObjectDAO;
import org.archiviststoolkit.structure.DC.ElementType;
import org.archiviststoolkit.structure.DC.ObjectFactory;
import org.archiviststoolkit.util.StringHelper;


public class DCExport {
    private boolean internalOnly;
    
    public void convertDBRecordToFile(DigitalObjects digitalObject,File file, boolean internalOnly){
        String xml = convertDBRecordToXML(digitalObject,internalOnly,true);
        System.out.println(xml);
        String header = "<ROW xmlns=\"http://www.cdlib.org/schemas/xmldata\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.cdlib.org/schemas/xmldata http://ark.cdlib.org/schemas/xmldata/xmldata.xsd\">";
        xml = header+xml+"</ROW>";
        try{
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
        Writer out = new BufferedWriter(osw);
        out.write(xml);
        out.close();
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
        //System.out.println(xml);
    }
    public String convertDBRecordToXML(DigitalObjects digitalObject,boolean internalOnly,boolean standalone){
        ObjectFactory obj =new ObjectFactory();
        Vector<JAXBElement> elements = new Vector();
        ElementType etype = new ElementType();
        JAXBElement type;
        this.internalOnly = internalOnly;
        if(digitalObject.getTitle()!=null && digitalObject.getTitle().length()>0){
        etype.setValue(StringHelper.tagRemover(digitalObject.getTitle()));
        type = obj.createTitle(etype);
        elements.add(type);
        }

        if(digitalObject.getObjectType()!=null && digitalObject.getObjectType().length()>0){        
        etype = new ElementType();
        etype.setValue(StringHelper.tagRemover(digitalObject.getObjectType()));
        type = obj.createType(etype);       
        elements.add(type);
        }

        if(digitalObject.getLanguageCode()!=null && digitalObject.getLanguageCode().length()>0){
        etype = new ElementType();
        etype.setValue(StringHelper.tagRemover(digitalObject.getLanguageCode()));
        type = obj.createLanguage(etype);       
        elements.add(type);
        }

        boolean dateEntered = false;
        if(digitalObject.getDateExpression()!=null && digitalObject.getDateExpression().length()>0){
        etype = new ElementType();
        etype.setValue(StringHelper.tagRemover(digitalObject.getDateExpression()));
        type = obj.createDate(etype);       
        elements.add(type);
        dateEntered=true;
        }
        
        if(digitalObject.getDateBegin()!=null && digitalObject.getDateBegin()>0){
        etype = new ElementType();
        etype.setValue(digitalObject.getDateBegin()+"-"+digitalObject.getDateEnd());
        type = obj.createDate(etype);       
        if(standalone && dateEntered==false)
            elements.add(type);
        else if(!standalone)
            elements.add(type);                        
        }
        
        //if(digitalObject.getLanguageCode()!=null && digitalObject.getLanguageCode().length()>0){
        //etype = new ElementType();
        //etype.setValue(METSExport.tagRemover(digitalObject.getLanguageCode()));
        //type = obj.createLanguage(etype);       
        //elements.add(type);        
        //}
        
        setCreators(elements,digitalObject,obj);
        setNameSubjects(elements,digitalObject,obj);
        setSubjects(elements,digitalObject,obj);
        setCoverage(elements,digitalObject,obj);
        setType(elements,digitalObject,obj);
        setDescription(elements,digitalObject,obj);
        setNoteDescription(elements,digitalObject,obj);
        setRights(elements,digitalObject,obj);
        setFormat(elements,digitalObject,obj);
        setSource(elements,digitalObject,obj);
        setRelation(elements,digitalObject,obj,standalone);
        if(standalone)
            setIdentifiers(elements,digitalObject,obj);

        try{
            JAXBContext context = JAXBContext.newInstance("org.archiviststoolkit.structure.DC");
            StringWriter sw = null;
            StringBuffer sb = new StringBuffer();
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FRAGMENT,Boolean.FALSE);
            m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE );
            for(JAXBElement element:elements)
            {
                sw = new StringWriter();
                m.marshal(element,sw);
                String elementS = sw.toString().substring("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>".length());
                elementS = elementS.replace("xmlns=\"http://purl.org/dc/elements/1.1/\"","");
                sb.append(elementS);
                sb.append("\n");
            }
            
            return sb.toString();
            //return(sw.toString().substring("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>".length()+1));
        }
        catch(JAXBException jabe){
            jabe.printStackTrace();
            return "";
        }    
    }

    private void setCreators(Vector elements, DigitalObjects digitalObjects, ObjectFactory obj)
    {
        for(ArchDescriptionNames name:digitalObjects.getNames())
        {
            String function = name.getNameLinkFunction();
            if(function.equalsIgnoreCase("creator"))
            {
                ElementType etype = new ElementType();
                etype.setValue(StringHelper.tagRemover(name.getSortName()));
                JAXBElement type = obj.createCreator(etype);       
                elements.add(type);        
            }
        }
    }
    
    private void setNameSubjects(Vector elements, DigitalObjects digitalObjects, ObjectFactory obj)
    {
        for(ArchDescriptionNames name:digitalObjects.getNames())
        {
            String function = name.getNameLinkFunction();
            if(function.equalsIgnoreCase("subject"))
            {
                ElementType etype = new ElementType();
                etype.setValue(StringHelper.tagRemover(name.getSortName()));
                JAXBElement type = obj.createSubject(etype);       
                elements.add(type);        
            }
        }
    }

    private void setSubjects(Vector elements, DigitalObjects digitalObjects, ObjectFactory obj)
    {
        for(ArchDescriptionSubjects subject:digitalObjects.getSubjects())
        {
            String typeS = subject.getSubject().getSubjectTermType();
            if(typeS.equalsIgnoreCase("Topical Term (650)")||
               typeS.equalsIgnoreCase("Occupation (656)")||
               typeS.equalsIgnoreCase("Function (657)") ||
               typeS.equalsIgnoreCase("Uniform Title (630)"))
            {
                ElementType etype = new ElementType();
                etype.setValue(StringHelper.tagRemover(subject.getSubjectTerm()));
                JAXBElement type = obj.createSubject(etype);       
                elements.add(type);        
            }
        }
    }    

    private void setCoverage(Vector elements, DigitalObjects digitalObjects, ObjectFactory obj)
    {
        for(ArchDescriptionSubjects subject:digitalObjects.getSubjects())
        {
            String typeS = subject.getSubject().getSubjectTermType();
            if(typeS.equalsIgnoreCase("Geographic Name (651)"))
            {
                ElementType etype = new ElementType();
                etype.setValue(StringHelper.tagRemover(subject.getSubjectTerm()));
                JAXBElement type = obj.createCoverage(etype);       
                elements.add(type);        
            }
        }
    }

    private void setType(Vector elements, DigitalObjects digitalObjects, ObjectFactory obj)
    {
        for(ArchDescriptionSubjects subject:digitalObjects.getSubjects())
        {
            String typeS = subject.getSubject().getSubjectTermType();
            if(typeS.equalsIgnoreCase("Genre / Form (655)"))
            {
                ElementType etype = new ElementType();
                etype.setValue(StringHelper.tagRemover(subject.getSubjectTerm()));
                JAXBElement type = obj.createType(etype);       
                elements.add(type);        
            }
        }
    }

    private void setDescription(Vector elements, DigitalObjects digitalObjects, ObjectFactory obj)
    {
        ElementType etype;
        JAXBElement type;
        // updateMap for(ArchDescriptionSubjects subject:digitalObjects.getSubjects())
        // updateMap {
            // updateMap String typeS = subject.getSubject().getSubjectTermType();
            // updateMap if(typeS.equalsIgnoreCase("Curriculum objective (658)"))
            // updateMap {
                // updateMap etype = new ElementType();
                // updateMap etype.setValue(METSExport.tagRemover(subject.getSubjectTerm()));
                // updateMap type = obj.createDescription(etype);       
                // updateMap elements.add(type);        
            // updateMap }
        // updateMap }
        if(digitalObjects.getDigitalInstance()==null)return;
        if(digitalObjects.getDigitalInstance().getResource()==null)return;
        if(digitalObjects.getDigitalInstance().getResource().getRepository()==null)return;
        
        Repositories repos = digitalObjects.getDigitalInstance().getResource().getRepository();
        etype = new ElementType();
        String value = StringHelper.concatenateFields(",",repos.getRepositoryName(),repos.getUrl());
        if(StringHelper.isNotEmpty(value)){
            etype.setValue("Digital Objects made available by "+value);
            type = obj.createDescription(etype);       
            elements.add(type); 
        }
    }
    private void setNoteDescription(Vector elements, DigitalObjects digitalObjects, ObjectFactory obj)
    {
        ElementType etype;
        JAXBElement type;
        for(ArchDescriptionRepeatingData repeatingData:digitalObjects.getRepeatingData())
        {
            if(repeatingData instanceof ArchDescriptionNotes)
            {
                boolean status = ((ArchDescriptionNotes)repeatingData).getInternalOnly().booleanValue() && internalOnly;
                if((repeatingData.getType().equalsIgnoreCase("Abstract")||
                    repeatingData.getType().equalsIgnoreCase("Biographical/Historical note")||
                    repeatingData.getType().equalsIgnoreCase("General note")||
                    repeatingData.getType().equalsIgnoreCase("Scope and Contents note")||
                    repeatingData.getType().equalsIgnoreCase("Preferred Citation note")) && (!status)){
                    etype = new ElementType();
                    etype.setValue(StringHelper.tagRemover(repeatingData.getContent()));
                    type = obj.createDescription(etype);       
                    elements.add(type);        
                }
            }
        }
        /*String value=null;
        if(digitalObjects.getDigitalInstance()!=null){
        value = 
                StringHelper.concatenateFields(" ",digitalObjects.getDigitalInstance().getResource().getRepository().getRepositoryName(),digitalObjects.getDigitalInstance().getResource().getRepository().getUrl());
        }
        
        if(StringHelper.isNotEmpty(value)){
            etype = new ElementType();
            etype.setValue(value);
            type = obj.createDescription(etype);
            elements.add(type);
        }*/
    }
    private void setRights(Vector elements, DigitalObjects digitalObjects, ObjectFactory obj)
    {
        for(ArchDescriptionRepeatingData repeatingData:digitalObjects.getRepeatingData())
        {
            if(repeatingData instanceof ArchDescriptionNotes)
            {
                //boolean status = ((ArchDescriptionNotes)repeatingData).getInternalOnly().booleanValue();
                boolean status = ((ArchDescriptionNotes)repeatingData).getInternalOnly().booleanValue() && internalOnly;
                if((repeatingData.getType().equalsIgnoreCase("Conditions Governing Access note")||
                    repeatingData.getType().equalsIgnoreCase("Conditions Governing Use note") ||
                    repeatingData.getType().equalsIgnoreCase("Legal Status note")) 
                    && (!status)){
                    ElementType etype = new ElementType();
                    etype.setValue(StringHelper.tagRemover(repeatingData.getContent()));
                    JAXBElement type = obj.createRights(etype);       
                    elements.add(type);        
                }
            }
        }
    }
    private void setFormat(Vector elements, DigitalObjects digitalObjects, ObjectFactory obj)
    {
        for(ArchDescriptionRepeatingData repeatingData:digitalObjects.getRepeatingData())
        {
            if(repeatingData instanceof ArchDescriptionNotes)
            {
                //boolean status = ((ArchDescriptionNotes)repeatingData).getInternalOnly().booleanValue();
                 boolean status = ((ArchDescriptionNotes)repeatingData).getInternalOnly().booleanValue() && internalOnly;
                if((repeatingData.getType().equalsIgnoreCase("Dimensions note")||
                   repeatingData.getType().equalsIgnoreCase("General Physical Description note")||
                   repeatingData.getType().equalsIgnoreCase("Material Specific Details note")||
                   repeatingData.getType().equalsIgnoreCase("Physical Characteristics and Technical Requirements note")||
                   repeatingData.getType().equalsIgnoreCase("Physical Facet note")) && (!status)){
                    ElementType etype = new ElementType();
                    etype.setValue(StringHelper.tagRemover(repeatingData.getContent()));
                    JAXBElement type = obj.createFormat(etype);       
                    elements.add(type);        
                }
            }
        }
    }
    private void setSource(Vector elements, DigitalObjects digitalObjects, ObjectFactory obj)
    {
        for(ArchDescriptionRepeatingData repeatingData:digitalObjects.getRepeatingData())
        {
            if(repeatingData instanceof ArchDescriptionNotes)
            {
                //boolean status = ((ArchDescriptionNotes)repeatingData).getInternalOnly().booleanValue();
                 boolean status = ((ArchDescriptionNotes)repeatingData).getInternalOnly().booleanValue() && internalOnly;

                if((repeatingData.getType().equalsIgnoreCase("Existence and Location of Originals note")||repeatingData.getType().equalsIgnoreCase("Immediate Source of Acquisition note")) && (!status)){
                    ElementType etype = new ElementType();
                    etype.setValue(StringHelper.tagRemover(repeatingData.getContent()));
                    JAXBElement type = obj.createSource(etype);       
                    elements.add(type);        
                }
            }
        }
    }

    private void setIdentifiers(Vector elements, DigitalObjects digitalObjects, ObjectFactory obj)
    {
        for(DigitalObjects digitalObject:digitalObjects.getDigitalObjectChildren())
        {
            for(FileVersions fv:digitalObject.getFileVersions())
                handleFileVersions(elements,fv,obj);
                setIdentifiers(elements,digitalObject,obj);
        }
    }

    private void handleFileVersions(Vector elements, FileVersions fv,ObjectFactory obj){
        ElementType eType = new ElementType();;
        JAXBElement type;
        String value=null;
        String use = "";
        use = fv.getUseStatement();
        if(StringHelper.isNotEmpty(use))
            use = "("+use+")";
        value = StringHelper.concatenateFields(" ",fv.getUri(),use);
        if(StringHelper.isNotEmpty(value)){
            eType.setValue(value);
            type = obj.createIdentifier(eType);
            elements.add(type);
       }
    }
    
    private void setRelation(Vector elements, DigitalObjects digitalObjects, ObjectFactory obj, boolean standalone)
    {
        ElementType etype;
        JAXBElement type;
        for(ArchDescriptionRepeatingData repeatingData:digitalObjects.getRepeatingData())
        {
            if(repeatingData instanceof ArchDescriptionNotes)
            {
                //boolean status = ((ArchDescriptionNotes)repeatingData).getInternalOnly().booleanValue();
                 boolean status = ((ArchDescriptionNotes)repeatingData).getInternalOnly().booleanValue() && internalOnly;
                
                if ((repeatingData.getType().equalsIgnoreCase("Related Archival Materials note")) && 
                    (!status)) {
                    etype = new ElementType();
                    etype.setValue(StringHelper.tagRemover(repeatingData.getContent()));
                    type = obj.createRelation(etype);       
                    elements.add(type);        
                }
            }
        }
        
        String value = null;
        if(digitalObjects.getDigitalInstance()!=null){
            DigitalObjectDAO access = new DigitalObjectDAO();
            Resources resource = access.findResourceByDigitalObject(digitalObjects.getDigitalInstance());
            System.out.println("ResourceID"+resource.getResourceId());
            if(resource!=null)
                value = StringHelper.concatenateFields(" ",StringHelper.tagRemover(resource.getTitle()),resource.getEadFaLocation());
        }
        if(StringHelper.isNotEmpty(value)){
            etype = new ElementType();
            etype.setValue(value);
            type = obj.createRelation(etype);
            elements.add(type);
        }
    
        if(standalone){
            handleChildNodes(elements,digitalObjects,obj);
        }
    
    }

    private void handleChildNodes(Vector elements,DigitalObjects dig,ObjectFactory obj){
        ElementType etype;
        JAXBElement type;    
        for(DigitalObjects digObj:dig.getDigitalObjectChildren()){
            String value = StringHelper.tagRemover((digObj.getTitle()));
            etype = new ElementType();
            etype.setValue(value); 
            type = obj.createRelation(etype);
            if(StringHelper.isNotEmpty(value))
                elements.add(type);
            handleChildNodes(elements,digObj,obj); 
        }
    }

}