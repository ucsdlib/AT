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
 * @author Lee Mandell
 * Date: Jan 11, 2006
 * Time: 1:52:15 PM
 */

package org.archiviststoolkit.swing;

import com.inet.jortho.SpellChecker;
import com.jgoodies.validation.formatter.EmptyNumberFormatter;
import com.jgoodies.validation.formatter.EmptyDateFormatter;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.binding.value.ComponentValueModel;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.JTextComponent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.model.LookupListItems;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.ATDateUtils;

public class ATBasicComponentFactory extends BasicComponentFactory {


	public static JFormattedTextField createIntegerField(PresentationModel detailsModel, String fieldName) {
		return createIntegerField(detailsModel, fieldName, false, false);
	}

	public static JFormattedTextField createBufferedIntegerField(PresentationModel detailsModel, String fieldName) {
		return createIntegerField(detailsModel, fieldName, false, true);
	}

	public static JFormattedTextField createBufferedIntegerField(PresentationModel detailsModel, String fieldName, boolean useGrouping) {
		return createIntegerField(detailsModel, fieldName, useGrouping, true);
	}

	public static JFormattedTextField createIntegerField(PresentationModel detailsModel, String fieldName, boolean useGrouping) {
		return createIntegerField(detailsModel, fieldName, useGrouping, false);
	}

	public static JFormattedTextField createIntegerField(PresentationModel detailsModel, String fieldName, boolean useGrouping, boolean buffered) {
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setGroupingUsed(useGrouping);
		JFormattedTextField returnField;
		if (buffered) {
			returnField = BasicComponentFactory.createIntegerField(detailsModel.getBufferedModel(fieldName), nf);
		} else {
			returnField = BasicComponentFactory.createIntegerField(detailsModel.getModel(fieldName), nf);
		}
		returnField.addFocusListener(new ATFormattedTextFocusListener(returnField, "Invalid entry. This field requires an integer."));
		return returnField;
	}

    /**
     * Method to create a formated textfield which only accepts integers with a certain range
     * @param detailsModel The details model
     * @param fieldName The field name to bound to
     * @param min The minimum value allowed
     * @param max The maximum value allowed
     * @return
     */
    public static JFormattedTextField createIntegerField(PresentationModel detailsModel, String fieldName, int min, int max) {
        NumberFormat nf = NumberFormat.getIntegerInstance();
        JFormattedTextField returnField;
	    returnField = BasicComponentFactory.createIntegerField(detailsModel.getModel(fieldName), nf);
		returnField.addFocusListener(new ATFormattedTextFocusListener(returnField, "Invalid entry. This field requires an integer between " + min + " and " + max, min, max));
		return returnField;
    }

	public static JFormattedTextField createUnboundIntegerField() {
		return createUnboundIntegerField(true);
	}

