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
import org.archiviststoolkit.structure.EAD.Subject;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.StringHelper;

public class HandleSubjectAction implements Action {
    private static String subject = "Topical Term (650)";

    public  void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel){
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing subject element.");
        ArrayList subjectList = (ArrayList) getChildren(o);
        String subjectS = (String) EADHelper.getClassFromList(subjectList,String.class);                
        if(subjectS!=null)
            subjectS=StringHelper.cleanUpWhiteSpace(subjectS);
        String source = ((Subject)o).getSource();
        String lli = LookupListUtils.getLookupListItemFromCode(Subjects.class,"subjectSource",source);
        if(lli!=null){
            source=lli;
        }
        if(source==null||source.length()<1)
            source = "ingest";
        try{
            if(subjectS!=null && subjectS.length()>0)             
                addSubjects(archDescription,subjectS,subject,source);
        }
        catch (PersistenceException pe){
            pe.printStackTrace();
        }
        catch (UnknownLookupListException pe){
            pe.printStackTrace();
        }
        catch (ValidationException pe){
            pe.printStackTrace();
        }        
    }
    public List getChildren(Object element){
        Subject subj = (Subject)element;
        return subj.getContent();
    }
    
	private void addSubjects(ArchDescription archDescription,
							 String subjectS,
							 String subjectTermType,
							 String subjectSource) throws PersistenceException, ValidationException, UnknownLookupListException {
        SubjectsDAO subjectDao = new SubjectsDAO();
        Subjects subject;
        ArchDescriptionSubjects accessionSubject;

            subject = subjectDao.lookupSubject(subjectS, subjectTermType, subjectSource, true);
            accessionSubject = new ArchDescriptionSubjects(subject, archDescription);
            archDescription.addSubject(accessionSubject);
    
    }    
}