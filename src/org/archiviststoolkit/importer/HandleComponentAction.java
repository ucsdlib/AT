package org.archiviststoolkit.importer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.structure.EAD.C;
import org.archiviststoolkit.structure.EAD.Did;
import org.archiviststoolkit.util.StringHelper;

public class HandleComponentAction implements Action {

	private static int depth = 2;


		public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel){
            if(o instanceof JAXBElement)
                o = ((JAXBElement)o).getValue();

            C c = (C)o;
            Did did = null;
			depth++;
//			if (additionalLines.size() <= depth) {
//				additionalLines.add("Component: " + HandleDidAction.getLabel(c.getDid()));
//			} else {
//				additionalLines.set(depth - 1, "Component: " + HandleDidAction.getLabel(c.getDid()));
//			}
			if (progressPanel != null) {
                // if process was cancelled then return
                if(progressPanel.isProcessCancelled()) {
                    return;
                }
				
                progressPanel.setTextLine( HandleDidAction.getLabel(c.getDid()), depth);
			}

			ResourcesComponents rc = new ResourcesComponents();
            String pid = EADInfo.getReferenceTOResources().getNextPersistentIdAndIncrement();
                
            if(archDescription instanceof Resources){
                ((Resources)archDescription).addResourcesComponents(rc);
                rc.setResource((Resources)archDescription);
                rc.setSequenceNumber(((Resources)archDescription).getResourcesComponents().size());
                rc.setPersistentId(pid);
                //EADInfo.addIdPairs(c.getId(),rc.getPersistentId());
                EADInfo.addIdPairs(c.getId(),pid);

            }    
            else if (archDescription instanceof ResourcesComponents){
                ((ResourcesComponents)archDescription).addResourcesComponents(rc);
                rc.setResourceComponentParent((ResourcesComponents)archDescription); 
                rc.setSequenceNumber(((ResourcesComponents)archDescription).getResourcesComponents().size());
                //EADInfo.addIdPairs(c.getId(),rc.getPersistentId());
                EADInfo.addIdPairs(c.getId(),pid);

                rc.setPersistentId(pid);

            }

            String level="";
            if(c.getLevel()!=null){
                level = (c.getLevel().value());          
            }   
            
            if(level==null||level.length()==0){
                level = "otherlevel";
            }
            
            EADHelper.setProperty(rc,"level",level);
 
            if("otherlevel".equals(level)){
                String otherLevel = c.getOtherlevel();
                EADHelper.setProperty(rc,"otherLevel",otherLevel,ResourcesComponents.class);
            }        
            
            String audience = "";
            audience = c.getAudience();
            if(StringHelper.isNotEmpty(audience) && audience.equalsIgnoreCase("internal")){
                rc.setInternalOnly(true);
            }
            
            ArrayList containerChildren = (ArrayList) getChildren(c);
            Iterator it;
            Action action;
            Object eadElem = null;
            it = containerChildren.iterator();
            EADInfo eadInfo = new EADInfo();
            
            did = c.getDid();
            action = eadInfo.getActionFromClass(did);
            action.processElement(rc,did, progressPanel);
            
            while(it.hasNext()){
                rc.setHasChild(true);
                eadElem = it.next();
                action = eadInfo.getActionFromClass(eadElem);
                if(action!=null)
                action.processElement(rc,eadElem, progressPanel);
            }
            depth--;
        }
        public List getChildren(Object element){
            List list = new ArrayList();
            C c = (C)element;
            List l = new ArrayList();
            l.addAll(c.getTheadAndC());
            l.addAll(c.getMDescFull());
            return l;
        }
}
