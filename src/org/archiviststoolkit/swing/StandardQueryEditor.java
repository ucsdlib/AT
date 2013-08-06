/*
 * Created by JFormDesigner on Wed Sep 28 11:54:44 EDT 2005
 */

package org.archiviststoolkit.swing;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.border.*;
import com.jformdesigner.examples.beans.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Lee Mandell
 */
public class StandardQueryEditor extends StandardEditor {

//	public StandardQueryEditor() {
//		initComponents();
//	}
	public StandardQueryEditor(final Frame parent, final String mainHeader) {
		super(parent, mainHeader);
		initComponents();
	}

	/**
	 * Constructor taking the parent frame and a mainHeader.
	 * @param parent the parent frame
	 * @param mainHeader the mainHeader of this editor
	 */

	public StandardQueryEditor(final Dialog parent, final String mainHeader) {
		super(parent, mainHeader);
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		gradientPanel1 = new JGradientPanel();
		headerLabel = new JLabel();
		contentPanel = new JPanel();
		label2 = new JLabel();
		subjectTerm = new JTextField();
		label12 = new JLabel();
		subjectTermType = new JComboBox();
		label13 = new JLabel();
		subjectSource = new JTextField();
		label14 = new JLabel();
		scrollPane1 = new JScrollPane();
		subjectScopeNote = new JTextArea();
		buttonPanel = new JPanel();
		cancelButton = new JButton();
		okButton = new JButton();
		label8 = new JLabel();
		label9 = new JLabel();
		gradientPanel2 = new JGradientPanel();
		auditLabel = new JLabel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBackground(new Color(231, 188, 251));
		setModal(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			ColumnSpec.decodeSpecs("default:grow"),
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC
			}));

		//======== gradientPanel1 ========
		{
			gradientPanel1.setBackground(new Color(229, 211, 237));
			gradientPanel1.setBackground2(new Color(204, 204, 255));
			gradientPanel1.setDirection(JGradientPanel.WEST);
			gradientPanel1.setLayout(new FormLayout(
				"default:grow",
				"35px"));
			
			//---- headerLabel ----
			headerLabel.setText("Subjects");
			headerLabel.setFont(new Font("Lucida Grande", Font.BOLD, 14));
			gradientPanel1.add(headerLabel, new CellConstraints(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT, new Insets( 0, 10, 0, 0)));
		}
		contentPane.add(gradientPanel1, cc.xy(1, 1));

		//======== contentPanel ========
		{
			contentPanel.setBorder(Borders.DLU4_BORDER);
			contentPanel.setBackground(new Color(231, 188, 251));
			contentPanel.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec("max(default;400px):grow")
				},
				new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}));
			
			//---- label2 ----
			label2.setText("Subject Term");
			contentPanel.add(label2, cc.xy(1, 1));
			contentPanel.add(subjectTerm, cc.xy(3, 1));
			
			//---- label12 ----
			label12.setText("Subject Term Type");
			contentPanel.add(label12, cc.xy(1, 3));
			
			//---- subjectTermType ----
			subjectTermType.setBackground(new Color(231, 188, 251));
			contentPanel.add(subjectTermType, cc.xy(3, 3));
			
			//---- label13 ----
			label13.setText("Source");
			contentPanel.add(label13, cc.xy(1, 5));
			contentPanel.add(subjectSource, cc.xy(3, 5));
			
			//---- label14 ----
			label14.setText("Scope Note");
			label14.setVerticalAlignment(SwingConstants.TOP);
			contentPanel.add(label14, cc.xywh(1, 7, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));
			
			//======== scrollPane1 ========
			{
				scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane1.setMaximumSize(new Dimension(32767, 100));
				
				//---- subjectScopeNote ----
				subjectScopeNote.setRows(4);
				subjectScopeNote.setLineWrap(true);
				subjectScopeNote.setTabSize(20);
				subjectScopeNote.setWrapStyleWord(true);
				scrollPane1.setViewportView(subjectScopeNote);
			}
			contentPanel.add(scrollPane1, cc.xy(3, 7));
		}
		contentPane.add(contentPanel, cc.xy(1, 3));

		//======== buttonPanel ========
		{
			buttonPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
			buttonPanel.setLayout(new FormLayout(
				"default, default",
				"default, default"));
			
			//---- cancelButton ----
			cancelButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/cancel.jpg")));
			buttonPanel.add(cancelButton, cc.xy(1, 1));
			
			//---- okButton ----
			okButton.setIcon(new ImageIcon(getClass().getResource("/org/archiviststoolkit/resources/images/ok.jpg")));
			buttonPanel.add(okButton, cc.xy(2, 1));
			
			//---- label8 ----
			label8.setText("Cancel");
			label8.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
			buttonPanel.add(label8, cc.xywh(1, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
			
			//---- label9 ----
			label9.setText("OK");
			label9.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
			buttonPanel.add(label9, cc.xywh(2, 2, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
		}
		contentPane.add(buttonPanel, cc.xywh(1, 5, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

		//======== gradientPanel2 ========
		{
			gradientPanel2.setBackground(new Color(229, 211, 237));
			gradientPanel2.setBackground2(new Color(204, 204, 255));
			gradientPanel2.setDirection(JGradientPanel.EAST);
			gradientPanel2.setLayout(new FormLayout(
				"default:grow",
				"35px"));
			
			//---- auditLabel ----
			auditLabel.setText("created | modified");
			gradientPanel2.add(auditLabel, new CellConstraints(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 10, 0, 0)));
		}
		contentPane.add(gradientPanel2, cc.xy(1, 7));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		WindowAdapter windowAdapter = new WindowAdapter() {
			public void windowClosing(final WindowEvent windowEvent) {
				myIsDialogCancelled = true;

				setVisible(false);
			}
		};

		addWindowListener(windowAdapter);

	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JGradientPanel gradientPanel1;
	private JLabel headerLabel;
	private JPanel contentPanel;
	private JLabel label2;
	private JTextField subjectTerm;
	private JLabel label12;
	private JComboBox subjectTermType;
	private JLabel label13;
	private JTextField subjectSource;
	private JLabel label14;
	private JScrollPane scrollPane1;
	private JTextArea subjectScopeNote;
	private JPanel buttonPanel;
	private JButton cancelButton;
	private JButton okButton;
	private JLabel label8;
	private JLabel label9;
	private JGradientPanel gradientPanel2;
	private JLabel auditLabel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

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
