package org.archiviststoolkit.importer;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.structure.EAD.Author;
import org.archiviststoolkit.structure.EAD.Sponsor;
import org.archiviststoolkit.structure.EAD.Subtitle;
import org.archiviststoolkit.structure.EAD.Titleproper;
import org.archiviststoolkit.structure.EAD.Titlestmt;

public class HandleTitlestmtAction implements Action
{
    public void processElement(ArchDescription archDescription, Object o, InfiniteProgressPanel progressPanel)
    {   
        if(o instanceof JAXBElement)
            o = ((JAXBElement)o).getValue();
//        ApplicationFrame.getInstance().updateProgressMessageSecondLine("Importing titlestmt element");
        
        

        List<Subtitle> subTitles = ((Titlestmt)o).getSubtitle();
        if(subTitles!=null && subTitles.size() >0 && subTitles.get(0)!=null && subTitles.get(0) instanceof Subtitle){
            Subtitle subTitle = (Subtitle) subTitles.get(0);
            String subTitleString = (String) EADHelper.getClassFromList(subTitle.getContent(),String.class);
            subTitleString = EADHelper.removeTagsFromString(subTitleString);
            System.out.println("subtitle string"+subTitleString);
            EADHelper.setProperty(archDescription,"findingAidSubtitle",subTitleString,Resources.class);               

        }
        
        
        List<Titleproper> titleProper = ((Titlestmt)o).getTitleproper();
        
        for (Titleproper titleproper:titleProper){
        if(titleProper!=null && titleProper.get(0)!=null && titleProper.get(0) instanceof Titleproper){
        //Titleproper titleproper = (Titleproper) titleProper.get(0);
        String type = titleproper.getType();
        
        String titleproperString = EADHelper.ObjectNodetoString(titleproper); 
        
        //String titleproperString = (String) EADHelper.getClassFromList(titleproper.getContent(),String.class);
        //titleproperString = EADHelper.removeTagsFromString(titleproperString);
        if(type !=null && type.equals("filing"))
        	EADHelper.setProperty(archDescription,"findingAidFilingTitle",titleproperString,Resources.class);
        else
        	EADHelper.setProperty(archDescription,"findingAidTitle",titleproperString,Resources.class);
        }
        }
        Author author = ((Titlestmt)o).getAuthor();
        String authorString=null;
        if(author!=null)
            authorString = EADHelper.removeTagsFromString(EADHelper.ObjectNodetoString(author));
            //authorString = (String) EADHelper.getClassFromList(author.getContent(),String.class);
        if(authorString!= null)
            EADHelper.setProperty(archDescription,"author",authorString,Resources.class);
            //((Resources)archDescription).setAuthor(authorString);
          
        Sponsor sponsor = ((Titlestmt)o).getSponsor();
        String sponsorString = null;
        if(sponsor!=null)
        	sponsorString =  EADHelper.removeTagsFromString(EADHelper.ObjectNodetoString(sponsor));
        EADHelper.setProperty(archDescription,"sponsorNote",sponsorString,Resources.class);

    }
    public List getChildren(Object element)
    {
        return null;
    }
}