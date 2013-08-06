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

//==============================================================================    
// Import Declarations
//==============================================================================    

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.event.EventListenerList;
import java.awt.event.ActionListener;

/**
 * The Concrete action is used to manage delegate actions.
 * 
 */

public class ConcreteAction extends AbstractAction {

    /** Map of listerners who are interested in this action. */
    
	private EventListenerList actionListeners = null;
    
	/**
     * Class constructor adds default values into the Action.
     * @param name the name of the action
      */
    
    public ConcreteAction (final String name) {
        putValue(Action.SHORT_DESCRIPTION, name);
        putValue(Action.NAME, name);
    }
    
    /**
     * sets the icon for the action.
     * @param icon the icon to set
     */
    
    public final void setIcon (final ImageIcon icon) {
    	putValue(Action.SMALL_ICON, icon);
    }
    
    /**
     * sets the description on the action.
     * @param name the text to assign to the description
     */
    public final void setDescription (final String name) {
    	putValue(Action.SHORT_DESCRIPTION,name);
    	putValue(Action.NAME,name);
    }

	/**
	 * Add an listener to this action.
	 * @param listener to be added
	 */
	
	public final void addActionListener (final ActionListener listener) {
		if (actionListeners == null) {
			actionListeners = new EventListenerList();
		}
		actionListeners.add(ActionListener.class, listener);
	}

	/**
	 * Remove the listener.
	 * @param listener to be removed
	 */
	
	public final void removeActionListener(final ActionListener listener) {
		if (actionListeners == null) {
			return;
		}
		actionListeners.remove(ActionListener.class, listener);
	}
	
    /**
     * delegates the actions back out to the specific component.
     * @param actionEvent the event that needs to be handled
     */
    
    public final void actionPerformed(final ActionEvent actionEvent)  {

		if (actionListeners != null) {
			Object[] listenerList = actionListeners.getListenerList();
			actionEvent.setSource (this);
			for (int loop = 0; loop <= listenerList.length - 2; loop += 2) {
				((ActionListener) listenerList[loop + 1]).actionPerformed(actionEvent);
			}
		}
    }
}