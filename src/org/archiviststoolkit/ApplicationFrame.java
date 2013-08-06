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
 */

package org.archiviststoolkit;

//==============================================================================
// Import Declarations
//==============================================================================

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.*;
import java.util.prefs.Preferences;
import java.io.File;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.importer.*;
import org.archiviststoolkit.swing.*;
import org.archiviststoolkit.swing.SimpleFileFilter;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.structure.*;
import org.archiviststoolkit.util.*;
import org.archiviststoolkit.dialog.*;
import org.archiviststoolkit.dialog.SplashScreen;
import org.archiviststoolkit.exceptions.WrongNumberOfConstantsRecordsException;
import org.archiviststoolkit.exceptions.UnsupportedParentComponentException;
import org.archiviststoolkit.exceptions.DomainEditorCreationException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import com.centerkey.utils.BareBonesBrowserLaunch;

import org.archiviststoolkit.importer.MARCXML.MARCImportHandler;
import org.archiviststoolkit.report.ReportFactory;
import org.archiviststoolkit.report.RepositoryProfileProcessor;
import org.archiviststoolkit.plugin.*;
import org.ananas.mac.menu.ApplicationItemFactory;
import org.ananas.mac.menu.ApplicationItem;
import say.swing.JFontChooser;


/**
 * The is the main application frame which acts as the root container.
 * It does little but set up all the subcontainers, and binds the appropriate actions
 * to the appropriate handlers. The only real smarts are in the positioning and window state
 * which is stored in a user preference node.
 */

//public final class ApplicationFrame extends JFrame implements ActionListener, SingleInstanceListener {
public final class ApplicationFrame extends JFrame implements ActionListener {

	/**
	 * Static reference for child dialogs.
	 */

	private static ApplicationFrame singleton = null;

	private boolean debug = false;
	/**
	 * resource bundle used for i18n of text messages.
	 */

	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("org.archiviststoolkit.resources.messages.messages");

	/**
	 * The width the application should open its main window with.
	 * Only relevant the first time the application opens. From then on
	 * in the value is saved into Preferences.
	 */

	private static final int DEFAULT_WINDOW_WIDTH = 800;

    /**
     * HashMap that stores the ids and names of any default plugins found
     */
    private HashMap pluginNames = null;

    /**
     * HashMap that stores the ids and names of any import plugins found
     */
    private HashMap importPluginNames = null;

    /**
     * HashMap that stores the ids and names of any tool plugins found
     */
    private HashMap toolPluginNames = null;

	/**
	 * The height the application should open its main window with.
	 * Only relevant the first time the application opens. From then on
	 * in the value is saved into Preferences.
	 */

	private static final int DEFAULT_WINDOW_HEIGHT = 600;
	public static Color BACKGROUND_COLOR = new Color(200, 205, 232);
	public static Color MENU_BAR_BACKGROUND_COLOR = new Color(66, 60, 111);

	public static String URL_LICENSE_AGREEMENT = "http://www.archiviststoolkit.org/downloads/license.shtml";
	public static String URL_DOWNLOAD = "http://www.archiviststoolkit.org/atDistribution/install.shtml";
	public static SimpleDateFormat applicationDateFormat = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);

    // This specifies whether to enable the spell check function
    // and highlighting. The default is to have it enable
    public static boolean enableSpellCheck = true;
    public static boolean enableSpellCheckHighlight = true;

    // boolean indicating whether the editor for a record is opened
    // this is needed to fix a bug which doesn't allow dates and
    // extent information to be save correctly
    public static boolean editorOpen = false;

	private MyTimer timer;
	private String startupLog;

	private Boolean recordDirty;

	private Users currentUser;
	private String atVersionNumber;
	private String issueReportingURL;

	private DomainImportController controller;
	private ATFileChooser filechooser;
	private ImportHandler handler;
	private WorkSurfaceContainer workSurfaceContainer;
	/**
	 * File Menu Actions.
	 */
	private ConcreteAction exitAction = null;

	/**
	 * The import menu actions.
	 */
	private ConcreteAction import_OliviaName = null;
	private ConcreteAction import_Accessions = null;
	private ConcreteAction import_AccessionsXML = null;
	private ConcreteAction import_Subject = null;
	private ConcreteAction import_EacName = null;
	private ConcreteAction import_EAD = null;
	private ConcreteAction import_MARC = null;
	private ConcreteAction import_DigitalObjects = null;

	/**
	 * The repair menu actions.
	 */
