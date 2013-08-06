/**
 * Archivists' Toolkit(TM) Copyright © 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 * Created by JFormDesigner on Thu Sep 29 11:19:45 EDT 2005
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.editor.SubjectEditor;
import org.archiviststoolkit.editor.SubjectEnabledEditorFields;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.model.Subjects;
import org.archiviststoolkit.model.SubjectEnabledModel;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.swing.WorkSurfaceContainer;
import org.archiviststoolkit.ApplicationFrame;
import org.hibernate.exception.ConstraintViolationException;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;

/**
 * @author Lee Mandell
 */
public class SubjectTermLookup extends JDialog {

	/**
	 * Constructor used when linking names to archDescription records
 	 * @param owner - the owning dialog box
	 * @param parentEditorFields - the domain editor fields to link to
	 */
	public SubjectTermLookup(Dialog owner, SubjectEnabledEditorFields parentEditorFields) {
		super(owner);
		this.parentEditorFields = parentEditorFields;
		initComponents();
		selectPanel.setVisible(false);
		dialogForLinking = true;
		this.getRootPane().setDefaultButton(this.linkSubjectButton);
	}

	/**
	 * Constructor used when simply selecting a subject
	 * @param owner - the owning dialog box
	 */
	public SubjectTermLookup(Dialog owner) {
		super(owner);
		initComponents();
		linkingPanel.setVisible(false);
		dialogForLinking = false;
		this.getRootPane().setDefaultButton(this.selectButton);
	}

	/**
	 * Constructor used when simply selecting a subject
	 */
	public SubjectTermLookup(SubjectEnabledEditorFields parentEditorFields) {
		super();
		this.parentEditorFields = parentEditorFields;
		initComponents();
		selectPanel.setVisible(false);
		dialogForLinking = true;
		this.getRootPane().setDefaultButton(this.linkSubjectButton);
	}

	public void setMainHeaderByClass(Class clazz) {
		StandardEditor.setMainHeaderColorAndTextByClass(clazz, mainHeaderPanel, mainHeaderLabel);
	}

