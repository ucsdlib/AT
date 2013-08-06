package org.archiviststoolkit.importer;

import com.jgoodies.validation.ValidationResult;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.parsers.ParserConfigurationException;

import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.exceptions.DuplicateRecordException;
import org.archiviststoolkit.structure.EAD.Archdesc;
import org.archiviststoolkit.structure.EAD.Did;
import org.archiviststoolkit.structure.EAD.Ead;
import org.archiviststoolkit.structure.EAD.Unitid;
import org.archiviststoolkit.swing.ImportOptionsEAD;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.validators.ATValidator;
import org.archiviststoolkit.model.validators.ValidatorFactory;
import org.archiviststoolkit.mydomain.DomainImportController;
import org.archiviststoolkit.mydomain.LookupException;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.mydomain.ResourcesDAO;
import org.archiviststoolkit.util.CharacterConvert;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.MyTimer;
import org.archiviststoolkit.util.ResourceUtils;
import org.archiviststoolkit.util.SimpleFileFilter;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.util.XMLUtil;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class EADImportHandler extends ImportHandler {

	private Repositories repository;
	final int UPDATE = 0;
	final int CREATE = 1;

    // make this resource record static so that it can be set in digital instances
    // to make searching for digital objects by resource also extend to digital objects
    // linked to resource components
	public static Resources resourceRecord = null;

    String timeToImport = null;
	String validationResult = null;
	String lookupListResult = null;

	private boolean debug = false;

	private static ResourcesDAO resourcesDAO = null;
	private static String truncations = "";
	
	/**
	 * Constructor.
	 */
	public EADImportHandler(ImportOptionsEAD importOptions) {
		super();
		this.repository = importOptions.getRepository();
	}

	public boolean canImportFile(File file) {
		return true;
	}

	public boolean importFile(File file, DomainImportController domainController, InfiniteProgressPanel progressPanel)
			throws ImportException {
		LookupListUtils.initIngestReport();

		if (file.isFile()) {
			try {
				ApplicationFrame.getInstance().getTimer().reset();
				progressPanel.setTextLine("Importing " + file.getName(), 1);
				boolean val = importFileNew(file, progressPanel);

                if(val) {
                    setTimeToImport(MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillis()));
                    progressPanel.close();
				    displaySingleImportLog();
                } else {
                    progressPanel.close();
                }

				return val;
			} catch (Exception e) {
				setTimeToImport(MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillis()));
				progressPanel.close();
				displaySingleImportErrorLog(e);
				return false;
			}
			finally{
				if (debug) {
					System.out.println("HERE in Clause");
				}
				File temp = new File(file.getParent()+"\\modifiedEAD.xml");
				File temp2 = new File("modifiedEAD.xml");
				if(temp.exists())
					temp.delete();
				if(temp2.exists())
					temp2.delete();
			}
		}

		else if (file.isDirectory()) {
			int recordsSaved = 0;
			int recordsNotSaved = 0;

			ApplicationFrame.getInstance().getTimer().reset();
			File files[] = file.listFiles(new SimpleFileFilter(".xml"));
			StringBuffer detail = new StringBuffer();

			int count = 1;
			int numberOfFiles = files.length;
			for (File f : files) {
                // if process was cancelled then return
                if(progressPanel.isProcessCancelled()) {
                    detail.append("Import of record(s) cancelled by user");
                    recordsSaved--;
                    break;
                }

                if (f.isFile()) {
					if (!f.getName().equals("modifiedEAD.xml")) {
						try{
							progressPanel.setTextLine("Importing file " + count++ + " of " +
									numberOfFiles + " -- " + f.getName(), 1);
							importFileNew(f, progressPanel);
							detail.append("Filename: " + f.getName() + " -- success");
							detail.append("\n"+getValidationResult());
							recordsSaved++;
						}
						 catch (Exception e) {
							detail.append("Filename: " + f.getName() + " -- " + evaluateImportError(e));
							recordsNotSaved++;
						}
						finally{
							File temp = new File(f.getParent()+"\\modifiedEAD.xml");
							File temp2 = new File("modifiedEAD.xml");
							if(temp.exists())
								temp.delete();
							if(temp2.exists())
								temp2.delete();
						}
						String timer = MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillisSplit());
						detail.append(" -- " + timer + "\n\n");
					}
				}
			}
			progressPanel.close();
			showBatchImportLog(detail, recordsSaved, recordsNotSaved, MyTimer.toString(ApplicationFrame.getInstance()
					.getTimer().elapsedTimeMillis()));

		}
		return true;
	}

	/*
	 * Importing a file consists of the following steps: 1.) Validate the
	 * incoming file 2.) Preprocess the file. 3.) Validate the preprocessed
	 * file. 4.) Unmarshall the file into a Ead object. 5.) Look for required
	 * values (ie: unitid) in the ead. 6.) Check to see if: A new resource
	 * record needs to created for this ead (OR) A resource record already
	 * exists in the database for this ead and can be updated (OR) A resource
	 * record already exists in the database for this ead and cannot be updated.
	 * 7.) Convert ead object into resource record (core import functionality)
	 * 8.) validate resource object 9.) save resource object!
	 */
	public boolean importFileNew(File file, InfiniteProgressPanel progressPanel) throws JAXBException,
			PersistenceException, ImportException, DuplicateRecordException, SAXException, ParserConfigurationException, IOException {
		Resources resource = null;
		EADIngest2 importer = null;
		String newFileName;
		String unitId;
		Ead ead;
		int saveOrUpdate;
		String validationErrors = "";
		String lookuplistReport = "";

		// initialization
		//LookupListUtils.initIngestReport();
		setTimeToImport(null);
		setValidationResult(null);
		setLookupListResult(null);
		setTruncations("");

        // see whether to use the built in or user supplied ingest engine
        if(ImportFactory.getInstance().getEadIngest() != null) {
            importer = ImportFactory.getInstance().getEadIngest();
        } else {
            importer = new EADIngest2();
        }

		resourcesDAO = new ResourcesDAO();
		setResourcesDAO(resourcesDAO);

		// 1.) Validate incoming file
		progressPanel.setTextLine("Validating file", 2);
		int validationDocumentType = validate(file);

		// 2.) Preprocess the incoming file
		progressPanel.setTextLine("Preprocessing file", 2);
		newFileName = preprocessFile(file,validationDocumentType);

		// 3.) Validate preprocessed file
		progressPanel.setTextLine("Validating file", 2);
		//not sure if we want to do this for files that originally DTD based 
		//validate(new File(newFileName));

		// 4.) Unmarshall the xml file into Ead object
		progressPanel.setTextLine("Loading file into memory", 2);
		ead = (Ead) EADInfo.getContext().createUnmarshaller().unmarshal(
				new File(newFileName));

		// 5.) Get unitid		
		unitId = getUnitIdFromEad(ead);
		if(unitId==null){
			throw new ImportException("There is no unitid present in the EAD document.  This is a required element.");
		}

		//make sure the unitid does not have any extraneous whitespace before checking
		unitId = StringHelper.cleanUpWhiteSpace(unitId);
        int strlen = 0;
        strlen = EADHelper.checkFieldLength(Resources.class, "resourceIdentifier1");
        if(strlen!=-1 && unitId.length()>strlen)
        	unitId = unitId.substring(0,strlen);
        System.out.println("CHECKING for"+unitId);
		// 6.)lookup resource to save
		resource = getResourceToSave(unitId, this.repository);
		if (resource != null) {
			saveOrUpdate = UPDATE;
			if (resource.getResourcesComponents().size() > 0) {
				throw new DuplicateRecordException();
			}
		} else {
			resource = new Resources();
			saveOrUpdate = CREATE;
		}

        if (debug) {
			System.out.println("HERE-----------------------1");
		}

        // set the resource record so that it becomes available to the Action Handelers
        setResourceRecord(resource);

		// 7.)convert ead element into given resource element
		importer.convertFileToResourceNew(ead, resource, progressPanel);

        // check to see if the import process was cancelled if so returned false
        if(progressPanel.isProcessCancelled()) {
            return false;
        }

		resource.setRepository(this.repository);
		if (debug) {
			System.out.println("HERE-----------------------2");
		}
		// 8.) validate resource
		validationErrors = validateResource(resource);
		if (debug) {
			System.out.println("HERE-----------------------3");
		}

		// 9.) save resource
		saveResource(resource, saveOrUpdate, progressPanel);
		if (debug) {
			System.out.println("HERE-----------------------4");
		}

		lookuplistReport = LookupListUtils.getIngestReport();
		Resources.addResourceToLookupList(resource);
	
		setValidationResult(validationErrors);
		setLookupListResult(lookuplistReport);
		
		return true;
	}

	public int validate(File file) throws SAXException, ParserConfigurationException, IOException, ImportException {
		try{
			return(XMLUtil.validateFile(file));
			
		}
		catch(SAXException saxe){
			throw saxe;
		}
	}

	public String validateResource(Resources resource) {
		ATValidator validator;
		ValidationResult validationResult;
		String validationErrors = "";

		validator = ValidatorFactory.getInstance().getValidator(resource);
		if (validator != null) {
			validationResult = validator.validate();
			if (validationResult.hasErrors()) {
				validationErrors = validationErrors + "Invalid Record\n"
						+ validationResult.getMessagesText() + "\n";
			}
		}
		return validationErrors;
	}

	public void saveResource(Resources resource, int method, InfiniteProgressPanel progressPanel)
			throws PersistenceException {
		if (method == UPDATE) {
			if (debug) {
				System.out.println("before rewrite");
			}
			ResourceUtils.rewriteTargets(resource, EADIngest2.idPairs, progressPanel);
			if (debug) {
				System.out.println("after rewrite");
			}
			progressPanel.setTextLine("Saving record", 2);
			getResourcesDAO().updateLongSession(resource);
		} else if (method == CREATE) {
			if (debug) {
				System.out.println("before rewrite");
			}
			ResourceUtils.rewriteTargets(resource, EADIngest2.idPairs, progressPanel);
			if (debug) {
				System.out.println("after rewrite");
			}
			progressPanel.setTextLine("Saving record", 2);
			getResourcesDAO().add(resource);
		}
	}

	public Resources getResourceToSave(String unitIdString,
			Repositories repository) throws ImportException {
		Resources res = null;
		res = getResourcesDAO().lookupResource(unitIdString, false, repository);
		if (res != null) {
			try {
				res = (Resources) resourcesDAO.findByPrimaryKeyLongSession(res
						.getIdentifier());
			} catch (LookupException le) {
				throw new ImportException(
						"There was a problem looking up the resource to be saved",
						le);
			}

		} else {

			return null;
		}
		return res;
	}

	public String getUnitIdFromEad(Ead ead) {
		Archdesc archDesc = ead.getArchdesc();
		Did did = null;
		Unitid unitId = null;
		String unitIdString = "";
		if (archDesc != null)
			did = archDesc.getDid();
		if (did != null)
			unitId = (Unitid) EADHelper.getClassFromList(did.getMDid(),
					Unitid.class);
		if (unitId != null)
			unitIdString = (String) EADHelper.getClassFromList(unitId
					.getContent(), String.class);

		if (StringHelper.isEmpty(unitIdString)) {
			return null;
		} else {
			return unitIdString;
		}

	}

	private String preprocessFile(File fileName, int validationDocumentType) {
		CharacterConvert converter = new CharacterConvert();
		return converter.doConversion(fileName, validationDocumentType);
	}

	private void handleError(Exception e) { 
		String message = "There is an error with ingest of this document. Please contact the system administrator";
		if(e instanceof ImportException){
			message = e.getMessage();
		}
		ErrorDialog dialog = new ErrorDialog(message,StringHelper.getStackTrace(e));
		dialog.showDialog();
	}

	private String evaluateImportError(Exception e) {

		if (e instanceof JAXBException && e.getMessage() != null && e.getMessage().startsWith("unexpected element")) {
			return "The file you are importing does not appear to be an EAD document.";
		} else if (e instanceof DuplicateRecordException) {
			return "The record already exists in the system.";
        } else if (e instanceof ImportException) {
            return e.getMessage();
        } else if (e instanceof FileNotFoundException) {
            return e.getMessage();
        } else if (e instanceof UnmarshalException) {
			Throwable ex = ((UnmarshalException)e).getLinkedException();
			if (ex instanceof SAXParseException && ex != null && ex.getMessage().contains("entity") && ex.getMessage().contains("was referenced, but not declared")) {
				return "The instance contains named entities. They need to be resolved\n" + ex.getMessage();
			} else {
				return "There is a problem importing this EAD document.\n\n" + e.getMessage() + "\n" + StringHelper.getStackTrace(e);
			}
		} else {
			return "There is a problem importing this EAD document.\n\n" + e.getMessage() + "\n" + StringHelper.getStackTrace(e);
		}
	}

	private void displaySingleImportLog(){
		String logMessage = "";
		String header = "Summary\n";
		header = header + ("-------\n");
		String timeReport = "Time to Import Record:\t" + getTimeToImport() + "\n";
		String titleReport = "Title of Imported Record:\t" + getResourceRecord().getTitle() + "\n";
        
		logMessage = header + timeReport + titleReport  + "\n" +getValidationResult()+ "\n"+ getLookupListResult() + getTruncations();

		ImportExportLogDialog dialog = new ImportExportLogDialog(logMessage, ImportExportLogDialog.DIALOG_TYPE_IMPORT);
		dialog.showDialog();

	}

	private void displaySingleImportErrorLog(Exception e){
		String logMessage = "";
		String header = "Summary\n";
		header = header + ("-------\n");
		String timeReport = "Time to Import Record:\t" + getTimeToImport() + "\n";
		String titleReport = "\nThe document failed to import\n" + evaluateImportError(e) + "\n";

		logMessage = header + timeReport + titleReport;

		ImportExportLogDialog dialog = new ImportExportLogDialog(logMessage, ImportExportLogDialog.DIALOG_TYPE_IMPORT);
		dialog.showDialog();

	}

	public void showBatchImportLog(StringBuffer detail, int passed, int failed, String timeToImport){
		StringBuffer message = new StringBuffer();
		message.append("Summary\n");
		message.append("-------\n");

		message.append("Time to Import Record(s):\t" + timeToImport + "\n");
		message.append("Number of Files Imported:\t" + passed + "\n");
		message.append("Number of Files Not Imported:\t" + failed
				+ "\n\n");

		message.append(getLookupListResult()+"\n\n");

		message.append("Details\n");
		message.append("-------\n");

		message.append(detail);
		
		message.append(getTruncations());
		ImportExportLogDialog dialog = new ImportExportLogDialog(message.toString(), ImportExportLogDialog.DIALOG_TYPE_IMPORT);
		dialog.showDialog();
	}

	public static Resources getResourceRecord() {
		return resourceRecord;
	}

	private void setResourceRecord(Resources resourceRecord) {
		this.resourceRecord = resourceRecord;
	}

	private String getTimeToImport() {
		return timeToImport;
	}

	private void setTimeToImport(String timeToImport) {
		this.timeToImport = timeToImport;
	}

	private String getValidationResult() {
		return validationResult;
	}

	private void setValidationResult(String validationResult) {
		this.validationResult = validationResult;
	}

	private String getLookupListResult() {
		return lookupListResult;
	}

	private void setLookupListResult(String lookupListResult) {
		this.lookupListResult = lookupListResult;
	}

	private static ResourcesDAO getResourcesDAO() {
		return resourcesDAO;
	}

	private static void setResourcesDAO(ResourcesDAO resourcesDAO) {
		EADImportHandler.resourcesDAO = resourcesDAO;
	}

	public static String getTruncations() {
		return truncations;
	}

	public static void setTruncations(String truncations) {
		EADImportHandler.truncations = truncations;
	}
	
	public static void addToTruncations(String text) {
		EADImportHandler.truncations = EADImportHandler.truncations+text;
	}

}