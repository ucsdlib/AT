/*
 * Created by JFormDesigner on Wed Jul 14 10:12:09 EDT 2010
 */

package org.archiviststoolkit.plugin;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.plugin.utils.ImportExportUtils;
import org.archiviststoolkit.plugin.utils.WebServiceHelper;
import org.archiviststoolkit.importer.ImportExportLogDialog;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.dialog.ATFileChooser;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.swing.ATProgressUtil;
import org.archiviststoolkit.util.StringHelper;


/**
 * @author Nathan Stevens
 */
public class DOWorkflowDialog extends JDialog {
    private Dialog owner = null; // The parent dialog from which this was executed

    private Resources parentRecord = null; // The parent resource record

    private ResourcesCommon resourcesCommon = null; // This is either a resource component or resource record

    private HashMap<String, ResourcesComponents> componentsHash = null; // Stores any components for digital object import

    private String seriesTitle = null; // The title of the series resource component

    private String boxName = null; // used to export folder list of certain boxes

    private String resourceId = null; // The normalized resource id

    private String exportUseStatement = ""; // The use statement when exporting a shootlist

    private static final String DEFAULT_USESTATEMENT = "Text-Service"; // The default use statement

    private boolean runningInStandAloneMode = false; // specifies whether this is running is stand alone mode or as plugin in the AT

    private boolean exportTitle = false; // specifies whether to export the component title

    private boolean exportOptionalColumns = false;

    // Variable that keeps tracks of the number of digital objects that
    // are unlinked
    private int unlinkCount = 0;

    // keep track of the number of URI found
    private int uriCount = 0;

    /**
     * Main constructor of dialog
     * @param owner
     * @param title
     * @param parentRecord
     * @param resourcesCommon
     */
    public DOWorkflowDialog(Window owner, String title, Resources parentRecord, ResourcesCommon resourcesCommon) {
        super(owner, title);
        initComponents();

        if (owner instanceof Dialog) {
            this.owner = (Dialog) owner;
            runningInStandAloneMode = false;
        } else {
            // create a dialog so we can test the workflow dialog
            this.owner = new JDialog();
            runningInStandAloneMode = true;
        }

        this.parentRecord = parentRecord;
        this.resourcesCommon = resourcesCommon;
        this.seriesTitle = resourcesCommon.getTitle();

        // initialize the hashmap that stores resource components for digital object
        // import, keyed by the indicator1-indicator-2
        componentsHash = new HashMap<String, ResourcesComponents>();

        // set the title label
        nameLabel.setText(seriesTitle);

        if (resourcesCommon instanceof Resources) {
            label1.setText("Resource Name");
            persistentLabel.setText(parentRecord.getResourceIdentifier());
        } else {
            ResourcesComponents record = (ResourcesComponents) resourcesCommon;
            label1.setText("Component Name");
            persistentLabel.setText(record.getPersistentId());
        }
    }

    public DOWorkflowDialog(Frame owner) {
        super(owner);
        initComponents();
    }

    public DOWorkflowDialog(Dialog owner) {
        super(owner);
        initComponents();
    }

    // empty constructor used for unit testing
    public DOWorkflowDialog() { }

    // Method to return the owner component
    public Dialog getParentDialog() {
        return owner;
    }

    /**
     * Method to import a text file containing digital objects that need to be attached
     */
    private void importButtonActionPerformed() {
        final ATFileChooser filechooser = new ATFileChooser();

        if (filechooser.showOpenDialog(this, "Import") == JFileChooser.APPROVE_OPTION) {
            Thread performer = new Thread(new Runnable() {
                public void run() {
                    // disable the import button
                    importButton.setEnabled(false);

                    InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(owner, 1000, true);
                    monitor.start("Importing Digital Objects...");

                    try {
                        // create string builder which will be used to store import log
                        StringBuilder sb = new StringBuilder();

                        File file = filechooser.getSelectedFile();

                        sb.append("Imported ").append(file.getName()).append("\n\n");

                        // now attach the digital object
                        DOCreatorNYU doCreator = new DOCreatorNYU();
                        doCreator.createAndAttachDigitalObjects(file, monitor, sb, parentRecord, resourcesCommon);

                        // close the monitor
                        monitor.close();

                        // set this record dirty so that the user is ask if they want to save the resource record
                        ApplicationFrame.getInstance().setRecordDirty();

                        // show the import log
                        ImportExportLogDialog logDialog = new ImportExportLogDialog(owner, ImportExportLogDialog.DIALOG_TYPE_IMPORT, sb.toString());
                        logDialog.setTitle("Digital Object Import \"" + file.getName() + "\"");
                        logDialog.showDialog();
                    } catch (Exception e) {
                        monitor.close();

                        // todo this this error in an error dialog
                        e.printStackTrace();
                    }

                    // re-enable the import button
                    importButton.setEnabled(true);
                }
            }, "Import Digital Objects");
            performer.start();
        }
    }

