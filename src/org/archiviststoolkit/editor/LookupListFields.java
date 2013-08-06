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
 * Created by JFormDesigner on Tue Sep 13 11:34:17 EDT 2005
 */

package org.archiviststoolkit.editor;

import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.dialog.LookupListItemsDialog;
import org.archiviststoolkit.exceptions.ValidationException;
import org.archiviststoolkit.importer.ImportUtils;
import org.archiviststoolkit.model.LookupList;
import org.archiviststoolkit.model.LookupListItems;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.swing.*;
import org.archiviststoolkit.util.LookupListUtils;
import org.hibernate.exception.ConstraintViolationException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class LookupListFields extends DomainEditorFields {


	public LookupListFields() {
		super();
		initComponents();
		ListSelectionModel selectionModel = listItems.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				listItemsRowSelectionChange();
			}
		});

		listItems.setClazzAndColumns(LookupListItems.class, LookupListItems.PROPERTYNAME_LIST_ITEM, LookupListItems.PROPERTYNAME_CODE);
	}

	private void showRecordCountActionPerformed(ActionEvent e) {
		Thread performer = new Thread(new Runnable() {
			public void run() {
				InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(getParentEditor(), 1000);
				monitor.start("Calculating record counts...");
				try {
					int count = 1;
					int totalSize = lookupListModel.getListItems().size();
					for (LookupListItems item : lookupListModel.getListItems()) {
						item.setRecordCount(getCountBasedOnPropertyValue(lookupListModel, item.getListItem()));
						monitor.setTextLine(count++ + " of " + totalSize, 2);
					}
					tableCellRenderer.setIncludeRecordCount(true);
					listItems.invalidate();
					listItems.validate();
					listItems.repaint();
				} catch (PersistenceException e1) {
					monitor.close();
					new ErrorDialog(getParentEditor(), "Error get record count", e1).showDialog();
				} finally {
					monitor.close();
				}
			}
		}, "ShowRecordCount");
		performer.start();
	}

	private void listItemsRowSelectionChange() {

		LookupListItems selectedItem;
		removeItem.setEnabled(true);
		changeItem.setEnabled(true);
		if (listItems.getSelectedRowCount() > 1) {
			mergeItems.setEnabled(true);
		} else {
			mergeItems.setEnabled(false);
		}
		int permItemCount = 0;
		for (int index : listItems.getSelectedRows()) {
			selectedItem = (LookupListItems) listItems.getDomainObject(index);
			if (selectedItem != null && !selectedItem.getEditable()) {
				removeItem.setEnabled(false);
				changeItem.setEnabled(false);
				permItemCount++;
				if (permItemCount > 1) {
					mergeItems.setEnabled(false);
				}
			}
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label1 = new JLabel();
        listName = ATBasicComponentFactory.createTextField(detailsModel.getModel(LookupList.PROPERTYNAME_LIST_NAME));
        panel3 = new JPanel();
        pairedValues = ATBasicComponentFactory.createCheckBox(detailsModel, LookupList.PROPERTYNAME_PAIRED_VALUES, LookupList.class);
        pairedValues2 = ATBasicComponentFactory.createCheckBox(detailsModel, LookupList.PROPERTYNAME_RESTRICT_TO_NMTOKEN, LookupList.class);
        panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        listItems = new DomainSortableTable();
        panel2 = new JPanel();
        addItem = new JButton();
        removeItem = new JButton();
        changeItem = new JButton();
        mergeItems = new JButton();
        importItems = new JButton();
        showRecordCount = new JButton();
        label3 = new JLabel();
        label2 = new JLabel();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBorder(Borders.DLU4_BORDER);
        setOpaque(false);
        setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        setPreferredSize(new Dimension(800, 500));
        setLayout(new FormLayout(
            new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                new ColumnSpec("max(default;400px):grow")
            },
            new RowSpec[] {
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- label1 ----
        label1.setText("List Name");
        label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(label1, cc.xy(1, 1));
        add(listName, cc.xy(3, 1));

        //======== panel3 ========
        {
            panel3.setOpaque(false);
            panel3.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC
                },
                RowSpec.decodeSpecs("default")));

            //---- pairedValues ----
            pairedValues.setText("Paired values");
            pairedValues.setOpaque(false);
            pairedValues.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            pairedValues.setEnabled(false);
            pairedValues.setText(ATFieldInfo.getLabel(LookupList.class, LookupList.PROPERTYNAME_PAIRED_VALUES));
            panel3.add(pairedValues, cc.xy(1, 1));

            //---- pairedValues2 ----
            pairedValues2.setText("Restrict characters");
            pairedValues2.setOpaque(false);
            pairedValues2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            pairedValues2.setEnabled(false);
            pairedValues2.setText(ATFieldInfo.getLabel(LookupList.class, LookupList.PROPERTYNAME_RESTRICT_TO_NMTOKEN));
            panel3.add(pairedValues2, cc.xy(3, 1));
        }
        add(panel3, cc.xy(3, 3));

        //======== panel1 ========
        {
            panel1.setOpaque(false);
            panel1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            panel1.setLayout(new FormLayout(
                new ColumnSpec[] {
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC
                },
                RowSpec.decodeSpecs("default:grow")));

            //======== scrollPane1 ========
            {
                scrollPane1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                scrollPane1.setViewportView(listItems);
            }
            panel1.add(scrollPane1, cc.xywh(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

            //======== panel2 ========
            {
                panel2.setBackground(new Color(234, 201, 250));
                panel2.setOpaque(false);
                panel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                panel2.setLayout(new FormLayout(
                    ColumnSpec.decodeSpecs("default"),
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
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //---- addItem ----
                addItem.setText("Add Item");
                addItem.setBackground(new Color(234, 201, 250));
                addItem.setOpaque(false);
                addItem.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                addItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addItemActionPerformed(e);
                    }
                });
                panel2.add(addItem, cc.xy(1, 3));

                //---- removeItem ----
                removeItem.setText("Remove Item(s)");
                removeItem.setBackground(new Color(234, 201, 250));
                removeItem.setOpaque(false);
                removeItem.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                removeItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeItemActionPerformed(e);
                    }
                });
                panel2.add(removeItem, cc.xy(1, 5));

                //---- changeItem ----
                changeItem.setText("Change Item");
                changeItem.setBackground(new Color(234, 201, 250));
                changeItem.setOpaque(false);
                changeItem.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                changeItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        changeItemActionPerformed(e);
                    }
                });
                panel2.add(changeItem, cc.xy(1, 7));

                //---- mergeItems ----
                mergeItems.setText("Merge Items");
                mergeItems.setBackground(new Color(234, 201, 250));
                mergeItems.setOpaque(false);
                mergeItems.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                mergeItems.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        mergeItemsActionPerformed(e);
                    }
                });
                panel2.add(mergeItems, cc.xy(1, 9));

                //---- importItems ----
                importItems.setText("Import Items");
                importItems.setBackground(new Color(234, 201, 250));
                importItems.setOpaque(false);
                importItems.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                importItems.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        importItemsActionPerformed(e);
                    }
                });
                panel2.add(importItems, cc.xy(1, 11));

                //---- showRecordCount ----
                showRecordCount.setText("Show Record Count");
                showRecordCount.setBackground(new Color(234, 201, 250));
                showRecordCount.setOpaque(false);
                showRecordCount.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                showRecordCount.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        showRecordCountActionPerformed(e);
                    }
                });
                panel2.add(showRecordCount, cc.xy(1, 13));
            }
            panel1.add(panel2, cc.xywh(3, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));
        }
        add(panel1, cc.xywh(3, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

        //---- label3 ----
        label3.setText("Items in Red can't be modified");
        label3.setForeground(new Color(130, 0, 0));
        add(label3, cc.xy(3, 7));

        //---- label2 ----
        label2.setText("Items in Blue are AT initial values");
        label2.setForeground(new Color(45, 78, 114));
        add(label2, cc.xy(3, 9));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	private void changeItemActionPerformed(ActionEvent e) {
		final LookupListItems selectedItem = (LookupListItems) listItems.getDomainObject(listItems.getSelectedRow());
		if (selectedItem == null) {
			JOptionPane.showMessageDialog(this, "You must select an item to change.", "", JOptionPane.ERROR_MESSAGE);
		} else {
			final String oldValue = selectedItem.getListItem();
			LookupListItemsDialog dialog = new LookupListItemsDialog(getParentEditor(), lookupListModel.getPairedValues(), lookupListModel.getRestrictToNmtoken());
			dialog.setItemValue(selectedItem.getListItem());
			dialog.setCode(selectedItem.getCode());
//			final String newValue = (String)JOptionPane.showInputDialog(this, "Enter a new value", "", JOptionPane.PLAIN_MESSAGE, null, null, oldValue);
			if (dialog.showDialog() == JOptionPane.OK_OPTION) {
				boolean caseChangeOnly;
				final String newValue = dialog.getItemValue();
				final String code = dialog.getCode();
				if (oldValue.equalsIgnoreCase(newValue)) {
					caseChangeOnly = true;
				} else {
					caseChangeOnly = false;
				}
				if (!caseChangeOnly && lookupListModel.containsItem(newValue)) {
					JOptionPane.showMessageDialog(this, newValue + " already exists in the list. Please use merge.", "", JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						//first find out how many records will be changed.
						int recordsToUpdate = getCountBasedOnPropertyValue(lookupListModel, oldValue);
						if (JOptionPane.showConfirmDialog(this, "Are you sure you want to update " + recordsToUpdate +
								" record(s). This can't be undone. ", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

							Thread performer = new Thread(new Runnable() {
								public void run() {
									InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(getParentEditor(), 1000);
									monitor.start("Changing items...");

									try {
										int recordsUpdated = updateTextFieldValues(lookupListModel, oldValue, newValue, monitor);
										lookupListModel.removeListItem(selectedItem);
										lookupListModel.addListItem(new LookupListItems(lookupListModel, newValue, code));

                                        // table must be updated in EDT thread to prevent bug
                                        SwingUtilities.invokeLater(new Runnable() {
                                            public void run() {
                                                setListItems();
                                            }
                                        });
                                        //setListItems();
                                        
                                        monitor.close();
										JOptionPane.showMessageDialog(getParentEditor(), recordsUpdated + " records have been updated with the new value.", "", JOptionPane.PLAIN_MESSAGE);
									} catch (PersistenceException e1) {
										monitor.close();
										new ErrorDialog(getParentEditor(), "", e1).showDialog();
									} catch (IllegalAccessException e1) {
										monitor.close();
										new ErrorDialog(getParentEditor(), "", e1).showDialog();
									} catch (LookupException e1) {
										monitor.close();
										new ErrorDialog(getParentEditor(), "", e1).showDialog();
									} catch (InvocationTargetException e1) {
										monitor.close();
										new ErrorDialog(getParentEditor(), "", e1).showDialog();
									} catch (ValidationException e1) {
										monitor.close();
										new ErrorDialog(getParentEditor(), "", e1).showDialog();
									} catch (ConstraintViolationException e1) {
										monitor.close();
										String message = "The change operation could not be preformed. It will cause at least one record to be duplicated";
										message += "\n\nDetails:\n" + e1.getCause().getMessage();
										new ErrorDialog(message, ErrorDialog.DIALOG_TYPE_ERROR).showDialog();
									} catch (Exception e1) {
										monitor.close();
										new ErrorDialog(getParentEditor(), "", e1).showDialog();
									} finally {
										monitor.close();
									}
								}
							}, "Performer");
							performer.start();


						}
					} catch (PersistenceException e1) {
						new ErrorDialog(getParentEditor(), "There was an error updateing records", e1).showDialog();
					}
				}
			}
		}
	}

	private Object[] getParedDownSelectionList() {
		ArrayList<DomainObject> selectionList = listItems.getSelectedObjects();
		if (listType == LookupListUtils.NONEDITABLE) {
			//remove editable items as they were genereated from import
			Vector temp = new Vector();
			LookupListItems item;
			for (Object o : selectionList) {
				item = (LookupListItems) o;
				if (!item.getEditable()) {
					temp.add(item);
				}
			}
			if (temp.size() == 0) {
				JOptionPane.showMessageDialog(this, "You must select at least 1 non editable item to merge.");
				return null;
			} else {
				return temp.toArray();
			}
		} else if (listType == LookupListUtils.MIXED) {
			//check to see if there is a non-editable item
			//If so then return a list with just that Item
			LookupListItems item;
			for (Object o : selectionList) {
				item = (LookupListItems) o;
				if (!item.getEditable()) {
					Vector temp = new Vector();
					temp.add(item);
					return temp.toArray();
				}
			}
			//If we got here then just return the selection
			if (selectionList.size() < 2) {
				JOptionPane.showMessageDialog(this, "You must select at least 2 items to merge.");
				return null;
			} else {
				return selectionList.toArray();
			}
		} else {
			if (selectionList.size() < 2) {
				JOptionPane.showMessageDialog(this, "You must select at least 2 items to merge.");
				return null;
			} else {
				return selectionList.toArray();
			}
		}
	}

	private void mergeItemsActionPerformed(ActionEvent e) {
		ArrayList<DomainObject> selectionList = listItems.getSelectedObjects();
		Object[] popupValuesList = getParedDownSelectionList();
		LookupListItems item = null;
//		LookupList lookupListModel = (LookupList) this.getModel();

		if (popupValuesList != null) {
			SelectFromList dialog = new SelectFromList(this.getParentEditor(), "Select an item to merge into", getParedDownSelectionList());
			dialog.setCellRenderer(new LookupListCellRenderer(listType, true));
			try {
				for (Object o : selectionList) {
					item = (LookupListItems) o;
					item.setRecordCount(getCountBasedOnPropertyValue(lookupListModel, item.getListItem()));
				}
			} catch (PersistenceException e1) {
				new ErrorDialog(getParentEditor(), "Error get record count for item: " + item.getListItem(), e1).showDialog();
			}
//			LookupListItems mergeTarget = (LookupListItems) JOptionPane.showInputDialog(this,
//					"Select the item to merge into", "Merge",
//					JOptionPane.PLAIN_MESSAGE, null, popupValuesList, popupValuesList[0]);
			if (dialog.showDialog() == JOptionPane.OK_OPTION) {
				final LookupListItems mergeTarget = (LookupListItems) dialog.getSelectedValue();
				final String newValue = mergeTarget.getListItem();
				try {
					//first find out how many records will be changed.

					int recordsToUpdate = 0;
					for (Object o : selectionList) {
						item = (LookupListItems) o;
						if (item != mergeTarget) {
							recordsToUpdate += getCountBasedOnPropertyValue(lookupListModel, item.getListItem());
						}
					}
					if (JOptionPane.showConfirmDialog(this, "Are you sure you want to update " + recordsToUpdate +
							" record(s). This can't be undone. ", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

						Thread performer = new Thread(new Runnable() {
							public void run() {
								InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(getParentEditor(), 1000);
								monitor.start("Merging items...");
								try {
									ArrayList<DomainObject> selectionList = listItems.getSelectedObjects();
									LookupListItems item = null;
									int recordsUpdated = 0;
									StringBuffer errorBuffer = new StringBuffer();
									for (Object o : selectionList) {
										item = (LookupListItems) o;
										if (item != mergeTarget) {
											monitor.setTextLine("Old value: " + item.getListItem() + " new value: " + newValue, 1);
											try {
												recordsUpdated += updateTextFieldValues(lookupListModel, item.getListItem(), newValue, monitor);
												lookupListModel.removeListItem(item);
											}
											catch (ValidationException e) {
												errorBuffer.append("Unable to merge selected item \"").append(item.toString());
												errorBuffer.append("\" due to an invalid record being present.\n").append(e.getMessage());
											}
										}
									}

                                    // table must be updated in EDT thread to prevent bug
                                    SwingUtilities.invokeLater(new Runnable() {
                                        public void run() {
                                            setListItems();
                                        }
                                    });

                                    //setListItems();
									monitor.close();
									JOptionPane.showMessageDialog(getParentEditor(), recordsUpdated +
											" records have been merged.\n\n" + errorBuffer.toString());
								} catch (PersistenceException e1) {
									monitor.close();
									new ErrorDialog(getParentEditor(), "", e1).showDialog();
								} catch (IllegalAccessException e1) {
									monitor.close();
									new ErrorDialog(getParentEditor(), "", e1).showDialog();
								} catch (LookupException e1) {
									monitor.close();
									new ErrorDialog(getParentEditor(), "", e1).showDialog();
								} catch (InvocationTargetException e1) {
									monitor.close();
									new ErrorDialog(getParentEditor(), "", e1).showDialog();
//								} catch (ValidationException e1) {
//									monitor.close();
//									new ErrorDialog(getParentEditor(), "", e1).showDialog();
								} catch (ConstraintViolationException e1) {
									monitor.close();
									String message = "The merge operation could not be preformed. It will cause at least one record to be duplicated";
									message += "\n\nDetails:\n" + e1.getCause().getMessage();
									new ErrorDialog(getParentEditor(), message, ErrorDialog.DIALOG_TYPE_ERROR).showDialog();
								} catch (Exception e1) {
									monitor.close();
									new ErrorDialog(getParentEditor(), "", e1).showDialog();
								} finally {
									monitor.close();
								}
							}
						}, "Merge");
						performer.start();


					}
				} catch (PersistenceException e1) {
					new ErrorDialog(getParentEditor(), "There was an error updateing records", e1).showDialog();
				}
			}
		}
	}

	private int updateTextFieldValues(LookupList lookupListModel,
									  String oldValue,
									  String newValue,
									  InfiniteProgressPanel monitor) throws PersistenceException, IllegalAccessException, LookupException, InvocationTargetException, ValidationException {
		ArrayList<ATFieldInfo> fields = ATFieldInfo.getFieldInfoByLookupListName(lookupListModel.getListName());
		DomainAccessObject access;
		int recordsUpdated = 0;
		for (ATFieldInfo field : fields) {
			monitor.setTextLine("Field: " + field.getFieldLabel(), 2);
			access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(field.getClazz());
			recordsUpdated += access.updateTextField(field.getFieldName(), oldValue, newValue, monitor);
		}
		return recordsUpdated;
	}

	private int getCountBasedOnPropertyValue(LookupList lookupListModel, Object value) throws PersistenceException {
		ArrayList<ATFieldInfo> fields = ATFieldInfo.getFieldInfoByLookupListName(lookupListModel.getListName());
		DomainAccessObject access;
		int recordsUpdated = 0;
		for (ATFieldInfo field : fields) {
			access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(field.getClazz());
			recordsUpdated += access.getCountBasedOnPropertyValue(field.getFieldName(), value);
		}
		return recordsUpdated;
	}

	private void addItemActionPerformed(ActionEvent e) {
		LookupListItemsDialog dialog = new LookupListItemsDialog(getParentEditor(), lookupListModel.getPairedValues(), lookupListModel.getRestrictToNmtoken());
		if (dialog.showDialog() == JOptionPane.OK_OPTION) {
			LookupList lookupListModel = (LookupList) this.getModel();
			if (!lookupListModel.addListItem(new LookupListItems(lookupListModel, dialog.getItemValue(), dialog.getCode()))) {
				JOptionPane.showMessageDialog(getParent(), dialog.getItemValue() + " is already in the Lookup List");
			}
			setListItems();
		}


	}

	private void removeItemActionPerformed(ActionEvent e) {

//		LookupListItems selectedItem = (LookupListItems) listItems.getDomainObject(listItems.getSelectedRow());
		if (listItems.getSelectedRowCount() == 0) {
			JOptionPane.showMessageDialog(this, "You must select at least one item to remove.");
		} else {
//			String oldValue = selectedItem.getListItem();
			final LookupList lookupListModel = (LookupList) this.getModel();
			try {
				//first find out how many records will be changed.
				LookupListItems selectedItem;
				String oldValue;
				int recordsToUpdate = 0;
				final int itemCount = listItems.getSelectedRowCount();
				for (int index : listItems.getSelectedRows()) {
					selectedItem = (LookupListItems) listItems.getDomainObject(index);
					oldValue = selectedItem.getListItem();
					recordsToUpdate += getCountBasedOnPropertyValue(lookupListModel, oldValue);
				}
				if (JOptionPane.showConfirmDialog(this, "Are you sure you want to remove " + itemCount + " items affecting " + recordsToUpdate +
						" record(s). This can't be undone. ", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

					Thread performer = new Thread(new Runnable() {
						public void run() {
							InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(getParentEditor(), 1000);
							monitor.start("Calculating record counts...");
							try {
								int recordsUpdated = 0;
								int itemCountMinus = 0;
								LookupListItems selectedItem;
								String oldValue;
								StringBuffer errorBuffer = new StringBuffer();
								for (int index : listItems.getSelectedRows()) {
									selectedItem = (LookupListItems) listItems.getDomainObject(index);
									oldValue = selectedItem.getListItem();
									try {
										recordsUpdated += updateTextFieldValues(lookupListModel, oldValue, "", monitor);
										lookupListModel.removeListItem(selectedItem);
									}
									catch (ValidationException e) {
										errorBuffer.append("Unable to remove selected item \"").append(selectedItem.toString());
										errorBuffer.append("\" due to an invalid record being present.\n").append(e.getMessage());
										itemCountMinus++;
									}
								}

                                // table must be updated in EDT thread to prevent bug
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        setListItems();
                                    }
                                });
                                //setListItems();
								
                                monitor.close();
								JOptionPane.showMessageDialog(getParent(), (itemCount - itemCountMinus) + " items have been removed. " +
										recordsUpdated + " record(s) have been updated.\n\n" + errorBuffer.toString());
							} catch (PersistenceException e1) {
								monitor.close();
								new ErrorDialog(getParentEditor(), "Error get record count", e1).showDialog();
							} catch (LookupException e1) {
								monitor.close();
								new ErrorDialog("Error get record count", e1).showDialog();
							} catch (IllegalAccessException e1) {
								monitor.close();
								new ErrorDialog("Error get record count", e1).showDialog();
//							} catch (ValidationException e1) {
//								monitor.close();
//								new ErrorDialog("Error get record count", e1).showDialog();
							} catch (InvocationTargetException e1) {
								monitor.close();
								new ErrorDialog("Error get record count", e1).showDialog();
							} finally {
								monitor.close();
							}
							tableCellRenderer.setIncludeRecordCount(true);
						}
					}, "ShowRecordCount");
					performer.start();


				}
			} catch (PersistenceException e1) {
				new ErrorDialog(getParentEditor(), "There was an error updateing records", e1).showDialog();
			}
		}
//		LookupListItems selectedItem = (LookupListItems) listItems.getSelectedValue();
//		LookupList lookupListModel = (LookupList) this.getModel();
//		lookupListModel.removeListItem(selectedItem);
//		setListItems();
	}

	private void importItemsActionPerformed(ActionEvent e) {
		File file = ImportUtils.chooseFile(this, new SimpleFileFilter("txt", "Text File (*.txt)"));
		if (file != null) {
			ArrayList<String> array = ImportUtils.loadFileIntoStringArray(file);
			LookupList lookupListModel = (LookupList) this.getModel();
			StringBuffer messageBuffer = new StringBuffer();

			for (String itemValue : array) {
				if (!lookupListModel.addListItem(itemValue)) {
					messageBuffer.append(itemValue).append(" is already in the Lookup List\n");
				}
			}

			String message = messageBuffer.toString();

			if (message.length() > 0) {
				JOptionPane.showMessageDialog(getParent(), message);
			}

			setListItems();
		}
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label1;
    public JTextField listName;
    private JPanel panel3;
    public JCheckBox pairedValues;
    public JCheckBox pairedValues2;
    private JPanel panel1;
    private JScrollPane scrollPane1;
    private DomainSortableTable listItems;
    private JPanel panel2;
    private JButton addItem;
    private JButton removeItem;
    private JButton changeItem;
    private JButton mergeItems;
    private JButton importItems;
    private JButton showRecordCount;
    private JLabel label3;
    private JLabel label2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	private SelectionInList selectionInListModel = new SelectionInList();
	private int listType;
	//	private InfiniteProgressPanel progressPanel = new InfiniteProgressPanel(this, "Dummy text");
	private LookupList lookupListModel;
	LookupListTableCellRenderer tableCellRenderer;

	private void setListItems() {
		lookupListModel = (LookupList) this.getModel();
		ArrayList<LookupListItems> arrayList = new ArrayList<LookupListItems>(lookupListModel.getListItems());
		Collections.sort(arrayList);
//		try {
//			for (LookupListItems item: arrayList) {
//				item.setRecordCount(getCountBasedOnPropertyValue(lookupListModel, item.getListItem()));
//			}
//		} catch (PersistenceException e) {
//			new ErrorDialog("", e).showDialog();
//		}
//		selectionInListModel.setListModel(new ArrayListModel(arrayList));
		listType = lookupListModel.getListType();
		tableCellRenderer = new LookupListTableCellRenderer(listItems, listType);
		listItems.setCellRenderer(tableCellRenderer);
		if (listType == LookupListUtils.NONEDITABLE) {
			addItem.setEnabled(false);
			importItems.setEnabled(false);
		} else {
			addItem.setEnabled(true);
			importItems.setEnabled(true);
		}
//		if (lookupListModel.getPairedValues()) {
//			listItems = new DomainSortableTable();
//			listItems.setClazzAndColumns(LookupListItems.class, LookupListItems.PROPERTYNAME_LIST_ITEM, LookupListItems.PROPERTYNAME_CODE);
//		} else {
//			listItems = new DomainSortableTable();
//			listItems.setClazzAndColumns(LookupListItems.class, LookupListItems.PROPERTYNAME_LIST_ITEM);
//		}
		listItems.updateCollection(arrayList);
		listItems.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		mergeItems.setEnabled(false);
	}

	public final void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		setListItems();
	}

	public Component getInitialFocusComponent() {
		return listName;
	}

	class ArrayListModel extends AbstractListModel {

		private ArrayList data;

		private ArrayListModel(ArrayList data) {
			this.data = data;
		}

		/**
		 * Returns the length of the list.
		 *
		 * @return the length of the list
		 */
		public int getSize() {
			return data.size();
		}

		/**
		 * Returns the value at the specified index.
		 *
		 * @param index the requested index
		 * @return the value at <code>index</code>
		 */
		public Object getElementAt(int index) {
			if (index < 0 || index >= getSize()) {
				return null;
			} else {
				return data.get(index);
			}
		}
	}


}
