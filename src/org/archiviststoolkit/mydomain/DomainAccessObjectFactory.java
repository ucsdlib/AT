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
 */

package org.archiviststoolkit.mydomain;

//==============================================================================
// Import Declarations
//==============================================================================

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.model.*;

/**
 * Singleton factory which generates instances of DomainAccess objects.
 * TODO: add static helpers which do a getInstance().add type pattern
 * todo add support for classes that extend DomainAccessObjectImpl so that all dao requests can go through the factory
 */

public final class DomainAccessObjectFactory {

	/**
	 * internal factory singleton.
	 */

	private static DomainAccessObjectFactory singleton = null;

	/**
	 * cache for constructed domain access objects.
	 */

	private HashMap accessMap = new HashMap();

	/**
	 * Private constructor which grabs all persistent classes and
	 * builds a DomainAccessObject for them.
	 *
	 * @throws PersistenceException fatal persistence issue has occurred
	 */

	private DomainAccessObjectFactory() throws PersistenceException {
		Session session = SessionFactory.getInstance().openSession();

		try {

			Map classMap = SessionFactory.getSessionFactory().getAllClassMetadata();

			Iterator iterator = classMap.keySet().iterator();

			while (iterator.hasNext()) {
				//JoinedSubclassEntityPersister jp = (JoinedSubclassEntityPersister) iterator.next();
				//String tempString = (String) iterator.next();
				Class currentClass = Class.forName((String) iterator.next());
				if (currentClass == Resources.class) {
					accessMap.put(currentClass, new ResourcesDAO());
				} else if (currentClass == DigitalObjects.class) {
                    accessMap.put(currentClass, new DigitalObjectDAO());
                } else {
					accessMap.put(currentClass, new DomainAccessObjectImpl(currentClass));
				}
			}

			//==========================================
			// Now patch up the listeners, so everyone
			// knows whats going on
			//==========================================

//            iterator = classMap.keySet().iterator();
//            
//            while (iterator.hasNext()) {
//                Class currentClass = (Class)iterator.next();
//                ClassMetadata metaData = SessionFactory.getSessionFactory().getClassMetadata(currentClass);
//                
//                Type [] types = metaData.getPropertyTypes();
//                
//                for (int typeLoop = 0; typeLoop < types.length; typeLoop ++) {
//                    if (types[typeLoop] instanceof PersistentCollectionType) {
//                        PersistentCollectionType internalType = (PersistentCollectionType)types[typeLoop];
//                        DomainAccessObjectImpl impl = (DomainAccessObjectImpl) accessMap.get(currentClass);
//                        
//                        // Dont bind listener if its a self referential relationship. This will loop forever.
//                        
//                        if (!currentClass.equals(internalType.getAssociatedClass((SessionFactoryImplementor)SessionFactory.getSessionFactory()))) {
//                            impl.addListener((DomainAccessObjectImpl) accessMap.get(internalType.getAssociatedClass((SessionFactoryImplementor)SessionFactory.getSessionFactory())));
//                        }
//                    }/* else if (types[typeLoop] instanceof EntityType) {
//                                                EntityType internalType = (EntityType) types[typeLoop];
//                                                DomainAccessObjectImpl impl = (DomainAccessObjectImpl) accessMap.get(currentClass);
//                                                impl.addListener((DomainAccessObjectImpl) accessMap.get(internalType.getAssociatedClass()));
//                                        } */
//                }
//            }

			session.flush();

			session.connection().commit();

		} catch (HibernateException hibernateException) {
			throw new PersistenceException("failed to add " + hibernateException.toString(), hibernateException);
		} catch (Exception exception) {
			throw new PersistenceException("failed " + exception.toString());
		}

		SessionFactory.getInstance().closeSession(session);
	}

	/**
	 * Standard factory pattern get instance method.
	 *
	 * @return the singleton instance
	 * @throws PersistenceException a fatal persistence exception has occurred
	 */

	public synchronized static DomainAccessObjectFactory getInstance() throws PersistenceException {
		if (singleton == null) {
			singleton = new DomainAccessObjectFactory();
		}

		return (singleton);
	}

	/**
	 * Looks up the class in the cache and pulls out the appropriate DAO.
	 *
	 * @param clazz to return a Domain access object for
	 * @return the appropriate domain access object
	 */

	public DomainAccessObject getDomainAccessObject(final Class clazz) {
		return ((DomainAccessObject) accessMap.get(clazz));
	}
}

//=============================================================================
// $Log: DomainAccessObjectFactory.java,v $
// Revision 1.4  2004/12/23 23:36:32  lejames
// Reworked the model to contain a full list of fields
//
// Revision 1.3  2004/12/15 02:11:37  lejames
// added the opportunity model
//
// Revision 1.2  2004/11/29 03:35:55  lejames
// Part of the updates for version 1.1.1, this time we have cleaned up the
// code and removed all checkstyle issues
//
// Revision 1.1  2004/11/26 17:51:33  lejames
// Completion of roadmap to 1.1.0 including additional maven report plugins,
// hibernate and hsql integration, DOA layer and basic contact management
//
//
//=============================================================================
// EOF
//=============================================================================
