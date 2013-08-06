package org.archiviststoolkit.importer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.ArchDescriptionAnalogInstances;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.model.ResourcesCommon;
import org.archiviststoolkit.model.ResourcesComponents;
import org.archiviststoolkit.structure.EAD.Container;
import org.archiviststoolkit.structure.EAD.Did;
import org.archiviststoolkit.structure.EAD.Unittitle;
import org.archiviststoolkit.util.StringHelper;


public class HandleDidAction implements Action {
    public void processElement(ArchDescription archDescription, Object did, InfiniteProgressPanel progressPanel) {
        if(did instanceof JAXBElement)
            did = ((JAXBElement)did).getValue();
        ArrayList didChildren = (ArrayList)getChildren(did);
        Iterator it = null;
        Object eadElem = null;
        Action action = null;
        EADInfo eadInfo = new EADInfo();

        it = didChildren.iterator();
        while (it.hasNext()) {
            eadElem = it.next();
            action = 
                    eadInfo.getActionFromClass(eadElem);
            if (null != action) {
                action.processElement(archDescription, eadElem, progressPanel);
            }
        }


        // This is code that processes the <container> elements
        // It is a bit complicated because of the mapping from EAD to AT data model
        
        //get list of containers
        ArrayList containers = 
            (ArrayList)EADHelper.getClassesFromList(didChildren, 
                                                    Container.class);
        
        ArchDescriptionAnalogInstances instance; 
            
        // The instance map will store a <name, value> pair which is the id of the 
        // <container> element and a corresponding ArchDescriptionAnalogInstances object
        HashMap instanceMap = new HashMap();
        
        //iterate through the container elements
        if (containers != null && containers.size() > 0) {

            if (archDescription instanceof Resources){
            	instance = new ArchDescriptionAnalogInstances(archDescription);
            }
       
            for (int e = 0; e < containers.size(); e++) {
            	// status 0 indicates a parent container element
            	// status 1 indicates a child container element
            	// status 2 means there is no id/parent attribute present in the container element
            	int status = 0;
            	
            	Container container = (Container)containers.get(e);
            	instance = new ArchDescriptionAnalogInstances();
            	
            	if(container.getId()!=null && container.getId().length()>0){
                	if(instanceMap.get(container.getId())==null)
                		instanceMap.put(container.getId(), instance);
                	else
                		instance = (ArchDescriptionAnalogInstances)instanceMap.get(container.getId());
                	if(container.getLabel()!=null)
                		instance.setInstanceType(container.getLabel());
                	status = 0;
            	}
            
            	else if(container.getParent()!=null && container.getParent().size()>0){
            		Container pContainer = (Container)container.getParent().get(0);
            		if (instanceMap.get(pContainer.getId())==null)
            				instanceMap.put(pContainer.getId(), instance);
            		else
            			instance = (ArchDescriptionAnalogInstances)instanceMap.get(pContainer.getId());
            		status = 1;
            	}
            
            	else{
            		if(instanceMap.get("no_id")==null)
            			instanceMap.put("no_id", instance);
            		else
            			instance = (ArchDescriptionAnalogInstances)instanceMap.get("no_id");
            		status = 2;
            		String type = container.getLabel();
            		if(type!=null && type.length()>0)
            			instance.setInstanceType(type);
            	}
                //parse container element and populate the instance object	
                parseContainers(instance, status, container);
            }

            //iterate through and save all the ArchDescrptionAnalogInstances object
            Iterator it2 = instanceMap.values().iterator();
            
            while (it2.hasNext()){
            	instance = (ArchDescriptionAnalogInstances)it2.next();
            if(archDescription instanceof ResourcesComponents){
            	instance.setResourcesComponents((ResourcesComponents)archDescription);
                ((ResourcesCommon)archDescription).addInstance(instance);
            }
            else if(archDescription instanceof Resources){
            	instance.setResource((Resources)archDescription);
                ((ResourcesCommon)archDescription).addInstance(instance);
            }
           }
        } 
       }
        
    	//This function populates the ArchDescriptionAnalogInstances object
        private void setInstance(String containerType, String containerValue, ArchDescriptionAnalogInstances instance, int status){
            if(status==0 || (status==2 && instance.getContainer1Type().length()==0)){    
            	EADHelper.setProperty(instance,"container1Type",(String)containerType);
            	if(instance.getInstanceType()==null || instance.getInstanceType().length()==0)
            		instance.setInstanceType("Mixed Materials");
            	
            	if (containerValue instanceof java.lang.String) {
            		try {
            			instance.setContainer1NumericIndicator(Double.valueOf((String)containerValue));
            		} catch (NumberFormatException ne) {
            			instance.setContainer1NumericIndicator(null);
            			instance.setContainer1AlphaNumericIndicator(containerValue);
            		}
            	}
            }
            
            else if(status>0){
            
            	if(instance.getContainer2Type()!=null && instance.getContainer2Type().length()==0){
            
            		EADHelper.setProperty(instance,"container2Type",(String)containerType);

            		if (containerValue instanceof java.lang.String) {
            			try {
            				instance.setContainer2NumericIndicator(Double.valueOf((String)containerValue));
            			} catch (NumberFormatException ne) {
            				instance.setContainer2NumericIndicator(null);
            				instance.setContainer2AlphaNumericIndicator(containerValue);
            			}
            		}
            	}
            	else{
            		EADHelper.setProperty(instance,"container3Type",(String)containerType);
        	
            		if (containerValue instanceof java.lang.String) {
            			try {
            				instance.setContainer3NumericIndicator(Double.valueOf((String)containerValue));
            			} catch (NumberFormatException ne) {
            				instance.setContainer3NumericIndicator(null);
            				instance.setContainer3AlphaNumericIndicator(containerValue);
            			}
            		}
            	}	
            }
        }

    //This function parses the container element and call the fucntion to populate the instance object
    private String parseContainers(ArchDescriptionAnalogInstances instance, int status, Container container){
        if(StringHelper.isEmpty(container.getType())){
            String value = (String)EADHelper.getClassFromList(container.getContent(),String.class);
            if(value==null)return null;
            else{
                value.trim();
                String c[] = value.split(" ");
                if(c.length==2){
                    if((c[0].equalsIgnoreCase("box"))||(c[0].equalsIgnoreCase("boxes"))||(c[0].equalsIgnoreCase("folder"))){
                    	setInstance(c[0],c[1],instance,status);	
                    }
                }
                        
            }
        }
        else{
            String value = (String)EADHelper.getClassFromList(container.getContent(),String.class);
        	setInstance(container.getType(),value,instance,status);	
            
        }
        return container.getId();
    }
    public List getChildren(Object element) {
        //Retrieve child nodes of <archdesc>
        Did did = (Did)element;
        List didChildren = did.getMDid();
        return didChildren;
    }

	public static String getLabel(Did did) {
		String returnString = "Unknown component";
		for (Object o: did.getMDid()) {
			if (o instanceof Unittitle) {
				return StringHelper.cleanUpWhiteSpace(EADHelper.ObjectNodetoString(o));
			}
		}
		return returnString;
	}


}
