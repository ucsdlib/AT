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
 * Created by JFormDesigner on Wed Apr 19 14:35:25 EDT 2006
 */

package org.archiviststoolkit.dialog;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import com.jgoodies.validation.ValidationResult;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.exceptions.MismatchValuesException;
import org.archiviststoolkit.model.Locations;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.mydomain.DomainAccessObject;
import org.archiviststoolkit.mydomain.DomainAccessObjectImpl;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.swing.ATProgressUtil;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.util.JGoodiesValidationUtils;
import org.archiviststoolkit.util.LocationsUtils;

import javax.swing.*;
import javax.xml.bind.ValidationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


/**
 * @author Lee Mandell
 */
public class BatchLocationCreation extends JDialog {

	private static final int WARNING_MESSAGE_THRESHOLD = 500;

	private BatchLocationValidator validator;

	public BatchLocationCreation(Frame owner) {
		super(owner);
		initComponents();
		validator = new BatchLocationValidator(this);
		hideRepositoryPopupIfNecessary();
	}

	public BatchLocationCreation(Dialog owner) {
		super(owner);
		initComponents();
		validator = new BatchLocationValidator(this);
		hideRepositoryPopupIfNecessary();
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
        panel1 = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        building = new JTextField();
        label2 = new JLabel();
        floor = new JTextField();
        label3 = new JLabel();
        room = new JTextField();
        label4 = new JLabel();
        area = new JTextField();
        label12 = new JLabel();
        label13 = new JLabel();
        label14 = new JLabel();
        label5 = new JLabel();
        coordinate1Label = new JTextField();
        coordinate1Start = new JTextField();
        label6 = new JLabel();
        coordinate1End = new JTextField();
        label7 = new JLabel();
        coordinate2Label = new JTextField();
        coordinate2Start = new JTextField();
        label9 = new JLabel();
        coordinate2End = new JTextField();
        label8 = new JLabel();
        coordinate3Label = new JTextField();
        coordinate3Start = new JTextField();
        label10 = new JLabel();
        coordinate3End = new JTextField();
        label11 = new JLabel();
        repository = new JComboBox(new DefaultComboBoxModel(Repositories.getRepositoryList()));
        buttonBar = new JPanel();
        generateButton = new JButton();
        doneButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(null);
            dialogPane.setBackground(new Color(200, 205, 232));
            dialogPane.setLayout(new FormLayout(
                ColumnSpec.decodeSpecs("default:grow"),
                new RowSpec[] {
                    new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
                    FormFactory.LINE_GAP_ROWSPEC,
                    new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                }));

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
                    panel2.setBackground(new Color(80, 69, 57));
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
                    mainHeaderLabel.setText("Project Management");
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
                            new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                            FormFactory.RELATED_GAP_COLSPEC
                        },
                        new RowSpec[] {
                            FormFactory.RELATED_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.RELATED_GAP_ROWSPEC
                        }));

                    //---- subHeaderLabel ----
                    subHeaderLabel.setText("Batch Location Creation");
                    subHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
                    subHeaderLabel.setForeground(Color.white);
                    panel3.add(subHeaderLabel, cc.xy(2, 2));
                }
                HeaderPanel.add(panel3, cc.xy(2, 1));
            }
            dialogPane.add(HeaderPanel, cc.xy(1, 1));

            //======== panel1 ========
            {
                panel1.setOpaque(false);
                panel1.setBorder(Borders.DLU4_BORDER);
                panel1.setLayout(new FormLayout(
                    ColumnSpec.decodeSpecs("default:grow"),
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //======== contentPanel ========
                {
                    contentPanel.setOpaque(false);
                    contentPanel.setLayout(new FormLayout(
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
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC
                        }));

                    //---- label1 ----
                    label1.setText("Building");
                    ATFieldInfo.assignLabelInfo(label1, Locations.class, Locations.PROPERTYNAME_BUILDING);
                    contentPanel.add(label1, cc.xy(1, 1));
                    contentPanel.add(building, cc.xywh(3, 1, 9, 1));

                    //---- label2 ----
                    label2.setText("Floor");
                    ATFieldInfo.assignLabelInfo(label2, Locations.class, Locations.PROPERTYNAME_FLOOR);
                    contentPanel.add(label2, cc.xy(1, 3));
                    contentPanel.add(floor, cc.xywh(3, 3, 9, 1));

                    //---- label3 ----
                    label3.setText("Room");
                    ATFieldInfo.assignLabelInfo(label3, Locations.class, Locations.PROPERTYNAME_ROOM);
                    contentPanel.add(label3, cc.xy(1, 5));
                    contentPanel.add(room, cc.xywh(3, 5, 9, 1));

                    //---- label4 ----
                    label4.setText("Area");
                    ATFieldInfo.assignLabelInfo(label4, Locations.class, Locations.PROPERTYNAME_AREA);
                    contentPanel.add(label4, cc.xy(1, 7));
                    contentPanel.add(area, cc.xywh(3, 7, 9, 1));

                    //---- label12 ----
                    label12.setText("Label");
                    contentPanel.add(label12, cc.xy(3, 9));

                    //---- label13 ----
                    label13.setText("Start");
                    contentPanel.add(label13, cc.xy(5, 9));

                    //---- label14 ----
                    label14.setText("End");
                    contentPanel.add(label14, cc.xy(9, 9));

                    //---- label5 ----
                    label5.setText("Coordinate 1");
                    contentPanel.add(label5, cc.xy(1, 11));

                    //---- coordinate1Label ----
                    coordinate1Label.setColumns(8);
                    contentPanel.add(coordinate1Label, cc.xy(3, 11));

                    //---- coordinate1Start ----
                    coordinate1Start.setColumns(3);
                    contentPanel.add(coordinate1Start, cc.xy(5, 11));

                    //---- label6 ----
                    label6.setText("-");
                    contentPanel.add(label6, cc.xy(7, 11));

                    //---- coordinate1End ----
                    coordinate1End.setColumns(3);
                    contentPanel.add(coordinate1End, cc.xy(9, 11));

                    //---- label7 ----
                    label7.setText("Coordinate 2");
                    contentPanel.add(label7, cc.xy(1, 13));

                    //---- coordinate2Label ----
                    coordinate2Label.setColumns(8);
                    contentPanel.add(coordinate2Label, cc.xy(3, 13));

                    //---- coordinate2Start ----
                    coordinate2Start.setColumns(3);
                    contentPanel.add(coordinate2Start, cc.xy(5, 13));

                    //---- label9 ----
                    label9.setText("-");
                    contentPanel.add(label9, cc.xy(7, 13));

                    //---- coordinate2End ----
                    coordinate2End.setColumns(3);
                    contentPanel.add(coordinate2End, cc.xy(9, 13));

                    //---- label8 ----
                    label8.setText("Coordinate 3");
                    contentPanel.add(label8, cc.xy(1, 15));

                    //---- coordinate3Label ----
                    coordinate3Label.setColumns(8);
                    contentPanel.add(coordinate3Label, cc.xy(3, 15));

                    //---- coordinate3Start ----
                    coordinate3Start.setColumns(3);
                    contentPanel.add(coordinate3Start, cc.xy(5, 15));

                    //---- label10 ----
                    label10.setText("-");
                    contentPanel.add(label10, cc.xy(7, 15));

                    //---- coordinate3End ----
                    coordinate3End.setColumns(3);
                    contentPanel.add(coordinate3End, cc.xy(9, 15));

                    //---- label11 ----
                    label11.setText("Repository");
                    label11.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    ATFieldInfo.assignLabelInfo(label11, Locations.class, Locations.PROPERTYNAME_REPOSITORY);
                    contentPanel.add(label11, cc.xy(1, 17));

                    //---- repository ----
                    repository.setOpaque(false);
                    repository.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                    contentPanel.add(repository, cc.xywh(3, 17, 9, 1));
                }
                panel1.add(contentPanel, cc.xy(1, 1));

                //======== buttonBar ========
                {
                    buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                    buttonBar.setOpaque(false);
                    buttonBar.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.GLUE_COLSPEC,
                            FormFactory.BUTTON_COLSPEC,
                            FormFactory.RELATED_GAP_COLSPEC,
                            FormFactory.BUTTON_COLSPEC
                        },
                        RowSpec.decodeSpecs("pref")));

                    //---- generateButton ----
                    generateButton.setText("Generate");
                    generateButton.setOpaque(false);
                    generateButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            generateButtonActionPerformed(e);
                        }
                    });
                    buttonBar.add(generateButton, cc.xy(2, 1));

                    //---- doneButton ----
                    doneButton.setText("Done");
                    doneButton.setOpaque(false);
                    doneButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            doneButtonActionPerformed(e);
                        }
                    });
                    buttonBar.add(doneButton, cc.xy(4, 1));
                }
                panel1.add(buttonBar, cc.xywh(1, 3, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
            }
            dialogPane.add(panel1, cc.xy(1, 3));
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	private Integer count = 0;
	private int numberOfLocationsToCreate;
	private StringBuffer errorBuffer = new StringBuffer();

	private void generateButtonActionPerformed(ActionEvent e) {
		final DomainAccessObject access = new DomainAccessObjectImpl(Locations.class);
		count = 0;
		try {
			validateLocation();
			final String buildingText = getBuilding();
			final String floorText = getFloor();
			final String roomText = getRoom();
			final String areaText = getArea();
			final String coordinate1Text = getCoordinate1Label();
			final String coordinate2Text = getCoordinate2Label();
			final String coordinate3Text = getCoordinate3Label();
			final ArrayList<String> coordinate1List = generateRange(coordinate1Start.getText(), coordinate1End.getText());
			final ArrayList<String> coordinate2List = generateRange(coordinate2Start.getText(), coordinate2End.getText());
			final ArrayList<String> coordinate3List = generateRange(coordinate3Start.getText(), coordinate3End.getText());

			boolean createLocations = true;

			numberOfLocationsToCreate = 1;
			if (coordinate1List != null) {
				numberOfLocationsToCreate *= coordinate1List.size();
			}
			if (coordinate2List != null) {
				numberOfLocationsToCreate *= coordinate2List.size();
			}
			if (coordinate3List != null) {
				numberOfLocationsToCreate *= coordinate3List.size();
			}

			if (numberOfLocationsToCreate >= WARNING_MESSAGE_THRESHOLD) {
				if (JOptionPane.showConfirmDialog(this,
						"Are you sure you want to create " + numberOfLocationsToCreate + " location records?",
						"Create Locations", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
					createLocations = false;
				}
			}

			if (createLocations) {
				Thread performer = new Thread(new Runnable() {
					public void run() {
						InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
						monitor.start("Creating locations...");
						try {
							count = createLocations(access, buildingText, floorText, roomText, areaText,
									coordinate1Text, coordinate2Text, coordinate3Text, coordinate1List,
									coordinate2List, coordinate3List, monitor);
							monitor.close();
							finishLocationCreation();
						} finally {
							monitor.close();
						}
					}
				}, "Creating locations");
				performer.start();
			}

		} catch (MismatchValuesException e1) {
			new ErrorDialog(e1.getMessage(), ErrorDialog.DIALOG_TYPE_ERROR).showDialog();
		} catch (ValidationException e1) {
			//do nothing and just exit. The validator already presented a dialog
		}
	}

	private void finishLocationCreation() {
		// alert the user that the location(s) were added and if any wasn't tell then also
		String message = count + " location record(s) created";
		String errorString = errorBuffer.toString();

		if (errorString.length() != 0) {
			message += "\n\nNote, the following location(s) were not\n" +
					"created because they already exist or there\n" +
					"was a problem saving to the database:\n" + errorString;
		}

		JOptionPane.showMessageDialog(this, message);
	}

	private int createLocations(DomainAccessObject access,
								String buildingText,
								String floorText,
								String roomText,
								String areaText,
								String coordinate1Text,
								String coordinate2Text,
								String coordinate3Text,
								ArrayList<String> coordinate1List,
								ArrayList<String> coordinate2List,
								ArrayList<String> coordinate3List,
								InfiniteProgressPanel monitor) {
		Locations location;
		if (coordinate1List == null) {
			location = new Locations();
			location.setBuilding(buildingText);
			location.setRoom(roomText);
			location.setFloor(floorText);
			location.setArea(areaText);
			if (useRepositoryPopup) {
				location.setRepository(getRepository());
			}
			count += addLocationToDAO(access, location, errorBuffer);
			monitor.setTextLine(count + " of " + numberOfLocationsToCreate, 2);
		} else {
			for (String coordinate1 : coordinate1List) {
				if (coordinate2List == null) {
					location = new Locations();
					location.setBuilding(buildingText);
					location.setRoom(roomText);
					location.setFloor(floorText);
					location.setArea(areaText);
					location.setCoordinate1Label(coordinate1Text);
					if (Character.isDigit(coordinate1.charAt(0))) {
						location.setCoordinate1NumericIndicator(new Double(coordinate1));
					} else {
						location.setCoordinate1AlphaNumIndicator(coordinate1);
					}
					if (useRepositoryPopup) {
						location.setRepository(getRepository());
					}
					//access.add(location);
					//LocationsUtils.addLocationToLookupList(location);
					//count++;
					count += addLocationToDAO(access, location, errorBuffer);
					monitor.setTextLine(count + " of " + numberOfLocationsToCreate, 2);
				} else {
					for (String coordinate2 : coordinate2List) {
						if (coordinate3List == null) {
							location = new Locations();
							location.setBuilding(buildingText);
							location.setRoom(roomText);
							location.setFloor(floorText);
							location.setArea(areaText);
							location.setCoordinate1Label(coordinate1Text);
							if (Character.isDigit(coordinate1.charAt(0))) {
								location.setCoordinate1NumericIndicator(new Double(coordinate1));
							} else {
								location.setCoordinate1AlphaNumIndicator(coordinate1);
							}

							location.setCoordinate2Label(coordinate2Text);
							if (Character.isDigit(coordinate2.charAt(0))) {
								location.setCoordinate2NumericIndicator(new Double(coordinate2));
							} else {
								location.setCoordinate2AlphaNumIndicator(coordinate2);
							}

							if (useRepositoryPopup) {
								location.setRepository(getRepository());
							}
							//access.add(location);
							//LocationsUtils.addLocationToLookupList(location);
							//count++;
							count += addLocationToDAO(access, location, errorBuffer);
							monitor.setTextLine(count + " of " + numberOfLocationsToCreate, 2);
						} else {
							for (String coordinate3 : coordinate3List) {
								location = new Locations();
								location.setBuilding(buildingText);
								location.setRoom(roomText);
								location.setFloor(floorText);
								location.setArea(areaText);
								location.setCoordinate1Label(coordinate1Text);
								if (Character.isDigit(coordinate1.charAt(0))) {
									location.setCoordinate1NumericIndicator(new Double(coordinate1));
								} else {
									location.setCoordinate1AlphaNumIndicator(coordinate1);
								}

								location.setCoordinate2Label(coordinate2Text);
								if (Character.isDigit(coordinate2.charAt(0))) {
									location.setCoordinate2NumericIndicator(new Double(coordinate2));
								} else {
									location.setCoordinate2AlphaNumIndicator(coordinate2);
								}

								location.setCoordinate3Label(coordinate3Text);
								if (Character.isDigit(coordinate3.charAt(0))) {
									location.setCoordinate3NumericIndicator(new Double(coordinate3));
								} else {
									location.setCoordinate3AlphaNumIndicator(coordinate3);
								}

								if (useRepositoryPopup) {
									location.setRepository(getRepository());
								}
								count += addLocationToDAO(access, location, errorBuffer);
								monitor.setTextLine(count + " of " + numberOfLocationsToCreate, 2);
							}
						}
					}
				}
			}
		}
		return count;
	}

	// Method to catch any errors when adding locations
	public int addLocationToDAO(DomainAccessObject access, Locations location, StringBuffer errorBuffer) {
		int count = 0;

		try {
			access.add(location);
			LocationsUtils.addLocationToLookupList(location);
			count = 1;
		} catch (PersistenceException e) {
			// add text to string buffer indicating that the following location failed to add
			errorBuffer.append(location.toString()).append('\n');
		}

		return count;
	}

	public Repositories getRepository() {
		return (Repositories) repository.getSelectedItem();
	}

	public String getCoordinate3Label() {
		return coordinate3Label.getText();
	}

	public String getCoordinate2Label() {
		return coordinate2Label.getText();
	}

	public String getCoordinate1Label() {
		return coordinate1Label.getText();
	}

	public String getArea() {
		return area.getText();
	}

	public String getRoom() {
		return room.getText();
	}

	public String getFloor() {
		return floor.getText();
	}

	public String getBuilding() {
		return building.getText();
	}

//	private ArrayList<String> generateRange(String start) throws MismatchValuesException {
//		return generateRange(start, start);
//	}

	private void validateLocation() throws ValidationException {
		ValidationResult validationResult = validator.validate();
		if (validationResult.hasErrors()) {
			JGoodiesValidationUtils.showValidationMessage(
					this,
					"To save the record, please fix the following errors:",
					validationResult);
			throw new ValidationException("Invalid data");
		}
	}

	private ArrayList<String> generateRange(String start, String end) throws MismatchValuesException {

		char startChar;
		char endChar;
		if (start.length() == 0 && end.length() == 0) {
			return null;

		} else if (end.length() == 0) {
			ArrayList<String> returnArray = new ArrayList<String>();
			returnArray.add(start);
			return returnArray;

		} else if (start.length() == 0) {
			ArrayList<String> returnArray = new ArrayList<String>();
			returnArray.add(end);
			return returnArray;

		} else {
			startChar = start.toCharArray()[0];
			endChar = end.toCharArray()[0];
		}

		//find out if we are dealing with numbers or letters
		if (Character.isDigit(start.charAt(0)) && Character.isDigit(end.charAt(0))) {
			//we have numbers
			ArrayList<String> returnArray = new ArrayList<String>();
			try {
				int startInt = Integer.parseInt(start);
				int endInt = Integer.parseInt(end);
				if (startInt > endInt) {
					int temp = startInt;
					startInt = endInt;
					endInt = temp;
				}
				for (int i = startInt; i <= endInt; i++) {
					returnArray.add(Integer.toString(i));
				}
			} catch (NumberFormatException e) {
				throw new MismatchValuesException("Start: " + start + " - End: " + end
						+ "\n\nWhen specifying a range both start and finish must be either numbers or single characters");
			}
			return returnArray;

		} else if (Character.isLetter(start.charAt(0)) && Character.isLetter(end.charAt(0))) {
			if (start.length() > 1 || end.length() > 1) {
				throw new MismatchValuesException("Start: " + start + " - End: " + end
						+ "\n\nWhen specifying a range both start and finish must be either numbers or single characters");
			}
			int startValue = Character.getNumericValue(startChar);
			int endValue = Character.getNumericValue(endChar);
			if (startValue > endValue) {
				int temp = endValue;
				endValue = startValue;
				startValue = temp;
			}
			ArrayList<String> returnArray = new ArrayList<String>();
			char tmpChar = startChar;
			for (int i = startValue; i <= endValue; i++) {
				returnArray.add(Character.toString(tmpChar++));
			}
			return returnArray;

		} else {
			//mismatch
			throw new MismatchValuesException("Start: " + start + " - End: " + end
					+ "\n\nWhen specifying a range both start and finish must be either numbers or single characters");
		}
	}

	private void doneButtonActionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel HeaderPanel;
    private JPanel panel2;
    private JLabel mainHeaderLabel;
    private JPanel panel3;
    private JLabel subHeaderLabel;
    private JPanel panel1;
    private JPanel contentPanel;
    private JLabel label1;
    private JTextField building;
    private JLabel label2;
    private JTextField floor;
    private JLabel label3;
    private JTextField room;
    private JLabel label4;
    private JTextField area;
    private JLabel label12;
    private JLabel label13;
    private JLabel label14;
    private JLabel label5;
    private JTextField coordinate1Label;
    private JTextField coordinate1Start;
    private JLabel label6;
    private JTextField coordinate1End;
    private JLabel label7;
    private JTextField coordinate2Label;
    private JTextField coordinate2Start;
    private JLabel label9;
    private JTextField coordinate2End;
    private JLabel label8;
    private JTextField coordinate3Label;
    private JTextField coordinate3Start;
    private JLabel label10;
    private JTextField coordinate3End;
    private JLabel label11;
    public JComboBox repository;
    private JPanel buttonBar;
    private JButton generateButton;
    private JButton doneButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	private JTextField repositoryTextField = new JTextField();
	private Boolean useRepositoryPopup = true;

	public final void showDialog() {

		this.pack();
		setLocationRelativeTo(null);
		//populate default values
		assignDefaultValue(Locations.PROPERTYNAME_BUILDING, building);
		assignDefaultValue(Locations.PROPERTYNAME_AREA, area);
		assignDefaultValue(Locations.PROPERTYNAME_FLOOR, floor);
		assignDefaultValue(Locations.PROPERTYNAME_ROOM, room);
		assignDefaultValue(Locations.PROPERTYNAME_COORDINATE_1_LABEL, coordinate1Label);
		assignDefaultValue(Locations.PROPERTYNAME_COORDINATE_2_LABEL, coordinate2Label);
		assignDefaultValue(Locations.PROPERTYNAME_COORDINATE_3_LABEL, coordinate3Label);

		repositoryTextField.setText(ApplicationFrame.getInstance().getCurrentUserRepository().getRepositoryName());
		repository.setSelectedItem(ApplicationFrame.getInstance().getCurrentUserRepository());
		this.setVisible(true);
	}

	private void assignDefaultValue(String fieldName, JTextField textField) {
		DefaultValues value = DefaultValues.getDefaultValue(ApplicationFrame.getInstance().getCurrentUserRepository(), Locations.class.getSimpleName(), fieldName);
		if (value != null) {
			textField.setText(value.getStringValue());
		}
	}

	private void hideRepositoryPopupIfNecessary() {
		if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_SUPERUSER)) {
			repositoryTextField.setOpaque(false);
			repositoryTextField.setEditable(false);
			FormLayout formLayout = (FormLayout) contentPanel.getLayout();
			CellConstraints cc = formLayout.getConstraints(repository);
			contentPanel.remove(repository);
			contentPanel.add(repositoryTextField, cc);
			useRepositoryPopup = false;
		}
	}


}
