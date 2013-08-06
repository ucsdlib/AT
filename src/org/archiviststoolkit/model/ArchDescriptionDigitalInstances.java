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

package org.archiviststoolkit.model;

import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.mydomain.SequencedObject;
import org.archiviststoolkit.mydomain.DnDDigitalObjectTree;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.util.NoteEtcTypesUtils;
import org.archiviststoolkit.swing.jTreeDnD.DnDUtils;
import org.archiviststoolkit.structure.NotesEtcTypes;
import org.archiviststoolkit.ApplicationFrame;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.*;
import java.io.*;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

public class ArchDescriptionDigitalInstances extends ArchDescriptionInstances implements Serializable, Comparable {


    public static final String PROPERTYNAME_CONTAINER1_VALUE = "container1Value";
    public static final String PROPERTYNAME_CONTAINER1_TYPE = "container1Type";
    public static final String PROPERTYNAME_CONTAINER2_VALUE = "container2Value";
    public static final String PROPERTYNAME_CONTAINER2_TYPE = "container2Type";
    public static final String PROPERTYNAME_CONTAINER3_VALUE = "container3Value";
    public static final String PROPERTYNAME_CONTAINER3_TYPE = "container3Type";
    public static final String PROPERTYNAME_CONTAINER_LABEL = "containerLabel";
    public static final String PROPERTYNAME_PARENT_RESOURCE = "parentResource";

    private DigitalObjects digitalObject = null;

    //	private boolean isNewRecord = false;
    private transient DnDDigitalObjectTree digitalObjectJTree;
    private transient JTree plainJTree;

    // vector that stores the notes etc types allowed in digital records
    private Vector allowedNoteEtcTypes;

    // boolean which indicated whether to link a digital object or to create one
    boolean createDigitalObject = true;

    // set the parent resource for this digital instance. This is needed to allow searching
    // digital objects by resources.
    private Resources parentResource = null;

    /**
     * No-arg constructor for JavaBean tools.
     */
    public ArchDescriptionDigitalInstances() {
        super();
        initializeDigitalObjectTree(null);
    }

    /**
     * Constructor that just takes a digital object as an argument
     * 
     * @param digitalObject
     */
    public ArchDescriptionDigitalInstances(DigitalObjects digitalObject) {
        super();
        this.digitalObject = digitalObject;
        createDigitalObject = false;
    }

    public ArchDescriptionDigitalInstances(ResourcesCommon archDescription) {
        super(archDescription);
        initializeDigitalObjectTree(archDescription);
    }

    /**
     * Constructor that takes a resource component/resource and the parent resource
     * @param archDescription This may be a resource component or resource
     * @param parentResource The parent resource component
     */
    public ArchDescriptionDigitalInstances(ResourcesCommon archDescription, Resources parentResource) {
        super(archDescription);
        initializeDigitalObjectTree(archDescription);
        this.parentResource = parentResource;
    }

    public ArchDescriptionDigitalInstances(ResourcesCommon archDescription, DigitalObjects digitalObject, Resources parentResource) {
        super(archDescription);
        this.digitalObject = digitalObject;
        this.parentResource = parentResource;
        createDigitalObject = false;
        initializeDigitalObjectTree(archDescription);
    }
    
    public String getInstanceLabel() {
        if (digitalObject != null) {
            return digitalObject.getObjectLabel();
        } else {
            return "Missing digital object";
        }
    }

