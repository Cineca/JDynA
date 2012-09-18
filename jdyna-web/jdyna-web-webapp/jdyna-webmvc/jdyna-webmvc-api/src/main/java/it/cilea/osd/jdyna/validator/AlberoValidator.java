package it.cilea.osd.jdyna.validator;

import it.cilea.osd.jdyna.dto.DTOAlberoClassificatore;
import it.cilea.osd.jdyna.service.IValidatorClassificationService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.springframework.validation.Errors;

@RemoteProxy(creator = SpringCreator.class, creatorParams = @Param(name = "beanName", value = "classificazioneValidator"))
public class AlberoValidator extends JDynaBaseValidator {
	
	protected final Log log = LogFactory.getLog(getClass());
		
    private List<String> messages;

    private IValidatorClassificationService validatorService;
    
    public AlberoValidator(IValidatorClassificationService validatorService) {
		super(validatorService);
		this.validatorService = validatorService;
	}  
    
	public boolean supports(Class clazz) {
		return DTOAlberoClassificatore.class.isAssignableFrom(clazz);
	}
	
	
	public void validate(Object object, Errors errors) {			
		log.debug("AlberoValidator.java-----validate");		
		DTOAlberoClassificatore classificazione = (DTOAlberoClassificatore) object;
					
		validatorService.validaNomeAlbero(classificazione, errors);
		
	}
				 
    
	public List<String> getMessages() {
		if (messages == null) return new ArrayList<String>();
        return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	

}
