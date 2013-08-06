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
 * @author Lee Mandell
 * Created by JFormDesigner on Sun Sep 03 16:33:28 EDT 2006
 */

package org.archiviststoolkit.structure;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.util.ResourceUtils;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.StringHelper;

public class InLineTagEditor extends JDialog {

	public InLineTagEditor(InLineTags tag) {
		initComponents();
		initTag(tag);
	}

	private void okButtonActionPerformed() {
		status = JOptionPane.OK_OPTION;
		this.setVisible(false);
	}

	private void cancelButtonActionPerformed() {
		status = JOptionPane.CANCEL_OPTION;
		this.setVisible(false);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		tagName = new JLabel();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setModal(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG_BORDER);
			dialogPane.setBackground(new Color(200, 205, 232));
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setOpaque(false);
				contentPanel.setLayout(new FormLayout(
						new ColumnSpec[]{
								FormFactory.DEFAULT_COLSPEC,
								FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
								new ColumnSpec("max(default;400px):grow")
						},
						RowSpec.decodeSpecs("default")));

				//---- label1 ----
				label1.setText("Tag Name");
				contentPanel.add(label1, cc.xy(1, 1));

				//---- tagName ----
				tagName.setText("text");
				contentPanel.add(tagName, cc.xy(3, 1));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setOpaque(false);
				buttonBar.setLayout(new FormLayout(
						new ColumnSpec[]{
								FormFactory.GLUE_COLSPEC,
								FormFactory.BUTTON_COLSPEC,
								FormFactory.RELATED_GAP_COLSPEC,
								FormFactory.BUTTON_COLSPEC
						},
						RowSpec.decodeSpecs("pref")));

				//---- okButton ----
				okButton.setText("OK");
				okButton.setOpaque(false);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						okButtonActionPerformed();
					}
				});
				buttonBar.add(okButton, cc.xy(2, 1));

				//---- cancelButton ----
				cancelButton.setText("Cancel");
				cancelButton.setOpaque(false);
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelButtonActionPerformed();
					}
				});
				buttonBar.add(cancelButton, cc.xy(4, 1));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label1;
	private JLabel tagName;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	private ArrayList<AttributeFieldPair> attributes = new ArrayList<AttributeFieldPair>();
	protected int status = 0;

	private void initTag(InLineTags tag) {
		tagName.setText(tag.getTagName());
		FormLayout layout = (FormLayout) contentPanel.getLayout();
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.append("Tag Name", new JLabel(tag.getTagName()));
		builder.appendSeparator("Attributes");
		JComponent component;
		for (InLineTagAttributes attribute : tag.getAttributes()) {
			if (tag.getTagName().equalsIgnoreCase("ref") && attribute.getAttributeName().equalsIgnoreCase("target")) {
				component = ATBasicComponentFactory.createUnboundComboBox(ResourceUtils.getReferencesArray());
				component.setOpaque(false);
			} else if (attribute.getValueList().length() > 0) {
				component = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(attribute.getValueList()));
				component.setOpaque(false);
			} else {
				component = new JTextField();
			}
			builder.append(attribute.getAttributeName(), component);
			attributes.add(new AttributeFieldPair(attribute.getAttributeName(), component));
		}
		dialogPane.remove(contentPanel);
		JPanel panel = builder.getPanel();
		panel.setOpaque(false);
		dialogPane.add(panel, BorderLayout.CENTER);
	}

	public int showDialog() {

		this.invalidate();
		this.validate();
		this.pack();

		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
		return (status);
	}

	public String constructAttributeString() {
		ArrayList<String> attributeStrings = new ArrayList<String>();
		for (AttributeFieldPair attributeFieldPair : attributes) {
			attributeStrings.add(attributeFieldPair.constructAttributeValueString());
		}
		return StringHelper.concat(attributeStrings, " ");
	}

	private class AttributeFieldPair {

		private String attributeName;
		private JComponent component;

		private AttributeFieldPair(String attributeName, JComponent component) {
			this.attributeName = attributeName;
			this.component = component;
		}


		private String constructAttributeValueString() {
			String value = "";
			if (component instanceof JTextField) {
				value = ((JTextField) component).getText();
			} else if (component instanceof JComboBox) {
				JComboBox comboBox = (JComboBox) component;
				if (comboBox.getSelectedIndex() > 0) {
					if (comboBox.getSelectedItem() instanceof String) {
						value = (String) comboBox.getSelectedItem();
					} else if (comboBox.getSelectedItem() instanceof ResourceUtils.PersistentId){
						ResourceUtils.PersistentId id = (ResourceUtils.PersistentId)comboBox.getSelectedItem();
						value = id.getPersistentId();
					}
				}
			}

			if (attributeName.length() == 0 || value.length() == 0) {
				return "";
			} else {
				return attributeName + "=\"" + value + "\"";
			}
		}
	}
}
