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

import java.util.List;

import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.archiviststoolkit.model.ArchDescriptionNames;
import org.archiviststoolkit.model.ArchDescriptionNotes;
import org.archiviststoolkit.model.ArchDescriptionSubjects;
import org.archiviststoolkit.model.DigitalObjects;
import org.archiviststoolkit.model.FileVersions;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.model.Subjects;
import org.archiviststoolkit.structure.MODS.AbstractType;
import org.archiviststoolkit.structure.MODS.AccessConditionType;
import org.archiviststoolkit.structure.MODS.CodeOrText;
import org.archiviststoolkit.structure.MODS.DateType;
import org.archiviststoolkit.structure.MODS.GenreType;
import org.archiviststoolkit.structure.MODS.IdentifierType;
import org.archiviststoolkit.structure.MODS.LanguageType;
import org.archiviststoolkit.structure.MODS.ModsType;
import org.archiviststoolkit.structure.MODS.NamePartType;
import org.archiviststoolkit.structure.MODS.NameType;
import org.archiviststoolkit.structure.MODS.NameTypeAttribute;
import org.archiviststoolkit.structure.MODS.ObjectFactory;
import org.archiviststoolkit.structure.MODS.OriginInfoType;
import org.archiviststoolkit.structure.MODS.ResourceType;
import org.archiviststoolkit.structure.MODS.RoleType;
import org.archiviststoolkit.structure.MODS.RoleType.RoleTerm;
import org.archiviststoolkit.structure.MODS.TitleInfoType;
import org.archiviststoolkit.structure.MODS.LanguageType.LanguageTerm;
import org.archiviststoolkit.structure.MODS.LocationType;
import org.archiviststoolkit.structure.MODS.NoteType;
import org.archiviststoolkit.structure.MODS.PhysicalDescriptionType;
import org.archiviststoolkit.structure.MODS.RelatedItemType;
import org.archiviststoolkit.structure.MODS.SubjectType;
import org.archiviststoolkit.structure.MODS.TypeOfResourceType;
import org.archiviststoolkit.structure.MODS.UrlType;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.StringHelper;


public class MODSExport {
    ObjectFactory obj;
    static JAXBContext context;
    File file;
    InfiniteProgressPanel progressPanel;
    boolean internalOnly;
    boolean standalone;
    
    private JAXBElement getModsElement(DigitalObjects digitalObject) {
        obj = new ObjectFactory();
        ModsType modsType = obj.createModsType();
        JAXBElement mods = obj.createMods(modsType);
        List modsGroup = modsType.getModsGroup();
        getModsElements(digitalObject, modsGroup);
        if(standalone){
            for(DigitalObjects dig:digitalObject.getDigitalObjectChildren()){
                modsGroup.add(getRelatedElement(dig));
            }
        }
        return mods;
    }

    private RelatedItemType getRelatedElement(DigitalObjects digitalObject) {
        obj = new ObjectFactory();
        RelatedItemType relatedItem = obj.createRelatedItemType();
        relatedItem.setRiType("constituent");
        List modsGroup = relatedItem.getModsGroup();
        getModsElements(digitalObject, modsGroup);
        for(DigitalObjects dig:digitalObject.getDigitalObjectChildren()){
            modsGroup.add(getRelatedElement(dig));
        }
        return relatedItem;
    }


