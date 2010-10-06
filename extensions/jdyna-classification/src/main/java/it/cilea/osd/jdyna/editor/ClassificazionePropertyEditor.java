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

import it.cilea.osd.jdyna.model.Classificazione;
import it.cilea.osd.jdyna.service.IPersistenceClassificationService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClassificazionePropertyEditor extends java.beans.PropertyEditorSupport {
	/** My gateway to business logic */
	private IPersistenceClassificationService applicationService;
	

	/** The logger */
	private final static Log log = LogFactory.getLog(ClassificazionePropertyEditor.class);

	@Override
	public void setAsText(String text) throws IllegalArgumentException,IndexOutOfBoundsException {
		
		 // dentro TEXT non arriva l'ID della classificazione,questo 
		 // perche' nella classe Classificazione il getIdentifyingValue restituisce
		 // il CODICE...e' stato aggiunto anche l'id dell'albero classificatorio in modo
		// tale da fare la query
		
		//parserizzo la stringa text
		String resultAlbero = "";
		String resultCodice = "";
		int index = text.lastIndexOf(":");
		//if(index==-1) {
		if(text.trim().equals("")) {
			setValue(null);
		}
		else {
			 resultCodice = text.substring(index+1);
			 resultAlbero = text.substring(0, index);
			Classificazione classificazione = 
					applicationService.getClassificazioneByCodice(resultAlbero,resultCodice);
			setValue(classificazione);
		}
	}

	@Override
	public String getAsText() {
		log.debug("chiamato ClassificazioneConverter - getAsText");
		Classificazione valore = (Classificazione) getValue();		
		return valore==null || valore.getAlberoClassificatorio() == null?"":valore.getAlberoClassificatorio().getNome()+":"+valore.getCodice();
	}

	public IPersistenceClassificationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(IPersistenceClassificationService applicationService) {
		this.applicationService = applicationService;
	}

}
