package it.cilea.osd.common.model;

/**
 * This class came from Ted Bergeron 
 * see: http://www.triview.com/articles/hibernate/validator/canmeetyourneeds.html
 * 
 * 
 * This interface defines an object that can be selected
 * such as with a drop down list.
 *
 * @author Ted Bergeron
 * @version $Id: Selectable.java,v 1.2 2008-11-18 17:26:23 bollinicvs Exp $
 */
public interface Selectable {
    String getIdentifyingValue();

    String getDisplayValue();
}
