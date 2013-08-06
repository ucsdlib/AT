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
import java.io.Writer;

import java.text.SimpleDateFormat;

import java.util.Set;
import java.util.Date;

import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.archiviststoolkit.importer.ImportException;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.structure.MARCXML.CollectionType;
import org.archiviststoolkit.structure.MARCXML.ControlFieldType;
import org.archiviststoolkit.structure.MARCXML.DataFieldType;
import org.archiviststoolkit.structure.MARCXML.LeaderFieldType;
import org.archiviststoolkit.structure.MARCXML.ObjectFactory;
import org.archiviststoolkit.structure.MARCXML.RecordType;
import org.archiviststoolkit.structure.MARCXML.SubfieldatafieldType;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.StringHelper;


public class MARCExport {
    static JAXBContext context = null;
    static boolean suppressInternalOnly;
    Vector<DataFieldType>  _100, _110, _600, _610, _700, _710;

    public void convertDBRecordToFile(Resources resource, File file, 
                                      InfiniteProgressPanel progressPanel, 
                                      boolean internal) throws ImportException {
        ObjectFactory obj = new ObjectFactory();
        initialize();
        suppressInternalOnly = internal;
        CollectionType collectionType = obj.createCollectionType();
        JAXBElement collection = obj.createCollection(collectionType);
        RecordType record = obj.createRecordType();
        collectionType.getRecord().add(record);
        setLeader(obj, record, resource);
        setControlField(obj, record, resource);
        set040(record, resource);
        set041(record, resource);
        set099(record, resource);
        handle100or110(record, resource);
        addDataField(record, _100);
        addDataField(record, _110);
        set245(record, resource);
        set300(record, resource);
        set351(record, resource);
        set500(record, resource);
        set506(record, resource);
        set520(record, resource);
        set524(record, resource);
        set535(record, resource);
        set540(record, resource);
        set541(record, resource);
        set544(record, resource);
        set545(record, resource);
        set555(record, resource);
        set546(record, resource);
        set561(record, resource);
        set583(record, resource);
        set584(record, resource);
        addDataField(record, _600);
        addDataField(record, _610);
        handle630(record, resource);
        handle650(record, resource);
        handle651(record, resource);
        handle655(record, resource);
        handle656(record, resource);
        handle657(record, resource);
        addDataField(record, _700);
        addDataField(record, _710);
        set852(record, resource);
        set856(record, resource);

        // See if there are any marcxml export helpers for doing further modifications to the exported xml
        ExportHelperFactory helperFactory = ExportHelperFactory.getInstance();
        if(helperFactory.getMarcXMLHelper() != null) {
            helperFactory.getMarcXMLHelper().addInformationToMarcXML(resource, record, progressPanel, internal);
        }

        try {

            context = 
                    JAXBContext.newInstance("org.archiviststoolkit.structure.MARCXML");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            Writer out = new BufferedWriter(osw);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, 
                          "http://www.loc.gov/MARC21/slim http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd");
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(collection, out);
            progressPanel.setTextLine("Export Complete...", 2);
			fos.close();
		} catch (JAXBException jabe) {

            throw new ImportException("Error loading finding aid into memory", 
                                      jabe);
            //jabe.printStackTrace();
        } catch (IOException ioe) {
            throw new ImportException("Error saving file", ioe);
            //ioe.printStackTrace();
        } finally {
		}


    }

    public void convertDBRecordToFileD(DigitalObjects resource, File file, 
                                       InfiniteProgressPanel progressPanel, 
                                       boolean internal) throws ImportException {
        ObjectFactory obj = new ObjectFactory();
        initialize();
        suppressInternalOnly = internal;
        CollectionType collectionType = obj.createCollectionType();
        JAXBElement collection = obj.createCollection(collectionType);
        RecordType record = obj.createRecordType();
        collectionType.getRecord().add(record);
        setLeaderD(obj, record, resource);
        setControlFieldD(obj, record, resource);
        set040D(record, resource);
        set041(record, resource);
        handle100or110(record, resource);
        addDataField(record, _100);
        addDataField(record, _110);
        set245(record,resource);
        set260D(record, resource);
        set300D(record, resource);
        set500(record, resource);
        set506(record, resource);
        set511(record, resource);
        set520(record, resource);
        set524(record, resource);
        set535(record, resource);
        set540(record, resource);
        set541(record, resource);
        set544(record, resource);
        set545(record, resource);
        set546(record, resource);
        set561(record, resource);
        addDataField(record, _600);
        addDataField(record, _610);
        handle630(record, resource);
        handle650(record, resource);
        handle651(record, resource);
        handle655(record, resource);
        handle656(record, resource);
        handle657(record, resource);
        addDataField(record, _700);
        addDataField(record, _710);
        if(resource.getDigitalInstance() != null && resource.getDigitalInstance().getResource()!=null)
        	set852(record, resource.getDigitalInstance().getResource());
        else if(resource.getDigitalInstance() != null && resource.getDigitalInstance().getResourceComponent()!=null)
        	set852(record, resource.getDigitalInstance().getResourceComponent());
        set856D(record, resource);

        try {

            context = 
                    JAXBContext.newInstance("org.archiviststoolkit.structure.MARCXML");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            Writer out = new BufferedWriter(osw);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, 
                          "http://www.loc.gov/MARC21/slim http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd");
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            m.marshal(collection, out);
            out.close();
            progressPanel.setTextLine("Export Complete...", 2);
        } catch (JAXBException jabe) {
            throw new ImportException("Error loading finding aid into memory", 
                                      jabe);
                                      
            //jabe.printStackTrace();
        } catch (IOException ioe) {
            throw new ImportException("Error saving file", ioe);
            //ioe.printStackTrace();
        }


    }

    private void initialize(){
        _100 = new Vector<DataFieldType>();
        _110 = new Vector<DataFieldType>();
        _600 = new Vector<DataFieldType>();
        _610 = new Vector<DataFieldType>();
        _700 = new Vector<DataFieldType>();
        _710 = new Vector<DataFieldType>();
    }
    public static Resources getResourceFromResourceComponent(ResourcesComponents resourceComponent){
    	Resources resource = null;
    	if(resourceComponent.getResource()==null && resourceComponent.getResourceComponentParent()!=null)
    		resource = getResourceFromResourceComponent(resourceComponent.getResourceComponentParent());
    	else if(resourceComponent.getResource()!=null)
    		return resourceComponent.getResource();
    	return resource;
    }
    private void set852(RecordType record, ArchDescription instance) {
    	Resources resource = new Resources();
    	Repositories repository = null;
        if(instance instanceof ResourcesComponents){
        	//resource  = ((ResourcesComponents)instance).getResource();
        	resource  = getResourceFromResourceComponent((ResourcesComponents)instance);
        	if(resource!=null)
        		repository = resource.getRepository();
        }
        else if (instance instanceof Resources){
        		resource = (Resources)instance;
            	if(resource!=null)
            		repository = resource.getRepository();
        }
        
    	DataFieldType dataField = new DataFieldType();
        dataField.setTag("852");
        
        if (repository!=null) {
            createSubField(dataField, "a", 
            		repository.getAgencyCode());
            createSubField(dataField, "b", 
            		repository.getRepositoryName());
        }
        String value = 
            StringHelper.concatenateFields(".", resource.getResourceIdentifier1(), 
                                                resource.getResourceIdentifier2(), 
                                                resource.getResourceIdentifier3(), 
                                                resource.getResourceIdentifier4());
        createSubField(dataField, "c", value);

        if (dataField.getSubfield().size() > 0)
            record.getDatafield().add(dataField);

    }


    private void set856(RecordType record, Resources resource) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("856");
        dataField.setInd1("4");
        dataField.setInd2("2");
        createSubField(dataField, "z", "Finding aid online:");
        createSubField(dataField, "u", resource.getEadFaLocation());

        if (dataField.getSubfield().size() > 0 && 
            resource.getEadFaLocation().length() > 0)
            record.getDatafield().add(dataField);

    }

    private void set856D(RecordType record, DigitalObjects dig) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("856");
        dataField.setInd1("4");
        dataField.setInd2("0");
        createSubField(dataField, "u", dig.getMetsIdentifier());
        if (dataField.getSubfield().size() > 0)
            record.getDatafield().add(dataField);
        else
            handleAllFileVersions(dig,record);

    }
    
    private void handleAllFileVersions(DigitalObjects dig, RecordType record){
        for(FileVersions fv:dig.getFileVersions()){
            DataFieldType dataField = new DataFieldType();
            dataField.setTag("856");
            dataField.setInd1("4");
            dataField.setInd2("0");
            createSubField(dataField,"u",fv.getUri());
            createSubField(dataField,"z",fv.getUseStatement());
            if(dataField.getSubfield().size()>0)
                record.getDatafield().add(dataField);
        }
        for(DigitalObjects digOb:dig.getDigitalObjectChildren()){
            handleAllFileVersions(digOb,record);
        }
    }
    
    private void set245(RecordType record, ArchDescription resource) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("245");
        dataField.setInd1("1");
        createSubField(dataField, "a", resource.getTitle());
        if(!(resource instanceof DigitalObjects))
            createSubField(dataField, "f", resource.getDateExpression());
        if (resource.getDateExpression().length() == 0 && 
            (resource.getDateBegin() != null) && 
            (resource.getDateEnd() != null)) {
            createSubField(dataField, "f", 
                           resource.getDateBegin() + " - " + resource.getDateEnd());
        }
        if (resource instanceof Resources) {
            if (((Resources)resource).getBulkDateBegin() != null && 
                ((Resources)resource).getBulkDateEnd() != null) {
                createSubField(dataField, "g", 
                               ((Resources)resource).getBulkDateBegin() + 
                               " - " + ((Resources)resource).getBulkDateEnd());
            }
        }
        if (dataField.getSubfield().size() > 0)
            record.getDatafield().add(dataField);

    }

    private void set300(RecordType record, Resources resource) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("300");
        if (resource.getLevel().equalsIgnoreCase("item"))
            createSubField(dataField, "a", "1 item");
        else {
            if (resource.getExtentNumber() != null)
                createSubField(dataField, "a", 
                               resource.getExtentNumber() + " " + 
                               resource.getExtentType());
        }
        createSubField(dataField, "f", resource.getContainerSummary());

        if (dataField.getSubfield().size() > 0)
            record.getDatafield().add(dataField);

    }


    private void set300D(RecordType record, DigitalObjects dig) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("300");
        if (dig.getObjectType().length() > 0)
            createSubField(dataField, "a", "1 digital object");
        createSubField(dataField, "b", dig.getObjectType());

        if (dataField.getSubfield().size() > 0)
            record.getDatafield().add(dataField);

    }

    private void set260D(RecordType record, DigitalObjects dig) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("260");
        if (dig.getTitle().length() != 0 || dig.getLabel().length() != 0)
            createSubField(dataField, "c", dig.getDateExpression());
        else if (dig.getTitle().length() == 0 || 
                 dig.getLabel().length() == 0 && 
                 dig.getDateExpression().length() != 0 && 
                 dig.getDateBegin() != null)
            createSubField(dataField, "c", 
                           dig.getDateBegin() + "-" + dig.getDateEnd());

        if (dataField.getSubfield().size() > 0)
            record.getDatafield().add(dataField);

    }

    private void set351(RecordType record, Resources resource) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("351");
        Set<ArchDescriptionNotes> notes = 
            resource.getRepeatingData(ArchDescriptionNotes.class);
        for (ArchDescriptionNotes adrd: notes) {
            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Arrangement note") ||
                adrd.getNotesEtcType().getNotesEtcName().startsWith("File Plan note"))
                createSubFieldNote(dataField, "b", adrd);
        }

        if (dataField.getSubfield().size() > 0)
            record.getDatafield().add(dataField);

    }

    private void set500(RecordType record, ArchDescription resource) {
        int d=0;
        
        Set<ArchDescriptionNotes> notes = 
            resource.getRepeatingData(ArchDescriptionNotes.class);

        DataFieldType dataField[] = new DataFieldType[notes.size()];
        for(int a=0;a<notes.size();a++){
            dataField[a] = new DataFieldType();
            dataField[a].setTag("500");
        }
        
        for (ArchDescriptionNotes adrd: notes) {

            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("General note")) {
                if (adrd.getNoteContent().length() > 0)
                    createSubFieldNote(dataField[d++], "a", adrd);
            }

            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Dimensions")) {
                if (adrd.getNoteContent().length() > 0)
                    createSubFieldNote(dataField[d++], "a", 
                                   "Dimensions: ",adrd);
            }

            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("General Physical Description note")) {
                if (adrd.getNoteContent().length() > 0)
                    createSubFieldNote(dataField[d++], "a", 
                                   "Physical Description Note: " , 
                                   adrd);
            }

            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Material Specific Details note")) {
                if (adrd.getNoteContent().length() > 0)
                    createSubFieldNote(dataField[d++], "a", 
                                   "Material Specific Details:" , 
                                   adrd);
            }

            //if (resource instanceof Resources) {
            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Location note")) {
                if (adrd.getNoteContent().length() > 0)
                    createSubFieldNote(dataField[d++], "a", 
                                   "Location of resource: " , adrd);
            }
            //}
            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Physical Characteristics and Technical Requirements note")) {
                if (adrd.getNoteContent().length() > 0)
                    createSubFieldNote(dataField[d++], "a", 
                                   "Physical Characteristics / Technical Requirements: " , 
                                   adrd);
            }

            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Physical Facet note")) {
                if (adrd.getNoteContent().length() > 0)
                    createSubFieldNote(dataField[d++], "a", 
                                   "Physical Facet: " , adrd);
            }

            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Processing Information note")) {
                if (adrd.getNoteContent().length() > 0)
                    createSubFieldNote(dataField[d++], "a", 
                                   "Processing Information: " , adrd);
            }
            //if (resource instanceof Resources) {
            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Separated Materials note")) {
                if (adrd.getNoteContent().length() > 0)
                    createSubFieldNote(dataField[d++], "a", 
                                   "Materials Separated from the Resource: " , 
                                   adrd);
            }
        }
        //}
        for(int e=0;e<dataField.length;e++){
            if (dataField[e].getSubfield().size() > 0)
                record.getDatafield().add(dataField[e]);
        }

    }

    private void set506(RecordType record, ArchDescription resource) {
        Set<ArchDescriptionNotes> notes = 
            resource.getRepeatingData(ArchDescriptionNotes.class);
        for (ArchDescriptionNotes adrd: notes) {
            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Conditions Governing Access")) {
                DataFieldType dataField = new DataFieldType();
                dataField.setTag("506");            
                createSubFieldNote(dataField, "a", adrd);
                if (dataField.getSubfield().size() > 0)
                    record.getDatafield().add(dataField);
            }
        }
    }

    private void set511(RecordType record, ArchDescription resource) {
        Set<ArchDescriptionNotes> notes = 
            resource.getRepeatingData(ArchDescriptionNotes.class);
        for (ArchDescriptionNotes adrd: notes) {
            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Contributors Note")) {
                DataFieldType dataField = new DataFieldType();
                dataField.setTag("511");
                createSubFieldNote(dataField, "a", adrd);
                if (dataField.getSubfield().size() > 0)
                    record.getDatafield().add(dataField);
            }


        }
    }

    private void set520(RecordType record, ArchDescription resource) {


        Set<ArchDescriptionNotes> notes = 
            resource.getRepeatingData(ArchDescriptionNotes.class);

        for (ArchDescriptionNotes adrd: notes) {
            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Abstract") || adrd.getNotesEtcType().getNotesEtcName().startsWith("Scope and Contents note")) {
                DataFieldType dataField = new DataFieldType();
                dataField.setTag("520");
                createSubFieldNote(dataField, "a", adrd);
                if (dataField.getSubfield().size() > 0)
                    record.getDatafield().add(dataField);
            }
        }
    }

    private void set524(RecordType record, ArchDescription resource) {

        Set<ArchDescriptionNotes> notes = 
            resource.getRepeatingData(ArchDescriptionNotes.class);
        for (ArchDescriptionNotes adrd: notes) {
            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Preferred Citation")) {
                DataFieldType dataField = new DataFieldType();
                dataField.setTag("524");
                dataField.setInd1("8");
                createSubFieldNote(dataField, "a", adrd);
                if (dataField.getSubfield().size() > 0)
                    record.getDatafield().add(dataField);
            }
        }


    }

    private void set535(RecordType record, ArchDescription resource) {

        Set<ArchDescriptionNotes> notes = 
            resource.getRepeatingData(ArchDescriptionNotes.class);

        for (ArchDescriptionNotes adrd: notes) {

            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Existence and Location of Copies note") ||
                (adrd.getNotesEtcType().getNotesEtcName().startsWith("Existence and Location of Originals note"))) {
                DataFieldType dataField = new DataFieldType();
                dataField.setTag("535");
                dataField.setInd1("1");
                createSubFieldNote(dataField, "a", adrd);
                if (dataField.getSubfield().size() > 0)
                    record.getDatafield().add(dataField);
            }
        }


    }

    private void set540(RecordType record, ArchDescription resource) {

        Set<ArchDescriptionNotes> notes = 
            resource.getRepeatingData(ArchDescriptionNotes.class);
        for (ArchDescriptionNotes adrd: notes) {
            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Conditions Governing Use") ||
                (adrd.getNotesEtcType().getNotesEtcName().startsWith("Legal Status note"))) {
                DataFieldType dataField = new DataFieldType();
                dataField.setTag("540");
                dataField.setInd1("1");
                createSubFieldNote(dataField, "a", adrd);
                if (dataField.getSubfield().size() > 0)
                    record.getDatafield().add(dataField);
            }
        }


    }

    private void set541(RecordType record, ArchDescription resource) {
        DataFieldType dataField;
        Set<ArchDescriptionNotes> notes = 
            resource.getRepeatingData(ArchDescriptionNotes.class);
        for (ArchDescriptionNotes adrd: notes) {
            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Immediate Source of Acquisition")) {
                dataField = new DataFieldType();
                dataField.setTag("541");
                if (adrd.getInternalOnly())
                    dataField.setInd1("0");
                else
                    dataField.setInd1("1");
                createSubFieldNote(dataField, "a", adrd);

                if (dataField.getSubfield().size() > 0)
                    record.getDatafield().add(dataField);
            
            }
        }


    }

    private void set544(RecordType record, ArchDescription resource) {

        Set<ArchDescriptionNotes> notes = 
            resource.getRepeatingData(ArchDescriptionNotes.class);
        for (ArchDescriptionNotes adrd: notes) {
            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Related Archival Materials note")) {
                DataFieldType dataField = new DataFieldType();
                dataField.setTag("544");
                createSubFieldNote(dataField, "a", adrd);
                if (dataField.getSubfield().size() > 0)
                    record.getDatafield().add(dataField);
            }
        }


    }

    private void set545(RecordType record, ArchDescription resource) {

        Set<ArchDescriptionNotes> notes = 
            resource.getRepeatingData(ArchDescriptionNotes.class);
        for (ArchDescriptionNotes adrd: notes) {
            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Biographical/Historical note")) {
                DataFieldType dataField = new DataFieldType();
                dataField.setTag("545");
                dataField.setInd1(" ");
                createSubFieldNote(dataField, "a", adrd);
                if (dataField.getSubfield().size() > 0)
                    record.getDatafield().add(dataField);
            }
        }



    }

    private void set546(RecordType record, ArchDescription resource) {

        Set<ArchDescriptionNotes> notes = 
            resource.getRepeatingData(ArchDescriptionNotes.class);
        for (ArchDescriptionNotes adrd: notes) {
            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Language of Materials note")) {
                DataFieldType dataField = new DataFieldType();
                dataField.setTag("546");
                createSubFieldNote(dataField, "a", adrd);
                if (dataField.getSubfield().size() > 0)
                    record.getDatafield().add(dataField);
            }
        }


    }

    private void set555(RecordType record, Resources resource) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("555");
        createSubField(dataField, "a", "Finding Aid Available Online: ");
        createSubField(dataField, "u", resource.getEadFaLocation());
        if (dataField.getSubfield().size() > 0 && 
            resource.getEadFaLocation().length() > 0)
            record.getDatafield().add(dataField);

    }

    private void set561(RecordType record, ArchDescription resource) {

        Set<ArchDescriptionNotes> notes = 
            resource.getRepeatingData(ArchDescriptionNotes.class);

        for (ArchDescriptionNotes adrd: notes) {
            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Custodial History")) {
                DataFieldType dataField = new DataFieldType();
                dataField.setTag("561");
                if (adrd.getInternalOnly())
                    dataField.setInd1("0");
                else
                    dataField.setInd1("1");
                createSubFieldNote(dataField, "a", adrd);
                if (dataField.getSubfield().size() > 0)
                    record.getDatafield().add(dataField);
            }
        }


    }

    private void set583(RecordType record, Resources resource) {
        Set<ArchDescriptionNotes> notes = 
            resource.getRepeatingData(ArchDescriptionNotes.class);
        for (ArchDescriptionNotes adrd: notes) {
            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Appraisal note")) {
                DataFieldType dataField = new DataFieldType();
                dataField.setTag("583");
                if (adrd.getInternalOnly())
                    dataField.setInd1("0");
                else
                    dataField.setInd1("1");
                createSubFieldNote(dataField, "a", adrd);
                if (dataField.getSubfield().size() > 0)
                    record.getDatafield().add(dataField);
            }
        }


    }

    private void set584(RecordType record, Resources resource) {
        Set<ArchDescriptionNotes> notes = 
            resource.getRepeatingData(ArchDescriptionNotes.class);

        for (ArchDescriptionNotes adrd: notes) {
            if (adrd.getNotesEtcType().getNotesEtcName().startsWith("Accruals note")) {
                DataFieldType dataField = new DataFieldType();
                dataField.setTag("584");            
                createSubFieldNote(dataField, "a", adrd);
                if (dataField.getSubfield().size() > 0)
                    record.getDatafield().add(dataField);
            }
        }


    }

    private void set100(RecordType record, ArchDescriptionNames name) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("100");
        if (name.getName().getPersonalDirectOrder())
            dataField.setInd1("0");
        else
            dataField.setInd1("1");

        if (name.getName().getNameType().equalsIgnoreCase("family"))
            dataField.setInd1("3");

        if (name.getName().getNameType().equalsIgnoreCase("person")) {
            if (!(name.getName().getPersonalDirectOrder())){
                createSubField(dataField, "a", 
                               StringHelper.concatenateFields(", ",name.getName().getPersonalPrimaryName(),name.getName().getPersonalRestOfName()));
            }else
                createSubField(dataField, "a", 
                               StringHelper.concatenateFields(" ",name.getName().getPersonalPrimaryName(),name.getName().getPersonalRestOfName()));
        } else if (name.getName().getNameType().equalsIgnoreCase("family")) {
            //createSubField(dataField, "a", 
                           //StringHelper.concatenateAllFields(" ",name.getName().getFamilyName(),"family"));
        	createSubField(dataField, "a", name.getName().getFamilyName());
        }
        createSubField(dataField, "b", name.getName().getNumber());

        if (name.getName().getNameType().equalsIgnoreCase("person")) {
            createSubField(dataField, "c", 
                           StringHelper.concatenateFields(", ",name.getName().getPersonalPrefix(),name.getName().getPersonalTitle(),name.getName().getPersonalSuffix()));
        } else {
            createSubField(dataField, "c", 
                           name.getName().getFamilyNamePrefix());
        }

        if (name.getName().getNameType().equalsIgnoreCase("person")) {
            createSubField(dataField, "d", name.getName().getPersonalDates());
            createSubField(dataField, "q", 
                           name.getName().getPersonalFullerForm());
        }
        if (name.getRole().length() == 0)
            createSubField(dataField, "e", "creator");
        else
            createSubField(dataField, "e", name.getRole());
            
        createSubField(dataField, "g", name.getName().getQualifier());

        if (dataField.getSubfield().size() > 0)
            _100.add(dataField);

            //record.getDatafield().add(dataField);

    }

    private void set110(RecordType record, ArchDescriptionNames name) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("110");
        dataField.setInd1("2");
        createSubField(dataField, "a", 
                       name.getName().getCorporatePrimaryName());
        createSubField(dataField, "b", 
                       name.getName().getCorporateSubordinate1());
        createSubField(dataField, "b", 
                       name.getName().getCorporateSubordinate2());
        createSubField(dataField, "n", name.getName().getNumber());
        createSubField(dataField, "g", name.getName().getQualifier());
        if (name.getRole().length() == 0)
            createSubField(dataField, "e", "creator");
        else
            createSubField(dataField, "e", name.getRole());
        if (dataField.getSubfield().size() > 0)
            _110.add(dataField);
            //record.getDatafield().add(dataField);

    }

    private void handle100or110(RecordType record, ArchDescription resource) {
        Set<ArchDescriptionNames> adns = resource.getNamesForPrinting();
        System.out.println("here100  or  100");
        boolean in = false;
        for (ArchDescriptionNames aname: adns) {

            if (aname.getNameLinkFunction().equalsIgnoreCase("creator")) {
                if ((aname.getName().getNameType().equalsIgnoreCase("person") || 
                     aname.getName().getNameType().equalsIgnoreCase("family")) && 
                    (!in)) {
                    System.out.println("here 100");
                    set100(record, aname);
                    in = true;
                } else if ((aname.getName().getNameType().equalsIgnoreCase("person") || 
                            aname.getName().getNameType().equalsIgnoreCase("family")) && 
                           (in)) {
                    System.out.println(("here700"));
                    set700(record, aname);
                } else if (aname.getName().getNameType().equalsIgnoreCase("corporate body") && 
                           (!in)) {
                    System.out.println(("here110"));
                    set110(record, aname);
                    in = true;
                } else if (aname.getName().getNameType().equalsIgnoreCase("corporate body") && 
                           (in)) {
                    System.out.println(("here710"));
                    set710(record, aname);
                }
            } else if (aname.getNameLinkFunction().equalsIgnoreCase("subject")) {
                if (aname.getName().getNameType().equalsIgnoreCase("person") || 
                    aname.getName().getNameType().equalsIgnoreCase("family")) {
                    System.out.println("here600");
                    set600(record, aname);
                } else if (aname.getName().getNameType().equalsIgnoreCase("corporate body")) {
                    System.out.println(("here610"));
                    set610(record, aname);
                }
            } else if (aname.getNameLinkFunction().equalsIgnoreCase("source")) {
                    if (aname.getName().getNameType().equalsIgnoreCase("person") || 
                        aname.getName().getNameType().equalsIgnoreCase("family")) {
                        System.out.println("here700");
                        set700(record, aname);
                    } else if (aname.getName().getNameType().equalsIgnoreCase("corporate body")) {
                        System.out.println(("here710"));
                        set710(record, aname);
                    }
                }        
        }
    }

    private void createSubFieldNote(DataFieldType dataField, String subCode, 
                                    ArchDescriptionNotes value) {

        if (suppressInternalOnly == true && value.getInternalOnly() == true)
            return;
        else {
            createSubField(dataField, subCode, value.getNoteContent());
        }
    }

    private void createSubFieldNote(DataFieldType dataField, String subCode, 
                                    String notePrefix, 
                                    ArchDescriptionNotes value) {

        if (suppressInternalOnly == true && value.getInternalOnly() == true)
            return;
        else {
            createSubField(dataField, subCode, notePrefix+value.getNoteContent());
        }


    }
    private void createSubField(DataFieldType dataField, String subCode, 
                                String subFieldValue) {

        //converts all incoming subject sources from ingest to local
        if (subCode.equals("2") && subFieldValue.equals("ingest"))
            subFieldValue = "local";

        if (subFieldValue != null && subFieldValue.length() > 0) {
            subFieldValue.trim();
            subFieldValue = StringHelper.tagRemover(subFieldValue);
            if (null == dataField.getInd1())
                dataField.setInd1(" ");
            if (null == dataField.getInd2())
                dataField.setInd2(" ");
            SubfieldatafieldType subField = new SubfieldatafieldType();
            subField.setValue(subFieldValue);
            subField.setCode(subCode);
            dataField.getSubfield().add(subField);
        }
    }

    private void set040(RecordType record, Resources resource) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("040");
        if (resource.getRepository() != null && 
            resource.getRepository().getAgencyCode() != null && 
            resource.getRepository().getAgencyCode().length() > 0) {
            createSubField(dataField, "a", 
                           resource.getRepository().getAgencyCode());
                           
            String lang = resource.getRepository().getDescriptiveLanguage();
            String langString = LookupListUtils.getLookupListCodeFromItem(Repositories.class,"descriptiveLanguage",lang);
            if(StringHelper.isNotEmpty(langString)){
                createSubField(dataField, "b",langString);            
            }            

            createSubField(dataField, "c", 
                           resource.getRepository().getAgencyCode());
        }
        createSubField(dataField, "e", "dacs");
        if (dataField.getSubfield().size() > 0)
            record.getDatafield().add(dataField);
    }


    private void set040D(RecordType record, DigitalObjects dig) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("040");
        if (dig.getDigitalInstance() != null && dig.getDigitalInstance().getResource() != null && 
            dig.getDigitalInstance().getResource().getRepository().getAgencyCode() != 
            null && 
            dig.getDigitalInstance().getResource().getRepository().getAgencyCode().length() > 
            0) {
            createSubField(dataField, "a", 
                           dig.getDigitalInstance().getResource().getRepository().getAgencyCode());
            String lang = dig.getDigitalInstance().getResource().getRepository().getDescriptiveLanguage();
            String langString = LookupListUtils.getLookupListCodeFromItem(Repositories.class,"descriptiveLanguage",lang);
            if(StringHelper.isNotEmpty(langString)){
                createSubField(dataField, "b",langString);            
            }
            createSubField(dataField, "c", 
                           dig.getDigitalInstance().getResource().getRepository().getAgencyCode());
        }
        createSubField(dataField, "e", "aacr");
        if (dataField.getSubfield().size() > 0)
            record.getDatafield().add(dataField);
    }


    private void set041(RecordType record, ArchDescription resource) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("041");
        dataField.setInd1("0");
        dataField.setInd2(" ");
        String langCode = "";
        String langCode1 = "";
        if(resource instanceof DigitalObjects){
            langCode = ((DigitalObjects)resource).getLanguageCode();
            langCode1 = LookupListUtils.getLookupListCodeFromItem(DigitalObjects.class,"languageCode",langCode);

        }
        else if(resource instanceof Resources){
            langCode = ((Resources)resource).getLanguageCode();
            langCode1 = LookupListUtils.getLookupListCodeFromItem(DigitalObjects.class,"languageCode",langCode);

        }
        
        if(StringHelper.isNotEmpty(langCode1))
            createSubField(dataField, "a", langCode1);
        if (dataField.getSubfield().size() > 0)
            record.getDatafield().add(dataField);
    }

    private void set099(RecordType record, Resources resource) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("099");
        String value = 
            StringHelper.concatenateFields(".", resource.getResourceIdentifier1(), 
                                           resource.getResourceIdentifier2(), 
                                           resource.getResourceIdentifier3(), 
                                           resource.getResourceIdentifier4());
        createSubField(dataField, "a", value);
        if (dataField.getSubfield().size() > 0)
            record.getDatafield().add(dataField);
    }


    private void setControlField(ObjectFactory obj, RecordType record, 
                                 Resources resource) {
        ControlFieldType controlField = obj.createControlFieldType();
        controlField.setTag("008");
        StringBuffer controlFieldString = new StringBuffer();
        Date t = resource.getAuditInfo().getLastUpdated();
        if (t == null)
            resource.getAuditInfo().getCreated();

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String dateString = sdf.format(t);

        if (dateString.length() == 6)
            controlFieldString.append(dateString);
        else
            controlFieldString.append("      ");

        boolean aggregateResource = true; // specify if resource record is aggregated or not

        if (resource.getLevel().equalsIgnoreCase("item")) {
            controlFieldString.append("s");
            aggregateResource = false;
        } else
            controlFieldString.append("i");

        if (resource.getDateBegin() != null)
            controlFieldString.append(resource.getDateBegin());
        else
            controlFieldString.append("    ");

        if (resource.getDateEnd() != null && (aggregateResource || !resource.getDateEnd().equals(resource.getDateBegin())))
            controlFieldString.append(resource.getDateEnd());
        else
            controlFieldString.append("    ");

        controlFieldString.append("xx");
        controlFieldString.append("                  ");

        String langAbbrev = "";
        String lang = resource.getLanguageCode();
        if (StringHelper.isNotEmpty(lang)) {
            langAbbrev = LookupListUtils.getLookupListCodeFromItem(Resources.class,"languageCode",lang);            
        } 
        if(StringHelper.isNotEmpty(langAbbrev)&&langAbbrev.length()==3){
            controlFieldString.append(langAbbrev);
        }
        else
            controlFieldString.append("   ");
            
        controlFieldString.append(" d");

        controlField.setValue(controlFieldString.toString());
        record.getControlfield().add(controlField);


    }


    private void setLeaderD(ObjectFactory obj, RecordType record, 
                            DigitalObjects dig) {
        LeaderFieldType leaderType = obj.createLeaderFieldType();
        StringBuffer leaderString = new StringBuffer();
        leaderString.append("00000");
        leaderString.append("n");
        if (dig.getObjectType().equals("text"))
            leaderString.append("a");
        else if (dig.getObjectType().equals("notated music"))
            leaderString.append("c");
        else if (dig.getObjectType().equals("cartographic"))
            leaderString.append("e");
        else if (dig.getObjectType().equals("moving image"))
            leaderString.append("g");
        else if (dig.getObjectType().equals("still image"))
            leaderString.append("g");
        else if (dig.getObjectType().equals("three dimensional object"))
            leaderString.append("g");
        else if (dig.getObjectType().equals("sound recording"))
            leaderString.append("i");
        else if (dig.getObjectType().equals("sound recording-nonmusical"))
            leaderString.append("i");
        else if (dig.getObjectType().equals("sound recording-music"))
            leaderString.append("j");
        else if (dig.getObjectType().equals("software, multimedia"))
            leaderString.append("m");
        else if (dig.getObjectType().equals("mixed material"))
            leaderString.append("p");
        else
            leaderString.append("p"); //TODO - this is placeholder to make valid
        leaderString.append("m a2200000 u 4500");
        leaderType.setValue(leaderString.toString());
        record.setLeader(leaderType);
    }

    private void setLeader(ObjectFactory obj, RecordType record, 
                           Resources resource) {
        LeaderFieldType leaderType = obj.createLeaderFieldType();
        StringBuffer leaderString = new StringBuffer();
        leaderString.append("00000");
        leaderString.append("n");
        leaderString.append("p");
        if (resource.getLevel().equalsIgnoreCase("item"))
            leaderString.append("m");
        else
            leaderString.append("c");
        leaderString.append(" a2200000 u 4500");
        leaderType.setValue(leaderString.toString());
        record.setLeader(leaderType);
    }

    private void setControlFieldD(ObjectFactory obj, RecordType record, 
                                  DigitalObjects dig) {
    	String dateString = "";
    	ControlFieldType controlField = obj.createControlFieldType();
        controlField.setTag("008");
        StringBuffer controlFieldString = new StringBuffer();
        Date t = dig.getAuditInfo().getLastUpdated();
        if (t == null)
        	t = dig.getAuditInfo().getCreated();
        if(t!=null){
        	SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        	dateString = sdf.format(t);
        }
        if (dateString.length() == 6)
            controlFieldString.append(dateString);
        else
            controlFieldString.append("      ");

        boolean aggregateResource = true; // specify if resource record is aggregated or not

        if (dig.getDigitalInstance() != null && dig.getDigitalInstance().getResource()!=null && dig.getDigitalInstance().getResource().getLevel().equalsIgnoreCase("item")) {
            controlFieldString.append("s");
            aggregateResource = false;
        } else
            controlFieldString.append("i");

        if (dig.getDateBegin() != null)
            controlFieldString.append(dig.getDateBegin());
        else
            controlFieldString.append("    ");

        if (dig.getDateEnd() != null && (aggregateResource || !dig.getDateEnd().equals(dig.getDateBegin())))
            controlFieldString.append(dig.getDateEnd());
        else
            controlFieldString.append("    ");


        controlFieldString.append("xx");
        controlFieldString.append("                  ");

        String langAbbrev = "";
        String lang = dig.getLanguageCode();
        if (StringHelper.isNotEmpty(lang)) {
            langAbbrev = LookupListUtils.getLookupListCodeFromItem(DigitalObjects.class,"languageCode",lang);            
        } 
        if(StringHelper.isNotEmpty(langAbbrev)&&langAbbrev.length()==3){
            controlFieldString.append(langAbbrev);
        }
        else
            controlFieldString.append("   ");

        controlFieldString.append(" d");
        controlField.setValue(controlFieldString.toString());
        record.getControlfield().add(controlField);
    }


    private void set600(RecordType record, ArchDescriptionNames name) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("600");

        if (name.getName().getPersonalDirectOrder())
            dataField.setInd1("0");
        else
            dataField.setInd1("1");

        if (name.getName().getNameType().equalsIgnoreCase("family"))
            dataField.setInd1("3");
        setIndicatorFromSource(dataField, name.getName().getNameSource());


        if (name.getName().getNameType().equalsIgnoreCase("person")) {
            System.out.println("directOrd"+name.getName().getPersonalDirectOrder()+"--"+name.getName().getPersonalPrimaryName());
            if (!(name.getName().getPersonalDirectOrder())){
                System.out.println("A this order with comma");
                createSubField(dataField, "a", 
                               StringHelper.concatenateFields(", ",name.getName().getPersonalPrimaryName(),name.getName().getPersonalRestOfName()));
            }
            else{
                createSubField(dataField, "a", 
                               StringHelper.concatenateFields(" ",name.getName().getPersonalPrimaryName(),name.getName().getPersonalRestOfName()));
                System.out.println("A this order with no comma");
            }
        } else if (name.getName().getNameType().equalsIgnoreCase("family")) {
            //createSubField(dataField, "a", 
                          //StringHelper.concatenateAllFields(" ",name.getName().getFamilyName(),"family"));
            createSubField(dataField, "a", name.getName().getFamilyName());
        }
        createSubField(dataField, "b", name.getName().getNumber());

        if (name.getName().getNameType().equalsIgnoreCase("person")) {
            createSubField(dataField, "c", 
                           StringHelper.concatenateFields(", ",name.getName().getPersonalPrefix(),name.getName().getPersonalTitle(),name.getName().getPersonalSuffix()));
        } else {
            createSubField(dataField, "c", 
                           name.getName().getFamilyNamePrefix());
        }

        if (name.getName().getNameType().equalsIgnoreCase("person")) {
            createSubField(dataField, "d", name.getName().getPersonalDates());
            createSubField(dataField, "q", 
                           name.getName().getPersonalFullerForm());
        }
        createSubField(dataField, "g", name.getName().getQualifier());
        createSubField(dataField, "e", name.getRole());

        if (dataField.getInd2().equalsIgnoreCase("7"))
            createSubField(dataField, "2", name.getName().getNameSource());

        if (dataField.getSubfield().size() > 0)
            _600.add(dataField);
            //record.getDatafield().add(dataField);

    }

    private void set700(RecordType record, ArchDescriptionNames name) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("700");
        if (name.getName().getPersonalDirectOrder())
            dataField.setInd1("0");
        else
            dataField.setInd1("1");

        if (name.getName().getNameType().equalsIgnoreCase("family"))
            dataField.setInd1("3");
        //setIndicatorFromSource(dataField, name.getName().getNameSource());


        if (name.getName().getNameType().equalsIgnoreCase("person")) {
            if (!(name.getName().getPersonalDirectOrder()))
                createSubField(dataField, "a", 
                               StringHelper.concatenateFields(", ", 
                                                              name.getName().getPersonalPrimaryName(), 
                                                              name.getName().getPersonalRestOfName()));
            else
                createSubField(dataField, "a", 
                               StringHelper.concatenateFields(" ",name.getName().getPersonalPrimaryName(),name.getName().getPersonalRestOfName()));
        } else if (name.getName().getNameType().equalsIgnoreCase("family")) {
            
            //createSubField(dataField, "a", 
                           //StringHelper.concatenateAllFields(" ",name.getName().getFamilyName(),"family"));
            createSubField(dataField, "a", name.getName().getFamilyName());            
            
        }
        createSubField(dataField, "b", name.getName().getNumber());

        if (name.getName().getNameType().equalsIgnoreCase("person")) {System.out.println("Size = "+name.getName().getPersonalPrefix().length());
            createSubField(dataField, "c", 
                           StringHelper.concatenateFields(", ",name.getName().getPersonalPrefix(),name.getName().getPersonalTitle(),name.getName().getPersonalSuffix()));
        } else {
            createSubField(dataField, "c", 
                           name.getName().getFamilyNamePrefix());
        }

        if (name.getName().getNameType().equalsIgnoreCase("person")) {
            createSubField(dataField, "d", name.getName().getPersonalDates());
            createSubField(dataField, "q", 
                           name.getName().getPersonalFullerForm());
        }
        createSubField(dataField, "g", name.getName().getQualifier());
        //createSubField(dataField, "e", name.getRole());

        if (!(StringHelper.isNotEmpty(name.getRole()))){

        if (name.getNameLinkFunction().equalsIgnoreCase("creator"))
            createSubField(dataField, "e", "creator");
        else if (name.getNameLinkFunction().equalsIgnoreCase("source"))
            createSubField(dataField, "e", "donor");
        }
        else{
            createSubField(dataField, "e", name.getRole());
        }
        
        if (dataField.getSubfield().size() > 0)
            _700.add(dataField);
            //record.getDatafield().add(dataField);


    }

    private void addDataField(RecordType record,Vector<DataFieldType> dataFields){
        for(DataFieldType dataField:dataFields){
            record.getDatafield().add(dataField);
        }
    }
    private void set610(RecordType record, ArchDescriptionNames name) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("610");
        dataField.setInd1("2");
        setIndicatorFromSource(dataField, name.getName().getNameSource());
        createSubField(dataField, "a", 
                       name.getName().getCorporatePrimaryName());
        createSubField(dataField, "b", 
                       name.getName().getCorporateSubordinate1());
        createSubField(dataField, "b", 
                       name.getName().getCorporateSubordinate2());
        createSubField(dataField, "n", name.getName().getNumber());
        createSubField(dataField, "g", name.getName().getQualifier());
        createSubField(dataField, "e", name.getRole());
        if (dataField.getInd2().equals("7"))
            createSubField(dataField, "2", name.getName().getNameSource());
        if (dataField.getSubfield().size() > 0)
            _610.add(dataField);
            //record.getDatafield().add(dataField);
    }

    private void set710(RecordType record, ArchDescriptionNames name) {
        DataFieldType dataField = new DataFieldType();

        dataField.setTag("710");
        dataField.setInd1("2");
        //setIndicatorFromSource(dataField, name.getName().getNameSource());
        createSubField(dataField, "a", 
                       name.getName().getCorporatePrimaryName());
        createSubField(dataField, "b", 
                       name.getName().getCorporateSubordinate1());
        createSubField(dataField, "b", 
                       name.getName().getCorporateSubordinate2());
        createSubField(dataField, "n", name.getName().getNumber());
        createSubField(dataField, "g", name.getName().getQualifier());

        if (name.getRole().length() == 0)
            createSubField(dataField, "e", "creator");
        else if (name.getNameLinkFunction().equalsIgnoreCase("creator") ||
                 name.getNameLinkFunction().equalsIgnoreCase("donor"))
            createSubField(dataField, "e", "creator");
        else if (name.getNameLinkFunction().equalsIgnoreCase("source"))
            createSubField(dataField, "e", "source");
        else
            createSubField(dataField, "e", name.getRole());
        if (dataField.getSubfield().size() > 0)
            _710.add(dataField);
            //record.getDatafield().add(dataField);
    }


    private void handle630(RecordType record, ArchDescription resource) {
        for (ArchDescriptionSubjects subject: resource.getSubjectsForPrinting()) {
            if (subject.getSubject().getSubjectTermType().equals("Uniform Title (630)"))
                set630(record, subject.getSubject());
        }
    }

    private void set630(RecordType record, Subjects subject) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("630");
        setIndicatorFromSource(dataField, subject.getSubjectSource());
        createSubField(dataField, "a", subject.getSubjectTerm());
        if (dataField.getInd2().equals("7"))
            createSubField(dataField, "2", subject.getSubjectSource());
        if (dataField.getSubfield().size() > 0)
            record.getDatafield().add(dataField);

    }

    private void handle650(RecordType record, ArchDescription resource) {
        for (ArchDescriptionSubjects subject: resource.getSubjectsForPrinting()) {
            if (subject.getSubject().getSubjectTermType().startsWith("Topical Term (650)"))
                set650(record, subject.getSubject());
        }
    }

    private void set650(RecordType record, Subjects subject) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("650");
        setIndicatorFromSource(dataField, subject.getSubjectSource());
        System.out.println("Source = "+subject.getSubjectSource());
        createSubField(dataField, "a", subject.getSubjectTerm());
        if (dataField.getInd2().equals("7"))
            createSubField(dataField, "2", subject.getSubjectSource());
        if (dataField.getSubfield().size() > 0)
            record.getDatafield().add(dataField);


    }

    private void handle651(RecordType record, ArchDescription resource) {
        for (ArchDescriptionSubjects subject: resource.getSubjectsForPrinting()) {
            if (subject.getSubject().getSubjectTermType().startsWith("Geographic Name (651)"))
                set651(record, subject.getSubject());
        }
    }

    private void set651(RecordType record, Subjects subject) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("651");
        setIndicatorFromSource(dataField, subject.getSubjectSource());
        createSubField(dataField, "a", subject.getSubjectTerm());
        if (dataField.getInd2().equals("7"))
            createSubField(dataField, "2", subject.getSubjectSource());
        if (dataField.getSubfield().size() > 0)
            record.getDatafield().add(dataField);

    }

    private void handle655(RecordType record, ArchDescription resource) {
        for (ArchDescriptionSubjects subject: resource.getSubjectsForPrinting()) {
            if (subject.getSubject().getSubjectTermType().startsWith("Genre / Form (655)"))
                set655(record, subject.getSubject());
        }
    }

    private void set655(RecordType record, Subjects subject) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("655");
        setIndicatorFromSource(dataField, subject.getSubjectSource());
        createSubField(dataField, "a", subject.getSubjectTerm());
        if (dataField.getInd2().equals("7"))
            createSubField(dataField, "2", subject.getSubjectSource());
        if (dataField.getSubfield().size() > 0)
            record.getDatafield().add(dataField);

    }

    private void handle656(RecordType record, ArchDescription resource) {
        for (ArchDescriptionSubjects subject: resource.getSubjectsForPrinting()) {
            if (subject.getSubject().getSubjectTermType().startsWith("Occupation (656)"))
                set656(record, subject.getSubject());
        }
    }

    private void set656(RecordType record, Subjects subject) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("656");
        setIndicatorFromSource(dataField, subject.getSubjectSource());
        createSubField(dataField, "a", subject.getSubjectTerm());
        if (dataField.getInd2().equals("7"))
            createSubField(dataField, "2", subject.getSubjectSource());
        if (dataField.getSubfield().size() > 0)
            record.getDatafield().add(dataField);


    }

    private void handle657(RecordType record, ArchDescription resource) {
        for (ArchDescriptionSubjects subject: resource.getSubjectsForPrinting()) {
            if (subject.getSubject().getSubjectTermType().startsWith("Function (657)"))
                set657(record, subject.getSubject());
        }
    }

    private void set657(RecordType record, Subjects subject) {
        DataFieldType dataField = new DataFieldType();
        dataField.setTag("657");
        setIndicatorFromSource(dataField, subject.getSubjectSource());
        createSubField(dataField, "a", subject.getSubjectTerm());
        if (dataField.getInd2().equals("7"))
            createSubField(dataField, "2", subject.getSubjectSource());
        if (dataField.getSubfield().size() > 0)
            record.getDatafield().add(dataField);

    }

    private String setIndicatorFromSource(DataFieldType dataField, 
                                          String value) {

        if (value == null) {
            dataField.setInd2("4");
            return "4";
        }

        if (value.startsWith("Library of Congress Subject Headings")) {
            dataField.setInd2("0");
            return "0";
        } else if (value.startsWith("Library of Congress Name Authority File")) {
            dataField.setInd2("0");
            return "0";
        } else if (value.startsWith("LC subject headings for children's literature")) {
            dataField.setInd2("1");
            return "1";
        } else if (value.startsWith("Medical Subject Headings")) {
            dataField.setInd2("2");
            return "2";
        } else if (value.startsWith("National Agricultural Library subject authority file")) {
            dataField.setInd2("3");
            return "3";
        //} else if (value.startsWith("local") || value.length() == 0) {
            //dataField.setInd2("4");
            //return "4";
        } else if (value.length() == 0) {
            dataField.setInd2("4");
            return "4";       
        } else if (value.startsWith("Canadian Subject Headings")) {
            dataField.setInd2("5");
            return "5";
        } else if (value.startsWith("R�pertoire de vedettes-mati�re")) {
            dataField.setInd2("6");
            return "6";
        } else {
            dataField.setInd2("7");
            return "7";
        }
    }

    public static void main(String[] args) {
		System.out.println(StringHelper.concatenateAllFields(" ", "this is","a","one"));
    }

}