    public void convertDBRecordToFile(DigitalObjects digitalObject, File file, 
                                      InfiniteProgressPanel progressPanel, 
                                      boolean internalOnly,boolean standalone) {

        this.file = file;
        this.progressPanel = progressPanel;
        this.internalOnly = internalOnly;
        this.standalone=standalone;
        obj = new ObjectFactory();
        JAXBElement mods  = getModsElement(digitalObject);
        
        try {
            context = JAXBContext.newInstance("org.archiviststoolkit.structure.MODS");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            Writer out = new BufferedWriter(osw);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,"http://www.loc.gov/mods/v3 http://www.loc.gov/standards/mods/v3/mods-3-3.xsd");
            m.setProperty("com.sun.xml.bind.namespacePrefixMapper",new NamespacePrefixMapperImpl());            
            m.marshal(mods,out);
            out.close();
        } catch (JAXBException jabe) {
            jabe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public String convertResourceMODSXML(DigitalObjects digitalObject, 
                                      InfiniteProgressPanel progressPanel, 
                                      boolean internalOnly,boolean standalone) {

        this.progressPanel = progressPanel;
        this.internalOnly = internalOnly;
        this.standalone=standalone;
        obj = new ObjectFactory();
        
        //minimum required fields - move to calling method?
        if(!containsValue(digitalObject.getTitle())&&
           !containsValue(digitalObject.getDateExpression())&&
           !containsValue(digitalObject.getDateBegin())&&
           !containsValue(digitalObject.getDateEnd())&&
           !containsValue(digitalObject.getObjectType())&&
           !containsValue(digitalObject.getRepeatingData(ArchDescriptionNotes.class).size())&&
           !containsValue(digitalObject.getNames().size())&&
           !containsValue(digitalObject.getSubjects().size()))
                return "";
        try {
            context = 
                JAXBContext.newInstance("org.archiviststoolkit.structure.MODS");
            JAXBElement mods  = getModsElement(digitalObject);
            StringWriter sw = new StringWriter();
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,"http://www.loc.gov/mods/v3 http://www.loc.gov/standards/mods/v3/mods-3-3.xsd");
            m.setProperty("com.sun.xml.bind.namespacePrefixMapper",new NamespacePrefixMapperImpl());            
            
            m.marshal(mods, sw);
            if(standalone)
                return sw.toString();
            else
                return sw.toString().substring("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>".length()+1);
        } catch (JAXBException jabe) {
            jabe.printStackTrace();
        }
        return "";
    }


    private boolean containsValue(Integer inte){
        if(inte!=null && inte!=0)
            return true;
        else return false;
    }
    public List getModsElements(DigitalObjects digitalObject, List modsGroup) {

        if(StringHelper.isNotEmpty(digitalObject.getTitle())){
            TitleInfoType titleInfo = obj.createTitleInfoType();
            modsGroup.add(titleInfo);
            JAXBElement title = 
                obj.createBaseTitleInfoTypeTitle(digitalObject.getTitle());
                titleInfo.getTitleOrSubTitleOrPartNumber().add(title);
        }
        LanguageType language = obj.createLanguageType();

        LanguageTerm languageTerm = obj.createLanguageTypeLanguageTerm();
        String langCode = digitalObject.getLanguageCode();
        //String lang1=StringHelper.getFirstPartofLangString(langCode,";");
        //String lang2=StringHelper.getSecondPartofLangString(langCode,";");
        String lang2 = LookupListUtils.getLookupListCodeFromItem(DigitalObjects.class,"languageCode",langCode);
        languageTerm.setValue(lang2);
        languageTerm.setType(CodeOrText.CODE);
        languageTerm.setAuthority("iso639-2b");
        if(StringHelper.isNotEmpty(lang2))
            language.getLanguageTerm().add(languageTerm);

        languageTerm = obj.createLanguageTypeLanguageTerm();
        languageTerm.setType(CodeOrText.TEXT);
        languageTerm.setValue(langCode);
        if(StringHelper.isNotEmpty(langCode))
            language.getLanguageTerm().add(languageTerm);
        if(language.getLanguageTerm().size()>0)
            modsGroup.add(language);

        TypeOfResourceType typeOfResource = obj.createTypeOfResourceType();
        try{
            typeOfResource.setValue(ResourceType.fromValue(digitalObject.getObjectType()));
            modsGroup.add(typeOfResource);
        }
        catch (IllegalArgumentException iae){
            System.out.println("Not Valid Image Type:"+digitalObject.getObjectType());
        }

        OriginInfoType originInfo = obj.createOriginInfoType();
        boolean makeOriginInfo=false;
        DateType dateType = obj.createDateType();
        
        if (containsValue(digitalObject.getDateExpression())) {
            dateType.setValue(digitalObject.getDateExpression());
            JAXBElement dateCreated = 
                obj.createOriginInfoTypeDateCreated(dateType);
            originInfo.getPlaceOrPublisherOrDateIssued().add(dateCreated);
            makeOriginInfo=true;
        }

        if (digitalObject.getDateBegin() != null && !(digitalObject.getDateBegin().equals(0))) {
            dateType = obj.createDateType();
            dateType.setPoint("start");
            dateType.setValue(digitalObject.getDateBegin() + "");
            JAXBElement dateCreated = 
                obj.createOriginInfoTypeDateCreated(dateType);
            originInfo.getPlaceOrPublisherOrDateIssued().add(dateCreated);
            makeOriginInfo=true;

        }

        if (digitalObject.getDateEnd() != null && !(digitalObject.getDateBegin().equals(0))) {
            dateType = obj.createDateType();
            dateType.setPoint("end");
            dateType.setValue(digitalObject.getDateEnd() + "");
            JAXBElement dateCreated = 
                obj.createOriginInfoTypeDateCreated(dateType);
            originInfo.getPlaceOrPublisherOrDateIssued().add(dateCreated);
            makeOriginInfo=true;

        }

        if(makeOriginInfo)
            modsGroup.add(originInfo);


        handleNames(digitalObject.getNamesForPrinting(), modsGroup);
        handleSubject(digitalObject.getSubjectsForPrinting(), modsGroup);
        handleNotes(digitalObject.getRepeatingData(ArchDescriptionNotes.class),modsGroup);
        if(digitalObject.getParent()==null)
            createRelatedItemforResource(digitalObject,modsGroup);
        if(standalone)
            handleFileVersions(digitalObject.getFileVersions(),modsGroup);
        return modsGroup;
    }


