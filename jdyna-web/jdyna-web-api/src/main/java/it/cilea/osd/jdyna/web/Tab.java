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
package it.cilea.osd.jdyna.web;

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
import org.springframework.web.multipart.MultipartFile;

import it.cilea.osd.common.model.IdentifiableObject;
import it.cilea.osd.common.util.Utils;
import it.cilea.osd.jdyna.model.AuthorizationContext;

/** Classe che gestisce un'area dell'Anagrafica 
 * @author pascarelli
 *
 */
@Entity
@Inheritance (strategy = InheritanceType.TABLE_PER_CLASS)
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public abstract class Tab<H extends IPropertyHolder> extends IdentifiableObject implements Comparable<Tab<H>>, AuthorizationContext {
	
	@Id	
	//@GeneratedValue(strategy = GenerationType.TABLE)		
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JDYNA_TAB_SEQ")
    @SequenceGenerator(name = "JDYNA_TAB_SEQ", sequenceName = "JDYNA_TAB_SEQ", allocationSize = 1)
    /** Id primary key */
	private Integer id;
	
	/** tab shortname */
	@Column(unique=true)
	private String shortName;
	
	/** tab label */
	private String title;
	
	/** Priority level */
	public int priority;
	
	/**
	 * Mandatory level 
	 */
	public boolean mandatory;
		
	/**
	 * Level of visibility 
	 */
	public Integer visibility;
	
	private String ext;
	
	private String mime;
	
	/**
	 * Icon tab
	 */
	@Transient
	private MultipartFile iconFile;
	
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
	 * Get the list of box included in this tab
	 * 
	 * @return the list of box included in this tab
	 */
	@Transient
	@Sort(type=SortType.UNSORTED)
	public abstract List<H> getMask();
	
	@Transient
	public abstract void setMask(List<H> mascherate);
	
	@Transient
	public abstract String getFileSystemPath();
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priorita) {
		this.priority = priorita;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
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
	
	/**
	 * Order by priority, if priority is the same then get an alphabetical order by <code>shortName</code> 
	 * 
	 * @return
	 * 
	 */	
	public int compareTo(Tab secondTip) {		
		if (secondTip == null) return -1;
		if (priority < secondTip.priority) return -1;
		else if (priority > secondTip.priority) return 1;
			 else return shortName.compareTo(secondTip.getShortName());
	}
	
	public MultipartFile getIconFile() {
		return iconFile;
	}
	public void setIconFile(MultipartFile iconFile) {
		this.iconFile = iconFile;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getExt() {
		return ext;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}

	public String getMime() {
		return mime;
	}
}
