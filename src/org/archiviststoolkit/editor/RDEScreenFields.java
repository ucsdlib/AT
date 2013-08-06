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

import java.awt.event.*;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.model.RDEScreen;
import org.archiviststoolkit.model.RDEScreenPanels;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.TableNotConfiguredException;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.util.SequencedObjectsUtils;
import org.archiviststoolkit.util.RDEUtils;
import org.archiviststoolkit.util.RDEFactory;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.exceptions.DomainEditorCreationException;
import org.archiviststoolkit.exceptions.RDEPanelCreationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Collections;

public class RDEScreenFields extends DomainEditorFields {

	Vector<Options> optionList;
	RDEScreen RDEModel;

	public RDEScreenFields() {
		super();
		initComponents();
	}

	private void initOptions(Class clazz) throws TableNotConfiguredException {

		optionList = new Vector<Options>();
		for (ATFieldInfo fieldInfo: ATFieldInfo.getFieldsForRDE(clazz)) {
            String label = fieldInfo.getFieldLabel();
            if(!label.equalsIgnoreCase("level")) {
                optionList.add(new Options(fieldInfo));
            }
        }
		addClassSpecificOptions(clazz);
		Collections.sort(optionList);

		options.setModel(new DefaultComboBoxModel(optionList));
	}

	private void insertButtonActionPerformed() {
		int selectedIndex = rdeScreenPanels.getSelectedRow();
		if (selectedIndex == -1) {
			JOptionPane.showMessageDialog(this, "You must select a panel to insert above.", "warning", JOptionPane.WARNING_MESSAGE);
		} else {
			addPanel(SequencedObjectsUtils.ADD_ABOVE_SELECTION);
		}
	}

	private boolean duplicateCheckOkToAdd(Options selectedOption) {
		String selectedPanelType = selectedOption.getPanelType();
		if (selectedPanelType.equals(RDEScreenPanels.PANEL_TYPE_SIMPLE) ||
				selectedPanelType.equals(RDEScreenPanels.PANEL_TYPE_SUBJECT_LINK) ||
				selectedPanelType.equals(RDEScreenPanels.PANEL_TYPE_NAME_LINK)) {
			for (RDEScreenPanels panel: RDEModel.getScreenPanels()) {

				if (panel.getPanelType().equals(RDEScreenPanels.PANEL_TYPE_SIMPLE) &&
						panel.getPropertyName().equals(selectedOption.getPropertyName())) {
					return false;
				} else if (panel.getPanelType().equals(RDEScreenPanels.PANEL_TYPE_SUBJECT_LINK) &&
						selectedPanelType.equals(RDEScreenPanels.PANEL_TYPE_SUBJECT_LINK)) {
					return false;
				} else if (panel.getPanelType().equals(RDEScreenPanels.PANEL_TYPE_NAME_LINK) &&
						selectedPanelType.equals(RDEScreenPanels.PANEL_TYPE_NAME_LINK)) {
					return false;
				}
			}
			return true;
		} else {
			//only simple types can't be repeated
			return true;
		}
	}

	private void addButtonActionPerformed() {
		addPanel(SequencedObjectsUtils.ADD_AT_END);
	}

	private void addPanel(String where) {
		int selectedIndex = options.getSelectedIndex();
		if (selectedIndex == -1) {
			JOptionPane.showMessageDialog(this, "You must select a panel to add.", "warning", JOptionPane.WARNING_MESSAGE);
		} else {
			Options selectedOption = (Options)options.getSelectedValue();
			if (!duplicateCheckOkToAdd(selectedOption)) {
				JOptionPane.showMessageDialog(this, selectedOption + " already exists in the list.", "warning", JOptionPane.WARNING_MESSAGE);
			} else {

				int sequenceNumber = SequencedObjectsUtils.determineSequenceOfNewItem(where, rdeScreenPanels);
				RDEScreenPanels newPanel = null;
				try {
					newPanel = RDEFactory.getInstance().createRDEScreenPanel(RDEModel, selectedOption.getPanelType(), selectedOption.getPropertyName());
				} catch (RDEPanelCreationException e) {
					new ErrorDialog("There was a problem adding a panel", e).showDialog();
				}
				addPanel(sequenceNumber, newPanel);
			}
		}

	}