    private boolean isNoteTitle(ArchDescriptionNotes note, String value) {
  
        if (note.getNotesEtcType().getNotesEtcName().equals(value)){
            return true;
        }
        else{
            return false;
        }
    }

    private void handleFileVersions(Set<FileVersions> fileVersions,List modsGroup){
        for(FileVersions fileVersion:fileVersions){
            IdentifierType identifier = obj.createIdentifierType();
            if(containsValue(fileVersion.getUri()))
                identifier.setValue(fileVersion.getUri());
            if(containsValue(fileVersion.getUseStatement()))
                identifier.setDisplayLabel(fileVersion.getUseStatement());
                //identifier.setValue(identifier.getValue()+"("+fileVersion.getUseStatement()+")");
            if(containsValue(fileVersion.getUri())||containsValue(fileVersion.getUseStatement()))
                modsGroup.add(identifier);
        }
    }
    private void handleNotes(Set<ArchDescriptionNotes> repeatingData, List modsGroup) {
        PhysicalDescriptionType physDesc = obj.createPhysicalDescriptionType();
        boolean pdesc = false;
       
        for (ArchDescriptionNotes note: repeatingData) {
        	if(!note.getInternalOnly() || !internalOnly){
            if (isNoteTitle(note, "Abstract")) {
                AbstractType abstracT = obj.createAbstractType();
                abstracT.setDisplayLabel(note.getTitle());
                if(!(StringHelper.isNotEmpty(note.getTitle())))
                    abstracT.setDisplayLabel(note.getType());
                abstracT.setValue(note.getContent());
                modsGroup.add(abstracT);

            } else if (isNoteTitle(note, "Scope and Contents note")) {
                AbstractType abstracT = obj.createAbstractType();
                abstracT.setDisplayLabel(note.getTitle());
                if(!(StringHelper.isNotEmpty(note.getTitle())))
                    abstracT.setDisplayLabel(note.getType());
                abstracT.setValue(note.getContent());
                modsGroup.add(abstracT);
            } else if (isNoteTitle(note, "Biographical/Historical note")) {
                NoteType noteM = obj.createNoteType();
                noteM.setDisplayLabel(note.getTitle());
                if(!(StringHelper.isNotEmpty(note.getTitle())))
                    noteM.setDisplayLabel(note.getType());
                noteM.setValue(note.getContent());
                modsGroup.add(noteM);
            } else if (isNoteTitle(note, "General note")) {
                NoteType noteM = obj.createNoteType();
                noteM.setDisplayLabel(note.getTitle());
                if(!(StringHelper.isNotEmpty(note.getTitle())))
                    noteM.setDisplayLabel(note.getType());
                noteM.setValue(note.getContent());
                modsGroup.add(noteM);

            } else if (isNoteTitle(note, 
                                   "Immediate Source of Acquisition note")) {
                NoteType noteM = obj.createNoteType();
                noteM.setUtType("acquisition");
                noteM.setDisplayLabel(note.getTitle());
                if(!(StringHelper.isNotEmpty(note.getTitle())))
                    noteM.setDisplayLabel(note.getType());
                noteM.setValue(note.getContent());
                modsGroup.add(noteM);

            } else if (isNoteTitle(note, "Preferred Citation note")) {
                NoteType noteM = obj.createNoteType();
                noteM.setUtType("citation");
                noteM.setDisplayLabel(note.getTitle());
                if(!(StringHelper.isNotEmpty(note.getTitle())))
                    noteM.setDisplayLabel(note.getType());
                noteM.setValue(note.getContent());
                modsGroup.add(noteM);
            } else if (isNoteTitle(note, "Conditions Governing Access note")) {
                AccessConditionType noteM = obj.createAccessConditionType();
                noteM.setUtType("restrictionOnAccess");
                noteM.setDisplayLabel(note.getTitle());
                if(!(StringHelper.isNotEmpty(note.getTitle())))
                    noteM.setDisplayLabel(note.getType());
                noteM.setValue(note.getContent());
                modsGroup.add(noteM);
            } else if (isNoteTitle(note, "Conditions Governing Use note")) {
                AccessConditionType noteM = obj.createAccessConditionType();
                noteM.setUtType("useAndReproduction");
                noteM.setDisplayLabel(note.getTitle());
                if(!(StringHelper.isNotEmpty(note.getTitle())))
                    noteM.setDisplayLabel(note.getType());
                noteM.setValue(note.getContent());
                modsGroup.add(noteM);
            } else if (isNoteTitle(note, "Legal Status note")) {
                AccessConditionType noteM = obj.createAccessConditionType();
                noteM.setDisplayLabel(note.getTitle());
                if(!(StringHelper.isNotEmpty(note.getTitle())))
                    noteM.setDisplayLabel(note.getType());
                noteM.setValue(note.getContent());
                modsGroup.add(noteM);
            } else if (isNoteTitle(note, "Dimensions note")) {
                JAXBElement extent = 
                    obj.createPhysicalDescriptionTypeExtent(note.getContent());
                physDesc.getFormOrReformattingQualityOrInternetMediaType().add(extent);
                if (!pdesc)
                    modsGroup.add(physDesc);
                pdesc = true;
            } else if (isNoteTitle(note, 
                                   "General Physical Description note")) {
                NoteType noteM = obj.createNoteType();
                noteM.setDisplayLabel(note.getTitle());
                if(!(StringHelper.isNotEmpty(note.getTitle())))
                    noteM.setDisplayLabel(note.getType());
                noteM.setValue(note.getContent());
                physDesc.getFormOrReformattingQualityOrInternetMediaType().add(obj.createPhysicalDescriptionTypeNote(noteM));
                if(!pdesc)
                    modsGroup.add(physDesc);
                pdesc = true;
            } else if (isNoteTitle(note, "Material Specific Details note")) {
                NoteType noteM = obj.createNoteType();
                noteM.setDisplayLabel(note.getTitle());
                if(!(StringHelper.isNotEmpty(note.getTitle())))
                    noteM.setDisplayLabel(note.getType());
                noteM.setValue(note.getContent());
                physDesc.getFormOrReformattingQualityOrInternetMediaType().add(obj.createPhysicalDescriptionTypeNote(noteM));
                if (!pdesc)
                    modsGroup.add(physDesc);
                pdesc = true;    
            } else if (isNoteTitle(note, 
                                   "Physical Characteristics and Technical Requirements note")) {
                NoteType noteM = obj.createNoteType();
                noteM.setDisplayLabel(note.getTitle());
                if(!(StringHelper.isNotEmpty(note.getTitle())))
                    noteM.setDisplayLabel(note.getType());
                noteM.setValue(note.getContent());
                physDesc.getFormOrReformattingQualityOrInternetMediaType().add(obj.createPhysicalDescriptionTypeNote(noteM));
                if (!pdesc)
                    modsGroup.add(physDesc);
                pdesc = true;

            } else if (isNoteTitle(note, "Physical Facet note")) {
                NoteType noteM = obj.createNoteType();
                noteM.setDisplayLabel(note.getTitle());
                if(!(StringHelper.isNotEmpty(note.getTitle())))
                    noteM.setDisplayLabel(note.getType());
                noteM.setValue(note.getContent());
                physDesc.getFormOrReformattingQualityOrInternetMediaType().add(obj.createPhysicalDescriptionTypeNote(noteM));
                if (!pdesc)
                    modsGroup.add(physDesc);
                pdesc = true;
            } else if (isNoteTitle(note, 
                                   "Existence and Location of Copies note")) {
                RelatedItemType relatedItem = obj.createRelatedItemType();
                relatedItem.setRiType("otherFormat");
                NoteType noteM = obj.createNoteType();
                noteM.setDisplayLabel(note.getTitle());
                if(!(StringHelper.isNotEmpty(note.getTitle())))
                    noteM.setDisplayLabel(note.getType());
                noteM.setValue(note.getContent());
                relatedItem.getModsGroup().add(noteM);
                modsGroup.add(relatedItem);
            } else if (isNoteTitle(note, 
                                   "Existence and Location of Originals note")) {
                RelatedItemType relatedItem = obj.createRelatedItemType();
                relatedItem.setRiType("original");
                NoteType noteM = obj.createNoteType();
                noteM.setDisplayLabel(note.getTitle());
                if(!(StringHelper.isNotEmpty(note.getTitle())))
                    noteM.setDisplayLabel(note.getType());
                noteM.setValue(note.getContent());
                relatedItem.getModsGroup().add(noteM);
                modsGroup.add(relatedItem);
            } else if (isNoteTitle(note, "Related Archival Materials note")) {
                RelatedItemType relatedItem = obj.createRelatedItemType();
                //relatedItem.setRiType("otherFormat");
                NoteType noteM = obj.createNoteType();
                noteM.setDisplayLabel(note.getTitle());
                if(!(StringHelper.isNotEmpty(note.getTitle())))
                    noteM.setDisplayLabel(note.getType());
                noteM.setValue(note.getContent());
                relatedItem.getModsGroup().add(noteM);
                modsGroup.add(relatedItem);

            }
        }
        }
    }