	public static JFormattedTextField createUnboundIntegerField(boolean useGrouping) {
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setGroupingUsed(useGrouping);
		NumberFormatter numberFormatter =
			new EmptyNumberFormatter(nf);
		numberFormatter.setValueClass(Integer.class);
		JFormattedTextField returnField = new JFormattedTextField(numberFormatter);
		returnField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				Component component = e.getComponent();
				if (component instanceof JFormattedTextField) {
					if (!((JFormattedTextField)component).isEditValid()) {
						JOptionPane.showMessageDialog(null, "Invalid entry. This field requires an integer.");
					}
				}

			}
		});
		return returnField;
	}

	/**
	 * Creates and returns a formatted text field that is bound
	 * to the Date value of the given ValueModel.
	 * The JFormattedTextField is configured with an AbstractFormatter
	 * that uses two different DateFormats to edit and display the Date.
	 * A <code>SHORT</code> DateFormat with strict checking is used to edit
	 * (parse) a date; the DateFormatter's default DateFormat is used to
	 * display (format) a date. In both cases <code>null</code> Dates are
	 * mapped to the empty String.
	 *
	 * @param valueModel the model that holds the value to be edited
	 * @return a formatted text field for Date instances that is bound
	 *         to the given value model
	 * @throws NullPointerException if the valueModel is <code>null</code>
	 */
	public static JFormattedTextField createDateField(
			ValueModel valueModel) {
		JFormattedTextField.AbstractFormatter defaultFormatter =
				new EmptyDateFormatter(ApplicationFrame.applicationDateFormat);
		JFormattedTextField.AbstractFormatter displayFormatter =
				new EmptyDateFormatter(ApplicationFrame.applicationDateFormat);
		DefaultFormatterFactory formatterFactory =
				new DefaultFormatterFactory(defaultFormatter, displayFormatter);

		JFormattedTextField returnField = createFormattedTextField(valueModel, formatterFactory);
		returnField.addFocusListener(new ATFormattedTextFocusListener(returnField, "Invalid date format. Dates must be entered with the format\n" +
								ApplicationFrame.applicationDateFormat.toPattern(), true));
		return returnField;
	}

	public static JFormattedTextField createUnboundDateField() {
		JFormattedTextField.AbstractFormatter defaultFormatter =
				new EmptyDateFormatter(ApplicationFrame.applicationDateFormat);
		JFormattedTextField.AbstractFormatter displayFormatter =
				new EmptyDateFormatter(ApplicationFrame.applicationDateFormat);
		DefaultFormatterFactory formatterFactory =
				new DefaultFormatterFactory(defaultFormatter, displayFormatter);

		JFormattedTextField returnField = new JFormattedTextField(formatterFactory);
		returnField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				Component component = e.getComponent();
				if (component instanceof JFormattedTextField) {
					if (!((JFormattedTextField)component).isEditValid()) {
						JOptionPane.showMessageDialog(null, "Invalid date format. Dates must be entered with the format\n" +
								ApplicationFrame.applicationDateFormat.toPattern());
					} else {
                        ATDateUtils.isValidDatabaseDate((JFormattedTextField)component);
                    }
				}

			}
		});
		return returnField;
	}

    /**
     * This create a textfeild that is used to input iso dates. The only thing this does
     * from a regular textfield is that it does checking of the date format on focus lost
     * valid formats are yyyy, yyyy-mm, yyyy-mm-dd in the date format being used by the AT
     * @param valueModel The vlaue model to bind to
     * @return  The JTextfield
     */
    public static JTextField createISODateField (ValueModel valueModel) {
		JTextField textField = new JTextField();
		Bindings.bind(textField, valueModel, true);
		textField.addFocusListener(new ISODateFocusListener(textField));
		return textField;
	}


//	public static JFormattedTextField getSQLDateField() {
//		DateFormatter dateFormatter =
//				new DateFormatter();
//		dateFormatter.setValueClass(Date.class);
//		return new JFormattedTextField(dateFormatter);
//	}
//

	public static JFormattedTextField createDoubleField(PresentationModel detailsModel, String fieldName) {
		return createDoubleField(detailsModel, fieldName, false);
	}

	public static JFormattedTextField createDoubleField(PresentationModel detailsModel, String fieldName, boolean useGrouping) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(useGrouping);
		NumberFormatter numberFormatter =
				new EmptyNumberFormatter(nf, null);
		numberFormatter.setValueClass(Double.class);
		JFormattedTextField returnField = BasicComponentFactory.createFormattedTextField(detailsModel.getModel(fieldName), numberFormatter);
		returnField.addFocusListener(new ATFormattedTextFocusListener(returnField, "Invalid entry. This field requires a number."));
		return returnField;
	}

    /**
     * Method to return a text field which has been formated to accept currency
     *
     * @param detailsModel The model to bind to
     * @param fieldName The name of the field in the model to bind to
     * @param useGrouping
     * @return The textfeild formatted to accept currency
     */
    public static JFormattedTextField createCurrencyField(PresentationModel detailsModel, String fieldName, boolean useGrouping) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(useGrouping);
        nf.setMaximumFractionDigits(2);
		NumberFormatter numberFormatter =
				new EmptyNumberFormatter(nf, null);
		numberFormatter.setValueClass(Double.class);
		JFormattedTextField returnField = BasicComponentFactory.createFormattedTextField(detailsModel.getModel(fieldName), numberFormatter);
		returnField.addFocusListener(new ATFormattedTextFocusListener(returnField, "Invalid entry. This field requires a number."));
		return returnField;
	}

	public static JFormattedTextField createUnboundDoubleField() {
		return createUnboundDoubleField(true);
	}


	public static JFormattedTextField createUnboundDoubleField(boolean useGrouping) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(useGrouping);
		NumberFormatter numberFormatter =
				new EmptyNumberFormatter(nf, null);
		numberFormatter.setValueClass(Double.class);
		JFormattedTextField returnField = new JFormattedTextField(numberFormatter);
		returnField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				Component component = e.getComponent();
				if (component instanceof JFormattedTextField) {
					if (!((JFormattedTextField)component).isEditValid()) {
						JOptionPane.showMessageDialog(null, "Invalid entry. This field requires a number.");
					}
				}

			}
		});
		return returnField;
	}

	public static JCheckBox createCheckBox(PresentationModel detailsModel, String fieldName, Class clazz) {
		return createCheckBox(detailsModel, fieldName, clazz, false);
	}

	public static JCheckBox createCheckBox(PresentationModel detailsModel, String fieldName, Class clazz, boolean buffered) {
		ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(clazz.getName() + "." + fieldName);
		JCheckBox checkBox;
		if (fieldInfo == null) {
			if (buffered) {
				checkBox = BasicComponentFactory.createCheckBox(detailsModel.getBufferedModel(fieldName), fieldName);
			} else {
				checkBox = BasicComponentFactory.createCheckBox(detailsModel.getModel(fieldName), fieldName);
			}
		} else {
			if (buffered) {
				checkBox = BasicComponentFactory.createCheckBox(detailsModel.getBufferedModel(fieldName), fieldInfo.getFieldLabel());
			} else {
				checkBox = BasicComponentFactory.createCheckBox(detailsModel.getModel(fieldName), fieldInfo.getFieldLabel());
			}
			checkBox.setToolTipText(fieldInfo.getPopupHelpText());
		}
		checkBox.addFocusListener(new ATCheckBoxListener(checkBox));
		return checkBox;
	}