    private void initializeDigitalObjectTree(ResourcesCommon archDescription) {
        if(createDigitalObject) {
            digitalObject = new DigitalObjects();
        } else {
            digitalObject.setDigitalInstance(this);
            return;
        }

        if (archDescription != null) {
            digitalObject.setRepository(ApplicationFrame.getInstance().getCurrentUserRepository());
                
            digitalObject.setTitle(StringHelper.tagRemover(archDescription.getTitle()));
            digitalObject.setLanguageCode(archDescription.getLanguageCode());
            digitalObject.setDateExpression(archDescription.getDateExpression());
            digitalObject.setDateBegin(archDescription.getDateBegin());
            digitalObject.setDateEnd(archDescription.getDateEnd());
            digitalObject.setRestrictionsApply(archDescription.getRestrictionsApply());

            // add subject links
            for (ArchDescriptionSubjects subjectLink : archDescription.getSubjects()) {
                try {
                    digitalObject.addSubject(subjectLink.getSubject());
                } catch (DuplicateLinkException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage() + " is already linked to this record");
                }
            }

            // add name links
            for (ArchDescriptionNames nameLink : archDescription.getNames()) {
                try {
                    if (!nameLink.getNameLinkFunction().equalsIgnoreCase(ArchDescriptionNames.PROPERTYNAME_FUNCTION_SOURCE)) {
                        digitalObject.addName(new ArchDescriptionNames(
                                nameLink.getName(),
                                digitalObject,
                                nameLink.getRole(),
                                nameLink.getNameLinkFunction(),
                                nameLink.getForm()));
                    }
                } catch (DuplicateLinkException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage() + " is already linked to this record");
                }
            }

            // add any notes from resource or resource component
            // TODO move this code to a utility class
            ArchDescriptionNotes aNote;
//			int sequenceOrder = 1;
            for (ArchDescriptionRepeatingData repeatingData : archDescription.getRepeatingData()) {
                if (repeatingData instanceof ArchDescriptionNotes) {
                    aNote = (ArchDescriptionNotes) repeatingData;
                    if(canAddNoteToDigitalObject(aNote)) {
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

        digitalObject.setDigitalInstance(this);
    }

    /**
     * Check to see if this note should be added to a digital object
     *
     * @param aNote The note to check to see if it can be added to the digital object
     * @return boolean idicating whether the particular note can be added
     * to a digital object
     */
    public boolean canAddNoteToDigitalObject(ArchDescriptionNotes aNote) {
        NotesEtcTypes notesEtcType = aNote.getNotesEtcType();
        return NoteEtcTypesUtils.getDigitalObjectNotesTypesList().contains(notesEtcType);
    }

    public DigitalObjects getDigitalObject() {
        return digitalObject;
    }

    public void setDigitalObject(DigitalObjects digitalObject) {
        this.digitalObject = digitalObject;
    }

    private void buildResourceJTree() {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(digitalObject);

        Set<DigitalObjects> children = this.digitalObject.getDigitalObjectChildren();
        TreeSet<DigitalObjects> ts = new TreeSet<DigitalObjects>(children);

        for (DigitalObjects thisDigitalObject : ts) {
            buildNodes(thisDigitalObject, top);
        }
        // TODO 8/15/08 DnDDigitalObjectTree is proable not needed any more as such should be deleted
        DnDDigitalObjectTree tree = new DnDDigitalObjectTree(top);
        this.setDigitalObjectJTree(tree);
        this.setPlainDigitalObjectJTree(new JTree(top));
    }

    private void buildNodes(DigitalObjects digitalObject, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode component = new DefaultMutableTreeNode(digitalObject);
        parent.add(component);

        // This is so in efficient I am cringing, but this is what needs to be done to get things to display correctly?
        Set<DigitalObjects> children = digitalObject.getDigitalObjectChildren();
        TreeSet<DigitalObjects> ts = new TreeSet<DigitalObjects>(children);

        for (DigitalObjects thisDigitalObject : ts) {
            buildNodes(thisDigitalObject, component);
        }
    }

    public JTree getDigitalObjectJTree() {
        this.buildResourceJTree();
//		if (digitalObjectJTree == null) {
//			this.buildResourceJTree();
//		}
        return digitalObjectJTree;
    }

    public void setDigitalObjectJTree(DnDDigitalObjectTree digitalObjectJTree) {
        this.digitalObjectJTree = digitalObjectJTree;
    }

    public JTree getPlainDigitalObjectJTree() {
        this.buildResourceJTree();
        plainJTree.getSelectionModel().setSelectionMode(TreeSelectionModel.
                SINGLE_TREE_SELECTION);
        return plainJTree;
    }

    public void setPlainDigitalObjectJTree(JTree plainJTree) {
        this.plainJTree = plainJTree;
    }

    public void clearPlainDigitalObjectJTree() {
        this.plainJTree = null;
    }

    public Resources getParentResource() {
        return parentResource;
    }

    public void setParentResource(Resources parentResource) {
        this.parentResource = parentResource;
    }
}
