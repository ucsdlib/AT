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
 * Created by JFormDesigner on Fri Oct 07 14:45:18 EDT 2005
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collections;
import java.util.Date;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.mydomain.DatabaseTablesDAO;
import org.archiviststoolkit.structure.DatabaseFields;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.hibernate.ATSearchCriterion;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.MatchMode;

public class QueryEditor extends JDialog {

	private static final String SELECT_A_FIELD = "Select a field";

	public QueryEditor(Frame owner, Class clazz) {
		super(owner);
		this.setClazz(clazz);
		initComponents();
		init();
	}

	public QueryEditor(Dialog owner, Class clazz) {
		super(owner);
		this.setClazz(clazz);
		initComponents();
		init();
	}

	private void init() {
		StandardEditor.setMainHeaderColorAndTextByClass(this.clazz, this.mainHeaderPanel, this.mainHeaderLabel);
		createPopupValues();
		this.getRootPane().setDefaultButton(queryButton);
		if (clazz == Subjects.class || clazz == Names.class || clazz == Assessments.class) {
            tabbedPane1.remove(1);
		    includeComponents.setVisible(false);
            setDigitalObjectGUIComponentsVisible(false);
		} else if (clazz == Accessions.class) {
			resourceOnlyPanel.setVisible(false);
			includeComponents.setVisible(false);
			identifierLabel.setText("Find by " + ATFieldInfo.getLabel(Resources.class, Resources.PROPERTYNAME_RESOURCE_IDENTIFIER).toLowerCase());
            setDigitalObjectGUIComponentsVisible(false);
		} else if (clazz == Resources.class) {
			identifierLabel.setText("Find by " + ATFieldInfo.getLabel(Accessions.class, Accessions.PROPERTYNAME_ACCESSION_NUMBER).toLowerCase());
            setDigitalObjectGUIComponentsVisible(false);
		} else if (clazz == DigitalObjects.class) {
            panel3.setVisible(false);
            panel4.setVisible(false);
            label6.setVisible(false);
            location.setVisible(false);
            lookupLocation.setVisible(false);
            clearLocation.setVisible(false);
            label7.setVisible(false);
            instanceType.setVisible(false);
            //includeComponents.setVisible(false);
            //includeComponentsRelatedSearch.setVisible(false);
            setDigitalObjectGUIComponentsVisible(true);
        }
		setSearchEditorLabel();
	}

    /**
     * Method to show or hide the GUI components associated with digital objects
     *
     * @param visible If true then components are set visible if false then they are hidden
     */
    private void setDigitalObjectGUIComponentsVisible(boolean visible) {
        resourceLabel.setVisible(visible);
        resource.setVisible(visible);
        lookupResource.setVisible(visible);
        clearResource.setVisible(visible);
        fileLabel.setVisible(visible);
        fileURI.setVisible(visible);
    }

	private void setSearchEditorLabel() {
		String table = "";
		if (clazz == Accessions.class) {
			table = "accessions";
		} else if (clazz == Names.class) {
			table = "names";
		} else if (clazz == Resources.class) {
			table = "resources";
		} else if (clazz == Subjects.class) {
			table = "subjects";
		} else if (clazz == Assessments.class) {
			table = "assessments";
		} else if (clazz == DigitalObjects.class) {
            table = "digital objects";
        }

		searchEditorLabel.setText("Search " + table + ":");
	}

	private void thisWindowClosing() {
		status = javax.swing.JOptionPane.CANCEL_OPTION;
		this.setVisible(false);
	}

	private void lookupNameActionPerformed() {
		NameAuthorityLookup nameLookupDialog = new NameAuthorityLookup(this);
		nameLookupDialog.setMainHeaderByClass(clazz);
		if (nameLookupDialog.showDialog() == JOptionPane.OK_OPTION) {
			selectedName = nameLookupDialog.getSelectedName();
			sortName.setText(selectedName.getSortName());
		}
	}

	private void queryButton2ActionPerformed(ActionEvent e) {
		if (selectedName == null
				&& selectedSubject == null
				&& selectedLocation == null
                && selectedResource == null
                && fileURI.getText().length() == 0
				&& noteText.getText().length() == 0
				&& (clazz == Resources.class || clazz == Accessions.class || clazz == DigitalObjects.class)
				&& selectedInstanceType.length() == 0
				&& !areDeaccessionDatesFilledOut()
				&& !areBucketsFilledOut()) {
			JOptionPane.showMessageDialog(this, "You must enter information to search on first");
		} else {
			status = javax.swing.JOptionPane.OK_OPTION;
			alternateQuery = true;
			this.setVisible(false);
		}
	}

	private void lookupSubjectActionPerformed() {
		SubjectTermLookup subjectTermLookupDialog = new SubjectTermLookup(this);
		subjectTermLookupDialog.setMainHeaderByClass(clazz);
		if (subjectTermLookupDialog.showDialog() == JOptionPane.OK_OPTION) {
			selectedSubject = subjectTermLookupDialog.getSelectedSubject();
			subject.setText(selectedSubject.getSubjectTerm());
		}
	}

	private void lookupLocationActionPerformed() {
		LocationAssignmentAccessions locationLookupDialog = new LocationAssignmentAccessions(this);
		locationLookupDialog.setMainHeaderByClass(clazz);
		if (locationLookupDialog.showDialog() == JOptionPane.OK_OPTION) {
			selectedLocation = locationLookupDialog.getSelectedLocation();
			location.setText(selectedLocation.toString());
		}
	}

    private void lookupResourceActionPerformed() {
        ResourceLookup resourceLookupDialog = new ResourceLookup(this, ResourceLookup.PARENT_EDITOR_TYPE_DIGITALOBJECT);
		if (resourceLookupDialog.showDialog(this) == JOptionPane.OK_OPTION) {
			selectedResource = resourceLookupDialog.getSelectedResource();
			resource.setText(selectedResource.getTitle());
		}
    }

