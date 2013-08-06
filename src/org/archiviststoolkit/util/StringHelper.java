/*
 * Static String formatting and query routines.
 * Copyright (C) 2001,2002 Stephen Ostermiller <utils@Ostermiller.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * See COPYING.TXT for details.
 */

package org.archiviststoolkit.util;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

import org.archiviststoolkit.model.Accessions;
import org.archiviststoolkit.model.AccessionsResourcesCommon;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.Resources;

import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;

/**
 * Utilities for String formatting, manipulation, and queries.
 * More information about this class is available from <a href=
 * "http://ostermiller.org/utils/StringHelper.html">ostermiller.org</a>.
 * <p/>
 * 20Nov03 Fixed bug in replace method.
 * length = stringLength + (count * (replaceLength - findLength));
 */
public class StringHelper {

	private static boolean debug = false;

	private static CollapsedStringAdapter whiteSpaceStripper = new CollapsedStringAdapter();

	public static String convertPipeToCarriageReturn(String toConvert) {
		String returnString = "";

		String[] result = toConvert.split("\\|");
		for (int i = 0; i < result.length; i++) {
			if (i == 0) {
				returnString = result[i].trim();
			} else {
				returnString += "\n" + result[i].trim();
			}
		}
		return returnString;
	}


	/**
	 * Handles underscores by replacing them with spaces, and handles camelCase by breaking
	 * words apart at the humps; it capitalizes the first word.
	 */
	public static String makePrettyName(String name) {

		StringBuffer prettyName = new StringBuffer();
		//Handle underscores
		name = name.replace('_', ' ');
		//Handle camelCase
		char a;
		char b;
		prettyName.append(Character.toUpperCase(name.charAt(0)));
		for (int i = 0; i < name.length() - 1; i++) {
			a = name.charAt(i);
			b = name.charAt(i + 1);
			if (Character.isLowerCase(a) &&
					Character.isUpperCase(b)) {
				prettyName.append(' ');
				prettyName.append(b);
			} else {
				prettyName.append(b);
			}
		}
		return prettyName.toString();
	}


	/**
	 * Pad the beginning of the given String with spaces until
	 * the String is of the given length.
	 * <p/>
	 * If a String is longer than the desired length,
	 * it will not be truncated, however no padding
	 * will be added.
	 *
	 * @param s	  String to be padded.
	 * @param length desired length of result.
	 * @return padded String.
	 * @throws NullPointerException if s is null.
	 */
	public static String prepad(String s, int length) {
		return prepad(s, length, ' ');
	}

	/**
	 * Pre-pend the given character to the String until
	 * the result is the desired length.
	 * <p/>
	 * If a String is longer than the desired length,
	 * it will not be truncated, however no padding
	 * will be added.
	 *
	 * @param s	  String to be padded.
	 * @param length desired length of result.
	 * @param c	  padding character.
	 * @return padded String.
	 * @throws NullPointerException if s is null.
	 */
	public static String prepad(String s, int length, char c) {
		int needed = length - s.length();
		if (needed <= 0) {
			return s;
		}
		StringBuffer sb = new StringBuffer(length);
		for (int i = 0; i < needed; i++) {
			sb.append(c);
		}
		sb.append(s);
		return (sb.toString());
	}

	/**
	 * Pad the end of the given String with spaces until
	 * the String is of the given length.
	 * <p/>
	 * If a String is longer than the desired length,
	 * it will not be truncated, however no padding
	 * will be added.
	 *
	 * @param s	  String to be padded.
	 * @param length desired length of result.
	 * @return padded String.
	 * @throws NullPointerException if s is null.
	 */
	public static String postpad(String s, int length) {
		return postpad(s, length, ' ');
	}

	/**
	 * Append the given character to the String until
	 * the result is  the desired length.
	 * <p/>
	 * If a String is longer than the desired length,
	 * it will not be truncated, however no padding
	 * will be added.
	 *
	 * @param s	  String to be padded.
	 * @param length desired length of result.
	 * @param c	  padding character.
	 * @return padded String.
	 * @throws NullPointerException if s is null.
	 */
	public static String postpad(String s, int length, char c) {
		int needed = length - s.length();
		if (needed <= 0) {
			return s;
		}
		StringBuffer sb = new StringBuffer(length);
		sb.append(s);
		for (int i = 0; i < needed; i++) {
			sb.append(c);
		}
		return (sb.toString());
	}