    /**
     * Method to import a file to do URI replacement
     */
    private void importURIButtonActionPerformed() {
        final ATFileChooser filechooser = new ATFileChooser();

        if (filechooser.showOpenDialog(this, "Import") == JFileChooser.APPROVE_OPTION) {
            Thread performer = new Thread(new Runnable() {
                public void run() {
                    // disable the import button
                    importURIButton.setEnabled(false);

                    InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(owner, 1000, true);
                    monitor.start("Importing Digital Object URIs...");

                    try {
                        // create string builder which will be used to store import log
                        StringBuilder sb = new StringBuilder();

                        // get the import file and return if it's null
                        File file = filechooser.getSelectedFile();
                        if(file == null) return;

                        sb.append("Imported ").append(file.getName()).append("\n\n");

                        // if we exporting titles then put it in the header
                        String titleHeader = "\t";
                        if(exportTitle) {
                            titleHeader = "\tDigital Object Title\t";
                        }

                        // print the column descriptions
                        sb.append("URI #").append(titleHeader).append("METS Identifier\tNew URI Link\n");

                        // get the uri map and if its null just return
                        HashMap<Long, String> uriMap = getURIMap(file, monitor);

                        if(uriMap == null) return;

                        // now go through the components and find the digital objects to replace the URIs
                        for (ResourcesComponents component : resourcesCommon.getResourcesComponents()) {
                            processComponentsForURIExportOrImport(component, sb, uriMap, monitor);
                        }

                        // close the monitor
                        monitor.close();

                        // set this record dirty so that the user is ask if they want to save the resource record
                        ApplicationFrame.getInstance().setRecordDirty();

                        // show the import log
                        ImportExportLogDialog logDialog = new ImportExportLogDialog(owner, ImportExportLogDialog.DIALOG_TYPE_IMPORT, sb.toString());
                        logDialog.setTitle("Digital Object URIs Imported \"" + file.getName() + "\"");
                        logDialog.showDialog();
                    } catch (Exception e) {
                        monitor.close();

                        // todo this this error in an error dialog
                        e.printStackTrace();
                    }

                    // re-enable the import button
                    importURIButton.setEnabled(true);
                }
            }, "Import Digital Object URIs");
            performer.start();
        }
    }

    /**
     * Method to return map containing the new URIs
     * @param file
     * @param monitor
     * @return
     * @throws Exception
     */
    private HashMap<Long, String> getURIMap(File file, InfiniteProgressPanel monitor) throws Exception {
        // Read in the file and place new URI in a hashmap using the database id
        HashMap<Long, String> uriMap = new HashMap<Long, String>();

        // get the buffered reader
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        String line;

        // keep track of the line count, and column index of uri
        int lineCount = 1;
        int uriIndex = -1;
        int uriIdIndex = -1;
        int metsIdIndex = -1;

        while ((line = in.readLine()) != null) {
            // if line number is one then get the index for the needed information
            if (lineCount == 1) {
                String headerLine = line.trim();

                uriIdIndex = ImportExportUtils.getColumnIndex(ImportExportUtils.URI_ID, headerLine);
                uriIndex = ImportExportUtils.getColumnIndex(ImportExportUtils.NEW_URI_LINK, headerLine);
                metsIdIndex = ImportExportUtils.getColumnIndex(ImportExportUtils.METS_IDENTIFIER, headerLine);

                lineCount++;
                continue;
            }

            // check to see if the process wasn't canceled
            if (monitor.isProcessCancelled()) {
                return null;
            }

            String items[] = line.trim().split("\\s*\t\\s*");

            // create the key which is just the URI ID
            Long key = new Long(items[uriIdIndex]);
            String uri = items[uriIndex];
            String metsId = items[metsIdIndex];

            // store the uri keyed by the database id
            uriMap.put(key, uri);

            // indicate the resource component that being processed
            monitor.setTextLine("Loaded New URI for: " + metsId, 2);

            // increment the line count
            lineCount++;
        }

        return uriMap;
    }

    /**
     * Method to export a list of box/folders for the selected series or subseries
     */
    private void exportActionPerformed() {
        Thread performer = new Thread(new Runnable() {
            public void run() {
                // disable the export button
                exportButton.setEnabled(false);

                InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(getParentDialog(), 1000, false);
                monitor.start("Exporting Component Information...");

                StringBuilder sb = new StringBuilder();

                // set whether to export the component title, and optional columns
                exportTitle = exportTitleCheckBox.isSelected();
                exportOptionalColumns = optionalColumnsCheckBox.isSelected();

                // get the box name
                boxName = exportBoxNameTextField.getText().trim();

                // create the normalized resource id which is needed to form the unique key
                resourceId = parentRecord.getResourceIdentifier().replace(" ","");
                resourceId = resourceId.replace(".", "-");

                // Get the use statement which is needed to create a shootlist. Default to Image-Service
                // if none is provided
                exportUseStatement = (String)importUseStatementComboBox.getSelectedItem();

                if(exportUseStatement.contains("Select")) {
                    exportUseStatement = "Image-Service";
                }

                try {
                    // if we exporting component titles then put it in the header
                    String titleHeader = "\t";
                    String optionalColumns = "\t";
                    String lineEnd = "\n";
                    String useStatementHeader = ImportExportUtils.USE_STATEMENT;

                    if(exportTitle) {
                        titleHeader = "\tComponent Title\t";
                    }

                    if(exportOptionalColumns) {
                        if(shootlistBarcodeCheckBox.isSelected() || shootlistFileLevelCheckBox.isSelected()) {
                            optionalColumns = "\t" + ImportExportUtils.getOptionalHeaderColumns() + "\t";
                        } else {
                            optionalColumns = "\t" + useStatementHeader + "\t" + ImportExportUtils.getOptionalHeaderColumns() + "\t";
                        }

                        lineEnd = "\t" + ImportExportUtils.URI_LINK + "\n";
                    }

                    // print the column description and get use statement if needed
                    if (shootlistItemLevelCheckBox.isSelected()) {
                        sb.append("Persistent Id").append(titleHeader).append("Box #\tFolder #\tItem #")
                                .append(optionalColumns).append("Image #").append(lineEnd);
                    } else if(shootlistBarcodeCheckBox.isSelected()) {
                        sb.append("Persistent Id").append(titleHeader).append("Barcode\t").append(useStatementHeader)
                                .append(optionalColumns).append("Filename").append(lineEnd);
                    } else if(shootlistFileLevelCheckBox.isSelected()) {
                        sb.append("Persistent Id\tSeries/Subseries\tTitle\t").append(useStatementHeader)
                                .append(optionalColumns).append("Filename 1").append(lineEnd);
                    } else {
                        sb.append("Persistent Id").append(titleHeader).append("Box #\tFolder #")
                                .append(optionalColumns).append("Image #").append(lineEnd);
                    }

                    // load the resource components
                    for (ResourcesComponents component : resourcesCommon.getResourcesComponents()) {
                        processComponentsForExport(component, sb, monitor);
                    }

                    // close the monitor
                    monitor.close();

                    // show this in the export log dialog
                    ImportExportLogDialog logDialog = new ImportExportLogDialog(owner, ImportExportLogDialog.DIALOG_TYPE_EXPORT, sb.toString());
                    logDialog.setTitle("Information Export");
                    logDialog.showDialog();
                } catch (Exception e) {
                    monitor.close();

                    JOptionPane.showMessageDialog(owner,
                            "There was a problem processing the specified file",
                            "Error Loading File",
                            JOptionPane.ERROR_MESSAGE);

                    e.printStackTrace();
                }

                // re-enable the export button
                exportButton.setEnabled(true);
            }
        }, "Information Export");
        performer.start();
    }

