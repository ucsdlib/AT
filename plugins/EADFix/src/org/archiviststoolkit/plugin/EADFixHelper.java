package org.archiviststoolkit.plugin;

import org.archiviststoolkit.swing.InfiniteProgressPanel;

import java.io.*;

/**
 * Archivists' Toolkit(TM) Copyright © 2005-2009 Regents of the University of California, New York University, & Five Colleges, Inc.
 * All rights reserved.
 * <p/>
 * This software is free. You can redistribute it and / or modify it under the terms of the Educational Community License (ECL)
 * version 1.0 (http://www.opensource.org/licenses/ecl1.php)
 * <p/>
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the ECL license for more details about permissions and limitations.
 * <p/>
 * <p/>
 * Archivists' Toolkit(TM)
 * http://www.archiviststoolkit.org
 * info@archiviststoolkit.org
 * <p/>
 * Created by IntelliJ IDEA.
 *
 * @author: Nathan Stevens
 * Date: Apr 22, 2009
 * Time: 6:49:33 PM
 */
public class EADFixHelper {
    /**
     * Method to do a search and replace persistent ids in an EAD  file
     * @param file The EAD file to read
     * @param monitor The pogress monitor
     */
    public static void assignPersistentIDs(File file, InfiniteProgressPanel monitor) {
        StringBuffer eadBuffer = new StringBuffer();
        
        // if file exists then read in database information
        if(file.exists()) {
            try {
                //use buffering, reading one line at a time
                //FileReader always assumes default encoding is OK!
                BufferedReader input = new BufferedReader(new FileReader(file));
                try {
                    String line = null; //not declared within while loop
                    /*
                    * readLine is a bit quirky :
                    * it returns the content of a line MINUS the newline.
                    * it returns null only for the END of the stream.
                    * it returns an empty String if two newlines appear in a row.
                    */
                    int idCount = 1;
                    while ((line = input.readLine()) != null) {
                        String newLine = line;
                        if(line.indexOf("id=\"ref") != -1) {
                            monitor.setTextLine("Assigning New Persistent ID: " + "ref" +idCount, 2);
                            //System.out.println("Old Line : " + line);
                            newLine = line.replaceAll("ref\\d+", "ref" +idCount);
                            //System.out.println("New Line : " + newLine);
                            idCount++;
                        }

                        // check to see process was cancelled
                        if(monitor.isProcessCancelled()) {
                            return;
                        }

                        eadBuffer.append(newLine).append("\n");
                    }

                    // save to file now
                    File newFile = new File(file.getParent(), getNewFileName(file));
                    Writer output = new BufferedWriter(new FileWriter(newFile));

                    // alert user that new file is being written
                    monitor.setTextLine("Writing New File: " + newFile.getName(), 2);
                    
                    output.write(eadBuffer.toString());
                    output.close();
                }
                finally {
                    input.close();
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Method to get the new file name
    private static String getNewFileName(File file) {
        String oldName = file.getName();
        int index = oldName.lastIndexOf(".");
        return oldName.substring(0,index) + "_modified.xml";
    }
}
