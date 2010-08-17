package it.cilea.osd.jdyna.validator;

import it.cilea.osd.jdyna.dto.DTOSoggetto;
import it.cilea.osd.jdyna.model.Soggettario;
import it.cilea.osd.jdyna.service.IValidatorClassificationService;
import it.cilea.osd.jdyna.service.IValidatorSubjectService;
import it.cilea.osd.jdyna.service.ValidatorService.ValidationResult;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@RemoteProxy(creator = SpringCreator.class, creatorParams = @Param(name = "beanName", value = "dtoSoggettoValidator"))
public class DTOSoggettoValidator extends JDynaBaseValidator {


	private IValidatorSubjectService validatorService;
	
	protected final Log log = LogFactory.getLog(getClass());
		
    private List<String> messages;

	public DTOSoggettoValidator(IValidatorSubjectService validatorService) {
		super(validatorService);	
		this.validatorService = validatorService;
	}
	
	public boolean supports(Class clazz) {
		return DTOSoggetto.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object object, Errors errors) {			
		log.debug("SoggettoValidator.java-----validate");		
		DTOSoggetto soggetto = (DTOSoggetto) object;						
		validateVoce(soggetto.getSoggettario(),soggetto.getVoce(),errors);
	}
				 

	
	/** 
	 * Verify the uniqueness of the subject.
	 */
	public void validateVoce(Soggettario soggettario,String voce,Errors errors)  {
				
		if(voce != null && soggettario != null) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "voce",
			"error.message.fallita.campo.obbligatorio");			
			ValidationResult result = validatorService.controllaVoceSuSoggettario(soggettario, voce);
			if (!result.isSuccess()) {					
				errors.rejectValue("voce",result.getMessage());
			}			
			
		}
	}
    
	public List<String> getMessages() {
		if (messages == null) return new ArrayList<String>();
        return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
}
