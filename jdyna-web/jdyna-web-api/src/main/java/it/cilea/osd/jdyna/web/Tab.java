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
package it.cilea.osd.jdyna.web;

import it.cilea.osd.common.model.IdentifiableObject;
import it.cilea.osd.common.util.Utils;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/** Classe che gestisce un'area dell'Anagrafica 
 * @author pascarelli
 *
 */
@Entity
@Inheritance (strategy = InheritanceType.TABLE_PER_CLASS)
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public abstract class Tab extends IdentifiableObject {
	
	@Id	
	@GeneratedValue(strategy = GenerationType.TABLE)
    /**Chiave primaria di accesso*/
	private Integer id;
	
	/**Etichetta mostrata come intestazione dell'area*/
	@Column(unique=true)
	private String title;
	
	public int priority;

	
	// getter and setter 
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public boolean equals(Object object) {
		try {
			Tab area=(Tab)object;
			return Utils.equals(this.title, area.getTitle());			
		} catch (ClassCastException ex) {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		if (getTitle() != null) {
			return getTitle().hashCode();
		} else {
			return 0;
		}
	}
	
	/**
	 * Restituisce la lista delle tipologie di proprietà da <b>non</b> mostrare nell'area
	 * 
	 * @author bollini
	 * @return lista di tipologie di proprietà non ammesse nell'area
	 */
	@Transient
	public abstract List<IPropertyHolder> getMaschera();
	
	@Transient
	public abstract void setMaschera(List<IPropertyHolder> mascherate);
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priorita) {
		this.priority = priorita;
	}
	
	
}