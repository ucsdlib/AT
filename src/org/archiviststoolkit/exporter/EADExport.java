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

package

org.archiviststoolkit.exporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import java.util.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import javax.xml.bind.Unmarshaller;

import javax.xml.transform.stream.StreamSource;

import org.archiviststoolkit.importer.EADHelper;
import org.archiviststoolkit.importer.HandleDidAction;
import org.archiviststoolkit.model.ArchDescriptionAnalogInstances;
import org.archiviststoolkit.model.ArchDescriptionDigitalInstances;
import org.archiviststoolkit.model.ArchDescriptionNames;
import org.archiviststoolkit.model.ArchDescriptionNotes;
import org.archiviststoolkit.model.ArchDescriptionRepeatingData;
import org.archiviststoolkit.model.ArchDescriptionStructuredDataItems;
import org.archiviststoolkit.model.ArchDescriptionSubjects;
import org.archiviststoolkit.model.BibItems;
import org.archiviststoolkit.model.Bibliography;
import org.archiviststoolkit.model.ChronologyItems;
import org.archiviststoolkit.model.ChronologyList;
import org.archiviststoolkit.model.DigitalObjects;
import org.archiviststoolkit.model.Events;
import org.archiviststoolkit.model.FileVersions;
import org.archiviststoolkit.model.Index;
import org.archiviststoolkit.model.IndexItems;
import org.archiviststoolkit.model.ListDefinition;
import org.archiviststoolkit.model.ListDefinitionItems;
import org.archiviststoolkit.model.ListOrdered;
import org.archiviststoolkit.model.ListOrderedItems;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.ResourcesCommon;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.model.Subjects;
import org.archiviststoolkit.structure.EAD.Abstract;
import org.archiviststoolkit.structure.EAD.Accessrestrict;
import org.archiviststoolkit.structure.EAD.Accruals;
import org.archiviststoolkit.structure.EAD.Acqinfo;
import org.archiviststoolkit.structure.EAD.Address;
import org.archiviststoolkit.structure.EAD.Addressline;
import org.archiviststoolkit.structure.EAD.Altformavail;
import org.archiviststoolkit.structure.EAD.Appraisal;
import org.archiviststoolkit.structure.EAD.Archdesc;
import org.archiviststoolkit.structure.EAD.Arrangement;
import org.archiviststoolkit.structure.EAD.Author;
import org.archiviststoolkit.structure.EAD.AvLevel;
import org.archiviststoolkit.structure.EAD.Bibref;
import org.archiviststoolkit.structure.EAD.Bioghist;
import org.archiviststoolkit.structure.EAD.C;
import org.archiviststoolkit.structure.EAD.C01;
import org.archiviststoolkit.structure.EAD.C02;
import org.archiviststoolkit.structure.EAD.C03;
import org.archiviststoolkit.structure.EAD.C04;
import org.archiviststoolkit.structure.EAD.C05;
import org.archiviststoolkit.structure.EAD.C06;
import org.archiviststoolkit.structure.EAD.C07;
import org.archiviststoolkit.structure.EAD.C08;
import org.archiviststoolkit.structure.EAD.C09;
import org.archiviststoolkit.structure.EAD.C10;
import org.archiviststoolkit.structure.EAD.C11;
import org.archiviststoolkit.structure.EAD.C12;
import org.archiviststoolkit.structure.EAD.Change;
import org.archiviststoolkit.structure.EAD.Chronitem;
import org.archiviststoolkit.structure.EAD.Chronlist;
import org.archiviststoolkit.structure.EAD.Container;
import org.archiviststoolkit.structure.EAD.Controlaccess;
import org.archiviststoolkit.structure.EAD.Corpname;
import org.archiviststoolkit.structure.EAD.Creation;
import org.archiviststoolkit.structure.EAD.Custodhist;
import org.archiviststoolkit.structure.EAD.Dao;
import org.archiviststoolkit.structure.EAD.Daodesc;
import org.archiviststoolkit.structure.EAD.Date;
import org.archiviststoolkit.structure.EAD.Defitem;
import org.archiviststoolkit.structure.EAD.Descrules;
import org.archiviststoolkit.structure.EAD.Did;
import org.archiviststoolkit.structure.EAD.Dimensions;
import org.archiviststoolkit.structure.EAD.Dsc;
import org.archiviststoolkit.structure.EAD.Ead;
import org.archiviststoolkit.structure.EAD.Eadid;
import org.archiviststoolkit.structure.EAD.Eadheader;

import org.archiviststoolkit.structure.EAD.Editionstmt;
import org.archiviststoolkit.structure.EAD.Event;
import org.archiviststoolkit.structure.EAD.Eventgrp;
import org.archiviststoolkit.structure.EAD.Extent;
import org.archiviststoolkit.structure.EAD.Extref;
import org.archiviststoolkit.structure.EAD.Famname;
import org.archiviststoolkit.structure.EAD.Filedesc;
import org.archiviststoolkit.structure.EAD.Fileplan;
import org.archiviststoolkit.structure.EAD.Function;
import org.archiviststoolkit.structure.EAD.Genreform;
import org.archiviststoolkit.structure.EAD.Geogname;
import org.archiviststoolkit.structure.EAD.Head;
import org.archiviststoolkit.structure.EAD.Indexentry;
import org.archiviststoolkit.structure.EAD.Item;
import org.archiviststoolkit.structure.EAD.Label;
import org.archiviststoolkit.structure.EAD.Langmaterial;
import org.archiviststoolkit.structure.EAD.Language;
import org.archiviststoolkit.structure.EAD.Langusage;
import org.archiviststoolkit.structure.EAD.Legalstatus;
import org.archiviststoolkit.structure.EAD.Materialspec;
import org.archiviststoolkit.structure.EAD.Name;
import org.archiviststoolkit.structure.EAD.Note;
import org.archiviststoolkit.structure.EAD.Notestmt;
import org.archiviststoolkit.structure.EAD.Num;
import org.archiviststoolkit.structure.EAD.ObjectFactory;
import org.archiviststoolkit.structure.EAD.Occupation;
import org.archiviststoolkit.structure.EAD.Odd;
import org.archiviststoolkit.structure.EAD.Originalsloc;
import org.archiviststoolkit.structure.EAD.Origination;
import org.archiviststoolkit.structure.EAD.Otherfindaid;
import org.archiviststoolkit.structure.EAD.P;
import org.archiviststoolkit.structure.EAD.Persname;
import org.archiviststoolkit.structure.EAD.Physdesc;
import org.archiviststoolkit.structure.EAD.Physfacet;
import org.archiviststoolkit.structure.EAD.Physloc;
import org.archiviststoolkit.structure.EAD.Phystech;
import org.archiviststoolkit.structure.EAD.Prefercite;
import org.archiviststoolkit.structure.EAD.Processinfo;
import org.archiviststoolkit.structure.EAD.Profiledesc;
import org.archiviststoolkit.structure.EAD.Publicationstmt;
import org.archiviststoolkit.structure.EAD.Publisher;
import org.archiviststoolkit.structure.EAD.Ref;
import org.archiviststoolkit.structure.EAD.Relatedmaterial;
import org.archiviststoolkit.structure.EAD.Repository;
import org.archiviststoolkit.structure.EAD.Revisiondesc;
import org.archiviststoolkit.structure.EAD.Scopecontent;
import org.archiviststoolkit.structure.EAD.Separatedmaterial;
import org.archiviststoolkit.structure.EAD.Seriesstmt;
import org.archiviststoolkit.structure.EAD.Sponsor;
import org.archiviststoolkit.structure.EAD.Subject;
import org.archiviststoolkit.structure.EAD.Subtitle;
import org.archiviststoolkit.structure.EAD.Title;
import org.archiviststoolkit.structure.EAD.Titleproper;
import org.archiviststoolkit.structure.EAD.Titlestmt;
import org.archiviststoolkit.structure.EAD.Unitdate;
import org.archiviststoolkit.structure.EAD.Unitid;
import org.archiviststoolkit.structure.EAD.Unittitle;
import org.archiviststoolkit.structure.EAD.Userestrict;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

