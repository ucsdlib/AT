package org.archiviststoolkit.importer.MARCXML;

import java.util.List;
import java.util.Vector;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.exceptions.ValidationException;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.structure.MARCXML.DataFieldType;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

public class Handle651Action implements MARCXMLAction
{
    public void processElement(Resources resource, Object o, InfiniteProgressPanel progressPanel)
    {
    DataFieldType dataField = (DataFieldType) o;
    String indicator2 = dataField.getInd2();
    String thesaurus;
    String subjects[] = {"3","a","e","v","x","y","z","4"};
    Vector <String> subjectsV;
    subjectsV = MARCIngest.arrayToVector(subjects);   
    String subjectS = MARCIngest.getSpecificSubCodeValuesAsDelimitedString(dataField,subjectsV,"--");        

    if(!indicator2.equals("7"))
    thesaurus = (String) MARCIngest.thesaurusMappings.get(indicator2);
    else
    thesaurus = MARCIngest.getSubCodeValue(dataField,"2");
    try{
        MARCIngest.addSubjects(resource,subjectS,"Geographic Name (651)",thesaurus);
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