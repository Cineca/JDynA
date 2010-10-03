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
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

@Entity
@Inheritance (strategy = InheritanceType.TABLE_PER_CLASS)
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public abstract class Box<C extends Containable> implements IPropertyHolder<C> {

	@Id
	//@GeneratedValue(strategy = GenerationType.TABLE)		
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOX_SEQ")
    @SequenceGenerator(name = "BOX_SEQ", sequenceName = "BOX_SEQ")
	/** Primary key */
	private Integer id;

	/** Tab shortname */
	@Column(unique=true)
	private String shortName;

	/** Priority level */
	public int priority;
	
	/** Tab label */
	private String title;

	/**
	 * Level of visibility  
	 */
	public Integer visibility;
	
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
	 * Get field to show on the box.
	 *
	 * @return containables list to show
	 */
	@Transient
	@Sort(type=SortType.UNSORTED)
	public abstract List<C> getMask();
	
	@Transient
	public abstract void setMask(List<C> mask);

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getShortName() {
		return shortName;
	}

	public Integer getVisibility() {
		return visibility;
	}
	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priorita) {
		this.priority = priorita;
	}
}