//    private ConcreteAction repair_UpdateResourceHasNotesAndHasChildrenFlags = null;
//    private ConcreteAction repair_UpdateDisplayFields = null;
//	private ConcreteAction repair_ThrowError = null;
//	private ConcreteAction repair_reconcileFieldInfo = null;
//	private ConcreteAction repair_parseLocationInfo = null;

	/**
	 * The admin manu actions.
	 */
    private ConcreteAction admin_CompileJasperReport = null;
    private ConcreteAction admin_ReloadReports = null;
	//	private ConcreteAction admin_UserPreferences = null;
	private ConcreteAction admin_DateFormatSetting = null;

	private ConcreteAction admin_EditUsers = null;
	private ConcreteAction admin_EditRepositories = null;
	private ConcreteAction admin_ConfigureApplication = null;
	private ConcreteAction admin_EditLookupLists = null;
	private ConcreteAction admin_EditLocations = null;
	private ConcreteAction admin_EditNotesEtc = null;
	private ConcreteAction admin_RepositoryProfile = null;
	private ConcreteAction admin_RDE = null;
	private ConcreteAction admin_RDE_DO = null;
	private ConcreteAction admin_Font = null;
	private ConcreteAction admin_Spellcheck = null;
	private ConcreteAction admin_SpellcheckHiglight = null;

    // add the global checkbox menu item
    private JCheckBoxMenuItem spellCheckMenuItem = null; // this needs to be global v
    private JCheckBoxMenuItem spellCheckHighlightMenuItem = null; // this needs to be global v

    /**
	 * The tools menu action.
	 */
	private ConcreteAction assessmentAction = null;


	/**
	 * The help menu action.
	 */
	private ConcreteAction helpAction = null;
	private ConcreteAction homeAction = null;
	private ConcreteAction aboutAction = null;
	private ConcreteAction requestFeatureAction = null;
	private ConcreteAction reportBugAction = null;

	/**
	 * The query actions.
	 */
	private ConcreteAction searchAction = null;
	private ConcreteAction findAllAction = null;
	private ConcreteAction newRecordAction = null;
	private ConcreteAction reportsAction = null;
	private ConcreteAction deleteAction = null;
	private ConcreteAction mergeAction = null;
	private ConcreteAction exportEADAction = null;
	private ConcreteAction exportMARCAction = null;
	private ConcreteAction exportMETSAction = null;
	private ConcreteAction exportMODSAction = null;
	private ConcreteAction exportDublinCoreAction = null;
	private JButton exportEADButton;
	private JButton exportMARCButton;
	private JButton mergeButton;
    private JButton exportMETSButton; // Digital Object specific button
    private JButton exportMODSButton; // Digital Object specific button
    private JButton exportDublinCoreButton; // Digital Object specific button

	/**
	 * The query actions.
	 */
	private FilterPanel namesFilter = new FilterPanel("Filter search results");
	private FilterPanel subjectsFilter = new FilterPanel("Filter search results");
	private FilterPanel accessionsFilter = new FilterPanel("Filter search results");
	private FilterPanel resourcesFilter = new FilterPanel("Filter search results");
	private FilterPanel digitalObjectFilter = new FilterPanel("Filter search results");


	private Hashtable<Class, DomainTableWorkSurface> worksurfaces = new Hashtable<Class, DomainTableWorkSurface>();

    private ApplicationFrame() {
		super();
		atVersionNumber = resourceBundle.getString("archiviststoolkit.releasenumber");
		String releaseType = resourceBundle.getString("archiviststoolkit.releaseType");
		String releaseUpdate = resourceBundle.getString("archiviststoolkit.releaseupdate");

        if (!releaseType.equals("production")) {
			atVersionNumber += " - " + releaseType;
		}

        if (!releaseUpdate.equals("0")) {
            if(releaseUpdate.matches("\\d+")) { // check to see if it matches a number
			    atVersionNumber += " - update " + releaseUpdate;
            } else { // not a number, so just append the releaseUpdate String
                atVersionNumber += " - " + releaseUpdate;
            }
		}

        issueReportingURL = resourceBundle.getString("archiviststoolkit.issueReportingURL");
		timer = new MyTimer();
		startupLog = "";
	}

	public static ApplicationFrame getInstance() {
		if (singleton == null) {
			singleton = new ApplicationFrame();
		}
		return (singleton);
	}

	/**
	 * Application Constructor sets up window and internal components.
	 * Saves the frame size and position
	 *
	 * @param splashScreen - the initial splash screen for starting the application.
	 */

	public void initializeMainFrame(SplashScreen splashScreen) {

		//=====================================================================
		// First action is to get the default window positioning from the
		// preferences store (registry under windows). or use the default
		// values if this is the first time we have been run.
		//=====================================================================

		Preferences preferences = Preferences.userNodeForPackage(ApplicationFrame.class);

		setExtendedState(preferences.getInt("extendedstate", JFrame.NORMAL));
		setLocation(preferences.getInt("x", 0), preferences.getInt("y", 0));
		setSize(preferences.getInt("width", DEFAULT_WINDOW_WIDTH), preferences.getInt("height", DEFAULT_WINDOW_HEIGHT));
		setBackground(BACKGROUND_COLOR);

		//=====================================================================
		// Now add in the window listener looking for that ever elusive
		// exit event
		//=====================================================================

		addWindowListener(new WindowAdapter() {
			public void windowClosing(final WindowEvent windowevent) {
				doQuit();
			}
		});

		//=====================================================================
		// Set up some basic window administration
		//=====================================================================

		String title = resourceBundle.getString("archiviststoolkit.application.title") + " - " + ApplicationFrame.getInstance().getAtVersionNumber();
		if (!resourceBundle.getString("archiviststoolkit.releaseType").equals("production")) {
			title += " - WARNING: This is an pre-release version of the Archivists' Toolkit. Please use this version for testing purposes only.";
		}
		this.setTitle(title);
		this.setIconImage(new ImageIcon(this.getClass().getResource("/org/archiviststoolkit/resources/images/launchIcon16x16.gif")).getImage());

		workSurfaceContainer = new WorkSurfaceContainer();

//set up names
		if (splashScreen != null) {
			splashScreen.setMessageText("Initializing Names");
		} else {
			System.out.println("Application frame created without splashscreen");
		}
		DomainTableWorkSurface worksurface = new DomainTableWorkSurface(org.archiviststoolkit.model.Names.class,
				resourceBundle.getString("archiviststoolkit.table.names"),
				new ImageIcon(ApplicationFrame.class.getResource("/org/archiviststoolkit/resources/images/namesIcon.png")));
		this.worksurfaces.put(Names.class, worksurface);
		workSurfaceContainer.addWorkSurface(worksurface);
		Names.initNamesLookupList();

//set up subjects
		if (splashScreen != null) {
			splashScreen.setMessageText("Initializing Subjects");
		}
		worksurface = new DomainTableWorkSurface(Subjects.class,
				resourceBundle.getString("archiviststoolkit.table.subjects"),
				new ImageIcon(ApplicationFrame.class.getResource("/org/archiviststoolkit/resources/images/subjectsIcon.png")));
		this.worksurfaces.put(Subjects.class, worksurface);
		workSurfaceContainer.addWorkSurface(worksurface);
		Subjects.initSubjectLookupList();

//set up accessions
		if (splashScreen != null) {
			splashScreen.setMessageText("Initializing Accessions");
		}
		worksurface = new DomainTableWorkSurface(Accessions.class,
				resourceBundle.getString("archiviststoolkit.table.accessions"),
				new ImageIcon(ApplicationFrame.class.getResource("/org/archiviststoolkit/resources/images/accessionsIcon.png")));

		this.worksurfaces.put(Accessions.class, worksurface);
		workSurfaceContainer.addWorkSurface(worksurface);

//set up resources
		if (splashScreen != null) {
			splashScreen.setMessageText("Initializing Resources");
		}
		worksurface = new ResourceTableWorkSurface(Resources.class,
				resourceBundle.getString("archiviststoolkit.table.resources"),
				new ImageIcon(ApplicationFrame.class.getResource("/org/archiviststoolkit/resources/images/resourcesIcon.png")));

		this.worksurfaces.put(Resources.class, worksurface);
		workSurfaceContainer.addWorkSurface(worksurface);
		Resources.initResourcesLookupList();

        // set up the digital objects work surface
        if (splashScreen != null) {
			splashScreen.setMessageText("Initializing Digital Objects");
		}
		worksurface = new DigitalObjectTableWorkSurface(DigitalObjects.class,
				resourceBundle.getString("archiviststoolkit.table.digitalObjects"),
				new ImageIcon(ApplicationFrame.class.getResource("/org/archiviststoolkit/resources/images/digitalObjectsIcon.png")));

		this.worksurfaces.put(DigitalObjects.class, worksurface);
		workSurfaceContainer.addWorkSurface(worksurface);


		if (splashScreen != null) {
			splashScreen.setMessageText("Loading editors");
		}
		DomainEditorFactory.getInstance().createDomainEditors();

		searchAction = workSurfaceContainer.getSearchAction();
		findAllAction = workSurfaceContainer.getListAllAction();
		newRecordAction = workSurfaceContainer.getNewRecordAction();
		reportsAction = workSurfaceContainer.getReportsAction();
		deleteAction = workSurfaceContainer.getDeleteAction();
		exportEADAction = workSurfaceContainer.getExportEADAction();
		exportMARCAction = workSurfaceContainer.getExportMARCAction();
        exportMETSAction = workSurfaceContainer.getExportMETSAction();
        exportMODSAction = workSurfaceContainer.getExportMODSAction();
        exportDublinCoreAction = workSurfaceContainer.getExportDublinCoreAction();
		mergeAction = workSurfaceContainer.getMergeAction();

		makeActions();
		this.setJMenuBar(makeMenuBar());

		this.getContentPane().add(this.makeToolBar(), BorderLayout.NORTH);
		this.getContentPane().add(workSurfaceContainer, BorderLayout.CENTER);
		workSurfaceContainer.setCurrentWorkSurface(Names.class);
	}

	/**
	 * This is the shutdown call which is executed when the window listener detects a shutdown event.
	 * It saves the window state to the user package node preferences object.
	 */

	private void doQuit() {
		Preferences preferences = Preferences.userNodeForPackage(ApplicationFrame.class);

		Point framelocation = getLocation();
		Dimension framesize = getSize();
		preferences.putInt("x", (int) framelocation.getX());
		preferences.putInt("y", (int) framelocation.getY());
		preferences.putInt("width", (int) framesize.getWidth());
		preferences.putInt("height", (int) framesize.getHeight());
		preferences.putInt("extendedstate", getExtendedState());

        // clear any record locks that have not been cleared yet
        RecordLockUtils.clearAllMyRecordLocks();

        System.exit(0);
	}

	/**
	 * Make actions.
	 */
	public void makeActions() {

		exitAction = new ConcreteAction(resourceBundle.getString("archiviststoolkit.action.exitapplication.name"));
		exitAction.addActionListener(this);

		helpAction = new ConcreteAction(resourceBundle.getString("archiviststoolkit.action.helpContents.name"));
		helpAction.addActionListener(this);

		aboutAction = new ConcreteAction(resourceBundle.getString("archiviststoolkit.action.about.name"));
		aboutAction.addActionListener(this);

		homeAction = new ConcreteAction("Archivists' Toolkit Website");
		homeAction.addActionListener(this);

		reportBugAction = new ConcreteAction("Report bug");
		reportBugAction.addActionListener(this);

		import_Subject = new ConcreteAction("Import Subjects");
		import_Subject.addActionListener(this);

		import_EacName = new ConcreteAction("Import EAC Names");
		import_EacName.addActionListener(this);

		import_OliviaName = new ConcreteAction("Import OLIVIA Names");
		import_OliviaName.addActionListener(this);

		import_Accessions = new ConcreteAction("Import Accessions (Tab delimited)");
		import_Accessions.addActionListener(this);

		import_AccessionsXML = new ConcreteAction("Import Accessions (XML)");
		import_AccessionsXML.addActionListener(this);

		import_EAD = new ConcreteAction("Import EAD");
		import_EAD.addActionListener(this);

		import_MARC = new ConcreteAction("Import MARC");
		import_MARC.addActionListener(this);

        import_DigitalObjects = new ConcreteAction("Import Digital Object (Tab delimited)");
		import_DigitalObjects.addActionListener(this);

//        repair_UpdateResourceHasNotesAndHasChildrenFlags = new ConcreteAction("Update Resource notes and children flags");
//        repair_UpdateResourceHasNotesAndHasChildrenFlags.addActionListener(this);
//
//        repair_UpdateDisplayFields = new ConcreteAction("Update display fields");
//        repair_UpdateDisplayFields.addActionListener(this);
//
//		repair_ThrowError = new ConcreteAction("Throw error");
//		repair_ThrowError.addActionListener(this);
//
//		repair_reconcileFieldInfo = new ConcreteAction("Reconcile field info");
//		repair_reconcileFieldInfo.addActionListener(this);
//
//		repair_parseLocationInfo = new ConcreteAction("Parse location info");
//		repair_parseLocationInfo.addActionListener(this);

        admin_CompileJasperReport = new ConcreteAction("Compile Jasper Report");
        admin_CompileJasperReport.addActionListener(this);

        admin_ReloadReports = new ConcreteAction("Reload Reports");
        admin_ReloadReports.addActionListener(this);

		admin_DateFormatSetting = new ConcreteAction("Date Format Setting");
		admin_DateFormatSetting.addActionListener(this);

		admin_EditUsers = new ConcreteAction("Users");
		admin_EditUsers.addActionListener(this);

		admin_EditRepositories = new ConcreteAction("Repositories");
		admin_EditRepositories.addActionListener(this);

		admin_ConfigureApplication = new ConcreteAction("Configure Application");
		admin_ConfigureApplication.addActionListener(this);

		admin_EditLookupLists = new ConcreteAction("Lookup Lists");
		admin_EditLookupLists.addActionListener(this);

		admin_EditLocations = new ConcreteAction("Locations");
		admin_EditLocations.addActionListener(this);

		admin_EditNotesEtc = new ConcreteAction("Notes Etc.");
		admin_EditNotesEtc.addActionListener(this);

		admin_RepositoryProfile = new ConcreteAction("Generate Repository Profile");
		admin_RepositoryProfile.addActionListener(this);

		admin_RDE = new ConcreteAction("Configure Rapid Data Entry Screens");
		admin_RDE.addActionListener(this);

        admin_RDE_DO = new ConcreteAction("Configure Digital Objects Rapid Data Entry Screens");
		admin_RDE_DO.addActionListener(this);

        admin_Font = new ConcreteAction("Configure Font");
		admin_Font.addActionListener(this);

        admin_Spellcheck = new ConcreteAction("Enable");
        admin_SpellcheckHiglight = new ConcreteAction("Highlight");

        assessmentAction = new ConcreteAction("Assessment Records");
		assessmentAction.addActionListener(this);
	}

	/**
	 * Make the toolbar.
	 *
	 * @return the toolbar we have constructed
	 */

	private JToolBar makeToolBar() {
		JToolBar toolBar = new JToolBar();

		toolBar.setFloatable(false);

		JButton button = toolBar.add(searchAction);
		button.setText("Search");
		button.setHorizontalTextPosition(SwingConstants.RIGHT);
		button.setVerticalTextPosition(SwingConstants.CENTER);

		button = toolBar.add(findAllAction);
		button.setText("List All");
		button.setHorizontalTextPosition(SwingConstants.RIGHT);
		button.setVerticalTextPosition(SwingConstants.CENTER);

		button = toolBar.add(newRecordAction);
		button.setText("New Record");
		button.setHorizontalTextPosition(SwingConstants.RIGHT);
		button.setVerticalTextPosition(SwingConstants.CENTER);

		button = toolBar.add(reportsAction);
		button.setText("Reports");
		button.setHorizontalTextPosition(SwingConstants.RIGHT);
		button.setVerticalTextPosition(SwingConstants.CENTER);

        // 1/11/2013 User Class 1 should not have delete, or merge privileges
		if(Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_ADVANCED_DATA_ENTRY)) {
            button = toolBar.add(deleteAction);
		    button.setText("Delete");
		    button.setHorizontalTextPosition(SwingConstants.RIGHT);
		    button.setVerticalTextPosition(SwingConstants.CENTER);
        }

		mergeButton = toolBar.add(mergeAction);
		mergeButton.setText("Merge");
		mergeButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		mergeButton.setVerticalTextPosition(SwingConstants.CENTER);

		exportEADButton = toolBar.add(exportEADAction);
		exportEADButton.setText("Export EAD");
		exportEADButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		exportEADButton.setVerticalTextPosition(SwingConstants.CENTER);

		exportMARCButton = toolBar.add(exportMARCAction);
		exportMARCButton.setText("Export MARC");
		exportMARCButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		exportMARCButton.setVerticalTextPosition(SwingConstants.CENTER);

        exportMETSButton = toolBar.add(exportMETSAction);
		exportMETSButton.setText("Export METS");
		exportMETSButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		exportMETSButton.setVerticalTextPosition(SwingConstants.CENTER);

        exportMODSButton = toolBar.add(exportMODSAction);
		exportMODSButton.setText("Export MODS");
		exportMODSButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		exportMODSButton.setVerticalTextPosition(SwingConstants.CENTER);

        exportDublinCoreButton = toolBar.add(exportDublinCoreAction);
		exportDublinCoreButton.setText("Export Dublin Core");
		exportDublinCoreButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		exportDublinCoreButton.setVerticalTextPosition(SwingConstants.CENTER);

		toolBar.add(subjectsFilter);
		toolBar.add(accessionsFilter);
		toolBar.add(resourcesFilter);
		toolBar.add(namesFilter);
        toolBar.add(digitalObjectFilter);
		setButtonVisiblity(Names.class);


		toolBar.setBackground(BACKGROUND_COLOR);

		return (toolBar);
	}

	/**
	 * Creates the application menu bar.
	 *
	 * @return the made menubar
	 */

	private JMenuBar makeMenuBar() {
		boolean onMac = false;

//        JMenu menu = null;

		if (System.getProperty("mrj.version") != null) {
			onMac = true;
			initMacSpecificStuff(new JMenu());
		}
		JMenuBar menuBar = new JMenuBar();

		if (!onMac) {
			JMenu fileMenu = new JMenu("File");
			fileMenu.setBackground(MENU_BAR_BACKGROUND_COLOR);
			fileMenu.setForeground(Color.white);
			fileMenu.add(this.exitAction);
			menuBar.add(fileMenu);
		}

        getImportPluginNames();

		JMenu importMenu = new JMenu("Import");
		importMenu.setBackground(MENU_BAR_BACKGROUND_COLOR);
		importMenu.setForeground(Color.white);
		importMenu.add(this.import_Accessions);
		importMenu.add(this.import_AccessionsXML);
		importMenu.add(this.import_EAD);
		importMenu.add(this.import_MARC);
		importMenu.add(this.import_DigitalObjects);

        if(importPluginNames != null) { // if any import plugins were found add them
           addPluginMenuItems(importMenu, importPluginNames);
        }
        menuBar.add(importMenu);

		JMenu setupMenu = new JMenu("Setup");
		setupMenu.setBackground(MENU_BAR_BACKGROUND_COLOR);
		setupMenu.setForeground(Color.white);
		if (Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_PROJECT_MANAGER)) {
			setupMenu.add(admin_EditUsers);
			setupMenu.add(admin_EditRepositories);
            setupMenu.add(admin_RDE);
            //setupMenu.add(admin_RDE_DO);
        }
		setupMenu.add(admin_EditLookupLists);
		//setupMenu.add(admin_EditLocations);
		if (Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_SUPERUSER)) {
			setupMenu.add(admin_EditNotesEtc);
			setupMenu.add(admin_DateFormatSetting);
		}
		if (Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_SUPERUSER)) {
			setupMenu.add(admin_ConfigureApplication);
		}

        // add the menu to configure system font
        setupMenu.add(admin_Font);

        // add the submenu to enable or disable the spell check function
        JMenu spellCheckMenu = new JMenu("Spell Check");

        spellCheckMenuItem = new JCheckBoxMenuItem(admin_Spellcheck);
        spellCheckMenuItem.setSelected(enableSpellCheck);
        admin_Spellcheck.addActionListener(this);
        spellCheckMenu.add(spellCheckMenuItem);

        spellCheckHighlightMenuItem = new JCheckBoxMenuItem(admin_SpellcheckHiglight);
        spellCheckHighlightMenuItem.setSelected(enableSpellCheckHighlight);
        spellCheckHighlightMenuItem.setEnabled(enableSpellCheck);
        admin_SpellcheckHiglight.addActionListener(this);
        spellCheckMenu.add(spellCheckHighlightMenuItem);
        
        setupMenu.add(spellCheckMenu);

        menuBar.add(setupMenu);

		JMenu reportMenu = new JMenu("Reports");
		reportMenu.setBackground(MENU_BAR_BACKGROUND_COLOR);
		reportMenu.setForeground(Color.white);
        if (Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_SUPERUSER)) {
        reportMenu.add(admin_CompileJasperReport);
        reportMenu.add(admin_ReloadReports);
        }

        if (Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_REPOSITORY_MANAGER)) {
			reportMenu.add(admin_RepositoryProfile);
		}
		menuBar.add(reportMenu);

        // add the tools menu for class 2 user and above
        if (Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_ADVANCED_DATA_ENTRY)) {
            getToolPluginNames();

            JMenu toolMenu = new JMenu("Tools");
            toolMenu.setBackground(MENU_BAR_BACKGROUND_COLOR);
            toolMenu.setForeground(Color.white);

            toolMenu.add(this.assessmentAction);
            toolMenu.add(admin_EditLocations);

            if(toolPluginNames != null) { // if any tool plugins were found add them
                addPluginMenuItems(toolMenu, toolPluginNames);
            }

            menuBar.add(toolMenu);
        }

        // see whether to add the plugin menu
        getPluginNames();
        if(pluginNames != null) {
           menuBar.add(getPluginMenu());
        }

		JMenu helpMenu = new JMenu("Help");
		helpMenu.setBackground(MENU_BAR_BACKGROUND_COLOR);
		helpMenu.setForeground(Color.white);
		helpMenu.add(homeAction);
		helpMenu.add(reportBugAction);
		if (!onMac) {
			helpMenu.addSeparator();
			helpMenu.add(aboutAction);
		}
		menuBar.add(helpMenu);
		menuBar.setBackground(MENU_BAR_BACKGROUND_COLOR);
		menuBar.setForeground(Color.white);
		return (menuBar);
	}

	/**
	 * Takes events from menu items and carries out the appropriate actions.
	 *
	 * @param actionEvent the action event which has just fired
	 */


	public void actionPerformed(final ActionEvent actionEvent) {
		if (actionEvent.getSource() == this.exitAction) {
			this.doQuit();

		} else if (actionEvent.getSource() == this.aboutAction) {
			doAbout();

		} else if (actionEvent.getSource() == this.homeAction) {
			launchBrowser("http://archiviststoolkit.org/");

		} else if (actionEvent.getSource() == this.requestFeatureAction) {
			launchBrowser("http://sourceforge.net/tracker/?group_id=92345&atid=600425");

		} else if (actionEvent.getSource() == this.reportBugAction) {
			JiraReportDialog dialog = new JiraReportDialog(this);
			if (dialog.showDialog() == JOptionPane.OK_OPTION) {
				dialog.setVisible(false);
				//String message = "Your bug has been submitted and assiged a number of " + dialog.getRemoteIssue().getKey() +
				//"\n\nYou can view all issues at:\nhttps://jira.nyu.edu:8443/jira/browse/ART";

				String message = "Your bug has been submitted and assiged a number of " + dialog.getIssueId() +
						"\n\nYou can view all issues at:\nhttps://jira.nyu.edu:8443/jira/browse/ART";

				JOptionPane.showMessageDialog(this, message);
			}


		} else if (actionEvent.getSource() == this.helpAction) {
			launchBrowser("http://www.archiviststoolkit.org/guidebook/");

		} else if (actionEvent.getSource() == this.admin_CompileJasperReport) {
            ATFileChooser atFileChooser = new ATFileChooser(new SimpleFileFilter("xml"));
            atFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            atFileChooser.setMultiSelectionEnabled(true);
            if (atFileChooser.showOpenDialog(this, "Compile") == JFileChooser.APPROVE_OPTION) {
                File[] reportFiles = atFileChooser.getSelectedFiles();
                for (File reportFile: reportFiles) {
                    try {
                            JasperCompileManager.compileReportToFile(reportFile.getPath());
                            JOptionPane.showMessageDialog(this, reportFile.getName() + " compiled successfully");
                    } catch (JRException e) {
                        new ErrorDialog(this, "There is a problem compiling report: " + reportFile.getName(),
                                e).showDialog();
                    }
                }
            }
        } else if (actionEvent.getSource() == this.admin_ReloadReports) {
            ReportFactory.getInstance().ParseReportDirectory(true);

        } else if (actionEvent.getSource() == this.admin_DateFormatSetting) {
			try {
				Constants.modifyConstantsRecord();
			} catch (PersistenceException e) {
				new ErrorDialog(this, "Error modifying application preferences", e).showDialog();
			} catch (LookupException e) {
				new ErrorDialog(this, "Error modifying application preferences", e).showDialog();
			} catch (WrongNumberOfConstantsRecordsException e) {
				new ErrorDialog(this, "Error modifying application preferences", e).showDialog();
			}

		} else if (actionEvent.getSource() == this.admin_EditUsers) {
			try {
				GeneralAdminDialog dialog = new GeneralAdminDialog(this);
				dialog.setDialogTitle("Edit Users");
				dialog.setClazz(Users.class, Users.PROPERTYNAME_USERNAME);
        if(!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_REPOSITORY_MANAGER)) {
          dialog.hideAddRemoveButtons();
        }
        dialog.showDialog();
			} catch (DomainEditorCreationException e) {
				new ErrorDialog(this, "Error creating editor for Users", e).showDialog();
			}

		} else if (actionEvent.getSource() == this.admin_EditRepositories) {
			try {
				GeneralAdminDialog dialog = new GeneralAdminDialog(this);
				dialog.setDialogTitle("Edit Repositories");
				dialog.setClazz(Repositories.class, Repositories.PROPERTYNAME_REPOSITORY_NAME);
        if(!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_SUPERUSER)) {
          dialog.hideAddRemoveButtons();
        }
        dialog.showDialog();

				Thread performer = new Thread(new Runnable() {
					public void run() {
						InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
						monitor.start("Loading repositories...");
						try {
							Repositories.loadRepositories();
							monitor.setTextLine("Loading default values...", 1);
							DefaultValues.initDefaultValueLookup();
							System.out.println("one");
							monitor.setTextLine("Loading editors...", 1);
							DomainEditorFactory.getInstance().updateDomainEditors();
						} finally {
							monitor.close();
						}
					}
				}, "Thread Name");
				performer.start();
			} catch (DomainEditorCreationException e) {
				new ErrorDialog(this, "Error creating editor for Repositories", e).showDialog();
			}

		} else if (actionEvent.getSource() == this.admin_ConfigureApplication) {
			try {
				GeneralAdminDialog dialog = new GeneralAdminDialog(this);
				dialog.setDialogTitle("Configure Application");
				if (!getCurrentUserName().equalsIgnoreCase(Users.USERNAME_DEVELOPER)) {
					dialog.hideAddRemoveButtons();
				}
				dialog.setClazz(DatabaseTables.class, DatabaseTables.PROPERTYNAME_TABLE_NAME);
				dialog.showDialog();
				Thread performer = new Thread(new Runnable() {
					public void run() {
						InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
						monitor.start("Initailizing field info...");
						try {
							ATFieldInfo.loadFieldList();
							monitor.setTextLine("Loading editors...", 1);
							DomainEditorFactory.getInstance().updateDomainEditors();

                            // Update the columns in the EDT thread to fix ART-1284
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    workSurfaceContainer.updateColumns();
                                }
                            });

                            //workSurfaceContainer.updateColumns();
							ReportFactory.getInstance().updatePrintScreens();
						} catch (Exception e){
							monitor.close();
							new ErrorDialog(ApplicationFrame.getInstance(), "Error creating editor for DatabaseTables", e).showDialog();

						} finally {
							monitor.close();
						}
					}
				}, "Performer");
				performer.start();
			} catch (DomainEditorCreationException e) {
				new ErrorDialog(this, "Error creating editor for DatabaseTables", e).showDialog();
			}

		} else if (actionEvent.getSource() == this.admin_EditLookupLists) {
			try {
				GeneralAdminDialog dialog = new GeneralAdminDialog(this);
				dialog.setDialogTitle("Edit Lookup Lists");
				dialog.setClazz(LookupList.class, LookupList.PROPERTYNAME_LIST_NAME);
				dialog.hideAddRemoveButtons();
				dialog.showDialog();
				Thread performer = new Thread(new Runnable() {
					public void run() {
						InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
						monitor.start("Loading lookup lists...");
						try {
							LookupListUtils.loadLookupLists();
							monitor.setTextLine("Loading editors...", 1);
							DomainEditorFactory.getInstance().updateDomainEditors();
						} finally {
							monitor.close();
						}
					}
				}, "EditLookupLists");
				performer.start();
			} catch (DomainEditorCreationException e) {
				new ErrorDialog(this, "Error creating editor for LookupLists", e).showDialog();
			}

		} else if (actionEvent.getSource() == this.admin_RDE) {
			try {
				GeneralAdminDialog dialog = new GeneralAdminDialog(this);
				dialog.setDialogTitle("Edit Rapid Data Entry Screens");
				dialog.setRDEClazz(ResourcesComponents.class);
				dialog.showDialog();
				Thread performer = new Thread(new Runnable() {
					public void run() {
						InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
						monitor.start("Loading rapid data entry screens...");
						try {
							RDEFactory.getInstance().loadRDEs();
							monitor.setTextLine("Loading editors...", 1);
							DomainEditorFactory.getInstance().updateDomainEditors();
						} finally {
							monitor.close();
						}
					}
				}, "LoadRDEs");
				performer.start();
			} catch (DomainEditorCreationException e) {
				new ErrorDialog(this, "Error creating editor for LookupLists", e).showDialog();
			}

		} else if (actionEvent.getSource() == this.admin_RDE_DO) {
			try {
				GeneralAdminDialog dialog = new GeneralAdminDialog(this);
				dialog.setDialogTitle("Edit Digital Objects Rapid Data Entry Screens");
				dialog.setRDEClazz(DigitalObjects.class);
				dialog.showDialog();
				Thread performer = new Thread(new Runnable() {
					public void run() {
						InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
						monitor.start("Loading rapid data entry screens...");
						try {
							RDEFactory.getInstance().loadRDEs();
							monitor.setTextLine("Loading editors...", 1);
							DomainEditorFactory.getInstance().updateDomainEditors();
						} finally {
							monitor.close();
						}
					}
				}, "LoadRDEs");
				performer.start();
			} catch (DomainEditorCreationException e) {
				new ErrorDialog(this, "Error creating editor for LookupLists", e).showDialog();
			}

		} else if (actionEvent.getSource() == this.admin_RepositoryProfile) {
			Thread performer = new Thread(new Runnable() {
				public void run() {
					InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000, true);
					monitor.start("Generating Repository Profile...");
					try {
						RepositoryProfileProcessor processor = new RepositoryProfileProcessor();
						processor.gatherStats(monitor);
						processor.displayStats();
					} catch (JRException e) {
						monitor.close();
						new ErrorDialog(ApplicationFrame.getInstance(), "Error printing repository profile", e).showDialog();
					} catch (UnsupportedParentComponentException e) {
						monitor.close();
						new ErrorDialog(ApplicationFrame.getInstance(), "Error printing repository profile", e).showDialog();
					} finally {
						monitor.close();
					}
				}
			}, "RepositoryProfile");
			performer.start();

		} else if (actionEvent.getSource() == this.admin_EditLocations) {
			try {
				LocationManagement dialog = new LocationManagement(this);
				dialog.setDialogTitle("Location Management");
				dialog.showDialog();
			} catch (DomainEditorCreationException e) {
				new ErrorDialog(this, "Error creating editor for Locations", e).showDialog();
			}
		} else if (actionEvent.getSource() == this.admin_EditNotesEtc) {
			try {
				GeneralAdminDialog dialog = new GeneralAdminDialog(this);
				dialog.setDialogTitle("Notes Etc.");
				dialog.hideAddRemoveButtons();
				dialog.setClazz(NotesEtcTypes.class);
				dialog.showDialog();
				Thread performer = new Thread(new Runnable() {
					public void run() {
						InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
						monitor.start("Loading editors...");
						try {
							NoteEtcTypesUtils.loadNotesEtcTypes();
							LookupListUtils.loadLookupLists();
							DomainEditorFactory.getInstance().updateDomainEditors();
						} finally {
							monitor.close();
						}
					}
				}, "Performer");
				performer.start();
			} catch (DomainEditorCreationException e) {
				new ErrorDialog(this, "Error creating editor for NotesEtcTypes", e).showDialog();
			}

		} else if (actionEvent.getSource() == this.admin_Font) {
            // get the current font if any
            UserPreferences userPref = UserPreferences.getInstance();
            Font font = userPref.getFont();

            JFontChooser fontChooser = new JFontChooser();

            if(font != null) {
                fontChooser.setSelectedFont(font);
            }

            // now if a font is selected set it
            int result = fontChooser.showDialog(this);
            if (result == JFontChooser.OK_OPTION) {
                font = fontChooser.getSelectedFont();
                userPref.setFont(font);
                userPref.saveToPreferences();

                // update the UI now
                UIManager.put("TextField.font", new FontUIResource(font));
                UIManager.put("TextArea.font", new FontUIResource(font));
                UIManager.put("Tree.font", new FontUIResource(font));
                UIManager.put("Table.font", new FontUIResource(font));

                SwingUtilities.updateComponentTreeUI(this);
            }

        } else if (actionEvent.getSource() == this.admin_Spellcheck || actionEvent.getSource() == this.admin_SpellcheckHiglight) {
            // get the user preferences and save the state check spell enable
            UserPreferences userPref = UserPreferences.getInstance();
            userPref.setEnableSpellCheck(spellCheckMenuItem.isSelected());
            userPref.setEnableSpellCheckHighlight(spellCheckHighlightMenuItem.isSelected());
            userPref.saveToPreferences();

            // see whether to disable the highlighting check box
            if(!spellCheckMenuItem.isSelected()) {
                spellCheckHighlightMenuItem.setEnabled(false);
            } else {
                spellCheckHighlightMenuItem.setEnabled(true);
            }

        } else if (actionEvent.getSource() == this.import_Subject) {
			ATFileChooser filechooser = new ATFileChooser();
			filechooser.setSize(600, 400);

			if (filechooser.showDialog(this, "Import") == JFileChooser.APPROVE_OPTION) {
				DomainImportController controller = new DomainImportController();
				TabSubjectImportHandler handler = new TabSubjectImportHandler();
				handler.startImportThread(filechooser.getSelectedFile(), controller);
			}

		} else if (actionEvent.getSource() == this.import_Accessions) {
			filechooser = new ATFileChooser(new ImportOptionsAccessions(), new SimpleFileFilter(".txt"));

			if (filechooser.showOpenDialog(this, "Import") == JFileChooser.APPROVE_OPTION) {
				controller = new DomainImportController();
				handler = new DelimitedAccessionImportHandler((ImportOptionsAccessions) filechooser.getAccessory());
				Thread performer = new Thread(new Runnable() {
					public void run() {
						InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000, true);
						monitor.start("Importing accessions...");
						try {
							boolean success = handler.importFile(filechooser.getSelectedFile(), controller, monitor);
							if(success) {
                                LookupListUtils.loadLookupLists();
							    DomainEditorFactory.getInstance().updateDomainEditors();
                            }
						} catch (ImportException e) {
							monitor.close();
							new ErrorDialog(ApplicationFrame.getInstance(), "Import Problem", e).showDialog();
						} finally {
							monitor.close();
						}
					}
				}, "ImportAccessions");
				performer.start();
			}

		} else if (actionEvent.getSource() == this.import_AccessionsXML) {
			filechooser = new ATFileChooser(new ImportOptionsAccessions(ImportOptionsAccessions.SUPPRESS_DATE_FORMAT), new SimpleFileFilter(".xml"));

			if (filechooser.showOpenDialog(this, "Import") == JFileChooser.APPROVE_OPTION) {
				controller = new DomainImportController();
				handler = new AccessionImportXmlHandler((ImportOptionsAccessions) filechooser.getAccessory());
				Thread performer = new Thread(new Runnable() {
					public void run() {
						InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000, true);
						monitor.start("Importing accessions...");
						try {
							handler.importFile(filechooser.getSelectedFile(), controller, monitor);
						} catch (ImportException e) {
							monitor.close();
							new ErrorDialog(ApplicationFrame.getInstance(), "Import Problem", e).showDialog();
						} finally {
							monitor.close();
						}

						monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
						monitor.start("Loading lookup lists...");
						try {
							LookupListUtils.loadLookupLists();
							monitor.setTextLine("Loading editors", 1);
							DomainEditorFactory.getInstance().updateDomainEditors();
						} finally {
							monitor.close();
						}
					}
				}, "ImportAccessionsXML");
				performer.start();
			}

		} else if (actionEvent.getSource() == this.import_EAD) {
			filechooser = new ATFileChooser(new ImportOptionsEAD(), new SimpleFileFilter(".xml"));
			filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

			if (filechooser.showOpenDialog(this, "Import") == JFileChooser.APPROVE_OPTION) {
				controller = new DomainImportController();
				handler = new EADImportHandler((ImportOptionsEAD) filechooser.getAccessory());
				Thread performer = new Thread(new Runnable() {
					public void run() {
						InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000, true);
						monitor.start("Importing EAD...");
						try {
							handler.importFile(filechooser.getSelectedFile(), controller, monitor);
						} catch (ImportException e) {
							monitor.close();
							new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
						} finally {
							monitor.close();
						}
						monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
						monitor.start("Loading lookup lists...");
						try {
							LookupListUtils.loadLookupLists();
							monitor.setTextLine("Loading editors", 1);
							DomainEditorFactory.getInstance().updateDomainEditors();
						} finally {
							monitor.close();
						}
					}
				}, "ImportEAD");
				performer.start();
			}

		} else if (actionEvent.getSource() == this.import_MARC) {
			filechooser = new ATFileChooser(new ImportOptionsMARC(), new SimpleFileFilter(".xml"));

			if (filechooser.showOpenDialog(this, "Import") == JFileChooser.APPROVE_OPTION) {
				controller = new DomainImportController();
				handler = new MARCImportHandler((ImportOptionsMARC) filechooser.getAccessory());
				Thread performer = new Thread(new Runnable() {
					public void run() {
						InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000, true);
						monitor.start("Importing MARCXML...");
						try {
							handler.importFile(filechooser.getSelectedFile(), controller, monitor);
						} catch (Exception e) {
							monitor.close();
							new ErrorDialog(ApplicationFrame.getInstance(), "", e).showDialog();
						} finally {
							monitor.close();
						}
						monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000);
						monitor.start("Loading lookup lists...");
						try {
							LookupListUtils.loadLookupLists();
							monitor.setTextLine("Loading editors", 1);
							DomainEditorFactory.getInstance().updateDomainEditors();
						} finally {
							monitor.close();
						}
					}
				}, "ImportMARC");
				performer.start();
			}

		} else if (actionEvent.getSource() == this.import_DigitalObjects) {
			filechooser = new ATFileChooser(new ImportOptionsDigitalObjects(), new SimpleFileFilter(".txt"));

			if (filechooser.showOpenDialog(this, "Import") == JFileChooser.APPROVE_OPTION) {
				controller = new DomainImportController();
				handler = new DelimitedDigitalObjectImportHandler((ImportOptionsDigitalObjects) filechooser.getAccessory());
				Thread performer = new Thread(new Runnable() {
					public void run() {
						InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 1000, true);
						monitor.start("Importing digital objects...");
						try {
							boolean success = handler.importFile(filechooser.getSelectedFile(), controller, monitor);
							if(success) {
                                LookupListUtils.loadLookupLists();
							    DomainEditorFactory.getInstance().updateDomainEditors();
                            }
						} catch (ImportException e) {
							monitor.close();
							new ErrorDialog(ApplicationFrame.getInstance(), "Import Problem", e).showDialog();
						} finally {
							monitor.close();
						}
					}
				}, "ImportDigitalObjects");
				performer.start();
			}

		} else if (actionEvent.getSource() == this.import_EacName) {
			JFileChooser filechooser = new JFileChooser();

			if (filechooser.showDialog(this, "Import") == JFileChooser.APPROVE_OPTION) {
				DomainImportController controller = new DomainImportController();
				ImportFactory.getInstance().importFile(filechooser.getSelectedFile(), controller);
			}
		} else if (actionEvent.getSource() == this.assessmentAction) {
            try {
				AssessmentManagement dialog = new AssessmentManagement(this);
				dialog.setDialogTitle("Assessment Records");
				dialog.showDialog();
			} catch (DomainEditorCreationException e) {
				new ErrorDialog(this, "Error creating editor for Assessments", e).showDialog();
			}
        } else { // must be a plugin action so get the plugin and display it. In the future it maynot have to be display, but we will handel that when we get to it
            ConcreteAction pluginAction = (ConcreteAction)actionEvent.getSource();
            String id = (String)pluginAction.getValue("id"); // get the plugin id
            if(id.indexOf("::") == -1) { // default plugin so just call the showPlugin methods
                ATPlugin plugin = ATPluginFactory.getInstance().getPlugin(id);
                plugin.setApplicationFrame(this);
                plugin.showPlugin();
            } else { // the id and task are tied so split them apart and call the doTask method in the plugin
                String[] sa = id.split("::");
                ATPlugin plugin = ATPluginFactory.getInstance().getPlugin(sa[0]);
                plugin.setApplicationFrame(this);
                plugin.doTask(sa[1]);
            }
        }
	}

	private void doAbout() {
		new ApplicationAboutDialog().showDialog();
	}

	/**
	 * takes in new activation information.
	 * does nothing with it for the moment.
	 *
	 * @param params the string arguments passed on startup
	 */

	public void newActivation(final String[] params) {
		// do nothing for the moment
	}

	/**
	 * Launch the browser when instructed.
	 *
	 * @param address the url to send the browser to.
	 */

	private void launchBrowser(final String address) {

		BareBonesBrowserLaunch.openURL(address);
	}

	public String getAtVersionNumber() {
		return atVersionNumber;
	}

	public void setAtVersionNumber(String atVersionNumber) {
		this.atVersionNumber = atVersionNumber;
	}

	public String getCurrentUserName() {
		return currentUser.getUserName();
	}

	public Repositories getCurrentUserRepository() {
		return currentUser.getRepository();
	}

	public DomainTableWorkSurface getWorkSurface(Class clazz) {
		return worksurfaces.get(clazz);
	}

	public boolean hasWorkSurface(Class clazz) {
		if (getWorkSurface(clazz) == null) {
			return false;
		} else {
			return true;
		}
	}

	public Integer getCurrentUserAccessClass() {
		return currentUser.getAccessClass();
	}

	public Long getCurrentUserRepositoryId() {
		return currentUser.getRepository().getIdentifier();
	}

	public Users getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Users currentUser) {
		this.currentUser = currentUser;
	}

	public String getIssueReportingURL() {
		return issueReportingURL;
	}

	public void setIssueReportingURL() {
		this.issueReportingURL = issueReportingURL;
	}

	public static String gatherSystemInformation() {
		String returnString = "";
		returnString = "AT Version: " + ApplicationFrame.getInstance().getAtVersionNumber();
		returnString += "\nJava Version: " + System.getProperty("java.version");
		returnString += "\nOperating System: " + System.getProperty("os.name");
		returnString += "\nOS Version: " + System.getProperty("os.version");
		returnString += "\nPlatform: " + System.getProperty("os.arch");
		returnString += "\nDatabase: " + UserPreferences.getInstance().getDatabaseUrl();
        returnString += "\nDatabase Version: " + DatabaseConnectionUtils.getDatabaseVersionString();

		return returnString;
	}

	public void addLineToStartupLog(String line) {
		if (startupLog.length() == 0) {
			startupLog = line + MyTimer.toString(timer.elapsedTimeMillisSplit());
		} else {
			startupLog += "\n" + line + MyTimer.toString(timer.elapsedTimeMillisSplit());
		}
	}

	public void finishStartupLog(SplashScreen splashScreen) {
		if (splashScreen.getMessage().getText().length() > 0) {
			addLineToStartupLog(splashScreen.getMessage().getText());
		}
		startupLog += "\nTotal startup time..." + MyTimer.toString(timer.elapsedTimeMillis());
	}

	/**
	 * Set the filter field and button visibility based on the selected class
	 * @param clazz - the selected class
	 */
	public void setButtonVisiblity(Class clazz) {
		resourcesFilter.setVisible(false);
		namesFilter.setVisible(false);
		subjectsFilter.setVisible(false);
		accessionsFilter.setVisible(false);
        digitalObjectFilter.setVisible(false);

		if (clazz == Resources.class) {
			exportEADButton.setVisible(true);
			exportMARCButton.setVisible(true);
            exportMETSButton.setVisible(false);
            exportMODSButton.setVisible(false);
            exportDublinCoreButton.setVisible(false);

			if (Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_REPOSITORY_MANAGER)) {
				mergeButton.setVisible(true);
			} else {
				mergeButton.setVisible(false);
			}

            resourcesFilter.setVisible(true);

		} else if (clazz == Subjects.class) {
			exportEADButton.setVisible(false);
			exportMARCButton.setVisible(false);
            exportMETSButton.setVisible(false);
            exportMODSButton.setVisible(false);
            exportDublinCoreButton.setVisible(false);

			if(Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_ADVANCED_DATA_ENTRY)) {
                mergeButton.setVisible(true);
            } else {
                mergeButton.setVisible(false);
            }

            subjectsFilter.setVisible(true);

		} else if (clazz == Names.class) {
			exportEADButton.setVisible(false);
			exportMARCButton.setVisible(false);
            exportMETSButton.setVisible(false);
            exportMODSButton.setVisible(false);
            exportDublinCoreButton.setVisible(false);

            if(Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_ADVANCED_DATA_ENTRY)) {
                mergeButton.setVisible(true);
            } else {
                mergeButton.setVisible(false);
            }

			namesFilter.setVisible(true);

		} else if (clazz == Accessions.class){
			exportEADButton.setVisible(false);
			exportMARCButton.setVisible(false);
            exportMETSButton.setVisible(false);
            exportMODSButton.setVisible(false);
            exportDublinCoreButton.setVisible(false);
			mergeButton.setVisible(false);
			accessionsFilter.setVisible(true);
            
		} else if(clazz == DigitalObjects.class) {
            exportEADButton.setVisible(false);
			exportMARCButton.setVisible(true);
            exportMETSButton.setVisible(true);
            exportMODSButton.setVisible(true);
            exportDublinCoreButton.setVisible(true);
			mergeButton.setVisible(false);
			digitalObjectFilter.setVisible(true);
        }
	}

	public JTextField getFilterField(Class clazz) {
		if (clazz == Resources.class) {
			return resourcesFilter.getFilterField();

		} else if (clazz == Subjects.class) {
			return subjectsFilter.getFilterField();

		} else if (clazz == Names.class) {
			return namesFilter.getFilterField();

		} else if (clazz == Accessions.class){
			return accessionsFilter.getFilterField();
            
		} else if (clazz == DigitalObjects.class) {
            return digitalObjectFilter.getFilterField();    
        }
        else {
			return null;
		}
	}

	private void initMacSpecificStuff(JMenu menu) {
		ApplicationItem about = ApplicationItemFactory.createAboutItem();
		about.register("About...", menu);
		ApplicationItem quit = ApplicationItemFactory.createQuitItem();
		quit.register("Exit...", menu);
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAbout();
			}
		});
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doQuit();
			}
		});

	}

	public MyTimer getTimer() {
		return timer;
	}

	public void setTimer(MyTimer timer) {
		this.timer = timer;
	}

	public String getStartupLog() {
		return startupLog;
	}

	public void setStartupLog(String startupLog) {
		this.startupLog = startupLog;
	}

	public WorkSurfaceContainer getWorkSurfaceContainer() {
		return workSurfaceContainer;
	}

	public void setWorkSurfaceContainer(WorkSurfaceContainer workSurfaceContainer) {
		this.workSurfaceContainer = workSurfaceContainer;
	}

	public Boolean getRecordDirty() {
		return recordDirty;
	}

	public void setRecordDirty() {
		if (debug) {
			System.out.println("record dirty");
		}
		this.recordDirty = true;
	}

	public void setRecordClean() {
		if (debug) {
			System.out.println("record clean");
		}
		this.recordDirty = false;
	}

    /**
     * Method to return the menu containing the list of all plugin items
     * @return Plugin Jmenu
     */
    private JMenu getPluginMenu() {
        JMenu pluginMenu = new JMenu("Plugins");
		pluginMenu.setBackground(MENU_BAR_BACKGROUND_COLOR);
		pluginMenu.setForeground(Color.white);

        addPluginMenuItems(pluginMenu, pluginNames);

        return pluginMenu;
    }

    /**
     * Method to add plugin items to a particular
     * @param menu The JMenu to add the plugin items to
     * @param pnames HashMap containing the names of the plugins
     */
    private void addPluginMenuItems(JMenu menu, HashMap pnames) {
        // interate through the plugin names and add them to the menu
        Iterator iter = pnames.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry pairs = (Map.Entry)iter.next();
            String id = (String)pairs.getKey();
            String name = (String)pairs.getValue();

            if(name.indexOf("::") == -1) { // just a regular name so add plain entry
                // create a concret action now
                ConcreteAction pluginAction = new ConcreteAction(name);
                pluginAction.putValue("id", id);
                pluginAction.addActionListener(this);

                // add it to the menu now
                menu.add(pluginAction);
            } else { // plugin can do more than one task so add them individually
                String[] sa = name.split("::");
                JMenu subMenu = new JMenu(sa[0]);
                for(int i = 1; i < sa.length; i++) {
                    // create a composite id which has the task attached to it
                    String newid = id + "::" + sa[i];
                    ConcreteAction pluginAction = new ConcreteAction(sa[i]);
                    pluginAction.putValue("id", newid);
                    pluginAction.addActionListener(this);

                    // add it to the menu now
                    subMenu.add(pluginAction);
                }
                menu.add(subMenu);
            }
        }
    }

    /**
     * Method to get the ids and names of the default plugins that where found.
     * This plugins will be displayed under the plugin menu
     */
    public void getPluginNames() {
        pluginNames = ATPluginFactory.getInstance().getPluginNamesByCategory(ATPlugin.DEFAULT_CATEGORY);
    }

    /**
     * Method to get the ids and names of the import plugins that where found.
     * This plugins will be displayed under the import menu
     */
    public void getImportPluginNames() {
        importPluginNames = ATPluginFactory.getInstance().getPluginNamesByCategory(ATPlugin.IMPORT_CATEGORY);
    }

    /**
     * Method to get the ids and names of the tools plugins that where found.
     * This plugins will be displayed under the tool menu
     */
    public void getToolPluginNames() {
        toolPluginNames = ATPluginFactory.getInstance().getPluginNamesByCategory(ATPlugin.TOOL_CATEGORY);
    }
}