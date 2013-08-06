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
import java.util.Collection;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.exceptions.RDEPopulateException;
import org.archiviststoolkit.exceptions.RDEPanelCreationException;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.ApplicationFrame;

public class RdeDigitalInstance extends RdePanel {

	private boolean typeSticky;
    private boolean titleSticky;
	private boolean dateExpressionSticky;
	private boolean dateBeginSticky;
	private boolean dateEndSticky;
	private boolean uri1Sticky;
	private boolean useStatement1Sticky;
	private boolean actuate1Sticky;
	private boolean show1Sticky;
	private boolean uri2Sticky;
	private boolean useStatement2Sticky;
	private boolean actuate2Sticky;
	private boolean show2Sticky;

	public RdeDigitalInstance(RdePanelContainer parentPanel) {
		super(parentPanel);
		initComponents();
	}

	public void populateComponent(ResourcesComponents component) throws RDEPopulateException {
		if(addToComponent()) {
            ArchDescriptionDigitalInstances instance = new ArchDescriptionDigitalInstances(component);

            instance.setInstanceType(ArchDescriptionInstances.DIGITAL_OBJECT_INSTANCE);
            DigitalObjects digitalObject = new DigitalObjects();
            digitalObject.setRepository(ApplicationFrame.getInstance().getCurrentUserRepository());
            digitalObject.setObjectType(getObjectType());
            digitalObject.setMetsIdentifier(getDigitalObjectID());
            digitalObject.setTitle(title.getText());
            digitalObject.setDateExpression(dateExpression.getText());
            digitalObject.setDateBegin(getYearBegin());
            digitalObject.setDateEnd(getYearEnd());
            FileVersions fileVersion;
            if (getUri1().length() > 0 && getUseStatement1().length() > 0) {
                fileVersion = new FileVersions(digitalObject);
                fileVersion.setUri(getUri1());
                fileVersion.setUseStatement(getUseStatement1());
                fileVersion.setEadDaoActuate(getActuate1());
                fileVersion.setEadDaoShow(getShow1());
                digitalObject.addFileVersion(fileVersion);
            }
            if (getUri2().length() > 0 && getUseStatement2().length() > 0) {
                fileVersion = new FileVersions(digitalObject);
                fileVersion.setUri(getUri2());
                fileVersion.setUseStatement(getUseStatement2());
                fileVersion.setEadDaoActuate(getActuate2());
                fileVersion.setEadDaoShow(getShow2());
                digitalObject.addFileVersion(fileVersion);
            }

            instance.setDigitalObject(digitalObject);
            digitalObject.setDigitalInstance(instance);
            component.addInstance(instance);
        }
    }

	public void clearFields() {
		digitalObjectID.setText("");

        if (!typeSticky) {
            objectType.setSelectedIndex(0);
        }
        if (!titleSticky) {
			title.setText("");
		}
		if (!dateExpressionSticky) {
			dateExpression.setText("");
		}
		if (!dateBeginSticky) {
			yearBegin.setValue(null);
		}
		if (!dateEndSticky) {
			yearEnd.setValue(null);
		}
		if (!show1Sticky) {
			show1.setSelectedIndex(0);
		}
		if (!actuate1Sticky) {
			actuate1.setSelectedIndex(0);
		}
		if (!useStatement1Sticky) {
			useStatement1.setSelectedIndex(0);
		}
		if (!uri1Sticky) {
			uri1.setText("");
		}
		if (!show2Sticky) {
			show2.setSelectedIndex(0);
		}
		if (!actuate2Sticky) {
			actuate2.setSelectedIndex(0);
		}
		if (!useStatement2Sticky) {
			useStatement2.setSelectedIndex(0);
		}
		if (!uri2Sticky) {
			uri2.setText("");
		}
	}

