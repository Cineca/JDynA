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
import it.cilea.osd.common.model.Selectable;

import java.util.List;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;

@MappedSuperclass
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public abstract class ATipologia<TP extends PropertiesDefinition> extends IdentifiableObject implements Selectable {
	
	/** shortName **/
	@Field(index=Index.TOKENIZED)
	private String shortName;
	
	/** label della tipologia di progetto */
	private String label;

	public String getLabel() {
		return label;
	}

	public void setLabel(String descrizione) {
		this.label = descrizione;
	}

	/**
	 * Restituisce la lista delle tipologie di proprieta della classe prevista per l'oggetto applicabili alla sua particolare tipologia.
	 * 
	 * @author bollini
	 * 
	 * @return la lista delle tipologie di proprieta applicabili all'oggetto
	 */
	@Transient
	public abstract List<TP> getMask();

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String nome) {
		this.shortName = nome;
	}
	
	/**
	 * Adapter per l'interfaccia {@link Selectable}. Restituisce il shortName della tipologia
	 * 
	 * @author bollini
	 * @return {@link ATipologia#getShortName()}  
	 */
	public String getDisplayValue() {
		return shortName;
	}
	
	/**
	 * Adapter per l'interfaccia {@link Selectable}. Restituisce l'id della tipologia
	 * 
	 * @author bollini
	 * @return {@link ATipologia#getId()}  
	 */
	public String getIdentifyingValue() {
		return getId().toString();
	}
	
	@Override
	public boolean equals(Object object) {
		try {
			ATipologia<TP> tipologia2 = (ATipologia<TP>) object;
			if ((tipologia2.getShortName() != null && tipologia2.getShortName().equals(shortName))
					|| ((shortName == null && tipologia2.getShortName() == null))) {
				return true;
			} else
				return super.equals(object);
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		if(getShortName()!=null){
			return getShortName().hashCode();
		}
		else {
			return super.hashCode();
		}
	}
}
