package org.archiviststoolkit.importer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.exceptions.ValidationException;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.ArchDescriptionSubjects;
import org.archiviststoolkit.model.Subjects;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.mydomain.SubjectsDAO;
import org.archiviststoolkit.structure.EAD.Geogname;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.StringHelper;

public class HandleGeognameAction implements Action
{
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel)
    {
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing geogname element");
        ArrayList gNameList = (ArrayList) getChildren(o);
        String gname = null;
        if(gNameList!=null)
            gname = (String) EADHelper.getClassFromList(gNameList,String.class);
        String source = ((Geogname)o).getSource();
        String lli = LookupListUtils.getLookupListItemFromCode(Subjects.class,"subjectSource",source);
        if(lli!=null){
            source=lli;
        }
        if(source==null||source.length()<1)
            source = "ingest";
        try{
            if(gname!=null){
                gname = StringHelper.cleanUpWhiteSpace(gname);
                addSubjects(archDescription,gname,"Geographic Name (651)",source);
            
            }
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
    public List getChildren(Object element)
    {
        Geogname geog = (Geogname)element;
        return geog.getContent();   
    }
    
    private void addSubjects(ArchDescription accession,
                                                     String subjectS,
                                                     String subjectTermType,
                                                     String subjectSource) throws PersistenceException, ValidationException, UnknownLookupListException {
    
        if(accession==null || !StringHelper.isNotEmpty(subjectS) || !StringHelper.isNotEmpty(subjectTermType) || !StringHelper.isNotEmpty(subjectSource))
            return;
        SubjectsDAO subjectDao = new SubjectsDAO();
        Subjects subject;
        ArchDescriptionSubjects accessionSubject;
        subject = subjectDao.lookupSubject(subjectS, subjectTermType, subjectSource, true);
        accessionSubject = new ArchDescriptionSubjects(subject, accession);
        accession.addSubject(accessionSubject);
    
    }    
    
}