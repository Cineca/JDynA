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

import it.cilea.osd.jdyna.model.Soggettario;
import it.cilea.osd.jdyna.model.Soggetto;
import it.cilea.osd.jdyna.service.IPersistenceSubjectService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SoggettoPropertyEditor extends java.beans.PropertyEditorSupport {
	/** My gateway to business logic */
	private IPersistenceSubjectService applicationService;
	
	
	/** The logger */
	private final static Log log = LogFactory.getLog(SoggettoPropertyEditor.class);
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		log.debug("chiamato SoggettoConverter - setAsText"+text);
		//la stringa che arriva è del tipo 'IDSOGGETTARIO:VOCESOGGETTO'
		//devo parserizzare
		int index = text.indexOf(":");
		String sogIDstr = "";
		String voceSoggetto = "";
		Soggettario soggettario = null;
		if(text==null || text.length()==0) {		
			setValue(null);
		}
		else {			
			sogIDstr = text.substring(0, index);
			Integer sogID = Integer.parseInt(sogIDstr);
			if (text.substring(index + 1) == null
					|| text.substring(index + 1).length() == 0) {
				setValue(null);
			} else {
				voceSoggetto = text.substring(index + 1);
				soggettario = (Soggettario) applicationService.get(
						Soggettario.class, sogID);
				Soggetto soggetto = applicationService.findSoggetto(sogID,
						voceSoggetto);
				if (soggetto != null)
					setValue(soggetto);
				else {
					soggetto = soggettario.createSoggetto();
					soggetto.setVoce(voceSoggetto);
					setValue(soggetto);
				}
			}
		}
	}

	@Override
	public String getAsText() {
		log.debug("chiamato SoggettoConverter - getAsText");
		Soggetto valore = (Soggetto) getValue();
		if (valore == null || valore.getVoce().length()==0) return "";
		log.debug("valore = "+valore.getVoce());
		return valore==null?"":valore.getSoggettario().getId()
			+":"+valore.getVoce();
	}

	public IPersistenceSubjectService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(IPersistenceSubjectService applicationService) {
		this.applicationService = applicationService;
	}

}
