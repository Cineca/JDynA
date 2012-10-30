package it.cilea.osd.jdyna.service;

import it.cilea.osd.jdyna.model.ATypeNestedObject;
import it.cilea.osd.jdyna.model.IPropertiesDefinition;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.service.ValidatorService.ValidationResult;

public interface IValidatorDynaService {
	


	/**
	 * Properties definition shortname validation
	 *  
	 * @param clazz
	 * @param metadato
	 * @return
	 */
	public ValidationResult checkShortName(Class clazz,PropertiesDefinition metadato);
	public ValidationResult checkShortName(Class clazz,ATypeNestedObject metadato);

}
