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
 * Date: Dec 30, 2005
 * Time: 10:26:36 AM
 */

package org.archiviststoolkit.util;

import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.exceptions.UnsupportedDatabaseType;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.dialog.UserPreferencesDialog;
import org.archiviststoolkit.dialog.ErrorDialog;

import java.util.prefs.Preferences;
import java.util.prefs.BackingStoreException;
import java.awt.*;

public class UserPreferences {

	public static final String DATABASE_URL = "databaseUrl";
	public static final String DATABASE_USERNAME = "databaseUsername";
	public static final String DATABASE_PASSWORD = "databasePassword";
	public static final String SAVE_PATH = "savepath";
	public static final String DATABASE_TYPE = "databaseType";
	public static final String FONT_FAMILY = "fontFamily";
	public static final String FONT_STYLE = "fontStyle";
    public static final String FONT_SIZE = "fontSize";
    public static final String SPELLCHECK_ENABLE = "spellCheckEnable";
    public static final String SPELLCHECK_HIGHLIGHT_ENABLE = "spellCheckHighlightEnable";

    private String databaseUrl = "";
	private String databaseUserName = "";
	private String databasePassword = "";
	private String savePath = "";
	private String databaseType = "";
    private Font font = null;
    private boolean enableSpellCheck = false;
    private boolean enableSpellCheckHighlight = true;
    private boolean spellCheckSet = true;

	private static UserPreferences singleton = null;

	private UserPreferences() {
	}

	public static UserPreferences getInstance() {
		if (singleton == null) {
			singleton = new UserPreferences();
		}

		return singleton;
	}

	public String getDatabaseUrl() {
		return databaseUrl;
	}

	public void populateFromPreferences() {
		Preferences userPrefs = Preferences.userNodeForPackage(ApplicationFrame.class);
		this.setDatabaseUrl(userPrefs.get(DATABASE_URL, ""));
		this.setDatabaseUserName(userPrefs.get(DATABASE_USERNAME, ""));
		this.setDatabasePassword(userPrefs.get(DATABASE_PASSWORD, ""));
		this.setDatabaseType(userPrefs.get(DATABASE_TYPE, SessionFactory.DATABASE_TYPE_MYSQL));
		this.setSavePath(userPrefs.get(SAVE_PATH, "."));

        // now get information for the user defined font
        String fontFamily = userPrefs.get(FONT_FAMILY,"");
        int fontStyle =  Integer.parseInt(userPrefs.get(FONT_STYLE,"" + Font.PLAIN));
        int fontSize = Integer.parseInt(userPrefs.get(FONT_SIZE,"12"));

        if(StringHelper.isNotEmpty(fontFamily.trim())) {
            font = new Font(fontFamily, fontStyle, fontSize);
        }

        // set the spell check options
        String spellCheck = userPrefs.get(SPELLCHECK_ENABLE, "");
        if(spellCheck.equals("yes")) {
            enableSpellCheck = true;
        } else if(spellCheck.equals("no")) {
            enableSpellCheck = false;
        } else { // set variable used if spell check has not been set
            spellCheckSet = false;
        }

        String spellCheckHighlight = userPrefs.get(SPELLCHECK_HIGHLIGHT_ENABLE, "");
        if(spellCheckHighlight.equals("yes")) {
            enableSpellCheckHighlight = true;
        } else {
            enableSpellCheckHighlight = false;
        }
	}

