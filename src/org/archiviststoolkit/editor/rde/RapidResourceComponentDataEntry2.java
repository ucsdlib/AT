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
 * Created by JFormDesigner on Mon Nov 06 13:12:42 EST 2006
 */

package org.archiviststoolkit.editor.rde;

import java.awt.*;
import java.awt.event.*;
import java.util.Set;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.validation.ValidationResult;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.model.validators.ValidatorFactory;
import org.archiviststoolkit.model.validators.ATValidator;
import org.archiviststoolkit.exceptions.RDEPopulateException;
import org.archiviststoolkit.util.JGoodiesValidationUtils;

public class RapidResourceComponentDataEntry2 extends JDialog {
    private ResourcesComponents resourceComponent = null;

    public RapidResourceComponentDataEntry2(final Dialog owner, Resources resource, RdePanelContainer panels) {
        super(owner);
        this.resource = resource;
        initComponents();
        this.panels = panels;
        panels.setResource(resource);
        this.setContentPanel(this.panels);
        WindowAdapter windowAdapter = new WindowAdapter() {
            public void windowClosing(final WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(owner, "Clicking the close box is the same as clicking cancel. All changes will not be saved.\n" +
                        "Are you sure you wish to do this?", "Confirm cancel", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    cancelButtonActionPerformed();
                }
            }
        };
        addWindowListener(windowAdapter);
        this.getRootPane().setDefaultButton(this.okAndAnotherButton);
    }

    public void setContentPanel(JPanel panel) {
//        FormLayout formLayout = (FormLayout) contentPanel.getLayout();
//        CellConstraints cc = formLayout.getConstraints(temp);
//        contentPanel.remove(temp);
//        contentPanel.add(panel, cc);

		dialogPane.remove(contentPanel);
		CellConstraints cc = new CellConstraints();
		dialogPane.add(panel, cc.xy(1, 2));

	}

    private void okButtonActionPerformed() {
        if (validateData()) {
            status = JOptionPane.OK_OPTION;
            this.setVisible(false);
        }
    }

    private void cancelButtonActionPerformed() {
        status = javax.swing.JOptionPane.CANCEL_OPTION;
        this.setVisible(false);
    }

    public JButton getOkAndAnotherButton() {
        return okAndAnotherButton;
    }

    private void okAndAnotherButtonActionPerformed() {
        if (validateData()) {
            status = StandardEditor.OK_AND_ANOTHER_OPTION;
            this.setVisible(false);
        }
    }

