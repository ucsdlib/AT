package org.archiviststoolkit.util;

import org.archiviststoolkit.swing.*;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.dialog.ATFileChooser;

import javax.swing.*;
import java.io.File;

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
 * @author lee
 * Date: Jun 19, 2007
 * Time: 12:13:46 PM
 */

public class FileUtils {

    public static final int ONE_RECORD_TO_PRINT = 1;

    public static File chooseFileOrDirectory(int numberOfRecords, ImportOptions importOptions) {
        ATFileChooser filechooser;
        if (numberOfRecords == 0) {
            JOptionPane.showMessageDialog(ApplicationFrame.getInstance(), "You must select at least 1 record to export");
            return null;
        } else if (numberOfRecords == 1) {
            filechooser = new ATFileChooser(importOptions, new org.archiviststoolkit.swing.SimpleFileFilter(".xml"));
            if (filechooser.showSaveDialog(ApplicationFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
                return filechooser.getSelectedFile();
            } else {
                return null;
            }
        } else {
            filechooser = new ATFileChooser(importOptions);
            if (filechooser.showSaveDialog(ApplicationFrame.getInstance(), "Select") == JFileChooser.APPROVE_OPTION) {
                return filechooser.getSelectedFile();
            } else {
                return null;
            }
        }
    }


}