    /**
     * Method to export the URI of digital objects
     */
    private void exportURIButtonActionPerformed() {
        Thread performer = new Thread(new Runnable() {
            public void run() {
                // disable the export button
                exportURIButton.setEnabled(false);

                InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(getParentDialog(), 1000, false);
                monitor.start("Exporting Digital Object URIs...");

                StringBuilder sb = new StringBuilder();

                // set whether to export the title
                exportTitle = exportTitleCheckBox.isSelected();

                try {
                    // if we exporting titles then put it in the header
                    String titleHeader = "\t";
                    if(exportTitle) {
                        titleHeader = "\tDigital Object Title\t";
                    }

                    // print the column descriptions
                    sb.append("URI #").append(titleHeader).append("METS Identifier\tURI ID\tURI Link\n");

                    // reset uri count and load the resource components
                    uriCount = 0;
                    for (ResourcesComponents component : resourcesCommon.getResourcesComponents()) {
                        processComponentsForURIExportOrImport(component, sb, null, monitor);
                    }

                    // close the monitor
                    monitor.close();

                    // show this in the export log dialog
                    ImportExportLogDialog logDialog = new ImportExportLogDialog(owner, ImportExportLogDialog.DIALOG_TYPE_EXPORT, sb.toString());
                    logDialog.setTitle("Digital Object URI Export");
                    logDialog.showDialog();
                } catch (Exception e) {
                    monitor.close();

                    JOptionPane.showMessageDialog(owner,
                            "There was a problem processing the specified file",
                            "Error Loading File",
                            JOptionPane.ERROR_MESSAGE);

                    e.printStackTrace();
                }

                // re-enable the export button
                exportURIButton.setEnabled(true);
            }
        }, "Information Export");
        performer.start();
    }

    /**
     * Method to process all the components in the series or subseries. This make
     * recursive calls
     *
     * @param component The Resource component to process
     */
    private void processComponentsForExport(ResourcesComponents component, StringBuilder sb, InfiniteProgressPanel monitor) {
        // see if the process was cancelled
        if(monitor.isProcessCancelled()) {
            return;
        }

        // get any children components
        if (component.isHasChild()) {
            for (ResourcesComponents childComponent : component.getResourcesComponents()) {
                processComponentsForExport(childComponent, sb, monitor);
            }
        }

        // indicate the resource component that being processed
        monitor.setTextLine("Resource Component : " + component, 2);

        // check to see if we exporting information for file level
        // records or analog instances
        if(!shootlistFileLevelCheckBox.isSelected()) {
            // check to if this component has any instance. If it does then export
            // the box folder list
            Set<ArchDescriptionInstances> instances = component.getInstances();
            for (ArchDescriptionInstances instance : instances) {
                if (instance instanceof ArchDescriptionAnalogInstances) {
                    ArchDescriptionAnalogInstances analogInstance = (ArchDescriptionAnalogInstances) instance;

                    // If no boxName is provide, then export all boxes/instances found
                    if (boxName.length() == 0) {
                        exportBoxInformation(component, analogInstance, sb, monitor);
                    } else if (boxName.equals(analogInstance.getContainer1Indicator())) { // ok we have boxName so need to verify the box is the same
                        exportBoxInformation(component, analogInstance, sb, monitor);
                    }
                }
            }
        } else if(component.getLevel().equals("file")) {
            exportFileLevelInformation(component, sb, monitor);
        }

        // check to see if the component has the attachment keyword
        // as part of the processing note, in which case we need
        // to extract information from this note.
        if(doesComponentHaveAttachmentKeyword(component)) {
            exportProcessingNoteInformation(component, sb, monitor);
        }
    }

