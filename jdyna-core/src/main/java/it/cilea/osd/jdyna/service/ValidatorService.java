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
package it.cilea.osd.jdyna.service;

import it.cilea.osd.jdyna.model.ATypeNestedObject;
import it.cilea.osd.jdyna.model.IPropertiesDefinition;
import it.cilea.osd.jdyna.model.PropertiesDefinition;

/** Classe di utilita' per la validazione delle singole proprieta', 
 *  offre anche altri metodi per validazioni specifiche 
 *  
 *  @author pascarelli
 *  */
public class ValidatorService implements IValidatorDynaService {

	private IPersistenceDynaService applicationService;	
		
	
	public ValidatorService(IPersistenceDynaService applicationService) {
		this.applicationService = applicationService;
	}

	
	//	utilizzato per inoltrare dei messaggi alla view 
	public class ValidationResult {
		private String tipoMessage;
		private String message;
		private boolean success;
		
		public ValidationResult() {
			getSimpleSuccessValidationResult(this);
		}
		
		public ValidationResult(String message, boolean success) {
			this.message = message;
			this.success = success;
		}
		public ValidationResult(String message, boolean success, String tipoMessage) {
			this.message = message;
			this.success = success;
			this.tipoMessage = tipoMessage;
		}
		
		public void getSimpleSuccessValidationResult(ValidationResult validationResult) {
			validationResult.setSuccess(true);
			validationResult.setMessage("");
			validationResult.setTipoMessage("Ok");
		}
		
		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public boolean hasMessage()
		{
			return message == null || message.length() == 0;
		}

		public String getTipoMessage() {
			return tipoMessage;
		}

		public void setTipoMessage(String tipoMessage) {
			this.tipoMessage = tipoMessage;
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public ValidationResult checkShortName(Class clazz,PropertiesDefinition metadato) {
		if(metadato.getId()==null || !applicationService.exist(metadato.getClass(), metadato.getId())) {
			if(applicationService.findPropertiesDefinitionByShortName(clazz, metadato.getShortName())!=null)
				return new ValidationResult("error.message.validation.shortname",false,"Error");
		}
		return new ValidationResult();
	}

	/**
     * {@inheritDoc}
     */
    public ValidationResult checkShortName(Class clazz,ATypeNestedObject metadato) {
        if(metadato.getId()==null || !applicationService.exist(metadato.getClass(), metadato.getId())) {
            if(applicationService.findTipologiaByNome(clazz, metadato.getShortName())!=null)
                return new ValidationResult("error.message.validation.shortname",false,"Error");
        }
        return new ValidationResult();
    }


	public IPersistenceDynaService getApplicationService() {
		return applicationService;
	}

}
