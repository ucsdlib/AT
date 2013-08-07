package org.archiviststoolkit.plugin.utils;

import org.archiviststoolkit.util.StringHelper;

import java.util.HashMap;

/**
 * This class contains utility methods use when either exporting or importing files for the creation
 * and linking of Digital Objects to resource components
 *
 * Created by IntelliJ IDEA.
 * User: nathan
 * Date: 4/18/12
 * Time: 2:32 PM
 *
 */
public class ImportExportUtils {
    // these static fields are used to located the index of a particular column in the
    // import file
    public static final String USE_STATEMENT = "Use Statement";
    public static final String URI_LINK = "URI Link";
    public static final String URI_ID = "URI ID";
    public static final String NEW_URI_LINK = "New URI Link";
    public static final String METS_IDENTIFIER = "METS Identifier";

    // additional fields supported in version 2.2 on import
    public static final String DO_TITLE = "Digital Object Title";
    public static final String LABEL = "Label";
    public static final String DATE_EXPRESSION = "Date Expression";
    public static final String DATE_BEGIN = "Date Begin";
    public static final String DATE_END = "Date End";
    public static final String RESTRICTIONS_APPLY = "Restrictions Apply";
    public static final String OBJECT_TYPE = "Object Type";

    public static final String EAD_DAO_ACTUATE = "EAD Dao Actuate";
    public static final String EAD_DAO_SHOW = "EAD Dao Show";

    /**
     * Method to return the index of a particular column
     *
     * @param columnName
     * @param headerLine
     * @return
     */
    public static int getColumnIndex(String columnName, String headerLine) {
        String[] sa = headerLine.split("\\s*\t\\s*");
        int index = -1;

        for(int i = 0; i < sa.length; i++) {
            if(sa[i].equalsIgnoreCase(columnName)) {
                index = i;
                break;
            }
        }

        return index;
    }

    /**
     * Method to return the list of optional header columns
     *
     * @return
     */
    public static String getOptionalHeaderColumns() {
        String optionalHeaderColumns = DO_TITLE + "\t" +
                LABEL + "\t" +
                DATE_EXPRESSION + "\t" +
                DATE_BEGIN + "\t" +
                DATE_END + "\t" +
                RESTRICTIONS_APPLY + "\t" +
                OBJECT_TYPE + "\t" +
                EAD_DAO_ACTUATE + "\t" +
                EAD_DAO_SHOW;

        return optionalHeaderColumns;
    }

    /**
     * Method to return the indexes of the data elements by column name
     *
     * @param headerLine
     * @return
     */
    public static HashMap<String, Integer> getHeaderIndexes(String headerLine) {
        HashMap<String, Integer> headerIndexMap = new HashMap<String, Integer>();

        String[] sa = headerLine.split("\\s*\t\\s*");

        for(int i = 0; i < sa.length; i++) {
            if(sa[i].equalsIgnoreCase(USE_STATEMENT)) {
                headerIndexMap.put(USE_STATEMENT, i);
            } else if(sa[i].equalsIgnoreCase(DO_TITLE)) {
                headerIndexMap.put(DO_TITLE, i);
            } else if(sa[i].equalsIgnoreCase(LABEL)) {
                headerIndexMap.put(LABEL, i);
            } else if(sa[i].equalsIgnoreCase(DATE_EXPRESSION)) {
                headerIndexMap.put(DATE_EXPRESSION, i);
            } else if(sa[i].equalsIgnoreCase(DATE_BEGIN)) {
                headerIndexMap.put(DATE_BEGIN, i);
            } else if(sa[i].equalsIgnoreCase(DATE_END)) {
                headerIndexMap.put(DATE_END, i);
            } else if(sa[i].equalsIgnoreCase(RESTRICTIONS_APPLY)) {
                headerIndexMap.put(RESTRICTIONS_APPLY, i);
            } else if(sa[i].equalsIgnoreCase(OBJECT_TYPE)) {
                headerIndexMap.put(OBJECT_TYPE, i);
            } else if(sa[i].equalsIgnoreCase(OBJECT_TYPE)) {
                headerIndexMap.put(OBJECT_TYPE, i);
            } else if(sa[i].equalsIgnoreCase(EAD_DAO_ACTUATE)) {
                headerIndexMap.put(EAD_DAO_ACTUATE, i);
            } else if(sa[i].equalsIgnoreCase(EAD_DAO_SHOW)) {
                headerIndexMap.put(EAD_DAO_SHOW, i);
            } else if(sa[i].equalsIgnoreCase(URI_LINK)) {
                headerIndexMap.put(URI_LINK, i);
            } else {
                headerIndexMap.put(sa[i], i);
            }
        }

        return headerIndexMap;
    }

    /**
     * Method to return the component title which has been strip of tags, new lines,
     * and truncated to less than 96 characters if needed. A call to this method
     * is need to make sure the tab delimited files being exported are valid
     *
     * @return cleaned up component title
     */
    public static String getSafeTitle(String componentTitle) {
        String safeTitle = "tile";

        // remove any tags and newline characters
        String title = StringHelper.tagRemover(componentTitle.replace("\n", " ")).trim();

        if(title.length() < 96) {
            safeTitle = title;
        } else {
            String titlePart1 = title.substring(0, 61);
            String titlePart2 = title.substring(title.length() - 32);
            safeTitle = titlePart1 + "..." + titlePart2;
        }

        return safeTitle;
    }
}