	private void addPanel(int sequenceNumber, RDEScreenPanels newPanel) {
		newPanel.setSequenceNumber(sequenceNumber);
		SequencedObject.adjustSequenceNumberForAdd(RDEModel.getScreenPanels(), newPanel);
		RDEModel.addScreenPanels(newPanel);
		rdeScreenPanels.getEventList().add(newPanel);
//		if (!where.equals(SequencedObjectsUtils.ADD_AT_END)) {
			rdeScreenPanels.setRowSelectionInterval(sequenceNumber, sequenceNumber);
//		}
	}

    // function to add any panels that should be there by defualt
    private void addDefaultPanels() {
        // add the level panel
        RDEScreenPanels newPanel = null;
        try {
            newPanel = RDEFactory.getInstance().createRDEScreenPanel(RDEModel, RDEScreenPanels.PANEL_TYPE_SIMPLE, "level");
        } catch (RDEPanelCreationException e) {
            new ErrorDialog("There was a problem adding the Level panel", e).showDialog();
        }

        addPanel(0, newPanel);
    }

    private void optionsMouseClicked(MouseEvent e) {
		if (e.getClickCount() ==2) {
			addPanel(SequencedObjectsUtils.ADD_AT_END);
		}
	}

	private void removeButtonActionPerformed() {
        if(canRemoveSelectedPanels()) {
            try {
                this.removeRelatedTableRow(rdeScreenPanels, null, RDEModel);
                SequencedObject.resequenceSequencedObjects(RDEModel.getScreenPanels());
            } catch (ObjectNotRemovedException e) {
                new ErrorDialog("Item not removed", e).showDialog();
            }
            rdeScreenPanels.updateCollection(RDEModel.getScreenPanels());
        }
    }

    // Method to check to see if the user can remove the selected panels. Need this to make sure that none of the
    // required panels can be removed
    private boolean canRemoveSelectedPanels() {
        boolean canRemove = true;

        ArrayList<DomainObject> domainObjects = rdeScreenPanels.getSelectedObjects();
        for (DomainObject rdePanel: domainObjects) {
            if(rdePanel.toString().equalsIgnoreCase("level")) {
                JOptionPane.showMessageDialog(this, "Level item is required and cannot be removed", "warning", JOptionPane.WARNING_MESSAGE);
                canRemove = false;
                break;
            }
        }

        return canRemove;
    }

    private void moveUpButtonActionPerformed() {
		int selectedPanelIndex = rdeScreenPanels.getSelectedRow();
		if (selectedPanelIndex == -1) {
			JOptionPane.showMessageDialog(this, "You must select a panel to add.", "warning", JOptionPane.WARNING_MESSAGE);
		} else if (selectedPanelIndex > 0){
			handleMoveActions(selectedPanelIndex, -1);
//			RDEScreenPanels panelToMove = (RDEScreenPanels)rdeScreenPanels.getSortedList().get(selectedPanelIndex);
//			RDEModel.removeRelatedObject(panelToMove);
//			SequencedObject.resequenceSequencedObjects(RDEModel.getScreenPanels());
//			rdeScreenPanels.getEventList().remove(panelToMove);
//			addPanel(panelToMove.getSequenceNumber() - 1, panelToMove);

//			panelToMove.setSequenceNumber(panelToMove.getSequenceNumber() - 1);
//			SequencedObject.adjustSequenceNumberForAdd(RDEModel.getScreenPanels(), panelToMove);
//			RDEModel.addScreenPanels(panelToMove);
//			rdeScreenPanels.updateCollection(RDEModel.getScreenPanels());
//			rdeScreenPanels.setRowSelectionInterval(selectedPanelIndex - 1, selectedPanelIndex - 1);
		}
	}

	private void moveDownButtonActionPerformed() {
		int selectedPanelIndex = rdeScreenPanels.getSelectedRow();
		if (selectedPanelIndex == -1) {
			JOptionPane.showMessageDialog(this, "You must select a panel to add.", "warning", JOptionPane.WARNING_MESSAGE);
		} else if (selectedPanelIndex < rdeScreenPanels.getRowCount() - 1){
			handleMoveActions(selectedPanelIndex, 1);


//			panelToMove.setSequenceNumber(panelToMove.getSequenceNumber() + 1);
//			SequencedObject.adjustSequenceNumberForAdd(RDEModel.getScreenPanels(), panelToMove);
//			RDEModel.addScreenPanels(panelToMove);
//			rdeScreenPanels.updateCollection(RDEModel.getScreenPanels());
//			rdeScreenPanels.setRowSelectionInterval(selectedPanelIndex + 1, selectedPanelIndex + 1);
		}
	}

