/*
 * JDynA, Dynamic Metadata Management for Java Domain Object
 * 
 *  Copyright (c) 2008, CILEA and third-party contributors as
 *  indicated by the @author tags or express copyright attribution
 *  statements applied by the authors.  All third-party contributions are
 *  distributed under license by CILEA.
 * 
 *  This copyrighted material is made available to anyone wishing to use, modify,
 *  copy, or redistribute it subject to the terms and conditions of the GNU
 *  Lesser General Public License v3 or any later version, as published 
 *  by the Free Software Foundation, Inc. <http://fsf.org/>.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 *  for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this distribution; if not, write to:
 *   Free Software Foundation, Inc.
 *   51 Franklin Street, Fifth Floor
 *   Boston, MA  02110-1301  USA
 */
package it.cilea.osd.jdyna.utils;

import it.cilea.osd.common.model.Selectable;

import java.io.Serializable;

import javax.persistence.Transient;

public class SimpleSelectableObject implements Selectable, Serializable {
	@Transient
	private String identifyingValue;
	@Transient	
	private String displayValue;
	
	public SimpleSelectableObject() {
	}
	
	public SimpleSelectableObject(String identifyingValue, String displayValue) {
		this.identifyingValue = identifyingValue;
		this.displayValue = displayValue;
	}

	public String getIdentifyingValue() {
		if (identifyingValue == null)
			return null;
		return String.valueOf(identifyingValue);
	}
		
	public String getDisplayValue() {
		return displayValue;
	}

	public void setIdentifyingValue(String identifyingValue) {
		this.identifyingValue = identifyingValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}
}
