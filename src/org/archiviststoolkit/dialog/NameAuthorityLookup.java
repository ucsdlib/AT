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
 * Created by JFormDesigner on Mon Sep 19 15:26:15 EDT 2005
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.editor.NameEnabledEditor;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.UnsupportedDomainObjectModelException;
import org.archiviststoolkit.exceptions.DomainEditorCreationException;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.swing.WorkSurfaceContainer;
import org.archiviststoolkit.ApplicationFrame;
import org.hibernate.exception.ConstraintViolationException;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;

public class NameAuthorityLookup extends JDialog {

	/**
	 * Constructor used when linking names to archDescription records
 	 * @param owner
	 * @param parentEditor
	 */
	public NameAuthorityLookup(Dialog owner, NameEnabledEditor parentEditor) {
		super(owner);
		this.parentEditor = parentEditor;
		initComponents();
		selectPanel.setVisible(false);
		dialogForLinking = true;
		this.getRootPane().setDefaultButton(this.linkButton);
	}

	/**
	 * Constructor used when linking names to archDescription records
	 * @param parentEditor
	 */
	public NameAuthorityLookup(NameEnabledEditor parentEditor) {
		super();
		this.parentEditor = parentEditor;
		initComponents();
		selectPanel.setVisible(false);
		dialogForLinking = true;
		this.getRootPane().setDefaultButton(this.linkButton);
	}

	/**
	 * Constructor used when simply selecting a name
	 * @param owner
	 */
	public NameAuthorityLookup(Dialog owner) {
		super(owner);
		initComponents();
		linkingPanel.setVisible(false);
		dialogForLinking = false;
		this.getRootPane().setDefaultButton(this.selectButton);
	}

	private void doneButtonActionPerformed(ActionEvent e) {
        status = javax.swing.JOptionPane.CANCEL_OPTION;
        this.setVisible(false);
	}

	public JButton getSelectButton() {
		return selectButton;
	}

