package org.archiviststoolkit.importer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.ResourcesCommon;
import org.archiviststoolkit.structure.EAD.Dimensions;
import org.archiviststoolkit.structure.EAD.Extent;
import org.archiviststoolkit.structure.EAD.Physdesc;
import org.archiviststoolkit.util.StringHelper;

public class HandlePhysdescAction implements Action {

    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel)
    {
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing physdesc element");
        ArrayList physDescChildren = (ArrayList) getChildren(o);

        Iterator it;
        Object eadElem = null;
        Action action = null;
        String level="";
        it = physDescChildren.iterator();
        //EADInfo eadInfo = new EADInfo();       
        StringBuffer containerSummary = new StringBuffer();
        Object ob = null;


        while (it.hasNext()) {
            ob = it.next();
            if(ob instanceof JAXBElement){
            	ob = ((JAXBElement)ob).getValue();
            }
            if (ob instanceof String){
                containerSummary.append((String)ob);
            }
            else if(ob instanceof Dimensions){
                action = EADInfo.getActionFromClass(ob);
                action.processElement(archDescription, ob, progressPanel);
        	}
            else{
            	boolean parseExtent=false;
            	if(ob instanceof Extent){
            		parseExtent = parseExtentInformation(archDescription, (Extent)ob);
            	}
                if(!parseExtent)
                	containerSummary.append(EADHelper.ObjectNodetoString(ob));
            }
        }
    
        ((ResourcesCommon)archDescription).setContainerSummary(containerSummary.toString());

    }
    public static boolean parseExtentInformation(ArchDescription archDescription, Extent extent){
    	String extentString = (String)EADHelper.getClassFromList(extent.getContent(), String.class);
    	if(extentString ==null)
    		return false;
    	if(!(extentString.contains(" ")))
    		return false;
    	
    	int firstSpace = extentString.indexOf(" ");
    	String partOne = null;
    	String partTwo = null;
    	
    	partOne = extentString.substring(0,firstSpace);
    	partOne = partOne.trim();
    	if(extentString.length()>firstSpace+1){
    		partTwo = extentString.substring(firstSpace+1,extentString.length());
    		partTwo = partTwo.trim();
    	}
    		else return false;
    	
    	
    	
    	if(StringHelper.isNotEmpty(partOne) && StringHelper.isNotEmpty(partTwo)){
    		if(partTwo.length()>20)
    			return false;
    		Double partOneDouble = null;
    		try{
    			partOneDouble = Double.parseDouble(partOne);
    			EADHelper.setProperty(archDescription,"extentNumber",partOneDouble);
    			
    			EADHelper.setProperty(archDescription,"extentType",partTwo);

    		}
    		catch (NumberFormatException pe){
    			pe.printStackTrace();
    			return false;
    		}
    		return true;
    	}
    	return false;
    }
    public static void main(String args[]){
    	Extent extent = new Extent();
    	extent.getContent().add("1.0 linear feet");
    	boolean parse = HandlePhysdescAction.parseExtentInformation(null,extent);
    }
    public List getChildren(Object element)
    {
        return ((Physdesc)element).getContent();
    }

}