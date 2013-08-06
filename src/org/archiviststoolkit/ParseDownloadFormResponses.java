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
 * Date: Dec 18, 2006
 * Time: 4:07:55 PM
 */

package org.archiviststoolkit;

import org.archiviststoolkit.importer.ImportUtils;
import org.archiviststoolkit.util.StringHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;

public class ParseDownloadFormResponses {

	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("org.archiviststoolkit.resources.messages.messages");
	private static DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);

	public static void main(final String[] args) {

		File fileToParse = null;

		if (args.length > 0) {
			fileToParse = new File(args[0]);
		} else {
			System.exit(1);
		}

		ArrayList<String> recordLines = null;
		ArrayList<DownloadRecords> downloadRecords = new ArrayList<DownloadRecords>();
		ArrayList<String> fileLines = ImportUtils.loadFileIntoStringArray(fileToParse);
		for (String line : fileLines) {
			if (line.startsWith("From:")) {
				if (recordLines != null) {
					downloadRecords.add(new DownloadRecords(recordLines));
				}
				recordLines = new ArrayList<String>();
			}
			recordLines.add(line);
		}
		downloadRecords.add(new DownloadRecords(recordLines));

		try {
//			String driverClass = resourceBundle.getString("archiviststoolkit.jdbc.driver");
//			SessionFactory.setDriverClass(driverClass);
			Class.forName(resourceBundle.getString("archiviststoolkit.jdbc.driver"));
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://dlibdev.nyu.edu:3308/AT_Downloads",
					"atadmin",
					"archd0lph1n");

			PreparedStatement pstmtInsert = conn.prepareStatement(
					"INSERT INTO downloads (id, firstName, lastName, institution, address, city, region, zip, email, country, downloadDate, downloadCount) " +
							"VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			PreparedStatement pstmtSelect = conn.prepareStatement("SELECT id, downloadCount from downloads where email = ?");
			PreparedStatement pstmtUpdate = conn.prepareStatement("UPDATE downloads SET downloadCount = ? WHERE id = ?");
			for (DownloadRecords record : downloadRecords) {
				record.insertIntoDatabase(pstmtInsert, pstmtSelect, pstmtUpdate);
				record.printRecord();
			}
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	private static class DownloadRecords {

		private String name = "";
		private String firstName = "";
		private String lastName = "";
		private String institution = "";
		private String address = "";
		private String city = "";
		private String region = "";
		private String zip = "";
		private String email = "";
		private String country = "";
		private Date downloadDate = null;
		private int numberOfDownloads = 1;

		private DownloadRecords(ArrayList<String> recordLines) {
			Iterator<String> iterator = recordLines.iterator();
			String currentLine = "";
			//get to the first line
			while (iterator.hasNext() && !currentLine.startsWith("Date: ")) {
				currentLine = iterator.next();
			}
			try {
				downloadDate = new Date(df.parse(currentLine.replaceFirst("Date: ", "")).getTime());
			} catch (ParseException e) {
				System.out.println(e);
			}
			while (iterator.hasNext() && !currentLine.startsWith("firstName  ")) {
				currentLine = iterator.next();
			}
			firstName = currentLine.replaceFirst("firstName  ", "");
			currentLine = iterator.next();
			lastName = currentLine.replaceFirst("lasttName  ", "");
			currentLine = iterator.next();
			institution = currentLine.replaceFirst("institution  ", "");
			currentLine = iterator.next();
			address = currentLine.replaceFirst("Address  ", "");
			currentLine = iterator.next();
			while (!currentLine.startsWith("city  ")) {
				address = StringHelper.concat("\n", address, currentLine);
				currentLine = iterator.next();
			}
			city = currentLine.replaceFirst("city  ", "");
			currentLine = iterator.next();
			region = currentLine.replaceFirst("region  ", "");
			currentLine = iterator.next();
			zip = currentLine.replaceFirst("zip  ", "");
			currentLine = iterator.next();
			email = currentLine.replaceFirst("email  ", "");
			currentLine = iterator.next();
			country = currentLine.replaceFirst("country  ", "");
		}

		private void insertIntoDatabase(PreparedStatement pstmtInsert,
										PreparedStatement pstmtSelect,
										PreparedStatement pstmtUpdate) throws SQLException {

			pstmtSelect.setString(1, email);
			ResultSet rs = pstmtSelect.executeQuery();
			if (rs.next()) {
				int downloadCount = rs.getInt("downloadCount");
				long id = rs.getLong("id");
				pstmtUpdate.setInt(1, downloadCount + 1);
				pstmtUpdate.setLong(2, id);
				pstmtUpdate.executeUpdate();

			} else {
				pstmtInsert.setString(1, firstName);
				pstmtInsert.setString(2, lastName);
				pstmtInsert.setString(3, institution);
				pstmtInsert.setString(4, address);
				pstmtInsert.setString(5, city);
				pstmtInsert.setString(6, region);
				pstmtInsert.setString(7, zip);
				pstmtInsert.setString(8, email);
				pstmtInsert.setString(9, country);
				pstmtInsert.setDate(10, downloadDate);
				pstmtInsert.setInt(11, numberOfDownloads);
				pstmtInsert.executeUpdate();
			}

		}

		private void printRecord() {
			System.out.println("Name: " + name);
			System.out.println("First name: " + firstName);
			System.out.println("Last name: " + lastName);
			System.out.println("Institution: " + institution);
			System.out.println("Address: " + address);
			System.out.println("City: " + city);
			System.out.println("Region: " + region);
			System.out.println("Zip: " + zip);
			System.out.println("Email: " + email);
			System.out.println("Country: " + country + "\n\n");
		}
	}

}
