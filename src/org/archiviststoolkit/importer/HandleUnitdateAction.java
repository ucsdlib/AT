package org.archiviststoolkit.importer;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.structure.EAD.Unitdate;
import org.archiviststoolkit.util.StringHelper;

public class HandleUnitdateAction implements Action{

	private boolean debug = false;

    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel){
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing unitdate element");
        Unitdate udate;
        if(o instanceof JAXBElement)
            udate = (Unitdate)((JAXBElement)o).getValue();            
        else
            udate = (Unitdate) o;
        String unitdate=null;
        if(udate!=null)
            unitdate = (String) EADHelper.getClassFromList(udate.getContent(),String.class);
            if(unitdate!=null){
                if(archDescription.getDateExpression().length()>0){
                    EADHelper.setProperty(archDescription,"dateExpression",archDescription.getDateExpression()+" "+unitdate,null);
                    //archDescription.setDateExpression(archDescription.getDateExpression()+","+unitdate);
                }
                else
                    EADHelper.setProperty(archDescription,"dateExpression",unitdate,null);
                    //archDescription.setDateExpression(unitdate);
        
            }
        String type = "";
        String normal= "";
        type = udate.getType();
        normal = udate.getNormal();
        //System.out.println("normal1"+normal);
        if(type==null||(!type.equalsIgnoreCase("bulk"))||(type.equalsIgnoreCase("inclusive"))){
			if (debug) {
				System.out.println("hereInc");
			}
			if(normal!=null && normal.length()>0){
                Integer start = archDescription.getDateBegin();
                Integer end = archDescription.getDateEnd();
                StringHelper.simpleParseDate(archDescription,normal, true);
                
                if(archDescription.getDateBegin()!=null && start !=null){
                    if(start<archDescription.getDateBegin())
                        archDescription.setDateBegin(start);
                }
                if(archDescription.getDateEnd()!=null && end!=null){
                    if(end>archDescription.getDateEnd())
                        archDescription.setDateEnd(end);
                }      
            }
        }
        else if (type!=null && type.equals("bulk")){System.out.println("hereBulk");
        	if(normal!=null && normal.length()>0){
        		StringHelper.simpleParseDate(archDescription,normal, false);
        	}
        }

 }
        
    public List getChildren(Object element){
        return null;
    }
}
