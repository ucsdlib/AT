package org.archiviststoolkit.importer.MARCXML;

import java.util.List;
import java.util.Vector;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.exceptions.ValidationException;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.structure.MARCXML.DataFieldType;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class Handle752Action implements MARCXMLAction
{
    public void processElement(Resources resource, Object o, InfiniteProgressPanel progressPanel)
    {
    DataFieldType dataField = (DataFieldType) o;
    String subjects[] = {"a","b","c","d","f","g","h"};
    Vector <String> subjectsV;
    subjectsV = MARCIngest.arrayToVector(subjects);   
    String subjectS = MARCIngest.getSpecificSubCodeValuesAsDelimitedString(dataField,subjectsV,"--");        
    String source = MARCIngest.getSubCodeValue(dataField,"2");

    try{
        MARCIngest.addSubjects(resource,subjectS,"Geographic Name (651)",source);
    }
    catch (UnknownLookupListException ulle)
    {
        ulle.printStackTrace();   
    }
    catch (PersistenceException pe)
    {
        pe.printStackTrace();
    }
    catch (ValidationException ve)
    {
        ve.printStackTrace();
    }        
      
    }
    public List getChildren(Object element)
    {
        return null;
    }
}