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
 *
 * Created by JFormDesigner on Wed Aug 24 16:08:56 EDT 2005
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.swing.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.util.SequencedObjectsUtils;
import org.archiviststoolkit.plugin.ATPlugin;
import org.archiviststoolkit.plugin.ATPluginFactory;

public class SubjectFields extends DomainEditorFields {
    private ArrayList<ATPlugin> plugins = null; // stores any embedded editor plugins

    public SubjectFields() {
		super();
		initComponents();
		initAccess();
        initPlugins();
	}

	private void subjectTermKeyTyped(KeyEvent e) {
		// TODO add your code here
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label_subjectTerm = new JLabel();
        subjectTerm = ATBasicComponentFactory.createTextField(detailsModel.getModel(Subjects.PROPERTYNAME_SUBJECT_TERM));
        label_subjectTermType = new JLabel();
        subjectTermType = ATBasicComponentFactory.createComboBox(detailsModel, Subjects.PROPERTYNAME_SUBJECT_TERM_TYPE, Subjects.class);
        label_subjectSource = new JLabel();
        subjectSource = ATBasicComponentFactory.createComboBox(detailsModel, Subjects.PROPERTYNAME_SUBJECT_SOURCE, Subjects.class);
        label_subjectScopeNote = new JLabel();
        scrollPane1 = new JScrollPane();
        subjectScopeNote = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Subjects.PROPERTYNAME_SUBJECT_SCOPE_NOTE));
        separator3 = new JSeparator();
        label_subjectScopeNote3 = new JLabel();
        scrollPane3 = new JScrollPane();
        accessionsTable = new DomainSortableTable(Accessions.class, Accessions.PROPERTYNAME_ACCESSION_NUMBER);
        separator4 = new JSeparator();
        label_subjectScopeNote2 = new JLabel();
        scrollPane2 = new JScrollPane();
        resourcesTable = new ResourceAndComponentLinkTable(Resources.class, Resources.PROPERTYNAME_RESOURCE_IDENTIFIER);
        label1 = new JLabel();
        separator5 = new JSeparator();
        label_subjectScopeNote4 = new JLabel();
        scrollPane4 = new JScrollPane();
        digitalObjectsTable = new DomainSortableTable(DigitalObjects.class, DigitalObjects.PROPERTYNAME_METS_IDENTIFIER);
        tabbedPane = new JTabbedPane();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBorder(Borders.DLU4_BORDER);
        setOpaque(false);
        setPreferredSize(new Dimension(800, 500));
        setLayout(new FormLayout(
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
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- label_subjectTerm ----
        label_subjectTerm.setText("Subject Term");
        label_subjectTerm.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectTerm, Subjects.class, Subjects.PROPERTYNAME_SUBJECT_TERM);
        add(label_subjectTerm, cc.xy(1, 1));

        //---- subjectTerm ----
        subjectTerm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                subjectTermKeyTyped(e);
            }
        });
        add(subjectTerm, cc.xy(3, 1));

        //---- label_subjectTermType ----
        label_subjectTermType.setText("Subject Term Type");
        label_subjectTermType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectTermType, Subjects.class, Subjects.PROPERTYNAME_SUBJECT_TERM_TYPE);
        add(label_subjectTermType, cc.xy(1, 3));

        //---- subjectTermType ----
        subjectTermType.setOpaque(false);
        add(subjectTermType, cc.xywh(3, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- label_subjectSource ----
        label_subjectSource.setText("Source");
        label_subjectSource.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectSource, Subjects.class, Subjects.PROPERTYNAME_SUBJECT_SOURCE);
        add(label_subjectSource, cc.xy(1, 5));

        //---- subjectSource ----
        subjectSource.setOpaque(false);
        add(subjectSource, cc.xywh(3, 5, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- label_subjectScopeNote ----
        label_subjectScopeNote.setText("Scope Note");
        label_subjectScopeNote.setVerticalAlignment(SwingConstants.TOP);
        label_subjectScopeNote.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_subjectScopeNote, Subjects.class, Subjects.PROPERTYNAME_SUBJECT_SCOPE_NOTE);
        add(label_subjectScopeNote, cc.xywh(1, 7, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

        //======== scrollPane1 ========
        {
            scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane1.setMaximumSize(new Dimension(32767, 100));

            //---- subjectScopeNote ----
            subjectScopeNote.setRows(4);
            subjectScopeNote.setLineWrap(true);
            subjectScopeNote.setTabSize(20);
            subjectScopeNote.setWrapStyleWord(true);
            scrollPane1.setViewportView(subjectScopeNote);
        }
        add(scrollPane1, cc.xy(3, 7));

        //---- separator3 ----
        separator3.setBackground(new Color(220, 220, 232));
        separator3.setForeground(new Color(147, 131, 86));
        separator3.setMinimumSize(new Dimension(1, 10));
        separator3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(separator3, cc.xywh(1, 9, 3, 1));

        //---- label_subjectScopeNote3 ----
        label_subjectScopeNote3.setText("Accessions");
        label_subjectScopeNote3.setVerticalAlignment(SwingConstants.TOP);
        label_subjectScopeNote3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(label_subjectScopeNote3, cc.xywh(1, 11, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

        //======== scrollPane3 ========
        {
            scrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

            //---- accessionsTable ----
            accessionsTable.setPreferredScrollableViewportSize(new Dimension(450, 200));
            scrollPane3.setViewportView(accessionsTable);
        }
        add(scrollPane3, cc.xywh(1, 13, 3, 1));

        //---- separator4 ----
        separator4.setBackground(new Color(220, 220, 232));
        separator4.setForeground(new Color(147, 131, 86));
        separator4.setMinimumSize(new Dimension(1, 10));
        separator4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(separator4, cc.xywh(1, 15, 3, 1));

        //---- label_subjectScopeNote2 ----
        label_subjectScopeNote2.setText("Resources");
        label_subjectScopeNote2.setVerticalAlignment(SwingConstants.TOP);
        label_subjectScopeNote2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(label_subjectScopeNote2, cc.xywh(1, 17, 3, 1));

        //======== scrollPane2 ========
        {
            scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

            //---- resourcesTable ----
            resourcesTable.setPreferredScrollableViewportSize(new Dimension(450, 200));
            scrollPane2.setViewportView(resourcesTable);
        }
        add(scrollPane2, cc.xywh(1, 19, 3, 1));

        //---- label1 ----
        label1.setText("Resources in Red have the subject term linked at the component level");
        label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(label1, cc.xywh(1, 21, 3, 1));

        //---- separator5 ----
        separator5.setBackground(new Color(220, 220, 232));
        separator5.setForeground(new Color(147, 131, 86));
        separator5.setMinimumSize(new Dimension(1, 10));
        separator5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(separator5, cc.xywh(1, 23, 3, 1));

        //---- label_subjectScopeNote4 ----
        label_subjectScopeNote4.setText("Digital Objects");
        label_subjectScopeNote4.setVerticalAlignment(SwingConstants.TOP);
        label_subjectScopeNote4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(label_subjectScopeNote4, cc.xy(1, 25));

        //======== scrollPane4 ========
        {
            scrollPane4.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

            //---- digitalObjectsTable ----
            digitalObjectsTable.setPreferredScrollableViewportSize(new Dimension(450, 200));
            scrollPane4.setViewportView(digitalObjectsTable);
        }
        add(scrollPane4, cc.xywh(1, 27, 3, 1));
        add(tabbedPane, cc.xywh(1, 29, 3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label_subjectTerm;
    public JTextField subjectTerm;
    private JLabel label_subjectTermType;
    public JComboBox subjectTermType;
    private JLabel label_subjectSource;
    public JComboBox subjectSource;
    private JLabel label_subjectScopeNote;
    private JScrollPane scrollPane1;
    public JTextArea subjectScopeNote;
    private JSeparator separator3;
    private JLabel label_subjectScopeNote3;
    private JScrollPane scrollPane3;
    private DomainSortableTable accessionsTable;
    private JSeparator separator4;
    private JLabel label_subjectScopeNote2;
    private JScrollPane scrollPane2;
    private ResourceAndComponentLinkTable resourcesTable;
    private JLabel label1;
    private JSeparator separator5;
    private JLabel label_subjectScopeNote4;
    private JScrollPane scrollPane4;
    private DomainSortableTable digitalObjectsTable;
    private JTabbedPane tabbedPane;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public final void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		Subjects subjectModel = (Subjects) super.getModel();
		resourcesTable.updateCollection(subjectModel.getResources());
		accessionsTable.updateCollection(subjectModel.getAccessions());
        digitalObjectsTable.updateCollection(subjectModel.getDigitalObjects());

        setPluginModel(); // update any plugins with this new domain object
	}

	public Component getInitialFocusComponent() {
		return subjectTerm;
	}

	private void initAccess() {
		if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_ADVANCED_DATA_ENTRY)) {
			setFormToReadOnly();
			convertComboBoxToNonEnterableTextField(subjectTermType, Subjects.PROPERTYNAME_SUBJECT_TERM_TYPE);
			convertComboBoxToNonEnterableTextField(subjectSource, Subjects.PROPERTYNAME_SUBJECT_SOURCE);
		}
	}

    /**
     * Method that initializes any embedded plugins that would add an editor
     */
    private void initPlugins() {
        plugins = ATPluginFactory.getInstance().getEmbeddedSubjectEditorPlugins();
        if(plugins != null) {
            for(ATPlugin plugin : plugins) {
                plugin.setEditorField(this);
                HashMap pluginPanels = plugin.getEmbeddedPanels();
                for(Object key : pluginPanels.keySet()) {
                    String panelName = (String)key;
                    JPanel pluginPanel = (JPanel)pluginPanels.get(key);

                    // put in a hack here that allows a single plugin to remove
                    // all other components in the JPanel. This also means
                    // that plugin support will have to be added New Panel
                    if(panelName.indexOf("::") == -1) { // add it to end
                        tabbedPane.addTab(panelName, pluginPanel);
                    } else if (panelName.indexOf("::main") != -1){
                        removeAll(); // remove all components
                        setLayout(new FlowLayout());
                        add(pluginPanel);
                        return; // don't want to add any more plugins here
                    }
                }
            }
        }
    }

    /**
     * Method set the model for any plugins loaded
     */
    private void setPluginModel() {
        if(plugins != null) {
            for(ATPlugin plugin : plugins) {
                plugin.setModel(getModel(), null);
            }
        }
    }
}
