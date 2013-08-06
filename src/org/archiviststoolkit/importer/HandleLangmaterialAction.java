package org.archiviststoolkit.importer;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.structure.EAD.Langmaterial;
import org.archiviststoolkit.structure.EAD.Language;

public class HandleLangmaterialAction implements Action{
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel){
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing Langmaterial element");
        Langmaterial langmaterial = (Langmaterial) o;
        String langcode = "";
        if(langmaterial!=null){
            Language language = (Language) EADHelper.getClassFromList(langmaterial.getContent(),Language.class);
            if(language!=null){
                langcode = language.getLangcode();
                String langItem = LookupListUtils.getLookupListItemFromCode(Resources.class,"languageCode",langcode);
                EADHelper.setProperty(archDescription,"languageCode",langItem,null);
                //TODO should the previous be ResourcesCommon also
                /*if(langcode!=null&&langcode.length()>0){
                    try{
                        langcode = LookupListUtils.addItemToListByField(Resources.class,"languageCode",langcode);
                    }
                    catch (FieldNotFoundException fnfe){
                        fnfe.printStackTrace();
                    }
                    catch (UnknownLookupListException ulle){
                        ulle.printStackTrace();
                    }
                    ((ResourcesCommon)archDescription).setLanguageCode(langcode);
                }*/
            }
        }        
    }
    public List getChildren(Object element){
        return null;
    }
}
