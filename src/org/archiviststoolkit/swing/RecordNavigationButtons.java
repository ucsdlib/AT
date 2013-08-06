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
 * Created by JFormDesigner on Wed Aug 24 15:20:07 EDT 2005
 */

package org.archiviststoolkit.swing;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

public class RecordNavigationButtons extends JPanel {
	public RecordNavigationButtons() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		firstRecord = new JButton();
		previousRecord = new JButton();
		nextRecord = new JButton();
		lastRecord = new JButton();
		cancelButton2 = new JButton();
		cancelButton = new JButton();
		okButton = new JButton();
		firstRecordLabel = new JLabel();
		previousRecordLabel = new JLabel();
		nextRecordLabel = new JLabel();
		lastRecordLabel = new JLabel();
		cancelButtonLabel2 = new JLabel();
		cancelButtonLabel = new JLabel();
		okRecordLabel = new JLabel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(Borders.DLU2_BORDER);
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
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
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC
			}));

		//---- firstRecord ----
		firstRecord.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/firstRecord.jpg")));
		add(firstRecord, cc.xy(1, 1));

		//---- previousRecord ----
		previousRecord.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/prevRecord.jpg")));
		add(previousRecord, cc.xy(3, 1));

		//---- nextRecord ----
		nextRecord.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/nextRecord.jpg")));
		add(nextRecord, cc.xy(5, 1));

		//---- lastRecord ----
		lastRecord.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/lastRecord.jpg")));
		add(lastRecord, cc.xy(7, 1));

		//---- cancelButton2 ----
		cancelButton2.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/print.jpg")));
		add(cancelButton2, cc.xy(11, 1));

		//---- cancelButton ----
		cancelButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/cancel.jpg")));
		add(cancelButton, cc.xy(15, 1));

		//---- okButton ----
		okButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/ok.jpg")));
		add(okButton, cc.xy(17, 1));

		//---- firstRecordLabel ----
		firstRecordLabel.setText("First");
		firstRecordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		firstRecordLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		add(firstRecordLabel, cc.xy(1, 3));

		//---- previousRecordLabel ----
		previousRecordLabel.setText("Previous");
		previousRecordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		previousRecordLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		add(previousRecordLabel, cc.xy(3, 3));

		//---- nextRecordLabel ----
		nextRecordLabel.setText("Next");
		nextRecordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nextRecordLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		add(nextRecordLabel, cc.xy(5, 3));

		//---- lastRecordLabel ----
		lastRecordLabel.setText("Last");
		lastRecordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lastRecordLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		add(lastRecordLabel, cc.xy(7, 3));

		//---- cancelButtonLabel2 ----
		cancelButtonLabel2.setText("Print");
		cancelButtonLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		cancelButtonLabel2.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		add(cancelButtonLabel2, cc.xy(11, 3));

		//---- cancelButtonLabel ----
		cancelButtonLabel.setText("Cancel");
		cancelButtonLabel.setHorizontalAlignment(SwingConstants.CENTER);
		cancelButtonLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		add(cancelButtonLabel, cc.xy(15, 3));

		//---- okRecordLabel ----
		okRecordLabel.setText("OK");
		okRecordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		okRecordLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		add(okRecordLabel, cc.xy(17, 3));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JButton firstRecord;
	private JButton previousRecord;
	private JButton nextRecord;
	private JButton lastRecord;
	private JButton cancelButton2;
	private JButton cancelButton;
	private JButton okButton;
	private JLabel firstRecordLabel;
	private JLabel previousRecordLabel;
	private JLabel nextRecordLabel;
	private JLabel lastRecordLabel;
	private JLabel cancelButtonLabel2;
	private JLabel cancelButtonLabel;
	private JLabel okRecordLabel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public JButton getFirstRecord() {
		return firstRecord;
	}

	public void setFirstRecord(JButton firstRecord) {
		this.firstRecord = firstRecord;
	}

	public JButton getPreviousRecord() {
		return previousRecord;
	}

	public void setPreviousRecord(JButton previousRecord) {
		this.previousRecord = previousRecord;
	}

	public JButton getNextRecord() {
		return nextRecord;
	}

	public void setNextRecord(JButton nextRecord) {
		this.nextRecord = nextRecord;
	}

	public JButton getLastRecord() {
		return lastRecord;
	}

	public void setLastRecord(JButton lastRecord) {
		this.lastRecord = lastRecord;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(JButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	public JButton getOkButton() {
		return okButton;
	}

	public void setOkButton(JButton okButton) {
		this.okButton = okButton;
	}
}
