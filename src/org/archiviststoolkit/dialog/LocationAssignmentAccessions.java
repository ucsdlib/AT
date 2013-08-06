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
 * Created by JFormDesigner on Tue Aug 15 10:49:30 EDT 2006
 */

package org.archiviststoolkit.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.model.Locations;
import org.archiviststoolkit.model.Accessions;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.editor.AccessionFields;
import org.archiviststoolkit.editor.LocationEditor;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.util.LocationsUtils;
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
public class LocationAssignmentAccessions extends JDialog {

    public LocationAssignmentAccessions(Dialog owner, AccessionFields parentEditor) {
        super(owner);
        this.parentEditor = parentEditor;
        initComponents();
//		selectOnly = false;
        selectPanel.setVisible(false);
        dialogForLinking = true;
        this.getRootPane().setDefaultButton(this.linkLocationButton);
    }

    public LocationAssignmentAccessions(Dialog owner) {
        super(owner);
        initComponents();
//		selectOnly = true;
//		note.setVisible(false);
//		noteContainerLabel.setVisible(false);
        linkingPanel.setVisible(false);
        dialogForLinking = false;
        locationLookupTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.getRootPane().setDefaultButton(this.linkLocationButton);
    }

    private void subjectLookupTableMouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            addSelectedLocations();
        }
    }

    private void createLocationButtonActionPerformed(ActionEvent e) {
        Accessions accessionsModel = (Accessions) parentEditor.getModel();

        DomainEditor dialog = new LocationEditor(this);
        dialog.setButtonListeners();
        Locations instance = new Locations();
        dialog.setModel(instance, null);
        dialog.disableNavigationButtons();
        if (dialog.showDialog() == javax.swing.JOptionPane.OK_OPTION) {
            try {
                DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Locations.class);
                access.add(instance);
                if (!dialogForLinking) {
                    setSelectedLocation(instance);
                    status = javax.swing.JOptionPane.OK_OPTION;
                    this.setVisible(false);
                } else {
                    DomainObject link = accessionsModel.addLocation(instance, note.getText());
                    parentEditor.getLocationsTable().addDomainObject(link);
                    initLookup();
                    note.setText("");
                    filterField.setText("");
                }
                LocationsUtils.addLocationToLookupList(instance);

            } catch (ConstraintViolationException persistenceException) {
                JOptionPane.showMessageDialog(this, "Can't save, Duplicate record:" + instance);
            } catch (PersistenceException persistenceException) {
                if (persistenceException.getCause() instanceof ConstraintViolationException) {
                    JOptionPane.showMessageDialog(this, "Can't save, Duplicate record:" + instance);
                    return;
                }
                new ErrorDialog(
                        "Error saving new record.",
                        StringHelper.getStackTrace(persistenceException)).showDialog();
            } catch (DuplicateLinkException e1) {
                JOptionPane.showMessageDialog(this, e1.getMessage() + " is already linked to this record");
            }
        }
    }

    private void doneButtonActionPerformed(ActionEvent e) {
        status = javax.swing.JOptionPane.CANCEL_OPTION;
        this.setVisible(false);
    }

    public JButton getCreateLocationButton() {
        return createLocationButton;
    }

    public DomainSortableTable getLocationLookupTable() {
        return locationLookupTable;
    }

    public JTextField getFilterField() {
        return filterField;
    }

    public JButton getLinkLocationButton() {
        return linkLocationButton;
    }

    private void linkLocationButtonActionPerformed() {
        if (getLocationLookupTable().getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "You must select a location to link");
        } else {
            addSelectedLocations();
        }
    }

    private void selectButtonActionPerformed() {
        if (getLocationLookupTable().getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "You must select a location to link");
        } else {
            status = javax.swing.JOptionPane.OK_OPTION;
            this.setVisible(false);
        }
    }

    public JButton getSelectButton() {
        return selectButton;
    }

    private void locationLookupTableKeyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            linkLocationButtonActionPerformed();
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
        filterField = new JTextField();
        scrollPane1 = new JScrollPane();
        locationLookupTable = new DomainSortableTable(Locations.class, filterField);
        linkingPanel = new JPanel();
        label3 = new JLabel();
        label4 = new JLabel();
        noteContainerLabel = new JTextField();
        note = new JTextField();
        buttonBar = new JPanel();
        linkLocationButton = new JButton();
        createLocationButton = new JButton();
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
                        new ColumnSpec[]{
                                new ColumnSpec(Sizes.bounded(Sizes.MINIMUM, Sizes.dluX(100), Sizes.dluX(200))),
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        },
                        RowSpec.decodeSpecs("default")));

                //======== mainHeaderPanel ========
                {
                    mainHeaderPanel.setBackground(new Color(80, 69, 57));
                    mainHeaderPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    mainHeaderPanel.setLayout(new FormLayout(
                            new ColumnSpec[]{
                                    FormFactory.RELATED_GAP_COLSPEC,
                                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            },
                            new RowSpec[]{
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
                            new ColumnSpec[]{
                                    FormFactory.RELATED_GAP_COLSPEC,
                                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                            },
                            new RowSpec[]{
                                    FormFactory.RELATED_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.RELATED_GAP_ROWSPEC
                            }));

                    //---- subHeaderLabel ----
                    subHeaderLabel.setText("Location Lookup");
                    subHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
                    subHeaderLabel.setForeground(Color.white);
                    panel3.add(subHeaderLabel, cc.xy(2, 2));
                }
                HeaderPanel.add(panel3, cc.xy(2, 1));
            }
            dialogPane.add(HeaderPanel, BorderLayout.NORTH);

            //======== contentPane ========
            {
                contentPane.setBackground(new Color(231, 188, 251));
                contentPane.setOpaque(false);
                contentPane.setLayout(new FormLayout(
                        new ColumnSpec[]{
                                FormFactory.UNRELATED_GAP_COLSPEC,
                                FormFactory.DEFAULT_COLSPEC,
                                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                FormFactory.UNRELATED_GAP_COLSPEC
                        },
                        new RowSpec[]{
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
                contentPane.add(filterField, cc.xy(4, 2));

                //======== scrollPane1 ========
                {
                    scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                    //---- locationLookupTable ----
                    locationLookupTable.setPreferredScrollableViewportSize(new Dimension(450, 300));
                    locationLookupTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            subjectLookupTableMouseClicked(e);
                        }
                    });
                    locationLookupTable.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            locationLookupTableKeyTyped(e);
                        }
                    });
                    scrollPane1.setViewportView(locationLookupTable);
                }
                contentPane.add(scrollPane1, cc.xywh(2, 4, 3, 1));

                //======== linkingPanel ========
                {
                    linkingPanel.setOpaque(false);
                    linkingPanel.setLayout(new FormLayout(
                            ColumnSpec.decodeSpecs("default:grow"),
                            new RowSpec[]{
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.LINE_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.UNRELATED_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.LINE_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC,
                                    FormFactory.LINE_GAP_ROWSPEC,
                                    FormFactory.DEFAULT_ROWSPEC
                            }));

                    //---- label3 ----
                    label3.setText("Double click on a Location to add it to the record.");
                    linkingPanel.add(label3, cc.xy(1, 1));

                    //---- label4 ----
                    label4.setText("Or hit enter if a Location is highlighted.");
                    linkingPanel.add(label4, cc.xy(1, 3));

                    //---- noteContainerLabel ----
                    noteContainerLabel.setText("Note/Container information");
                    noteContainerLabel.setOpaque(false);
                    noteContainerLabel.setBorder(null);
                    linkingPanel.add(noteContainerLabel, cc.xy(1, 5));
                    linkingPanel.add(note, cc.xy(1, 7));

                    //======== buttonBar ========
                    {
                        buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                        buttonBar.setBackground(new Color(231, 188, 251));
                        buttonBar.setOpaque(false);
                        buttonBar.setLayout(new FormLayout(
                                new ColumnSpec[]{
                                        FormFactory.DEFAULT_COLSPEC,
                                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                                        FormFactory.BUTTON_COLSPEC,
                                        FormFactory.RELATED_GAP_COLSPEC,
                                        FormFactory.BUTTON_COLSPEC
                                },
                                RowSpec.decodeSpecs("pref")));

                        //---- linkLocationButton ----
                        linkLocationButton.setText("Link");
                        linkLocationButton.setOpaque(false);
                        linkLocationButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                linkLocationButtonActionPerformed();
                            }
                        });
                        buttonBar.add(linkLocationButton, cc.xy(1, 1));

                        //---- createLocationButton ----
                        createLocationButton.setText("Create Location");
                        createLocationButton.setBackground(new Color(231, 188, 251));
                        createLocationButton.setOpaque(false);
                        createLocationButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                createLocationButtonActionPerformed(e);
                            }
                        });
                        buttonBar.add(createLocationButton, cc.xy(3, 1));

                        //---- doneButton ----
                        doneButton.setText("Close Window");
                        doneButton.setBackground(new Color(231, 188, 251));
                        doneButton.setOpaque(false);
                        doneButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                doneButtonActionPerformed(e);
                            }
                        });
                        buttonBar.add(doneButton, cc.xy(5, 1));
                    }
                    linkingPanel.add(buttonBar, cc.xywh(1, 9, 1, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
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
                                new ColumnSpec[]{
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
                                doneButtonActionPerformed(e);
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
    private JTextField filterField;
    private JScrollPane scrollPane1;
    private DomainSortableTable locationLookupTable;
    private JPanel linkingPanel;
    private JLabel label3;
    private JLabel label4;
    private JTextField noteContainerLabel;
    private JTextField note;
    private JPanel buttonBar;
    private JButton linkLocationButton;
    private JButton createLocationButton;
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
    FilterList textFilteredIssues;
    EventTableModel lookupTableModel;
    private AccessionFields parentEditor;
    //	private boolean selectOnly;
    boolean dialogForLinking;
    private Locations selectedLocation;

    public void setMainHeaderByClass(Class clazz) {
        StandardEditor.setMainHeaderColorAndTextByClass(clazz, mainHeaderPanel, mainHeaderLabel);
    }

    public final int showDialog() {

        this.pack();
        initLookup();
        setLocationRelativeTo(getOwner());
        this.setVisible(true);

        return (status);
    }

    private void initLookup() {
        // update the location list from the database so that
        // any edits made will show up correctly.
        LocationsUtils.initLocationLookupList();

        SortedList sortedLocations = LocationsUtils.getLocationsGlazedList();
        textFilteredIssues = new FilterList(sortedLocations, new TextComponentMatcherEditor(filterField, new DomainFilterator()));
        lookupTableModel = new EventTableModel(textFilteredIssues, new DomainTableFormat(Locations.class));
        locationLookupTable.setModel(lookupTableModel);
        TableComparatorChooser tableSorter = new TableComparatorChooser(locationLookupTable, sortedLocations, true);
        filterField.requestFocusInWindow();
    }

    private void addSelectedLocations() {
        if (!dialogForLinking) {
            int selectedRow = locationLookupTable.getSelectedRow();
            Locations location = (Locations) lookupTableModel.getElementAt(selectedRow);
            setSelectedLocation(location);
            status = javax.swing.JOptionPane.OK_OPTION;
            this.setVisible(false);
        } else {
            try {
                int[] selectedRows = locationLookupTable.getSelectedRows();
                Locations location;
                for (int selectedRow : selectedRows) {
                    location = (Locations) lookupTableModel.getElementAt(selectedRow);
                    Accessions accessionsModel = (Accessions) parentEditor.getModel();
                    DomainObject link = accessionsModel.addLocation(location, note.getText());
                    if (link != null) {
                        parentEditor.getLocationsTable().addDomainObject(link);
                    }
                }
                //set the record to dirty
                ApplicationFrame.getInstance().setRecordDirty();
            } catch (DuplicateLinkException e) {
                JOptionPane.showMessageDialog(this, e.getMessage() + " is already linked to this record");
            }
        }
    }

    public Locations getSelectedLocation() {
        return (Locations) lookupTableModel.getElementAt(locationLookupTable.getSelectedRow());
    }

    private void setSelectedLocation(Locations selectedLocation) {
        this.selectedLocation = selectedLocation;
    }
}
