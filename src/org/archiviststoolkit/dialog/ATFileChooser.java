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
 * Programmer: Lee Mandell
 * Date: Jan 6, 2006
 * Time: 6:12:54 PM
 */

package org.archiviststoolkit.dialog;

import org.archiviststoolkit.importer.ImportUtils;
import org.archiviststoolkit.util.UserPreferences;
import org.archiviststoolkit.swing.SimpleFileFilter;
import org.archiviststoolkit.swing.ImportOptions;
import org.archiviststoolkit.ApplicationFrame;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class ATFileChooser extends JFileChooser {
	/**
	 * Constructs a <code>JFileChooser</code> pointing to the user's
	 * default directory. This default depends on the operating system.
	 * It is typically the "My Documents" folder on Windows, and the
	 * user's home directory on Unix.
	 */
	public ATFileChooser() {
		super(UserPreferences.getInstance().getSavePath());
		this.setPreferredSize(new Dimension(1000, 500));
		this.setBackground(ApplicationFrame.BACKGROUND_COLOR);
		this.setAcceptAllFileFilterUsed(false);
	}

	public void approveSelection() {

		File selectedFile = getSelectedFile();

		//check to see if the file exists
		boolean save = true;
		if (!open) {
			String filePath = selectedFile.getPath();
			FileFilter fileFilter = getFileFilter();
			if (fileFilter != null) {
				SimpleFileFilter simpleFileFilter = (SimpleFileFilter)fileFilter;
				if (!filePath.endsWith(simpleFileFilter.getExtension())) {
					selectedFile = new File(filePath + simpleFileFilter.getExtension());
					this.setSelectedFile(selectedFile);
				}
			}
			
			if (selectedFile.isFile() && selectedFile.exists()) {
				if (JOptionPane.showConfirmDialog(this,
						getSelectedFile().getName() + " exists. Overwrite it?", "Overwrite file",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					save = true;
				} else {
					save = false;
				}
			}
		}

		if (save) {
			if (open && !selectedFile.exists()) {
				JOptionPane.showMessageDialog(this, "You must select either a file or folder to import");
				return;
			}
			if (importOptions != null) {
				if (importOptions.allDataEntered()) {
					super.approveSelection();
				}
			} else {
				super.approveSelection();
			}
		}
	}

	public ATFileChooser(ImportOptions importOptions, FileFilter fileFilter) {
		super(UserPreferences.getInstance().getSavePath());
		this.importOptions = importOptions;
		this.setAccessory((JPanel) importOptions);
		this.setPreferredSize(new Dimension(1000, 500));
		this.setBackground(ApplicationFrame.BACKGROUND_COLOR);
		if (fileFilter != null) {
			this.setAcceptAllFileFilterUsed(false);
			this.setFileFilter(fileFilter);
		} else
			this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}

	public ATFileChooser(ImportOptions importOptions) {
		super(UserPreferences.getInstance().getSavePath());
		this.importOptions = importOptions;
		this.setAccessory((JPanel) importOptions);
		this.setPreferredSize(new Dimension(1000, 500));
		this.setBackground(ApplicationFrame.BACKGROUND_COLOR);
		this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}

	public ATFileChooser(FileFilter fileFilter) {
		super(UserPreferences.getInstance().getSavePath());
		this.setPreferredSize(new Dimension(1000, 500));
		this.setBackground(ApplicationFrame.BACKGROUND_COLOR);
		this.setAcceptAllFileFilterUsed(false);
		this.setFileFilter(fileFilter);
	}

	public int showSaveDialog(Component component) throws HeadlessException {
		open = false;
		this.setCurrentDirectory(new File(UserPreferences.getInstance().getSavePath()));
		int returnValue = super.showSaveDialog(component);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			UserPreferences.getInstance().setSavePath(this.getCurrentDirectory().getPath());
		}
		return returnValue;
	}

    // Method to display a save dialog only cable of selecting directories
    public int showSaveDialog(Component parent, String approveButtonText) {
        open = true;
        setApproveButtonText(approveButtonText);
        this.setCurrentDirectory(new File(UserPreferences.getInstance().getSavePath()));
		this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = super.showSaveDialog(parent);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			UserPreferences.getInstance().setSavePath(this.getCurrentDirectory().getPath());
		}
		return returnValue;
    }

    public int showOpenDialog(Component parent, String approveButtonText) throws HeadlessException {
		this.setCurrentDirectory(new File(UserPreferences.getInstance().getSavePath()));
		open = true;
		setApproveButtonText(approveButtonText);
		int returnValue = super.showOpenDialog(parent);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			UserPreferences.getInstance().setSavePath(this.getCurrentDirectory().getPath());
		}
		return returnValue;
	}


	private ImportOptions importOptions;
	private Boolean open = false;
}
