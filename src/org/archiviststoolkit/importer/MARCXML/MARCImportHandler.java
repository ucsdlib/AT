package org.archiviststoolkit.importer.MARCXML;

import com.jgoodies.validation.ValidationResult;

import java.io.File;

import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.swing.ImportOptionsMARC;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.importer.ImportException;
import org.archiviststoolkit.importer.ImportHandler;
import org.archiviststoolkit.importer.ImportExportLogDialog;
import org.archiviststoolkit.importer.ImportFactory;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.validators.ATValidator;
import org.archiviststoolkit.model.validators.ValidatorFactory;
import org.archiviststoolkit.mydomain.DomainImportController;
import org.archiviststoolkit.mydomain.LookupException;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.mydomain.ResourcesDAO;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.MyTimer;

public class MARCImportHandler extends ImportHandler {

    private Repositories repository;

    private ImportOptionsMARC importOptions;


    private boolean resourceExists = false;

    /**
     * Constructor.
     */
    public MARCImportHandler(ImportOptionsMARC importOptions) {
        super();
        this.repository = importOptions.getRepository();
        this.importOptions = importOptions;
    }

    public boolean canImportFile(File file) {
        return true;
    }

    public boolean importFile(File file,
							  DomainImportController domainController, InfiniteProgressPanel progressPanel) throws ImportException {
        Resources res = null;
        ApplicationFrame.getInstance().getTimer().reset();
        ResourcesDAO rdao = new ResourcesDAO();
        boolean importAll = importOptions.getImportEntireRecord();
        System.out.println(importAll);
        String id1 = this.importOptions.getResourceIdentifier1();
        String id2 = this.importOptions.getResourceIdentifier2();
        String id3 = this.importOptions.getResourceIdentifier3();
        String id4 = this.importOptions.getResourceIdentifier4();

        LookupListUtils.initIngestReport();


        res = 
getResourcetoSave(id1, id2, id3, id4, this.repository, rdao, importAll);
        if (!resourceExists && res != null) {
            if (id1 != null)
                res.setResourceIdentifier1(id1);
            if (id2 != null)
                res.setResourceIdentifier2(id2);
            if (id3 != null)
                res.setResourceIdentifier3(id3);
            if (id4 != null)
                res.setResourceIdentifier4(id4);
            res.setRepository(this.repository);
        }
        if (res == null && resourceExists) {
			progressPanel.close();
            ImportExportLogDialog dialog =
                new ImportExportLogDialog("The record already exists in the system.", ImportExportLogDialog.DIALOG_TYPE_IMPORT);
            dialog.showDialog();
            return false;
        }

        if (!resourceExists && !importAll) {
			progressPanel.close();
            ImportExportLogDialog dialog =
                new ImportExportLogDialog("The valid record identifer must be entered to add/import name and subject headings.", ImportExportLogDialog.DIALOG_TYPE_IMPORT);
            dialog.showDialog();
            return false;
        }

        progressPanel.setTextLine("Saving Record", 1);

        res.setLevel(this.importOptions.getResourceLevel());

        // see whether to use the built in or user supplied ingest engine
        MARCIngest mingest;

        if(ImportFactory.getInstance().getMarcIngest() != null) {
            mingest = ImportFactory.getInstance().getMarcIngest();
        } else {
            mingest = new MARCIngest();
        }

        boolean importSuccess = mingest.doIngest(file, res, importAll, progressPanel);
        if (!importSuccess)
            return false;
        MARCIngest.report = "";
        String header =  "Summary\n";
        header = header +("-------\n");
        MARCIngest.report = 
                MARCIngest.report + "Title of Imported Record:\t" + res.getTitle() + 
                "\n";


        ATValidator validator;
        ValidationResult validationResult;
        String errors = "";
        validator = ValidatorFactory.getInstance().getValidator(res);
        if (validator != null) {
            validationResult = validator.validate();
            if (validationResult.hasErrors()) {
                errors = 
                        errors + "Invalid Record:\n" + validationResult.getMessagesText() + 
                        "\n";
            }
        }

        String lookupReport = "";
        lookupReport = LookupListUtils.getIngestReport();
        if (MARCIngest.report != null || MARCIngest.report.length() > 0)
            MARCIngest.report = MARCIngest.report + lookupReport + "\n";
            
           

        try {
            if (resourceExists)
                rdao.updateLongSession(res);
            else
                rdao.add(res);
           Resources.addResourceToLookupList(res);
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        
        String timer = MyTimer.toString(ApplicationFrame.getInstance().getTimer().elapsedTimeMillisSplit());

        MARCIngest.report = MARCIngest.report + errors + "\n";

        String timeReport = 
                "Time to Import Record:\t" + timer + 
                "\n"; 

        MARCIngest.report = header+timeReport+MARCIngest.report;
        
        ImportExportLogDialog dialog = new ImportExportLogDialog(MARCIngest.report, ImportExportLogDialog.DIALOG_TYPE_IMPORT);
		progressPanel.close();
		dialog.showDialog();
        return true;
    }

    private Resources getResourcetoSave(String id1, String id2, String id3, 
                                        String id4, Repositories repository, 
                                        ResourcesDAO resourcesDAO, 
                                        boolean importALL) {
        //resourcesDAO = new ResourcesDAO();
        Resources res = 
            resourcesDAO.lookupResource(id1, id2, id3, id4, false, repository);
        if (res != null) {
            try {
                res = 
(Resources)resourcesDAO.findByPrimaryKeyLongSession(res.getIdentifier());
                if (res != null && res.getResourcesComponents().size() > 0) {
                    this.resourceExists = true;
                    if (importALL)
                        return null;
                    else
                        return res;
                } else {
                    this.resourceExists = true;
                    return res;

                }
            } catch (LookupException le) {
                le.printStackTrace();
            }

        } else {
            resourceExists = false;
            res = new Resources();
            return res;
        }
        return res;
    }


}