//	public static JComboBox createComboBox(PresentationModel detailsModel, String fieldName, Class clazz) {
//		ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(clazz.getName() + "." + fieldName);
//		Vector<String> values = null;
//		if (fieldInfo != null) {
//			values = LookupListUtils.getLookupListValues(fieldInfo.getLookupList());
//		}
//		if (values == null) {
//			values = new Vector<String>();
//		}
//		return addListCellRenderer(BasicComponentFactory.createComboBox(new SelectionInList(values, detailsModel.getModel(fieldName))));
//	}

	public static JComboBox createComboBox(PresentationModel detailsModel, String fieldName, Class clazz) {
		return createComboBox(detailsModel, fieldName, clazz, 0, false);
	}

	public static JComboBox createComboBox(PresentationModel detailsModel, String fieldName, Class clazz, boolean buffered) {
		return createComboBox(detailsModel, fieldName, clazz, 0, buffered);
	}

	public static JComboBox createComboBox(PresentationModel detailsModel, String fieldName, Class clazz, Integer maxLength) {
		return createComboBox(detailsModel, fieldName, clazz, maxLength, false);
	}

	public static JComboBox createComboBox(PresentationModel detailsModel, String fieldName, Class clazz, Integer maxLength, boolean buffered) {
		ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(clazz.getName() + "." + fieldName);
		Vector<LookupListItems> values = null;
		JComboBox returnCombobox;
		if (fieldInfo != null) {
			values = LookupListUtils.getLookupListValues2(fieldInfo.getLookupList());
		}
		if (values == null) {
			values = new Vector<LookupListItems>();
		} else {

		}
		if (fieldInfo == null) {
			returnCombobox = addListCellRenderer(BasicComponentFactory.createComboBox(
					new SelectionInList(values)), maxLength);
		} else {
			if (buffered) {
				ValueModel valueModel = detailsModel.getBufferedModel(fieldName);
				detailsModel.observeChanged(valueModel);
				returnCombobox = addListCellRenderer(BasicComponentFactory.createComboBox(
						new SelectionInList(values, ATJgoodiesBindingConverterFactory.createLookupListConverter(valueModel, fieldInfo.getLookupList()))), maxLength);
			} else {
				ValueModel valueModel = detailsModel.getModel(fieldName);
				detailsModel.observeChanged(valueModel);
				returnCombobox = addListCellRenderer(BasicComponentFactory.createComboBox(
						new SelectionInList(values,
								ATJgoodiesBindingConverterFactory.createLookupListConverter(valueModel, fieldInfo.getLookupList()))), maxLength);
			}
		}
		returnCombobox.addFocusListener(new ATComboBoxListener(returnCombobox));
		return returnCombobox;
	}

	public static JComboBox createComboBoxWithConverter(PresentationModel detailsModel, String fieldName, Vector values, String listName) {
		return addListCellRenderer(BasicComponentFactory.createComboBox(new SelectionInList(values,
				ATJgoodiesBindingConverterFactory.createLookupListConverter(detailsModel.getModel(fieldName), listName))), 0);
//		return createComboBox(detailsModel, fieldName, values, 0);
	}



	private static JComboBox addListCellRenderer(JComboBox comboBox, Integer maxLength) {
		comboBox.setRenderer(new DisplayListCellRenderer(maxLength));
		return comboBox;
	}

	public static JComboBox createComboBox(PresentationModel detailsModel, String fieldName, Object[] values) {
		return createComboBox(detailsModel, fieldName, values, 0, false);
	}

	public static JComboBox createComboBox(PresentationModel detailsModel, String fieldName, Object[] values, boolean buffered) {
		return createComboBox(detailsModel, fieldName, values, 0, buffered);
	}

	public static JComboBox createComboBox(PresentationModel detailsModel, String fieldName, Object[] values, Integer maxLength, boolean buffered) {
		JComboBox returnCombobox;
		if (buffered) {
			returnCombobox = addListCellRenderer(BasicComponentFactory.createComboBox(new SelectionInList(values, detailsModel.getModel(fieldName))), maxLength);
		} else {
			returnCombobox = addListCellRenderer(BasicComponentFactory.createComboBox(new SelectionInList(values, detailsModel.getBufferedModel(fieldName))), maxLength);
		}
		returnCombobox.addFocusListener(new ATComboBoxListener(returnCombobox));
		return returnCombobox;
	}

	public static JComboBox createComboBox(PresentationModel detailsModel, String fieldName, ArrayList values) {
		return createComboBox(detailsModel, fieldName, values, 0);
	}

	public static JComboBox createComboBox(PresentationModel detailsModel, String fieldName, ArrayList values, Integer maxLength) {
		JComboBox returnCombobox = addListCellRenderer(BasicComponentFactory.createComboBox(new SelectionInList(values, detailsModel.getModel(fieldName))), maxLength);
		returnCombobox.addFocusListener(new ATComboBoxListener(returnCombobox));
		return returnCombobox;
	}

	public static JComboBox createComboBox(PresentationModel detailsModel, String fieldName, Vector values) {
		JComboBox returnCombobox =  createComboBox(detailsModel, fieldName, values, 0);
		returnCombobox.addFocusListener(new ATComboBoxListener(returnCombobox));
		return returnCombobox;
	}

	public static JComboBox createComboBox(PresentationModel detailsModel, String fieldName, Vector values, Integer maxLength) {
		return addListCellRenderer(BasicComponentFactory.createComboBox(new SelectionInList(values, detailsModel.getModel(fieldName))), maxLength);
//		return addListCellRenderer(BasicComponentFactory.createComboBox(
//				new SelectionInList(values,
//						ATJgoodiesBindingConverterFactory.createLookupListConverter(detailsModel.getModel(fieldName), ""))), maxLength);
	}

	public static JComboBox createUnboundComboBox(Vector values) {
		return new JComboBox(new DefaultComboBoxModel(values));
	}

	public static JComboBox createUnboundComboBox(Object[] values) {
		return new JComboBox(new DefaultComboBoxModel(values));
	}

	public static JTextArea createTextArea(ValueModel valueModel) {
		return createTextArea(valueModel, true);
	}

	public static JTextArea createTextArea(ValueModel valueModel, boolean commitOnFocusLost) {
		JTextArea textArea = BasicComponentFactory.createTextArea(valueModel, commitOnFocusLost);
		textArea.addFocusListener(new ATTextFocusListener(textArea));
		return fixTabBehavior(textArea);
	}

	public static JTextArea createUnboundedTextArea() {
		return fixTabBehavior(new JTextArea());
	}

	private static JTextArea fixTabBehavior(JTextArea textArea) {
		textArea.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
		textArea.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);

        // add support for spell checking here
        // using Jortho spell checker library
        if(ApplicationFrame.enableSpellCheck) {
            try {
                SpellChecker.register(textArea);
                SpellChecker.enableAutoSpell(textArea, ApplicationFrame.enableSpellCheckHighlight);
            } catch(Exception e) {
                // just print the stack trace and do nothing
                e.printStackTrace();
            }
        }

        return textArea;
	}

	public static JTextField createTextField(ValueModel valueModel) {
		return ATBasicComponentFactory.createTextField(valueModel, false);
	}

	/**
	 * Creates and returns a text field with the content bound
	 * to the given ValueModel. Text changes can be committed to the model
	 * on focus lost or on every character typed.
	 *
	 * @param valueModel         the model that provides the text value
	 * @param commitOnFocusLost  true to commit text changes on focus lost,
	 *     false to commit text changes on every character typed
	 * @return a text field that is bound to the given value model
	 *
	 * @throws NullPointerException if the valueModel is <code>null</code>
	 *
	 * @see #createTextField(ValueModel)
	 */
	public static JTextField createTextField(
			ValueModel valueModel, boolean commitOnFocusLost) {
		JTextField textField = new JTextField();
		Bindings.bind(textField, valueModel, commitOnFocusLost);
		textField.addFocusListener(new ATTextFocusListener(textField));
		return textField;
	}

	public static JLabel createLabel(Class clazz, String fieldName, String defaultValue) {
		ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(clazz.getName() + "." + fieldName);
		if (fieldInfo == null) {
			return new JLabel(defaultValue);
		} else {
			String labelText = fieldInfo.getFieldLabel();
			JLabel label;
			if (labelText.length() == 0) {
				label = new JLabel(defaultValue);
			} else {
				label = new JLabel(labelText);
			}
			label.setToolTipText(fieldInfo.getPopupHelpText());
			return label;
		}
	}

	public static JLabel createLabel(Class clazz, String fieldName) {
		return createLabel(clazz, fieldName, fieldName);
	}

	/**
	 * Listens to property changes in a ComponentValueModel and
	 * updates the associated component state.
	 *
	 * @see ComponentValueModel
	 */
	private static final class ATTextFocusListener implements FocusListener {

		private String oldValue = null;
		private JTextComponent textComponent;

		public ATTextFocusListener(JTextComponent textComponent) {
			this.textComponent = textComponent;
		}

		public void focusGained(FocusEvent event) {
			oldValue = textComponent.getText();
		}

		public void focusLost(FocusEvent event) {
			if (!oldValue.equals(textComponent.getText())) {
				ApplicationFrame.getInstance().setRecordDirty();
			}
		}
	}

	/**
	 * Listens to property changes in a ComponentValueModel and
	 * updates the associated component state.
	 *
	 * @see ComponentValueModel
	 */
	private static final class ATComboBoxListener implements FocusListener {

		private Object oldValue = null;
		private JComboBox comboBox;

		public ATComboBoxListener(JComboBox comboBox) {
			this.comboBox = comboBox;
		}

		public void focusGained(FocusEvent event) {
			oldValue = comboBox.getSelectedItem();
		}

		public void focusLost(FocusEvent event) {
			if (oldValue!= null && !oldValue.equals(comboBox.getSelectedItem())) {
				ApplicationFrame.getInstance().setRecordDirty();
			}
		}
	}

	/**
	 * Listens to property changes in a ComponentValueModel and
	 * updates the associated component state.
	 *
	 * @see ComponentValueModel
	 */
	private static final class ATCheckBoxListener implements FocusListener {

		private Object oldValue = null;
		private JCheckBox checkBox;

		public ATCheckBoxListener(JCheckBox checkBox) {
			this.checkBox = checkBox;
		}

		public void focusGained(FocusEvent event) {
			oldValue = checkBox.isSelected();
		}

		public void focusLost(FocusEvent event) {
			if (!oldValue.equals(checkBox.isSelected())) {
				ApplicationFrame.getInstance().setRecordDirty();
			}
		}
	}

	/**
	 * Listens to property changes in a ComponentValueModel and
	 * updates the associated component state.
	 *
	 * @see ComponentValueModel
	 */
	private static final class ATFormattedTextFocusListener implements FocusListener {

		private String oldValue = null;
		private JFormattedTextField formattedTextField;
		private String invalidImputErrorMessage;
        private boolean isDateField = false; // default this to false;
        private boolean checkIntegerRange = false; // see weather to check the range of an integer field
        private int min = 0; // the minimum value an integer can be
        private int max = 0; // the maximum value an integer can be

		public ATFormattedTextFocusListener(JFormattedTextField formattedTextField, String invalidImputErrorMessage) {
			this.formattedTextField = formattedTextField;
			this.invalidImputErrorMessage = invalidImputErrorMessage;
		}

        /**
         * Constructor that allows setting that this is a date field so that the date range can be checked
         * @param formattedTextField
         * @param invalidImputErrorMessage
         * @param isDateField
         */
        public ATFormattedTextFocusListener(JFormattedTextField formattedTextField, String invalidImputErrorMessage, boolean isDateField) {
			this.formattedTextField = formattedTextField;
			this.invalidImputErrorMessage = invalidImputErrorMessage;
            this.isDateField = isDateField;
		}

        /**
         * Constructor that allows for setting the minimum and maximum an integer should be
         * @param formattedTextField
         * @param invalidImputErrorMessage
         * @param max
         * @param min
         */
        public ATFormattedTextFocusListener(JFormattedTextField formattedTextField, String invalidImputErrorMessage, int min, int max) {
            this.formattedTextField = formattedTextField;
			this.invalidImputErrorMessage = invalidImputErrorMessage;
            this.min = min;
            this.max = max;
            this.checkIntegerRange = true;
        }

		public void focusGained(FocusEvent event) {
			oldValue = formattedTextField.getText();
		}

		public void focusLost(FocusEvent event) {
			if (isDateField) { // it is a date field so must also check minimum allowed date for the database
                if (!(formattedTextField.isEditValid())) {
				    JOptionPane.showMessageDialog(null, invalidImputErrorMessage);
			    } else {
                    ATDateUtils.isValidDatabaseDate(formattedTextField);
                }
                // check to see if the date has changed
                if (!oldValue.equals(formattedTextField.getText())) {
					ApplicationFrame.getInstance().setRecordDirty();
				}
            } else if(checkIntegerRange) {
                if (!(formattedTextField.isEditValid())) {
				    JOptionPane.showMessageDialog(null, invalidImputErrorMessage);
			    } else { // check to see if the entered integer is within a valid range
                    try {
                        int intValue = Integer.parseInt(formattedTextField.getText());

                        if (intValue < min || intValue > max) {
                            JOptionPane.showMessageDialog(null, invalidImputErrorMessage);
                            formattedTextField.setText("");
                        }
                    } catch(NumberFormatException nfe) { } // do nothing about this is just a blank
                }

                if (!oldValue.equals(formattedTextField.getText())) {
					ApplicationFrame.getInstance().setRecordDirty();
				}
            } else if(!(formattedTextField.isEditValid())) {
				JOptionPane.showMessageDialog(null, invalidImputErrorMessage);
			} else if (oldValue == null && formattedTextField.getText() == null) {
				//do nothing
			} else if (oldValue == null && formattedTextField.getText() != null) {
				ApplicationFrame.getInstance().setRecordDirty();
			} else {
				if (!oldValue.equals(formattedTextField.getText())) {
					ApplicationFrame.getInstance().setRecordDirty();
				}
			}
		}
	}

    /**
	 * Listens to property changes in a ComponentValueModel and
	 * updates the associated component state.
     * Also check that the iso date is valid. Valid dates formats are
	 * yyyy, yyyy-mm, yyyy-dd,
	 * @see ComponentValueModel
	 */
	private static final class ISODateFocusListener implements FocusListener {

		private String oldValue = null;
		private JTextComponent textComponent;

		public ISODateFocusListener(JTextComponent textComponent) {
			this.textComponent = textComponent;
		}

		public void focusGained(FocusEvent event) {
			oldValue = textComponent.getText();
		}

		public void focusLost(FocusEvent event) {
			String newValue = textComponent.getText();

            // check that the iso date format is valid
            if(newValue.length() > 0 && ATDateUtils.isValidISODate((JTextField)textComponent)) {
                // now check if the value was changed
                if (!oldValue.equals(newValue)) {
				    ApplicationFrame.getInstance().setRecordDirty();
			    } else {
                    textComponent.setText(oldValue);
                }
            }
		}
	}

}
