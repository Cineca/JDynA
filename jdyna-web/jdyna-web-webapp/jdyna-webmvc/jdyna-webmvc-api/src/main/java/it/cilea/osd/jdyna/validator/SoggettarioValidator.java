package it.cilea.osd.jdyna.validator;

import it.cilea.osd.jdyna.model.Soggettario;
import it.cilea.osd.jdyna.service.IValidatorClassificationService;
import it.cilea.osd.jdyna.service.IValidatorSubjectService;
import it.cilea.osd.jdyna.service.ValidatorService.ValidationResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class SoggettarioValidator extends JDynaBaseValidator {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private IValidatorSubjectService validatorService;
	
	public SoggettarioValidator(IValidatorSubjectService validatorService) {
		super(validatorService);	
		this.validatorService = validatorService;
	}
	
	public boolean supports(Class clazz) {
		return Soggettario.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object object, Errors errors) {			
		log.debug("SoggettoValidator.java-----validate");		
		Soggettario soggetto = (Soggettario) object;						
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome",
		"error.message.fallita.campo.obbligatorio");	
		validateVoce(soggetto,errors);
	}
				 

	
	/** Verifica se la voce e' stata inserita ed e' unica sul soggettario.
	 */
	public void validateVoce(Soggettario soggettario,Errors errors)  {
			ValidationResult result = validatorService.controllaNomeSuSoggettario(soggettario, soggettario.getNome());
			if (!result.isSuccess()) {					
				errors.rejectValue("nome",result.getMessage());
			}			
			
	}

}