	public void setStickyLabels() {
        setLabelColor(typeSticky, label_type);
        setLabelColor(titleSticky, label_title);
		setLabelColor(dateExpressionSticky, label_DateExpression);
		setLabelColor(dateBeginSticky, label_yearBegin);
		setLabelColor(dateEndSticky, label_yearEnd);

		setLabelColor(show1Sticky, label_show1);
		setLabelColor(actuate1Sticky, label_actuate1);
		setLabelColor(useStatement1Sticky, label_useStatement1);
		setLabelColor(uri1Sticky, label_uri1);

		setLabelColor(show2Sticky, label_show2);
		setLabelColor(actuate2Sticky, label_actuate2);
		setLabelColor(useStatement2Sticky, label_useStatement2);
		setLabelColor(uri2Sticky, label_uri2);
	}

	public void initializeStickyLabels(Collection<RDEScreenPanelItems> panelItems) throws RDEPanelCreationException {
		for(RDEScreenPanelItems panelItem: panelItems) {
			if(panelItem.getPropertyName().equals(DigitalObjects.PROPERTYNAME_OBJECT_TYPE)) {
                this.typeSticky = panelItem.getSticky();
            } else if (panelItem.getPropertyName().equals(DigitalObjects.PROPERTYNAME_TITLE)) {
			    this.titleSticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(DigitalObjects.PROPERTYNAME_DATE_EXPRESSION)){
				this.dateExpressionSticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(DigitalObjects.PROPERTYNAME_DATE_BEGIN)){
				this.dateEndSticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(DigitalObjects.PROPERTYNAME_DATE_END)){
				this.dateBeginSticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(FileVersions.PROPERTYNAME_EAD_DAO_SHOW + "1")){
				this.show1Sticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(FileVersions.PROPERTYNAME_FILE_VERSIONS_USE_STATEMENT + "1")){
				this.useStatement1Sticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(FileVersions.PROPERTYNAME_EAD_DAO_ACTUATE + "1")){
				this.actuate1Sticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(FileVersions.PROPERTYNAME_FILE_VERSIONS_URI + "1")){
				this.uri1Sticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(FileVersions.PROPERTYNAME_EAD_DAO_SHOW + "2")){
				this.show2Sticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(FileVersions.PROPERTYNAME_FILE_VERSIONS_USE_STATEMENT + "2")){
				this.useStatement2Sticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(FileVersions.PROPERTYNAME_EAD_DAO_ACTUATE + "2")){
				this.actuate2Sticky = panelItem.getSticky();
			} else if (panelItem.getPropertyName().equals(FileVersions.PROPERTYNAME_FILE_VERSIONS_URI + "2")){
				this.uri2Sticky = panelItem.getSticky();
			} else {
				throw new RDEPanelCreationException(panelItem.getPropertyName() + "is not supported here");
			}
		}
	}

    // Method to set the default values
    public void setDefaultValues() {
        String tableName = "DigitalObjects";
        DefaultValues defaultValue0 = getDefaultValue(tableName, "objectType");
        DefaultValues defaultValue1 = getDefaultValue(tableName, "title");
        DefaultValues defaultValue2 = getDefaultValue(tableName, "eadDaoActuate");
        DefaultValues defaultValue3 = getDefaultValue(tableName, "eadDaoShow");

        // set the default object type
        if (defaultValue0 != null && (!typeSticky || showDefaultValues)) {
            String value = defaultValue0.getTextValue();
            int size = objectType.getItemCount();

            for (int i = 1; i < size; i++) {
                Object o = objectType.getItemAt(i);
                if (o.toString().equals(value)) {
                    objectType.setSelectedIndex(i);
                    break;
                }
            }
        }

        // set the title default value
        if (defaultValue1 != null && (!titleSticky || showDefaultValues)) {
            String value = defaultValue1.getStringValue();
            title.setText(value);
        }
        
        if (defaultValue2 != null) {
            String value = defaultValue2.getStringValue();
            int size = actuate1.getItemCount();

            for (int i = 1; i < size; i++) {
                Object o = actuate1.getItemAt(i);
                if (o.toString().equals(value)) {
                    if(!actuate1Sticky || showDefaultValues) {
                        actuate1.setSelectedIndex(i);
                    }
                    if(!actuate2Sticky || showDefaultValues) {
                        actuate2.setSelectedIndex(i);
                    }
                    break;
                }
            }
        }

        if (defaultValue3 != null) {
            String value = defaultValue3.getStringValue();
            int size = show1.getItemCount();

            for (int i = 1; i < size; i++) {
                Object o = show1.getItemAt(i);
                if (o.toString().equals(value)) {
                    if(!show1Sticky || showDefaultValues) {
                        show1.setSelectedIndex(i);
                    }
                    if(!show2Sticky || showDefaultValues) {
                        show2.setSelectedIndex(i);
                    }
                    break;
                }
            }
        }

        showDefaultValues = false;
    }