	private void clearNameActionPerformed() {
		selectedName = null;
		sortName.setText("");
	}

	private void clearSubjectActionPerformed() {
		selectedSubject = null;
		subject.setText("");
	}

	private void clearLocationActionPerformed() {
		selectedLocation = null;
		location.setText("");
	}

    private void clearResourceActionPerformed() {
		selectedResource = null;
		resource.setText("");
	}

	private void instanceTypeActionPerformed() {
		selectedInstanceType = (String) instanceType.getSelectedItem();
	}

	private void deaccessionDateEndFocusGained() {
		if (deaccessionDateEnd.getValue() == null) {
			deaccessionDateEnd.setValue(deaccessionDateStart.getValue());
			/* After a formatted text field gains focus, it replaces its text with its
			 * current value, formatted appropriately of course. It does this _after_
			 * any focus listeners are notified. So, if we are editable, we queue
			 * up a selectAll to be done after the current events in the thread are done. */
			Runnable doSelect = new Runnable() {
				public void run() {
					deaccessionDateEnd.selectAll();
				}
			};
			SwingUtilities.invokeLater(doSelect);
		}
	}

	public JTextField getNoteText() {
		return noteText;
	}

	private void includeComponentsActionPerformed() {
		checkIncludeComponentViablility(getChosenField1());
		checkIncludeComponentViablility(getChosenField2());
	}

	public JTextField getBucket1() {
		return bucket1;
	}

	public JTextField getBucket2() {
		return bucket2;
	}

	public JTextField getBucket3() {
		return bucket3;
	}

