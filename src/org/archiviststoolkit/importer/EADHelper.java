package org.archiviststoolkit.importer;
import java.io.File;
import java.io.StringWriter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import java.text.Annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import javax.xml.namespace.QName;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.exporter.EADExport;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.BasicNames;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.structure.EAD.ObjectFactory;
import org.archiviststoolkit.structure.FieldNotFoundException;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.StringHelper;
import org.archiviststoolkit.structure.EAD.Archdesc;
import org.archiviststoolkit.structure.EAD.Corpname;
import org.archiviststoolkit.structure.EAD.Ead;
import org.archiviststoolkit.structure.EAD.P;
import org.archiviststoolkit.structure.EAD.*;
import org.archiviststoolkit.structure.StringLengthValidationRequried;

public class EADHelper 
{
	public static final Boolean debug = false;
	public static HashMap indexTypeMap = null;
	public static Object getClassFromList(List list,Class clazz){
        Object o =null;
        if(list==null || list.size()==0){
            return null;
        }    
        else{    
            Iterator it = list.iterator();
            while(it.hasNext()){
                o = it.next();
                if(o instanceof JAXBElement){
                    o=((JAXBElement)o).getValue();
                }
                if(o.getClass().getName() == clazz.getName())
                    return o; 
            }
        }
        return null; 
    }

    public static List getClassesFromList(List list,Class clazz){
        Object o =null;
        List list2 = new ArrayList();
        if(list==null || list.size()==0){
            return null;
        }    
        else{    
            Iterator it = list.iterator();
            while(it.hasNext()){
            	o=it.next();
            	if(o instanceof JAXBElement){
                    o=((JAXBElement)o).getValue();
            	}

            	if(o.getClass().getName() == clazz.getName())
                    list2.add(o); 
            }
        }
        return list2;
    }    
    
    public static void getClassesFromList(List list2,List list,Class clazz){
        Object o =null;
        if(list==null){
            return;
        }    
        else{    
            Iterator it = list.iterator();
            while(it.hasNext()){
            if((o=it.next()).getClass().getName() == clazz.getName())
                list2.add(o); 
            }
        }
    }
    
    public static void getClassesFromListandGroupOthers(List list,Class clazz,List retrievedClass,List notHandled,List handledClasses){
        Object o =null;
        if(list==null || list.size()==0){
            return;
        }    
        else{    
            Iterator it = list.iterator();
            while(it.hasNext()){
            if((o=it.next()).getClass().getName() == clazz.getName())
                retrievedClass.add(o);
            else if (!handledClasses.contains(o.getClass()))
                notHandled.add(o);  
            }
        }
    }

