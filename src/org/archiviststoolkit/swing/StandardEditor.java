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
 * Created by JFormDesigner on Wed Sep 28 10:18:47 EDT 2005
 */

package org.archiviststoolkit.swing;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.UnsupportedTableModelException;
import org.archiviststoolkit.ApplicationFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.util.Date;

public class StandardEditor extends JDialog implements ActionListener {

	public static final int OK_AND_ANOTHER_OPTION = 10;

    public static final int NO_SAVE_OPTION = 20;

    public static final int ALREADY_SAVED = 30; // used to that the object is already saved

    public static final String MODULE_HEADER_ACCESSIONS = "Accessions";
	public static final Color MODULE_HEADER_COLOR_ACCESSIONS = new Color(3, 78, 88);

	public static final String MODULE_HEADER_RESOURCES = "Resources";
	public static final String MODULE_HEADER_RESOURCES_COMPONENT = "Resource Component";
	public static final Color MODULE_HEADER_COLOR_RESOURCES = new Color(73, 43, 104);

	public static final String MODULE_HEADER_DIGITAL_OBJECTS = "Digital Objects  ";
	public static final String MODULE_HEADER_NOTE = "Resources-Note";
	public static final Color MODULE_HEADER_COLOR_DIGITAL_OBJECTS = new Color(121, 104, 56);

	public static final String MODULE_HEADER_SUBJECTS = "Subjects";
	public static final Color MODULE_HEADER_COLOR_SUBJECTS = new Color(80, 35, 45);

	public static final String MODULE_HEADER_NAMES = "Names";
	public static final Color MODULE_HEADER_COLOR_NAMES = new Color(80, 86, 45);

    public static final String MODULE_HEADER_ASSESSMENTS = "Assessments";
	public static final Color MODULE_HEADER_COLOR_ASSESSMENTS = new Color(80, 35, 45);

	public static final String MODULE_HEADER_ADMINISTRATION = "Administration";
	public static final Color MODULE_HEADER_COLOR_ADMINISTRATION = new Color(80, 69, 57);

	public static final String MODULE_HEADER_CONFIGURE_APPLICATION = "Configure Application";
	public static final String MODULE_HEADER_REPOSITORIES = "Repositories";

	public static final String MODULE_SUB_HEADER_BLANK = " ";
	public static final String MODULE_SUB_HEADER_NON_PREFERRED_NAMES = "Non Preferred Names";
	public static final String MODULE_SUB_HEADER_NAME_CONTACT_NOTES = "Contact Notes";
	public static final String MODULE_SUB_HEADER_RESOURCE_ANALOG_INSTANCE = "Analog Instance";
	public static final String MODULE_SUB_HEADER_RESOURCE_DIGITAL_INSTANCE = "Digital Object";
	public static final String MODULE_SUB_HEADER_EXTERNAL_REFERENCE = "External Documents";
	public static final String MODULE_SUB_HEADER_TEXT = "Text";
	public static final String MODULE_SUB_HEADER_NOTES = "Notes";
	public static final String MODULE_SUB_HEADER_BIBLIOGORAPHY = "Bibliography";
	public static final String MODULE_SUB_HEADER_CHRONOLOGY = "Chronology";
	public static final String MODULE_SUB_HEADER_CHRONOLOGY_ITEMS = "Chronology items";
	public static final String MODULE_SUB_HEADER_INDEX = "Index";
	public static final String MODULE_SUB_HEADER_LIST_ORDERED = "List: ordered";
	public static final String MODULE_SUB_HEADER_LIST_DEFINITION = "List: definition";
	public static final String MODULE_SUB_HEADER_FILE_VERSIONS = "File Version";
	public static final String MODULE_SUB_HEADER_DEACCESSIONS = "Deaccession";
	public static final String MODULE_SUB_HEADER_EVENTS = "Chronology items - Events";
	public static final String MODULE_SUB_HEADER_DATABASE_TABLES = "Database Tables";
	public static final String MODULE_SUB_HEADER_DATABASE_FIELDS = "Database Fields";
	public static final String MODULE_SUB_HEADER_DEFAULT_VALUES = "Default Values";
	public static final String MODULE_SUB_HEADER_NOTES_DEFAULT_VALUES = "Notes Default Values";
	public static final String MODULE_SUB_HEADER_LOCATIONS = "Locations";
	public static final String MODULE_SUB_HEADER_REPOSITORIES = "Repositories";
	public static final String MODULE_SUB_HEADER_REPOSITORY_STATISTICS = "Statistics";
	public static final String MODULE_SUB_HEADER_USERS = "Users";
	public static final String MODULE_SUB_HEADER_LOOKUP_LISTS = "Lookup Lists";
	public static final String MODULE_SUB_HEADER_RDE = "Rapid Data Entry Screen";
	public static final String MODULE_SUB_HEADER_RDE_PANEL = "Rapid Data Entry Screen Panel";
	public static final String MODULE_SUB_HEADER_LOOKUP_LIST_ITEMS = "Lookup List Items";
	public static final String MODULE_SUB_HEADER_NOTES_ETC = "Notes etc.";
	public static final String MODULE_SUB_HEADER_CONSTANTS = "Date Format Setting";