    /**
     * Method to process all the components in the series or subseries. This make
     * recursive calls
     *
     * @param component The Resource component to process
     */
    private void processComponentsForURIExportOrImport(ResourcesComponents component, StringBuilder sb, HashMap<Long,String> uriMap, InfiniteProgressPanel monitor) {
        // see if the process was cancelled
        if(monitor.isProcessCancelled()) {
            return;
        }

        // get any children components
        if (component.isHasChild()) {
            for (ResourcesComponents childComponent : component.getResourcesComponents()) {
                processComponentsForURIExportOrImport(childComponent, sb, uriMap, monitor);
            }
        }

        // indicate the resource component that being processed
        monitor.setTextLine("Resource Component : " + component, 2);

        // get the digital instance to export information for
        Set<ArchDescriptionInstances> instances = component.getInstances();
        for (ArchDescriptionInstances instance : instances) {
            if (instance instanceof ArchDescriptionDigitalInstances) {
                ArchDescriptionDigitalInstances digitalInstance = (ArchDescriptionDigitalInstances) instance;

                DigitalObjects digitalObject = digitalInstance.getDigitalObject();
	            String metsId = digitalObject.getMetsIdentifier();

                // get the digital object title if needed
                String title = "\t";
                if(exportTitle) {
                    title = "\t" + digitalObject.getTitle() + "\t";
                }

	            // for all the file versions add the URI
	            Set fileVersions = digitalObject.getFileVersions();
	            for(Object object: fileVersions) {
		            FileVersions fileVersion = (FileVersions)object;
                    Long id = fileVersion.getFileVersionId();

                    // see if we are exporting the uri or importing a new uri to replace the current one with
                    if(uriMap == null) {
                        String uri = fileVersion.getUri();

		                sb.append(++uriCount);
                        sb.append(title);
                        sb.append(metsId).append("\t");
                        sb.append(id).append("\t");
                        sb.append(uri).append("\n");
                    } else {
                        String uri = uriMap.get(id);

                        if(uri != null) {
                            fileVersion.setUri(uri);

                            sb.append(++uriCount);
                            sb.append(title);
                            sb.append(metsId).append("\t");
                            sb.append(uri).append("\n");
                        }
                    }
	            }
            }
        }
    }

    /**
     * Method to extract information from analog instance and export it as a line
     * of text
     *
     * @param component
     * @param analogInstance
     * @param sb
     * @param monitor
     */
    private void exportBoxInformation(ResourcesComponents component, ArchDescriptionAnalogInstances analogInstance, StringBuilder sb, InfiniteProgressPanel monitor) {
        // alert the user which instance is being exported
        monitor.setTextLine("Exporting instance : " + analogInstance.getInstanceLabel(), 4);

        // attach the resource id to persistent id to form unique key,
        // which can be used for constructing unique filename
        String persistentId = resourceId + "_" + component.getPersistentId();

        // if we exporting component title then get the cleaned up title
        String componentTitle = "\t";

        if(exportTitle) {
            componentTitle = "\t" + ImportExportUtils.getSafeTitle(component.getTitle()) + "\t";
        }

        if (shootlistItemLevelCheckBox.isSelected()) {
            sb.append(persistentId);
            sb.append(componentTitle);
            sb.append(analogInstance.getContainer1Indicator()).append("\t");
            sb.append(analogInstance.getContainer2Indicator()).append("\t");
            sb.append(analogInstance.getContainer3Indicator()).append("\t");
            sb.append(" ").append("\n");
        } else if (shootlistBarcodeCheckBox.isSelected()) {
            sb.append(persistentId);
            sb.append(componentTitle);
            sb.append(analogInstance.getBarcode()).append("\t");
            sb.append(" \t ").append("\n");
        } else {
            sb.append(persistentId);
            sb.append(componentTitle);
            sb.append(analogInstance.getContainer1Indicator()).append("\t");
            sb.append(analogInstance.getContainer2Indicator()).append("\t");
            sb.append(" ").append("\n");
        }
    }

    /**
     * Method to export information for file level components used to
     * create a shootlist for born digital collection
     *
     * @param component
     * @param sb
     * @param monitor
     */
    private void exportFileLevelInformation(ResourcesComponents component, StringBuilder sb, InfiniteProgressPanel monitor) {
        // alert the user which component is being exported
        String title = ImportExportUtils.getSafeTitle(component.getTitle());
        String parentTitle = "None";

        if(component.getParent() != null) {
            parentTitle = ImportExportUtils.getSafeTitle(component.getParent().getTitle());
        }

        monitor.setTextLine("Exporting File level Component: " + title, 4);

        // attach the resource id to persistent id to form unique key,
        // which can be used for constructing unique filename
        String persistentId = resourceId + "_" + component.getPersistentId();

        sb.append(persistentId).append("\t");
        sb.append(parentTitle).append("\t");
        sb.append(title).append("\t").append(exportUseStatement);
        sb.append("\t ").append("\n");
    }

    /**
     * Method to export information from components in which the attachment keyword
     * is part of the repository processing note.
     *
     * Get the use statement which should be in the form "Attachment:Text-Service" etc. Those lines would then
     * cleaned up?
     *
     *
     * @param component
     * @param sb
     * @param monitor
     */
    private void exportProcessingNoteInformation(ResourcesComponents component, StringBuilder sb, InfiniteProgressPanel monitor) {
        // get the repository processing note and split into lines. If line contains the keyword attachment
        // get the use statement which should be in the form attachment:Text-Service etc. Those lines would then
        // cleaned up?
        String processingNote = component.getRepositoryProcessingNote();
        String[] lines = processingNote.split("\n");

        // get the component title
        String title = component.getTitle();

        // get the persistent id
        String persistentId = resourceId + "_" + component.getPersistentId();

        // process each line looking for the attachment keyword
        String useStatement = "";

        for(int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            if(line.toLowerCase().contains("attachment")) {
                monitor.setTextLine("Exporting for Attachment Keyword: " + title, 4);

                // check to see if the use statement is not part of the attachment statement
                if (line.indexOf(":") == -1) {
                    useStatement = DEFAULT_USESTATEMENT;
                } else {
                    useStatement = line.split(":")[1].trim();
                }

                // now add the information for this
                sb.append(persistentId).append("\t");
                sb.append(title).append("\t");
                sb.append("Attachment").append("\t").append(useStatement);
                sb.append("\t ").append("\n");
            }
        }

        // now update the processing note. (3/23/2011) For now just remove all the text in the
        // repository processing note.
        if(!runningInStandAloneMode) {
            component.setRepositoryProcessingNote("");
            ApplicationFrame.getInstance().setRecordDirty();
        }
    }

