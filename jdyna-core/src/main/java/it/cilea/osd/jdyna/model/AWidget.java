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
package it.cilea.osd.jdyna.model;

import it.cilea.osd.common.model.IdentifiableObject;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.util.ValidationMessage;

import java.beans.PropertyEditor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/** Classe che rappresenta un Widget cioè una 
 *  tipologia di valore gestibile dal sistema
 *  
 * @author biondo,pascarelli
 *
 */

@Entity
@Inheritance (strategy = InheritanceType.TABLE_PER_CLASS)
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public abstract class AWidget<AV extends AValue> extends IdentifiableObject {
	@Transient
	protected Log log = LogFactory.getLog(getClass());
	
    /**Chiave primaria di accesso*/
	@Id	
	@GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;
	
	@Deprecated
	@Transient	
	public String getConfiguration() {
		return "";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Transient
	public boolean isNull(Object valore) {
		return valore == null;
	}

	@Transient
	public abstract String getTriview();

	public String toHTML(Object valore) {
		return toString(valore);
	}

	public String toString(Object valore) {		
		if(valore==null) {
			return "";
		}
		//Passiamo un application service null ma il property editor lo usa solo per il passaggio da text -> object e non viceversa
		PropertyEditor propertyEditor = getPropertyEditor(null);
		if (propertyEditor != null) {
			propertyEditor.setValue(valore);
			return propertyEditor.getAsText();
		} else {
			return valore.toString();
		}
		//throw new UnsupportedOperationException();
	}
	
	@Transient
	public abstract AV getInstanceValore();
	
	@Transient
	public abstract PropertyEditor getPropertyEditor(IPersistenceDynaService applicationService);

	@Transient
	public abstract Class<AV> getValoreClass();

	@Transient
	public abstract ValidationMessage valida(Object valore);
	
	@Transient
	public PropertyEditor getImportPropertyEditor(IPersistenceDynaService applicationService) {
		return getPropertyEditor(applicationService);
	}	

}