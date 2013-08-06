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

import edu.harvard.hul.ois.mets.Agent;
import edu.harvard.hul.ois.mets.Div;
import edu.harvard.hul.ois.mets.DmdSec;
import edu.harvard.hul.ois.mets.FLocat;
import edu.harvard.hul.ois.mets.FileGrp;
import edu.harvard.hul.ois.mets.FileSec;
import edu.harvard.hul.ois.mets.Fptr;
import edu.harvard.hul.ois.mets.Loctype;
import edu.harvard.hul.ois.mets.MdWrap;
import edu.harvard.hul.ois.mets.Mdtype;
import edu.harvard.hul.ois.mets.Mets;
import edu.harvard.hul.ois.mets.MetsHdr;
import edu.harvard.hul.ois.mets.Name;
import edu.harvard.hul.ois.mets.Note;
import edu.harvard.hul.ois.mets.Role;
import edu.harvard.hul.ois.mets.StructMap;
import edu.harvard.hul.ois.mets.Type;
import edu.harvard.hul.ois.mets.XmlData;
import edu.harvard.hul.ois.mets.helper.MetsException;
import edu.harvard.hul.ois.mets.helper.MetsValidator;
import edu.harvard.hul.ois.mets.helper.MetsWriter;
import edu.harvard.hul.ois.mets.helper.PCData;

import edu.harvard.hul.ois.mets.helper.PreformedXML;

import java.io.FileOutputStream;
import java.io.IOException;

import java.util.*;

import org.archiviststoolkit.model.DigitalObjects;
import org.archiviststoolkit.model.FileVersions;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.structure.DC.ElementType;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.util.StringHelper;
//import edu.harvard.hul.ois.mets.File;


public class METSExport {
    private String metadataType;
    private InfiniteProgressPanel progressPanel;
    private int order = 1;
    private boolean debug = false;
    private int pOrder = 0;

    public static Resources getResourceFromDigitalObject(DigitalObjects digitalObject) {
        Resources resource = null;
        if (digitalObject.getDigitalInstance() != null && digitalObject.getDigitalInstance().getResource() == null && digitalObject.getDigitalInstance().getResourceComponent() != null)
            resource = MARCExport.getResourceFromResourceComponent(digitalObject.getDigitalInstance().getResourceComponent());
        else if (digitalObject.getDigitalInstance() != null && digitalObject.getDigitalInstance().getResource() != null) {
            resource = digitalObject.getDigitalInstance().getResource();
        }
        return resource;
    }

