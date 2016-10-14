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
package it.cilea.osd.jdyna.model;

import it.cilea.osd.common.model.IdentifiableObject;
import it.cilea.osd.common.util.Utils;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.Index;

@Entity
@Table(name="jdyna_values")
@Inheritance (strategy = InheritanceType.SINGLE_TABLE)
@org.hibernate.annotations.Table(appliesTo="jdyna_values", indexes={@Index(name="jdyna_values_dtype_idx", columnNames={"dtype"})})
public abstract class AValue<P> extends IdentifiableObject {
    
    @Transient
    protected Log log = LogFactory.getLog(getClass());
    
	@Id
	//@GeneratedValue
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JDYNA_VALUES_SEQ")
    @SequenceGenerator(name = "JDYNA_VALUES_SEQ", sequenceName = "JDYNA_VALUES_SEQ", allocationSize = 1)
	private Integer id;	
	
	@Basic
	protected String sortValue;

	@Transient
	private boolean modified;
	
	@Transient
	public abstract P getObject();
	
	@Deprecated
	@Transient
	public P getReal(){
		return getObject();
	}

	public void setOggetto(P oggetto){
		modified = Utils.equals(getObject(), oggetto);
		setReal(oggetto);
	}

	/**
	 * Questo metodo e' esclusivamente ad uso interno e va implementato dalle
	 * sottoclassi per gestire correttamente la persistenza. Non utilizzare per altri casi d'uso.
	 * Per settare il valore wrappato dalla classe utilizzare il metodo {@link #setOggetto(Object)}
	 */
	protected abstract void setReal(P oggetto);
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	/** Ritorna il valore di default */
	@Transient
	public abstract P getDefaultValue();
	
	public String getSortValue(){
		return sortValue;
	}
	
	public abstract String[] getUntokenizedValue();
	
	public abstract String[] getTokenizedValue();
	
	public boolean isModified() {		
		return modified;
	}
	
	@Override
	public String toString()
	{	 
	    if(getObject()!=null) {
	        return getObject().toString();
	    }
	    return "";
	}
}
