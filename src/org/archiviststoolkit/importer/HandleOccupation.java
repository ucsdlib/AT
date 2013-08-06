package org.archiviststoolkit.importer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.ArchDescriptionSubjects;
import org.archiviststoolkit.model.Subjects;
import org.archiviststoolkit.exceptions.ValidationException;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.mydomain.SubjectsDAO;
import org.archiviststoolkit.structure.EAD.Occupation;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.StringHelper;

public class HandleOccupation implements Action {
    private static String occupation = "Occupation (656)";
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel) {
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
        ArrayList subjectList = (ArrayList)getChildren(o);
        String subjectS = 
            (String)EADHelper.getClassFromList(subjectList, String.class);
        if (subjectS != null)
            subjectS = StringHelper.cleanUpWhiteSpace(subjectS);
        String source = ((Occupation)o).getSource();
        String lli = 
            LookupListUtils.getLookupListItemFromCode(Subjects.class, "subjectSource", 
                                                      source);
        if (lli != null) {
            source = lli;
        }
        if (source == null || source.length() < 1)
            source = "ingest";
        try {
            if (subjectS != null && subjectS.length() > 0)
                addSubjects(archDescription, subjectS, occupation, 
                            source);
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        } catch (UnknownLookupListException pe) {
            pe.printStackTrace();
        } catch (ValidationException pe) {
            pe.printStackTrace();
        }
    }

    public List getChildren(Object element) {
        Occupation occup = (Occupation)element;
        return occup.getContent();
    }

    private void addSubjects(ArchDescription archDescription, String subjectS, 
                             String subjectTermType, 
                             String subjectSource) throws PersistenceException, 
                                                          ValidationException, 
                                                          UnknownLookupListException {
        SubjectsDAO subjectDao = new SubjectsDAO();
        Subjects subject;
        ArchDescriptionSubjects accessionSubject;
        if (archDescription == null || !StringHelper.isNotEmpty(subjectS) || 
            !StringHelper.isNotEmpty(subjectTermType) || 
            !StringHelper.isNotEmpty(subjectSource))
            return;
        subject = 
                subjectDao.lookupSubject(subjectS, subjectTermType, subjectSource, 
                                         true);
        accessionSubject = 
                new ArchDescriptionSubjects(subject, archDescription);
        archDescription.addSubject(accessionSubject);

    }
}
