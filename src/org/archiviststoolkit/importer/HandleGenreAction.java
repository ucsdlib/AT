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
import org.archiviststoolkit.structure.EAD.Genreform;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.StringHelper;

public class HandleGenreAction implements Action {
        private static String genre = "Genre / Form (655)";
	public  void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel){

                if(o instanceof JAXBElement)
                    o = ((JAXBElement)o).getValue();
//		ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing genre element");
		ArrayList genreList = (ArrayList) getChildren(o);
		String genreS = (String) EADHelper.getClassFromList(genreList,String.class);
		String source = ((Genreform)o).getSource();
	    String lli = LookupListUtils.getLookupListItemFromCode(Subjects.class,"subjectSource",source);
	    if(lli!=null){
	        System.out.println("Subject source = "+lli);
	        source=lli;
	    }
		if(source==null||source.length()<1)
			source = "ingest";
		try{
			if(StringHelper.isNotEmpty(genreS)){
                            System.out.println("genreS = "+genreS+"--"+genreS.length());
                            genreS = StringHelper.cleanUpWhiteSpace(genreS);
                            System.out.println("genre = "+genreS+"--"+genreS.length());

                            addSubjects(archDescription,genreS,genre,source);
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
	public List getChildren(Object element){
		Genreform genre = (Genreform)element;
		return genre.getContent();
	}


	private void addSubjects(ArchDescription accession,
							 String subjectS,
							 String subjectTermType,
							 String subjectSource) throws PersistenceException, ValidationException, UnknownLookupListException {
                if(StringHelper.isEmpty(subjectS))
                    return;
                SubjectsDAO subjectDao = new SubjectsDAO();
		Subjects subject;
		ArchDescriptionSubjects accessionSubject;

			subject = subjectDao.lookupSubject(subjectS, subjectTermType, subjectSource, true);
			accessionSubject = new ArchDescriptionSubjects(subject, accession);
			accession.addSubject(accessionSubject);

	}
}