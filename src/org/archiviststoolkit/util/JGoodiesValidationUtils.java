/*
 * Copyright (c) 2003-2006 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.archiviststoolkit.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.jgoodies.validation.ValidationResult;


/**
 * Consists only of static convenience methods used throughout
 * the examples of the JGoodies Validation tutorial.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.6 $
 */
public final class JGoodiesValidationUtils {

    private static ValidationResult errorsResult;
    private static ValidationResult warningsResult;
    private static ValidationResult mixedResult;


    private JGoodiesValidationUtils() {
        // Suppresses default constructor, ensuring non-instantiability.
    }


    // Misc *******************************************************************

    /**
     * Returns a listener that writes bean property changes to the console.
     * The log entry includes the PropertyChangeEvent's source, property name,
     * old value, and new value.
     *
     * @return a debug listener that logs bean changes to the console
     */
    public static PropertyChangeListener createDebugPropertyChangeListener() {
        PropertyChangeListener listener = new DebugPropertyChangeListener();
        debugListeners.add(listener);
        return listener;
    }


    /**
     * Creates and returns an Action that exists the system if performed.
     *
     * @return an Action that exists the system if performed
     *
     * @see System#exit(int)
     */
    public static Action getCloseAction() {
        return new CloseAction();
    }


    // Example Validation Results *********************************************

    /**
     * Lazily creates and returns an example ValidationResult
     * that contains only errors.
     *
     * @return a ValidationResult that contains only errors
     */
    public static ValidationResult getErrorsResult() {
        if (errorsResult == null) {
            errorsResult = new ValidationResult();
            errorsResult.addError("The order no is mandatory.");
            errorsResult.addError("The order date is mandatory.");
        }
        return errorsResult;
    }


    /**
     * Lazily creates and returns an example ValidationResult
     * that contains errors and warnings.
     *
     * @return a ValidationResult that contains errors and warnings
     */
    public static ValidationResult getMixedResult() {
        if (mixedResult == null) {
            mixedResult = new ValidationResult();
            mixedResult.addWarning("The order no length shall be in [5, 10].");
            mixedResult.addError("The order date is mandatory.");
        }
        return mixedResult;
    }


    /**
     * Lazily creates and returns an example ValidationResult
     * that contains only warnings.
     *
     * @return a ValidationResult that contains only warnings
     */
    public static ValidationResult getWarningsResult() {
        if (warningsResult == null) {
            warningsResult = new ValidationResult();
            warningsResult.addWarning("The order no length shall be in [5, 10].");
            warningsResult.addWarning("The order date shall be in the future.");
        }
        return warningsResult;
    }


    // Screen Position ********************************************************

    /**
     * Locates the given component on the screen's center.
     *
     * @param component   the component to be centered
     */
    public static void locateOnScreenCenter(Component component) {
        Dimension paneSize = component.getSize();
        Dimension screenSize = component.getToolkit().getScreenSize();
        component.setLocation(
            (screenSize.width  - paneSize.width)  / 2,
            (screenSize.height - paneSize.height) / 2);
    }


    /**
     * Opens a message dialog that shows the validation result,
     * things the user must fix, before the selection can change.
     *
     * @param headerText        the text displayed above the validation message
     * @param validationResult  the validation result to be displayed
     *
     * @throws IllegalArgumentException if the validation result is empty
     */
    public static void showValidationMessage(
            EventObject e,
            String headerText,
            ValidationResult validationResult) {
        if (validationResult.isEmpty())
            throw new IllegalArgumentException("The validation result must not be empty.");

        Object eventSource = e.getSource();
        Component parent = null;
        if (eventSource instanceof Component) {
            parent = SwingUtilities.windowForComponent((Component) eventSource);
        }
		showValidationMessage(parent, headerText, validationResult);
	}

	public static void showValidationMessage(Component parent, String headerText, ValidationResult validationResult) {

		if (validationResult.isEmpty())
			throw new IllegalArgumentException("The validation result must not be empty.");

		boolean error = validationResult.hasErrors();
		String messageText = headerText + "\n\n" + validationResult.getMessagesText() + "\n\n";
		String titleText   = "Validation " + (error ? "Error" : "Warning");
		int messageType    = error ? JOptionPane.ERROR_MESSAGE : JOptionPane.WARNING_MESSAGE;
		JOptionPane.showMessageDialog(parent, messageText, titleText, messageType);
	}

	// Debug Listener *********************************************************

    /**
     * Used to hold debug listeners, so they won't be removed by
     * the garbage collector, even if registered by a listener list
     * that is based on weak references.
     *
     * @see #createDebugPropertyChangeListener()
     * @see WeakReference
     */
    private static List debugListeners = new LinkedList();


    /**
     * Writes the source, property name, old/new value to the system console.
     */
    private static class DebugPropertyChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            System.out.println();
            System.out.println("The source: " + evt.getSource());
            System.out.println(
                    "changed '" + evt.getPropertyName()
                  + "' from '" + evt.getOldValue()
                  + "' to '" + evt.getNewValue() + "'.");
        }
    }


    // Actions ****************************************************************

    /**
     * An Action that exists the System.
     */
    private static final class CloseAction extends AbstractAction {

        private CloseAction() {
            super("Close");
        }

        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }


}
