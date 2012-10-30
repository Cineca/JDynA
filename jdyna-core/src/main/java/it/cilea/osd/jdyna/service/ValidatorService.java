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