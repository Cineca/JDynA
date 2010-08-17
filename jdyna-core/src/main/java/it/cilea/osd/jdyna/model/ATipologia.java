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
	
	/** nome della tipologia di oggetto **/
	@Field(index=Index.TOKENIZED)
	private String nome;
	
	/** descrizione della tipologia di progetto */
	private String descrizione;

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * Restituisce la lista delle tipologie di proprieta della classe prevista per l'oggetto non
	 * applicabili alla sua particolare tipologia.
	 * 
	 * @author bollini
	 * @see EpiObject#getClassPropertiesDefinition()
	 * @return la lista delle tipologie di proprieta non applicabili all'oggetto
	 */
	@Transient
	public abstract List<TP> getMaschera();

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * Adapter per l'interfaccia {@link Selectable}. Restituisce il nome della tipologia
	 * 
	 * @author bollini
	 * @return {@link ATipologia#getNome()}  
	 */
	public String getDisplayValue() {
		return nome;
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
			if ((tipologia2.getNome() != null && tipologia2.getNome().equals(nome))
					|| ((nome == null && tipologia2.getNome() == null))) {
				return true;
			} else
				return super.equals(object);
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		if(getNome()!=null){
			return getNome().hashCode();
		}
		else {
			return super.hashCode();
		}
	}
}
