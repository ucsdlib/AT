/**
 * Archivists' Toolkit(TM) Copyright © 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
 * All rights reserved.
 *
 * This software is free. You can redistribute it and / or modify it under the terms of the Educational Community License (ECL)
 * version 1.0 (http://www.opensource.org/licenses/ecl1.php)
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the ECL license for more details about permissions and limitations.
 *
 *
 * Archivists' Toolkit(TM)
 * http://www.archiviststoolkit.org
 * info@archiviststoolkit.org
 *
 * Created on July 19, 2005, 11:54 AM
 * @author leemandell
 *
 */

package org.archiviststoolkit.model;

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.mydomain.DomainAccessObject;
import org.archiviststoolkit.mydomain.DomainAccessObjectFactory;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.StringLengthValidationRequried;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.exceptions.DeleteException;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.Collection;

public class Users  extends DomainObject {

    // Names of the Bound Bean Properties *************************************

    public static final int ACCESS_CLASS_SUPERUSER = 5;
    public static final int ACCESS_CLASS_REPOSITORY_MANAGER = 4;
    public static final int ACCESS_CLASS_PROJECT_MANAGER = 3;
    public static final int ACCESS_CLASS_ADVANCED_DATA_ENTRY = 2;
    public static final int ACCESS_CLASS_BEGINNING_DATA_ENTRY = 1;
    public static final int ACCESS_CLASS_REFERENCE_STAFF = 0;

    public static final String PROPERTYNAME_USERNAME    = "userName";
    public static final String PROPERTYNAME_PASSWORD = "password";
    public static final String PROPERTYNAME_FULL_NAME  = "fullName";
    public static final String PROPERTYNAME_TITLE    = "title";
    public static final String PROPERTYNAME_DEPARTMENT    = "department";
    public static final String PROPERTYNAME_ACCESS_CLASS    = "accessClass";
    public static final String PROPERTYNAME_REPOSITORY    = "repository";
    public static final String PROPERTYNAME_EMAIL    = "email";

    public static final String USERNAME_DEVELOPER    = "developer";

    @IncludeInApplicationConfiguration
    @ExcludeFromDefaultValues
    private Long UserId;

    @IncludeInApplicationConfiguration(1)
    @ExcludeFromDefaultValues
	@StringLengthValidationRequried
    private String userName = "";

    private byte[] password;

    @IncludeInApplicationConfiguration(2)
    @ExcludeFromDefaultValues
	@StringLengthValidationRequried
    private String fullName = "";

    @IncludeInApplicationConfiguration(3)
    @ExcludeFromDefaultValues
	@StringLengthValidationRequried
    private String title = "";

    @IncludeInApplicationConfiguration(4)
	@StringLengthValidationRequried
    private String department = "";

    @IncludeInApplicationConfiguration
    @ExcludeFromDefaultValues
	@StringLengthValidationRequried
    private String email = "";

    @IncludeInApplicationConfiguration
    @ExcludeFromDefaultValues
    private Integer accessClass = 1;

	//a place to save the original value of the accessClass
	private Integer oldAccessClass;

	@IncludeInApplicationConfiguration(5)
    @ExcludeFromDefaultValues
    private Repositories repository;

    /** Creates a new instance of Users */
    public Users() {
        if (ApplicationFrame.getInstance().getCurrentUser() != null) {
            repository = ApplicationFrame.getInstance().getCurrentUserRepository();
        }
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        this.UserId = userId;
    }

    public String getUserName() {
		if (this.userName != null) {
			return this.userName;
		} else {
			return "";
		}
	}

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
		if (this.fullName != null) {
			return this.fullName;
		} else {
			return "";
		}
	}

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTitle() {
		if (this.title != null) {
			return this.title;
		} else {
			return "";
		}
	}

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getIdentifier() {
        return this.getUserId();
    }

    public void setIdentifier(Long identifier) {
        this.setUserId(identifier);
    }

	public String toString() {
        return getUserName();
    }

    //A place holder to determine if an object is safe to delete. To be
//overridden if logic is necessary.
    public void testDeleteRules() throws DeleteException, PersistenceException {
        //if the user is a superuser make sure they are not the only one.
        if (getAccessClass() == ACCESS_CLASS_SUPERUSER) {
            DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Users.class);
            int count = access.getCountBasedOnPropertyValue(Users.PROPERTYNAME_ACCESS_CLASS, Users.ACCESS_CLASS_SUPERUSER);
            if (count <= 1) {
                throw new DeleteException("This is the last superuser account and can not be deleted");
            }
        }
    }

    public static final byte[] hashString(char[] input) throws NoSuchAlgorithmException {
        String temp = new String(input);
        return hashBytes(temp.getBytes());
        }

    public static final byte[] hashString(String input) throws NoSuchAlgorithmException {
        return hashBytes(input.getBytes());
        }

    public static final byte[] hashBytes(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return md.digest(input);
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public Repositories getRepository() {
        return repository;
    }

    public void setRepository(Repositories repository) {
        this.repository = repository;
    }

    public String getDepartment() {
		if (this.department != null) {
			return this.department;
		} else {
			return "";
		}
	}

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getAccessClass() {
        return accessClass;
    }

    public void setAccessClass(Integer accessClass) {
        this.accessClass = accessClass;
    }


    public String getEmail() {
		if (this.email != null) {
			return this.email;
		} else {
			return "";
		}
	}

    public void setEmail(String email) {
        this.email = email;
    }

	//todo move to utility class
	public static boolean doesCurrentUserHaveAccess(Integer level) {
		return doesUserHaveAccess(ApplicationFrame.getInstance().getCurrentUser(), level);
    }

	public static boolean doesUserHaveAccess(Users user, Integer level) {
        if (user.getAccessClass() >= level) {
            return true;
        } else {
            return false;
        }
    }

    public static Users lookupUser(String userName) {
        return lookupUser(userName, null, false);
    }

    public static Users lookupUser(String userName, byte[] password) {
        return lookupUser(userName, password, true);
    }

    /**
     * Method to return a user object from the database.
     * If use passPassword is true the the password is compared
     * if false then it's not. Setting this to false is useful
     * when a authentication plugin is used.
     * 
     * @param userName
     * @param password
     * @param usePassword
     * @return
     */
    public static Users lookupUser(String userName, byte[] password, boolean usePassword) {
        Session session = SessionFactory.getInstance().openSession();
        Criteria crit;
        Collection collection;

        crit = session.createCriteria(Users.class);
        crit.add(Expression.eq("userName", userName));

        if(usePassword) {
            crit.add(Expression.eq("password", password));
        }

        collection = crit.list();
        if (collection.size() == 1) {
            return (Users) collection.iterator().next();
        } else {
            return null;
        }
    }


	public Integer getOldAccessClass() {
		return oldAccessClass;
	}

	public void setOldAccessClass(Integer oldAccessClass) {
		this.oldAccessClass = oldAccessClass;
	}
}
