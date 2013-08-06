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
 * Date: Dec 28, 2005
 * Time: 12:44:46 PM
 */

package org.archiviststoolkit.report;

import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.JRException;
import org.archiviststoolkit.mydomain.DomainTableFormat;
import java.awt.*;

public class JRReturnScreen {

	private static final int WIDTH = 576;

	private JasperDesign designTemplate;
	private DomainTableFormat tableFormat;

	public JRReturnScreen(JasperDesign designTemplate, DomainTableFormat tableFormat) {

		this.designTemplate = designTemplate;
		this.tableFormat = tableFormat;
	}

	public void createHeaderAndDetail() throws JRException {
		JRDesignStaticText staticText;

		JRDesignBand columnHeaderBand = (JRDesignBand)designTemplate.getColumnHeader();

        // (8/17/2011) This was changed after update ll to get program
        // to compile using Jasper Reports 4.1.1
		JRDesignBand detailBand = (JRDesignBand)((JRSection)designTemplate.getDetailSection()).getBands()[0];

		int numberOfColumns = tableFormat.getColumnCount();
		int columnWidth = WIDTH/numberOfColumns;
//		DomainTableModelColumn columnModel = null;
		String fieldName;
		Class columnClass;
		JRDesignField jrDesignField;
		JRDesignTextField textField;
		JRDesignExpression textFieldExpression;
		JRDesignVariable jrDesignVariable;
		JRDesignExpression variableExpression;

		for (int i =0; i < numberOfColumns; i++) {
//			columnModel = tableModel.getTableModelColumn(i);

			staticText = new JRDesignStaticText();
			staticText.setText(tableFormat.getColumnName(i));
			staticText.setY(5);
			staticText.setX(i*columnWidth);
			staticText.setHeight(15);
			staticText.setWidth(columnWidth);
			staticText.setForecolor(Color.black);
			staticText.setBackcolor(Color.white);
			staticText.setBold(true);
			columnHeaderBand.addElement(staticText);

			fieldName = tableFormat.getColumnFieldName(i);
			columnClass = tableFormat.getColumnClass(i);
			jrDesignField = new JRDesignField();
			jrDesignField.setName(fieldName);
			jrDesignField.setValueClass(columnClass);
			designTemplate.addField(jrDesignField);

			textField = new JRDesignTextField();
			textFieldExpression = new JRDesignExpression();
			if (columnClass.getPackage() == Package.getPackage("org.archiviststoolkit.model")) {
			   	jrDesignVariable = new JRDesignVariable();
				jrDesignVariable.setName(fieldName+"Variable");
				jrDesignVariable.setValueClass(String.class);
				variableExpression = new JRDesignExpression();
				variableExpression.setText("$F{" + fieldName + "}.toString()");
				variableExpression.setValueClass(String.class);
				jrDesignVariable.setExpression(variableExpression);
				designTemplate.addVariable(jrDesignVariable);
				textFieldExpression.setText("$V{" + fieldName + "Variable}");
				textFieldExpression.setValueClass(String.class);
			} else {
				textFieldExpression.setText("$F{" + fieldName + "}");
				textFieldExpression.setValueClass(columnClass);
			}

			textField.setExpression(textFieldExpression);
			textField.setY(0);
			textField.setX(i*columnWidth);
			textField.setHeight(15);
			textField.setWidth(columnWidth);
			textField.setStretchWithOverflow(true);
			textField.setForecolor(Color.black);
			textField.setBackcolor(Color.white);
			textField.setPrintRepeatedValues(true);
			detailBand.addElement(textField);
		}
	}
}