	public void saveToPreferences() {
		Preferences userPrefs = Preferences.userNodeForPackage(ApplicationFrame.class);
		userPrefs.put(DATABASE_URL, this.getDatabaseUrl());
		userPrefs.put(DATABASE_USERNAME, this.getDatabaseUserName());
		userPrefs.put(DATABASE_PASSWORD, this.getDatabasePassword());
		userPrefs.put(DATABASE_TYPE, this.getDatabaseType());
		userPrefs.put(SAVE_PATH, this.getSavePath());

        // see if to set the font preferences
        if(font != null) {
            userPrefs.put(FONT_FAMILY, font.getFamily());
            userPrefs.put(FONT_STYLE,"" + font.getStyle());
            userPrefs.put(FONT_SIZE,"" + font.getSize());
        }

        // save the enable spell check option
        if(enableSpellCheck) {
            userPrefs.put(SPELLCHECK_ENABLE, "yes");
        } else {
            userPrefs.put(SPELLCHECK_ENABLE, "no");
        }

        if(enableSpellCheckHighlight) {
            userPrefs.put(SPELLCHECK_HIGHLIGHT_ENABLE, "yes");
        } else {
            userPrefs.put(SPELLCHECK_HIGHLIGHT_ENABLE, "no");
        }

		try {
			userPrefs.flush();
		} catch (BackingStoreException e) {
			new ErrorDialog("", e).showDialog();
		}
	}

	public boolean checkForDatabaseUrl() {
		if (databaseUrl.length() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public int displayUserPreferencesDialog() {
		UserPreferencesDialog userPrefDialog = new UserPreferencesDialog();
		return init(userPrefDialog);
	}

	public int displayUserPreferencesDialog(Frame owner) {
		UserPreferencesDialog userPrefDialog = new UserPreferencesDialog(owner);
		return init(userPrefDialog);
	}

	public int displayUserPreferencesDialog(Dialog owner) {
		UserPreferencesDialog userPrefDialog = new UserPreferencesDialog(owner);
		return init(userPrefDialog);
	}

	private int init(UserPreferencesDialog userPrefDialog) {
		userPrefDialog.populateFromUserPreferences(this);
		int returnValue = userPrefDialog.showDialog();
		if (returnValue == javax.swing.JOptionPane.OK_OPTION) {
			this.setDatabaseUrl(userPrefDialog.getDatabaseUrl().trim());
			this.setDatabaseUserName(userPrefDialog.getUserName().trim());
			this.setDatabasePassword(userPrefDialog.getPassword());
			this.setDatabaseType(userPrefDialog.getDatabaseType());
			this.saveToPreferences();
		}

		return returnValue;
	}

	public void updateSessionFactoryInfo() throws UnsupportedDatabaseType {
		SessionFactory.resetFactory();
		SessionFactory.setDatabaseUrl(this.getDatabaseUrl());
		SessionFactory.setUserName(this.getDatabaseUserName());
		SessionFactory.setPassword(this.getDatabasePassword());
		SessionFactory.setDatabaseType(this.getDatabaseType());
	}

	public void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}

	public String getDatabaseUserName() {
		return databaseUserName;
	}

	public void setDatabaseUserName(String databaseUserName) {
		this.databaseUserName = databaseUserName;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public void setDatabasePassword(char[] databasePassword) {
		this.databasePassword = new String(databasePassword).trim();
	}

	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

    /**
     * Method to return the user defined font
     */
    public Font getFont() {
        return font;
    }

   /**
     * Set the user defined font
     *
     * @param font
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * Method use to specify whether to use the experimental spell check
     * function
     *
     * @param enableSpellCheck
     */
    public void setEnableSpellCheck(boolean enableSpellCheck) {
        this.enableSpellCheck = enableSpellCheck;
    }

    /**
     * Method to get the boolean which indicates if spel checker is being used
     *
     * @return The enable spell check variable
     */
    public boolean getEnableSpellCheck() {
        return enableSpellCheck;
    }

    /**
     * Method to enable spell check highlighting
     *
     * @param enableSpellCheckHighlight
     */
    public void setEnableSpellCheckHighlight(boolean enableSpellCheckHighlight) {
        this.enableSpellCheckHighlight = enableSpellCheckHighlight;
    }

    /**
     * Method to return boolean that states whether to enable spellcheck
     * highlighting
     *
     * @return boolean to specify whether to enable highlighting
     */
    public boolean getEnableSpellCheckHighlighting() {
        return enableSpellCheckHighlight;
    }

    /**
     * Method to return if spell check was set or not. This is needed
     * to provide a way to detect if the user turned of spell check
     * all together
     *
     * @return true if its set, false otherwise
     */
    public boolean getSpellCheckSet() {
        return spellCheckSet;
    }
}
