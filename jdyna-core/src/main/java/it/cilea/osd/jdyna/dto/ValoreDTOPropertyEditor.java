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

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;

public class ValoreDTOPropertyEditor extends PropertyEditorSupport{
	private PropertyEditor internalPropertyEditor;

	public ValoreDTOPropertyEditor(PropertyEditor internalPropertyEditor) {
		super();
		this.internalPropertyEditor = internalPropertyEditor;
	}
	
	@Override
	public void setAsText(String arg0) throws IllegalArgumentException {
		ValoreDTO valoreDTO = new ValoreDTO();
		internalPropertyEditor.setAsText(arg0);
		valoreDTO.setObject(internalPropertyEditor.getValue());
		setValue(valoreDTO);
	}
	
	@Override
	public String getAsText() {	
		return internalPropertyEditor.getAsText();
	}
	
	@Override
	public void setValue(Object arg0) {
		super.setValue(arg0);
		ValoreDTO valoreDTO = (ValoreDTO) arg0;
		internalPropertyEditor.setValue(valoreDTO==null?null:valoreDTO.getObject());
	}	
}
