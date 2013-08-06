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
 * Date: Jul 15, 2008
 * Time: 5:48:12 PM
 */

package org.archiviststoolkit.editor.rde;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.exceptions.RDEPopulateException;

public class RdePanelContainer extends JPanel {

	private String title;

	public RdePanelContainer(String title) throws HeadlessException {
		super();
		this.title = title;
		addSubPanel(new RdeInstructions(this));
//		addSubPanels();
//		initComponents();
	}

	public void initComponents() {

		//======== this ========
		setBorder(Borders.DLU4_BORDER);
        setOpaque(true);
        setBackground(new Color(200, 205, 232));
        setLayout(new FormLayout(
			"fill:default:grow",
			"top:default:grow"));

		scrollPane = new javax.swing.JScrollPane();
		scrollPane.setPreferredSize(new Dimension(800,600));
		panel = buildPanel();


		scrollPane.setViewportView(panel);
        scrollPane.setOpaque(true);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(200, 205, 232));
        CellConstraints cc = new CellConstraints();
		this.add(scrollPane, cc.xy(1, 1));

	}

	public void componentFocusGained(java.awt.event.FocusEvent evt) {
		java.awt.Component focusedComponent = evt.getComponent();
		panel.scrollRectToVisible(focusedComponent.getBounds(null));
		System.out.println(focusedComponent);
		repaint();
	}


	public void componentFocusGained(java.awt.event.FocusEvent evt, JPanel panel) {
		this.panel.scrollRectToVisible(panel.getBounds(null));
		repaint();
	}


	private javax.swing.JScrollPane scrollPane;
	private javax.swing.JPanel panel;
	private Resources resource;
	private ArrayList<RdePanel> subPanels = new ArrayList<RdePanel>();

	public JPanel buildPanel() {

		ArrayList<RowSpec> rowSpecs = new ArrayList<RowSpec>();
		rowSpecs.add(FormFactory.DEFAULT_ROWSPEC);
		for (int i = 0; i < subPanels.size(); i++ ) {
			rowSpecs.add(FormFactory.LINE_GAP_ROWSPEC);
			rowSpecs.add(FormFactory.DEFAULT_ROWSPEC);
		}

		FormLayout layout = new FormLayout(
				ColumnSpec.decodeSpecs("default:grow"),
				rowSpecs.toArray(new RowSpec[0]));

        PanelBuilder builder = new PanelBuilder(layout);
		builder.setBorder(null);

        CellConstraints cc = new CellConstraints();
		int row = 1;
		for (RdePanel panel: subPanels) {
			builder.add(panel, cc.xy(1, row));
			row += 2;
		}

		JPanel returnPanel = builder.getPanel();
        returnPanel.setBackground(new Color(200, 205, 232));
        return returnPanel;
	}

	public void addSubPanel(RdePanel panel) {
		subPanels.add(panel);
	}

	public void clearFields() {
		for (RdePanel panel: subPanels) {
			panel.clearFields();
		}
	}

    // Method to set the deault values
    public void setDefaultValues() {
        for (RdePanel panel: subPanels) {
			panel.setDefaultValues();
		}
    }

    public void populateComponent(ResourcesComponents component) throws RDEPopulateException {
		for (RdePanel panel: subPanels) {
			panel.populateComponent(component);
		}
	}

	public void setStickyLabels() {
		for (RdePanel panel: subPanels) {
			panel.setStickyLabels();
		}
	}

	public Resources getResource() {
		return resource;
	}

	public void setResource(Resources resource) {
		this.resource = resource;
	}

	public String toString() {
		return this.getTitle();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