    /**
     * Method to check to see if there is the attachment keyword in the
     * repository processing note which indicates that a digital object
     * should be linked to this component.
     *
     * The format of the keyword are the following
     * Attachment
     * Attachment:"use statement"
     *
     * In the first case the defualt use statment is used (Text-Service)
     * In the second one the use case is specified. This can be any valid
     * keyword
     *
     * @param component
     * @return boolean specifying if attachment keyword is in this components repository processing note
     */
    private boolean doesComponentHaveAttachmentKeyword(ResourcesComponents component) {
        String processingNote = component.getRepositoryProcessingNote().toLowerCase();

        return processingNote.contains("attachment");
    }


    /**
     * Close this dialog
     */
    private void cancelButtonActionPerformed() {
        setVisible(false);
        dispose();
    }

    /**
     * Method to import a shoot list consisting of a list of images number and
     * folder number. But not box number, which is entered in the user interface.
     * There is also an option to do item level import which created 2 digital
     * objects per image in the list
     */
    private void importShootListButtonActionPerformed() {
        final ATFileChooser filechooser = new ATFileChooser();

        if (filechooser.showOpenDialog(this, "Import") == JFileChooser.APPROVE_OPTION) {
            Thread performer = new Thread(new Runnable() {
                public void run() {
                    // disable the import button
                    importShootListButton.setEnabled(false);

                    InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(owner, 1000, true);
                    monitor.start("Importing Digital Objects...");

                    try {
                        // create string builder which will be used to store import log
                        StringBuilder sb = new StringBuilder();

                        File file = filechooser.getSelectedFile();

                        // get the box number
                        String boxNumber = "";

                        // get whether this is item level
                        boolean isItemLevel = itemLevelCheckBox.isSelected();
                        boolean isBarcode = barcodeCheckBox.isSelected();
                        boolean isFileLevel = fileLevelCheckBox.isSelected();
                        boolean isFileList  = fileListCheckBox.isSelected();

                        sb.append("Imported ").append(file.getName()).append("\n\n");

                        // set the correct handle and noid minter service urls based on the type
                        // of handles we are creating.
                        int handleType = handleServiceUrlComboBox.getSelectedIndex();

                        String handleServiceUrl = "";

                        if (handleType == 1) {
                            handleServiceUrl = WebServiceHelper.RESTFUL_HANDLE_URL;
                        } else if (handleType == 2){ // using test handles
                            handleServiceUrl = WebServiceHelper.RESTFUL_HANDLE_TEST_URL;
                        } else if (handleType == 3){ // just read the URI from the file
                            handleServiceUrl = WebServiceHelper.RESTFUL_HANDLE_FILE_URL;
                        } else if (handleType == 4){ // create based on filename
                            handleServiceUrl = WebServiceHelper.RESTFUL_HANDLE_FILENAME_URL;
                        } else { // just set this to the dummy url
                            handleServiceUrl = WebServiceHelper.RESTFUL_HANDLE_DUMMY_URL;
                        }

                        // get the user id and password for connecting to the handle service
                        String userID = userIDTextField.getText();
                        String password = new String(passwordField.getPassword());

                        // now attach the digital object
                        DOCreatorNYU doCreator = new DOCreatorNYU();

                        doCreator.setHandleServiceUrl(handleServiceUrl, handleUrlTextField.getText().trim(),
                                userID, password);

                        // set the url "bit" that is appended to form the thumbnail url
                        doCreator.setThumbnailURL(thumbnailURLTextbox.getText().trim());

                        doCreator.createAndAttachDigitalObjectsFromWorkOrder(file, monitor, sb,
                                parentRecord, resourcesCommon, isItemLevel, isBarcode, isFileLevel, isFileList);

                        // close the monitor
                        monitor.close();

                        // set this record dirty so that the user is ask if they want to save the resource record
                        ApplicationFrame.getInstance().setRecordDirty();

                        // show the import log
                        ImportExportLogDialog logDialog = new ImportExportLogDialog(owner, ImportExportLogDialog.DIALOG_TYPE_IMPORT, sb.toString());
                        logDialog.setTitle("Digital Object Import \"" + file.getName() + "\"");
                        logDialog.showDialog();
                    } catch (Exception e) {
                        e.printStackTrace();

                        // alert the user that some error occurred
                        monitor.close();

                        JOptionPane.showMessageDialog(null,
                        e.getMessage(),
                        "Import Error",
                        JOptionPane.ERROR_MESSAGE);
                    }

                    // re-enable the import button
                    importShootListButton.setEnabled(true);
                }
            }, "Import Digital Objects");
            performer.start();
        }
    }

    /**
     * Method to clear selected options and box name textfields
     */
    private void clearButtonActionPerformed() {
        // use the hidden hide checkboxes to un-check radio buttons
        shootlistHideCheckBox.setSelected(true);
        hideCheckBox.setSelected(true);

        // clear the box name text fields
        exportBoxNameTextField.setText("");
    }

