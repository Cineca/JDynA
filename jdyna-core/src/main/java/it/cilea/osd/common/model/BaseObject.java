package it.cilea.osd.common.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class came from Ted Bergeron 
 * see: http://www.triview.com/articles/hibernate/validator/canmeetyourneeds.html
 * 
 * Base class for model objects.
 *
 * @author Ted Bergeron
 * @version $Id: BaseObject.java,v 1.2 2008-11-18 17:26:23 bollinicvs Exp $
 */
public abstract class BaseObject implements Serializable {

    /**
     * Used in hashCode methods
     */
    protected static final int MAGICNUM1 = -48647637, MAGICNUM2 = 1859709343;

    /**
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof BaseObject)) {
            return false;
        }

        return new EqualsBuilder()
                .appendSuper(super.equals(object))
                        //.append(id, obj.getId()) Use this in subclasses that have properties
                .isEquals();
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(MAGICNUM1, MAGICNUM2)
                .appendSuper(super.hashCode())
                        // .append(id) Use this in subclasses that have properties
                .toHashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                        //.append(id) Use this in subclasses that have properties
                .toString();
    }


}
