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
 * @author Lee Mandell
 * Date: Mar 18, 2006
 * Time: 5:34:03 PM
 */

package org.archiviststoolkit.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;
import java.lang.reflect.Method;

public class ATBeanUtils {

	public static Class getPropertyType(Class clazz, String propertyName) throws IntrospectionException {
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor pd: propertyDescriptors) {
			if (pd.getName().equalsIgnoreCase(propertyName)) {
				return pd.getPropertyType();
			}
		}

		return null;
	}

	public static Method getReadMethod(Class clazz, String propertyName) throws IntrospectionException {
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor pd: propertyDescriptors) {
			if (pd.getName().equalsIgnoreCase(propertyName)) {
				return pd.getReadMethod();
			}
		}

		return null;
	}

	public static Method getWriteMethod(Class clazz, String propertyName) throws IntrospectionException {
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor pd: propertyDescriptors) {
			if (pd.getName().equalsIgnoreCase(propertyName)) {
				return pd.getWriteMethod();
			}
		}

		return null;
	}

}
