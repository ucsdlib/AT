package org.archiviststoolkit.util;

import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.hibernate.SessionFactory;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Archivists' Toolkit(TM) Copyright 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 *
 * Simple utility class for checking that an entered date is valid for the given database
 * and for handeling iso dates in the AT
 *
 * Created by IntelliJ IDEA.
 * User: Nathan Stevens
 * Date: Aug 28, 2008
 * Time: 10:46:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class ATDateUtils {
	public static final long MYSQL_MINDATE = -30609774000000L; // (1/1/1000) used to not allow dates before this from going into DB
	public static final long MICROSOFT_SQL_SERVER_MINDATE = -6847786800000L; //(1/1/1753) used to not allow dates before this from going into DB

    /**
     * Method to check to see if a textField as a date that can be 
     * @param textField
     */
	public static void isValidDatabaseDate(JFormattedTextField textField) {
		String dateText = textField.getText();
		try {
			Date date = ApplicationFrame.applicationDateFormat.parse(dateText);
			Date mysqlOldDate = new Date(MYSQL_MINDATE);
			Date mssqlOldDate = new Date(MICROSOFT_SQL_SERVER_MINDATE);

			SimpleDateFormat df = new SimpleDateFormat(ApplicationFrame.applicationDateFormat.toPattern());
			String mysqlOldDateString = df.format(mysqlOldDate);
			String mssqlOldDateString = df.format(mssqlOldDate);

			if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MYSQL)
					&& date.before(mysqlOldDate)) {
				JOptionPane.showMessageDialog(null, "Invalid MySQL date. Date must be equal to, or greater than\n" + mysqlOldDateString);
				textField.setText("");
			} else if (SessionFactory.getDatabaseType().equals(SessionFactory.DATABASE_TYPE_MICROSOFT_SQL_SERVER)
					&& date.before(mssqlOldDate)) {
				JOptionPane.showMessageDialog(null, "Invalid Microsoft SQL Server date. Date must be equal to, or greater than\n" + mssqlOldDateString);
				textField.setText("");
			}
		}
		catch (ParseException pe) {
		} // empty string so do nothing
	}

    /**
     * Method to check to see if the text in the textfield is a valid date. 
     * @param textField The text field
     */
    public static boolean isValidATDate(JTextField textField) {
        SimpleDateFormat sdf = ApplicationFrame.applicationDateFormat;

        // declare and initialize testDate variable, this is what will hold
        // our converted string
        Date testDate = null;

        String errorMessage;

        // we will now try to parse the string into date form
        try {
            testDate = sdf.parse(textField.getText());
        } catch (ParseException e) {
            return false;
        }

        // dateformat.parse will accept any date as long as it's in the format
        // you defined, it simply rolls dates over, for example, december 32
        // becomes jan 1 and december 0 becomes november 30
        // This statement will make sure that once the string
        // has been checked for proper formatting that the date is still the
        // date that was entered, if it's not, we assume that the date is invalid

        if (!sdf.format(testDate).equals(textField.getText())) {
            return false;
        }

        // if we make it to here without getting an error it is assumed that
        // the date was a valid one and that it's in the proper format
        return true;
    }

    /**
     * Method to check to see if the string that was passed in is a valid iso date.
     * valid iso dates can be either yyyy, yyyy-mm, yyyy-mm-dd
     *
     * @param textField The textfield containing the iso date to check
     * @return true if the date passes the check for an iso date
     */
    public static boolean isValidISODate(JTextField textField) {
        String isoDate = textField.getText();
        SimpleDateFormat sdf = ApplicationFrame.applicationDateFormat;
        String pattern = sdf.toPattern();
        String newISODate = "";

        // fisrt check to if it matches a iso date
        if(isoDate.matches("\\d{1,4}") || isoDate.matches("\\d{1,4}-\\d{1,2}") || isoDate.matches("\\d{1,4}-\\d{1,2}-\\d{1,2}")) {
            newISODate = formatISODate(isoDate);
        }

        // now check to see if it not in a date format that AT is using. if it is then convert to iso format
        if (isValidATDate(textField)) {
            try {
                SimpleDateFormat isodf = new SimpleDateFormat("yyyy-MM-dd");
                Date testDate = sdf.parse(isoDate);
                newISODate = isodf.format(testDate);
            } catch(ParseException pe) {
                newISODate = "";
            }
        } else if(pattern.contains("M/d") && isoDate.indexOf("/") != -1) { // assume format is mm/yyyy
            String[] sa = isoDate.split("/");
            newISODate = formatISODate(sa[1] + "-" + sa[0]);
        } else if(pattern.contains("M-d") && isoDate.indexOf("-") != -1) { // assume format is mm-yyyy
            String[] sa = isoDate.split("-");
            newISODate = formatISODate(sa[1] + "-" + sa[0]);
        } else if(pattern.contains("y/M") && isoDate.indexOf("/") != -1) { // assume format is yyyy/mm
            String[] sa = isoDate.split("/");
            newISODate = formatISODate(sa[0] + "-" + sa[1]);
        } else if(pattern.contains("d/M") && isoDate.indexOf("/") != -1) { // assume format is mm/yyyy
            String[] sa = isoDate.split("/");
            newISODate = formatISODate(sa[1] + "-" + sa[0]);
        } else if(pattern.contains("d-M") && isoDate.indexOf("-") != -1) { // assume format is mm-yyyy
            String[] sa = isoDate.split("-");
            newISODate = formatISODate(sa[1] + "-" + sa[0]);
        }

        // now check that the final date is in an iso format, if not display error to user
        if(newISODate.matches("\\d{4}") || newISODate.matches("\\d{4}-\\d{2}") || newISODate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            textField.setText(newISODate);
            return true;
        } else { // display error to user
            String errorMessage = "The date provided is an invalid ISO date format.\nValid formats are YYYY, YYYY-MM, YYYY-MM-DD";
            JOptionPane.showMessageDialog(null, errorMessage);
            return false;
        }
    }

    /**
     * Method to format an inputed date to the iso format of either
     * yyyy, yyyy-mm, or yyyy-mm-dd
     * @param date The input date to format
     * @return The formated iso date
     */
    public static String formatISODate(String date) {
        if(date.matches("\\d{1,4}")) { // matches yyyy
            return padYear(date);
        } else if (date.matches("\\d{1,4}-\\d{1,2}")) { // matches yyyy-mm format
            String[] sa = date.split("-");
            return padYear(sa[0]) + "-" + padMonthOrDay(sa[1]);
        } else if (date.matches("\\d{1,4}-\\d{1,2}-\\d{1,2}")) {// matches yyyy-mm-dd
            String[] sa = date.split("-");
            return padYear(sa[0]) + "-" + padMonthOrDay(sa[1]) + "-" + padMonthOrDay(sa[2]);
        } else { // return blank
            return "";
        }
    }

    /**
     * Method to pad the given year with leading zero
     * @param year The year that is pasted in
     * @return The year paded with zero or an empty string if not a valid year
     */
    public static String padYear(String year) {
        // fisrt check to see if it is a year that needs to be padded
        if(year.matches("\\d{4}")) {
            return year;
        } else if(year.matches("\\d{3}")) {
            return "0"+ year;
        } else if(year.matches("\\d{2}")) {
            return "00"+ year;
        } else if(year.matches("\\d{1}")) {
            return "000"+ year;
        } else { // just return the year
            return ""; // year was not good
        }
    }

    /**
     * Pad the month or day with a leading zero
     * @param mod The month or day to pad
     * @return The padded month or day or a blank string if not a valid day or year
     */
    public static String padMonthOrDay(String mod) {
        // fisrt check to see if it is a year that needs to be padded
        if(mod.matches("\\d{2}")) {
            return mod;
        } else if(mod.matches("\\d")) {
            return "0"+ mod;
        } else { // month or day format not right return blank
            return "";
        }
    }

    /**
     * Method to return the year part from an iso date in either
     * yyyy, yyyy-mm, or yyyy-mm-dd
     * @param isoDate
     * @return the year from an iso date
     */
    public static Integer getYearFromISODate(String isoDate) {
        Integer year = null;

        if(isoDate.matches("\\d{4}")) { // must just be year
            year = new Integer(isoDate);
        } else if (isoDate.matches("\\d{4}-\\d{2}") || isoDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            String[] sa = isoDate.split("-");
            year = new Integer(sa[0]);
        } else if (isoDate.matches("\\d{8}")) { //looks like YYYYMMDD
            String stringYear = isoDate.substring(0, 4);
            year = new Integer(stringYear);
        }

        return year;
    }

    /**
     * Method to get an iso date in the format yyyy, yyyy-mm, or yyyy-mm-dd in GMT seconds
     * @param isoDate The isodate to convert to seconds
     * @return GMT Seconds representing the given date 
     */
    public static Long getISODateInSeconds(String isoDate) {
        String dateString = "";
        SimpleDateFormat isodf = new SimpleDateFormat("yyyy-MM-dd");

        // first check the format of the isodate to see if anything need to be added to
        // get it to the yyyy-mm-dd format
        if(isoDate.matches("\\d{4}")) { // must just be year so add 1st month and 1st day
            dateString = isoDate + "-01-01";
        } else if (isoDate.matches("\\d{4}-\\d{2}")) { // must be yyyy-mm so add 1st day
            dateString = isoDate + "-01";
        } else if (isoDate.matches("\\d{8}")) { // must be YYYYMMDD
            dateString = isoDate;
            isodf = new SimpleDateFormat("yyyyMMdd");
        } else { // must be full date so use that
            dateString = isoDate;
        }

        // now convert to java date object and get the seconds from that
        try {
            Date testDate = isodf.parse(dateString);
            return testDate.getTime();
        } catch(ParseException pe) {
            return null;
        }
    }
}