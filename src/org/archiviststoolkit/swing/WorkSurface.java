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


package org.archiviststoolkit.swing;

import javax.swing.Icon;
import javax.swing.JComponent;

//==============================================================================
// Import Declarations
//==============================================================================

/**
 * The worksurface is the base of all worksurface types within the Swing
 * based GUI. Implement this if you want to extend the interface.
 */

public interface WorkSurface {
	
	/**
	 * Get the name of the worksurface.
	 * @return the worksurface name
	 */
	
    String getName ();
    
    /**
     * Get the icon for this worksurface.
     * @return the icon
     */
    
    Icon getIcon ();
    
    /**
     * Get the tooltip for this worksurface. 
     * @return the tooltip string
     */
    
    String getTooltip ();
    
    /**
     * Get the worksurface component.
     * @return the component representing the worksuface
     */
    
    JComponent getComponent ();
}