    private void createRelatedItemforResource(DigitalObjects dig, List modsGroup) {
        if(dig.getDigitalInstance()==null)
            return;
        Resources resource = dig.getDigitalInstance().getResource();
        if(resource==null){
            ResourcesComponents rc  = dig.getDigitalInstance().getResourceComponent();
            if (rc!=null)
            	resource = MARCExport.getResourceFromResourceComponent(rc);
        }
        if(resource==null)
        	return;
        
        if (resource.getLevel().equals("item")) {
            RelatedItemType relatedItem = obj.createRelatedItemType();
            relatedItem.setRiType("original");
            IdentifierType identifier = obj.createIdentifierType();
            identifier.setType("local");
            identifier.setValue(resource.getResourceIdentifier());
            relatedItem.getModsGroup().add(identifier);
            if (containsValue(resource.getEadFaLocation())) {
                LocationType location = obj.createLocationType();
                UrlType url = obj.createUrlType();
                url.setValue(resource.getEadFaLocation());
                location.getUrl().add(url);
                relatedItem.getModsGroup().add(location);
            }
            modsGroup.add(relatedItem);
        } else if (resource.getLevel().equals("collection") || 
                   resource.getLevel().equals("fonds") || 
                   resource.getLevel().equals("recordgrp")) {
            RelatedItemType relatedItem = obj.createRelatedItemType();
            relatedItem.setRiType("host");
            modsGroup.add(relatedItem);
            if (containsValue(resource.getTitle())) {
                TitleInfoType titleInfo = obj.createTitleInfoType();
                JAXBElement title = 
                    obj.createBaseTitleInfoTypeTitle(resource.getTitle());
                titleInfo.getTitleOrSubTitleOrPartNumber().add(title);
                relatedItem.getModsGroup().add(titleInfo);
            }

            OriginInfoType originInfo = obj.createOriginInfoType();
            relatedItem.getModsGroup().add(originInfo);
            DateType dateType = obj.createDateType();

            if (containsValue(resource.getDateExpression())) {
                dateType.setValue(resource.getDateExpression());
                JAXBElement dateCreated = 
                    obj.createOriginInfoTypeDateCreated(dateType);
                originInfo.getPlaceOrPublisherOrDateIssued().add(dateCreated);
            }

            if (resource.getDateBegin() != null) {
                dateType = obj.createDateType();
                dateType.setPoint("start");
                dateType.setValue(resource.getDateBegin() + "");
                JAXBElement dateCreated = 
                    obj.createOriginInfoTypeDateCreated(dateType);
                originInfo.getPlaceOrPublisherOrDateIssued().add(dateCreated);
            }

            if (resource.getDateEnd() != null) {
                dateType = obj.createDateType();
                dateType.setPoint("end");
                dateType.setValue(resource.getDateEnd() + "");
                JAXBElement dateCreated = 
                    obj.createOriginInfoTypeDateCreated(dateType);
                originInfo.getPlaceOrPublisherOrDateIssued().add(dateCreated);
            }
            IdentifierType identifier = obj.createIdentifierType();
            identifier.setType("local");
            identifier.setValue(resource.getResourceIdentifier());
            relatedItem.getModsGroup().add(identifier);

            if (containsValue(resource.getEadFaLocation())) {
                LocationType location = obj.createLocationType();
                UrlType url = obj.createUrlType();
                url.setValue(resource.getEadFaLocation());
                location.getUrl().add(url);
                relatedItem.getModsGroup().add(location);
            }

            NoteType note = obj.createNoteType();
            String url = "";
            if(StringHelper.isNotEmpty(resource.getRepository().getUrl()))
                url = "("+resource.getRepository().getUrl()+")";
                
                
            String value = StringHelper.concatenateFields(", ",
                                           resource.getRepository().getRepositoryName(), 
                                           resource.getRepository().getAddress1(), 
                                           resource.getRepository().getAddress2(), 
                                           resource.getRepository().getAddress3(), 
                                           resource.getRepository().getCity(), 
                                           resource.getRepository().getRegion(), 
                                           resource.getRepository().getMailCode(), 
                                           resource.getRepository().getCountry(), 
                                           url);
            note.setDisplayLabel("Digital object made available by ");

            if(StringHelper.isNotEmpty(value)){
                note.setValue(value);
                modsGroup.add(note);
            }
        }
    }