	/**
	 * Pad the beginning and end of the given String with spaces until
	 * the String is of the given length.  The result is that the original
	 * String is centered in the middle of the new string.
	 * <p/>
	 * If the number of characters to pad is even, then the padding
	 * will be split evenly between the beginning and end, otherwise,
	 * the extra character will be added to the end.
	 * <p/>
	 * If a String is longer than the desired length,
	 * it will not be truncated, however no padding
	 * will be added.
	 *
	 * @param s	  String to be padded.
	 * @param length desired length of result.
	 * @return padded String.
	 * @throws NullPointerException if s is null.
	 */
	public static String midpad(String s, int length) {
		return midpad(s, length, ' ');
	}

	/**
	 * Pad the beginning and end of the given String with the given character
	 * until the result is  the desired length.  The result is that the original
	 * String is centered in the middle of the new string.
	 * <p/>
	 * If the number of characters to pad is even, then the padding
	 * will be split evenly between the beginning and end, otherwise,
	 * the extra character will be added to the end.
	 * <p/>
	 * If a String is longer than the desired length,
	 * it will not be truncated, however no padding
	 * will be added.
	 *
	 * @param s	  String to be padded.
	 * @param length desired length of result.
	 * @param c	  padding character.
	 * @return padded String.
	 * @throws NullPointerException if s is null.
	 */
	public static String midpad(String s, int length, char c) {
		int needed = length - s.length();
		if (needed <= 0) {
			return s;
		}
		int beginning = needed / 2;
		int end = beginning + needed % 2;
		StringBuffer sb = new StringBuffer(length);
		for (int i = 0; i < beginning; i++) {
			sb.append(c);
		}
		sb.append(s);
		for (int i = 0; i < end; i++) {
			sb.append(c);
		}
		return (sb.toString());
	}

	/**
	 * Split the given String into tokens.
	 * <p/>
	 * This method is meant to be similar to the split
	 * function in other programming languages but it does
	 * not use regular expressions.  Rather the String is
	 * split on a single String literal.
	 * <p/>
	 * Unlike java.util.StringTokenizer which accepts
	 * multiple character tokens as delimiters, the delimiter
	 * here is a single String literal.
	 * <p/>
	 * Each null token is returned as an empty String.
	 * Delimiters are never returned as tokens.
	 * <p/>
	 * If there is no delimiter because it is either empty or
	 * null, the only element in the result is the original String.
	 * <p/>
	 * StringHelper.split("1-2-3", "-");<br>
	 * result: {"1", "2", "3"}<br>
	 * StringHelper.split("-1--2-", "-");<br>
	 * result: {"", "1", ,"", "2", ""}<br>
	 * StringHelper.split("123", "");<br>
	 * result: {"123"}<br>
	 * StringHelper.split("1-2---3----4", "--");<br>
	 * result: {"1-2", "-3", "", "4"}<br>
	 *
	 * @param s		 String to be split.
	 * @param delimiter String literal on which to split.
	 * @return an array of tokens.
	 * @throws NullPointerException if s is null.
	 */
	public static String[] split(String s, String delimiter) {
		int delimiterLength;
		// the next statement has the side effect of throwing a null pointer
		// exception if s is null.
		int stringLength = s.length();
		if (delimiter == null || (delimiterLength = delimiter.length()) == 0) {
			// it is not inherently clear what to do if there is no delimiter
			// On one hand it would make sense to return each character because
			// the null String can be found between each pair of characters in
			// a String.  However, it can be found many times there and we don'
			// want to be returning multiple null tokens.
			// returning the whole String will be defined as the correct behavior
			// in this instance.
			return new String[]{s};
		}

		// a two pass solution is used because a one pass solution would
		// require the possible resizing and copying of memory structures
		// In the worst case it would have to be resized n times with each
		// resize having a O(n) copy leading to an O(n^2) algorithm.

		int count;
		int start;
		int end;

		// Scan s and count the tokens.
		count = 0;
		start = 0;
		while ((end = s.indexOf(delimiter, start)) != -1) {
			count++;
			start = end + delimiterLength;
		}
		count++;

		// allocate an array to return the tokens,
		// we now know how big it should be
		String[] result = new String[count];

		// Scan s again, but this time pick out the tokens
		count = 0;
		start = 0;
		while ((end = s.indexOf(delimiter, start)) != -1) {
			result[count] = (s.substring(start, end));
			count++;
			start = end + delimiterLength;
		}
		end = stringLength;
		result[count] = s.substring(start, end);

		return (result);
	}