    private boolean showConfirmDialog = true; // specifies wheather to keep showing the confirm close dialog
    protected int choice; // this is set by the return value of the confirm close dialog
    protected DomainObject model = null;
    private Thread showThread;
    private boolean editorDone = false; // this is used for waiting on a thread
    protected Boolean readOnly = false; // ue this to set the read only status

    public static void setMainHeaderColorAndTextByClass(Class clazz, JPanel mainHeaderPanel, JLabel mainHeaderLabel) {
		if (clazz == Accessions.class) {
			mainHeaderPanel.setBackground(StandardEditor.MODULE_HEADER_COLOR_ACCESSIONS);
			mainHeaderLabel.setText(StandardEditor.MODULE_HEADER_ACCESSIONS);
		} else if (clazz == Resources.class) {
			mainHeaderPanel.setBackground(StandardEditor.MODULE_HEADER_COLOR_RESOURCES);
			mainHeaderLabel.setText(StandardEditor.MODULE_HEADER_RESOURCES);
		} else if (clazz == Names.class) {
			mainHeaderPanel.setBackground(StandardEditor.MODULE_HEADER_COLOR_NAMES);
			mainHeaderLabel.setText(StandardEditor.MODULE_HEADER_NAMES);
		} else if (clazz == Subjects.class) {
			mainHeaderPanel.setBackground(StandardEditor.MODULE_HEADER_COLOR_SUBJECTS);
			mainHeaderLabel.setText(StandardEditor.MODULE_HEADER_SUBJECTS);
		} else if (clazz == DigitalObjects.class) {
			mainHeaderPanel.setBackground(StandardEditor.MODULE_HEADER_COLOR_DIGITAL_OBJECTS);
			mainHeaderLabel.setText(StandardEditor.MODULE_HEADER_DIGITAL_OBJECTS);
		} else if (clazz == Locations.class) {
			mainHeaderPanel.setBackground(StandardEditor.MODULE_HEADER_COLOR_ADMINISTRATION);
			mainHeaderLabel.setText(StandardEditor.MODULE_SUB_HEADER_LOCATIONS);
		} else if (clazz == Assessments.class) {
			mainHeaderPanel.setBackground(StandardEditor.MODULE_HEADER_COLOR_ASSESSMENTS);
			mainHeaderLabel.setText(StandardEditor.MODULE_HEADER_ASSESSMENTS);
		}
	}

//	public StandardEditor() {
//		initComponents();
//
//	}
//
//	public StandardEditor(String mainHeader) {
//		initComponents();
//		this.mainHeaderLabel.setText(mainHeader);
//		this.subHeaderLabel.setText(" ");
//	}
//
//	public StandardEditor(String mainHeader, String subHeader) {
//		initComponents();
//		this.mainHeaderLabel.setText(mainHeader);
//		this.subHeaderLabel.setText(subHeader);
//	}

	public StandardEditor(Frame owner) {
		super(owner);
		initComponents();
	}

	public StandardEditor(Dialog owner) {
		super(owner);
		initComponents();
	}

