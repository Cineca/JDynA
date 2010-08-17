package it.cilea.osd.common.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class came from Ted Bergeron 
 * see: http://www.triview.com/articles/hibernate/validator/canmeetyourneeds.html
 *  
 * 
 * Base class for objects that can be uniquely identified via an Integer
 * property, id.
 * 
 * @author Ted Bergeron
 * @version $Id: IdentifiableObject.java,v 1.2 2008-11-18 17:26:23 bollinicvs Exp $
 */
@MappedSuperclass
public abstract class IdentifiableObject extends BaseObject implements
		Identifiable, Serializable {

	public abstract Integer getId();
	
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof IdentifiableObject)) {
			return false;
		}

		IdentifiableObject obj = (IdentifiableObject) object;

		return new EqualsBuilder().appendSuper(super.equals(object)).append(
				getId(), obj.getId()).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(MAGICNUM1, MAGICNUM2).appendSuper(
				super.hashCode()).append(getId()).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append(
				getId()).toString();
	}

}
