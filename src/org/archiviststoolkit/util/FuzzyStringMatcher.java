/*

This software is OSI Certified Open Source Software.
OSI Certified is a certification mark of the Open Source Initiative.

The license (Mozilla version 1.0) can be read at the MMBase site.
See http://www.MMBase.org/license

*/

package org.archiviststoolkit.util;

/**
 * Utility class, providing methods for a fuzzy comparison between strings.
 * @author Rob van Maris (Finalist IT Group)
 * @since MMBase-1.5
 * @version $Id: FuzzyStringMatcher.java,v 1.4 2003/12/22 16:47:19 pierre dead $
 */

public class FuzzyStringMatcher {

    /** Replacements for diacritical characters. */
    private final static String[] NORMALIZE_REPLACEMENTS = {
        "a", "a", "a", "a", "a", "a", "ae", "c", //c0-c7
        "e", "e", "e", "e", "i", "i", "i", "i", //c8-cf
        "d", "n", "o", "o", "o", "o", "o", "x", //d0-d7
        "o", "u", "u", "u", "u", "y", "b", "s", //d8-df
        "a", "a", "a", "a", "a", "a", "ae", "c", //e0-e7
        "e", "e", "e", "e", "i", "i", "i", "i", //e8-ef
        "o", "n", "o", "o", "o", "o", "o", " ", //f0-f7
        "o", "u", "u", "u", "u", "y", "b", "y" //f8-ff
    };

    /* Strings to be matched, converted to char arrays. */
    private char[] chars1, chars2;

    /* Length of the strings. */
    private int length1, length2;

    /* Cache of getMismatch() results,
     * i.e. resultsCache[i1, 12] caches the result of calculateMismatch(i1, i2).
     */
    private Integer[][] resultsCache;

    /* Result of the matching, this is the minimum number of typo's
     * necessary to account for the differences between the two strings,
     * if they were meant to be identical.
     */
    private int mismatch;

    /**
     * Calculates the mismatch between two strings.
     * @param string1 first string
     * @param string2 second string
     * @return The number of mismatches,this is the minimum number of typo's
     * necessary to account for the differences between the two strings,
     * if they were meant to be identical.
     */
    public static int getMismatch(String string1, String string2) {
        return new FuzzyStringMatcher(string1, string2).getMismatch();
    }

    /**
     * Calculates the match rate, a value between 0 and 1, proportional
     * to the rate the two strings match (1 is exact match).
     * This is calculated as
     * 1 - (mismatch/max(string1.length(), string2.length())).
     * @param string1 first string
     * @param string2 second string
     * @return The match rate.
     */
    public static float getMatchRate(String string1, String string2) {
        return new FuzzyStringMatcher(string1, string2).getMatchRate();
    }

