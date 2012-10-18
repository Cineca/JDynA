/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 * 
 * http://www.dspace.org/license/
 * 
 * The document has moved 
 * <a href="https://svn.duraspace.org/dspace/licenses/LICENSE_HEADER">here</a>
 */
package it.cilea.osd.jdyna.dao;

import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.web.AbstractEditTab;
import it.cilea.osd.jdyna.web.AbstractTab;
import it.cilea.osd.jdyna.web.Box;
import it.cilea.osd.jdyna.web.IPropertyHolder;



public interface EditTabDao<H extends IPropertyHolder<Containable>, D extends AbstractTab<H>, T extends AbstractEditTab<H,D>> extends TabDao<H,T> {

	public T uniqueByDisplayTab(int tabId);
	
}