	public JTextField getBucket4() {
		return bucket4;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        HeaderPanel = new JPanel();
        mainHeaderPanel = new JPanel();
        mainHeaderLabel = new JLabel();
        subHeaderPanel = new JPanel();
        subHeaderLabel = new JLabel();
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        searchEditorLabel = new JLabel();
        contentPane = new JPanel();
        fieldSelector1 = new JComboBox();
        placeHolder1 = new QueryEditorTextPanel();
        boolean1 = new JComboBox();
        fieldSelector2 = new JComboBox();
        placeHolder2 = new QueryEditorTextPanel();
        includeComponents = new JCheckBox();
        buttonBar2 = new JPanel();
        cancelButton2 = new JButton();
        queryButton = new JButton();
        classSpecificPanel = new JPanel();
        altQueryLabel = new JLabel();
        panel2 = new JPanel();
        label1 = new JLabel();
        sortName = new JTextField();
        lookupName = new JButton();
        clearName = new JButton();
        label2 = new JLabel();
        function = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(ArchDescriptionNames.class, ArchDescriptionNames.PROPERTYNAME_NAME_LINK_FUNCTION));
        label5 = new JLabel();
        subject = new JTextField();
        lookupSubject = new JButton();
        clearSubject = new JButton();
        label6 = new JLabel();
        location = new JTextField();
        lookupLocation = new JButton();
        clearLocation = new JButton();
        resourceLabel = new JLabel();
        resource = new JTextField();
        lookupResource = new JButton();
        clearResource = new JButton();
        fileLabel = new JLabel();
        fileURI = new JTextField();
        panel3 = new JPanel();
        label8 = new JLabel();
        deaccessionDateStart = ATBasicComponentFactory.createUnboundDateField();
        label3 = new JLabel();
        deaccessionDateEnd = ATBasicComponentFactory.createUnboundDateField();
        panel4 = new JPanel();
        identifierLabel = new JLabel();
        bucket1 = new JTextField();
        bucket2 = new JTextField();
        bucket3 = new JTextField();
        bucket4 = new JTextField();
        resourceOnlyPanel = new JPanel();
        label9 = new JLabel();
        noteText = new JTextField();
        label7 = new JLabel();
        instanceType = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(ArchDescriptionInstances.class, ArchDescriptionInstances.PROPERTYNAME_INSTANCE_TYPE));
        includeComponentsRelatedSearch = new JCheckBox();
        buttonBar = new JPanel();
        cancelButton = new JButton();
        queryButton2 = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing();
            }
        });
        Container contentPane2 = getContentPane();
        contentPane2.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(null);
            dialogPane.setBackground(new Color(200, 205, 232));
            dialogPane.setLayout(new BorderLayout());

            //======== HeaderPanel ========
            {
                HeaderPanel.setOpaque(false);
                HeaderPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                HeaderPanel.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        new ColumnSpec(Sizes.bounded(Sizes.MINIMUM, Sizes.dluX(100), Sizes.dluX(200))),
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    },
                    RowSpec.decodeSpecs("default")));

                //======== mainHeaderPanel ========
                {
                    mainHeaderPanel.setBackground(new Color(80, 69, 57));
                    mainHeaderPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    mainHeaderPanel.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.RELATED_GAP_COLSPEC,
                            new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        },
                        new RowSpec[] {
                            FormFactory.RELATED_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.RELATED_GAP_ROWSPEC
                        }));

                    //---- mainHeaderLabel ----
                    mainHeaderLabel.setText("Main Header");
                    mainHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
                    mainHeaderLabel.setForeground(Color.white);
                    mainHeaderPanel.add(mainHeaderLabel, cc.xy(2, 2));
                }
                HeaderPanel.add(mainHeaderPanel, cc.xy(1, 1));

                //======== subHeaderPanel ========
                {
                    subHeaderPanel.setBackground(new Color(66, 60, 111));
                    subHeaderPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    subHeaderPanel.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.RELATED_GAP_COLSPEC,
                            new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        },
                        new RowSpec[] {
                            FormFactory.RELATED_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.RELATED_GAP_ROWSPEC
                        }));

                    //---- subHeaderLabel ----
                    subHeaderLabel.setText("Search Editor");
                    subHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
                    subHeaderLabel.setForeground(Color.white);
                    subHeaderPanel.add(subHeaderLabel, cc.xy(2, 2));
                }
                HeaderPanel.add(subHeaderPanel, cc.xy(2, 1));
            }
            dialogPane.add(HeaderPanel, BorderLayout.NORTH);

            //======== tabbedPane1 ========
            {

                //======== panel1 ========
                {
                    panel1.setBorder(Borders.DIALOG_BORDER);
                    panel1.setOpaque(false);
                    panel1.setLayout(new FormLayout(
                        ColumnSpec.decodeSpecs("default:grow"),
                        new RowSpec[] {
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        }));

                    //---- searchEditorLabel ----
                    searchEditorLabel.setText("Search editor");
                    panel1.add(searchEditorLabel, cc.xy(1, 1));

                    //======== contentPane ========
                    {
                        contentPane.setBackground(new Color(231, 188, 251));
                        contentPane.setOpaque(false);
                        contentPane.setBorder(Borders.DIALOG_BORDER);
                        contentPane.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec("400px:grow"),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            },
                            new RowSpec[] {
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC,
                                FormFactory.LINE_GAP_ROWSPEC,
                                FormFactory.DEFAULT_ROWSPEC
                            }));

                        //---- fieldSelector1 ----
                        fieldSelector1.setOpaque(false);
                        fieldSelector1.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                fieldSelector1ActionPerformed(e);
                            }
                        });
                        contentPane.add(fieldSelector1, cc.xy(1, 1));

                        //---- placeHolder1 ----
                        placeHolder1.setOpaque(false);
                        contentPane.add(placeHolder1, cc.xy(3, 1));

                        //---- boolean1 ----
                        boolean1.setModel(new DefaultComboBoxModel(new String[] {
                            "and",
                            "or"
                        }));
                        boolean1.setOpaque(false);
                        contentPane.add(boolean1, cc.xy(5, 1));

                        //---- fieldSelector2 ----
                        fieldSelector2.setOpaque(false);
                        fieldSelector2.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                fieldSelector2ActionPerformed(e);
                            }
                        });
                        contentPane.add(fieldSelector2, cc.xy(1, 2));

                        //---- placeHolder2 ----
                        placeHolder2.setOpaque(false);
                        contentPane.add(placeHolder2, cc.xy(3, 2));

                        //---- includeComponents ----
                        includeComponents.setText("Include components in search");
                        includeComponents.setOpaque(false);
                        includeComponents.setSelected(true);
                        includeComponents.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                includeComponentsActionPerformed();
                            }
                        });
                        contentPane.add(includeComponents, cc.xywh(1, 4, 5, 1));

                        //======== buttonBar2 ========
                        {
                            buttonBar2.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                            buttonBar2.setBackground(new Color(231, 188, 251));
                            buttonBar2.setOpaque(false);
                            buttonBar2.setLayout(new FormLayout(
                                new ColumnSpec[] {
                                    FormFactory.GLUE_COLSPEC,
                                    FormFactory.BUTTON_COLSPEC,
                                    FormFactory.RELATED_GAP_COLSPEC,
                                    FormFactory.BUTTON_COLSPEC
                                },
                                RowSpec.decodeSpecs("pref")));

                            //---- cancelButton2 ----
                            cancelButton2.setText("Cancel");
                            cancelButton2.setOpaque(false);
                            cancelButton2.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    cancelButtonActionPerformed(e);
                                }
                            });
                            buttonBar2.add(cancelButton2, cc.xy(2, 1));

                            //---- queryButton ----
                            queryButton.setText("Search");
                            queryButton.setOpaque(false);
                            queryButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    queryButtonActionPerformed(e);
                                }
                            });
                            buttonBar2.add(queryButton, cc.xy(4, 1));
                        }
                        contentPane.add(buttonBar2, cc.xywh(1, 6, 5, 1));
                    }
                    panel1.add(contentPane, cc.xy(1, 3));
                }
                tabbedPane1.addTab("Search", panel1);


                //======== classSpecificPanel ========
                {
                    classSpecificPanel.setOpaque(false);
                    classSpecificPanel.setBorder(Borders.DIALOG_BORDER);
                    classSpecificPanel.setLayout(new FormLayout(
                        ColumnSpec.decodeSpecs("default:grow"),
                        new RowSpec[] {
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC
                        }));

                    //---- altQueryLabel ----
                    altQueryLabel.setText("Search by linked record:");
                    classSpecificPanel.add(altQueryLabel, cc.xy(1, 1));

                    //======== panel2 ========
                    {
                        panel2.setOpaque(false);
                        panel2.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC
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
                                FormFactory.DEFAULT_ROWSPEC
                            }));

                        //---- label1 ----
                        label1.setText("Find by name");
                        panel2.add(label1, cc.xy(1, 1));

                        //---- sortName ----
                        sortName.setEditable(false);
                        sortName.setOpaque(false);
                        panel2.add(sortName, cc.xy(3, 1));

                        //---- lookupName ----
                        lookupName.setText("Lookup");
                        lookupName.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
                        lookupName.setOpaque(false);
                        lookupName.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                lookupNameActionPerformed();
                            }
                        });
                        panel2.add(lookupName, cc.xy(5, 1));

                        //---- clearName ----
                        clearName.setText("Clear");
                        clearName.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
                        clearName.setOpaque(false);
                        clearName.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                clearNameActionPerformed();
                            }
                        });
                        panel2.add(clearName, cc.xy(7, 1));

                        //---- label2 ----
                        label2.setText("Function");
                        panel2.add(label2, cc.xy(9, 1));

                        //---- function ----
                        function.setOpaque(false);
                        panel2.add(function, cc.xywh(11, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                        //---- label5 ----
                        label5.setText("Find by subject");
                        panel2.add(label5, cc.xy(1, 3));

                        //---- subject ----
                        subject.setEditable(false);
                        subject.setOpaque(false);
                        panel2.add(subject, cc.xy(3, 3));

                        //---- lookupSubject ----
                        lookupSubject.setText("Lookup");
                        lookupSubject.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
                        lookupSubject.setOpaque(false);
                        lookupSubject.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                lookupSubjectActionPerformed();
                            }
                        });
                        panel2.add(lookupSubject, cc.xy(5, 3));

                        //---- clearSubject ----
                        clearSubject.setText("Clear");
                        clearSubject.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
                        clearSubject.setOpaque(false);
                        clearSubject.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                clearSubjectActionPerformed();
                            }
                        });
                        panel2.add(clearSubject, cc.xy(7, 3));

                        //---- label6 ----
                        label6.setText("Find by location");
                        panel2.add(label6, cc.xy(1, 5));

                        //---- location ----
                        location.setEditable(false);
                        location.setOpaque(false);
                        panel2.add(location, cc.xy(3, 5));

                        //---- lookupLocation ----
                        lookupLocation.setText("Lookup");
                        lookupLocation.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
                        lookupLocation.setOpaque(false);
                        lookupLocation.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                lookupLocationActionPerformed();
                            }
                        });
                        panel2.add(lookupLocation, cc.xy(5, 5));

                        //---- clearLocation ----
                        clearLocation.setText("Clear");
                        clearLocation.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
                        clearLocation.setOpaque(false);
                        clearLocation.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                clearLocationActionPerformed();
                            }
                        });
                        panel2.add(clearLocation, cc.xy(7, 5));

                        //---- resourceLabel ----
                        resourceLabel.setText("Find by resource");
                        panel2.add(resourceLabel, cc.xy(1, 7));

                        //---- resource ----
                        resource.setEditable(false);
                        resource.setOpaque(false);
                        panel2.add(resource, cc.xy(3, 7));

                        //---- lookupResource ----
                        lookupResource.setText("Lookup");
                        lookupResource.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
                        lookupResource.setOpaque(false);
                        lookupResource.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                lookupResourceActionPerformed();
                            }
                        });
                        panel2.add(lookupResource, cc.xy(5, 7));

                        //---- clearResource ----
                        clearResource.setText("Clear");
                        clearResource.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
                        clearResource.setOpaque(false);
                        clearResource.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                clearResourceActionPerformed();
                            }
                        });
                        panel2.add(clearResource, cc.xy(7, 7));

                        //---- fileLabel ----
                        fileLabel.setText("Find by file URI");
                        panel2.add(fileLabel, cc.xy(1, 9));
                        panel2.add(fileURI, cc.xywh(3, 9, 5, 1));

                        //======== panel3 ========
                        {
                            panel3.setOpaque(false);
                            panel3.setLayout(new FormLayout(
                                new ColumnSpec[] {
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    FormFactory.DEFAULT_COLSPEC
                                },
                                RowSpec.decodeSpecs("default")));

                            //---- label8 ----
                            label8.setText("Find by deaccession date");
                            panel3.add(label8, cc.xy(1, 1));

                            //---- deaccessionDateStart ----
                            deaccessionDateStart.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            deaccessionDateStart.setColumns(12);
                            panel3.add(deaccessionDateStart, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                            //---- label3 ----
                            label3.setText("-");
                            panel3.add(label3, cc.xy(5, 1));

                            //---- deaccessionDateEnd ----
                            deaccessionDateEnd.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                            deaccessionDateEnd.setColumns(12);
                            deaccessionDateEnd.addFocusListener(new FocusAdapter() {
                                @Override
                                public void focusGained(FocusEvent e) {
                                    deaccessionDateEndFocusGained();
                                }
                            });
                            panel3.add(deaccessionDateEnd, cc.xywh(7, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
                        }
                        panel2.add(panel3, cc.xywh(1, 13, 11, 1));

                        //======== panel4 ========
                        {
                            panel4.setOpaque(false);
                            panel4.setLayout(new FormLayout(
                                new ColumnSpec[] {
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                                },
                                RowSpec.decodeSpecs("default")));
                            ((FormLayout)panel4.getLayout()).setColumnGroups(new int[][] {{3, 5, 7, 9}});

                            //---- identifierLabel ----
                            identifierLabel.setText("identifier");
                            panel4.add(identifierLabel, cc.xy(1, 1));
                            panel4.add(bucket1, cc.xy(3, 1));
                            panel4.add(bucket2, cc.xy(5, 1));
                            panel4.add(bucket3, cc.xy(7, 1));
                            panel4.add(bucket4, cc.xy(9, 1));
                        }
                        panel2.add(panel4, cc.xywh(1, 15, 11, 1));

                        //======== resourceOnlyPanel ========
                        {
                            resourceOnlyPanel.setOpaque(false);
                            resourceOnlyPanel.setLayout(new FormLayout(
                                new ColumnSpec[] {
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    FormFactory.DEFAULT_COLSPEC,
                                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                    FormFactory.DEFAULT_COLSPEC
                                },
                                new RowSpec[] {
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.LINE_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC
                                }));

                            //---- label9 ----
                            label9.setText("Find by note text");
                            resourceOnlyPanel.add(label9, cc.xy(1, 1));
                            resourceOnlyPanel.add(noteText, cc.xy(3, 1));

                            //---- label7 ----
                            label7.setText("Find by instance type");
                            resourceOnlyPanel.add(label7, cc.xy(1, 2));

                            //---- instanceType ----
                            instanceType.setOpaque(false);
                            instanceType.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    instanceTypeActionPerformed();
                                }
                            });
                            resourceOnlyPanel.add(instanceType, cc.xywh(3, 2, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                            //---- includeComponentsRelatedSearch ----
                            includeComponentsRelatedSearch.setText("Include components in search");
                            includeComponentsRelatedSearch.setOpaque(false);
                            includeComponentsRelatedSearch.setSelected(true);
                            resourceOnlyPanel.add(includeComponentsRelatedSearch, cc.xywh(1, 4, 11, 1));
                        }
                        panel2.add(resourceOnlyPanel, cc.xywh(1, 17, 11, 1));
                    }
                    classSpecificPanel.add(panel2, cc.xy(1, 3));

                    //======== buttonBar ========
                    {
                        buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                        buttonBar.setBackground(new Color(231, 188, 251));
                        buttonBar.setOpaque(false);
                        buttonBar.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.GLUE_COLSPEC,
                                FormFactory.BUTTON_COLSPEC,
                                FormFactory.RELATED_GAP_COLSPEC,
                                FormFactory.BUTTON_COLSPEC
                            },
                            RowSpec.decodeSpecs("pref")));

                        //---- cancelButton ----
                        cancelButton.setText("Cancel");
                        cancelButton.setOpaque(false);
                        cancelButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                cancelButtonActionPerformed(e);
                            }
                        });
                        buttonBar.add(cancelButton, cc.xy(2, 1));

                        //---- queryButton2 ----
                        queryButton2.setText("Search");
                        queryButton2.setOpaque(false);
                        queryButton2.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                queryButton2ActionPerformed(e);
                            }
                        });
                        buttonBar.add(queryButton2, cc.xy(4, 1));
                    }
                    classSpecificPanel.add(buttonBar, cc.xy(1, 5));
                }
                tabbedPane1.addTab("Search By Linked Record", classSpecificPanel);

            }
            dialogPane.add(tabbedPane1, BorderLayout.SOUTH);
        }
        contentPane2.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		status = javax.swing.JOptionPane.CANCEL_OPTION;
		this.setVisible(false);
	}

	private void queryButtonActionPerformed(ActionEvent e) {
		//make sure there is information filled out
		if (fieldSelector1.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(this, "You must select a field first.");

		} else if (!currentValue1Component.validDataEntered()) {
			JOptionPane.showMessageDialog(this, "You must enter information to search on first.");

		} else if (fieldSelector2.getSelectedIndex() != 0 && !currentValue2Component.validDataEntered()) {
			JOptionPane.showMessageDialog(this, "You must enter information to search on first.");

		} else {
			status = javax.swing.JOptionPane.OK_OPTION;
			alternateQuery = false;
			this.setVisible(false);
		}
	}


	private void fieldSelector1ActionPerformed(ActionEvent e) {
		if (fieldSelector1.getSelectedIndex() == 0) {
			contentPane.remove(currentValue1Component);
			currentValue1Component = placeHolder1;
			contentPane.add(currentValue1Component, value1CellConstraints);
		} else {
			QueryField queryField = (QueryField) fieldSelector1.getSelectedItem();
			contentPane.remove(currentValue1Component);
			currentValue1Component = queryField.getValueComponent();
			contentPane.add(currentValue1Component, value1CellConstraints);
			currentValue1Component.requestInitialFocus();
			checkIncludeComponentViablility(getChosenField1());
		}
		this.invalidate();
		this.validate();
		this.repaint();
	}

	private void fieldSelector2ActionPerformed(ActionEvent e) {
		if (fieldSelector2.getSelectedIndex() == 0) {
			contentPane.remove(currentValue2Component);
			currentValue2Component = placeHolder2;
			contentPane.add(currentValue2Component, value2CellConstraints);
		} else {
			QueryField queryField = (QueryField) fieldSelector2.getSelectedItem();
			contentPane.remove(currentValue2Component);
			currentValue2Component = queryField.getValueComponent();
			contentPane.add(currentValue2Component, value2CellConstraints);
			currentValue2Component.requestInitialFocus();
			checkIncludeComponentViablility(getChosenField2());
		}
		this.invalidate();
		this.validate();
		this.repaint();
	}

	private void checkIncludeComponentViablility(String fieldName) {
		//when include component is checked we need to make sure that the selected fields
		//exist in components as well as resources
		if (getClazz() == Resources.class && getIncludeComponents()) {
			if (fieldName.length() > 0) {
				if (ATFieldInfo.getFieldInfo(ResourcesComponents.class, fieldName) == null) {
					JOptionPane.showMessageDialog(this, "Including components only works for fields that occur in both Resources and Components");
					includeComponents.setSelected(false);
				}
			}
		}
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel HeaderPanel;
    private JPanel mainHeaderPanel;
    private JLabel mainHeaderLabel;
    private JPanel subHeaderPanel;
    private JLabel subHeaderLabel;
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JLabel searchEditorLabel;
    private JPanel contentPane;
    private JComboBox fieldSelector1;
    private QueryEditorTextPanel placeHolder1;
    private JComboBox boolean1;
    private JComboBox fieldSelector2;
    private QueryEditorTextPanel placeHolder2;
    private JCheckBox includeComponents;
    private JPanel buttonBar2;
    private JButton cancelButton2;
    private JButton queryButton;
    private JPanel classSpecificPanel;
    private JLabel altQueryLabel;
    private JPanel panel2;
    private JLabel label1;
    private JTextField sortName;
    private JButton lookupName;
    private JButton clearName;
    private JLabel label2;
    private JComboBox function;
    private JLabel label5;
    private JTextField subject;
    private JButton lookupSubject;
    private JButton clearSubject;
    private JLabel label6;
    private JTextField location;
    private JButton lookupLocation;
    private JButton clearLocation;
    private JLabel resourceLabel;
    private JTextField resource;
    private JButton lookupResource;
    private JButton clearResource;
    private JLabel fileLabel;
    private JTextField fileURI;
    private JPanel panel3;
    private JLabel label8;
    private JFormattedTextField deaccessionDateStart;
    private JLabel label3;
    private JFormattedTextField deaccessionDateEnd;
    private JPanel panel4;
    private JLabel identifierLabel;
    private JTextField bucket1;
    private JTextField bucket2;
    private JTextField bucket3;
    private JTextField bucket4;
    private JPanel resourceOnlyPanel;
    private JLabel label9;
    private JTextField noteText;
    private JLabel label7;
    private JComboBox instanceType;
    private JCheckBox includeComponentsRelatedSearch;
    private JPanel buttonBar;
    private JButton cancelButton;
    private JButton queryButton2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	/**
	 * The status of the editor.
	 */
	protected int status = 0;

//    private ArrayList fieldValues;

	private QueryEditorPanel currentValue1Component;
	private CellConstraints value1CellConstraints;

	private QueryEditorPanel currentValue2Component;
	private CellConstraints value2CellConstraints;

	private Class clazz;

	private Names selectedName = null;
	private Subjects selectedSubject = null;
	private Locations selectedLocation = null;
    private Resources selectedResource = null;
	private String selectedInstanceType = "";
	private Date deaccessionStartDate = null;
	private Date deaccessionEndDate = null;

	private Boolean alternateQuery = false;

	public JComboBox getFieldSelector1() {
		return fieldSelector1;
	}

	public void setFieldSelector1(JComboBox fieldSelector) {
		this.fieldSelector1 = fieldSelector;
	}

	/**
	 * Displays the dialog box representing the editor.
	 *
	 * @return true if it displayed okay
	 */

	public final int showDialog() {

		this.pack();

		setLocationRelativeTo(null);
		this.setVisible(true);

		return (status);
	}

	public String getChosenField1() {
		Object choice = fieldSelector1.getSelectedItem();
		if (choice instanceof QueryField) {
			return ((QueryField) choice).getFieldName();
		} else {
			return "";
		}
	}

	public String getChosenField2() {
		Object choice = fieldSelector2.getSelectedItem();
		if (choice instanceof QueryField) {
			return ((QueryField) choice).getFieldName();
		} else {
			return "";
		}
	}

	public String getChosenBoolean1() {
		return (String) boolean1.getSelectedItem();
	}

//	public String getSortField() {
//		Object choice = sortFieldSelector.getSelectedItem();
//		if (choice instanceof QueryField) {
//			return ((QueryField) choice).getFieldName();
//		} else {
//			return "";
//		}
//	}

	public void setFieldValues(ArrayList fieldValues1, ArrayList fieldValues2, ArrayList sortFieldValues) {
//        this.fieldValues1 = fieldValues;
		fieldValues1.add(0, SELECT_A_FIELD);
		fieldValues2.add(0, SELECT_A_FIELD);
		fieldSelector1.setModel(new DefaultComboBoxModel(fieldValues1.toArray()));
		fieldSelector2.setModel(new DefaultComboBoxModel(fieldValues2.toArray()));
//		sortFieldSelector.setModel(new DefaultComboBoxModel(sortFieldValues.toArray()));
	}

	private void createPopupValues() {

		ArrayList<QueryField> fieldValues1 = new ArrayList<QueryField>();
		ArrayList<QueryField> fieldValues2 = new ArrayList<QueryField>();
		ArrayList<QueryField> sortFieldValues = new ArrayList<QueryField>();

		Set<DatabaseFields> fields = new DatabaseTablesDAO().getFieldList(this.getClazz().getSimpleName());
		for (DatabaseFields field : fields) {
			if (field.getIncludeInSearchEditor()) {
				fieldValues1.add(new QueryField(field, getClazz()));
				fieldValues2.add(new QueryField(field, getClazz()));
			}
//			if (field.getAllowSort()) {
//				sortFieldValues.add(new QueryField(field.getFieldName(), getClazz()));
//			}
		}
		Collections.sort(fieldValues1);
		Collections.sort(fieldValues2);
		Collections.sort(sortFieldValues);
		setFieldValues(fieldValues1, fieldValues2, sortFieldValues);

		FormLayout formLayout = (FormLayout) contentPane.getLayout();
		placeHolder1.disableValueField();
		currentValue1Component = placeHolder1;
		value1CellConstraints = formLayout.getConstraints(placeHolder1);
		placeHolder2.disableValueField();
		currentValue2Component = placeHolder2;
		value2CellConstraints = formLayout.getConstraints(placeHolder2);
	}

	public ATSearchCriterion getCriterion1() {
		return this.currentValue1Component.getQueryCriterion(clazz, this.getChosenField1());
	}

	public ATSearchCriterion getCriterion2() {
		return this.currentValue2Component.getQueryCriterion(clazz, this.getChosenField2());
	}

	public ArrayList<CriteriaRelationshipPairs> getAltFormCriteria() {
		ArrayList<CriteriaRelationshipPairs> criterionList = new ArrayList<CriteriaRelationshipPairs>();
		CriteriaRelationshipPairs pair;
		String humanReadableSearchString;

		if (getSelectedName() != null) {
			pair = new CriteriaRelationshipPairs(Restrictions.eq(ArchDescriptionNames.PROPERTYNAME_NAME, getSelectedName()), ArchDescription.PROPERTYNAME_NAMES);
			humanReadableSearchString = ArchDescription.PROPERTYNAME_NAMES + " equals " + getSelectedName();
			String selectedFunction = (String) function.getSelectedItem();
			if (selectedFunction.length() > 0) {
				pair.addCriteria(Restrictions.eq(ArchDescriptionNames.PROPERTYNAME_NAME_LINK_FUNCTION, selectedFunction));
				humanReadableSearchString += " and " + ArchDescriptionNames.PROPERTYNAME_NAME_LINK_FUNCTION + " equals " + selectedFunction;
			}
			pair.setHumanReadableSearchString(humanReadableSearchString);
			pair.setContext("Names");
			criterionList.add(pair);
		}

		if (selectedSubject != null) {
			criterionList.add(new CriteriaRelationshipPairs(Restrictions.eq(ArchDescriptionSubjects.PROPERTYNAME_SUBJECT, selectedSubject),
					ArchDescription.PROPERTYNAME_SUBJECTS, ArchDescription.PROPERTYNAME_SUBJECTS + " equals " + selectedSubject));
		}

		if (noteText != null && noteText.getText().length() > 0) {
			criterionList.add(new CriteriaRelationshipPairs(Restrictions.like(ArchDescriptionNotes.PROPERTYNAME_ARCH_DESCRIPTION_NOTE_CONTENT, noteText.getText(), MatchMode.ANYWHERE),
					ArchDescription.PROPERTYNAME_REPEATING_DATA,
					ArchDescriptionNotes.PROPERTYNAME_ARCH_DESCRIPTION_NOTE_CONTENT + " contains " + noteText.getText(),
					"Note text"));
		}

		if (areDeaccessionDatesFilledOut()) {
			pair = new CriteriaRelationshipPairs(Restrictions.ge(Deaccessions.PROPERTYNAME_DATE, deaccessionStartDate), AccessionsResourcesCommon.PROPERTYNAME_DEACCESSIONS);
			pair.addCriteria(Restrictions.le(Deaccessions.PROPERTYNAME_DATE, deaccessionEndDate));
			pair.setResourceOnly(true);
			pair.setHumanReadableSearchString(Deaccessions.PROPERTYNAME_DATE + " is between " + deaccessionDateStart.getText() + " and " + deaccessionDateEnd.getText());
			criterionList.add(pair);
		}

		if (areBucketsFilledOut()) {
			if (getBucket1().getText().length() > 0) {
				if(clazz == Accessions.class) {
					createAndAddBucketPair(criterionList, Accessions.PROPERTYNAME_RESOURCES, AccessionsResources.PROPERTYNAME_RESOURCE,
							Resources.PROPERTYNAME_RESOURCE_IDENTIFIER_1, getBucket1().getText(), Resources.class);
				} else if (clazz == Resources.class) {
					createAndAddBucketPair(criterionList, Resources.PROPERTYNAME_ACCESSIONS, AccessionsResources.PROPERTYNAME_ACCESSION,
							Accessions.PROPERTYNAME_ACCESSION_NUMBER_1, getBucket1().getText(), Accessions.class, true);
				}
			}

			if (getBucket2().getText().length() > 0) {
				if(clazz == Accessions.class) {
					createAndAddBucketPair(criterionList, Accessions.PROPERTYNAME_RESOURCES, AccessionsResources.PROPERTYNAME_RESOURCE,
							Resources.PROPERTYNAME_RESOURCE_IDENTIFIER_2, getBucket2().getText(), Resources.class);
				} else if (clazz == Resources.class) {
					createAndAddBucketPair(criterionList, Resources.PROPERTYNAME_ACCESSIONS, AccessionsResources.PROPERTYNAME_ACCESSION,
							Accessions.PROPERTYNAME_ACCESSION_NUMBER_2, getBucket2().getText(), Accessions.class, true);
				}
			}

			if (getBucket3().getText().length() > 0) {
				if(clazz == Accessions.class) {
					createAndAddBucketPair(criterionList, Accessions.PROPERTYNAME_RESOURCES, AccessionsResources.PROPERTYNAME_RESOURCE,
							Resources.PROPERTYNAME_RESOURCE_IDENTIFIER_3, getBucket3().getText(), Resources.class);
				} else if (clazz == Resources.class) {
					createAndAddBucketPair(criterionList, Resources.PROPERTYNAME_ACCESSIONS, AccessionsResources.PROPERTYNAME_ACCESSION,
							Accessions.PROPERTYNAME_ACCESSION_NUMBER_3, getBucket3().getText(), Accessions.class, true);
				}
			}

			if (getBucket4().getText().length() > 0) {
				if(clazz == Accessions.class) {
					createAndAddBucketPair(criterionList, Accessions.PROPERTYNAME_RESOURCES, AccessionsResources.PROPERTYNAME_RESOURCE,
							Resources.PROPERTYNAME_RESOURCE_IDENTIFIER_4, getBucket4().getText(), Resources.class);
				} else if (clazz == Resources.class) {
					createAndAddBucketPair(criterionList, Resources.PROPERTYNAME_ACCESSIONS, AccessionsResources.PROPERTYNAME_ACCESSION,
							Accessions.PROPERTYNAME_ACCESSION_NUMBER_4, getBucket4().getText(), Accessions.class, true);
				}
			}
		}

		if (selectedLocation != null) {
			if (clazz == Accessions.class) {
				criterionList.add(new CriteriaRelationshipPairs(Restrictions.eq(AccessionsLocations.PROPERTYNAME_LOCATION, selectedLocation),
						Accessions.PROPERTYNAME_LOCATIONS, AccessionsLocations.PROPERTYNAME_LOCATION + " equals " + selectedLocation, "Locations"));
			} else if (clazz == Resources.class) {
				criterionList.add(new CriteriaRelationshipPairs(Restrictions.eq(ArchDescriptionAnalogInstances.PROPERTYNAME_LOCATION, selectedLocation),
						ResourcesCommon.PROPERTYNAME_INSTANCES, ArchDescriptionAnalogInstances.PROPERTYNAME_LOCATION + " equals " + selectedLocation, "Locations"));
			}
		}

        // Search by resource records. Used only when searching through digital objects
        if (selectedResource != null) {
            criterionList.add(new CriteriaRelationshipPairs(Restrictions.eq(ArchDescriptionDigitalInstances.PROPERTYNAME_PARENT_RESOURCE, selectedResource),
						DigitalObjects.PROPERTYNAME_DIGITAL_INSTANCE,ArchDescriptionDigitalInstances.PROPERTYNAME_RESOURCE + " equals " + selectedResource, "Resources"));
        }

        // Search by file URI. Used only when searching through digital objects
        if (fileURI != null && fileURI.getText().length() > 0) {
			criterionList.add(new CriteriaRelationshipPairs(Restrictions.like(FileVersions.PROPERTYNAME_FILE_VERSIONS_URI, fileURI.getText(), MatchMode.ANYWHERE),
					DigitalObjects.PROPERTYNAME_FILE_VERSIONS,
					FileVersions.PROPERTYNAME_FILE_VERSIONS_URI + " contains " + fileURI.getText(),
					"File URI"));
		}

		if (clazz == Resources.class && selectedInstanceType.length() > 0) {
			criterionList.add(new CriteriaRelationshipPairs(Restrictions.eq(ArchDescriptionInstances.PROPERTYNAME_INSTANCE_TYPE, selectedInstanceType),
					ResourcesCommon.PROPERTYNAME_INSTANCES, ArchDescriptionInstances.PROPERTYNAME_INSTANCE_TYPE + " equals " + selectedInstanceType));
		}

		return criterionList;
	}

	private void createAndAddBucketPair(ArrayList<CriteriaRelationshipPairs> criterionList,
										String firstPropertyName,
										String secondPropertyName,
										String targetProperty,
										String value,
										Class targetClass) {
		createAndAddBucketPair(criterionList, firstPropertyName, secondPropertyName, targetProperty, value, targetClass, false);
	}

	private void createAndAddBucketPair(ArrayList<CriteriaRelationshipPairs> criterionList,
										String firstPropertyName,
										String secondPropertyName,
										String targetProperty,
										String value,
										Class targetClass,
										boolean resourcesOnly) {
		CriteriaRelationshipPairs pair;
		pair = new CriteriaRelationshipPairs(Restrictions.ilike(targetProperty, value, MatchMode.START), firstPropertyName);
		pair.setSecondPropertyName(secondPropertyName);
		pair.setResourceOnly(resourcesOnly);
		pair.setHumanReadableSearchString(ATFieldInfo.getLabel(targetClass, targetProperty) + " begins with " + value);
		criterionList.add(pair);
	}

	private boolean areBucketsFilledOut() {
		return bucket1.getText().length() > 0 || bucket2.getText().length() > 0 || bucket3.getText().length() > 0 || bucket4.getText().length() > 0;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public Boolean getAlternateQuery() {
		return alternateQuery;
	}

	public void setAlternateQuery(Boolean alternateQuery) {
		this.alternateQuery = alternateQuery;
	}

	public Names getSelectedName() {
		return selectedName;
	}

	public void setSelectedName(Names selectedName) {
		this.selectedName = selectedName;
	}

	public Boolean getIncludeComponents() {
		return includeComponents.isSelected();
	}

	public Boolean getIncludeComponentsRelatedSearch() {
		return includeComponentsRelatedSearch.isSelected();
	}

	private boolean areDeaccessionDatesFilledOut() {
		deaccessionStartDate = (Date) deaccessionDateStart.getValue();
		deaccessionEndDate = (Date) deaccessionDateEnd.getValue();
		if (deaccessionStartDate == null && deaccessionEndDate == null) {
			return false;
		} else if (deaccessionStartDate == null) {
			deaccessionStartDate = deaccessionEndDate;
			return true;
		} else if (deaccessionEndDate == null) {
			deaccessionEndDate = deaccessionStartDate;
			return true;
		} else {
			return true;
		}
	}

    public class CriteriaRelationshipPairs {
		private String propertyName;
		private String secondPropertyName = null;
		private ArrayList<Criterion> criteriaList;
		private Boolean resourceOnly = false;
		private String humanReadableSearchString;
		private String context = null;

		protected CriteriaRelationshipPairs(Criterion criterion, String propertyName) {
			this.propertyName = propertyName;
			criteriaList = new ArrayList<Criterion>();
			criteriaList.add(criterion);
		}

		protected CriteriaRelationshipPairs(Criterion criterion, String propertyName, String humanReadableSearchString) {
			this.propertyName = propertyName;
			this.humanReadableSearchString = humanReadableSearchString;
			criteriaList = new ArrayList<Criterion>();
			criteriaList.add(criterion);
		}

		protected CriteriaRelationshipPairs(Criterion criterion, String propertyName, String humanReadableSearchString, String context) {
			this.propertyName = propertyName;
			this.humanReadableSearchString = humanReadableSearchString;
			this.context = context;
			criteriaList = new ArrayList<Criterion>();
			criteriaList.add(criterion);
		}

		public String getPropertyName() {
			return propertyName;
		}

		public void setPropertyName(String propertyName) {
			this.propertyName = propertyName;
		}

		public ArrayList<Criterion> getCriteriaList() {
			return criteriaList;
		}

		public void setCriteriaList(ArrayList<Criterion> criteriaList) {
			this.criteriaList = criteriaList;
		}

		public void addCriteria(Criterion criterion) {
			this.criteriaList.add(criterion);
		}

		public Boolean getResourceOnly() {
			return resourceOnly;
		}

		public void setResourceOnly(Boolean resourceOnly) {
			this.resourceOnly = resourceOnly;
		}

		public String getHumanReadableSearchString() {
			return humanReadableSearchString;
		}

		public void setHumanReadableSearchString(String humanReadableSearchString) {
			this.humanReadableSearchString = humanReadableSearchString;
		}

		public String getSecondPropertyName() {
			return secondPropertyName;
		}

		public void setSecondPropertyName(String secondPropertyName) {
			this.secondPropertyName = secondPropertyName;
		}

		public String getContext() {
			String fieldLabel;
			ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(clazz, propertyName);
			if (context != null) {
				fieldLabel = context;
			} else if (fieldInfo != null){
				fieldLabel = fieldInfo.getFieldLabel();
			} else {
				fieldLabel = propertyName;
			}

			if (secondPropertyName == null) {
				return fieldLabel;
			} else {
				String secondContext = "";
				if (ATFieldInfo.getFieldInfo(clazz, secondPropertyName) != null) {
					secondContext = ATFieldInfo.getFieldInfo(clazz, secondPropertyName).getFieldLabel();
				}

				return StringHelper.concat("/", fieldLabel, secondContext);
			}
		}

		public void setContext(String context) {
			this.context = context;
		}
	}
}
