package org.archiviststoolkit.importer;

import java.util.HashMap;
import javax.xml.bind.JAXBException;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.structure.EAD.Archdesc;
import org.archiviststoolkit.structure.EAD.Ead;

import org.archiviststoolkit.structure.EAD.Eadheader;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class EADIngest2 {

	public static HashMap idPairs;

	public Resources convertFileToResourceNew(Ead ead,Resources resource, InfiniteProgressPanel progressPanel) throws JAXBException{

		idPairs = new HashMap();
		EADInfo.sequence = 0;
		EADInfo.setReferenceTOResources(resource);
	
		Archdesc archDesc = null;
		Eadheader eadHeader = null;
		
		archDesc = ead.getArchdesc();
		eadHeader = ead.getEadheader();
		Action a = new HandleEadheaderAction();
		a.processElement(resource, eadHeader, progressPanel);
		a = new HandleArchDescAction();
		a.processElement(resource, archDesc, progressPanel);

		return resource;		
	}
}
