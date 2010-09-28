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
package it.cilea.osd.jdyna.widget;

import it.cilea.osd.jdyna.editor.LinkPropertyEditor;
import it.cilea.osd.jdyna.model.AValue;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.util.ValidationMessage;
import it.cilea.osd.jdyna.value.LinkValue;

import java.beans.PropertyEditor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/** 
 * 
 * Link widget
 * 
 * 
 * @author pascarelli
 *
 */
@Entity
@Table(name="jdyna_widget_link")
public class WidgetLink extends AWidget {

	@Transient
	private String triview;
	
	private int size = 30;
	
	public String getTriview() {		
		return "link";
	}
	
	@Override
	/**
	 * Restituisce lo StringTrimmer editor configurato per la conversione delle stringhe vuote in null
	 */
	public PropertyEditor getPropertyEditor(IPersistenceDynaService applicationService) {	
		return new LinkPropertyEditor(applicationService);
	}
	

	@Override
	public AValue getInstanceValore() {		
		return new LinkValue();
	}

	@Override
	public Class<? extends AValue> getValoreClass() {
		return LinkValue.class;
	}

	@Override
	public ValidationMessage valida(Object valore) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}


    
}
