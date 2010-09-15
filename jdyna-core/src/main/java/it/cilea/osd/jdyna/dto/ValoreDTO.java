/*
 * JDynA, Dynamic Metadata Management for Java Domain Object
 *
 * Copyright (c) 2008, CILEA and third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by CILEA.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License v3 or any later version, as published 
 * by the Free Software Foundation, Inc. <http://fsf.org/>.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
package it.cilea.osd.jdyna.dto;

import java.io.Serializable;

public class ValoreDTO implements Serializable {
	/** The real contents entered/showed by the UI */
	private Object object;
	private Boolean visibility;
	
	public ValoreDTO() {
	}
	
	public ValoreDTO(Object object) {
		super();
		this.object = object;
		this.setVisibility(false);
	}

	
	public ValoreDTO(Object object, Boolean visibility) {
		super();
		this.object = object;
		this.setVisibility(visibility);
	}

	public Object getObject() {
		return object;
	}

	void setObject(Object object) {
		this.object = object;
	}
	
	@Override
	public String toString() {
		if (object != null){
			return object.toString();
		} else {
			//FIXME attenzione "al primo giro" su dyna viene chiamato il toString del valore...
			return "";
		}
	}

	public void setVisibility(Boolean visibility) {
		this.visibility = visibility==null?false:visibility;		
	}

	public Boolean getVisibility() {
		if(this.visibility==null) {
			return false;
		}
		return visibility;
	}
}