    /**
     * Method which will unlink all digital objects from this resource component
     */
    private void unlinkDigitalObjectButtonActionPerformed() {
        String message = "Really unlink all digital objects from this\n" +
                "collection/component, and all child records?";

        // confirm that user actually wants to do this since it
        // can't easily be undone
        int answer = JOptionPane.showConfirmDialog(this,
            message, "Unlink Confirmation", JOptionPane.YES_NO_OPTION);

        if(answer == JOptionPane.YES_OPTION) {
            Thread performer = new Thread(new Runnable() {
            public void run() {
                // set the digital object unlink count to zero
                unlinkCount = 0;

                // disable the unlink digital object button
                unlinkDigitalObjectButton.setEnabled(false);

                InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(getParentDialog(), 1000, false);
                monitor.start("Unlinking Digital Object ...");

                StringBuilder sb = new StringBuilder();

                try {
                    // load the resource components
                    for (ResourcesComponents component : resourcesCommon.getResourcesComponents()) {
                        processComponentsForDigitalObjectUnLinking(component, sb, monitor);
                    }

                    // close the monitor
                    monitor.close();

                    String message = sb.toString();

                    // append the word TEST to end to indicate that this was only a test
                    if(testUnlinkCheckBox.isSelected()) {
                        message = message + "\n\n***TEST OF UNLINK FUNCTIONALITY***";
                    }

                    if(message.length() < 5) {
                        message = "No Digital Objects Found to Unlink ...";
                    } else {
                        message = unlinkCount + " Digital Object Unlinked\n" + message;

                        // set this record dirty so that the user is ask if they want to save the resource record
                        ApplicationFrame.getInstance().setRecordDirty();
                    }

                    // show message in the export log dialog
                    ImportExportLogDialog logDialog = new ImportExportLogDialog(owner, ImportExportLogDialog.DIALOG_TYPE_EXPORT, message);
                    logDialog.setTitle("Unlink Digital Object Log");
                    logDialog.showDialog();
                } catch (Exception e) {
                    monitor.close();

                    JOptionPane.showMessageDialog(owner,
                            "There was a problem unlinking digital objects",
                            "Error Unlinking Digital Objects",
                            JOptionPane.ERROR_MESSAGE);

                    e.printStackTrace();
                }

                // re-enable the unlink button
                unlinkDigitalObjectButton.setEnabled(true);
            }
        }, "Information Export");
        performer.start();
        }
    }