	private void selectButtonActionPerformed() {
		if (namesLookupTable.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "You must select a name");
		} else {
			status = javax.swing.JOptionPane.OK_OPTION;
			this.setVisible(false);
		}
	}

	public Names getSelectedName() {
		return (Names)namesTableModel.getElementAt(namesLookupTable.getSelectedRow());
	}

	public JButton getLinkButton() {
		return linkButton;
	}

	private void linkButtonActionPerformed() {
		if (namesLookupTable.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "You must select a name to link");
		} else {
			addSelectedNames();
		}
	}

	private void namesLookupTableKeyTyped(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			 linkButtonActionPerformed();
		}

	}

	private void initComponents() {

		setModal(true);

		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        HeaderPanel = new JPanel();
        mainHeaderPanel = new JPanel();
        mainHeaderLabel = new JLabel();
        panel3 = new JPanel();
        subHeaderLabel = new JLabel();
        contentPane = new JPanel();
        label1 = new JLabel();
        nameLookup = new JTextField();
        scrollPane1 = new JScrollPane();
        namesLookupTable = new DomainSortableTable(Names.class, nameLookup);
        linkingPanel = new JPanel();
        label4 = new JLabel();
        label3 = new JLabel();
        label_function = new JLabel();
        function = new JComboBox();
        label_role = new JLabel();
        role = new JComboBox();
        label_form = new JLabel();
        form = new JComboBox();
        buttonBarLinking = new JPanel();
        linkButton = new JButton();
        createName = new JButton();
        doneButton = new JButton();
        selectPanel = new JPanel();
        selectButton = new JButton();
        doneButton2 = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        Container contentPane2 = getContentPane();
        contentPane2.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(null);
            dialogPane.setBackground(new Color(200, 205, 232));
            dialogPane.setLayout(new BorderLayout());

            //======== HeaderPanel ========
            {
                HeaderPanel.setBackground(new Color(80, 69, 57));
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

                //======== panel3 ========
                {
                    panel3.setBackground(new Color(66, 60, 111));
                    panel3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    panel3.setLayout(new FormLayout(
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
                    subHeaderLabel.setText("Name Lookup");
                    subHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
                    subHeaderLabel.setForeground(Color.white);
                    panel3.add(subHeaderLabel, cc.xy(2, 2));
                }
                HeaderPanel.add(panel3, cc.xy(2, 1));
            }
            dialogPane.add(HeaderPanel, BorderLayout.NORTH);

            //======== contentPane ========
            {
                contentPane.setOpaque(false);
                contentPane.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.UNRELATED_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.UNRELATED_GAP_COLSPEC
                    },
                    new RowSpec[] {
                        FormFactory.UNRELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.UNRELATED_GAP_ROWSPEC
                    }));

                //---- label1 ----
                label1.setText("Filter:");
                contentPane.add(label1, cc.xy(2, 2));
                contentPane.add(nameLookup, cc.xy(4, 2));

                //======== scrollPane1 ========
                {
                    scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                    //---- namesLookupTable ----
                    namesLookupTable.setPreferredScrollableViewportSize(new Dimension(450, 300));
                    namesLookupTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            namesLookupTableMouseClicked(e);
                        }
                    });
                    namesLookupTable.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            namesLookupTableKeyTyped(e);
                        }
                    });
                    scrollPane1.setViewportView(namesLookupTable);
                }
                contentPane.add(scrollPane1, cc.xywh(2, 4, 3, 1));

                //======== linkingPanel ========
                {
                    linkingPanel.setOpaque(false);
                    linkingPanel.setLayout(new FormLayout(
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
                            FormFactory.DEFAULT_ROWSPEC
                        }));

                    //---- label4 ----
                    label4.setText("Double click on a Name to add it to the record.");
                    linkingPanel.add(label4, cc.xywh(1, 1, 3, 1));

                    //---- label3 ----
                    label3.setText("Or hit enter if a Term is highlighted.");
                    linkingPanel.add(label3, cc.xywh(1, 3, 3, 1));

                    //---- label_function ----
                    label_function.setText("Function");
                    linkingPanel.add(label_function, cc.xy(1, 5));

                    //---- function ----
                    function.setOpaque(false);
                    function.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            functionActionPerformed(e);
                        }
                    });
                    linkingPanel.add(function, cc.xywh(3, 5, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //---- label_role ----
                    label_role.setText("Role");
                    linkingPanel.add(label_role, cc.xy(1, 7));

                    //---- role ----
                    role.setOpaque(false);
                    linkingPanel.add(role, cc.xywh(3, 7, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //---- label_form ----
                    label_form.setText("Form Subdivision");
                    linkingPanel.add(label_form, cc.xy(1, 9));

                    //---- form ----
                    form.setOpaque(false);
                    linkingPanel.add(form, cc.xywh(3, 9, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //======== buttonBarLinking ========
                    {
                        buttonBarLinking.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                        buttonBarLinking.setBackground(new Color(231, 188, 251));
                        buttonBarLinking.setOpaque(false);
                        buttonBarLinking.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.BUTTON_COLSPEC,
                                FormFactory.RELATED_GAP_COLSPEC,
                                FormFactory.BUTTON_COLSPEC
                            },
                            RowSpec.decodeSpecs("pref")));

                        //---- linkButton ----
                        linkButton.setText("Link");
                        linkButton.setOpaque(false);
                        linkButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                linkButtonActionPerformed();
                            }
                        });
                        buttonBarLinking.add(linkButton, cc.xy(1, 1));

                        //---- createName ----
                        createName.setText("Create Name");
                        createName.setBackground(new Color(231, 188, 251));
                        createName.setOpaque(false);
                        createName.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                createNameActionPerformed(e);
                            }
                        });
                        buttonBarLinking.add(createName, cc.xy(3, 1));

                        //---- doneButton ----
                        doneButton.setText("Close Window");
                        doneButton.setBackground(new Color(231, 188, 251));
                        doneButton.setOpaque(false);
                        doneButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                doneButtonActionPerformed(e);
                            }
                        });
                        buttonBarLinking.add(doneButton, cc.xy(5, 1));
                    }
                    linkingPanel.add(buttonBarLinking, cc.xywh(1, 11, 3, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
                }
                contentPane.add(linkingPanel, cc.xywh(2, 6, 3, 1));

                //======== selectPanel ========
                {
                    selectPanel.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                    selectPanel.setBackground(new Color(231, 188, 251));
                    selectPanel.setOpaque(false);
                    selectPanel.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.DEFAULT_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.BUTTON_COLSPEC,
                            FormFactory.RELATED_GAP_COLSPEC,
                            FormFactory.BUTTON_COLSPEC
                        },
                        RowSpec.decodeSpecs("pref")));

                    //---- selectButton ----
                    selectButton.setText("Select");
                    selectButton.setOpaque(false);
                    selectButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            selectButtonActionPerformed();
                        }
                    });
                    selectPanel.add(selectButton, cc.xy(3, 1));

                    //---- doneButton2 ----
                    doneButton2.setText("Cancel");
                    doneButton2.setBackground(new Color(231, 188, 251));
                    doneButton2.setOpaque(false);
                    doneButton2.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            doneButtonActionPerformed(e);
                        }
                    });
                    selectPanel.add(doneButton2, cc.xy(5, 1));
                }
                contentPane.add(selectPanel, cc.xywh(1, 8, 4, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
            }
            dialogPane.add(contentPane, BorderLayout.CENTER);
        }
        contentPane2.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	public void setMainHeaderByClass(Class clazz) {
		StandardEditor.setMainHeaderColorAndTextByClass(clazz, mainHeaderPanel, mainHeaderLabel);
	}

	private void namesLookupTableMouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
			if (dialogForLinking) {
				addSelectedNames();
			} else {
				status = javax.swing.JOptionPane.OK_OPTION;
				this.setVisible(false);
			}
		}
    }

	private void addSelectedNames() {
		if (this.getFunction().length() == 0) {
			JOptionPane.showMessageDialog(this, "You must enter a function");
		} else if (namesLookupTable.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "You must select a name");
		} else {
			Names name;
            for (int selectedRow: namesLookupTable.getSelectedRows()) {
                try {
                    name = (Names)namesTableModel.getElementAt(selectedRow);
                    NameEnabledModel nameEnabledModel = (NameEnabledModel)parentEditor.getNameEnabledModel();
                    DomainObject link = nameEnabledModel.addName(name, this.getFunction(), this.getRole(), this.getForm());
                    if (link != null) {
                        parentEditor.getNamesTable().addDomainObject(link);
                    }
					//set the record to dirty
					ApplicationFrame.getInstance().setRecordDirty();
                } catch (DuplicateLinkException e) {
                    new ErrorDialog(this, "That name is already linked").showDialog();
                }
            }
            resetPopupsAndFilter();
		}
	}


	private void functionActionPerformed(ActionEvent e) {
		String selectedFunction = function.getSelectedItem().toString();
		if (selectedFunction.equalsIgnoreCase("subject")) {
			form.setVisible(true);
			label_form.setVisible(true);
			role.setModel(new DefaultComboBoxModel(LookupListUtils.getLookupListValues(LookupListUtils.LIST_NAME_NAME_LINK_SUBJECT_CREATOR_ROLE)));
			role.setVisible(true);
			label_role.setVisible(true);
		} else if (selectedFunction.equalsIgnoreCase("source")) {
			form.setVisible(false);
			label_form.setVisible(false);
			role.setModel(new DefaultComboBoxModel(LookupListUtils.getLookupListValues(LookupListUtils.LIST_NAME_NAME_LINK_SOURCE_ROLE)));
			role.setVisible(true);
			label_role.setVisible(true);
		} else if (selectedFunction.equalsIgnoreCase("creator")) {
			form.setVisible(false);
			label_form.setVisible(false);
			role.setModel(new DefaultComboBoxModel(LookupListUtils.getLookupListValues(LookupListUtils.LIST_NAME_NAME_LINK_SUBJECT_CREATOR_ROLE)));
			role.setVisible(true);
			label_role.setVisible(true);
		} else {
			form.setVisible(false);
			label_form.setVisible(false);
			role.setVisible(false);
			label_role.setVisible(false);
		}

		this.invalidate();
		this.validate();
		this.repaint();

	}

	private void createNameActionPerformed(ActionEvent e) {
		NameEnabledEditor parentEditor = this.parentEditor;
		DomainEditor dialog = null;
		try {
			dialog = DomainEditorFactory.getInstance().createDomainEditorWithParent(Names.class, this, false);
		} catch (DomainEditorCreationException e1) {
			new ErrorDialog(this, "Error creating editor for Names", e1).showDialog();
		}
		dialog.setButtonListeners();
		String nameType = Names.selectNameType(this);
		if ((nameType != null) && (nameType.length() > 0)) {
			Names instance = new Names();
			instance.setNameType(nameType);
			dialog.setModel(instance, null);
			dialog.disableNavigationButtons();
			if (dialog.showDialog() == javax.swing.JOptionPane.OK_OPTION) {
				try {
					DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Names.class);
					access.add(instance);
					Names.addNameToLookupList(instance);
					WorkSurfaceContainer.getWorkSurfaceByClass(Names.class).addToResultSet(instance);
					initLookup();
					nameLookup.setText(instance.getSortName());
					namesLookupTable.setRowSelectionInterval(0,0);

				} catch (ConstraintViolationException persistenceException) {
					JOptionPane.showMessageDialog(this, "Can't save, Duplicate record:" + instance);
				} catch (PersistenceException persistenceException) {
					if (persistenceException.getCause() instanceof ConstraintViolationException) {
						JOptionPane.showMessageDialog(this, "Can't save, Duplicate record:" + instance);
						return;
					}
					new ErrorDialog(this, "Error saving new record.", persistenceException).showDialog();
				}
			}
		}

	}

	private void initLookup() {
		SortedList sortedNames = Names.getNamesGlazedList();
		textFilteredIssues = new FilterList(sortedNames, new TextComponentMatcherEditor(nameLookup, new DomainFilterator()));
		namesTableModel = new EventTableModel(textFilteredIssues, new DomainTableFormat(Names.class));
		namesLookupTable.setModel(namesTableModel);
		TableComparatorChooser tableSorter = new TableComparatorChooser(namesLookupTable, sortedNames, true);
		nameLookup.requestFocusInWindow();
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel HeaderPanel;
    private JPanel mainHeaderPanel;
    private JLabel mainHeaderLabel;
    private JPanel panel3;
    private JLabel subHeaderLabel;
    private JPanel contentPane;
    private JLabel label1;
    private JTextField nameLookup;
    private JScrollPane scrollPane1;
    private DomainSortableTable namesLookupTable;
    private JPanel linkingPanel;
    private JLabel label4;
    private JLabel label3;
    private JLabel label_function;
    private JComboBox function;
    private JLabel label_role;
    private JComboBox role;
    private JLabel label_form;
    private JComboBox form;
    private JPanel buttonBarLinking;
    private JButton linkButton;
    private JButton createName;
    private JButton doneButton;
    private JPanel selectPanel;
    private JButton selectButton;
    private JButton doneButton2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	/**
	 * The status of the editor.
	 */
	protected int status = 0;

	private Names nameModel = null;
	private NameEnabledEditor parentEditor;
	FilterList textFilteredIssues;
	EventTableModel namesTableModel;
	Vector nameLinkFunctionvalues;
	boolean dialogForLinking;

	/**
	 * Flag indicating if the "Cancel" button was pressed to close dialog.
	 */
	protected boolean myIsDialogCancelled = true;

	/**
	 * Displays the dialog box representing the editor.
	 *
	 * @return true if it displayed okay
	 */

	public final int showDialog() {

		this.pack();
		initLookup();
		setLocationRelativeTo(this.getOwner());
		if (dialogForLinking) {
			resetPopupsAndFilter();
		}

		this.setVisible(true);

		return (status);
	}

    private void resetPopupsAndFilter() {
        form.setVisible(false);
        form.setModel(new DefaultComboBoxModel(LookupListUtils.getLookupListValues(LookupListUtils.LIST_NAME_NAME_LINK_FORM)));
        label_form.setVisible(false);
        role.setVisible(false);
        label_role.setVisible(false);
        function.setModel(new DefaultComboBoxModel(nameLinkFunctionvalues));
        nameLookup.setText("");
        nameLookup.requestFocusInWindow();
        namesLookupTable.clearSelection();
        this.invalidate();
        this.validate();
        this.repaint();
    }

    public void setFunctionLookupvalues(Class clazz) throws UnsupportedDomainObjectModelException {
		if (clazz == Accessions.class) {
			nameLinkFunctionvalues = LookupListUtils.getLookupListValues(LookupListUtils.LIST_NAME_NAME_LINK_FUNCTION);

		} else if (clazz == Resources.class) {
			nameLinkFunctionvalues = LookupListUtils.getLookupListValues(LookupListUtils.LIST_NAME_NAME_LINK_FUNCTION);

		} else if (clazz == ResourcesComponents.class) {
			nameLinkFunctionvalues = LookupListUtils.getLookupListValues(LookupListUtils.LIST_NAME_NAME_LINK_FUNCTION);
			nameLinkFunctionvalues.remove("Source");

		} else if (clazz == DigitalObjects.class) {
			nameLinkFunctionvalues = LookupListUtils.getLookupListValues(LookupListUtils.LIST_NAME_NAME_LINK_FUNCTION);
			nameLinkFunctionvalues.remove("Source");
		} else {
			throw new UnsupportedDomainObjectModelException(clazz);
		}
	}

	public Names getNameModel() {
		return nameModel;
	}

	public void setNameModel(Names nameModel) {
		this.nameModel = nameModel;
	}

	public String getRole() {
		return role.getSelectedItem().toString();
	}

	public String getFunction() {
		return function.getSelectedItem().toString();
	}

	public String getForm() {
		return form.getSelectedItem().toString();
	}
}