    private void handleNames(Set<ArchDescriptionNames> anames, 
                             List modsGroup) {
        for (ArchDescriptionNames aname: anames) {
            // update to map if (aname.getNameLinkFunction().equals("Creator") ||
            // update   aname.getNameLinkFunction().equals("Source")) {
            if (aname.getNameLinkFunction().equals("Creator")){
                NameType name = buildName(aname);
                modsGroup.add(name);
            }

            else if (aname.getNameLinkFunction().equals("Subject")) {
                SubjectType subjectType = new SubjectType();
                JAXBElement topic = obj.createSubjectTypeTopic(aname.getForm());
                NameType name = buildName(aname);
                subjectType.getTopicOrGeographicOrTemporal().add(obj.createSubjectTypeName(name));
                if(StringHelper.isNotEmpty(aname.getForm()))
                    subjectType.getTopicOrGeographicOrTemporal().add(topic);
                modsGroup.add(subjectType);
            }
        }
    }

    private boolean isSubjectTermType(ArchDescriptionSubjects subject, 
                                      String value) {
        if (subject.getSubject().getSubjectTermType().equals(value))
            return true;
        else
            return false;
    }

    private boolean containsValue(String value) {
        if (value != null && value.length() > 0)
            return true;
        else
            return false;
    }

    private void handleSubject(Set<ArchDescriptionSubjects> aSubjects, 
                               List modsGroup) {
        for (ArchDescriptionSubjects aSubject: aSubjects) {
            String subjectAbbrev = LookupListUtils.getLookupListCodeFromItem(Subjects.class,"subjectSource",aSubject.getSubjectSource());
            if(StringHelper.isEmpty(subjectAbbrev))
                subjectAbbrev=null;

            if (isSubjectTermType(aSubject, "Uniform Title (630)")) {
                SubjectType subject = obj.createSubjectType();
                subject.setAuthority(subjectAbbrev);
                TitleInfoType titleInfo = new TitleInfoType();
                titleInfo.getTitleOrSubTitleOrPartNumber().add(obj.createBaseTitleInfoTypeTitle(aSubject.getSubject().getSubjectTerm()));
                subject.getTopicOrGeographicOrTemporal().add(obj.createSubjectTypeTitleInfo(titleInfo));
                modsGroup.add(subject);
            }
            if (isSubjectTermType(aSubject, "Topical Term (650)")) {
                SubjectType subject = obj.createSubjectType();
                subject.setAuthority(subjectAbbrev);
                subject.getTopicOrGeographicOrTemporal().add(obj.createSubjectTypeTopic(aSubject.getSubject().getSubjectTerm()));
                modsGroup.add(subject);
            }
            if (isSubjectTermType(aSubject, "Function (657)")) {
                SubjectType subject = obj.createSubjectType();
                subject.getTopicOrGeographicOrTemporal().add(obj.createSubjectTypeTopic(aSubject.getSubject().getSubjectTerm()));
                modsGroup.add(subject);
            }
            if (isSubjectTermType(aSubject, "Geographic Name (651)")) {
                SubjectType subject = obj.createSubjectType();
                subject.setAuthority(subjectAbbrev);
                subject.getTopicOrGeographicOrTemporal().add(obj.createSubjectTypeGeographic(aSubject.getSubject().getSubjectTerm()));
                modsGroup.add(subject);

            }
            if (isSubjectTermType(aSubject, "Occupation (656)")) {
                SubjectType subject = obj.createSubjectType();
                subject.setAuthority(subjectAbbrev);
                subject.getTopicOrGeographicOrTemporal().add(obj.createSubjectTypeOccupation(aSubject.getSubject().getSubjectTerm()));
                modsGroup.add(subject);
            }

            if (isSubjectTermType(aSubject, "Genre / Form (655)")) {
                GenreType genre = obj.createGenreType();
                genre.setAuthority(subjectAbbrev);
                genre.setValue(aSubject.getSubject().getSubjectTerm());
                modsGroup.add(genre);

            }
            // update map if (isSubjectTermType(aSubject, "Curriculum Objective (658)")) {
            // update map    TargetAudienceType target = obj.createTargetAudienceType();
            // update map    target.setAuthority(subjectAbbrev);
            // update map    target.setValue(aSubject.getSubject().getSubjectTerm());
            // update map    modsGroup.add(target);
            // update map}

        }
    }