    /**
     * Method to process all the components in the series or subseries to unlink digital
     * objects
     *
     * @param component The Resource component to process
     */
    private void processComponentsForDigitalObjectUnLinking(ResourcesComponents component, StringBuilder sb, InfiniteProgressPanel monitor) {
        // get any children components
        if (component.isHasChild()) {
            for (ResourcesComponents childComponent : component.getResourcesComponents()) {
                processComponentsForDigitalObjectUnLinking(childComponent, sb, monitor);
            }
        }

        // indicate the resource component that's being processed
        monitor.setTextLine("Searching Resource Component : " + component, 2);

        ArrayList<ArchDescriptionInstances> instances = new ArrayList<ArchDescriptionInstances>(component.getInstances());

        for (ArchDescriptionInstances instance : instances) {
            if (instance instanceof ArchDescriptionDigitalInstances) {
                try {
                    if(!testUnlinkCheckBox.isSelected()) {
                        component.removeRelatedObject(instance);
                    }

                    // increment the count of digital of objects that were unlinked
                    unlinkCount++;

                    monitor.setTextLine("Unlinking Digital Object " + instance.getInstanceLabel(), 4);

                    sb.append("Unlinked Digital Object ").append(instance.getInstanceLabel());
                    sb.append(" From ").append(component.getTitle()).append("\n");
                } catch (Exception e) {
                    sb.append("Failed to Unlink Digital Object ").append(instance.getInstanceLabel());
                    sb.append(" From ").append(component.getTitle()).append("\n");

                    e.printStackTrace();
                }
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        nameLabel = new JLabel();
        label2 = new JLabel();
        persistentLabel = new JLabel();
        importButton = new JButton();
        exportButton = new JButton();
        panel2 = new JPanel();
        label4 = new JLabel();
        exportBoxNameTextField = new JTextField();
        shootlistBarcodeCheckBox = new JCheckBox();
        shootlistItemLevelCheckBox = new JCheckBox();
        shootlistFileLevelCheckBox = new JCheckBox();
        importUseStatementComboBox = new JComboBox();
        shootlistHideCheckBox = new JCheckBox();
        importShootListButton = new JButton();
        panel1 = new JPanel();
        barcodeCheckBox = new JCheckBox();
        itemLevelCheckBox = new JCheckBox();
        fileLevelCheckBox = new JCheckBox();
        hideCheckBox = new JCheckBox();
        fileListCheckBox = new JCheckBox();
        label9 = new JLabel();
        panel5 = new JPanel();
        exportURIButton = new JButton();
        importURIButton = new JButton();
        unlinkDigitalObjectButton = new JButton();
        testUnlinkCheckBox = new JCheckBox();
        panel3 = new JPanel();
        label6 = new JLabel();
        handleServiceUrlComboBox = new JComboBox();
        label7 = new JLabel();
        handleUrlTextField = new JTextField();
        label3 = new JLabel();
        thumbnailURLTextbox = new JTextField();
        panel4 = new JPanel();
        label5 = new JLabel();
        userIDTextField = new JTextField();
        label8 = new JLabel();
        passwordField = new JPasswordField();
        buttonBar = new JPanel();
        clearButton = new JButton();
        exportTitleCheckBox = new JCheckBox();
        optionalColumnsCheckBox = new JCheckBox();
        cancelButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setTitle("Digital Object Workflow 2.2 (01/29/2013)");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.DIALOG_BORDER);
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setBorder(null);
                contentPanel.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    },
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //---- label1 ----
                label1.setText("Component Name");
                contentPanel.add(label1, cc.xy(1, 1));

                //---- nameLabel ----
                nameLabel.setText("name");
                nameLabel.setForeground(Color.blue);
                contentPanel.add(nameLabel, cc.xy(3, 1));

                //---- label2 ----
                label2.setText("Persistent ID");
                contentPanel.add(label2, cc.xy(1, 3));

                //---- persistentLabel ----
                persistentLabel.setText("perstent id");
                persistentLabel.setForeground(Color.blue);
                contentPanel.add(persistentLabel, cc.xy(3, 3));

                //---- importButton ----
                importButton.setText("Import Digital Object");
                importButton.setVisible(false);
                importButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        importButtonActionPerformed();
                    }
                });
                contentPanel.add(importButton, cc.xy(1, 5));

                //---- exportButton ----
                exportButton.setText("Export Work Order");
                exportButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        exportActionPerformed();
                    }
                });
                contentPanel.add(exportButton, cc.xy(1, 7));

                //======== panel2 ========
                {
                    panel2.setLayout(new FlowLayout(FlowLayout.LEFT));

                    //---- label4 ----
                    label4.setText("Box #");
                    panel2.add(label4);

                    //---- exportBoxNameTextField ----
                    exportBoxNameTextField.setColumns(2);
                    panel2.add(exportBoxNameTextField);

                    //---- shootlistBarcodeCheckBox ----
                    shootlistBarcodeCheckBox.setText("barcode");
                    panel2.add(shootlistBarcodeCheckBox);

                    //---- shootlistItemLevelCheckBox ----
                    shootlistItemLevelCheckBox.setText("item level");
                    panel2.add(shootlistItemLevelCheckBox);

                    //---- shootlistFileLevelCheckBox ----
                    shootlistFileLevelCheckBox.setText("file level");
                    panel2.add(shootlistFileLevelCheckBox);

                    //---- importUseStatementComboBox ----
                    importUseStatementComboBox.setModel(new DefaultComboBoxModel(new String[] {
                        "Select Use Statement",
                        "Audio-Service",
                        "Image-Service",
                        "Image-Service/Thumbnail",
                        "Text-Service",
                        "Video-Service"
                    }));
                    importUseStatementComboBox.setToolTipText("Select the Default Use Statement to use when creating digital objects on import.\nDefaults to Image-Service if none is selected.");
                    panel2.add(importUseStatementComboBox);

                    //---- shootlistHideCheckBox ----
                    shootlistHideCheckBox.setText("hide");
                    shootlistHideCheckBox.setVisible(false);
                    panel2.add(shootlistHideCheckBox);
                }
                contentPanel.add(panel2, cc.xy(3, 7));

                //---- importShootListButton ----
                importShootListButton.setText("Import Work Order");
                importShootListButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        importShootListButtonActionPerformed();
                    }
                });
                contentPanel.add(importShootListButton, cc.xy(1, 9));

                //======== panel1 ========
                {
                    panel1.setLayout(new FlowLayout(FlowLayout.LEFT));

                    //---- barcodeCheckBox ----
                    barcodeCheckBox.setText("barcode");
                    panel1.add(barcodeCheckBox);

                    //---- itemLevelCheckBox ----
                    itemLevelCheckBox.setText("item level");
                    panel1.add(itemLevelCheckBox);

                    //---- fileLevelCheckBox ----
                    fileLevelCheckBox.setText("file level");
                    panel1.add(fileLevelCheckBox);

                    //---- hideCheckBox ----
                    hideCheckBox.setText("hide");
                    hideCheckBox.setVisible(false);
                    panel1.add(hideCheckBox);

                    //---- fileListCheckBox ----
                    fileListCheckBox.setText("file list");
                    panel1.add(fileListCheckBox);
                }
                contentPanel.add(panel1, cc.xy(3, 9));

                //---- label9 ----
                label9.setText("Additional Functions");
                label9.setHorizontalAlignment(SwingConstants.CENTER);
                label9.setFont(new Font("Lucida Grande", Font.BOLD, 13));
                contentPanel.add(label9, cc.xy(1, 11));

                //======== panel5 ========
                {
                    panel5.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC
                        },
                        new RowSpec[] {
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC
                        }));

                    //---- exportURIButton ----
                    exportURIButton.setText("Export Digital Object URI List");
                    exportURIButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            exportURIButtonActionPerformed();
                        }
                    });
                    panel5.add(exportURIButton, cc.xy(1, 1));

                    //---- importURIButton ----
                    importURIButton.setText("Import Digital Object URI List");
                    importURIButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            importURIButtonActionPerformed();
                        }
                    });
                    panel5.add(importURIButton, cc.xy(1, 3));

                    //---- unlinkDigitalObjectButton ----
                    unlinkDigitalObjectButton.setText("Unlink Digital Objects");
                    unlinkDigitalObjectButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            unlinkDigitalObjectButtonActionPerformed();
                        }
                    });
                    panel5.add(unlinkDigitalObjectButton, cc.xy(1, 5));

                    //---- testUnlinkCheckBox ----
                    testUnlinkCheckBox.setText("Test Only");
                    panel5.add(testUnlinkCheckBox, cc.xy(3, 5));
                }
                contentPanel.add(panel5, cc.xy(3, 11));

                //======== panel3 ========
                {
                    panel3.setBorder(new TitledBorder(null, "URI Source Configuration", TitledBorder.LEADING, TitledBorder.TOP));
                    panel3.setLayout(new GridBagLayout());
                    ((GridBagLayout)panel3.getLayout()).columnWidths = new int[] {0, 0, 0};
                    ((GridBagLayout)panel3.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0};
                    ((GridBagLayout)panel3.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
                    ((GridBagLayout)panel3.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};

                    //---- label6 ----
                    label6.setText("URI Source");
                    panel3.add(label6, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- handleServiceUrlComboBox ----
                    handleServiceUrlComboBox.setModel(new DefaultComboBoxModel(new String[] {
                        "No URI",
                        "Production Handle Service",
                        "Test Handle Service",
                        "Read From File",
                        "Default URL + Filename"
                    }));
                    panel3.add(handleServiceUrlComboBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //---- label7 ----
                    label7.setText("Default URL");
                    panel3.add(label7, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- handleUrlTextField ----
                    handleUrlTextField.setText("http://dlib.nyu.edu/object-in-processing");
                    panel3.add(handleUrlTextField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //---- label3 ----
                    label3.setText("Thumbnail URL");
                    panel3.add(label3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- thumbnailURLTextbox ----
                    thumbnailURLTextbox.setText(DOCreatorNYU.THUMBNAIL_URL);
                    panel3.add(thumbnailURLTextbox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //======== panel4 ========
                    {
                        panel4.setLayout(new GridLayout(1, 4));

                        //---- label5 ----
                        label5.setText("User ID");
                        panel4.add(label5);

                        //---- userIDTextField ----
                        userIDTextField.setText("dlts-rhs");
                        panel4.add(userIDTextField);

                        //---- label8 ----
                        label8.setText("Password");
                        panel4.add(label8);
                        panel4.add(passwordField);
                    }
                    panel3.add(panel4, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                contentPanel.add(panel3, cc.xywh(1, 13, 3, 1));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                buttonBar.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.BUTTON_COLSPEC,
                        FormFactory.RELATED_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.BUTTON_COLSPEC
                    },
                    RowSpec.decodeSpecs("pref")));

                //---- clearButton ----
                clearButton.setText("Clear");
                clearButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        clearButtonActionPerformed();
                    }
                });
                buttonBar.add(clearButton, cc.xy(1, 1));

                //---- exportTitleCheckBox ----
                exportTitleCheckBox.setText("Add Title to Export");
                buttonBar.add(exportTitleCheckBox, cc.xy(3, 1));

                //---- optionalColumnsCheckBox ----
                optionalColumnsCheckBox.setText("Add Optional Columns to Export");
                buttonBar.add(optionalColumnsCheckBox, cc.xy(5, 1));

                //---- cancelButton ----
                cancelButton.setText("Close");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        cancelButtonActionPerformed();
                    }
                });
                buttonBar.add(cancelButton, cc.xy(9, 1));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());

        //---- buttonGroup3 ----
        ButtonGroup buttonGroup3 = new ButtonGroup();
        buttonGroup3.add(shootlistBarcodeCheckBox);
        buttonGroup3.add(shootlistItemLevelCheckBox);
        buttonGroup3.add(shootlistFileLevelCheckBox);
        buttonGroup3.add(shootlistHideCheckBox);

        //---- buttonGroup4 ----
        ButtonGroup buttonGroup4 = new ButtonGroup();
        buttonGroup4.add(barcodeCheckBox);
        buttonGroup4.add(itemLevelCheckBox);
        buttonGroup4.add(fileLevelCheckBox);
        buttonGroup4.add(hideCheckBox);
        buttonGroup4.add(fileListCheckBox);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JLabel nameLabel;
    private JLabel label2;
    private JLabel persistentLabel;
    private JButton importButton;
    private JButton exportButton;
    private JPanel panel2;
    private JLabel label4;
    private JTextField exportBoxNameTextField;
    private JCheckBox shootlistBarcodeCheckBox;
    private JCheckBox shootlistItemLevelCheckBox;
    private JCheckBox shootlistFileLevelCheckBox;
    private JComboBox importUseStatementComboBox;
    private JCheckBox shootlistHideCheckBox;
    private JButton importShootListButton;
    private JPanel panel1;
    private JCheckBox barcodeCheckBox;
    private JCheckBox itemLevelCheckBox;
    private JCheckBox fileLevelCheckBox;
    private JCheckBox hideCheckBox;
    private JCheckBox fileListCheckBox;
    private JLabel label9;
    private JPanel panel5;
    private JButton exportURIButton;
    private JButton importURIButton;
    private JButton unlinkDigitalObjectButton;
    private JCheckBox testUnlinkCheckBox;
    private JPanel panel3;
    private JLabel label6;
    private JComboBox handleServiceUrlComboBox;
    private JLabel label7;
    private JTextField handleUrlTextField;
    private JLabel label3;
    private JTextField thumbnailURLTextbox;
    private JPanel panel4;
    private JLabel label5;
    private JTextField userIDTextField;
    private JLabel label8;
    private JPasswordField passwordField;
    private JPanel buttonBar;
    private JButton clearButton;
    private JCheckBox exportTitleCheckBox;
    private JCheckBox optionalColumnsCheckBox;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
