package org.archiviststoolkit.swing.jTreeDnD;

import org.archiviststoolkit.exceptions.ObjectNotRemovedException;

import java.util.Set;

/**
 * Archivists' Toolkit(TM) Copyright © 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
 * All rights reserved.
 * <p/>
 * This software is free. You can redistribute it and / or modify it under the terms of the Educational Community License (ECL)
 * version 1.0 (http://www.opensource.org/licenses/ecl1.php)
 * <p/>
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the ECL license for more details about permissions and limitations.
 * <p/>
 * <p/>
 * Archivists' Toolkit(TM)
 * http://www.archiviststoolkit.org
 * info@archiviststoolkit.org
 * <p/>
 * Created by IntelliJ IDEA.
 * User: Nathan Stevens
 * Date: Aug 14, 2008
 * Time: 10:05:46 AM
 * To change this template use File | Settings | File Templates.
 */

public interface treeDnDSequencedObject {
  // Method to get the parent
  public treeDnDSequencedObject getParentObject();
  // Method to set the parent
  public void setParentObject(treeDnDSequencedObject parent);

  // Merthod to add a child
  public void addChild(treeDnDSequencedObject child);

  // Method to remove a child
  public void removeChild(treeDnDSequencedObject child) throws ObjectNotRemovedException;

  // return the children of this sequence object
  public Set getChildren();

  // Method to get the sequence number
  public Integer getSequenceNumber();

	public void setSequenceNumber(Integer sequenceNumber);
}