    private void initComponents() {
                           // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
                           // Generated using JFormDesigner non-commercial license
                           dialogPane = new JPanel();
                           HeaderPanel = new JPanel();
                           panel2 = new JPanel();
                           mainHeaderLabel = new JLabel();
                           panel3 = new JPanel();
                           subHeaderLabel = new JLabel();
                           contentPanel = new JPanel();
                           temp = new JButton();
                           panel1 = new JPanel();
                           separator2 = new JSeparator();
                           buttonPanel = new JPanel();
                           autoSaveCheckBox = new JCheckBox();
                           cancelButton = new JButton();
                           okButton = new JButton();
                           okAndAnotherButton = new JButton();
                           cancelButtonLabel = new JLabel();
                           okButtonLabel = new JLabel();
                           okAndAnotherButtonLabel = new JLabel();
                           CellConstraints cc = new CellConstraints();

                           //======== this ========
                           setBackground(new Color(200, 205, 232));
                           setModal(true);
                           Container contentPane = getContentPane();
                           contentPane.setLayout(new BorderLayout());

                           //======== dialogPane ========
                           {
                               dialogPane.setBorder(null);
                               dialogPane.setBackground(new Color(200, 205, 232));
                               dialogPane.setLayout(new FormLayout(
                                   "default:grow",
                                   "fill:default, fill:default:grow, fill:default"));

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

                                   //======== panel2 ========
                                   {
                                       panel2.setBackground(new Color(73, 43, 104));
                                       panel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                       panel2.setLayout(new FormLayout(
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
                                       mainHeaderLabel.setText("Resources");
                                       mainHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
                                       mainHeaderLabel.setForeground(Color.white);
                                       panel2.add(mainHeaderLabel, cc.xy(2, 2));
                                   }
                                   HeaderPanel.add(panel2, cc.xy(1, 1));

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
                                       subHeaderLabel.setText("Rapid Data Entry");
                                       subHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
                                       subHeaderLabel.setForeground(Color.white);
                                       panel3.add(subHeaderLabel, cc.xy(2, 2));
                                   }
                                   HeaderPanel.add(panel3, cc.xy(2, 1));
                               }
                               dialogPane.add(HeaderPanel, cc.xy(1, 1));

                               //======== contentPanel ========
                               {
                                   contentPanel.setOpaque(false);
                                   contentPanel.setBorder(Borders.DLU4_BORDER);
                                   contentPanel.setMaximumSize(new Dimension(1000, 600));
                                   contentPanel.setBackground(new Color(200, 205, 232));
                                   contentPanel.setLayout(new FormLayout(
                                       "center:default:grow",
                                       "top:default:grow"));

                                   //---- temp ----
                                   temp.setText("text");
                                   temp.setBackground(new Color(200, 205, 232));
                                   contentPanel.add(temp, cc.xy(1, 1));
                               }
                               dialogPane.add(contentPanel, cc.xy(1, 2));

                               //======== panel1 ========
                               {
                                   panel1.setOpaque(false);
                                   panel1.setBorder(Borders.DLU2_BORDER);
                                   panel1.setLayout(new FormLayout(
                                       "default:grow",
                                       "default, default:grow"));

                                   //---- separator2 ----
                                   separator2.setBackground(new Color(220, 220, 232));
                                   separator2.setForeground(new Color(147, 131, 86));
                                   separator2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                   panel1.add(separator2, cc.xy(1, 1));

                                   //======== buttonPanel ========
                                   {
                                       buttonPanel.setBorder(null);
                                       buttonPanel.setBackground(new Color(200, 205, 232));
                                       buttonPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                       buttonPanel.setLayout(new FormLayout(
                                           new ColumnSpec[] {
                                               new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                                               FormFactory.UNRELATED_GAP_COLSPEC,
                                               FormFactory.DEFAULT_COLSPEC,
                                               FormFactory.UNRELATED_GAP_COLSPEC,
                                               FormFactory.DEFAULT_COLSPEC,
                                               FormFactory.UNRELATED_GAP_COLSPEC,
                                               FormFactory.DEFAULT_COLSPEC
                                           },
                                           RowSpec.decodeSpecs("default, default")));

                                       //---- autoSaveCheckBox ----
                                       autoSaveCheckBox.setText("Auto Save");
                                       autoSaveCheckBox.setSelected(true);
                                       autoSaveCheckBox.setOpaque(false);
                                       buttonPanel.add(autoSaveCheckBox, cc.xy(1, 1));

                                       //---- cancelButton ----
                                       cancelButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/cancel.jpg")));
                                       cancelButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                       cancelButton.setOpaque(false);
                                       cancelButton.addActionListener(new ActionListener() {
                                           public void actionPerformed(ActionEvent e) {
                                               cancelButtonActionPerformed();
                                           }
                                       });
                                       buttonPanel.add(cancelButton, cc.xy(3, 1));

                                       //---- okButton ----
                                       okButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/ok.jpg")));
                                       okButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                       okButton.setOpaque(false);
                                       okButton.addActionListener(new ActionListener() {
                                           public void actionPerformed(ActionEvent e) {
                                               okButtonActionPerformed();
                                           }
                                       });
                                       buttonPanel.add(okButton, cc.xy(5, 1));

                                       //---- okAndAnotherButton ----
                                       okAndAnotherButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/okPlus1.jpg")));
                                       okAndAnotherButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                       okAndAnotherButton.setOpaque(false);
                                       okAndAnotherButton.addActionListener(new ActionListener() {
                                           public void actionPerformed(ActionEvent e) {
                                               okAndAnotherButtonActionPerformed();
                                           }
                                       });
                                       buttonPanel.add(okAndAnotherButton, cc.xy(7, 1));

                                       //---- cancelButtonLabel ----
                                       cancelButtonLabel.setText("Cancel");
                                       cancelButtonLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                       buttonPanel.add(cancelButtonLabel, cc.xywh(3, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

                                       //---- okButtonLabel ----
                                       okButtonLabel.setText("OK");
                                       okButtonLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                       buttonPanel.add(okButtonLabel, cc.xywh(5, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

                                       //---- okAndAnotherButtonLabel ----
                                       okAndAnotherButtonLabel.setText("+ 1");
                                       okAndAnotherButtonLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                                       buttonPanel.add(okAndAnotherButtonLabel, cc.xywh(7, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
                                   }
                                   panel1.add(buttonPanel, cc.xywh(1, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
                               }
                               dialogPane.add(panel1, cc.xy(1, 3));
                           }
                           contentPane.add(dialogPane, BorderLayout.CENTER);
                           setSize(1000, 840);
                           setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel HeaderPanel;
    private JPanel panel2;
    private JLabel mainHeaderLabel;
    private JPanel panel3;
    private JLabel subHeaderLabel;
    private JPanel contentPanel;
    private JButton temp;
    private JPanel panel1;
    private JSeparator separator2;
    private JPanel buttonPanel;
    private JCheckBox autoSaveCheckBox;
    protected JButton cancelButton;
    protected JButton okButton;
    protected JButton okAndAnotherButton;
    private JLabel cancelButtonLabel;
    private JLabel okButtonLabel;
    private JLabel okAndAnotherButtonLabel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    /**
     * The status of the editor.
     */
    protected int status = 0;

    private RdePanelContainer panels;

    private Resources resource;

    /**
     * Displays the dialog box representing the editor.
     *
     * @return true if it displayed okay
     */

    public final int showDialog() {

        this.pack();
        panels.clearFields();
        panels.setStickyLabels();
        panels.setDefaultValues();
        setLocationRelativeTo(this.getOwner());
        this.setVisible(true);
        return (status);
    }


    private boolean validateData() {
        // generate resource components
        try {
            resourceComponent = generateComponentRecord();
            ATValidator validator = null;

            // validate any instances
            Set<ArchDescriptionInstances> instances = resourceComponent.getInstances();
            for (ArchDescriptionInstances adi : instances) {
                if(adi instanceof ArchDescriptionAnalogInstances) {
                    validator = ValidatorFactory.getInstance().getValidator((ArchDescriptionAnalogInstances)adi);
                } else if(adi instanceof ArchDescriptionDigitalInstances) {
                    DigitalObjects digitalObject = ((ArchDescriptionDigitalInstances)adi).getDigitalObject();
                    validator = ValidatorFactory.getInstance().getValidator(digitalObject);
                } else {
                    continue; // should never get here
                }

                ValidationResult validationResult = validator.validate();
                if (validationResult.hasErrors()) {
                    JGoodiesValidationUtils.showValidationMessage(
                            this,
                            "To save the record, please fix the following errors:",
                            validationResult);
                    return false;
                }

                if (validationResult.hasWarnings()) {
                    JGoodiesValidationUtils.showValidationMessage(
                            this,
                            "Note: some fields are invalid.",
                            validationResult);
                }
            }

            // validate any repeating data
            Set<ArchDescriptionRepeatingData> repeatingData = resourceComponent.getRepeatingData();
            for (ArchDescriptionRepeatingData adrd : repeatingData) {
                if(adrd instanceof ArchDescriptionNotes) {
                    validator = ValidatorFactory.getInstance().getValidator((ArchDescriptionNotes)adrd);
                }
                else {
                    continue; // only validating ArchDescription Notes for now
                }

                ValidationResult validationResult = validator.validate();
                if (validationResult.hasErrors()) {
                    JGoodiesValidationUtils.showValidationMessage(
                            this,
                            "To save the record, please fix the following errors:",
                            validationResult);
                    return false;
                }

                if (validationResult.hasWarnings()) {
                    JGoodiesValidationUtils.showValidationMessage(
                            this,
                            "Note: some fields are invalid.",
                            validationResult);
                }
            }

            // get resource component validator
            validator = ValidatorFactory.getInstance().getValidator(resourceComponent);

            ValidationResult validationResult = validator.validate();
            if (validationResult.hasErrors()) {
                JGoodiesValidationUtils.showValidationMessage(
                        this,
                        "To save the record, please fix the following errors:",
                        validationResult);
                return false;
            }

            if (validationResult.hasWarnings()) {
                JGoodiesValidationUtils.showValidationMessage(
                        this,
                        "Note: some fields are invalid.",
                        validationResult);
            }
        } catch (RDEPopulateException e) {
            resourceComponent = null;
            return false;
        }

        return true;
    }

    private ResourcesComponents generateComponentRecord() throws RDEPopulateException {
        ResourcesComponents component = new ResourcesComponents();
        panels.populateComponent(component);
        return component;

    }

    // Method to return the resource component
    public ResourcesComponents getComponentRecord() throws RDEPopulateException {
        if (resourceComponent != null) {
            return resourceComponent;
        } else {
            return generateComponentRecord();
        }
    }

    // Method to set the resource component, not used for now
    public void setComponentRecord(ResourcesComponents resourceComponent) {
        this.resourceComponent = resourceComponent;
    }

    public boolean autoSaveRecord() {
        return autoSaveCheckBox.isSelected();
    }
}