	/**
	 * Replace occurrences of a substring.
	 * <p/>
	 * StringHelper.replace("1-2-3", "-", "|");<br>
	 * result: "1|2|3"<br>
	 * StringHelper.replace("-1--2-", "-", "|");<br>
	 * result: "|1||2|"<br>
	 * StringHelper.replace("123", "", "|");<br>
	 * result: "123"<br>
	 * StringHelper.replace("1-2---3----4", "--", "|");<br>
	 * result: "1-2|-3||4"<br>
	 * StringHelper.replace("1-2---3----4", "--", "---");<br>
	 * result: "1-2----3------4"<br>
	 *
	 * @param s	   String to be modified.
	 * @param find	String to find.
	 * @param replace String to replace.
	 * @return a string with all the occurrences of the string to find replaced.
	 * @throws NullPointerException if s is null.
	 */
	public static String replace(String s, String find, String replace) {
		int findLength;
		// the next statement has the side effect of throwing a null pointer
		// exception if s is null.
		int stringLength = s.length();
		if (find == null || (findLength = find.length()) == 0) {
			// If there is nothing to find, we won't try and find it.
			return s;
		}
		if (replace == null) {
			// a null string and an empty string are the same
			// for replacement purposes.
			replace = "";
		}
		// check to see if there is an exact match
		if (s.equalsIgnoreCase(find)) {
			return replace;
		}
		int replaceLength = replace.length();

		// We need to figure out how long our resulting string will be.
		// This is required because without it, the possible resizing
		// and copying of memory structures could lead to an unacceptable runtime.
		// In the worst case it would have to be resized n times with each
		// resize having a O(n) copy leading to an O(n^2) algorithm.
		int length;
		if (findLength == replaceLength) {
			// special case in which we don't need to count the replacements
			// because the count falls out of the length formula.
			length = stringLength;
		} else {
			int count;
			int start;
			int end;

			// Scan s and count the number of times we find our target.
			count = 0;
			start = 0;
			while ((end = s.indexOf(find, start)) != -1) {
				count++;
				start = end + findLength;
			}
			if (count == 0) {
				// special case in which on first pass, we find there is nothing
				// to be replaced.  No need to do a second pass or create a string buffer.
				return s;
			}
			//			length = stringLength - (count * (replaceLength - findLength));
			length = stringLength + (count * (replaceLength - findLength));
		}

		int start = 0;
		int end = s.indexOf(find, start);
		if (end == -1) {
			// nothing was found in the string to replace.
			// we can get this if the find and replace strings
			// are the same length because we didn't check before.
			// in this case, we will return the original string
			return s;
		}
		// it looks like we actually have something to replace
		// *sigh* allocate memory for it.
		StringBuffer sb = new StringBuffer(length);

		// Scan s and do the replacements
		while (end != -1) {
			sb.append(s.substring(start, end));
			sb.append(replace);
			start = end + findLength;
			end = s.indexOf(find, start);
		}
		end = stringLength;
		sb.append(s.substring(start, end));

		return (sb.toString());
	}

