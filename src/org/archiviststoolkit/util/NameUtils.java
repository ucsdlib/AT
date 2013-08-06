package org.archiviststoolkit.util;

import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.ArchDescriptionNames;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.mydomain.NamesDAO;
import org.archiviststoolkit.mydomain.PersistenceException;

import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

public class NameUtils {
    public static void addName(ArchDescription archDescription, Names name, String nameRole) throws PersistenceException, UnknownLookupListException, DuplicateLinkException {
        NamesDAO nameDao = new NamesDAO();
        ArchDescriptionNames archDescriptionName;

        if (name.getNameSource().length() == 0) {
            name.setNameSource("ingest");
        }
        try {
            NameUtils.setMd5Hash(name);

            name = nameDao.lookupName(name, true);
            archDescriptionName = new ArchDescriptionNames(name, archDescription);
            archDescriptionName.setNameLinkFunction(nameRole);

            archDescription.addName(archDescriptionName);
        } catch (DuplicateLinkException e) {
            e.printStackTrace();
            throw e;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            //throw e;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            //throw e;
        }
    }

    // Method to generate and set the md5 hash for a Names object. It should
    // also be call every time the update is called
    public static String setMd5Hash(Names name) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        StringBuffer textBuffer = new StringBuffer();
        String md5hash = null;

        // must append numbers so that empty spaces are accounted for and should lead to creation of unique hash
        textBuffer.append(name.getNameType()).append(0);
        textBuffer.append(name.getSortName()).append(1);
        textBuffer.append(name.getNumber()).append(2);
        textBuffer.append(name.getQualifier()).append(3);
        textBuffer.append(name.getCorporatePrimaryName()).append(4);
        textBuffer.append(name.getCorporateSubordinate1()).append(5);
        textBuffer.append(name.getCorporateSubordinate2()).append(6);
        textBuffer.append(name.getPersonalPrimaryName()).append(7);
        textBuffer.append(name.getPersonalRestOfName()).append(8);
        textBuffer.append(name.getPersonalPrefix()).append(9);
        textBuffer.append(name.getPersonalSuffix()).append(10);
        textBuffer.append(name.getPersonalDates()).append(11);
        textBuffer.append(name.getPersonalFullerForm()).append(12);
        textBuffer.append(name.getPersonalTitle()).append(13);
        textBuffer.append(name.getFamilyName()).append(14);
        textBuffer.append(name.getFamilyNamePrefix()).append(15);

        String text = textBuffer.toString();
        md5hash = StringHelper.MD5(text);
        name.setMd5Hash(md5hash);

        return md5hash;
    }


    /**
     * Method to remove any dates or other number from the name that is provided.
     * Its assumes that the name are dilimited by "," so it not a very flexible
     * way of doing this.
     *
     * @param name The name to be cleaned up
     * @return The name with anything other than the name returned
     */
    public static String getCleanName(String name) {
        String cleanName = "";
        if(name.indexOf(",") != -1) {
            String[] sa = name.split("\\s*,\\s*");

            for(String namePart: sa) {
                // check to make sure this is not a date or any other number
                if(namePart.matches("\\D+")) {
                    if (cleanName.length() == 0) {
                        cleanName += namePart;
                    } else {
                        cleanName += ", " + namePart;
                    }
                }
            }
        } else {
            cleanName = name;
        }

        return cleanName;
    }
}