    /**
     * Creates normalized title.
     * e.g. all non-alphanumeric characters replaced by white space, all characters converted
     * to lowercase non-diacritical characters, and all white space
     * sequences contracted to a single white space character.
     * This is a convenience method, provided to make string comparison
     * easier by removing (more or less) arbitrary differences.
     *
     * @param str The original title.
     * @return The normalized title.
     */
    public static String normalizeString(String str) {
        StringBuffer sb = new StringBuffer();
        char[] chars = str.toCharArray();
        boolean whiteSpace = false;

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch == '&') {    // Ampersand
                sb.append(ch);
            } else if (ch < 0x0030) {
                if (!whiteSpace) {
                    sb.append(" ");
                    whiteSpace = true;
                }
            } else if (ch < 0x003a) {   // Digits.
                sb.append(ch);
                whiteSpace = false;
            } else if (ch < 0x0041) {
                if (!whiteSpace) {
                    sb.append(" ");
                    whiteSpace = true;
                }
            } else if (ch < 0x005b) { // Upper case letters.
                sb.append((char) (ch + 32));
                whiteSpace = false;
            } else if (ch < 0x061) {
                if (!whiteSpace) {
                    sb.append(" ");
                    whiteSpace = true;
                }
            } else if (ch < 0x007b) { // Lower case letters.
                sb.append(ch);
                whiteSpace = false;
            } else if (ch < 0x0c0) {
                if (!whiteSpace) {
                    sb.append(" ");
                    whiteSpace = true;
                }
            } else if (ch == 0xf7) {
                if (!whiteSpace) {
                    sb.append(" ");
                    whiteSpace = true;
                }
            } else {
                sb.append(NORMALIZE_REPLACEMENTS[ch-0x0c0]);
                whiteSpace = false;
            }
        }
        return sb.toString();
    }

    /**
     * Creates new FuzzyStringMatcher and executes the matching routine.
     * @param string1 first string
     * @param string2 second string
     */
    private FuzzyStringMatcher(String string1, String string2) {

        // Convert strings to char arrays in order to speed up access
        // to the individual characters.
        // Tests confirm a slight performance gain over using plain
        // String objects.
        chars1 = string1.toCharArray();
        chars2 = string2.toCharArray();

        // Store the string lengths, instead calling length() repeatedly.
        length1 = string1.length();
        length2 = string2.length();

        // Initialize the cache. It is dimensioned to match the range
        // of input parameters of the calculateMismatch method.
        resultsCache = new Integer[length1 + 1][length2 + 1];

        // Execute the matching routine.
        mismatch = calculateMismatch(0, 0);

        // Make the resultsCache eligible for garbage collection.
        resultsCache = null;
    }


    /**
     * Get the result of the matching. This is calculated on creation of
     * this instance, so it is not calculated again when this method
     * is called.
     * @return The number of mismatches,this is the minimum number of typo's
     * necessary to account for the differences between the two strings,
     * if they were meant to be identical.
     */
    private int getMismatch() {
        return mismatch;
    }

    /**
     * Get the match rate, a value between 0 and 1, proportional
     * to the rate the two strings match.
     * This is calculated as
     * 1 - (mismatch/max(string1.length(), string2.length())).
     * @return the match rate.
     */
    private float getMatchRate() {
        return 1 - (mismatch / (float) Math.max(length1, length2));
    }

    /**
     * Calculate the mismatch between a substring of string1
     * and a substring of string2. This method calls itself
     * recursively.
     * @param i1Start start index in substring of string1
     * @param i2Start start index in substring of string2
     * @return the mismatch between the substrings.
     */
    private int calculateMismatch(int i1Start, int i2Start) {

        // Retreive the result from the cache if possible.
        if (resultsCache[i1Start][i2Start] != null) {
            return resultsCache[i1Start][i2Start].intValue();
        }

        int mismatch = 0;

        int i1 = i1Start;
        int i2 = i2Start;
        while (i1 < length1 && i2 < length2) {
            if (chars1[i1] == chars2[i2]) {

                // Characters at current position match,
                // so proceed to next position.
                i1++;
                i2++;
            } else {

                // Characters at current position don't match.
                mismatch++;

                // Calculate the mismatch in the remaining substrings,
                // based on 3 different assumptions on how the mismatch
                // at the current position was produced:
                // 1) Assume  a character was ommitted in string1.
                // (Or, equivalently: a character was added in string2.)
                int m1 = calculateMismatch(i1, i2 + 1);

                // 2) Assume a wrong character was substituted
                // in one of the strings.
                int m2 = calculateMismatch(i1 + 1, i2);

                // 3) Assume a character was ommitted in string2.
                // (Or, equivalently: a character was added in string1.)
                int m3 = calculateMismatch(i1 + 1, i2 + 1);

                // Of these three, adopt the one that produces the
                // smallest mismatch; adjust the mismatch accordingly.
                if (m1 < m2) {
                    if (m1 < m3) {
                        mismatch += m1;
                    } else {
                        mismatch += m3;
                    }
                } else {
                    if (m2 < m3) {
                        mismatch += m2;
                    } else {
                        mismatch += m3;
                    }
                }

                // Store the result in the cache and return it.
                resultsCache[i1Start][i2Start] = new Integer(mismatch);
                return mismatch;
            }
        }

        // The end of one of the strings has been reached.
        // Each remaining character in the other string is a mismatch.
        if (length1 == i1) {

            // The end of string1 is reached, add the remaining
            // number of characters in string2 to the mismatch.
            mismatch += (length2 - i2);
        } else {

            // The end of string2 is reached, add the remaining
            // number of characters in string1 to the mismatch.
            mismatch += (length1 - i1);
        }

        // Store the result in the cacht and return it.
        resultsCache[i1Start][i2Start] = new Integer(mismatch);
        return mismatch;
    }

//    // for testing only
//    private static void test(String s1, String s2) {
//        float result = 0;
//        FuzzyStringMatcher sm = null;
//        long startTime = System.currentTimeMillis();
//        for (int i = 0; i < 10000; i++) {
//            result = getMismatch(s1, s2);
//        }
//        long timeNeeded = System.currentTimeMillis() - startTime;
//        System.out.println("mismatch: " + result
//            + ", in " + timeNeeded/10000f + "ms.");
//    }
//
//    // for testing only
//    public static void main(String[] args) {
//        test(args[0], args[1]);
//    }

}