import org.archiviststoolkit.util.CharacterConvert;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.StringHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class EADExport {
    static JAXBContext context = null;
    public static int aa;
    public static int levelC;
    public static boolean numberedCs;
    public static boolean suppressElements;
    public static boolean includeDaos;
    public static boolean useDOIDAsHREF = false; // specifies whether to use the Digital Object Id aka metsId as the HREF for DAO export
    public static ObjectFactory ob;

	private static int depth = 2;
	private static Physdesc physDesc;
    public String convertResourceToXML(Resources resource,
                                       InfiniteProgressPanel progressPanel,
                                       boolean internalOnly,
                                       boolean includeDaos, boolean suppressNameSpace, boolean useDOIDAsHREF) {

        this.useDOIDAsHREF = useDOIDAsHREF;

        try {
            if (context == null) {
                context = 
                        JAXBContext.newInstance("org.archiviststoolkit.structure.EAD");
              
            }
            Ead ead = 
                convertResourceToEadElement(resource, progressPanel, internalOnly, 
                                            true, includeDaos);
            StringWriter sw = new StringWriter();
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(ead, sw);
            if(suppressNameSpace){
                return sw.toString().replace("xmlns=\"urn:isbn:1-931666-22-9\"",""); 
            }
            else{
                return sw.toString();
            }
        } catch (JAXBException jabe) {
            jabe.printStackTrace();
        }

        return "";
    }

    public void convertResourceToFile(Resources resource, File file, 
                                      InfiniteProgressPanel progressPanel, 
                                      boolean internalOnly, boolean numberedCs, 
                                      boolean includeDaos, boolean useDOIDAsHREF) throws Exception{

        this.useDOIDAsHREF = useDOIDAsHREF;

        try {
            context = 
                    JAXBContext.newInstance("org.archiviststoolkit.structure.EAD");
            Ead ead = 
                convertResourceToEadElement(resource, progressPanel, internalOnly, 
                                            numberedCs, includeDaos);

            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            Writer out = new BufferedWriter(osw);

            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, 
                          "urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd");

            m.marshal(ead, out);
            out.close();

            /*String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+"\n";
             header = header+"<!DOCTYPE ead PUBLIC \"+//ISBN 1-931666-00-8//DTD ead.dtd (Encoded Archival Description (EAD) Version 2002)//EN\" \"ead.dtd\">";
	     StringWriter sw = new StringWriter();
	     Marshaller m = context.createMarshaller();
	     m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
	     m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	     m.marshal(ead, sw);
	     out.write(sw.toString().replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>",header));
             out.close();*/


            progressPanel.setTextLine("Export Complete...", 2);


        } catch (JAXBException jabe) {
        	jabe.printStackTrace();
        	throw jabe;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw ioe;
        }
    }

    public Ead convertResourceToEadElement(Resources resource, 
                                           InfiniteProgressPanel progressPanel, 
                                           boolean internalOnly, 
                                           boolean numberedCs, 
                                           boolean includeDaos) {
        progressPanel.setTextLine("Exporting Resource to EAD XML...", 2);
        this.numberedCs = numberedCs;
        this.suppressElements = internalOnly;
        this.includeDaos = includeDaos;
        ob = new ObjectFactory();

        Ead ead = ob.createEad();
        List didChildren = new ArrayList();
        Archdesc archDesc = new Archdesc();
        String level = resource.getLevel();
        if (StringHelper.isNotEmpty(level)) {
            AvLevel lev = AvLevel.fromValue(level);
            archDesc.setLevel(lev);
            if(level.equalsIgnoreCase("otherlevel") && StringHelper.isNotEmpty(resource.getOtherLevel()))
            	archDesc.setOtherlevel(resource.getOtherLevel());
        }
        if (resource.getInternalOnly())
            archDesc.setAudience("internal");
        progressPanel.setTextLine("Exporting <ead>.<eadHeader> elements....", 2);

        setHeaders(resource, ead);

        progressPanel.setTextLine("Exporting <archdesc>.<did> elements...", 2);

        Did did = new Did();
        didChildren = did.getMDid();
        setDidChildren(resource, didChildren);
        archDesc.setDid(did);
        progressPanel.setTextLine("Exporting note type elements...", 2);
        handleNotesEtc(resource.getRepeatingData(), archDesc.getMDescFull(), 
                       did.getMDid());

        if (includeDaos) {
            handleDigitalObjects(resource.getInstances(ArchDescriptionDigitalInstances.class), 
                                 archDesc.getMDescFull());
        }
        progressPanel.setTextLine("Exporting <controlaccess> elements...", 2);
        handleControlAccess(resource, archDesc.getMDescFull(), did);

        progressPanel.setTextLine("Exporting components....", 2);
        buildResourceComponentElements(resource, archDesc, progressPanel);
        ead.setArchdesc(archDesc);
        return ead;
    }

    private void setProperty(List list, String value) {
        if (value != null && value.length() > 0)
            list.add(value);
    }

    private void setProperty(List list, Object object, List list2) {
        if (list == null || list2 == null || object == null)
            return;
        if (list2.size() > 0)
            list.add(object);
    }

    private void setHeaders(Resources resource, Ead ead) {
        Eadid eadId = new Eadid();
        eadId.setContent(resource.getEadFaUniqueIdentifier());
        String code = StringHelper.concatenateAllFields("-",resource.getRepository().getCountryCode(),resource.getRepository().getAgencyCode());
        if (StringHelper.isNotEmpty(code))
            eadId.setMainagencycode(code);
        if (StringHelper.isNotEmpty(resource.getRepository().getCountryCode()))
            eadId.setCountrycode(resource.getRepository().getCountryCode());
        if (StringHelper.isNotEmpty(resource.getEadFaLocation()))
            eadId.setUrl(resource.getEadFaLocation());

        Eadheader eadHeader = new Eadheader();
        eadHeader.setCountryencoding("iso3166-1");
        eadHeader.setRepositoryencoding("iso15511");
        eadHeader.setLangencoding("iso639-2b");
        eadHeader.setDateencoding("iso8601");
        eadHeader.setEadid(eadId);
        if (StringHelper.isNotEmpty(resource.getFindingAidStatus()))
            eadHeader.setFindaidstatus(resource.getFindingAidStatus());

        Profiledesc profileDesc = new Profiledesc();
        Creation creation = new Creation();
        creation.getContent().add("This finding aid was produced using the Archivists' Toolkit");
        ObjectFactory ob = new ObjectFactory();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
        Date date = new Date();
        date.getContent().add(sdf.format(new java.util.Date()));

        JAXBElement je = ob.createCreationDate(date);
        creation.getContent().add(je);

        profileDesc.setCreation(creation);
        eadHeader.setProfiledesc(profileDesc);

        if (StringHelper.isNotEmpty(resource.getLanguageOfFindingAid())) {
            Langusage langusage = new Langusage();
            //langusage.getContent().add(resource.getLanguageOfFindingAid());
            langusage = 
                    (Langusage)buildNode(resource.getLanguageOfFindingAid(), 
                                         "langusage", Langusage.class);
            profileDesc.setLangusage(langusage);
        }
        Descrules descrules = new Descrules();
        //descrules.getContent().add(resource.getDescriptionRules());
        
        if (StringHelper.isNotEmpty(resource.getDescriptionRules())){
        	descrules = (Descrules)buildNode(resource.getDescriptionRules(),"descrules",Descrules.class);
        	profileDesc.setDescrules(descrules);
        }

        Filedesc fileDesc = new Filedesc();
        Publicationstmt pstmt = new Publicationstmt();
        if(StringHelper.isNotEmpty(resource.getRepository().getBrandingDevice())){
        	P p= new P();
        	Extref extref = ob.createExtref();
        	extref.setHref(resource.getRepository().getBrandingDevice());
        	extref.setActuate("onLoad");
        	extref.setShow("embed");
        	extref.setType("simple");
        	p.getContent().add(ob.createPExtref(extref));
        	pstmt.getPublisherOrDateOrAddress().add(p);
        }
        
        Publisher publisher = new Publisher();
        publisher.getContent().add(resource.getRepositoryName());
        pstmt.getPublisherOrDateOrAddress().add(publisher);
        fileDesc.setPublicationstmt(pstmt);
        eadHeader.setFiledesc(fileDesc);

        Address address = new Address();
        Addressline addressLine = new Addressline();
        
        Repositories repository = resource.getRepository();
        
        setProperty(addressLine.getContent(), 
                    resource.getRepository().getAddress1());
        setProperty(address.getAddressline(), addressLine, 
                    addressLine.getContent());

        addressLine = new Addressline();
        setProperty(addressLine.getContent(), 
                    resource.getRepository().getAddress2());
        setProperty(address.getAddressline(), addressLine, 
                    addressLine.getContent());

        addressLine = new Addressline();
        setProperty(addressLine.getContent(), 
                    resource.getRepository().getAddress3());
        setProperty(address.getAddressline(), addressLine, 
                    addressLine.getContent());

         if(repository!=null){
             String cityregionzip= StringHelper.concatenateFields(", ",repository.getCity(),repository.getRegion(),repository.getMailCode());

        addressLine = new Addressline();
        setProperty(addressLine.getContent(), 
                    cityregionzip);
        setProperty(address.getAddressline(), addressLine, 
                    addressLine.getContent());
         }


        addressLine = new Addressline();
        setProperty(addressLine.getContent(), 
                    resource.getRepository().getTelephone());
        setProperty(address.getAddressline(), addressLine, 
                    addressLine.getContent());

        addressLine = new Addressline();
        setProperty(addressLine.getContent(), 
                    resource.getRepository().getEmail());
        setProperty(address.getAddressline(), addressLine, 
                    addressLine.getContent());

        setProperty(pstmt.getPublisherOrDateOrAddress(), address, 
                    address.getAddressline());
        

        
        Titlestmt titleStmt = new Titlestmt();

        if (resource.getAuthor().length() > 0) {
            Author author = new Author();
            //author.getContent().add("Finding aid prepared by " + " " +
            //		    resource.getAuthor());
            author = 
                    (Author)buildNode("Finding aid prepared by " + resource.getAuthor(), 
                                      "author", Author.class);
            titleStmt.setAuthor((Author)author);
            //fileDesc.setTitlestmt(titleStmt);
        }

        if (resource.getFindingAidSubtitle().length() > 0) {
            Subtitle subTitle = new Subtitle();
            //subTitle.getContent().add(resource.getFindingAidSubtitle());
            subTitle = 
                    (Subtitle)buildNode(resource.getFindingAidSubtitle(), "subtitle", 
                                        Subtitle.class);
            titleStmt.getSubtitle().add(subTitle);
        }

        if (resource.getSponsorNote().length() > 0) {
            Sponsor sponsor = new Sponsor();
            //sponsor.getContent().add(resource.getSponsorNote());
            sponsor = 
                    (Sponsor)buildNode(resource.getSponsorNote(), "sponsor", Sponsor.class);
            titleStmt.setSponsor(sponsor);
        }

        P p;
        if (resource.getEditionStatement().length() > 0) {
            Editionstmt editionStmt = new Editionstmt();
            p = new P();
            //p.getContent().add(resource.getEditionStatement());
            handlePElements(resource.getEditionStatement(), 
                            editionStmt.getEditionOrP());
            //editionStmt.getEditionOrP().add(p);
            fileDesc.setEditionstmt(editionStmt);
        }

        if (resource.getFindingAidDate() != null && 
            resource.getFindingAidDate().length() > 0) {
            Date date2 = new Date();
            date2.getContent().add(resource.getFindingAidDate());
            pstmt.getPublisherOrDateOrAddress().add(date2);
        }

        //if (resource.getFindingAidTitle().length() > 0) {
        Titleproper titleProper = new Titleproper();
        titleProper = 
                (Titleproper)buildNode(resource.getFindingAidTitle(), "titleproper", 
                                       Titleproper.class);
        if(titleProper==null)
            titleProper = new Titleproper();
            
            titleStmt.getTitleproper().add(titleProper);
            Num num = ob.createNum();
            num.getContent().add(resource.getResourceIdentifier());
            titleProper.getContent().add(ob.createTitleproperNum(num));
            
            if(resource.getFindingAidFilingTitle()!=null && resource.getFindingAidFilingTitle().length()>0){
            	titleProper = 
            		(Titleproper)buildNode(resource.getFindingAidFilingTitle(), "titleproper", 
                                       Titleproper.class); 
            	if(titleProper!=null){
            		titleProper.setType("filing");
            		titleStmt.getTitleproper().add(titleProper);
            	
            	}
            }
        
        //}
        fileDesc.setTitlestmt(titleStmt);


        if (resource.getSeries().length() > 0) {
            Seriesstmt seriesStmt = new Seriesstmt();
            //p = new P();
            //p.getContent().add(resource.getSeries());
            //seriesStmt.getTitleproperOrNumOrP().add(p);
            handlePElements(resource.getSeries(), 
                            seriesStmt.getTitleproperOrNumOrP());
            fileDesc.setSeriesstmt(seriesStmt);
        }

        Change change = new Change();
        if ((resource.getRevisionDate() != null) && 
            resource.getRevisionDate().length() > 0) {
            System.out.println("REV DATE = "+resource.getRevisionDate());
            Revisiondesc revisionDesc = new Revisiondesc();
            Date date2 = new Date();
            date2.getContent().add(resource.getRevisionDate());
            change.setDate(date2);
            Item item = ob.createItem();
            item.getContent().add(resource.getRevisionDescription());
            change.getItem().add(item);
            revisionDesc.getChange().add(change);
            eadHeader.setRevisiondesc(revisionDesc);
        }

        /*if (resource.getRevisionDescription().length() > 0) {
            //Item item = new Item();
            //item.getContent().add(resource.getRevisionDescription());
            //change.getItem().add(item);
            Revisiondesc revisionDesc = 
                (Revisiondesc)buildNode(resource.getRevisionDescription(), 
                                        "revisiondesc", Revisiondesc.class);
            eadHeader.setRevisiondesc(revisionDesc);
        }*/

        if (resource.getFindingAidNote().length() > 0) {
            Notestmt notestmt = new Notestmt();
            Note note = new Note();
            //p = new P();
            //p.getContent().add(resource.getFindingAidNote());
            //note.getAddressOrChronlistOrList().add(p);
            handlePElements(resource.getFindingAidNote(), note.getMBlocks());
            notestmt.getNote().add(note);
            fileDesc.setNotestmt(notestmt);
        }

        //if (resource.getInternalOnly())
        //eadHeader.setAudience("internal");

        ead.setEadheader(eadHeader);

    }

    private void setDidChildren(ResourcesCommon resource, List didChildren) {
        //Unittitle utitle = new Unittitle();
        String title = "";
        title = resource.getTitle();
        //List list = utitle.getContent();
        //list.add(title);
        

        Unittitle utitle;
        utitle = (Unittitle)buildNode(title, "unittitle", Unittitle.class);
        //utitle = buildTitleNode(title);
        didChildren.add(utitle);
        
        Unitid uid = new Unitid();
        String unitId = "";
        if (resource instanceof Resources)
            unitId = ((Resources)resource).getResourceIdentifier();
        else if (resource instanceof ResourcesComponents)
            unitId = 
                    ((ResourcesComponents)resource).getComponentUniqueIdentifier();

        List list = uid.getContent();
        list.add(unitId);
        if (StringHelper.isNotEmpty(unitId))
            didChildren.add(uid);

        if (resource instanceof Resources){
            String rname = ((Resources)resource).getRepositoryName();
            if(StringHelper.isNotEmpty(rname)){
                Corpname corpname  = ob.createCorpname();
                corpname.getContent().add(rname);
                Repository repository = ob.createRepository();
                repository.getContent().add(ob.createRefCorpname(corpname));
                didChildren.add(repository);
            }
        }


        Langmaterial langMaterial = new Langmaterial();
        Language language = new Language();
        String langcode = "";
        langcode = resource.getLanguageCode();
        String langAbbrev = 
            LookupListUtils.getLookupListCodeFromItem(Resources.class, 
                                                      "languageCode", 
                                                      langcode);


        if (langAbbrev != null && langAbbrev.length() > 0) {
            language.setLangcode(langAbbrev);
            JAXBElement je = ob.createLangmaterialLanguage(language);
            langMaterial.getContent().add(je);
            didChildren.add(langMaterial);
        }

        handleContainers(resource.getInstances(ArchDescriptionAnalogInstances.class), 
                         didChildren);

        Physdesc physdesc = getPhysdesc();
        
        List physdescChildren = physdesc.getContent();

        Extent extent = new Extent();
        List extentChildren = extent.getContent();
        String extentString = "";
        if (resource.getExtentNumber() != null && 
            resource.getExtentNumber().toString().length() > 0) {
            extentString = 
                    resource.getExtentNumber() + " " + resource.getExtentType();
            extentChildren.add(extentString);
            physdescChildren.add(ob.createPhysdescExtent(extent));
        }

        extent = new Extent();
        if (resource.getContainerSummary().length() > 0) {
            extent.getContent().add(resource.getContainerSummary());
            JAXBElement je = ob.createPhysdescExtent(extent);
            physdesc.getContent().add(je);
        }
        if (physdesc.getContent().size() > 0)
            didChildren.add(physdesc);

        Unitdate unitdate = new Unitdate();
        Integer bde = resource.getBulkDateBegin();
        Integer bdee = resource.getBulkDateEnd();
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setMinimumIntegerDigits(4);
        nf.setGroupingUsed(false);
        

        
        if (bde != null && bdee != null) {
            unitdate.setNormal(nf.format(bde) + "/" + nf.format(bdee));
            unitdate.setType("bulk");
            if (bde.equals(bdee))
                unitdate.getContent().add("Bulk, " + nf.format(bde));
            else
                unitdate.getContent().add("Bulk, " + nf.format(bde) + "-" + nf.format(bdee));
            didChildren.add(unitdate);
        }

        Unitdate unitdate2 = new Unitdate();
        Integer db = resource.getDateBegin();
        Integer de = resource.getDateEnd();
        if (db != null && de != null) {
            unitdate2.setNormal(nf.format(db) + "/" + nf.format(de));
            if (!db.equals(de))
                unitdate2.setType("inclusive");
            if (resource.getDateExpression().length() == 0) {
                if (!db.equals(de))
                    unitdate2.getContent().add(nf.format(db) + "-" + nf.format(de));
                else
                    unitdate2.getContent().add(nf.format(db) + "");
            } else
                unitdate2.getContent().add(resource.getDateExpression());
            didChildren.add(unitdate2);
        } else {
            if (resource.getDateExpression().length() != 0) {
                unitdate2.getContent().add(resource.getDateExpression());
                unitdate.setLabel(resource.getDateExpression());
                didChildren.add(unitdate2);
            }
        }
    }    
    private void handleDigitalObjects(Set<ArchDescriptionDigitalInstances> instances, 
                                      List archDescOrC) {
        for (ArchDescriptionDigitalInstances instance: instances) {
            if (instance.getDigitalObject() != null)
                handleDigitalObjects(instance.getDigitalObject(), archDescOrC);
        }
    }

    private void handleDigitalObjects(DigitalObjects dig, List archDescOrC) {
        if (dig.getMetsIdentifier() == null)
            System.out.println("METSID == null");

        //if (dig.getMetsIdentifier() == null || dig.getMetsIdentifier().length() == 0) {
        if(!useDOIDAsHREF) {
            for (FileVersions fileVersion: dig.getFileVersions()) {
                Dao dao = new Dao();
                
                
                if(StringHelper.isNotEmpty(fileVersion.getEadDaoActuate()))
                    dao.setActuate(fileVersion.getEadDaoActuate());
                if(StringHelper.isNotEmpty(fileVersion.getEadDaoShow()))
                    dao.setShow(fileVersion.getEadDaoShow());

                if (dig.getTitle().length() > 0)
                    dao.setTitle(dig.getTitle());
                else if (dig.getDateExpression().length() > 0)
                    dao.setTitle(dig.getDateExpression());
                else if (dig.getDateBegin() != null && 
                         dig.getDateEnd() != null){
                	if(dig.getDateBegin().equals(dig.getDateEnd()))
                			dao.setTitle(dig.getDateBegin()+"");
                    else
                			dao.setTitle(dig.getDateBegin() + "-" + dig.getDateEnd());
                }
                else if (dig.getLabel().length() > 0)
                    dao.setTitle(dig.getLabel());


                StringBuffer desc = new StringBuffer();
                boolean title = false;
                boolean date = false;
                
                if (dig.getTitle().length() > 0){
                    desc.append(dig.getTitle());
                    title = true;
                }
                
                if (dig.getDateExpression().length() > 0){
                	date=true;
                	if(title)
                		desc.append(", ");
                    desc.append(dig.getDateExpression() + " ");
                }
                    
                else if (dig.getDateBegin() != null && dig.getDateEnd() != null){
                	date=true;
                	if(title)
                		desc.append(", ");
                	if(dig.getDateBegin().equals(dig.getDateEnd()))
                		desc.append(dig.getDateBegin());
                	else
                		desc.append(dig.getDateBegin() + "-" + dig.getDateEnd() + " ");
                }
                if (dig.getLabel().length() > 0){
                	if(!date && title)
                		desc.append(" ");
                    desc.append("("+dig.getLabel() + ")");
                }
                Daodesc daodesc = new Daodesc();

                if (desc.toString().length() > 0) {
                    P p = new P();
                    p.getContent().add(desc.toString());
                    daodesc.getMBlocks().add(p);
                    dao.setDaodesc(daodesc);
                }
                
                dao.setActuate("onRequest");
                dao.setShow("embed");
                
                //TODO handle lowercasing at data level
                if (dig.getEadDaoActuate().length() > 0)
                    dao.setActuate(dig.getEadDaoActuate());
                if (dig.getEadDaoShow().length() > 0)
                    dao.setShow(dig.getEadDaoShow());

                dao.setHref(fileVersion.getUri());
                dao.setRole(fileVersion.getUseStatement());

                archDescOrC.add(dao);
            }
            for (DigitalObjects digC: dig.getDigitalObjectChildren()) {
                handleDigitalObjects(digC, archDescOrC);
            }
        } else {

            Dao dao = new Dao();
            //dao.setId(dig.getMetsIdentifier());
            dao.setHref(dig.getMetsIdentifier());
            dao.setActuate("onRequest");
            dao.setShow("new");

            //TODO handle at data level
            if (dig.getEadDaoActuate().length() > 0)
                dao.setActuate(dig.getEadDaoActuate());
            if (dig.getEadDaoShow().length() > 0)
                dao.setShow(dig.getEadDaoShow());

            if (dig.getTitle().length() > 0)
                dao.setTitle(dig.getTitle());
            else if (dig.getDateExpression().length() > 0)
                dao.setTitle(dig.getDateExpression());
            else if (dig.getDateBegin() != null && dig.getDateEnd() != null)
                dao.setTitle(dig.getDateBegin() + "-" + dig.getDateEnd());
            else if (dig.getLabel().length() > 0)
                dao.setTitle(dig.getLabel());

            StringBuffer desc = new StringBuffer();
            
            boolean title = false;
            boolean date = false;
            
            if (dig.getTitle().length() > 0){
                desc.append(dig.getTitle());
                title = true;
            }
            
            if (dig.getDateExpression().length() > 0){
            	date=true;
            	if(title)
            		desc.append(", ");
                desc.append(dig.getDateExpression() + " ");
            }
                
            else if (dig.getDateBegin() != null && dig.getDateEnd() != null){
            	date=true;
            	if(title)
            		desc.append(", ");
            	if(dig.getDateBegin().equals(dig.getDateEnd()))
            		desc.append(dig.getDateBegin());
            	else
            		desc.append(dig.getDateBegin() + "-" + dig.getDateEnd() + " ");
            }
            if (dig.getLabel().length() > 0){
            	if(!date && title)
            		desc.append(" ");
                desc.append("("+dig.getLabel() + ")");
            }

            Daodesc daodesc = new Daodesc();
            P p = new P();
            p.getContent().add(desc.toString());
            daodesc.getMBlocks().add(p);
            dao.setDaodesc(daodesc);

            archDescOrC.add(dao);

        }
        //for (DigitalObjects digC: dig.getDigitalObjectChildren()) {
        //handleDigitalObjects(digC, archDescOrC);
        //}


    }

    private void handleNotesInArchdesc(ArchDescriptionNotes note, 
                                       Object object, List newNote, 
                                       List archDescChildren, Head h, 
                                       boolean suppressElements) {


        TreeSet<ArchDescriptionRepeatingData> adrds = 
            new TreeSet<ArchDescriptionRepeatingData>();
        if (note.getMultiPart()) {
            for (ArchDescriptionRepeatingData adrd: note.getChildren()) {
                adrds.add(adrd);
            }


            for (ArchDescriptionRepeatingData adrd: adrds) {

                if (adrd.getType().equals(NotesEtcTypes.DATA_TYPE_LIST_TEXT))
                    handleMultipartText(object, newNote, adrd, 
                                        archDescChildren);
                if (adrd.getType().equals(NotesEtcTypes.DATA_TYPE_LIST_DEFINITION))
                    handleMultipartListdef(object, newNote, adrd, 
                                           archDescChildren);
                if (adrd.getType().equals(NotesEtcTypes.DATA_TYPE_LIST_ORDERED))
                    handleMultipartListord(object, newNote, adrd, 
                                           archDescChildren);
                if (adrd.getType().equals(NotesEtcTypes.DATA_TYPE_CHRONOLOGY))
                    handleMultipartChronlist(object, newNote, adrd, 
                                             archDescChildren);
            }
            
            EADHelper.setProperty(object, "id", 
                                  (note.getPersistentId().toString()), null);
            String internal = "internal";
            if (note.getInternalOnly())
                EADHelper.setProperty(object, "audience", internal, null);

            if (!suppressElements)
                archDescChildren.add(object);
            else if (suppressElements && !(note.getInternalOnly()))
                archDescChildren.add(object);
            
            //archDescChildren.add(object);
            return;
        }

        else {

            if (!(object instanceof Abstract) && !(object instanceof Materialspec) && !(object instanceof Physloc)) {
                handlePElements(note.getNoteContent(), newNote);
            } else {
                //newNote.add(note.getContent());
            }
            EADHelper.setProperty(object, "id", 
                                  (note.getPersistentId().toString()), null);
            String internal = "internal";
            if (note.getInternalOnly())
                EADHelper.setProperty(object, "audience", internal, null);

            if (!suppressElements)
                archDescChildren.add(object);
            else if (suppressElements && !(note.getInternalOnly()))
                archDescChildren.add(object);

        }
        
        
        
    }

    private void handleMultipartText(Object obj, List list, 
                                     ArchDescriptionRepeatingData adrd, 
                                     List archDescChildren) {
        handlePElements(adrd.getContent(), list);

    }

    private void handleMultipartListdef(Object obj, List list, 
                                        ArchDescriptionRepeatingData adrd, 
                                        List archDescChildren) {
        org.archiviststoolkit.structure.EAD.List lisT = 
            new org.archiviststoolkit.structure.EAD.List();
        ListDefinition listDef = (ListDefinition)adrd;
        Defitem defItem = new Defitem();
        Label labeL = new Label();
        Item iteM = new Item();
        lisT.setType("deflist");
        Head h = new Head();
        h.getContent().add((listDef.getTitle()));
        if(StringHelper.isNotEmpty(listDef.getTitle()))
            lisT.setHead(h);
        boolean hasItems = false;
        for (Object item1: 
             sortSet(listDef.getListItems())) {
            hasItems = true;
            defItem = new Defitem();
            labeL = new Label();
            iteM = new Item();
            ListDefinitionItems item = (ListDefinitionItems)item1;
            String label = item.getLabel();
            String value = item.getItemValue();
            labeL.getContent().add(label);
            iteM = (Item)buildNode(value,"item",Item.class);
            //iteM.getContent().add(value);
            defItem.setLabel(labeL);
            defItem.setItem(iteM);
            lisT.getDefitem().add(defItem);
        }
        if(hasItems)
            list.add(lisT);
    }

    private void handleMultipartListord(Object obj, List list, 
                                        ArchDescriptionRepeatingData adrd, 
                                        List archDescChildren) {
        org.archiviststoolkit.structure.EAD.List lisT = 
            new org.archiviststoolkit.structure.EAD.List();
        ListOrdered listOrd = (ListOrdered)adrd;
        lisT.setType("ordered");
        Head h = new Head();
        h.getContent().add((listOrd.getTitle()));
        if(StringHelper.isNotEmpty(listOrd.getTitle()))
            lisT.setHead(h);
        if(StringHelper.isNotEmpty(listOrd.getNumeration()))
            lisT.setNumeration(listOrd.getNumeration());
        boolean hasItems = false;
        for (Object item1: 
             sortSet(listOrd.getListItems())) {
            hasItems = true;
            Item iteM = new Item();
            ListOrderedItems item = (ListOrderedItems)item1;
            String value = item.getItemValue();
            iteM = (Item)buildNode(value,"item",Item.class);
            //iteM.getContent().add(value);
            lisT.getItem().add(iteM);
        }
        if(hasItems)
            list.add(lisT);
    }

    private void handleMultipartChronlist(Object obj, List list, 
                                          ArchDescriptionRepeatingData adrd, 
                                          List archDescChildren) {
        ChronologyList chronology = (ChronologyList)adrd;
        Chronlist chronList = new Chronlist();
        Chronitem chronItem = new Chronitem();
        Event event = new Event();
        Eventgrp eventgrp = new Eventgrp();
        Date date = new Date();
        
        TreeSet<ArchDescriptionStructuredDataItems> itemsOrdered = new TreeSet<ArchDescriptionStructuredDataItems>();
        for(ArchDescriptionStructuredDataItems itemss:chronology.getChronologyItems()){
            itemsOrdered.add(itemss);  
        }
        
        if(StringHelper.isNotEmpty(chronology.getTitle())){
        	Head title = new Head();
        	title.getContent().add(chronology.getTitle());
        	chronList.setHead(title);
        }
        
        for (ArchDescriptionStructuredDataItems item1:itemsOrdered) {
            ChronologyItems item = (ChronologyItems)item1;
            chronItem = new Chronitem();
            chronList.getChronitem().add(chronItem);
            String datE = item.getEventDate();
            date = new Date();
            date.getContent().add(datE);
            chronItem.setDate(date);
            eventgrp = new Eventgrp();
            if (item.getEvents().size() > 1)
                chronItem.setEventgrp(eventgrp);
                
            for (Object evenT: sortSet(item.getEvents())) {
                String desc = ((Events)evenT).getEventDescription();
                event = new Event();
                if (item.getEvents().size() == 1) {
                    //event.getContent().add(desc);

                    event = (Event)buildNode(desc,"event",Event.class);
                    
                    chronItem.setEvent(event);
                } else {
                    event.getContent().add(desc);
                    eventgrp.getEvent().add(event);
                }
            }
        }
        list.add(chronList);

    }

    
    private Set sortSet(Set set){
        TreeSet itemsOrdered = new TreeSet();
        for(Object item:set){
        //TODO - look into adding each items as its type rather than as Object
        //TODO - then on retrieval we don't have to cast back to actual type
            itemsOrdered.add(item);  
        }
        return itemsOrdered;
    }


    private void handleNotes(ArchDescriptionNotes note, List archDescChildren, 
                             List didChildren) {

        String noteType = note.getNotesEtcType().getNotesEtcName();
        String noteContent = note.getNoteContent();
        String headString = "";
        String title = note.getTitle();
        String internal = null;
        if (note.getInternalOnly())
            internal = "internal";

        if (title == null || title.length() == 0)
            headString = noteType;
        else
            headString = title;


        Head h = new Head();
        //List headContent = h.getContent();
        //headContent.add(headString);
        h = (Head)buildNode(headString, "head", Head.class);

        if (noteType == null)
            noteType = "";

		
        if (noteType.startsWith("Abstract")) {
            Abstract newNote = new Abstract();
            newNote = 
                    (Abstract)buildNode(note.getContent(), "abstract", Abstract.class);
            newNote.setLabel(headString);

            handleNotesInArchdesc(note, newNote, newNote.getContent(), 
                                  didChildren, h, suppressElements);
        }
        if (noteType.startsWith("Conditions Governing Access note")) {
            Accessrestrict newNote = new Accessrestrict();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
        if (noteType.startsWith("Accruals note")) {
            Accruals newNote = new Accruals();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
        if (noteType.startsWith("Existence and Location of Copies note")) {
            Altformavail newNote = new Altformavail();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
        if (noteType.startsWith("Appraisal note")) {
            Appraisal newNote = new Appraisal();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
        if (noteType.startsWith("Immediate Source of Acquisition note")) {
            Acqinfo newNote = new Acqinfo();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
        if (noteType.startsWith("Arrangement note")) {
            Arrangement newNote = new Arrangement();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
        if (noteType.startsWith("Biographical/Historical note")) {
            Bioghist newNote = new Bioghist();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
        if (noteType.startsWith("Conditions Governing Use note")) {
            Userestrict newNote = new Userestrict();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
        if (noteType.startsWith("Custodial History note")) {
            Custodhist newNote = new Custodhist();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
        if (noteType.startsWith("Dimensions note")) {
            Physdesc physdesc = getPhysdesc();
            EADHelper.setProperty(physdesc, "id", 
                                  (note.getPersistentId().toString()), null);
            Dimensions dimensions = new Dimensions();
            dimensions = (Dimensions)buildNode(noteContent,"dimensions",Dimensions.class);
            dimensions.setAudience(internal);
            physdesc.setLabel(headString);

            //List dimensionsChildren = null;
            //dimensionsChildren = dimensions.getContent();
            //dimensionsChildren.add(noteContent);
            
            JAXBElement je = ob.createPhysdescDimensions(dimensions);



			physdesc.getContent().add(je);
            if (!suppressElements || !note.getInternalOnly())
                didChildren.add(physdesc);
        }

        if (noteType.startsWith("Existence and Location of Originals note")) {
            Originalsloc newNote = new Originalsloc();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
        if (noteType.startsWith("File Plan note")) {
            Fileplan newNote = new Fileplan();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
        if (noteType.startsWith("Other Finding Aids note")) {
            Otherfindaid newNote = new Otherfindaid();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
        if (noteType.startsWith("General note")) {
            Odd newNote = new Odd();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
        if (noteType.startsWith("General Physical Description note")) {
        
            Physdesc newNote = getPhysdesc();
            newNote = (Physdesc)buildNode(noteContent,"physdesc",Physdesc.class);
            newNote.setLabel(headString);
            newNote.setAudience(internal);
            EADHelper.setProperty(newNote, "id", 
                                  (note.getPersistentId().toString()), null);
            //List physDescChildren = null;
            //physDescChildren = newNote.getContent();
            //physDescChildren.add(noteContent);
            if (!suppressElements || !note.getInternalOnly())
                didChildren.add(newNote);
        }
        if (noteType.startsWith("Language of Materials note")) {
            Langmaterial newNote = new Langmaterial();
            newNote = (Langmaterial)buildNode(noteContent,"langmaterial",Langmaterial.class);

            newNote.setAudience(internal);
            EADHelper.setProperty(newNote, "id", 
                                  (note.getPersistentId().toString()), null);
            newNote.setLabel(headString);
            //List langMaterialChildren = null;
            //langMaterialChildren = newNote.getContent();
            //langMaterialChildren.add(noteContent);
            if (!suppressElements || !note.getInternalOnly())
                didChildren.add(newNote);
        }
        if (noteType.startsWith("Legal Status note")) {
            Accessrestrict accessRestrict = new Accessrestrict();
            accessRestrict.setHead(h);
            EADHelper.setProperty(accessRestrict, "id", 
                                  (note.getPersistentId().toString()), null);
            Legalstatus legalStatus = new Legalstatus();
            legalStatus = (Legalstatus)buildNode(noteContent,"legalstatus",Legalstatus.class);

            legalStatus.setAudience(internal);
            //List legalStatusChildren = null;
            //legalStatusChildren = legalStatus.getContent();
            //legalStatusChildren.add(noteContent);
            accessRestrict.getAddressOrChronlistOrList().add(legalStatus);
            if (!suppressElements || !note.getInternalOnly())
                archDescChildren.add(accessRestrict);
        }
        /*if (noteType.startsWith("Location note")) {
            Physloc newNote = new Physloc();
            EADHelper.setProperty(newNote, "id", 
                                  (note.getPersistentId().toString()), null);
            newNote.setAudience(internal);
            List physlocChildren = null;
            physlocChildren = newNote.getContent();
            physlocChildren.add(noteContent);
            if (!suppressElements)
                didChildren.add(newNote);
        }*/
        if (noteType.startsWith("Location note")) {
        	Physloc newNote = new Physloc();

            newNote = 
                    (Physloc)buildNode(note.getContent(), "physloc", Physloc.class);
            newNote.setLabel(headString);

            handleNotesInArchdesc(note, newNote, newNote.getContent(), 
                                  didChildren, h, suppressElements);
        }
        
        
        /*if (noteType.startsWith("Material Specific Details note")) {
            Materialspec newNote = new Materialspec();
            EADHelper.setProperty(newNote, "id", 
                                  (note.getPersistentId().toString()), null);
            newNote.setAudience(internal);
            List materialSpecChildren = null;
            materialSpecChildren = newNote.getContent();
            materialSpecChildren.add(noteContent);
            if (!suppressElements)
                didChildren.add(newNote);
        }*/
        
        if (noteType.startsWith("Material Specific Details note")) {
        	Materialspec newNote = new Materialspec();
        	newNote = 
                    (Materialspec)buildNode(note.getContent(), "materialspec", Materialspec.class);
            newNote.setLabel(headString);

            handleNotesInArchdesc(note, newNote, newNote.getContent(), 
                                  didChildren, h, suppressElements);
        }
        
        if (noteType.startsWith("Physical Characteristics and Technical Requirements note")) {
            Phystech newNote = new Phystech();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
        if (noteType.startsWith("Physical Facet note")) {
            Physdesc physDesc = getPhysdesc();
            EADHelper.setProperty(physDesc, "id", 
                                  (note.getPersistentId().toString()), null);
            Physfacet physfacet = new Physfacet();
            physfacet = 
                (Physfacet)buildNode(note.getContent(), "physfacet", Physfacet.class);
            physfacet.setAudience(internal);
            physDesc.setLabel(headString);
            //List physfacetChildren = null;
            //physfacetChildren = physfacet.getContent();
            //physfacetChildren.add(noteContent);
            JAXBElement je = ob.createPhysdescPhysfacet(physfacet);
            physDesc.getContent().add(je);
            if (!suppressElements || !note.getInternalOnly())
                didChildren.add(physDesc);
        }
        if (noteType.startsWith("Preferred Citation note")) {
            Prefercite newNote = new Prefercite();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
        if (noteType.startsWith("Processing Information note")) {
            Processinfo newNote = new Processinfo();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
        if (noteType.startsWith("Related Archival Materials note")) {
            Relatedmaterial newNote = new Relatedmaterial();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
        if (noteType.startsWith("Scope and Contents note")) {
            Scopecontent newNote = new Scopecontent();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
        if (noteType.startsWith("Separated Materials note")) {
            Separatedmaterial newNote = new Separatedmaterial();
            newNote.setHead(h);
            handleNotesInArchdesc(note, newNote, 
                                  newNote.getAddressOrChronlistOrList(), 
                                  archDescChildren, h, suppressElements);
        }
    }


    private void handleBibliography(List archDescChildren, Bibliography bib, boolean suppressElements) {

        	
    	org.archiviststoolkit.structure.EAD.Bibliography eadBib = 
            new org.archiviststoolkit.structure.EAD.Bibliography();
        
    	if(bib.getInternalOnly()!=null){
        if(suppressElements && bib.getInternalOnly())
        	return;
        else if (!suppressElements && bib.getInternalOnly())
        	eadBib.setAudience("internal");
    	}
        Head h = new Head();
        /*if (StringHelper.isNotEmpty(bib.getTitle()))
            h.getContent().add(bib.getTitle());
        else
            h.getContent().add(bib.getRepeatingDataType());
        */
        if (StringHelper.isNotEmpty(bib.getTitle()))
        	h = (Head)buildNode(bib.getTitle(),"head",Head.class);
        else
            h.getContent().add(bib.getRepeatingDataType());
        
        if(StringHelper.isNotEmpty(bib.getNote())){
        	P p = new P();
            p = (P)buildNode(bib.getNote(), "p", P.class);

        	//p.getContent().add(bib.getNote());
        	eadBib.getAddressOrChronlistOrList().add(p);
        }
        eadBib.setHead(h);
        eadBib.setId(bib.getPersistentId());
        for (ArchDescriptionStructuredDataItems item: bib.getBibItems()) {
            Bibref bibRef = new Bibref();
            bibRef = 
                    (Bibref)buildNode(((BibItems)item).getItemValue(), "bibref", 
                                      Bibref.class);
            eadBib.getAddressOrChronlistOrList().add(bibRef);
        }
        archDescChildren.add(eadBib);
    }

    private void handleNotesEtc(Set<ArchDescriptionRepeatingData> archDescriptionNotes, 
                                List archDescChildren, List didChildren) {
        for (ArchDescriptionRepeatingData repeatingData: 
             archDescriptionNotes) {
            if (repeatingData instanceof ArchDescriptionNotes) {
                handleNotes((ArchDescriptionNotes)repeatingData, 
                            archDescChildren, didChildren);
            } else if (repeatingData instanceof Bibliography) {
                handleBibliography(archDescChildren, 
                                   (Bibliography)repeatingData, suppressElements);
            } else if (repeatingData instanceof Index) {
                handleIndex(archDescChildren, (Index)repeatingData, suppressElements);
            }
        }
    }

    private Object mapTypeToObject(String type) {
        if (type == null || type.length() == 0)
            return null;
        HashMap map = new HashMap();
        map.put("Corporate Name", new Corpname());
        map.put("Family Name", new Famname());
        map.put("Function", new Function());        
        map.put("Genre Form", new Genreform());
        map.put("Geographic Name", new Geogname());
        map.put("Name", new Name());
        map.put("Occupation", new Occupation());
        map.put("Personal Name", new Persname());
        map.put("Subject", new Subject());
        map.put("Title", new Title());
       
        return map.get(type);

    }

    private void handleIndex(List archDescChildren, Index index, boolean suppressElements) {
        org.archiviststoolkit.structure.EAD.Index indeX = 
            new org.archiviststoolkit.structure.EAD.Index();
        
        if(index.getInternalOnly()!=null){
        	if(suppressElements && index.getInternalOnly())
        		return;
        	else if (!suppressElements && index.getInternalOnly())
        		indeX.setAudience("internal");
        }
        
        Head h = new Head();
        Ref ref = new Ref();
        indeX.setId(index.getPersistentId());
        Indexentry indexEntry = new Indexentry();
        //h.getContent().add(index.getTitle());
        
        Persname p = new Persname();
        if (StringHelper.isNotEmpty(index.getTitle())){
            h = (Head)buildNode(index.getTitle(),"head",Head.class);
        	indeX.setHead(h);
            }
        handlePElements(index.getContent(), indeX.getMBlocks());
        archDescChildren.add(indeX);
        
        TreeSet<ArchDescriptionStructuredDataItems> indexItems = new TreeSet<ArchDescriptionStructuredDataItems>();
        for(ArchDescriptionStructuredDataItems indexItem:index.getIndexItems()){
            indexItems.add(indexItem);  
        }

        int arrayListIndex = 0;
        for (ArchDescriptionStructuredDataItems indexItem1:indexItems) {
            IndexItems indexItem = (IndexItems)indexItem1;
            indexEntry = new Indexentry();
            ref = new Ref();
            Object obj = mapTypeToObject(indexItem.getItemType());
            String itemValue = indexItem.getItemValue();


            /*List content = (List)EADHelper.getProperty(obj, "content");
            if (content != null)
                content.add(itemValue);
            */
            obj = buildNode(itemValue, obj.getClass().getName(), obj.getClass());

            
            if (obj instanceof Corpname)
                indexEntry.setCorpname((Corpname)obj);
            else if (obj instanceof Famname)
                indexEntry.setFamname((Famname)obj);
            else if (obj instanceof Function)
                indexEntry.setFunction((Function)obj);          
            else if (obj instanceof Genreform)
                indexEntry.setGenreform((Genreform)obj);
            else if (obj instanceof Geogname)
                indexEntry.setGeogname((Geogname)obj);           
            else if (obj instanceof Name)
                indexEntry.setName((Name)obj);
            else if (obj instanceof Occupation)
                indexEntry.setOccupation((Occupation)obj);
            else if (obj instanceof Persname)
                indexEntry.setPersname((Persname)obj);
            else if (obj instanceof Subject)
                indexEntry.setSubject((Subject)obj);
            else if (obj instanceof Title)
                indexEntry.setTitle((Title)obj);
            
            if (StringHelper.isNotEmpty(indexItem.getReferenceText())){
                ref = (Ref)buildNode(indexItem.getReferenceText(), "ref", Ref.class);

            	
            	//ref.getContent().add(indexItem.getReferenceText());
            }

            if (StringHelper.isNotEmpty(indexItem.getReference())) {
                //Ref reff = new Ref();
                //reff.setId(indexItem.getReference());
                ref.setTarget(indexItem.getReference());
            }

            if (StringHelper.isNotEmpty(indexItem.getReferenceText()) || 
                StringHelper.isNotEmpty(indexItem.getReference()))
                indexEntry.setRef(ref);

            indeX.getIndexentry().add(arrayListIndex, indexEntry);
            arrayListIndex++;
        }

    }

    private void handleControlAccess(ResourcesCommon resource, 
                                     List archDescOrCChildren, Did did) {
        Controlaccess controlAccess = new Controlaccess();
        Set<ArchDescriptionNames> names = resource.getNamesForPrinting();
        for (ArchDescriptionNames name: names) {
            setName(name, controlAccess.getAddressOrChronlistOrList(), did);
        }
        Set<ArchDescriptionSubjects> archSubjects = resource.getSubjectsForPrinting();
        for (ArchDescriptionSubjects archSubject: archSubjects) {
            setSubject(archSubject, 
                       controlAccess.getAddressOrChronlistOrList());
        }
        if (controlAccess.getAddressOrChronlistOrList().size() > 0)
            archDescOrCChildren.add(controlAccess);
    }

    private void setSubject(ArchDescriptionSubjects archSubject, 
                            List controlAccessChildren) {
        String termType = archSubject.getSubject().getSubjectTermType();
        String term = archSubject.getSubjectTerm();
        String origsource = archSubject.getSubjectSource();
        String source = 
            LookupListUtils.getLookupListCodeFromItem(Subjects.class, 
                                                      "subjectSource", 
                                                      origsource);
        if ((termType.toLowerCase()).startsWith("function")) {
            Function outputTerm = new Function();
            outputTerm.setSource(source);
            outputTerm.getContent().add(term);
            controlAccessChildren.add(outputTerm);
        } else if ((termType.toLowerCase()).startsWith("uniform title")) {
            Title outputTerm = new Title();
            outputTerm.setSource(source);
            outputTerm.getContent().add(term);
            controlAccessChildren.add(outputTerm);
        } else if ((termType.toLowerCase()).startsWith("genre")) {
            Genreform outputTerm = new Genreform();
            outputTerm.setSource(source);
            outputTerm.getContent().add(term);
            controlAccessChildren.add(outputTerm);

        } else if ((termType.toLowerCase()).startsWith("geographic")) {
            Geogname outputTerm = new Geogname();
            outputTerm.setSource(source);
            outputTerm.getContent().add(term);
            controlAccessChildren.add(outputTerm);

        } else if ((termType.toLowerCase()).startsWith("occupation")) {
            Occupation outputTerm = new Occupation();
            outputTerm.setSource(source);
            outputTerm.getContent().add(term);
            controlAccessChildren.add(outputTerm);

        } else if ((termType.toLowerCase()).startsWith("topical")) {
            Subject outputTerm = new Subject();
            outputTerm.setSource(source);
            outputTerm.getContent().add(term);
            controlAccessChildren.add(outputTerm);
        }
    }

    private void setName(ArchDescriptionNames adName, 
                         List controlAccessChildren, Did did) {

        // this is intentional; AT role = EAD function; (not EAD role)
        //String role = adName.getNameLinkFunction();
        String function = adName.getNameLinkFunction();
        String role = adName.getRole();
        if(StringHelper.isEmpty(role))
            role=null;
        Names name = adName.getName();
        String type = name.getNameType();
        String sortName = name.getSortName();
        if(StringHelper.isNotEmpty(adName.getForm()))
            sortName = sortName+" -- "+adName.getForm();
        String origsource = name.getNameSource();
        String code = LookupListUtils.getLookupListCodeFromItem(Names.class,"nameRule",name.getNameRule());

        String source = 
            LookupListUtils.getLookupListCodeFromItem(Names.class, "nameSource", 
                                                      origsource);
        if (StringHelper.isEmpty(source)) {
            source = origsource;
        }
        if (type.equals(Names.PERSON_TYPE)) {
            Persname outputName = new Persname();
            if(StringHelper.isNotEmpty(code)){
                outputName.setRules(code);
            }
            outputName.getContent().add(sortName);
            outputName.setRole(role);
            if(StringHelper.isNotEmpty(source))
                outputName.setSource(source);
            if (function.equalsIgnoreCase("creator")) {
                Origination orig = new Origination();
                orig.setLabel("creator");
                orig.getContent().add(ob.createOriginationPersname(outputName));
                did.getMDid().add(orig);
            } else
                controlAccessChildren.add(outputName);
        } else if (type.equals(Names.CORPORATE_BODY_TYPE)) {
            Corpname outputName = new Corpname();
            if(StringHelper.isNotEmpty(code)){
                outputName.setRules(code);
            }
            outputName.getContent().add(sortName);
            outputName.setRole(role);
            if(StringHelper.isNotEmpty(source))
                outputName.setSource(source);
            if (function.equalsIgnoreCase("creator")) {
                Origination orig = new Origination();
                orig.setLabel("creator");
                orig.getContent().add(ob.createOriginationCorpname(outputName));
                did.getMDid().add(orig);
            } else
                controlAccessChildren.add(outputName);
        } else if (type.equals(Names.FAMILY_TYPE)) {
            Famname outputName = new Famname();
            if(StringHelper.isNotEmpty(code)){
                outputName.setRules(code);
            }
            outputName.getContent().add(sortName);
            outputName.setRole(role);
            if(StringHelper.isNotEmpty(source))
                outputName.setSource(source);
            if (function.equalsIgnoreCase("creator")) {
                Origination orig = new Origination();
                orig.setLabel("creator");
                orig.getContent().add(ob.createOriginationFamname(outputName));
                did.getMDid().add(orig);
            } else
                controlAccessChildren.add(outputName);
        }


    }

    private void handlePElements(String noteContent, 
                                 List scopeContentChildren) {
        List pChildren = null;
        String charr = "";
        String eChar = "\n\n";
        String pString[] = noteContent.split(eChar);
        String elemString = "";
        P p = new P();
        int e = 0;
        //System.out.println("P Contents");
        for (int a = 0; a < pString.length; a++) {
            p = new P();
            pChildren = p.getContent();
            elemString = pString[a];
            //e = charr.length();
            //pChildren.add(elemString.substring(e,pString[a].length()));
            //System.out.println("p content = "+pString[a]);
            //String newP = "<p>" + pString[a] + "</p>";
            p = (P)buildNode(pString[a], "p", P.class);
            scopeContentChildren.add(p);
        }
        return;
    }

    private P buildPNode(String content) {
        try {
            StringReader sr = new StringReader(content);
            Object o = context.createUnmarshaller().unmarshal(sr);
            return (P)o;

        } catch (JAXBException jabe) {
            jabe.printStackTrace();
            System.out.println("Content = " + content);
            return new P();
        }
    }

    private Unittitle buildTitleNode(String content) {
        try {
            StringReader sr = 
                new StringReader("<unittitle xmlns=\"urn:isbn:1-931666-22-9\">" + 
                                 content + "</unittitle>");
            Unmarshaller un = context.createUnmarshaller();
            un.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
            JAXBElement<Unittitle> o = 
                un.unmarshal(new StreamSource(sr), Unittitle.class);
            //System.out.println(context.toString());
            return o.getValue();

        } catch (JAXBException jabe) {
            jabe.printStackTrace();
            System.out.println("TitleExc = " + content);
            return new Unittitle();
        }
    }

    /*Object buildNode(String content,String elementNamez) {
        try {
            StringReader sr = new StringReader("<author>"+content+"</author>");
            Unmarshaller un = context.createUnmarshaller();
            JAXBElement o = context.createUnmarshaller().unmarshal(new StreamSource(sr),Object.class);
            //Author auth = o.getValue();
            return o.getValue();

        } catch (JAXBException jabe) {
            jabe.printStackTrace();
            System.out.println("Title = " + content);
            return new Object();
        }
    }*/

    private String addXlinkNamespace(String content){
    	//content = CharacterConvert.replaceAttribute(content, "linktype", "ns2:type");
    	content = CharacterConvert.replaceAttribute(content, "title", "ns2:title");

    	content = CharacterConvert.replaceAttribute(content, "linktype", "ns2:type");
    	content = CharacterConvert.replaceAttribute(content, "href", "ns2:href");
    	content = CharacterConvert.replaceAttribute(content, "show", "ns2:show");
    	content = CharacterConvert.replaceAttribute(content, "actuate", "ns2:actuate");	
    	return content;
    }
    
    private Object buildNode(String content, String tagName, Class clazz) {
        try {
        	context.toString();
        	content = StringHelper.cleanUpWhiteSpace(content);
            content = addXlinkNamespace(content);
            content = content.replaceAll("&amp;", "&");
            content = content.replaceAll("&", "&amp;");
            
            //content = StringHelper.escapeHTML(content);
 
        	StringReader sr = 
                new StringReader("<" + tagName + " xmlns=\"urn:isbn:1-931666-22-9\" xmlns:ns2=\"http://www.w3.org/1999/xlink\">" + 
                                 content + "</" + tagName + ">");
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setSchema(null);
        	JAXBElement o = 
        		unmarshaller.unmarshal(new StreamSource(sr), 
                                                       clazz);
            return o.getValue();

        } catch (JAXBException jabe) {
            jabe.printStackTrace();
            System.out.println("Node = " + content);
            try {
				return clazz.newInstance();
			} catch (InstantiationException e) {
				return null;
			} catch (IllegalAccessException e) {
				return null;
			}
        }
    }


    private Bibref buildBibrefNode(String content) {
        try {
            StringReader sr = 
                new StringReader("<bibref>" + content + "</bibref>");
            Object o = context.createUnmarshaller().unmarshal(sr);
            return (Bibref)o;

        } catch (JAXBException jabe) {
            jabe.printStackTrace();
            System.out.println("Bibref = " + content);
            return new Bibref();
        }
    }


    private void buildResourceComponentElements(Resources resource, 
                                                Archdesc archDesc,
												InfiniteProgressPanel progressPanel) {
        List archDescChildren = archDesc.getMDescFull();
        C c = new C();
        Dsc dsc = new Dsc();
        List dscChildren = null;
        archDescChildren.add(dsc);
        SortedSet<ResourcesComponents> resourcesComponents = 
            resource.getResourcesComponents();
        levelC = 1;
        for (ResourcesComponents resourcesComponent: resourcesComponents) {
            if(progressPanel.isProcessCancelled()) { // prcess cancelled so just return
                return;
            }

            if (!numberedCs) {
                dscChildren = dsc.getCAndThead();
                c = new C();
                if(resourcesComponent.getInternalOnly())
                	c.setAudience("internal");
                c.setId(resourcesComponent.getPersistentId().toString());
                if ((!suppressElements) || 
                    (!resourcesComponent.getInternalOnly())) {
                    dscChildren.add(c);
                    buildResourceComponentElement(resourcesComponent, c, progressPanel);
                }
            } else {
                C01 c1 = new C01();
                dscChildren = dsc.getC01AndThead();
                if(resourcesComponent.getInternalOnly())
                	c1.setAudience("internal");
                c1.setId(resourcesComponent.getPersistentId().toString());

                if ((!suppressElements) || 
                    (!resourcesComponent.getInternalOnly())) {
                    dscChildren.add(c1);
                    buildResourceComponentElement(resourcesComponent, c1, progressPanel);
                }
            }
        }

    }

    private void buildResourceComponentElement(ResourcesComponents resourceComponent, 
                                               C c,
											   InfiniteProgressPanel progressPanel) {

        if(progressPanel.isProcessCancelled()) { // prcess cancelled so just return
            return;
        }

        String level = resourceComponent.getLevel().toLowerCase();
        if (StringHelper.isNotEmpty(level)) {
            c.setLevel(AvLevel.fromValue(level));
            if(level.equalsIgnoreCase("otherlevel") && StringHelper.isNotEmpty(resourceComponent.getOtherLevel())) {
            	c.setOtherlevel(resourceComponent.getOtherLevel());
            }
        }

        c.setId(resourceComponent.getPersistentId().toString());
        List cChildren = c.getTheadAndC();
        Did did = new Did();
        List didChildren = did.getMDid();
        setDidChildren(resourceComponent, didChildren);
        c.setDid(did);
        handleNotesEtc(resourceComponent.getRepeatingData(), c.getMDescFull(), 
                       didChildren);
        if (includeDaos)
            handleDigitalObjects(resourceComponent.getInstances(ArchDescriptionDigitalInstances.class), 
                                 c.getMDescFull());
        handleControlAccess(resourceComponent, c.getMDescFull(), did);
        //handleContainers(resourceComponent.getInstances(ArchDescriptionAnalogInstances.class),
        //                 didChildren);
        c.setDid(did);

		//update the progress message
		depth++;
		if (progressPanel != null) {
			progressPanel.setTextLine(HandleDidAction.getLabel(c.getDid()), depth);
		}

        if(resourceComponent.getInternalOnly())
        	c.setAudience("internal");
        
        SortedSet<ResourcesComponents> resourcesComponents = resourceComponent.getResourcesComponents();
        if (resourcesComponents.size() > 0)
            System.out.println("number of Components: " + 
                               resourcesComponents.size());
        for (ResourcesComponents resourcesComponent: resourcesComponents) {
            C newC = new C();
            //if(resourcesComponent.getInternalOnly())
            	//newC.setAudience("internal");
            if ((!suppressElements) || 
                (!resourcesComponent.getInternalOnly())) {
                cChildren.add(newC);
                buildResourceComponentElement(resourcesComponent, newC, progressPanel);
            }
        }
		depth--;
    }


    private void buildResourceComponentElement(ResourcesComponents resourceComponent, 
                                               Object c,
											   InfiniteProgressPanel progressPanel) {

        if(progressPanel.isProcessCancelled()) { // prcess cancelled so just return
            return;
        }

        String level = resourceComponent.getLevel();
        List cChildren = null;
        List cContent = null;
        Did did = new Did();
        Object temp = new Object();
        int lev = 1;
        temp = resourceComponent.getParent();
        while (temp != null) {
            lev++;
            if (temp instanceof ResourcesComponents)
                temp = ((ResourcesComponents)temp).getParent();
            else
                temp = null;

        }
        levelC = levelC + 1;
        if (c instanceof C01) {
            cChildren = ((C01)c).getTheadAndC02();
            cContent = ((C01)c).getMDescFull();

            if(StringHelper.isNotEmpty(level)) {
                ((C01)c).setLevel(AvLevel.fromValue(level));
                if(level.equalsIgnoreCase("otherlevel") && StringHelper.isNotEmpty(resourceComponent.getOtherLevel())) {
            	    ((C01)c).setOtherlevel(resourceComponent.getOtherLevel());
                }
            }

            ((C01)c).setDid(did);
        }
        if (c instanceof C02) {
            cChildren = ((C02)c).getTheadAndC03();
            cContent = ((C02)c).getMDescFull();

            if(StringHelper.isNotEmpty(level)) {
                ((C02)c).setLevel(AvLevel.fromValue(level));
                if(level.equalsIgnoreCase("otherlevel") && StringHelper.isNotEmpty(resourceComponent.getOtherLevel())) {
            	    ((C02)c).setOtherlevel(resourceComponent.getOtherLevel());
                }
            }

            ((C02)c).setDid(did);
        }
        if (c instanceof C03) {
            cChildren = ((C03)c).getTheadAndC04();
            cContent = ((C03)c).getMDescFull();

            if(StringHelper.isNotEmpty(level)) {
                ((C03)c).setLevel(AvLevel.fromValue(level));
                if(level.equalsIgnoreCase("otherlevel") && StringHelper.isNotEmpty(resourceComponent.getOtherLevel())) {
            	    ((C03)c).setOtherlevel(resourceComponent.getOtherLevel());
                }
            }

            ((C03)c).setDid(did);
        }
        if (c instanceof C04) {
            cChildren = ((C04)c).getTheadAndC05();
            cContent = ((C04)c).getMDescFull();

            if(StringHelper.isNotEmpty(level)) {
                ((C04)c).setLevel(AvLevel.fromValue(level));
                if(level.equalsIgnoreCase("otherlevel") && StringHelper.isNotEmpty(resourceComponent.getOtherLevel())) {
            	    ((C04)c).setOtherlevel(resourceComponent.getOtherLevel());
                }
            }

            ((C04)c).setDid(did);
        }
        if (c instanceof C05) {
            cChildren = ((C05)c).getTheadAndC06();
            cContent = ((C05)c).getMDescFull();

            if(StringHelper.isNotEmpty(level)) {
                ((C05)c).setLevel(AvLevel.fromValue(level));
                if(level.equalsIgnoreCase("otherlevel") && StringHelper.isNotEmpty(resourceComponent.getOtherLevel())) {
            	    ((C05)c).setOtherlevel(resourceComponent.getOtherLevel());
                }
            }

            ((C05)c).setDid(did);
        }
        if (c instanceof C06) {
            cChildren = ((C06)c).getTheadAndC07();
            cContent = ((C06)c).getMDescFull();
            if(StringHelper.isNotEmpty(level)) {
                ((C06)c).setLevel(AvLevel.fromValue(level));
                if(level.equalsIgnoreCase("otherlevel") && StringHelper.isNotEmpty(resourceComponent.getOtherLevel())) {
            	    ((C06)c).setOtherlevel(resourceComponent.getOtherLevel());
                }
            }
            ((C06)c).setDid(did);
        }
        if (c instanceof C07) {
            cChildren = ((C07)c).getTheadAndC08();
            cContent = ((C07)c).getMDescFull();
            if(StringHelper.isNotEmpty(level)) {
                ((C07)c).setLevel(AvLevel.fromValue(level));
                if(level.equalsIgnoreCase("otherlevel") && StringHelper.isNotEmpty(resourceComponent.getOtherLevel())) {
            	    ((C07)c).setOtherlevel(resourceComponent.getOtherLevel());
                }
            }
            ((C07)c).setDid(did);
        }
        if (c instanceof C08) {
            cChildren = ((C08)c).getTheadAndC09();
            cContent = ((C08)c).getMDescFull();
            if(StringHelper.isNotEmpty(level)) {
                ((C08)c).setLevel(AvLevel.fromValue(level));
                if(level.equalsIgnoreCase("otherlevel") && StringHelper.isNotEmpty(resourceComponent.getOtherLevel())) {
            	    ((C08)c).setOtherlevel(resourceComponent.getOtherLevel());
                }
            }
            ((C08)c).setDid(did);
        }
        if (c instanceof C09) {
            cChildren = ((C09)c).getTheadAndC10();
            cContent = ((C09)c).getMDescFull();
            if(StringHelper.isNotEmpty(level)) {
                ((C09)c).setLevel(AvLevel.fromValue(level));
                if(level.equalsIgnoreCase("otherlevel") && StringHelper.isNotEmpty(resourceComponent.getOtherLevel())) {
            	    ((C09)c).setOtherlevel(resourceComponent.getOtherLevel());
                }
            }
            ((C09)c).setDid(did);
        }
        if (c instanceof C10) {
            cChildren = ((C10)c).getTheadAndC11();
            cContent = ((C10)c).getMDescFull();
            if(StringHelper.isNotEmpty(level)) {
                ((C10)c).setLevel(AvLevel.fromValue(level));
                if(level.equalsIgnoreCase("otherlevel") && StringHelper.isNotEmpty(resourceComponent.getOtherLevel())) {
            	    ((C10)c).setOtherlevel(resourceComponent.getOtherLevel());
                }
            }
            ((C10)c).setDid(did);
        }
        if (c instanceof C11) {
            cChildren = ((C11)c).getTheadAndC12();
            cContent = ((C11)c).getMDescFull();
            if(StringHelper.isNotEmpty(level)) {
                ((C11)c).setLevel(AvLevel.fromValue(level));
                if(level.equalsIgnoreCase("otherlevel") && StringHelper.isNotEmpty(resourceComponent.getOtherLevel())) {
            	    ((C11)c).setOtherlevel(resourceComponent.getOtherLevel());
                }
            }
            ((C11)c).setDid(did);
        }
        if (c instanceof C12) {
            cContent = ((C12)c).getMDescFull();
            if(StringHelper.isNotEmpty(level)) {
                ((C12)c).setLevel(AvLevel.fromValue(level));
                if(level.equalsIgnoreCase("otherlevel") && StringHelper.isNotEmpty(resourceComponent.getOtherLevel())) {
            	    ((C12)c).setOtherlevel(resourceComponent.getOtherLevel());
                }
            }
            ((C12)c).setDid(did);
        }

		// TODO do this for all variables above
        EADHelper.setProperty(c, "id", 
                              resourceComponent.getPersistentId().toString(), 
                              null);
        if(resourceComponent.getInternalOnly()){
            EADHelper.setProperty(c, "audience", "internal", null);
        }
        List didChildren = did.getMDid();
        setDidChildren(resourceComponent, didChildren);
        handleNotesEtc(resourceComponent.getRepeatingData(), cContent, 
                       didChildren);
        if (includeDaos)
            handleDigitalObjects(resourceComponent.getInstances(ArchDescriptionDigitalInstances.class), 
                                 cContent);
        handleControlAccess(resourceComponent, cContent, did);

		depth++;
		if (progressPanel != null) {
			progressPanel.setTextLine(HandleDidAction.getLabel(did), depth);
		}
 
        Set<ResourcesComponents> resourcesComponents = 
            resourceComponent.getResourcesComponents();
        boolean in = false;
        for (ResourcesComponents resourcesComponent: resourcesComponents) {
//            System.out.println("status  = "+resourceComponent.getInternalOnly());

            /*if ((!suppressElements) || 
                (!resourceComponent.getInternalOnly())) {
                if (!in) {
                    in = true;
                }
            }*/
            

            
            Object newC = null;
            String name;
            if (lev < 10)
                name = "org.archiviststoolkit.structure.EAD." + "C0" + lev;
            else
                name = "org.archiviststoolkit.structure.EAD." + "C" + lev;
            try {
                Class a = Class.forName(name);
                newC = a.newInstance();
            } catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
            } catch (IllegalAccessException iae) {
                iae.printStackTrace();
            } catch (InstantiationException ie) {
                ie.printStackTrace();
            }
            if (((!suppressElements) || 
                 (!resourcesComponent.getInternalOnly())) && 
                cChildren != null) {           
                cChildren.add(newC);
                buildResourceComponentElement(resourcesComponent, newC, progressPanel);
            }
        }
		depth--;
    }

    private void handleContainers(Set<ArchDescriptionAnalogInstances> instances, 
                                  List didChildren) {

        for (ArchDescriptionAnalogInstances instance: instances) {
            String parentId = "cid" + instance.getIdentifier().toString();

            /*****************************Container 1*************************************/
            String type = instance.getContainer1Type();
            String instanceType = instance.getInstanceType();
            String containerIndicator = instance.getContainer1Indicator();
            String barcode = "";
            barcode = instance.getBarcode();

            Container container1 = new Container();

            // if barcode is present place it in the label
            if (StringHelper.isNotEmpty(barcode)) {
                String label = instanceType + " (" + barcode + ")";
                container1.setLabel(label);
            } else {
                container1.setLabel(instanceType);
            }

            container1.setId(parentId);

            if (null != type)
                container1.setType(type);
            container1.getContent().add(containerIndicator);
            if (null != type && type.length() > 0) {
                didChildren.add(container1);
            }

            /*****************************Container 2*************************************/
            type = instance.getContainer2Type();
            containerIndicator = instance.getContainer2Indicator();

            Container container2 = new Container();
            container2.getParent().add(container1);
            
            if (null != type)
                container2.setType(type);
            container2.getContent().add(containerIndicator);
            if (null != type && type.length() > 0)
                didChildren.add(container2);

            /*****************************Container 3*************************************/
            type = instance.getContainer3Type();
            containerIndicator = instance.getContainer3Indicator();

            Container container3 = new Container();
            container3.getParent().add(container1);
            if (null != type)
                container3.setType(type);
            container3.getContent().add(containerIndicator);
            if (null != type && type.length() > 0)
                didChildren.add(container3);
        }
    }

    public Element buildElementEAD(Document dom) {
        Element ead = (Element)dom.createElement("ead");
        Element eadHeader = (Element)buildElementEADHeader(dom);
        ead.appendChild(eadHeader);
        Element archDesc = (Element)buildElementArchDesc(dom);
        ead.appendChild(archDesc);

        return ead;
    }

    public Element buildElementEADHeader(Document dom) {
        Element ead = (Element)dom.createElement("eadheader");
        return ead;
    }

    public Element buildElementArchDesc(Document dom) {
        Element ead = (Element)dom.createElement("archdesc");
        return ead;
    }
    
    public Physdesc getPhysdesc(){
    	//if(this.physDesc==null){
    		//this.physDesc = new Physdesc();
    	//}
    	physDesc = new Physdesc();
    	return physDesc;
    }
}

