package org.archiviststoolkit.plugin;

import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.swing.ATProgressUtil;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 * Simple class which extract barcodes from the instance type label and places
 * it in the barcode field in an analog instance. This code is proof of concept
 * for the RDE plugins framework
 *
 * <p/>
 * Created by IntelliJ IDEA.
 * User: Nathan Stevens
 * Date: Aug 18, 2010
 * Time: 2:12:37 PM
 */
public class BarcodeCleaner implements RDEPlugin {
    private Resources parentRecord = null; // The parent resource record
    private int barcodesFound = 0; // keep track of the barcodes found and cleaned

    // The default constructor
    public BarcodeCleaner() { }

    /**
     * Method to set the resource record and component record. Only the resource record
     * is used in this class.
     *
     * @param parentRecord
     * @param resourcesCommon
     */
    public void setModel(Resources parentRecord, ResourcesCommon resourcesCommon) {
        this.parentRecord = parentRecord;
    }

    /**
     * Method to specify if this plugin display an dialog or just
     * executes a task
     *
     * @return true if it displays a dialog, false otherwise
     */
    public boolean hasDialog() {
        return true;
    }

    /**
     * Method that called to execute task in which no dialogs are displayed.
     * Its not used here
     */
    public void doTask() { }

    /**
     * Parent window
     *
     * @param jDialog
     * @param title
     */
    public void showPlugin(JDialog jDialog, String title) {
        showPlugin(jDialog, title, false);
    }

    /**
     * actually execute the code that cleans up the barcodes. It execute is a thread
     *
     * @param owner The dialog from which this was opened
     * @param title The title of the dialog. not used here
     */
    public void showPlugin(final Window owner, String title, boolean standalone) {
        Thread performer = new Thread(new Runnable() {
            public void run() {
                InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(owner, 1000, true);
                monitor.start("Cleaning up Barcodes ...");

                try {
                    // process each resource component looking for the instances which have
                    // barcodes encapsulated in parenthesis i.e. (BARCODE)
                    for (ResourcesComponents component : parentRecord.getResourcesComponents()) {
                        processComponentsForBarcodes(component, monitor);
                    }

                    // close the monitor
                    monitor.close();

                    // alert user the number of barcode cleaned
                    String message = "";

                    if(barcodesFound > 0) {
                        message = "Cleaned up \"" + barcodesFound + "\" barcodes";

                        // set this record dirty so that the user is ask if they want
                        // to save the resource record on close
                        ApplicationFrame.getInstance().setRecordDirty();
                    } else {
                        message = "No barcodes found ...";
                    }

                    JOptionPane.showMessageDialog(owner,
                        message,
                        "Barcode Cleaner",
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    monitor.close();
                    e.printStackTrace();
                }
            }
        }, "Barcode Cleaner");
        performer.start();
    }

    /**
     * Method that actually does recursion through resource components looking for barcodes
     *
     * @param component The resource component
     * @param monitor   This is used to inform the user what's going on
     */
    private void processComponentsForBarcodes(ResourcesComponents component, InfiniteProgressPanel monitor) {
        // get any children components
        if (component.isHasChild()) {
            for (ResourcesComponents childComponent : component.getResourcesComponents()) {
                processComponentsForBarcodes(childComponent, monitor);
            }
        }

        // indicate the resource component that being processed
        monitor.setTextLine("Resource Component : " + component, 2);

        // check to if this component has any instance. If it does then export
        // check to see if the instance type has the barcode within parenthesis
        Set<ArchDescriptionInstances> instances = component.getInstances();
        for (ArchDescriptionInstances instance : instances) {
            if (instance instanceof ArchDescriptionAnalogInstances) {
                ArchDescriptionAnalogInstances analogInstance = (ArchDescriptionAnalogInstances) instance;

                // alert the user which instance is being exported
                monitor.setTextLine("Processing Analog Instance : " + analogInstance.getInstanceLabel(), 3);

                // check to see if we have barcode information in instance type
                // in we do extract it out and put it in correct field
                String instanceType = analogInstance.getInstanceType();

                if(instanceType.matches("\\w+\\s*\\w*\\s*\\(\\s*\\d+\\s*\\)")) {
                    String[] sa = instanceType.split("[\\(\\)]");

                    analogInstance.setInstanceType(sa[0].trim());
                    analogInstance.setBarcode(sa[1].trim());

                    // increment the barcodes found counter
                    barcodesFound++;

                    // update the user that a barcode was cleaned up
                    monitor.setTextLine("Cleaning up Barcode : " + instanceType, 4);
                    System.out.println("Found barcode " + instanceType);
                }
            }
        }
    }
}

