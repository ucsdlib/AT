package org.archiviststoolkit.importer;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.BasicNames;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.structure.EAD.Corpname;
import org.archiviststoolkit.structure.EAD.Famname;
import org.archiviststoolkit.structure.EAD.Name;
import org.archiviststoolkit.structure.EAD.Origination;
import org.archiviststoolkit.structure.EAD.Persname;
import org.archiviststoolkit.util.NameUtils;
import org.archiviststoolkit.util.StringHelper;

public class HandleOriginationAction implements Action {
	public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel) {
		if (o instanceof JAXBElement)
			o = ((JAXBElement) o).getValue();
		//ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing origination element");
		String role = "Creator";
		Origination origination = (Origination) o;
		String creator = "";
		if (origination != null) {

			List persnames = EADHelper.getClassesFromList(origination
					.getContent(), Persname.class);
			for (Object persname : persnames) {
				if (persname != null) {
					{
						creator = (String) EADHelper.getClassFromList(
								((Persname) persname).getContent(),
								String.class);
						if (creator != null)
							creator = StringHelper.cleanUpWhiteSpace(creator);
						Names name = new Names();
						//name.setSortName(creator);
						//name.setPersonalPrimaryName(creator);
				        EADHelper.setProperty(name, "sortName", creator, 
				                Names.class);
				        EADHelper.setProperty(name, "personalPrimaryName", creator, 
				                Names.class);						
						name.setNameType(Names.PERSON_TYPE);
						addName(archDescription, name, role);

					}
				}
			}
			
			List names = EADHelper.getClassesFromList(origination
					.getContent(), Name.class);
			for (Object eadname : names) {
				if (eadname != null) {
					{
						creator = (String) EADHelper.getClassFromList(
								((Name) eadname).getContent(),
								String.class);
						if (creator != null)
							creator = StringHelper.cleanUpWhiteSpace(creator);
						Names name = new Names();
						//name.setSortName(creator);
						//name.setPersonalPrimaryName(creator);
				        EADHelper.setProperty(name, "sortName", creator, 
				                Names.class);
				        EADHelper.setProperty(name, "personalPrimaryName", creator, 
				                Names.class);						
						name.setNameType(Names.PERSON_TYPE);
						addName(archDescription, name, role);

					}
				}
			}			

			List famnames = EADHelper.getClassesFromList(origination
					.getContent(), Famname.class);
			for (Object famname : famnames) {
				if (famname != null) {
					{
						creator = (String) EADHelper.getClassFromList(
								((Famname) famname).getContent(),
								String.class);
						if (creator != null)
							creator = StringHelper.cleanUpWhiteSpace(creator);
						Names name = new Names();
				        EADHelper.setProperty(name, "sortName", creator, 
				                Names.class);
				        EADHelper.setProperty(name, "familyName", creator, 
				                Names.class);						
						name.setNameType(Names.FAMILY_TYPE);
						addName(archDescription, name, role);

					}
				}
			}			
			
			
			
			List<Corpname> corpnames = EADHelper.getClassesFromList(origination
					.getContent(), Corpname.class);
			for (Corpname corpname : corpnames) {

				if (corpname != null) {
					creator = (String) EADHelper.getClassFromList(corpname
							.getContent(), String.class);
					if (creator != null)
						creator = StringHelper.cleanUpWhiteSpace(creator);
					Names name = new Names();
					//name.setSortName(creator);
					//name.setCorporatePrimaryName(creator);
				      EADHelper.setProperty(name, "sortName", creator, 
				                Names.class);
				        EADHelper.setProperty(name, "corporatePrimaryName", creator, 
				                Names.class);
					name.setNameType(Names.CORPORATE_BODY_TYPE);
					addName(archDescription, name, role);
				}
			}
		}
	}

	private void addName(ArchDescription archDescription, Names name,
			String role) {

		try {
			NameUtils.addName(archDescription, name, role);
		} catch (PersistenceException pe) {
			pe.printStackTrace();
		} catch (UnknownLookupListException ulle) {
			ulle.printStackTrace();
		} catch (DuplicateLinkException dle) {
			dle.printStackTrace();
		}

	}

	public List getChildren(Object element) {
		return null;
	}

}