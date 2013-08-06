package org.archiviststoolkit.importer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.structure.EAD.Archdesc;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class HandleArchDescAction implements Action
{
	public void processElement(ArchDescription archDescription, Object archDesc, InfiniteProgressPanel progressPanel){
	    if(archDesc instanceof JAXBElement)
	        archDesc = ((JAXBElement)archDesc).getValue();
//		ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing Archdesc element");
		
                EADInfo eadInfo = new EADInfo();
                if(((Archdesc)archDesc).getLevel()!=null){
                String level = ((Archdesc)archDesc).getLevel().value();
                EADHelper.setProperty(archDescription,"level",level,Resources.class);
                if("otherlevel".equals(level)){
                    String otherLevel = ((Archdesc)archDesc).getOtherlevel();
                    EADHelper.setProperty(archDescription,"otherLevel",otherLevel,Resources.class);
                }
                }
		for (Object eadElem:getChildren(archDesc)){
			Action action = eadInfo.getActionFromClass(eadElem);
			if (progressPanel != null && progressPanel.isProcessCancelled()) {
                return;
            } else if (null!=action){
				action.processElement(archDescription,eadElem, progressPanel);
			}
		}

	}
	public List getChildren(Object element){
		//Retrieve child nodes of <archdesc>
		Archdesc archDesc = (Archdesc)element;
                List list = new ArrayList();
		List archDescChildren = archDesc.getMDescFull();
                if(archDesc.getDid()!=null)
                    list.add(archDesc.getDid());
                if(archDescChildren!=null)
                    list.addAll(archDescChildren);
                return list;
	}
}