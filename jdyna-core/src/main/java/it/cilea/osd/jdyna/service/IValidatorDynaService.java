package it.cilea.osd.jdyna.service;

import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.service.ValidatorService.ValidationResult;

public interface IValidatorDynaService {
	

	/** validazione shortname delle tipologia proprieta */
	public ValidationResult controllaShortName(Class clazz,PropertiesDefinition metadato);

}