    public static String ObjectNodetoString(Object o){
        try{
            //JAXBContext context = JAXBContext.newInstance("org.archiviststoolkit.structure.EAD");
            if(o==null)return "";
            //if(o instanceof P)
                //parseP((P)o);
            if(o instanceof String)
                return (String)o;
            if(EADInfo.getContext()==null)
                EADInfo.setContext(JAXBContext.newInstance("org.archiviststoolkit.structure.EAD"));  
            JAXBElement jaxbe = new JAXBElement(new QName("","local"),o.getClass(),o);
            //jaxbe.setValue(o);
            StringWriter xml2 = new StringWriter();
            Marshaller m = EADInfo.getContext().createMarshaller();
            m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.FALSE);
            m.marshal(jaxbe,xml2);
            //System.out.println("function: " + xml2.toString());
            return (EADHelper.removeTagsFromString(xml2.toString()).replace("ns3:",""));
            //return (xml2.toString());

            //return (EADHelper.removeTagsFromString(xml2.toString()));
            //return(xml2.toString().substring("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>".length()+1));
        }
        catch (JAXBException je){
            je.printStackTrace();
            return null;
        }
    }

    public static String ObjectNodetoStringWithTags(Object o){
        try{
            //JAXBContext context = JAXBContext.newInstance("org.archiviststoolkit.structure.EAD");
            if(o==null)return "";
            //if(o instanceof P)
                //parseP((P)o);
            if(o instanceof String)
                return (String)o;
            if(EADInfo.getContext()==null)
                EADInfo.setContext(JAXBContext.newInstance("org.archiviststoolkit.structure.EAD"));  
            JAXBElement jaxbe = new JAXBElement(new QName("urn:isbn:1-931666-22-9",(EADInfo.getElementNameFromClassName(o.getClass().getName().toLowerCase()))),o.getClass(),o);
            //jaxbe.setValue(o);
            StringWriter xml2 = new StringWriter();
            Marshaller m = EADInfo.getContext().createMarshaller();
            m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.FALSE);
            m.marshal(jaxbe,xml2);
            //System.out.println(xml2.toString());
            return xml2.toString().replace(" xmlns:ns2=\"http://www.w3.org/1999/xlink\" xmlns=\"urn:isbn:1-931666-22-9\"","").replace("ns2:","");

            //return xml2.toString().replace(" xmlns:ns1=\"http://www.w3.org/1999/xlink\" xmlns:ns2=\"urn:isbn:1-931666-22-9\"","").replace("ns2:","");
            //return (xml2.toString());

            //return (EADHelper.removeTagsFromString(xml2.toString()));
            //return(xml2.toString().substring("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>".length()+1));
        }
        catch (JAXBException je){
            je.printStackTrace();
            return null;
        }
    }    
    public static void parseP(P p){
        //System.out.println("items in P:");
        for (Object o:p.getContent()){
            System.out.println(o.getClass().getName());
            if(o instanceof String)
            System.out.println("String val ="+(String)o);

        }
    }

    public static void main2(String[] args) {
    	ObjectFactory ob = new ObjectFactory();
        try{
        EADInfo.setContext(JAXBContext.newInstance("org.archiviststoolkit.structure.EAD"));

        Object o = 
            EADInfo.getContext().createUnmarshaller().unmarshal(new File("C://ATProject//sampleData//EAD/bellm2V.xml"));
        Ead ead = (Ead)o;
        //Object archDesc = ob.createArchdesc();
        Object archDesc = new Archdesc();
        
        //Object archDesc = ob.createP();
        //Object archDesc = new P();

        //archDesc.getContent().add("hi");
 
        JAXBElement jaxbe = new JAXBElement(new QName("urn:isbn:1-931666-22-9",(EADInfo.getElementNameFromClassName(archDesc.getClass().getName().toLowerCase()))),archDesc.getClass(),archDesc);
        
        
        StringWriter xml2 = new StringWriter();
        Marshaller m = EADInfo.getContext().createMarshaller();
        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
        m.marshal(jaxbe,xml2);
        System.out.println(xml2.toString());
        
        
        }
        catch(JAXBException jaxbe){
            jaxbe.printStackTrace();
        }
        

        
        /*ObjectFactory ob = new ObjectFactory();

        P p = ob.createP();
        p.getContent().add("hi");
        Persname pers = new Persname();
        pers.getContent().add("persname");
        
        p.getContent().add(ob.createPPersname(pers));
        EADHelper ead = new EADHelper();
        System.out.println(EADHelper.ObjectNodetoStringWithTags(p));
        */
        
    }   
    
    public static String ArrayListofNodesToString(ArrayList al){
        if (al==null||al.size()==0)
            return null;
        StringBuffer nodeValue = new StringBuffer();
        Iterator it = null;
        it = al.iterator();
        String str = "";
        while (it.hasNext()){
            Object o = it.next();            
            str = ObjectNodetoString(o);
            nodeValue.append(str);
        }        
        return nodeValue.toString();
    }
    
    public static String removeTagsFromString(String line){
        String temp;
        if (line==null) return "";
        int a,b;
        a = line.indexOf(">");
        if(a<0)
            return line;
        temp = line.substring(a+1);
        b = temp.lastIndexOf("</");
        if (b<0)
            return line;
        temp = temp.substring(0,b);
        //System.out.println("original string = "+line);
        //System.out.println("new string = "+temp);
        return temp;
    }
    
    public static boolean isNullorEmpty(String str)
    {
        if (str==null || str.length()<1)
            return true;
        else return false;
    }

    public static Object getProperty(Object obj,String property){
        try{
        if(obj==null)
            return null;
        Object object = PropertyUtils.getProperty(obj,property);
        return object;
        }
        catch (IllegalAccessException e){
            e.printStackTrace();
            return null;
        }
        catch(InvocationTargetException e){
            e.printStackTrace();
            return null;
        }
        catch(NoSuchMethodException e){
            e.printStackTrace();
            return null;
        }
        
        
    }
    
    public static void setProperty(Object object, String property, String value, boolean cleanupField){
        EADHelper.setProperty(object,property,value,object.getClass(),cleanupField);
    }
    public static void setProperty(Object object, String property, String value){
        EADHelper.setProperty(object,property,value,object.getClass());
    }
    
    public static void setProperty(Object object, String property, String value, Class clazz){
    	setProperty(object, property, value, clazz, true);
    }
    public static void setProperty(Object object, String property, String value, Class clazz, boolean cleanupField){
        if(value==null || object == null || property==null)
            return;
        
        if(cleanupField)
        	value = StringHelper.cleanUpWhiteSpace(value);
        
        if(clazz!=null){
            int strlen = 0;
            strlen = EADHelper.checkFieldLength(clazz, property);
            if(strlen!=-1 && value.length()>strlen){
            	if(EADImportHandler.getTruncations().length()==0)
            		EADImportHandler.addToTruncations("The following string(s) have been truncated upon import:\n");
        		EADImportHandler.addToTruncations("field:"+property+"\n"+"Original String: "+value+"\n"+"Truncated String: "+value.substring(0,strlen)+"\n\n");
            	value = value.substring(0,strlen);
            }
        }
        
        String normValue = "";    
        try{
        if(clazz!=null){
            normValue = LookupListUtils.addItemToListByField(clazz,property,value);
        }
        else {
            normValue = value;
        }
        
        PropertyUtils.setProperty(object,property,normValue);
        }
        catch (IllegalAccessException e){
            System.out.println("IAE");
        }
        catch(InvocationTargetException e){
            System.out.println("ITE");

        }
        catch(NoSuchMethodException e){
            e.printStackTrace();
        }
        catch(FieldNotFoundException e ){
            e.printStackTrace();
            System.out.println("FNFE");
        }
        catch (UnknownLookupListException e){
            System.out.println("ULLE");

        }
    }
    
    public static void setProperty(Object object, String property, Double value){
    	try{
    		PropertyUtils.setProperty(object,property,value);
    	}
        catch (IllegalAccessException e){
            e.printStackTrace();
        }
        catch(InvocationTargetException e){
            e.printStackTrace();

        }
        catch(NoSuchMethodException e){
            e.printStackTrace();
        }

    }

    
    public static int checkFieldLength(Class clazz, String fieldName){

        try{
            Field f = clazz.getDeclaredField(fieldName);
            StringLengthValidationRequried an = f.getAnnotation(StringLengthValidationRequried.class);
            if(an!=null)
                return an.value();
        }
        catch(NoSuchFieldException nsfe){
			if (debug) {
				System.out.println("No field found -- "+ clazz.getName()+"."+fieldName);
			}
			if(clazz.getSuperclass()!=null){
            	try{	
            		Field f = clazz.getSuperclass().getDeclaredField(fieldName);
            		StringLengthValidationRequried an = f.getAnnotation(StringLengthValidationRequried.class);
            		if(an!=null)
            			return an.value();
            	}
            	catch(NoSuchFieldException nsfe2){
					if (debug) {
						System.out.println("No field found -- "+ clazz.getName()+"."+fieldName);
					}
					return -1;
            	}
            }
        }  
        return -1;
    }
    
    public static HashMap getIndexTypeMap(){
    	if(indexTypeMap==null){
    		indexTypeMap = new HashMap();
    		indexTypeMap.put(Corpname.class, "Corporate Name");
    		indexTypeMap.put(Famname.class, "Family Name");
    		indexTypeMap.put(Function.class, "Function");
    		indexTypeMap.put(Genreform.class, "Genre Form");
    		indexTypeMap.put(Geogname.class, "Geographic Name");
    		indexTypeMap.put(Name.class, "Name");
    		indexTypeMap.put(Occupation.class, "Occupation");
    		indexTypeMap.put(Persname.class, "Personal Name");
    		indexTypeMap.put(Subject.class, "Subject");
    		indexTypeMap.put(Title.class, "Title");
    	}
    	return indexTypeMap;
    }
    
    
    
    public static void main (String args[]){
    	System.out.println(checkFieldLength(BasicNames.class,"personalPrimaryName"));
    }
        
        /*public static void main (String args[]){

        System.out.println(checkFieldLength(Resources.class,"resourceIdentifier1"));
        String value = "abcde";
        System.out.println(value.subSequence(0,4));
        }*/
        //ArchDescription a = new Resources();
        //String testString = "�";
        //int len = testString.length();
        //try{
        //PropertyUtils.setProperty(a,"eadFaLocation","eadFaLocation");
        //EADHelper.setProperty(a,"title","here",Resources.class);
        //System.out.println(testString.length());

        //System.out.println(testString.codePointCount(0,len));
        //System.out.println(testString);

        //}
        //catch (IllegalAccessException e){
            
        //}
        //catch(InvocationTargetException e){
            
        //}
        //catch(NoSuchMethodException e){
            
        //}
    
    
    
    
    
    
    
}