	public StandardEditor(Frame owner, String mainHeader) {
//		super(owner, mainHeader);
		super(owner);
		initComponents();
		this.mainHeaderLabel.setText(mainHeader + " ");
		this.subHeaderLabel.setText(" ");
	}

	public StandardEditor(Frame owner, String mainHeader, String subHeader) {
//		super(owner, mainHeader);
		super(owner);
		initComponents();
		this.mainHeaderLabel.setText(mainHeader + " ");
		this.subHeaderLabel.setText(subHeader);
	}

	public StandardEditor(Dialog owner, String mainHeader) {
//		super(owner, mainHeader);
		super(owner);
		initComponents();
		this.mainHeaderLabel.setText(mainHeader + " ");
		this.subHeaderLabel.setText(" ");
	}

	public StandardEditor(Dialog owner, String mainHeader, String subHeader) {
//		super(owner, mainHeader);
		super(owner);
		initComponents();
		this.mainHeaderLabel.setText(mainHeader + " ");
		this.subHeaderLabel.setText(subHeader);
	}

	public JButton getOkAndAnotherButton() {
		return okAndAnotherButton;
	}

	public JLabel getSubHeaderLabel() {
		return subHeaderLabel;
	}

	public JPanel getMainHeaderPanel() {
		return mainHeaderPanel;
	}

	public JPanel getSubHeaderPanel() {
		return subHeaderPanel;
	}

	private void thisWindowClosing(WindowEvent e) {
		status = JOptionPane.CANCEL_OPTION;
	}

