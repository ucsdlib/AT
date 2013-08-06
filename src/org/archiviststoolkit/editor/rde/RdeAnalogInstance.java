/*
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
 * Created by JFormDesigner on Fri Jul 18 10:57:13 EDT 2008
 */

package org.archiviststoolkit.editor.rde;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Collection;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.model.ArchDescriptionAnalogInstances;
import org.archiviststoolkit.model.ArchDescriptionInstances;
import org.archiviststoolkit.model.RDEScreenPanelItems;
import org.archiviststoolkit.exceptions.RDEPopulateException;
import org.archiviststoolkit.exceptions.RDEPanelCreationException;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.swing.ATBasicComponentFactory;

public class RdeAnalogInstance extends RdePanel {

	private boolean instanceTypeSticky;
	private boolean container1TypeSticky;
	private boolean container1IndicatorSticky;
    private boolean barcodeSticky;
	private boolean container2IndicatorSticky;
    private boolean container2TypeSticky;
	private boolean container3IndicatorSticky;
    private boolean container3TypeSticky;

    // store the number of containers, and gui components, and stick values in array. This are used
    // for setting default values
    private static final int CONTAINER_COUNT = 3;
    private JComboBox[] containerTypes;
    private JTextField[] containerIndicators;
    private boolean[] typesSticky;
    private boolean[] indicatorSticky;

    public RdeAnalogInstance(RdePanelContainer parentPanel) {
		super(parentPanel);
		initComponents();
		setInstanceTypeList();
    }

	private void setInstanceTypeList() {
		Vector<String> values = LookupListUtils.getLookupListValues(ArchDescriptionInstances.class, ArchDescriptionInstances.PROPERTYNAME_INSTANCE_TYPE);
		values.remove("Digital object");
		instanceType.setModel(new DefaultComboBoxModel(values));
	}

	public void populateComponent(ResourcesComponents component) throws RDEPopulateException {
		if (addToComponent()) {
			ArchDescriptionAnalogInstances instance = new ArchDescriptionAnalogInstances(component);
			instance.setInstanceType(getInstanceType());
			instance.setContainer1Type(getContainer1Type());
			instance.setContainer2Type(getContainer2Type());
			instance.setContainer3Type(getContainer3Type());

            instance.setContainer1Indicator(getContainer1IndicatorValue());
            instance.setContainer2Indicator(getContainer2IndicatorValue());
            instance.setContainer3Indicator(getContainer3IndicatorValue());

            instance.setBarcode(getBarcode());
            
            component.addInstance(instance);
		}
	}

    public void clearFields() {
		if (!instanceTypeSticky) {
			instanceType.setSelectedIndex(0);
		}
		if (!container1TypeSticky) {
			container1Type.setSelectedIndex(0);
		}
		if (!container1IndicatorSticky) {
			container1Indicator.setText("");
		}
		if (!container2TypeSticky) {
			container2Type.setSelectedIndex(0);
		}
		if (!container2IndicatorSticky) {
			container2Indicator.setText("");
		}
		if (!container3TypeSticky) {
			container3Type.setSelectedIndex(0);
		}
		if (!container3IndicatorSticky) {
			container3Indicator.setText("");
		}
		if (!barcodeSticky) {
			barcode.setText("");
		}
	}

	public void setStickyLabels() {
		setLabelColor(instanceTypeSticky, label_instanceType);
		setLabelColor(container1TypeSticky, label_container1Type);
		setLabelColor(container1IndicatorSticky, label_container1Indicator);
		setLabelColor(container2TypeSticky, label_container2Type);
		setLabelColor(container2IndicatorSticky, label_container2Indicator);
		setLabelColor(container3TypeSticky, label_container3Type);
		setLabelColor(container3IndicatorSticky, label_container3Indicator);
		setLabelColor(barcodeSticky, label_barcode);
	}