    private void componentFocusGained(FocusEvent e) {
		parentPanel.componentFocusGained(e, this);
	}

	private void yearEndFocusGained(FocusEvent e) {
		parentPanel.componentFocusGained(e, this);
		if (yearEnd.getValue() == null) {
			yearEnd.setValue(yearBegin.getValue());
			/* After a formatted text field gains focus, it replaces its text with its
			 * current value, formatted appropriately of course. It does this _after_
			 * any focus listeners are notified. So, if we are editable, we queue
			 * up a selectAll to be done after the current events in the thread are done. */
			Runnable doSelect = new Runnable() {
				public void run() {
					yearEnd.selectAll();
				}
			};
			SwingUtilities.invokeLater(doSelect);
		}
	}

	private void yearBeginFocusGained(FocusEvent e) {
		parentPanel.componentFocusGained(e, this);
	}

    private void label_typeMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			typeSticky = !typeSticky;
			setStickyLabels();
		}
	}

    private void label_titleMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			titleSticky = !titleSticky;
			setStickyLabels();
		}
	}

    private void label_yearBeginMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			dateBeginSticky = !dateBeginSticky;
			setStickyLabels();
		}
	}

	private void label_yearEndMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			dateEndSticky = !dateEndSticky;
			setStickyLabels();
		}
	}

	private void label_dateExpressionMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			dateExpressionSticky = !dateExpressionSticky;
			setStickyLabels();
		}
	}

	private void label_uri1MouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			uri1Sticky = !uri1Sticky;
			setStickyLabels();
		}
	}

	private void label_useStatement1MouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			useStatement1Sticky = !useStatement1Sticky;
			setStickyLabels();
		}
	}

	private void label_actuate1MouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			actuate1Sticky = !actuate1Sticky;
			setStickyLabels();
		}
	}

	private void label_show1MouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			show1Sticky = !show1Sticky;
			setStickyLabels();
		}
	}

	private void label_uri2MouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			uri2Sticky = !uri2Sticky;
			setStickyLabels();
		}
	}

	private void label_useStatement2MouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			useStatement2Sticky = !useStatement2Sticky;
			setStickyLabels();
		}
	}

	private void label_actuate2MouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			actuate2Sticky = !actuate2Sticky;
			setStickyLabels();
		}
	}

	private void label_show2MouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.isControlDown()) {
			show2Sticky = !show2Sticky;
			setStickyLabels();
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        separator6 = new JSeparator();
        label1 = new JLabel();
        panel2 = new JPanel();
        label_type = new JLabel();
        objectType = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(DigitalObjects.class, DigitalObjects.PROPERTYNAME_OBJECT_TYPE));
        label2 = new JLabel();
        digitalObjectID = new JTextField();
        panel14 = new JPanel();
        label_title = new JLabel();
        scrollPane1 = new JScrollPane();
        title = ATBasicComponentFactory.createUnboundedTextArea();
        label_DateExpression = new JLabel();
        dateExpression = new JTextField();
        panel1 = new JPanel();
        label = new JLabel();
        label_yearBegin = new JLabel();
        yearBegin = ATBasicComponentFactory.createUnboundIntegerField(false);
        label_yearEnd = new JLabel();
        yearEnd = ATBasicComponentFactory.createUnboundIntegerField(false);
        fileVersion1 = new JPanel();
        label_fileVersion1 = new JLabel();
        panel4 = new JPanel();
        label_uri1 = new JLabel();
        uri1 = new JTextField();
        label_useStatement1 = new JLabel();
        useStatement1 = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(FileVersions.class, FileVersions.PROPERTYNAME_FILE_VERSIONS_USE_STATEMENT));
        panel5 = new JPanel();
        label_actuate1 = new JLabel();
        actuate1 = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(FileVersions.class, FileVersions.PROPERTYNAME_EAD_DAO_ACTUATE));
        label_show1 = new JLabel();
        show1 = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(FileVersions.class, FileVersions.PROPERTYNAME_EAD_DAO_SHOW));
        fileVersion2 = new JPanel();
        label_fileVersion2 = new JLabel();
        panel6 = new JPanel();
        label_uri2 = new JLabel();
        uri2 = new JTextField();
        label_useStatement2 = new JLabel();
        useStatement2 = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(FileVersions.class, FileVersions.PROPERTYNAME_FILE_VERSIONS_USE_STATEMENT));
        panel7 = new JPanel();
        label_actuate2 = new JLabel();
        actuate2 = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(FileVersions.class, FileVersions.PROPERTYNAME_EAD_DAO_ACTUATE));
        label_show2 = new JLabel();
        show2 = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(FileVersions.class, FileVersions.PROPERTYNAME_EAD_DAO_SHOW));
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBackground(new Color(200, 205, 232));
        setLayout(new FormLayout(
            new ColumnSpec[] {
                FormFactory.UNRELATED_GAP_COLSPEC,
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

        //---- separator6 ----
        separator6.setBackground(new Color(220, 220, 232));
        separator6.setForeground(new Color(147, 131, 86));
        separator6.setMinimumSize(new Dimension(1, 10));
        separator6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        add(separator6, cc.xywh(1, 1, 2, 1));

        //---- label1 ----
        label1.setText("Digital Instance");
        add(label1, cc.xywh(1, 3, 2, 1));

        //======== panel2 ========
        {
            panel2.setBackground(new Color(200, 205, 232));
            panel2.setLayout(new FormLayout(
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

            //---- label_type ----
            label_type.setText("Object Type");
            label_type.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    label_typeMouseClicked(e);
                }
            });
            panel2.add(label_type, cc.xy(1, 1));

            //---- objectType ----
            objectType.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    componentFocusGained(e);
                }
            });
            panel2.add(objectType, cc.xy(3, 1));

            //---- label2 ----
            label2.setText("Digital Object ID");
            panel2.add(label2, cc.xy(5, 1));
            panel2.add(digitalObjectID, cc.xy(7, 1));
        }
        add(panel2, cc.xy(2, 5));

        //======== panel14 ========
        {
            panel14.setOpaque(false);
            panel14.setBackground(new Color(238, 238, 238));
            panel14.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                },
                new RowSpec[] {
                    new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC
                }));

            //---- label_title ----
            label_title.setText("Title");
            label_title.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            label_title.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    label_titleMouseClicked(e);
                }
            });
            ATFieldInfo.assignLabelInfo(label_title, DigitalObjects.class, DigitalObjects.PROPERTYNAME_TITLE);
            panel14.add(label_title, cc.xy(1, 1));

            //======== scrollPane1 ========
            {

                //---- title ----
                title.setRows(4);
                title.setWrapStyleWord(true);
                title.setLineWrap(true);
                title.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        componentFocusGained(e);
                    }
                });
                scrollPane1.setViewportView(title);
            }
            panel14.add(scrollPane1, cc.xy(3, 1));

            //---- label_DateExpression ----
            label_DateExpression.setText("Date Expression");
            label_DateExpression.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            label_DateExpression.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    label_dateExpressionMouseClicked(e);
                }
            });
            ATFieldInfo.assignLabelInfo(label_DateExpression, DigitalObjects.class, DigitalObjects.PROPERTYNAME_DATE_EXPRESSION);
            panel14.add(label_DateExpression, cc.xy(1, 3));

            //---- dateExpression ----
            dateExpression.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    componentFocusGained(e);
                }
            });
            panel14.add(dateExpression, cc.xywh(3, 3, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

            //======== panel1 ========
            {
                panel1.setOpaque(false);
                panel1.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC
                    },
                    RowSpec.decodeSpecs("default")));

                //---- label ----
                label.setText("Date");
                panel1.add(label, cc.xy(1, 1));

                //---- label_yearBegin ----
                label_yearBegin.setText("Begin");
                label_yearBegin.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                label_yearBegin.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        label_yearBeginMouseClicked(e);
                    }
                });
                panel1.add(label_yearBegin, cc.xy(3, 1));

                //---- yearBegin ----
                yearBegin.setColumns(5);
                yearBegin.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        yearBeginFocusGained(e);
                    }
                });
                panel1.add(yearBegin, cc.xy(5, 1));

                //---- label_yearEnd ----
                label_yearEnd.setText("End");
                label_yearEnd.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                label_yearEnd.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        label_yearEndMouseClicked(e);
                    }
                });
                panel1.add(label_yearEnd, cc.xy(7, 1));

                //---- yearEnd ----
                yearEnd.setColumns(5);
                yearEnd.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        yearEndFocusGained(e);
                    }
                });
                panel1.add(yearEnd, cc.xy(9, 1));
            }
            panel14.add(panel1, cc.xywh(1, 5, 3, 1));
        }
        add(panel14, cc.xy(2, 7));

        //======== fileVersion1 ========
        {
            fileVersion1.setOpaque(false);
            fileVersion1.setLayout(new FormLayout(
                ColumnSpec.decodeSpecs("default:grow"),
                new RowSpec[] {
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC
                }));

            //---- label_fileVersion1 ----
            label_fileVersion1.setText("File Version 1");
            label_fileVersion1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            fileVersion1.add(label_fileVersion1, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

            //======== panel4 ========
            {
                panel4.setOpaque(false);
                panel4.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.UNRELATED_GAP_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    },
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //---- label_uri1 ----
                label_uri1.setText("URI");
                label_uri1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                label_uri1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        label_uri1MouseClicked(e);
                    }
                });
                ATFieldInfo.assignLabelInfo(label_uri1, FileVersions.class, FileVersions.PROPERTYNAME_FILE_VERSIONS_URI);
                panel4.add(label_uri1, cc.xy(3, 1));

                //---- uri1 ----
                uri1.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        componentFocusGained(e);
                    }
                });
                panel4.add(uri1, cc.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                //---- label_useStatement1 ----
                label_useStatement1.setText("Use Statement");
                label_useStatement1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                label_useStatement1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        label_useStatement1MouseClicked(e);
                    }
                });
                ATFieldInfo.assignLabelInfo(label_useStatement1, FileVersions.class, FileVersions.PROPERTYNAME_FILE_VERSIONS_USE_STATEMENT);
                panel4.add(label_useStatement1, cc.xy(3, 3));

                //---- useStatement1 ----
                useStatement1.setOpaque(false);
                useStatement1.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        componentFocusGained(e);
                    }
                });
                panel4.add(useStatement1, cc.xywh(5, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                //======== panel5 ========
                {
                    panel5.setOpaque(false);
                    panel5.setLayout(new FormLayout(
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

                    //---- label_actuate1 ----
                    label_actuate1.setText("EAD DAO Actuate");
                    label_actuate1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    label_actuate1.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            label_actuate1MouseClicked(e);
                        }
                    });
                    ATFieldInfo.assignLabelInfo(label_actuate1, FileVersions.class, FileVersions.PROPERTYNAME_EAD_DAO_ACTUATE);
                    panel5.add(label_actuate1, cc.xy(1, 1));

                    //---- actuate1 ----
                    actuate1.setOpaque(false);
                    actuate1.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            componentFocusGained(e);
                        }
                    });
                    panel5.add(actuate1, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //---- label_show1 ----
                    label_show1.setText("EAD DAO Show");
                    label_show1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    label_show1.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            label_show1MouseClicked(e);
                        }
                    });
                    ATFieldInfo.assignLabelInfo(label_show1, FileVersions.class, FileVersions.PROPERTYNAME_EAD_DAO_SHOW);
                    panel5.add(label_show1, cc.xy(5, 1));

                    //---- show1 ----
                    show1.setOpaque(false);
                    show1.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            componentFocusGained(e);
                        }
                    });
                    panel5.add(show1, cc.xywh(7, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
                }
                panel4.add(panel5, cc.xywh(3, 5, 3, 1));
            }
            fileVersion1.add(panel4, cc.xy(1, 3));
        }
        add(fileVersion1, cc.xy(2, 9));

        //======== fileVersion2 ========
        {
            fileVersion2.setOpaque(false);
            fileVersion2.setLayout(new FormLayout(
                ColumnSpec.decodeSpecs("default:grow"),
                new RowSpec[] {
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC
                }));

            //---- label_fileVersion2 ----
            label_fileVersion2.setText("File Version 2");
            label_fileVersion2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            fileVersion2.add(label_fileVersion2, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

            //======== panel6 ========
            {
                panel6.setOpaque(false);
                panel6.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.UNRELATED_GAP_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                    },
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //---- label_uri2 ----
                label_uri2.setText("URI");
                label_uri2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                label_uri2.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        label_uri2MouseClicked(e);
                    }
                });
                ATFieldInfo.assignLabelInfo(label_uri2, FileVersions.class, FileVersions.PROPERTYNAME_FILE_VERSIONS_URI);
                panel6.add(label_uri2, cc.xy(3, 1));

                //---- uri2 ----
                uri2.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        componentFocusGained(e);
                    }
                });
                panel6.add(uri2, cc.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                //---- label_useStatement2 ----
                label_useStatement2.setText("Use Statement");
                label_useStatement2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                label_useStatement2.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        label_useStatement2MouseClicked(e);
                    }
                });
                ATFieldInfo.assignLabelInfo(label_useStatement2, FileVersions.class, FileVersions.PROPERTYNAME_FILE_VERSIONS_USE_STATEMENT);
                panel6.add(label_useStatement2, cc.xy(3, 3));

                //---- useStatement2 ----
                useStatement2.setOpaque(false);
                useStatement2.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        componentFocusGained(e);
                    }
                });
                panel6.add(useStatement2, cc.xywh(5, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                //======== panel7 ========
                {
                    panel7.setOpaque(false);
                    panel7.setLayout(new FormLayout(
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

                    //---- label_actuate2 ----
                    label_actuate2.setText("EAD DAO Actuate");
                    label_actuate2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    label_actuate2.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            label_actuate2MouseClicked(e);
                        }
                    });
                    ATFieldInfo.assignLabelInfo(label_actuate2, FileVersions.class, FileVersions.PROPERTYNAME_EAD_DAO_ACTUATE);
                    panel7.add(label_actuate2, cc.xy(1, 1));

                    //---- actuate2 ----
                    actuate2.setOpaque(false);
                    actuate2.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            componentFocusGained(e);
                        }
                    });
                    panel7.add(actuate2, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                    //---- label_show2 ----
                    label_show2.setText("EAD DAO Show");
                    label_show2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    label_show2.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            label_show2MouseClicked(e);
                        }
                    });
                    ATFieldInfo.assignLabelInfo(label_show2, FileVersions.class, FileVersions.PROPERTYNAME_EAD_DAO_SHOW);
                    panel7.add(label_show2, cc.xy(5, 1));

                    //---- show2 ----
                    show2.setOpaque(false);
                    show2.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            componentFocusGained(e);
                        }
                    });
                    panel7.add(show2, cc.xywh(7, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
                }
                panel6.add(panel7, cc.xywh(3, 5, 3, 1));
            }
            fileVersion2.add(panel6, cc.xy(1, 3));
        }
        add(fileVersion2, cc.xy(2, 11));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JSeparator separator6;
    private JLabel label1;
    private JPanel panel2;
    private JLabel label_type;
    private JComboBox objectType;
    private JLabel label2;
    private JTextField digitalObjectID;
    private JPanel panel14;
    private JLabel label_title;
    private JScrollPane scrollPane1;
    private JTextArea title;
    private JLabel label_DateExpression;
    private JTextField dateExpression;
    private JPanel panel1;
    private JLabel label;
    private JLabel label_yearBegin;
    private JFormattedTextField yearBegin;
    private JLabel label_yearEnd;
    private JFormattedTextField yearEnd;
    private JPanel fileVersion1;
    private JLabel label_fileVersion1;
    private JPanel panel4;
    private JLabel label_uri1;
    private JTextField uri1;
    private JLabel label_useStatement1;
    private JComboBox useStatement1;
    private JPanel panel5;
    private JLabel label_actuate1;
    private JComboBox actuate1;
    private JLabel label_show1;
    private JComboBox show1;
    private JPanel fileVersion2;
    private JLabel label_fileVersion2;
    private JPanel panel6;
    private JLabel label_uri2;
    private JTextField uri2;
    private JLabel label_useStatement2;
    private JComboBox useStatement2;
    private JPanel panel7;
    private JLabel label_actuate2;
    private JComboBox actuate2;
    private JLabel label_show2;
    private JComboBox show2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

    // Method to check to see if to add a digital instance to resource component
    // do this by checking to see of any thing was entered in any of the fields
    private boolean addToComponent() {
        // check to is the type was selected
        if(getObjectType().trim().length() > 0) {
            return true;
        }

        if(getDigitalObjectID().trim().length() > 0) {
            return true;
        }

        if(getUseStatement1().trim().length() > 0) {
            return true;
        }

        if(getUseStatement2().trim().length() > 0) {
            return true;
        }

        if(getActuate1().trim().length() > 0) {
            return true;
        }

        if(getActuate2().trim().length() > 0) {
            return true;
        }

        if(getShow1().trim().length() > 0) {
            return true;
        }

        if(getUri1().trim().length() > 0) {
            return true;
        }

        if(getUri2().trim().length() > 0) {
            return true;
        }

        if(getYearBegin() > 0) {
            return true;
        }

        if(getYearEnd() > 0) {
            return true;
        }

        // nothing was entered so return false;
        return false;
    }

    // Method to return the digital object type
    protected String getObjectType() {
		if (objectType.getSelectedItem() == null) {
			return "";
		} else {
			return (String)objectType.getSelectedItem();
		}
	}

    /**
     * Method to return the digital object ID
     *
     * @return String which is the digital object ID which is unique
     */
    protected String getDigitalObjectID() {
        return digitalObjectID.getText();
    }

    protected String getUseStatement1() {
		if (useStatement1.getSelectedItem() == null) {
			return "";
		} else {
			return (String)useStatement1.getSelectedItem();
		}
	}

	protected String getActuate1() {
		if (actuate1.getSelectedItem() == null) {
			return "";
		} else {
			return (String)actuate1.getSelectedItem();
		}
	}

	protected String getShow1() {
		if (show1.getSelectedItem() == null) {
			return "";
		} else {
			return (String)show1.getSelectedItem();
		}
	}

	protected String getUseStatement2() {
		if (useStatement2.getSelectedItem() == null) {
			return "";
		} else {
			return (String)useStatement2.getSelectedItem();
		}
	}

	protected String getActuate2() {
		if (actuate2.getSelectedItem() == null) {
			return "";
		} else {
			return (String)actuate2.getSelectedItem();
		}
	}

	protected String getShow2() {
		if (show2.getSelectedItem() == null) {
			return "";
		} else {
			return (String)show2.getSelectedItem();
		}
	}

	protected String getUri1() {
		if (uri1.getText() == null) {
			return "";
		} else {
			return (String)uri1.getText();
		}
	}

	protected String getUri2() {
		if (uri2.getText() == null) {
			return "";
		} else {
			return (String)uri2.getText();
		}
	}

	protected Integer getYearBegin() {
		if (yearBegin.getValue() == null) {
			return 0;
		} else {
			return (Integer)yearBegin.getValue();
		}
	}

	protected Integer getYearEnd() {
		if (yearEnd.getValue() == null) {
			return 0;
		} else {
			return (Integer)yearEnd.getValue();
		}
	}
}