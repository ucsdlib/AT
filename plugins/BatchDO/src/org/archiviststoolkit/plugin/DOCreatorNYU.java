package org.archiviststoolkit.plugin;

import org.archiviststoolkit.plugin.utils.ImportExportUtils;
import org.archiviststoolkit.plugin.utils.WebServiceHelper;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.util.NoteEtcTypesUtils;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.SequencedObject;
import org.archiviststoolkit.structure.NotesEtcTypes;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Archivists' Toolkit(TM) Copyright � 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
 * All rights reserved.
 * <p/>
 * This software is free. You can redistribute it and / or modify it under the terms of the Educational Community License (ECL)
 * version 1.0 (http://www.opensource.org/licenses/ecl1.php)
 * <p/>
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the ECL license for more details about permissions and limitations.
 * <p/>
 * <p/>
 * Archivists' Toolkit(TM)
 * http://www.archiviststoolkit.org
 * info@archiviststoolkit.org
 * <p/>
 * Created by IntelliJ IDEA.
 * <p/>
 * Simple class to import digital record and attach them a select resource record
 *
 * @author: Nathan Stevens
 * @author: Jason Varghese
 * Date: Apr 20, 2009
 * Time: 2:20:41 PM
 */
public class DOCreatorNYU {
    // flag to set if this is running is debug mode
    private boolean debug = false;

    // stores all resource components for a given resource
    // component unique identifier is the key, resource component is the value.
    private HashMap<String, ResourcesComponents> resourceComponents = null;

    // this monitor is used to alert the user to the progress of the program
    private InfiniteProgressPanel monitor = null;

    // this string builder is used for storing the import log
    StringBuilder sb = null;

    // String that specify the version of the AT this plugin is running in.
    // It is needed so that we know what methods to call on the digital objects
    String atVersionNumber = "";

    // This are used to specify whether the resource components in the resourceComponent hash
    // is keyed by box-folder or persistent ID
    private final String BOX_FOLDER_KEY = "Box Folder";
    private final String PERSISTENT_ID_KEY = "Persistent ID";

    // The 2 use statements that are current used here
    private final String USE_STATEMENT_SERVICE = "Image-Service";
    private final String USE_STATEMENT_THUMBNAIL = "Image-Thumbnail";

    // This is append to the uri to get the thumbnail image when dealing with item level images
    public static final String THUMBNAIL_URL = "?urlappend=/mode/thumb";

    // This fields hold the url for the noid minter and REST handle web services
    private String handleServiceUrl = null;
    private String handlePrefix = "";
    private String defaultHandleUrl = ""; // The url for the handle
    private String handleServiceUserID = ""; // The user id for the handle service
    private String handleServicePassword = ""; // The password for the handle service

    // field that hold the thumbnail URL that is attached to the
    private String thumbnailURL = "";

    /**
     * The default constructor
     */
    public DOCreatorNYU() {
    }

    /**
     * Constructor that takes version number
     */
    public DOCreatorNYU(String atVersionNumber) {
        this.atVersionNumber = atVersionNumber;
    }

    /**
     * Method to set the url for the REST handle service and the default handle url that each
     * handle gets
     *
     * @param handleServiceUrl
     * @param defaultHandleUrl
     * @param userID
     * @param password
     */
    public void setHandleServiceUrl(String handleServiceUrl, String defaultHandleUrl, String userID, String password) {
        this.handleServiceUrl = handleServiceUrl;
        this.defaultHandleUrl = defaultHandleUrl;
        this.handleServiceUserID = userID;
        this.handleServicePassword = password;

        // get the prefix from the handle service url
        int index = handleServiceUrl.lastIndexOf("/");
        handlePrefix = handleServiceUrl.substring(index + 1);
    }

    /**
     * Method to the thumbnail URL. This is attached to the main URL when
     * importing item level items
     *
     * @param thumbnailURL
     */
    public void setThumbnailURL(String thumbnailURL) {
        if(thumbnailURL == null || thumbnailURL.length() == 0) {
            this.thumbnailURL = THUMBNAIL_URL;
        } else {
            this.thumbnailURL = thumbnailURL;
        }
    }

    /**
     * This method creates digital object for the resource components of the
     * given resource object. The DO information (component unique identifier,
     * use statement, and URI are read in from the file object.
     *
     * @param file    The text file to read in
     * @param monitor the progress monitor
     * @param record  The resource record to attach the created digital objects to
     * @throws IOException Exception if there is any kind of problem processing file
     */
    public void createAndAttachDigitalObjects(File file, InfiniteProgressPanel monitor, Resources record) throws IOException {
        this.monitor = monitor;

        resourceComponents = new HashMap<String, ResourcesComponents>();
        String line = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

        // update the progress monitor
        monitor.setTextLine("Loading Resource Components", 2);

        //store values in resourceComponents HashMap
        for (ResourcesComponents rc : record.getResourcesComponents()) {
            if (rc.getComponentUniqueIdentifier() != null && rc.getComponentUniqueIdentifier().length() > 0) {
                //load all first level resource components
                resourceComponents.put(rc.getComponentUniqueIdentifier(), rc);
                monitor.setTextLine(rc.getTitle(), 4);
            }
            //load all second and higher level resource components
            loadResourceComponentsByComponentId(rc);
        }

        String previousCUI = "";
        int sequence = 1;

        // update the progress monitor
        monitor.setTextLine("Creating Digital Objects", 2);

        // int that keeps track of the digital objects that have been created so far
        int doCount = 0;

        //read in each line and create DO when appropriate
        while ((line = in.readLine()) != null) {
            String items[] = line.split("\t");

            // this set the sequence number to append to the METSID.
            if (resourceComponents.containsKey(items[0])) {
                if (items[0].equals(previousCUI))
                    sequence++;
                else
                    sequence = 1;

                // items[0] is the component unique identifier.
                // items[1] is the use statement
                // items[2] is the URI
                ResourcesComponents component = resourceComponents.get(items[0]);

                boolean doExists = digitalObjectExists(component, items[1], items[2]);

                if (!doExists) {
                    // create DO and attach to resource component
                    ArchDescriptionDigitalInstances digitalInstance = new ArchDescriptionDigitalInstances();
                    //digitalInstance.setResource(record);
                    DigitalObjects dObjects = new DigitalObjects();
                    dObjects.setTitle(component.getTitle());
                    dObjects.setDateExpression(component.getDateExpression());
                    dObjects.setDateBegin(component.getDateBegin());
                    dObjects.setDateEnd(component.getDateEnd());
                    copyNotesFromComponent(component, dObjects);
                    digitalInstance.setInstanceType(ArchDescriptionInstances.DIGITAL_OBJECT_INSTANCE);
                    digitalInstance.setDigitalObject(dObjects);
                    dObjects.setDigitalInstance(digitalInstance);

                    //set METSID to CUI + sequence number
                    //dObjects.setMetsIdentifier(component.getComponentUniqueIdentifier() + "-" + sequence);

                    FileVersions fVersions = new FileVersions();
                    fVersions.setUseStatement(items[1]);
                    fVersions.setUri(items[2]);
                    fVersions.setDigitalObject(dObjects);
                    dObjects.addFileVersion(fVersions);
                    component.addInstance(digitalInstance);
                    digitalInstance.setResourcesComponents(component);
                    previousCUI = items[0];

                    // based if this plugin is running in version 1.5.9 or a later
                    // version of the AT then we need to set the repository, metsid, and parent resource
                    if (!atVersionNumber.equals("1.5.9")) {
                        dObjects.setRepository(record.getRepository());
                        digitalInstance.setParentResource(record); // set parent resource to used when searching
                        //set METSID to the URI
                        dObjects.setMetsIdentifier(component.getComponentUniqueIdentifier() + "-" + sequence);
                    }

                    doCount++;
                    monitor.setTextLine("Digital Object " + doCount + " : " + dObjects.getTitle(), 4);
                }
            }
        }
    }


    /**
     * This method creates digital object for the resource components of the
     * given resource object. The digital object information either in
     * (Box number, folder number, use statement, and URI) or
     * (PersistentID, use statement, and URI)
     * are read in from the tab delimited text file.
     *
     * @param file            The tab delimited text file containing the digital object information
     * @param monitor         The progress monitor
     * @param sb              The string builder used for the export log
     * @param record          The resource record to attach the created digital objects to
     * @param resourcesCommon The is series, subseries where to attach digital object
     * @throws IOException Exception if there is any kind of problem processing file
     */
    public void createAndAttachDigitalObjects(File file, InfiniteProgressPanel monitor, StringBuilder sb, Resources record,
                                              ResourcesCommon resourcesCommon) throws Exception {

        // need to figure out if we are going to be processing digital objects which are keyed
        // by box-folders or persistent id. This is done by reading first line and seeing how
        // many columns there are. This assumes the first line is the column header
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

        String line = in.readLine();
        String items[] = line.split("\\s*\t\\s*");

        if (items.length == 3) { // adding digital objects keyed by resource component persistent id
            createAndAttachDigitalObjects(in, monitor, sb, record, resourcesCommon, PERSISTENT_ID_KEY);
        } else if (items.length == 4) { // digital objects keyed by box-folder numbers
            createAndAttachDigitalObjects(in, monitor, sb, record, resourcesCommon, BOX_FOLDER_KEY);
        } else {
            throw new Exception("Unknown file format ");
        }
    }

    /**
     * This method creates digital object for the resource components of the
     * given resource object. The DO information (Box number, folder number,
     * use statement, and URI are read in from the file object.
     *
     * @param in              The buffered reader
     * @param monitor         The progress monitor
     * @param sb              The string builder used for the export log
     * @param record          The resource record to attach the created digital objects to
     * @param resourcesCommon The is series, subseries where to attach digital object
     * @param keyedBy         This specifies how the resource components are keyed in the resourceComponents HashMap
     * @throws IOException Exception if there is any kind of problem processing file
     */
    public void createAndAttachDigitalObjects(BufferedReader in, InfiniteProgressPanel monitor, StringBuilder sb, Resources record,
                                              ResourcesCommon resourcesCommon, String keyedBy) throws Exception {
        this.monitor = monitor;
        this.sb = sb;
        resourceComponents = new HashMap<String, ResourcesComponents>();

        // update the progress monitor
        monitor.setTextLine("Loading Resource Components", 2);

        // gather the resource components for this series/subseries
        for (ResourcesComponents component : resourcesCommon.getResourcesComponents()) {
            if (keyedBy.equals(BOX_FOLDER_KEY)) {
                loadResourceComponentsForBoxFolderImport(component);
            } else if (keyedBy.equals(PERSISTENT_ID_KEY)) {
                loadResourceComponentsByPersistentId(component);
            } else { // unknown key type so throw error
                throw new Exception("Unknown Hash Map key " + keyedBy);
            }
        }

        // create the Buffer reader object for reading the digital object information from
        // the file
        String line = null;

        // keep track of the key value so that the digital object sequence can work correct
        String previousKey = "";

        // keep track of digital objects that belong to the same resource components
        // This is used for assigning the METS identifier
        int sequence = 1;

        // update the progress monitor
        monitor.setTextLine("Creating Digital Objects", 2);

        // int that keeps track of the digital objects that have been created so far
        int doCount = 0;

        // keep track of the line count
        int lineCount = 1;

        // Read in each line and create DO when appropriate. Once the line is split using tabs
        // the information is in the following format for files using box-folder as the key :
        //
        // items[0] is the box number.
        // items[1] is the folder number
        // items[2] is the use statement
        // items[3] is the URI
        //
        // or for using persistent ID as the key :
        //
        // items[0] is the persistent id of the resource component.
        // items[1] is the use statement
        // items[2] is the URI
        while ((line = in.readLine()) != null) {
            // check to see if the process wasn't canceled
            if (monitor.isProcessCancelled()) {
                return;
            }

            String items[] = line.split("\\s*\t\\s*");

            // create the key which is a combination of the box and folder number
            String key = "";
            String useStatement = "";
            String uri = "";

            if (keyedBy.equals(BOX_FOLDER_KEY)) {
                key = items[0] + "-" + items[1];
                useStatement = items[2];
                uri = items[3];
            } else if (keyedBy.equals(PERSISTENT_ID_KEY)) {
                key = items[0];
                useStatement = items[1];
                uri = items[2];
            }

            // this set the sequence number to append to the METSID.
            if (resourceComponents.containsKey(key)) {
                if (key.equals(previousKey)) {
                    sequence++;
                } else {
                    sequence = 1;
                }

                ResourcesComponents component = resourceComponents.get(key);

                boolean doExists = digitalObjectExists(component, useStatement, uri);

                if (!doExists) {
                    // create DO and attach to resource component
                    ArchDescriptionDigitalInstances digitalInstance = new ArchDescriptionDigitalInstances();
                    DigitalObjects dObjects = new DigitalObjects();
                    dObjects.setTitle(component.getTitle());
                    dObjects.setDateExpression(component.getDateExpression());
                    dObjects.setDateBegin(component.getDateBegin());
                    dObjects.setDateEnd(component.getDateEnd());

                    copyNotesFromComponent(component, dObjects);

                    digitalInstance.setInstanceType(ArchDescriptionInstances.DIGITAL_OBJECT_INSTANCE);
                    digitalInstance.setDigitalObject(dObjects);
                    dObjects.setDigitalInstance(digitalInstance);

                    // create the file version
                    FileVersions fVersions = new FileVersions();
                    fVersions.setUseStatement(useStatement);
                    fVersions.setUri(uri);
                    fVersions.setDigitalObject(dObjects);
                    dObjects.addFileVersion(fVersions);
                    component.addInstance(digitalInstance);
                    digitalInstance.setResourcesComponents(component);

                    // set the current key to the previous key value
                    previousKey = key;

                    // set the repository, metsid, and parent resource record
                    dObjects.setRepository(record.getRepository());
                    digitalInstance.setParentResource(record); // set parent resource to used when searching

                    // set the mets id to the resource indentifier + component persistent identifier and the sequence number
                    dObjects.setMetsIdentifier(record.getResourceIdentifier() + "." + component.getPersistentId() + "." + sequence);

                    doCount++;
                    monitor.setTextLine("Added Digital Object " + doCount + " : " + dObjects.getTitle(), 4);

                    // create a log entry
                    sb.append("Line ").append(lineCount).append("\t");
                    sb.append("Created Digital Object for ").append(keyedBy).append(" ");
                    sb.append(key).append(", URI : ");
                    sb.append(uri).append("\n");
                } else {
                    // create log entry telling user that url already exist
                    sb.append("Line ").append(lineCount).append("\t");
                    sb.append("Digital object already exist for ").append(keyedBy).append(" ");
                    sb.append(key).append(", URI : ");
                    sb.append(uri).append("\n");
                }
            } else { // let the user know that no box/folder could be found
                // create log entry telling user that no box/folder can be found to
                // to attach digital instance
                sb.append("Line ").append(lineCount).append("\t");
                sb.append("No Resource Component Found for Digital Object");
                sb.append(key).append(", URI : ");
                sb.append(uri).append("\n");
            }

            // increment the line count
            lineCount++;
        }
    }

    /**
     * This method creates digital objects for the resource components of the
     * given resource object. The format of the input file depends on if file level
     * records are being processed.
     * 
     *
     * @param file            The tab delimited text file containing the digital object information
     * @param monitor         The progress monitor
     * @param sb              The string builder used for the export log
     * @param record          The resource record to attach the created digital objects to
     * @param resourcesCommon The is series, subseries where to attach digital object
     * @param isItemLevel     Whether item level records are created or is this folder level
     * @param isFileLevel     Whether a file level resource components are needed
     * @param isFileList      Whether the file being imported is a list of files
     * @throws IOException Exception if there is any kind of problem processing file
     */
    public void createAndAttachDigitalObjectsFromWorkOrder(File file, InfiniteProgressPanel monitor,
                                                           StringBuilder sb, Resources record,
                                                           ResourcesCommon resourcesCommon,
                                                           boolean isItemLevel, boolean isBarcode, boolean isFileLevel, boolean isFileList) throws Exception {

        this.monitor = monitor;
        this.sb = sb;
        resourceComponents = new HashMap<String, ResourcesComponents>();

        // initiate a string builder which holds the map between image number
        // and the assigned URI
        StringBuilder sb2 = new StringBuilder();

        // update the progress monitor
        monitor.setTextLine("Loading Resource Components", 2);

        // if we are loading from a file list then it needs to be processed differently
        if(isFileList) {
            createAndAttachDigitalObjectsFromFileList(file, record, resourcesCommon);
            return;
        }

        // Set the "keyed by" value so the relevant resource components can be loaded
        String keyedBy = PERSISTENT_ID_KEY;

        // gather the resource components for this series/subseries
        for (ResourcesComponents component : resourcesCommon.getResourcesComponents()) {
            loadResourceComponentsByPersistentId(component);
        }

        // get the buffered reader
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

        // create the Buffer reader object for reading the digital object information from
        // the file
        String line;

        // hast map that stores the index of columns based on names
        HashMap<String, Integer> headerIndexMap = null;

        // keep track of the key value so that the digital object sequence can work correct
        String previousKey = "";

        // keep track of digital objects that belong to the same resource components
        // This is used for assigning the METS identifier
        int sequence = 1;

        // update the progress monitor
        monitor.setTextLine("Creating Digital Objects", 2);

        // int that keeps track of the digital objects that have been created so far
        int doCount = 0;

        // keep track of the line count
        int lineCount = 1;

        // int that holds the index of the use statement and other information
        int useStatementIndex = -1;

        // Read in each tab delimited line and create DO when appropriate.
        // For item level import, the line is in the following the format
        // items[0] is the persistent ID
        // items[1] is the component title, if exported, and rest of indexes is shifted up by one
        // items[1] is the box number
        // items[2] is the folder number
        // items[3] is the item #
        // items[4] is the image number in the format 00001, 00002, 00003, etc ...
        // items[last_index] is the URI, if the URI is stored within the file
        //
        // For barcode import in the following the format
        // items[0] is the persistent ID
        // items[1] is the component title, if exported, and rest of indexes is shifted up by one
        // items[1] is the barcode
        // items[2] is the use statement
        // items[3] is the filename
        // items[last_index] is the URI, if the URI is stored with the file
        //
        // For file level import then the following is the format
        // items[0] is the persistent ID
        // items[1] is the series or subseries name
        // items[2] is the title of the file level component or just "Attachment"
        // items[3] is the use statement
        // items[4, to last_index] is the name of the files using 1 per column
        // items[last_index] is the URI, if the URI is stored with the file.
        //
        // For none file level or barcode, only items[0] is needed for the import. For barcodes
        // the column containing the use statement is also needed.  The rest of the fields
        // are for display purposes only, so that the person doing the digitization can track what
        // they are digitizing.  For file level import, item[3], the use statement, is also needed
        while ((line = in.readLine()) != null) {
            // if line number is one then add place for newly created handle
            if (lineCount == 1) {
                headerIndexMap = ImportExportUtils.getHeaderIndexes(line.trim());

                if(headerIndexMap.containsKey(ImportExportUtils.USE_STATEMENT)) {
                    useStatementIndex = headerIndexMap.get(ImportExportUtils.USE_STATEMENT);
                }

                sb2.append(line);
                sb2.append("\tHandle URI\n");
            } else {
                // check to see if the process wasn't canceled
                if (monitor.isProcessCancelled()) {
                    return;
                }

                //String items[] = line.split("\\s*\t\\s*");
                String items[] = line.split("\t");

                // create the key which is just the persistentID in the AT
                String key = "";

                // the key is by the persistentID
                String tmpKey = items[0].trim();

                // remove the resource id from key if it's there
                if (tmpKey.contains("_")) {
                    String[] sa = tmpKey.split("_");
                    key = sa[sa.length -1]; // the reference id should be at last position
                } else {
                    key = tmpKey;
                }

                // get the string in the last index, which may be the URI, if they
                // are being read in from the file
                String uriFromFile = items[(items.length - 1)].trim();

                // this set the sequence number to append to the METSID.
                if (resourceComponents.containsKey(key)) {
                    ResourcesComponents component = resourceComponents.get(key);

                    // create the description string for the handle
                    String handleDescription = getHandleDescription(record, component);

                    if (key.equals(previousKey)) {
                        sequence++;
                    } else {
                        sequence = 1;
                    }

                    // holds the assigned and use statements URIs
                    String[] useStatements = null;
                    String[] URIs = null;
                    String[] useStatements2 = null;
                    String[] URIs2 = null;


                    // check to see if item level digital objects should be created
                    if(isItemLevel) {
                        // set the use statement to an image service which is the default
                        useStatements = new String[]{USE_STATEMENT_SERVICE};
                        useStatements2 = new String[]{USE_STATEMENT_THUMBNAIL};

                        // get the URI
                        String serviceURI = getURIFromWebService(handleDescription, uriFromFile);
                        String thumbnailURI = serviceURI + thumbnailURL;

                        URIs = new String[]{serviceURI};
                        URIs2 = new String[]{thumbnailURI};
                    } else if (isBarcode || isFileLevel) {
                        // this is a barcode or file level aka born digital import, so read use statment
                        useStatements = new String[]{items[useStatementIndex].trim()};

                        // get the URIs
                        URIs = new String[]{getURIFromWebService(handleDescription, uriFromFile)};
                    } else { // assume none item level image was digitized
                        // set the use statement to an image service which is the default
                        useStatements = new String[]{USE_STATEMENT_SERVICE};

                        // get the URI
                        URIs = new String[]{getURIFromWebService(handleDescription, uriFromFile)};
                    }

                    // create and attach the digital object now
                    DigitalObjects digitalObject = createDigitalObject(record, component, useStatements,
                                URIs, sequence);

                    // there might be more information in line which can be used to populate more
                    // the digital object and file version information
                    populateDigitalObjectWithInformationFromFile(headerIndexMap, digitalObject, items);

                    // get the uri to add log file
                    String uri = digitalObject.getFileVersion(0).getUri();

                    // increment the digital object count
                    doCount++;

                    // update the import log
                    updateImportLog(digitalObject, keyedBy, key, uri, lineCount, doCount);

                    // if we doing item level import, then we need to create another digital
                    // object for the thumbnail image
                    if(isItemLevel) {
                        sequence++; // increment the sequence count

                        digitalObject = createDigitalObject(record, component, useStatements2,
                                URIs2, sequence);

                        doCount++;

                        updateImportLog(digitalObject, keyedBy, key, URIs2[0], lineCount, doCount);
                    }

                    // Create export which maps URI with image number. This will have to be
                    // have to be persisted somewhere though
                    sb2.append(line).append("\t").append(uri).append("\n");

                    // set the previous key
                    previousKey = key;
                } else { // let the user know that no box/folder could be found
                    // create log entry telling user that no box/folder can be found
                    // to attach digital object instance
                    this.sb.append("Line ").append(lineCount).append("\t");
                    this.sb.append("No Resource Component Found to Attach Digital Object\n");

                    // Create uri if this is an actual entry, but it's not found in the
                    // resource record
                    if(items.length >= 2) {
                        String uri = getURIFromWebService("AT -- No Resource Component", uriFromFile);
                        sb2.append(line).append("\t").append(uri).append("\n");
                    }
                }
            }

            // increment the line count
            lineCount++;
        }

        // save the URI information to a file now so this information can be used
        // later on in the publication process
        saveURIMappingFile(file, sb2);
    }

    /**
     * Method used to populate the digital object with information from a line in the file
     * if that information is entered
     *
     * @param headerIndexMap
     * @param digitalObject
     * @param items
     */
    private void populateDigitalObjectWithInformationFromFile(HashMap<String, Integer> headerIndexMap, DigitalObjects digitalObject, String[] items) {
        // set what additional information can be added to the digital object
        if(headerIndexMap.containsKey(ImportExportUtils.DO_TITLE)) {
            digitalObject.setTitle(items[headerIndexMap.get(ImportExportUtils.DO_TITLE)]);
        }

        if(headerIndexMap.containsKey(ImportExportUtils.LABEL)) {
            digitalObject.setLabel(items[headerIndexMap.get(ImportExportUtils.LABEL)]);
        }

        if(headerIndexMap.containsKey(ImportExportUtils.DATE_EXPRESSION)) {
            digitalObject.setDateExpression(items[headerIndexMap.get(ImportExportUtils.DATE_EXPRESSION)]);
        }

        if(headerIndexMap.containsKey(ImportExportUtils.DATE_BEGIN)) {
            try {
                Integer dateBegin = new Integer(items[headerIndexMap.get(ImportExportUtils.DATE_BEGIN)]);
                digitalObject.setDateBegin(dateBegin);
            } catch(NumberFormatException nfe) { }
        }

        if(headerIndexMap.containsKey(ImportExportUtils.DATE_END)) {
            try {
                Integer dateEnd = new Integer(items[headerIndexMap.get(ImportExportUtils.DATE_END)]);
                digitalObject.setDateBegin(dateEnd);
            } catch(NumberFormatException nfe) { }
        }

        if(headerIndexMap.containsKey(ImportExportUtils.RESTRICTIONS_APPLY)) {
            Boolean restrictionApply = new Boolean(items[headerIndexMap.get(ImportExportUtils.RESTRICTIONS_APPLY)]);
            digitalObject.setRestrictionsApply(restrictionApply);
        }

        if(headerIndexMap.containsKey(ImportExportUtils.OBJECT_TYPE)) {
            digitalObject.setObjectType(items[headerIndexMap.get(ImportExportUtils.OBJECT_TYPE)]);
        }

        // set values for the first file version object
        FileVersions fileVersion = digitalObject.getFileVersion(0);

        // see if to set the new URI, if it hast been set already
        if(headerIndexMap.containsKey(ImportExportUtils.URI_LINK) &&
                (fileVersion.getUri() == null || fileVersion.getUri().equalsIgnoreCase("dummy url"))) {
            fileVersion.setUri(items[headerIndexMap.get(ImportExportUtils.URI_LINK)]);
        }

        if(headerIndexMap.containsKey(ImportExportUtils.USE_STATEMENT)) {
            fileVersion.setUseStatement(items[headerIndexMap.get(ImportExportUtils.USE_STATEMENT)]);
        }

        if(headerIndexMap.containsKey(ImportExportUtils.EAD_DAO_ACTUATE)) {
            fileVersion.setEadDaoActuate(items[headerIndexMap.get(ImportExportUtils.EAD_DAO_ACTUATE)]);
        }

        if(headerIndexMap.containsKey(ImportExportUtils.EAD_DAO_SHOW)) {
            fileVersion.setEadDaoShow(items[headerIndexMap.get(ImportExportUtils.EAD_DAO_SHOW)]);
        }
    }

    /**
     * Method to read a file containing a list of file names and header information
     * which specifies the import process
     *
     * @param file
     * @param record
     * @param resourcesCommon
     */
    private void createAndAttachDigitalObjectsFromFileList(File file, Resources record, ResourcesCommon resourcesCommon) throws Exception {
         // get the buffered reader to read the file
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

        // create the Buffer reader object for reading the digital object information from
        // the file
        String line = null;

        // keep track of the line count
        int lineCount = 0;

        String keyedBy = ""; // holds how resource components should be stored
        String replaceString = ""; // string that should be replace in instance label

        // String that specifies import option. for right now only none and thumbnail.
        // If thumbnail is specified then a digital object is created for the thumbnail
        String importOption = "";

        // hashmap that hold the use statements
        HashMap<String,String> useStatementMap = new HashMap<String, String>();

        // find the line after header (designated by #) containing information needed for the import process
        // This line is in the following comma delimited format
        // key by (instance1 or persistentID), String to search and replace in instance label or
        // persistent id (none or i.e -:_),extension 1:use statement 1,extension2:use statement 2
        while ((line = in.readLine()) != null) {
            lineCount++;

            if(!line.startsWith("#") && line.length() > 10) {
                String[] sa1 = line.trim().split(",");
                keyedBy = sa1[0].trim();
                replaceString = sa1[1].trim();
                importOption = sa1[2].trim();

                // now place the extensions:use statement pairs in hashmap
                for(int i = 3; i < sa1.length; i++) {
                    String[] sa2 = sa1[i].trim().split(":");
                    useStatementMap.put(sa2[0], sa2[1]);
                }

                break; // exit the while loop now
            }
        }

        // get the text that needs to replace
        String replaceText = "";
        String replaceWith = "";

        if(replaceString.equalsIgnoreCase("none")) {
            replaceText = "none";
        } else {
            String[] sa = replaceString.split(":");
            replaceText = sa[0];
            replaceWith = sa[1];
        }

        // now populated the hashmap that holds the resource components
        if(keyedBy.equals("persistentID")) {
            for (ResourcesComponents component : resourcesCommon.getResourcesComponents()) {
                loadResourceComponentsByPersistentId(component);
            }
        } else if(keyedBy.equals("componentID")) {
            for (ResourcesComponents component : resourcesCommon.getResourcesComponents()) {
                loadResourceComponentsByComponentId(component);
            }
        } else { // must be keyed by instance label aka container indicator 1 ... 3
            for (ResourcesComponents component : resourcesCommon.getResourcesComponents()) {
                loadResourceComponentsByInstanceLabel(component, keyedBy, replaceText, replaceWith);
            }
        }

        // initiate a string builder which holds the map between filename
        // and the assigned URI
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Filename, Persistent ID, Assigned URI\n");

        // keep track of the key value so that the digital object sequence can work correct
        String previousKey = "";

        // keep track of digital objects that belong to the same resource components
        // This is used for assigning the METS identifier
        int sequence = 1;

        // update the progress monitor
        monitor.setTextLine("Creating Digital Objects", 2);

        // variable that keeps track of the digital objects that have been created so far
        int doCount = 0;

        // variable that keeps tracks of the amount of iterations used when processing
        // the input file. Main used for debuging purposes
        int itcount = 0;

        // variable that stores the list of files for which no component was found
        String notImportedMessage = "";

        // variable that keeps track of files which are on 2 or more lines in the input file
        Set<String> importedFiles = new HashSet<String>();
        String duplicateMessage = "";

        // process lines from the file looking for any digital objects to import
        while ((line = in.readLine()) != null) {
            lineCount++; // increment the line count

            // check to see if the process wasn't canceled
            if (monitor.isProcessCancelled()) {
                return;
            }

            // see if URI was also supplied with along with the filename
            // using a tab delimited field
            String sa2[] = line.trim().split("\t");

            // create the key from the filename
            String filename = "";
            String uri = "";

            if(sa2.length == 2) {
                filename = sa2[0].trim();
                uri = sa2[1].trim();
            } else {
                filename = line.trim();
                uri = filename;
            }

            boolean foundComponent = false;
            String key = "";

            // Assuming the hashmap key is part of the filename, and find if it exists
            // in a very inefficient, brute force manner.
            // There must be better way, but I can't think one right now.
            // One way would to to use regex to extract key from filename, but
            // that would require user know regex
            for(String currentKey : resourceComponents.keySet()) {
                itcount++;

                // see if the filename contains the current key irrespective of case
                if(filename.toLowerCase().contains(currentKey.toLowerCase())) {
                    key = currentKey;
                    foundComponent = true;
                    break;
                }
            }

            // this set the sequence number to append to the METSID.
            if (foundComponent) {
                // check to see that this file hasn't already been imported
                if(importedFiles.contains(filename)) {
                    String message = "Duplicate entry for \""  + filename + "\" on line # " + lineCount;
                    duplicateMessage += message + "\n";

                    System.out.println(message);

                    continue;
                } else {
                    importedFiles.add(filename);
                }

                ResourcesComponents component = resourceComponents.get(key);

                // create the description string for the handle
                String handleDescription = getHandleDescription(record, component);

                if (key.equals(previousKey)) {
                    sequence++;
                } else {
                    sequence = 1;
                }

                // holds the assigned URIs
                String[] useStatements = new String[1];
                String[] URIs;

                // set the use statement base on the filename
                for(String hashKey: useStatementMap.keySet()) {
                    if(filename.contains(hashKey)) {
                        useStatements[0] = useStatementMap.get(hashKey);
                        break;
                    }
                }

                // if no use statement found then continue to next file
                if(useStatements[0] == null) {
                    System.out.println("No use statement for " + filename);
                    continue;
                }

                // get the URI
                String mainURI = getURIFromWebService(handleDescription, uri);
                URIs = new String[]{mainURI};

                // create and attach the digital object now
                DigitalObjects digitalObject = createDigitalObject(record, component, useStatements,
                        URIs, sequence);

                // increment the digital object count
                doCount++;

                // update the import log
                updateImportLog(digitalObject, keyedBy, key, URIs[0], lineCount, doCount);

                // Create export which maps URI with image number. This will have to be
                // have to be persisted somewhere though
                sb2.append(filename).append(", ").append(component.getPersistentId()).append(", ").append(URIs[0]).append("\n");

                // if the option thumbnail was specified, create a thumbnail digital object
                if(importOption.contains("thumbnail")) {
                    useStatements[0] = USE_STATEMENT_THUMBNAIL;

                    if(importOption.contains(":")) {
                        String[] sa3 = importOption.split(":");
                        URIs[0] = mainURI + sa3[1];
                    } else {
                        URIs[0] = mainURI + thumbnailURL;
                    }

                    // increment the sequence count
                    sequence++;

                    // create and attach the digital object now
                    DigitalObjects thumbnailDigitalObject = createDigitalObject(record, component, useStatements,
                        URIs, sequence);

                    // increment the digital object count
                    doCount++;

                    // update the import log
                    updateImportLog(thumbnailDigitalObject, keyedBy, key, URIs[0], lineCount, doCount);
                }

                // set the previous key
                previousKey = key;
            } else { // print out the line that no resource component was not found for
                if(line.length() > 4) {
                    String message = "No Component found for \""  + filename + "\" on line # " + lineCount;
                    notImportedMessage += message + "\n";

                    System.out.println(message);
                }
            }
        }

        // specify at the end of the import the number of digital objects that
        // were imported and any messages about which files were not imported
        // and also the number of duplicated found
        sb.append("\n\n").append(doCount).append(" Digital Objects Created ...");
        sb.append("\n\n").append(notImportedMessage);
        sb.append("\n").append(duplicateMessage);

        // save the URI information to a file now so this information can be used
        // later on in the publication process
        saveURIMappingFile(file, sb2);

        System.out.println("Number of Iterations " + itcount);
    }

    /**
     * Method to create a digital object with an attached file version
     *
     * @param record
     * @param component
     * @param useStatements
     * @param URIs
     * @param sequence
     * @return The digital object with file versions link
     */
    private DigitalObjects createDigitalObject(Resources record, ResourcesComponents component, String[] useStatements, String[] URIs, int sequence) {
        // create DO and attach to resource component
        ArchDescriptionDigitalInstances digitalInstance = new ArchDescriptionDigitalInstances();
        DigitalObjects dObjects = new DigitalObjects();
        dObjects.setTitle(component.getTitle());
        dObjects.setDateExpression(component.getDateExpression());
        dObjects.setDateBegin(component.getDateBegin());
        dObjects.setDateEnd(component.getDateEnd());

        copyNotesFromComponent(component, dObjects);

        digitalInstance.setInstanceType(ArchDescriptionInstances.DIGITAL_OBJECT_INSTANCE);
        digitalInstance.setDigitalObject(dObjects);
        dObjects.setDigitalInstance(digitalInstance);

        // create the file versions now
        for (int i = 0; i < useStatements.length; i++) {
            FileVersions fVersions = new FileVersions();
            fVersions.setUseStatement(useStatements[i]);
            fVersions.setUri(URIs[i]);
            fVersions.setDigitalObject(dObjects);
            dObjects.addFileVersion(fVersions);
        }

        // add the digital instance to the resource component
        component.addInstance(digitalInstance);
        digitalInstance.setResourcesComponents(component);

        // set the repository, metsid, and parent resource record
        dObjects.setRepository(record.getRepository());
        digitalInstance.setParentResource(record); // set parent resource to used when searching

        // set the mets id to the resource identifier + component persistent identifier and the sequence number
        dObjects.setMetsIdentifier(record.getResourceIdentifier() + "." + component.getPersistentId() + "." + sequence);

        return dObjects;
    }


    /**
     * Method to return the uri which is generate by call the noid minter
     * web service followed by calling the handle server web service
     *
     * @param description The description to attach to the handle
     * @param uriFromFile This contains the URI from the file if it was provided
     * @return The URI
     */
    private String getURIFromWebService(String description, String uriFromFile) throws Exception {
        if(handleServiceUrl.equals(WebServiceHelper.RESTFUL_HANDLE_DUMMY_URL)) {
            return "dummy url";
        } else if(handleServiceUrl.equals(WebServiceHelper.RESTFUL_HANDLE_FILE_URL)) {
            return uriFromFile; // just return this
        } else if(handleServiceUrl.equals(WebServiceHelper.RESTFUL_HANDLE_FILENAME_URL)) {
            return defaultHandleUrl + uriFromFile;
        } else {
            // call the restful handle web service to register a handle with the default url
            return WebServiceHelper.createHandle(handleServiceUrl, defaultHandleUrl, description,
                    handleServiceUserID, handleServicePassword);
        }
    }

    /**
     * Method to update the import log when a digital object is created and attached
     * tp a resource component
     *
     * @param digitalObject
     * @param keyedBy
     * @param key
     * @param uri
     * @param lineCount
     * @param doCount
     */
    private void updateImportLog(DigitalObjects digitalObject, String keyedBy, String key,
                                 String uri, int lineCount, int doCount) {
        // post message to user
        monitor.setTextLine("Added Digital Object " + doCount + " : " + digitalObject.getTitle(), 4);

        // create a log entry
        sb.append("Line ").append(lineCount).append("\t");
        sb.append("Created Digital Object for ").append(keyedBy).append(" ");
        sb.append(key).append(", URI : ");
        sb.append(uri).append("\n");
    }

    /**
     * Method to copy the notes from resource component to the digital object
     *
     * @param archDescription The resource component
     * @param digitalObject   The digital object
     */
    private void copyNotesFromComponent(ArchDescription archDescription, DigitalObjects digitalObject) {
        ArchDescriptionNotes aNote;
        for (ArchDescriptionRepeatingData repeatingData : archDescription.getRepeatingData()) {
            if (repeatingData instanceof ArchDescriptionNotes) {
                aNote = (ArchDescriptionNotes) repeatingData;
                if (canAddNoteToDigitalObject(aNote)) {
                    digitalObject.addRepeatingData(new ArchDescriptionNotes(
                            digitalObject,
                            aNote.getTitle(),
                            aNote.getRepeatingDataType(),
                            aNote.getSequenceNumber(),
                            aNote.getNotesEtcType(),
                            StringHelper.tagRemover(aNote.getNoteContent())));
                }
            }
            SequencedObject.resequenceSequencedObjects(digitalObject.getRepeatingData());
        }
    }

    /**
     * check to see if this note should be added to a digital object
     *
     * @param aNote The note to check to see if it can be added to a digital object
     * @return boolean indicating whether the particular note can be added
     *         to a digital object
     */
    public boolean canAddNoteToDigitalObject(ArchDescriptionNotes aNote) {
        NotesEtcTypes notesEtcType = aNote.getNotesEtcType();

        try {
            return NoteEtcTypesUtils.getDigitalObjectNotesTypesList().contains(notesEtcType);
        } catch(Exception e) {
            //e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks to see if a digital object with same use statement and uri for
     * the given resource component.
     *
     * @param component
     * @param useStatement
     * @param uri
     * @return
     */
    private boolean digitalObjectExists(ResourcesComponents component, String useStatement, String uri) {
        for (ArchDescriptionInstances instance : component.getInstances()) {
            if (instance instanceof ArchDescriptionDigitalInstances) {
                DigitalObjects dObject = ((ArchDescriptionDigitalInstances) instance).getDigitalObject();
                for (FileVersions fv : dObject.getFileVersions()) {
                    if (useStatement.equals(fv.getUseStatement()) && uri.equals(fv.getUri())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This method recursively loads (into variable: HashMap resourceComponents)
     * all resource components for the given resource component into a hashmap which is
     * keyed by unique identifier.
     *
     * @param component
     */
    private void loadResourceComponentsByComponentId(ResourcesComponents component) {
        //component.getComponentUniqueIdentifier();
        for (ResourcesComponents rc : component.getResourcesComponents()) {

            if (rc.getComponentUniqueIdentifier() != null && rc.getComponentUniqueIdentifier().length() > 0) {
                resourceComponents.put(rc.getComponentUniqueIdentifier(), rc);
                monitor.setTextLine(rc.getTitle(), 4);
            }

            loadResourceComponentsByComponentId(rc);
        }
    }

    /**
     * Method to load resource components and store them in a hashmap keyed by the
     * box number - folder number.
     *
     * @param component The resource component to process
     */
    private void loadResourceComponentsForBoxFolderImport(ResourcesComponents component) {
        // check to see if the process wasn't canceled
        if (monitor.isProcessCancelled()) {
            return;
        }

        // get any children components
        if (component.isHasChild()) {
            for (ResourcesComponents childComponent : component.getResourcesComponents()) {
                loadResourceComponentsForBoxFolderImport(childComponent);
            }
        }

        // indicate the resource component that being processed
        monitor.setTextLine("Resource Component : " + component, 2);

        // check to if this component has any instance. If it does then export
        // the box folder list
        Set<ArchDescriptionInstances> instances = component.getInstances();
        for (ArchDescriptionInstances instance : instances) {
            if (instance instanceof ArchDescriptionAnalogInstances) {
                ArchDescriptionAnalogInstances analogInstance = (ArchDescriptionAnalogInstances) instance;

                // alert the user which instance is being processed
                monitor.setTextLine("Indexing instance : " + analogInstance.getInstanceLabel(), 4);

                String key = analogInstance.getContainer1Indicator() + "-" + analogInstance.getContainer2Indicator();

                resourceComponents.put(key, component);
            }
        }
    }

    /**
     * Method to load resource components and store them in a hashmap keyed by the
     * instance label or derivative of instance label if replace string is provided.
     *
     * @param component The resource component to process
     */
    private void loadResourceComponentsByInstanceLabel(ResourcesComponents component, String keyedBy, String replaceText, String replaceWith) {
        // check to see if the process wasn't canceled
        if (monitor.isProcessCancelled()) {
            return;
        }

        // get any children components
        if (component.isHasChild()) {
            for (ResourcesComponents childComponent : component.getResourcesComponents()) {
                loadResourceComponentsByInstanceLabel(childComponent, keyedBy, replaceText, replaceWith);
            }
        }

        // indicate the resource component that being processed
        monitor.setTextLine("Resource Component : " + component, 2);

        // check to if this component has any instance. If it does then export
        // the box folder list
        Set<ArchDescriptionInstances> instances = component.getInstances();
        for (ArchDescriptionInstances instance : instances) {
            if (instance instanceof ArchDescriptionAnalogInstances) {
                ArchDescriptionAnalogInstances analogInstance = (ArchDescriptionAnalogInstances) instance;

                // alert the user which instance is being processed
                monitor.setTextLine("Indexing instance : " + analogInstance.getInstanceLabel(), 4);

                String key = "";

                if(keyedBy.equals("barcode")) {
                    key = analogInstance.getBarcode();
                } else if(keyedBy.equals("instance1")) {
                    key = analogInstance.getContainer1Indicator();
                } else if(keyedBy.equals("instance2")) {
                    key = analogInstance.getContainer2Indicator();
                } else { // the keyed by must be instance3
                    key = analogInstance.getContainer3Indicator();
                }

                if(!replaceText.equals("none")) {
                    key = key.replace(replaceText, replaceWith);
                }

                resourceComponents.put(key, component);
            }
        }
    }

    /**
     * Method to store the resource components in a hash map keyed by the persistent id
     * This method makes recursive calls to do this
     *
     * @param component The component
     */
    private void loadResourceComponentsByPersistentId(ResourcesComponents component) {
        // check to see if the process wasn't canceled
        if (monitor.isProcessCancelled()) {
            return;
        }

        String key = component.getPersistentId().trim();
        resourceComponents.put(key, component);

        // If any children components process them until we get a leaf node.
        if (component.isHasChild()) {
            for (ResourcesComponents childComponent : component.getResourcesComponents()) {
                loadResourceComponentsByPersistentId(childComponent);
            }
        }
    }

    /**
     * Method to save the uri mapping, which also contains the noid to a file. In reality
     * this mapping information should really be submitted to a service
     *
     * @param file
     * @param sb
     */
    private void saveURIMappingFile(File file, StringBuilder sb) throws Exception {
        // get the file name and create the new filename. assume .csv, with _uri.csv
        String oldFilename = file.getName();
        int index = oldFilename.lastIndexOf(".");

        String filename = oldFilename.substring(0, index) + "_URI.csv";

        // create the new file
        File mapperFile = new File(file.getParentFile(), filename);

        // now write xml to the file
        BufferedWriter bw = new BufferedWriter
                (new OutputStreamWriter(new FileOutputStream(mapperFile)));
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    /**
     * Method to return a description to attach to the Handle. This will help
     * to identify which handles beong to which AT records
     *
     * @param record The parent resource record
     * @param component The resource component containing the handle
     * @return String containing the description;
     */
    private String getHandleDescription(Resources record, ResourcesComponents component) {
        return "AT -- Digital Object -- " + record.getResourceIdentifier() + " / " + component.getPersistentId();
    }
}
