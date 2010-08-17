package it.cilea.osd.jdyna.web;

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

@Entity
@Inheritance (strategy = InheritanceType.TABLE_PER_CLASS)
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public abstract class Box implements IPropertyHolder {

	@Id	
	@GeneratedValue(strategy = GenerationType.TABLE)
    /**Chiave primaria di accesso*/
	private Integer id;
	
	/**Etichetta mostrata come intestazione dell'area*/
	@Column(unique=true)
	private String title;
	
	public int priority;

	
// accessori e setter 
	
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
			Box area=(Box)object;
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
	public abstract List<IContainable> getMaschera();
	
	@Transient
	public abstract void setMaschera(List<IContainable> mascherate);
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priorita) {
		this.priority = priorita;
	}

}