	/**
	 * Replaces characters that may be confused by a HTML
	 * parser with their equivalent character entity references.
	 * <p/>
	 * Any data that will appear as text on a web page should
	 * be be escaped.  This is especially important for data
	 * that comes from untrusted sources such as Internet users.
	 * A common mistake in CGI programming is to ask a user for
	 * data and then put that data on a web page.  For example:<pre>
	 * Server: What is your name?
	 * User: &lt;b&gt;Joe&lt;b&gt;
	 * Server: Hello <b>Joe</b>, Welcome</pre>
	 * If the name is put on the page without checking that it doesn't
	 * contain HTML code or without sanitizing that HTML code, the user
	 * could reformat the page, insert scripts, and control the the
	 * content on your web server.
	 * <p/>
	 * This method will replace HTML characters such as &gt; with their
	 * HTML entity reference (&amp;gt;) so that the html parser will
	 * be sure to interpret them as plain text rather than HTML or script.
	 * <p/>
	 * This method should be used for both data to be displayed in text
	 * in the html document, and data put in form elements. For example:<br>
	 * <code>&lt;html&gt;&lt;body&gt;<i>This in not a &amp;lt;tag&amp;gt;
	 * in HTML</i>&lt;/body&gt;&lt;/html&gt;</code><br>
	 * and<br>
	 * <code>&lt;form&gt;&lt;input type="hidden" name="date" value="<i>This data could
	 * be &amp;quot;malicious&amp;quot;</i>"&gt;&lt;/form&gt;</code><br>
	 * In the second example, the form data would be properly be resubmitted
	 * to your cgi script in the URLEncoded format:<br>
	 * <code><i>This data could be %22malicious%22</i></code>
	 *
	 * @param s String to be escaped
	 * @return escaped String
	 * @throws NullPointerException if s is null.
	 */
	public static String escapeHTML(String s) {
		int length = s.length();
		int newLength = length;
		// first check for characters that might
		// be dangerous and calculate a length
		// of the string that has escapes.
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			switch (c) {
				case'\"': {
					newLength += 5;
				}
				break;
				case'&':
				case'\'': {
					newLength += 4;
				}
				break;
				case'<':
				case'>': {
					newLength += 3;
				}
				break;
			}
		}
		if (length == newLength) {
			// nothing to escape in the string
			return s;
		}
		StringBuffer sb = new StringBuffer(newLength);
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			switch (c) {
				case'\"': {
					sb.append("&quot;");
				}
				break;
				case'\'': {
					sb.append("&#39;");
				}
				break;
				case'&': {
					sb.append("&amp;");
				}
				break;
				case'<': {
					sb.append("&lt;");
				}
				break;
				case'>': {
					sb.append("&gt;");
				}
				break;
				default: {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Replaces characters that may be confused by an SQL
	 * parser with their equivalent escape characters.
	 * <p/>
	 * Any data that will be put in an SQL query should
	 * be be escaped.  This is especially important for data
	 * that comes from untrusted sources such as Internet users.
	 * <p/>
	 * For example if you had the following SQL query:<br>
	 * <code>"SELECT * FROM addresses WHERE name='" + name + "' AND private='N'"</code><br>
	 * Without this function a user could give <code>" OR 1=1 OR ''='"</code>
	 * as their name causing the query to be:<br>
	 * <code>"SELECT * FROM addresses WHERE name='' OR 1=1 OR ''='' AND private='N'"</code><br>
	 * which will give all addresses, including private ones.<br>
	 * Correct usage would be:<br>
	 * <code>"SELECT * FROM addresses WHERE name='" + StringHelper.escapeSQL(name) + "' AND private='N'"</code><br>
	 * <p/>
	 * Another way to avoid this problem is to use a PreparedStatement
	 * with appropriate placeholders.
	 *
	 * @param s String to be escaped
	 * @return escaped String
	 * @throws NullPointerException if s is null.
	 */
	public static String escapeSQL(String s) {
		int length = s.length();
		int newLength = length;
		// first check for characters that might
		// be dangerous and calculate a length
		// of the string that has escapes.
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			switch (c) {
				case'\\':
				case'\"':
				case'\'':
				case'0': {
					newLength += 1;
				}
				break;
			}
		}
		if (length == newLength) {
			// nothing to escape in the string
			return s;
		}
		StringBuffer sb = new StringBuffer(newLength);
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			switch (c) {
				case'\\': {
					sb.append("\\\\");
				}
				break;
				case'\"': {
					sb.append("\\\"");
				}
				break;
				case'\'': {
					sb.append("\\\'");
				}
				break;
				case'0': {
					sb.append("\\0");
				}
				break;
				default: {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Replaces characters that are not allowed in a Java style
	 * string literal with their escape characters.  Specifically
	 * quote ("), single quote ('), new line (\n), carriage return (\r),
	 * and backslash (\), and tab (\t) are escaped.
	 *
	 * @param s String to be escaped
	 * @return escaped String
	 * @throws NullPointerException if s is null.
	 */
	public static String escapeJavaLiteral(String s) {
		int length = s.length();
		int newLength = length;
		// first check for characters that might
		// be dangerous and calculate a length
		// of the string that has escapes.
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			switch (c) {
				case'\"':
				case'\'':
				case'\n':
				case'\r':
				case'\t':
				case'\\': {
					newLength += 1;
				}
				break;
			}
		}
		if (length == newLength) {
			// nothing to escape in the string
			return s;
		}
		StringBuffer sb = new StringBuffer(newLength);
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			switch (c) {
				case'\"': {
					sb.append("\\\"");
				}
				break;
				case'\'': {
					sb.append("\\\'");
				}
				break;
				case'\n': {
					sb.append("\\n");
				}
				break;
				case'\r': {
					sb.append("\\r");
				}
				break;
				case'\t': {
					sb.append("\\t");
				}
				break;
				case'\\': {
					sb.append("\\\\");
				}
				break;
				default: {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Trim any of the characters contained in the second
	 * string from the beginning and end of the first.
	 *
	 * @param s String to be trimmed.
	 * @param c list of characters to trim from s.
	 * @return trimmed String.
	 * @throws NullPointerException if s is null.
	 */
	public static String trim(String s, String c) {
		int length = s.length();
		if (c == null) {
			return s;
		}
		int cLength = c.length();
		if (c.length() == 0) {
			return s;
		}
		int start = 0;
		int end = length;
		boolean found; // trim-able character found.
		int i;
		// Start from the beginning and find the
		// first non-trim-able character.
		found = false;
		for (i = 0; !found && i < length; i++) {
			char ch = s.charAt(i);
			found = true;
			for (int j = 0; found && j < cLength; j++) {
				if (c.charAt(j) == ch) found = false;
			}
		}
		// if all characters are trim-able.
		if (!found) return "";
		start = i - 1;
		// Start from the end and find the
		// last non-trim-able character.
		found = false;
		for (i = length - 1; !found && i >= 0; i--) {
			char ch = s.charAt(i);
			found = true;
			for (int j = 0; found && j < cLength; j++) {
				if (c.charAt(j) == ch) found = false;
			}
		}
		end = i + 2;
		return s.substring(start, end);
	}

	public static String highlightTerms(String s, ArrayList searchTermList, String highlightStartTag, String highlightEndTag) {
		String lowerTerm;
		String searchTerm;
		String lowerString = s.toLowerCase();
		String returnString = s;
		if (lowerString.indexOf("central") >= 0) {
			//System.out.println("StringHelper -- highlightTerms s: " + s);
			//System.out.println("StringHelper -- highlightTerms lowerString: " + lowerString);
			for (int i = 0; i < searchTermList.size(); i++) {
				//System.out.println("StringHelper -- highlightTerms searchTerm: " + (String)searchTermList.get(i));
			}
		}
		int start;
		int end;
		int replaceLengthAdd = highlightStartTag.length() + highlightEndTag.length();
		for (int i = 0; i < searchTermList.size(); i++) {
			start = 0;
			end = 0;
			boolean wildAtStart = false;
			boolean wildAtEnd = false;
			searchTerm = (String) searchTermList.get(i);
			if (searchTerm.startsWith("*")) {
				wildAtStart = true;
				searchTerm = searchTerm.substring(1);
			}
			if (searchTerm.endsWith("*")) {
				wildAtEnd = true;
				searchTerm = searchTerm.substring(0, searchTerm.length() - 1);
			}
			lowerTerm = searchTerm.toLowerCase();
			while ((start = lowerString.indexOf(lowerTerm, end)) >= 0) {
				end = start + lowerTerm.length();
				if (start == 0 ||
						wildAtStart ||
						Character.isWhitespace(lowerString.charAt(start - 1)) ||
						!Character.isLetterOrDigit(lowerString.charAt(start - 1))) {
					if (end >= lowerString.length() ||
							wildAtEnd ||
							Character.isWhitespace(lowerString.charAt(end)) ||
							!Character.isLetterOrDigit(lowerString.charAt(end))) {

						returnString = returnString.substring(0, start) +
								highlightStartTag + returnString.substring(start, end) +
								highlightEndTag + returnString.substring(end);

						end += replaceLengthAdd;
						lowerString = returnString.toLowerCase();
					}
				}
			}
		}
		return returnString;
	}


	/**
	 * A hash can be passed as a param using its toString() method; it comes
	 * in looking like:
	 * {key1=val1, key2=val2, key3=val3} ; this method restores it to a hash
	 * <p/>
	 * copied from tedmaint in ServletUtilities (CM)
	 */
	public static Hashtable unpackHash(String str) {

		Hashtable hash = new Hashtable();
		str = str.replace('{', ' ');
		str = str.replace('}', ' ');
		str = str.trim();
		String pairs[] = StringHelper.split(str, ",");
		String nextPair[];
		for (int i = 0; i < pairs.length; i++) {
			nextPair = StringHelper.split(pairs[i], "=");
			hash.put(nextPair[0].trim(), nextPair[1].trim());
		}
		return hash;

	}//end public static Hashtable unpackHash(String str)


	public static String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.flush();
		String ret = sw.toString();
		pw.close();
		try {
			sw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}

	public static String getStackTrace(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.flush();
		String ret = sw.toString();
		pw.close();
		try {
			sw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}

	public static String trimToLength(String stringToTrim, int maxLength) {
		if (stringToTrim != null) {
			if (stringToTrim.length() <= maxLength) {
				return stringToTrim;
			} else {
				return stringToTrim.substring(0, maxLength - 1);
			}
		} else {
			return "";
		}
	}

	public static void parseDate(ArchDescription accession, String dateString) {
		//System.out.println("here1");
		String allowedCharacters = "0123456789-,/ ;";
		String delimeters = "/-, ";

		String newString = "";
		String oldString = dateString.trim();

		String thisCharacter;

		for (int i = 0; i < oldString.length(); i++) {
			thisCharacter = oldString.substring(i, i + 1);
			if (allowedCharacters.contains(thisCharacter)) {
				newString += thisCharacter;
			}
		}

		ArrayList<String> tokenArray = new ArrayList<String>();
		String thisToken = "";
		for (int i = 0; i < newString.length(); i++) {
			thisCharacter = newString.substring(i, i + 1);
			if (delimeters.contains(thisCharacter)) {
				//we have a delimiter
				if (thisToken.length() != 0) {
					tokenArray.add(thisToken);
					thisToken = "";
				}
				System.out.println("delimiter = " + thisCharacter);
				tokenArray.add("delimiter:" + thisCharacter);
			} else {
				thisToken += thisCharacter;
			}
		}
		if (thisToken.length() != 0) {
			tokenArray.add(thisToken);
			thisToken = "";
		}

		//remove anything that is not 4 digits or a delimiter
		ArrayList<String> tokenArray2 = new ArrayList<String>();
		for (String s : tokenArray) {
			if (s.startsWith("delimiter:")) {
				tokenArray2.add(s);
			} else if (s.length() == 4) {
				tokenArray2.add(s);
			}
		}
		//remove consective delimiters preferencing a dash
		ArrayList<String> tokenArray3 = new ArrayList<String>();
		boolean isPreviousDelimiter = false;
		for (String s : tokenArray2) {
			if (s.startsWith("delimiter:")) {
				if (isPreviousDelimiter) {
					//do nothing for now
				} else {
					tokenArray3.add(s);
				}
			} else {
				tokenArray3.add(s);
			}
		}

		String stringBeginDate;
		String stringEndDate;

		if (tokenArray3.size() == 1) {
			stringBeginDate = tokenArray3.get(0);
			if (stringBeginDate.length() == 4) {
				//System.out.println(stringBeginDate+"--"+stringBeginDate);
				accession.setDateBegin(new Integer(stringBeginDate));
				accession.setDateEnd(new Integer(stringBeginDate));
			}
		} else if (tokenArray3.size() == 3 && tokenArray3.get(1).equalsIgnoreCase("delimiter:-")) {
			stringBeginDate = tokenArray3.get(0);
			stringEndDate = tokenArray3.get(2);
			if (stringBeginDate.length() == 4 && stringEndDate.length() == 4) {
				//System.out.println(stringBeginDate+"--"+stringEndDate);
				accession.setDateBegin(new Integer(stringBeginDate));
				accession.setDateEnd(new Integer(stringEndDate));
			}
		}

	}

	//takes dates of the form XXXXdelimiterXXXX
	//Example: 1990-1995; 1990,1995; 1990 1995
	//
	//
	public static void simpleParseDate(ArchDescription archDescription, String str, boolean inclusive) {
		String delim = "";
		if (debug) {
			System.out.println("IN here");
		}
		//System.out.println("str = "+str);
		if (str.contains(","))
			delim = delim + ",";
		else if (str.contains("/"))
			delim = delim + "/";
		else if (str.contains(" "))
			delim = delim + " ";
		if (delim.length() > 1)
			return;
		if (delim.equals("")) {
			try {
				if(inclusive){
					archDescription.setDateBegin(Integer.parseInt(str));
					archDescription.setDateEnd(Integer.parseInt(str));
				}
				else if(!inclusive && archDescription instanceof AccessionsResourcesCommon){
					((AccessionsResourcesCommon)archDescription).setBulkDateBegin(Integer.parseInt(str));
					((AccessionsResourcesCommon)archDescription).setBulkDateEnd(Integer.parseInt(str));	
				}
			}
			catch (NumberFormatException nfe) {
				return;
			}
		}
		String dates[] = str.split(delim);
		//System.out.println("dates0 = "+dates[0]);
		if (dates.length != 2)
			return;
		else {
			try {
				if(inclusive){
					archDescription.setDateBegin(Integer.parseInt(dates[0]));
					archDescription.setDateEnd(Integer.parseInt(dates[1]));
				}
				else if(!inclusive){
					((AccessionsResourcesCommon)archDescription).setBulkDateBegin(Integer.parseInt(dates[0]));
					((AccessionsResourcesCommon)archDescription).setBulkDateEnd(Integer.parseInt(dates[1]));	
				}
			}
			catch (NumberFormatException nfe) {
				return;
			}
		}

	}

	public static String getFileExtension(File file) {
		return getFileExtension(file.getPath());
	}

	public static String getFileExtension(String pathName) {
		// getting the extension of a filename, (plain or including dirname)
// This code is much faster than any regex technique.

// filename without the extension
		String choppedFilename;

// extension without the dot
		String ext;

// where the last dot is. There may be more than one.
		int dotPlace = pathName.lastIndexOf('.');

		if (dotPlace >= 0) {
			// possibly empty
			choppedFilename = pathName.substring(0, dotPlace);

			// possibly empty
			ext = pathName.substring(dotPlace + 1);
		} else {
			// was no extension
			choppedFilename = pathName;
			ext = "";
		}
		return ext;
	}

	public static String getFilePathWithoutExtension(File file) {
		return getFilePathWithoutExtension(file.getPath());
	}

	public static String getFilePathWithoutExtension(String pathName) {
		// getting the extension of a filename, (plain or including dirname)
// This code is much faster than any regex technique.

// filename without the extension
		String choppedFilename;

// extension without the dot
		String ext;

// where the last dot is. There may be more than one.
		int dotPlace = pathName.lastIndexOf('.');

		if (dotPlace >= 0) {
			// possibly empty
			choppedFilename = pathName.substring(0, dotPlace);

			// possibly empty
			ext = pathName.substring(dotPlace + 1);
		} else {
			// was no extension
			choppedFilename = pathName;
			ext = "";
		}
		return choppedFilename;
	}

	/**
	 * This method will remove the tags from a given string.
	 * If the string contains no tags it will simply be returned.
	 * Example: <tag>string in tag</tag> returns "string in tag"
	 * Example: <tag att="1">string in tag</tag> returns "string in tag"
	 */

	public static String tagRemover(String str) {
        if(str==null)
            return null;
        
		if (!(str.contains("<"))) {
			return str;
		}
		while (str.contains("<")) {
			int index1 = str.indexOf("<");
			int index2 = str.indexOf(">");
			if (index2 < index1)
				return str;
			String tag = str.substring(index1, index2 + 1);
			str = str.replace(tag, "");
		}
		return str;

	}

	public static String[] extractStringAndValueInParenthesis(String val) {
		String value[] = new String[2];
		if (val == null) {
			return null;
		} else if (val.length() < 3) {
			return null;
		} else if ((!val.contains("(")) || (!(val.contains(")")))) {
			//value[0]=val;
			//value[1]=val;
			//return value;
			return null;
		}

		int a;
		int b;
		a = val.indexOf("(");
		b = val.indexOf(")");
		value[0] = val.substring(0, a).trim();
		value[1] = val.substring(a + 1, b).trim();
		return value;
	}

	public static String getFirstPartofLangString(String val, String delim) {
		if (val == null)
			return val;
		if ((val.indexOf(delim)) == -1)
			return val;
		String a[] = val.split(";");
		if (a.length > 0)
			return a[0].trim();
		else return val;
	}

	public static String getSecondPartofLangString(String val, String delim) {
		if ((val.indexOf(delim)) == -1)
			return val;
		String a[] = val.split(";");
		if (a.length > 1)
			return a[1].trim();
		else return val;
	}

	public static boolean isNotEmpty(String val) {
		if (val != null && val.length() > 0)
			return true;
		else
			return false;
	}

	public static boolean isEmpty(String val) {
		if (val != null && val.length() > 0)
			return false;
		else
			return true;
	}

	public static String concatenateFields(String delimiter, String... fields) {
		String value = "";
		for (int a = 0; a < fields.length; a++) {
			if (null != fields[a] && fields[a].length() != 0) {
				value = value + fields[a];
				//if(a!=(fields.length-1))
				value = value + delimiter;
			}
		}
		if (value != null && value.length() > delimiter.length())
			return value.substring(0, (value.length() - delimiter.length()));
		else
			return value;
	}

	public static String concatenateAllFields(String delimiter, String... fields) {
		String value = "";
		for (int a = 0; a < fields.length; a++) {
			if (fields[a] == null || fields[a].length() == 0)
				return "";
		}
		value = concatenateFields(delimiter, fields);
		return value;
	}

	public static String handleRunningSpaces(String val) {
		if (val == null || val.length() < 2)
			return val;
		while (val.contains("  ")) {
			val = val.replace("  ", " ");
		}
		return val;
	}

	public static String cleanUpWhiteSpace(String stringToStrip) {
		return whiteSpaceStripper.unmarshal(stringToStrip);
	}

	public static String concat(ArrayList array, String seperator) {

		String returnValue = "";
		String thisString = "";
		for (int i=0; i < array.size(); i++) {
			thisString = (String)array.get(i);
			if (thisString != null && thisString.length() > 0) {
				returnValue += seperator + thisString;
			}
		}

		if(returnValue.length() == 0) {
			return returnValue;
		} else {
			return returnValue.substring(seperator.length());
		}
	}

	public static String concat(String seperator, String... strings) {

		String returnValue = "";
		for (String thisString: strings) {
			if (thisString != null && thisString.length() > 0) {
				returnValue += seperator + thisString;
			}
		}

		if(returnValue.length() == 0) {
			return returnValue;
		} else {
			return returnValue.substring(seperator.length());
		}
	}

	public static String convertToNmtoken(String string) {
		//convert all spaces to _
		string = string.trim();
		string = string.replace(" ", "_");
		//convert everything that is not a letter or digit or ., -, _, : to _
		String legalValues =".-_:";
		String returnString = "";
		for (char character: string.toCharArray()) {
			if (Character.isLetterOrDigit(character)) {
				returnString += character;
			} else if (legalValues.contains(Character.toString(character))) {
				returnString += character;
			} else {
				returnString += "_";
			}
		}

		return returnString;
	}

  /** A simple set of methods
  * http://www.anyexample.com/programming/java/java_simple_class_to_compute_md5_hash.xml
  * that computes the MD5 hash from a given string.
  */
  private static String convertToHex(byte[] data) {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < data.length; i++) {
      int halfbyte = (data[i] >>> 4) & 0x0F;
      int two_halfs = 0;
      do {
        if ((0 <= halfbyte) && (halfbyte <= 9))
          buf.append((char) ('0' + halfbyte));
        else
          buf.append((char) ('a' + (halfbyte - 10)));
        halfbyte = data[i] & 0x0F;
      } while (two_halfs++ < 1);
    }
    return buf.toString();
  }

  // this is called by passing the string to it
  public static String MD5(String text)
          throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest md;
    md = MessageDigest.getInstance("MD5");
    byte[] md5hash = new byte[32];
    md.update(text.getBytes("iso-8859-1"), 0, text.length());
    md5hash = md.digest();
    return convertToHex(md5hash);
  }
  
  public static String removeInvalidFileNameCharacters(String fileName){
	  if(fileName==null)
		  return fileName;
	  char a = 0x5c;
	  fileName = fileName.replaceAll(Matcher.quoteReplacement("\\"),"");
	  fileName = fileName.replaceAll("/", "");
	  fileName = fileName.replaceAll(":", "");
	  fileName = fileName.replaceAll("\\*", "");
	  fileName = fileName.replaceAll("\\?", "");
	  fileName = fileName.replaceAll("\"", "");
	  fileName = fileName.replaceAll("<", "");
	  fileName = fileName.replaceAll(">", "");
	  fileName = fileName.replaceAll("|", "");
	  return fileName;
  }

    /**
     * Method to return a decimal which only has zeros following the decimal as an integer value
     * @param valueD The String value of the decimal
     * @return The cleaned up decimal
     */
    public static String handleDecimal(String valueD) {
        if (valueD != null && valueD.contains(".")) {
            String dec =
                valueD.substring(valueD.indexOf(".") + 1, valueD.length());
            if (dec.matches("0+")) { // check to see we don't have zero one or more times
                String temp = valueD.substring(0, valueD.indexOf("."));
                return temp;
            }
        }
        return valueD;

    }

  public static void main(String args[]) {
//		System.out.println(cleanUpWhiteSpace("this\nis sample                sdf   "));
		System.out.println(removeInvalidFileNameCharacters("sampl*?\\e"));

  }

}