    private void setRoleForNameTypes(ArchDescriptionNames aname, NameType name){
        RoleType roleType = obj.createRoleType();
        
        String fullRoleName = "";
        String abbrevRoleName = "";
        String role[];
        role = StringHelper.extractStringAndValueInParenthesis(aname.getRole());
        System.out.println("role - "+aname.getRole());
        if(role==null){
            if(aname.getNameLinkFunction().equals("Creator")){
                fullRoleName = "creator";
                abbrevRoleName = "cre";

            }
            else if(aname.getNameLinkFunction().equals("Source")){
                fullRoleName = "donor";
                abbrevRoleName = "dnr";
            }
        } 
         else{
             fullRoleName = role[0];
             abbrevRoleName = role[1];
         }
            
        
            
            
            
        RoleTerm roleTerm = obj.createRoleTypeRoleTerm();
        roleType.getRoleTerm().add(roleTerm);
        roleTerm.setType(CodeOrText.fromValue("text"));
        roleTerm.setAuthority("marcrelator");
        roleTerm.setValue(fullRoleName);

        roleTerm = obj.createRoleTypeRoleTerm();
        roleType.getRoleTerm().add(roleTerm);
        roleTerm.setType(CodeOrText.fromValue("code"));
        roleTerm.setAuthority("marcrelator");
        roleTerm.setValue(abbrevRoleName);
        
        if(StringHelper.isNotEmpty(fullRoleName) && StringHelper.isNotEmpty(abbrevRoleName))
            name.getNamePartOrDisplayFormOrAffiliation().add(obj.createNameTypeRole(roleType));

        //name.getNamePartOrDisplayFormOrAffiliation().add(obj.createNameTypeRole(roleType));

        
    }

