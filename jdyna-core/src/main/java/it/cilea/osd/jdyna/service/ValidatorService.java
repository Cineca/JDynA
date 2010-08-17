package it.cilea.osd.jdyna.service;

import it.cilea.osd.jdyna.model.PropertiesDefinition;

/** Classe di utilità per la validazione delle singole proprietà, 
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


	/** validazione shortname delle tipologia proprieta */
	public ValidationResult controllaShortName(Class clazz,PropertiesDefinition metadato) {
		if(metadato.getId()==null || !applicationService.exist(metadato.getClass(), metadato.getId())) {
			if(applicationService.findTipologiaProprietaByShortName(clazz, metadato.getShortName())!=null)
				return new ValidationResult("error.message.fallita.validazione.shortname",false,"Error");
		}
		return new ValidationResult();
	}

}