	private void handleMoveActions(int selectedPanelIndex, int offset) {
		RDEScreenPanels panelToMove = (RDEScreenPanels)rdeScreenPanels.getSortedList().get(selectedPanelIndex);
		RDEModel.removeRelatedObject(panelToMove);
		SequencedObject.resequenceSequencedObjects(RDEModel.getScreenPanels());
		rdeScreenPanels.getEventList().remove(panelToMove);
		addPanel(panelToMove.getSequenceNumber() + offset, panelToMove);
	}

	private void rdeScreenPanelsMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			int selectedRow = getRdeScreenPanels().getSelectedRow();
			if (selectedRow != -1) {
				DomainObject currentNote = getRdeScreenPanels().getSortedList().get(selectedRow);
				DomainEditor dialog = null;
				try {
					dialog = DomainEditorFactory.getInstance().createDomainEditorWithParent(RDEScreenPanels.class, getParentEditor(), getRdeScreenPanels(), super.getModel());
				} catch (DomainEditorCreationException e1) {
					new ErrorDialog(getParentEditor(), "Error creating editor for RDE Screen Panels", e1).showDialog();

				}
				dialog.setSelectedRow(selectedRow);
				dialog.setModel(currentNote, null);
				dialog.setNavigationButtons();
				int status = dialog.showDialog();

				if (status == javax.swing.JOptionPane.OK_OPTION) {
					getRdeScreenPanels().getSortedList().set(getRdeScreenPanels().getSelectedRow(),
							getRdeScreenPanels().getSortedList().get(getRdeScreenPanels().getSelectedRow()));
				}
			}
		}
	}


	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label_RDE_name = new JLabel();
        rdeScreenName = ATBasicComponentFactory.createTextField(detailsModel.getModel(RDEScreen.PROPERTYNAME_NAME_RDE_SCREEN_NAME));
        panel1 = new JPanel();
        panel2 = new JPanel();
        label2 = new JLabel();
        label3 = new JLabel();
        scrollPane2 = new JScrollPane();
        options = new JList();
        panel3 = new JPanel();
        addButton = new JButton();
        removeButton = new JButton();
        moveUpButton = new JButton();
        moveDownButton = new JButton();
        scrollPane1 = new JScrollPane();
        rdeScreenPanels = new DomainSortedTable(RDEScreenPanels.class);
        label1 = new JLabel();
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
                new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
            }));

        //---- label_RDE_name ----
        label_RDE_name.setText("RDE Name");
        label_RDE_name.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label_RDE_name, RDEScreen.class, RDEScreen.PROPERTYNAME_NAME_RDE_SCREEN_NAME);
        add(label_RDE_name, cc.xy(1, 1));
        add(rdeScreenName, cc.xy(3, 1));

        //======== panel1 ========
        {
            panel1.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC
                },
                RowSpec.decodeSpecs("")));
        }
        add(panel1, cc.xywh(1, 3, 3, 1));

        //======== panel2 ========
        {
            panel2.setLayout(new FormLayout(
                new ColumnSpec[] {
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                },
                new RowSpec[] {
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC
                }));
            ((FormLayout)panel2.getLayout()).setColumnGroups(new int[][] {{1, 5}});

            //---- label2 ----
            label2.setText("Items to pick from");
            panel2.add(label2, cc.xy(1, 1));

            //---- label3 ----
            label3.setText("Items picked");
            panel2.add(label3, cc.xy(5, 1));

            //======== scrollPane2 ========
            {

                //---- options ----
                options.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                options.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        optionsMouseClicked(e);
                    }
                });
                scrollPane2.setViewportView(options);
            }
            panel2.add(scrollPane2, cc.xywh(1, 3, 1, 3, CellConstraints.DEFAULT, CellConstraints.FILL));

            //======== panel3 ========
            {
                panel3.setLayout(new FormLayout(
                    ColumnSpec.decodeSpecs("default"),
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //---- addButton ----
                addButton.setText("Add ->");
                addButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addButtonActionPerformed();
                    }
                });
                panel3.add(addButton, cc.xy(1, 1));

                //---- removeButton ----
                removeButton.setText("Remove");
                removeButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeButtonActionPerformed();
                    }
                });
                panel3.add(removeButton, cc.xy(1, 3));

                //---- moveUpButton ----
                moveUpButton.setText("Move Up");
                moveUpButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        moveUpButtonActionPerformed();
                    }
                });
                panel3.add(moveUpButton, cc.xy(1, 5));

                //---- moveDownButton ----
                moveDownButton.setText("Move Down");
                moveDownButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        moveDownButtonActionPerformed();
                    }
                });
                panel3.add(moveDownButton, cc.xy(1, 7));
            }
            panel2.add(panel3, cc.xy(3, 3));

            //======== scrollPane1 ========
            {

                //---- rdeScreenPanels ----
                rdeScreenPanels.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        rdeScreenPanelsMouseClicked(e);
                    }
                });
                scrollPane1.setViewportView(rdeScreenPanels);
            }
            panel2.add(scrollPane1, cc.xy(5, 3));

            //---- label1 ----
            label1.setText("Double click to set stickiness options");
            panel2.add(label1, cc.xy(5, 5));
        }
        add(panel2, cc.xywh(1, 5, 3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label_RDE_name;
    public JTextField rdeScreenName;
    private JPanel panel1;
    private JPanel panel2;
    private JLabel label2;
    private JLabel label3;
    private JScrollPane scrollPane2;
    private JList options;
    private JPanel panel3;
    private JButton addButton;
    private JButton removeButton;
    private JButton moveUpButton;
    private JButton moveDownButton;
    private JScrollPane scrollPane1;
    private DomainSortedTable rdeScreenPanels;
    private JLabel label1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public final void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		if (model instanceof RDEScreen) {
			RDEModel = (RDEScreen)model;
			try {
				initOptions(RDEModel.getClazz());
			} catch (TableNotConfiguredException e) {
				new ErrorDialog("", e).showDialog();
			} catch (ClassNotFoundException e) {
				new ErrorDialog("", e).showDialog();
			}
			rdeScreenPanels.updateCollection(RDEModel.getScreenPanels());

            // check to see if to add any default panels by seeing if there are no panels in the RDEModel already
            if(RDEModel.getScreenPanels().isEmpty()) {
                addDefaultPanels();
            }
        }
	}

	public Component getInitialFocusComponent() {
		return rdeScreenName;
	}

	private void addClassSpecificOptions(Class clazz) {
		if (clazz == ResourcesComponents.class) {
			optionList.add(new Options(RDEScreenPanels.PANEL_TYPE_NOTE));
			optionList.add(new Options(RDEScreenPanels.PANEL_TYPE_EXTENT));
			optionList.add(new Options(RDEScreenPanels.PANEL_TYPE_YEAR_RANGE_INCLUSIVE));
			optionList.add(new Options(RDEScreenPanels.PANEL_TYPE_YEAR_RANGE_BULK));
			optionList.add(new Options(RDEScreenPanels.PANEL_TYPE_ANALOG_INSTANCE));
			optionList.add(new Options(RDEScreenPanels.PANEL_TYPE_DIGITAL_INSTANCE));
			optionList.add(new Options(RDEScreenPanels.PANEL_TYPE_SUBJECT_LINK));
			optionList.add(new Options(RDEScreenPanels.PANEL_TYPE_NAME_LINK));
		}
	}

	public DomainSortedTable getRdeScreenPanels() {
		return rdeScreenPanels;
	}

	public void setRdeScreenPanels(DomainSortedTable rdeScreenPanels) {
		this.rdeScreenPanels = rdeScreenPanels;
	}

	private class Options implements Comparable{

		private ATFieldInfo fieldInfo = null;
		private String panelType;

		private Options(String panelType) {
			this.panelType = panelType;
		}


		private Options(ATFieldInfo fieldInfo) {
			this.fieldInfo = fieldInfo;
			this.panelType = RDEScreenPanels.PANEL_TYPE_SIMPLE;
		}

		public String toString() {
			if (fieldInfo != null) {
				return fieldInfo.getFieldLabel();
			} else {
				return RDEUtils.getPrettyName(panelType);
			}
		}

		public String getPropertyName() {
			if (fieldInfo != null) {
				return fieldInfo.getFieldName();
			} else {
				return "";
			}
		}

		public String getPanelType() {
			return panelType;
		}

		public void setPanelType(String panelType) {
			this.panelType = panelType;
		}

		public int compareTo(Object o) {
			return this.toString().compareTo(((Options)o).toString());
		}
	}

}