	private void createSubjectButtonActionPerformed() {
		SubjectEnabledEditorFields parentEditorFields = this.parentEditorFields;
		SubjectEnabledModel subjectEnabledModel = (SubjectEnabledModel) parentEditorFields.getSubjectEnabledModel();

		DomainEditor dialog = new SubjectEditor(this);
		dialog.setButtonListeners();
		Subjects instance = new Subjects();
		dialog.setModel(instance, null);
		dialog.disableNavigationButtons();
		if (dialog.showDialog() == javax.swing.JOptionPane.OK_OPTION) {
			try {
				DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Subjects.class);
				access.add(instance);
				DomainObject link = subjectEnabledModel.addSubject(instance);
				parentEditorFields.getSubjectsTable().addDomainObject(link);
				Subjects.addSubjectToLookupList(instance);
				WorkSurfaceContainer.getWorkSurfaceByClass(Subjects.class).addToResultSet(instance);
				initLookup();
				resetSelection();
			} catch (ConstraintViolationException persistenceException) {
				JOptionPane.showMessageDialog(this, "Can't save, Duplicate record:" + instance);
			} catch (PersistenceException persistenceException) {
				if (persistenceException.getCause() instanceof ConstraintViolationException) {
					JOptionPane.showMessageDialog(this, "Can't save, Duplicate record:" + instance);
					return;
				}
				new ErrorDialog(this, "Error saving new record.", persistenceException).showDialog();
			} catch (DuplicateLinkException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage() + " is already linked to this record");
			}
		}
	}

	private void subjectLookupTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			if (dialogForLinking) {
				addSelectedSubjects();
			} else {
				status = javax.swing.JOptionPane.OK_OPTION;
				this.setVisible(false);
			}
		}
	}

	private void linkSubjectButtonActionPerformed() {
		if (getSubjectLookupTable().getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "You must select a subject to link");
		} else {
			addSelectedSubjects();
		}
	}

	public JButton getLinkSubjectButton() {
		return linkSubjectButton;
	}

	private void selectButtonActionPerformed() {
		if (getSubjectLookupTable().getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "You must select a subject");
		} else {
			status = javax.swing.JOptionPane.OK_OPTION;
			this.setVisible(false);
		}
	}

	public Subjects getSelectedSubject() {
		return (Subjects) subjectsTableModel.getElementAt(getSubjectLookupTable().getSelectedRow());
	}

	public JButton getSelectButton() {
		return selectButton;
	}

	private void doneButtonActionPerformed() {
		status = javax.swing.JOptionPane.CANCEL_OPTION;
		this.setVisible(false);
	}

	private void subjectLookupTableKeyTyped(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            // check to see if we have multiple row selection. If not, we need to reset the selection
            // to fix ART-1705
			if(getSubjectLookupTable().getSelectedRowCount() == 1) {
                int row = getSubjectLookupTable().getSelectedRow();
                if(row == 0) { // must have selected last item is list so set rwo to the number of items in the list
                    row = getSubjectLookupTable().getRowCount();
                }
                row--; // go back one row and set that has the selected row
                getSubjectLookupTable().setRowSelectionInterval(row, row);
            }

            linkSubjectButtonActionPerformed();
		}		

	}

	private void initComponents() {
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
        subjectLookup = new JTextField();
        scrollPane1 = new JScrollPane();
        subjectLookupTable = new DomainSortableTable(Subjects.class, subjectLookup);
        linkingPanel = new JPanel();
        label3 = new JLabel();
        label4 = new JLabel();
        buttonBar = new JPanel();
        linkSubjectButton = new JButton();
        createSubjectButton = new JButton();
        doneButton = new JButton();
        selectPanel = new JPanel();
        buttonBar2 = new JPanel();
        selectButton = new JButton();
        cancelButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal(true);
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
                    subHeaderLabel.setText("Subject Term Lookup");
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
                        FormFactory.DEFAULT_COLSPEC
                    },
                    new RowSpec[] {
                        FormFactory.UNRELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //---- label1 ----
                label1.setText("Filter:");
                contentPane.add(label1, cc.xy(2, 2));
                contentPane.add(subjectLookup, cc.xy(4, 2));

                //======== scrollPane1 ========
                {
                    scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                    //---- subjectLookupTable ----
                    subjectLookupTable.setPreferredScrollableViewportSize(new Dimension(450, 300));
                    subjectLookupTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            subjectLookupTableMouseClicked(e);
                        }
                    });
                    subjectLookupTable.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            subjectLookupTableKeyTyped(e);
                        }
                    });
                    scrollPane1.setViewportView(subjectLookupTable);
                }
                contentPane.add(scrollPane1, cc.xywh(2, 4, 3, 1));

                //======== linkingPanel ========
                {
                    linkingPanel.setOpaque(false);
                    linkingPanel.setLayout(new FormLayout(
                        ColumnSpec.decodeSpecs("default:grow"),
                        new RowSpec[] {
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC
                        }));

                    //---- label3 ----
                    label3.setText("Double click on a Subject Term to add it to the record.");
                    linkingPanel.add(label3, cc.xy(1, 1));

                    //---- label4 ----
                    label4.setText("Or hit enter if a Term is highlighted.");
                    linkingPanel.add(label4, cc.xy(1, 3));

                    //======== buttonBar ========
                    {
                        buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                        buttonBar.setBackground(new Color(231, 188, 251));
                        buttonBar.setOpaque(false);
                        buttonBar.setLayout(new FormLayout(
                            new ColumnSpec[] {
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                FormFactory.BUTTON_COLSPEC,
                                FormFactory.RELATED_GAP_COLSPEC,
                                FormFactory.BUTTON_COLSPEC
                            },
                            RowSpec.decodeSpecs("pref")));

                        //---- linkSubjectButton ----
                        linkSubjectButton.setText("Link");
                        linkSubjectButton.setOpaque(false);
                        linkSubjectButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                linkSubjectButtonActionPerformed();
                            }
                        });
                        buttonBar.add(linkSubjectButton, cc.xy(1, 1));

                        //---- createSubjectButton ----
                        createSubjectButton.setText("Create Subject");
                        createSubjectButton.setOpaque(false);
                        createSubjectButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                createSubjectButtonActionPerformed();
                            }
                        });
                        buttonBar.add(createSubjectButton, cc.xy(3, 1));

                        //---- doneButton ----
                        doneButton.setText("Close Window");
                        doneButton.setOpaque(false);
                        doneButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                doneButtonActionPerformed();
                            }
                        });
                        buttonBar.add(doneButton, cc.xy(5, 1));
                    }
                    linkingPanel.add(buttonBar, cc.xywh(1, 5, 1, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
                }
                contentPane.add(linkingPanel, cc.xywh(2, 6, 3, 1));

                //======== selectPanel ========
                {
                    selectPanel.setOpaque(false);
                    selectPanel.setLayout(new FormLayout(
                        "default:grow",
                        "default"));

                    //======== buttonBar2 ========
                    {
                        buttonBar2.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                        buttonBar2.setBackground(new Color(231, 188, 251));
                        buttonBar2.setOpaque(false);
                        buttonBar2.setLayout(new FormLayout(
                            new ColumnSpec[] {
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
                        buttonBar2.add(selectButton, cc.xy(1, 1));

                        //---- cancelButton ----
                        cancelButton.setText("Cancel");
                        cancelButton.setOpaque(false);
                        cancelButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                doneButtonActionPerformed();
                            }
                        });
                        buttonBar2.add(cancelButton, cc.xy(3, 1));
                    }
                    selectPanel.add(buttonBar2, cc.xywh(1, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
                }
                contentPane.add(selectPanel, cc.xywh(2, 8, 3, 1));
            }
            dialogPane.add(contentPane, BorderLayout.CENTER);
        }
        contentPane2.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents

	}

	public JButton getCreateSubjectButton() {
		return createSubjectButton;
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
    private JTextField subjectLookup;
    private JScrollPane scrollPane1;
    private DomainSortableTable subjectLookupTable;
    private JPanel linkingPanel;
    private JLabel label3;
    private JLabel label4;
    private JPanel buttonBar;
    private JButton linkSubjectButton;
    private JButton createSubjectButton;
    private JButton doneButton;
    private JPanel selectPanel;
    private JPanel buttonBar2;
    private JButton selectButton;
    private JButton cancelButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	/**
	 * The status of the editor.
	 */
	protected int status = 0;
	private SubjectEnabledEditorFields parentEditorFields;
	FilterList textFilteredIssues;
	EventTableModel subjectsTableModel;
	boolean dialogForLinking;

	public final int showDialog() {

		this.pack();
		initLookup();
		setLocationRelativeTo(this.getOwner());
		subjectLookup.setText("");
		this.setVisible(true);

		return (status);
	}

	private void initLookup() {
		SortedList sortedSubjects = Subjects.getSubjectsGlazedList();
		textFilteredIssues = new FilterList(sortedSubjects, new TextComponentMatcherEditor(subjectLookup, new SubjectsFilterator()));
		subjectsTableModel = new EventTableModel(textFilteredIssues, new DomainTableFormat(Subjects.class));
		getSubjectLookupTable().setModel(subjectsTableModel);
		TableComparatorChooser tableSorter = new TableComparatorChooser(getSubjectLookupTable(), sortedSubjects, true);
		subjectLookup.requestFocusInWindow();
	}

	public JTable getSubjectLookupTable() {
		return subjectLookupTable;
	}

	public JTextField getSubjectLookup() {
		return subjectLookup;
	}

	public void setSubjectLookup(JTextField subjectLookup) {
		this.subjectLookup = subjectLookup;
	}

	private void addSelectedSubjects() {

        for (int selectedRow: getSubjectLookupTable().getSelectedRows()) {
//            Subjects subject = (Subjects) subjectsTableModel.getElementAt(selectedRow);
			Subjects subject = (Subjects) textFilteredIssues.get(selectedRow);
			try {
                SubjectEnabledModel subjectEnabledModel = (SubjectEnabledModel) parentEditorFields.getSubjectEnabledModel();
                DomainObject link = subjectEnabledModel.addSubject(subject);
                if (link != null) {
                    parentEditorFields.getSubjectsTable().addDomainObject(link);
                }
				//set the record to dirty
				ApplicationFrame.getInstance().setRecordDirty();
            } catch (DuplicateLinkException e) {
                JOptionPane.showMessageDialog(this, e.getMessage() + " is already linked to this record");
            }
        }
		resetSelection();
    }

	private void resetSelection() {
		subjectLookup.setText("");
		subjectLookup.requestFocusInWindow();
		subjectLookupTable.clearSelection();
	}

}
