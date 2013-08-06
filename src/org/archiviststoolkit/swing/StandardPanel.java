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
 */

package org.archiviststoolkit.swing;

//==============================================================================
// Import Declarations
//==============================================================================

import javax.swing.JPanel;

import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import javax.swing.JComponent;
import java.awt.*;

/**
 * A panel used to render lists of editoring lines.
 */

public class StandardPanel extends JPanel {

	/** Magic Number for text width. */
	public static final int TEXT_WIDTH = 20;

	/** Magic Number for text height. */
	public static final int TEXT_HEIGHT = 20;

	/** Magic Number Inset. */

	static final int INSET_WIDTH = 10;

	/** Magic Number Constraint. */
	static final int CONSTRAINT_HEIGHT = 99;

	/** The row to add the next labelled item to. */
	private int myNextItemRow = 0;

	/**
	 * This method is the default constructor.
	 */

	public StandardPanel() {
		init();
	}

	/**
	 * This method initialises the panel and layout manager.
	 */

	private void init() {
		setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEtchedBorder());

		// Create a blank label to use as a vertical fill so that the
		// label/item pairs are aligned to the top of the panel and are not
		// grouped in the centre if the parent component is taller than
		// the preferred size of the panel.

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx   = 0;
		constraints.gridy   = CONSTRAINT_HEIGHT;
		constraints.insets  = new Insets(INSET_WIDTH, 0, 0, 0);
		constraints.weighty = 1.0;
		constraints.fill    = GridBagConstraints.VERTICAL;

		JLabel verticalFillLabel = new JLabel();

		add(verticalFillLabel, constraints);
	}

	/**
	 * This method adds a labelled item to the panel. The item is added to
	 * the row below the last item added.
	 *
	 * @param labelText The label text for the item.
	 * @param item      The item to be added.
	 */

	public final void addItem(final String labelText, final JComponent item) {
		//default to editable items
		addItem(labelText, item, true);
	}

	/**
	 * This method adds a labelled item to the panel. The item is added to
	 * the row below the last item added.
	 *
	 * @param labelText The label text for the item.
	 * @param item      The item to be added.
	 */

	public final void addItem(final String labelText, final JComponent item, final boolean enabled) {
		// Create the label and its constraints

		JLabel label = new JLabel(labelText);

		GridBagConstraints labelConstraints = new GridBagConstraints();

		labelConstraints.gridx   = 0;
		labelConstraints.gridy   = myNextItemRow;
		labelConstraints.insets  = new Insets(INSET_WIDTH, INSET_WIDTH, 0, 0);
		labelConstraints.anchor  = GridBagConstraints.NORTHEAST;
		labelConstraints.fill    = GridBagConstraints.NONE;

		add(label, labelConstraints);

		// Add the component with its constraints

		GridBagConstraints itemConstraints = new GridBagConstraints();

		itemConstraints.gridx   = 1;
		itemConstraints.gridy   = myNextItemRow;
		itemConstraints.insets  = new Insets(INSET_WIDTH, INSET_WIDTH, 0, INSET_WIDTH);
		itemConstraints.weightx = 1.0;
		itemConstraints.anchor  = GridBagConstraints.WEST;
		itemConstraints.fill    = GridBagConstraints.HORIZONTAL;
		item.setEnabled(enabled);

		add(item, itemConstraints);

		myNextItemRow++;
	}

	/**
	 * This method adds a labelled item to the panel. The item is added to
	 * the row below the last item added.
	 *
	 * @param labelText The label text for the item.
	 * @param item      The item to be added.
	 */

	public final void addScrollPane(final String labelText, final JScrollPane item) {
		//default to editable items
		addScrollPane(labelText, item, true);
	}

	/**
	 * This method adds a labelled item to the panel. The item is added to
	 * the row below the last item added.
	 *
	 * @param labelText The label text for the item.
	 * @param item      The item to be added.
	 */

	public final void addScrollPane(final String labelText, final JScrollPane item, final boolean enabled) {
		// Create the label and its constraints

		JLabel label = new JLabel(labelText);

		GridBagConstraints labelConstraints = new GridBagConstraints();

		labelConstraints.gridx   = 0;
		labelConstraints.gridy   = myNextItemRow;
		labelConstraints.insets  = new Insets(INSET_WIDTH, INSET_WIDTH, 0, 0);
		labelConstraints.anchor  = GridBagConstraints.NORTHEAST;
		labelConstraints.fill    = GridBagConstraints.NONE;

		add(label, labelConstraints);

		// Add the component with its constraints

		GridBagConstraints itemConstraints = new GridBagConstraints();

		itemConstraints.gridx   = 1;
		itemConstraints.gridy   = myNextItemRow;
		itemConstraints.insets  = new Insets(INSET_WIDTH, INSET_WIDTH, 0, INSET_WIDTH);
		itemConstraints.weightx = 1.0;
		itemConstraints.anchor  = GridBagConstraints.WEST;
		itemConstraints.fill    = GridBagConstraints.HORIZONTAL;
//		itemConstraints.gridheight = 20;
		item.setEnabled(enabled);
		item.getViewport().setExtentSize(new Dimension(10,10));

		add(item, itemConstraints);

		myNextItemRow++;
	}

	public final void removeAllItems() {
		this.removeAll();
		this.myNextItemRow = 0;
	}
}


//=============================================================================
// $Log: StandardPanel.java,v $
// Revision 1.4  2004/12/28 02:54:41  lejames
// outlook express is now fully mapped within the new import scheme.
//
// Revision 1.3  2004/12/15 02:11:37  lejames
// added the opportunity model
//
// Revision 1.2  2004/11/29 03:35:55  lejames
// Part of the updates for version 1.1.1, this time we have cleaned up the
// code and removed all checkstyle issues
//
// Revision 1.1  2004/11/26 17:51:33  lejames
// Completion of roadmap to 1.1.0 including additional maven report plugins,
// hibernate and hsql integration, DOA layer and basic contact management
//
//
//
//=============================================================================
// EOF
//=============================================================================