    private NameType buildName(ArchDescriptionNames anames) {
        NameType name = obj.createNameType();

        String type = anames.getName().getNameType();
        Names atName = anames.getName();
        String nameSource = LookupListUtils.getLookupListCodeFromItem(Names.class,"nameSource",atName.getNameSource());
        if(StringHelper.isEmpty(nameSource))
            nameSource=null;

        if (type.equals("Person")) {
            name.setNtType(NameTypeAttribute.fromValue("personal"));
            name.setAuthority(nameSource);
            setRoleForNameTypes(anames,name);
            NamePartType namePart = obj.createNamePartType();
            namePart.setValue(anames.getName().getPersonalPrimaryName());
            namePart.setType("family");
            if(StringHelper.isNotEmpty(namePart.getValue()))
                name.getNamePartOrDisplayFormOrAffiliation().add(obj.createNameTypeNamePart(namePart));

            namePart = obj.createNamePartType();
            namePart.setValue(anames.getName().getPersonalRestOfName());
            namePart.setType("given");
            if(StringHelper.isNotEmpty(namePart.getValue()))
                name.getNamePartOrDisplayFormOrAffiliation().add(obj.createNameTypeNamePart(namePart));

            namePart = obj.createNamePartType();
            namePart.setValue(StringHelper.concatenateFields(", ", 
                                                             atName.getPersonalPrefix(), 
                                                             atName.getPersonalTitle(), 
                                                             atName.getPersonalSuffix(), 
                                                             atName.getNumber()));
            namePart.setType("termsOfAddress");
            if(StringHelper.isNotEmpty(namePart.getValue()))
                name.getNamePartOrDisplayFormOrAffiliation().add(obj.createNameTypeNamePart(namePart));

            namePart = obj.createNamePartType();
            namePart.setValue(atName.getPersonalDates());
            namePart.setType("date");
            if(StringHelper.isNotEmpty(namePart.getValue()))
                name.getNamePartOrDisplayFormOrAffiliation().add(obj.createNameTypeNamePart(namePart));

        }

        if (type.equals("Family")) {
            name.setNtType(NameTypeAttribute.fromValue("personal"));
            name.setAuthority(nameSource);
            
            setRoleForNameTypes(anames,name);
            
            NamePartType namePart = obj.createNamePartType();
            namePart.setValue(atName.getFamilyName());
            namePart.setType("family");
            if(StringHelper.isNotEmpty(namePart.getValue()))
                name.getNamePartOrDisplayFormOrAffiliation().add(obj.createNameTypeNamePart(namePart));

            namePart = obj.createNamePartType();
            namePart.setValue(atName.getFamilyNamePrefix());
            namePart.setType("termsOfAddress");
            if(StringHelper.isNotEmpty(namePart.getValue()))
                name.getNamePartOrDisplayFormOrAffiliation().add(obj.createNameTypeNamePart(namePart));
        }

        if (type.equals("Corporate Body")) {
            name.setNtType(NameTypeAttribute.fromValue("corporate"));
            name.setAuthority(nameSource);

            setRoleForNameTypes(anames,name);
            
            NamePartType namePart = obj.createNamePartType();
            namePart.setValue(atName.getCorporatePrimaryName());
            if(StringHelper.isNotEmpty(namePart.getValue()))
                name.getNamePartOrDisplayFormOrAffiliation().add(obj.createNameTypeNamePart(namePart));

            namePart = obj.createNamePartType();
            namePart.setValue(atName.getCorporateSubordinate1());
            if(StringHelper.isNotEmpty(namePart.getValue()))
                name.getNamePartOrDisplayFormOrAffiliation().add(obj.createNameTypeNamePart(namePart));

            namePart = obj.createNamePartType();
            namePart.setValue(atName.getCorporateSubordinate2());
            if(StringHelper.isNotEmpty(namePart.getValue()))
                name.getNamePartOrDisplayFormOrAffiliation().add(obj.createNameTypeNamePart(namePart));

            namePart = obj.createNamePartType();
            namePart.setValue(atName.getNumber());
            if(StringHelper.isNotEmpty(namePart.getValue()))
            name.getNamePartOrDisplayFormOrAffiliation().add(obj.createNameTypeNamePart(namePart));


        }

        if(StringHelper.isNotEmpty(atName.getQualifier())){
            JAXBElement description = obj.createNameTypeDescription(atName.getQualifier());
            name.getNamePartOrDisplayFormOrAffiliation().add(description);
        }
        if(StringHelper.isNotEmpty(atName.getSortName())){      
            JAXBElement displayForm = obj.createNameTypeDisplayForm(atName.getSortName());
            name.getNamePartOrDisplayFormOrAffiliation().add(displayForm);
        }
        return name;


    }

    public static void main(String[] args) {
        //MODSExport modsExport = new MODSExport();
        //modsExport.convertDBRecordToXML(new DigitalObjects(), true);
        /*Resources res = new Resources();
        ArchDescriptionNames anames = new ArchDescriptionNames();
        anames.setForm("form");

        HashMap map = new HashMap();
        map.put("names",anames);

        BeanUtilsBean beanUtils = new BeanUtilsBean();
        try{
            beanUtils.populate(res,map);
            System.out.println(res.getName(0).getForm());
        }
        catch (IllegalAccessException iae)
        {
            iae.printStackTrace();
        }
        catch (InvocationTargetException iae)
        {
            iae.printStackTrace();
        }*/
    }
}
