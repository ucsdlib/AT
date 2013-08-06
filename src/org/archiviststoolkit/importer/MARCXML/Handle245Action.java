package org.archiviststoolkit.importer.MARCXML;

import java.util.List;

import java.util.Vector;

import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.structure.MARCXML.DataFieldType;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class Handle245Action implements MARCXMLAction
{
    public static int titlePrecedence = 4;
    public void processElement(Resources resource, Object o, InfiniteProgressPanel progressPanel){
        DataFieldType dataField = (DataFieldType) o;
        String titles[] = {"a","b","h","k","n","p","s"};
        Vector <String> titlesV;
        titlesV = MARCIngest.arrayToVector(titles);
        String title = MARCIngest.getSpecificSubCodeValuesAsDelimitedString(dataField,titlesV,",");

        String dates[] = {"f","g"};
        Vector <String> datesV;
        datesV = MARCIngest.arrayToVector(dates);
        String dateExp = MARCIngest.getSpecificSubCodeValuesAsDelimitedString(dataField,datesV,",");
        
        resource.setDateExpression(dateExp);
        
        if(MARCIngest.resourceTitle==null){
            resource.setTitle(title);
            MARCIngest.resourceTitle = (DataFieldType)o;            
            MARCIngest.resourceTitlePriority=this.titlePrecedence;System.out.println("here1");
        }
        else{            
            DataFieldType resTitle = MARCIngest.getResourceTitleTag();
            System.out.println("here2");
            MARCIngest.resourceTitle = (DataFieldType)o;            
            MARCIngest.resourceTitlePriority=this.titlePrecedence;        
            MARCIngest.processElement(resTitle,resource,true);
            resource.setTitle(title);
        }
    
    }
    public List getChildren(Object element)
    {
        return null;
    }
}
