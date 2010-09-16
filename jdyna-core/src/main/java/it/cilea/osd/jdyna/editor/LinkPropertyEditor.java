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
package it.cilea.osd.jdyna.editor;

import it.cilea.osd.common.service.IPersistenceService;
import it.cilea.osd.jdyna.value.EmbeddedLinkValue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LinkPropertyEditor extends java.beans.PropertyEditorSupport {
	/** My gateway to business logic */
	private IPersistenceService applicationService;
	
	
	
	/** The logger */
	private final static Log log = LogFactory.getLog(LinkPropertyEditor.class);
	
	public LinkPropertyEditor() {
	
	}
	
	public LinkPropertyEditor(IPersistenceService applicationService) {
		this.applicationService = applicationService;
	}
	
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		log.debug("chiamato SoggettoConverter - setAsText"+text);
		//la stringa che arriva è del tipo 'IDSOGGETTARIO:VOCESOGGETTO'
		//devo parserizzare
		int index = text.indexOf("|||");
		String linkvalue = "";
		String linkdescription = "";
		EmbeddedLinkValue link = new EmbeddedLinkValue();
		if(text==null || text.length()==0) {		
			setValue(null);
		}
		else {			
			linkvalue = text.substring(0, index);
			link.setValueLink(linkvalue);
			if (text.substring(index + 1) == null
					|| text.substring(index + 1).length() == 0) {
				setValue(null);
			} else {
				linkdescription = text.substring(index + 3);
				link.setDescriptionLink(linkdescription);				
			}
			setValue(link);
		}
	}

	@Override
	public String getAsText() {
		log.debug("send call to LinkConverter - getAsText");
		EmbeddedLinkValue valore = (EmbeddedLinkValue) getValue();
		if (valore == null) return "";
		log.debug("value = "+valore.getValueLink());
		log.debug("description = "+valore.getDescriptionLink());
		return valore==null?"":valore.getValueLink()
			+"|||"+valore.getDescriptionLink();
	}

	public IPersistenceService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(IPersistenceService applicationService) {
		this.applicationService = applicationService;
	}

}