	public void initializeStickyLabels(Collection<RDEScreenPanelItems> panelItems) throws RDEPanelCreationException {
		for(RDEScreenPanelItems panelItem: panelItems) {
			if (panelItem.getPropertyName().equals(ArchDescriptionAnalogInstances.PROPERTYNAME_INSTANCE_TYPE)) {
				this.instanceTypeSticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_TYPE)){
				this.container1TypeSticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_INDICATOR)){
				this.container1IndicatorSticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_TYPE)){
				this.container2TypeSticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_INDICATOR)){
				this.container2IndicatorSticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER3_TYPE)){
				this.container3TypeSticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER3_INDICATOR)){
				this.container3IndicatorSticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(ArchDescriptionAnalogInstances.PROPERTYNAME_BARCODE)){
				this.barcodeSticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().indexOf("Indicator") != -1) {
                // do nothing here. this is just to prevent error from being thrown
            } else {
				throw new RDEPanelCreationException(panelItem.getPropertyName() + "is not supported here");
			}
		}
	}

    // Method to set the default values
    public void setDefaultValues() {
        initComponentArrays();
        String tableName = "ArchDescriptionAnalogInstances";

        for(int i = 1; i <= CONTAINER_COUNT; i++) {
            String fieldName1 = "container" + i + "Type";
            String fieldName2 = "container" + i + "Indicator";

            DefaultValues defaultValue1 = getDefaultValue(tableName, fieldName1);
            DefaultValues defaultValue2 = getDefaultValue(tableName, fieldName2);

            // set the default value for container types
            if (defaultValue1 != null && (!typesSticky[i-1] || showDefaultValues)) {
                String value = defaultValue1.getStringValue();
                int size = containerTypes[i-1].getItemCount();

                for (int k = 1; k < size; k++) {
                    Object o = containerTypes[i-1].getItemAt(k);
                    if (o.toString().equals(value)) {
                        containerTypes[i-1].setSelectedIndex(k);
                        break;
                    }
                }
            }

            // set the default values for container numeric indicators
            if (defaultValue2 != null && (!indicatorSticky[i-1] || showDefaultValues)) {
                String value = defaultValue2.getStringValue();
                containerIndicators[i-1].setText(value);
            }

            showDefaultValues = false;
        }
    }

    private void label_instanceTypeMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			instanceTypeSticky = !instanceTypeSticky;
			setStickyLabels();
		}
	}

	private void label_container1TypeMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			container1TypeSticky = !container1TypeSticky;
			setStickyLabels();
		}
	}

	private void label_container1AlphaMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			container1IndicatorSticky = !container1IndicatorSticky;
			setStickyLabels();
		}
	}

	private void label_barcodeMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			barcodeSticky = !barcodeSticky;
			setStickyLabels();
		}
	}

	private void label_container3AlphaMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			container3IndicatorSticky = !container3IndicatorSticky;
			setStickyLabels();
		}
	}

	private void label_container3TypeMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			container3TypeSticky = !container3TypeSticky;
			setStickyLabels();
		}
	}

	private void componentFocusGained(FocusEvent e) {
		parentPanel.componentFocusGained(e, this);		
	}

	private void label_container2TypeMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			container2TypeSticky = !container2TypeSticky;
			setStickyLabels();
		}
	}

	private void label_container2AlphaMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			container2IndicatorSticky = !container2IndicatorSticky;
			setStickyLabels();
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        separator6 = new JSeparator();
        panel12 = new JPanel();
        label_instanceType = new JLabel();
        instanceType = new JComboBox();
        panel7 = new JPanel();
        label_container1Type = new JLabel();
        container1Type = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_TYPE));
        label_container1Indicator = new JLabel();
        container1Indicator = new JTextField();
        panel8 = new JPanel();
        label_container2Type = new JLabel();
        container2Type = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_TYPE));
        label_container2Indicator = new JLabel();
        container2Indicator = new JTextField();
        panel9 = new JPanel();
        label_container3Type = new JLabel();
        container3Type = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER3_TYPE));
        label_container3Indicator = new JLabel();
        container3Indicator = new JTextField();
        panel14 = new JPanel();
        label_barcode = new JLabel();
        barcode = new JTextField();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBackground(new Color(200, 205, 232));
        setLayout(new FormLayout(
            ColumnSpec.decodeSpecs("default:grow"),
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

        //---- separator6 ----
        separator6.setBackground(new Color(220, 220, 232));
        separator6.setForeground(new Color(147, 131, 86));
        separator6.setMinimumSize(new Dimension(1, 10));
        separator6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(separator6, cc.xy(1, 1));

        //======== panel12 ========
        {
            panel12.setOpaque(false);
            panel12.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC
                },
                RowSpec.decodeSpecs("default")));

            //---- label_instanceType ----
            label_instanceType.setText("Instance Type");
            label_instanceType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            label_instanceType.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    label_instanceTypeMouseClicked(e);
                }
            });
            ATFieldInfo.assignLabelInfo(label_instanceType, ArchDescriptionInstances.class, ArchDescriptionInstances.PROPERTYNAME_INSTANCE_TYPE);
            panel12.add(label_instanceType, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

            //---- instanceType ----
            instanceType.setOpaque(false);
            instanceType.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    componentFocusGained(e);
                }
            });
            panel12.add(instanceType, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
        }
        add(panel12, cc.xy(1, 3));

        //======== panel7 ========
        {
            panel7.setOpaque(false);
            panel7.setPreferredSize(new Dimension(900, 27));
            panel7.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                },
                RowSpec.decodeSpecs("default")));

            //---- label_container1Type ----
            label_container1Type.setText("Container 1 Type");
            label_container1Type.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            label_container1Type.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    label_container1TypeMouseClicked(e);
                }
            });
            ATFieldInfo.assignLabelInfo(label_container1Type, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_TYPE);
            panel7.add(label_container1Type, cc.xy(1, 1));

            //---- container1Type ----
            container1Type.setOpaque(false);
            container1Type.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    componentFocusGained(e);
                }
            });
            panel7.add(container1Type, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

            //---- label_container1Indicator ----
            label_container1Indicator.setText("Container 1 Indicator");
            label_container1Indicator.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            label_container1Indicator.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    label_container1AlphaMouseClicked(e);
                }
            });
            ATFieldInfo.assignLabelInfo(label_container1Indicator, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER1_INDICATOR);
            panel7.add(label_container1Indicator, cc.xy(5, 1));

            //---- container1Indicator ----
            container1Indicator.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    componentFocusGained(e);
                }
            });
            panel7.add(container1Indicator, cc.xywh(7, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        }
        add(panel7, cc.xywh(1, 5, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

        //======== panel8 ========
        {
            panel8.setOpaque(false);
            panel8.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                },
                RowSpec.decodeSpecs("default")));

            //---- label_container2Type ----
            label_container2Type.setText("Container 2 Type");
            label_container2Type.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            label_container2Type.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    label_container2TypeMouseClicked(e);
                }
            });
            ATFieldInfo.assignLabelInfo(label_container2Type, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_TYPE);
            panel8.add(label_container2Type, cc.xy(1, 1));

            //---- container2Type ----
            container2Type.setOpaque(false);
            container2Type.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    componentFocusGained(e);
                }
            });
            panel8.add(container2Type, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

            //---- label_container2Indicator ----
            label_container2Indicator.setText("Container 2 Indicator");
            label_container2Indicator.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            label_container2Indicator.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    label_container2AlphaMouseClicked(e);
                }
            });
            ATFieldInfo.assignLabelInfo(label_container2Indicator, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER2_INDICATOR);
            panel8.add(label_container2Indicator, cc.xy(5, 1));

            //---- container2Indicator ----
            container2Indicator.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    componentFocusGained(e);
                }
            });
            panel8.add(container2Indicator, cc.xywh(7, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        }
        add(panel8, cc.xy(1, 7));

        //======== panel9 ========
        {
            panel9.setOpaque(false);
            panel9.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                },
                RowSpec.decodeSpecs("default")));

            //---- label_container3Type ----
            label_container3Type.setText("Container 3 Type");
            label_container3Type.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            label_container3Type.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    label_container3TypeMouseClicked(e);
                }
            });
            ATFieldInfo.assignLabelInfo(label_container3Type, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER3_TYPE);
            panel9.add(label_container3Type, cc.xy(1, 1));

            //---- container3Type ----
            container3Type.setOpaque(false);
            container3Type.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    componentFocusGained(e);
                }
            });
            panel9.add(container3Type, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

            //---- label_container3Indicator ----
            label_container3Indicator.setText("Container 3 Indicator");
            label_container3Indicator.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            label_container3Indicator.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    label_container3AlphaMouseClicked(e);
                }
            });
            ATFieldInfo.assignLabelInfo(label_container3Indicator, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_CONTAINER3_INDICATOR);
            panel9.add(label_container3Indicator, cc.xy(5, 1));

            //---- container3Indicator ----
            container3Indicator.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    componentFocusGained(e);
                }
            });
            panel9.add(container3Indicator, cc.xywh(7, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        }
        add(panel9, cc.xy(1, 9));

        //======== panel14 ========
        {
            panel14.setOpaque(false);
            panel14.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                },
                RowSpec.decodeSpecs("default")));

            //---- label_barcode ----
            label_barcode.setText("Barcode");
            label_barcode.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            label_barcode.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    label_barcodeMouseClicked(e);
                }
            });
            ATFieldInfo.assignLabelInfo(label_barcode, ArchDescriptionAnalogInstances.class, ArchDescriptionAnalogInstances.PROPERTYNAME_BARCODE);
            panel14.add(label_barcode, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

            //---- barcode ----
            barcode.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    componentFocusGained(e);
                }
            });
            panel14.add(barcode, cc.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        }
        add(panel14, cc.xy(1, 11));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JSeparator separator6;
    private JPanel panel12;
    private JLabel label_instanceType;
    private JComboBox instanceType;
    private JPanel panel7;
    private JLabel label_container1Type;
    private JComboBox container1Type;
    private JLabel label_container1Indicator;
    private JTextField container1Indicator;
    private JPanel panel8;
    private JLabel label_container2Type;
    private JComboBox container2Type;
    private JLabel label_container2Indicator;
    private JTextField container2Indicator;
    private JPanel panel9;
    private JLabel label_container3Type;
    private JComboBox container3Type;
    private JLabel label_container3Indicator;
    private JTextField container3Indicator;
    private JPanel panel14;
    private JLabel label_barcode;
    private JTextField barcode;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

    // Method to add the components to the appropriate array for use in setting default values
    private void initComponentArrays() {
        containerTypes = new JComboBox[CONTAINER_COUNT];
        containerIndicators = new JTextField[CONTAINER_COUNT];
        typesSticky = new boolean[CONTAINER_COUNT];
        indicatorSticky = new boolean[CONTAINER_COUNT];

        containerTypes[0] = container1Type;
        containerTypes[1] = container2Type;
        containerTypes[2] = container3Type;

        typesSticky[0] = container1TypeSticky;
        typesSticky[1] = container2TypeSticky;
        typesSticky[2] = container3TypeSticky;

        containerIndicators[0] = container1Indicator;
        containerIndicators[1] = container2Indicator;
        containerIndicators[2] = container3Indicator;

        indicatorSticky[0] = container1IndicatorSticky;
        indicatorSticky[1] = container2IndicatorSticky;
        indicatorSticky[2] = container3IndicatorSticky;
    }

    // Method to check to see if to add an analog instance to resource component
    // do this by checking to see of any thing was entered in any of the fields
    private boolean addToComponent() {
        // check to is the type was selected
        if(getInstanceType().trim().length() > 0) {
            return true;
        }

        // check to see if bar code was entered
        if(getBarcode().trim().length() > 0) {
            return true;
        }

        // now check the containers
        for(int i = 0; i < CONTAINER_COUNT; i++) {
            if(getContainerType(containerTypes[i]).trim().length() > 0) {
                return true;
            }

            if(containerIndicators[i].getText().trim().length() > 0) {
                return true;
            }
        }

        // nothing was entered so return false;
        return false;
    }

    private String getContainerType(JComboBox containerType) {
        if (containerType.getSelectedItem() == null) {
			return "";
		} else {
			return (String)containerType.getSelectedItem();
		}
    }

    protected String getContainer1Type() {
		if (container1Type.getSelectedItem() == null) {
			return "";
		} else {
			return (String)container1Type.getSelectedItem();
		}
	}

	protected String getContainer2Type() {
		if (container2Type.getSelectedItem() == null) {
			return "";
		} else {
			return (String)container2Type.getSelectedItem();
		}
	}

	protected String getContainer3Type() {
		if (container3Type.getSelectedItem() == null) {
			return "";
		} else {
			return (String)container3Type.getSelectedItem();
		}
	}

	protected String getContainer1IndicatorValue() {
		if (container1Indicator.getText() == null) {
			return "";
		} else {
			return container1Indicator.getText();
		}
	}

	protected String getContainer2IndicatorValue() {
		if (container2Indicator.getText() == null) {
			return "";
		} else {
			return container2Indicator.getText();
		}
	}

	protected String getContainer3IndicatorValue() {
		if (container3Indicator.getText() == null) {
			return "";
		} else {
			return container3Indicator.getText();
		}
	}

	protected String getInstanceType() {
		if (instanceType.getSelectedItem() == null) {
			return "";
		} else {
			return (String)instanceType.getSelectedItem();
		}
	}

	protected String getBarcode() {
		if (barcode.getText() == null) {
			return "";
		} else {
			return barcode.getText();
		}
	}
}