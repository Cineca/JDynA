package it.cilea.osd.jdyna.web;

import it.cilea.osd.common.util.Utils;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="model_dyna_box")
public class Box implements IPropertyHolder {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	/**Chiave primaria di accesso*/
	private Integer id;

	/** Etichetta mostrata come intestazione dell'area */
	@Column(unique = true)
	private String title;

	/** Le proprietà da nascondere in questa area */
	@ManyToMany
	@JoinTable(name = "model_box_dyna_box2containable")
	private List<IContainable> mask;

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
			Box area = (Box) object;
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
	 * Restituisce la lista delle tipologie di proprietà da <b>non</b> mostrare
	 * nell'area
	 * 
	 * @author bollini
	 * @return lista di tipologie di proprietà non ammesse nell'area
	 */

	public List<IContainable> getMask() {
		return this.mask;
	}

	public void setMaschera(List<IContainable> mask) {
		this.mask = mask;
	}

}