	public JLabel getPrintLabel() {
		return printLabel;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		mainPanel = new JPanel();
		HeaderPanel = new JPanel();
		mainHeaderPanel = new JPanel();
		mainHeaderLabel = new JLabel();
		subHeaderPanel = new JPanel();
		subHeaderLabel = new JLabel();
		recordPosition = new JLabel();
		contentPanel = new JPanel();
		button1 = new JButton();
		separator1 = new JSeparator();
		buttonPanel = new JPanel();
		firstButton = new JButton();
		previousButton = new JButton();
		nextButton = new JButton();
		lastButton = new JButton();
		printButton = new JButton();
		cancelButton = new JButton();
		okButton = new JButton();
		saveButton = new JButton();
		okAndAnotherButton = new JButton();
		firstLabel = new JLabel();
		previousLabel = new JLabel();
		nextLabel = new JLabel();
		lastLabel = new JLabel();
		printLabel = new JLabel();
		cancelButtonLabel = new JLabel();
		okButtonLabel = new JLabel();
		saveButtonLabel = new JLabel();
		okAndAnotherButtonLabel = new JLabel();
		separator2 = new JSeparator();
		gradientPanel2 = new JPanel();
		auditLabel = new JLabel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setBackground(new Color(200, 205, 232));
		setModal(true);
		setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				thisWindowClosing(e);
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			"default:grow",
			"top:default:grow"));

		//======== mainPanel ========
		{
			mainPanel.setBackground(new Color(200, 205, 232));
			mainPanel.setLayout(new FormLayout(
				ColumnSpec.decodeSpecs("default:grow"),
				new RowSpec[] {
					new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
					FormFactory.LINE_GAP_ROWSPEC,
					new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}));

			//======== HeaderPanel ========
			{
				HeaderPanel.setBackground(new Color(80, 69, 57));
				HeaderPanel.setOpaque(false);
				HeaderPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				HeaderPanel.setPreferredSize(new Dimension(300, 30));
				HeaderPanel.setLayout(new FormLayout(
					new ColumnSpec[] {
						new ColumnSpec(Sizes.bounded(Sizes.MINIMUM, Sizes.dluX(100), Sizes.dluX(200))),
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
					},
					RowSpec.decodeSpecs("fill:default")));

				//======== mainHeaderPanel ========
				{
					mainHeaderPanel.setBackground(new Color(80, 69, 57));
					mainHeaderPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					mainHeaderPanel.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.RELATED_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC
						},
						new RowSpec[] {
							FormFactory.RELATED_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.RELATED_GAP_ROWSPEC
						}));

					//---- mainHeaderLabel ----
					mainHeaderLabel.setText("Main Header");
					mainHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
					mainHeaderLabel.setForeground(Color.white);
					mainHeaderPanel.add(mainHeaderLabel, cc.xy(2, 2));
				}
				HeaderPanel.add(mainHeaderPanel, cc.xy(1, 1));

				//======== subHeaderPanel ========
				{
					subHeaderPanel.setBackground(new Color(66, 60, 111));
					subHeaderPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					subHeaderPanel.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.RELATED_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.UNRELATED_GAP_COLSPEC
						},
						new RowSpec[] {
							FormFactory.RELATED_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.RELATED_GAP_ROWSPEC
						}));

					//---- subHeaderLabel ----
					subHeaderLabel.setText("Sub Header");
					subHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
					subHeaderLabel.setForeground(Color.white);
					subHeaderPanel.add(subHeaderLabel, cc.xy(2, 2));

					//---- recordPosition ----
					recordPosition.setText("record x of n");
					recordPosition.setForeground(Color.white);
					subHeaderPanel.add(recordPosition, cc.xywh(4, 2, 1, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
				}
				HeaderPanel.add(subHeaderPanel, cc.xy(2, 1));
			}
			mainPanel.add(HeaderPanel, cc.xywh(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

			//======== contentPanel ========
			{
				contentPanel.setBorder(Borders.DLU4_BORDER);
				contentPanel.setBackground(new Color(200, 205, 232));
				contentPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contentPanel.setLayout(new FormLayout(
					"center:default:grow",
					"top:default:grow"));

				//---- button1 ----
				button1.setText("text");
				contentPanel.add(button1, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL));
			}
			mainPanel.add(contentPanel, cc.xy(1, 3));

			//---- separator1 ----
			separator1.setBackground(new Color(220, 220, 232));
			separator1.setForeground(new Color(147, 131, 86));
			separator1.setMinimumSize(new Dimension(1, 10));
			separator1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			mainPanel.add(separator1, cc.xy(1, 5));

			//======== buttonPanel ========
			{
				buttonPanel.setBorder(null);
				buttonPanel.setBackground(new Color(200, 205, 232));
				buttonPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				buttonPanel.setMinimumSize(new Dimension(380, 60));
				buttonPanel.setLayout(new FormLayout(
					"default, default, default, default, default, left:10dlu, default, 10dlu, default, default, default, default",
					"default, default"));

				//---- firstButton ----
				firstButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/firstRecord.jpg")));
				firstButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				firstButton.setOpaque(false);
				buttonPanel.add(firstButton, cc.xy(2, 1));

				//---- previousButton ----
				previousButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/prevRecord.jpg")));
				previousButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				previousButton.setOpaque(false);
				buttonPanel.add(previousButton, cc.xy(3, 1));

				//---- nextButton ----
				nextButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/nextRecord.jpg")));
				nextButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				nextButton.setOpaque(false);
				buttonPanel.add(nextButton, cc.xy(4, 1));

				//---- lastButton ----
				lastButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/lastRecord.jpg")));
				lastButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				lastButton.setOpaque(false);
				buttonPanel.add(lastButton, cc.xy(5, 1));

				//---- printButton ----
				printButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/print.jpg")));
				printButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				printButton.setOpaque(false);
				buttonPanel.add(printButton, cc.xy(7, 1));

				//---- cancelButton ----
				cancelButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/cancel.jpg")));
				cancelButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				cancelButton.setOpaque(false);
				buttonPanel.add(cancelButton, cc.xy(9, 1));

				//---- okButton ----
				okButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/ok.jpg")));
				okButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				okButton.setOpaque(false);
				buttonPanel.add(okButton, cc.xy(10, 1));

				//---- saveButton ----
				saveButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/save.jpg")));
				saveButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				saveButton.setOpaque(false);
				buttonPanel.add(saveButton, cc.xy(11, 1));

				//---- okAndAnotherButton ----
				okAndAnotherButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/savePlus1.jpg")));
				okAndAnotherButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				okAndAnotherButton.setOpaque(false);
				buttonPanel.add(okAndAnotherButton, cc.xy(12, 1));

				//---- firstLabel ----
				firstLabel.setText("First");
				firstLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				buttonPanel.add(firstLabel, cc.xywh(2, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

				//---- previousLabel ----
				previousLabel.setText("Previous");
				previousLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				buttonPanel.add(previousLabel, cc.xywh(3, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

				//---- nextLabel ----
				nextLabel.setText("Next");
				nextLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				buttonPanel.add(nextLabel, cc.xywh(4, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

				//---- lastLabel ----
				lastLabel.setText("Last");
				lastLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				buttonPanel.add(lastLabel, cc.xywh(5, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

				//---- printLabel ----
				printLabel.setText("Reports");
				printLabel.setHorizontalAlignment(SwingConstants.CENTER);
				printLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				buttonPanel.add(printLabel, cc.xy(7, 2));

				//---- cancelButtonLabel ----
				cancelButtonLabel.setText("Cancel");
				cancelButtonLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				buttonPanel.add(cancelButtonLabel, cc.xywh(9, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

				//---- okButtonLabel ----
				okButtonLabel.setText("OK");
				okButtonLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				buttonPanel.add(okButtonLabel, cc.xywh(10, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

				//---- saveButtonLabel ----
				saveButtonLabel.setText("Save");
				saveButtonLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				buttonPanel.add(saveButtonLabel, cc.xywh(11, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

				//---- okAndAnotherButtonLabel ----
				okAndAnotherButtonLabel.setText("+ 1");
				okAndAnotherButtonLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				buttonPanel.add(okAndAnotherButtonLabel, cc.xywh(12, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
			}
			mainPanel.add(buttonPanel, cc.xywh(1, 7, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

			//---- separator2 ----
			separator2.setBackground(new Color(220, 220, 232));
			separator2.setForeground(new Color(147, 131, 86));
			separator2.setMinimumSize(new Dimension(1, 10));
			separator2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			mainPanel.add(separator2, cc.xy(1, 9));

			//======== gradientPanel2 ========
			{
				gradientPanel2.setBackground(new Color(200, 205, 232));
				gradientPanel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				gradientPanel2.setLayout(new FormLayout(
					ColumnSpec.decodeSpecs("default:grow"),
					new RowSpec[] {
						new RowSpec("35px"),
						FormFactory.UNRELATED_GAP_ROWSPEC
					}));

				//---- auditLabel ----
				auditLabel.setText("created | modified");
				auditLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				gradientPanel2.add(auditLabel, new CellConstraints(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 10, 0, 0)));
			}
			mainPanel.add(gradientPanel2, cc.xy(1, 11));
		}
		contentPane.add(mainPanel, cc.xywh(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		final StandardEditor editor = this;
		WindowAdapter windowAdapter = new WindowAdapter() {
			public void windowClosing(final WindowEvent windowEvent) {
        if (saveButton.isVisible()) {
            if(readOnly) {
                closeAndNoSave();
            }
            else if (showCloseConfirmDialog(0) == JOptionPane.YES_OPTION) {
                if (!model.validateAndDisplayDialog(windowEvent)) { // didnot validate so don't save it
                    choice = -1;
                } else {
                    setVisible(false);
                }
            }
        }
        else if (JOptionPane.showConfirmDialog(editor, "Clicking the close box is the same as clicking cancel. All changes will not be saved.\n" +
						"Are you sure you wish to do this?", "Confirm cancel", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					myIsDialogCancelled = true;

					setVisible(false);
				}
			}
		};

		addWindowListener(windowAdapter);
		this.getRootPane().setDefaultButton(this.getOkButton());
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel mainPanel;
	private JPanel HeaderPanel;
	private JPanel mainHeaderPanel;
	private JLabel mainHeaderLabel;
	private JPanel subHeaderPanel;
	private JLabel subHeaderLabel;
	private JLabel recordPosition;
	private JPanel contentPanel;
	private JButton button1;
	private JSeparator separator1;
	private JPanel buttonPanel;
	private JButton firstButton;
	private JButton previousButton;
	private JButton nextButton;
	private JButton lastButton;
	private JButton printButton;
	protected JButton cancelButton;
	protected JButton okButton;
	protected JButton saveButton;
	protected JButton okAndAnotherButton;
	private JLabel firstLabel;
	private JLabel previousLabel;
	private JLabel nextLabel;
	private JLabel lastLabel;
	private JLabel printLabel;
	private JLabel cancelButtonLabel;
	private JLabel okButtonLabel;
	private JLabel saveButtonLabel;
	private JLabel okAndAnotherButtonLabel;
	private JSeparator separator2;
	private JPanel gradientPanel2;
	private JLabel auditLabel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	/**
	 * The status of the editor.
	 */
	protected int status = 0;
	protected Boolean newRecord = false;
	protected Boolean savedNewRecord = false; // used to see if a new record was saved
	private Boolean includeSaveButton = false;
    private Boolean includeOkAndAnotherButton = true; // used to see if to hide the ok + 1 button

    /**
	 * Flag indicating if the "Cancel" button was pressed to close dialog.
	 */
	protected boolean myIsDialogCancelled = true;

	protected JPanel currentContentPanel = null;

	private JTable callingTable = null;
	protected int selectedRow;


	/**
	 * Check to see if the user has hit cancel.
	 *
	 * @return true if cancel button has been hit
	 */

	public final boolean hasUserCancelled() {
		return myIsDialogCancelled;
	}

	public final void addAuditPanel(DomainObject model) {
		DateFormat df = DateFormat.getDateInstance();
		Date dateCreated = model.creationDate();
		Date dateModified = model.lastUpdated();
		String createdBy = model.getCreatedBy();
		String modifiedBy = model.getLastUpdatedBy();
		Long recordNumber = model.getIdentifier();
		if (dateCreated == null) {
			this.auditLabel.setText("");
		} else {
			this.auditLabel.setText("Created: " + df.format(new Date(dateCreated.getTime())) + " by " + createdBy
					+ " | " + "Modified: " + df.format(new Date(dateModified.getTime())) + " by " + modifiedBy
					+ " | " + "Record Number: " + recordNumber);
		}
	}

	/**
	 * Displays the dialog box representing the editor.
	 *
	 * @return true if it displayed okay
	 */

	public int showDialog() {

        if (newRecord) {
            if (includeOkAndAnotherButton) {
                okAndAnotherButton.setVisible(true);
                okAndAnotherButtonLabel.setVisible(true);
            } else {
                okAndAnotherButton.setVisible(false);
                okAndAnotherButtonLabel.setVisible(false);
            }
            disableNavigationButtons();
        } else {
            okAndAnotherButton.setVisible(false);
            okAndAnotherButtonLabel.setVisible(false);
            enableNavigationButtons();
        }

        if (includeSaveButton) {
            saveButton.setVisible(true);
            saveButtonLabel.setVisible(true);
            cancelButton.setVisible(false);
            cancelButtonLabel.setVisible(false);
            okButtonLabel.setText("Close");
            okButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/cancel.jpg")));
            okAndAnotherButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/savePlus1.jpg")));
        } else {
            saveButton.setVisible(false);
            saveButtonLabel.setVisible(false);
            okButtonLabel.setText("OK");
            okButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/ok.jpg")));
            cancelButton.setVisible(true);
            cancelButtonLabel.setVisible(true);
            okAndAnotherButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/okPlus1.jpg")));
        }

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        if (callingTable != null && !newRecord) {
            setNavigationButtons();
        }

        status = JOptionPane.CLOSED_OPTION;

        if (!SwingUtilities.isEventDispatchThread()) {
            editorDone = false;

            showThread = new Thread(new Runnable() {
				public void run() {
					pack();
                    setVisible(true);
                    setEditorDone(true);
                }
			});

            SwingUtilities.invokeLater(showThread);

            // wait for dialog to close to continue
            try {
                while (editorDone == false) {
                    Thread.sleep(500);
                }
            } catch(InterruptedException ie) { }
        }
        else {
            this.pack();
            this.setVisible(true);
        }

        return (status);
	}

    //Method to set done true
    private void setEditorDone(boolean done) {
        editorDone = done;
    }

    // Method to show the confirm dialog
    public int showCloseConfirmDialog(int status) {
        if(readOnly) {
            choice = NO_SAVE_OPTION;
        } else if(status == StandardEditor.OK_AND_ANOTHER_OPTION) {
            choice = JOptionPane.YES_OPTION;
        } else if (saveButton.isVisible() && !ApplicationFrame.getInstance().getRecordDirty()) {
			if(choice == ALREADY_SAVED) {
                choice = JOptionPane.YES_OPTION;
            } else {
                choice = NO_SAVE_OPTION;
            }
            setVisible(false);
		} else if (saveButton.isVisible()) { // only used when save button is visible
            choice = JOptionPane.showConfirmDialog(this, "Do you want to save your changes?");

            if (choice == JOptionPane.NO_OPTION) { // if no then this window
                setVisible(false);
            }
        } else {
            choice = JOptionPane.YES_OPTION;
        }

        return choice;
    }

    // Method to set to return the return value for the Confirm Close Dialog
    public int getConfirmDialogReturn() {
        return choice;
    }

    /**
     * Method to close window and set choice to to aleart any action listeners not to save
     * the current record
     */
    public void closeAndNoSave() {
        choice = NO_SAVE_OPTION;
        setVisible(false);
    }

  /**
	 * capture and handle action events.
	 *
	 * @param ae the action event
	 */

	public void actionPerformed(final ActionEvent ae) {
		if (ae.getSource() == getOkButton()) {
			status = javax.swing.JOptionPane.OK_OPTION;
		} else if (ae.getSource() == getCancelButton()) {
			status = javax.swing.JOptionPane.CANCEL_OPTION;
		} else if (ae.getSource() == getOkAndAnotherButton()) {
			status = OK_AND_ANOTHER_OPTION;
		} else {
			status = javax.swing.JOptionPane.CANCEL_OPTION;
		}

		this.setVisible(false);
	}


	public JPanel getButtonPanel() {
		return buttonPanel;
	}

	public void setButtonPanel(JPanel buttonPanel) {
//		mainPaneAccessor.replaceBean("buttonPanel", buttonPanel);
	}

	public JPanel getContentPanel() {
		return contentPanel;
	}

	public void setContentPanel(DomainEditorFields newContentPanel) {
//		Container contentPane = getContentPane();
		mainPanel.remove(contentPanel);
		CellConstraints cc = new CellConstraints();
		mainPanel.add(newContentPanel, cc.xy(1, 3));
		this.contentPanel = newContentPanel;
	}

	public void disableNavigationButtons() {
		this.getFirstButton().setEnabled(false);
		this.getPreviousButton().setEnabled(false);
		this.getNextButton().setEnabled(false);
		this.getLastButton().setEnabled(false);
	}

	public void enableNavigationButtons() {
		this.getFirstButton().setEnabled(true);
		this.getPreviousButton().setEnabled(true);
		this.getNextButton().setEnabled(true);
		this.getLastButton().setEnabled(true);
	}

	public void hidePrintAndNavigationButtons() {
		this.getFirstButton().setVisible(false);
		firstLabel.setVisible(false);
		this.getPreviousButton().setVisible(false);
		previousLabel.setVisible(false);
		this.getNextButton().setVisible(false);
		nextLabel.setVisible(false);
		this.getLastButton().setVisible(false);
		lastLabel.setVisible(false);
		this.getPrintButton().setVisible(false);
		printLabel.setVisible(false);
		recordPosition.setVisible(false);
	}

  public void setNavigationButtons() {

		int numberOfRows = getCallingTable().getRowCount();
//        selectedRow = getCallingTable().getSelectedRow();
		if (numberOfRows <= 1) {
			selectedRow = 0;
			if (numberOfRows > 0) {
				getCallingTable().setRowSelectionInterval(selectedRow, selectedRow);
			}
			getPreviousButton().setEnabled(false);
			getFirstButton().setEnabled(false);
			getNextButton().setEnabled(false);
			getLastButton().setEnabled(false);

		} else if (selectedRow <= 0) {
			selectedRow = 0;
			getCallingTable().setRowSelectionInterval(selectedRow, selectedRow);
			getPreviousButton().setEnabled(false);
			getFirstButton().setEnabled(false);
			getNextButton().setEnabled(true);
			getLastButton().setEnabled(true);
		} else if (selectedRow >= numberOfRows - 1) {
			selectedRow = numberOfRows - 1;
			getCallingTable().setRowSelectionInterval(selectedRow, selectedRow);
			getPreviousButton().setEnabled(true);
			getFirstButton().setEnabled(true);
			getNextButton().setEnabled(false);
			getLastButton().setEnabled(false);

		} else {
			getPreviousButton().setEnabled(true);
			getFirstButton().setEnabled(true);
			getNextButton().setEnabled(true);
			getLastButton().setEnabled(true);
		}
	}

	public JButton getOkButton() {
		return okButton;
	}

	public void setOkButton(JButton okButton) {
		this.okButton = okButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(JButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	public void setMainHeaderLabel(String mainHeaderLabel) {
		this.getMainHeaderLabel().setText(mainHeaderLabel);
	}

	public void setMainSubHeaderLabel(String mainSubHeaderLabel) {
		this.subHeaderLabel.setText(mainSubHeaderLabel);
	}

	public JLabel getMainHeaderLabel() {
		return mainHeaderLabel;
	}

	public void setMainHeaderLabel(JLabel mainHeaderLabel) {
		this.mainHeaderLabel = mainHeaderLabel;
	}

	public JButton getFirstButton() {
		return firstButton;
	}

	public void setFirstButton(JButton firstButton) {
		this.firstButton = firstButton;
	}

	public JButton getPreviousButton() {
		return previousButton;
	}

	public void setPreviousButton(JButton previousButton) {
		this.previousButton = previousButton;
	}

	public JButton getNextButton() {
		return nextButton;
	}

	public void setNextButton(JButton nextButton) {
		this.nextButton = nextButton;
	}

	public JButton getLastButton() {
		return lastButton;
	}

	public void setLastButton(JButton lastButton) {
		this.lastButton = lastButton;
	}

	public JButton getPrintButton() {
		return printButton;
	}

	public void setPrintButton(JButton printButton) {
		this.printButton = printButton;
	}

	public void setCallingTable(JTable callingTable) throws UnsupportedTableModelException {
		this.callingTable = callingTable;
	}

	public JTable getCallingTable() {
		return callingTable;
	}

	public void setFormToReadOnly() {
		getOkButton().setVisible(false);
		okButtonLabel.setVisible(false);
		getSaveButton().setVisible(false);
		saveButtonLabel.setVisible(false);
		cancelButtonLabel.setText("Done");
	}

	public Boolean getNewRecord() {
		return newRecord;
	}

	public void setNewRecord(Boolean newRecord) {
		this.newRecord = newRecord;
	}

	public Boolean getSavedNewRecord() {
		return savedNewRecord;
	}

	public void setSavedNewRecord(Boolean savedNewRecord) {
		this.savedNewRecord = savedNewRecord;
	}

	public void setRecordPositionText(int recordNumber, int totalRecords) {
		this.recordPosition.setText("Record " + (recordNumber + 1) + " of " + totalRecords);
	}

	public void clearRecordPositionText() {
		this.recordPosition.setText("");
	}

	public int getStatus() {
		return status;
	}

	public Boolean getIncludeSaveButton() {
		return includeSaveButton;
	}

	public void setIncludeSaveButton(Boolean includeSaveButton) {
		this.includeSaveButton = includeSaveButton;
	}

  public void setIncludeOkAndAnotherButton(Boolean includeOkAndAnotherButton) {
    this.includeOkAndAnotherButton = includeOkAndAnotherButton;
  }

  public JButton getSaveButton() {
		return saveButton;
	}

	public void setSaveButton(JButton saveButton) {
		this.saveButton = saveButton;
	}

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
        if(readOnly) {
            saveButton.setEnabled(false);
            choice = NO_SAVE_OPTION;
        } else {
            saveButton.setEnabled(true);
        }
    }
}
