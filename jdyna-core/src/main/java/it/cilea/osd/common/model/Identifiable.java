package it.cilea.osd.common.model;

import java.io.Serializable;

/**
 * This class came from Ted Bergeron 
 * see: http://www.triview.com/articles/hibernate/validator/canmeetyourneeds.html
 * 
 * An object that can be identified by its ID.
 * 
 * @author Ted Bergeron
 * @version $Id: Identifiable.java,v 1.2 2008-11-18 17:26:23 bollinicvs Exp $
 */
public interface Identifiable extends Serializable {
	Integer getId();
}
