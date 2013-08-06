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
 * Date: Jan 23, 2007
 * Time: 10:34:01 AM
 */

package org.archiviststoolkit.maintenance;

import org.netbeans.spi.wizard.WizardPanelProvider;
import org.netbeans.spi.wizard.WizardController;

import javax.swing.*;
import java.util.Map;

class InitialSteps extends WizardPanelProvider {

	/**
	 * Creates a new instance of InitialSteps
	 */
	InitialSteps () {
		super( "AT Maintenance Program", new String[] { "Select operation"},
			new String[] { "Select operation"} );
	}

	protected JComponent createPanel (final WizardController controller,
									  final String id, final Map data) {

		switch ( indexOfStep( id ) ) {

			case 0 :
				return new ChooseOperation ();

			default :
				throw new IllegalArgumentException ( id );
		}
	}
}