    public void convertDBRecordToFile(DigitalObjects digitalObject, java.io.File outputFile, InfiniteProgressPanel progressPanel, boolean internalOnly, String metadataType) throws IOException, MetsException {
        this.metadataType = metadataType;
        this.progressPanel = progressPanel;
        ElementType etype = new ElementType();
        Mets mets = new Mets();
        mets.setOBJID(digitalObject.getMetsIdentifier());
        mets.setTYPE(digitalObject.getObjectType());
        mets.setLABEL(digitalObject.getTitle());
        mets.setPROFILE("Archivists' Toolkit Profile");
        MetsHdr metsHdr = new MetsHdr();
        metsHdr.setCREATEDATE(new java.util.Date());
        Agent agent = new Agent();
        agent.setROLE(Role.CREATOR);
        agent.setTYPE(Type.ORGANIZATION);

        Name name = new Name();
        //Resources resource = getResourceFromDigitalObject(digitalObject);
        String rName = null;
        String rUrl = null;

        // get the repository name
        rName = digitalObject.getRepository().getRepositoryName();
        rUrl = digitalObject.getRepository().getUrl();

        /**if(resource!=null){
         rName = resource.getRepositoryName();

         }*/

        if (StringHelper.isNotEmpty(rName))
            name.getContent().add(new PCData(rName));
        //name.getContent().add (new PCData("Archivists' Toolkit"));
        agent.getContent().add(name);
        Note note = new Note();
        note.getContent().add(new PCData("Produced by Archivists' Toolkit &#153;"));
        agent.getContent().add(note);
        note = new Note();
        //String rUrl = digitalObject.getDigitalInstance().getResource().getRepository().getUrl();
        if (StringHelper.isNotEmpty(rUrl)) {
            note.getContent().add(new PCData(rUrl));
            //note.getContent().add(new PCData("http://www.archiviststoolkit.org"));
            agent.getContent().add(note);
        }
        metsHdr.getContent().add(agent);
        mets.getContent().add(metsHdr);

        Vector idsNotUsed = new Vector();
        buildDmdSec(digitalObject, mets, internalOnly, idsNotUsed);


        FileSec fileSec = new FileSec();
        FileGrp fileGrp = null;
        edu.harvard.hul.ois.mets.File file = null;
        FLocat fLocat = null;

        HashMap useTypes = new HashMap();
        StructMap lstructMap = new StructMap();
        lstructMap.setTYPE("logical");
        Div ldiv = new Div();

        StructMap pstructMap = new StructMap();
        pstructMap.setTYPE("physical");
        Div pDiv = new Div();
        pDiv.setORDER(1);
        pDiv.setLABEL(digitalObject.getTitle());
        //pDiv.setTYPE(digitalObject.getObjectType());
        pDiv.setTYPE("item");
        if (!idsNotUsed.contains(digitalObject.getDigitalObjectId()))
            pDiv.setDMDID("dm" + digitalObject.getDigitalObjectId() + "");
        pOrder = 0;
        handleFileSec(digitalObject, useTypes, ldiv, pDiv, idsNotUsed, 0);

        lstructMap.getContent().add(ldiv);
        pstructMap.getContent().add(pDiv);


        Set<String> keys = useTypes.keySet();
        boolean in = false;
        for (String key : keys) {
            in = true;
            fileGrp = new FileGrp();
            fileGrp.setUSE(key);
            Vector<FileVersions> fileVersions = (Vector) useTypes.get(key);
            for (FileVersions fileVersion : fileVersions) {
                //in=true;
                file = new edu.harvard.hul.ois.mets.File();
                file.setID("FID" + fileVersion.getFileVersionId() + "");
                file.setGROUPID(fileVersion.getDigitalObject().getDigitalObjectId() + "");
                file.setUSE(fileVersion.getUseStatement());
                fLocat = new FLocat();
                fLocat.setXlinkHref(fileVersion.getUri());
                fLocat.setLOCTYPE(Loctype.URL);
                file.getContent().add(fLocat);
                fileGrp.getContent().add(file);
            }
            fileSec.getContent().add(fileGrp);
        }
        //mets.getContent ().add (dmdSec);
        if (in)
            mets.getContent().add(fileSec);
        mets.getContent().add(lstructMap);
        mets.getContent().add(pstructMap);


        try {
            if (debug) {
                System.out.println("here5");
            }
            mets.validate(new MetsValidator());
            if (debug) {
                System.out.println("here6");
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            System.out.println("here7");
            mets.write(new MetsWriter(out));
            if (debug) {
                System.out.println("here8");
            }
            out.close();
            if (debug) {
                System.out.println("here9");
            }
        }
        catch (MetsException me) {
            me.printStackTrace();
            throw me;

        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            throw ioe;

        }
    }

    private void buildDmdSec(DigitalObjects digitalObject, Mets mets, boolean internalOnly, Vector idsNotUsed) {
        DmdSec dmdSec = new DmdSec();
        dmdSec.setID("dm" + digitalObject.getDigitalObjectId());
        MdWrap mdWrap = new MdWrap();
        mdWrap.setMIMETYPE("text/xml");
        XmlData xmlData = new XmlData();
        //xmlData.setSchema("dc","http://purl.org/dc/elements/1.1/");

        DCExport dcExport = new DCExport();
        MODSExport modsExport = new MODSExport();
        String dmdXML = "";
        if (metadataType.equals("dc")) {
            mdWrap.setMDTYPE(Mdtype.DC);
            dmdXML = dcExport.convertDBRecordToXML(digitalObject, internalOnly, false);
        } else if (metadataType.equals("mods")) {
            dmdXML = modsExport.convertResourceMODSXML(digitalObject, progressPanel, internalOnly, false);
            mdWrap.setMDTYPE(Mdtype.MODS);
            //mdWrap.setSchema("http://www.loc.gov/mods/v3","http://www.loc.gov/standards/mods/v3/mods-3-2.xsd");
        }

        if (dmdXML.length() > 0) {
            PreformedXML pxml = new PreformedXML(dmdXML);
            xmlData.getContent().add(pxml);
            mdWrap.getContent().add(xmlData);
            dmdSec.getContent().add(mdWrap);
            mets.getContent().add(dmdSec);
        } else {
            idsNotUsed.add(digitalObject.getDigitalObjectId());
        }

        // place in array list so that children DO are returned to in the
        // correct order by sorting
        ArrayList<DigitalObjects> digitalObjects = new ArrayList<DigitalObjects>(digitalObject.getDigitalObjectChildren());
        Collections.sort(digitalObjects);

        for (DigitalObjects digO : digitalObjects) {
            buildDmdSec(digO, mets, internalOnly, idsNotUsed);
        }
    }


    //private void handleStructMapL()
    private void handleFileSec(DigitalObjects digitalObject, HashMap useTypes, Div lDiv, Div pDiv, Vector idsNotUsed, int order) {
        if (order == 0) {
            lDiv.setORDER(1);
            if (!idsNotUsed.contains(digitalObject.getDigitalObjectId()))
                lDiv.setDMDID("dm" + digitalObject.getDigitalObjectId());
        } else {
            lDiv.setORDER(++order);
            if (!idsNotUsed.contains(digitalObject.getDigitalObjectId()))
                lDiv.setDMDID("dm" + digitalObject.getDigitalObjectId());
        }

        lDiv.setTYPE("item");
        lDiv.setLABEL(digitalObject.getTitle());
        if (digitalObject.getTitle().length() == 0)
            lDiv.setLABEL(digitalObject.getLabel());

        if (StringHelper.isNotEmpty(digitalObject.getObjectType()))
            lDiv.setTYPE(digitalObject.getObjectType());
        Fptr fptr, fptr2 = null;
        Set<FileVersions> fileVersions = digitalObject.getFileVersions();
        Div pDiv2 = new Div();
        if (fileVersions != null && fileVersions.size() > 0) {
            //pDiv2 = new Div();
            pDiv.getContent().add(pDiv2);
            //pDiv2.setORDER(digitalObject.getSequence());
            pDiv2.setORDER(++pOrder);
            pDiv2.setLABEL(digitalObject.getTitle());
            if (!idsNotUsed.contains(digitalObject.getDigitalObjectId()))
                pDiv2.setDMDID("dm" + digitalObject.getDigitalObjectId() + "");
            if (digitalObject.getTitle().length() == 0)
                pDiv2.setLABEL(digitalObject.getLabel());
            pDiv2.setTYPE("page");

        }
        //container for the fptrs
        Div div3 = new Div();
        div3.setTYPE("page");
        div3.setORDER(1);
        if (fileVersions.size() > 0)
            lDiv.getContent().add(div3);

        for (FileVersions fileVersion : fileVersions) {
            fptr = new Fptr();
            fptr.setFILEID("FID" + fileVersion.getFileVersionId() + "");
            //lDiv.getContent ().add (fptr);
            div3.getContent().add(fptr);

            //Div div4 = new Div();
            fptr2 = new Fptr();
            fptr2.setFILEID("FID" + fileVersion.getFileVersionId() + "");
            //div4.setTYPE("page");
            //div4.getContent().add(fptr2);
            //div4.setDMDID(digitalObject.getDigitalObjectId()+"");
            //div4.setORDER(fileVersion.getSequenceNumber());
            pDiv2.getContent().add(fptr2);

            String use = fileVersion.getUseStatement();
            Vector dos = (Vector) useTypes.get(use);
            if (dos == null) {
                dos = new Vector();
                dos.add(fileVersion);
                useTypes.put(use, dos);
            } else {
                dos.add(fileVersion);
            }
        }

        //if(fileVersions!=null&&fileVersions.size()>0)
        //pDiv=pDiv2;

        // place in array list so that children DO are returned to in the
        // correct order by sorting
        ArrayList<DigitalObjects> digitalObjects = new ArrayList<DigitalObjects>(digitalObject.getDigitalObjectChildren());
        Collections.sort(digitalObjects);

        int objectOrder = 0;
        for (DigitalObjects digitalObj : digitalObjects) {

            Div lDiv2 = new Div();
            lDiv.getContent().add(lDiv2);
            handleFileSec(digitalObj, useTypes, lDiv2, pDiv, idsNotUsed, objectOrder);
            //pOrder++;
            objectOrder++;
        }
    }

    public static void main(String args[]) {
        //METSExport dcE = new METSExport();
        //System.out.println(dcE.tagRemover("<this> is a <tag> here"));
        //Object s = new String();
        //System.out.println(s.getClass());
        String val = "English; eng";
        System.out.println("1:" + StringHelper.getFirstPartofLangString(val, ";"));
        System.out.println("2:" + StringHelper.getSecondPartofLangString(val, ";"));

    }
}
